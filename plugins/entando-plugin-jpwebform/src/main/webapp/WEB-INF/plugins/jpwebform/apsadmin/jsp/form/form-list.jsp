<%@ taglib uri="/struts-tags" prefix="s" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<h1 class="panel panel-default title-page">
    <span class="panel-body display-block">
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity">
               <s:param name="entityManagerName">
                   <s:property value="entityManagerName" />
               </s:param>
           </s:url>" 
           title="<s:text name="note.goToSomewhere" />: <s:text name="title.entityManagement" />">
            <s:text name="title.entityAdmin.manage" />
        </a>&#32;/&#32;
        <s:text name="label.configure" />:&#32;<s:property value="entityManagerName" />
    </span>
</h1>


<div id="main">

    <div class="panel panel-default">
        <div class="panel-body">
            <p><s:text name="note.entityTypes.intro.1" /></p>
            <p><s:text name="note.entityTypes.intro.2" /></p>
        </div>
    </div>    

    <s:if test="hasActionErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.ActionErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="actionErrors">
                    <li><s:property escapeHtml="false" /></li>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="hasFieldErrors()">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
            <ul class="margin-base-vertical">
                <s:iterator value="fieldErrors">
                    <s:iterator value="value">
                        <li><s:property escapeHtml="false" /></li>
                        </s:iterator>
                    </s:iterator>
            </ul>
        </div>
    </s:if>
    <s:if test="%{#parameters.error != null}">
        <div class="alert alert-danger alert-dismissable">
            <button type="button" class="close" data-dismiss="alert"><span class="icon fa fa-times"></span></button>
            <h2 class="h4 margin-none"><s:text name="message.title.FieldErrors" /></h2>
            <p class="margin-base-vertical"><s:text name="%{#parameters.error}" /></p>
        </div>
    </s:if>

    <p class="margin-base-vertical">
        <a class="btn btn-default" href="<s:url namespace="/do/Entity" action="initAddEntityType" >
               <s:param name="entityManagerName">
                   <s:property value="entityManagerName" />
               </s:param>
           </s:url>">
            <span class="icon fa fa-plus-circle"></span>&#32;
            <s:text name="menu.entityAdmin.entityTypes.new" />
        </a>
    </p>

    <s:if test="%{entityPrototypes.size > 0}">

        <table class="table table-bordered">
            <tr>
                <th class="text-center"><abbr title="<s:text name="label.actions" />">&ndash;</abbr></th>
                <th class="text-center"><s:text name="label.code" /></th>
                <th><s:text name="label.description" /></th>
                <th class="text-right"><s:text name="name.version" /></th>
                    <%-- <th class="icon"><abbr title="<s:text name="label.edit.long" />"><s:text name="label.edit.short" /></abbr></th> --%>
                    <%--<th class="icon"><abbr title="<s:text name="jpwebform.steps.configure.long" />"><s:text name="jpwebform.steps.configure.short" /></abbr></th>--%>
                <th class="text-center"><s:text name="jpwebform.gui.configure.long" /></th>
                <th class="text-center"><abbr title="<s:text name="label.references.long" />"><s:text name="label.references.short" /></abbr></th> 
            </tr>

            <s:iterator value="entityPrototypes" var="entityType" status="counter">
                <s:set var="entityAnchor" value="%{'entityCounter'+#counter.count}" />
                <tr>
                    <td class="text-center">
                        <div class="btn-group btn-group-xs">
                            <a class="btn btn-default" id="<s:property value="#entityAnchor" />" href="
                               <s:url namespace="/do/Entity" action="initEditEntityType">
                                   <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                   <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                               </s:url>"
                               title="<s:text name="label.edit" />: <s:property value="#entityType.typeDescr" />">
                                <span class="icon fa fa-pencil-square-o"></span>
                            </a>

                            <a class="btn btn-default" id="<s:property value="#entityAnchor" />" href="
                               <s:url namespace="/do/jpwebform/Config/Step" action="entry">
                                   <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                               </s:url>
                               " title="<s:text name="jpwebform.steps.configure.long" />: <s:property value="#entityType.typeDescr" />">
                                <span class="icon fa fa-arrow-right"></span>
                                <span class="sr-only"><s:text name="jpwebform.steps.configure.long" />:&#32;<s:property value="#entityType.typeDescr" /></span>
                            </a>

                            <s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">
                                <a class="btn btn-default" href="
                                   <s:url namespace="/do/Entity" action="initViewEntityTypes" anchor="%{#entityAnchor}">
                                       <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                   </s:url>
                                   " title="<s:text name="label.references.status.wip" />">
                                    <span class="icon"></span>
                                    <span class="sr-only"><s:text name="label.references.status.wip" /></span>
                                </a>
                            </s:if>
                            <s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2">
                                <a class="btn btn-default" href="
                                   <s:url namespace="/do/Entity" action="reloadEntityTypeReferences" anchor="%{#entityAnchor}">
                                       <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                       <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                   </s:url>
                                   " title="<s:text name="label.references.status.ko" />">
                                    <span class="icon fa fa-refresh"></span>
                                    <span class="sr-only"><s:text name="label.references.status.ko" /></span>
                                </a>
                            </s:elseif>
                            <s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0">
                                <a class="btn btn-default" href="
                                   <s:url namespace="/do/Entity" action="reloadEntityTypeReferences" anchor="%{#entityAnchor}">
                                       <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                       <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                   </s:url>
                                   " title="<s:text name="label.references.status.ok" />">
                                    <span class="icon fa fa-refresh"></span>
                                    <span class="sr-only"><s:text name="label.references.status.ok" /></span>
                                </a>
                            </s:elseif>        
                            <s:if test="%{checkEntityGui(#entityType.typeCode)}">
                                <a class="btn btn-default" id="<s:property value="#entityAnchor" />" href="
                                   <s:url namespace="/do/jpwebform/Publish" action="entry">
                                       <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                   </s:url>
                                   " title="<s:text name="jpwebform.form.publish.long" />:&#32;<s:property value="#entityType.typeDescr" />">
                                    <span class="icon fa fa-globe"></span>
                                </a>
                            </s:if>
                            <s:else>
                                <button type="button" class="btn btn-default disabled" title="<s:property value="#entityType.typeDescr" />&#32;<s:text name="jpwebform.form.publish.cannot" />">
                                    <span class="icon fa fa-globe"></span>    
                                </button>
                            </s:else>                    
                        </div>
                        <div class="btn-group btn-group-xs">
                            <a class="btn btn-warning" href=" 
                               <s:url namespace="/do/Entity" action="trashEntityType">
                                   <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                   <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                               </s:url>
                               " title="<s:text name="label.remove" />: <s:property value="#entityType.typeDescr" />">
                                <span class="icon fa fa-times-circle-o"></span>
                                <span class="sr-only"><s:text name="label.alt.clear" /></span>
                            </a>
                        </div>
                    </td>
                    <td class="text-center"><code><s:property value="#entityType.typeCode" /></code></td>

                    <td><s:property value="#entityType.typeDescr" /></td>

                    <td class="text-right"><s:property value="#entityType.versionType" /></td>
                    <%--
                    <td><a id="<s:property value="#entityAnchor" />" href="
                                    <s:url namespace="/do/Entity" action="initEditEntityType">
                                            <s:param name="entityManagerName"><s:property value="entityManagerName" /></s:param>
                                            <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                    </s:url>
                            " title="<s:text name="label.edit" />: <s:property value="#entityType.typeDescr" />">
                            EDITA
                            </a>
                    </td>
                    --%>
                    <%--
                                    <td class="icon">
                                        <a id="<s:property value="#entityAnchor" />" href="
                                                    <s:url namespace="/do/jpwebform/Config/Step" action="entry">
                                                            <s:param name="entityTypeCode"><s:property value="#entityType.typeCode" /></s:param>
                                                    </s:url>
                                            " title="<s:text name="jpwebform.steps.configure.long" />: <s:property value="#entityType.typeDescr" />">
                                            <img src="<wp:resourceURL />administration/common/img/icons/next.png" alt="<s:text name="jpwebform.steps.configure.long" />: <s:property value="#entityType.typeDescr" />" />
                                        </a>
                                    </td>
                    --%>
                    <td class="editGui col-sm-3">
                        <s:set var="stepsConfigVar" value="%{getStepsConfigExtended(#entityType.typeCode)}" />
                        <s:form action="editGui" namespace="/do/jpwebform/Config/Gui">
                            <s:hidden name="_csrf" value="%{csrfToken}"/>
                            <wpsf:hidden name="entityTypeCode" value="%{#entityType.typeCode}"/>

                            <div class="input-group">
                                <wpsf:select 
                                    list="#stepsConfigVar.steps" 
                                    name="stepCode" 
                                    id="stepCode" 
                                    listKey="code" 
                                    listValue="%{code + ' ('+getText('jpwebform.label.built')+': ' + getText(builtGui==true ? 'label.yes' : 'label.no') + ')'}"  
                                    value="code" 
                                    cssClass="form-control"
                                    />
                                <div class="input-group-btn">
                                    <wpsf:submit cssClass="btn btn-default" value="%{getText('jpwebform.label.editgui')}" />
                                </div>
                            </div>
                        </s:form>
                        <%-- 
                        <s:iterator var="stepVar" value="#stepsConfigVar.steps">
                                <fieldset>
                                        <legend>CODE <s:property value="#stepVar.code" /></legend>
                                BuiltGUI <s:property value="#stepVar.builtGui" />
                                <br />
                                <s:iterator var="attributeNameVar" value="#stepVar.attributeOrder" >
                                        <s:set var="attributeConfigVar" value="%{#stepVar.attributeConfigs[#attributeNameVar]}" />
                                        <s:set var="attributeVar" value="%{#entityPrototypeVar.getAttribute(#attributeNameVar)}" />
                                        <s:property value="#attributeConfigVar.name" /> - <s:property value="#attributeConfigVar.view" /> -
                                        <s:if test="null == #attributeVar" >ATTRIBUTE ISN'T INTO MODEL</s:if><s:else>TYPE <s:property value="#attributeVar.type" /></s:else>
                                        <br />
                                </s:iterator>

                                <wpsa:actionParam action="editGui" var="actionName" ><wpsa:actionSubParam name="stepCode" value="%{#stepVar.code}" /></wpsa:actionParam>
                                <wpsf:submit useTabindexAutoIncrement="true" action="%{#actionName}" value="%{getText('label.editGui')}" title="%{getText('label.editGui')}" />
                                </fieldset>
                        </s:iterator>
                        --%>
                    </td>
                    <td class="text-center">
                        <s:if test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 1">
                            <span class="icon fa fa-pause" title="<s:text name="label.references.status.wip" />"></span>
                            <span class="sr-only"><s:text name="label.references.status.wip" /></span>
                        </s:if>
                        <s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 2">
                            <span class="icon fa fa-exclamation" title="<s:text name="label.references.status.ko" />"></span>
                            <span class="sr-only"><s:text name="label.references.status.ko" /></span>
                        </s:elseif>
                        <s:elseif test="getEntityManagerStatus(entityManagerName, #entityType.typeCode) == 0">
                            <span class="icon fa fa-check text-success" title="<s:text name="label.references.status.ok" />"></span>
                            <span class="sr-only"><s:text name="label.references.status.ok" /></span>
                        </s:elseif>
                    </td>
                </tr>
            </s:iterator>
        </table>

    </s:if>
    <s:else>
        <div class="alert alert-info"><s:text name="note.entityTypes.modelList.empty" /></div>
    </s:else>

    <s:include value="/WEB-INF/apsadmin/jsp/entity/include/entity-type-references-operations-reload.jsp" />
</div>
