package org.afscme.enterprise.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.ejb.CreateException;
import javax.ejb.RemoveException;
import javax.naming.NamingException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.afscme.enterprise.common.ConfigurationData;
import org.afscme.enterprise.controller.ActionPrivileges;
import org.afscme.enterprise.reporting.base.LabelConfigurationData;
import org.afscme.enterprise.reporting.base.PDFConfigurationData;
import org.afscme.enterprise.roles.ejb.MaintainPrivileges;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;




/**
 * Handles loading of the application configuration files.
 */
public class ConfigUtil {
    
    /** The main configuration data (enterprise_config.xml) */
	protected static ConfigurationData s_config;
    
    /** The ActionPrivileges (acl.xml) */
	protected static ActionPrivileges s_actionPrivileges;
    
    /** PDF Configuration for base reporting (pdf_config.xml) */
	protected static PDFConfigurationData s_pdfConfig;
    
    /** Label format configuration (label_config.xml) */
	protected static LabelConfigurationData s_labelConfig;
    
    /** Message strings (message.properties) */
	protected static Map s_messages;

    /** Initialization flag, true if ConfigUtil has been initialized */
	private static boolean s_initialized;
    
    /** Logger for this class to user */
	private static Logger log;
	
	/**
	 * Gets the ConfigurationData.
	 */
	public static ConfigurationData getConfigurationData() {
		return s_config;
	}
	
	/**
	 * Gets the system messages.
	 */
	public static Map getMessages() {
		return s_messages;
	}
	
	
	/**
	 * Gets the PDFConfigurationData.
	 */
	public static PDFConfigurationData getPDFConfigurationData() {
		return s_pdfConfig;
	}
	
	/**
	 * Gets the LabelConfigurationData.
	 */
	public static LabelConfigurationData getLabelConfigurationData() {
		return s_labelConfig;
	}
	
	/**
	 * Gets the ActionPrivileges.
	 */
	public static ActionPrivileges getActionPrivileges() {
		return s_actionPrivileges;
	}
	
    /** Loads all the configuration files.  Called once when the class is loaded. */
	public static synchronized void init() {

            //derace the initialization flag
            if (s_initialized)
                    return;
            s_initialized = true;

            try {
            //	configLog4j("log_config.xml");
                    log = Logger.getLogger(ConfigUtil.class);

                s_config 		= loadConfigurationData		(getConfigDocument("enterprise_config.xml"));
                s_actionPrivileges 	= loadActionPrivileges		(getConfigDocument("acl.xml"));
                s_pdfConfig 		= loadPDFConfigurationData	(getConfigDocument("pdf_config.xml"));
                s_labelConfig 		= loadLabelConfigurationData	(getConfigDocument("label_config.xml"));
                s_messages 		= loadMessageFormats		(getConfigProperties("messages.properties"));

            } catch (Exception e) {
                //can't log the exception, since log may not be configured.
                e.printStackTrace(System.err);
                throw new RuntimeException(e);
            }

	}
	
	/**
	 * Loads and parses the configuration file 'name', and returns it as a map object.
	 */
	protected static Map getConfigProperties(String name) throws IOException {
		InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(name);
		Properties props = new Properties();
		props.load(is);
		Map map = new HashMap();
		map.putAll(props);
		return map;
	}
	
	/**
	 * Loads and parses the configuration file 'name', and returns it as an XML DOM Document.
	 */
	protected static Document getConfigDocument(String name) throws IOException, ParserConfigurationException, SAXException {
		InputStream is = ConfigUtil.class.getClassLoader().getResourceAsStream(name);
                
                // set validation of XML files to false
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(is);
		is.close();
		log.debug("Retrieved Configuration Document: " + name);
		return doc;
	}
	
