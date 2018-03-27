var infosourceJS = 0;

var MergedAffiliateTypeJS = 0;
var MergedAffiliateLocalJS = "";
var MergedAffiliateStateJS = 0;
var MergedAffiliateSubunitJS = "";
var MergedAffiliateCouncilJS = "";

var SplitAffiliateTypeJS = 0;
var SplitAffiliateLocalJS = "";
var SplitAffiliateStateJS = 0;
var SplitAffiliateSubunitJS = "";
var SplitAffiliateCouncilJS = "";

var NewAffiliateTypeJS = 0;
var NewAffiliateLocalJS = "";
var NewAffiliateStateJS = 0;
var NewAffiliateSubunitJS = "";
var NewAffiliateCouncilJS = "";

var StatusValueJS = "";

function initializeChangeForm() {
    var form = document.forms['massChangeForm'];
    var statusChangeField = null;
    for (var i = 0; i < form.elements.length; i++) {
        var field = form.elements[i];
        if (field.name == "massChangeSelect") {
            if (field.value == "1" && field.checked) {
                statusChangeField = field;
                setFieldsForStatusChange(form);
            }
            if (field.value == "2" && field.checked) {
                resetNewAffiliateJSvars(form);
                infosourceJS = form.infoSourceNew.selectedIndex;
                setFieldsForOtherChanges(form);
            }
        }
        if (field.name == "statusChangeSelect" && statusChangeField != null) {
            if (field.value == "1" && field.checked) {
                resetMergedAffiliateJSvars(form);
                setFieldsForMergedStatus(form);
            }
            if (field.value == "2" && field.checked) {
                resetSplitAffiliateJSvars(form);
                setFieldsForSplitStatus(form);
            }
            if (field.value == "3" && field.checked) {
                setFieldsForDeactivatedStatus(form);
            }
        }
    }
}

function resetMergedAffiliateJSvars(form) {
    MergedAffiliateTypeJS = form.elements['mergedAffiliate.type'].selectedIndex;
    MergedAffiliateLocalJS = form.elements['mergedAffiliate.local'].value;
    MergedAffiliateStateJS = form.elements['mergedAffiliate.state'].selectedIndex;
    MergedAffiliateSubunitJS = form.elements['mergedAffiliate.subUnit'].value;
    MergedAffiliateCouncilJS = form.elements['mergedAffiliate.council'].value;
}

function resetSplitAffiliateJSvars(form) {
    SplitAffiliateTypeJS = form.elements['splitAffiliate.type'].selectedIndex;
    SplitAffiliateLocalJS = form.elements['splitAffiliate.local'].value;
    SplitAffiliateStateJS = form.elements['splitAffiliate.state'].selectedIndex;
    SplitAffiliateSubunitJS = form.elements['splitAffiliate.subUnit'].value;
    SplitAffiliateCouncilJS = form.elements['splitAffiliate.council'].value;
}

function resetNewAffiliateJSvars(form) {
    NewAffiliateTypeJS = form.elements['newAffiliate.type'].selectedIndex;
    NewAffiliateLocalJS = form.elements['newAffiliate.local'].value;
    NewAffiliateStateJS = form.elements['newAffiliate.state'].selectedIndex;
    NewAffiliateSubunitJS = form.elements['newAffiliate.subUnit'].value;
    NewAffiliateCouncilJS = form.elements['newAffiliate.council'].value;
}

function setFieldsForStatusChange(form) {
    disableFieldsForOtherChanges(form);
    enableRadioSet(form, 'statusChangeSelect');
    if (StatusValueJS == "Merged") {
        setFieldsForMergedStatus(form);
    } else if (StatusValueJS == "Split") {
        setFieldsForSplitStatus(form);
    } else if (StatusValueJS == "Deactivated") {
        setFieldsForDeactivatedStatus(form);
    }
    form.NewStatus.value = StatusValueJS;
}

function disableFieldsForStatusChange(form) {
    disableRadioSet(form, 'statusChangeSelect');
    disableSplitAffiliateIdentifierFields(form);
    disableMergedAffiliateIdentifierFields(form);
    form.NewStatus.value = "";
}

function setFieldsForOtherChanges(form) {
    disableFieldsForStatusChange(form);
    form.infoSourceFg.disabled = false;
    form.mbrCardBypassFg.disabled = false;
    form.peMailBypassFg.disabled = false;
    form.mbrRenewalFg.disabled = false;
    form.newAffiliateFg.disabled = false;
    setFieldsForInfoSource(form);
    setFieldsForNoMbrCards(form);
    setFieldsForNoPEMail(form);
    setFieldsForMemberRenewal(form);
    setFieldsForNewAffiliateIdentifier(form);
}

