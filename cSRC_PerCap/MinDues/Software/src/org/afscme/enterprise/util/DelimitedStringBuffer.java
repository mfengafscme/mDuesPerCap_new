package org.afscme.enterprise.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

/**
 * Helps build delimited strings.  Clients speicify a delimiter and then repeatedly call append(str).  If 'str' is null
 * it is not appended, and no delimiter is added.  This behaviour is appropriate for building proper names, addresses etc.. but
 * not for building delimited files.
 */
public class DelimitedStringBuffer {
	
	private String m_delimiter;
	private PrintWriter m_writer;
	private ByteArrayOutputStream m_baos;

    private boolean isEmpty() {
        return m_baos == null;
    }
    
    private void init() {
        m_baos = new ByteArrayOutputStream();
        m_writer = new PrintWriter(m_baos);
    }
        
	public DelimitedStringBuffer() {
	}

    public DelimitedStringBuffer(String delimiter) {
		m_delimiter = delimiter;
	}

    /**
    * Appends 'str' if it is not null.
    **/
	public DelimitedStringBuffer append(String str) {
        return append(str, m_delimiter);
    }

    /**
    * Appends 'str' if it is not null.
    **/
	public DelimitedStringBuffer append(String str, String delim) {
		if (str != null) {
			if (!isEmpty()) {
                if (delim != null) {
    				m_writer.write(delim);
                }
            }
            else {
                init();
            }
			m_writer.write(str);
		}
		return this;
	}

	/**
	 * Appends 'ch'
	 **/
	public DelimitedStringBuffer append(char ch) {
        return append(ch, m_delimiter);
    }

    /**
	 * Appends 'ch'
	 **/
	public DelimitedStringBuffer append(char ch, String delim) {
        if (!isEmpty()) {
            if (delim != null) {
                m_writer.write(delim);
            }
        }
        else {
            init();
        }
        
        m_writer.write(ch);
		return this;
	}
    
    /* prepends 'str' if it is not null */
    public DelimitedStringBuffer prepend(String str) {
        if (str != null) {
			if (isEmpty()) {
				append(str);
			} else {
				String current = toString();
				init();
				m_writer.write(str);
				append(current);
			}
        }
        return this;
    }
    
    
    /** Changes the delimiter to the one provided */
    public void setDelimiter(String delimiter) {
        m_delimiter = delimiter;
    }

    /**
	 * Returns the delimited string
	 **/
	public String toString() {
        if (m_writer == null)
            return "";
        else {
            m_writer.flush();
    		return m_baos.toString();
        }
	}
}
