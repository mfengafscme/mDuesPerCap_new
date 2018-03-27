// Global vars...

	// These are needed for Affiliate Finder
	var typeField;
	var localField;
	var stateField;
	var subunitField;
	var subunitField;
        var codeField;
        var affPkField;
	var aidWin;

	// These are needed for Political Rebate
	var denied;
	var deniedDateJS = "";
	var deniedReasonJS = 0;

	var resubmitted;
	var resubmittedDeniedReason;
	var resubmittedDeniedDateJS = "";
	var resubmittedDeniedReasonJS = 0;

// functions needed for Help functionality
function showHelp(helpFile) {

    var helpWin = window.open(helpFile, "helpWin", "width=600,height=600,toolbar=yes,resizeable=yes,scrollbars=yes");
    helpWin.focus();
}

function deleteEmpWarning() {
	input_box=confirm("Are you sure you want to delete this employer and all the associated wage increase data?");
	if (input_box==true)
        {
          // Output when OK is clicked
           return true;
        }

        else
        {
          // Output when Cancel is clicked
          return false;
        }
}

function checkEmpEditable() {
	var empInfoEditable = document.ViewDataEntryForm.elements["empEditable"].value;
	if (empInfoEditable == "yes") {
	    return true;
	}
	else if (empInfoEditable == "no") {
	    alert("The employer info is not editable because there is data for a previous dues year.");
	    return false;
	}
}

// functions needed for Affiliate Finder functionality
function showAffiliateIDResults(type, local, state, subunit, council, code, affPk) {
    //alert("inside showAffiliateIDResults");
    typeField       = type;
    localField      = local;
    stateField      = state;
    subunitField    = subunit;
    councilField    = council;
    codeField       = code;
    affPkField      = affPk;

    if (typeField.type == "hidden") {
        var typeFieldValue = typeField.value;
    } else {
        var typeFieldValue = typeField.options[typeField.selectedIndex].value;
    }

    if (stateField.type == "hidden") {
    	var stateFieldValue = stateField.value;
    } else {
    	var stateFieldValue = stateField.options[stateField.selectedIndex].value;
    }

    if (validateAffiliateIDFinderFieldsByFields(typeField, localField, stateField, subunitField, councilField)) {
        var action = "/mdu/searchAffiliateFinder.action" +
                     "?affIdType=" + typeFieldValue +
                     "&affIdLocal=" + localField.value +
                     "&affIdState=" + stateFieldValue +
                     "&affIdSubUnit=" + subunitField.value +
                     "&affIdCouncil=" + councilField.value +
                     "&finder=true&new"
        ;
        //alert(action);
        aidWin = window.open(action, "affIDwin", "width=800,height=600,toolbar=yes,resizeable=yes,scrollbars=yes");
        //aidWin = window.open(action, "affIDwin", "width=800,height=600");
        aidWin.focus();
    }
}

function viewAgreement_V() {
	var agreementPk = document.EditDataEntryForm.elements["agreementPk"].value;

        var action = "/mdu/viewAgreement.action?agreementPk=" + agreementPk;

        //alert(action);
        aidWin = window.open(action, "affIDwin", "width=610,height=470,toolbar=yes,resizeable=yes,scrollbars=yes");
        aidWin.focus();
}

function viewAgreement(form) {
	var agreementPk = form.agreementName.value;

	if ((trim(agreementPk) == "0") || (trim(agreementPk) == "")) {
		alert("No agreement is chosen for viewing.");
		return false;
	}

        var action = "/mdu/viewAgreement.action?agreementPk=" + agreementPk;

        //alert(action);
        aidWin = window.open(action, "affIDwin", "width=610,height=470,toolbar=yes,resizeable=yes,scrollbars=yes");
        aidWin.focus();
}

// old resultsReturn function
/*
function resultsReturn(type, local, state, subunit, council, code, pk) {
    alert(  "values: \n" +
            "type    = " + type + "\n" +
            "local   = " + local + "\n" +
            "state   = " + state + "\n" +
            "subunit = " + subunit + "\n" +
            "council = " + council + "\n" +
            "code    = " + coude + "\n" +
            "pk      = " + pk
    );

    localField.value	= local;
    subunitField.value	= subunit;
    councilField.value	= council;

    if (codeField != null) {
        codeField.value = code;
    }
    if (affPkField != null) {
        affPkField.value = pk;
    }

    for (var i = 0; i < typeField.length; i++) {
        if (typeField.options[i].text == type || typeField.options[i].value == type) {
                typeField.options[i].selected = true;
        }
    }
    for (var i = 0; i < stateField.length; i++) {
        if (stateField.options[i].text == state || stateField.options[i].value == state) {
                stateField.options[i].selected = true;
        }
    }
    aidWin.close();
    lockAffiliateIDFieldsByFields(typeField, localField, stateField, subunitField, councilField);
}


function resultsReturn(employer, type, local, state, subunit, council, code, pk) {
    localField.value	= local;
    subunitField.value	= subunit;
    councilField.value	= council;
    //employerName.value	= employer;
    document.DataEntryForm.elements["employerName"].value = employer;

    if (codeField != null) {
        codeField.value = code;
    }
    if (affPkField != null) {
        affPkField.value = pk;
    }

    for (var i = 0; i < typeField.length; i++) {
        if (typeField.options[i].text == type || typeField.options[i].value == type) {
                typeField.options[i].selected = true;
        }
    }
    for (var i = 0; i < stateField.length; i++) {
        if (stateField.options[i].text == state || stateField.options[i].value == state) {
                stateField.options[i].selected = true;
        }
    }
    aidWin.close();
    lockAffiliateIDFieldsByFields(typeField, localField, stateField, subunitField, councilField);
}
*/

