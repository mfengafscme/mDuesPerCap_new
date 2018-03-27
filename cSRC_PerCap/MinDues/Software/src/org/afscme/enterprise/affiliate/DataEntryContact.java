package org.afscme.enterprise.affiliate;

import java.util.Collection;
import java.util.ArrayList;
import java.sql.Timestamp;

import org.afscme.enterprise.common.RecordData;
import org.afscme.enterprise.util.CollectionUtil;

/**
 * Represents WageIncForm data.
 */
public class DataEntryContact {
    private int wifFk;
    private String name;
    private String phone;
    private String email;

    public DataEntryContact() {
        this.wifFk = 0;
        this.name = null;
        this.phone = null;
        this.email = null;
    }

    /** Getter for property wifFk.
     * @return Value of property wifFk.
     *
     */
    public int getWifFk() {
        return wifFk;
    }    

    /** Setter for property wifFk.
     * @param wifFk New value of property wifFk.
     *
     */
    public void setWifFk(int wifFk) {
        this.wifFk = wifFk;
    }
    
    /** Getter for property name.
     * @return Value of property name.
     *
     */
    public java.lang.String getName() {
        return name;
    }
    
    /** Setter for property name.
     * @param name New value of property name.
     *
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }
    
    /** Getter for property phone.
     * @return Value of property phone.
     *
     */
    public java.lang.String getPhone() {
        return phone;
    }
    
    /** Setter for property phone.
     * @param phone New value of property phone.
     *
     */
    public void setPhone(java.lang.String phone) {
        this.phone = phone;
    }
    
    /** Getter for property email.
     * @return Value of property email.
     *
     */
    public java.lang.String getEmail() {
        return email;
    }
    
    /** Setter for property email.
     * @param email New value of property email.
     *
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }
    
}
