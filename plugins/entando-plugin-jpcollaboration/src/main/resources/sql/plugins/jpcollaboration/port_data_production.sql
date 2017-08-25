INSERT INTO sysconfig(version, item, descr, config)   VALUES ('production', 'jpcollaboration_config', 'Configurazione servizio Crowd Sourcing', '<crowdSourcingConfig>
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
    <property key="en">Collaboration - Publish idea</property>
    <property key="it">Collaboration - Pubblica istanza</property>
</properties>', '<config>
    <parameter name="instanceCode">Discussion instance code (mandatory)</parameter>
    <action name="jpcrowdsourcingIdeaInstanceViewerConfig"/>
</config>', 'jpcollaboration', NULL, NULL, 1, NULL);


INSERT INTO widgetcatalog(code, titles, parameters, plugincode, parenttypecode, defaultconfig, locked)
VALUES('jpcollaboration_idea', '<?xml version="1.0" encoding="UTF-8"?>
<properties>
    <property key="en">Collaboration - Ideas</property>
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
    <property key="en">Collaboration - Search Ideas</property>
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
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_TAG_JOIN', 'en', 'Join category');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SAVE_COMMENT', 'en', 'Send Comment');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IDEA_TITLE', 'en', 'Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH_IDEAS', 'en', 'Search Ideas');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_SEARCH', 'en', 'Search');
INSERT INTO localstrings (keycode, langcode, stringvalue) VALUES ('jpcollaboration_STATISTIC_TITLE', 'en', 'Statistics');
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


INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_idea_find', 'jpcollaboration_idea_find', 'jpcollaboration', NULL, ' <#assign wp=JspTaglibs["/aps-core"]>
<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
<#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
<#assign jacms=JspTaglibs["/jacms-aps-core"]>
<#assign wpsa=JspTaglibs["/apsadmin-core"]>
<#assign wpsf=JspTaglibs["/apsadmin-form"]>
<#assign s=JspTaglibs["/struts-tags"]>
<div class="ibox float-e-margins">
    <div class="jpcrowdsourcing jpcrowdsourcing_idea_find">
        <div class="ibox-title">
            <h5><@wp.i18n key="jpcollaboration_TITLE_SEARCH" /></h5>
        </div>

        <@jpcrwsrc.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
        <@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar" />

        <div class="ibox-content">        
            <#if  (instanceVar)?? >
            <@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceVar}" listResult=false/>
            <#else>
            <@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_search_result" listResult=false/>
            </#if>
            <@jpcrwsrc.ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>
            <form action="<@wp.url page="listIdea_page.code" />" method="get">
                  <input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value=instanceVar/>
                <div class="form-group">
                    <label class="control-label" for="jpcrowdsourcing_search_for"><@wp.i18n key="jpcollaboration_SEARCH_FOR" /></label>
                    <input id="jpcrowdsourcing_search_for" name="ideaText" type="text" value="<#if (ideaText)?? > ${ideaText}</#if>" class="form-control" />
                </div>

                <#if (categoryInfoList?size > 0) >
                <div class="form-group">
                    <label class="control-label" for="jpcrowdsourcing_search_in">In</label>
                    <select id="jpcrowdsourcing_search_in" name="ideaTag"  class="form-control">
                        <option value="" <#if (param.ideaTag)?? > selected="selected" </#if>><@wp.i18n key="jpcollaboration_SEARCH_IN_ALL" /></option>
                        <#list categoryInfoList  as categoryInfo  >
                        <option value=${categoryInfo.category.code} <#if  (param.ideaTag)??  && param.ideaTag==categoryInfo.category.code> selected="selected" </#if>>
                                ${categoryInfo.title}
                    </option>
                    </#list>
                </select>
            </div>
            </#if>
            <p><input type="submit" value="<@wp.i18n key="jpcollaboration_SEARCH" />" class="btn btn-default pull-right" /></p>
            </p><br><br>
        </form>
    </div>
    ', 1);


    INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_fastEntryIdea', 'jpcollaboration_fastEntryIdea', 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
    <#assign wp=JspTaglibs["/aps-core"]>
    <#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
    <#assign jacms=JspTaglibs["/jacms-aps-core"]>
    <#assign s=JspTaglibs["/struts-tags"]>


    <div class="ibox float-e-margins">
        <div class="jpcrowdsourcing jpcrowdsourcing_fastEntryIdea">
            <div class="ibox-title">
                <h5><@wp.i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h5>
            </div>
            <div class="ibox-content">
                <@jpcrwsrc.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
                <@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar" />
                <@jpcrwsrc.pageWithWidget var="entryIdea_page" widgetTypeCode="jpcollaboration_entryIdea" listResult=false />

                <#if Session.currentUser != "guest">
                <form action="<@wp.url page="${entryIdea_page.code}" />" method="post" accept-charset="UTF-8">

                <#if instanceVar??>
                <input type="hidden" name="jpcrowdsourcing_fastInstanceCode" value="${instanceVar}" />
                </#if>
                <p>
                    <label for="jpcrowdsourcing_fastDescr" class="control-label"><@wp.i18n key="jpcollaboration_LABEL_DESCR" /></label>
                    <textarea rows="5" cols="40" name="jpcrowdsourcing_fastDescr" id="jpcrowdsourcing_fastDescr"  class="form-control"></textarea>
                </p>
                <p>
                    <input type="submit" value="<@wp.i18n key="jpcollaboration_SUBMIT_IDEA" />" class="btn btn-default pull-right" />
                </p><br><br>
                </form>
                <#else>
                <p class="alert alert-danger"><@wp.i18n key="jpcollaboration_DO_LOGIN" /></p>
                </#if>
            </div>
        </div>
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

    <div class="ibox float-e-margins">
        <div class="jpcrowdsourcing entryIdea">
            <div class="ibox-title">
                <h5><@wp.i18n key="jpcollaboration_NEW_IDEA_TITLE" /></h5>
            </div>	

            <div class="ibox-content">
                <form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/NewIdea/save.action" />" method="post"     accept-charset="UTF-8" >
                      <@s.if test="null != #parameters.entryIdea_form">
                      <@s.if test="hasActionErrors()">
                      <div class="alert alert-danger alert-dismissable">
                        <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                        <@wp.i18n key="ERRORS" />
                        <ul>
                            <@s.set var="actionErrorsVar"  value="actionErrors"/>
                            <#list actionErrorsVar as itemAcitonError>
                            ${itemAcitonError}
                            </#list>
                        </ul>
                    </div>
                    </@s.if>
                    <@s.if test="hasActionMessages()">
                    <div class="alert alert-danger alert-dismissable">
                        <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                        <@wp.i18n key="MESSAGES" />
                        <ul>
                            <@s.set var="actionMessagesVar"  value="actionMessages"/>
                            <#list actionMessagesVar as itemActionMessages>
                            ${itemActionMessages}
                            </#list>
                        </ul>
                    </div>
                    </@s.if>
                    <@s.if test="hasFieldErrors()">
                    <div class="alert alert-danger alert-dismissable">
                        <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                        <@wp.i18n key="ERRORS" />
                        <@s.set var="fieldErrorsVar"  value="fieldErrors"/>
                        <ul>
                            <@s.set var="fieldErrorsVar"  value="fieldErrors"/>
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


                    <div class="form-group">
                        <label for="idea_instanceCode" class="control-label"><@wp.i18n key="jpcollaboration_INSTANCE" /></label>
                        <div class="controls">
                            <@wpsf.select cssClass="form-control"  list="ideaInstances" name="idea.instanceCode" id="idea_instanceCode" listKey="code" 
                            listValue="code" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="idea_title" class="control-label"><@wp.i18n key="jpcollaboration_LABEL_TITLE" /></label>
                        <div class="controls">
                            <@wpsf.textfield name="idea.title" id="idea_title" value="%{idea.title}" cssClass="form-control" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="idea_descr" class="control-label"><@wp.i18n key="jpcollaboration_LABEL_DESCR" /></label>
                        <div class="controls">
                            <@wpsf.textarea name="idea.descr" id="idea_descr" value="%{idea.descr}" cssClass="form-control" cols="40" rows="3" />
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="" class="control-label"><@wp.i18n key="jpcollaboration_TAGS_TITLE" /></label>
                        <div class="input-append">
                            <#assign jquery_libraries>
                            <@sj.head jqueryui="true"/>
                            <script type="text/javascript"><!--//--><![CDATA[//><!--jQuery.noConflict();//-->< !]] ></script>
                            </#assign>
                            <@wp.headInfo type="JS_JQUERY" var="jquery_libraries" />
                            <@sj.autocompleter id="tag"  name="tag" cssClass="form-control"  list="%{tagsArray}" forceValidOption="false" />

                            <@s.set var="labelJoin"><@wp.i18n key="jpcollaboration_TAG_JOIN" /></@s.set>
                            <br>
                            <@wpsf.submit action="joinCategory" value="%{#labelJoin}" cssClass="btn btn-sm btn-info" id="join_tag"/>

                            <script type="text/javascript">//<![CDATA[//>
                                        jQuery("#join_tag").click(function (event) {
                                    var checktag = jQuery.isEmptyObject(jQuery("#tag_widget").val());
                                    if (checktag)
                                        event.preventDefault();
                                });//<!]]>
                            </script>
                        </div>
                    </div>


                    <@s.if test="!(null == tags || tags.size == 0)">
                    <div class="well">
                        <ul class="dd-list">
                            <@s.iterator value="tags" var="tag">
                            <@s.set value="getCategory(#tag)" var="currentCat" />
                            <@s.if test="null != #currentCat">
                            <li>
                                <@wpsf.hidden name="tags" value="%{#currentCat.code}" />
                                <@wpsa.actionParam action="removeCategory" var="removeTagAction"><@wpsa.actionSubParam name="tag" value="%{#currentCat.code}" /></@wpsa.actionParam>
                                <@s.set var="labelRemove"><@wp.i18n key="jpcollaboration_TAG_REMOVE" /></@s.set>
                                <span class="btn btn-success btn-sm ">
                                    <i class="fa fa-tag" aria-hidden="true"></i>&nbsp;&nbsp;
                                    <strong><@s.property value="#currentCat.title" /></strong>
                                    <@wpsf.submit action="%{#removeTagAction}" type="button" theme="simple" cssClass="btn btn-warning btn-xs"  title="%{#labelRemove}" >
                                    <i class="fa fa-times"></i>
                                    </@wpsf.submit>
                                </span>
                            </li>
                            </@s.if>
                            </@s.iterator>
                        </ul>
                    </div>
                    </@s.if>
                    <div>
                        <@s.set var="labelSubmit"><@wp.i18n key="jpcollaboration_SUBMIT_IDEA" /></@s.set>
                        <@wpsf.submit  value="%{#labelSubmit}" cssClass="btn btn-sm btn-primary pull-right" />
                    </div><br><br>
                </form>
            </div>
        </div>
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
    <div class="ibox float-e-margins">
