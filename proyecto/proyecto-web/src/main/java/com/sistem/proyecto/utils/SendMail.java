/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sistem.proyecto.utils;

import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Miguel
 */
public class SendMail {

    public final static boolean enviarMensaje(final String userNameEmail, final String passWord, List<String> to,
            List<String> cc, List<String> bcc, String mensaje, String cabecera) {
        try {

            Properties props = System.getProperties();
            String host = "smtp.gmail.com";
            //props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", host);
            props.put("mail.smtp.user", userNameEmail);
            props.put("mail.smtp.password", passWord);
            props.put("mail.smtp.port", "465");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");

            

            Authenticator authenticator = null;
            if (true) {
                authenticator = new Authenticator() {
                    private PasswordAuthentication pa = new PasswordAuthentication(userNameEmail, passWord);

                    @Override
                    public PasswordAuthentication getPasswordAuthentication() {
                        return pa;
                    }
                };
            }

            Session session = Session.getDefaultInstance(props, authenticator);
            session.setDebug(true);
            MimeMessage msg = new MimeMessage(session);
            msg.setText(mensaje);
            msg.setSubject(cabecera);
            msg.setFrom(new InternetAddress(userNameEmail));

            for (String to1 : to) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to1));
            }
            for (String cc1 : cc) {
                msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc1));
            }
            for (String bcc1 : bcc) {
                msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc1));
            }
            msg.saveChanges();

            Transport transport = session.getTransport("smtp");
            transport.connect(host, userNameEmail, passWord);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();

        } catch (MessagingException ex) {
            System.err.println("Error a enviar mensaje. " + ex);
            return false;
        }
        return true;

    }

}
