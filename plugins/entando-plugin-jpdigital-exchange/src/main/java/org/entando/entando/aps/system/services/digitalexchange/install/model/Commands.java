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
package org.entando.entando.aps.system.services.digitalexchange.install.model;

import java.util.Map;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JAXB model for retrieving the map of all the available commands.
 */
@XmlRootElement(name = "commands")
public class Commands {

    private Map<String, String> commandsMap;

    @XmlElementWrapper(name = "map")
    public Map<String, String> getCommandsMap() {
        return commandsMap;
    }

    public void setCommandsMap(Map<String, String> commandsMap) {
        this.commandsMap = commandsMap;
    }

    public String getExpression(String commandName) {
        return commandsMap.get(commandName);
    }
}
