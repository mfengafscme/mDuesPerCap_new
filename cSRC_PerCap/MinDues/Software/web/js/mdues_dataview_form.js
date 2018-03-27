// envoke when form load
function initForm() {   
   
    var tmpAmountType = document.EditDataEntryForm.elements["h_amountType"].value;    
    var viewYear = document.EditDataEntryForm.elements["viewYear"].value;
    var right_now = new Date();
    var currentDuesYear = right_now.getYear() + 1;
    
    if (tmpAmountType == "dollar/yr") {
    	document.EditDataEntryForm.amountType[1].checked = true;
    }
   
    if (currentDuesYear > viewYear) {
        // disable delete or edit buttons
  	document.EditDataEntryForm.elements["editButton"].disabled = true;
  	document.EditDataEntryForm.elements["deleteButton"].disabled = true;
    }
    else {
    	// enable delete or edit buttons
    	document.EditDataEntryForm.elements["editButton"].disabled = false;
  	document.EditDataEntryForm.elements["deleteButton"].disabled = false;  	
    }

    if (document.EditDataEntryForm.elements["h_inNegotiations"].value == "0") {
	document.EditDataEntryForm.inNegotiations[0].checked = true;
	document.EditDataEntryForm.inNegotiations[1].checked = false;
    }
    else {
	document.EditDataEntryForm.inNegotiations[0].checked = false;
	document.EditDataEntryForm.inNegotiations[1].checked = true;
    }
    
    if (document.EditDataEntryForm.elements["h_formCompleted"].value == "0") {
    	document.EditDataEntryForm.elements["formCompleted"].checked = false;
    }
    else {
    	document.EditDataEntryForm.elements["formCompleted"].checked = true;
    }
  
    if (document.EditDataEntryForm.elements["h_agreementReceived"].value == "0") {
    	document.EditDataEntryForm.elements["agreementReceived"].checked = false;
    }
    else {
    	document.EditDataEntryForm.elements["agreementReceived"].checked = true;
    }
    
    if (document.EditDataEntryForm.elements["h_correspondence"].value == "0") {
	document.EditDataEntryForm.elements["correspondence"].checked = false;
    }
    else {
    	document.EditDataEntryForm.elements["correspondence"].checked = true;
    }
    
    document.EditDataEntryForm.inNegotiations[0].disabled = true;
    document.EditDataEntryForm.inNegotiations[1].disabled = true;
    document.EditDataEntryForm.elements["formCompleted"].disabled = true;
    document.EditDataEntryForm.elements["agreementReceived"].disabled = true;
    document.EditDataEntryForm.elements["correspondence"].disabled = true;
}

function confirmDeleteEmpYear() {
	input_box=confirm("Are you sure you want to delete this employer's wage increase data for the selected year?");
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

function checkActive() {  
	if ((document.EditDataEntryForm.elements["empActive"].value == "No") || 
	(document.EditDataEntryForm.elements["empActive"].value == "no")) {
		alert("This is not an active employer and the existing Data Entry Forms are not editable.");
		return false;
	}
}