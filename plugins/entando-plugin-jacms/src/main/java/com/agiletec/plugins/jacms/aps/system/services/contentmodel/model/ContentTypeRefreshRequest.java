package com.agiletec.plugins.jacms.aps.system.services.contentmodel.model;

import com.google.common.collect.ImmutableList;

import java.util.*;

public class ContentTypeRefreshRequest {

    private List<String> profileTypeCodes = Collections.emptyList();

    public List<String> getProfileTypeCodes() {
        return profileTypeCodes;
    }

    public void setProfileTypeCodes(List<String> profileTypeCodes) {
        this.profileTypeCodes = ImmutableList.copyOf(profileTypeCodes);
    }
}
