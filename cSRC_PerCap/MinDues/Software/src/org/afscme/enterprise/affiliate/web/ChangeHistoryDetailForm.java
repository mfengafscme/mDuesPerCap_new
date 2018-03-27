package org.afscme.enterprise.affiliate.web;

// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="changeHistoryDetailForm"
 */
public class ChangeHistoryDetailForm extends ActionForm {
    
    private Integer affPk;
    private Integer section;
    private String changedDate;
    private Collection changes;
    
    /** Creates a new instance of ChangeHistoryDetailForm */
    public ChangeHistoryDetailForm() {
        this.init();
    }
    
// Struts Methods...
    
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.init();
    }

// General Methods...
    
    protected void init() {
        this.affPk = null;
        this.section = null;
        this.changedDate = null;
        this.changes = null;
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("[");
        sb.append("affPk = ");
        sb.append(this.affPk);
        sb.append(", section = ");
        sb.append(this.section);
        sb.append(", changedDate = ");
        sb.append(this.changedDate);
        sb.append(", changes = ");
        sb.append(CollectionUtil.toString(this.changes));
        sb.append("]");
        return sb.toString().trim();
    }
    
// Getter and Setter Methods...
    
    /** Getter for property changedDate.
     * @return Value of property changedDate.
     *
     */
    public String getChangedDate() {
        return changedDate;
    }
    
    /** Setter for property changedDate.
     * @param changedDate New value of property changedDate.
     *
     */
    public void setChangedDate(String changedDate) {
        if (TextUtil.isEmptyOrSpaces(changedDate)) {
            this.changedDate = null;
        } else {
            this.changedDate = changedDate;
        }
    }
    
    /** Getter for property changes.
     * @return Value of property changes.
     *
     */
    public Collection getChanges() {
        return changes;
    }
    
    /** Setter for property changes.
     * @param changes New value of property changes.
     *
     */
    public void setChanges(Collection changes) {
        if (CollectionUtil.isEmpty(changes)) {
            this.changes = null;
        } else {
            this.changes = new ArrayList(changes);
        }
    }
    
    /** Getter for property section.
     * @return Value of property section.
     *
     */
    public Integer getSection() {
        return section;
    }
    
    /** Setter for property section.
     * @param section New value of property section.
     *
     */
    public void setSection(Integer section) {
        if (section == null || section.intValue() < 1) {
            this.section = null;
        } else {
            this.section = section;
        }
    }
    
    /** Getter for property affPk.
     * @return Value of property affPk.
     *
     */
    public Integer getAffPk() {
        return affPk;
    }
    
    /** Setter for property affPk.
     * @param affPk New value of property affPk.
     *
     */
    public void setAffPk(Integer affPk) {
        if (affPk == null || affPk.intValue() < 1) {
            this.affPk = null;
        } else {
            this.affPk = affPk;
        }
    }
    
}
