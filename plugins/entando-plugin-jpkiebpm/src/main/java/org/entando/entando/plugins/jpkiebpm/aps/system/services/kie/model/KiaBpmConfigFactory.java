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

    public KieBpmConfig getKiaBpmConfig(String hostname) {
        KieBpmConfig kieBpmConfig = this.getKieBpmConfigeMap().get(hostname);
        return kieBpmConfig;
    }

    public void addKiaBpmConfig(KieBpmConfig kieBpmConfig) {

        this.getKieBpmConfigeMap().put(kieBpmConfig.getName(), kieBpmConfig);

    }

    public KieBpmConfig getFirstKiaBpmConfig() {
        /*
         Directly referencing HashMap.Entry (my conf is JDK 1.8u161 (macos)) will cause a compilation error like:
         java.util.HashMap.Entry is not public in java.util.HashMap

        HashMap.Entry<String,KieBpmConfig> entry= this.getKieBpmConfigeMap().entrySet().iterator().next();
        return entry.getValue();
*/
        return this.getKieBpmConfigeMap().entrySet().iterator().next().getValue();
    }

    public HashMap<String, KieBpmConfig> getKieBpmConfigeMap() {
        return kieBpmConfigeMap;
    }

    public void setKieBpmConfigeMap(HashMap<String, KieBpmConfig> _kieBpmConfigeMap) {
        this.kieBpmConfigeMap = _kieBpmConfigeMap;
    }

}