<@s.set var="idea" value="%{getIdea(ideaId)}" />
<@s.if test="#idea!=null">
    <#assign instanceCodeVar=idea.instanceCode>
    <@jpcrwsrc.pageWithWidget var="ideaList_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue="${instanceCodeVar}" listResult=false/>
    <@wp.freemarkerTemplateParameter var="ideaList_page" valueName="ideaList_page"/>
    <@wp.url page=ideaList_page.code var="listPage"/>

    
   <div class="ibox-title">
                <h5><@s.property value="#idea.title" /></h5>
            </div>

 <div class="ibox-content">
    <#if (idea.tags?? && idea.tags?size > 0 )>
        <p>
            <i class="fa fa-tag" aria-hidden="true"></i>&#32;<@wp.i18n key="jpcollaboration_TAG" />:&#32;
            <#list idea.tags as cat>
            <#assign catVar=cat>
            <@wp.url var="ideaList_pageUrl" page="${ideaList_page.code}"><@wp.urlPar name="ideaTag" >${cat}</@wp.urlPar></@wp.url>
            <a href="${ideaList_pageUrl}">${cat}</a><@s.if test="!#status.last">,&#32;</@s.if>
            </#list>
        </p>
    </#if>

 <p class="p-xs bg-muted">
<@s.property value=''#idea.descr.trim().replaceAll("\r", "").replaceAll("\n\n+", "</p><p>").replaceAll("\n", "</p><p>")'' escapeHtml=false />
</p>
    
