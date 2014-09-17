// entando widget jpmyportalplus code //start */
(function($){
////////widget-start
$(document).ready(function() {
//dom ready start
	//config
	var widgetOptions = {
			ENTANDO_WIDGET_SELECTOR: $.extend({
				column: ".widget-column",
				widget: ".widget",
				handle: ".widget-draggable",
				widgetToolbar: ".widget-toolbar",
				widgetContent: ".widget-body",
				widgetConfiguration: ".editContentBox",
				widgetToolbar: ".widget-draggable",
				buttonOpenClose: ".button-toggler-open-close",
				buttonConfigure: ".button-toggler-edit",
				buttonRemove: ".button-toggler-remove",
				widgetListContainer: "#editshowletlist",
				placeholder: "widget-clone"
			}, window.ENTANDO_WIDGET_SELECTOR),
			duration: 180,
			cssClass: $.extend({
				draggin: "",
				cloned: "dragdrop_while_dragging dragdrop_boxClone",
				hover: "dragdrop_hover",
				editFormElMessage: "form_message"
			}, window.ENTANDO_WIDGET_CSS_CLASS),
			widgetReadDomData: function(widget) {
				var codePrefix = "__s__";
				if (widget===undefined || widget === null) return [null, null];
				var data = {code : null, position: null, status: null};
				var splittedId = widget.attr("id").split(codePrefix);
				data.code = splittedId[0].replace(/^widget_/, "");
				data.position = new Number(splittedId[1]).valueOf();
				if (widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetContent).css("display") == "none") {
					data.status = "closed";
				}
				else {
					data.status = "open";
				}
				return data;
			},
			widgetDiscoverSiblingPositions: function(widget) {
				var finalpos = { prevpos: null, nextpos: null };
				var prevEl = widget.prev();
				var nextEl = widget.next();
				var previousPositionValue = null;
				var nextPositionValue = null;
				if (prevEl.length==1) {
					finalpos.prevpos = widgetOptions.widgetReadDomData(prevEl).position;
				}
				if (nextEl.length==1) {
					finalpos.nextpos = widgetOptions.widgetReadDomData(nextEl).position;
				}
				if (finalpos.prevpos == null && finalpos.nextpos == null) {
					var parentId = widget.parentsUntil(null, widgetOptions.ENTANDO_WIDGET_SELECTOR.column).attr("id");
					if (parentId==undefined) {
						parentId = widget.parentsUntil(widgetOptions.ENTANDO_WIDGET_SELECTOR.column).attr("id");
					}
					finalpos.prevpos = widgetOptions.ajaxConfig.frames[parentId];
				}
				return finalpos;
			},
			widgetMove: function(widget, data) {
				if (data.shiftingElements!==undefined && data.shiftingElements!==null) {
					var elementsToShift = [];
					for (var i = 0;i<data.shiftingElements.length;i++) {
						var element = data.shiftingElements[i][0];
						var newPos = data.shiftingElements[i][1];
						element = $(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget+"[id$=__s__"+element+"]");
						if (element.length>0) {
							elementsToShift.push([element,newPos]);
						}
					}
					for (var i = 0;i< elementsToShift.length;i++) {
						var currentEl = elementsToShift[i][0];
						var currentNewPos = elementsToShift[i][1];
						currentEl.attr("id", widgetOptions.widgetReadDomData(currentEl).code + "__s__" + currentNewPos);
					}
				}
				widget.attr("id", widgetOptions.widgetReadDomData(widget).code + "__s__" + data.result);
				widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetConfiguration+'input[name="'+widgetOptions.ajaxConfig.URL.paramNames.startFramePos+'"]').val(data.result);
				widget.trigger("move", [widget]);
			},
			widgetApplyLoading: function(widget) {
				$(widgetOptions.loadingImg).prependTo($(widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetToolbar)));
			},
			widgetRemoveLoading: function(widget) {
				var fn = function() {
					this.detach();
				}.bind(widgetOptions.loadingImg);
				setTimeout(fn, 450);
			},
			ajaxConfig: $.extend({
				URL : {
					paramNames: {
						startFramePos: "startFramePos",
						endFramePos: "targetFramePos",
						targetPrevFramePos: "targetPrevFramePos",
						targetNextFramePos: "targetNextFramePos",
						frameToEmpty: "frameToEmpty",
						formMove: "frameWhereOpenSection",
						configWidget: "frameWhereOpenSection"
					},
					moveFrame: "./swapFrames.action",
					removeFrame: "./removeFrame.action",
					closeFrame: "./resizeFrame.action",
					openFrame: "./resizeFrame.action",
					openFrameStandard: "front/openFrame.action",
					closeFrameStandard: "front/closeFrame.action",
					formMove: "./openSwapSection.action",
					configWidget: "./openConfigSection.action"
				},
				frames: {
					"widget-col1": 14,
					"widget-col2": 64,
					"widget-col3": 114
				},
				statusParams: {
					edit: "editFrame",
					openclose: "frameToResize"
				},
				i18n: {
					loading: "loading...",
					error: "error.",
					statusOpen: '<img src="closed-true.png" alt="open/close">',
					statusClosed: '<img src="closed-false.png" alt="open/close">'
				},
				loaderImgPath: "resources/plugins/jpmyportalplus/static/img/ajax-loader.gif"
			}, window.JPMYPORTALPLUS_CONFIG)
		};
		if(jQuery.browser.mobile) {
			$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column+" " +widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetToolbar).addClass(widgetOptions.ENTANDO_WIDGET_SELECTOR.handle.substring(1));
		}
	//setup img
		widgetOptions.loadingImg = $('<a class="widget-loading"><img src="'+widgetOptions.ajaxConfig.loaderImgPath+'" alt="loading..." /></a>');

	//helper fix sortable
	widgetOptions.ajax = {
		updateLastCall: {
			code: null,
			position: null,
			prevpos: null
		}
	};
	//setup sortable
		$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column).sortable({
			connectWith: widgetOptions.ENTANDO_WIDGET_SELECTOR.column,
			handle: widgetOptions.ENTANDO_WIDGET_SELECTOR.handle,
			opacity: (jQuery.browser.mobile ? false : 0.65),
			grid: (jQuery.browser.mobile ? [15,15] : [3,2]),
			scrollSensitivity: 20,
			tolerance: (jQuery.browser.mobile ? "pointer" : "intersect"),
			placeholder: ENTANDO_WIDGET_SELECTOR.placeholder,
			start: function(event, ui) {
				var h = 0;
				var cols = $(widgetOptions.ENTANDO_WIDGET_SELECTOR.column);
				$.each(cols, function(index, item){
					var tmp =  $(item).outerHeight();
					if (tmp > 0) h = tmp;
				});
				cols.css("min-height", ((h+20)+"px"));
				var widget = $(ui.item);
				widget.addClass(widgetOptions.cssClass.draggin);
				$(ui.placeholder).addClass(widgetOptions.cssClass.cloned);
				$(ui.placeholder).css("visibility", "visible");
			},
			stop: function(event, ui) {
				var cols = $(widgetOptions.ENTANDO_WIDGET_SELECTOR.column);
				cols.css("min-height", "");
				var widget = $(ui.item);
				widget.removeClass(widgetOptions.cssClass.draggin);
			},
			update: function(ev, ui) {
				var widget = $(ui.item);
				var widgetData = widgetOptions.widgetReadDomData(widget);
				//fix sortable bug
				if (
					widgetOptions.ajax.updateLastCall.code != widgetData.code
					||
					widgetData.position != widgetOptions.ajax.updateLastCall.position
					||
					widgetData.prevpos != widgetOptions.ajax.updateLastCall.prevpos
					||
					widgetData.nextpos != widgetOptions.ajax.updateLastCall.nextpos
				) {
					widgetOptions.widgetApplyLoading(widget);
					var posRef = widgetOptions.widgetDiscoverSiblingPositions(widget);
					var objReq = {};
					objReq["id"] = widgetData.position;
					objReq[widgetOptions.ajaxConfig.URL.paramNames.startFramePos] = widgetData.position;
					if (posRef.prevpos!=null) {
						objReq[widgetOptions.ajaxConfig.URL.paramNames.targetPrevFramePos] = posRef.prevpos;
						//console.log("prev", posRef.prevpos);
					}
					if (posRef.nextpos!=null) {
						objReq[widgetOptions.ajaxConfig.URL.paramNames.targetNextFramePos] = posRef.nextpos;
						//console.log("next", posRef.nextpos);
					}
					jQuery.ajax(widgetOptions.ajaxConfig.URL.moveFrame, {
						cache: false,
						data: objReq,
						//crossDomain: true,
						dataType: "json",
						success: function(data, textStatus, jqXHR) {
							widgetOptions.widgetMove(this, data);
							widgetOptions.widgetRemoveLoading(this);
						}.bind(widget)
					});
					widget.focus();
					widgetOptions.ajax.updateLastCall = widgetData;
				}
			}
		});
	//widget: drag
		if(widgetOptions.ENTANDO_WIDGET_SELECTOR.handle!=null) {
			$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column).on("click",widgetOptions.ENTANDO_WIDGET_SELECTOR.widget+" "+widgetOptions.ENTANDO_WIDGET_SELECTOR.handle, function(ev) {
				ev.preventDefault();
			});
		}
	//widget: toggle openclose
		if (widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonOpenClose!=null) {
			$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column).on("click", widgetOptions.ENTANDO_WIDGET_SELECTOR.widget+" "+widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonOpenClose, function(ev) {
				ev.preventDefault();
				var widget = $(this).parentsUntil(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget).parent();
				var data = widgetOptions.widgetReadDomData(widget);
				var content = $(widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetContent));
				var display = content.css("display");
				var contentStatus = (display===undefined||display===null||(display!=='none')? true: false);
				if (jQuery.browser.mobile) {
					if (contentStatus) {
						content.css("display", "none");
					}
					else {
						content.css("display", "");
					}
				}
				else {
					$(widget).find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetContent).toggle(widgetOptions.duration);
					if (contentStatus) {
						$(widget).find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetConfiguration).hide(widgetOptions.duration);
					}
				}
				var newData = widgetOptions.widgetReadDomData(widget);
				var url;
				var objReq = {};
				if (contentStatus) {
					if (widgetOptions.ajaxConfig.i18n.statusOpen!==undefined&&widgetOptions.ajaxConfig.i18n.statusOpen!==null) {
						widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonOpenClose).html(widgetOptions.ajaxConfig.i18n.statusOpen);
					}
					url = widgetOptions.ajaxConfig.URL.closeFrame;
				}
				else {
					if (widgetOptions.ajaxConfig.i18n.statusOpen!==undefined&&widgetOptions.ajaxConfig.i18n.statusOpen!==null) {
						widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonOpenClose).html(widgetOptions.ajaxConfig.i18n.statusClosed);
					}
					url = widgetOptions.ajaxConfig.URL.openFrame;
				}
				objReq[widgetOptions.ajaxConfig.statusParams.openclose] = data.position;
				jQuery.ajax(url, {
					cache: false,
					data: objReq,
					//crossDomain: true,
					dataType: "json"
				});
				$(ev.target).blur();
				widget.trigger((contentStatus?"close": "open"), [widget]);
			});
		}
	//widget: configure
		if (widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonConfigure!=null) {
			$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column).on("click", widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonConfigure, function(ev) {
				ev.preventDefault();
				var widget = $(this).parentsUntil(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget).parent();
				var data = widgetOptions.widgetReadDomData(widget);
				var objReq = {};
				var content = $(widget.find(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetConfiguration));
				var display = content.css("display");
				var contentStatus = (display===undefined||display===null||(display!=='none')? true: false);
				if (!contentStatus) {
					widgetOptions.widgetApplyLoading(widget);
					objReq[widgetOptions.ajaxConfig.URL.paramNames.formMove] = data.position;
					jQuery.ajax(widgetOptions.ajaxConfig.URL.formMove, {
						cache: false,
						data: objReq,
						//crossDomain: true,
						dataType: "html",
						success: function(data, textStatus, jqXHR) {
							content.html(data);
							if (jQuery.browser.mobile) {
								content.css("display", "");
							}
							else {
								content.toggle(widgetOptions.duration);
							}
							widgetOptions.widgetRemoveLoading(widget);
							widget.trigger("openConfigure", [widget]);
						}
					});
				}
				else {
					if (jQuery.browser.mobile) {
						content.css("display", "none");
					}
					else {
						content.toggle(widgetOptions.duration);
					}
					widget.trigger("closeConfigure", [widget]);
				}
			});
		}
	//widget: remove
		if (widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonRemove) {
			$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column).on("click", widgetOptions.ENTANDO_WIDGET_SELECTOR.buttonRemove, function(ev) {
				ev.preventDefault();
				var widget = $(this).parentsUntil(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget).parent();
				var data = widgetOptions.widgetReadDomData(widget);
				if (jQuery.browser.mobile) {
					widget.trigger("remove", [widget, data]);
					widget.remove();
				}
				else {
					$(widget).hide(widgetOptions.duration, function() {
						widget.trigger("remove", [widget , data]);
						widget.remove();
						//removeLoading
					});
				}
				var objReq = {};
				objReq[widgetOptions.ajaxConfig.URL.paramNames.frameToEmpty] = data.position;
				jQuery.ajax(widgetOptions.ajaxConfig.URL.removeFrame, {
					cache: false,
					data: objReq,
					//crossDomain: true,
					dataType: "json"
				});
				jQuery(widgetOptions.ENTANDO_WIDGET_SELECTOR.widgetListContainer + ' input[value="'+data.code+'"]' ).attr("checked", false);
			});
		}
	//hover
		$(widgetOptions.ENTANDO_WIDGET_SELECTOR.column+" "+widgetOptions.ENTANDO_WIDGET_SELECTOR.handle).hover(
			function () {
				var widget = $(this).parents(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget);
				widget.addClass(widgetOptions.cssClass.hover);
				widget.trigger("hover", [widget]);
			},
			function () {
				var widget = $(this).parents(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget);
				$(this).parents(widgetOptions.ENTANDO_WIDGET_SELECTOR.widget).removeClass(widgetOptions.cssClass.hover);
				widget.trigger("hoverout", [widget]);
		}).disableSelection();
////////widget-end

//dom ready end
});
})(jQuery);