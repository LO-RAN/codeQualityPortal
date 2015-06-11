/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.carscode.parser.util.mail;

import com.sun.mail.imap.IMAPFolder;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.FolderClosedException;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.event.MessageCountEvent;
import javax.mail.event.MessageCountListener;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.ParseException;

/**
 *
 * @author cwfr-lizac
 */
public class MessagesProcessor implements MessageCountListener {

    int level = 0;
    int attnum = 1;
    File destDir = null;
    static final Logger logger = Logger.getLogger(MessagesProcessor.class.getName());
    boolean isProcessed = false;
    String prefix = "";

    public MessagesProcessor(File theDestDir) {

        this.destDir = theDestDir;
        if (!this.destDir.exists()) {
            this.destDir.mkdirs();
        }
    }

    public void messagesAdded(MessageCountEvent ev) {
        Message[] msgs = ev.getMessages();
        logger.log(Level.INFO, "Processing " + msgs.length + " new message(s)...");

        for (int i = 0; i < msgs.length; i++) {
            processMessage(msgs[i]);
        }
    }

    public void messagesRemoved(MessageCountEvent ev) {
        Message[] msgs = ev.getMessages();
        for (int i = 0; i < msgs.length; i++) {
            logger.log(Level.INFO, "Message " + msgs[i].getMessageNumber() + " removed");
        }
    }

    public void checkNewMessages(String host, int port, String user, String password, String mbox, String frequency, String thePrefix, String protocol) {
        logger.log(Level.INFO, "\nMonitoring {0}...\n", mbox);
        this.prefix = thePrefix;
        try {
            Properties props = System.getProperties();

            // Get a Session object
            Session session = Session.getInstance(props, null);
            // session.setDebug(true);

            // Get a Store object
            Store store = session.getStore(protocol);

            // Connect
            store.connect(host, port, user, password);

            // Open a Folder
            Folder folder = store.getFolder(mbox);
            if (folder == null || !folder.exists()) {
                logger.log(Level.SEVERE, "Invalid folder");
                return;
            }

            folder.open(Folder.READ_WRITE);

            // Add messageCountListener to listen for new messages
            folder.addMessageCountListener(this);

            // Check mail once in "freq" MILLIseconds
            int freq = Integer.parseInt(frequency);

            while (!isProcessed /*&& attempts>0*/) {
                    logger.log(Level.INFO, "Going to sleep for " + freq/1000 + " seconds...");
                    Thread.sleep(freq); // sleep for freq milliseconds
                    // This is to force the IMAP server to send us
                    // EXISTS notifications.
                    folder.getMessageCount();
                }               
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }
    }

    private void processMessage(Message msg) {


        logger.log(Level.INFO, "Processing message " + msg.getMessageNumber() + ":");
        try {
            //msg.writeTo(System.out);
            if (msg.getSubject().startsWith(prefix)) {
                dumpPart(msg);
                isProcessed = true;

            } else {
                pr("Ignoring message because subject does not start with '" + prefix + "' prefix!");
            }
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
            isProcessed = true;
        }
    }

    private void dumpPart(Part p) throws Exception {
        if (p instanceof Message) {
            dumpEnvelope((Message) p);
        }

        String ct = p.getContentType();
        try {
            pr("CONTENT-TYPE: " + (new ContentType(ct)).toString());
        } catch (ParseException pex) {
            pr("BAD CONTENT-TYPE: " + ct);
        }
        String filename = p.getFileName();
        if (filename != null) {
            filename=filename.replaceFirst(prefix, "");
            pr("FILENAME: " + filename);
        }

        /*
         * Using isMimeType to determine the content type avoids
         * fetching the actual content data until we need it.
         */
        if (p.isMimeType("text/plain")) {
            pr("This is plain text");
            pr("---------------------------");
        } else if (p.isMimeType("multipart/*")) {
            pr("This is a Multipart");
            pr("---------------------------");
            Multipart mp = (Multipart) p.getContent();
            level++;
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                dumpPart(mp.getBodyPart(i));
            }
            level--;
        } else if (p.isMimeType("message/rfc822")) {
            pr("This is a Nested Message");
            pr("---------------------------");
            level++;
            dumpPart((Part) p.getContent());
            level--;
        }
        /*
         * write out anything that
         * looks like an attachment into an appropriately named
         * file.  Don't overwrite existing files to prevent
         * mistakes.
         */
        if (level != 0 && !p.isMimeType("multipart/*")) {
            String disp = p.getDisposition();
            // many mailers don't include a Content-Disposition
            if (disp == null || disp.equalsIgnoreCase(Part.ATTACHMENT)) {
                if (filename == null) {
                    filename = "Attachment" + attnum++;
                }
                pr("Saving attachment to file " + filename);
                try {
                    File f = new File(destDir, filename);
                    if (f.exists()) {
                        throw new IOException("file exists");
                    }
                    ((MimeBodyPart) p).saveFile(f);
                } catch (IOException ex) {
                    pr("Failed to save attachment: " + ex);
                }
                pr("---------------------------");
            }
        }
    }

    public static void dumpEnvelope(Message m) throws Exception {
        pr("This is the message envelope");
        pr("---------------------------");
        Address[] a;
        // FROM
        if ((a = m.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                pr("FROM: " + a[j].toString());
            }
        }

        // REPLY TO
        if ((a = m.getReplyTo()) != null) {
            for (int j = 0; j < a.length; j++) {
                pr("REPLY TO: " + a[j].toString());
            }
        }

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                pr("TO: " + a[j].toString());
                InternetAddress ia = (InternetAddress) a[j];
                if (ia.isGroup()) {
                    InternetAddress[] aa = ia.getGroup(false);
                    for (int k = 0; k < aa.length; k++) {
                        pr("  GROUP: " + aa[k].toString());
                    }
                }
            }
        }

        // SUBJECT
        pr("SUBJECT: " + m.getSubject());

        // DATE
        Date d = m.getSentDate();
        pr("SendDate: "
                + (d != null ? d.toString() : "UNKNOWN"));

        // FLAGS
        Flags flags = m.getFlags();
        StringBuilder sb = new StringBuilder();
        Flags.Flag[] sf = flags.getSystemFlags(); // get the system flags

        boolean first = true;
        for (int i = 0; i < sf.length; i++) {
            String s;
            Flags.Flag f = sf[i];
            if (f == Flags.Flag.ANSWERED) {
                s = "\\Answered";
            } else if (f == Flags.Flag.DELETED) {
                s = "\\Deleted";
            } else if (f == Flags.Flag.DRAFT) {
                s = "\\Draft";
            } else if (f == Flags.Flag.FLAGGED) {
                s = "\\Flagged";
            } else if (f == Flags.Flag.RECENT) {
                s = "\\Recent";
            } else if (f == Flags.Flag.SEEN) {
                s = "\\Seen";
            } else {
                continue;
            }	// skip it
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            sb.append(s);
        }

        String[] uf = flags.getUserFlags(); // get the user flag strings
        for (int i = 0; i < uf.length; i++) {
            if (first) {
                first = false;
            } else {
                sb.append(' ');
            }
            sb.append(uf[i]);
        }
        pr("FLAGS: " + sb.toString());

        // X-MAILER
        String[] hdrs = m.getHeader("X-Mailer");
        if (hdrs != null) {
            pr("X-Mailer: " + hdrs[0]);
        } else {
            pr("X-Mailer NOT available");
        }
    }

    /**
     * Print a, possibly indented, string.
     */
    public static void pr(String s) {
        System.out.println(s);
    }
}
