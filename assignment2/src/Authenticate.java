import java.security.spec.InvalidKeySpecException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Authenticate {

    // in lieu of a database engine or other login management system, credentials
    // are stored here
    // in the interest of security the passwords are not stored clear text, however
    // for testing purposes
    // the password for "sdevadmin" is "425!pass" and the password for "user" is
    // "password"
    private String[][] users = { { "sdevadmin",
            "10:b6ec9b40493e808ca384a79cf73a5fdb:70ff2ceb4ac1f03e6a0bfc436541b047839bc95433ad602d90358ea9e8eded1b205123ec580a8f70e81a26c6fdfcb2d7b92feab2242dd951a5ce362b03d65a2a",
            "admin" },
            { "user",
                    "10:ed863b5f8eea0723bf38f5b95eb1a70d:a0773001023c7cbe73318b0af7e0a3d2a3bbc713fb3922f2af3913d711ed30a37d62769bc0db79544b24ab96e093c389415220cd9a78c703b88b80b41502e898",
                    "base" } };

    // this method iterates the users array for matching usernames and passwords
    // supplied
    public boolean validateLogin(String username, String password) {
        for (int i = 0; i < users.length; i++) {
            if (users[i][0].equals(username)) {
                for (int x = 1; x == 1; x++) {
                    try {
                        if (validatePassword(password, users[i][x])) {
                            if (users[i][2] == "admin") {
                                Scanner sc = new Scanner(System.in);
                                String tgt = "0";
                                // int tries = 1;
                                while (tgt == "0") {
                                    System.out.println("Enter email used for two factor authentication");
                                    String email = sc.nextLine();
                                    tgt = twoFactor(email);
                                    // tries++;

                                    // if (tries > 3) {
                                    // System.out.println("Attempts exceeded max. Terminating program");
                                    // System.exit(0);
                                    // }
                                }
                                System.out.println("Enter code from email: ");
                                String attempt = sc.nextLine();
                                if (tgt.equals(attempt)) {
                                    sc.close();
                                }
                                return tgt.equals(attempt);
                            }
                            return true;
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }
                }

            }

        }
        return false;
    }

    // this method compares a password hash to validate login
    private boolean validatePassword(String enteredPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] passwordParts = storedPassword.split(":");
        int iterations = Integer.parseInt(passwordParts[0]);

        byte[] salt = fromHex(passwordParts[1]);
        byte[] hash = fromHex(passwordParts[2]);

        PBEKeySpec spec = new PBEKeySpec(enteredPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] test = key.generateSecret(spec).getEncoded();

        int diff = hash.length ^ test.length;

        for (int i = 0; i < hash.length && i < test.length; i++) {
            diff |= hash[i] ^ test[i];
        }

        return diff == 0;
    }

    private byte[] fromHex(String hex) {
        byte[] bytes = new byte[hex.length() / 2];

        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    // this method sends an email to verify 2 factor authentication
    // other testers may want to change the email destination, but for now it is
    // hard coded
    private String twoFactor(String email) {

        final String username = "sdev425testing@gmail.com";
        final String password = "kkfpzwsmajujjmxb";

        Random r = new Random();
        String randomNumber = String.format("%04d", (Object) Integer.valueOf(r.nextInt(1001)));

        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        // System.out.println("Input email to receive 2 step authentication code: ");
        // String recipient = sc.nextLine();

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("sdev425testing@gmail.com"));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            message.setSubject("Testing Subject");
            message.setText("Input this code into the command prompt: \n" + randomNumber);

            Transport.send(message);

        } catch (MessagingException e) {
            System.out.println("Invalid email address");
            return "0";
        }
        return randomNumber;
    }
}
