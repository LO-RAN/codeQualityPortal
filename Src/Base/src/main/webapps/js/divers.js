function autofitIframe(id){
    var v = parent.document.getElementById(id);
    if(v)
        v.style.height=this.document.body.scrollHeight+50+"px";
}

function autofitIframeParent(id){
    parent.parent.document.getElementById(id).style.height=this.document.body.scrollHeight+100+"px";
}

function autofitIframeWidth(id){
    parent.document.getElementById(id).style.width=this.document.body.scrollWidth+50+"px";
}
