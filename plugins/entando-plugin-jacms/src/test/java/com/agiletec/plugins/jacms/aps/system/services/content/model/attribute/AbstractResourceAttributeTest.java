/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.content.model.attribute;

import com.agiletec.plugins.jacms.aps.system.services.resource.model.ImageResource;
import java.util.List;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 * @author E.Santoboni
 */
public class AbstractResourceAttributeTest {

    @Mock
    public ImageResource resource;

    //@Mock
    //public Map<String, String> textMap;
    @InjectMocks
    private ImageAttribute imageAttribute;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.imageAttribute.setType("Image");
        this.imageAttribute.setName("ImageKey");
        imageAttribute.setText("Text EN", "en");
        imageAttribute.setText("Text IT", "it");
        when(resource.getId()).thenReturn("idResource");
        when(resource.getType()).thenReturn("Image");
        imageAttribute.getResources().put("en", this.resource);
    }

    @Test
    public void getJDOMElement_1() throws Exception {
        this.imageAttribute.setMetadata("metadata_key_1", "en", "metadata_value_1");
        this.imageAttribute.setMetadata("metadata_key_2", "en", "metadata_value_2");
        this.testGetJDOMElement(true);
    }

    @Test
    public void getJDOMElement_2() throws Exception {
        this.testGetJDOMElement(false);
    }

    private void testGetJDOMElement(boolean hasMetadata) throws Exception {
        Assert.assertNotNull(imageAttribute.getResources());
        Element element = this.imageAttribute.getJDOMElement();
        Assert.assertNotNull(element);
        Assert.assertEquals("Image", element.getAttributeValue("attributetype"));
        Assert.assertEquals("ImageKey", element.getAttributeValue("name"));
        List<Element> resourceElements = element.getChildren("resource");
        Assert.assertNotNull(resourceElements);
        Assert.assertEquals(1, resourceElements.size());
        Element resourceElement = resourceElements.get(0);
        Assert.assertEquals("Image", resourceElement.getAttributeValue("resourcetype"));
        Assert.assertEquals("idResource", resourceElement.getAttributeValue("id"));
        List<Element> textElements = element.getChildren("text");
        Assert.assertEquals(2, textElements.size());
        for (Element textElement : textElements) {
            String langCode = textElement.getAttributeValue("lang");
            String text = textElement.getText();
            if (langCode.equals("en")) {
                Assert.assertEquals("Text EN", text);
            } else if (langCode.equals("it")) {
                Assert.assertEquals("Text IT", text);
            } else {
                Assert.fail();
            }
        }
        Element metadatasElement = element.getChild("metadatas");
        if (hasMetadata) {
            Assert.assertNotNull(metadatasElement);
            List<Element> metadataElements = metadatasElement.getChildren("metadata");
            Assert.assertEquals(2, metadataElements.size());
            for (Element metadataElement : metadataElements) {
                Assert.assertEquals("en", metadataElement.getAttributeValue("lang"));
                String key = metadataElement.getAttributeValue("key");
                String value = metadataElement.getText();
                if (key.equals("metadata_key_1")) {
                    Assert.assertEquals("metadata_value_1", value);
                } else if (key.equals("metadata_key_2")) {
                    Assert.assertEquals("metadata_value_2", value);
                } else {
                    Assert.fail();
                }
            }
        } else {
            Assert.assertNull(metadatasElement);
        }
    }

}
