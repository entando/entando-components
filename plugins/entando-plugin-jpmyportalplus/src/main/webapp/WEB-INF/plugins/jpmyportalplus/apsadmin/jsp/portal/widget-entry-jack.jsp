<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<fieldset class="col-xs-12">
	<legend><s:text name="jpmyportalplus.widget.swappableflag.long" /></legend>
	<s:if test="strutsAction != 2 || #widgetTypeVar.logic || 
		  null == #widgetTypeVar.typeParameters || #widgetTypeVar.typeParameters.size() == 0">
		<div class="form-group">
			<wpsf:checkbox id="jpmyportalplus_swappable" name="swappable" />
			<label for="jpmyportalplus_swappable"><s:text name="jpmyportal.label.widget.swappable" /></label>
		</div>
	</s:if>
	<s:else>
		<div class="form-group">
			<s:text name="jpmyportalplus.widget.not.compatible" />
		</div>
	</s:else>
</fieldset>