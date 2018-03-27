package org.afscme.enterprise.common.web;

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
import org.afscme.enterprise.util.TextUtil;

/**
 * @struts:form name="commentHistoryForm"
 */
public class CommentHistoryForm extends org.apache.struts.action.ActionForm {
    
    public static final String COMMENT_HISTORY_FOR_AFFILIATE       = "A";
    public static final String COMMENT_HISTORY_FOR_OFFICER_TITLES  = "OT";
    public static final String COMMENT_HISTORY_FOR_PERSON          = "P";
    public static final String COMMENT_HISTORY_FOR_MEMBER          = "M";
    public static final String COMMENT_HISTORY_FOR_AFFILIATE_STAFF = "AS";
    public static final String COMMENT_HISTORY_FOR_ORG_ASSOCIATE   = "OA";
    
    /**
     * One of the values defined in the constants above. Useful for determining
     * things like the table header and footer on the jsp.
     */
    private String commentHistoryFor;
    
    /**
     * Action to use for the return button back to the last screen. Must include
     * parameters needed to return to previous screen.
     */
    private String returnAction;
    
    /**
     * Pk of the Organization for which to retrieve the comments if needed.
     * 
     * Needed for Affiliate. Also needed for Aff Staff and Org Associate along 
     * with personPk.
     */
    private Integer orgPk;
    
    /**
     * Pk of the Person for which to retrieve the comments if needed. 
     * 
     * Needed for Person and Member. Also needed for Aff Staff and Org Associate 
     * along with orgPk.
     */
    private Integer personPk;
    
    /**
     * Collection of CommentData objects.
     */
    private Collection comments;
    
    /** Creates a new instance of CommentHistoryForm */
    public CommentHistoryForm() {
    }
    
    /** Getter for property commentHistoryFor.
     * @return Value of property commentHistoryFor.
     *
     */
    public String getCommentHistoryFor() {
        return commentHistoryFor;
    }
    
    /** Setter for property commentHistoryFor.
     * @param commentHistoryFor New value of property commentHistoryFor.
     *
     */
    public void setCommentHistoryFor(String commentHistoryFor) {
        if (TextUtil.isEmptyOrSpaces(commentHistoryFor)) {
            this.commentHistoryFor = null;
        } else {
            this.commentHistoryFor = commentHistoryFor;
        }
    }
    
    /** Getter for property returnAction.
     * @return Value of property returnAction.
     *
     */
    public String getReturnAction() {
        return returnAction;
    }
    
    /** Setter for property returnAction.
     * @param returnAction New value of property returnAction.
     *
     */
    public void setReturnAction(String returnAction) {
        if (TextUtil.isEmptyOrSpaces(returnAction)) {
            this.returnAction = null;
        } else {
            this.returnAction = returnAction;
        }
    }
    
    /** Getter for property comments.
     * @return Value of property comments.
     *
     */
    public Collection getComments() {
        return comments;
    }
    
    /** Setter for property comments.
     * @param comments New value of property comments.
     *
     */
    public void setComments(Collection comments) {
        this.comments = comments;
    }
    
    /** Getter for property orgPk.
     * @return Value of property orgPk.
     *
     */
    public Integer getOrgPk() {
        return orgPk;
    }
    
    /** Setter for property orgPk.
     * @param orgPk New value of property orgPk.
     *
     */
    public void setOrgPk(Integer orgPk) {
        if (orgPk == null || orgPk.intValue() == 0) {
            this.orgPk = null;
        } else {
            this.orgPk = orgPk;
        }
    }
    
    /** Getter for property personPk.
     * @return Value of property personPk.
     *
     */
    public Integer getPersonPk() {
        return personPk;
    }
    
    /** Setter for property personPk.
     * @param personPk New value of property personPk.
     *
     */
    public void setPersonPk(Integer personPk) {
        if (personPk == null || personPk.intValue() == 0) {
            this.personPk = null;
        } else {
            this.personPk = personPk;
        }
    }
    
}
