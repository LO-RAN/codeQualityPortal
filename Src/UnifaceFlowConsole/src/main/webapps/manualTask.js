// Javascript manualTask.js

    function focusTask() {
    
        // This javascript function is called when the window is loaded, to
        // put focus on the first editable field.
        
        for (i = 0; i < window.document.forms[0].elements.length; i++) {
            if (!(window.document.forms[0].elements[i].type == "hidden" ||
                  window.document.forms[0].elements[i].type == "button") ) {
                if (!(window.document.forms[0].elements[i].disabled)) {
                    window.document.forms[0].elements[i].select();
                    window.document.forms[0].elements[i].focus();
                    break;
                }
            }
        }
        
    }

    function completeTask() {

        // This javascript function is called when the button to Complete the Manual task is
        // clicked.
        
        newurl = new String;
        newurl = "./completeManualTask.do?action=Complete";
        
        // Add each input type=text field as a parameter to the URL,
        // also the hidden field that holds the taskID is added.
        
        for (i = 0; i < window.document.forms[0].elements.length; i++) {
            if (window.document.forms[0].elements[i].type == "button") {
                continue;
            }
            newurl = newurl+"&";
            newurl = newurl+window.document.forms[0].elements[i].name;
            newurl = newurl+"=";
            
            if (window.document.forms[0].elements[i].type == "checkbox") {
                if (window.document.forms[0].elements[i].checked) {
                    newurl = newurl+"true";
                }
                else {
                     newurl = newurl+"false";
                }
            }
            else {
                newurl = newurl+window.document.forms[0].elements[i].value;
            }
            
        }
        
        location = newurl;
        
    }
    
    function cancelTask() {
    
        // This javascript function is called when the button to Cancel the Manual task is
        // clicked.
        
        newurl = new String;
        newurl = "./processWorkflowDriven.do?process=WorkflowDriven&action=Cancel";
        
        // Add the taskID to the URL
        newurl = newurl+"&taskID=";
        newurl = newurl+window.document.forms[0].taskID.value;
        
        location = newurl;
        
    }