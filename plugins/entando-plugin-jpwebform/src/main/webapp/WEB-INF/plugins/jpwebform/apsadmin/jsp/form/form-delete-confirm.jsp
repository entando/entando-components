<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

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
        <a href="<s:url action="initViewEntityTypes" namespace="/do/Entity">
               <s:param name="entityManagerName">
                   <s:property value="entityManagerName" />
               </s:param>
           </s:url>" 
           title="<s:text name="note.goToSomewhere" />:&#32;<s:text name="title.entityAdmin.manager" />&#32;<s:property value="entityManagerName" />">
            <s:text name="title.entityAdmin.manager" />:&#32;<s:property value="entityManagerName" />
        </a>&#32;/&#32;
        <s:text name="title.entityTypes.editType.delete" />:&#32;
        <s:property value="entityTypeCode" />&#32;-&#32;
        <s:property value="%{getEntityPrototype(entityTypeCode).typeDescr}" />
    </span>
</h1>

<s:form namespace="/do/jpwebform/Form" action="removeFormWorkVersion">
    <s:hidden name="_csrf" value="%{csrfToken}"/>
    <p class="noscreen">
        <wpsf:hidden name="entityManagerName" />
        <wpsf:hidden name="entityTypeCode" />
    </p>
    <div class="alert alert-warning">

        <p>
            <s:text name="note.entityTypes.deleteType.areYouSure" />:&#32;
            <em class="important">
                <span class="monospace">
                    <code><s:property value="entityTypeCode" />&#32;-&#32;<s:property value="%{getEntityPrototype(entityTypeCode).typeDescr}" /></code>
                </span>
            </em>&#32;?
        </p>

        <div class="text-center margin-large-top">
            <s:submit type="button" cssClass="btn btn-warning btn-lg">
                <span class="icon fa fa-times-circle"></span>&#32;
                <s:text name="%{getText('label.remove')}" />
            </s:submit>
            <p class="text-center margin-small-top">

                <a class="btn btn-link" 
                   href="<s:url action="initViewEntityTypes" namespace="/do/Entity">
                       <s:param name="entityManagerName">
                           <s:property value="entityManagerName" />
                       </s:param>
                   </s:url>">
                    <s:text name="note.backToSomewhere" />:&#32;
                    <s:text name="title.entityAdmin.manager" />&#32;
                    <s:property value="entityManagerName" />
                </a>
            </p>
        </div>
    </div>
</s:form>