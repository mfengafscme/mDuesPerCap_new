function initForm() {   
    document.EditDataEntryForm.elements["personName"].style.background = '#FFFFC6';  
      
    if (document.EditDataEntryForm.elements["h_correspondence"].value == "1") {
       document.EditDataEntryForm.elements["correspondence"].checked = true;
       document.EditDataEntryForm.elements["correspondenceDate"].disabled = false;
       document.EditDataEntryForm.elements["correspondenceDate"].style.background = '#FFFFC6'; 
    }
    else {  
       document.EditDataEntryForm.elements["correspondence"].checked = false;
       document.EditDataEntryForm.elements["correspondenceDate"].disabled = true;
       document.EditDataEntryForm.elements["correspondenceDate"].value = "";
       document.EditDataEntryForm.elements["correspondenceDate"].style.background = '#C0C0C0';
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
    
    if (document.EditDataEntryForm.elements["h_agreementReceived"].value == "0")  {
    	document.EditDataEntryForm.elements["agreementReceived"].checked = false;
    }
    else { 
   	document.EditDataEntryForm.elements["agreementReceived"].checked = true;
    }
    
    if (document.EditDataEntryForm.elements["secType"].value == "A") {
    	document.EditDataEntryForm.elements["averageWage"].disabled = true;
    	document.EditDataEntryForm.elements["averageWage"].style.background = '#C0C0C0';
	
	document.EditDataEntryForm.elements["percentWageIncList[0].percent_a"].style.background = '#FFFFC6';
	document.EditDataEntryForm.elements["percentWageIncList[0].effective_a"].style.background = '#FFFFC6';
	document.EditDataEntryForm.elements["percentWageIncList[0].noOfMember_a"].style.background = '#FFFFC6';
    }
    else {
    	document.EditDataEntryForm.elements["averageWage"].disabled = false;
    	document.EditDataEntryForm.elements["averageWage"].style.background = '#FFFFC6';        

	document.EditDataEntryForm.elements["initAmount_b"].style.background = '#FFFFC6';
	document.EditDataEntryForm.elements["initEffective_b"].style.background = '#FFFFC6';
	document.EditDataEntryForm.elements["initNoOfMember_b"].style.background = '#FFFFC6';
    }
    
}

function manipulateCorrespondenceDate() {   

    if (document.EditDataEntryForm.elements["correspondence"].checked == false) {
       document.EditDataEntryForm.elements["correspondenceDate"].disabled = true;
       document.EditDataEntryForm.elements["correspondenceDate"].value = "";
       document.EditDataEntryForm.elements["correspondenceDate"].style.background = '#C0C0C0';
    }
    else {
       document.EditDataEntryForm.elements["correspondenceDate"].disabled = false;
       document.EditDataEntryForm.elements["correspondenceDate"].style.background = '#FFFFC6';    
    }
    
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

/*
function compareWithStat() {
	var noMember = document.EditDataEntryForm.elements["numberOfMember"].value - 0;
	var stat = document.EditDataEntryForm.elements["h_statAverage"].value - 0;
	
	if ((noMember > stat) && (stat != 0)) {
		alert("The Total Number of Members and Fee Payers you entered should be less or equal to the STAT Average!");
	}
}
*/

function fillLastRowNoOfMemberA(ct) {
   var tmp1 = 0;
   var tmp2 = 0;
   var sum = 0; 
   
   tmp1 = document.EditDataEntryForm.elements["numberOfMember"].value;
   if (tmp1 == null) {
   	tmp1 = 0;
   }
   else {
   	tmp1 = tmp1 - 0;
   }
   
   for (var i = 0; i < ct; i++) 
   {
   	tmp2 = document.EditDataEntryForm.elements["percentWageIncList["+i+"].noOfMember_a"].value;
	tmp2 = tmp2 - 0;
 	sum = sum + tmp2 - 0;
   }
   
   if (tmp1 - sum - 0) {
    	document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value = tmp1 - sum - 0;
   }
   else {
    	document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value = "";
 	alert("Warning: Calculator cannot be used because the sum of Members Affected is not equal to the Total Number of Members and Fee Payers!");
   }
   
   calMemberTimesIncALoop();
}

function fillLastRowNoOfMemberB(ct) {
   var tmp1 = 0;
   var tmp2 = 0;
   var sum = 0; 
   
   tmp1 = document.EditDataEntryForm.elements["numberOfMember"].value;
   if (tmp1 == null)
   	tmp1 = 0;
   else {
   	tmp1 = tmp1 - 0;
   }
   
   for (var i = 0; i < ct; i++) 
   {
   	tmp2 = document.EditDataEntryForm.elements["amountWageIncList["+i+"].noOfMember_b"].value;
	tmp2 = tmp2 - 0;
 	sum = sum + tmp2 - 0;
   };
   
   if (tmp1 - sum - 0 > 0)
    	document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value = tmp1 - sum - 0;
   else {
    	document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value = "";
 	alert("Warning: Calculator cannot be used because the sum of Members Affected is not equal to the Total Number of Members and Fee Payers!");
   }
   
   calMemberTimesIncBLoop();
}

function calMemberTimesIncALoop() {
  for (ct = 0; ct <= 15; ct++)
  {
    if (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
  	var m1ALoop = formatStringToNumber(document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value);
  	var inc1ALoop = formatStringToNumber(document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value);

  	var m2ALoop = formatStringToNumber(document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].value);
  	var inc2ALoop = formatStringToNumber(document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].value);

  	document.EditDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].value = round_decimals(m1ALoop*inc1ALoop + m2ALoop*inc2ALoop, 3);
    }
  }
}

