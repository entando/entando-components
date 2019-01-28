/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig;



public class DashboardConfig {

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public String getServerDescription() {
		return _serverDescription;
	}
	public void setServerDescription(String serverDescription) {
		this._serverDescription = serverDescription;
	}

	public String getServerURI() {
		return _serverURI;
	}
	public void setServerURI(String serverURI) {
		this._serverURI = serverURI;
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

	public int getTimeConnection() {
		return _timeConnection;
	}
	public void setTimeConnection(int timeConnection) {
		this._timeConnection = timeConnection;
	}

	public int getActive() {
		return _active;
	}
	public void setActive(int active) {
		this._active = active;
	}

	public int getDebug() {
		return _debug;
	}
	public void setDebug(int debug) {
		this._debug = debug;
	}

	
	private int _id;
	private String _serverDescription;
	private String _serverURI;
	private String _username;
	private String _password;
	private String _token;
	private int _timeConnection;
	private int _active;
	private int _debug;

}
