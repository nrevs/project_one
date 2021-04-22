package project_one;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

    private static ConfigReader cr = ConfigReader.getInstance();
    private static Properties props = cr.getProps();

    public static void send(String to, String subject, String msg) {

        /* ConfigReader reads .myconfig in resources folder
        * You should update .myconfig according to your email server configurations
        * .myconfig is ignored by git for obvious reasons
        * .myconfig should have the following properties and format:
        *      user=<email server username>
        *      pass=<email server password>
        *      mail.smtp.host=<mail server host>
        *      mail.smtp.auth=true
        *
        *  eg.
        *
        * user=johnsnow
        * pass=password
        * mail.smtp.host=mail.wombat.com
        * mail.smtp.auth=true
        */
        Session session = Session.getDefaultInstance(props, 
            new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(props.getProperty("user"), props.getProperty("pass"));
                }
            }
        );

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(props.getProperty("user")) );
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setText(msg);
            
            Transport.send(message);

        } catch(MessagingException meE) {
            meE.printStackTrace();
        }

    }

}
