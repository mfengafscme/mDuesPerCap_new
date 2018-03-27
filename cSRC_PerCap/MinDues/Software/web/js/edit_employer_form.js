function initForm() { 
	document.EditDelEmployerForm.elements["affIdState"].style.background = '#FFFFC6'; 
	document.EditDelEmployerForm.elements["employerName"].style.background = '#FFFFC6'; 
	//document.EditDelEmployerForm.elements["affIdCouncil"].style.background = '#FFFFC6'; 
	document.EditDelEmployerForm.elements["affIdLocal"].style.background = '#FFFFC6'; 
	
	empTypeChanged();
}

function empTypeChanged() {
	if (document.EditDelEmployerForm.elements["affIdType"].value == "U") {
		document.EditDelEmployerForm.elements["affIdSubUnit"].style.background = '#FFFFC6'; 		
	}
	else if (document.EditDelEmployerForm.elements["affIdType"].value == "L") {
		document.EditDelEmployerForm.elements["affIdSubUnit"].value = ""; 
		document.EditDelEmployerForm.elements["affIdSubUnit"].style.background = '#C0C0C0'; 	
	}
	else if (document.EditDelEmployerForm.elements["affIdType"].value == "") {
		document.EditDelEmployerForm.elements["affIdSubUnit"].style.background = 'white';
	}
}

function checkAllFieldsBeforeSubmit() {
	if ((document.EditDelEmployerForm.elements["affIdType"].value == "U")
		&& (trim(document.EditDelEmployerForm.elements["affIdSubUnit"].value) == "")) {
	   alert("Since you chose U as the employer type, subunit field is required.");
	   return false;
	} 
	else if ((document.EditDelEmployerForm.elements["affIdType"].value == "L")
		&& (trim(document.EditDelEmployerForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since you chose L as the employer type, subunit field should be empty.");
	   return false;
	} 
	else if ((trim(document.EditDelEmployerForm.elements["affIdType"].value) == "")
		&& (trim(document.EditDelEmployerForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since subunit field is not empty, you should chose U as the employer type.");
	   return false;
	} 
	else if (document.EditDelEmployerForm.elements["affIdState"].value == "") {
	   alert("State field is required.");
	   return false;
	} 
	else if (trim(document.EditDelEmployerForm.elements["affIdLocal"].value) == "") {
	   alert("Local field is required.");
	   return false;
	} 
	//else if (trim(document.EditDelEmployerForm.elements["affIdCouncil"].value) == "") {
	//   alert("Council field is required.");
	//   return false;
	//} 
	else if (trim(document.EditDelEmployerForm.elements["employerName"].value) == "") {
	   alert("Employer name field is required.");
	   return false;
	} 
	else {
	   return true;
	} 	
}