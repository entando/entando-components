INSERT INTO sysconfig(version, item, descr, config)   VALUES ('test', 'jpcollaboration_config', 'Configurazione servizio Crowd Sourcing', '<crowdSourcingConfig>
	<idea>
		<moderateEntries descr="Determina se la pubblicazione deve essere moderata">true</moderateEntries>
	</idea>
</crowdSourcingConfig>');

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES('jpcollaboration_entryIdea', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - New Idea</property>
<property key="it">Collaboration - Nuova Idea</property>
</properties>', NULL, 'jpcollaboration',
'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/collaboration/FrontEnd/Idea/NewIdea/intro.action</property>
</properties>', 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpcollaboration_ideaInstance', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Publish instance</property>
<property key="it">Collaboration - Pubblica istanza</property>
</properties>', '<config>
  <parameter name="instanceCode">Discussion instance code (mandatory)</parameter>
  <action name="jpcrowdsourcingIdeaInstanceViewerConfig"/>
</config>', 'jpcollaboration', NULL, NULL, 1, NULL);


INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES('jpcollaboration_idea', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Idea Details</property>
<property key="it">Collaboration - Dettagli Idea</property>
</properties>', NULL, 'jpcollaboration',
'formAction', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="actionPath">/ExtStr2/do/collaboration/FrontEnd/Idea/viewIdea.action</property>
</properties>', 1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES('jpcollaboration_idea_tags', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Categories</property>
<property key="it">Collaboration - Categorie</property>
</properties>', NULL, 'jpcollaboration',
NULL, NULL, 1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES('jpcollaboration_idea_find', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Search Idea</property>
<property key="it">Collaboration - Ricerca Idea</property>
</properties>', NULL, 'jpcollaboration',
NULL, NULL, 1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES('jpcollaboration_statistics', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Statistics</property>
<property key="it">Collaboration - Statistiche</property>
</properties>', NULL, 'jpcollaboration',
NULL, NULL, 1);

INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
	VALUES('jpcollaboration_fastEntryIdea', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Quick Entry Idea</property>
<property key="it">Collaboration - Inserimento Rapido Idea</property>
</properties>', NULL, 'jpcollaboration',
NULL, NULL, 1);

INSERT INTO widgetcatalog (code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked, maingroup) VALUES ('jpcollaboration_search_result', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
<property key="en">Collaboration - Search Result</property>
<property key="it">Collaboration - Risultati della ricerca</property>
</properties>', NULL, 'jpcollaboration', NULL, NULL, 1, NULL);


INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_MORE_RECENT', 'it', 'Le più recenti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_MORE_VOTED', 'it', 'Le più votate');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_VOTE_AGREE', 'it', 'Mi piace');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_VOTE_DISAGREE', 'it', 'Non mi piace');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_LIKE_IT', 'it', 'Mi piace');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_NOT_LIKE_IT', 'it', 'Non mi piace');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_GO_TO', 'it', 'Torna alle');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_PUBBL', 'it', 'Scritta da');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_COMMENT_SAID', 'it', 'ha scritto');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LABEL_TITLE', 'it', 'Titolo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG_REMOVE', 'it', 'Elimina');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SAVE_IDEA', 'it', 'Salva');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG_JOIN', 'it', 'Associa');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SAVE_COMMENT', 'it', 'Commenta');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IDEA_TITLE', 'it', 'Ricerca');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IDEAS', 'it', 'Cerca Idee');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH', 'it', 'Cerca');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_STATISTIC_TITLE', 'it', 'Statistiche');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_STATISTICS_EMPTY', 'it', 'Non ci sono statistiche');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SUBMIT_IDEA', 'en', 'Submit');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SUBMIT_IDEA', 'it', 'Invia');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_DO_LOGIN', 'en', 'Sign in if you want to participate.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_DO_LOGIN', 'it', 'Accedi se vuoi partecipare.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_MORE_VOTED', 'en', 'Most Voted');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_VOTE_AGREE', 'en', 'I like it');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_VOTE_DISAGREE', 'en', 'I don''t like it');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_LIKE_IT', 'en', 'I like it');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_NOT_LIKE_IT', 'en', 'I don''t like it');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_GO_TO', 'en', 'Go to');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_PUBBL', 'en', 'Written by');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_COMMENT_SAID', 'en', 'said');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_COMMENT', 'en', 'Your comment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_COMMENT', 'it', 'Il tuo commento');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LABEL_TITLE', 'en', 'Title');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LABEL_DESCR', 'en', 'Your entry');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LABEL_DESCR', 'it', 'Descrizione');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG_REMOVE', 'en', 'Remove');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SAVE_IDEA', 'en', 'Save');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG_JOIN', 'en', 'Join');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SAVE_COMMENT', 'en', 'Send Comment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IDEA_TITLE', 'en', 'Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IDEAS', 'en', 'Search Ideas');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH', 'en', 'Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_STATISTIC_TITLE', 'en', 'Stats');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_STATISTICS_EMPTY', 'en', 'No stats at the moment.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_NOIDEA_NOW', 'en', 'No Ideas at the moment.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_MORE_RECENT', 'en', 'Recent');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG', 'en', 'Filed under');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG', 'it', 'Categoria');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAGS_ALL', 'en', 'All');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAGS_ALL', 'it', 'Tutte');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAGS_EMPTY', 'en', 'No tags available.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAGS_EMPTY', 'it', 'Non ci sono categorie disponibili.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAGS_TITLE', 'en', 'Tags');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAGS_TITLE', 'it', 'Categorie');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_TITLE', 'en', 'Crowdsourcing Ideas');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_TITLE', 'it', 'Idee');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LIST_IDEA_TITLE', 'en', 'Entries');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LIST_IDEA_TITLE', 'it', 'Contributi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LIST_IDEA_FILTERED_BY', 'en', 'Filtered by');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LIST_IDEA_FILTERED_BY', 'it', 'Lista filtrata per');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LIST_IDEA_ALL', 'en', 'Remove the filter and view all');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_LIST_IDEA_ALL', 'it', 'Annulla il filtro');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TITLE_SEARCH', 'en', 'Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TITLE_SEARCH', 'it', 'Cerca');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_FOR', 'en', 'Search for');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_FOR', 'it', 'Cerca testo');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IN_ALL', 'en', 'All categories');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IN_ALL', 'it', 'Tutte le categorie');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_NEW_IDEA_TITLE', 'en', 'Quick entry');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_NEW_IDEA_TITLE', 'it', 'Contributo rapido');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_VOTES', 'en', 'Votes');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_VOTES', 'it', 'Voti espressi');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_COMMENTS', 'en', 'Comments');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_COMMENTS', 'it', 'Commenti inseriti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_USERS', 'en', 'Users involved');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_USERS', 'it', 'Utenti');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_PROPOSED_IDEAS', 'en', 'Proposed ideas');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_PROPOSED_IDEAS', 'it', 'Idee proposte');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_REMOVEFILTER', 'en', 'Remove filter');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_REMOVEFILTER', 'it', 'Rimuovi filtro');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_NOIDEA_FOUND', 'en', 'No idea found.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_NOIDEA_FOUND', 'it', 'Nessuna idea trovata.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_NOT_FOUND', 'it', 'L''idea richiesta non è disponibile. Prova ad effettuare una ricerca.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_NOT_FOUND', 'en', 'Requested idea not found. Try using the search form.');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_DETAILS', 'it', 'Dettagli Idea');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_IDEA_DETAILS', 'en', 'Idea Details');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_INSTANCE', 'it', 'Istanza');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_INSTANCE', 'en', 'Instance');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_BACK_TO_LIST', 'it', 'Lista');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_BACK_TO_LIST', 'en', 'List');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_RESULT', 'it', 'Risultati della ricerca');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_RESULT', 'en', 'Search results');



INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_listIdea', NULL, 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"]>
<#assign fn=JspTaglibs["http://java.sun.com/jsp/jstl/functions"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
<#assign jpavatar=JspTaglibs["/jpavatar-apsadmin-core"]>

<div class="jpcrowdsourcing listIdea">
<@wp.info key="currentLang" var="currentLang" />
<@s.set var="ideaListDate" value="%{getIdeas(0)}"  />
<@s.set var="ideaListVote" value="%{getIdeas(1)}" />
<@jpcrwsrc.pageWithWidget var="viewIdea_page" widgetTypeCode="jpcollaboration_idea" listResult=false/>
<@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaList" listResult=false/>
<@jpcrwsrc.ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>


<@wp.url var="currentCategoryUrl" page="listIdea_page.code" paramRepeat=false/>
<#if (categoryInfoList?size >0)>
    <#assign paramTag>
      <@s.property value="ideaTag"/>
    </#assign>
    <#list categoryInfoList as categoryInfo>
        <#if (paramTag?? && paramTag==categoryInfo.category.code)>
	<@wp.url var="currentCategoryUrl" paramRepeat=false page="listIdea_page.code">
	<@wp.urlPar name="ideaTag">
	<@c.out value="${categoryInfo.category.code}" />
	</@wp.urlPar>
	</@wp.url>
	<@c.set var="currentCategoryCode" value="${categoryInfo.category.code}"></@c.set>
        <@c.set var="currentCategoryTitle" value="${categoryInfo.title}"></@c.set>
</#if>
     </#list>
</#if>



<#assign paramText>
      <@s.property value="ideaText"/>
    </#assign>
<h1><@wp.i18n key="jpcollaboration_LIST_IDEA_TITLE" /></h1>
<#if (currentCategoryTitle?? && paramText??)>
	<p>
		<#if (currentCategoryTitle??)>
			<span class="label label-info"><span class="icon-tags icon-white" title="<@wp.i18n key="jpcollaboration_LIST_IDEA_FILTERED_BY" />: <@c.out value="${currentCategoryTitle}" />"></span>&nbsp;<@c.out value="${currentCategoryTitle}" />&nbsp;&nbsp;&nbsp;&nbsp;<a href="<@wp.url page="listIdea_page.code" />" title="<@wp.i18n key="jpcollaboration_LIST_IDEA_ALL" />"><span class="icon-remove icon-white"></span></a></span>
		</#if>
		<#--TO DO--<#if (paramText??)>
			<span class="label label-info"><span class="icon-search icon-white" title="<@wp.i18n key="SEARCHED_FOR" />:${paramText}"></span>&nbsp;${paramText}&nbsp;&nbsp;&nbsp;&nbsp;<a href="<@c.out value="${currentCategoryUrl}" />" title="<@wp.i18n key="jpcollaboration_SEARCH_REMOVEFILTER" />"><span class="icon-remove icon-white"></span></a></span>
		</#if>-->
	</p>