	/**
	 * Creates a new ConfigurationData object from data in 'doc' and returns it.
	 */
	protected static ConfigurationData loadConfigurationData(Document doc) throws SAXException {
		
		ConfigurationData data = new ConfigurationData();
		
		Element config = doc.getDocumentElement();
		Element login = (Element)config.getElementsByTagName("login").item(0);
		data.setMaxLoginAttempts(Integer.parseInt(login.getAttribute("maxAttempts")));
		data.setLockoutTime(Integer.parseInt(login.getAttribute("lockoutTime")));
		Element reportQueue = (Element)config.getElementsByTagName("reportQueue").item(0);
		data.setReportFromEmail(reportQueue.getAttribute("fromEmail"));
		data.setReportFromName(reportQueue.getAttribute("fromName"));
		data.setReportQueueEmail(reportQueue.getAttribute("toEmail"));
		data.setReportQueueName(reportQueue.getAttribute("toName"));
		Element smtp = (Element)config.getElementsByTagName("smtp").item(0);
		data.setSMTPServer(smtp.getAttribute("server"));
		Element temp = (Element)config.getElementsByTagName("temp").item(0);
		data.setTempDir(temp.getAttribute("dir"));
		Element aflcioExtract = (Element)config.getElementsByTagName("aflcioExtract").item(0);
		data.setAFLCIOExtractDestination(aflcioExtract.getAttribute("destination"));
		Element requestPassword = (Element)config.getElementsByTagName("requestPassword").item(0);
		data.setRequestPasswordFromEmail(requestPassword.getAttribute("fromEmail"));
		data.setRequestPasswordFromName(requestPassword.getAttribute("fromName"));
		Element searchResults = (Element)config.getElementsByTagName("searchResults").item(0);
		data.setSearchResultPageSize(Integer.parseInt(searchResults.getAttribute("pageSize")));
		Element challengeQuestion = (Element)config.getElementsByTagName("challengeQuestion").item(0);
		data.setDefaultChallengeQuestion(new Integer(Integer.parseInt(challengeQuestion.getAttribute("defaultPk"))));
		Element upload = (Element)config.getElementsByTagName("upload").item(0);
		data.setMinUploadFileSize(Long.parseLong(upload.getAttribute("minFileSize")));
		data.setAfscmeUploadFolder(upload.getAttribute("afscmeDir"));
		data.setAffiliateUploadFolder(upload.getAttribute("affiliateDir"));
		Element yearConversion = (Element)config.getElementsByTagName("yearConversion").item(0);
		data.setYearConversionCutoff(Integer.parseInt(yearConversion.getAttribute("cutoff")));
		Element jndiInfo = (Element)config.getElementsByTagName("jndi").item(0);
		data.setJNDIClusterURL(jndiInfo.getAttribute("clusterURL"));
		Element jmsInfo = (Element)config.getElementsByTagName("jms").item(0);
		data.setJMSFactoryName(jmsInfo.getAttribute("factoryName"));
		data.setJMSReportQueueName(jmsInfo.getAttribute("reportQueue"));
		data.setJMSSystemLogQueueName(jmsInfo.getAttribute("systemLogQueue"));
		data.setJMSUpdateQueueName(jmsInfo.getAttribute("updateQueue"));
                
		if (log.isDebugEnabled())
			log.debug(data.toString());
		
		return data;
	}
	
	/**
	 * Creates a new ActionPrivileges object from data in 'doc' and returns it.
	 */
	protected static ActionPrivileges loadActionPrivileges(Document doc) throws SAXException, NamingException, CreateException, RemoveException, ParserConfigurationException, IOException  {
		
        StringBuffer debug = new StringBuffer();
		Map actionPrivilegesMap = new HashMap();
		Element config = doc.getDocumentElement();
		NodeList privilegeList = config.getElementsByTagName("privilege");
        
        //diagnostics
        Set allActions = new HashSet();
        Set allPrivileges = new HashSet();
		///
        
        //FOR EACH privilege

		for (int i = 0; i < privilegeList.getLength(); i++) {

            //get the actions
            Element privilegeElement = (Element)privilegeList.item(i);
			String privilegeName = privilegeElement.getAttribute("key");
			NodeList actionList = privilegeElement.getElementsByTagName("action");

            //diagnostics
            if (allPrivileges.contains(privilegeName))
                log.warn("Privilege " + privilegeName + " multiply defined in ACL file ");
            else
                allPrivileges.add(privilegeName);
            //

            //FOR EACH action
            
            for (int j = 0; j < actionList.getLength(); j++) {

                Element actionElement = (Element)actionList.item(j);
                String actionName = actionElement.getAttribute("name");
                Set privilegeSet = (Set)actionPrivilegesMap.get(actionName);
                
                if (privilegeSet == null) {
                    privilegeSet = new HashSet();
                    
                    //add the action to the privilege
                    actionPrivilegesMap.put(actionName, privilegeSet);  
                }

                //add the privilege to the map of privileges
                privilegeSet.add(privilegeName);
              
                //diagnostics
                allActions.add(actionName);
                //
            }
		}
        
        //diagnostics
        checkActions(allActions);
        //
        
		return new ActionPrivileges(actionPrivilegesMap);
	}
	
	
	/**
	 * Creates a new PDFConfigurationData object from data in 'doc' and returns it.
	 */
	protected static PDFConfigurationData loadPDFConfigurationData(Document doc) throws SAXException {
		
		PDFConfigurationData data = new PDFConfigurationData();
		
		//config
		Element config = doc.getDocumentElement();
		
		//config/page
		Element page = (Element)config.getElementsByTagName("page").item(0);
		
		//config/page/size
		Element size = (Element)page.getElementsByTagName("size").item(0);
		data.setPageWidth(size.getAttribute("width"));
		data.setPageHeight(size.getAttribute("height"));
		
		//config/page/margin
		Element margin = (Element)page.getElementsByTagName("margin").item(0);
		data.setLeftMargin(margin.getAttribute("left"));
		data.setRightMargin(margin.getAttribute("right"));
		data.setBottomMargin(margin.getAttribute("bottom"));
		data.setTopMargin(margin.getAttribute("top"));
		
		//config/header
		Element header = (Element)config.getElementsByTagName("header").item(0);
		data.setBeforeRegion(header.getAttribute("topSize"));
		data.setAfterRegion(header.getAttribute("bottomSize"));
		
		//config/header/font
		Element font = (Element)header.getElementsByTagName("font").item(0);
		data.setPageHeaderFooterFont(font.getAttribute("family"));
		data.setPageHeaderFooterFontSize(font.getAttribute("size"));
		
		// config/titlePage
		Element titlePage = (Element)config.getElementsByTagName("titlePage").item(0);
		
		//config/titlePage/title
		Element title = (Element)config.getElementsByTagName("title").item(0);
		
		//config/titlePage/title/font
		font = (Element)title.getElementsByTagName("font").item(0);
		data.setTitleFont(font.getAttribute("family"));
		data.setTitleFontSize(font.getAttribute("size"));
		
		//config/titlePage/content
		Element titleContent = (Element)config.getElementsByTagName("titleContent").item(0);
		
		//config/titlePage/description/font
		font = (Element)titleContent.getElementsByTagName("font").item(0);
		data.setTitleContentFont(font.getAttribute("family"));
		data.setTitleContentFontSize(font.getAttribute("size"));
		
		//config/columnHeader
		Element columnHeader = (Element)config.getElementsByTagName("columnHeader").item(0);
		
		//config/columnHeader/font
		font = (Element)columnHeader.getElementsByTagName("font").item(0);
		data.setTableColumnHeaderFont(font.getAttribute("family"));
		data.setTableColumnHeaderFontSize(font.getAttribute("size"));
		
		//config/columnHeader/paddding
		Element padding = (Element)columnHeader.getElementsByTagName("padding").item(0);
		data.setColumnPadding(padding.getAttribute("size"));
		
		//config/tableContent
		Element tableContent = (Element)config.getElementsByTagName("tableContent").item(0);
		
		//config/tableContent/font
		font = (Element)tableContent.getElementsByTagName("font").item(0);
		data.setTableContentFont(font.getAttribute("family"));
		data.setTableContentFontSize(font.getAttribute("size"));
		
		//config/output
		Element output = (Element)config.getElementsByTagName("output").item(0);
		data.setHyphenationOn(output.getAttribute("hyphenation").equalsIgnoreCase("on"));
		data.setMaxRowsInSequence(Integer.parseInt(output.getAttribute("maxRowsPerSequence")));
		
		return data;
	}
	
