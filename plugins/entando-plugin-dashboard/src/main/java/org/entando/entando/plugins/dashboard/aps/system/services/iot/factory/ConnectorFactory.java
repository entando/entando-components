package org.entando.entando.plugins.dashboard.aps.system.services.iot.factory;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.BaseConnectorIot;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ConnectorFactory  implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the proper processor for the given connectorType
   * @param serverType 
   * @param datasourceType
   * @return IConnectorIot
   */
  public IConnectorFactory getConnector(String serverType, DatasourceType datasourceType) {
    Map<String, IConnectorFactory> beans = applicationContext.getBeansOfType(IConnectorFactory.class);
    IConnectorFactory defName = beans.values().stream()
        .filter(service -> service.supports(serverType) && service.getDatasourceType().equals(datasourceType))
        .findFirst()
        .orElseGet(BaseConnectorIot::new);
    return defName;
  }

  public List<DatasourceType> getDatasourceTypes(String serverType) {
    Map<String, IConnectorFactory> beans = applicationContext.getBeansOfType(IConnectorFactory.class);
    List<DatasourceType> datasourceTypes = new ArrayList<>();
    beans.values().stream().filter(service -> service.supports(serverType)).forEach(impl -> datasourceTypes.add(impl.getDatasourceType()));
    return datasourceTypes;
  }
  
  public List<ServerType> getServerType() {
    Map<String, IConnectorFactory> beans = applicationContext.getBeansOfType(IConnectorFactory.class);
    List<ServerType> serverTypes = new ArrayList<>();
    beans.values().forEach(conn -> serverTypes.add(conn.getServerType()));
    return serverTypes.stream().distinct().collect(Collectors.toList());
  }
}
