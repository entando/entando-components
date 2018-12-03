package org.entando.entando.plugins.jacms.web.contentmodel.util;

import com.agiletec.plugins.jacms.aps.system.services.contentmodel.model.ContentTypeDto;
import org.entando.entando.web.common.model.PagedMetadata;

import java.util.*;

public class RestControllerTestUtil {

    public static final PagedMetadata EMPTY_PAGED_METADATA = new EmptyMetadata<>();

    private static class EmptyMetadata<E> extends PagedMetadata<E> {
        @Override
        public List<E> getBody() {
            return Collections.EMPTY_LIST;
        }
    }

    public static PagedMetadata<ContentTypeDto> createNonEmptyPagedMetadata() {
        PagedMetadata<ContentTypeDto> pagedMetadata = new PagedMetadata<>();
        List<ContentTypeDto> allResults = new ArrayList<>();
        allResults.add(new ContentTypeDtoBuilder().withId(1L).build());
        allResults.add(new ContentTypeDtoBuilder().withId(2L).build());
        allResults.add(new ContentTypeDtoBuilder().withId(3L).build());
        pagedMetadata.setBody(allResults);
        return pagedMetadata;
    }

    public static Long generateNextId() {
        return Math.abs(new Random().nextLong());
    }
}
