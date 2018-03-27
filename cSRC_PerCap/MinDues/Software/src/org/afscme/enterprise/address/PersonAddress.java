package org.afscme.enterprise.address;

import java.sql.Timestamp;
import org.afscme.enterprise.util.TextUtil;

/**
 * Person a address data.
 */
public class PersonAddress extends Address
{
    /** Address Source is Affiliate Staff or Update File: 'S' */

    public static final char SOURCE_AFFILIATE_STAFF = 'S';

    /** Address Source is Owner: 'O' */
    public static final char SOURCE_OWNER = 'O';

    /** Address Source is AFSCME Staff: 'A' */
    public static final char SOURCE_AFSCME_STAFF = 'A';

    /** Address Source is NCOA Update: 'N' */
    public static final char SOURCE_NCOA_UPDATE = 'N';

    /**
     * primary key of a department common code
     */
    protected Integer department;

    /**
     * true iff this is the primary address for this department
     */
    protected boolean primary;

    /**
     * true iff the address is private to the department of the creator
     */
    protected boolean privateFg;

    /**
     * Source of the address.  Values are one of the SOURCE_XXX constants defined in this class
     */
    protected char source;

    /**
     * primary key of the affiliate that updated the address via affiliate file.  This value is null unless 'source' is 'U' (SOURCE_UPDATE_FILE)
     */
    protected Integer updateSource;

    /**
     * primary key pointer to an 'PersonAddressType' common code
     */
    protected Integer type;

    /**
     * true if this address has been determined to be 'bad' aka 'not mailable'
     */
    protected boolean bad;

    /**
     * Date the address was marked bad
     */
    protected Timestamp badDate;

    public boolean contentEquals(PersonAddress other) {
        return super.contentEquals(other) &&
        ((department == null && other.department == null) ||
        (department != null && department.equals(other.department))) &&
        privateFg == other.privateFg &&
        primary == other.primary &&
        source == other.source &&
        bad == other.bad &&
            
        TextUtil.equals(updateSource, other.updateSource);
    }

    public String toString() {
        return "PersonAddress[" +
        "primary="+primary+", "+
        "private="+privateFg+", "+
        "source="+source+", "+
        "updateSource="+updateSource+", "+
        "type="+type+", "+
        "bad="+bad+", "+
        "badDate="+badDate+"]"+
        super.toString();
    }

    /** Getter for property bad.
     * @return Value of property bad.
     *
     */
    public boolean isBad() {
        return bad;
    }

    /** Setter for property bad.
     * @param bad New value of property bad.
     *
     */
    public void setBad(boolean bad) {
        this.bad = bad;
    }

    /** Getter for property badDate.
     * @return Value of property badDate.
     *
     */
    public Timestamp getBadDate() {
        return badDate;
    }

    /** Setter for property badDate.
     * @param badDate New value of property badDate.
     *
     */
    public void setBadDate(Timestamp badDate) {
        this.badDate = badDate;
    }

    /** Getter for property department.
     * @return Value of property department.
     *
     */
    public Integer getDepartment() {
        return department;
    }

    /** Setter for property department.
     * @param department New value of property department.
     *
     */
    public void setDepartment(Integer department) {
        this.department = department;
    }

    /** Getter for property primary.
     * @return Value of property primary.
     *
     */
    public boolean isPrimary() {
        return primary;
    }

    /** Setter for property primary.
     * @param primary New value of property primary.
     *
     */
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    /** Getter for property privateFg.
     * @return Value of property privateFg.
     *
     */
    public boolean isPrivate() {
        return privateFg;
    }

    /** Setter for property privateFg.
     * @param privateFg New value of property privateFg.
     *
     */
    public void setPrivate(boolean privateFg) {
        this.privateFg = privateFg;
    }

    /**
     * @deprecated Use isPrivate instead
     */
    public boolean isPrivateFg() {
        return isPrivate();
    }

    /**
	 *
	 * @deprecated Use setPrivate instead.
     *
     */
    public void setPrivateFg(boolean privateFg) {
		setPrivate(privateFg);
    }

    /** Getter for property source.
     * @return Value of property source.
     *
     */
    public char getSource() {
        return source;
    }

    /** Setter for property source.
     * @param source New value of property source.
     *
     */
    public void setSource(char source) {
        this.source = source;
    }

    /**
	 *
	 * @deprecated use setSource(char) instead
     *
     */
    public void setSource(String str) {
		this.source = 'A';
    }


    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public Integer getType() {
        return type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /** Getter for property updateSource.
     * @return Value of property updateSource.
     *
     */
    public Integer getUpdateSource() {
        return updateSource;
    }

    /** Setter for property updateSource.
     * @param updateSource New value of property updateSource.
     *
     */
    public void setUpdateSource(Integer updateSource) {
        this.updateSource = updateSource;
    }



}
