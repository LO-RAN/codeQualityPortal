         function onClick(evt, paramList) {
         
           // First detect the Classification of the object
           classification = new String;
           listpart       = new String;
           id             = new String;
           newurl         = new String;
           
           classstart = new Number;
           classend   = new Number;
           classlen   = new Number;
           
           idstart    = new Number;
           idend      = new Number;
           idlen      = new Number;
           
           //alert(paramList);
           
           classstart = paramList.search("Classification");
           
           listpart = paramList.substr(classstart);

           classstart = listpart.search("=");
           classstart++;
           classend    = listpart.search(";");

           classlen = classend - classstart;
           classification = listpart.substr(classstart, classlen);
           
           classend++;
           listpart = listpart.substr(classend);

           if (classification == "Process") {

               // Get the clickable image of the selected process 
               // Extract the ID.
               idstart = listpart.search("ActivityID=");
               if (idstart < 0) {
                 // dealing with a Process definition
                 idstart = listpart.search("ProcessID=");
                 idstart = idstart + 10 ;
               }
               else {
                 idstart = idstart + 10;
               }
               listpart = listpart.substr(idstart);
               idend   = listpart.search(";");
               if (idend < 0) {
                 // last item extract to the end
                 id = listpart;
               }
               else {
                 idlen   = idend - 1;
                 id      = listpart.substr(1, idlen);
               }
           
               //alert("window.location = " + window.location);
              
               // Create a new request to get the next Activity(Process) or ProcessDef.
               newurl = "./showActivityInfoOfProcessIns.do?action=ShowSVGDiagramItem&processID="
               newurl = newurl.concat(id);
              //alert("newurl = " + newurl);
              location = newurl;


           } else {
             
               //Get the details of the activity
               //Extract the definition ID
               if (classification == "Task") {
                   idstart = listpart.search("TaskID=");
                   if (idstart >= 0) {
                     idstart = idstart + 7;
                     // Definition ID is fixed length; no prefix.
                     defId      = listpart.substr(idstart, 23);
                   }
               } else if (classification == "FlowControl") {
                   idstart = listpart.search("FlowControlID=");
                   if (idstart >= 0) {
                     idstart = idstart + 14;
                     // Definition ID is fixed length; no prefix.
                     defId      = listpart.substr(idstart, 23);
                   }
               } else if (classification == "Process") {
                   idstart = listpart.search("ProcessID=");
                   if (idstart >= 0) {
                     idstart = idstart + 10;
                     // Definition ID is fixed length; no prefix.
                     defId      = listpart.substr(idstart, 23);
                   }
               } else if (classification == "Recurrence") {
                   idstart = listpart.search("RecurrenceID=");
                   if (idstart >= 0) {
                     idstart = idstart + 13;
                     // Definition ID is fixed length; no prefix.
                     defId      = listpart.substr(idstart, 23);
                   }
               }
               // Extract the Activity ID.
               idstart = listpart.search("ActivityID=");
               if (idstart >= 0) {
                 idstart = idstart + 10;
                 listpart = listpart.substr(idstart);
                 idend   = listpart.search(";");
                 idlen   = idend - 1;
                 id      = listpart.substr(1, idlen);
               }
               // Create a new request to get the Details of a Task or FlowControl.
               newurl = "./showActivityDetails.do?action=ShowActivityDetails"
               newurl = newurl+"&activityID="+id
               newurl = newurl+"&classification="+classification
               newurl = newurl+"&defID="+defId
               //alert("url :"+newurl);
               parent.details_frame.location = newurl;
           }
         }
           
         function onMouseOver(evt, paramList) {
	 }
	   
	 function onMouseOut(evt, paramList) {
	 }
	   
	 function onMouseUp(evt, paramList) {}
	 
	 function onMouseDown(evt, paramList) {}
	 
	 function onMouseMove(evt, paramList) {}
	 
	 function onFocusIn(evt, paramList) {}
	 
	 function onFocusOut(evt, paramList) {}
	 
	 function ChangeStateColors() {
           //window.changeStateColor('UFD1', '0 1 0 0 0  0 0 1 0 0  1 0 0 0 0  0 0 0 1 0')
           //window.changeBlinkColor('Blink','.4 0 0 0 0  0 .4 0 0 0  .7 .7 2 0 0   0 0 -0.2 1 0', '400ms')
	 }