function resultsReturn(employer, type, local, state, subunit, council, code, pk) {
    localField.value	= local;
    subunitField.value	= subunit;
    councilField.value	= council;
    //employerName.value	= employer;
    document.DataEntryForm.elements["employerName"].value = employer;
    document.DataEntryForm.elements["affIdLocal"].value = local;
    document.DataEntryForm.elements["affIdSubUnit"].value = subunit;

    if (codeField != null) {
        codeField.value = code;
    }
    if (affPkField != null) {
        affPkField.value = pk;
    }

    for (var i = 0; i < typeField.length; i++) {
        if (typeField.options[i].text == type || typeField.options[i].value == type) {
                typeField.options[i].selected = true;
        }
    }
    for (var i = 0; i < stateField.length; i++) {
        if (stateField.options[i].text == state || stateField.options[i].value == state) {
                stateField.options[i].selected = true;
        }
    }

    aidWin.close();
}

function validateAffiliateIDFinderFieldsByFields(type, local, state, subunit, council) {
    //alert("inside validateAffiliateIDFinderFieldsByFields");
    if (type.value == "" &&
        local.value == "" &&
        state.value == "" &&
        subunit.value == "" &&
        council.value == ""
    ) {
        alert('You must enter at least one of the Affiliate Identifier fields before performing using the Finder.');
        return false;
    }
    return true;
}

function openDocument() {
    //var constDocWin = window.open("/viewConstitutionDocument", "constDocWin", "width=800,height=600");
    var constDocWin = window.open("/viewConstitutionDocument", "constDocWin");
    constDocWin.focus();
}

// Validation functions
function validateChangeHistorySearch(form) {
	if ( (form.Section.selectedIndex < 1) && (form.FromDate.value == "") && (form.ToDate.value == "") && (form.UserID.value == "") ) {
		alert("You must enter at least one field to search.");
		return false;
	}
	return true;
}

/* Validates Comments in a textarea based on a max size of 254 for all comments. Should
 * be changed to read from a configurable source.
 */
function validateComments(field) {
        enforceTextAreaMaxLength(field, 254);
}

/* Validates Charter Name in a textarea based on a max size of 250. Should  be changed to
 * read from a configurable source.
 */
function validateCharterName(field) {
        enforceTextAreaMaxLength(field, 250);
}

/* Validates Charter Name in a textarea based on a max size of 5000. Should  be changed to
 * read from a configurable source.
 */
function validateCharterJurisdiction(field) {
        enforceTextAreaMaxLength(field, 5000);
}

/* Enforces that the text area in the param, field, is no larger than the value in the param, length.
 */
function enforceTextAreaMaxLength(field, length) {
        if (field.value.length > length) {
                field.value = field.value.substring(0, length);
        }
}


function validatePrimaryPhoneAsHome(code, primaryFlag) {
	if (primaryFlag.checked == true && code.options[code.selectedIndex].text.toUpperCase() != "HOME") {
		alert("Only a Home Phone Number can be marked as Primary.");
		return false;
	} else if (primaryFlag.checked == false && code.options[code.selectedIndex].text.toUpperCase() == "HOME") {
		alert("If a Home Phone Number is entered, it must be marked as Primary.");
		return false;
	}
	return true;
}

function initRebateDenial(form) {
	denied = form.denied.checked;
	resubmitted = form.resubmitted.checked;
	resubmittedDeniedReason = form.resubmittedDeniedReason.selectedIndex;
	validateRebateDenial(form);
	validateRebateResubmitDenial(form);
}

function resetRebateDenial(form) {
	form.denied.checked = denied;
	form.resubmitted.checked = resubmitted;
	form.resubmittedDeniedReason.selectedIndex = resubmittedDeniedReason;
	validateRebateDenial(form);
	validateRebateResubmitDenial(form);
}

function validateRebateDenial(form) {
	deniedReasonJS = form.deniedReason.selectedIndex;
	deniedDateJS = form.deniedDate.value;
	if (form.denied.checked == true) {
		// enable deniedDate and restore from backup or today's date
		form.deniedDate.disabled = false;
		if (deniedDateJS == "") {
			form.deniedDate.value = getTodaysDate();
		} else {
			form.deniedDate.value = deniedDateJS;
		}

		// enable deniedReason and restore from backup
		form.deniedReason.disabled = false;
		form.deniedReason.selectedIndex = deniedReasonJS;

		// enable deniedCalendar
		deniedCalendar.disabled = false;
	} else {
		// disable deniedDate and set backup
		form.deniedDate.disabled = true;
		form.deniedDate.value = "";

		// disable deniedReason and set backup
		form.deniedReason.disabled = true;
		form.deniedReason.selectedIndex = 0;

		// disable deniedCalendar
		deniedCalendar.disabled = true;
	}
}

