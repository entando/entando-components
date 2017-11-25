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

    /*



    11"country" : "USA",
      "" : 1506590295001,

      "" : "John",
      "" : null,
      "" : null,
      "" : null,
      "" : null,
      "" : "William",

      "" : "Consultant",
      "" : null,
      "" : "n.puddu@entando.com",
      "" : "2.0"
*/


    @XmlRootElement
    public static class VariableMap {
        public String name;
        public Long clientid;
        public String pname;
        public String creditscore;
        public String phonenumber;
        public String type;
        public Long dateofbirth;
        public String ssn;
        public String surname;
        public String relationship;
        public String bic;
        public String email;
        public String status;
    }

    @Override
    public HashMap<String, Object> unmarshal(VariableMap p) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", p.name);
        map.put("clientid", p.clientid);
        map.put("pname", p.pname);
        map.put("creditscore", p.creditscore);
        map.put("phonenumber", p.phonenumber);
        map.put("type", p.type);
        map.put("dateofbirth", p.dateofbirth);
        map.put("ssn", p.ssn);
        map.put("surname", p.surname);
        map.put("relationship", p.relationship);
        map.put("bic", p.bic);
        map.put("email", p.email);
        map.put("status", p.status);

        return map;
    }


    @Override
    public VariableMap marshal(HashMap<String, Object> v) throws Exception {
        VariableMap p = new VariableMap();
        p.name = (String) v.get("name");
        p.clientid = (Long) v.get("clientid");
        p.pname = (String) v.get("pname");
        p.creditscore = (String) v.get("creditscore");
        p.phonenumber = (String) v.get("phonenumber");
        p.type = (String) v.get("type");
        p.dateofbirth = (Long) v.get("dateofbirth");
        p.ssn = (String) v.get("ssn");
        p.surname = (String) v.get("surname");
        p.relationship = (String) v.get("relationship");
        p.bic = (String) v.get("bic");
        p.email = (String) v.get("email");
        p.status = (String) v.get("status");

        return p;
    }
}


