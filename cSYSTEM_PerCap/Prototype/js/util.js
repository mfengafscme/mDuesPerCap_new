//
// Utility functions
//

// Displays an OK/Cancel message box.  If the user selects OK, they
// are sent to 'locationAfterDelete'.
function confirmDelete(message, locationAfterDelete) {

    if (confirm(message)) {
        window.location = locationAfterDelete;
    }
}

