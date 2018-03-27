//
// function for clear the minimumdues data entry form
//

// envoke when form load
function initForm() {
    var tmpSec = document.AddDataEntryForm.elements["secType"].value;

    manipulateCorrespondenceDate();

    document.AddDataEntryForm.elements["personName"].style.background = '#FFFFC6';


    if (tmpSec == "B" ) {
       disableSectionA();
       document.AddDataEntryForm.elements["averageWage"].disabled = false;
       document.AddDataEntryForm.elements["averageWage"].style.background = '#FFFFC6';
       document.AddDataEntryForm.section[1].checked = true;

       document.AddDataEntryForm.elements["initAmount_b"].style.background = '#FFFFC6';
       document.AddDataEntryForm.elements["initEffective_b"].style.background = '#FFFFC6';
       document.AddDataEntryForm.elements["initNoOfMember_b"].style.background = '#FFFFC6';
    }
    else if (tmpSec == "A" ) {
       disableSectionB();
       document.AddDataEntryForm.elements["averageWage"].disabled = true;
       document.AddDataEntryForm.elements["averageWage"].style.background = '#C0C0C0';
       document.AddDataEntryForm.section[0].checked = true;

       document.AddDataEntryForm.elements["initPercent_a"].style.background = '#FFFFC6';
       document.AddDataEntryForm.elements["initEffective_a"].style.background = '#FFFFC6';
       document.AddDataEntryForm.elements["initNoOfMember_a"].style.background = '#FFFFC6';
    }
    else {
       disableSectionB();
       document.AddDataEntryForm.elements["averageWage"].disabled = true;
       document.AddDataEntryForm.elements["averageWage"].style.background = '#C0C0C0';
       document.AddDataEntryForm.section[0].checked = true;
       //clearForm();
    }

    if (document.AddDataEntryForm.inNegotiations[1].checked == true) {
        document.AddDataEntryForm.elements["durationFrom"].disabled = true;
        document.AddDataEntryForm.elements["durationTo"].disabled = true;

        document.AddDataEntryForm.elements["durationFrom"].style.background = '#C0C0C0';
        document.AddDataEntryForm.elements["durationTo"].style.background = '#C0C0C0';
    }
    else if (document.AddDataEntryForm.inNegotiations[0].checked == true) {
        document.AddDataEntryForm.elements["durationFrom"].disabled = false;
        document.AddDataEntryForm.elements["durationTo"].disabled = false;

        document.AddDataEntryForm.elements["durationFrom"].style.background = 'white';
        document.AddDataEntryForm.elements["durationTo"].style.background = 'white';
    }

}

function manipulateCorrespondenceDate() {

    if (document.AddDataEntryForm.elements["correspondence"].checked == false) {
       document.AddDataEntryForm.elements["correspondenceDate"].disabled = true;
       document.AddDataEntryForm.elements["correspondenceDate"].value = "";
       document.AddDataEntryForm.elements["correspondenceDate"].style.background = '#C0C0C0';
    }
    else {
       document.AddDataEntryForm.elements["correspondenceDate"].disabled = false;
       document.AddDataEntryForm.elements["correspondenceDate"].style.background = '#FFFFC6';
    }

}