function validateRebateResubmitDenial(form) {
	resubmittedDeniedReasonJS = form.resubmittedDeniedReason.selectedIndex;
	resubmittedDeniedDateJS = form.resubmittedDeniedDate.value;

	if (form.resubmitted.checked == true) {
		if (form.resubmittedDeniedReason.disabled == true) {
			// enable resubmittedDeniedReason and restore from backup
			form.resubmittedDeniedReason.disabled = false;
			form.resubmittedDeniedReason.selectedIndex = resubmittedDeniedReasonJS;
		}
		if (form.resubmittedDeniedReason.selectedIndex > 0) {
			// enable resubmittedDeniedDate and restore from backup or today's date
			form.resubmittedDeniedDate.disabled = false;
			if (resubmittedDeniedDateJS == "") {
				form.resubmittedDeniedDate.value = getTodaysDate();
				resubmittedDeniedDateJS = form.resubmittedDeniedDate.value;
			} else {
				form.resubmittedDeniedDate.value = resubmittedDeniedDateJS;
			}
			// enable resubmittedDeniedCalendar
			resubmittedDeniedCalendar.disabled = false;
		} else if (form.resubmittedDeniedDate.disabled == false) {
			// disable resubmittedDeniedDate and set backup
			form.resubmittedDeniedDate.disabled = true;
			resubmittedDeniedDateJS = form.resubmittedDeniedDate.value;
			form.resubmittedDeniedDate.value = "";
			// disable resubmittedDeniedCalendar
			resubmittedDeniedCalendar.disabled = true;
		}
	} else {
		// disable resubmittedDeniedReason and set backup
		form.resubmittedDeniedReason.disabled = true;
		resubmittedDeniedReasonJS = form.resubmittedDeniedReason.selectedIndex;
		form.resubmittedDeniedReason.selectedIndex = 0;
		if (form.resubmittedDeniedDate.disabled == false) {
			// disable resubmittedDeniedDate and set backup
			form.resubmittedDeniedDate.disabled = true;
			resubmittedDeniedDateJS = form.resubmittedDeniedDate.value;
			form.resubmittedDeniedDate.value = "";
		}
		// disable resubmittedDeniedCalendar
		resubmittedDeniedCalendar.disabled = true;
	}
}

function validateRequiredAffiliateIdentifierFields(form) {
	return validateRequiredAffiliateIdentifierFieldsByFields(form.affiliateType, form.affiliateLocal, form.affiliateCode, form.affiliateState);
}

function validateRequiredAffiliateIdentifierFieldsByFields(type, number, code, state) {
	var typeValue = type.options[form.affiliateType.selectedIndex].text;
	var codeValue = code.value;
	var stateValue = state.options[form.affiliateState.selectedIndex].text;
	var numberValue = number.value;
	//if the Type, Code, and/or State is entered, then the Number must contain a value.
	if ( (typeValue != "" ||
		  codeValue != "" ||
		  stateValue != "") &&
		 numberValue == ""
	) {
		alert('If the Affiliate Identifier\'s Type, Code, or State fields contain data, \n\n' +
			  'then you must enter a value for the Affiliate Identifier Number.'
		);
		return false;
	}
	return true;
}

// Other functions here...

function disableRadioSet(form, radioFieldName) {
    for (var i = 0; i < form.elements.length; i++) {
        var field = form.elements[i];
        if (field.name == radioFieldName) {
            field.disabled = true;
        }
    }
}

function enableRadioSet(form, radioFieldName) {
    for (var i = 0; i < form.elements.length; i++) {
        var field = form.elements[i];
        if (field.name == radioFieldName) {
            field.disabled = false;
        }
    }
}

function resetAll() {
	for (var i = 0; i < document.forms.length; i++) {
		form = document.forms[i];
		form.reset();
	}
}

function swapOfficeCountWithVariable(numberField, checkField, numPreviousValue) {
	if (checkField.checked)  {
		numPreviousValue = numberField.value;
		numberField.value = "";
		numberField.disabled = true;
	} else {
		numberField.disabled = false;
		numberField.value = numPreviousValue;
	}
	return numPreviousValue;
}

function initializeFocus() {
	var form = document.forms[0];
	for (var i = 0; i < form.elements.length; i++) {
		var element = form.elements[i];
		if (element.type == "text" || element.type == "textarea" || element.type == "select-one" ||
			((element.type == "checkbox" || element.type == "radio") && element.disabled == false)
		) {
			element.focus();
			return element;
		}
	}
}

function forceTab(input) {
	var element = input.form[(getIndex(input)+1) % input.form.length];
	if (element.type == "text" || element.type == "textarea") {
		element.select();
	} else {
		element.focus();
	}
	return true;
}

function disableAllTextFields(form) {
	disableAllTextFieldsWithExemption(form, [], []);
}

function clearHiddenFields(form) {
    //alert(form + " " + form.name);
    var field;
    for (var i = 0; i < form.elements.length; i++) {
        field = form.elements[i];
        if (field.type == "hidden") {
            field.value = "";
        }
    }
}

function clearHiddenFieldsAff(form) {
    //alert(form + " " + form.name);
    var field;
    for (var i = 0; i < form.elements.length; i++) {
        field = form.elements[i];
        if ((field.type == "hidden") && (field.name != "affIdType")) {
            field.value = "";
        }
    }
}


/* disables all text, hidden, and dropdown fields except the ones that are passed in as the param, exemptFieldMatrix,
 * which is an array of field name/value pairs. Each entry in the array must contain an array with two entries as follows:
 * e.g. ['fieldname', fieldvalue]
 */
