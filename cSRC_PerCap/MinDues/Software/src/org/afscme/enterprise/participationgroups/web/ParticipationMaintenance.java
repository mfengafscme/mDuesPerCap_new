package org.afscme.enterprise.participationgroups.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.afscme.enterprise.participationgroups.ParticipationDetailData;
import org.afscme.enterprise.participationgroups.ParticipationGroupData;
import org.afscme.enterprise.participationgroups.ParticipationOutcomeData;
import org.afscme.enterprise.participationgroups.ParticipationTypeData;


/** Holds the data on the Participation Group View screen
 */
public class ParticipationMaintenance
{
    /** Map of Lists of ParticipationTypeData objects, by group pk */
    protected Map typeListsByGroupPk;

    /** Map of Lists of ParticipationDetailData objects, by type pk */
    protected Map detailListsByTypePk;

    /** Map of Lists of ParticipationOutcomeData objects, by detail pk */
    protected Map outcomeListsByDetailPk;


    /** Participation Group of the data */
    protected Integer groupPk;
    protected String groupNm;


    /** Getter for property groupPk.
     */
    public Integer getGroup() {
        return this.groupPk;
    }

    /** Setter for property groupPk.
     * @param groupPk New value of property groupPk.
     */
    public void setGroup(Integer groupPk) {
        this.groupPk = groupPk;
    }

    /** Returns a list of type objects for a GroupPk
     * @param groupPk value for groupPk.
     */
    public List getGroupTypes(int groupPk) {
        return (List)typeListsByGroupPk.get(new Integer(groupPk));
    }

    /** Returns just of a list of groupPks in typeListsByGroupPk
     */
    public List getTypes() {
        LinkedList types = new LinkedList();
        types.addAll(typeListsByGroupPk.keySet());
        return types;
    }

    /** Setter for property typeListsByGroupPk.
     * @param types New value of property typeListsByGroupPk.
     */
    public void setTypes(List types) {
        typeListsByGroupPk = new HashMap();
        Iterator it = types.iterator();
        while (it.hasNext()) {
            ParticipationTypeData item = (ParticipationTypeData)it.next();
            Integer pk = item.getGroupPk();
            if (pk != null) {
                List typeList = (List)typeListsByGroupPk.get(pk);
                if (typeList == null) {
                    typeList = new LinkedList();
                    typeListsByGroupPk.put(pk, typeList);
                }
                typeList.add(item);
            } else {
                //types with no group can't be viewed on this page ;)
            }
        }
    }

    /** Returns a list of detail objects for a TypePk
     * @param typePk value for typePk.
     */
    public List getTypeDetails(int typePk) {
        return (List)detailListsByTypePk.get(new Integer(typePk));
    }

    /** Returns just of a list of typePks in detailListsByTypePk
     */
    public List getDetails() {
        LinkedList details = new LinkedList();
        details.addAll(detailListsByTypePk.keySet());
        return details;
    }

    /** Setter for property detailListsByTypePk.
     * @param details New value of property detailListsByTypePk.
     */
    public void setDetails(List details) {
        detailListsByTypePk = new HashMap();
        Iterator it = details.iterator();
        while (it.hasNext()) {
            ParticipationDetailData item = (ParticipationDetailData)it.next();
            Integer pk = item.getTypePk();
            if (pk != null) {
                List detailList = (List)detailListsByTypePk.get(pk);
                if (detailList == null) {
                    detailList = new LinkedList();
                    detailListsByTypePk.put(pk, detailList);
                }
                detailList.add(item);
            } else {
                //details with no type can't be viewed on this page ;)
            }
        }
    }

    /** Returns a list of outcome objects for a DetailPk
     * @param detailPk value for detailPk.
     */
    public List getDetailOutcomes(int detailPk) {
        return (List)outcomeListsByDetailPk.get(new Integer(detailPk));
    }

    /** Returns just of a list of detailPks in outcomeListsByDetailPk
     */
    public List getOutcomes() {
        LinkedList outcomes = new LinkedList();
        outcomes.addAll(outcomeListsByDetailPk.keySet());
        return outcomes;
    }

    /** Setter for property outcomeListsByDetailPk.
     * @param outcomes New value of property outcomeListsByDetailPk.
     */
    public void setOutcomes(List outcomes) {
        outcomeListsByDetailPk = new HashMap();
        Iterator it = outcomes.iterator();
        while (it.hasNext()) {
            ParticipationOutcomeData item = (ParticipationOutcomeData)it.next();
            Integer pk = item.getDetailPk();
            if (pk != null) {
                List outcomeList = (List)outcomeListsByDetailPk.get(pk);
                if (outcomeList == null) {
                    outcomeList = new LinkedList();
                    outcomeListsByDetailPk.put(pk, outcomeList);
                }
                outcomeList.add(item);
            } else {
                //outcomes with no detail can't be viewed on this page ;)
            }
        }
    }

    /** Getter for property groupNm.
     * @return Value of property groupNm.
     *
     */
    public java.lang.String getGroupNm() {
        return groupNm;
    }
    
    /** Setter for property groupNm.
     * @param groupNm New value of property groupNm.
     *
     */
    public void setGroupNm(java.lang.String groupNm) {
        this.groupNm = groupNm;
    }
    
}