function confirmDeleteEmpYear()
{
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
function compareWithStat()
{
	var numberOfMember = document.AddDataEntryForm.elements["numberOfMember"].value;
	var stat = document.AddDataEntryForm.elements["h_statAverage"].value;
	numberOfMember = numberOfMember - 0;
	stat = stat - 0;

	if ((numberOfMember > stat) && (stat != 0)) {
		alert("The Total Number of Members and Fee Payers you entered should be less or equal to the STAT Average!");
	}
}
*/


function fillLastRowNoOfMemberA(ct) {
   var tmp1 = 0;
   var tmp2 = 0;
   var tmp3 = 0;
   var sum = 0;

   tmp1 = document.AddDataEntryForm.elements["numberOfMember"].value;
   tmp1 = tmp1 - 0;
   tmp3 = document.AddDataEntryForm.elements["initNoOfMember_a"].value;
   tmp3 = tmp3 - 0;

   for (var i = 0; i < ct; i++)
   {
   	tmp2 = document.AddDataEntryForm.elements["percentWageIncList["+i+"].noOfMember_a"].value;
	tmp2 = tmp2 - 0;
 	sum = sum + tmp2 - 0;
   };

   if (tmp1 - sum - tmp3 - 0 > 0)
   	document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value = tmp1 - sum - tmp3 - 0;
   else {
   	document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value = "";
	alert("Warning: Calculator cannot be used because the sum of Members Affected exceeds Total Number of Members and Fee Payers!");
   }

   calMemberTimesIncALoop();
}

function fillLastRowNoOfMemberB(ct) {
   var tmp1 = 0;
   var tmp2 = 0;
   var tmp3 = 0;
   var sum = 0;

   tmp1 = document.AddDataEntryForm.elements["numberOfMember"].value;
   tmp1 = tmp1 - 0;
   tmp3 = document.AddDataEntryForm.elements["initNoOfMember_b"].value;
   tmp3 = tmp3 - 0;

   for (var i = 0; i < ct; i++)
   {
   	tmp2 = document.AddDataEntryForm.elements["amountWageIncList["+i+"].noOfMember_b"].value;
	tmp2 = tmp2 - 0;
 	sum = sum + tmp2 - 0;
   };

   if (tmp1 - sum - tmp3 - 0 > 0)
    	document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value = tmp1 - sum - tmp3 - 0;
   else {
    	document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value = "";
 	alert("Warning: Calculator cannot be used because the sum of Members Affected exceeds Total Number of Members and Fee Payers!");
   }

   calMemberTimesIncBLoop();
}

function calInitMemberTimesIncA() {
	if (document.AddDataEntryForm.elements["initPercent_a"] != null) {
	  var m1A = formatStringToNumber(document.AddDataEntryForm.elements["initPercent_a"].value);
	  var inc1A = formatStringToNumber(document.AddDataEntryForm.elements["initNoOfMember_a"].value);

	  var m2A = formatStringToNumber(document.AddDataEntryForm.elements["initPercentInc_adj_a"].value);
	  var inc2A = formatStringToNumber(document.AddDataEntryForm.elements["initNoOfMember_adj_a"].value);

	  document.AddDataEntryForm.elements["initMbrTimesInc_a"].value = round_decimals(m1A*inc1A + m2A*inc2A, 3);
	}
}

function calMemberTimesIncALoop() {
  for (ct = 0; ct <= 15; ct++)
  {
  	if (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
	  var m1ALoop = formatStringToNumber(document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value);
	  var inc1ALoop = formatStringToNumber(document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value);

	  var m2ALoop = formatStringToNumber(document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].value);
	  var inc2ALoop = formatStringToNumber(document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].value);

	  document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].value = round_decimals(m1ALoop*inc1ALoop + m2ALoop*inc2ALoop, 3);
	}
  }
}

function calInitMemberTimesIncB() {
	//if (document.AddDataEntryForm.elements["initAmount_b"] != null) {
	// var m1B = formatStringToNumber(document.AddDataEntryForm.elements["initAmount_b"].value);
	// var inc1B = formatStringToNumber(document.AddDataEntryForm.elements["initNoOfMember_b"].value);

	// var m2B = formatStringToNumber(document.AddDataEntryForm.elements["initAmountInc_adj_b"].value);
	// var inc2B = formatStringToNumber(document.AddDataEntryForm.elements["initNoOfMember_adj_b"].value);

	//  document.AddDataEntryForm.elements["initMbrTimesInc_b"].value = round_decimals(m1B*inc1B + m2B*inc2B, 3);
	//}

	if ((document.AddDataEntryForm.elements["initAmount_b"] != null) && (document.AddDataEntryForm.elements["averageWage"] != null) && (document.AddDataEntryForm.elements["averageWage"].value != 0)){
	  var AveWage = formatStringToNumber(document.AddDataEntryForm.elements["averageWage"].value);
	  var m1B = formatStringToNumber(document.AddDataEntryForm.elements["initAmount_b"].value);
	  var inc1B = formatStringToNumber(document.AddDataEntryForm.elements["initNoOfMember_b"].value);

	  var m2B = formatStringToNumber(document.AddDataEntryForm.elements["initAmountInc_adj_b"].value);
	  var inc2B = formatStringToNumber(document.AddDataEntryForm.elements["initNoOfMember_adj_b"].value);

	  document.AddDataEntryForm.elements["initMbrTimesInc_b"].value = round_decimals(((m1B*inc1B)+(m2B*inc2B))*100/AveWage, 3);
	}

	if ((document.AddDataEntryForm.elements["averageWage"] == null) || (document.AddDataEntryForm.elements["averageWage"].value.length == 0)) {
		document.AddDataEntryForm.elements["initMbrTimesInc_b"].value = "";
	}

}