function disableAllTextFieldsWithExemption(form, exemptFieldMatrix) {
	var field;
	for (var i = 0; i < form.elements.length; i++) {
		field = form.elements[i];
		var j = 0;
		var found = false;
		do {
			if (exemptFieldMatrix.length == 0) {
				if (field.type == "text" || field.type == "hidden" || field.type == "select-one") {
					field.disabled = true;
					field.value = field.defaultValue;
				}
			} else {
				var fieldName = exemptFieldMatrix[j][0];
				var fieldValue = exemptFieldMatrix[j][1];
				if (field.name == fieldName) {
					found = true;
					if (field.type == "text" || field.type == "hidden" || field.type == "select-one") {
						field.disabled = false;
						if (field.type == "select-one") {
							field.selectedIndex = fieldValue;
						} else {
							field.value = fieldValue;
						}
					}
				} else {
					if (field.type == "text" || field.type == "hidden" || field.type == "select-one") {
						field.disabled = true;
						field.value = field.defaultValue;
					}
				}
			}
		} while (++j < exemptFieldMatrix.length && !found);
	}
}

function reverseFlag(hidden) {

	if(hidden.value == "true")
		hidden.value = false;
	else
		hidden.value = true;
}

function setAllMailFlags(form) {
	if (form.noMailFg.checked) {
		form.noCardsFg.checked = true;
		form.noCardsFg.disabled = true;
		form.noPublicEmpFg.checked = true;
		form.noPublicEmpFg.disabled = true;
		form.noLegislativeMailFg.checked = true;
		form.noLegislativeMailFg.disabled = true;
		form.noCardsFlag.value = true;
		form.noPublicEmpFlag.value = true;
		form.noLegislativeMailFlag.value = true;

	} else {
//		form.noCardsFg.checked = false;
//		form.noCardsFg.disabled = false;
//		form.noPublicEmpFg.checked = false;
//		form.noPublicEmpFg.disabled = false;
//		form.noLegislativeMailFg.checked = false;
//		form.noLegislativeMailFg.disabled = false;
	}

}

function setMailFlagsByMemberType(form) {

    if (form.mbrType.options[form.mbrType.selectedIndex].text == "Union Shop Objector")
    {
       setMailFlagsUnionShopObjector(form);
    }
    if (form.mbrType.options[form.mbrType.selectedIndex].text == "Agency Fee Payer")
    {
       setMailFlagsAgencyFee(form);
    }
    if (form.mbrType.options[form.mbrType.selectedIndex].text == "Retiree Spouse")
    {
       setMailFlagsRetireeSpouse(form);
    }
    if (form.mbrType.options[form.mbrType.selectedIndex].text == "Union Shop" || form.mbrType.options[form.mbrType.selectedIndex].text == "Retiree" ||
            form.mbrType.options[form.mbrType.selectedIndex].text == "Regular" )
    {
       setMailFlagsAllOthers(form);
    }

}


function setMailFlagsUnionShopObjector(form) {
    form.noCardsFg.checked = true;
    form.noCardsFg.disabled = true;
    form.noCardsFlag.value = true;
    if (!form.noMailFg.checked) {
        form.noPublicEmpFg.disabled = false;
        form.noLegislativeMailFg.disabled = false;
        }
}

function setMailFlagsAgencyFee(form) {
     form.noCardsFg.checked = true;
     form.noCardsFg.disabled = true;
     form.noCardsFlag.value = true;

     form.noLegislativeMailFg.checked = true;
     form.noLegislativeMailFg.disabled = true;
     form.noLegislativeMailFlag.value = true;
     if (!form.noMailFg.checked) {
         form.noPublicEmpFg.disabled = false;
     }
}

function setMailFlagsRetireeSpouse(form) {
    form.noPublicEmpFg.checked = true;
    form.noPublicEmpFg.disabled = true;
    form.noPublicEmpFlag.value = true;
    if (!form.noMailFg.checked) {
        form.noLegislativeMailFg.disabled = false;
        form.noCardsFg.disabled = false;
    }
}

function setMailFlagsAllOthers(form) {
    if (!form.noMailFg.checked) {
        form.noLegislativeMailFg.disabled = false;
        form.noCardsFg.disabled = false;
        form.noPublicEmpFg.disabled = false;
    }

}


function editOnCheck(flag, editField) {
	editField.disabled = !flag.checked;
}


function cancelActualSalary(form) {
	if (form.EmployeeSalaryRange.selectedIndex > 0) {
		form.EmployeeSalary.value = "";
	}
}

function cancelSalaryRange(form) {
	if (form.EmployeeSalary.value != null) {
		form.EmployeeSalaryRange.selectedIndex = 0;
	}
}

function validateOnePresident(form) {
	if (form.afscmeTitle.options[form.afscmeTitle.selectedIndex].text == "President") {
		form.numWithTitle.value = 1;
		form.numWithTitle.disabled = false;
	} else {
		form.numWithTitle.disabled = false;
	}
}

function validateReportingOfficer(form) {
	if ((form.numWithTitle.value != 1) && (form.afscmeTitle.options[form.afscmeTitle.selectedIndex].text != "Steward")) {
		form.reportingOfficer.checked = false;
		form.reportingOfficer.disabled = true;
	}else if ((form.numWithTitle.value = 1) && (form.afscmeTitle.options[form.afscmeTitle.selectedIndex].text != "Steward"))  {
		form.reportingOfficer.disabled = false;
	}
}

