<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<h1><s:text name="title.contentNotifierManagement.configuration" /></h1>
<div id="main">
	<s:form action="save" method="post">
		<%-- 
		<fieldset>
			<legend><s:text name="notifier.generalSettings" /></legend>
		</fieldset>
		--%>
		<fieldset>
			<legend><s:text name="notifier.schedulerSettings" /></legend>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" id="notifier_active" name="active" value="%{active}" cssClass="radiocheck" />
				<label for="notifier_active"><s:text name="notifier.active" /></label>
			</p>
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" id="notifier_onlyOwner" name="onlyOwner"  value="%{onlyOwner}" cssClass="radiocheck" />
				<label for="notifier_onlyOwner"><s:text name="notifier.onlyOwner"/></label>
			</p>
			<p>
				<s:set name="hoursDelayVar" value="%{hoursDelay}" scope="page" />
				<label for="hoursDelay" class="basic-mint-label"><s:text name="notifier.delay"/></label>
				<select name="hoursDelay" id="hoursDelay" tabindex="<wpsa:counter />">
					<c:forEach begin="1" end="10" varStatus="status">
						<option <c:if test="${(status.count*24) == hoursDelayVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count*24}" />" ><c:out value="${status.count*24}" /></option>
					</c:forEach>
				</select>
			</p>
			
			<p>
				<s:set name="dayVar" value="day" scope="page" />
				<span class="bold"><s:text name="notifier.startDate"/>&#32;(<label for="day"><s:text name="notifier.day"/></label>/<label for="notifier.month"><s:text name="notifier.month"/></label>/<label for="notifier.year"><s:text name="notifier.year"/></label>)</span>
				<select name="day" id="day" tabindex="<wpsa:counter />" class="smallsize">
					<c:forEach begin="1" end="31" varStatus="status">
						<option <c:if test="${status.count == dayVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count}"/>"><c:out value="${status.count}"/></option>
					</c:forEach>
				</select>
				<s:set name="monthVar" value="month" scope="page" />
				<select id="notifier.month" name="month" tabindex="<wpsa:counter />" class="smallsize">
					<c:forEach begin="1" end="12" varStatus="status">
						<option <c:if test="${(status.count-1) == monthVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count-1}"/>"><c:out value="${status.count}"/></option>
					</c:forEach>
				</select>
				<s:set name="yearVar" value="year" scope="page" />
				<select id="notifier.year" name="year" tabindex="<wpsa:counter />" class="smallsize">
					<c:forEach begin="2007" end="2020" varStatus="status">
						<option <c:if test="${(status.count+2008-1) == yearVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count+2008-1}"/>"><c:out value="${status.count+2008-1}"/></option>
					</c:forEach>
				</select>
			</p>
			
			<p>
				<span class="bold"><s:text name="notifier.time"/>&#32;(<label for="time"><s:text name="notifier.hour" /></label>/<label for="minute"><s:text name="notifier.minutes" /></label>)</span>
				<s:set name="hourVar" value="hour" scope="page" />
				<select name="hour" id="time" tabindex="<wpsa:counter />" class="smallsize">
					<c:forEach begin="1" end="24" varStatus="status">
						<option <c:if test="${(status.count-1) == hourVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count-1}"/>"><c:out value="${status.count-1}"/></option>
					</c:forEach>
				</select>:
				<s:set name="minuteVar" value="minute" scope="page" />
				<select name="minute" id="minute" tabindex="<wpsa:counter />" class="smallsize">
					<c:forEach begin="1" end="60" varStatus="status">
						<option <c:if test="${(status.count-1) == minuteVar}">selected="selected"</c:if> 
							value="<c:out value="${status.count-1}"/>"><c:out value="${status.count-1}"/></option>
					</c:forEach>
				</select>
			</p>
		</fieldset>
		
		<fieldset>
			<legend><s:text name="notifier.mailSettings"/></legend>
			
			<p class="noscreen"><wpsf:hidden name="notifyRemove" id="notifyRemove" /></p>
			
			<p>
				<label for="notifier.senderCode" class="basic-mint-label"><s:text name="notifier.senderCode"/>:</label>
				<wpsf:select useTabindexAutoIncrement="true" name="senderCode" id="notifier.senderCode" list="senderCodes" />
			</p>
			
			<p>
				<label for="mailAttrName" class="basic-mint-label"><s:text name="notifier.mailAttrName"/>:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" name="mailAttrName" id="mailAttrName" cssClass="text" />
			</p>
			
			<p>
				<wpsf:checkbox useTabindexAutoIncrement="true" name="html" id="notifer_html" value="html"  cssClass="radiocheck"/>
				<label for="notifer_html"><s:text name="notifier.html"/></label>
			</p>
		</fieldset>
		<fieldset>
			<legend><s:text name="notifier.emailTemplate" /></legend>
			
			<p><s:text name="notifier.emailTemplate.variableTemplateIntro" /></p>
			
			<dl class="table-display">
				<dt><code>{type}</code></dt>
				<dd>
				<s:text name="notifier.emailTemplate.type" /></dd>
			
				<dt><code>{descr}</code></dt>
				<dd>
				<s:text name="notifier.emailTemplate.descr" /></dd>
			
				<dt><code>{date}</code></dt>
				<dd>
				<s:text name="notifier.emailTemplate.date" /></dd>
				
				<dt><code>{time}</code></dt>
				<dd><s:text name="notifier.emailTemplate.time" /></dd>
				
				<dt><code>{link}</code></dt>
				<dd>
				<s:text name="notifier.emailTemplate.link" /></dd>
			</dl>
			
			<p>
				<label for="notifer_subject" class="basic-mint-label"><s:text name="notifier.subject"/>:</label>
				<wpsf:textfield useTabindexAutoIncrement="true" cssClass="text" name="subject" id="notifer_subject" value="%{subject}" />
			</p>
			
			<p>
				<label for="notifier_header" class="basic-mint-label"><s:text name="notifier.header"/>:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="header" id="notifier_header" cols="50" rows="10" value="%{header}" />
			</p>
			
			<p>
				<label for="notifier.templateInsert" class="basic-mint-label"><s:text name="notifier.templateInsert"/>:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" name="templateInsert" id="notifier.templateInsert" cols="50" rows="6" value="%{templateInsert}" />
			</p>
			
			<p>
				<label for="notifier.templateUpdate" class="basic-mint-label"><s:text name="notifier.templateUpdate"/>:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" id="notifier.templateUpdate" name="templateUpdate" cols="50" rows="6" value="%{templateUpdate}"  />
			</p>	
			
		<s:if test="notifyRemove">
			<p>
				<label for="templateRemove" class="basic-mint-label"><s:text name="notifier.templateRemove"/>:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" id="templateRemove" name="templateRemove" cols="50" rows="6" value="%{templateRemove}" />
			</p>
		</s:if>
			
			<p>		
				<label for="notifier.footer" class="basic-mint-label"><s:text name="notifier.footer"/>:</label>
				<wpsf:textarea useTabindexAutoIncrement="true" id="notifier.footer" name="footer" cols="50" rows="6"  value="%{footer}" />
			</p>
			
		</fieldset>
		
		<p class="centerText">
			<wpsf:submit useTabindexAutoIncrement="true" cssClass="button" value="%{getText('label.save')}" />
		</p>
		
	</s:form>
</div>