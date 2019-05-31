package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import java.util.List;

public interface IDatasourceUtilizer {

  Class getDatasourceUtilizers(String datasource);
  
  void deleteDatasourceReferences(String datasource);
  
}
