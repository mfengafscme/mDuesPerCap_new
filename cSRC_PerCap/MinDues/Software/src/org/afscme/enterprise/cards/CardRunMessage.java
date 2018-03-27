package org.afscme.enterprise.cards;


/**
 * The JMS Message sent by CardsBean to itself to start a run for a particular
 * type.
 */
public class CardRunMessage {
    
    /**
     * Common code key from 'CardRunType'.CommonCodeKey
     */
    
    public Integer runType;
}
