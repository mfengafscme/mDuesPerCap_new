function checkExistingYear() {  
	if (document.ViewDataEntryForm.elements["viewYear"].value == "")  {
		alert("No year is specified for viewing.");
		
		return false;
	}
}

function checkAddYear() {  
	if (document.AddDataEntryForm.elements["addYear"].value == "")  {
		alert("No year to add.");
		return false;
	}
	
	checkActive();
}

function checkActive() {  
	if ((document.AddDataEntryForm.elements["empActive"].value == "No") 
		|| (document.AddDataEntryForm.elements["empActive"].value == "no")) {
		alert("This is not an active employer and new Data Entry Form cannot be added.");
		return false;
	}
}