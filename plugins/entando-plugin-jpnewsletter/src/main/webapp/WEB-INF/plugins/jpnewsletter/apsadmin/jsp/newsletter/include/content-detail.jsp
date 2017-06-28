<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="/apsadmin-core" prefix="wpsa"%>
<%@ taglib uri="/apsadmin-form" prefix="wpsf"%>
<%@ taglib prefix="wp" uri="/aps-core"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="carriageReturnChar" value="\r" />
<c:set var="tabChar" value="\t" />

<legend>
    <s:text name="jpnewsletter.title.newsletterEntry.info" />:
    <em><s:property value="contentVo.descr" /></em>
</legend>
<s:set var="contentReport" value="contentReport" />
<s:if test="%{#contentReport==null}">
    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <p>
            <s:text name="Message.newsletter.notSent" />
        </p>
    </div>
</s:if>
<s:else>
    <div class="alert alert-info">
        <span class="pficon pficon-info"></span>
        <p>
            <s:text name="Message.newsletter.sent" />
            &nbsp;
            <s:date name="%{#contentReport.sendDate}" format="dd/MM/yyyy HH:mm" />
        </p>
    </div>
    <table class="table table-bordered no-mb">
        <tr>
            <th class="text-right table-w-10">
                <s:text name="jpnewsletter.label.subject" />
            </th>
            <td>
                <s:property value="%{#contentReport.subject}" />
            </td>
        </tr>
        <tr>
            <th class="text-right table-w-10">
                <s:text name="jpnewsletter.label.simpleTextBody" />
            </th>
            <td>
                <c:set var="templateVar">
                    <s:property value="#contentReport.textBody" />
                </c:set>
                <c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(templateVar, tabChar, '&emsp;'),carriageReturnChar, '')}" />
                <pre><code><c:out value="${ESCAPED_STRING}" escapeXml="true" /></code></pre>
            </td>
        </tr>
        <s:if test="%{null != #contentReport.htmlBody && #contentReport.htmlBody != ''}">
            <tr>
                <th class="text-right table-w-10">
                    <s:text name="jpnewsletter.label.viewHtmlBody" />
                </th>
                <td>
                    <c:set var="templateVar">
                        <s:property value="#contentReport.htmlBody" />
                    </c:set>
                    <c:set var="ESCAPED_STRING" value="${fn:replace(fn:replace(templateVar, tabChar, '&emsp;'),carriageReturnChar, '')}" />
                    <pre><code><c:out value="${ESCAPED_STRING}" escapeXml="true" /></code></pre>
                </td>
            </tr>
        </s:if>
    </table>
    <s:if test="%{#contentReport.recipients != null && #contentReport.recipients.size() > 0}">
        <hr />
        <table class="table table-striped table-bordered table-hover no-mb">
            <thead>
                <tr>
                    <th>
                        <s:text name="label.username" />
                    </th>
                    <th>
                        <s:text name="label.eMail" />
                    </th>
                </tr>
            </thead>
            <tbody>
                <s:iterator value="%{#contentReport.recipients.entrySet()}" var="recipient">
                    <tr>
                        <td>
                            <code>
                                <s:property value="#recipient.key" />
                            </code>
                        </td>
                        <td>
                            <s:property value="#recipient.value" />
                        </td>
                    </tr>
                </s:iterator>
            </tbody>
        </table>
    </s:if>
</s:else>