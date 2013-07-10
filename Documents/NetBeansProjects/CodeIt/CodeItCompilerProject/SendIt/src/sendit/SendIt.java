package sendit;

import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Redemption
 */
public class SendIt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
        JFrame sendIt = new JFrame("SendIt! © Version 1.0");
        sendIt.add(new SendItGui());
        sendIt.setSize(800, 640);
        sendIt.setResizable(false);
        sendIt.setLocationRelativeTo(null);
        sendIt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sendIt.setVisible(true);
    }
}
