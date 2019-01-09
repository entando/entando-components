package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.widgettype.validators;

import com.agiletec.aps.system.services.page.IPage;
import org.entando.entando.aps.system.services.widgettype.validators.WidgetConfigurationValidator;
import org.entando.entando.web.page.model.WidgetConfigurationRequest;
import org.springframework.validation.BeanPropertyBindingResult;

public class DashboardTableWidgetValidator implements WidgetConfigurationValidator {

    public static final String WIDGET_KEY_ID = "id";

    @Override
    public boolean supports(String widgetCode) {
        return true;
    }

    @Override
    public BeanPropertyBindingResult validate(WidgetConfigurationRequest widget, IPage page) {
        return new BeanPropertyBindingResult(widget, widget.getClass().getSimpleName());

    }
}
