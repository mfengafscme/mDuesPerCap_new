/*
 * ViewApplyUpdateMemberEditSummaryForm.java
 *
 * Created on August 1, 2003, 3:20 PM
 */

package org.afscme.enterprise.update.web;
import java.text.ParseException;
import java.sql.Timestamp;
import org.apache.struts.action.*;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import javax.servlet.http.HttpServletRequest;
import org.afscme.enterprise.util.TextUtil;
import org.afscme.enterprise.common.web.SearchForm;
import org.afscme.enterprise.codes.CodeData;
import org.apache.struts.upload.*;
import org.afscme.enterprise.util.TextUtil;
import java.util.Vector;
/**
 * @struts:form name="ViewApplyUpdateMemberReviewForm"
 */
public class ViewApplyUpdateMemberReviewForm extends ActionForm{
    
    private Vector update   = null;
    private int pk          = 0;
    
    /***************************setter methods************************************/
    
    public void setUpdate(Integer data){
        update.add( data);
    }
    public void setPk(int data){
        pk = data;
    }
    
    
    /************************getter methods*****************************************/
    public Vector getUpdate(){
        return update ;
    }
    public int getPk(){
        return pk;
    }
    
}
