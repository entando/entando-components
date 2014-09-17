<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="/apsadmin-form" prefix="wpsf" %>
<%@ taglib uri="/aps-core" prefix="wp" %>

<s:if test="#group.size > #group.max">
	<s:if test="%{1 == #group.currItem}">
		<s:set id="goFirst" name="goFirst"><wp:resourceURL/>plugins/jpwebmail/static/img/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goFirst" name="goFirst"><wp:resourceURL/>plugins/jpwebmail/static/img/go-first.png</s:set>
	</s:else>

	<s:if test="%{1 == #group.beginItemAnchor}">
		<s:set id="jumpBackward" name="jumpBackward"><wp:resourceURL/>plugins/jpwebmail/static/img/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="jumpBackward" name="jumpBackward"><wp:resourceURL/>plugins/jpwebmail/static/img/go-jump-backward.png</s:set>
	</s:else>

	<s:if test="%{1 == #group.currItem}">
		<s:set id="goPrevious" name="goPrevious"><wp:resourceURL/>plugins/jpwebmail/static/img/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goPrevious" name="goPrevious"><wp:resourceURL/>plugins/jpwebmail/static/img/go-previous.png</s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set id="goNext" name="goNext"><wp:resourceURL/>plugins/jpwebmail/static/img/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goNext" name="goNext"><wp:resourceURL/>plugins/jpwebmail/static/img/go-next.png</s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.endItemAnchor}">
		<s:set id="jumpForward" name="jumpForward"><wp:resourceURL/>plugins/jpwebmail/static/img/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="jumpForward" name="jumpForward"><wp:resourceURL/>plugins/jpwebmail/static/img/go-jump-forward.png</s:set>
	</s:else>

	<s:if test="%{#group.maxItem == #group.currItem}">
		<s:set id="goLast" name="goLast"><wp:resourceURL/>plugins/jpwebmail/static/img/transparent.png</s:set>
	</s:if>
	<s:else>
		<s:set id="goLast" name="goLast"><wp:resourceURL/>plugins/jpwebmail/static/img/go-last.png</s:set>
	</s:else>
 	
<p>
	<s:if test="#group.advanced"><wpsf:submit useTabindexAutoIncrement="true" type="image" name="pagerItem_1" value="%{getText('label.goToFirst')}" title="%{getText('label.goToFirst')}" src="%{#goFirst}" disabled="%{1 == #group.currItem}" /><wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{'pagerItem_' + (#group.currItem - #group.offset) }" value="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}" title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.backward')}" src="%{#jumpBackward}" disabled="%{1 == #group.beginItemAnchor}" /></s:if>	
	<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{'pagerItem_' + #group.prevItem}" value="%{getText('label.prev')}" title="%{getText('label.prev.full')}" src="%{#goPrevious}" disabled="%{1 == #group.currItem}" />	
	<s:subset source="#group.items" count="#group.endItemAnchor-#group.beginItemAnchor+1" start="#group.beginItemAnchor-1">
		<s:iterator id="item"><wpsf:submit useTabindexAutoIncrement="true" name="%{'pagerItem_' + #item}" value="%{#item}" disabled="%{#item == #group.currItem}" cssClass="paddingLateral05" /></s:iterator>
	</s:subset>
	<wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{'pagerItem_' + #group.nextItem}" value="%{getText('label.next')}" title="%{getText('label.next.full')}" src="%{#goNext}" disabled="%{#group.maxItem == #group.currItem}" />
	<s:if test="#group.advanced"><s:set name="jumpForwardStep" value="#group.currItem + #group.offset"></s:set><wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{'pagerItem_' + (#jumpForwardStep)}" value="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}" title="%{getText('label.jump') + ' ' + #group.offset + ' ' + getText('label.forward')}" src="%{#jumpForward}" disabled="%{#group.maxItem == #group.endItemAnchor}" /><wpsf:submit useTabindexAutoIncrement="true" type="image" name="%{'pagerItem_' + #group.size}" value="%{getText('label.goToLast')}" title="%{getText('label.goToLast')}" src="%{#goLast}" disabled="%{#group.maxItem == #group.currItem}" /></s:if>
</p>

</s:if>