<p><i class="fa fa-info-circle" aria-hidden="true"></i>&#32;
<@wp.i18n key="jpcollaboration_IDEA_PUBBL" />&#32;
<span class="font-italic"><@s.property value="#idea.username"/>&#32;</strong>
<strong title="<@s.date name="#idea.pubDate" />">
<@s.date name="#idea.pubDate" nice=true/>
</strong>
</p>

        <@s.if test="null == #parameters.comment_form && null != #parameters.userAction">
            <@s.if test="hasActionErrors()">
            <div class="alert alert-error">
                 <div class="alert alert-danger alert-dismissable">
                            <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                <@wp.i18n key="ERRORS" /> 
                <ul>
                    <@s.set var="actionErrorsVar"  value="actionErrors"/>
                    <#list actionErrorsVar as itemAcitonError>
                        ${itemAcitonError}
                    </#list>
                </ul>
            </div>
        </@s.if>
        <@s.if test="hasActionMessages()">
             <div class="alert alert-danger alert-dismissable">
                 <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                  <@wp.i18n key="MESSAGES" /> 
                <ul>
                    <@s.set var="actionMessagesVar"  value="actionMessages"/>
                    <#list actionMessagesVar as itemActionMessages>
                        ${itemActionMessages}
                    </#list>
                </ul>
            </div>
        </@s.if>
        <@s.if test="hasFieldErrors()">
             <div class="alert alert-danger alert-dismissable">
                  <button aria-hidden="true" data-dismiss="alert" class="close" type="button">×</button>
                 <@wp.i18n key="ERRORS" /> 
                <ul>
                <@s.set var="fieldErrorsVar"  value="fieldErrors"/>
                <#list fieldErrorsVar?values as it>
                    <#list it as var>
                    <li>${var}</li>
                    </#list>
                </#list>
                </ul>
            </div>
        </@s.if>
        </@s.if>

        <form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/ideaLike.action"/>" method="post" class="form-inline display-inline">
            <p class="noscreen">
                <@wpsf.hidden name="ideaId" value="%{ideaId}" />
                <input type="hidden" name="userAction" value="like" />
            </p>

            <@s.token name="listIdea" />
            <@s.set var="labelSubmit"><@wp.i18n key="jpcollaboration_IDEA_LIKE_IT" escapeXml=false /></@s.set>
            <@wpsf.submit value="%{#labelSubmit}" cssClass="btn btn-xs btn-success" />
            <span class="badge badge-success" title="<@s.property value="#idea.votePositive" /> 
            <@wp.i18n key="jpcollaboration_IDEA_VOTE_AGREE" />">&#32;
            <i class="fa fa-thumbs-up" aria-hidden="true"></i>&#32;
            <@s.property value="#idea.votePositive" />
            </span>

        </form>

        <form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/ideaUnlike.action"/>" method="post" class="form-inline display-inline">
            <p class="noscreen">
                <@wpsf.hidden name="ideaId" value="%{ideaId}" />
                <input type="hidden" name="userAction" value="dislike" />
            </p>

            <@s.token name="listIdea" />
            <@s.set var="labelSubmit"><@wp.i18n key="jpcollaboration_IDEA_NOT_LIKE_IT" escapeXml=false /></@s.set>
            <@wpsf.submit value="%{#labelSubmit}" cssClass="btn btn-xs btn-danger" />
            <span class="badge badge-danger" title="<@s.property value="#idea.voteNegative" /> 
            <@wp.i18n key="jpcollaboration_IDEA_VOTE_DISAGREE" />">&#32;
            <i class="fa fa-thumbs-down" aria-hidden="true"></i>&#32;
            <@s.property value="#idea.voteNegative" />&#32;
            </span>
        </form>


        <@s.set value="#idea.comments[3]" var="currentComments" />
         <legend  style="margin: 20px 0 0 0;" id="jpcrdsrc_comments_<@s.property value="#idea.id" />">
