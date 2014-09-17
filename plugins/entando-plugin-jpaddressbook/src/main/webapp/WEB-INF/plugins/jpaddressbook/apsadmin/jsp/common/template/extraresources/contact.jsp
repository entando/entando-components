<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-common.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/layouts/assets-more/inc/snippet-datepicker.jsp" />

<script type="text/javascript" src="<wp:resourceURL />administration/js/ckeditor/ckeditor.js"></script>
<script type="text/javascript" src="<wp:resourceURL />administration/js/ckeditor/adapters/jquery.js"></script>

<script type="text/javascript">
	jQuery(function() {
		$('[data-toggle="entando-hypertext"]').ckeditor({
			customConfig : '/portalexample/resources/administration/js/ckeditor/entando-ckeditor_config.js',
			EntandoLinkActionPath: "/portalexample/do/jacms/Content/Hypertext/entandoInternalLink.action?contentOnSessionMarker=ANN_newContent",
			language: '<s:property value="locale" />'
		});
	});
</script>


<%--
<s:include value="/WEB-INF/apsadmin/jsp/common/template/defaultExtraResources.jsp" />
<s:include value="/WEB-INF/apsadmin/jsp/common/template/extraresources/inc/snippet-calendar.jsp" />
<script type="text/javascript">
<!--//--><![CDATA[//><!--

<s:set name="lang" value="defaultLang" />
//per attributo Date
<s:iterator value="contact.attributes" var="attribute">

<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

<s:if test="#attribute.type == 'Date'">
window.addEvent('domready', function() { myCal_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" /> = new Calendar({ <s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />: 'd/m/Y' }, {
		navigation: 1,
		months: ['<s:text name="calendar.month.gen" />','<s:text name="calendar.month.feb" />','<s:text name="calendar.month.mar" />','<s:text name="calendar.month.apr" />','<s:text name="calendar.month.may" />','<s:text name="calendar.month.jun" />','<s:text name="calendar.month.jul" />','<s:text name="calendar.month.aug" />','<s:text name="calendar.month.sep" />','<s:text name="calendar.month.oct" />','<s:text name="calendar.month.nov" />','<s:text name="calendar.month.dec" />'],
		days: ['<s:text name="calendar.week.sun" />','<s:text name="calendar.week.mon" />','<s:text name="calendar.week.tue" />','<s:text name="calendar.week.wen" />','<s:text name="calendar.week.thu" />','<s:text name="calendar.week.fri" />','<s:text name="calendar.week.sat" />']
	});});
</s:if>

<s:if test="#attribute.type == 'Monolist'">
<s:set name="masterAttributeTracer" value="#attributeTracer" />
<s:set name="masterAttribute" value="#attribute" />
<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
<s:set name="attributeTracer" value="#masterAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>
<s:set name="elementIndex" value="#elementStatus.index" />
	<s:if test="#attribute.type == 'Date'">
window.addEvent('domready', function() { myCal_<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" /> = new Calendar({ <s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />: 'd/m/Y' }, {
		navigation: 1,
		months: ['<s:text name="calendar.month.gen" />','<s:text name="calendar.month.feb" />','<s:text name="calendar.month.mar" />','<s:text name="calendar.month.apr" />','<s:text name="calendar.month.may" />','<s:text name="calendar.month.jun" />','<s:text name="calendar.month.jul" />','<s:text name="calendar.month.aug" />','<s:text name="calendar.month.sep" />','<s:text name="calendar.month.oct" />','<s:text name="calendar.month.nov" />','<s:text name="calendar.month.dec" />'],
		days: ['<s:text name="calendar.week.sun" />','<s:text name="calendar.week.mon" />','<s:text name="calendar.week.tue" />','<s:text name="calendar.week.wen" />','<s:text name="calendar.week.thu" />','<s:text name="calendar.week.fri" />','<s:text name="calendar.week.sat" />']
	});});
	</s:if>
</s:iterator>
<s:set name="attributeTracer" value="#masterAttributeTracer" />
<s:set name="attribute" value="#masterAttribute" />
</s:if>

</s:iterator>
//fine attributo Date

