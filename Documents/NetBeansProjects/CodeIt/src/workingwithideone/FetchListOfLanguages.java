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

import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.ksoap.SoapObject;
import org.ksoap.transport.HttpTransportSE;

/**
 * FetchListOfLanguages.java Purpose: To fetch the list of languages currently
 * supported by Ideone API
 *
 * @author Snehasish Roy
 * @version 1.0 17/06/2013
 */
class FetchListOfLanguages implements Runnable {

    String namespace = "http://ideone.com/api/1/service";
    Hashtable data = new Hashtable();
    CodeIt ref;
    private final CodeIt outer;

    /**
     * Initialization of data member
     *
     * @param outer CodeIt reference
     */
    public FetchListOfLanguages(final CodeIt outer) {
        super();
        this.outer = outer;
    }

    /**
     * getLanguages() method will be called from here
     */
    @Override
    public void run() {
        try {
            String methodName = "getLanguages";
            HttpTransportSE transport = new HttpTransportSE(namespace, methodName);
            SoapObject request = new SoapObject(namespace, methodName);

            //Adding properties to request
            request.addProperty("user", Authentication.userName);
            request.addProperty("pass", Authentication.password);

            //Calling the method
            SoapObject so = (SoapObject) transport.call(request);
            int count = so.getPropertyCount();
            for (int i = 0; i < count; ++i) {
                SoapObject so2 = (SoapObject) so.getProperty(i);
                String key = (String) so2.getProperty(0);
                Object val = so2.getProperty(1);
                //System.out.println(key + " " + val);

                //Putting the returned associative array(pair of languages, id) into a Hashtable
                data.put(key, val);
            }

            so = (SoapObject) data.get("languages");
            count = so.getPropertyCount();
            for (int i = 0; i < count; ++i) {
                SoapObject so2 = (SoapObject) so.getProperty(i);
                Integer key = (Integer) so2.getProperty(0);
                String val = (String) so2.getProperty(1);
                //System.out.println(key + " " + val);

                //Putting the pairs of languages, id into another hashtable 
                outer.ret.put(key, val);
            }

            //Work finished... So Set the main panel
            outer.query.setNotification("Connected. Redirecting you now......");
            Thread.sleep(1000);
            outer.setMainPanel(outer.ret);
        } catch (Exception ex) {
            Logger.getLogger(FetchListOfLanguages.class.getName()).log(Level.SEVERE, null, ex);
            outer.query.setNotification("Unable to connect. Check your Internet Connection.");
            outer.query.enableProgressBar(false);
        }
    }
}
