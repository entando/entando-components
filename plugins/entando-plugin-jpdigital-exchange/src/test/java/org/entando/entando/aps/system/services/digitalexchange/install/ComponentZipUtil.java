/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.aps.system.services.digitalexchange.install;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

public class ComponentZipUtil {

    public static File getTestWidgetZip() {
        return ComponentZipUtil.zip("de_test_widget", "component.xml", "data/de_test_widget.json");
    }

    public static File getTestPageModelZip() {
        return ComponentZipUtil.zip("de_test_page_model", "component.xml", "data/port_data_test.sql",
                "data/de_test_page_model.json", "data/de_test_label.json", "resources/test.css");
    }

    public static File getTestRoleZip() {
        return ComponentZipUtil.zip("de_test_role", "component.xml", "data/de_test_role.json");
    }

    public static File getTestGroupZip() {
        return ComponentZipUtil.zip("de_test_group", "component.xml", "data/de_test_group.json");
    }

    /**
     * Creates a temporary zip file assembling a set of files loaded from test
     * resources.
     */
    private static File zip(String component, String... files) {

        try {
            File temporaryZip = Files.createTempFile("tmp", null).toFile();

            try (FileOutputStream fos = new FileOutputStream(temporaryZip);
                    ZipOutputStream zos = new ZipOutputStream(fos)) {

                for (String file : files) {
                    zos.putNextEntry(new ZipEntry(file));

                    String filePath = "components/" + component + "/" + file;

                    try (InputStream in = ComponentZipUtil.class.getClassLoader().getResourceAsStream(filePath)) {
                        IOUtils.copy(in, zos);
                    }
                    zos.closeEntry();
                }
            }

            return temporaryZip;

        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
