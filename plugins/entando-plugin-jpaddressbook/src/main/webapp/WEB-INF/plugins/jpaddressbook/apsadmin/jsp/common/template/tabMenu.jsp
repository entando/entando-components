<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags"  %>

<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpaddressbook.name"/>
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title=""
                       data-content="<s:text name="jpaddressbook.menu.messages.help"/>" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern">
                <li class="active">
                    <a href="<s:url namespace="/do/jpaddressbook/AddressBook" action="list" />">
                        <s:text name="jpaddressbook.title.menuList"/>
                    </a>
                </li>
                <li>
                    <a href="<s:url namespace="/do/jpaddressbook/VCard" action="edit" />">
                        <s:text name="jpaddressbook.vcard.conf"/>
                    </a>
                </li>
            </ul>
        </div>
    </div>

</div>
<br>