function checkSteward(form) {
	if (form.afscmeTitle.options[form.afscmeTitle.selectedIndex].text == "Steward") {
		form.lengthOfTerm.selectedIndex = getSelectedIndex(form.lengthOfTerm, "Indefinite");
		form.lengthOfTerm.disabled = false;
		form.termEnd.value = "9999";
		form.termEnd.disabled = false;
		form.delegatePriority.value = "0";
		form.delegatePriority.disabled = true;
		form.reportingOfficer.checked = false;
		form.reportingOfficer.disabled = true;
		form.execBoard.checked = false;
		form.execBoard.disabled = true;
	} else {
		form.lengthOfTerm.disabled = false;
		form.termEnd.disabled = false;
		form.delegatePriority.disabled = false;
		if (form.numWithTitle.value == 1){
		   form.reportingOfficer.disabled = false;
		} else {
                   form.execBoard.disabled = false;
                }
	}
}

function indefiniteOrTemporaryTerm(form) {
	if (form.lengthOfTerm.options[form.lengthOfTerm.selectedIndex].text == "Indefinite") {
		form.termEnd.value = "9999";
		form.termEnd.disabled = false;
	} else if (form.lengthOfTerm.options[form.lengthOfTerm.selectedIndex].text == "Temporary") {
		form.termEnd.value = "9999";
		form.termEnd.disabled = false;
	} else {
		form.termEnd.disabled = false;

        }
}

function lockAffiliateIDFields(form) {
    lockAffiliateIDFieldsByFields(form.affIdType, form.affIdLocal, form.affIdState, form.affIdSubUnit, form.affIdCouncil);
}

function lockAffiliateIDFieldsByFields(affilType, affilLocal, affilState, affilSubunit, affilCouncil) {
    if (affilType.selectedIndex != 0 && affilType.selectedIndex != null) {
        var value = affilType.options[affilType.selectedIndex].text;
        if (value == 'C' || value == 'R') {
            // disabled and initialize subunit
            affilSubunit.value = "";
            affilSubunit.disabled = true;
            // disabled and initialize local
            affilLocal.value = "";
            affilLocal.disabled = true;
            // enable council
            affilCouncil.disabled = false;
        } else if (value == 'L' || value == 'S') {
            // disabled and initialize subunit
            affilSubunit.value = "";
            affilSubunit.disabled = true;
            // enable local and council
            affilLocal.disabled = false;
            affilCouncil.disabled = false;
        } else {
            // enable all fields
            //affilSubunit.value = "";
            affilSubunit.disabled = false;
            //affilLocal.value = "";
            affilLocal.disabled = false;
            //affilCouncil.value = "";
            affilCouncil.disabled = false;
        }
    }
}

function lockAffiliateIDFieldsAdd(form) {
    lockAffiliateIDFieldsByFieldsAdd(form.affIdType, form.affIdLocal, form.affIdState,
    				form.affIdSubUnit, form.affIdCouncil, form.charterName,
    				form.charterJurisdiction, form.charterCode, form.charterDateMonth,
    				form.charterDateYear, form.charterCounty1, form.charterCounty2,
    				form.charterCounty3,form.approvedConstitution,
    				form.generateDefaultOffices, form.affilAgreementDateMonth);
}

function lockAffiliateIDFieldsByFieldsAdd(affilType, affilLocal, affilState,
				affilSubunit, affilCouncil, charterName, charterJurisdiction,
				charterCode, charterDateMonth, charterDateYear, charterCounty1,
				charterCounty2, charterCounty3, approvedConstitution,
    				generateDefaultOffices, affilAgreementDateMonth) {
    if (affilType.selectedIndex != 0) {
        var value = affilType.options[affilType.selectedIndex].text;
        if (value == 'C' || value == 'R') {
            // disabled and initialize subunit
            affilSubunit.value = "";
            affilSubunit.disabled = true;
            // disabled and initialize local
            affilLocal.value = "";
            affilLocal.disabled = true;
            // enable council and charter/const
            affilCouncil.disabled = false;
	    charterJurisdiction.disabled = false;
	    charterCode.disabled = false;
	    charterDateMonth.disabled = false;
	    charterDateYear.disabled = false;
	    charterCounty1.disabled = false;
	    charterCounty2.disabled = false;
	    charterCounty3.disabled = false;
	    approvedConstitution.disabled = false;
	    generateDefaultOffices.disabled = false;
	    affilAgreementDateMonth.disabled = false;
        } else if (value == 'L' || value == 'S') {
            // disabled and initialize subunit
            affilSubunit.value = "";
            affilSubunit.disabled = true;
            // enable local and council and charter/const
            affilLocal.disabled = false;
            affilCouncil.disabled = false;

	    charterJurisdiction.disabled = false;
	    charterCode.disabled = false;
	    charterDateMonth.disabled = false;
	    charterDateYear.disabled = false;
	    charterCounty1.disabled = false;
	    charterCounty2.disabled = false;
	    charterCounty3.disabled = false;
	    approvedConstitution.disabled = false;
	    generateDefaultOffices.disabled = false;
	    affilAgreementDateMonth.disabled = false;
        } else {
            // enable all fields
            //affilSubunit.value = "";
            affilSubunit.disabled = false;
            //affilLocal.value = "";
            affilLocal.disabled = false;
            //affilCouncil.value = "";
            affilCouncil.disabled = false;

            // disable charter/const
	    charterJurisdiction.disabled = true;
	    charterCode.disabled = true;
	    charterDateMonth.disabled = true;
	    charterDateYear.disabled = true;
	    charterCounty1.disabled = true;
	    charterCounty2.disabled = true;
	    charterCounty3.disabled = true;
	    approvedConstitution.disabled = true;
	    generateDefaultOffices.disabled = true;
	    affilAgreementDateMonth.disabled = true;
        }
    }
}

