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

import java.io.IOException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ksoap.SoapObject;
import org.ksoap.transport.HttpTransportSE;

/**
 * CreateSubmission.java Purpose: Calls the createSubmission() method of Ideone
 * API
 *
 * @author Snehasish Roy
 * @version 1.0 17/06/2013
 */
class CreateSubmission implements Runnable {

    String namespace = "http://ideone.com/api/1/service", user, pass, sourceCode, input;
    Integer key;
    boolean runnable, privateEnabled, newInput;
    Hashtable data = new Hashtable();
    private final CodeIt outer;
    OutputPanel outPanel;

    /**
     * Initialize all the fields required
     *
     * @param newInput set to true if called from new input menu else false if
     * called from edit menu
     * @param user UserName for calling Ideone API
     * @param pass Password for calling Ideone API
     * @param sourceCode SourceCode of submission
     * @param key Integer language code
     * @param input Input of the submission
     * @param runnable set to true to execute the program
     * @param privateEnabled set to true to make the submission private
     * @param outer CodeIt reference
     * @param outPanel OutputPanel reference
     */
    public CreateSubmission(boolean newInput, String user, String pass, String sourceCode, Integer key, String input, boolean runnable, boolean privateEnabled, final CodeIt outer, OutputPanel outPanel) {
        super();
        this.outer = outer;
        this.user = user;
        this.pass = pass;
        this.key = key;
        this.sourceCode = sourceCode;
        this.input = input;
        this.runnable = runnable;
        this.privateEnabled = privateEnabled;
        this.newInput = newInput;
        this.outPanel = outPanel;
    }

    /**
     * createSubmission() method will be called from here
     */
    @Override
    public void run() {
        try {
            String methodName = "createSubmission";
            HttpTransportSE transport = new HttpTransportSE(namespace, methodName);
            SoapObject request = new SoapObject(namespace, methodName);

            //Adding all the parameters of request
            request.addProperty("user", user);
            request.addProperty("pass", pass);
            request.addProperty("sourceCode", sourceCode);
            request.addProperty("language", key);
            request.addProperty("input", input);
            request.addProperty("run", runnable);
            request.addProperty("private", privateEnabled);

            //Calling the method
            SoapObject so = (SoapObject) transport.call(request);
            int count = so.getPropertyCount();
            for (int i = 0; i < count; i++) {
                SoapObject so2 = (SoapObject) so.getProperty(i);
                String key = (String) so2.getProperty(0);
                Object val = so2.getProperty(1);

                //Putting the returned value in a Hashtable
                data.put(key, val);
            }

            String error = (String) data.get("error");

            //If Authentication error occurs
            if (!error.equals("OK")) {
                //System.out.println("Auth error while creating submission");
                if (!newInput) {
                    outer.submitQuery.setNotification("Status: Authentication Error !");
                }

            } else {
                String paste = (String) data.get("link");

                //Starting another thread for getting submission status of the paste
                Thread thread = new Thread(new GetSubmissionStatus(paste, outer, sourceCode, input, key, runnable, privateEnabled, outPanel, newInput));
                thread.start();
            }
        } catch (IOException ex) {
            Logger.getLogger(CodeIt.class.getName()).log(Level.SEVERE, null, ex);
            if (!newInput) {
                outer.submitQuery.setNotification("Unable to connect. Check your Internet Connection.");
                outer.submitQuery.enableProgressBar(false);

            } else {
                outPanel.setStatus("Unable to connect !");
            }
        }
    }
}