function calMemberTimesIncBLoop() {
  //for (ct = 0; ct <= 15; ct++)
  //{
  //   if (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) {
  //	  var m1BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].value);
  //	  var inc1BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value);

  //	  var m2BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].value);
  //	  var inc2BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].value);

  //	  document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].value = round_decimals(m1BLoop*inc1BLoop + m2BLoop*inc2BLoop, 3);
  //	}
  // }

  var AveWageLoop = formatStringToNumber(document.AddDataEntryForm.elements["averageWage"].value);

  for (ct = 0; ct <= 15; ct++)
  {
     if ( (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) && (document.AddDataEntryForm.elements["averageWage"] != null) && (document.AddDataEntryForm.elements["averageWage"].value != 0) ) {
	  var m1BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].value);
	  var inc1BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value);

	  var m2BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].value);
	  var inc2BLoop = formatStringToNumber(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].value);

	  document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].value = round_decimals(((m1BLoop*inc1BLoop + m2BLoop*inc2BLoop))*100/AveWageLoop, 3);
	 }

     if ((document.AddDataEntryForm.elements["averageWage"] == null) || (document.AddDataEntryForm.elements["averageWage"].value.length == 0)) {
			document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].value = "";
	 }
  }

}

function clearAddEmployerForm() {
  //alert("clear clearAddEmployerForm");
  document.AddEmployerForm.elements["affIdState"].value = "";
  document.AddEmployerForm.elements["affIdType"].value = "";
  document.AddEmployerForm.elements["affIdLocal"].value = "";
  document.AddEmployerForm.elements["affIdSubUnit"].value = "";
  document.AddEmployerForm.elements["affIdCouncil"].value = "";
}


function identifyNewRow() {
  document.forms[0].action="/Minimumdues/DataEntry.do?addNewRow=true";
  document.forms[0].submit();
}

function ckeckMinimumDuesDataEntryForm() {
  if (document.AddDataEntryForm.elements["affIdState"].value == "") {
      	alert("State is a reuired field.");
      	return false;
  }
  else
  	return true;
}

function clearInNegotiationsCheckBox() {
  document.AddDataEntryForm.inNegotiations[0].checked = false;
  document.AddDataEntryForm.inNegotiations[1].checked = false;
}

function clearFormCompletedCheckBox() {
  document.AddDataEntryForm.elements["formCompleted"].checked = false;
}

function clearCorrespondenceCheckBox() {
  document.AddDataEntryForm.elements["correspondence"].checked = false;
}

function clearForm() {
  document.AddDataEntryForm.elements["numberOfMember"].value = "";
  document.AddDataEntryForm.elements["durationFrom"].value = "";
  document.AddDataEntryForm.elements["durationTo"].value = "";
  document.AddDataEntryForm.elements["telephone"].value = "";
  document.AddDataEntryForm.elements["personName"].value = "";
  document.AddDataEntryForm.elements["averageWage"].value = "";
  document.AddDataEntryForm.elements["correspondenceDate"].value = "";
  document.AddDataEntryForm.elements["comments"].value = "";
  document.AddDataEntryForm.elements["initPercent_a"].value = "";
  document.AddDataEntryForm.elements["initEffective_a"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_a"].value = "";
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].value = "";
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].value = "";
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].value = "";
  document.AddDataEntryForm.elements["initAmount_b"].value = "";
  document.AddDataEntryForm.elements["initEffective_b"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_b"].value = "";
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].value = "";
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].value = "";
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].value = "";
  document.AddDataEntryForm.inNegotiations[0].checked = true;

  document.AddDataEntryForm.elements["formCompleted"].checked = false;
  document.AddDataEntryForm.elements["correspondence"].checked = false;

}

function disableSectionA() {

  disableSectionInitA();

  for (ct = 0; ct <= 9; ct++)
  {
  	if (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].disabled = true;

		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].style.background = '#C0C0C0';
	}

	if (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) {
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].disabled = false;

		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].style.background = 'white';
	}
  }

  document.AddDataEntryForm.elements["averageWage"].disabled = false;
  document.AddDataEntryForm.elements["averageWage"].style.background = '#FFFFC6';

}


