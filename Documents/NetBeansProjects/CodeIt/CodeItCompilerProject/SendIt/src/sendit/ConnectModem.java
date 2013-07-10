package sendit;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Redemption
 */
public class ConnectModem {

    String portName;
    Integer baudRate;
    Integer dataBits;
    Integer stopBits;
    Integer parityBits;
    String flowControl;
    SendItGui ref;
    static InputStream in;
    static OutputStream out;
    
    public ConnectModem(SendItGui ref, String portName, Integer baudRate, Integer dataBits, Double stopBits, String parityBits, String flowControl) {
        this.portName = portName;
        this.baudRate = baudRate;
        this.ref = ref;
        //this.dataBits = dataBits;
        switch (dataBits) {
            case 5:
                this.dataBits = SerialPort.DATABITS_5;
                break;
            case 6:
                this.dataBits = SerialPort.DATABITS_6;
                break;
            case 7:
                this.dataBits = SerialPort.DATABITS_7;
                break;
            case 8:
                this.dataBits = SerialPort.DATABITS_8;
                break;
        }
        //this.stopBits = stopBits;
        if (stopBits == 1) {
            this.stopBits = SerialPort.STOPBITS_1;
        } else if (stopBits == 1.5) {
            this.stopBits = SerialPort.STOPBITS_1_5;
        } else {
            this.stopBits = SerialPort.STOPBITS_2;
        }

        //this.parityBits = parityBits;
        if (parityBits.equals("Even")) {
            this.parityBits = SerialPort.PARITY_EVEN;
        } else if (parityBits.equals("Odd")) {
            this.parityBits = SerialPort.PARITY_ODD;
        } else if (parityBits.equals("None")) {
            this.parityBits = SerialPort.PARITY_NONE;
        } else if (parityBits.equals("Mark")) {
            this.parityBits = SerialPort.PARITY_MARK;
        } else if (parityBits.equals("Space")) {
            this.parityBits = SerialPort.PARITY_SPACE;
        }

        this.flowControl = flowControl;
        tryToConnect();
    }

    private void tryToConnect() {
        CommPortIdentifier portIdentifier = null;
        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if (portIdentifier.isCurrentlyOwned()) {
                System.out.println("Error: Port is currently in use");
                JOptionPane.showMessageDialog(null, "Port is currently in use by " + portIdentifier.getCurrentOwner(), "Unable to open port", JOptionPane.ERROR_MESSAGE);
            } else {
                CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

                if (commPort instanceof SerialPort) {
                    SerialPort serialPort = (SerialPort) commPort;

                    if (flowControl.equals("None")) {
                        serialPort.setFlowControlMode(serialPort.FLOWCONTROL_NONE);
                    }

                    serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parityBits);
                    in = serialPort.getInputStream();
                    out = serialPort.getOutputStream();

                    ref.setStatus("Connected !!");
                    ref.enableDisconnect(true);
                    //(new Thread(new SerialReader(ref, in))).start();
                    //(new Thread(new SerialWriter(ref, out))).start();
                    //(new Thread(new SerialMsgWriter(out))).start();

                } else {
                    System.out.println("Error: Only serial ports are handled by this example.");
                    ref.setStatus("Provide a valid Serial port only");
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ConnectModem.class.getName()).log(Level.SEVERE, null, ex);
            ref.setStatus("Unable to communicate with modem. Retry !");

        } catch (UnsupportedCommOperationException ex) {
            Logger.getLogger(ConnectModem.class.getName()).log(Level.SEVERE, null, ex);
            ref.setStatus("Unsupported operation. Retry !");
            
        } catch (PortInUseException ex) {
            Logger.getLogger(ConnectModem.class.getName()).log(Level.SEVERE, null, ex);
            ref.setStatus("Port is already in use by "+portIdentifier.getCurrentOwner());
            
        } catch (NoSuchPortException ex) {
            Logger.getLogger(ConnectModem.class.getName()).log(Level.SEVERE, null, ex);
            ref.setStatus("No such port found. Provide a valid port name !");
        }
    }
}
