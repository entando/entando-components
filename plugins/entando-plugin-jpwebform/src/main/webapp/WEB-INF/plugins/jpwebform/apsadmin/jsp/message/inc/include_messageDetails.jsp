<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<div class="centerText">
    <table class="table table-bordered">
        <tr>
            <th class="text-right"><s:text name="label.user" /></th>
            <td><code><s:property value="%{message.username}" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="label.creationDate" /></th>
            <td><code><s:date name="%{message.creationDate}" format="dd/MM/yyyy HH:mm" /></code></td>
        </tr>
        <tr>
            <th class="text-right"><s:text name="title.messageManagement.details.info" /></th>
            <td><s:property value="#typeDescr"/></td>
        </tr>

        <s:set name="lang" value="defaultLang" />
        <s:iterator value="message.attributeList" var="attribute">
            <%-- INIZIALIZZAZIONE TRACCIATORE --%>
            <s:set name="attributeTracer" value="initAttributeTracer(#attribute, #lang)"></s:set>
                <tr>
                    <th class="text-right"><s:property value="#attribute.name" /></th>
                <td>
                    <s:if test="#attribute.type == 'Boolean' || #attribute.type == 'CheckBox'">
                        <%-- ############# ATTRIBUTO Boolean ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/booleanAttribute.jsp" />
                    </s:if>

                    <s:elseif test="#attribute.type == 'ThreeState'">
                        <%-- ############# ATTRIBUTO ThreeState ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/threeStateAttribute.jsp" />
                    </s:elseif>

                    <s:elseif test="#attribute.type == 'Date'">
                        <%-- ############# ATTRIBUTO ThreeState ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/dateAttribute.jsp" />
                    </s:elseif>

                    <s:elseif test="#attribute.type == 'Number'">
                        <%-- ############# ATTRIBUTO ThreeState ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/numberAttribute.jsp" />
                    </s:elseif>

                    <s:elseif test="#attribute.type == 'Composite'">
                        <%-- ############# ATTRIBUTO Composite ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/entity/view/compositeAttribute.jsp" />
                    </s:elseif>

                    <s:elseif test="#attribute.type == 'List'">
                        <%-- ############# ATTRIBUTO List ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/entity/view/listAttribute.jsp" />
                    </s:elseif>

                    <s:elseif test="#attribute.type == 'Monolist'">
                        <%-- ############# ATTRIBUTO Monolist ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/entity/view/monolistAttribute.jsp" />
                    </s:elseif>

                    <s:elseif test="#attribute.type == 'File'">
                        <%-- ############# ATTRIBUTO File ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/fileAttribute.jsp" />
                    </s:elseif>

                    <s:else>
                        <%-- ############# ATTRIBUTO Monotext (Text, Longtext, Enumerator, ecc.) ############# --%>
                        <s:include value="/WEB-INF/plugins/jpwebform/apsadmin/jsp/message/modules/view/monotextAttribute.jsp" />
                    </s:else>
                </td>
            </tr>
        </s:iterator>
    </table>
</div>