</#if>

<@s.if test="null != #parameters.listIdea_form">
	<@s.if test="hasActionErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="ERRORS" /></p>
				<ul>
					<@s.set var="actionErrorsVar"  value="actionErrors"/>
					<#list actionErrorsVar as itemAcitonError>
						${itemAcitonError}
				    </#list>
				</ul>
			</div>
		</@s.if>
	<@s.if test="hasActionMessages()">
			<div class="alert alert-info">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="MESSAGES" /></p>
				<ul>
				    <@s.set var="actionMessagesVar"  value="actionMessages"/>
					<#list actionMessagesVar as itemActionMessages>
						${itemActionMessages}
				    </#list>
				</ul>
			</div>
		</@s.if>
		<@s.if test="hasFieldErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="ERRORS" /></p>
				<ul>
					<@s.set var="errorsVar"  value="fieldErrors"/>
                    <#list errorsVar as itemError>
						<@s.set var="tempValue"  value="value"/>
						<#list tempValue as itemValue>
							${itemValue}
						</#list>
				    </#list>						
				</ul>
			</div>
		</@s.if>
	</@s.if>
	

 <#assign typeT>
 	  <@s.property value="%{#parameters.type}" />
 </#assign>

	<#if typeT?? && typeT = "vote">
			<@c.set var="titleLabel" value="jpcrowdsourcing_TYPE_VOTE" />
			<@s.set var="currentList" value="#ideaListVote" scope="page" />
		<#else>
			<@c.set var="titleLabel" value="jpcrowdsourcing_TYPE_DATE" />
			<@s.set var="currentList" value="#ideaListDate" scope="page" />
	</#if>  
		   
		 
		  
	<ul class="nav nav-pills">
		<li<#if (typeT?? && typeT!=''date'' && typeT!=''vote'')> class="active"</#if>>
	<a href="<@c.out value="${currentCategoryUrl}" escapeXml=false />"><@wp.i18n key="jpcollaboration_TAGS_ALL" /></a>
		</li>
		<@wp.url var="linkTypeListDate"><@wp.urlPar name="type">date</@wp.urlPar><@wp.urlPar name="ideaTag">${paramTag}</@wp.urlPar></@wp.url>
	
			   <li<#if (typeT?? && typeT==''date'')> class="active"</#if>>
			<a href="<@c.out value="${linkTypeListDate}" escapeXml=false/>">
			<@wp.i18n key="jpcollaboration_IDEA_MORE_RECENT" />
			(<@s.property value="#ideaListDate.size"/>)</a>
		</li>
    <@wp.url var="linkTypeListVote"><@wp.urlPar name="type">vote</@wp.urlPar><@wp.urlPar name="ideaTag">${paramTag}</@wp.urlPar></@wp.url>
 		 
		 <li<#if (typeT?? && typeT==''vote'')> class="active"</#if>>
 			<a href="<@c.out value="${linkTypeListVote}" escapeXml=false />">
 			<@wp.i18n key="jpcollaboration_IDEA_MORE_VOTED" />
 			(<@s.property value="#ideaListVote.size"/>)</a>
 		</li>	
	</ul>



<#if !(currentList?size > 0)>
<p class="alert alert-info">
    <@wp.i18n key="jpcollaboration_NOIDEA_FOUND" />
</p>
<#else>
	
<@wp.pager listName="currentList" objectName="groupIdea" pagerIdFromFrame=true max=5 pagerId="listIdeaPager" >


<@wp.freemarkerTemplateParameter var="group" valueName="groupIdea"  > 
<@wp.fragment code="default_pagerBlock" escapeXml=false />

<#list currentList as ideaId>

<#if (ideaId_index >= groupIdea.begin) && (ideaId_index <= groupIdea.end)>
	
<@wp.freemarkerTemplateParameter var="ideaId" valueName="ideaId"/>
<@s.set var="idea" value="%{getIdea(${ideaId})}" />
<@s.set var="maxChars" value="5" />
<@s.set var="ideaAbstract" value="%{getIdea(${ideaId}).descr.trim().substring(0,maxChars)}" />


<#if viewIdea_page?? && viewIdea_page.code??>
	<@wp.url var="viewIdea_pageUrl" page="${viewIdea_page.code}"><@wp.urlPar name="ideaId" ><@s.property value="#idea.id"/></@wp.urlPar></@wp.url>
	<#assign link>
	<@c.out value="${viewIdea_pageUrl}" />
	</#assign>
<#else>
	<@wp.url var="viewIdea_pageUrl" page=""><@wp.urlPar name="ideaId" ><@s.property value="#idea.id"/></@wp.urlPar></@wp.url>
	<#assign link>

	</#assign>
</#if>
<h2><a href=${link}><@s.property value="#idea.title" /></a></h2>
<@s.set var="categories" value="%{getIdeaTags(#idea)}" />

<@s.if test="null != #categories && #categories.size > 0">
<p>
<span class="icon-tags"></span>&#32;<@wp.i18n key="jpcollaboration_TAG" />:&#32;
<@s.iterator value="#categories" var="cat" status="status">


