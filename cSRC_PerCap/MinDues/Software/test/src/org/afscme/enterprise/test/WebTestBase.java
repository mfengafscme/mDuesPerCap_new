package org.afscme.enterprise.test;

import com.meterware.httpunit.*;
import java.util.*;
import org.xml.sax.SAXException;
import junit.framework.TestCase;
import java.util.Enumeration;
import org.afscme.enterprise.util.TextUtil;

/**
 * Abstract Base class for HTTP testing.
 * Wraps a WebConversion and provides some helper functions to make test scripts in derived classes simple and easy to read.
 * Users should derive from TestBase and implement their test in the run() method.
 */
public abstract class WebTestBase extends TestCase
{
    protected WebConversation m_wc;
    protected WebResponse m_response;
    protected WebForm m_form;
	protected Map m_params;

	private String m_urlBase;
    
    public WebTestBase(String name) {
        super(name);
		m_params = new HashMap();
		reset();
    }

    /** returns the base url for the test.  e.g. http://localhost:8080/mywebapp */
    public String getURLBase() {
		if (m_urlBase == null)
			m_urlBase = System.getProperty("test.http.url", "http://127.0.0.1:8080");

		return m_urlBase;
	}


    /** Sets a parameter in the current request. */
    protected void setParameter(String name, String value) throws Exception {
		m_params.put(name, value);
    }

    /** Sets a parameter in the current request. */
    protected void setParameter(String name, String[] values) throws Exception {
		m_params.put(name, values);
    }
	
	/** Gets a parameter value from the form */
	protected String getParameterValue(String name) throws Exception {
		WebForm form = m_response.getForms()[0];
		return form.getParameterValue(name);
	}
		
	/** Creates a URL by appending <code>relativeURL</code> to the base URL */
    protected String makeURL(String relativeURL) {
        return getURLBase() + relativeURL;
    }
    
    /** Logs to stdout */
    protected static void log(String message) {
        System.out.print(message);
    }

    /** Logs to stdout with a newline */
    protected static void logln(String message) {
       log(message + "\n"); 
    }

    /** Finds the link that is displayed as <code>name</code> and selects it */
    protected void selectLink(String name) throws Exception {
        WebLink link = getLinkFromText(name);
        if (link == null)
            link = m_response.getLinkWith(name);
        if (link == null)
            throw new Exception("Link '" + name + "' does not exist in the form '");
		selectLink(link);
    }
	
    /** Selects the given link */
	protected void selectLink(WebLink link) throws Exception {
		logln("Selecting Link '" + link.asText() + "'");
        setResponse(m_wc.getResponse(link.getRequest()));
	}

    /**
     * Returns the first link which extactly matches the specified text.
     **/
    protected WebLink getLinkFromText( String text ) throws SAXException {
        WebLink[] links = m_response.getLinks();
        for (int i = 0; i < links.length; i++) {
            if (links[i].asText().equals(text))
                return links[i];
        }
        return null;
    }

	/**
	 * Selects the first link which matches 'linkText' (regex),
	 * and is in a table row which matches 'rowText' (regex).
	 * This allows you to find the right edit/delete links for specific rows.
	 */
	protected void selectRowLink(String linkText, String rowText) throws Exception {
        WebLink link = getLinkFromRowText(linkText, rowText);
        if (link == null)
            throw new Exception("Cannot find link '" + linkText + "' in row with '" + rowText + "'");
		selectLink(link);
	}

	/**
	 * Returns the first link which matches 'linkText' (regex),
	 * and is in a table row which matches 'rowText' (regex).
	 * This allows you to find the right edit/delete links for specific rows.
	 */
	protected WebLink getLinkFromRowText(String linkText, String rowText) throws SAXException {
		return getLinkFromRowText(getResponse(), linkText, rowText);
	}

