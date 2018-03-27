package org.afscme.enterprise.common.web;

// Java imports
import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.Iterator;

// Struts imports
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

// afscme imports
import org.afscme.enterprise.participationgroups.ParticipationGroupData;
import org.afscme.enterprise.participationgroups.ParticipationDetailData;
import org.afscme.enterprise.participationgroups.ParticipationTypeData;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * Like struts html:select but places an additional parameter, id, in the <select>
 */
public class IdSelectTag extends SelectTag {
    
    /** Identifier for Javascript function */
    protected String id;
    protected String whichKind;

    /**
     * Retrieve the required property and expose it as a scripting variable.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {  
        ResponseUtils.write(pageContext, "<select size\"20\" id=\"" + id + "\" name=\""+property+"\"  onChange=\"" + getOnchange() + "\">");
        ResponseUtils.write(pageContext,"<option value=\"\"></option>");
        ServletRequest sr = pageContext.getRequest();
        Collection colToIterate = (Collection)sr.getAttribute(whichKind);
        if (!CollectionUtil.isEmpty(colToIterate)) {            
            Iterator it = colToIterate.iterator();
            String optionName = null;
            String optionValue= null;                
            while(it.hasNext()) {
                Object o = it.next();
                if (o instanceof ParticipationGroupData) {
                    ParticipationGroupData pdg = (ParticipationGroupData)o;
                    optionName = pdg.getName();
                    optionValue = pdg.getGroupPk().toString();
                } else {
                    if (o instanceof ParticipationTypeData) {
                        ParticipationTypeData ptg = (ParticipationTypeData)o;                            
                        optionName = ptg.getName();
                        optionValue = ptg.getTypePk().toString();                        
                    } else {                            
                        if (o instanceof ParticipationDetailData) {
                            ParticipationDetailData pdd = (ParticipationDetailData)o;                                
                            optionName = pdd.getName();
                            optionValue = pdd.getDetailPk().toString();                    
                        } else {                            
                            if (o instanceof ParticipationOutcomeData) {
                                ParticipationOutcomeData pod = (ParticipationOutcomeData)o;                                
                                optionName = pod.getOutcomeNm();
                                optionValue = pod.getOutcomePk().toString();
                            }
                        }
                    }
                }
                ResponseUtils.write(pageContext,"<option value=\"" + optionValue + "\" ");
                if (optionValue != null && optionValue.equals(value))
                    ResponseUtils.write(pageContext," selected ");
                ResponseUtils.write(pageContext,">" + optionName + "</option>");                    
            }            
        }
        return (SKIP_BODY);
    }

    public void release() {
        super.release();
        id  = null;
        whichKind = null;
    }
	
    /** Getter for property id.
     * @return Value of property id.
     *
     */
    public String getId() {
        return id;
    }
    
    /** Setter for property id.
     * @param id New value of property id.
     *
     */
    public void setId(String id) {
        this.id = id;
    }
    
    /** Getter for property whichKind.
     * @return Value of property whichKind.
     *
     */
    public String getWhichKind() {
        return whichKind;
    }
    
    /** Setter for property whichKind.
     * @param id New value of property whichKind.
     *
     */
    public void setWhichKind(String whichKind) {
        this.whichKind = whichKind;
    }        
    
    /** Setter for property value whe input is Int.
     * @return Value of property value.
     *
     */
    public void setValue(Integer value) {
        if (value != null) {
            this.value = value.toString();
        } else {
            this.value = null;
        }
    }    
    
}