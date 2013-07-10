/**
 * Copyright 2013 Snehasish Roy
 *
 * This file is part of CodeIt! ©. 
 * 
 * CodeIt! © is free software: you can
 * redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * CodeIt! © is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * CodeIt! ©. If not, see <http://www.gnu.org/licenses/>.
 */
package workingwithideone;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.ksoap.SoapPrimitive;
import org.ksoap.transport.HttpTransportSE;

/**
 * CodeIt.java Purpose: This class initiates the JFrame for the application also
 * allowing different panels to be set on the frame
 *
 * @author Snehasish Roy
 * @version 1.0 17/06/13
 */
public class CodeIt extends JFrame {

    private static final long serialVersionUID = 1L;
    MainPanel initialPanel;
    QueryInProgress query;
    SubmissionQuery submitQuery;
    Hashtable ret = new Hashtable();
    Integer Height = 1250;
    JPanel base;
    JScrollPane scroller;
    FileWriter writer;
    FileReader reader;

    public static void main(String[] args) {
        CodeIt ref = new CodeIt();
        ref.create();
    }

    /**
     * Initiates the JFrame
     */
    private void create() {
        boolean proxyRequired = false;
        String host = null;
        String port = null;
        String userName = null;
        String passWord = null;

        /*
         * Check the Configuration File.
         * If present retrieve the settings
         * else create it
         */
        String fileName = System.getProperty("user.home") + "/CodeItConfig.ini";
        try {
            reader = new FileReader(fileName);
            BufferedReader br = new BufferedReader(reader);
            String line = null;
            try {
                line = br.readLine();
                if (line.equals("false")) {
                    proxyRequired = false;
                } else {
                    proxyRequired = true;
                    line = br.readLine();
                    host = line; // host
                    line = br.readLine();
                    port = line; // port

                    System.out.println(host + " " + port);
                    System.getProperties().put("http.proxyHost", host);
                    System.getProperties().put("http.proxyPort", port);
                    if ((line = br.readLine()) == null) {
                        System.out.println("No user name or password provided");
                    } else {
                        userName = line;
                        System.getProperties().put("http.proxyUser", userName);
                        if ((line = br.readLine()) == null) {

                            System.out.println("no password provided");
                        } else {
                            passWord = line;
                            System.getProperties().put("http.proxyPassword", passWord);
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(CodeIt.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            try {
                // If FileNotFound create the file
                writer = new FileWriter(fileName);
                PrintWriter pw = new PrintWriter(writer);
                pw.println("false");
                writer.close();
                pw.close();
            } catch (IOException ex1) {
                Logger.getLogger(CodeIt.class.getName()).log(Level.SEVERE, null, ex1);
                JOptionPane.showMessageDialog(null, "Unable to save configuration"
                        + " file. You have to manually configure settings each time !",
                        "Error writing to file", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Set Look and feel to Nimbus
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CodeIt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        setSize(1365, 738);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CodeIt © ! Online Compiler for 65+ languages");

        Thread thread = null;

        /* Starts a thread that fetches list of programming languages in the background */
        if (proxyRequired) {
            query = new QueryInProgress(thread, this, host, port, userName, passWord);
        } else {
            query = new QueryInProgress(thread, this);
        }
        thread = new Thread(new FetchListOfLanguages(this));
        thread.start();
        query.setThread(thread);
        getContentPane().add(query);
        revalidate();
        repaint();

        setVisible(true);
    }

    /**
     * Removes the query panel and sets the MainPanel
     *
     * @param data Hashtable containing the list of programming languages
     */
    public void setMainPanel(Hashtable data) {
        getContentPane().remove(query);
        query = null;
        initialPanel = new MainPanel(this, data);
        initialPanel.setListItems(ret);
        getContentPane().add(initialPanel);
        revalidate();
        repaint();
    }

    /**
     * Removes the query panel and sets the output panel
     *
     * @param status Integer status code
     * @param data Hashtable containing the submission info
     * @param link Unique link of the submission
     * @param sourceCode SourceCode of the submission
     * @param input Input of the submission
     * @param language Integer language code
     * @param runnable set to true to execute the program
     * @param privateEnabled set to true to make the submission private
     */
    public void setOutputPanel(Integer status, Hashtable data, String link, String sourceCode, String input, Integer language,
            boolean runnable, boolean privateEnabled) {

        base = new JPanel();
        OutputPanel output1 = new OutputPanel(this, language, runnable, privateEnabled);

        output1.disableSrcCodeAndInput();
        output1.setPrivateStatus(privateEnabled);

        // Fetch all the submission info
        Integer memory = (Integer) data.get("memory");
        Double mem = memory / 1024.0;
        output1.setMemory(Double.toString(mem) + " MB");

        String langName = (String) data.get("langName");
        String langVersion = (String) data.get("langVersion");
        output1.setLanguage(langName + " " + langVersion);

        Double time = Double.valueOf(((SoapPrimitive) data.get("time")).toString());
        output1.setTime(time);

        if (data.get("output").equals("")) {
            output1.showOutput(false);
        } else {
            String output = (String) data.get("output");
            output1.setOutput(output);
        }

        if (data.get("cmpinfo").equals("")) {
            output1.showCmpInfo(false);
        } else {
            String cmpInfo = (String) data.get("cmpinfo");
            output1.setCmpInfo(cmpInfo);
        }

        if (data.get("stderr").equals("")) {
            output1.showStdErr(false);
        } else {
            String stderr = (String) data.get("stderr");
            output1.setStderr(stderr);
        }
        output1.setSrcCode(sourceCode);
        Integer signal = (Integer) data.get("signal");
        output1.interpretSignal(signal);
        //System.out.println(signal);

        output1.setInput(input);
        output1.setPaste("http://ideone.com/" + link);

        base.setLayout(new BoxLayout(base, BoxLayout.Y_AXIS));
        base.add(output1);
        output1.interpretStatus(status);


        scroller = new JScrollPane(base);
        scroller.getVerticalScrollBar().setUnitIncrement(15);
        scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        remove(submitQuery);
        submitQuery = null;
        getContentPane().add(scroller);
        revalidate();
        repaint();
    }

    /**
     * Removes the output panel and resets the MainPanel (New Code)
     */
    void displayNewCodeMenu() {
        remove(scroller);

        initialPanel.initialiseText("", "");
        getContentPane().add(initialPanel);
        revalidate();
        repaint();
    }

    /**
     * Remove the output panel and sets the MainPanel to the provided sourceCode
     * and input
     *
     * @param src Source Code to be set
     * @param input Input to be set
     */
    void displayClonedMainPanel(String src, String input) {
        remove(scroller);
        initialPanel.initialiseText(src, input);
        getContentPane().add(initialPanel);
        revalidate();
        repaint();
    }

    /**
     * Sets the query panel and starts a background thread
     *
     * @param user UserName for calling Ideone API
     * @param pass Password for calling Ideone API
     * @param sourceCode sourceCode of the submission
     * @param key Integer language code of the submission
     * @param input Input of the submission
     * @param enabled set to true to execute the submission
     * @param privateEnabled set to true to make the submission private
     */
    void displayStatus(String user, String pass, String sourceCode, Integer key, String input, boolean enabled, boolean privateEnabled) {
        Thread thread = null;
        //query = new QueryInProgress(thread, this);
        //query.setNotification("Status: Waiting for Compilation !");
        submitQuery = new SubmissionQuery(this);
        submitQuery.setNotification("Status: Waiting for Compilation !");
        remove(initialPanel);
        getContentPane().add(submitQuery);

        //Starting a background thread which calls createSubmission() method of Ideone API
        thread = new Thread(new CreateSubmission(false, user, pass, sourceCode, key, input, enabled, privateEnabled, this, new OutputPanel()));
        thread.start();
        revalidate();
        repaint();
    }

    /**
     * Adds a new Submission menu to the current output panel
     *
     * @param srcCode SourceCode of the submission
     * @param language Integer language code
     * @param runnable set to true to execute the program
     * @param privateEnabled set to true to make the submission private
     * @param lang String language (along with version)
     * @param paste Unique identifier of submission
     * @param newInputOrNot set to true if called from Upload a New Input label
     * else false
     */
    void setNewInput(String srcCode, Integer language, boolean runnable, boolean privateEnabled, String lang,
            String paste, final boolean newInputOrNot) {

        final OutputPanel newInput = new OutputPanel(this, language, runnable, privateEnabled);
        if (newInputOrNot) { //Its a new Input
            newInput.showSrcCode(false);
            newInput.enableInputEditor();
        } else {
            newInput.enableSrcCode();
        }

        newInput.showCmpInfo(false);
        newInput.showStdErr(false);
        newInput.showOutput(false);
        newInput.setLanguage(lang);
        newInput.setPaste(paste);
        newInput.setSrcCode(srcCode);
        newInput.setPrivateStatus(privateEnabled);
        newInput.enableResubmit(true);
        newInput.disableFurtherInput();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                base.add(newInput);

                //Revalidating and repainting the panel
                base.revalidate();
                base.repaint();
                scroller.revalidate();
                scroller.repaint();

                //To make the new Panel visible to user without manual scrolling
                //I have made an approximate increase :D
                int value = scroller.getVerticalScrollBar().getValue();
                if (newInputOrNot) {
                    scroller.getVerticalScrollBar().setValue(value + 800);
                } else {
                    scroller.getVerticalScrollBar().setValue(value + 1300);
                }
            }
        });

    }
}