function disableFieldsForOtherChanges(form) {
    form.infoSourceFg.disabled = true;
    form.mbrCardBypassFg.disabled = true;
    form.peMailBypassFg.disabled = true;
    form.mbrRenewalFg.disabled = true;
    form.newAffiliateFg.disabled = true;
    setFieldsForInfoSource(form);
    setFieldsForNoMbrCards(form);
    setFieldsForNoPEMail(form);
    setFieldsForMemberRenewal(form);
    setFieldsForNewAffiliateIdentifier(form);
}

function setFieldsForNewAffiliateIdentifier(form) {
    form.elements['newAffiliate.type'].disabled = !form.newAffiliateFg.checked || form.newAffiliateFg.disabled;
    form.elements['newAffiliate.local'].disabled = !form.newAffiliateFg.checked || form.newAffiliateFg.disabled;
    form.elements['newAffiliate.state'].disabled = !form.newAffiliateFg.checked || form.newAffiliateFg.disabled;
    form.elements['newAffiliate.subUnit'].disabled = !form.newAffiliateFg.checked || form.newAffiliateFg.disabled;
    form.elements['newAffiliate.council'].disabled = !form.newAffiliateFg.checked || form.newAffiliateFg.disabled;

    if (form.newAffiliateFg.checked && !form.newAffiliateFg.disabled) {
        form.elements['newAffiliate.type'].selectedIndex = NewAffiliateTypeJS;
        form.elements['newAffiliate.local'].value = NewAffiliateLocalJS;
        form.elements['newAffiliate.state'].selectedIndex = NewAffiliateStateJS;
        form.elements['newAffiliate.subUnit'].value = NewAffiliateSubunitJS;
        form.elements['newAffiliate.council'].value = NewAffiliateCouncilJS;

        form.elements['newAffiliate.type'].focus();
        lockNewAffiliateID(form, form.elements['newAffiliate.type'])
    } else {
        form.elements['newAffiliate.type'].selectedIndex = 0;
        form.elements['newAffiliate.local'].value = form.elements['newAffiliate.local'].defaultValue;
        form.elements['newAffiliate.state'].selectedIndex = 0;
        form.elements['newAffiliate.subUnit'].value = form.elements['newAffiliate.subUnit'].defaultValue;
        form.elements['newAffiliate.council'].value = form.elements['newAffiliate.council'].defaultValue;			
    }
}

function setFieldsForInfoSource(form) {
    form.infoSourceNew.disabled = !form.infoSourceFg.checked || form.infoSourceFg.disabled;
    if (form.infoSourceFg.checked && !form.infoSourceFg.disabled) {
        form.infoSourceNew.selectedIndex = infosourceJS;
        form.infoSourceNew.focus();
    } else {
        form.infoSourceNew.value = form.infoSourceNew.defaultValue;
    }
}

function setFieldsForNoMbrCards(form) {
    form.mbrCardBypassFgNew.disabled = !form.mbrCardBypassFg.checked || form.mbrCardBypassFg.disabled;
}

function setFieldsForNoPEMail(form) {
    form.peMailBypassFgNew.disabled = !form.peMailBypassFg.checked || form.peMailBypassFg.disabled;
}

function setFieldsForMemberRenewal(form) {
    form.mbrRenewalFgNew.disabled = !form.mbrRenewalFg.checked || form.mbrRenewalFg.disabled;
}

function setFieldsForMergedStatus(form) {
    StatusValueJS = "Merged";
    form.NewStatus.value = StatusValueJS;
    disableSplitAffiliateIdentifierFields(form);
    enableMergedAffiliateIdentifierFields(form);
    lockAffiliateIDFieldsByFields(form.elements['mergedAffiliate.type'], form.elements['mergedAffiliate.local'], form.elements['mergedAffiliate.state'], form.elements['mergedAffiliate.subUnit'], form.elements['mergedAffiliate.council']);
    form.elements['mergedAffiliate.type'].focus();
}

function setFieldsForSplitStatus(form) {
    StatusValueJS = "Split";
    form.NewStatus.value = StatusValueJS;
    disableMergedAffiliateIdentifierFields(form);
    enableSplitAffiliateIdentifierFields(form);
    lockAffiliateIDFieldsByFields(form.elements['splitAffiliate.type'], form.elements['splitAffiliate.local'], form.elements['splitAffiliate.state'], form.elements['splitAffiliate.subUnit'], form.elements['splitAffiliate.council']);
    form.elements['splitAffiliate.type'].focus();
}

