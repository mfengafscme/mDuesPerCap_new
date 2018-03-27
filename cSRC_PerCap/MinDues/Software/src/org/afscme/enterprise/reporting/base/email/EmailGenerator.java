/*
 * @(#)EmailGenerator.java
 *
 * Copyright (c) 2002 AFSCME org.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of AFSCME
 * Orgnization. ("Confidential Information"). 
 */

package org.afscme.enterprise.reporting.base.email;

import javax.mail.MessagingException;
import java.text.DateFormat;
import org.afscme.enterprise.util.MailUtil;
import org.afscme.enterprise.util.ConfigUtil;
import java.io.IOException;
import org.afscme.enterprise.common.ConfigurationData;
import org.apache.log4j.Logger;

public class EmailGenerator {
    
    private static Logger logger =  Logger.getLogger(EmailGenerator.class);
    
    public static void sendEmail(ReportEmailData emailData) throws MessagingException, IOException {
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        String requestedTime = dateFormat.format(emailData.getRequestedTime());

        String body;
        if (!emailData.isMailingList())
            body = "The following report is generated for you:\n\n" +
                    "Report Name, Output Format, Requestor User ID, Requested Time\n\n" +
                    emailData.getReportName() + ", " +
                    emailData.getOutputFormat() + ", " +
                    emailData.getRequestorUserID() + ", " +
                    requestedTime;
        else
            body = "The following report is generated for you:\n\n" +
                    "Report Name, Output Format, Requestor User ID, Requested Time, The Number of Pieces Of Mail\n\n" +
                    emailData.getReportName() + ", " +
                    emailData.getOutputFormat() + ", " +
                    emailData.getRequestorUserID() + ", " +
                    requestedTime + ", " +
                    emailData.getNumOfMail();

        String attachmentFileName = null;
        if (emailData.getReportMimeType().equals(ReportEmailData.PDF_MIME))
            attachmentFileName = emailData.getReportName() + ".pdf";
        else
            attachmentFileName = emailData.getReportName() + ".txt";
                logger.debug("emailData.getGeneratedReport()===>" + emailData.getGeneratedReport());
		ConfigurationData config = ConfigUtil.getConfigurationData();
		
		MailUtil.sendMail(config.getReportQueueEmail(),
                                  config.getReportQueueName(),
                                  config.getReportFromEmail(),
                                  config.getReportFromName(),
                                  emailData.getSubject(),
                                  body,
                                  emailData.getGeneratedReport(),
                                  attachmentFileName,
                                  emailData.getReportMimeType());
	}

}