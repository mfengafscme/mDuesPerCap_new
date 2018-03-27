// envoke when form load
//function initForm() {
//    document.affiliateSearchForm.elements["affIdType"].value = "";
//    document.affiliateSearchForm.elements["affIdLocal"].value = "";
//    document.affiliateSearchForm.elements["affIdState"].value = "";
//    document.affiliateSearchForm.elements["affIdCouncil"].value = "";
//    document.affiliateSearchForm.elements["affIdSubUnit"].value = "";
//}

// envoke when form load
function checkAllFieldsBeforeSubmit() {

	if (	trim(document.affiliateSearchForm.elements["affIdType"].value) 	== "" &&
		trim(document.affiliateSearchForm.elements["affIdState"].value) 	== "" &&
		trim(document.affiliateSearchForm.elements["affIdLocal"].value) 	== "" &&
		trim(document.affiliateSearchForm.elements["affIdSubUnit"].value) 	== "" &&
		trim(document.affiliateSearchForm.elements["affIdCouncil"].value) 	== "" )
	{
		alert("You must enter at least one of the fields to search employers.");
        	return false;
	}

	if (
		(trim(document.affiliateSearchForm.elements["employerNm"].value).length > 0) && (trim(document.affiliateSearchForm.elements["employerNm"].value).length < 3)
		)
	{
		alert("Employer Name field must contain more than two characters.");
        	return false;
	}

}