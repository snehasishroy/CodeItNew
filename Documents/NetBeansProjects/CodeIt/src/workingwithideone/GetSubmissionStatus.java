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
 * GetSubmissionStatus.java Purpose: calls getSubmissionStatus and
 * getSubmissionDetails method of Ideone API
 *
 * @author Snehasish Roy
 * @version 1.0 17/06/2013
 */
class GetSubmissionStatus implements Runnable {

    String paste, input, sourceCode, namespace = "http://ideone.com/api/1/service";
    Hashtable data, data1;
    Integer language;
    boolean runnable, privateEnabled, newInput;
    private final CodeIt outer;
    OutputPanel outPanel;

    /**
     * Initialize the data members
     *
     * @param str Unique link of the submission
     * @param outer CodeIt reference
     * @param sourceCode Source code of the submission
     * @param input Input of the submission
     * @param language Integer Language code of the submission
     * @param runnable set to true to execute the program
     * @param privateEnabled set to true to make the submission private
     * @param outPanel Output Panel reference
     * @param newInput set to true if called from Upload with new Input label
     * else set to false if called from Edit Label
     */
    public GetSubmissionStatus(String str, final CodeIt outer, String sourceCode, String input, Integer language,
            boolean runnable, boolean privateEnabled, OutputPanel outPanel, boolean newInput) {
        super();
        this.outer = outer;
        this.paste = str;
        this.sourceCode = sourceCode;
        this.input = input;
        this.language = language;
        this.runnable = runnable;
        this.privateEnabled = privateEnabled;
        this.outPanel = outPanel;
        this.newInput = newInput;
    }

    /**
     * getSubmissionStatus() and getSubmissionDetails() will be called from here
     */
    @Override
    public void run() {
        while (true) {
            try {
                String methodName = "getSubmissionStatus";
                HttpTransportSE transport = new HttpTransportSE(namespace, methodName);
                SoapObject request = new SoapObject(namespace, methodName);

                //Adding properties to the request
                request.addProperty("user", Authentication.userName);
                request.addProperty("pass", Authentication.password);
                request.addProperty("link", paste);

                //Calling the getSubmissionStatus method
                SoapObject so = (SoapObject) transport.call(request);
                int count = so.getPropertyCount();
                data = new Hashtable();
                for (int i = 0; i < count; i++) {
                    SoapObject so2 = (SoapObject) so.getProperty(i);
                    String key = (String) so2.getProperty(0);
                    Object val = so2.getProperty(1);
                    //System.out.println(key + " " + val);

                    //Putting the returned values in a Hashtable
                    data.put(key, val);
                }
                String error = (String) data.get("error");
                if (!error.equals("OK")) {
                    //System.out.println("Error while getting Submission Status");
                } else {
                    Integer stat = (Integer) data.get("status");
                    Integer result = (Integer) data.get("result");

                    //Intepreting the status code
                    if (stat < 0) {
                        try {
                            if (!newInput) {
                                outer.submitQuery.setNotification("Status: Waiting for Compilation !");
                            } else {
                                outPanel.setStatus("Please Wait...");
                            }
                            data = null;

                            // retry after 2 secs
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CodeIt.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (stat == 0) {
                        if (!newInput) {
                            outer.submitQuery.setNotification("Status: Finished Execution. Fetching Details!");
                        } else {
                            outPanel.setStatus("Finished..");
                        }

                        // fetch submission status 
                        //start getSubmissionDetails and then display output panel and quit the thread
                        data = null;
                        data1 = new Hashtable();

                        methodName = "getSubmissionDetails";
                        transport = new HttpTransportSE(namespace, methodName);
                        request = new SoapObject(namespace, methodName);
                        request.addProperty("user", Authentication.userName);
                        request.addProperty("pass", Authentication.password);
                        request.addProperty("link", paste);
                        request.addProperty("withSource", false);
                        request.addProperty("withInput", false);
                        request.addProperty("withOutput", true);
                        request.addProperty("withStderr", true);
                        request.addProperty("withCmpinfo", true);

                        // calling the getSubmissionDetails method
                        SoapObject sop = (SoapObject) transport.call(request);
                        count = sop.getPropertyCount();
                        for (int i = 0; i < count; i++) {
                            SoapObject sop2 = (SoapObject) sop.getProperty(i);
                            String key = (String) sop2.getProperty(0);
                            Object val = sop2.getProperty(1);

                            //Putting all the submission info in a hashtable
                            data1.put(key, val);
                        }
                        String res = (String) data1.get("error");
                        if (!res.equals("OK")) {
                            //System.out.println("error occured while fetching submission details");
                        } else {
                            if (!newInput) {

                                //Set output panel
                                outer.setOutputPanel(result, data1, paste, sourceCode, input, language, runnable, privateEnabled);
                            } else {

                                // if new Input then set the info 
                                outPanel.setInfo(result, data1, paste);
                            }
                            break;
                        }
                    } else if (stat == 1) {
                        if (!newInput) {
                            outer.submitQuery.setNotification("Status: Being Compiled !");
                        } else {
                            outPanel.setStatus("Please Wait...");
                        }
                        data = null;
                        try {

                            // retry after 2 secs
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CodeIt.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (stat == 2) {
                        if (!newInput) {
                            outer.submitQuery.setNotification("Status: Being Executed !");
                        } else {
                            outPanel.setStatus("Please Wait...");
                        }
                        data = null;
                        try {

                            // retry after 2 sec                            
                            Thread.sleep(2000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(CodeIt.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            } catch (IOException ex) {
                if (!newInput) {

                    outer.submitQuery.setNotification("Unable to connect. Check your Internet Connection.");
                    outer.submitQuery.enableProgressBar(false);

                } else {
                    outPanel.setStatus("Unable to connect.");
                }
            }
        }
    }
}
