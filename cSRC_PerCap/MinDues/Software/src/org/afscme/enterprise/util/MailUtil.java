package org.afscme.enterprise.util;

import javax.mail.Multipart;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.Message;
import javax.mail.Session;
import java.util.Date;
import java.util.Properties;
import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.mail.Message.RecipientType;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.InternetAddress;

/**
 * Contains static methods for common Mail functions.
 */
public class MailUtil {
	
	static final public String MAIL_PROTOCOL = "mail.smtp.host";
	
	/**
	 * Sends email
	 * @param to - address to send the mail to.
	 * @param toName - Display name of the recipient.
	 * @param from - address the email is from
	 * @param fromName - display name of the sender
	 * @param subject - Subject of the message
	 * @param body - message body.
	 */
	public static void sendMail(String to,
								String toName,
								String from,
								String fromName,
								String subject,
								String body)
								throws IOException, MessagingException {
		
		try {
			Properties props = new Properties();
        	// Setup mail server (using SMTP)
        	props.put(MAIL_PROTOCOL, ConfigUtil.getConfigurationData().getSMTPServer());

        	// Get a Session object
        	Session session = Session.getDefaultInstance(props, null);
        
        	// construct the message
        	Message msg = new MimeMessage(session);
        	msg.setFrom(new InternetAddress(from, fromName));
        	msg.setRecipient(RecipientType.TO, new InternetAddress(to, toName));
        	msg.setSubject(subject);
        	msg.setSentDate(new Date());
   			msg.setText(body);

        	// send the message to the server
        	Transport.send(msg);
        }
    	catch(UnsupportedEncodingException exp) {
    		throw (IOException)exp;
    	}		
	}
	
	/**
	 * Sends email with an attachment.
	 * @param to - address of the recipient
	 * @param toName - Display name of the recipient.
	 * @param from - Address of the sender
	 * @param fromName - Display name of the sender
	 * @param subject - Subject of the message
	 * @param body - Message body
	 * @param attachment - Input stream of attachment content.
	 * @param attachmentFileName - File name of the attachment.
	 * @param attachmentMimeType - the mime type of the attachment.
	 */
	public static void sendMail(String to,
								String toName,
								String from,
								String fromName,
								String subject,
								String body,
								InputStream attachment,
								String attachmentFileName,
								String attachmentMimeType)
								throws MessagingException, IOException {

		// define the local inner class.
		class PrivateDataSource implements DataSource {
	
			private byte[] data;	// data
    		private String type;	// content-type

    		/* Create a DataSource from an input stream */
    		public PrivateDataSource(InputStream is, String type) throws IOException {
        		this.type = type;
    
        		// create a buffered output stream
            	ByteArrayOutputStream os = new ByteArrayOutputStream();
	    		BufferedOutputStream bufferOutput = new BufferedOutputStream(os);
	    		
				// create a buffered input stream.
	    		BufferedInputStream bufferInput = new BufferedInputStream(is);
	    			
	    		// read the input into the output
	    		int ch;
	    		while ((ch = bufferInput.read()) != -1) {
	    			bufferOutput.write(ch);
	    		}
	    		bufferOutput.flush(); // must do this!
	    		data = os.toByteArray();
	    			
	    		bufferOutput.close();
	    		bufferInput.close();
   				os.close();
	    		is.close();    	   		
        	}

		    /**
    		 * Return an InputStream for the data.
    		 * Note - a new stream must be returned each time.
   			 */
    		public InputStream getInputStream() throws IOException {
				if (data == null)
	    			throw new IOException("no data");
				return new ByteArrayInputStream(data);				
    		}

    		public OutputStream getOutputStream() throws IOException {
				throw new IOException("cannot do this");
    		}

    		public String getContentType() {
        		return type;
    		}

    		public String getName() {
        		return "dummy";
    		}
		} // end of the inner class
		
		try {
			Properties props = System.getProperties();
        	props.put(MAIL_PROTOCOL, ConfigUtil.getConfigurationData().getSMTPServer());

        	// Get a Session object
        	Session session = Session.getDefaultInstance(props, null);

			// construct the message
        	Message msg = new MimeMessage(session);
        	msg.setFrom(new InternetAddress(from, fromName));
       		msg.setRecipient(RecipientType.TO, new InternetAddress(to, toName));
        	msg.setSubject(subject);
        	msg.setSentDate(new Date());
            
        	// create and fill the first message part
	    	MimeBodyPart mbp1 = new MimeBodyPart();
	    	mbp1.setText(body);

			// create the second message part
	    	MimeBodyPart mbp2 = new MimeBodyPart();

        	// attach the inputstream to the message
   	    	PrivateDataSource dataSource = new PrivateDataSource(attachment, attachmentMimeType);   		
	   		mbp2.setDataHandler(new DataHandler(dataSource));
	    	mbp2.setFileName(attachmentFileName);

    		// create the Multipart and its parts within it
    		Multipart mp = new MimeMultipart();
    		mp.addBodyPart(mbp1);
    		mp.addBodyPart(mbp2);

    		// add the Multipart to the message
    		msg.setContent(mp);

	       	// send the message
    	   	Transport.send(msg);
    	 }
    	 catch(UnsupportedEncodingException exp) {
    	 	throw (IOException)exp;
    	 }
    }
}
