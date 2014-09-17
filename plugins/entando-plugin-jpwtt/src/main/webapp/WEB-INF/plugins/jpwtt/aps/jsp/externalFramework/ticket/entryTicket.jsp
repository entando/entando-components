<%@ page contentType="charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="showlet jpwtt">
	<s:set name="customShowletTitle"><wp:i18n key="jpwtt_SHOWLET_TITLE" /></s:set>

		<c:choose>
			<c:when test="${sessionScope.currentUser != 'guest'}">
			<%-- if the user is logged in....  --%>
				<form action="<wp:action path="/ExtStr2/do/jpwtt/Ticket/User/save.action"/>" method="post">
					<s:if test="hasFieldErrors()">
						<div class="message message_error">
							<h3><s:text name="message.title.FieldErrors" /></h3>
							<ul>
								<s:iterator value="fieldErrors">
									<s:iterator value="value"><li><span><s:property /></span></li></s:iterator>
								</s:iterator>
							</ul>
						</div>
					</s:if>

					<s:if test="hasActionErrors()">
						<div class="message message_error">
							<h3><s:text name="message.title.ActionErrors" /></h3>
							<ul>
								<s:iterator value="actionErrors">
									<li><span><s:property /></span></li>
								</s:iterator>
							</ul>
						</div>
					</s:if>

					<c:set var="MANDATORY_SNIPPET">&nbsp;<abbr title="<wp:i18n key="jpwtt_MANDATORY_FULL" />"><wp:i18n key="jpwtt_MANDATORY_SHORT" /></abbr></c:set>
					<p>
						<label for="invia_segnalazione_cognome"><wp:i18n key="jpwtt_COGNOME" />:</label><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" /><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="cognome" id="invia_segnalazione_cognome" />
					</p>

					<p>
						<label for="invia_segnalazione_nome"><wp:i18n key="jpwtt_NOME" />:</label><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" /><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text"  name="nome" id="invia_segnalazione_nome" />
					</p>

					<p>
						<label for="invia_segnalazione_codFisc"><wp:i18n key="jpwtt_CODICEFISCALE" />:</label><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" /><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="codFisc" maxlength="16" id="invia_segnalazione_codFisc" />
					</p>

					<p>
						<label for="invia_segnalazione_comune"><wp:i18n key="jpwtt_COMUNE" />:</label><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" /><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="comune" id="invia_segnalazione_comune" />
					</p>

					<p>
						<label for="invia_segnalazione_localita"><wp:i18n key="jpwtt_LOCALITA" />:</label><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" /><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text"  name="localita" id="invia_segnalazione_localita" />
					</p>

					<p>
						<span><wp:i18n key="jpwtt_INDIRIZZO" /></span><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" /><br />
						<span class="labelWidth">
						<%--
						<label for="invia_segnalazione_tipoIndirizzo">tipoIndirizzo</label>
							<wpsf:textfield useTabindexAutoIncrement="true" cssClass="labelWidth" name="tipoIndirizzo" id="invia_segnalazione_tipoIndirizzo" />
						--%>
						<select id="tipo_via_piazza" name="tipoIndirizzo">
							<option value="Via" <s:if test="%{tipoIndirizzo == 'Via'}">selected="selected"</s:if>><wp:i18n key="jpwtt_INDIRIZZO_VIA" /></option>
							<option value="Piazza" <s:if test="%{tipoIndirizzo == 'Piazza'}">selected="selected"</s:if>><wp:i18n key="jpwtt_INDIRIZZO_PIAZZA" /></option></select>
						</span>
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="indirizzo" id="invia_segnalazione_indirizzo"  />
					</p>

					<p>
						<label for="invia_segnalazione_numero"><wp:i18n key="jpwtt_ADDRESS_NUMBER" />:</label><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="numeroIndirizzo" id="invia_segnalazione_numero" maxlength="6"/>
					</p>

					<p>
						<label for="invia_segnalazione_telefono"><wp:i18n key="jpwtt_TELEPHONE" />:</label><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="telefono" id="invia_segnalazione_telefono"  />
					</p>

					<p>
						<label for="invia_segnalazione_email"><wp:i18n key="jpwtt_EMAIL" />:</label><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="email" id="invia_segnalazione_email" />
					</p>

					<p>
						<label class="labelWidth" for="invia_segnalazione_emailConfirm"><wp:i18n key="jpwtt_EMAIL_CONFIRM" />:</label><br />
						<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="emailConfirm" id="invia_segnalazione_emailConfirm"  />
					</p>

					<p>
						<label for="invia_segnalazione_message"><wp:i18n key="jpwtt_TEXT" />:</label><br />
						<wpsf:textarea useTabindexAutoIncrement="true" rows="5" cols="10" cssClass="text" name="message" id="invia_segnalazione_message" />
					</p>

					<p>
						<label for="invia_segnalazione_interventionType"><wp:i18n key="jpwtt_INTERVENTION_TYPE" />:</label><br />
						<%-- <wpsf:textfield useTabindexAutoIncrement="true" name="interventionType" id="invia_segnalazione_interventionType" /> --%>
						<%--
						<wpsf:select useTabindexAutoIncrement="true" name="interventionType" id="invia_segnalazione_interventionType"
								headerKey="" headerValue="Generico"
								list="interventionTypes.values()" listKey="id" listValue="descr" />
						--%>

						<select name="interventionType" id="invia_segnalazione_interventionType" tabindex="<wpsa:counter />">
							<option value=""><wp:i18n key="jpwtt_IT_GENERIC" /></option>
							<s:iterator var="item" value="%{interventionTypes.values()}">
								<option value="<s:property value="#item.id"/>" <s:if test="%{interventionType == #item.id}"> selected="selected"</s:if>>
									<c:set var="I18N_DESCR_KEY">jpwtt_IT_<s:property value="#item.descr"/></c:set>
									<wp:i18n key="${I18N_DESCR_KEY}" />
								</option>
							</s:iterator>
						</select>
					</p>

					<p class="leggi">
						<a href="<wp:url page="privacy" />">
							<wp:i18n key="jpwtt_READ_PRIVACY_AGREE" />
						</a>
					</p>

					<p>
						<wpsf:checkbox useTabindexAutoIncrement="true" name="trattamentoDati" id="auth_check"  />
						<label class="inline" for="auth_check"><wp:i18n key="jpwtt_AGREE_PRIVACY" /></label><c:out value="${MANDATORY_SNIPPET}" escapeXml="false" />
					</p>

					<p class="centerText">
						<input type="hidden" name="request_locale" value="<wp:info key="currentLang" />" />
						<input type="submit" class="button" value="<wp:i18n key="jpwtt_SEND" />" />
					</p>
				</form>
			</c:when>
			<c:otherwise>
			<%-- please login... --%>
				<p><wp:i18n key="jpwtt_PLEASE_LOGIN" /></p>
			</c:otherwise>
		</c:choose>

</div>