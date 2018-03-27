package org.afscme.enterprise.rebate;

/**
 * Contains contants to support the Political Rebate Component.
 */
public interface PRBConstants {
    // Political Rebate Process Level
    public static final int PRB_REQUEST = 1;
    public static final int PRB_APPLICATION = 2;
    public static final int PRB_ROSTER = 3;
    
    // Political Rebate form/action names
    public static final String PRB_ANNUAL_REBATE_INFORMATION_FORM = "politicalRebateAnnualRebateInformationEditForm";
    public static final String PRB_ANNUAL_REBATE_INFORMATION_ACTION = "/editPoliticalRebateAnnualRebateInformation.action";
    public static final String PRB_REQUEST_FORM = "politicalRebateRequestForm";
    public static final String PRB_REQUEST_ACTION = "/editPoliticalRebateRequest.action";
    public static final String PRB_APPLICATION_FORM = "politicalRebateApplicationForm";
    public static final String PRB_APPLICATION_ACTION = "/editPoliticalRebateApplication.action";
    
    // Political Rebate Statuses
    public static final String STATUS_APPROVED = "Approved";
    public static final String STATUS_DENIED = "Denied";
    public static final String STATUS_IN_PROGRESS = "In Progress";
    public static final String STATUS_CHECK_ISSUED = "Check Issued";
    public static final String STATUS_PRELIMINARY = "Preliminary";
    public static final String STATUS_FINAL = "Final";    
        
    // Misc Constants
    public static final int MAX_REQUEST_AFFILIATE_ALLOWED = 3;
    public static final String CONFIG_VARIABLE_APP_MAILED_DT = "PRBAppMailedDate";  

    // Error Codes
    public static final int ERROR_ADD_UNSUCCESSFUL = -1;
    public static final int ERROR_MAX_REQUEST_AFFILIATE_ALLOWED = -2;
    
}
