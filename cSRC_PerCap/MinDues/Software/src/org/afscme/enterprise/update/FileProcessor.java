package org.afscme.enterprise.update;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import org.afscme.enterprise.update.filequeue.FileEntry;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Base class for classes which handle update processing for a specific entity.
 */
public abstract class FileProcessor extends DefaultHandler {
    
    /**
     * Map of affiliate primary key Integers, by AffiliateIdentifier.  (set by 
     * setAffiliate(), contains the affiliates that are children of the affiliate that 
     * is being updated).
     */
    // protected Map affiliatePrimaryKeys;
    
    protected FileEntry fileEntry = null;
    
    /**
     * Sets the file entry that will be processed
     * 
     * @param queuePk primary key of the file that will be processed
     */
    public void setFileEntry(FileEntry fileEntry) {
        this.fileEntry = fileEntry;
    }
    
    /**
     * Validates the file set with setQueue().  Returns 0 if valid, else the return code if error.
     */
    abstract public int validate();
    
    /**
     * Implemented by derived classes, generates the pre-update summary for a file.
     */
    abstract public PreUpdateSummary generatePreUpdateSummary();
    
    /**
     * Implemented by derived classes, applys the updates for a file.
     * @param affPks A list of affiliate pks that the user has selected to process.
     */
    abstract public void applyUpdate(List affPks, Integer queuePk, Integer userPk);
    
}
