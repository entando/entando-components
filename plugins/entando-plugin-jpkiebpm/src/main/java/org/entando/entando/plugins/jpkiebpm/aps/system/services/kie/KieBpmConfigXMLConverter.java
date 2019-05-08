/*
 * The MIT License
 *
 * Copyright 2019 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import javax.servlet.ServletContext;
import org.entando.entando.crypt.DefaultTextEncryptor;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieBpmConfigFactory;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.context.support.ServletContextResource;

public class KieBpmConfigXMLConverter implements ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(KieBpmConfigXMLConverter.class);

    private ServletContext servletContext;
    private String keyFile;

    public String marshal(KieBpmConfigFactory configFactory) throws Throwable {
        KieBpmConfigFactory copiedConfigFactory = new KieBpmConfigFactory(configFactory);

        copiedConfigFactory.getKieBpmConfigMap().values().forEach(config -> {
            if (config.getPassword() != null) {
                config.setPassword(getTextEncryptor().encrypt(config.getPassword()));
            }
        });

        return JAXBHelper.marshall(copiedConfigFactory, true, false);
    }

    public KieBpmConfigFactory unmarshal(String xml) throws Throwable {
        KieBpmConfigFactory configFactory = (KieBpmConfigFactory) JAXBHelper.unmarshall(xml, KieBpmConfigFactory.class, true, false);

        configFactory.getKieBpmConfigMap().values().forEach(config -> {
            if (config.getPassword() != null) {
                String password = config.getPassword();
                try {
                    password = getTextEncryptor().decrypt(config.getPassword());
                } catch (Throwable ex) {
                    logger.warn("Unable to decrypt BPM password " + password);
                }
                config.setPassword(password);
            }
        });

        return configFactory;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void setKeyFile(String keyFile) {
        this.keyFile = keyFile;
    }

    private TextEncryptor textEncryptor;

    private TextEncryptor getTextEncryptor() {
        if (textEncryptor == null) {
            Resource resource = new ServletContextResource(servletContext, keyFile);
            this.textEncryptor = new DefaultTextEncryptor(resource);
        }
        return textEncryptor;
    }
}
