<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<fieldset class="form-horizontal">
	<legend><s:text name="jpmyportalplus.widget.swappableflag.long" /></legend>
	<s:if test="strutsAction != 2 || #widgetTypeVar.logic || null == #widgetTypeVar.typeParameters || #widgetTypeVar.typeParameters.size() == 0">
		<div class="form-group">
			<label for="jpmyportalplus_swappable" class="col-sm-2 control-label">
				<s:text name="jpmyportal.label.widget.swappable" />
			</label>
            <div class="col-sm-10">
			    <wpsf:checkbox id="jpmyportalplus_swappable" name="swappable" cssClass="radiocheck bootstrap-switch"  data-toggle="toggle"/>
            </div>
		</div>
	</s:if>
	<s:else>
		<div class="form-group">
			<s:text name="jpmyportalplus.widget.not.compatible" />
		</div>
	</s:else>
</fieldset>