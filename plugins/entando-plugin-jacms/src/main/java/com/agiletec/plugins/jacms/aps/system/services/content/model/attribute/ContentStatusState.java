package com.agiletec.plugins.jacms.aps.system.services.content.model.attribute;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;

public enum ContentStatusState {
    PUBLISHED,
    UNPUBLISHED,
    READY,
    PUBLISHED_WITH_NEW_VERSION_IN_READY_STATE,
    PUBLISHED_WITH_NEW_VERSION_IN_DRAFT_STATE;


    /**
     * Check and returns the ContentStatus enum based on content internal status
     * @param contentDto the ContentDto of which return the status representation
     * @return the string representation of a Content status
     */
    public static ContentStatusState calculateState(ContentDto contentDto) {
        if (contentDto.getStatus().equals(Content.STATUS_PUBLIC)) {
            return PUBLISHED;
        } else if (contentDto.getStatus().equals(Content.STATUS_READY)) {
            if (contentDto.isOnLine()) {
                return PUBLISHED_WITH_NEW_VERSION_IN_READY_STATE;
            } else {
                return READY;
            }
        } else {
            if (contentDto.isOnLine()) {
                return PUBLISHED_WITH_NEW_VERSION_IN_DRAFT_STATE;
            } else {
                return UNPUBLISHED;
            }
        }
    }

    @Override
    public String toString() {
        return this.name();
    }
}
