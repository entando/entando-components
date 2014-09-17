<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />
<script type="text/javascript">
jQuery(function(){
	//pageTree of facetnav

	<s:set var="pageTreeStyleVar" ><wp:info key="systemParam" paramName="treeStyle_page" /></s:set>
	<s:if test="#pageTreeStyleVar == 'classic'">
	jQuery("#pageTree").EntandoWoodMenu({
		menuToggler: "subTreeToggler",
		menuRetriever: function(toggler) {
			return $(toggler).parent().children("ul");
		},
		openClass: "node_open",
		closedClass: "node_closed",
		showTools: true,
		onStart: function() {
			this.collapseAll();
		},
		expandAllLabel: "<s:text name="label.expandAll" />",
		collapseAllLabel: "<s:text name="label.collapseAll" />",
	<s:if test="%{facetCode != null && !(facetCode.equalsIgnoreCase(''))}">
		startIndex: "fagianonode_<s:property value="facetCode" />",
	</s:if>
		toolTextIntro: "<s:text name="label.introExpandAll" />",
		toolexpandAllLabelTitle: "<s:text name="label.expandAllTitle" />",
		toolcollapseLabelTitle: "<s:text name="label.collapseAllTitle" />"
	});
	</s:if>


	//for each elements 'form.action-form'
jQuery.each($('#facetnav-actionform'), function(index, currentForm){

	//current form
	var currentForm = $(currentForm);

	//the inputs type[radio]
	var currentFormInputs = $('li.tree_node_flag input[type="radio"]', currentForm);
			currentFormInputs.each(function() {
					$(this).css('position', 'absolute');
					$(this).css('left', '-9999px');
			});

	//the fieldset
	var fieldset = $('[data-toggle="tree-toolbar"]', currentForm).first();

	//the actions
	var myActionMenu = $('[data-toggle="tree-toolbar-actions"]', fieldset).first();

	fieldset.remove();
	myActionMenu.removeClass('margin-small-vertical');

	//the labels
	var labels = $('li.tree_node_flag label', currentForm);

	//for each label if checked just show the menu
	jQuery.each(labels, function(index, myLabel){
			var myLabel = $(myLabel);
			var myInput = $('#'+myLabel.attr('for'));
			if (myInput.attr('checked') == 'checked') {
					$(myLabel).addClass('text-info');

					//prepare and attach popover to selected label
					$(myLabel).popover({
							html: true,
							content: myActionMenu,
							placement: "right",
							animation: false,
							container: currentForm
					});
					$(myLabel).popover("show");

					//prepare and attach tooltips to action buttons
					$(myActionMenu).tooltip({
							container: myActionMenu,
							selector: "[data-toggle=tooltip]"
					});
			}
			myLabel.css("cursor","pointer");
	});


	//onclick event delegation for the labels
	$(currentForm).delegate('li.tree_node_flag label', 'click', function() {
			//e.stop();
			var clickedLabel = this;

			//prepare and attach popover to selected label
			$(clickedLabel).popover({
					html: true,
					content: myActionMenu,
					placement: "right",
					animation: false,
					container: currentForm
			});
			$(clickedLabel).popover("show");

			//destroy any popover except for the one of the clicked label
			//destroy proved to be asynchronous, that's why we are excluding the
			//unwanted label using .not()
			labels.not(clickedLabel).popover('destroy');

			//prepare and attach tooltips to action buttons
			$(myActionMenu).tooltip({
					container: myActionMenu,
					selector: "[data-toggle=tooltip]"
			});

			//remove the class text-info from the other labels
			labels.removeClass('text-info');
			//add the class to the clicked label
			$(clickedLabel).addClass('text-info');

	});
});

})
</script>
