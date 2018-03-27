//
// Functions for the User Edit and User Add screens
//


var selectedDepartment;
var selectedCouncil = '';
var selectedLocal = '';
var selectedSubLocal = '';
function disableAffiliate() {
    if (!user.council.disabled) {
        selectedCouncil = user.council.value;
        selectedLocal = user.local.value;
        selectedSubLocal = user.subLocal.value;
        user.council.value = 'N/A';
        user.local.value = 'N/A';
        user.subLocal.value = 'N/A';
        user.council.disabled = true;
        user.local.disabled = true;
        user.subLocal.disabled = true;
    }
}
function enableAffiliate() {
    if (user.council.disabled) {
        user.council.disabled = false;
        user.council.value = selectedCouncil;
        user.local.disabled = false;
        user.local.value = selectedLocal;
        user.subLocal.disabled = false;
        user.subLocal.value = selectedSubLocal;
    }
}

function enableDepartment() {
    if (user.department.disabled) {
        user.department.options[user.department.selectedIndex].text = selectedDepartment;
        user.department.disabled = false;
    }
}
function disableDepartment() {
    if (!user.department.disabled) {
        user.department.disabled = true;
        var index = user.department.selectedIndex;
        selectedDepartment = user.department.options[index].text;
        user.department.options[index].text = 'N/A';
    }
}
function changeUserType() {
    if (user.userType.value == 3) {
        disableDepartment();
        disableAffiliate();
        if (user.selectRoles != null)
            user.selectRoles.disabled = true;
        if (user.submit != null)
            user.submit.disabled = false;
    }
    if (user.userType.value == 2 || user.userType.value == 4) {
        disableDepartment();
        enableAffiliate();
        if (user.selectRoles != null)
            user.selectRoles.disabled = false;
        if (user.submit != null)
            user.submit.disabled = true;
    }
    if (user.userType.value == 1) {
        enableDepartment();
        disableAffiliate();
        if (user.selectRoles != null)
            user.selectRoles.disabled = false;
        if (user.submit != null)
            user.submit.disabled = true;
    }
    if (user.userType.value == 0) {
        enableDepartment();
        enableAffiliate();
        if (user.selectRoles != null)
            user.selectRoles.disabled = false;
        if (user.submit != null)
            user.submit.disabled = true;
    }
}