function calMemberTimesIncBLoop() {
  for (ct = 0; ct <= 15; ct++)
  {
    if (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) {
	  var m1BLoop = formatStringToNumber(document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].value);
	  var inc1BLoop = formatStringToNumber(document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value);

	  var m2BLoop = formatStringToNumber(document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].value);
	  var inc2BLoop = formatStringToNumber(document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].value);

	  document.EditDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].value = round_decimals(m1BLoop*inc1BLoop + m2BLoop*inc2BLoop, 3);
    }
  }
}

function manipulateStartEndDate() {
  if (document.EditDataEntryForm.inNegotiations[0].checked == true) {

    document.EditDataEntryForm.elements["durationFrom"].disabled = false;
    document.EditDataEntryForm.elements["durationTo"].disabled = false;
    
    document.EditDataEntryForm.elements["durationFrom"].style.background = 'white';
    document.EditDataEntryForm.elements["durationTo"].style.background = 'white';
    
    document.EditDataEntryForm.elements["averageWage"].disabled = false;
    document.EditDataEntryForm.elements["averageWage"].style.background = '#FFFFC6';
    
    enableSectionAOnly();
    disableSectionBOnly();
  }
  else if (document.EditDataEntryForm.inNegotiations[1].checked == true) {
    document.EditDataEntryForm.elements["durationFrom"].value = "";
    document.EditDataEntryForm.elements["durationTo"].value = ""; 
    document.EditDataEntryForm.elements["averageWage"].value = "";
    
    document.EditDataEntryForm.elements["durationFrom"].disabled = true;
    document.EditDataEntryForm.elements["durationTo"].disabled = true;  
    
    document.EditDataEntryForm.elements["durationFrom"].style.background = '#C0C0C0';
    document.EditDataEntryForm.elements["durationTo"].style.background = '#C0C0C0';
    
    document.EditDataEntryForm.elements["averageWage"].disabled = true;
    document.EditDataEntryForm.elements["averageWage"].style.background = 'C0C0C0';
    
    disableSectionAOnly();
    disableSectionBOnly();  
  }
}
 
function disableSectionAOnly() {
  
  for (ct = 0; ct <= 9; ct++)
  {
  	if (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value = "";
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].value = "";
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value = "";
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].value = "";
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].value = "";
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].value = "";
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].value = "";

		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].disabled = true;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].disabled = true;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].disabled = true;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].disabled = true;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].disabled = true;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].disabled = true;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].disabled = true;

		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].style.background = '#C0C0C0';
	}
  }
  
  document.EditDataEntryForm.elements["averageWage"].disabled = false;
  document.EditDataEntryForm.elements["averageWage"].style.background = 'white';
 
}


