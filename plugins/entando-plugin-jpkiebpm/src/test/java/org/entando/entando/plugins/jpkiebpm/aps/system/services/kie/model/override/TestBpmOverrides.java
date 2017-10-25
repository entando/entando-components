/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override;

import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessProperty;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;

/**
 *
 * @author Entando
 */
public class TestBpmOverrides extends TestCase {

    public void testBpmDropDownOverride() throws Throwable {
        final String VAL1 = "Entando";
        final String VAL2 = "4.3";
        DropDownOverride dd = new DropDownOverride();

        String a[] = {VAL1, VAL2};
        dd.setValues(Arrays.asList(a));

        String xml = dd.toXML();
        assertNotNull(xml);
//        System.out.println("XML:\n" + xml);
        DropDownOverride ver = (DropDownOverride) JAXBHelper
                .unmarshall(xml, DropDownOverride.class, true, false);
        assertNotNull(ver);
        assertNotSame(ver, dd);
        assertEquals(DropDownOverride.OVERRIDE_TYPE_NAME,
                ver.getType());
        assertNotNull(ver.getValues());
        assertFalse(ver.getValues().isEmpty());
        assertEquals(2,
                ver.getValues().size());
        for (String value: ver.getValues()) {
            assertTrue(
                    value.equals(VAL1)
                    || value.equals(VAL2)
            );
        }
        KieProcessProperty kieProp = dd.toKieProperty();
        assertEquals(DropDownOverride.OVERRIDE_TYPE_NAME,
                kieProp.getName());
        assertEquals(VAL1 + "," + VAL2,
                kieProp.getValue());
    }

    public void testBpmPlaceHolderOverride() throws Throwable {
        final String PLACEHOLDER = "change me!";
        PlaceHolderOverride ph = new PlaceHolderOverride();

        ph.setPlaceHolder(PLACEHOLDER);
        String xml = ph.toXML();
//        System.out.println("XML:\n" + xml);
        PlaceHolderOverride ver = (PlaceHolderOverride) JAXBHelper
                .unmarshall(xml, PlaceHolderOverride.class, true, false);
        assertNotNull(ver);
        assertNotSame(ver, ph);
        assertEquals(PLACEHOLDER, ver.getPlaceHolder());
        assertEquals(PlaceHolderOverride.OVERRIDE_TYPE_NAME,
                ver.getType());

        KieProcessProperty kieProp = ph.toKieProperty();
        assertEquals(PlaceHolderOverride.OVERRIDE_TYPE_NAME,
                kieProp.getName());
        assertEquals(PLACEHOLDER,
                kieProp.getValue());
    }

    public void testBpmpDefaultValueOverride() throws Throwable {
        final String DEFAULTVALUE = "default!";
        DefaultValueOverride df = new DefaultValueOverride();

        df.setDefaultValue(DEFAULTVALUE);
        String xml = df.toXML();
//        System.out.println("XML:\n" + xml);
        DefaultValueOverride ver = (DefaultValueOverride) JAXBHelper
                .unmarshall(xml, DefaultValueOverride.class, true, false);
        assertNotNull(ver);
        assertNotSame(ver, df);
        assertEquals(DEFAULTVALUE, ver.getDefaultValue());
        assertEquals(DefaultValueOverride.OVERRIDE_TYPE_NAME,
                ver.getType());
         KieProcessProperty kieProp = df.toKieProperty();
        assertEquals(DefaultValueOverride.OVERRIDE_TYPE_NAME,
                kieProp.getName());
        assertEquals(DEFAULTVALUE,
                kieProp.getValue());
    }


    public void testOverrideList() throws Throwable {
        final String DEFAULTVALUE = "default value!";
        DefaultValueOverride df = new DefaultValueOverride();

        df.setDefaultValue(DEFAULTVALUE);
        final String PLACEHOLDER = "change me asap!";
        PlaceHolderOverride ph = new PlaceHolderOverride();

        ph.setPlaceHolder(PLACEHOLDER);
        final String VAL1 = "Entando";
        final String VAL2 = "4.3 override";
        DropDownOverride dd = new DropDownOverride();

        String values[] = {VAL1, VAL2};
        dd.setValues(Arrays.asList(values));
        OverrideList ol = new OverrideList();
        ol.addOverride(df);
        ol.addOverride(ph);
        ol.addOverride(dd);

        String xml = JAXBHelper.marshall(ol, true, false);
//        System.out.println(">>>\n" + xml);

        OverrideList ver = (OverrideList) JAXBHelper
                .unmarshall(xml, OverrideList.class, true, false);
        assertNotNull(ver);
        assertFalse(ver.getList().isEmpty());
        assertEquals(3, ver.getList().size());

        assertTrue(ver.getList().get(0) instanceof DefaultValueOverride);
        assertTrue(ver.getList().get(1) instanceof PlaceHolderOverride);
        assertTrue(ver.getList().get(2) instanceof DropDownOverride);

        DefaultValueOverride elem1 = (DefaultValueOverride) ver.getList().get(0);
        assertEquals(DEFAULTVALUE,
                elem1.getDefaultValue());
        PlaceHolderOverride elem2 = (PlaceHolderOverride) ver.getList().get(1);
        assertEquals(PLACEHOLDER,
                elem2.getPlaceHolder());
        DropDownOverride elem3 = (DropDownOverride) ver.getList().get(2);
        List<String> vals = elem3.getValues();
        assertNotNull(vals);
        assertFalse(vals.isEmpty());
        for (String value: vals) {
            assertTrue(
                    value.equals(VAL1)
                    || value.equals(VAL2)
            );
        }

    }



}