	/**
	 * Creates a new LabelConfigurationData object from data in 'doc' and returns it.
	 */
	protected static LabelConfigurationData loadLabelConfigurationData(Document doc) throws SAXException {
		LabelConfigurationData data = new LabelConfigurationData();
		
		//config
		Element config = doc.getDocumentElement();
		
		//config/page
		Element page = (Element)config.getElementsByTagName("page").item(0);
		data.setLabelsPerLine(Integer.parseInt(page.getAttribute("labelsPerLine")));
		data.setLinesPerPage(Integer.parseInt(page.getAttribute("linesPerPage")));
		
		//config/label
		Element label = (Element)config.getElementsByTagName("label").item(0);
		data.setLabelWidth(Integer.parseInt(label.getAttribute("labelWidth")));
		data.setCityWidth(Integer.parseInt(label.getAttribute("cityWidth")));
		
		return data;
	}

    /** Loads the message resources file */
	private static Map loadMessageFormats(Map messages) {
		Iterator it = messages.keySet().iterator();
		Map result = new HashMap();
		while (it.hasNext()) {
			String key = (String)it.next();
			MessageFormat mf = new MessageFormat((String)messages.get(key));
			result.put(key, mf);
		}
		return result;
	}

    //diagnostics
    private static void checkActions(Set actionsInACLFile) throws IOException, ParserConfigurationException, SAXException {

        Set strutsActions = getStrutsActions();
        Iterator it = strutsActions.iterator();
        while (it.hasNext()) {
            String actionName = (String)it.next();
            if (!actionsInACLFile.contains(actionName))
                log.warn("Struts Action '" + actionName + "' has no associated privilege in the ACL file");
        }
        
        it = actionsInACLFile.iterator();
        while (it.hasNext()) {
            String actionName = (String)it.next();
            if (!strutsActions.contains(actionName))
                log.warn("Action '" + actionName + "' in ACL file, but there is no such Struts action");
        }
    }

    //diagnostics
    private static Set getStrutsActions() throws IOException, ParserConfigurationException, SAXException {

        Set result = new HashSet();
        Document doc = getConfigDocument("struts-config.xml");
		
		//struts-config
		Element config = doc.getDocumentElement();
		
		//action-mappings
		Element action_mappings = (Element)config.getElementsByTagName("action-mappings").item(0);
		
        NodeList actions = action_mappings.getElementsByTagName("action");
        for (int i = 0; i < actions.getLength(); i++) {
            Element action = (Element)actions.item(i);
            String actionName = action.getAttribute("path").substring(1);
            result.add(actionName);
        }
        
        return result;
    }
        
        
        

}
