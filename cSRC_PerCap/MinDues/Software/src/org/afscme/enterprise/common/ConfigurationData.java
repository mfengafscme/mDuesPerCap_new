package org.afscme.enterprise.common;

import org.afscme.enterprise.reporting.base.PDFConfigurationData;
import org.afscme.enterprise.controller.ActionPrivileges;
import java.io.Serializable;




/**
 * Holds all the configuration parameters for the appplication needed by the 
 * application code.
 */
public class ConfigurationData implements Serializable
{
	
	/**
	 * The maximum number of consecutive bad login attempts a user may make before 
	 * being locked out.
	 */
	protected int maxLoginAttempts;
	
	/**
	 * The number of minutes a user account is locked out after exceeding the maximum 
	 * bad login attempts.
	 */
	protected int lockoutTime;
	
	/**
	 * The ActionPrivileges object.
	 */
	protected ActionPrivileges actionPrivileges;
	
	/**
	 * The host name of the FTP server the AFLCIO Extract is sent to.
	 */
	protected String AFLCIOExtractDestination;
	
	/**
	 * Host name of the SMTP server to send email through.
	 */
	protected String SMTPServer;
	
	/**
	 * The default fromEmail for email message sent by the system.
	 */
	protected String reportFromEmail;
	
	/**
	 * Display name for the 'from' field in report queue emails.
	 */
	protected String reportFromName;
	
	/**
	 * Email addres to send generates reports and mailing lists to.
	 */
	protected String reportQueueEmail;
	
	/**
	 * Display name for the 'to' field in report queue emails.
	 */
	protected String reportQueueName;

	/**
	 * The application-wide temporary directory.
	 */
	protected String tempDir;
    
	/** Email address that password requests will be sent from. e.g. 'secadmin@afscme.org' */
	protected String requestPasswordFromEmail;

	/** Email Name that password requests will be sent from. e.g. 'AFSCME Enterprise Security Admin' */
	protected String requestPasswordFromName;
	
	/** Maximum size of a page of search results in the UI */
	protected int searchResultPageSize;
	
	/** Primary key of default challenge question */
	protected Integer defaultChallengeQuestion;
        
    /** Minimum file size for an Uploaded File. */
    protected long minUploadFileSize;

    /** 2 digit value used to determine when to add year to 1900 or 2000. */
    protected int yearConversionCutoff;

    /** Name of the JMS Factory for the application server. */
    protected String JMSFactoryName;

    /** Name of the JMS Report Queue. */
    protected String JMSReportQueueName;

    /** Name of the JMS System Log Queue. */
    protected String JMSSystemLogQueueName;

    /** Name of the JMS Update Queue. */
    protected String JMSUpdateQueueName;

    /** Name of the folder containing AFSCME upload files. */
    protected String afscmeUploadFolder;

    /** Name of the folder containing Affiliate upload files. */
    protected String affiliateUploadFolder;

    /** URL of the cluster-wide JNDI context server (HA-JNDI) */
    protected String JNDIClusterURL;

    /** Getter for property AFLCIOExtractDestination.
     * @return Value of property AFLCIOExtractDestination.
     */
    public String getAFLCIOExtractDestination() {
        return AFLCIOExtractDestination;
    }
    
    /** Setter for property AFLCIOExtractDestination.
     * @param AFLCIOExtractDestination New value of property AFLCIOExtractDestination.
     */
    public void setAFLCIOExtractDestination(String AFLCIOExtractDestination) {
        this.AFLCIOExtractDestination = AFLCIOExtractDestination;
    }
    
    /** Getter for property lockoutTime.
     * @return Value of property lockoutTime.
     */
    public int getLockoutTime() {
        return lockoutTime;
    }
    
    /** Setter for property lockoutTime.
     * @param lockoutTime New value of property lockoutTime.
     */
    public void setLockoutTime(int lockoutTime) {
        this.lockoutTime = lockoutTime;
    }
    
    /** Getter for property maxLoginAttempts.
     * @return Value of property maxLoginAttempts.
     */
    public int getMaxLoginAttempts() {
        return maxLoginAttempts;
    }
    
    /** Setter for property maxLoginAttempts.
     * @param maxLoginAttempts New value of property maxLoginAttempts.
     */
    public void setMaxLoginAttempts(int maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }
    
    /** Getter for property reportFromEmail.
     * @return Value of property reportFromEmail.
     */
    public String getReportFromEmail() {
        return reportFromEmail;
    }
    
    /** Setter for property reportFromEmail.
     * @param reportFromEmail New value of property reportFromEmail.
     */
    public void setReportFromEmail(String reportFromEmail) {
        this.reportFromEmail = reportFromEmail;
    }
    
    /** Getter for property reportFromName.
     * @return Value of property reportFromName.
     */
    public String getReportFromName() {
        return reportFromName;
    }
    
