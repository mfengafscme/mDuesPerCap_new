function initForm() {  
	document.AddEmployerForm.elements["affIdState"].style.background = '#FFFFC6'; 
	document.AddEmployerForm.elements["affilName"].style.background = '#FFFFC6'; 
	document.AddEmployerForm.elements["affIdLocal"].style.background = '#FFFFC6'; 
	//document.AddEmployerForm.elements["affIdCouncil"].style.background = '#FFFFC6'; 
	
	empTypeChanged();
	
	//document.AddEmployerForm.elements["addEmployerButton"].focus();
}

function changeAffIdType() {
	var tmpAffIdSubUnit = document.AddEmployerForm.elements["affIdSubUnit"].value;
	if (trim(tmpAffIdSubUnit) == "") {
		document.AddEmployerForm.elements["affIdType"].value = 'L'; 		
	}
	else {
		document.AddEmployerForm.elements["affIdType"].value = 'U'; 	
	}
}

function empTypeChanged() {
	if (document.AddEmployerForm.elements["affIdType"].value == "U") {
		document.AddEmployerForm.elements["affIdSubUnit"].style.background = '#FFFFC6'; 
		document.AddEmployerForm.elements["affIdSubUnit"].disabled = false;
	}
	else if (document.AddEmployerForm.elements["affIdType"].value == "L") {
		document.AddEmployerForm.elements["affIdSubUnit"].value = ""; 
		document.AddEmployerForm.elements["affIdSubUnit"].disabled = true;
		document.AddEmployerForm.elements["affIdSubUnit"].style.background = '#C0C0C0'; 	
	}
	//else if (document.AddEmployerForm.elements["affIdType"].value == "") {
	//	document.AddEmployerForm.elements["affIdSubUnit"].style.background = 'white';
	//}
}

function checkAllFieldsBeforeSubmit() {
	if ((document.AddEmployerForm.elements["affIdType"].value == "U")
		&& (trim(document.AddEmployerForm.elements["affIdSubUnit"].value) == "")) {
	   alert("Since you chose U as the employer type, subunit field is required.");
	   return false;
	} 
	else if ((document.AddEmployerForm.elements["affIdType"].value == "L")
		&& (trim(document.AddEmployerForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since you chose L as the employer type, subunit field should be empty.");
	   return false;
	} 
	else if ((trim(document.AddEmployerForm.elements["affIdType"].value) == "")
		&& (trim(document.AddEmployerForm.elements["affIdSubUnit"].value) != "")) {
	   alert("Since subunit field is not empty, you should chose U as the employer type.");
	   return false;
	} 
	else if (document.AddEmployerForm.elements["affIdState"].value == "") {
	   alert("State field is required.");
	   return false;
	} 
	else if (trim(document.AddEmployerForm.elements["affIdLocal"].value) == "") {
	   alert("Local field is required.");
	   return false;
	} 
	//else if (trim(document.AddEmployerForm.elements["affIdCouncil"].value) == "") {
	//   alert("Council field is required.");
	//   return false;
	//} 
	else if (trim(document.AddEmployerForm.elements["affilName"].value) == "") {
	   alert("Employer name field is required.");
	   return false;
	} 
	else {
	   return true;
	} 	
}
	    
