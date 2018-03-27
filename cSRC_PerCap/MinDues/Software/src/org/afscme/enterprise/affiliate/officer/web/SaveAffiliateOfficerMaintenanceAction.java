package org.afscme.enterprise.affiliate.officer.web;

// Struts imports
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

// Java imports
import java.util.*;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Calendar;
import java.sql.Timestamp;

// AFSCME imports
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.affiliate.officer.AffiliateOfficerMaintenance;
import org.afscme.enterprise.util.DateUtil;
import org.afscme.enterprise.affiliate.officer.ReplaceOfficerResults;
import org.afscme.enterprise.affiliate.officer.ReplaceOfficerCriteria;
import org.afscme.enterprise.codes.Codes.ActivityType;

/**
 * @struts:action   path="/saveAffiliateOfficerMaintenance"
 *                  name="affiliateOfficerMaintenanceForm"
 *                  validate="false"
 *                  scope="session"
 *                  input="/Membership/AffiliateOfficerMaintenanceEdit.jsp"
 *
 * @struts:action-forward   name="View"  path="/viewAffiliateOfficerMaintenance.action"
 */
public class SaveAffiliateOfficerMaintenanceAction extends AFSCMEAction {
    
    private static final String STEWARD = "Steward";
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response,
                                UserSecurityData usd) throws Exception {
        
        AffiliateOfficerMaintenanceForm aomf = (AffiliateOfficerMaintenanceForm)form;
        
        // Make form validations
        ActionErrors errors = aomf.validate(mapping, request);
        if (!errors.isEmpty()) {
            saveErrors(request, errors);                        
            return mapping.getInputForward();
        }                            
        
        Integer currentAff = getCurrentAffiliatePk(request);
        Map officerList = aomf.getOfficerList();
        Iterator mIt = officerList.entrySet().iterator();
        
        Timestamp ts;
        int renewCount = 0;
        int vacateCount = 0;
        int replaceCount = 0;
        
        // First iteration through, just checking for replace data validation errors
        while (mIt.hasNext()) {
            
            Entry entry = (Entry)mIt.next();
            Integer fieldPk = (Integer)entry.getKey();
            AffiliateOfficerMaintenance aom = (AffiliateOfficerMaintenance)entry.getValue();
            if (aom.getOfficerAction() != null) {
                if (aom.getOfficerAction().equals("v")) {
                    
                    // vacate the office
                    s_maintainAffiliateOfficers.setVacantOfficer(currentAff, aom.getSurrKey(), usd.getPersonPk());                                   
                    vacateCount++;
                    aom.setOfficerPersonPk(null);
                    aom.setFirstName(null);
                    aom.setMiddleName(null);
                    aom.setLastName(null);
                    aom.setSuffix(null);
                    aom.setSteward(Boolean.FALSE);
                    aom.setAi(null);
                    aom.setOfficerAction(null);
                    aom.setExpirationYear(null);
                    aom.setExpirationMonth(null);
                    continue;
                } 
                if (aom.getOfficerAction().equals("r")) {
                    
                    // call function that will update the expiration date to new date 
                    // if a person currently occupies the position
                    Integer newEndTerm = new Integer(aom.getLengthOfTerm().intValue() + aom.getEndTerm().intValue());                    
                   
                    // set up end dt in history table 
                    // create month value from code by subtracting difference in value, 78000
                    ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, newEndTerm.intValue(), false) ;   
                    s_maintainAffiliateOfficers.setRenewOfficerHistory(ts, aom.getSurrKey(), usd.getPersonPk());
                    renewCount++;
                    
                    //  Update Steward flag
                    s_maintainAffiliateOfficers.updateOfficerHistoryStewardFlag(aom.getSurrKey(), aom.getSteward().booleanValue(), usd.getPersonPk());                    
                    aom = s_maintainAffiliateOfficers.getSingleOfficerHistory(aom.getSurrKey());
                    aom.setOfficerAction(null);
                    officerList.put(fieldPk, aom);
                    continue;
                }
                if (aom.getOfficerAction().equals("p")) {                
                    
                    if (aom.getReplacePersonPk() != null && aom.getReplacePersonPk().intValue() > 0) {
                        
                        // If officer was selected on the Replace pop up screen, just make change
                        if (aom.getOfficerPersonPk() != null && aom.getOfficerPersonPk().intValue() > 0) {
                            s_maintainAffiliateOfficers.setVacantOfficer(currentAff, aom.getSurrKey(), usd.getPersonPk());
                            vacateCount++;
                            if (aom.getExpirationYear() != null)
                                ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, aom.getExpirationYear().intValue(), false);                            
                            else 
                                ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, aom.getEndTerm().intValue(), false);                            
                        } else {
                            ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, aom.getEndTerm().intValue(), false);
                            
                            // HLM: 1/21/2004
                            // One Person cannot hold more than one position in a given Affiliate except Steward. 
                            // They can, however, hold positions at different levels within the Affiliate's Hierarchy.
                            if (aom.getOfficerTitle() != null && !aom.getOfficerTitle().equalsIgnoreCase(STEWARD)) {                                
                                if (!s_maintainAffiliateOfficers.isOfficerEligibleToHoldPosition(aom.getReplacePersonPk(), currentAff)) {
                                    errors.add("officerData(" + fieldPk.intValue() + ").officerPersonPk" , new ActionError("error.affiliate.maintenance.multipleposition"));   
                                    continue;
                                }
                            }                            
                        }
                        
                        aom.setSurrKey(s_maintainAffiliateOfficers.insertOfficerHistory( aom.getReplacePersonPk(),
                                                       aom.getOfficeGroupPk(), currentAff,
                                                       aom.getAfscmeOfficePk(), aom.getReplaceAffPk(), ts,
                                                       usd.getPersonPk(), aom.getSteward().booleanValue()));
                        replaceCount++;
                        aom = s_maintainAffiliateOfficers.getSingleOfficerHistory(aom.getSurrKey());                        
                        aom.setOfficerAction(null);
                        officerList.put(fieldPk, aom);
                        continue;
                    } else {
                        ReplaceOfficerCriteria oc = new ReplaceOfficerCriteria();                

                        oc.setFirstName(aom.getFirstName());
                        oc.setMiddleName(aom.getMiddleName());
                        oc.setLastName(aom.getLastName());
                        oc.setSuffix(aom.getSuffix());
                        oc.setAffPk(currentAff);
                        oc.setElected(aom.getElectedOfficerFg());
                        
                        // get collection of matches to name entered on edit screen
                        Collection possibleOfficers = s_maintainAffiliateOfficers.maintainOfficerSearch(oc);                                                                           

                        log.debug ("SIZE = " + possibleOfficers.size());

                        if (possibleOfficers.size() == 0) {

                            // if none were found, return error stating name provided doesn't match any of
                            // candidate names.  Assigning to officerPersonPk for lack of any better form field
                            // to assign error to.                        
                            errors.add("officerData(" + fieldPk.intValue() + ").officerPersonPk" , new ActionError("error.affiliate.maintenance.nomatch"));   
                        }

                        if (possibleOfficers.size() > 1) {

                            // Times 2 plus 3 = relationship between the fieldPk in the map 
                            // and the row location on the .jsp
                            int iValue = fieldPk.intValue() * 2 + 3;

                            // if more than 1 is returned, generate error with link inside that will allow user
                            // to open up the replace officer results page and pick the person they want.
                            // Assigning to officerAction for lack of a better place.                       
                            errors.add("officerData(" + fieldPk.intValue() + ").officerAction" , 
                                       new ActionError("error.affiliate.maintenance.multiplematch", 
                                                       "<a href='javascript:showOfficerSearchResults(titlesTable.rows(" + iValue + ").cells(6).childNodes(0), titlesTable.rows(" + iValue + ").cells(7).childNodes(0), titlesTable.rows(" + iValue + ").cells(8).childNodes(0), titlesTable.rows(" + iValue + ").cells(9).childNodes(0), titlesTable.rows(" + iValue + ").cells(6).childNodes(4), titlesTable.rows(" + iValue + ").cells(6).childNodes(2), titlesTable.rows(" + iValue + ").cells(6).childNodes(6)); '>Click Here</a>"));   
                        }

                        // If 1 is returned, then go ahead and process the replace                                        
                        if (possibleOfficers.size() == 1) {
                            
                            Iterator kIt = possibleOfficers.iterator();
                            ReplaceOfficerResults ror = (ReplaceOfficerResults)kIt.next();
                            
                            // If officer is in place already, set their end date.
                            if (aom.getOfficerPersonPk() != null && aom.getOfficerPersonPk().intValue() > 0) {
                                s_maintainAffiliateOfficers.setVacantOfficer(currentAff, aom.getSurrKey(), usd.getPersonPk());
                                if (aom.getExpirationYear() != null)
                                    ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, aom.getExpirationYear().intValue(), false);
                                else
                                    ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, aom.getEndTerm().intValue(), false);
                            } else {
                                ts = DateUtil.getTimestamp(aom.getMonthOfElection().intValue() - 78000, aom.getEndTerm().intValue(), false);
                            }

                            // HLM: 1/21/2004
                            // One Person cannot hold more than one position in a given Affiliate except Steward. 
                            // They can, however, hold positions at different levels within the Affiliate's Hierarchy.
                            if (aom.getOfficerTitle() != null && !aom.getOfficerTitle().equalsIgnoreCase(STEWARD)) {                                
                                if (!s_maintainAffiliateOfficers.isOfficerEligibleToHoldPosition(ror.getPersonPk(), currentAff)) {
                                    errors.add("officerData(" + fieldPk.intValue() + ").officerPersonPk" , new ActionError("error.affiliate.maintenance.multipleposition"));   
                                    continue;
                                }
                            }
                                                      
                            // Add record to Officer History table for new officer.
                            aom.setSurrKey(s_maintainAffiliateOfficers.insertOfficerHistory(ror.getPersonPk(),                            
                                                           aom.getOfficeGroupPk(), currentAff,
                                                           aom.getAfscmeOfficePk(), ror.getAffPk(), ts,
                                                           usd.getPersonPk(), aom.getSteward().booleanValue()));
                            replaceCount++;                            
                            aom = s_maintainAffiliateOfficers.getSingleOfficerHistory(aom.getSurrKey());                        
                            aom.setOfficerAction(null);
                            officerList.put(fieldPk, aom);
                        }
                        continue;
                    }
                } 
            } else {            
                if (aom.getOfficerPersonPk() != null && aom.getOfficerPersonPk().intValue() > 0) {
                    
                    // update steward flag if that is all that was selected, for current officers
                    s_maintainAffiliateOfficers.updateOfficerHistoryStewardFlag(aom.getSurrKey(), aom.getSteward().booleanValue(), usd.getPersonPk());
                }
            }                               
        }                
                       
        if (renewCount > 0)
            s_maintainAffiliateOfficers.updateAffOffActivity(currentAff, ActivityType.U , renewCount);
        if (vacateCount > 0)
            s_maintainAffiliateOfficers.updateAffOffActivity(currentAff, ActivityType.D , vacateCount);
        if (replaceCount > 0)
            s_maintainAffiliateOfficers.updateAffOffActivity(currentAff, ActivityType.A , replaceCount);

        aomf.setOfficerList(officerList);
            
        if (!errors.isEmpty()) {
            errors.add(ActionErrors.GLOBAL_ERROR, 
                          new ActionError("error.affiliate.maintenance.notification"));
            saveErrors(request, errors);                        
            return mapping.getInputForward();
        }       
        
        //See the new values entered in the form       
        return mapping.findForward("View");
    }
}