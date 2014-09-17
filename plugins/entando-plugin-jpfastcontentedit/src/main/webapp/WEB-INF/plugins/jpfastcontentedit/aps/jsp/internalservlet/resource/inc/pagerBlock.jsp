<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:if test="#group.size > #group.max">
	<s:if test="%{1 == #group.currItem}">
		<s:set id="goFirst" name="goFirst"><wp:resourceURL/>administration/common/img/icons/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goFirst" name="goFirst"><wp:resourceURL/>administration/common/img/icons/go-first.png</s:set>
	</s:else>

	<s:if test="%{1 == #group.beginItemAnchor}">
		<s:set id="jumpBackward" name="jumpBackward"><wp:resourceURL/>administration/common/img/icons/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="jumpBackward" name="jumpBackward"><wp:resourceURL/>administration/common/img/icons/go-jump-backward.png</s:set>
	</s:else>

	<s:if test="%{1 == #group.currItem}">
		<s:set id="goPrevious" name="goPrevious"><wp:resourceURL/>administration/common/img/icons/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goPrevious" name="goPrevious"><wp:resourceURL/>administration/common/img/icons/previous.png</s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set id="goNext" name="goNext"><wp:resourceURL/>administration/common/img/icons/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goNext" name="goNext"><wp:resourceURL/>administration/common/img/icons/next.png</s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.endItemAnchor}">
		<s:set id="jumpForward" name="jumpForward"><wp:resourceURL/>administration/common/img/icons/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="jumpForward" name="jumpForward"><wp:resourceURL/>administration/common/img/icons/go-jump-forward.png</s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set id="goLast" name="goLast"><wp:resourceURL/>administration/common/img/icons/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goLast" name="goLast"><wp:resourceURL/>administration/common/img/icons/go-last.png</s:set>
	</s:else>

	<s:if test="null != #group.pagerId"><s:set var="pagerIdMarker" value="#group.pagerId" /></s:if>
	<s:else><s:set var="pagerIdMarker">pagerItem</s:set></s:else>

<div class="pagination pagination-centered">
	<ul>
		<s:if test="#group.advanced">
			<s:if test="%{1 != #group.currItem}">
				<li>
					<wpsf:submit 
						cssClass="btn"
						useTabindexAutoIncrement="true" 
						type="image" 
						name="%{#pagerIdMarker + '_1'}" 
						value="%{getText('label.goToFirst')}" 
						title="%{getText('label.goToFirst')}" 
						src="%{#goFirst}" 
						disabled="%{1 == #group.currItem}" 
						/></li>
				<li>
					<wpsf:submit 
						cssClass="btn"
						useTabindexAutoIncrement="true" 
						type="image" 
						name="%{#pagerIdMarker + '_' + (#group.currItem - #group.offset) }" 
						value="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}" 
						title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}" 
						src="%{#jumpBackward}" disabled="%{1 == #group.beginItemAnchor}" 
						/></li>
				<li>
					<wpsf:submit 
						cssClass="btn"
						useTabindexAutoIncrement="true" 
						type="image" 
						name="%{#pagerIdMarker + '_' + #group.prevItem}" 
						value="%{getText('label.prev')}" 
						title="%{getText('label.prev.full')}" 
						src="%{#goPrevious}" 
						disabled="%{1 == #group.currItem}" 
						/></li>
			</s:if>
		</s:if>	
		<s:subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
			<s:iterator id="item">
				<li>
					<wpsf:submit 
						cssClass="btn %{#item == #group.currItem? 'disabled' : ''}" 
						useTabindexAutoIncrement="true" 
						name="%{#pagerIdMarker + '_' + #item}" 
						value="%{#item}" 
						disabled="%{#item == #group.currItem}" 
						/></li>
			</s:iterator>
		</s:subset>
		
		<s:if test="%{#group.maxItem != #group.currItem}">
			<li>
				<wpsf:submit 
					cssClass="btn"
					useTabindexAutoIncrement="true" 
					type="image" name="%{#pagerIdMarker + '_' + #group.nextItem}" 
					value="%{getText('label.next')}" 
					title="%{getText('label.next.full')}" 
					src="%{#goNext}" 
					disabled="%{#group.maxItem == #group.currItem}" 
					/></li>
			<s:if test="#group.advanced">
				<s:set var="jumpForwardStep" value="#group.currItem + #group.offset" />
				<li>
					<wpsf:submit 
						cssClass="btn"
						useTabindexAutoIncrement="true" 
						type="image" 
						name="%{#pagerIdMarker + '_' + (#jumpForwardStep)}" 
						value="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}" 
						title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}" 
						src="%{#jumpForward}" 
						disabled="%{#group.maxItem == #group.endItemAnchor}" 
						/></li>
				<li>
					<wpsf:submit 
						cssClass="btn"
						useTabindexAutoIncrement="true" 
						type="image" 
						name="%{#pagerIdMarker + '_' + #group.size}" 
						value="%{getText('label.goToLast')}" 
						title="%{getText('label.goToLast')}" 
						src="%{#goLast}" 
						disabled="%{#group.maxItem == #group.currItem}" 
						/></li>
			</s:if>
		</s:if>
		<s:set var="pagerIdMarker" value="null" />
	</ul>
</div>

</s:if>