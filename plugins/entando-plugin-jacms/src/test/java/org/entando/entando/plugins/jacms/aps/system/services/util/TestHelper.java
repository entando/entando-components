package org.entando.entando.plugins.jacms.aps.system.services.util;

import com.agiletec.aps.system.services.group.Group;

import java.util.ArrayList;
import java.util.List;

public class TestHelper {

    public static List<Group> createGroups() {
        List<Group> groups = new ArrayList<>();

        Group free = new Group();
        free.setName("free");
        free.setDescription("Free");

        Group admin = new Group();
        admin.setName("admin");
        admin.setDescription("Administrators");

        groups.add(free);
        groups.add(admin);

        return groups;
    }
}
