/**
 * Copyright 2013 Snehasish Roy
 *
 * This file is part of CodeIt! ©.
 *
 * CodeIt! © is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * QueryInProgress.java Purpose: Displays the query panel
 *
 * @author Snehasish Roy
 * @version 1.0 17/06/2013
 */
public class QueryInProgress extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;
    Thread thread;
    CodeIt ref;
    FileReader reader;

    QueryInProgress(Thread ref, CodeIt reference, String host, String port, String userName, String passWord) {
        initComponents();
        this.ref = reference;
        this.thread = ref;
        jCheckBox1.setSelected(true);
        jTextField1.setText(host);
        jTextField2.setText(port);
        jTextField3.setText(userName);
        jTextField4.setText(passWord);
    }

    /**
     * Enable and sets the text of the Notification Label
     */
    public void setNotification(String text) {
        jLabel1.setText(text);
    }

    /**
     * Disable the indeterminate mode of the JProgressBar
     */
    public void enableProgressBar(boolean val) {
        jProgressBar1.setIndeterminate(val);
    }

    /**
     * Initializes the GUI
     */
    public QueryInProgress(Thread ref, CodeIt reference) {
        initComponents();
        this.ref = reference;
        this.thread = ref;
        enableProxy(false);
    }

    public void setThread(Thread ref) {
        this.thread = ref;
    }
    //Auto generated by NetBeans IDE

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setLayout(new java.awt.GridBagLayout());

        jProgressBar1.setCursor(new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR));
        jProgressBar1.setIndeterminate(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 359;
        gridBagConstraints.ipady = 19;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(26, 57, 0, 0);
        add(jProgressBar1, gridBagConstraints);

        jLabel1.setFont(new java.awt.Font("SansSerif", 1, 18)); // NOI18N
        jLabel1.setText("Fetching list of available languages. Please wait :)");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(102, 93, 0, 2);
        add(jLabel1, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jCheckBox1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jCheckBox1.setText("Enable proxy");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel2.setText("Server:");

        jLabel3.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel3.setText("Port:");

        jLabel4.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel4.setText("UserName:");

        jLabel5.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jLabel5.setText("Password:");

        jTextField1.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jTextField2.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jTextField3.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jTextField4.setFont(new java.awt.Font("SansSerif", 0, 14)); // NOI18N

        jButton1.setFont(new java.awt.Font("SansSerif", 1, 14)); // NOI18N
        jButton1.setText("Retry !");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(78, 78, 78)
                        .addComponent(jCheckBox1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel4)
                        .addGap(7, 7, 7)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jLabel5)
                        .addGap(11, 11, 11)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(104, 104, 104)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(49, 49, 49)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(27, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jCheckBox1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel3))
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel4))
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addComponent(jButton1))
        );

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(28, 165, 11, 0);
        add(jPanel1, gridBagConstraints);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Enables/disables the proxy settings
     *
     * @param val Set to true to enable the proxy settings
     */
    public void enableProxy(boolean val) {
        jLabel2.setEnabled(val);
        jLabel3.setEnabled(val);
        jLabel4.setEnabled(val);
        jLabel5.setEnabled(val);
        jTextField1.setEnabled(val);
        jTextField2.setEnabled(val);
        jTextField3.setEnabled(val);
        jTextField4.setEnabled(val);
    }

    /**
     * Allows user to enable/disable proxy settings
     *
     * @param evt Not used
     */
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        if (jCheckBox1.isSelected()) {
            enableProxy(true);
        } else {
            enableProxy(false);
        }
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    /**
     * Retry connecting after configuring proxy settings
     *
     * @param evt Not used
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (jCheckBox1.isSelected() && !jTextField1.getText().equals("") && !jTextField2.getText().equals("")) {
            FileWriter writer = null;
            try {
                if (thread.isAlive()) {
                    thread.stop();
                }
                //System.out.println("Configuring proxy settings");
                System.getProperties().put("http.proxyHost", jTextField1.getText());
                System.getProperties().put("http.proxyPort", jTextField2.getText());
                System.getProperties().put("http.proxyUser", jTextField3.getText());
                System.getProperties().put("http.proxyPassword", jTextField4.getText());
                thread = new Thread(new FetchListOfLanguages(ref));
                thread.start();
                setNotification("Fetching list of available languages. Please wait :)");
                enableProgressBar(true);
                String fileName = System.getProperty("user.home") + "/CodeItConfig.ini";
                writer = new FileWriter(fileName);
                PrintWriter pw = new PrintWriter(writer);
                pw.println("true");
                if (jTextField1.getText() != null) {
                    pw.println(jTextField1.getText());
                }
                if (jTextField2.getText() != null) {
                    pw.print(jTextField2.getText());
                }
                if (jTextField3.getText() != null) {
                    pw.print("\n" + jTextField3.getText());
                }
                if (jTextField4.getText() != null) {
                    pw.print("\n" + jTextField4.getText());
                }
                pw.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(QueryInProgress.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (!jCheckBox1.isSelected()) {
            FileWriter writer = null;
            try {
                if (thread.isAlive()) {
                    thread.stop();
                }

                thread = new Thread(new FetchListOfLanguages(ref));
                thread.start();
                setNotification("Fetching list of available languages. Please wait :)");
                enableProgressBar(true);
                String fileName = System.getProperty("user.home") + "/CodeItConfig.ini";
                writer = new FileWriter(fileName);
                PrintWriter pw = new PrintWriter(writer);
                pw.print("false");

                pw.close();
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(QueryInProgress.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    // End of variables declaration//GEN-END:variables
}
