package org.afscme.enterprise.organization.web;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.afscme.enterprise.address.Address;
import org.afscme.enterprise.address.ZipCodeEntry;
import org.afscme.enterprise.codes.Codes.Country;
import org.afscme.enterprise.controller.UserSecurityData;
import org.afscme.enterprise.controller.web.AFSCMEAction;
import org.afscme.enterprise.organization.LocationData;
import org.afscme.enterprise.organization.OrgAddressRecord;
import org.afscme.enterprise.util.CollectionUtil;
import org.afscme.enterprise.util.TextUtil;


/**
 * Handles the submits from the 'Add/Edit Location' page.
 *
 * @struts:action   path="/saveLocation"
 *                  name="locationForm"
 *                  scope="request"
 *                  validate="false"
 *                  input="/Membership/LocationMaintenanceEdit.jsp"
 *
 * @struts:action-forward   name="LocationMaintenance"  path="/viewLocationInformation.action"
 * @struts:action-forward   name="ProcessReturnedMail"  path="/processReturnedMailSummary.action"
 */
public class SaveLocationAction extends AFSCMEAction {

    private static final Integer[] ERROR_CODES = {
        Address.ERROR_CITY_EMPTY,
        Address.ERROR_STATE_EMPTY,
        Address.ERROR_PROVINCE_EMPTY,
        Address.ERROR_ZIPCODE_EMPTY,
        Address.ERROR_ZIPCODE_INVALID,
        Address.ERROR_ZIPPLUS_INVALID,
        Address.ERROR_STATE_ZIP_MISMATCH,
        LocationData.ERROR_CITY_EMPTY,
        LocationData.ERROR_STATE_EMPTY,
        LocationData.ERROR_PROVINCE_EMPTY,
        LocationData.ERROR_ZIPCODE_EMPTY,
        LocationData.ERROR_ZIPCODE_INVALID,
        LocationData.ERROR_ZIPPLUS_INVALID,
        LocationData.ERROR_STATE_ZIP_MISMATCH,
        LocationData.ERROR_TITLE_EMPTY,
        LocationData.ERROR_COUNTRY_EMPTY,
        LocationData.ERROR_OFFICE_PHONE_NUMBER_EMPTY,
        LocationData.ERROR_OFFICE_PHONE_AREA_CODE_EMPTY,
        LocationData.ERROR_FAX_PHONE_AREA_CODE_EMPTY,
        LocationData.ERROR_FAX_PHONE_COUNTRY_CODE_EMPTY,
        LocationData.ERROR_FAX_PHONE_PHONE_NUMBER_EMPTY,
    };
    
    private static final String[] ERROR_FIELDS= {
        "regularLocationCity",
        "regularLocationState",
        "regularLocationProvince",
        "regularLocationZip",
        "regularLocationZip",
        "regularLocationZip4",
        "regularLocationZip",
        "shippingLocationCity",
        "shippingLocationState",
        "shippingLocationProvince",
        "shippingLocationZip",
        "shippingLocationZip",
        "shippingLocationZip4",
        "shippingLocationZip",
        "locationTitle",
        "regularLocationCountry",
        "officeLocationPhoneNumber",
        "officeLocationPhoneAreaCode",
        "faxLocationPhoneAreaCode",
        "faxLocationPhoneCountryCode",
        "faxLocationPhoneNumber",
    };
    
    private static final String[] ERROR_MESSAGES = {
        "error.address.city.empty",
        "error.address.state.empty",
        "error.address.province.empty",
        "error.address.zipCode.empty",
        "error.address.zipCode.invalid",
        "error.address.zipPlus.invalid",
        "error.address.zipCode.stateZipMismatch",
        "error.address.city.empty",
        "error.address.state.empty",
        "error.address.province.empty",
        "error.address.zipCode.empty",
        "error.address.zipCode.invalid",
        "error.address.zipPlus.invalid",
        "error.address.zipCode.stateZipMismatch",
        "error.location.title.required",
        "error.location.country.required",
        "error.location.officePhoneNumber.required",
        "error.location.officeAreaCode.required",
        "error.location.faxAreaCode.required",
        "error.location.faxCountryCode.required",
        "error.location.faxPhoneNumber.required",
    };
    
    private static final Integer[] ERROR_ADDRESS_CODES = {
        Address.ERROR_CITY_EMPTY,
        Address.ERROR_STATE_EMPTY,
        Address.ERROR_PROVINCE_EMPTY,
        Address.ERROR_ZIPCODE_EMPTY,
        Address.ERROR_ZIPCODE_INVALID,
        Address.ERROR_ZIPPLUS_INVALID,
        Address.ERROR_STATE_ZIP_MISMATCH,
    };

