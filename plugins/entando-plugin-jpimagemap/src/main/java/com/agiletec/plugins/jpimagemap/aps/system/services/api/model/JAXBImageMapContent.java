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
package com.agiletec.plugins.jpimagemap.aps.system.services.api.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.agiletec.aps.system.common.entity.model.attribute.JAXBHypertextAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.JAXBListAttribute;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.SymbolicLink;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBLinkValue;
import com.agiletec.plugins.jacms.aps.system.services.content.model.extraAttribute.JAXBResourceValue;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model.JAXBAreaValue;
import com.agiletec.plugins.jpimagemap.aps.system.services.content.model.attribute.model.JAXBImageMapValue;

import org.entando.entando.plugins.jacms.aps.system.services.api.model.JAXBContent;

/**
 * @author E.Santoboni
 */
@XmlRootElement(name = "content")
@XmlSeeAlso({ArrayList.class, HashMap.class, JAXBImageMapValue.class, 
	JAXBAreaValue.class, JAXBHypertextAttribute.class, JAXBListAttribute.class, JAXBResourceValue.class, JAXBLinkValue.class, SymbolicLink.class})
public class JAXBImageMapContent extends JAXBContent {
    
    public JAXBImageMapContent() {
        super();
    }
    
    public JAXBImageMapContent(Content mainContent, String langCode) {
        super(mainContent, langCode);
    }
    
}