<@wp.freemarkerTemplateParameter var="listIdea_page" valueName="listIdea_page"/>
<#-- <#assign var>
${listIdea_page.code}
</#assign> -->
<@wp.url var="listIdea_pageUrl" page=var><@wp.urlPar name="ideaTag" ><@s.property value="#cat.code" /></@wp.urlPar></@wp.url>
<a href="<@c.out value="${listIdea_pageUrl}" escapeXml=false />"><@s.property value="#cat.title" /></a><@s.if test="!#status.last">,&#32;</@s.if>
		
</@s.iterator>
</p>
</@s.if>

<@s.set var="avatarUsername" value="#idea.username" scope="request"/>
<@jpavatar.avatar username="avatarUsername" var="currentAvatar" />

<p><img src="${currentAvatar}" class="img-polaroid" width="32" title="<@wp.i18n key="jpcollaboration_IDEA_PUBBL" />
&#32;<@s.property value="#idea.username"/>&#32;<@s.date name="#idea.pubDate" />" />
&#32;<@s.property value="#idea.username"/>&#32;<span title="<@s.date name="#idea.pubDate" />">
<@s.date name="#idea.pubDate" nice=true/></span></p>

<form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/Manage/ideaLike.action" />" method="post" class="form-inline display-inline">
<p class="noscreen">
<@s.if test="%{null != #parameters[''type'']}"><@wpsf.hidden name="type" value="%{#parameters[''type]}" /></@s.if>
<@s.if test="%{null != #parameters[''ideaTag'']}"><@wpsf.hidden name="ideaTag" value="%{#parameters[''ideaTag'']}" /></@s.if>
<@s.if test="%{null != #parameters[#pagerIdNameVar + ''_item'']}"><@wpsf.hidden name="%{#pagerIdNameVar + ''_item''}" value="%{#parameters[#pagerIdNameVar + ''_item'']}" /></@s.if>
<@wpsf.hidden name="ideaId" value="%{#idea.id}" />
<@wpsf.hidden name="listIdea_form" value="listIdea_form" />
<input type="hidden" name="userAction" value="like" />
</p>
<@s.token name="listIdea" />
<@s.set name="labelSubmit"><@wp.i18n key="jpcollaboration_IDEA_LIKE_IT" escapeXml=false /></@s.set>
<@wpsf.submit value="%{#labelSubmit}" cssClass="btn btn-small btn-success" />
<span class="badge badge-success" title="<@s.property value="#idea.votePositive" /> <@wp.i18n key="jpcollaboration_IDEA_VOTE_AGREE" />">&#32;<i class="icon-thumbs-up icon-white"></i>&#32;<@s.property value="#idea.votePositive" /></span>
</form>

<form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/Manage/ideaUnlike.action" />" method="post" class="form-inline display-inline">
<p class="noscreen">
<@s.if test="%{null != #parameters[''type'']}"><@wpsf.hidden name="type" value="%{#parameters[''type'']}" /></@s.if>
<@s.if test="%{null != #parameters[''ideaTag'']}"><@wpsf.hidden name="ideaTag" value="%{#parameters[''ideaTag'']}" /></@s.if>
<@s.set var="pagerIdNameVar" ><@c.out value="${groupIdea.pagerId}" /></@s.set>
<@s.if test="%{null != #parameters[#pagerIdNameVar + ''_item'']}"><@wpsf.hidden name="%{#pagerIdNameVar + ''_item''}" value="%{#parameters[#pagerIdNameVar + ''_item'']}" /></@s.if>
<@wpsf.hidden name="ideaId" value="%{#idea.id}" />
<@wpsf.hidden name="listIdea_form" value="listIdea_form" />
<input type="hidden" name="userAction" value="unlike" />
</p>
<@s.token name="listIdea" />
<@s.set name="labelSubmit"><@wp.i18n key="jpcollaboration_IDEA_NOT_LIKE_IT" escapeXml=false /></@s.set>
<span class="badge badge-important" title="<@s.property value="#idea.voteNegative" /> <@wp.i18n key="jpcollaboration_IDEA_VOTE_DISAGREE" />">&#32;<@s.property value="#idea.voteNegative" />&#32;<i class="icon-thumbs-down icon-white"></i></span>&#32;<@wpsf.submit value="%{#labelSubmit}" cssClass="btn btn-small btn-danger" />
</form>

<p class="margin-medium-vertical">
	<@s.set value="#idea.comments[3]" var="currentComments" />
        <a href="<@c.out value="${viewIdea_pageUrl}" />#jpcrdsrc_comments_<@s.property value="#idea.id" />">
	<i class="icon-comment"></i>&#32;
	<@s.if test="null == #currentComments || #currentComments.size == 0">0</@s.if>
	<@s.else><@s.property value="#currentComments.size" /></@s.else>&#32;
	<@wp.i18n key="jpcollaboration_IDEA_COMMENTS" />
	</a>
</p>

</#if>
</#list>

	<@wp.fragment code="default_pagerBlock" escapeXml=false />
     </@wp.freemarkerTemplateParameter >
	</@wp.pager>

	</#if>


</div>', 1);



INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_idea_find', 'jpcollaboration_idea_find', 'jpcollaboration', NULL, '<#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
<#assign jacms=JspTaglibs["/jacms-aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>

