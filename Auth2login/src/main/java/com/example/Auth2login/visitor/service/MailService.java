package com.example.Auth2login.visitor.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.example.Auth2login.visitor.model.Visitor;
import com.example.Auth2login.visitor.repository.VisitorRepository;
import com.google.common.io.CharStreams;
import com.sun.mail.imap.protocol.UIDSet;


@Service
public class MailService 
{	
	@Autowired
	private Environment env;
	
	@Autowired
	VisitorService us;
	
	@Autowired
	VisitorRepository ur;
	
	Properties prop = new Properties();
	Session session;	
	Map<String, String> valuesMap = new HashMap<>();
	
	public void configPropertis() 
	{
		System.out.println("Inside Configure");
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.starttls.enable", "true");
		
		String username=env.getProperty("spring.mail.username");
		System.out.println(username);
		
		String password=env.getProperty("spring.mail.password");
		System.out.println(password);
		System.out.println("mail configure");
		
		session = Session.getInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}
	
	public String readFile(String filePath) 
	{
		try {
			InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(filePath);
			Reader reader = new InputStreamReader(resourceAsStream);
			String data = CharStreams.toString(reader);
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected String readEmailFromHtml(String filePath, Map<String, String> input)
	{
	    String msg = readContentFromFile(filePath);
	    try
	    {
	    Set<Entry<String, String>> entries = input.entrySet();
	    for(Map.Entry<String, String> entry : entries) {
	        msg = msg.replace(entry.getKey().trim(), entry.getValue().trim());
	    }
	    }
	    catch(Exception exception)
	    {
	        exception.printStackTrace();
	    }
	    return msg;
	}
	
	//Method to read HTML file as a String 
	private String readContentFromFile(String fileName)
	{
	    StringBuffer contents = new StringBuffer();
	    
	    try {
	      //use buffering, reading one line at a time
	      BufferedReader reader =  new BufferedReader(new FileReader(fileName));
	      try {
	        String line = null; 
	        while (( line = reader.readLine()) != null){
	          contents.append(line);
	          contents.append(System.getProperty("line.separator"));
	        }
	      }
	      finally {
	          reader.close();
	      }
	    }
	    catch (IOException ex){
	      ex.printStackTrace();
	    }
	    return contents.toString();
	}
	

	
	public void sendMail() 
	{
		/*configPropertis();
		MimeMessage message = new MimeMessage(session);
		try {
			   
	         message.addRecipient(Message.RecipientType.TO,new InternetAddress("ankit.sharma@accoliteindia.com"));  
	         message.setSubject("Ping");  
	         message.setText("Hello, this is example of sending email  ");  
			Transport.send(message);	
		} catch (MessagingException e) {
			e.printStackTrace();		
		}	*/
	}
	
	
	public void sendApprovalMail() 
	{
		configPropertis();
		MimeMessage message = new MimeMessage(session);
		
		
		  try {
	            //Set email data 
	            message.addRecipient(Message.RecipientType.TO,new InternetAddress("ankit.sharma@accoliteindia.com"));
	            message.setSubject("mail_subject");
	            MimeMultipart multipart = new MimeMultipart();
	            BodyPart messageBodyPart = new MimeBodyPart();
	            
	            //Set key values
	            Map<String, String> input = new HashMap<String, String>();
	               input.put("Author", "java2db.com");
	               input.put("Topic", "HTML Template for Email");
	               input.put("Content In", "English");
	             
	            //HTML mail content
	            String htmlText = readEmailFromHtml("C:/mail/approval_request.html", input);
	            messageBodyPart.setContent(htmlText, "text/html");
	            
	            multipart.addBodyPart(messageBodyPart); 
	            message.setContent(multipart);
	 
	            //Conect to smtp server and send Email
	           
	            Transport.send(message, message.getAllRecipients());
	            
	            System.out.println("Mail sent successfully..."); 
	     
	        }
	            catch (MessagingException ex) {
	                
	            }        catch (Exception ae) {
	            ae.printStackTrace();
	        }    
	        }   
	      
		
		
	
	
	public void check(String host, String storeType, String user,String password) 
	{
		String approverEmail = null;
		String visitorEmail = null;
		String approvalStatus = null;
		List<Visitor> user1 = null;
		
		Pattern pattern = Pattern.compile("\\<(.*?)\\>");
		Pattern pattern1 = Pattern.compile("\\.*((?i)approved|declined(?-i))\\.*");
		try 
		{
			//create properties field
		    Properties properties = new Properties();
		    properties.put("mail.imap.host", host);
		    properties.put("mail.imap.port", "995");
		    properties.put("mail.imap.starttls.enable", "true");
		    properties.put("mail.transport.protocol", "imaps");
		    Session emailSession = Session.getDefaultInstance(properties);
		  
		    //create the POP3 store object and connect with the pop server
		    Store store = emailSession.getStore("imaps");
		    store.connect(host, user, password);

		    //create the folder object and open it
		    Folder emailFolder = store.getFolder("INBOX");
		    emailFolder.open(Folder.READ_WRITE);

		    // retrieve the messages from the folder in an array and print it
		    Flags seen = new Flags(Flags.Flag.SEEN);
		    FlagTerm unseenFlagTerm = new FlagTerm(seen, false);
		    //Message[] messages = emailFolder.getMessages();
		    Message messages[] = emailFolder.search(unseenFlagTerm);
		    //System.out.println("messages.length---" + messages.length);

		    for (int i = 0;  i < messages.length; i++)
		    {
		         Message message = messages[i];
		         //System.out.println("---------------------------------");
		         //System.out.println("Email Number " + (i + 1));
		         //System.out.println("Subject: " + message.getSubject());
		         visitorEmail = PatternMatcher(pattern, message.getSubject().toString());
		         //System.out.println("Visitor Email Id  "+visitorEmail);
		         //System.out.println("From: " + message.getFrom()[0]);
		         //String requiredString = message.getSubject().toString().substring(message.getSubject().toString().indexOf("(") + 1, message.getSubject().toString().indexOf(")"));
		         //System.out.println(requiredString);
		         approverEmail = PatternMatcher(pattern, message.getFrom()[0].toString());
		         //System.out.println("Approver Email Id  "+approverEmail);
		         MimeMultipart msgContent =  (MimeMultipart) message.getContent();
		         String msgBody = getTextFromMimeMultipart(msgContent);
		         message.setFlag(Flags.Flag.SEEN, true);
		         approvalStatus = PatternMatcher(pattern1, msgBody); 
		         System.out.println(approvalStatus);
		        if (approvalStatus!=null) 
		        {	
		        	ur.FindandUpdateByEmail(visitorEmail,approverEmail,approvalStatus.toUpperCase());
				}		         
		      }

		      //close the store and folder objects
		      emailFolder.close(false);
		      store.close();

		      } catch (NoSuchProviderException e) {
		         e.printStackTrace();
		      } catch (MessagingException e) {
		         e.printStackTrace();
		      } catch (Exception e) {
		         e.printStackTrace();
		      	}
	}
	
	private static String PatternMatcher(Pattern pattern, String data) 
	{
		String result = null;
		Matcher m = pattern.matcher(data);
		while (m.find()) {
			result = m.group().replace("<", "").replace(">", "");
		}
		return result;

	}
	

	private static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws MessagingException, IOException {
		String result = "";
		int count = mimeMultipart.getCount();
		for (int i = 0; i < count; i++) {
			BodyPart bodyPart = mimeMultipart.getBodyPart(i);
			if (bodyPart.isMimeType("text/plain")) {
				result = result + bodyPart.getContent();
				break; // without break same text appears twice in my tests
			} 
		}
		return result;
	}

	
}	
