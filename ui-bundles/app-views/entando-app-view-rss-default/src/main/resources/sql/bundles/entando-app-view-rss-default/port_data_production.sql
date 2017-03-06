INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked) VALUES ('jprss_rssChannels', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">RSS Channels</property>
<property key="it">Canali RSS</property>
</properties>', NULL, 'jprss', NULL, NULL, 1);




INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('jprss_rssChannels', 'jprss_rssChannels', 'jprss', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign jprss=JspTaglibs["/jprss-core"]>

<@jprss.rssList listName="rssList" />
<@wp.info key="langs" var="langs" />

<h1><@wp.i18n key="jprss_FEED_LIST"/></h1>
<dl class="dl-horizontal">
<#if (rssList??) && (rssList?has_content) && (rssList?size > 0)>
	<#list rssList as channel>
		<h2><span class="icon  icon-th-list"></span>&#32;${channel.title}</h2>
		<p>${channel.description}</p> 
		<ul class="unstyled inline margin-medium-bottom">
			<#list langs as channelLang>
				<li class="inline">
					<a href="<@wp.info key="systemParam" paramName="applicationBaseURL" />do/jprss/Rss/Feed/show.action?id=${channel.id}&amp;lang=${channelLang.code}"
						class="badge">${channelLang.descr}</a>
				</li>
			</#list>
		</ul>
	</#list>
</#if>', 1);




INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprss_FEED_LIST', 'en', 'Channels');
INSERT INTO localstrings(keycode, langcode, stringvalue) VALUES ('jprss_FEED_LIST', 'it', 'Canali Rss');