function disableSectionBOnly() {
  
  for (ct = 0; ct <= 9; ct++)
  {
	if (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) 
	{
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].value = "";
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].value = "";
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value = "";
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].value = "";  
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].value = "";
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].value = "";
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].value = "";

		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].disabled = true;
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].disabled = true;
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].disabled = true;
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].disabled = true;  
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].disabled = true;
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].disabled = true;
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].disabled = true;

		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].style.background = '#C0C0C0';  
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].style.background = '#C0C0C0';
		document.EditDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].style.background = '#C0C0C0';
	}
  }

  document.EditDataEntryForm.elements["averageWage"].disabled = true;
  document.EditDataEntryForm.elements["averageWage"].style.background = '#C0C0C0';
}

function enableSectionAOnly() {
  
  for (ct = 0; ct <= 9; ct++)
  {
  	if (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {

		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].disabled = false;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].disabled = false;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].disabled = false;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].disabled = false;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].disabled = false;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].disabled = false;
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].disabled = false;

		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].style.background = 'white';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].style.background = 'white';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].style.background = 'white';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].style.background = 'white';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].style.background = 'white';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].style.background = 'white';
		document.EditDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].style.background = 'white';
	}
  }
  
  document.EditDataEntryForm.elements["percentWageIncList[0].percent_a"].style.background = '#FFFFC6';
  document.EditDataEntryForm.elements["percentWageIncList[0].effective_a"].style.background = '#FFFFC6';
  document.EditDataEntryForm.elements["percentWageIncList[0].noOfMember_a"].style.background = '#FFFFC6';
	
  document.EditDataEntryForm.elements["averageWage"].disabled = true;
  document.EditDataEntryForm.elements["averageWage"].style.background = '#C0C0C0'; 
}