	/**
	 * Returns the first link which matches 'linkText' (regex),
	 * and is in a table row which matches 'rowText' (regex).
	 * This allows you to find hyperlinks on specific rows.
	 */
	protected WebLink getLinkFromRowText(HTMLSegment html, String linkText, String rowText) throws SAXException {
		WebTable[] tables = html.getTables();
		for (int t = 0; t < tables.length; t++) {						//FOR EACH TABLE
			WebTable table = tables[t];
			int rowCount = table.getRowCount();
			int colCount = table.getColumnCount();
			for (int r = 0; r < rowCount; r++) {						//FOR EACH ROW
				for (int c = 0; c < colCount; c++) {					//FOR EACH COLUMN
					TableCell cell = table.getTableCell(r, c);
					if (cell.asText().matches(rowText)) {
						for (int c2 = 0; c2 < colCount; c2++) {			//FOR EACH COLUMN AGAIN (IFF WE FOUND THE TEXT)
							WebLink[] links = table.getTableCell(r, c2).getLinks();	
							for (int l = 0; l < links.length; l++) {	//FOR EACH LINK
								if (links[l].asText().matches(linkText))
									return links[l];
							}
						}
					} else {
						WebLink link = getLinkFromRowText(cell, linkText, rowText);	//RECURSE
						if (link != null)
							return link;
					}
				}
			}
		}

		return null;
	}
					
    /** Performs an HTTP GET on <code>url</code> */
    protected void get(String url) throws Exception {
        logln("Getting " + (url.equals("") ? "home page" : url));
        setResponse(m_wc.getResponse(getURLBase() + url));
    }

	/** Submits the current form */
    protected void submit() throws Exception {
		submit("");
	}
	
	/** Submits the form by pressing the submit button with the specified name */
	protected void submit(String buttonName) throws Exception {
        logln("Submitting Form with button '" + buttonName + "'");
		WebRequest request = getForm().getRequest(buttonName);
		Iterator it = m_params.keySet().iterator();
		while (it.hasNext()) {
			String key = (String)it.next();
			Object value = m_params.get(key);
			if (value instanceof String)
				request.setParameter(key, (String)value);
			else if (value instanceof String[])
				request.setParameter(key, (String[])value);
			else throw new RuntimeException("Inavlid paramter type " + value.getClass().getName() + " for parameter " + key);
		}
		log(getParamsAsString());
        setResponse(m_wc.getResponse(request));
    }

    /** Gets the first form on the page */
    protected WebForm getForm() throws Exception {
        if (m_form == null)
            m_form = m_response.getForms()[0];

        return m_form;
    }
    
    /** Resets to the initial state.  This is the equivalent of shutting down the browser and restarting. */
    protected void reset() {
        m_wc = new WebConversation();
		m_wc.getClientProperties().setUserAgent("AFSCME Test Client");
        m_response = null;
        m_form = null;
		m_params.clear();
    }

    /** Gets the current request parameters as a string */
    protected String getParamsAsString() throws Exception {
        StringBuffer buffer = new StringBuffer();
        Iterator it = m_params.keySet().iterator();
        
		while (it.hasNext()) {
			String key = (String)it.next();
            Object value = m_params.get(key);
            buffer.append("\t" + key + "='" + TextUtil.toString(value) + "'\n");
        }

        return buffer.toString();
    }
	
	protected WebResponse getResponse() {
		if (m_response == null)
			throw new RuntimeException("INTERNAL TESTING ERROR: Attempt to get HTML page when no respons object exists");

		return m_response;
	}
    
    /** Asserts that the current pages title matches the given title */
    protected void checkTitle(String title) throws SAXException {
        assertEquals(title, getResponse().getTitle());
    }
    
    private void setResponse(WebResponse response) throws Exception {
        m_response = response;
        logln("Response: '" + m_response.getResponseMessage() + "'  Title: '" + m_response.getTitle() + "'");
        assertEquals(200, m_response.getResponseCode());
        m_form = null;
		m_params.clear();
    }
}


