<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpmpp" uri="/jpmyportalplus-apsadmin-core" %>

<wpmpp:widgetType key="%{#showletType.key}" property="swappable" var="swappableVar" />
<td class=text-center>
	<s:if test="#swappableVar > -1">
		<s:if test="#swappableVar == 1">
            <button 
                class="btn btn-sm btn-link js_joinCategory"
				alt="<s:text name="jpmyportalplus.widget.swappable" />"
				title="<s:property value="#showletType.value" />: <s:text name="jpmyportalplus.widget.swappable" />">
                <span class="icon fa fa-check fa-lg text-success"></span>
            </button>    
		</s:if>
		<s:else>
            <button 
                class="btn btn-sm btn-link js_joinCategory"
                alt="<s:text name="jpmyportalplus.widget.not.swappable" />" 
                title="<s:property value="#showletType.value" />: <s:text name="jpmyportalplus.widget.not.swappable" />">
                <span class="icon fa fa-exclamation fa-lg text-warning"></span>
            </button>    
		</s:else>
	</s:if>
	<s:else>
		<button class="btn btn-sm btn-link js_joinCategory">
			<span class="icon fa fa-minus fa-lg"></span>
		</button>    
	</s:else>
	<s:set var="swappableVar" value="null" />
</td>