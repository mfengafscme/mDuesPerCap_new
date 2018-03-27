package org.afscme.enterprise.cards;

import java.util.Set;
import java.io.OutputStream;

/**
 * Class that implements generation of the membership cards file
 */
public class CardFileGenerator {
    
    /**
     * Generates the cards data for the given affiliates and run type.
     *
     * @param runType common code pk for the type of run to generate generate
     * @param Set of affiliate primary key Integers indicating what affiliates to
     * generate cards for
     * @param stream The stream to write the cards file data to@param affiliates
     */
    public void generate(Integer runType, Set affiliates, OutputStream stream) {
        
    }
}