function checkAllFieldsBeforeSubmit() {
  if (document.EditDataEntryForm.inNegotiations[0].checked == true) 
  {
	var dateFromStr = document.EditDataEntryForm.elements["durationFrom"].value;
	var dateToStr = document.EditDataEntryForm.elements["durationTo"].value;
	var yearVal = document.EditDataEntryForm.elements["viewYear"].value;
	
	var durationFromDate = new Date(dateFromStr.substring(6,10),
                            		dateFromStr.substring(0,2)-1,
                            		dateFromStr.substring(3,5));
	var durationToDate   = new Date(dateToStr.substring(6,10),
                            		dateToStr.substring(0,2)-1,
                            		dateToStr.substring(3,5));
          
	var noMember = document.EditDataEntryForm.elements["numberOfMember"].value - 0;
	var stat = document.EditDataEntryForm.elements["h_statAverage"].value - 0;
	
	if ((noMember > stat) && (stat != 0)) {
		alert("The Total Number of Members and Fee Payers should not exceed the STAT Average.");
		return false;
	}
	
	if ((document.EditDataEntryForm.elements["correspondence"].checked) && (trim(document.EditDataEntryForm.elements["correspondenceDate"].value) == "") ) {
		alert("Since Correspondence is checked, the Correspondence Date field is required.");
	   	return false;
	}	
	else if (trim(document.EditDataEntryForm.elements["personName"].value) == "") {
	   	alert("Person Completing the Form is required (Enter N/A if no name is available).");
	   	return false;
	} 
	else if ((document.EditDataEntryForm.elements["amountWageIncList[0].amountInc_b"] != null) && (trim(document.EditDataEntryForm.elements["averageWage"].value) == "")) {
		alert("Since Sec B is chosen for entering data, the Average Wage field is required.");
		return false;
	}
	else if ((trim(dateFromStr) != "") && (trim(dateToStr) != "") && (durationFromDate > durationToDate)) {
	   alert("Agreement Duration To Date should not be earlier than the From Date.");
	   return false;
	}  	
	else if (document.EditDataEntryForm.elements["secType"].value == "A" )
	{ // sec A
 
	  for (ct = 0; ct <= 15; ct++)
	  {
	  	if (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"] != null) {
			if (validateEffectiveDate(document.EditDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"], "viewYear") == false)
			 	return false;
		}
  	  }
  	  
  	  if ((document.EditDataEntryForm.elements["numberOfMember"] != null) 
	  	      && (trim(document.EditDataEntryForm.elements["numberOfMember"].value) != "") 
	  	      && (trim(document.EditDataEntryForm.elements["numberOfMember"].value) != "0"))
	  { 
  	  	var sumA = 0;
  	  	for (ct = 0; ct <= 15; ct++)
	  	{
	  		var ctPlusOne = ct - 0 + 1;
	  		
			if ( (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"] != null)
				&& (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value != "")
				&& (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null)
				&& (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value != "")
				&& (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value - 0 < 0 ) ) {
				alert("The Number of Member and Fee Payer effected in row " + ctPlusOne + " on the wage increase data list cannot be negative.");
				return false;	  		
			}
			else if ((document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"] != null) 
			    && (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value != "")) {
					sumA = sumA * 1 - 0 + (document.EditDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value - 0);
			}
			  
		}

		if (sumA != (document.EditDataEntryForm.elements["numberOfMember"].value - 0)) {
		  	alert("The sum of Members Affected should be equal to Total Number of Members and Fee Payers.");
		  	return false;
		}
		  
		//if (sumA != (document.EditDataEntryForm.elements["numberOfMember"].value - 0)) {
		//	input_box=confirm("The sum of Members Affected is not equal to" +
		//		" \nthe Total Number of Members and Fee Payers. Still want to submit?");
		//	if (input_box==true)
		//	{ 
			   // Output when OK is clicked
			   // return true;
		//	}
		//	else
		//	{
			   // Output when Cancel is clicked
		//	   return false;
		//	}
		//}
	  }
	}
	else if (document.EditDataEntryForm.elements["secType"].value == "B" )
	{ // Sec B

	  for (ct = 0; ct <= 15; ct++)
	  {
	  	if (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"] != null) {
			if (validateEffectiveDate(document.EditDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"], "viewYear") == false)
			 	return false;
		}
  	  }
  	  
  	  if ((document.EditDataEntryForm.elements["numberOfMember"] != null) 
	  	      && (trim(document.EditDataEntryForm.elements["numberOfMember"].value) != "") 
	  	      && (trim(document.EditDataEntryForm.elements["numberOfMember"].value) != "0") ) 
	  {
	     	var sumB = 0;
	     	
	  	for (ct = 0; ct <= 15; ct++)
	  	{	
	  		var ctPlusOne = ct - 0 + 1;
	  		
	  		if ( (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"] != null)
				&& (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value != "")
				&& (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amount_b"] != null)
				&& (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amount_b"].value != "")
	  			&& (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value - 0 < 0 ) ) {
				alert("The Number of Member and Fee Payer effected in row " + ctPlusOne + " on the wage increase data list cannot be negative.");
				return false;	  		
			}
			else if ((document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"] != null) 
			    && (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].amount_b"].value != "")) {
				sumB = sumB * 1 - 0 + (document.EditDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value - 0);
			}
	  }
  	  		
	  if (sumB != (document.EditDataEntryForm.elements["numberOfMember"].value - 0)) {
		alert("The sum of Members Affected should be equal to Total Number of Members and Fee Payers.");
		return false;
	  }
		  
	  //if (sumB != (document.EditDataEntryForm.elements["numberOfMember"].value - 0)) {
	  //	input_box=confirm("The sum of Members Affected should be equal to Total Number of Members and Fee Payers. Still want to submit?");
	  //	if (input_box==true)
	  //	{ 
		   // Output when OK is clicked
		   // return true;
	  //	}
	  //	else
	  //	{
		   // Output when Cancel is clicked
	  //	   return false;
	  //	}
	  //}

 	  
	  }
	}

   }
   
}
	    
