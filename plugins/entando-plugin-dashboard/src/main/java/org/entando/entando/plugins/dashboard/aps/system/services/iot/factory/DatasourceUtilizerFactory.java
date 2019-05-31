package org.entando.entando.plugins.dashboard.aps.system.services.iot.factory;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.ServerType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DatasourceType;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.BaseConnectorIot;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorFactory;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorIot;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IDatasourceUtilizer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Component
public class DatasourceUtilizerFactory  implements ApplicationContextAware {

  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  /**
   * Returns the proper processor for the given connectorType
   * @return List<Class>
   */
  public List<Class> getDatasourceUtilizers(String datasource) {
    Map<String, IDatasourceUtilizer> beans = applicationContext.getBeansOfType(IDatasourceUtilizer.class);
    List<Class> defName = beans.values().stream()
        .map(service -> {
          if(service.getDatasourceUtilizers(datasource) != null) {
            return service.getDatasourceUtilizers(datasource);
          }
          return null;
        }).collect(Collectors.toList());
    return defName;
  }

  public void deleteDatasourceUtilizers(String datasource) {
    Map<String, IDatasourceUtilizer> beans = applicationContext.getBeansOfType(IDatasourceUtilizer.class);
    beans.values().forEach(service -> service.deleteDatasourceReferences(datasource));
  }
}
