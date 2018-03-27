/*
 * ApplicationLog.java
 *
 * Created on November 4, 2003, 3:51 PM
 */

package org.afscme.enterprise.log;
import java.io.Serializable;

public class ApplicationLog implements Serializable {
    /**
     * Name of event
     */
    protected String event_name;
    
    /**
     * user performing the action while this event occured
     */
    protected String event_user_id;
    
    /**
     * data that needs to be recorded into the log
     */
    protected String event_data;
    
    
    
    /** Creates a new instance of ApplicationLog */
    public ApplicationLog() {
    }
    
    
    /** Getter for property EventName.
     * @return Value of property EventName.
     *
     */
    public String getEventName() {
        return event_name;
    }
    
    /** Setter for property EventName.
     * @param cards New value of property EventName.
     *
     */
    public void setEventName(String name) {
        this.event_name = name;
    }
    
    /** Getter for property EventUserId.
     * @return Value event_user_id of property EventUserId.
     *
     */
    public String getEventUserId() {
        return event_user_id;
    }
    
    /** Setter for property EventUserId.
     * @param event_user_id New value of property EventUserId.
     *
     */
    public void setEventUserId(String user) {
        this.event_user_id = user;
    }
    
    /** Getter for property EventData.
     * @return Value event_data of property EventData.
     *
     */
    public String getEventData() {
        return event_data;
    }
    
    /** Setter for property EventData.
     * @param event_data New value of property EventData.
     *
     */
    public void setEventData(String data) {
        this.event_data = data;
    }
}