<div class="jpcrowdsourcing jpcrowdsourcing_idea_find">
<h1><@wp.i18n key="jpcollaboration_TITLE_SEARCH" /></h1>
<@jpcrwsrc.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
<@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar" />
<#if  (instanceVar)?? >
<@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceVar}" listResult=false/>
<#else>
<@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_search_result" listResult=false/>			
</#if>
<@jpcrwsrc.ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>
<form action="<@wp.url page="listIdea_page.code" />" method="get">
<input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value=instanceVar/>
<p>	
<label for="jpcrowdsourcing_search_for"><@wp.i18n key="jpcollaboration_SEARCH_FOR" /></label>
<input id="jpcrowdsourcing_search_for" name="ideaText" type="text" value="<#if (ideaText)?? > ${ideaText}</#if>" class="input-block-level" />
</p>
</p>
<#if (categoryInfoList?size > 0) >
<p>
<label for="jpcrowdsourcing_search_in">In</label>
<select id="jpcrowdsourcing_search_in" name="ideaTag" class="input-block-level">
<option value="" <#if (param.ideaTag)?? > selected="selected" </#if>><@wp.i18n key="jpcollaboration_SEARCH_IN_ALL" /></option>
<#list categoryInfoList  as categoryInfo  >
<option value=${categoryInfo.category.code} <#if  (param.ideaTag)??  && param.ideaTag==categoryInfo.category.code> selected="selected" </#if>>
${categoryInfo.title}
</option>
</#list>
</select>
</p>
</#if>
<p><input type="submit" value="<@wp.i18n key="jpcollaboration_SEARCH" />" class="btn" /></p>
</p>
</form>
</div> 
', 1);


INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_fastEntryIdea', 'jpcollaboration_fastEntryIdea', 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
<#assign jacms=JspTaglibs["/jacms-aps-core"]>
<#assign s=JspTaglibs["/struts-tags"]>


<div class="jpcrowdsourcing jpcrowdsourcing_fastEntryIdea">
	<h1><@wp.i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h1>	
	<@jpcrwsrc.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
	<@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar" />
	<#if Session.currentUser != ''guest''>
		 <@jpcrwsrc.pageWithWidget var="entryIdea_page" widgetTypeCode="jpcollaboration_entryIdea" listResult=false />
		 
		 <form action="<@wp.url page="${entryIdea_page.code}" />" method="post" accept-charset="UTF-8">
		 
		 <#if instanceVar??>
			<input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value="${instanceVar}" />		
		 </#if>	
		 <p>
			<label for="jpcrowdsourcing_fastDescr"><@wp.i18n key="jpcollaboration_LABEL_DESCR" /></label>
			<textarea rows="5" cols="40" name="jpcrowdsourcing_fastDescr" id="jpcrowdsourcing_fastDescr" class="input-block-level"></textarea>
			</p>
			<p>
			<input type="submit" value="<@wp.i18n key="jpcollaboration_SUBMIT_IDEA" />" class="btn" />
			</p>
			</form>
	<#else>	
		<p class="alert alert-warning"><@wp.i18n key="jpcollaboration_DO_LOGIN" /></p>
	</#if>

</div>', 1);


INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_entryIdea', 'jpcollaboration_entryIdea', 'jpcollaboration', NULL, '<#assign sj=JspTaglibs["/struts-jquery-tags"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"]>
<#assign fn=JspTaglibs["http://java.sun.com/jsp/jstl/functions"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>  

<div class="jpcrowdsourcing entryIdea">
	<h1><@wp.i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h1>
    <form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/NewIdea/save.action" />" method="post"     accept-charset="UTF-8" class="form-horizontal">
		<@s.if test="null != #parameters.entryIdea_form">
	<@s.if test="hasActionErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="ERRORS" /></p>
				<ul>
					<@s.set var="actionErrorsVar"  value="actionErrors"/>
					<#list actionErrorsVar as itemAcitonError>
						${itemAcitonError}
				    </#list>
				</ul>
			</div>
		</@s.if>
	<@s.if test="hasActionMessages()">
			<div class="alert alert-info">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="MESSAGES" /></p>
				<ul>
				    <@s.set var="actionMessagesVar"  value="actionMessages"/>
					<#list actionMessagesVar as itemActionMessages>
						${itemActionMessages}
				    </#list>
				</ul>
			</div>
		</@s.if>
		<@s.if test="hasFieldErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="ERRORS" /></p>
				<@s.set var="fieldErrorsVar"  value="fieldErrors"/>
				<ul>
					<#list fieldErrorsVar?values as it>
						
							<#list it as var>
								<li>${var}</li>
							</#list>
						
					</#list>
				</ul>	
			</div>
		</@s.if>
	</@s.if>
	

