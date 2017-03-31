/*
The MIT License (MIT)
Copyright (c) 2015 DarkHorse IT Consulting Private Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

/*
A Toast library. Just import this and the CSS class and call the appropriate methods. That's all!  
No need to write any HTML code in the HTML file. The HTML content is generated dynamically in this script
*/
/*
* Type0 - ERROR
* Type1 - INFO
* Type2 - WARN
*/
var Toasty = function () {
}


/*
The behaivour of the toast can be controlled using this.prop.VAR_NAME(if in this script) or Toasty.prop.VAR_NAME
"TRANS_DUR" determines the duration for which the toast will be visible
"title" can be used to show any sort of heading in the toast. It can be disables using the enableTitle flag by setting it to false
"type" determines the type of toast whether it is an error or a warning or some information
"currentClass" can be used to set the current CSS class of the toast based on the type. This is assigned by the setClass() function
"position" can be used to assign the position class
*/
Toasty.prop = {
    TRANS_DUR: 3,
    title: "",
    message: "",
    type: "",
    currentClass: "",
    position: "below-header",
    enableTitle: false,
    enableCloseBtn: true,
    parentContainer: "body"
}

/*
This function hides the toast after the specified duration
*/
Toasty.hide = function () {
    if (this.prop.TRANS_DUR != 0 && this.prop.TRANS_DUR != "" && this.prop.TRANS_DUR != null)
        $("#toasty").delay(this.prop.TRANS_DUR * 1000).fadeOut();
}

/*
Based on the type, the class for the toast can be assigned in this function. To assign the class use the "currentClass" property
*/
Toasty.setClass = function () {
    if (this.prop.type === 0) {
        this.prop.currentClass = "toast-error";
    }
    else if (this.prop.type === 1) {
        this.prop.currentClass = "toast-info";
    }
    else if (this.prop.type === 2) {
        this.prop.currentClass = "toast-warning";
    }

}

/*
For all types of toast, this is the only final call which will show the toast and then hide it after the specified duration
*/
Toasty.show = function () {
    this.setClass();
    this.template = "<div class='toast " + this.prop.position + "' id='toasty'> <div class='toast-title'>" + this.prop.title + "</div>" + this.prop.message + "</div>";
    $("#toasty").remove();
    $(this.prop.parentContainer).append(this.template);
    $("#toasty").addClass(this.prop.currentClass);
    this.hide();
}

//Info toast
Toasty.info = function (title, message) {
    this.prop.message = message;
    (this.prop.enableTitle) ? this.prop.title = title : this.prop.title = "";
    this.prop.type = 1;
    Toasty.show();
}

//Warning toast
Toasty.warn = function (title, message) {
    this.prop.message = message;
    (this.prop.enableTitle) ? this.prop.title = title : this.prop.title = "";
    this.prop.type = 2;
    Toasty.show();
}

//Error toast
Toasty.error = function (title, message) {
    this.prop.message = message;
    (this.prop.enableTitle) ? this.prop.title = title : this.prop.title = "";
    this.prop.type = 0;
    Toasty.show();
}