    private static final Integer[] ERROR_SHIPPING_ADDRESS_CODES = {
        LocationData.ERROR_CITY_EMPTY,
        LocationData.ERROR_STATE_EMPTY,
        LocationData.ERROR_PROVINCE_EMPTY,
        LocationData.ERROR_ZIPCODE_EMPTY,
        LocationData.ERROR_ZIPCODE_INVALID,
        LocationData.ERROR_ZIPPLUS_INVALID,
        LocationData.ERROR_STATE_ZIP_MISMATCH,
    };
    
    public ActionForward perform(ActionMapping mapping, ActionForm form,
                    HttpServletRequest request, HttpServletResponse response,
                    UserSecurityData usd)
    throws Exception {

        LocationForm locationForm = (LocationForm)form;

        String back = locationForm.getBack();
        
        //return to calling screen if cancel button is selected
        if (isCancelled(request))
            return mapping.findForward(back);
        
        Integer orgPk = locationForm.getOrgPK();

        //validate manually since form is shared with add and edit
        Set errors = null;
        Set shippingErrors = null;
        
        //validate the location form data (except for addresses)
        Set locationErrors = locationForm.validate();
        
        //validate the location regular address form data
        if (locationForm.hasRegularAddress()) {
            OrgAddressRecord regAddress = locationForm.getAddressFromRegularAddress();
            errors = s_systemAddress.validate((Address)regAddress);
        }
        
        //validate the location shipping address form data
        if (locationForm.hasShippingAddress()) {
            OrgAddressRecord shipAddress = locationForm.getAddressFromShippingAddress();
            shippingErrors = s_systemAddress.validate((Address)shipAddress);
            
            //translate the address errors to shipping errors to distinguish shipping and regular errors
            if (shippingErrors != null) {
                Iterator it = shippingErrors.iterator();
                while (it.hasNext()) {
                    if (errors == null)
                        errors = new TreeSet();
                    errors.add(CollectionUtil.transliterate(it.next(), ERROR_ADDRESS_CODES, ERROR_SHIPPING_ADDRESS_CODES));
                }
            }
        }
        
        //combine all of the validation errors together into one set
        if (errors != null) {
            errors.addAll(locationErrors);
        }    
        else {
            if (locationErrors != null) {
                errors = locationErrors;
            }
        }   
        
        //return to the Add Edit/page if validation errors
        if (errors != null && !errors.isEmpty()) {
            
            //reset read only data if returning to edit
            if (!(locationForm.isAdd())) {
                
                //set the record data information so the re-displayed form will have them (since read only)
                locationForm.setRecordData(s_maintainOrgLocations.getOrgLocation(locationForm.getPk()).getRecordData());
            }

            locationForm.setOrgPK(orgPk);
            locationForm.setIsAffiliatePk(s_maintainOrgLocations.isAffiliate(orgPk));
            
            List errorFields = CollectionUtil.transliterate(errors, ERROR_CODES, ERROR_FIELDS);
            List errorMessages = CollectionUtil.transliterate(errors, ERROR_CODES, ERROR_MESSAGES);
            return makeErrorForward(request, mapping, errorFields, errorMessages);
        }        

        //save location information
        Integer orgLocationPK = null;
        
        //update city, state and country if empty and valid zip code is entered for Reg Address
        ZipCodeEntry zipEntry = s_systemAddress.lookupZipCode(locationForm.getRegularLocationZip());
        if (zipEntry != null) {
            if (TextUtil.isEmpty(locationForm.getRegularLocationCity()))
                locationForm.setRegularLocationCity(zipEntry.getCity());
            if (TextUtil.isEmpty(locationForm.getRegularLocationState()))
                locationForm.setRegularLocationState(zipEntry.getState());
            if ((locationForm.getRegularLocationCountry() == null) || (locationForm.getRegularLocationCountry().intValue() == 0))
                locationForm.setRegularLocationCountry(Country.US);
        }

        //update city, state and country if empty and valid zip code is entered for Shipping Address
        zipEntry = s_systemAddress.lookupZipCode(locationForm.getShippingLocationZip());
        if (zipEntry != null) {
            if (TextUtil.isEmpty(locationForm.getShippingLocationCity()))
                locationForm.setShippingLocationCity(zipEntry.getCity());
            if (TextUtil.isEmpty(locationForm.getShippingLocationState()))
                locationForm.setShippingLocationState(zipEntry.getState());
            if ((locationForm.getShippingLocationCountry() == null) || (locationForm.getShippingLocationCountry().intValue() == 0))
                locationForm.setShippingLocationCountry(Country.US);
        }
        
        if (locationForm.isAdd()) {
            
            //add a new location
            orgLocationPK = s_maintainOrgLocations.addOrgLocation( locationForm.getLocationData(),
                                                                   orgPk,
                                                                   usd.getPersonPk());
        } else {
            
            //edit location information
            s_maintainOrgLocations.updateOrgLocation( locationForm.getLocationData(),
                                                      orgPk,
                                                      usd.getPersonPk());
        }

        // go to location maintenance or process return mail once everything is saved
        return mapping.findForward(back);        
    }
}
