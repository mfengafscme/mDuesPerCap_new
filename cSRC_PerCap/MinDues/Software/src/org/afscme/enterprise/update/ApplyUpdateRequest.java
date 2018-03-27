package org.afscme.enterprise.update;

import java.net.*;
import java.util.*;
import java.io.*;

/**
 * Creation date: (11/10/2003)
 * @author: Holly Maiwald
 */
public final class ApplyUpdateRequest {
     
    public static void main(java.lang.String[] args) {
        URL url;
        ObjectInputStream is;
        try {
            //calling the apply update servlet
            String uri = new String(args[0]);
            uri += "/applyUpdate";
            url = new URL(uri);

            // open input stream and read result message returned by the servlet
            is = new ObjectInputStream(url.openStream());
            String rs = (String) is.readObject();
            
            // print it out
            System.out.println("*****************************************************");
            System.out.println("Request:" + uri);
            System.out.println("Result:" + rs);
            System.out.println("*****************************************************");            
        }
        catch (Exception e) {
            System.out.println("Caught Exception while sending apply update request.");
            e.printStackTrace(System.err);
        }
    }
}