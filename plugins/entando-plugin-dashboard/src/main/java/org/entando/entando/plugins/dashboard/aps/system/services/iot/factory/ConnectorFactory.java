package org.entando.entando.plugins.dashboard.aps.system.services.iot.factory;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ConnectorFactory  implements ApplicationContextAware {

        private ApplicationContext applicationContext;

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

        /**
         * Returns the proper processor for the given connectorType
         * @param connectorType
         * @return
         */
        public IConnectorIot get(String connectorType) {
            Map<String, IConnectorIot> beans = applicationContext.getBeansOfType(IConnectorIot.class);
            IConnectorIot defName = beans.values().stream()
                    .filter(service -> service.supports(connectorType))
                    .findFirst().get();
//                    .orElseGet(NoOpWidgetConfigurationProcessor::new);
            return defName;
        }

    }
