/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.api;



import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.IotConfig;

@XmlRootElement(name = "iotConfig")
@XmlType(propOrder = {"id", "name", "hostname", "port", "webapp", "username", "password", "token"})
public class JAXBIotConfig {

    public JAXBIotConfig() {
        super();
    }

    public JAXBIotConfig(IotConfig iotConfig) {
		this.setId(iotConfig.getId());
		this.setName(iotConfig.getName());
		this.setHostname(iotConfig.getHostname());
		this.setPort(iotConfig.getPort());
		this.setWebapp(iotConfig.getWebapp());
		this.setUsername(iotConfig.getUsername());
		this.setPassword(iotConfig.getPassword());
		this.setToken(iotConfig.getToken());
    }
    
    public IotConfig getIotConfig() {
    	IotConfig iotConfig = new IotConfig();
		iotConfig.setId(this.getId());
		iotConfig.setName(this.getName());
		iotConfig.setHostname(this.getHostname());
		iotConfig.setPort(this.getPort());
		iotConfig.setWebapp(this.getWebapp());
		iotConfig.setUsername(this.getUsername());
		iotConfig.setPassword(this.getPassword());
		iotConfig.setToken(this.getToken());
    	return iotConfig;
    }

	@XmlElement(name = "id", required = true)
	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	@XmlElement(name = "name", required = true)
	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	@XmlElement(name = "hostname", required = true)
	public String getHostname() {
		return _hostname;
	}
	public void setHostname(String hostname) {
		this._hostname = hostname;
	}

	@XmlElement(name = "port", required = true)
	public int getPort() {
		return _port;
	}
	public void setPort(int port) {
		this._port = port;
	}

	@XmlElement(name = "webapp", required = true)
	public String getWebapp() {
		return _webapp;
	}
	public void setWebapp(String webapp) {
		this._webapp = webapp;
	}

	@XmlElement(name = "username", required = true)
	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	@XmlElement(name = "password", required = true)
	public String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		this._password = password;
	}

	@XmlElement(name = "token", required = true)
	public String getToken() {
		return _token;
	}
	public void setToken(String token) {
		this._token = token;
	}


	private int _id;
	private String _name;
	private String _hostname;
	private int _port;
	private String _webapp;
	private String _username;
	private String _password;
	private String _token;

}