function getSelectedIndex(field, text) {
	for (var i = 0; i < field.options.length; i++) {
		option = field.options[i];
		if (option.text == text || option.value == text) {
			return i;
		}
	}
	return 0;
}

function enableAllFields(form) {
    //alert("starting enableAllFields");
    for (var i = 0; i < form.elements.length; i++) {
        form.elements[i].disabled = false;
    }
    //alert("finishing enableAllFields");
}

function selectChange(control, controlToPopulate, ItemArray, GroupArray)
{
  var myEle ;
  var x ;
  // Empty the second drop down box of any choices
  for (var q=controlToPopulate.options.length;q>=0;q--) controlToPopulate.options[q]=null;
  if (control.id == "firstChoice") {
    // Empty the third drop down box of any choices
    for (var q=participationDetailForm.thirdChoice.options.length;q>=0;q--) participationDetailForm.thirdChoice.options[q] = null;
    for (var q=participationDetailForm.fourthChoice.options.length;q>=0;q--) participationDetailForm.fourthChoice.options[q] = null;
  }
  if (control.id == "secondChoice") {
    // Empty the fourth drop down box of any choices
    for (var q=participationDetailForm.fourthChoice.options.length;q>=0;q--) participationDetailForm.fourthChoice.options[q] = null;
  }
  // ADD Default Choice - in case there are no values
  myEle = document.createElement("option") ;
  myEle.value = "";
  myEle.text = "" ;
  controlToPopulate.add(myEle) ;
  // Now loop through the array of individual items
  // Any containing the same child id are added to
  // the second dropdown box
  for ( x = 0 ; x < ItemArray.length  ; x++ )
    {
      if ( GroupArray[x] == control.value )
        {
          myEle = document.createElement("option") ;
          myEle.value = x ;
          myEle.text = ItemArray[x] ;
          controlToPopulate.add(myEle) ;
        }
    }
}


function clearShortcut(form) {
    form.detailShortcut.value = "";
    form.oldDetailShortcut.value = "";
}

function shortcutExecute(form) {
    if (form.detailShortcut.value == "")
    	return;
    if (form.oldDetailShortcut != null)
    {
        if(form.oldDetailShortcut.value == form.detailShortcut.value)
    	    return;
    }
    var temp = "/addParticipationDetail.action?shortcut=" + form.detailShortcut.value;
    window.location=temp;
}

function shortcutExecuteEdit(form) {
    if (form.detailShortcut.value == "")
    	return;
    if (form.oldDetailShortcut != null)
    {
        if(form.oldDetailShortcut.value == form.detailShortcut.value)
    	    return;
    }
    var temp = "/editParticipationDetail.action?shortcut=" + form.detailShortcut.value + "&oldDetailPk=" + form.oldDetailPk.value;
    window.location=temp;
}

function resetParticipation(form) {
    form.comments.value = "";
    form.participationDate.value = "" ;
    form.detailShortcut.value = "";
    form.oldDetailShortcut.value= "";
    for (var q=form.thirdChoice.options.length;q>=0;q--) form.thirdChoice.options[q] = null;
    for (var q=form.fourthChoice.options.length;q>=0;q--) form.fourthChoice.options[q] = null;
    for (var q=form.secondChoice.options.length;q>=0;q--) form.secondChoice.options[q] = null;
}


function submitAndAddAnother(form) {
    form.another.value = "yes";
    form.submit();
}

function clearTheOtherSalaryField(form, field)
{
	if (field == 'salary')
	{
		form.salaryRange.value = "";
	}else if (field == 'salaryRange')
	{
		form.salary.value= "";
	}

}

function setAllMailFlagsVdu(form) {
 if (form.noMailFg.checked) {
  form.noLegislativeMailFg.checked = true;
  form.noLegislativeMailFg.disabled = true;
  form.noLegislativeMailFlag.value = true;
        }
}

function rowReset(i, k) {
	titlesTable.rows(i).cells(6).childNodes(0).value=titlesTable.rows(i).cells(6).childNodes(0).defaultValue;
	titlesTable.rows(i).cells(7).childNodes(0).value=titlesTable.rows(i).cells(7).childNodes(0).defaultValue;
	titlesTable.rows(i).cells(8).childNodes(0).value=titlesTable.rows(i).cells(8).childNodes(0).defaultValue;
	titlesTable.rows(i).cells(5).childNodes(1).value=titlesTable.rows(i).cells(0).childNodes(2).value;
	titlesTable.rows(i).cells(9).childNodes(0).selectedIndex=titlesTable.rows(i).cells(9).childNodes(0).defaultValue;
	titlesTable.rows(i).cells(6).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(7).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(8).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(5).childNodes(1).disabled=true;
	titlesTable.rows(i).cells(9).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(6).childNodes(2).value=0;
	titlesTable.rows(i).cells(6).childNodes(4).value=0;
	if (k < 1) {
		titlesTable.rows(i).cells(1).childNodes(0).checked=false;
		titlesTable.rows(i).cells(2).childNodes(0).checked=false;
		titlesTable.rows(i).cells(3).childNodes(0).checked=false;
	}
	if (titlesTable.rows(i).cells(10).childNodes(0).defaultChecked) {
		titlesTable.rows(i).cells(10).childNodes(0).checked=true;
	} else {
		titlesTable.rows(i).cells(10).childNodes(0).checked=false;
	}
	if (titlesTable.rows(i).cells(10).childNodes(2).value=='y') {
		titlesTable.rows(i).cells(10).childNodes(0).disabled=true;
	} else {
		titlesTable.rows(i).cells(10).childNodes(0).disabled=false;
	}
}

