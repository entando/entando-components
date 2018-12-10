<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form"%>

<section>
	<h1><wp:i18n key="jpiot_IOTLISTDEVICES_TRASH" /></h1>

	<form action="<wp:action path="/ExtStr2/do/FrontEnd/jpiot/IotListDevices/delete.action" />" method="post">
		<s:if test="hasFieldErrors()">
			<div class="alert alert-error">
				<h2><s:text name="message.title.FieldErrors" /></h2>
				<ul>
					<s:iterator value="fieldErrors">
						<s:iterator value="value">
           <li><s:property/></li>
						</s:iterator>
					</s:iterator>
				</ul>
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="alert alert-error">
				<h2><s:text name="message.title.ActionErrors" /></h2>
				<ul>
					<s:iterator value="actionErrors">
					<li><s:property/></li>
					</s:iterator>
				</ul>
			</div>
		</s:if>

		<p class="sr-only">
			<wpsf:hidden name="strutsAction" />
			<wpsf:hidden name="id" />
		</p>

		<div class="alert alert-warning">
			<p>
				<wp:i18n key="jpiot_IOTLISTDEVICES_TRASH_CONFIRM" />&#32;<wp:i18n key="jpiot_IOTLISTDEVICES_ID" />&#32;<s:property value="id" />?
			</p>

			<p>
				<wpsf:submit type="button" cssClass="btn btn-warning">
					<span class="icon-trash icon-white"></span>&#32;
					<wp:i18n key="TRASH" />
				</wpsf:submit>
			</p>
		</div>


	</form>
</section>