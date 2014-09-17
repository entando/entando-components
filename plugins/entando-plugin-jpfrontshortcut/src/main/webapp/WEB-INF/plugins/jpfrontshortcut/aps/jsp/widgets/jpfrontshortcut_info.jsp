<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h2>How to Use the Front Shortcut component</h2>
<ol>
	<li>
		<p><a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/login">Login as an administrator</a></p>
	</li>
	<li>
		<p>
			Activate the plugin from the <a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/BaseAdmin/configSystemParams.action">administration area menu Settings</a>
		</p>
	</li>
	<li>
		<p><a href="<wp:url paramRepeat="false" />">Come back here</a> logged in (as administrator or any other user with permission "Operations on Pages" and corresponding group as well) and notice new links and icons appear.</p>
	</li>
</ol>
<p>
	If activated, for each frame in the pagemodel you will find an edit frame icon (
	<img src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/edit.png" alt="Edit" /> )
	and the clear frame (<img src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/clear.png" alt="Remove Widget from frame" />)
</p>
<p>
	Also notice the icons near the menu for create (<img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/new.png" alt="New page from <c:out value="${currentTarget.title}" />" />), edit (<img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/edit.png" alt="Edit page <c:out value="${currentTarget.title}" />" />) and delete pages (<img width="12" height="12" src="<wp:resourceURL />plugins/jpfrontshortcut/static/img/icons/16x16/delete.png" alt="Delete page <c:out value="${currentTarget.title}" />" />)
</p>

<hr />

<h2>Contents Front Shortcut</h2>
<p>
	Publish a content in this page and start the front-end editing
</p>
<ol>
	<li><p><a href="<wp:info key="systemParam" paramName="applicationBaseURL" />do/Page/viewTree.action?selectedNode=<wp:currentPage param="code" />&amp;action%3Aconfigure=Configure">Publish a content in this page from the Page Tree</a></p></li>
	<li><p>Use the widget <em>Front Shortcut Contents - Publish Content</em> to place content in a position</p></li>
	<li>
		<p><a href="<wp:url paramRepeat="false" />">Come back here</a> logged in (as administrator or as any other user with permission "Content Editing" and corresponding group as well) and notice the new link near the content appeared.</p>
	</li>
</ol>

<hr />

<h2>How to use these in my portal?</h2>
<ul>
	<li><p>Reproduce the pagemodel configuration: notice how <em>/WEB-INF/plugins/jpfrontshortcut/aps/jsp/models/jpfrontshortcut_test.jsp</em> pagemodel works. You need to port the jQuery headinfo and the widget popup init. </p></li>
	<li><p>If you want to trigger the frontshort editing from the Content Models add this in you model:</p>
		<p>
			<pre style="overflow: scroll; border: 1px solid silver; width: 90%; padding: 0.5em 4%; background-color: #fafafa">
#if ($content.isUserAllowed("editContents"))
  #set ($frontEndEditingParam = $info.getConfigParameter("jpfrontshortcut_activeContentFrontEndEditing"))
   #if ($frontEndEditingParam &amp;&amp; $frontEndEditingParam == "true") 
    &lt;a id="options_anchor_$content.getId()" href="javascript:void(0)"&gt;
        $i18n.getLabel("EDIT_THIS_CONTENT") - Front Shortcut
    &lt;/a&gt;
    &lt;script type='text/javascript'&gt;
        &lt;!--//--&gt;&lt;![CDATA[//&gt;&lt;!--
        jQuery(document).ready(function () { 
            jQuery.struts2_jquery.bind(jQuery('#options_anchor_$content.getId()'),{
                "opendialog": "widgetDialog",
                "jqueryaction": "anchor",
                "id": "anchor_config_$content.getId()",
				"href": "$content.getConfigParameter("applicationBaseURL")do/jpfrontshortcut/Content/introView?modelId=&lt;INSERT HERE THE ID OF THE MODEL&gt;&amp;contentId=$content.getId()&amp;request_locale=$content.getLangCode()&amp;langCode=$content.getLangCode()",
                "button": false
            });
        });
        //--&gt;&lt;!]]&gt;
    &lt;/script&gt;
  #end
#end
			</pre>
		</p>
	</li>
	<li>
		<p>Remember to insert the correct id of the model in the <code>href</code></p>
	</li>
	<li>
		<p>Important: this component at the moment will work only in pages served with MimeType <strong>text/html</strong></p>
	</li>
</ul>