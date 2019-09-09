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
package org.entando.entando.aps.system.jpa;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Jackson serializer used to serialize a byte[] into a String (by default
 * Jackson converts the byte array into a String encoded in Base64). This is
 * particularly useful to serialize JPA entities having some @Lob fields (long
 * blobs) that should always be defined using byte[] instead of String to avoid
 * compatibility issues between different RDBMS (e.g. using Derby a String will
 * always be converted into a varchar(255) even if it is annotated by @Lob).
 */
public class ByteArrayStringSerializer extends StdSerializer<byte[]> {

    private static final long serialVersionUID = 1L;

    public ByteArrayStringSerializer() {
        super(byte[].class);
    }

    @Override
    public void serialize(byte[] bytes, JsonGenerator gen, SerializerProvider provider) throws IOException {
        String value = bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
        gen.writeString(value);
    }
}