function disableSectionB() {

  disableSectionInitB();

  for (ct = 0; ct <= 9; ct++)
  {
  	if (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].disabled = false;

		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].style.background = 'white';
	}

	if (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) {
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].disabled = true;

		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].style.background = '#C0C0C0';
	}
  }

  document.AddDataEntryForm.elements["averageWage"].value = "";
  document.AddDataEntryForm.elements["averageWage"].disabled = true;
  document.AddDataEntryForm.elements["averageWage"].style.background = '#C0C0C0';
}

function disableSectionInitA() {

  document.AddDataEntryForm.elements["initPercent_a"].disabled = true;
  document.AddDataEntryForm.elements["initEffective_a"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_a"].disabled = true;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].disabled = true;
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].disabled = true;
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].disabled = true;

  document.AddDataEntryForm.elements["initAmount_b"].disabled = false;
  document.AddDataEntryForm.elements["initEffective_b"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_b"].disabled = false;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].disabled = false;
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].disabled = false;
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].disabled = false;

  document.AddDataEntryForm.elements["addPencentageIncRowButton"].disabled = true;
  document.AddDataEntryForm.elements["addAmountIncRowButton"].disabled = false;

  document.AddDataEntryForm.elements["initPercent_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initEffective_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].style.background = '#C0C0C0';

  document.AddDataEntryForm.elements["initAmount_b"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initEffective_b"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initNoOfMember_b"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].style.background = 'white';

}


function disableSectionInitB() {

  document.AddDataEntryForm.elements["initPercent_a"].disabled = false;
  document.AddDataEntryForm.elements["initEffective_a"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_a"].disabled = false;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].disabled = false;
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].disabled = false;
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].disabled = false;

  document.AddDataEntryForm.elements["initAmount_b"].disabled = true;
  document.AddDataEntryForm.elements["initEffective_b"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_b"].disabled = true;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].disabled = true;
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].disabled = true;
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].disabled = true;

  document.AddDataEntryForm.elements["addAmountIncRowButton"].disabled = true;
  document.AddDataEntryForm.elements["addPencentageIncRowButton"].disabled = false;

  document.AddDataEntryForm.elements["initPercent_a"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initEffective_a"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initNoOfMember_a"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].style.background = 'white';
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].style.background = 'white';
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].style.background = 'white';
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].style.background = 'white';

  document.AddDataEntryForm.elements["initAmount_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initEffective_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].style.background = '#C0C0C0';
}

function manipulateStartEndDate() {
  if (document.AddDataEntryForm.inNegotiations[0].checked == true) {
    document.AddDataEntryForm.elements["durationFrom"].disabled = false;
    document.AddDataEntryForm.elements["durationTo"].disabled = false;

    document.AddDataEntryForm.elements["durationFrom"].style.background = 'white';
    document.AddDataEntryForm.elements["durationTo"].style.background = 'white';

    document.AddDataEntryForm.elements["averageWage"].disabled = false;
    document.AddDataEntryForm.elements["averageWage"].style.background = 'white';

    document.AddDataEntryForm.elements["agreementReceived"].disabled = false;
    document.AddDataEntryForm.elements["agreementReceived"].style.background = 'white';

    document.AddDataEntryForm.elements["agreementDesc"].disabled = false;
    document.AddDataEntryForm.elements["agreementDesc"].style.background = 'white';

    enableSectionAOnly();
    disableSectionBOnly();
  }
  else if (document.AddDataEntryForm.inNegotiations[1].checked == true) {
    document.AddDataEntryForm.elements["durationFrom"].value = "";
    document.AddDataEntryForm.elements["durationTo"].value = "";
    document.AddDataEntryForm.elements["durationFrom"].disabled = true;
    document.AddDataEntryForm.elements["durationTo"].disabled = true;
    document.AddDataEntryForm.elements["durationFrom"].style.background = '#C0C0C0';
    document.AddDataEntryForm.elements["durationTo"].style.background = '#C0C0C0';

    document.AddDataEntryForm.elements["averageWage"].value = "";
    document.AddDataEntryForm.elements["averageWage"].disabled = true;
    document.AddDataEntryForm.elements["averageWage"].style.background = 'C0C0C0';

    document.AddDataEntryForm.elements["agreementReceived"].checked = false;
    document.AddDataEntryForm.elements["agreementReceived"].disabled = true;
    document.AddDataEntryForm.elements["agreementReceived"].style.background = '#C0C0C0';

    document.AddDataEntryForm.elements["agreementDesc"].value = "";
    document.AddDataEntryForm.elements["agreementDesc"].disabled = true;
    document.AddDataEntryForm.elements["agreementDesc"].style.background = '#C0C0C0';

    disableSectionAOnly();
    disableSectionBOnly();
  }
}

