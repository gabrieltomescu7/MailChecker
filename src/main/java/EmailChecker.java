
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.NoSuchProviderException;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.event.MessageCountEvent;
import jakarta.mail.event.MessageCountListener;
import org.eclipse.angus.mail.imap.IMAPFolder;

import java.util.Properties;

public class EmailChecker {
    public static void check(String host, String storeType, String user,
                             String password) {

            try {
                Properties properties = new Properties();

                properties.put("mail.imap.host", host);
                properties.put("mail.imap.port", "993");
                properties.put("mail.imap.ssl.enable", "true");
                Session emailSession = Session.getDefaultInstance(properties);

                Store store = emailSession.getStore("imap");

                store.connect(host, user, password);

                Folder emailFolder = store.getFolder("INBOX");
                emailFolder.open(Folder.READ_ONLY);

                //afisare header mail-uri
            /*
            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

            }
            */

                //notificare mail-uri noi
                emailFolder.addMessageCountListener(new MessageCountListener() {
                    @Override
                    public void messagesAdded(MessageCountEvent messageCountEvent) {
                        for (Message message : messageCountEvent.getMessages()) {
                            try {
                                System.out.println("Mesaj nou! " +
                                        "\nSubiect: " + message.getSubject() +
                                        "\nExpeditor: " + message.getFrom()[0] +
                                        "\nData: " + message.getReceivedDate() +
                                        "\n========");

                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void messagesRemoved(MessageCountEvent messageCountEvent) {

                    }
                });
                System.out.println("Monitorizare mail-uri");
                while (true) {
                    ((IMAPFolder) emailFolder).idle();
                }
                //emailFolder.close(false);
                //store.close();

            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (MessagingException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    public static void main(String[] args) {

        String host = "imap.mail.yahoo.com";// imap pentru yahoo
        String mailStoreType = "imap";
        String username = "hufel77@yahoo.com";
        String password = "**************"; // yahoo app-pass setat din cont

        check(host, mailStoreType, username, password);

    }
}
