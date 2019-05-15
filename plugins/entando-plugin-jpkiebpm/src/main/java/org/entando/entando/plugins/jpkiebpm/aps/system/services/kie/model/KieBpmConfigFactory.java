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
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "kiaBpmConfigFactory")
@XmlAccessorType(XmlAccessType.FIELD)
public class KieBpmConfigFactory {

    @XmlElement(name = "kieBpmConfigeMap")
    private Map<String, KieBpmConfig> kieBpmConfigMap = new HashMap<>();

    public KieBpmConfig getKieBpmConfig(String hostname) {
        KieBpmConfig kieBpmConfig = this.getKieBpmConfigMap().get(hostname);
        return kieBpmConfig;
    }

    public KieBpmConfigFactory() {
    }

    public KieBpmConfigFactory(KieBpmConfigFactory other) {
        other.kieBpmConfigMap.entrySet().forEach(entry -> {
            this.kieBpmConfigMap.put(entry.getKey(), new KieBpmConfig(entry.getValue()));
        });
    }

    public void addKieBpmConfig(KieBpmConfig kieBpmConfig) {
        this.getKieBpmConfigMap().put(kieBpmConfig.getId(), kieBpmConfig);
    }

    public void removeKieBpmConfig(String kieId) {
        this.getKieBpmConfigMap().remove(kieId);
    }

    public KieBpmConfig getFirstKieBpmConfig() {
        return this.getKieBpmConfigMap().entrySet().iterator().next().getValue();
    }

    public Map<String, KieBpmConfig> getKieBpmConfigMap() {
        return kieBpmConfigMap;
    }

    public void setKieBpmConfigMap(Map<String, KieBpmConfig> kieBpmConfigMap) {
        this.kieBpmConfigMap = kieBpmConfigMap;
    }
}
