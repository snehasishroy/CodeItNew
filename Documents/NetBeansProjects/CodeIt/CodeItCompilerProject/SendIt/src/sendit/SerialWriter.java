package sendit;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Redemption
 */
class SerialWriter implements Runnable {

    OutputStream output;
    SendItGui ref;
    InputStream input;

    public SerialWriter(SendItGui ref) {
        this.output = ConnectModem.out;
        this.ref = ref;
        this.input = ConnectModem.in;
    }

    void writeMessage() {
        try {
            String text = ref.getMessage() + "\032";
            output.write(text.getBytes());
        } catch (IOException ex) {
            Logger.getLogger(SerialWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String recipient = ref.getRecipient();
        int numberOfRecipients = 1;
        //new Thread(new SerialReader(ref, this)).start();
        if (recipient.contains(" ")) {
            byte[] buffer = new byte[recipient.length()];
            int pos = 0;
            for (int i = 0; i < recipient.length(); i++) {
                if (recipient.charAt(i) != ' ') {
                    buffer[pos++] = (byte) recipient.charAt(i);
                }
            }
            recipient = new String(buffer, 0, pos);
        }
        if (recipient.contains(",")) {
            for (int i = 0; i < recipient.length(); i++) {
                if (recipient.charAt(i) == ',') {
                    numberOfRecipients++;
                }
            }

        }
        System.out.println("No of recipients: " + numberOfRecipients);

        String[] listOfRecipient = new String[numberOfRecipients];

        byte[] tmp = new byte[50];

        int pos = 0, k = 0;
        for (int i = 0; i < recipient.length(); i++) {

            if (recipient.charAt(i) == ',') {
                listOfRecipient[k++] = new String(tmp, 0, pos);
                pos = 0;
                tmp = new byte[50];
            } else {
                tmp[pos++] = (byte) recipient.charAt(i);
            }
        }
        listOfRecipient[k++] = new String(tmp, 0, pos);

        for (int i = 0; i < numberOfRecipients; i++) {
            System.out.println(listOfRecipient[i]);
        }

        //Extract no and send sms one by one
        for (int i = 0; i < numberOfRecipients; i++) {
            try {
                byte[] buffer = new byte[1024];
                //String command = "AT+CMGF=1;+CMGS=\"" + listOfRecipient[i] + "\"\r\n";
                String command = "AT\r\n";
                //System.out.println();
                output.write(command.getBytes());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(SerialWriter.class.getName()).log(Level.SEVERE, null, ex);
                }
                int charReturned = input.read(buffer);
                String response = new String(buffer, 0, charReturned);

                System.out.println("Response is:" + response);
//                if (response.contains("ERROR")) {
//                    ref.setStatus("AT+CMGF=1;+CMGS=\"\" not supported");
//                } else if (response.contains(">")) {
//                    writeMessage();
//                    charReturned = input.read(buffer);
//                    response = new String(buffer, 0, charReturned);
//                    if (response.equals("\r\nOK\r\n")) {
//                        ref.setStatus("Message sent");
//                    } else {
//                        ref.setStatus("Unable to send message");
//                    }
//                }

            } catch (IOException ex) {
                Logger.getLogger(SerialWriter.class.getName()).log(Level.SEVERE, null, ex);
                ref.setStatus("AT+CMGF=1;+CMGS=\"\" not supported");
            }
        }

    }
}
