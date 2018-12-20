package org.entando.entando.plugins.jacms.web.contentmodel.validator;

import com.agiletec.aps.system.common.entity.IEntityManager;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import org.entando.entando.web.entity.validator.AbstractEntityTypeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentTypeValidator extends AbstractEntityTypeValidator {

    private final IContentManager contentManager;

    @Autowired
    public ContentTypeValidator(IContentManager contentManager) {
        this.contentManager = contentManager;
    }

    @Override
    protected IEntityManager getEntityManager() {
        return contentManager;
    }
}
