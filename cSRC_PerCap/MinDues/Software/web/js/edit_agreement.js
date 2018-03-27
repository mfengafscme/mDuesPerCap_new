
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

function confirmDeleteAgreement() {
	input_box=confirm("Are you sure you want to delete the agreement");
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

function checkUncheckAll1(Obj)
{
	if (document.AgreementForm.elements["selectedItems1"] == null)
		return false;

	var moreThanOne = 0;

	if( document.AgreementForm.elements["selectedItems1"].value == null) {
		moreThanOne = 1;
	}
	else {
		moreThanOne = 0;
	}

	if (moreThanOne == "1") {
		var selectedItems1Length = document.AgreementForm.elements["selectedItems1"].length;

		if (Obj.checked == true) {

			for (i = 0; i < selectedItems1Length; i++) {
				document.AgreementForm.elements["selectedItems1"][i].checked = true;
			}
		}
		else {

			for (i = 0; i < selectedItems1Length; i++) {
				document.AgreementForm.elements["selectedItems1"][i].checked = false;
			}
		}
	}
	else {
	   	if (Obj.checked == true) {
			document.AgreementForm.elements["selectedItems1"].checked = true;
		}
		else {
			document.AgreementForm.elements["selectedItems1"].checked = false;
		}
	}
}

function checkUncheckAll2(Obj)
{
	if (document.AgreementForm.elements["selectedItems2"] == null)
		return false;

	var moreThanOne = 0;

	if( document.AgreementForm.elements["selectedItems2"].value == null) {
		moreThanOne = 1;
	}
	else {
		moreThanOne = 0;
	}

	if (moreThanOne == "1") {
		var selectedItems2Length = document.AgreementForm.elements["selectedItems2"].length;

		if (Obj.checked == true) {

			for (i = 0; i < selectedItems2Length; i++) {
				document.AgreementForm.elements["selectedItems2"][i].checked = true;
			}
		}
		else {

			for (i = 0; i < selectedItems2Length; i++) {
				document.AgreementForm.elements["selectedItems2"][i].checked = false;
			}
		}
	}
	else {
	   	if (Obj.checked == true) {
			document.AgreementForm.elements["selectedItems2"].checked = true;
		}
		else {
			document.AgreementForm.elements["selectedItems2"].checked = false;
		}
	}

}

