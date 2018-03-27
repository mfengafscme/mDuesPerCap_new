
package org.afscme.enterprise.member.web;

/*
 * Supports Verify Person (Member functionality) and Verify Person (Member) results
 * Created on August 4, 2003
 */



// Struts imports
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Calendar;
import javax.servlet.http.HttpServletRequest;

// AFSCME imports
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.DBUtil;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.member.MemberData;
import org.afscme.enterprise.member.web.SearchMembersForm;

/**
 * @struts:form name="verifyMemberForm"
 */
public class VerifyMemberForm extends SearchMembersForm
{
    private String back;
    private String vduFlag;
    
    public VerifyMemberForm() {
    
        vduFlag = null;
        
    }
 
    /** Getter for property back.
     * @return Value of property back.
     *
     */
    public java.lang.String getBack() {
        return back;
    }
    
    /** Setter for property back.
     * @param back New value of property back.
     *
     */
    public void setBack(java.lang.String back) {
        this.back = back;
    }
    
    /** Getter for property vduFlag.
     * @return Value of property vduFlag.
     *
     */
    public java.lang.String getVduFlag() {
        return vduFlag;
    }
    
    /** Setter for property vduFlag.
     * @param vduFlag New value of property vduFlag.
     *
     */
    public void setVduFlag(java.lang.String vduFlag) {
        this.vduFlag = vduFlag;
    }
    
}