function setRowDisabled(i) {
	titlesTable.rows(i).cells(6).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(7).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(8).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(5).childNodes(1).disabled=true;
	titlesTable.rows(i).cells(9).childNodes(0).disabled=true;
}

function vacate(i) {

	titlesTable.rows(i).cells(5).childNodes(1).value=titlesTable.rows(i).cells(5).childNodes(1).defaultValue;
	titlesTable.rows(i).cells(10).childNodes(0).disabled=true;
	titlesTable.rows(i).cells(6).childNodes(2).value=0;
	titlesTable.rows(i).cells(6).childNodes(4).value=0;
}

function replace(i) {
	titlesTable.rows(i).cells(6).childNodes(0).disabled=false;
	titlesTable.rows(i).cells(7).childNodes(0).disabled=false;
	titlesTable.rows(i).cells(8).childNodes(0).disabled=false;
	titlesTable.rows(i).cells(9).childNodes(0).disabled=false;
	titlesTable.rows(i).cells(5).childNodes(1).disabled=false;
	if ((titlesTable.rows(0).cells(0).childNodes(0).value == 'L') || (titlesTable.rows(0).cells(0).childNodes(0).value == 'U')) {
	     titlesTable.rows(i).cells(10).childNodes(0).disabled=false;
	}
}

function resetEntireForm(k) {
	for (var i = 3; i <= k ; i+= 2) {
		titlesTable.rows(i).cells(6).childNodes(0).value=titlesTable.rows(i).cells(6).childNodes(0).defaultValue;
		titlesTable.rows(i).cells(7).childNodes(0).value=titlesTable.rows(i).cells(7).childNodes(0).defaultValue;
		titlesTable.rows(i).cells(8).childNodes(0).value=titlesTable.rows(i).cells(8).childNodes(0).defaultValue;
		titlesTable.rows(i).cells(5).childNodes(1).value=titlesTable.rows(i).cells(0).childNodes(2).value;
		titlesTable.rows(i).cells(9).childNodes(0).selectedIndex=titlesTable.rows(i).cells(9).childNodes(0).defaultValue;
		titlesTable.rows(i).cells(6).childNodes(0).disabled=true;
		titlesTable.rows(i).cells(7).childNodes(0).disabled=true;
		titlesTable.rows(i).cells(8).childNodes(0).disabled=true;
		titlesTable.rows(i).cells(5).childNodes(1).disabled=true;
		titlesTable.rows(i).cells(9).childNodes(0).disabled=true;
		titlesTable.rows(i).cells(1).childNodes(0).checked=false;
		titlesTable.rows(i).cells(2).childNodes(0).checked=false;
		titlesTable.rows(i).cells(3).childNodes(0).checked=false;
		titlesTable.rows(i).cells(6).childNodes(2).value=0;
	        titlesTable.rows(i).cells(6).childNodes(4).value=0;
		if (titlesTable.rows(i).cells(10).childNodes(0).defaultChecked) {
			titlesTable.rows(i).cells(10).childNodes(0).checked=true;
		} else {
			titlesTable.rows(i).cells(10).childNodes(0).checked=false;
		}
		if (titlesTable.rows(i).cells(10).childNodes(2).value=='y') {
			titlesTable.rows(i).cells(10).childNodes(0).disabled=true;
		} else {
			titlesTable.rows(i).cells(10).childNodes(0).disabled=false;
		}
	}
}


function checkForm(k) {
	if ((titlesTable.rows(k).cells(5).childNodes(1).value !=
	    titlesTable.rows(k).cells(0).childNodes(0).value)
	    && (titlesTable.rows(k).cells(5).childNodes(1).value !=
	    titlesTable.rows(k).cells(0).childNodes(2).value)) {
		alert('The End Term Year entered does not meet the requirement for the Length of Term that corresponds to this title.');
	}
}

// functions needed for Affiliate Officer Maintenance functionality
function showOfficerSearchResults(first, middle, last, suffix, personPk, affPk, elected) {

    firstField       = first;
    middleField      = middle;
    lastField        = last;
    suffixField      = suffix;
    personPkField    = personPk;
    affPkField       = affPk;
    electedField     = elected

    var action = "/replaceOfficerResults.action" +
	     "?firstName=" + firstField.value +
	     "&middleName=" + middleField.value +
	     "&lastName=" + lastField.value +
	     "&suffix=" + suffixField.value +
	     "&personPk=" + personPkField.value +
	     "&affPk=" + affPkField.value +
	     "&elected=" + electedField.value + "&new=";

   // alert(action);
    aidWin = window.open(action, "offwin", "width=800,height=600,toolbar=yes,resizeable=yes,scrollbars=yes");
    aidWin.focus();

}

function officerResultsReturn(first, middle, last, suffix, personPk, affPk) {
    /* alert(  "values: \n" +
            "first    = " + first + "\n" +
            "middle   = " + middle + "\n" +
            "last   = " + last + "\n" +
            "suffix = " + suffix + "\n" +
            "personPk = " + personPk + "\n" +
            "affPk    = " + affPk);
    */
    firstField.value	= first;
    if (middle != 'null') {
    	middleField.value = middle;
    } else {
        middleField.value = "";
    }
    lastField.value	= last;
    personPkField.value = personPk;
    affPkField.value    = affPk;

    for (var i = 0; i < suffixField.length; i++) {
        if (suffixField.options[i].text == suffix || suffixField.options[i].value == suffix) {
                suffixField.options[i].selected = true;
        }
    }

    aidWin.close();
}

