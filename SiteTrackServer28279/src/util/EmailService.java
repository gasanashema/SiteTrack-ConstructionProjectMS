package util;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailService {

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_SECONDS = 5;

    public static boolean sendOtpEmail(String toEmail, String otpCode) {
        for (int attempt = 1; attempt <= MAX_RETRY_ATTEMPTS; attempt++) {
            try {
                if (attemptSend(toEmail, otpCode)) {
                    return true;
                }
            } catch (Exception ex) {
                System.err.println("OTP email send attempt " + attempt + " failed: " + ex.getMessage());
                
                if (attempt < MAX_RETRY_ATTEMPTS) {
                    try {
                        Thread.sleep(RETRY_DELAY_SECONDS * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        System.err.println("Failed to send OTP email after " + MAX_RETRY_ATTEMPTS + " attempts");
        return false;
    }

    private static boolean attemptSend(String toEmail, String otpCode) throws Exception {
        ConfigManager config = ConfigManager.getInstance();
        String host = config.getProperty("mail.host");
        String port = config.getProperty("mail.port");
        String username = config.getProperty("mail.username");
        String password = config.getProperty("mail.password");
        String auth = config.getProperty("mail.smtp.auth", "true");
        String starttls = config.getProperty("mail.smtp.starttls.enable", "true");

        if (username == null || username.isEmpty() || "your_email@gmail.com".equals(username)) {
            System.err.println("EmailService: SMTP credentials not configured properly in config.properties");
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username, "SiteTrack Security"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject("Your SiteTrack OTP: " + otpCode);
        
        String htmlContent = "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "  <meta charset='UTF-8'>" +
               "  <style>" +
               "    body { font-family: Arial, sans-serif; background-color: #f5f5f5; }" +
               "    .container { max-width: 600px; margin: 0 auto; background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }" +
               "    .header { background-color: #1B3A6B; color: white; padding: 20px; text-align: center; border-radius: 8px 8px 0 0; }" +
               "    .header h1 { margin: 0; font-size: 24px; }" +
               "    .content { padding: 20px; text-align: center; }" +
               "    .otp-box { background-color: #f0f0f0; border: 2px solid #1B3A6B; border-radius: 8px; padding: 20px; margin: 20px 0; }" +
               "    .otp-code { font-size: 36px; font-weight: bold; color: #1B3A6B; letter-spacing: 4px; font-family: 'Courier New', monospace; }" +
               "    .expiry { color: #666; font-size: 14px; margin-top: 10px; }" +
               "    .footer { background-color: #f5f5f5; padding: 15px; text-align: center; font-size: 12px; color: #999; }" +
               "    .warning { color: #F44336; font-weight: bold; }" +
               "  </style>" +
               "</head>" +
               "<body>" +
               "  <div class='container'>" +
               "    <div class='header'>" +
               "      <h1>SiteTrack Construction Manager</h1>" +
               "    </div>" +
               "    <div class='content'>" +
               "      <h2>Your One-Time Password (OTP)</h2>" +
               "      <p>Your OTP for login verification is:</p>" +
               "      <div class='otp-box'>" +
               "        <div class='otp-code'>" + otpCode + "</div>" +
               "        <div class='expiry'>Valid for 5 minutes</div>" +
               "      </div>" +
               "      <p><span class='warning'>Never share this code with anyone.</span></p>" +
               "      <p>If you did not request this OTP, please ignore this email.</p>" +
               "    </div>" +
               "    <div class='footer'>" +
               "      <p>&copy; 2026 SiteTrack Construction Manager. All rights reserved.</p>" +
               "      <p>This is an automated message, please do not reply.</p>" +
               "    </div>" +
               "  </div>" +
               "</body>" +
               "</html>";

        message.setContent(htmlContent, "text/html; charset=utf-8");

        Transport.send(message);
        return true;
    }
}
