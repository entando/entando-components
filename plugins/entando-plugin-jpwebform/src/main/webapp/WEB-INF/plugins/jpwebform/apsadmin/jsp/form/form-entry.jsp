<%@page contentType="charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="viewManagers" namespace="/do/Entity" />" 
           title="<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.entityManagement" />">
            <s:text name="title.entityManagement" />
        </a>&#32;/&#32;
        <a href="<s:url action="initViewEntityTypes" 
               namespace="/do/Entity">
               <s:param name="entityManagerName">
                   <s:property value="entityManagerName" />
               </s:param>
           </s:url>" 
           title="<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.entityAdmin.manager" />&#32;
           <s:property value="entityManagerName" />">
            <s:text name="title.entityAdmin.manager" />:&#32;<s:property value="entityManagerName" />
        </a>&#32;/&#32;
        <s:if test="operationId == 1">
            <s:text name="title.entityTypes.editType.new" />
        </s:if>
        <s:else>
            <s:text name="title.entityTypes.editType.edit" />
        </s:else>
    </span>
</h1>

<div id="main">

    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
                <ul class="margin-base-vertical">
                    <s:iterator value="fieldErrors">
                        <s:iterator value="value">
                            <li><s:property escape="false" /></li>
                            </s:iterator>
                        </s:iterator>
                </ul>
        </div>
    </s:if>

    <div class="panel panel-default">
        <div class="panel-body">
            <p><s:text name="note.entityTypes.editType.intro.1" />.</p>
            <p><s:text name="note.entityTypes.editType.intro.2" /></p>
        </div>
    </div>    

    <s:form action="saveEntityType">

        <s:set var="entityType" value="entityType" />
        <s:if test="operationId != 1">
            <p class="noscreen">	
                <wpsf:hidden name="entityTypeCode" value="%{#entityType.typeCode}" />
            </p>
        </s:if>

        <fieldset class="col-xs-12">
            <legend><s:text name="label.info" /></legend>
            <div class="form-group">
                <s:if test="operationId == 1">
                    <label for="entityTypeCode"><s:text name="label.code" /></label>
                    <wpsf:textfield name="entityTypeCode" id="entityTypeCode" value="%{#entityType.typeCode}" cssClass="form-control" />
                </s:if>
                <s:else>
                    <label for="entityTypeCode"><s:text name="label.code" /></label>
                    <wpsf:textfield name="entityTypeCode" id="entityTypeCode" value="%{#entityType.typeCode}" cssClass="form-control" disabled="true" />	
                </s:else>
            </div>

            <div class="form-group">
                <label for="entityTypeDescription"><s:text name="label.description" /></label>
                <wpsf:textfield name="entityTypeDescription" id="entityTypeDescription" value="%{#entityType.typeDescr}" cssClass="form-control" />
            </div>

            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox name="repeatable" id="repeatable" value="%{isFormRepeatable()}" cssClass="radiocheck" />
                    <label for="repeatable"><s:text name="jpwebform.label.repeatable" /></label>
                </div>
            </div>

            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox name="ignoreVersionEdit" id="ignoreVersionEdit" value="%{isFormIgnoreEdit()}" cssClass="radiocheck" />
                    <label for="ignoreVersionEdit"><s:text name="jpwebform.label.ignoreVersionEdit" /></label>
                </div>
            </div>
            
            <div class="form-group">
                <div class="checkbox">
                    <wpsf:checkbox name="ignoreVersionDisplay" id="ignoreVersionDisplay" value="%{isFormIgnoreDisplay()}" cssClass="radiocheck" />
                    <label for="ignoreVersionDisplay"><s:text name="jpwebform.label.ignoreVersionDisplay" /></label>
                </div>
            </div>
        </fieldset>

        <%-- 
                hookpoint for meta-info and the like
                allowed Plugins: jacms (but so far we have not a check on this)
                
                Based on the Plugin Pattern, we can calculate a proper path for this inclusion
                /WEB-INF/plugins/<plugin_code>/apsadmin/jsp/entity/include/entity-type-entry.jsp 
        --%>
        <s:if test="null != #hookpoint_plugin_code">
            <s:include value="%{'/WEB-INF/plugins/' + #hookpoint_plugin_code + '/apsadmin/jsp/entity/include/entity-type-entry.jsp'}" />
        </s:if>

        <fieldset class="col-xs-12">
            <legend><s:text name="label.attributes" /></legend>
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/attribute-operations-add.jsp" />
            <s:include value="/WEB-INF/apsadmin/jsp/entity/include/attribute-list.jsp" />
        </fieldset>

        <div class="form-horizontal">
            <div class="form-group">
                <div class="col-xs-12 col-sm-4 col-md-3 margin-small-vertical">
                    <s:submit action="saveEntityType" type="button" cssClass="btn btn-primary btn-block">
                        <span class="icon fa fa-floppy-o"></span>&#32;
                        <s:text name="label.save" />
                    </s:submit>
                </div>
            </div>
        </div>
    </s:form>
</div>