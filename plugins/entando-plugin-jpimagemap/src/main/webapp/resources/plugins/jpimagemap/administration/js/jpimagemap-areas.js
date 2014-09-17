window.addEvent("domready", function() {
//

	var draggable = document.id("areas").getElement(".area-current");
	var droppables = document.id("areas").getElements(".area-blocked");
	var container = document.id("areas");
	var messagebox = document.id("messages");

	container.addEvents({
		"mousewheel:relay(div.area)": function(ev) {
			ev.preventDefault();
			var oldOpacity = ev.target.getStyle("opacity").toFloat();
			var newOpacity = oldOpacity+(ev.wheel/10);
			if (newOpacity<0.25) newOpacity = 0.30;
			if (newOpacity>1) newOpacity = 1;
			ev.target.setStyle("opacity",newOpacity);
			//console.log("mousewheel",ev.wheel,"target",ev.target,"oldOpacity",oldOpacity,"newOpacity",newOpacity);
		},
		"dblclick:relay(div.area)": function(ev) {
			ev.preventDefault();
			var oldOpacity = ev.target.getStyle("opacity").toFloat();
			var newOpacity = 1;
			if (oldOpacity < 1) {
				newOpacity = 1;
			}
			else if(oldOpacity==1){
				newOpacity = 0.3;
			}
			ev.target.setStyle("opacity",newOpacity);
			//console.log("mousewheel",ev.wheel,"target",ev.target,"oldOpacity",oldOpacity,"newOpacity",newOpacity);
		}
	});

	draggable.store("name",draggable.get("text"));

	var getAreaName = function(area) {
		return area.get("text");
	};

	var applyConflict = function(droppable) {
		var str = "";
		droppable.each(function(item){
			str = str + " " + item.get("text");
		});
		messagebox.set("text",ImageMapAttribute_fieldError_linkedAreaElement_intersectedArea + " ("+str+")");
		document.getElement("input[type=submit]").set("disabled","disabled");
	};

	var removeConflict = function(droppable) {
		messagebox.set("text","");
		document.getElement("input[type=submit]").removeProperty("disabled");
	};

	var checkOverlap = function(shape1,shape2) {
		var overlap = false;
		if (shape1 != shape2) {
			var objDim = {
				"shape1": {
					"top": shape1.getPosition().y,
					"left": shape1.getPosition().x,
					"w": shape1.getDimensions().x,
					"h": shape1.getDimensions().y
				},
				"shape2": {
					"top": shape2.getPosition().y,
					"left": shape2.getPosition().x,
					"w": shape2.getDimensions().x,
					"h": shape2.getDimensions().y
				}
			};
			if (((objDim.shape1.top + objDim.shape1.h) >= objDim.shape2.top) && ( (objDim.shape2.top+objDim.shape2.h ) >= objDim.shape1.top)) {
				//overlapV = true;
				if ( (objDim.shape2.left <= (objDim.shape1.left+objDim.shape1.w) )&& ( (objDim.shape2.left+objDim.shape2.w) >= objDim.shape1.left )  ) {
					//overlapO = true;
					overlap = true;
					//this.setOverlap([shape1, shape2], "on");
				}
			} else {
				//this.setOverlap([shape1, shape2], "off");
			}
		}
		objDim = null;
    	return overlap;
	};

	var checkShapeOverlap =  function(shape) {
		var overlappingShapes = [];
		for (var i =0;i<droppables.length;i++) {
			var current = droppables[i];
			if (checkOverlap(shape,current)) {
				overlappingShapes.push(current);
			}
		}
		return overlappingShapes;
	};

	var applyPosition = function(shape) {
		var pos = shape.getPosition(shape.getParent());
		var dim = shape.getDimensions();
		var obj = {
				top: pos.y,
				left: pos.x,
				right: pos.x+dim.width,
				bottom: pos.y+dim.height
		};
		var title = shape.retrieve("name") + ": "+ obj.left +", "+ obj.top +", "+ obj.right +", "+ obj.bottom;
		title = title.clean();
		shape.set("title",  title);
		shape.set("html", shape.retrieve("name") + "<br /><span class=\"dim\">" + dim.width + "x" + dim.height+"</span>");
		document.getElement('input[name=top]').set("value",obj.top);
		document.getElement('input[name=left]').set("value",obj.left);
		document.getElement('input[name=right]').set("value",obj.right);
		document.getElement('input[name=bottom]').set("value",obj.bottom);
		document.getElement('input[name=width]').set("value",dim.width);
		document.getElement('input[name=height]').set("value",dim.height);

	};

	//handler
	var pos = draggable.getPosition(draggable.getParent());
	var dim = draggable.getDimensions();
	var obj = {
			top: pos.y,
			left: pos.x,
			right: pos.x+dim.width,
			bottom: pos.y+dim.height
	};

	var h = new Element("span", {
		"class": "handler",
		styles: {
			"top": obj.top+dim.height,
			"left": obj.left+dim.width
		},
		text: ""
	}).inject(container);
	//handler

	new Drag.Move(draggable, {
		snap: 1,
        container: container,
        droppables: droppables,
        onDrag: function(element, ev) {
        	var pos = draggable.getPosition(draggable.getParent());
        	var dim = draggable.getDimensions();
        	var obj = {
        			top: pos.y,
        			left: pos.x,
        			right: pos.x+dim.width,
        			bottom: pos.y+dim.height
        	};
        	h.setStyle("top", obj.top+dim.height);
			h.setStyle("left", obj.left+dim.width);

        	var check = checkShapeOverlap(element);
        	if (check.length>0) {
        		applyConflict(check);
        	}
        	else {
        		removeConflict();
        	}
        	applyPosition(element);

        },
        onDrop: function(element, droppable){
        	var check = checkShapeOverlap(element);
        	if (check.length>0) {
        		applyConflict(check);
        	}
        	else {
        		removeConflict();
        	}
        	applyPosition(element);
        }
    });

	draggable.makeResizable({
		snap: 1,
		handle: h,
		onDrag: function(element) {
			var pos = draggable.getPosition(draggable.getParent());
			var dim = draggable.getDimensions();
			var obj = {
					top: pos.y,
					left: pos.x,
					right: pos.x+dim.width,
					bottom: pos.y+dim.height
			};
			h.setStyle("top", obj.top+dim.height);
			h.setStyle("left", obj.left+dim.width);
			var check = checkShapeOverlap(element);
        	if (check.length>0) {
        		applyConflict(check);
        	}
        	else {
        		removeConflict();
        	}
        	applyPosition(element);
		},
		onComplete: function(element) {
			var check = checkShapeOverlap(element);
        	if (check.length>0) {
        		applyConflict(check);
        	}
        	else {
        		removeConflict();
        	}
        	applyPosition(element);
		}
	});

	var check = checkShapeOverlap(draggable);
	if (check.length>0) {
		applyConflict(check);
	}
	else {
		removeConflict();
	}

//
});
