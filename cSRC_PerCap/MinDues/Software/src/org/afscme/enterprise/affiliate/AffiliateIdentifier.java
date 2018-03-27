package org.afscme.enterprise.affiliate;

import org.afscme.enterprise.util.DelimitedStringBuffer;
import org.afscme.enterprise.util.TextUtil;

/**
 * Represents the result of a query for an individual affiliate identifier.  This
 * object is contained inside several other data objects that reference an
 * affiliate identifier.
 */
public class AffiliateIdentifier {

    public static String DEFAULT_ID_NUMBER = "";

    private Character type;
    private String local;
    private String state;
    private String subUnit;
    private String council;
    private Character code;
    private String administrativeLegislativeCouncil;

    public AffiliateIdentifier() {
        this.setType(null);
        this.setLocal(DEFAULT_ID_NUMBER);
        this.setState(null);
        this.setSubUnit(DEFAULT_ID_NUMBER);
        this.setCouncil(DEFAULT_ID_NUMBER);
        this.setCode(null);
        this.setAdministrativeLegislativeCouncil(null);
    }

    public AffiliateIdentifier(Character type, String local, String state,
                               String subUnit, String council) {
        this();
        this.setType(type);
        this.setLocal(local);
        this.setState(state);
        this.setSubUnit(subUnit);
        this.setCouncil(council);
    }

    /**
     * @deprecated  use the AffiliateIdentifier(String type, String local,
     *              String state, String subUnit, String council, Character code,
     *              String administrativeLegislativeCouncil) instead.
     */
    public AffiliateIdentifier(Character type, String local, String state,
                               String subUnit, String council, Character code) {
        this(type,local, state, subUnit, council);
        this.setCode(code);
    }

    public AffiliateIdentifier(Character type, String local, String state,
                               String subUnit, String council, Character code,
                               String administrativeLegislativeCouncil) {
        this(type, local, state, subUnit, council);
        this.setCode(code);
        this.setAdministrativeLegislativeCouncil(administrativeLegislativeCouncil);
    }

// General Methods...

    public String toString(){
        StringBuffer buf = new StringBuffer("[");
        buf.append("type = ");
        buf.append(this.type);
        buf.append(", local = ");
        buf.append(this.local);
        buf.append(", state = ");
        buf.append(this.state);
        buf.append(", subUnit = ");
        buf.append(this.subUnit);
        buf.append(", council = ");
        buf.append(this.council);
        buf.append(", code = ");
        buf.append(this.code);
        buf.append(", administrativeLegislativeCouncil = ");
        buf.append(this.administrativeLegislativeCouncil);
        buf.append("]");
        return buf.toString().trim();
    }

    public boolean equals(Object id) {
        if (id == null || !(id instanceof AffiliateIdentifier)) {
            return false;
        }
        return  TextUtil.equals(this.code, ((AffiliateIdentifier)id).getCode()) &&
                TextUtil.equals(this.council, ((AffiliateIdentifier)id).getCouncil()) &&
                TextUtil.equals(this.local, ((AffiliateIdentifier)id).getLocal()) &&
                TextUtil.equals(this.state, ((AffiliateIdentifier)id).getState()) &&
                TextUtil.equals(this.subUnit, ((AffiliateIdentifier)id).getSubUnit()) &&
                TextUtil.equals(this.type, ((AffiliateIdentifier)id).getType())
        ;
    }

    private int getHashCode(Object o) {
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }

    public int hashCode() {
        return  getHashCode(type) + getHashCode(local) + getHashCode(state) +
                getHashCode(subUnit) + getHashCode(council) + getHashCode(code)
        ;
    }

    public String toDisplayString() {
        return type + " " + local + " " +  state + " " + subUnit + " " + council;
    }

// Getter and Setter Methods...

    /** Getter for property council.
     * @return Value of property council.
     *
     */
    public String getCouncil() {
        return council;
    }

    /** Setter for property council.1
     * @param council New value of property council.
     *
     */
    public void setCouncil(String council) {
        setCouncil(council, false);
    }

    /** Setter for property council.
     * @param council New value of property council.
     * @param asIs Indicates whether any formatting is to be performed on the council property council.
     *
     */
    public void setCouncil(String council, boolean asIs) {
        if(!asIs) {
            if (council != null && TextUtil.isAllZeros(council)) {
                this.council = DEFAULT_ID_NUMBER;
            } else {
                this.council = TextUtil.trimLeading(council, '0');
            }
        } else {
            this.council = council;
        }
    }

    /** Getter for property local.
     * @return Value of property local.
     *
     */
    public String getLocal() {
        return local;
    }

    /** Setter for property local.
     * @param local New value of property local.
     *
     */
    public void setLocal(String local) {
        if (local != null && TextUtil.isAllZeros(local)) {
            this.local = DEFAULT_ID_NUMBER;
        } else {
            this.local = TextUtil.trimLeading(local, '0');
        }
    }

    /** Getter for property state.
     * @return Value of property state.
     *
     */
    public String getState() {
        return state;
    }

    /** Setter for property state.
     * @param state New value of property state.
     *
     */
    public void setState(String state) {
        if (TextUtil.isEmptyOrSpaces(state)) {
            this.state = null;
        } else {
            this.state = state;
        }
    }

    /** Getter for property subUnit.
     * @return Value of property subUnit.
     *
     */
    public String getSubUnit() {
        return subUnit;
    }

    /** Setter for property subUnit.
     * @param subUnit New value of property subUnit.
     *
     */
    public void setSubUnit(String subUnit) {
        if (subUnit != null && TextUtil.isAllZeros(subUnit)) {
            this.subUnit = DEFAULT_ID_NUMBER;
        } else {
			            //System.out.println("************ = " + subUnit );
            this.subUnit = TextUtil.trimLeading(subUnit, '0');
        }
    }

    /** Getter for property code.
     * @return Value of property code.
     *
     */
    public Character getCode() {
        return code;
    }

    /** Setter for property code.
     * @param code New value of property code.
     *
     */
    public void setCode(Character code) {
        if (TextUtil.isEmptyOrSpaces(code)) {
            this.code = null;
        } else {
            this.code = code;
        }
    }

    /** Getter for property type.
     * @return Value of property type.
     *
     */
    public Character getType() {
        return type;
    }

    /** Setter for property type.
     * @param type New value of property type.
     *
     */
    public void setType(Character type) {
        if (TextUtil.isEmptyOrSpaces(type)) {
            this.type = null;
        } else {
            this.type = type;
        }
    }

    /** Getter for property administrativeLegislativeCouncil.
     * @return Value of property administrativeLegislativeCouncil.
     *
     */
    public String getAdministrativeLegislativeCouncil() {
        return administrativeLegislativeCouncil;
    }

    /** Setter for property administrativeLegislativeCouncil.
     * @param administrativeLegislativeCouncil New value of property administrativeLegislativeCouncil.
     *
     */
    public void setAdministrativeLegislativeCouncil(String administrativeLegislativeCouncil) {
        if (administrativeLegislativeCouncil != null &&
            TextUtil.isAllZeros(administrativeLegislativeCouncil)
        ) {
            this.administrativeLegislativeCouncil = DEFAULT_ID_NUMBER;
        } else {
            this.administrativeLegislativeCouncil = TextUtil.trimLeading(administrativeLegislativeCouncil, '0');
        }
    }

}
