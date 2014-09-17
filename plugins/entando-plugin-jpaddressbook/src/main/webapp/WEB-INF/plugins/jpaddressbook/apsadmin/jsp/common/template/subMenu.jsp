<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/apsadmin-core" prefix="wpsa" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<li class="margin-large-bottom">
	<span class="h5"><s:text name="jpaddressbook.title.addressbook" /></span>
		<ul class="nav nav-pills nav-stacked">
			<li><a href="<s:url namespace="/do/jpaddressbook/AddressBook" action="list" />" ><s:text name="jpaddressbook.title.menuList" /></a>
			</li>
			<wp:ifauthorized permission="superuser">
					<li><a href="<s:url namespace="/do/jpaddressbook/VCard" action="edit" />" ><s:text name="jpaddressbook.vcard.conf" /></a></li>
			</wp:ifauthorized>
		</ul>
</li>