function setFieldsForDeactivatedStatus(form) {
    StatusValueJS = "Deactivated";
    form.NewStatus.value = StatusValueJS;
    disableSplitAffiliateIdentifierFields(form);
    disableMergedAffiliateIdentifierFields(form);
}

function disableSplitAffiliateIdentifierFields(form) {
    // only if they're not already disabled
    if (form.elements['splitAffiliate.type'].disabled == false) {
        // back up the values
        SplitAffiliateTypeJS = form.elements['splitAffiliate.type'].selectedIndex;
        SplitAffiliateLocalJS = form.elements['splitAffiliate.local'].value;
        SplitAffiliateStateJS = form.elements['splitAffiliate.state'].selectedIndex;
        SplitAffiliateSubunitJS = form.elements['splitAffiliate.subUnit'].value;
        SplitAffiliateCouncilJS = form.elements['splitAffiliate.council'].value;
        // blank the values
        form.elements['splitAffiliate.type'].selectedIndex = 0;
        form.elements['splitAffiliate.local'].value = "";   //form.elements['splitAffiliate.local'].value = form.elements['splitAffiliate.local'].defaultValue;
        form.elements['splitAffiliate.state'].selectedIndex = 0;
        form.elements['splitAffiliate.subUnit'].value = ""; //form.elements['splitAffiliate.subUnit'].value = form.elements['splitAffiliate.subUnit'].defaultValue;
        form.elements['splitAffiliate.council'].value = ""; //form.elements['splitAffiliate.council'].value = form.elements['splitAffiliate.council'].defaultValue;
        form.SplitStatusText.value = "";
        // lock the fields
        form.elements['splitAffiliate.type'].disabled = true;
        form.elements['splitAffiliate.local'].disabled = true;
        form.elements['splitAffiliate.state'].disabled = true;
        form.elements['splitAffiliate.subUnit'].disabled = true;
        form.elements['splitAffiliate.council'].disabled = true;
        Finder2.disabled = true;
    }
}

function enableSplitAffiliateIdentifierFields(form) {
    // restore the backed up values
    form.elements['splitAffiliate.type'].selectedIndex = SplitAffiliateTypeJS;
    form.elements['splitAffiliate.local'].value = SplitAffiliateLocalJS;
    form.elements['splitAffiliate.state'].selectedIndex = SplitAffiliateStateJS;
    form.elements['splitAffiliate.subUnit'].value = SplitAffiliateSubunitJS;
    form.elements['splitAffiliate.council'].value = SplitAffiliateCouncilJS;
    form.SplitStatusText.value = "Enter Affiliate to be Associated with";
    // unlock the fields
    form.elements['splitAffiliate.type'].disabled = false;
    form.elements['splitAffiliate.local'].disabled = false;
    form.elements['splitAffiliate.state'].disabled = false;
    form.elements['splitAffiliate.subUnit'].disabled = false;
    form.elements['splitAffiliate.council'].disabled = false;
    Finder2.disabled = false;		
}

function disableMergedAffiliateIdentifierFields(form) {
    // only if they're not already disabled
    if (form.elements['mergedAffiliate.type'].disabled == false) {
        // back up the values
        MergedAffiliateTypeJS = form.elements['mergedAffiliate.type'].selectedIndex;
        MergedAffiliateLocalJS = form.elements['mergedAffiliate.local'].value;
        MergedAffiliateStateJS = form.elements['mergedAffiliate.state'].selectedIndex;
        MergedAffiliateSubunitJS = form.elements['mergedAffiliate.subUnit'].value;
        MergedAffiliateCouncilJS = form.elements['mergedAffiliate.council'].value;
        // blank the values
        form.elements['mergedAffiliate.type'].selectedIndex = 0;
        form.elements['mergedAffiliate.local'].value = "";      //form.elements['mergedAffiliate.local'].value = form.elements['mergedAffiliate.subUnit'].defaultValue;
        form.elements['mergedAffiliate.state'].selectedIndex = 0;
        form.elements['mergedAffiliate.subUnit'].value = "";    //form.elements['mergedAffiliate.subUnit'].value = form.elements['mergedAffiliate.subUnit'].defaultValue;
        form.elements['mergedAffiliate.council'].value = "";    //form.elements['mergedAffiliate.council'].value = form.elements['mergedAffiliate.council'].defaultValue;
        form.MergedStatusText.value = "";
        // lock the fields
        form.elements['mergedAffiliate.type'].disabled = true;
        form.elements['mergedAffiliate.local'].disabled = true;
        form.elements['mergedAffiliate.state'].disabled = true;
        form.elements['mergedAffiliate.subUnit'].disabled = true;
        form.elements['mergedAffiliate.council'].disabled = true;
        Finder1.disabled = true;
    }
}

