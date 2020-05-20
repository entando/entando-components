package org.entando.entando.plugins.jacms.aps.system.services.mockhelper;

import org.entando.entando.web.common.model.Filter;
import org.entando.entando.web.common.model.RestListRequest;

public class ContentTypeMockHelper {

    public static final String CONTENT_TYPE_CODE = "COD";

    public static RestListRequest mockRestListRequest() {

        RestListRequest restListRequest = new RestListRequest();
        restListRequest.setSort("typeCode");
        restListRequest.addFilter(ContentTypeMockHelper.mockContentTypeFilter());
        return restListRequest;
    }

    public static Filter mockContentTypeFilter() {
        return new Filter("typeCode", CONTENT_TYPE_CODE, "eq");
    }

}