//per attributo Hypertext
<s:if test="htmlEditorCode != 'none'">

	<s:iterator value="userProfile.attributeList" id="attribute">
	<wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

	<s:if test="#attribute.type == 'Hypertext'">

		<s:if test="htmlEditorCode == 'fckeditor'">
			window.addEvent('domready', function() {
				var ofckeditor = new FCKeditor( "<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" );
				ofckeditor.Config["AppBaseUrl"] = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
				ofckeditor.BasePath = "<wp:resourceURL />administration/js/fckeditor/";
				ofckeditor.ToolbarSet = "jAPS-default";
				ofckeditor.Config["CustomConfigurationsPath"] = "<wp:resourceURL />administration/js/fckeditor/jAPSConfig.js";
				ofckeditor.Height = 250;
				ofckeditor.ReplaceTextarea();
			});
		</s:if>
		<s:if test="htmlEditorCode == 'hoofed'">
			window.addEvent('domready', function() {
				var ohoofed = new HoofEd({
					basePath: '<wp:resourceURL />administration/js/moo-japs/hoofed',
					lang: '<s:property value="currentLang.code" />',
					textareaID: '<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />',
					buttons: [ 'bold', 'italic', 'list', 'nlist', 'link', 'paragraph' ],
					toolPosition: "after",
					toolElement: "span"
				});
			});
		</s:if>
	</s:if>

	<s:if test="#attribute.type == 'Monolist'">
		<s:set name="masterAttributeTracer" value="#attributeTracer" />
		<s:set name="masterAttribute" value="#attribute" />
		<s:iterator value="#attribute.attributes" id="attribute" status="elementStatus">
			<s:set name="attributeTracer" value="#masterAttributeTracer.getMonoListElementTracer(#elementStatus.index)"></s:set>
			<s:set name="elementIndex" value="#elementStatus.index" />


			<s:if test="#attribute.type == 'Composite'">
				<s:set name="masterCompositeAttributeTracer" value="#attributeTracer" />
				<s:set name="masterCompositeAttribute" value="#attribute" />
				<s:iterator value="#attribute.attributes" id="attribute">
					<s:set name="attributeTracer" value="#masterCompositeAttributeTracer.getCompositeTracer(#masterCompositeAttribute)"></s:set>
					<s:set name="parentAttribute" value="#masterCompositeAttribute"></s:set>
					<s:if test="#attribute.type == 'Hypertext'">
						<s:if test="htmlEditorCode == 'fckeditor'">
							window.addEvent('domready', function() {
								var ofckeditor = new FCKeditor( "<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" );
								ofckeditor.Config["AppBaseUrl"] = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
								ofckeditor.BasePath = "<wp:resourceURL />administration/js/fckeditor/";
								ofckeditor.ToolbarSet = "jAPS-default";
								ofckeditor.Config["CustomConfigurationsPath"] = "<wp:resourceURL />administration/js/fckeditor/jAPSConfig.js";
								ofckeditor.Height = 250;
								ofckeditor.ReplaceTextarea();
							});
						</s:if>
						<s:if test="htmlEditorCode == 'hoofed'">
							window.addEvent('domready', function() {
								var ohoofed = new HoofEd({
									basePath: '<wp:resourceURL />administration/js/moo-japs/hoofed',
									lang: '<s:property value="currentLang.code" />',
									textareaID: '<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />',
									buttons: [ 'bold', 'italic', 'list', 'nlist', 'link', 'paragraph' ],
									toolPosition: "after",
									toolElement: "span"
								});
							});
						</s:if>
					</s:if>
				</s:iterator>
				<s:set name="attributeTracer" value="#masterCompositeAttributeTracer" />
				<s:set name="attribute" value="#masterCompositeAttribute" />
				<s:set name="parentAttribute" value=""></s:set>
			</s:if>


			<s:if test="#attribute.type == 'Hypertext'">
				<s:if test="htmlEditorCode == 'fckeditor'">
					window.addEvent('domready', function() {

						var ofckeditor = new FCKeditor( "<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" );
						ofckeditor.Config["AppBaseUrl"] = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
						ofckeditor.BasePath = "<wp:resourceURL />administration/js/fckeditor/";
						ofckeditor.ToolbarSet = "jAPS-default";
						ofckeditor.Config["CustomConfigurationsPath"] = "<wp:resourceURL />administration/js/fckeditor/jAPSConfig.js";
						ofckeditor.Height = 250;
						ofckeditor.ReplaceTextarea();
					});
				</s:if>
				<s:if test="htmlEditorCode == 'hoofed'">
					window.addEvent('domready', function() {
						var ohoofed = new HoofEd({
							basePath: '<wp:resourceURL />administration/js/moo-japs/hoofed',
							lang: '<s:property value="currentLang.code" />',
							textareaID: '<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />',
							buttons: [ 'bold', 'italic', 'list', 'nlist', 'link', 'paragraph' ],
							toolPosition: "after",
							toolElement: "span"
						});
					});
				</s:if>
			</s:if>
		</s:iterator>
		<s:set name="attributeTracer" value="#masterAttributeTracer" />
		<s:set name="attribute" value="#masterAttribute" />
	</s:if>
	</s:iterator>

</s:if>
//fine attributo Hypertext
<s:if test="#myClient == 'advanced'">
window.addEvent('domready', function(){
	var myTips = new Tips('.attribute-meta-tip', {
		className: 'tip-container',
		offset: {'x': 22, 'y': -8},
		fixed: true
	});

	$$('.attribute-meta-tip').each(function(el) {
		var myTitle = '<s:text name="label.info" />'
		var myTipText = "<ul>";
		//myTipText = el.innerHTML;
		el.getElements("abbr").each(function(abbr) {
			myTipText = myTipText + "<li>";
			myTipText = myTipText + abbr.get("title");
			if ( abbr.getNext(".attribute-meta-tip-info").get('text') != "") {
				myTipText = myTipText + ": " + abbr.getNext(".attribute-meta-tip-info").get('text');
			}
			myTipText = myTipText + "</li>";
		});

		myTipText = myTipText + "</ul>"

		el.innerHTML = "?";

		el.store('tip:title', myTitle);
		el.store('tip:text', myTipText);

		el.addClass('tip-handler');
	});


});
</s:if>


//--><!]]></script>
--%>
