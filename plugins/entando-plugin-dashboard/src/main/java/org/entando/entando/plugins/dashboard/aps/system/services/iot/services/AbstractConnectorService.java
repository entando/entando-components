package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import static org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants.CONNECTION_TIMEOUT;

public abstract class AbstractConnectorService implements IConnectorService{

  private final Logger logger = LoggerFactory.getLogger(getClass());
  
  protected <T extends DashboardConfigDto> boolean isServerReacheable(T server) throws IOException {
    boolean result = false;
    DashboardConfigDto serverObj = DashboardConfigDto.class.cast(server);
    logger.info("{} pings method to {}",this.getClass().getSimpleName(), server.getServerURI());
    URL urlObj = new URL(serverObj.getServerURI());
    HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
    con.setRequestMethod("GET");
    con.setConnectTimeout(CONNECTION_TIMEOUT);
    try {
      con.connect();
    }
    catch (SocketTimeoutException | UnknownHostException e ) {
      e.printStackTrace();
    }
    int code = con.getResponseCode();
    if (code == HttpStatus.OK.value()) {
      result = true;
      logger.debug("{} ping ok to {}", this.getClass().getSimpleName(), server.getServerURI());
    }
    return result;
  }

}