<#assign instanceCodeVar>
<@s.property value="idea.instanceCode" />
</#assign>
		<p class="noscreen">
			<@jpcrwsrc.pageWithWidget var="ideaList_page" showletTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="instanceCodeVar" listResult=false/>

			
			
			
			<input type="hidden" name="saveidea_destpage" value="ideaList_page.code" />
			<@wpsf.hidden name="idea.username" value="%{currentUser.username}" />
			<@wpsf.hidden name="entryIdea_form" value="entryIdea_form" />
			<@s.token name="entryIdea"/>
		</p>

		<div class="control-group">
			<label for="idea_instanceCode" class="control-label"><@wp.i18n key="jpcollaboration_INSTANCE" /></label>
			<div class="controls">
				<@wpsf.select list="ideaInstances" name="idea.instanceCode" id="idea_instanceCode" listKey="code" listValue="code" />
			</div>
		</div>

		<div class="control-group">
			<label for="idea_title" class="control-label"><@wp.i18n key="jpcollaboration_LABEL_TITLE" /></label>
			<div class="controls">
				<@wpsf.textfield name="idea.title" id="idea_title" value="%{idea.title}" cssClass="span6" />
			</div>
		</div>

		<div class="control-group">
			<label for="idea_descr" class="control-label"><@wp.i18n key="jpcollaboration_LABEL_DESCR" /></label>
			<div class="controls">
				<@wpsf.textarea name="idea.descr" id="idea_descr" value="%{idea.descr}" cssClass="span6" cols="40" rows="3" />
			</div>
		</div>

		<div class="control-group">
			<label for="" class="control-label"><@wp.i18n key="jpcollaboration_TAGS_TITLE" /></label>
			<div class="controls">
				<div class="input-append">
		
		<@c.set var="jquery_libraries">
		<@sj.head jqueryui="true"/>
		<script type="text/javascript"><!--//--><![CDATA[//><!--jQuery.noConflict();//--><!]]></script>
		</@c.set>
		<@wp.headInfo type="JS_JQUERY" var="jquery_libraries" />
		<@sj.autocompleter id="tag"  name="tag"  list="%{tagsArray}" forceValidOption="false" />
		
		
		
		 <@s.set name="labelJoin"><@wp.i18n key="jpcollaboration_TAG_JOIN" /></@s.set>
					<@wpsf.submit action="joinCategory" value="%{#labelJoin}" cssClass="btn btn-info" id="join_tag"/>
                        <script type="text/javascript">//<![CDATA[//>
                            jQuery("#join_tag").click(function(event) {
                                var checktag = jQuery.isEmptyObject(jQuery("#tag_widget").val());
                                if (checktag)
                                    event.preventDefault();
                            });//<!]]>
                        </script>     
		</div>
		</div>
		</div>
		
		<@s.if test="!(null == tags || tags.size == 0)">
		<div class="well well-small">
			<ul class="inline">
				<@s.iterator value="tags" var="tag">
					<@s.set value="getCategory(#tag)" var="currentCat" />
					<@s.if test="null != #currentCat">
						<li>
							<@wpsf.hidden name="tags" value="%{#currentCat.code}" />
							<@wpsa.actionParam action="removeCategory" var="removeTagAction"><@wpsa.actionSubParam name="tag" value="%{#currentCat.code}" /></@wpsa.actionParam>
							<@s.set name="labelRemove"><@wp.i18n key="jpcollaboration_TAG_REMOVE" /></@s.set>
							<span class="label label-info">
								<i class="icon-tags icon-white"></i>&nbsp;&nbsp;
								<@s.property value="#currentCat.title" />&#32;
								<@wpsf.submit action="%{#removeTagAction}" type="button" theme="simple" cssClass="btn btn-link btn-small" title="%{#labelRemove}" >
									<span class="icon-remove icon-white"></span>								
								</@wpsf.submit>
							</span>
						</li>
					</@s.if>
				</@s.iterator>
			</ul>
		</div>
		</@s.if>
			<p class="text-center margin-medium-vertical">
			<@s.set name="labelSubmit"><@wp.i18n key="jpcollaboration_SUBMIT_IDEA" /></@s.set>
			<@wpsf.submit  value="%{#labelSubmit}" cssClass="btn btn-primary btn-large" />
		</p>
		
	</form>

</div>
', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_viewIdea', NULL, 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign fmt=JspTaglibs["http://java.sun.com/jsp/jstl/fmt"]>
<#assign fn=JspTaglibs["http://java.sun.com/jsp/jstl/functions"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>