function enableMergedAffiliateIdentifierFields(form) {
    // restore the backed up values
    form.elements['mergedAffiliate.type'].selectedIndex = MergedAffiliateTypeJS;
    form.elements['mergedAffiliate.local'].value = MergedAffiliateLocalJS;
    form.elements['mergedAffiliate.state'].selectedIndex = MergedAffiliateStateJS;
    form.elements['mergedAffiliate.subUnit'].value = MergedAffiliateSubunitJS;
    form.elements['mergedAffiliate.council'].value = MergedAffiliateCouncilJS;
    form.MergedStatusText.value = "Enter Affiliate to be Merged with";
    // unlock the fields
    form.elements['mergedAffiliate.type'].disabled = false;
    form.elements['mergedAffiliate.local'].disabled = false;
    form.elements['mergedAffiliate.state'].disabled = false;
    form.elements['mergedAffiliate.subUnit'].disabled = false;
    form.elements['mergedAffiliate.council'].disabled = false;
    Finder1.disabled = false;
}

function disableAllFinders() {
    Finder1.disabled = true;
    Finder2.disabled = true;
}

function lockMergeAffiliateID(form, field) {
    lockAffiliateIDFieldsByFields(form.elements['mergedAffiliate.type'], form.elements['mergedAffiliate.local'], form.elements['mergedAffiliate.state'], form.elements['mergedAffiliate.subUnit'], form.elements['mergedAffiliate.council']);
    MergedAffiliateTypeJS = field.selectedIndex;
}

function lockSplitAffiliateID(form, field) {
    lockAffiliateIDFieldsByFields(form.elements['splitAffiliate.type'], form.elements['splitAffiliate.local'], form.elements['mergedAffiliate.state'], form.elements['splitAffiliate.subUnit'], form.elements['splitAffiliate.council']);
    SplitAffiliateTypeJS = field.selectedIndex;
}

function lockNewAffiliateID(form, field) {
    if (form.elements['newAffiliate.type'].value == "#") {
    	if (form.elements['affiliateIdCurrent.council'].value != "") {
    	    form.elements['newAffiliate.council'].disabled = true;    	    
    	} else {
    	    form.elements['newAffiliate.council'].disabled = false;
    	}
        form.elements['newAffiliate.local'].disabled = true;
        form.elements['newAffiliate.state'].disabled = true;
        form.elements['newAffiliate.subUnit'].disabled = true;  
    } else {    
        if ((form.elements['newAffiliate.type'].value == "C") ||
            (form.elements['newAffiliate.type'].value == "R")) {           
            form.elements['newAffiliate.local'].disabled = true;
            form.elements['newAffiliate.subUnit'].disabled = true;
            form.elements['newAffiliate.state'].disabled = false;            
            form.elements['newAffiliate.council'].disabled = false;
        } else {
            if ((form.elements['newAffiliate.type'].value == "L") ||
                (form.elements['newAffiliate.type'].value == "S")) {   	        
    	        form.elements['newAffiliate.council'].value = "";    	    
                form.elements['newAffiliate.council'].disabled = true;
                form.elements['newAffiliate.subUnit'].disabled = true;
                form.elements['newAffiliate.state'].disabled = false;            
                form.elements['newAffiliate.local'].disabled = false;                
            } 
            else {
                if (form.elements['newAffiliate.type'].value == "U") {
                    form.elements['newAffiliate.council'].disabled = true;
                    form.elements['newAffiliate.subUnit'].disabled = false;
            	    form.elements['newAffiliate.state'].disabled = false;            
            	    form.elements['newAffiliate.local'].disabled = true;                    
                }
            }
        }
    }
    
    // lockAffiliateIDFieldsByFields(form.elements['newAffiliate.type'], 
    // form.elements['newAffiliate.local'], 
    // form.elements['mergedAffiliate.state'],
    // form.elements['newAffiliate.subUnit'],
    // form.elements['newAffiliate.council']);
    NewAffiliateTypeJS = field.selectedIndex;
}