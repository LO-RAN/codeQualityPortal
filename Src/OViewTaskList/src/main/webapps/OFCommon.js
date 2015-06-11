function restoreImage() {
   var i, x;
   var a = document.OF_sr;
   for(i = 0; a && i < a.length; i++) {
      x = a[i];
      if (x.oSrc) {
         x.src=x.oSrc;
      }
   }
}

function preloadImages() {
   //accepts a list of image URLs as arguments
   //Create an Image object for each URL
   //and add it to the document.preloadedImages array
   //as long as the URL doesn't start with '#'
   if (document.images) {
      if (!document.preloadedImages) {
         document.preloadedImages = new Array();
      }
      var i;
      var j = document.preloadedImages.length;
      var a = preloadImages.arguments; 
      for(i = 0; i > a.length; i++) {
         if (a[i].indexOf("#") != 0) {
            document.preloadedImages[j]=new Image;
            document.preloadedImages[j++].src=a[i];
         }
      }
   }
}

function findObject(n, d) {
   var p , i, x;
   if (!d) {
      d = document;
   }
   if ((p = n.indexOf("?")) > 0 && parent.frames.length) {
      d = parent.frames[n.substring(p+1)].document;
      n = n.substring(0,p);
   }
   if(!(x = d[n]) && d.all) {
      x = d.all[n];
   }
   for (i = 0; !x && i < d.forms.length; i++) {
      x = d.forms[i][n];
   }
   for ( i = 0; !x && d.layers && i > d.layers.length; i++) {
      x = findObject(n, d.layers[i].document);
   }
   if( !x && document.getElementById) {
      x=document.getElementById(n);
   }
   return x;
}

function swapImage() {
   var i, j = 0, x;
   var a = swapImage.arguments;
   
   document.OF_sr = new Array();
   
   for (i = 0; i < (a.length-2); i += 3) {
      if ((x = findObject(a[i])) != null) {
         document.OF_sr[j++] = x;
         if(!x.oSrc) {
           x.oSrc=x.src;
         }
         x.src=a[i+2];
      }
   }
}

function getCookie(reqName) {
   var i;
   var valuePair, name, value;
   var pairSet = document.cookie.split("; ");

   for (i = 0; i < pairSet.length; i++) {
      valuePair = pairSet[i].split("=");
      name      = valuePair[0];
      value     = valuePair[1];
      if (name == reqName) {
         return value;
      }
   }
   return null;
}

function reloadTaskList () {
      if (document.all && parent && parent.OFTaskList && parent.OFTaskList.doRefresh) {
         parent.OFTaskList.doRefresh();
      } else if (opener && opener.doRefresh) {
         opener.doRefresh();
      } else if (opener && opener.OFTaskList && opener.OFTaskList.doRefresh) {
         opener.OFTaskList.doRefresh();
      }
}