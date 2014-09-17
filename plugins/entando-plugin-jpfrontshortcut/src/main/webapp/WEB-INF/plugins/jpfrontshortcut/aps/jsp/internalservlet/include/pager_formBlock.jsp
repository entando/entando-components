<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>
<%@ taglib prefix="sj" uri="/struts-jquery-tags"%>
<%@ taglib prefix="wpfssa" uri="/jpfrontshortcut-apsadmin-core" %>

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
 	
<p>
	<s:if test="null != #group.pagerId">
		<s:set var="pagerIdMarker" value="#group.pagerId" />
	</s:if>
	<s:else>
		<s:set var="pagerIdMarker">pagerItem</s:set>
	</s:else>
	<s:if test="#group.advanced">
		<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
			<wpfssa:actionSubParam name="%{#pagerIdMarker + '_1'}" value="%{getText('label.goToFirst')}" />
		</wpfssa:actionParam>
		<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
		<sj:submit targets="form-container" type="image" value="%{getText('label.goToFirst')}" title="%{getText('label.goToFirst')}" 
				   src="%{#goFirst}" button="true" href="%{#submitActionVar}" disabled="%{1 == #group.currItem}" />
		<%--
		<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{#pagerIdMarker + '_1'}" 
					 value="%{getText('label.goToFirst')}" title="%{getText('label.goToFirst')}" 
					 src="%{#goFirst}" disabled="%{1 == #group.currItem}" />
		--%>
		<s:set var="pagerIdValueVar" value="%{getText('label.jump') + '_' + #group.offset + '_' + getText('label.backward')}" />
		<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
			<wpfssa:actionSubParam name="%{#pagerIdMarker + '_' + (#group.currItem - #group.offset)}" value="%{#pagerIdValueVar}" />
		</wpfssa:actionParam>
		<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
		<sj:submit id="%{#pagerIdValueVar}" type="image" targets="form-container" title="%{#pagerIdValueVar}" value="%{#pagerIdValueVar}" 
				   button="true" href="%{#submitActionVar}" src="%{#jumpBackward}" disabled="%{1 == #group.beginItemAnchor}" />
		<%--
		<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{#pagerIdMarker + '_' + (#group.currItem - #group.offset) }" 
					 value="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}" 
					 title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}" 
					 src="%{#jumpBackward}" disabled="%{1 == #group.beginItemAnchor}" />
		--%>
	</s:if>	
	
	<s:set var="pagerIdValueVar" value="%{getText('label.prev')}" />
	<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
		<wpfssa:actionSubParam name="%{#pagerIdMarker + '_' + #group.prevItem}" value="%{#pagerIdValueVar}" />
	</wpfssa:actionParam>
	<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
	<sj:submit targets="form-container" type="image" 
			   value="%{#pagerIdValueVar}" 
			   title="%{#pagerIdValueVar}" button="true" 
			   href="%{#submitActionVar}" src="%{#goPrevious}" disabled="%{1 == #group.currItem}" />
	<%--
	<wpsf:submit useTabindexAutoIncrement="true" type="image" 
				 name="%{#pagerIdMarker + '_' + #group.prevItem}" value="%{getText('label.prev')}" 
				 title="%{getText('label.prev.full')}" src="%{#goPrevious}" disabled="%{1 == #group.currItem}" />	
	--%>
	<s:subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
		<s:iterator id="item">
			<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
				<wpfssa:actionSubParam name="%{#pagerIdMarker + '_' + #item}" value="%{#item}" />
			</wpfssa:actionParam>
			<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
			<sj:submit targets="form-container" value="%{#item}" title="%{#item}" button="true" 
					   href="%{#submitActionVar}" disabled="%{#item == #group.currItem}" />
			<%--
			<wpsf:submit useTabindexAutoIncrement="true" name="%{#pagerIdMarker + '_' + #item}" 
						 value="%{#item}" disabled="%{#item == #group.currItem}" />
			--%>
		</s:iterator>
	</s:subset>
	
	
	
	<s:set var="pagerIdValueVar" value="%{getText('label.next')}" />
	<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
		<wpfssa:actionSubParam name="%{#pagerIdMarker + '_' + #group.nextItem}" value="%{#pagerIdValueVar}" />
	</wpfssa:actionParam>
	<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
	<sj:submit targets="form-container" type="image" 
			   value="%{#pagerIdValueVar}" 
			   title="%{#pagerIdValueVar}" button="true" 
			   href="%{#submitActionVar}" src="%{#goNext}" disabled="%{#group.maxItem == #group.currItem}" />
	<%--
	<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{#pagerIdMarker + '_' + #group.nextItem}" 
				 value="%{getText('label.next')}" title="%{getText('label.next.full')}" 
				 src="%{#goNext}" disabled="%{#group.maxItem == #group.currItem}" />
	--%>
	<s:if test="#group.advanced">
		<s:set name="jumpForwardStep" value="#group.currItem + #group.offset" />
		<s:set var="pagerIdValueVar" value="%{getText('label.jump') + '_' + #group.offset + '_' + getText('label.forward')}" />
		<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
			<wpfssa:actionSubParam name="%{#pagerIdMarker + '_' + (#jumpForwardStep)}" value="%{#pagerIdValueVar}" />
		</wpfssa:actionParam>
		<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
		<sj:submit id="%{#pagerIdValueVar}" type="image" targets="form-container" title="%{#pagerIdValueVar}" value="%{#pagerIdValueVar}" 
				   button="true" href="%{#submitActionVar}" src="%{#jumpForward}" disabled="%{#group.maxItem == #group.endItemAnchor}" />
		<%--
		<s:set name="jumpForwardStep" value="#group.currItem + #group.offset" />
		<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{#pagerIdMarker + '_' + (#jumpForwardStep)}" 
					 value="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}" 
					 title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}" 
					 src="%{#jumpForward}" disabled="%{#group.maxItem == #group.endItemAnchor}" />
		--%>
		<wpfssa:actionParam action="%{#pagerSubmitActionNameVar}" var="submitActionNameVar" >
			<wpfssa:actionSubParam name="%{#pagerIdMarker + '_' + #group.size}" value="%{getText('label.goToLast')}" />
		</wpfssa:actionParam>
		<s:url var="submitActionVar" action="%{#submitActionNameVar}" />
		<sj:submit targets="form-container" type="image" value="%{getText('label.goToLast')}" title="%{getText('label.goToLast')}" 
				   src="%{#goLast}" button="true" href="%{#submitActionVar}" disabled="%{#group.maxItem == #group.currItem}" />
		<%--
		<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{#pagerIdMarker + '_' + #group.size}" 
					 value="%{getText('label.goToLast')}" title="%{getText('label.goToLast')}" src="%{#goLast}" 
					 disabled="%{#group.maxItem == #group.currItem}" />
		--%>
	</s:if>
	<s:set var="pagerIdMarker" value="null" />
</p>

</s:if>