<@wp.i18n key="jpcollaboration_IDEA_COMMENTS" />
</legend>
        <p style="margin: 20px 0 0 0;">
            <i class="icon-comment"></i>&#32;
            <@s.if test="null == #currentComments || #currentComments.size == 0">0</@s.if>
            <@s.else><@s.property value="#currentComments.size" /></@s.else>&#32;
            <@wp.i18n key="jpcollaboration_IDEA_COMMENTS" />
        </p>


    <#if Session.currentUser != ''guest''>
    <form action="<@wp.action path="/ExtStr2/do/collaboration/FrontEnd/Idea/saveComment.action"/>" method="post" accept-charset="UTF-8">
    <@s.if test="null != #parameters.comment_form">
    <@s.if test="hasActionErrors()">
             <div class="alert alert-danger">
                     <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                         <@wp.i18n key="ERRORS" />
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
                <@wp.i18n key="MESSAGES" />
                <ul>
                    <@s.set var="actionMessagesVar"  value="actionMessages"/>
                    <#list actionMessagesVar as itemActionMessages>
                        ${itemActionMessages}
                    </#list>
                </ul>
            </div>
        </@s.if>
        <@s.if test="hasFieldErrors()">
            <div class="alert alert-danger alert-dismissable">
               <button type="button" class="close" data-dismiss="alert"><i class="icon-remove"></i></button>
                 <@wp.i18n key="ERRORS" />
                <ul>
                <@s.set var="fieldErrorsVar"  value="fieldErrors"/>
                    <#list fieldErrorsVar?values as it>
                        <#list it as var>
                            <li>${var}</li>
                        </#list>
                    </#list>
                </ul>
            </div>
        </@s.if>
        </@s.if>

            <p class="noscreen">
                        <@wpsf.hidden name="comment_form" value="comment_form" />
                        <@wpsf.hidden name="ideaId" value="%{ideaId}" />
                        <@wpsf.hidden name="ideaComment.id" value="%{ideaComment.id}" />
                        <@wpsf.hidden name="ideaComment.ideaId" value="%{ideaId}" />
                    </p>

                    <@s.token name="saveComment"/>
                    <p>
                         <label class="control-label" for="ideaComment_comment"><@wp.i18n key="jpcollaboration_COMMENT" /></label>
                        <@wpsf.textarea id="ideaComment_comment" name="ideaComment.comment" cols="40" rows="5" cssClass="form-control" />
                    </p>

                    <@s.set var="labelSave"><@wp.i18n key="jpcollaboration_SAVE_COMMENT" escapeXml=false /></@s.set>
                    <div>
                  <@wpsf.submit value="%{#labelSave}" cssClass="btn btn-success pull-right" /></div>
                </form>

    <#else>
        <p class="alert alert-warning"><@wp.i18n key="jpcollaboration_DO_LOGIN" /></p>
    </#if>

        <@s.if test="#currentComments.size > 0">
           <div style="margin: 60px 0 0 0">
            <@s.iterator value="#currentComments" var="currentCommentId">
                <@s.set var="currentComment" value="%{getComment(#currentCommentId)}" />
                <p>
                        <i class="fa fa-comment-o" aria-hidden="true"></i>&#32;
                        <strong title="<@s.date name="#currentComment.creationDate" />">
                           <@s.date name="#currentComment.creationDate" nice=true />
                        </strong>&#32;
                        <em><@s.property value="#currentComment.username" /></em>&#32;
                        <@wp.i18n key="jpcollaboration_COMMENT_SAID" />:
                </p>
                <blockquote>
                    <p>
                        <@s.property value=''#currentComment.comment.trim().replaceAll("\r", "").replaceAll("\n\n+", "</p><p>").replaceAll("\n", "</p><p>")'' escapeHtml=false />
                    </p>
                </blockquote>
            </@s.iterator>
          </div>
        </@s.if>

        <form action="${listPage}" method="post" class="form-inline display-inline">
            <@s.set var="labelList"><@wp.i18n key="jpcollaboration_BACK_TO_LIST" escapeXml=false /></@s.set>
            <p>
             <@wpsf.submit value="%{#labelList}" cssClass="btn btn-default" />
            </p>
        </form>
    </@s.if>

    <@s.else>
        <div class="alert alert-danger">
            <@wp.i18n key="jpcollaboration_IDEA_DETAILS" />
            <p>
                <@wp.i18n key="jpcollaboration_IDEA_NOT_FOUND" />
            </p>
        </div>
    </@s.else>
        </div>
    </div>
