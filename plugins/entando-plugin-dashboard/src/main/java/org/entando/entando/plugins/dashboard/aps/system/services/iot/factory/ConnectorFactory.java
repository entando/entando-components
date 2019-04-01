package org.entando.entando.plugins.dashboard.aps.system.services.iot.factory;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.IDashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
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
        public IConnectorIot getConnector(String connectorType) {
            Map<String, IConnectorIot> beans = applicationContext.getBeansOfType(IConnectorIot.class);
            IConnectorIot defName = beans.values().stream()
                    .filter(service -> service.supports(connectorType))
                    .findFirst().get();
//                    .orElseGet(NoOpWidgetConfigurationProcessor::new);
            return defName;
        }
        
        public IDashboardDatasourceDto getDashboardDatasource(ServerType serverType) {
          Map<String, IDashboardDatasourceDto> beans = applicationContext.getBeansOfType(IDashboardDatasourceDto.class);
          IDashboardDatasourceDto defName = beans.values().stream().filter(
              conn -> conn.supports(serverType.getCode())).findFirst().get();
          return defName;
        }


  public List<ServerType> getServerType() {
    Map<String, IConnectorIot> beans = applicationContext.getBeansOfType(IConnectorIot.class);
    List<ServerType> serverTypes = new ArrayList<>();
    beans.values().forEach(conn -> serverTypes.add(conn.getServerType()));
    return serverTypes;
  }
}
