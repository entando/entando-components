/*
 * Copyright 2016-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jprestapi.aps.core.helper;


import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.eclipse.persistence.jaxb.JAXBContextProperties;


/**
 *
 * @author entando
 */
public class JAXBHelper {

  /**
   * Unmarshal the entity given its representation
   * @param source streamSource object describing the entity
   * @param expected the expected result class
   * @param hasRoot
   * @param isJson
   * @return
   * @throws Throwable
   */
  public static Object unmarshall(StreamSource source, Class<?> expected, boolean hasRoot, boolean isJson) throws Throwable {
    Object obj = null;
    JAXBContext jaxbContext = JAXBContext.newInstance(expected);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

    if (null != source && null != expected) {
      // JSON
      if (isJson) {
        jaxbUnmarshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT,
                hasRoot);
        jaxbUnmarshaller.setProperty(JAXBContextProperties.MEDIA_TYPE,
                "application/json");
        obj = jaxbUnmarshaller.unmarshal(source, expected).getValue();
      } else {
        // XML
        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_NAMESPACE_AWARE, false);
        XMLStreamReader xsr = xif.createXMLStreamReader(source);

         obj = jaxbUnmarshaller.unmarshal(xsr);
      }
    }
    return obj;
  }

  /**
   * Unmarshal the entity given its representation
   * @param source the string describing the entity
   * @param expected
   * @param hasRoot
   * @param isJson
   * @return
   * @throws Throwable
   */
  public static Object unmarshall(String source, Class<?> expected, boolean hasRoot, boolean isJson) throws Throwable {
    StringReader sr = new StringReader(source);
    StreamSource ss = new StreamSource(sr);

    return unmarshall(ss, expected, hasRoot, isJson);
  }

  /**
   * Serialize the given object in the desired output format
   * @param object
   * @param hasRoot
   * @param isJson
   * @return
   * @throws Throwable
   */
  public static String marshall(Object object, boolean hasRoot, boolean isJson) throws Throwable {
    String result = null;

    if (null != object) {
      JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
      Marshaller marshaller = jaxbContext.createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
      if (isJson) {
        marshaller.setProperty(JAXBContextProperties.MEDIA_TYPE, "application/json");
      }
      marshaller.setProperty(JAXBContextProperties.JSON_INCLUDE_ROOT, hasRoot);
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      marshaller.marshal(object, baos);
      result = baos.toString();
//      baos.close();
    }
    return result;
  }

}
