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
        return ComponentZipUtil.zip("test_widget", "component.xml", "port_data_test.sql", "port_uninstallation.sql");
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

                zos.putNextEntry(new ZipEntry(component + "/"));
                zos.closeEntry();

                for (String file : files) {
                    String entryName = component + "/" + file;
                    zos.putNextEntry(new ZipEntry(entryName));

                    String filePath = "components/" + entryName;

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
