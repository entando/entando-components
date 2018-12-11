<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>

<div class="form-group">
    <label for="Knowledge Source"><s:text name="Knowledge Source"/></label>
    <div class="input-group">
        <s:select 
            disabled="#isknowledgeSourcePathSetted"
            list="knowledgeSource" 
            id="knowledgeSourcePath" 
            name="knowledgeSourcePath"
            listKey="value.id"
            listValue="value.active ? value.name : (value.name + ' (' + getText('label.disabled') + ')')" class="form-control">
        </s:select>
        <s:if test="#isknowledgeSourcePathSetted">
            <s:hidden name="knowledgeSourcePath" />
        </s:if>
        <span class="input-group-btn">
            <s:if test="#isknowledgeSourcePathSetted">
                <wpsf:submit 
                    action="changeKnowledgeSourceForm" 
                    value="%{getText('label.changeKnowledgeSource')}"
                    cssClass="btn btn-warning"
                    />
            </s:if>
            <s:else>
                <wpsf:submit 
                    action="chooseKnowledgeSourceForm" 
                    value="%{getText('label.chooseKnowledgeSource')}"
                    cssClass="btn btn-success"
                    />
            </s:else>
        </span>
    </div>
</div>