function disableSectionAOnly() {

  disableSectionInitAOnly();

  for (ct = 0; ct <= 9; ct++)
  {
  	if (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value = "";
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].value = "";
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value = "";
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].value = "";
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].value = "";
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].value = "";
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].value = "";

		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].disabled = true;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].disabled = true;

		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].style.background = '#C0C0C0';
	}
  }

}


function disableSectionBOnly() {

  disableSectionInitBOnly();

  for (ct = 0; ct <= 9; ct++)
  {
	if (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) {
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].value = "";
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].value = "";
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value = "";
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].value = "";
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].value = "";
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].value = "";
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].value = "";

		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].disabled = true;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].disabled = true;

		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].style.background = '#C0C0C0';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].style.background = '#C0C0C0';
	}
  }

}

function enableSectionAOnly() {

  enableSectionInitAOnly();

  for (ct = 0; ct <= 9; ct++)
  {
  	if (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null) {
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].disabled = false;
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].disabled = false;

		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].typeOfPayment_adj_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percentInc_adj_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_adj_a"].style.background = 'white';
		document.AddDataEntryForm.elements["percentWageIncList["+ct+"].mbrTimesInc_a"].style.background = 'white';
	}
  }

  document.AddDataEntryForm.elements["averageWage"].disabled = true;
  document.AddDataEntryForm.elements["averageWage"].style.background = '#C0C0C0';
}


function enableSectionBOnly() {

  enableSectionInitBOnly();

  for (ct = 0; ct <= 9; ct++)
  {
	if (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"] != null) {
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].disabled = false;
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].disabled = false;

		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].typeOfPayment_adj_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amountInc_adj_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_adj_b"].style.background = 'white';
		document.AddDataEntryForm.elements["amountWageIncList["+ct+"].mbrTimesInc_b"].style.background = 'white';
	}
  }

  document.AddDataEntryForm.elements["averageWage"].disabled = false;
  document.AddDataEntryForm.elements["averageWage"].style.background = 'white';
}


function disableSectionInitAOnly() {
  document.AddDataEntryForm.elements["initPercent_a"].value = "";
  document.AddDataEntryForm.elements["initEffective_a"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_a"].value = "";
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].value = "";
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].value = "";
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].value = "";

  document.AddDataEntryForm.elements["initPercent_a"].disabled = true;
  document.AddDataEntryForm.elements["initEffective_a"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_a"].disabled = true;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].disabled = true;
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].disabled = true;
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].disabled = true;

  document.AddDataEntryForm.elements["addPencentageIncRowButton"].disabled = true;

  document.AddDataEntryForm.elements["initPercent_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initEffective_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].style.background = '#C0C0C0';
}


function disableSectionInitBOnly() {
  document.AddDataEntryForm.elements["initAmount_b"].value = "";
  document.AddDataEntryForm.elements["initEffective_b"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_b"].value = "";
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].value = "";
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].value = "";
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].value = "";
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].value = "";

  document.AddDataEntryForm.elements["initAmount_b"].disabled = true;
  document.AddDataEntryForm.elements["initEffective_b"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_b"].disabled = true;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].disabled = true;
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].disabled = true;
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].disabled = true;
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].disabled = true;


  document.AddDataEntryForm.elements["addAmountIncRowButton"].disabled = true;

  document.AddDataEntryForm.elements["initAmount_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initEffective_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].style.background = '#C0C0C0';
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].style.background = '#C0C0C0';
}

function enableSectionInitAOnly() {

  document.AddDataEntryForm.elements["initPercent_a"].disabled = false;
  document.AddDataEntryForm.elements["initEffective_a"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_a"].disabled = false;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].disabled = false;
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].disabled = false;
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].disabled = false;

  document.AddDataEntryForm.elements["addPencentageIncRowButton"].disabled = false;

  document.AddDataEntryForm.elements["initPercent_a"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initEffective_a"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initNoOfMember_a"].style.background = '#FFFFC6';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_a"].style.background = 'white';
  document.AddDataEntryForm.elements["initPercentInc_adj_a"].style.background = 'white';
  document.AddDataEntryForm.elements["initNoOfMember_adj_a"].style.background = 'white';
  document.AddDataEntryForm.elements["initMbrTimesInc_a"].style.background = 'white';
}


