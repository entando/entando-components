/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpnewsletter.apsadmin.config;

import java.util.List;

import com.agiletec.apsadmin.system.BaseAction;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SmallContentType;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.INewsletterManager;
import com.agiletec.plugins.jpnewsletter.aps.system.services.newsletter.model.NewsletterConfig;

/**
 * @author E.Santoboni
 */
public class AbstractNewsletterConfigAction extends BaseAction {
	
	public NewsletterConfig getNewsletterConfig() {
		return (NewsletterConfig) this.getRequest().getSession().getAttribute(NEWSLETTER_CONFIG_SESSION_PARAM);
	}
	public void setNewsletterConfig(NewsletterConfig newsletterConfig) {
		this.getRequest().getSession().setAttribute(NEWSLETTER_CONFIG_SESSION_PARAM, newsletterConfig);
	}
	
	public SmallContentType getSmallContentType(String typeCode) {
		return this.getContentManager().getSmallContentTypesMap().get(typeCode);
	}
	
	public ContentModel getContentModel(int modelId) {
		return this.getContentModelManager().getContentModel(modelId);
	}
	
	public List<ContentModel> getContentModelByType(String typeCode) {
		return this.getContentModelManager().getModelsForContentType(typeCode);
	}
	
	protected INewsletterManager getNewsletterManager() {
		return _newsletterManager;
	}
	public void setNewsletterManager(INewsletterManager newsletterManager) {
		this._newsletterManager = newsletterManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}
	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}
	
	private INewsletterManager _newsletterManager;
	private IContentManager _contentManager;
	private IContentModelManager _contentModelManager;
	
	public static String NEWSLETTER_CONFIG_SESSION_PARAM = "newsletterConfig_sessionParam";
	
	/*
	<newsletterConfig>
	<scheduler active="true" onlyOwner="false" delayHours="24" start="27/08/2009 11:08" />
	<subscriptions allAttributeName="NewsletterAllContents" >
		<descr>
			mappa delle corrispondenze tra attributo buleano 
			''sottoscrizione categoria newsletter'' di profilo utente 
			e categoria di contenuto/tematismo-newsletter
		</descr>
		<subscription categoryCode="diritto_civile" attributeName="NewsletterDirittoCivile" />
		<subscription categoryCode="diritto_penale" attributeName="NewsletterDirittoPenale" />
	</subscriptions>
	<contentTypes>
		<contentType code="NWS" defaultModel="35" htmlModel="36" />
		<contentType code="EVN" defaultModel="57" htmlModel="56" />
		<contentType code="BND" defaultModel="72" htmlModel="73" />
	</contentTypes>
	<mail alsoHtml="true" senderCode="CODE1" mailAttrName="email" >
		<subject><![CDATA[Newsletter]]></subject>
		<htmlHeader><![CDATA[<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="it">
<head></head>
<body><h1>Newsletter</h1><p>Ciao, <br />Ecco le ultime novità:</p>]]></htmlHeader>
		<htmlFooter><![CDATA[<br /><br />Buona lettura, <br /></body></html>]]></htmlFooter>
		<htmlSeparator><![CDATA[<br /><br /><hr /><br /><br />]]></htmlSeparator>
		<textHeader><![CDATA[Ecco le ultime novità: 


###########################################################################


]]></textHeader><textFooter><![CDATA[

###########################################################################

Buona lettura.]]></textFooter>
		<textSeparator><![CDATA[

###########################################################################


]]></textSeparator>
	</mail>
</newsletterConfig>
	*/
	
	
}