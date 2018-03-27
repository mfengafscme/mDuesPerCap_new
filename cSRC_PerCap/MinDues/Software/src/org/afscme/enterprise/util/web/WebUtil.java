package org.afscme.enterprise.util.web;

import java.sql.Timestamp;
import java.text.ParseException;
import org.afscme.enterprise.util.TextUtil;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;


/**
 * Contains static methods for common web functions.
 */
public class WebUtil {
    
    /**
     * Adds an ActionError error message key to the ActionErros object if the trimmed field is less than min or greater
     * than max characters in length.
     * 
     * @param field Name of the field being checked.
     * @param value Value of the field being checked
     * @param min minimum field length, or -1 if no minimum
     * @param max maximum field length, or -1 if no maximum
     * @param errors ActionErrors error will be appended to.
     */
    public static final void checkFieldLength(String field, String value, int min, int max, ActionErrors errors ) {

        if (min > 0) {
            if (TextUtil.isEmpty(value))
                errors.add(field, new ActionError("error.field.required"));
            else if (value.length() < min)
                errors.add(field, new ActionError("error.field.length.tooShort", new Integer(min)));
        }
        
        if (max > 0 && value != null && value.length() > max)
            errors.add(field, new ActionError("error.field.length.exceeded", new Integer(max)));
    }

    /**
     * Checks if an integer parses
     * 
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final int checkRequiredInt(String value, ActionErrors errors ) {
        return checkRequiredInt(ActionErrors.GLOBAL_ERROR, value, errors);
    }
    
    /**
     * Checks if an integer parses
     * 
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final int checkOptionalInt(String value, ActionErrors errors ) {
        return checkOptionalInt(ActionErrors.GLOBAL_ERROR, value, errors);
    }
    
    
    /**
     * Checks if an integer parses
     * 
     * @param field Name of the field being checked.
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final int checkRequiredInt(String field, String value, ActionErrors errors ) {

		if (TextUtil.isEmpty(value)) {
			errors.add(field, new ActionError("error.field.required"));
			return 0;
		} else {
            return checkInt(field, value, errors);
        }
    }
    
    
    /**
     * Checks if an integer parses
     * 
     * @param field Name of the field being checked.
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final int checkOptionalInt(String field, String value, ActionErrors errors ) {

		if (TextUtil.isEmpty(value)) {
			return 0;
		} else {
            return checkInt(field, value, errors);
        }
    }
    
    /**
     * Checks if a date parses
     * 
     * @param field Name of the field being checked.
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final Timestamp checkRequiredDate(String field, String value, ActionErrors errors ) {

		if (TextUtil.isEmpty(value)) {
			errors.add(field, new ActionError("error.field.required"));
			return null;
		} else {
            return checkDate(field, value, errors);
        }
    }
    
    /**
     * Checks if a date parses
     * 
     * @param field Name of the field being checked.
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final Timestamp checkOptionalDate(String field, String value, ActionErrors errors ) {

        if (TextUtil.isEmpty(value))
            return null;
        else return checkDate(field, value, errors);
    }
    
    /**
     * Checks if a date parses
     * 
     * @param field Name of the field being checked.
     * @param value Value of the field being checked
     * @param errors ActionErrors error will be appended to.
     */
    public static final Timestamp checkOptionalDate(String value, ActionErrors errors ) {
        return checkOptionalDate(ActionErrors.GLOBAL_ERROR, value, errors);
    }
    
    private static final Timestamp checkDate(String field, String value, ActionErrors errors) {

        Timestamp result = null;

        try {
            result = TextUtil.parseDate(value);
        } catch (ParseException e) {
            errors.add(field, new ActionError("error.field.mustBeDate.generic"));
        }
        return result;
    }
    
    private static final int checkInt(String field, String value, ActionErrors errors) {

        int result = 0;

        try {
            result = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            errors.add(field, new ActionError("error.field.mustBeInt.generic"));
        }
        return result;
    }
    
    public static final void checkValidAffiliateIdentifier(
                                    String typeField, Character typeValue, 
                                    String localField, String localValue, 
                                    String stateField, String stateValue,
                                    String subUnitField, String subUnitValue, 
                                    String councilField, String councilValue,
                                    ActionErrors errors
    ) {
        if (TextUtil.isEmptyOrSpaces(typeValue)) {
            errors.add(typeField, new ActionError("error.field.required.generic", "Affiliate Identifier Type"));
        } else {
            switch (typeValue.charValue()) {
                // Check for valid status based on Affiliate Type.
                case 'U':
                    if (TextUtil.isEmptyOrSpaces(subUnitValue)) {
                        errors.add(subUnitField, new ActionError("error.affiliate.conditional.subUnit"));
                    } else if (!TextUtil.isAlphaNumeric(subUnitValue)) {
                        errors.add(subUnitField, new ActionError("error.field.mustBeAlphaNumeric.generic", "Affiliate Identifier Sub Unit"));
                    }
                    
                case 'S':
                case 'L':
                    if (TextUtil.isEmptyOrSpaces(localValue)) {
                        errors.add(localField, new ActionError("error.affiliate.conditional.local"));
                    } else if (!TextUtil.isInt(localValue)) {
                        errors.add(localField, new ActionError("error.field.mustBeInt.generic", "Affiliate Identifier Local/Sub Chapter"));
                    } 
                    /* if Local is unaffiliated, break there since no need to 
                     * check the Council Number field
                     */
                    if (TextUtil.isEmptyOrSpaces(councilValue)) {
                        break;
                    } // else drop into next case and test the isInt rule...
                case 'R':
                case 'C':
                    if (TextUtil.isEmptyOrSpaces(councilValue)) {
                        errors.add(councilField, new ActionError("error.affiliate.conditional.council"));
                    } else if (!TextUtil.isInt(councilValue)) {
                        errors.add(councilField, new ActionError("error.field.mustBeInt.generic", "Affiliate Identifier Council/Retiree Chapter"));
                    } 
                    break;
                default:
                    // should never happen...
                    break;
            }
        }
        if (TextUtil.isEmptyOrSpaces(stateValue)) {
            errors.add(stateField, new ActionError("error.field.required.generic", "Affiliate Identifier State"));
        }
    }
}
