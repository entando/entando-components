package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import com.agiletec.aps.system.common.AbstractService;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.utils.IoTConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

public abstract class AbstractConnectorService extends AbstractService implements IConnectorService{

  private final Logger logger = LoggerFactory.getLogger(getClass());

  protected <T extends DashboardConfigDto> boolean isServerReacheable(T server) throws IOException {
    boolean result = false;
    DashboardConfigDto serverObj = DashboardConfigDto.class.cast(server);
    logger.info("{} pings method to {}",this.getClass().getSimpleName(), server.getServerURI());
    URL urlObj;
    try { urlObj = new URL(serverObj.getServerURI());}
    catch (MalformedURLException e) {
      throw new MalformedURLException(e.getMessage());
    }
    HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();
    con.setRequestMethod("GET");
    if(server.getTimeConnection() > IoTConstants.MINIMUM_TIMEOUT_CONNECTION) {
      con.setConnectTimeout(server.getTimeConnection());
    }
    else {
      con.setConnectTimeout(IoTConstants.MINIMUM_TIMEOUT_CONNECTION);
    }
    try {
      con.connect();
    }
    catch (SocketTimeoutException | UnknownHostException e ) {
      return false;
    }
    int code = con.getResponseCode();
    if (code == HttpStatus.OK.value()) {
      result = true;
      logger.debug("{} ping ok to {}", this.getClass().getSimpleName(), server.getServerURI());
    }
    return result;
  }

}
