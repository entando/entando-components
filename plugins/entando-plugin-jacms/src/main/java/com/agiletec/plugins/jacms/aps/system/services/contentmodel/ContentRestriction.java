package com.agiletec.plugins.jacms.aps.system.services.contentmodel;

public class ContentRestriction {

    public final static String OPEN_GROUP = "free";
    public final static String OPEN_VALUE = "OPEN";
    public final static String RESTRICTED_VALUE = "RESTRICTED";

    public static String getRestrictionValue(String mainGroup) {
        return null != mainGroup && mainGroup.equals(OPEN_GROUP) ? OPEN_VALUE : RESTRICTED_VALUE;
    }

}
