function initForm() {  
	document.AgreementForm.elements["agreementName"].style.background = '#FFFFC6';
	
	//document.AgreementForm.elements["agreementName"].value = ""; 
	//document.AgreementForm.elements["startDate"].value = ""; 
	//document.AgreementForm.elements["endDate"].value = ""; 
	//document.AgreementForm.elements["comments"].value = ""; 
}

function checkAllFieldsBeforeSubmit() {
	var startDateStr = document.AgreementForm.elements["startDate"].value;
	var endDateStr = document.AgreementForm.elements["endDate"].value;

	
	var startDateDD = new Date(startDateStr.substring(6,10),
                            		startDateStr.substring(0,2)-1,
                            		startDateStr.substring(3,5));
	var endDateDD   = new Date(endDateStr.substring(6,10),
                            		endDateStr.substring(0,2)-1,
                            		endDateStr.substring(3,5));
                            		
	if (trim(document.AgreementForm.elements["agreementName"].value) == "") {
	   alert("Agreement Name field is required.");
	   return false;
	} 
	else if ((trim(startDateStr) != "") && (trim(endDateStr) != "") && (startDateDD > endDateDD)) {
	   alert("Agreement Ending date should be earlier than the Starting Date.");
	   return false;
	} 
	else {
	   return true;
	} 	
}