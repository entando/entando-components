<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>

<wp:headInfo type="JS" info="entando-misc-jquery/jquery-1.10.0.min.js" />
<wp:headInfo type="JS" info="entando-misc-bootstrap/bootstrap.min.js" />

<c:set var="javascript_date_helper_code">
    /* Italian initialisation for the jQuery UI date picker plugin. */
    /* Written by Antonello Pasella (antonello.pasella@gmail.com). */
    jQuery(function($){
    $.datepicker.regional['it'] = {
    closeText: 'Chiudi',
    prevText: '&#x3c;Prec',
    nextText: 'Succ&#x3e;',
    currentText: 'Oggi',
    monthNames: ['Gennaio','Febbraio','Marzo','Aprile','Maggio','Giugno',
    'Luglio','Agosto','Settembre','Ottobre','Novembre','Dicembre'],
    monthNamesShort: ['Gen','Feb','Mar','Apr','Mag','Giu',
    'Lug','Ago','Set','Ott','Nov','Dic'],
    dayNames: ['Domenica','Luned&#236','Marted&#236','Mercoled&#236','Gioved&#236','Venerd&#236','Sabato'],
    dayNamesShort: ['Dom','Lun','Mar','Mer','Gio','Ven','Sab'],
    dayNamesMin: ['Do','Lu','Ma','Me','Gi','Ve','Sa'],
    weekHeader: 'Sm',
    dateFormat: 'dd/mm/yy',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: ''};
    });
</c:set>

<c:set var="javascript_attribute_code">
    <s:iterator value="langs" var="lang">
        <s:iterator value="content.attributeList" var="attribute">
            <wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

            <s:if test="#attribute.type == 'Date'">
                <wp:headInfo type="JS_RAW" info="${javascript_date_helper_code}" />
                <wp:headInfo type="JS" info="entando-misc-html5-essentials/modernizr-2.5.3-full.js" />
                <wp:headInfo type="JS_EXT" info="http://code.jquery.com/ui/1.10.3/jquery-ui.js" />
                <wp:headInfo type="CSS_EXT" info="http://code.jquery.com/ui/1.10.3/themes/base/jquery-ui.css" />
                <wp:headInfo type="JS_RAW" info="${js_for_datepicker}" />
                jQuery(function($) {
                if (Modernizr.touch && Modernizr.inputtypes.date) {
                $.each(	$('.attribute-type-Date input[type="text"]'), function(index, item) {
                item.type = 'date';
                });
                } else {
                $.datepicker.setDefaults( $.datepicker.regional[ "<wp:info key="currentLang" />" ] );
                $('.attribute-type-Date input[type="text"]').datepicker({
                changeMonth: true,
                changeYear: true,
                dateFormat: 'dd/mm/yy'
                });
                }
                });
            </s:if>

            <s:if test="#attribute.type == 'Hypertext'">
                <wp:headInfo type="JS" info="../../plugins/jpfastcontentedit/static/js/fckeditor/fckeditor.js" />
                jQuery(function(){
                var ofckeditor = new FCKeditor( "<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />" ); 
                ofckeditor.Config["AppBaseUrl"] = "<wp:info key="systemParam" paramName="applicationBaseURL" />";
                ofckeditor.BasePath = "<wp:resourceURL />plugins/jpfastcontentedit/static/js/fckeditor/";
                ofckeditor.Config["CustomConfigurationsPath"] = "<wp:resourceURL />plugins/jpfastcontentedit/static/js/fckeditor/fastContentEdit_jAPSConfig.js";
                ofckeditor.ToolbarSet = "jAPS";			
                ofckeditor.Height = 250;
                ofckeditor.ReplaceTextarea();
                });
            </s:if>
        </s:iterator>
    </s:iterator>
</c:set>
<wp:headInfo type="JS_RAW" info="${javascript_attribute_code}" /> 

<%-- <s:set name="removeIcon" var="removeIcon"><wp:resourceURL/>administration/common/img/icons/list-remove.png</s:set> --%>
<s:set name="removeIcon" var="removeIcon"><wp:resourceURL/>icon icon-remove icon-white</s:set>

    <h1><wp:i18n key="jpfastcontentedit_FASTCONTENTEDIT_WIDGET_TITLE" /></h1>



<p>
    <wp:i18n key="jpfastcontentedit_WORKING_ON" />:&#32;<em><s:property value="content.descr" /></em> (<s:property value="content.typeDescr" />)
</p>

