/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;



public class IotConfig {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getName() {
		return _name;
	}
	public void setName(String name) {
		this._name = name;
	}

	public String getHostname() {
		return _hostname;
	}
	public void setHostname(String hostname) {
		this._hostname = hostname;
	}

	public int getPort() {
		return _port;
	}
	public void setPort(int port) {
		this._port = port;
	}

	public String getWebapp() {
		return _webapp;
	}
	public void setWebapp(String webapp) {
		this._webapp = webapp;
	}

	public String getUsername() {
		return _username;
	}
	public void setUsername(String username) {
		this._username = username;
	}

	public String getPassword() {
		return _password;
	}
	public void setPassword(String password) {
		this._password = password;
	}

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
