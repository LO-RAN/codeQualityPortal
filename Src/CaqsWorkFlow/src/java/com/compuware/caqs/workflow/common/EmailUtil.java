/**
 * 
 */
package com.compuware.caqs.workflow.common;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * @author cwfr-fdubois
 *
 */
public class EmailUtil {

    public void sendEmail(String to, String projectName, String subject, String text) {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getResourceAsStream("/bonita-caqs-mail.properties"));

            String host = prop.getProperty("mail.host");
            String from = prop.getProperty("mail.from");
            String header = prop.getProperty("mail.header");
            String cc = prop.getProperty("mail.cc");

            Properties props = System.getProperties();
            props.put("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(props, null);

            Message msg = new MimeMessage(session);
            InternetAddress[] toAddrs = null;
            InternetAddress[] ccAddrs = null;

            if (to != null) {
                toAddrs = InternetAddress.parse(to, false);
                ccAddrs = InternetAddress.parse(cc, false);
                msg.setRecipients(Message.RecipientType.TO, toAddrs);
                msg.setRecipients(Message.RecipientType.CC, ccAddrs);
            }
            msg.setFrom(new InternetAddress(from));

            msg.setSubject(header + ": " + subject);
            msg.setText(text);

            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