function clearOfficerHiddenFields(i) {
	titlesTable.rows(i).cells(6).childNodes(2).value=0;
	titlesTable.rows(i).cells(6).childNodes(4).value=0;
}

function trim(s) {
  if (s != null) {
  	  // Remove leading spaces and carriage returns
	  while ((s.substring(0,1) == ' ') || (s.substring(0,1) == '\n') || (s.substring(0,1) == '\r'))
	  {
	    s = s.substring(1,s.length);
	  }

	  // then remove trailing spaces and carriage returns
	  while ((s.substring(s.length-1,s.length) == ' ') || (s.substring(s.length-1,s.length) == '\n') || (s.substring(s.length-1,s.length) == '\r'))
	  {
	    s = s.substring(0,s.length-1);
	  }
  }

  return s;
}

// Check the value to be numeric and remove comma
function checkNumeric(objName, minval, maxval, comma, period, hyphen)
{
	var numberfield = objName;
	if (chkNumeric(objName,minval,maxval,comma,period,hyphen) == false)
	{
		numberfield.select();
		numberfield.value = '';
		numberfield.focus();

		return false;
	}
	else
	{
		formatToNumber(objName);
		return true;
	}
}

function chkNumeric(objName, minval, maxval, comma, period, hyphen)
{
		// only allow 0-9 be entered, plus any values passed
		// (can be in any order, and don't have to be comma, period, or hyphen)
		// if all numbers allow commas, periods, hyphens or whatever,
		// just hard code it here and take out the passed parameters
		var checkOK = "0123456789" + comma + period + hyphen;
		var checkStr = objName;
		var allValid = true;
		var decPoints = 0;
		var allNum = "";

		if (checkStr.value.charAt(0) == ".") {
		     checkStr.value = "0"+checkStr.value;
		     //alert('here');
		}

		//alert('no chnage');

		for (i = 0;  i < checkStr.value.length;  i++)
		{
			ch = checkStr.value.charAt(i);
			for (j = 0;  j < checkOK.length;  j++)
				if (ch == checkOK.charAt(j))
					break;

			if (j == checkOK.length)
			{
				allValid = false;
				break;
			}

			if (ch != ",")
				allNum += ch;
		}

		if (!allValid)
		{
			if (period == "")
				alert("Please enter an integer value.");
			else
				alert("Please enter a numeric value.");

			return (false);
		}


		// set the minimum and maximum
		var chkVal = allNum;
		var prsVal = parseInt(allNum);

		if (chkVal != "" && !(prsVal >= minval && prsVal <= maxval))
		{
			alertsay = "Please enter a value greater than or "
			alertsay = alertsay + "equal to \"" + minval + "\" and less than or "
			alertsay = alertsay + "equal to \"" + maxval + "\" in the \"" + checkStr.name + "\" field."
			alert(alertsay);

			return (false);
		}
}

function formatToNumber(fieldname) {
     fieldname.value=filterNum(fieldname.value);

     function filterNum(str) {
          return replaceAll(str, ",", "");
     }
}

function formatStringToNumber(s) {
          return replaceAll(s, ",", "");
}

function replaceAll (s, fromStr, toStr)
{
	var new_s = s;

	for (i = 0; i < 100 && new_s.indexOf (fromStr) != -1; i++)
	{
		new_s = new_s.replace (fromStr, toStr);
	}

	return new_s;
}

function newAgreements(form, obj)  {
	// alert(obj.value);
	var agreementPk = obj.value;

	form.agreementPk.value=agreementPk;
	form.selectAgreement.value=agreementPk;
        form.submit();
}

function round_decimals(original_number, decimals) {
    var result1 = original_number * Math.pow(10, decimals);
    var result2 = Math.round(result1);
    var result3 = result2 / Math.pow(10, decimals);

    return addSeparatorsNF(pad_with_zeros(result3, decimals), '.', '.', ',');
}

function addSeparatorsNF(nStr, inD, outD, sep)
{
	nStr += '';
	var dpos = nStr.indexOf(inD);
	var nStrEnd = '';
	if (dpos != -1) {
		nStrEnd = outD + nStr.substring(dpos + 1, nStr.length);
		nStr = nStr.substring(0, dpos);
	}
	var rgx = /(\d+)(\d{3})/;
	while (rgx.test(nStr)) {
		nStr = nStr.replace(rgx, '$1' + sep + '$2');
	}
	return nStr + nStrEnd;
}

function pad_with_zeros(rounded_value, decimal_places) {
  // Convert the number to a string
  var value_string = rounded_value.toString()
  // Locate the decimal point
  var decimal_location = value_string.indexOf(".")
  // Is there a decimal point?
  if (decimal_location == -1) {
  // If no, then all decimal places will be padded with 0s
  decimal_part_length = 0
  // If decimal_places is greater than zero, tack on a decimal point
  value_string += decimal_places > 0 ? "." : ""
  }
  else {
	// If yes, then only the extra decimal places will be padded with 0s
	decimal_part_length = value_string.length - decimal_location - 1
  }

  // Calculate the number of decimal places that need to be padded with 0s
  var pad_total = decimal_places - decimal_part_length
  if (pad_total > 0) {
	// Pad the string with 0s
	for (var counter = 1; counter <= pad_total; counter++)
	value_string += "0"
  }
	return value_string
}
