
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TwoWaySerialComm {

    public TwoWaySerialComm() {
        super();
    }
    static boolean ready = false;

    public static class SerialMsgWriter implements Runnable {

        OutputStream out;
        String message = "HELP"+"\032";

        public SerialMsgWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            while (true) {
                if (ready) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(TwoWaySerialComm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        this.out.write(message.getBytes());
                        ready = false;

                        System.out.println("written");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    void connect(String portName) throws Exception {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if (portIdentifier.isCurrentlyOwned()) {
            System.out.println("Error: Port is currently in use");
        } else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setFlowControlMode(serialPort.FLOWCONTROL_NONE);

                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

                InputStream in = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                
                //(new Thread(new SerialReader(in))).start();
                (new Thread(new SerialWriter(out))).start();
                (new Thread(new SerialMsgWriter(out))).start();

            } else {
                System.out.println("Error: Only serial ports are handled by this example.");
            }
        }
    }

    /**
     *
     */
    public static class SerialReader implements Runnable {

        InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int len = -1;
            try {
                while ((len = this.in.read(buffer)) > -1) {
                    String response = new String(buffer, 0, len);

                    System.out.print(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     */
    public static class SerialWriter implements Runnable {

        OutputStream out;
        String message = "AT+CMGF=1;+CMGS=\""+"121"+"\"\r\n";

        public SerialWriter(OutputStream out) {
            this.out = out;
        }

        public void run() {
            try {

                this.out.write(message.getBytes());
                ready = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            (new TwoWaySerialComm()).connect("COM5");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}