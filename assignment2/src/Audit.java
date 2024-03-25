import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.net.*;

public class Audit {

    Writer auditWriter = new Writer();
    String auditMessage;

    // this method writes to the audit log when a user fails to log in
    public void loginFail(String username) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = now.format(format);
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            auditMessage = "Login fail for username: " + username + "\nFrom IP Address: " + localHost.toString()
                    + " \nat: " + formattedDateTime + "\n";
            auditWriter.appendWrite("audit.txt", auditMessage);
        } catch (UnknownHostException e) {
            auditMessage = "Login fail for username: " + username + "\nFrom unknown IP address: " + formattedDateTime
                    + "\n";
            auditWriter.appendWrite("audit.txt", auditMessage);
        }
    }

    // this method writes to the audit log when a user logs in successfully
    public void loginSuccess(String username) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = now.format(format);
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            auditMessage = "Login success for username: " + username + "\nFrom IP Address: " + localHost.toString()
                    + " \nat: " + formattedDateTime
                    + "\n";
            auditWriter.appendWrite("audit.txt", auditMessage);
        } catch (UnknownHostException e) {
            auditMessage = "Login success from unknown IP address: " + formattedDateTime + "\n";
            auditWriter.appendWrite("audit.txt", auditMessage);
        }
    }

    // this method writes to the audit log when a user exhausts log in attempts
    public void loginLock(String username) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = now.format(format);
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            auditMessage = "Login fail from IP Address: " + localHost.toString() + " \nat: " + formattedDateTime
                    + "\n Account with username " + username + " locked\n";
            auditWriter.appendWrite("audit.txt", auditMessage);
        } catch (UnknownHostException e) {
            auditMessage = "Login fail from unknown IP address: " + formattedDateTime + "\n";
            auditWriter.appendWrite("audit.txt", auditMessage);
        }
    }
}
