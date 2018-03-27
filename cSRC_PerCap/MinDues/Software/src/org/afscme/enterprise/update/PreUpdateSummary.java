package org.afscme.enterprise.update;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.afscme.enterprise.affiliate.AffiliateIdentifier;
import org.apache.log4j.Logger;
/**
 * Base class for member, officer, and rebate pre update summaries.
 */
public abstract class PreUpdateSummary implements Serializable {
    
    /**
     * A map of Lists of ExceptionData objects.
     */
    // key -- affPk, value -- A list of ExceptionData. 
    protected Map exceptions;
    
    
    /*************************************************************************/
    
    /**
     * A map of affPK and affId.
     */
    // key -- affPk, value -- affId. 
    protected Map affIds;
    /**
     *A list of Exception data
     */
    protected java.util.ArrayList list;
    
    /** Getter for property exceptions.
     * @return Value of property exceptions.
     *
     */
    
    public Map getExceptions() {
        return exceptions;
    }    
    
    /** Setter for property exceptions.
     * @param exceptions New value of property exceptions.
     *
     */
    public void setExceptions(Map exceptions) {
        this.exceptions = exceptions;
    }
    
    public void addException(Integer key, ExceptionData value) {
        
        if (this.exceptions == null) {
            this.exceptions= new HashMap();
        }
        List list = (List)this.exceptions.get(key);
        if (list == null) {
            list = new ArrayList();
            
            this.exceptions.put(key, list);
        }
        
        
           list.add(value);
        
    }
    
    /************************************************************************/
    /*Added the getter and setter methods for affid map                    */
    /***********************************************************************/
    /** Getter for property affid map.
     * @return Value of property affid.
     *
     */
    public Map getAffId() {
        return affIds;
    }    
    
    /** Setter for property exceptions.
     * @param exceptions New value of property exceptions.
     *
     */
    public void setAffId(Map affIds) {
        this.affIds = affIds;
    }
    
    public void addAffId(Integer key, AffiliateIdentifier value) {
        if (this.affIds == null) {
            this.affIds= new HashMap();
        }
        
        List list = (List)this.affIds.get(key);
        if (list == null) {
            list = new ArrayList();
            
            this.affIds.put(key, list);
        }        
        list.add(value);        
    }
    /*********************************************************************************/
}
