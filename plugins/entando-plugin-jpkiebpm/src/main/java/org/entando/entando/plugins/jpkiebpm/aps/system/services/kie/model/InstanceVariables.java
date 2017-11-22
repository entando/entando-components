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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model;

import java.util.HashMap;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.InstanceVariables.VariableMap;

/**
 * AAARGH! MOXy (as well as JAXB) don't work out-of-the-box with maps!
 *
 * @author Entando
 */
public class InstanceVariables extends XmlAdapter<VariableMap, HashMap<String, Object>> {

    @XmlRootElement
    public static class VariableMap {
        public String name;
        public Long clientid;
    }

    @Override
    public HashMap<String, Object> unmarshal(VariableMap p) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", p.name);
        map.put("clientid", p.clientid);
        return map;
    }


    @Override
    public VariableMap marshal(HashMap<String, Object> v) throws Exception {
        VariableMap p = new VariableMap();
        p.name = (String) v.get("name");
        p.clientid = (Long) v.get("clientid");
        return p;
    }
}


