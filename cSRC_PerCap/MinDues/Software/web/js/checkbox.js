//
// Checkbox related functions.
//


//Checks all boxes whose name begins with 'startsWith'.  If all
//boxes are already checked, unchecks them.
function toggleCheckBoxes(formName, startsWith) {
	matched = new Array(0);
	checkBoxes = document.forms[formName].elements;
	len = checkBoxes.length;
	allChecked = true;
	for(i = 0; i < len ; i++) {
		if ((checkBoxes[i].id.search(startsWith) != -1) ||
			checkBoxes[i].id.search(startsWith) != -1) {
			matched.push(checkBoxes[i]);
			if (!checkBoxes[i].checked) {
				allChecked = false;
			}
		}
	}
	len = matched.length;
	for (i = 0; i < len; i++) {
		if (!matched[i].disabled) {
			matched[i].checked = !allChecked;
		}
	}
}

//Changes the next checkBox to the value of the given checkBox
function changeNext(checkBox) {
	formElements = checkBox.form.elements;
	len = formElements.length;
	for( i=0 ; i<len ; i++) {
		if (formElements[i] == checkBox) {
			formElements[i+1].checked = checkBox.checked;
			formElements[i+1].disabled = checkBox.checked;
		}
	}
}

//Finds all checkboxes that start with 'startsWith' + 'from', followed by a 
//checkbox that starts with 'startsWith' + 'to', and makes both values equal
//to the value of the first checkbox.
//(i.e.  Makes the right column equal to the left column)
function syncCheckBoxes(formName, startsWith, from, to) {
	checkBoxes = document.forms[formName].elements;
	len = checkBoxes.length;
	for(i = 0; i < len ; i++) {
		if ((checkBoxes[i].id.search(startsWith + from) != -1) &&
			(checkBoxes[i+1].id.search(startsWith + to) != -1)) {

			if (checkBoxes[i].checked) {
				checkBoxes[i+1].checked = true;
			}
			checkBoxes[i+1].disabled = checkBoxes[i].checked;
		}
	}
}

