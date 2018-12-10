package org.entando.entando.plugins.jacms.web.contentmodel.util;

import org.entando.entando.aps.system.services.entity.model.EntityTypeShortDto;
import org.entando.entando.web.common.model.PagedMetadata;

import java.util.*;

public class RestControllerTestUtil {

    public static final PagedMetadata EMPTY_PAGED_METADATA = new EmptyMetadata<>();

    private static class EmptyMetadata<E> extends PagedMetadata<E> {
        @Override
        public List<E> getBody() {
            return Collections.emptyList();
        }
    }

    public static PagedMetadata<EntityTypeShortDto> createNonEmptyPagedMetadata() {
        PagedMetadata<EntityTypeShortDto> pagedMetadata = new PagedMetadata<>();
        List<EntityTypeShortDto> allResults = new ArrayList<>();
        allResults.add(new EntityTypeShortDto());
        allResults.add(new EntityTypeShortDto());
        allResults.add(new EntityTypeShortDto());
        pagedMetadata.setBody(allResults);
        return pagedMetadata;
    }

    public static Long generateNextId() {
        return Math.abs(new Random().nextLong());
    }
}