function enableSectionInitBOnly() {

  document.AddDataEntryForm.elements["initAmount_b"].disabled = false;
  document.AddDataEntryForm.elements["initEffective_b"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_b"].disabled = false;
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].disabled = false;
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].disabled = false;
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].disabled = false;
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].disabled = false;


  document.AddDataEntryForm.elements["addAmountIncRowButton"].disabled = false;

  document.AddDataEntryForm.elements["initAmount_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initEffective_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initNoOfMember_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initTypeOfPayment_adj_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initAmountInc_adj_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initNoOfMember_adj_b"].style.background = 'white';
  document.AddDataEntryForm.elements["initMbrTimesInc_b"].style.background = 'white';
}

function changeDeceFormetA(objName) {
	var checkStr = objName;

	if (checkStr.value.charAt(0) == ".") {
		 checkStr.value = "0"+checkStr.value;
		 // alert('change');
	}

	document.AddDataEntryForm.elements["initPercent_a"].value = checkStr.value;
}

function changeDeceFormetB(objName) {
	var checkStr = objName;

	if (checkStr.value.charAt(0) == ".") {
		 checkStr.value = "0"+checkStr.value;
		 // alert('change');
	}

	document.AddDataEntryForm.elements["initAmount_b"].value = checkStr.value;
}

