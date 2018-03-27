package org.afscme.enterprise.update.participation;

import java.util.Map;
import java.util.HashMap;
import org.afscme.enterprise.update.UpdateSummary;
import org.afscme.enterprise.util.CollectionUtil;


/**
 * Represents the data for update summary.
 */
public class ParticipationUpdateSummary extends UpdateSummary {


    /**
     * Map of ParticipationUpdateElement objects to be inserted, by personPk.
     * key = personPk; value = ParticipationUpdateElement
     */
    protected Map inserts;

    /**
     * Map of ParticipationUpdateElement objects to be updated, by personPk.
     * key = personPk; value = ParticipationUpdateElement
     */
    protected Map updates;


    public ParticipationUpdateSummary() {
        super();
        inserts = new HashMap();
        updates = new HashMap();
        exceptions = new HashMap();
    }

    public String toString() {
        return "ParticipationUpdateSummary[" +
                "updates=" + CollectionUtil.toString(updates) +
                ", inserts=" + CollectionUtil.toString(inserts) +
                ", exceptions=" + CollectionUtil.toString(exceptions) +
                "]"
        ;
    }

    /** Getter for property inserts.
     * @return Value of property inserts.
     *
     */
    public Map getInserts() {
        return inserts;
    }

    /** Setter for property inserts.
     * @param inserts New value of property inserts.
     *
     */
    public void setInserts(Map inserts) {
        this.inserts = inserts;
    }

    /** Getter for property updates.
     * @return Value of property updates.
     *
     */
    public Map getUpdates() {
        return updates;
    }

    /** Setter for property updates.
     * @param updates New value of property updates.
     *
     */
    public void setUpdates(Map updates) {
        this.updates = updates;
    }

}
