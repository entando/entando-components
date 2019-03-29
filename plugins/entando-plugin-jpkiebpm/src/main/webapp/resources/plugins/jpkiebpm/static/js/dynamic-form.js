String.prototype.initCap = function () {
    return this.toLowerCase().replace(/(?:^|\s)[a-z]/g, function (m) {
        return m.toUpperCase();
    });
};

var org = org || {};
org.entando = org.entando || {};
org.entando.form = org.entando.form || {};
org.entando.form.DynamicForm = function (jKIE) {
    this.json = {};
    this.json.method = jKIE.mainForm.method;
    this.json.action = jKIE.mainForm.action;
    this.json.html = new Array();
    
    
    // alert("jKIE.mainForm:" + JSON.stringify(jKIE.mainForm));
    
    var obj = {
        "id": "processId",
        "name": "processId",
        "type": "hidden",
        "value": jKIE.mainForm.processId
    };
    this.json.html.push(obj);
    obj = {
        "id": "containerId",
        "name": "containerId",
        "type": "hidden",
        "value": jKIE.mainForm.containerId
    }
    this.json.html.push(obj);
    if (jKIE.mainForm.taskId) {
        obj = {
            "id": "taskId",
            "name": "taskId",
            "type": "hidden",
            "value": jKIE.mainForm.taskId
        };
        this.json.html.push(obj);
    }
    ;
    var createFormGroupObject = function (el) {
        var obj = {};
        obj.type = "div";
        obj.class = "form-group";
        obj.html = {
            "id": el.name,
            "name": el.name,
            "readonly": el.readonly,
            "class": "form-control ui-widget",
            "type": el.type,
            "validate": {
                "required": true
            }
        };
        if (el.value) {
            obj.html.value = el.value;
        }
        return obj;
    }

    var addInpuntElement = function (el) {
        if (!el.fieldset) {
            return createObjectElement(el);
        } else {
            var obj = {};
            obj.type = "div";
            obj.html = {
                "type": "fieldset",
                "caption": el.fieldset.legend,
                "html": []
            };
            if (Array.isArray(el.fieldset.field)) {
                el.fieldset.field.forEach(function (el) {
                    obj.html.html.push(addInpuntElement(el));
                })
            } else {
                obj.html.html.push(addInpuntElement(el.fieldset.field));
            }

            return (obj);
        }
        return null;
    };


    var createObjectElement = function (el) {
        var obj = createFormGroupObject(el);

        if (el.datepicker) {
            obj.html.datepicker = el.datepicker;
            obj.html.class = "";
        } else if (el.type === 'checkbox') {
            obj.class = "checkbox";
            obj.html.class = "";

        } else if (el.type === 'checkboxes') {
            obj.class = "checkbox";
            obj.html.class = "";
            obj.html.options = el.options;

        } else if (el.type === 'radio') {
            obj.class = "radio";
            obj.html.class = "";

        } else if (el.type === 'radiobuttons') {
            obj.class = "radio";
            obj.html.class = "";
            obj.html.options = el.options;
        }

        if (el.type !== 'submit') {
            if (el.caption) {
                obj.html.caption = new String(el.caption).initCap();
                obj.html.labelKey = el.labelKey;
            } else {
                obj.html.caption = new String(el.name).initCap();
            }

            obj.html.placeholder = el.placeholder;
        }
        if (el.required) {
            obj.html.validate = addRequiredInInputElement(el);
        }
        return obj;
    };


    var addRequiredInInputElement = function (el) {
        var obj = {"required": true};
        if (el.minlength) {
            obj.minlength = el.minlength;
        } else if (el.maxlength) {
            obj.maxlength = el.maxlength;
        } else if (el.min) {
            obj.min = el.min;
        } else if (el.max) {
            obj.max = el.max;
        } else if (el.number) {
            obj.number = el.number;
        } else if (el.digits) {
            obj.digits = el.digits;
        }
        return obj;
    };

    var json = this.json;


    if (Array.isArray(jKIE.mainForm.fields)) {
        jKIE.mainForm.fields.forEach(function (el) {

            var element = addInpuntElement(el);
            if (element !== null)
                json.html.push(element);
        });
    } else {
        var element = addInpuntElement(jKIE.mainForm.fields);
        if (element !== null)
            json.html.push(element);
    }

    if (json.method!=='none') {
        json.html.push({
            type: "div",
            html: {
                "type": "submit",
                "value": "submit",
                "name": "submit-bpm-form",
                "class": "btn btn-primary"
            }
        });
    }
};

org.entando.form.loadFrom = function (url) {

    $.ajax({
        method: 'GET',
        headers: {
            Accept: "application/json"
        },
        url: url,
        success: function (data) {
            var jsonKie = data.response.result;
            org.entando.form.urlRequest = jsonKie;
            jsonKie.mainForm.method = "post";
            org.entando.form.dynamicForm = new org.entando.form.DynamicForm(jsonKie);
            $("#bpm-form").dform(org.entando.form.dynamicForm.json);
        }
    });
};
