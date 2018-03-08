/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author own_strong
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class KiaBpmConfigFactory {

    @XmlElement(name = "kieBpmConfigeMap")
    private HashMap<String, KieBpmConfig> kieBpmConfigeMap = new HashMap();

    public KieBpmConfig getKiaBpmConfig(String _hostname) {
        KieBpmConfig kieBpmConfig = this.getKieBpmConfigeMap().get(_hostname);
        return kieBpmConfig;
    }

    public void addKiaBpmConfig(KieBpmConfig _kieBpmConfig) {

        this.getKieBpmConfigeMap().put(_kieBpmConfig.getHostname(), _kieBpmConfig);

    }

    public KieBpmConfig getFirstKiaBpmConfig() {
        
        HashMap.Entry<String,KieBpmConfig> entry= this.getKieBpmConfigeMap().entrySet().iterator().next();
        return entry.getValue();
    }

//    public KiaBpmConfigFactory initiateKiaBpmConfigFactory() {
//        return new KiaBpmConfigFactory();
//    }

    public HashMap<String, KieBpmConfig> getKieBpmConfigeMap() {
        return kieBpmConfigeMap;
    }

    public void setKieBpmConfigeMap(HashMap<String, KieBpmConfig> _kieBpmConfigeMap) {
        this.kieBpmConfigeMap = _kieBpmConfigeMap;
    }

}