    /** Setter for property reportFromName.
     * @param reportFromName New value of property reportFromName.
     */
    public void setReportFromName(String reportFromName) {
        this.reportFromName = reportFromName;
    }
    
    /** Getter for property reportQueueEmail.
     * @return Value of property reportQueueEmail.
     */
    public String getReportQueueEmail() {
        return reportQueueEmail;
    }
    
    /** Setter for property reportQueueEmail.
     * @param reportQueueEmail New value of property reportQueueEmail.
     */
    public void setReportQueueEmail(String reportQueueEmail) {
        this.reportQueueEmail = reportQueueEmail;
    }
    
    /** Getter for property reportQueueName.
     * @return Value of property reportQueueName.
     */
    public String getReportQueueName() {
        return reportQueueName;
    }
    
    /** Setter for property reportQueueName.
     * @param reportQueueName New value of property reportQueueName.
     */
    public void setReportQueueName(String reportQueueName) {
        this.reportQueueName = reportQueueName;
    }
    
    /** Getter for property SMTPServer.
     * @return Value of property SMTPServer.
     */
    public String getSMTPServer() {
        return SMTPServer;
    }
    
    /** Setter for property SMTPServer.
     * @param SMTPServer New value of property SMTPServer.
     */
    public void setSMTPServer(String SMTPServer) {
        this.SMTPServer = SMTPServer;
    }
    
    /** Getter for property tempDir.
     * @return Value of property tempDir
     */
    public String getTempDir() {
    	return tempDir;
    }
    
    /** Setter for property tempDir.
     * @param tempDir New value of property tempDir.
     */
    public void setTempDir(String tempDir) {
    	this.tempDir = tempDir;
    }
    
	/** Getter for property requestPasswordFromEmail.
	 * @return Value of property requestPasswordFromEmail.
	 */
	public String getRequestPasswordFromEmail() {
		return this.requestPasswordFromEmail;
	}
	
	/** Setter for property requestPasswordFromEmail.
	 * @param requestPasswordFromEmail New value of property requestPasswordFromEmail.
	 */
	public void setRequestPasswordFromEmail(String requestPasswordFromEmail) {
		this.requestPasswordFromEmail = requestPasswordFromEmail;
	}

	/** Getter for property requestPasswordFromEmailName.
	 * @return Value of property requestPasswordFromEmailName.
	 */
	public String getRequestPasswordFromName() {
		return this.requestPasswordFromName;
	}
	
	/** Setter for property requestPasswordFromEmailName.
	 * @param requestPasswordFromEmailName New value of property requestPasswordFromEmailName.
	 */
	public void setRequestPasswordFromName(String requestPasswordFromName) {
		this.requestPasswordFromName = requestPasswordFromName;
	}
	
	/** Getter for property searchResultPageSize.
	 * @return Value of property searchResultPageSize.
	 */
	public int getSearchResultPageSize() {
		return searchResultPageSize;
	}
	
	/** Setter for property searchResultPageSize.
	 * @param searchResultPageSize New value of property searchResultPageSize.
	 */
	public void setSearchResultPageSize(int searchResultPageSize) {
		this.searchResultPageSize = searchResultPageSize;
	}
	
    /** Getter for property defaultChallengeQuestion.
     * @return Value of property defaultChallengeQuestion.
     *
     */
    public Integer getDefaultChallengeQuestion() {
        return defaultChallengeQuestion;
    }
    
    /** Setter for property defaultChallengeQuestion.
     * @param defaultChallengeQuestion New value of property defaultChallengeQuestion.
     *
     */
    public void setDefaultChallengeQuestion(Integer defaultChallengeQuestion) {
        this.defaultChallengeQuestion = defaultChallengeQuestion;
    }
   
        /** Getter for property minUploadFileSize.
         * @return Value of property minUploadFileSize.
         *
         */
        public long getMinUploadFileSize() {
            return minUploadFileSize;
        }
        