<div class="jpcrowdsourcing viewIdea">
<@s.set var="idea" value="%{getIdea(ideaId)}" />
<@s.if test="#idea!=null">
	<@c.set var="instanceCodeVar"><@s.property value="#idea.instanceCode" /></@c.set>
	<@jpcrwsrc.pageWithWidget var="ideaList_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceCodeVar}" listResult=false/>
	<@wp.freemarkerTemplateParameter var="ideaList_page" valueName="ideaList_page"/>
	<@wp.url page=ideaList_page.code var="listPage"/>
		
	<form action="${listPage}" method="post" class="form-inline display-inline">
			<@s.set name="labelList"><@wp.i18n key="jpcollaboration_BACK_TO_LIST" escapeXml=false /></@s.set>
			<p><@wpsf.submit value="%{#labelList}" cssClass="btn" /></p>
	</form>
	<h1><@s.property value="#idea.title" /></h1>
	<#if (idea.tags?? && idea.tags?size > 0 )>
		<p>
			<span class="icon-tags"></span>&#32;<@wp.i18n key="jpcollaboration_TAG" />:&#32;
			<#list idea.tags as cat>	
			<#assign catVar>${cat}</#assign>				
			<@wp.url var="ideaList_pageUrl" page="${ideaList_page.code}"><@wp.urlPar name="ideaTag" >${cat}</@wp.urlPar></@wp.url>
			<a href="${ideaList_pageUrl}">${cat}</a><@s.if test="!#status.last">,&#32;</@s.if>
			</#list>		
		</p>
	</#if>
		
	<p><@s.property value=''#idea.descr.trim().replaceAll("\r", "").replaceAll("\n\n+", "</p><p>").replaceAll("\n", "</p><p>")'' escapeHtml=false /></p>
	<p><i class="icon-info-sign"></i>&#32;<@wp.i18n key="jpcollaboration_IDEA_PUBBL" />&#32;<@s.property value="#idea.username"/>&#32;<span title="<@s.date name="#idea.pubDate" />"><@s.date name="#idea.pubDate" nice=true/></span></p>

		<form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/ideaLike.action"/>" method="post" class="form-inline display-inline">
			<p class="noscreen">
				<@wpsf.hidden name="ideaId" value="%{ideaId}" />
				<input type="hidden" name="userAction" value="like" />
			</p>

			<@s.token name="listIdea" />
			<@s.set name="labelSubmit"><@wp.i18n key="jpcollaboration_IDEA_LIKE_IT" escapeXml=false /></@s.set>
			<@wpsf.submit value="%{#labelSubmit}" cssClass="btn btn-small btn-success" />
			<span class="badge badge-success" title="<@s.property value="#idea.votePositive" /> <@wp.i18n key="jpcollaboration_IDEA_VOTE_AGREE" />">&#32;<i class="icon-thumbs-up icon-white"></i>&#32;<@s.property value="#idea.votePositive" /></span>

		</form>

		<form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/ideaUnlike.action"/>" method="post" class="form-inline display-inline">
			<p class="noscreen">
				<@wpsf.hidden name="ideaId" value="%{ideaId}" />
				<input type="hidden" name="userAction" value="dislike" />
			</p>

			<@s.token name="listIdea" />
			<@s.set name="labelSubmit"><@wp.i18n key="jpcollaboration_IDEA_NOT_LIKE_IT" escapeXml=false /></@s.set>
			<span class="badge badge-important" title="<@s.property value="#idea.voteNegative" /> <@wp.i18n key="jpcollaboration_IDEA_VOTE_DISAGREE" />">&#32;<@s.property value="#idea.voteNegative" />&#32;<i class="icon-thumbs-down icon-white"></i></span>&#32;<@wpsf.submit value="%{#labelSubmit}" cssClass="btn btn-small btn-danger" />
		</form>

		
		<@s.set value="#idea.comments[3]" var="currentComments" />
		<h2 id="jpcrdsrc_comments_<@s.property value="#idea.id" />"><@wp.i18n key="jpcollaboration_IDEA_COMMENTS" /></h2>
		<p>
			<i class="icon-comment"></i>&#32;
			<@s.if test="null == #currentComments || #currentComments.size == 0">0</@s.if>
			<@s.else><@s.property value="#currentComments.size" /></@s.else>&#32;
			<@wp.i18n key="jpcollaboration_IDEA_COMMENTS" />
		</p>

		
	<#if Session.currentUser != ''guest''>
	<form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/saveComment.action"/>" method="post" accept-charset="UTF-8">

	<@s.if test="hasActionErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="ERRORS" /></p>
				<ul>
					<@s.set var="actionErrorsVar"  value="actionErrors"/>
					<#list actionErrorsVar as itemAcitonError>
						${itemAcitonError}
				    </#list>
				</ul>
			</div>
		</@s.if>
		<@s.if test="hasActionMessages()">
			<div class="alert alert-info">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="MESSAGES" /></p>
				<ul>
				    <@s.set var="actionMessagesVar"  value="actionMessages"/>
					<#list actionMessagesVar as itemActionMessages>
						${itemActionMessages}
				    </#list>
				</ul>
			</div>
		</@s.if>
		<@s.if test="hasFieldErrors()">
			<div class="alert alert-error">
				<button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
				<p class="alert-heading"><@wp.i18n key="ERRORS" /></p>
				<ul>
					<@s.set var="errorsVar"  value="fieldErrors"/>
                    <#list errorsVar as itemError>
						<@s.set var="tempValue"  value="value"/>
						<#list tempValue as itemValue>
							${itemValue}
						</#list>
				    </#list>						
				</ul>
			</div>
		</@s.if>

			<p class="noscreen">
						<@wpsf.hidden name="comment_form" value="comment_form" />
						<@wpsf.hidden name="ideaId" value="%{ideaId}" />
						<@wpsf.hidden name="ideaComment.id" value="%{ideaComment.id}" />
						<@wpsf.hidden name="ideaComment.ideaId" value="%{ideaId}" />
					</p>

					<@s.token name="saveComment"/>
					<p>
						<label for="ideaComment_comment"><@wp.i18n key="jpcollaboration_COMMENT" /></label>
						<@wpsf.textarea id="ideaComment_comment" name="ideaComment.comment" cols="40" rows="5" cssClass="span6" />
					</p>

					<@s.set name="labelSave"><@wp.i18n key="jpcollaboration_SAVE_COMMENT" escapeXml=false /></@s.set>
					<p><@wpsf.submit value="%{#labelSave}" cssClass="btn" /></p>

				</form>
	
	<#else>
		<p class="alert alert-warning"><@wp.i18n key="jpcollaboration_DO_LOGIN" /></p>	
	</#if>

		<@s.if test="#currentComments.size > 0">
			<@s.iterator value="#currentComments" var="currentCommentId">
				<@s.set var="currentComment" value="%{getComment(#currentCommentId)}" />
				<p>
						<i class="icon-comment"></i>&#32;
						<span title="<@s.date name="#currentComment.creationDate" />"><@s.date name="#currentComment.creationDate" nice=true /></span>&#32;
						<em><@s.property value="#currentComment.username" /></em>&#32;
						<@wp.i18n key="jpcollaboration_COMMENT_SAID" />:
				</p>
				<blockquote>
					<p>
						<@s.property value=''#currentComment.comment.trim().replaceAll("\r", "").replaceAll("\n\n+", "</p><p>").replaceAll("\n", "</p><p>")'' escapeHtml=false />
					</p>
				</blockquote>
			</@s.iterator>
		</@s.if>
	
		<form action="${listPage}" method="post" class="form-inline display-inline">
			<@s.set name="labelList"><@wp.i18n key="jpcollaboration_BACK_TO_LIST" escapeXml=false /></@s.set>
			<p><@wpsf.submit value="%{#labelList}" cssClass="btn" /></p>
		</form>
	</@s.if>
	
	<@s.else>
		<div class="alert alert-error">
			<h1 class="alert-heading"><@wp.i18n key="jpcollaboration_IDEA_DETAILS" /></h1>
			<p>
				<@wp.i18n key="jpcollaboration_IDEA_NOT_FOUND" />
			</p>
		</div>
	</@s.else>
		
