/*
 * Copyright 2018-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpseo.apsadmin.portal;

import com.agiletec.apsadmin.portal.PageSettingsAction;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.lang3.StringUtils;
import static org.entando.entando.plugins.jpseo.apsadmin.portal.PageSettingsActionAspect.PARAM_ROBOT_ALTERNATIVE_PATH_CODE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class SeoPageSettingsAction extends PageSettingsAction {
    
    private static final Logger logger = LoggerFactory.getLogger(SeoPageSettingsAction.class);
    
    public String setRobotsPath() {
        String alternativePath = this.getRequest().getParameter(PARAM_ROBOT_ALTERNATIVE_PATH_CODE);
        try {
            this.initLocalMap();
            if (!isRightPath(alternativePath)) {
                this.getRequest().getSession().setAttribute(PARAM_ROBOT_ALTERNATIVE_PATH_CODE, 
                        this.getText("jpseo.error.robotFilePath.invalid", new String[]{alternativePath}));
                return SUCCESS;
            }
            if (!StringUtils.isBlank(alternativePath)) {
                this.saveTempFile(INPUT);
            }
        } catch (Throwable t) {
            logger.error("error in setRobotsPath", t);
            return FAILURE;
        } finally {
            File file = new File(alternativePath);
            if (file.exists()) {
                file.delete();
            }
        }
        return SUCCESS;
    }
    
    protected static boolean isRightPath(String alternativePath) {
        String rapLowerCase = alternativePath.toLowerCase();
        if (rapLowerCase.contains("web-inf")
                || rapLowerCase.contains("meta-inf")
                || rapLowerCase.contains("../")
                || rapLowerCase.contains("%2e%2e%2f")
                || rapLowerCase.contains("<")
                || rapLowerCase.endsWith("%3c")
                || rapLowerCase.endsWith("%00")
                || rapLowerCase.endsWith("'")
                || rapLowerCase.endsWith("\"")) {
            return false;
        }
        return true;
    }
    
    private void saveTempFile(String filePath) throws IOException {
        InputStream is = new ByteArrayInputStream("".getBytes("UTF-8"));
        FileOutputStream outStream = null;
        try {
            byte[] buffer = new byte[1024];
            int length = -1;
            outStream = new FileOutputStream(filePath);
            while ((length = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
                outStream.flush();
            }
        } catch (IOException t) {
            String message = "Error saving File '" + filePath + "' - " + t.getMessage();
            this.addFieldError(PARAM_ROBOT_ALTERNATIVE_PATH_CODE, message);
            logger.error("Error on saving file", t);
        } finally {
            if (null != outStream) {
                outStream.close();
            }
            if (null != is) {
                is.close();
            }
        }
    }
    
}