        /** Setter for property minUploadFileSize.
         * @param minUploadFileSize New value of property minUploadFileSize.
         *
         */
        public void setMinUploadFileSize(long minUploadFileSize) {
            this.minUploadFileSize = minUploadFileSize;
        }

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("maxLoginAttempts="+maxLoginAttempts+"\n");
		buf.append("lockoutTime="+lockoutTime+"\n");
		buf.append("actionPrivileges="+actionPrivileges+"\n");
		buf.append("AFLCIOExtractDestination="+AFLCIOExtractDestination+"\n");
		buf.append("SMTPServer="+SMTPServer+"\n");
		buf.append("reportFromEmail="+reportFromEmail+"\n");
		buf.append("reportFromEmail="+reportFromEmail+"\n");
		buf.append("reportQueueEmail="+reportQueueEmail+"\n");
		buf.append("maxLoginAttempts="+maxLoginAttempts+"\n");
		buf.append("reportQueueName="+reportQueueName+"\n");
		buf.append("tempDir="+tempDir+"\n");
		buf.append("requestPasswordFromEmail="+requestPasswordFromEmail+"\n");
		buf.append("requestPasswordFromName="+requestPasswordFromName+"\n");
		buf.append("searchResultPageSize="+searchResultPageSize+"\n");
		buf.append("defaultChallengeQuestion="+defaultChallengeQuestion+"\n");
        buf.append("minUploadFileSize="+minUploadFileSize+"\n");
        buf.append("yearConversionCutoff="+yearConversionCutoff+"\n");
        buf.append("JNDIClusterURL="+JNDIClusterURL+"\n");
        buf.append("JMSFactoryName="+JMSFactoryName+"\n");
        buf.append("JMSReportQueueName="+JMSReportQueueName+"\n");
        buf.append("JMSSystemLogQueueName="+JMSSystemLogQueueName+"\n");
        buf.append("JMSUpdateQueueName="+JMSUpdateQueueName+"\n");
		return buf.toString();
	}
        
        /** Getter for property yearConversionCutoff.
         * @return Value of property yearConversionCutoff.
         *
         */
        public int getYearConversionCutoff() {
            return yearConversionCutoff;
        }   
        
        /** Setter for property yearConversionCutoff.
         * @param yearConversionCutoff New value of property yearConversionCutoff.
         *
         */
        public void setYearConversionCutoff(int yearConversionCutoff) {
            this.yearConversionCutoff = yearConversionCutoff;
        }
        
        /** Getter for property JMSFactoryName.
         * @return Value of property JMSFactoryName.
         *
         */
        public String getJMSFactoryName() {
            return JMSFactoryName;
        }
        
        /** Setter for property JMSFactoryName.
         * @param JMSFactoryName New value of property JMSFactoryName.
         *
         */
        public void setJMSFactoryName(String JMSFactoryName) {
            this.JMSFactoryName = JMSFactoryName;
        }
        
        /** Getter for property JMSReportQueueName.
         * @return Value of property JMSReportQueueName.
         *
         */
        public String getJMSReportQueueName() {
            return JMSReportQueueName;
        }
        
        /** Setter for property JMSReportQueueName.
         * @param JMSReportQueueName New value of property JMSReportQueueName.
         *
         */
        public void setJMSReportQueueName(String JMSReportQueueName) {
            this.JMSReportQueueName = JMSReportQueueName;
        }
        
        /** Getter for property JMSSystemLogQueueName.
         * @return Value of property JMSSystemLogQueueName.
         *
         */
        public String getJMSSystemLogQueueName() {
            return JMSSystemLogQueueName;
        }
        
        /** Setter for property JMSSystemLogQueueName.
         * @param JMSSystemLogQueueName New value of property JMSSystemLogQueueName.
         *
         */
        public void setJMSSystemLogQueueName(String JMSSystemLogQueueName) {
            this.JMSSystemLogQueueName = JMSSystemLogQueueName;
        }
        
        /** Getter for property JMSUpdateQueueName.
         * @return Value of property JMSUpdateQueueName.
         *
         */
        public String getJMSUpdateQueueName() {
            return JMSUpdateQueueName;
        }
        
        /** Setter for property JMSUpdateQueueName.
         * @param JMSUpdateQueueName New value of property JMSUpdateQueueName.
         *
         */
        public void setJMSUpdateQueueName(String JMSUpdateQueueName) {
            this.JMSUpdateQueueName = JMSUpdateQueueName;
        }
        
        /** Getter for property afscmeUploadFolder.
         * @return Value of property afscmeUploadFolder.
         *
         */
        public String getAfscmeUploadFolder() {
            return afscmeUploadFolder;
        }
        
        /** Setter for property afscmeUploadFolder.
         * @param afscmeUploadFolder New value of property afscmeUploadFolder.
         *
         */
        public void setAfscmeUploadFolder(String afscmeUploadFolder) {
            this.afscmeUploadFolder = afscmeUploadFolder;
        }
        
        /** Getter for property affiliateUploadFolder.
         * @return Value of property affiliateUploadFolder.
         *
         */
        public String getAffiliateUploadFolder() {
            return affiliateUploadFolder;
        }
        
        /** Setter for property affiliateUploadFolder.
         * @param affiliateUploadFolder New value of property affiliateUploadFolder.
         *
         */
        public void setAffiliateUploadFolder(String affiliateUploadFolder) {
            this.affiliateUploadFolder = affiliateUploadFolder;
        }
        
        /** Getter for property JNDIClusterURL.
         * @return Value of property JNDIClusterURL.
         *
         */
        public java.lang.String getJNDIClusterURL() {
            return JNDIClusterURL;
        }
        
        /** Setter for property JNDIClusterURL.
         * @param JNDIClusterURL New value of property JNDIClusterURL.
         *
         */
        public void setJNDIClusterURL(java.lang.String JNDIClusterURL) {
            this.JNDIClusterURL = JNDIClusterURL;
        }
        
}
