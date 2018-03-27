function initForm() {  
	document.DataEntryForm.elements["affIdState"].style.background = '#FFFFC6'; 
	document.DataEntryForm.elements["employerName"].style.background = '#FFFFC6'; 
	document.DataEntryForm.elements["affIdLocal"].style.background = '#FFFFC6'; 
	//document.DataEntryForm.elements["affIdCouncil"].style.background = '#FFFFC6'; 
	document.DataEntryForm.elements["year"].style.background = '#FFFFC6';
	
	empTypeChanged();
}

function changeAffIdType() {
	var tmpAffIdSubUnit = document.DataEntryForm.elements["affIdSubUnit"].value;
	if (trim(tmpAffIdSubUnit) == "") {
		document.DataEntryForm.elements["affIdType"].value = 'L'; 		
	}
	else {
		document.DataEntryForm.elements["affIdType"].value = 'U'; 	
	}
}


function empTypeChanged() {
	if (document.DataEntryForm.elements["affIdType"].value == "U") {
		document.DataEntryForm.elements["affIdSubUnit"].style.background = '#FFFFC6'; 
		document.DataEntryForm.elements["affIdSubUnit"].disabled = false;
	}
	else if (document.DataEntryForm.elements["affIdType"].value == "L") {
		document.DataEntryForm.elements["affIdSubUnit"].value = ""; 
		document.DataEntryForm.elements["affIdSubUnit"].disabled = true;
		document.DataEntryForm.elements["affIdSubUnit"].style.background = '#C0C0C0'; 	
	}
	//else if (document.DataEntryForm.elements["affIdType"].value == "") {
	//	document.DataEntryForm.elements["affIdSubUnit"].style.background = 'white';
	//}
}

function checkAllFieldsBeforeSubmit() {

	if ((document.DataEntryForm.elements["year"].value == "")) {
	   alert("Year field is required.");
	   return false;
	} 
	if ((document.DataEntryForm.elements["affIdType"].value == "U")
		&& (document.DataEntryForm.elements["affIdSubUnit"].value == "")) {
	   alert("Since you chose U as the employer type, subunit field is required.");
	   return false;
	} 
	if ((document.DataEntryForm.elements["affIdType"].value == "L")
		&& (trim(document.DataEntryForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since you chose L as the employer type, subunit field should be empty.");
	   return false;
	} 
	else if (document.DataEntryForm.elements["affIdState"].value == "") {
	   alert("State field is required.");
	   return false;
	} 
	else if (document.DataEntryForm.elements["affIdLocal"].value == "") {
	   alert("Local field is required.");
	   return false;
	} 
	//else if (document.DataEntryForm.elements["affIdCouncil"].value == "") {
	//   alert("Council field is required.");
	//   return false;
	//} 
	else if (document.DataEntryForm.elements["employerName"].value == "") {
	   alert("Employer Name field is required.");
	   return false;
	} 	
	else {
	   return true;
	}
}


function checkAllFieldsBeforeAddEmployer() {

	if ((document.DataEntryForm.elements["affIdType"].value == "U")
		&& (trim(document.DataEntryForm.elements["affIdSubUnit"].value) == "")) {
	   alert("Since you chose U as the employer type, subunit field is required.");
	   return false;
	} 
	else if ((document.DataEntryForm.elements["affIdType"].value == "L")
		&& (trim(document.DataEntryForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since you chose L as the employer type, subunit field should be empty.");
	   return false;
	} 
	else if ((trim(document.DataEntryForm.elements["affIdType"].value) == "")
		&& (trim(document.DataEntryForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since subunit field is not empty, you should chose U as the employer type.");
	   return false;
	} 
	else if (document.DataEntryForm.elements["affIdState"].value == "") {
	   alert("State field is required.");
	   return false;
	} 
	else if (trim(document.DataEntryForm.elements["affIdLocal"].value) == "") {
	   alert("Local field is required.");
	   return false;
	} 
	//else if (trim(document.DataEntryForm.elements["affIdCouncil"].value) == "") {
	//   alert("Council field is required.");
	//   return false;
	//} 
	else if (trim(document.DataEntryForm.elements["affilName"].value) == "") {
	   alert("Employer name field is required.");
	   return false;
	} 
	else {
	   return true;
	} 
}