<form 
    class="form-horizontal" 
    action="<wp:action path="/ExtStr2/do/jpfastcontentedit/Content/save.action" />" method="post">

    <s:if test="hasFieldErrors()">
        <div class="alert alert-block">
            <p><strong><wp:i18n key="jpfastcontentedit_ERRORS" /></strong></p>
            <ul class="unstyled">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escape="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>

    <p class="noscreen hide">
        <wpsf:hidden name="contentOnSessionMarker" />
    </p>


    <ul class="nav nav-tabs">
        <li class="active"><a href="#jpfastcontentedit-info" data-toggle="tab">Info</a></li>
            <s:iterator value="langs" var="lang" status="langStatus">
            <li><a href="#<s:property value="#lang.code" />_tab" data-toggle="tab"><s:property value="#lang.descr" /></a></li>
            </s:iterator>
    </ul>

    <div class="tab-content">
        <%-- Info Tab --%>
        <div class="tab-pane form-horizontal active" id="jpfastcontentedit-info">
            <div class="control-group">
                <label class="control-label" for="mainGroup"><s:text name="label.ownerGroup" /></label>
                <div class="controls">
                    <s:set name="lockGroupSelect" value="%{content.id != null || content.mainGroup != null}" />
                    <wpsf:select 
                        useTabindexAutoIncrement="true" 
                        name="mainGroup" 
                        id="mainGroup" 
                        list="allowedGroups" 
                        value="content.mainGroup" 
                        listKey="name" 
                        listValue="descr" 
                        disabled="%{lockGroupSelect}" 
                        />
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="extraGroups"><s:text name="label.join" />&#32;<s:text name="label.extraGroups" /></label>
                <div class="controls">


                    <wpsf:select 
                        useTabindexAutoIncrement="true" 
                        name="extraGroupName" 
                        id="extraGroups" 
                        list="groups" 
                        listKey="name" 
                        listValue="descr" />
                    <wpsf:submit 
                        useTabindexAutoIncrement="true" 
                        action="joinGroup" 
                        value="%{getText('label.join')}" 
                        cssClass="btn" />
                </div>
            </div>
            <s:if test="content.groups.size != 0">
                <div class="well well-small">
                    <ul class="inline">
                        <s:iterator value="content.groups" var="groupName">
                            <li>
                                <wpsa:actionParam action="removeGroup" var="actionName" >
                                    <wpsa:actionSubParam name="extraGroupName" value="%{#groupName}" />
                                </wpsa:actionParam>
                                <s:set var="labelRemove" value="%{getText('label.remove')}"/>
                                <span class="label label-info">
                                    <i class="icon-tags icon-white"></i>&nbsp;&nbsp;
                                    <s:property value="%{getGroupsMap()[#groupName].getDescr()}" />&#32;
                                    <%-- chiedere ad andrea se può rifatorizzare --%>
                                    <wpsf:submit action="%{#actionName}" type="button" theme="simple" cssClass="btn btn-link btn-small" title="%{#labelRemove}" >
                                        <span class="<s:property value="%{#removeIcon}"/>"></span>
                                    </wpsf:submit>
                                    <%-- value="%{#labelRemove}"  --%>
                                </span>                                                            
                            </li>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>

            <div class="control-group">
                <label class="control-label" for="categories"><s:text name="label.join" />&#32;<s:text name="label.category" /></label>
                <div class="controls">
                    <s:set var="contentCategories" value="content.categories" />

                    <wp:categories var="categoriesVar" titleStyle="full" />
                    <wpsf:select list="%{#attr.categoriesVar}" name="categoryCode" listKey="key" listValue="value" />
                    <wpsf:submit 
                        useTabindexAutoIncrement="true" 
                        action="joinCategory" 
                        value="%{getText('label.join')}" 
                        cssClass="btn" />
                </div>
            </div>

            <s:if test="#contentCategories != null && #contentCategories.size() > 0">
                <div class="well well-small">
                    <ul class="inline">
                        <s:iterator value="#contentCategories" var="contentCategory">
                            <li>
                                <wpsa:actionParam action="removeCategory" var="actionName" >
                                    <wpsa:actionSubParam name="categoryCode" value="%{#contentCategory.code}" />
                                </wpsa:actionParam>
                                <s:set var="labelRemove" value="%{getText('label.remove')}"/>
                                <span class="label label-info">
                                    <i class="icon-tags icon-white"></i>&nbsp;&nbsp;
                                    <s:property value="#contentCategory.getFullTitle(currentLang.code)"/>&#32;
                                    <%-- chiedere ad andrea se può rifatorizzare --%>
                                    <wpsf:submit action="%{#actionName}" type="button" theme="simple" cssClass="btn btn-link btn-small" title="%{#labelRemove}" >
                                        <span class="<s:property value="%{#removeIcon}"/>"></span>
                                    </wpsf:submit>
                                    <%-- value="%{#labelRemove}"  --%>
                                </span>                                    
                            </li>
                        </s:iterator>
                    </ul>
                </div>
            </s:if>
        </div>
        <%-- Lang Tabs --%>

        <%-- Langs Iterator --%>
        <s:set name="contentType" value="content.typeCode" />
        <s:set name="lang" value="defaultLang" />
        <s:iterator value="langs" var="lang">
            <div class="tab-pane form-horizontal" id="<s:property value="#lang.code" />_tab">
                <h2 class="noscreen"><wp:i18n key="jpfastcontentedit_INTRO_${lang.code}"/></h2>

                <s:iterator value="content.attributeList" var="attribute">
                    <s:if test="#attribute.active">
                        <s:set var="attributeName" value="#attribute.name" />
                        <wpsa:tracerFactory var="attributeTracer" lang="%{#lang.code}" />

                        <%-- labels --%>
                        <s:if test="#attribute.type == 'List' || #attribute.type == 'Monolist'">
                            <div class="well well-small attribute-type-<s:property value="#attribute.type" />">
                                <fieldset>
                                    <legend>
                                        <wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/>
                                        <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo.jsp" />
                                    </legend>			
                                </s:if>
                                <s:elseif test="#attribute.type == 'Composite'">
                                    <div class="well well-small attribute-type-<s:property value="#attribute.type" />">
                                        <fieldset>
                                            <legend>
                                                <wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/>
                                                <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo.jsp" />
                                            </legend>
                                        </s:elseif>

                                        <s:elseif test="#attribute.type == 'Monotext' || #attribute.type == 'Text' || #attribute.type == 'Longtext' || #attribute.type == 'Hypertext' || #attribute.type == 'Attach' || #attribute.type == 'Number' || #attribute.type == 'Date' || #attribute.type == 'Link' || #attribute.type == 'Enumerator' || #attribute.type == 'EnumeratorMap' || #attribute.type == 'Image' || #attribute.type == 'CheckBox' || #attribute.type == 'Boolean' || #attribute.type == 'ThreeState'">
                                            <div class="control-group attribute-type-<s:property value="#attribute.type" />">
                                                <label class="control-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
                                                    <wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/>
                                                    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo.jsp" />
                                                </label>
                                                <div class="controls">
                                                </s:elseif>

                                                <s:else>
                                                    <div class="control-group attribute-type-<s:property value="#attribute.type" />">
                                                        <label class="control-label" for="<s:property value="%{#attributeTracer.getFormFieldName(#attribute)}" />">
                                                            <wp:i18n key="jpfastcontentedit_${contentType}_${attributeName}"/>
                                                            <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo.jsp" />
                                                        </label>
                                                        <div class="controls">
                                                        </s:else>


                                                        <%-- inputs --%>
                                                        <s:if test="#attribute.type == 'Monotext'">
                                                            <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
                                                            <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
                                                        </div>
                                                    </div>
                                                </s:if>
                                                <s:elseif test="#attribute.type == 'Text'">
                                                    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/textAttribute.jsp" />
                                                    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
                                                </div>
                                            </div>
                                        </s:elseif>
                                        <s:elseif test="#attribute.type == 'Longtext'">
                                            <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/longtextAttribute.jsp" />
                                            <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
                                    </div>
                            </div>
                        </s:elseif>
                        <s:elseif test="#attribute.type == 'Hypertext'">
                            <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/hypertextAttribute.jsp" />
                            <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
                        </div>
                    </div>
                </s:elseif>
                <s:elseif test="#attribute.type == 'Image'">
                    <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/imageAttribute.jsp" />
                    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
                </div>
            </div>
        </s:elseif>
        <s:elseif test="#attribute.type == 'Attach'">
            <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/attachAttribute.jsp" />
            <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
        </div>
    </div>
</s:elseif>
<s:elseif test="#attribute.type == 'CheckBox'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/checkBoxAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Boolean'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/booleanAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'ThreeState'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/threeStateAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Number'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/numberAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Date'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/dateAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Link'">
    <%-- <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/content/modules/linkAttribute.jsp" />--%>
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/linkAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Enumerator'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'EnumeratorMap'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/enumeratorMapAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Monolist'">
    <%-- <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/entity/modules/monolistAttribute.jsp" /> --%>
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/monolistAttribute.jsp" />
</fieldset>
</div> 
</s:elseif>
<s:elseif test="#attribute.type == 'List'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/listAttribute.jsp" />
</fieldset>
</div>
</s:elseif>
<s:elseif test="#attribute.type == 'Composite'">
    <s:include value="/WEB-INF/plugins/jacms/apsadmin/jsp/entity/modules/compositeAttribute.jsp" />
</fieldset>
</div> 
</s:elseif>
<s:elseif test="#attribute.type == 'Timestamp'">
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/timestampAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:elseif>
<s:else>
    <s:include value="/WEB-INF/apsadmin/jsp/entity/modules/monotextAttribute.jsp" />
    <s:include value="/WEB-INF/plugins/jpfastcontentedit/aps/jsp/internalservlet/content/modules/include/front_attributeInfo-help-block.jsp" />
</div>
</div>
</s:else>
</s:if>
</s:iterator>
</div>
</s:iterator>

</div>
<p class="form-actions">
    <wp:i18n key="jpfastcontentedit_SAVE" var="jpfastcontentedit_SAVE" />
    <wpsf:submit 
        useTabindexAutoIncrement="true" 
        action="save" 
        value="%{#attr.jpfastcontentedit_SAVE}" 
        title="%{#save_label}" 
        cssClass="btn btn-primary"/>
</p>
</form>