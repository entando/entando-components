<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<%@ taglib prefix="wpsf" uri="/apsadmin-form" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ol class="breadcrumb page-tabs-header breadcrumb-position">
    <li><s:text name="breadcrumb.integrations" /></li>
    <li><s:text name="breadcrumb.integrations.components" /></li>
    <li><s:text name="jpkiebpm.admin.menu.config" /></li>
    <li class="page-title-container">List</li>
</ol>
<div class="page-tabs-header">
    <div class="row">
        <div class="col-sm-6">
            <h1 class="page-title-container">
                <s:text name="jpkiebpm.admin.menu.config" />
                <span class="pull-right">
                    <a tabindex="0" role="button" data-toggle="popover" data-trigger="focus" data-html="true" title="" data-content="<s:text name="title.kiebpms.help" />" data-placement="left" data-original-title="">
                        <i class="fa fa-question-circle-o" aria-hidden="true"></i>
                    </a>
                </span>
            </h1>
        </div>
        <div class="col-sm-6">
            <ul class="nav nav-tabs nav-justified nav-tabs-pattern" id="frag-tab">
                <li class="active">
                    <a href="<s:url action="list" namespace="/do/jpkiebpm/Config" />" role="tab"> 
                        list
                    </a>
                </li>
                <li>
                    <a href="<s:url action="edit" namespace="/do/jpkiebpm/Config" />" role="tab"> 
                        <s:text name="label.kie.settings" />
                    </a>
                </li>
                <li>
                    <a href="<s:url action="list" namespace="/do/jpkiebpm/form/override"/>" role="tab"> 
                        <s:text name="label.kie.list" />
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>
<br/>

<!-- Tab panes -->
<div class="tab-content margin-large-bottom">
    <div class="tab-pane active" id="frag-list">

        <div class="col-xs-12 mb-20">

            <div class="table-responsive overflow-visible">
                <table id="sort" class="grid table table-bordered table-hover">
                    <thead>
                        <tr>
                            <th class="text-center table-w-5">active</th>
                            <th class="text-center table-w-5">hostname</th>
                            <th class="text-center table-w-5">schema</th>
                            <th class="text-center table-w-5">port</th>
                            <th class="text-center table-w-5">webapp</th>
                            <th class="text-center table-w-5">debug</th>
                        </tr>
                    </thead>
                    <tbody>
                        
                        <s:iterator value="knowledgeSource">
                            <tr>
                                <td class="index text-center">${value.active}</td>
                                <td class="index text-center">${value.hostname}</td>
                                <td class="index text-center">${value.schema}</td>
                                <td class="index text-center">${value.port}</td>
                                <td class="index text-center">${value.webapp}</td>
                                <td class="index text-center">${value.debug}</td>
                            </tr>
                        </s:iterator>

                    </tbody>
                </table>

                <style>
                    .ui-sortable-helper {
                        display: table;
                    }
                </style>
            </div>


        </div>
    </div>
    <div class="col-xs-12">
    </div>
</div>
<div class="tab-pane" id="frag-settings">
</div>
</div>