</div>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_statistics', 'jpcollaboration_statistics', 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign wpcs=JspTaglibs["/jpcrowdsourcing-aps-core"]>
<#assign jacms=JspTaglibs["/jacms-aps-core"]>

<div class="jpcrowdsourcing jpcrowdsourcing_statistics">

<h1><@wp.i18n key="jpcollaboration_STATISTIC_TITLE"/></h1>
 <@wpcs.currentPageWidget param="config" configParam="instanceCode" widget="collaboration_ideaInstance" var="instanceVar"/>
 <@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar"/>
	
 <@wpcs.statistic var="stats" instanceCode=instanceVar/>
<#if (stats??)>
  <ul class="unstyled">
   <li class="text-right">
   <@wp.i18n key="jpcollaboration_PROPOSED_IDEAS" />&#32;<span class="badge">
   ${stats.ideas}
    </span>
   </li>
   <li class="text-right">
    <@wp.i18n key="jpcollaboration_VOTES" />&#32;
    <span class="badge">
     ${stats.votes}
    </span>
   </li>
   <li class="text-right">
    <@wp.i18n key="jpcollaboration_IDEA_COMMENTS" />&#32;
    <span class="badge">
     ${stats.comments}
    </span>
   </li>
   <li class="text-right">
    <@wp.i18n key="jpcollaboration_USERS" />&#32;
    <span class="badge">
     ${stats.users}
    </span>
   </li>
	<#else>
   <p class="alert alert-warning">
    <@wp.i18n key="jpcollaboration_STATISTICS_EMPTY" />
   </p>
   </#if>
</div>', 1);
INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_idea_tags', 'jpcollaboration_idea_tags', 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign s=JspTaglibs["/struts-tags"]>
<#assign wp=JspTaglibs["/aps-core"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
<#assign jacms=JspTaglibs["/jacms-aps-core"]>

<div class="jpcrowdsourcing jpcrowdsourcing_idea_tags">
	<h1><@wp.i18n key="jpcollaboration_TAGS_TITLE" /></h1>
	<@jpcrwsrc.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
	<@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar"/>
	<@jpcrwsrc.ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>
	<@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue=instanceVar listResult=false/>
	<#if (categoryInfoList?? && categoryInfoList?size >0)>
<#if (RequestParameters.ideaTag??) >
<#assign ideaTagVar>${RequestParameters.ideaTag}</#assign>
<#else>
<#assign ideaTagVar>	
</#assign>
</#if>
		<ul class="nav nav-pills">
		<li<#if (ideaTagVar?? && ideaTagVar== "")  > class="active"</#if>>
			<a href="<@wp.url  page="${listIdea_page.code}" />">
			<@wp.i18n key="jpcollaboration_TAGS_ALL" />
			</a>
		</li>
	<#list categoryInfoList as categoryInfo>
	<#assign categoryVar>${categoryInfo.category.code}</#assign >
		<@wp.url var="listIdea_pageUrl" page="${listIdea_page.code}"><@wp.urlPar name="ideaTag" >${categoryInfo.category.code}</@wp.urlPar></@wp.url>
		<li<#if (ideaTagVar?? && ideaTagVar==categoryVar)  >
		class="active"</#if>>
			<a href="${listIdea_pageUrl}">
				${categoryInfo.title}
			</a>
		</li>		
		</#list>
		</ul>
	<#else>
	<p class="alert alert-warning"><@wp.i18n key="jpcollaboration_TAGS_EMPTY" /></p>
	</#if>
</div>
', 1);
