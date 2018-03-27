package org.afscme.enterprise.common.web;

// Java imports
import javax.servlet.ServletRequest;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

// Struts Imports
import org.apache.struts.taglib.html.SelectTag;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

// Afscme imports
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * Like struts html:select but places an additional parameter, id, in the <select>
 */
public class MemberParticipationJSTag extends TagSupport  {
 
    /**
     * Retrieve the participation collection and parse through it
     * generating the javascript variables.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {       

        ServletRequest sr = pageContext.getRequest();
        Collection allParticipationOutcomes = (Collection)sr.getAttribute("participationCollection");
        
        if (!CollectionUtil.isEmpty(allParticipationOutcomes)) {
            // Create the javascript variables
            ResponseUtils.write(pageContext,"var arrItems1 = new Array();");
            ResponseUtils.write(pageContext,"var arrItemsGrp1 = new Array();\n");
            ResponseUtils.write(pageContext,"var arrItems2 = new Array();\n");
            ResponseUtils.write(pageContext,"var arrItemsGrp2 = new Array();");
            ResponseUtils.write(pageContext,"var arrItems3 = new Array();");
            ResponseUtils.write(pageContext,"var arrItemsGrp3 = new Array();");
            
            Iterator it = allParticipationOutcomes.iterator();
            while(it.hasNext()) {        
                ParticipationOutcomeData pod = (ParticipationOutcomeData)it.next();
                ResponseUtils.write(pageContext,"arrItems1[" + pod.getTypePk() + "] = \"" + pod.getTypeNm() + "\";");
                ResponseUtils.write(pageContext,"arrItemsGrp1[" + pod.getTypePk() + "] = \"" + pod.getGroupPk() + "\";");
                ResponseUtils.write(pageContext,"arrItems2[" + pod.getDetailPk() + "] = \"" + pod.getDetailNm() + "\";");                
                ResponseUtils.write(pageContext,"arrItemsGrp2[" + pod.getDetailPk() + "] = \"" + pod.getTypePk() + "\";");
                ResponseUtils.write(pageContext,"arrItems3[" + pod.getOutcomePk() + "] = \"" + pod.getOutcomeNm() + "\";");
                ResponseUtils.write(pageContext,"arrItemsGrp3[" + pod.getOutcomePk() + "] = \"" + pod.getDetailPk() + "\";");                
            }
        }
        
        return (SKIP_BODY);
    }
   
}