function checkAllFieldsBeforeSubmit() {
  if (document.AddDataEntryForm.inNegotiations[0].checked == true)
  {
	var dateFromStr = document.AddDataEntryForm.elements["durationFrom"].value;
	var dateToStr = document.AddDataEntryForm.elements["durationTo"].value;
	var yearVal = document.AddDataEntryForm.elements["addYear"].value;

	var durationFromDate = new Date(dateFromStr.substring(6,10),
                            		dateFromStr.substring(0,2)-1,
                            		dateFromStr.substring(3,5));
	var durationToDate   = new Date(dateToStr.substring(6,10),
                            		dateToStr.substring(0,2)-1,
                            		dateToStr.substring(3,5));

        var initEffectiveVal_a = document.AddDataEntryForm.elements["initEffective_a"].value;
        var initEffectiveVal_b = document.AddDataEntryForm.elements["initEffective_b"].value;

	var initEffectiveDate_a = new Date(initEffectiveVal_a.substring(6,10),
                            		initEffectiveVal_a.substring(0,2)-1,
                            		initEffectiveVal_a.substring(3,5));
 	var initEffectiveDate_b = new Date(initEffectiveVal_b.substring(6,10),
                             		initEffectiveVal_b.substring(0,2)-1,
                             		initEffectiveVal_b.substring(3,5));

	var numberOfMember = document.AddDataEntryForm.elements["numberOfMember"].value - 0;
	var stat = document.AddDataEntryForm.elements["h_statAverage"].value - 0;

	if ((numberOfMember > stat) && (stat != 0)) {
		alert("The Total Number of Members and Fee Payers should not exceed the STAT Average.");
		return false;
	}

	//if (document.AddDataEntryForm.elements["numberOfMember"].value == "") {
	//   alert("Total Number of Members and Fee payers is required.");
	//   return false;
	//}
	//if ((document.AddDataEntryForm.inNegotiations[0].checked == true)
	//	&& ((document.AddDataEntryForm.elements["durationFrom"].value == "") || (document.AddDataEntryForm.elements["durationTo"].value == "")) ) {
	//	alert("Since the agreement is not in negotiation, the From / To Date field(s) is required.");
	//	return false;
	//}
	if ((document.AddDataEntryForm.section[1].checked == true) && (trim(document.AddDataEntryForm.elements["averageWage"].value) == "") ) {
		alert("Since Sec B is chosen for entering data, the Average Wage field is required.");
		return false;
	}
	else if ((document.AddDataEntryForm.elements["correspondence"].checked == true)
		&& (trim(document.AddDataEntryForm.elements["correspondenceDate"].value) == "")) {
		alert("Since Correspondence is checked, the Correspondence Date field is required.");
		return false;
	}
	else if (trim(document.AddDataEntryForm.elements["personName"].value) == "") {
	   alert("Person Completing the Form is required (Enter N/A if no name is available).");
	   return false;
	}
	else if ((trim(dateFromStr) != "") && (trim(dateToStr) != "") && (durationFromDate > durationToDate)) {
	   alert("Agreement Duration To Date should not be earlier than the From Date.");
	   return false;
	}
 	else if ( document.AddDataEntryForm.section[0].checked == true )
	{
	  // sec A
	  if (validateEffectiveDate(document.AddDataEntryForm.elements["initEffective_a"], "addYear") == false)
	  	return false;

	  for (ct = 0; ct <= 15; ct++)
	  {
	  	if (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"] != null) {
			if (validateEffectiveDate(document.AddDataEntryForm.elements["percentWageIncList["+ct+"].effective_a"], "addYear") == false)
			 	return false;
		}
  	  }

	  if ((initEffectiveVal_a == null) || (trim(initEffectiveVal_a) == "")) {
    	  	alert("The effective date in row 1 is required");
   	  	return false;
   	  }
	  else if ((document.AddDataEntryForm.elements["numberOfMember"] != null)
	      && (trim(document.AddDataEntryForm.elements["numberOfMember"].value) != "") )
	  {
		  var sumA = document.AddDataEntryForm.elements["initNoOfMember_a"].value - 0;

		  for (ct = 0; ct <= 15; ct++)
		  {
			var ctPlusOne = ct - 0 + 2;
			if ((document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"] != null)
				&& (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value != "")
				&& (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"] != null)
				&& (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value != "")
				&& (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value - 0 < 0 )) {
					alert("The Number of Member and Fee Payer effected in row " + ctPlusOne + " on the wage increase data list cannot be negative.");
					return false;
			}
			else if ((document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"] != null)
			    && (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].percent_a"].value != "")) {
				sumA = sumA * 1 - 0 + (document.AddDataEntryForm.elements["percentWageIncList["+ct+"].noOfMember_a"].value - 0);

			}
		  }

		  if (sumA != (document.AddDataEntryForm.elements["numberOfMember"].value - 0)) {
		  	alert("The sum of Members Affected should be equal to Total Number of Members and Fee Payers.");
		  	return false;
		  }

		  //if (sumA != (document.AddDataEntryForm.elements["numberOfMember"].value - 0)) {
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

	else if (document.AddDataEntryForm.section[1].checked == true)
	{
	  // Sec B
	  if (validateEffectiveDate(document.AddDataEntryForm.elements["initEffective_b"], "addYear") == false)
	  	return false;

	  for (ct = 0; ct <= 15; ct++)
	  {
	  	if (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"] != null) {
			if (validateEffectiveDate(document.AddDataEntryForm.elements["amountWageIncList["+ct+"].effective_b"], "addYear") == false)
			 	return false;
		}
  	  }

   	  if ((initEffectiveVal_b == null) || (trim(initEffectiveVal_b) == "")) {
   	  	alert("The effective date in row 1 is required");
  	  	return false;
  	  }
	  else if ((document.AddDataEntryForm.elements["numberOfMember"] != null)
		&& (trim(document.AddDataEntryForm.elements["numberOfMember"].value) != "") )
	  {
		  var sumB = document.AddDataEntryForm.elements["initNoOfMember_b"].value - 0;

		  for (ct = 0; ct <= 15; ct++)
		  {

			var ctPlusOne = ct - 0 + 2;
			if ( (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"] != null)
				&& (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value != "")
				&& (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amount_b"] != null)
				&& (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amount_b"].value != "")
				&& (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value - 0 < 0 ) ) {
				alert("The Number of Member and Fee Payer effected in row " + ctPlusOne + " on the wage increase data list cannot be negative.");
				return false;
			}
			else if ((document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"] != null)
			    && (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].amount_b"].value != "")) {
				sumB = sumB * 1 - 0 + (document.AddDataEntryForm.elements["amountWageIncList["+ct+"].noOfMember_b"].value - 0);
			}

		  }

		  if (sumB != (document.AddDataEntryForm.elements["numberOfMember"].value - 0)) {
		  	alert("The sum of Members Affected should be equal to Total Number of Members and Fee Payers.");
		  	return false;
		  }

		  //if (sumB != (document.AddDataEntryForm.elements["numberOfMember"].value - 0)) {
		  //	input_box=confirm("The sum of Members Affected is not equal to Total Number of Members and Fee Payers. Still want to submit?");
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


