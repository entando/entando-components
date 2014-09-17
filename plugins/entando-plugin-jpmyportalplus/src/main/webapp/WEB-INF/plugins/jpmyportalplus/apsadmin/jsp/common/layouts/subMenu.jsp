<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<wp:ifauthorized permission="superuser">
	<li class="margin-large-bottom"><span class="h5"><s:text name="jpmyportalplus.menu.config" /></span>
		<ul class="nav nav-pills nav-stacked">
	    	<li>
				<a href="<s:url action="edit" namespace="/do/jpmyportalplus/Config" />" >
					<s:text name="jpmyportalplus.shortCut.config.shortDesc" />
				</a>
			</li>
    	</ul>
	</li>
</wp:ifauthorized>