function initForm() {
	if (document.getElementById("increase_type").value == 1) {
			document.getElementById("centPerHrDoLumpSumBonus").disabled = true;
			document.getElementById("avgWagePerHrYr").disabled = true;
			document.getElementById("effDateInc").disabled = true;
			document.getElementById("noMemFeePayerAff2").disabled = true;

			document.getElementById("percentWageInc").disabled = false;
			document.getElementById("wageIncEffDate").disabled = false;
			document.getElementById("noMemFeePayerAff1").disabled = false;
	} else {
			document.getElementById("centPerHrDoLumpSumBonus").disabled = false;
			document.getElementById("avgWagePerHrYr").disabled = false;
			document.getElementById("effDateInc").disabled = false;
			document.getElementById("noMemFeePayerAff2").disabled = false;

			document.getElementById("percentWageInc").disabled = true;
			document.getElementById("wageIncEffDate").disabled = true;
			document.getElementById("noMemFeePayerAff1").disabled = true;
	}
}

function changeTypeSection(form) {
	if (form.increase_type.value == 1) {
			form.centPerHrDoLumpSumBonus.disabled = true;
			form.avgWagePerHrYr.disabled = true;
			form.effDateInc.disabled = true;
			form.noMemFeePayerAff2.disabled = true;

			form.percentWageInc.disabled = false;
			form.wageIncEffDate.disabled = false;
			form.noMemFeePayerAff1.disabled = false;
	} else {
			form.centPerHrDoLumpSumBonus.disabled = false;
			form.avgWagePerHrYr.disabled = false;
			form.effDateInc.disabled = false;
			form.noMemFeePayerAff2.disabled = false;

			form.percentWageInc.disabled = true;
			form.wageIncEffDate.disabled = true;
			form.noMemFeePayerAff1.disabled = true;
	}
}

