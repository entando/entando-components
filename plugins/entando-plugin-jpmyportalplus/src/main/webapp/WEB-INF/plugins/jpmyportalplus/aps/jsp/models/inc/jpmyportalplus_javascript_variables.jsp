<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="jpmpp" uri="/jpmyportalplus-core" %>
<%@ page contentType="charset=UTF-8" %>

<script type="text/javascript">
<!--//--><![CDATA[//><!--
	var JPMYPORTALPLUS_CONFIG = {
		"frames": { //most important thing here: the ID of the columns!
			"widget-col1": <jpmpp:columnInfo columnId="1" />,
			"widget-col2": <jpmpp:columnInfo columnId="2" />,
			"widget-col3": <jpmpp:columnInfo columnId="3" />
		},
		"URL" : {
			"paramNames": {
				"startFramePos": "startFramePos",
				"endFramePos": "targetFramePos",
				"targetPrevFramePos": "targetPrevFramePos",
				"targetNextFramePos": "targetNextFramePos",
				"frameToEmpty": "frameToEmpty",
				"formMove": "frameWhereOpenSection",
				"configWidget": "frameWhereOpenSection"
			},
			"moveFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/swapFrames.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"removeFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/removeFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"closeFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/closeFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"openFrame": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/openFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"openFrameStandard": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/openFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"closeFrameStandard": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/closeFrame.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"formMove": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/openSwapSection.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />",
			"configWidget": "<wp:info key="systemParam" paramName="applicationBaseURL" />do/jpmyportalplus/front/ajax/openConfigSection.action?request_locale=<wp:info key="currentLang" />&currentPageCode=<wp:currentPage param="code" />"
		},
		"statusParams": {
			"edit" : "editFrame",
			"openclose" : "frameToResize"
		},
		"i18n": {
			"loading": "<wp:i18n key="jpmyportalplus_LOADING_INFO" />",
			"error": "<wp:i18n key="jpmyportalplus_ERROR_INFO" />",
			"statusClosed": '<i class="icon-chevron-up" alt="close widget"></i><span class="sr-only">toggle open/close</span>',
			//"statusClosed": '<img src="<wp:resourceURL />plugins/jpmyportalplus/static/img/closed-false.png" alt="open/close">'
			//"statusOpen": '<img src="<wp:resourceURL />plugins/jpmyportalplus/static/img/closed-true.png" alt="open/close">',
			"statusOpen": '<i class="icon-chevron-down" alt="open widget"></i><span class="sr-only">toggle open/close</span>' 
		},
		loaderImgPath: "<wp:resourceURL />plugins/jpmyportalplus/static/img/ajax-loader.gif"
	};

	var ENTANDO_WIDGET_SELECTOR = {
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
		placeholder: undefined //"widget-clone"
	};

	var ENTANDO_WIDGET_CSS_CLASS = {
		draggin: "widget-while-dragging",
		cloned: "widget-while-dragging widget-clone",
		hover: "widget-hover",
		editFormElMessage: "form_message"
	};

	//configure page //start
	(function($){
		$(document).ready(function() {
			var editshowletEl = $("#editshowlet_title");
			var editshowletListEl = $("#editshowletlist");
			editshowletEl.on("click", function(ev){
				ev.preventDefault();
				var duration = jQuery.browser.mobile ? 0 : 180;
				$(this).toggle(duration, function(){
					if ($(this).css("display")=='none') {
						$("#configure-page").removeClass("spacer-border");
					}
					else {
						$("#configure-page").addClass("spacer-border");
						$('html,body').animate({scrollTop: ($(editshowletEl).offset().top-$(editshowletEl).outerHeight())}, duration);
					}
				});
			}.bind(editshowletListEl));

			//uncheck the checkbox when a widget is removed
			$(ENTANDO_WIDGET_SELECTOR.widget, ENTANDO_WIDGET_SELECTOR.column).on('remove', function(ev, widget, data){
				if (data) {
					$('[value="'+ data.code.substring("showlet_".length) +'"]',editshowletlist).attr('checked', false);
				}
			});

		});
	})(jQuery);
	//configure page //end

//--><!]]>
</script>