</div>', 1);
    INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_statistics', 'jpcollaboration_statistics', 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
    <#assign s=JspTaglibs["/struts-tags"]>
    <#assign wp=JspTaglibs["/aps-core"]>
    <#assign wpcs=JspTaglibs["/jpcrowdsourcing-aps-core"]>
    <#assign jacms=JspTaglibs["/jacms-aps-core"]>

    <div class="ibox float-e-margins">
        <div class="jpcrowdsourcing jpcrowdsourcing_statistics">
            <div class="ibox-title">
                <h5><@wp.i18n key="jpcollaboration_STATISTIC_TITLE"/></h5>
            </div>
            <div class="ibox-content">

                <@wpcs.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
                <@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar"/>

                <@wpcs.statistic var="stats" instanceCode=instanceVar/>
                <#if (stats??)>
                <ul class="unstyled">
                    <li class="text-right">
                        <@wp.i18n key="jpcollaboration_PROPOSED_IDEAS" />&#32;<span class="badge badge-info">
                            ${stats.ideas}
                        </span>
                    </li>
                    <li class="text-right">
                        <@wp.i18n key="jpcollaboration_VOTES" />&#32;
                        <span class="badge badge-info">
                            ${stats.votes}
                        </span>
                    </li>
                    <li class="text-right">
                        <@wp.i18n key="jpcollaboration_IDEA_COMMENTS" />&#32;
                        <span class="badge badge-info">
                            ${stats.comments}
                        </span>
                    </li>
                    <li class="text-right">
                        <@wp.i18n key="jpcollaboration_USERS" />&#32;
                        <span class="badge badge-info">
                            ${stats.users}
                        </span>
                    </li>
                </ul>
                <#else>
                <p class="alert alert-warning">
                    <@wp.i18n key="jpcollaboration_STATISTICS_EMPTY" />
                </p>
                </#if>
            </div>
        </div>
    </div>', 1);
    INSERT INTO guifragment (code, widgettypecode, plugincode, gui, defaultgui, locked) VALUES ('collaboration_idea_tags', 'jpcollaboration_idea_tags', 'jpcollaboration', NULL, '<#assign c=JspTaglibs["http://java.sun.com/jsp/jstl/core"]>
    <#assign s=JspTaglibs["/struts-tags"]>
    <#assign wp=JspTaglibs["/aps-core"]>
    <#assign jpcrwsrc=JspTaglibs["/jpcrowdsourcing-aps-core"]>
    <#assign jacms=JspTaglibs["/jacms-aps-core"]>

    <div class="jpcrowdsourcing jpcrowdsourcing_idea_tags">
        <div class="ibox float-e-margins">
            <div class="ibox-title">
                <h5><@wp.i18n key="jpcollaboration_TAGS_TITLE" /></h5>
            </div>
            <div class="ibox-content">
                <@jpcrwsrc.currentPageWidget param="config" configParam="instanceCode" widget="jpcollaboration_ideaInstance" var="instanceVar"/>
                <@wp.freemarkerTemplateParameter var="instanceVar" valueName="instanceVar"/>
                <@jpcrwsrc.ideaTagList var="categoryInfoList" onlyLeaf="false" categoryFilterType="tag"/>
                <@jpcrwsrc.pageWithWidget var="listIdea_page" widgetTypeCode="jpcollaboration_ideaInstance" configParam="instanceCode" configValue=instanceVar listResult=false/>
                <#if (categoryInfoList?? && categoryInfoList?size >0)>
                <#if (RequestParameters.ideaTag??) >
                <#assign ideaTagVar=RequestParameters.ideaTag>
                <#else>
                <#assign ideaTagVar>
                </#assign>
                </#if>
                <ul class="nav nav-pills">
                    <li<#if (ideaTagVar?? && ideaTagVar== "")  > class="active"</#if>>
                        <a href="<@wp.url  page="listIdea_page.code" />">
                       <@wp.i18n key="jpcollaboration_TAGS_ALL" />
                    </a>
                </li>
                <#list categoryInfoList as categoryInfo>
                <#assign categoryVar=categoryInfo.category.code>
                <@wp.url var="listIdea_pageUrl" page="listIdea_page.code"><@wp.urlPar name="ideaTag" >${categoryInfo.category.code}</@wp.urlPar></@wp.url>
                <li<#if (ideaTagVar?? && ideaTagVar==categoryVar)  >
                    class="active"</#if>>
                    <a href="${listIdea_pageUrl}">
                        ${categoryInfo.title}
                    </a>
                </li>
                </#list>
            </ul>
            <#else>
            <p class="alert alert-danger"><@wp.i18n key="jpcollaboration_TAGS_EMPTY" /></p>
            </#if>
        </div>
    </div>
</div>
', 1);
