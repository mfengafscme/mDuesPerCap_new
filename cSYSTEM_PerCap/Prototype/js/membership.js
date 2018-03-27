// Global vars...

	// These are needed for Affiliate Finder
	var typeField;
	var localField;
	var stateField;
	var subunitField;
	var subunitField;
	var aidWin;
	
	// These are needed for the Replace Search Results functionality
	var pkField;
	var firstField;
	var middleField;
	var lastField;
	var suffixField;
	var resultsWin;

	// These are needed for Political Rebate
	var DateDeniedJS = "";
	var ReasonDeniedJS = 0;
	
	var DateResubmitDeniedJS = "";
	var ReasonResubmitDeniedJS = 0;

// functions needed for Affiliate Finder functionality

	/* Accepts various Affiliate Identifier fields and passes the information to a Search 
	 * action that will forward to a jsp to show the results. 
	 * 
	 * TODO: This is only coded for the prototype. This will need to be altered based on the 
	 * actual system.
	 */
	function showAffiliateIDResults(type, local, state, subunit, council) {
		typeField		= type;
		localField		= local;
		stateField		= state;
		subunitField	= subunit;
		councilField	= council;
		
		aidWin = window.open("AffiliateIdentifierFinderResults.htm", "affIDwin", "width=800,height=350,toolbar=yes,resizeable=no,scrollbars=yes");
		aidWin.focus();
	}
	
	/* Used to take a result from the finder, and use it to populate the fields on the 
	 * originating screen.
	 *
	 * This needs to be the link on the JSP returned by the action called in the 
	 * showAffiliateIDResults function above. 
	 */
	function resultsReturn(type, local, state, subunit, council) {
		localField.value	= local;
		subunitField.value	= subunit;
		councilField.value	= council;
		
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

	/* Used to validate Affiliate Identifier required fields with the default names.
	 */	
	function validateAffiliateIDFinderFields(form) {
		return validateAffiliateIDFinderFieldsByFields(form.affiliateType.options[form.affiliateType.selectedIndex].text, form.affiliateLocal.value);
	}

	/* Used to validate Affiliate Identifier required fields.
	 */	
	function validateAffiliateIDFinderFieldsByFields(type, number) {
		if (type == "" || type == "*" || number == "" || number == "****") {
			alert('You must enter an \'Affiliate Identifier Type\' and an \'Affiliate Identifier Number\' before performing this action.');
			return false;
		}
		return true;
	}

// Functions needed for the Replace Officer Search Results functionality.

	
	function showReplaceResults(pk, first, middle, last, suffix) {
		pkField = pk;
		firstField = first;
		middleField = middle;
		lastField = last;
		suffixField = suffix;
		
		resultsWin = window.open("ReplaceOfficerResults.htm", "rrWin", "top=0,left=0,width=1024,height=600,toolbar=yes,resizeable=no,scrollbars=yes");
		resultsWin.focus();
	}
	
	function replaceResultsReturn(pk, first, middle, last, suffix) {
		pkField.value		= pk;
		firstField.value	= first;
		middleField.value	= middle;
		lastField.value		= last;
		
		for (var i = 0; i < suffixField.length; i++) {
			if (suffixField.options[i].text == suffix || suffixField.options[i].value == suffix) {
				suffixField.options[i].selected = true;
			}
		}
		
		resultsWin.close();
	}

// Validation functions

	/* Checks that at least one field has data entered for the search.
	 * 
	 * This will no longer be needed when validation is done by the server.
	 * 
	 * Not used. Should be removed.
	 */
	function validateChangeHistorySearch(form) {
		if ( (form.Section.selectedIndex < 1) && (form.FromDate.value == "") && (form.ToDate.value == "") ) {
			alert("You must enter at least one field to search.");
			return false;
		}
		return true;
	}

	/* Validates Comments in a textarea based on a max size of 255 for all comments. Should 
	 * be changed to read from a configurable source.
	 */	
	function validateComments(field) {
		enforceTextAreaMaxLength(field, 255);
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
	
	
	/* This function will not be needed at all, since there is info that will only be known at 
	 * the server to determine if the President already exists.
	 */
	function validateOnePresident(form) {
		if (form.titles.value == "01") {
			form.NumberOfOfficers.value = 1;
			form.NumberOfOfficers.disabled = true;
		} else {
			form.NumberOfOfficers.disabled = false;
		}
	}
	
	/* Not used. Should be removed.
	 */
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
	
	/* Enables and disables DateDenied and ReasonDenied fields based on the value of the RequestDenied checkbox.
	 */
	function validateRebateDenial(form) {
		if (form.RequestDenied.checked == true) { 
			// enable DateDenied and restore from backup or today's date
			form.DateDenied.disabled = false;
			if (DateDeniedJS == "") {
				form.DateDenied.value = getTodaysDate();
			} else {
				form.DateDenied.value = DateDeniedJS;
			}
			
			// enable ReasonDenied and restore from backup
			form.ReasonDenied.disabled = false;
			form.ReasonDenied.selectedIndex = ReasonDeniedJS;
			
			// enable DeniedCalendar
			DeniedCalendar.disabled = false;
		} else { 
			// disable DateDenied and set backup
			form.DateDenied.disabled = true;
			DateDeniedJS = form.DateDenied.value;
			form.DateDenied.value = "";
			
			// disable ReasonDenied and set backup
			form.ReasonDenied.disabled = true;
			ReasonDeniedJS = form.ReasonDenied.selectedIndex;
			form.ReasonDenied.selectedIndex = 0;
			
			// disable DeniedCalendar
			DeniedCalendar.disabled = true;
		}
	}
	
	/* Enables and disables ResubmissionDenied and DateResubmitDenied fields based on the value of the RequestResubmitted checkbox.
	 */
	function validateRebateResubmitDenial(form) {
		if (form.RequestResubmitted.checked == true) {
			if (form.ResubmissionDenied.disabled == true) {
				// enable ResubmissionDenied and restore from backup
				form.ResubmissionDenied.disabled = false;
				form.ResubmissionDenied.selectedIndex = ReasonResubmitDeniedJS;
			}
			if (form.ResubmissionDenied.selectedIndex > 0) {
				// enable DateResubmitDenied and restore from backup or today's date
				form.DateResubmitDenied.disabled = false;
				if (DateResubmitDeniedJS == "") {
					form.DateResubmitDenied.value = getTodaysDate();
					DateResubmitDeniedJS = form.DateResubmitDenied.value;
				} else {
					form.DateResubmitDenied.value = DateResubmitDeniedJS;
				}
				// enable ResubmitDeniedCalendar
				ResubmitDeniedCalendar.disabled = false;
			} else if (form.DateResubmitDenied.disabled == false) {
				// disable DateResubmitDenied and set backup
				form.DateResubmitDenied.disabled = true;
				DateResubmitDeniedJS = form.DateResubmitDenied.value;
				form.DateResubmitDenied.value = "";
				// disable ResubmitDeniedCalendar
				ResubmitDeniedCalendar.disabled = true;
			}
		} else {
			// disable ResubmissionDenied and set backup
			form.ResubmissionDenied.disabled = true;
			ReasonResubmitDeniedJS = form.ResubmissionDenied.selectedIndex;
			form.ResubmissionDenied.selectedIndex = 0;
			if (form.DateResubmitDenied.disabled == false) {
				// disable DateResubmitDenied and set backup
				form.DateResubmitDenied.disabled = true;
				DateResubmitDeniedJS = form.DateResubmitDenied.value;
				form.DateResubmitDenied.value = "";
			}
			// disable ResubmitDeniedCalendar
			ResubmitDeniedCalendar.disabled = true;
		}
	}
	
	/* Not used. Should be removed.
	 */
	function validateRequiredAffiliateIdentifierFields(form) {
		return validateRequiredAffiliateIdentifierFieldsByFields(form.affiliateType, form.affiliateLocal, form.affiliateCode, form.affiliateState);
	}
	
	/* Not used. Should be removed.
	 */
	function validateRequiredAffiliateIdentifierFieldsByFields(typeField, numberField, codeField, stateField) {
		var type = typeField.options[form.affiliateType.selectedIndex].text;
		var code = codeField.value;
		var state = stateField.options[form.affiliateState.selectedIndex].text;
		var number = numberField.value;
		//if the Type, Code, and/or State is entered, then the Number must contain a value.  
		if ( (type != "" || 
			  code != "" || 
			  state != "") && 
			 number == ""
		) {
			alert('If the Affiliate Identifier\'s Type, Code, or State fields contain data, \n\n' + 
				  'then you must enter a value for the Affiliate Identifier Number.'
			);
			return false;
		}
		return true;
	}

// Other functions here...

	/* Disables a set of Radio Buttons with the name passed in, radioFieldName.
	 */
	function disableRadioSet(radioFieldName) {
		for (var i = 0; i < form.elements.length; i++) {
			var field = form.elements[i];
			if (field.name == radioFieldName) {
				field.disabled = true;
			}
		}
	}
	
	/* Enables a set of Radio Buttons with the name passed in, radioFieldName.
	 */
	function enableRadioSet(radioFieldName) {
		for (var i = 0; i < form.elements.length; i++) {
			var field = form.elements[i];
			if (field.name == radioFieldName) {
				//alert(field.name + " : " + field.value);
				field.disabled = false;
			}
		}
	}
	
	/* Not used. Should be removed.
	 */
	function resetAll() {
		for (var i = 0; i < document.forms.length; i++) {
			form = document.forms[i];
			form.reset();
		}
	}
	
	/* Not used. Should be removed.
	 */
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
	
	/* Used to set cursor focus to the first field on a screen.
	 */
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
	
	/* Returns the index of the default option in a select.
	 */
	function getDefaultSelectedIndex(input) {
		if (input.type != "select-one") {
			return 0;
		}
		for (var i = 0; i < input.options.length; i++) {
			if (input.options[i].defaultSelected) {
				return i;
			}
		}
		return 0;
	}
	
	/* Returns true if the startValue parameter ends with the endValue parameter.
	 */
	function endsWith(startValue, endValue) {
		return startValue.substr(startValue.lastIndexOf(endValue)).length == endValue.length;
	}
	
	/* Forces the cursor to tab to the next field after the passed in field on the form.
	 */
	function forceTab(input) {
		var element = input.form[(getIndex(input)+1) % input.form.length];
		if (element.type == "text" || element.type == "textarea") {
			element.select();
		} else {
			element.focus();
		}
		return true;
	}
	
	/* Disables all text, hidden, and dropdown fields on the form.
	 */
	function disableAllTextFields(form) {
		disableAllTextFieldsWithExemption(form, [], []);
	}
	
	/* Disables all text, hidden, and dropdown fields with the exception of the ones that are passed in as the param, 
	 * exemptFieldMatrix, which is an array of field name/value pairs. Each entry in the array must contain an array 
	 * with two entries as follows: e.g. ['fieldname', fieldvalue]
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
	
	/* Enables or disables all the mail flags on the form based on the value of the NoMail checkbox.
	 *
	 * Probable could be replaced with more generic functions defined in checkbox.js.
	 */
	function setAllMailFlags(form) {
		if (form.NoMail.checked) {
			form.NoCards.checked = true;
			form.NoCards.disabled = true;
			form.NoPublicEmployee.checked = true;
			form.NoPublicEmployee.disabled = true;
			form.NoLegislativeMail.checked = true;
			form.NoLegislativeMail.disabled = true;
		} else {
			form.NoCards.checked = false;
			form.NoCards.disabled = false;
			form.NoPublicEmployee.checked = false;
			form.NoPublicEmployee.disabled = false;
			form.NoLegislativeMail.checked = false;
			form.NoLegislativeMail.disabled = false;
		}
		
	}
	
	/* Enables and disables the param, editField, based on the value of the param, flag.
	 */
	function editOnCheck(flag, editField) {
		editField.disabled = !flag.checked;
	}
	
	/* Blanks the EmployeeSalary if the EmployeeSalaryRange contains a value.
	 * 
	 * This will no longer be needed when validation is done by the server.
	 */
	function cancelActualSalary(form) {
		if (form.EmployeeSalaryRange.selectedIndex > 0) { 
			form.EmployeeSalary.value = "";
		}
	}
	
	/* Blanks the EmployeeSalaryRange if the EmployeeSalary contains a value.
	 * 
	 * This will no longer be needed when validation is done by the server.
	 */
	function cancelSalaryRange(form) {
		if (form.EmployeeSalary.value != null) { 
			form.EmployeeSalaryRange.selectedIndex = 0;
		}
	}
	
	/* Disables and sets the TermLength field to 'Indefinite' if the title field is 'Steward'.
	 */
	function checkSteward(form) {
		if (form.titles.options[form.titles.selectedIndex].text == "Steward") {
			form.TermLength.selectedIndex = getSelectedIndex(form.TermLength, "Indefinite");
			form.TermLength.disabled = true;
		} else {
			form.TermLength.disabled = false;
		}
	}
	
	/* Locks the appropriate Affiliate Identifier fields based on the value for the Affiliate Type field.
	 */
	function lockAffiliateIDFields(form) {
		lockAffiliateIDFieldsByFields(form.affiliateType, form.affiliateLocal, form.affiliateState, form.affiliateSubunit, form.affiliateCouncil);
	}
	
	/* Locks the appropriate Affiliate Identifier fields based on the value for the Affiliate Type field.
	 */
	function lockAffiliateIDFieldsByFields(affilType, affilLocal, affilState, affilSubunit, affilCouncil) {
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
			// set focus to state
			affilState.focus();
		} else if (value == 'L' || value == 'S') {
			// disabled and initialize subunit
			affilSubunit.value = "";
			affilSubunit.disabled = true;
			// enable local and council
			affilLocal.disabled = false;
			affilCouncil.disabled = false;
			// set focus to local
			affilLocal.focus();
		} else {
			// enable all fields
			//affilSubunit.value = "";
			affilSubunit.disabled = false;
			//affilLocal.value = "";
			affilLocal.disabled = false;
			//affilCouncil.value = "";
			affilCouncil.disabled = false;
			// set focus to local
			affilLocal.focus();
		}
	}
	
	/* Returns the selectedIndex in the field that matches the text passed in by the param, text. Returns 0 if the text is not found.
	 */	
	function getSelectedIndex(field, text) {
		for (var i = 0; i < field.options.length; i++) {
			option = field.options[i];
			if (option.text == text || option.value == text) {
				return i;
			}
		}
		return 0;
	}
	
	/* Enforces that the text area in the param, field, is no larger than the value in the param, length.
	 */	
	function enforceTextAreaMaxLength(field, length) {
		if (field.value.length > length) {
			field.value = field.value.substring(0, length);
		}
	}