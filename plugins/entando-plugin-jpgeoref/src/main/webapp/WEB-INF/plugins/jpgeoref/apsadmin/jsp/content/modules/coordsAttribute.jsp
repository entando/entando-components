<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<s:if test="#lang.default">
	<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
		<div class="margin-small-bottom">
			<div class="input-group">
				<span class="input-group-addon">X</span>
				<wpsf:textfield useTabindexAutoIncrement="true" id="%{'x_'+#attribute.name}"
								name="x_%{#attribute.name}" value="%{#attribute.x}"
								maxlength="254" cssClass="text" cssClass="form-control" />
			</div>
		</div>
		<div class="margin-small-bottom">
			<div class="input-group">
				<span class="input-group-addon">Y</span>
				<wpsf:textfield useTabindexAutoIncrement="true" id="%{'y_'+#attribute.name}"
								name="y_%{#attribute.name}" value="%{#attribute.y}"
								maxlength="254" cssClass="text" cssClass="form-control" />
			</div>
		</div>
		<div class="margin-small-bottom">
			<div class="input-group">
				<span class="input-group-addon">Z</span>
				<wpsf:textfield useTabindexAutoIncrement="true" id="%{'z_'+#attribute.name}"
								name="z_%{#attribute.name}" value="%{#attribute.z}"
								maxlength="254" cssClass="text" cssClass="form-control" />
			</div>
		</div>
	</div>
	<div class="col-xs-12 col-sm-12 col-md-6 col-lg-6">
		<div class="margin-base-bottom"><div id="mapcontainer_<s:property value="%{#attribute.name}" />" class="jpgeoref-mapcontainer display-block margin-base-bottom"></div></div>
	</div>
</s:if>
<s:else>
	<div class="alert alert-info"><s:text name="note.editContent.doThisInTheDefaultLanguage.must" /></div>
</s:else>
