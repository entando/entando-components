/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail;

/**
 * @author E.Santoboni
 */
public class WebMailConfig implements Cloneable {
	
	@Override
	public WebMailConfig clone() {
		WebMailConfig config = new WebMailConfig();
		
		config.setLocalhost(this.getLocalhost());
		config.setDomainName(this.getDomainName());
		config.setUseEntandoUserPassword(this.isUseEntandoUserPassword());
		
		config.setCertificateEnable(this.isCertificateEnable());
		config.setCertificateLazyCheck(this.isCertificateLazyCheck());
		config.setCertificatePath(this.getCertificatePath());
		config.setCertificateDebugOnConsole(this.isCertificateDebugOnConsole());
		
		config.setImapHost(this.getImapHost());
		config.setImapPort(this.getImapPort());
		config.setImapProtocol(this.getImapProtocol());
		
		config.setDebug(this.isDebug());
		config.setSmtpAuth(this.getSmtpAuth());
		config.setSmtpHost(this.getSmtpHost());
		config.setSmtpPort(this.getSmtpPort());
		config.setSmtpUserName(this.getSmtpUserName());
		config.setSmtpPassword(this.getSmtpPassword());
		config.setSmtpProtocol(this.getSmtpProtocol());
		
		config.setTrashFolderName(this.getTrashFolderName());
		config.setSentFolderName(this.getSentFolderName());
		
		return config;
	}
	
	public String getSmtpHost() {
		return _smtpHost;
	}
	public void setSmtpHost(String smtpHost) {
		this._smtpHost = smtpHost;
	}
	
	public String getSmtpPassword() {
		return _smtpPassword;
	}
	public void setSmtpPassword(String smtpPassword) {
		this._smtpPassword = smtpPassword;
	}
	
	public String getSmtpUserName() {
		return _smtpUserName;
	}
	public void setSmtpUserName(String smtpUserName) {
		this._smtpUserName = smtpUserName;
	}
	
	public Integer getSmtpPort() {
		return _smtpPort;
	}
	public void setSmtpPort(Integer smtpPort) {
		this._smtpPort = smtpPort;
	}
	
	public void setSmtpProtocol(Integer smtpProtocol) {
		this._smtpProtocol = smtpProtocol;
	}
	public Integer getSmtpProtocol() {
		return _smtpProtocol;
	}

	/*
	public boolean isSmtpEntandoUserAuth() {
	return _smtpEntandoUserAuth;
	}
	public void setSmtpEntandoUserAuth(boolean smtpEntandoUserAuth) {
	this._smtpEntandoUserAuth = smtpEntandoUserAuth;
	}
	@Deprecated
	public boolean isSmtpEntandoUserAuth() {
	return this.isSmtpEntandoUserAuth();
	}
	@Deprecated
	public void setSmtpEntandoUserAuth(boolean smtpEntandoUserAuth) {
	this.setSmtpEntandoUserAuth(smtpEntandoUserAuth);
	}
	*/
	public Integer getSmtpAuth() {
		return _smtpAuth;
	}
	public void setSmtpAuth(Integer smtpAuth) {
		this._smtpAuth = smtpAuth;
	}
	
	public boolean isDebug() {
		return _debug;
	}
	public void setDebug(boolean debug) {
		this._debug = debug;
	}
	
	public String getLocalhost() {
		return _localhost;
	}
	public void setLocalhost(String localhost) {
		this._localhost = localhost;
	}
	
	public String getDomainName() {
		return _domainName;
	}
	public void setDomainName(String domainName) {
		this._domainName = domainName;
	}
	
	public boolean isUseEntandoUserPassword() {
		return _useEntandoUserPassword;
	}
	public void setUseEntandoUserPassword(boolean useEntandoUserPassword) {
		this._useEntandoUserPassword = useEntandoUserPassword;
	}
	
	public String getImapHost() {
		return _imapHost;
	}
	public void setImapHost(String imapHost) {
		this._imapHost = imapHost;
	}
	
	public Integer getImapPort() {
		return _imapPort;
	}
	public void setImapPort(Integer imapPort) {
		this._imapPort = imapPort;
	}
	
	public String getImapProtocol() {
		return _imapProtocol;
	}
	public void setImapProtocol(String imapProtocol) {
		this._imapProtocol = imapProtocol;
	}
	
	public String getTempDiskRootFolder() {
		return _tempDiskRootFolder;
	}
	public void setTempDiskRootFolder(String tempDiskRootFolder) {
		this._tempDiskRootFolder = tempDiskRootFolder;
	}
	
	public String getTrashFolderName() {
		return _trashFolderName;
	}
	public void setTrashFolderName(String trashFolderName) {
		this._trashFolderName = trashFolderName;
	}
	
	public String getSentFolderName() {
		return _sentFolderName;
	}
	public void setSentFolderName(String sentFolderName) {
		this._sentFolderName = sentFolderName;
	}
	
	public boolean isCertificateEnable() {
		return _certificateEnable;
	}
	public void setCertificateEnable(boolean certificateEnable) {
		this._certificateEnable = certificateEnable;
	}
	
	public boolean isCertificateLazyCheck() {
		return _certificateLazyCheck;
	}
	public void setCertificateLazyCheck(boolean certificateLazyCheck) {
		this._certificateLazyCheck = certificateLazyCheck;
	}
	
	public String getCertificatePath() {
		return _certificatePath;
	}
	public void setCertificatePath(String certificatePath) {
		this._certificatePath = certificatePath;
	}
	
	public boolean isCertificateDebugOnConsole() {
		return _certificateDebugOnConsole;
	}
	public void setCertificateDebugOnConsole(boolean certificateDebugOnConsole) {
		this._certificateDebugOnConsole = certificateDebugOnConsole;
	}
	
	private boolean _certificateEnable;
	private boolean _certificateLazyCheck;
	private String	_certificatePath;
	private boolean _certificateDebugOnConsole;
	
	private String _localhost;
	private String _domainName;
	private boolean _useEntandoUserPassword; //Use Entando password
	
	private String _imapHost;
	private Integer _imapPort;
	private String _imapProtocol;
	
	private String _smtpHost;
	private String _smtpUserName;
	private String _smtpPassword;
	private Integer _smtpPort;
	private Integer _smtpProtocol;
	private boolean _debug;
	//private boolean _smtpEntandoUserAuth;
	private Integer _smtpAuth;
	
	private String _tempDiskRootFolder;
	
	private String _trashFolderName;
	private String _sentFolderName;
	
	public static final int PROTO_STD = 0;
	public static final int PROTO_SSL = 1;
	public static final int PROTO_TLS = 2;
	
	public static final int SMTP_AUTH_ANONYMOUS = 0;
	public static final int SMTP_AUTH_ENTANDO_USER = 1;
	public static final int SMTP_AUTH_ENTANDO_USER_WITH_DOMAIN = 2;
	public static final int SMTP_AUTH_CUSTOM = 3;
	
	
}
