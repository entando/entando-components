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
package com.agiletec.plugins.jpwebmail.aps.system.services.webmail.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.plugins.jpwebmail.aps.system.JpwebmailSystemConstants;
import com.agiletec.plugins.jpwebmail.aps.system.services.webmail.WebMailConfig;

public class CertificateHandler {
	
	/**
	 * Attiva il meccanismo che porta alla negoziazione dei certificati di sicurezza; SE l'handshake è stato effettuato allora ritorna
	 * immediatamente senza modificare alcun parametro.
	 * 
	 * @param host
	 * @param port
	 * @param protocol
	 * @throws ApsSystemException in caso di errore nell'elaborazione dei certificati
	 */
	public void aquireCertificate(String host, Integer port, String protocol, WebMailConfig config) throws ApsSystemException {
		boolean proceed;

		this.setConfig(config);
		if (!this._checkPerformed) {
			proceed = config.isCertificateEnable();
			if (null == port || null == host || null == protocol) {
				_mustCheckCertificates = false;
				throw new ApsSystemException(showConsoleInfo("Parametri per l'handshake SSL non validi, negoziazione certificati SSL cancellata"));
			} else {
				_mustCheckCertificates = true;
			}
			if ((!protocol.equalsIgnoreCase("imaps") && !protocol.equalsIgnoreCase("imaps")) || !proceed) {
				_mustCheckCertificates = false;
			}
			// infine...
			if (_mustCheckCertificates) {
				try {
					// esegui l'handshake con l'host
					doSSLHandshake(host,port,protocol);
					// modifica le proprietà del sistema
					System.setProperty("javax.net.ssl.trustStore", _certificateInUse);
					System.setProperty("javax.net.ssl.trustStorePassword", this.getPasswordInUse());
					System.setProperty("javax.net.ssl.trustStoreType","JKS");
					ApsSystemUtils.getLogger().info("'"+_certificateInUse+"' e password inseriti nelle proprietà di sistema");
				} catch (Throwable t) {
					ApsSystemUtils.getLogger().error(this.getClass().getName(), "Certificates ["+t.getLocalizedMessage()+"]",t);					
				}
			} else {			
				this._checkPerformed = true;
				ApsSystemUtils.getLogger().info(showConsoleInfo("Il controllo del certificato SSL è stato disabilitato da configurazione oppure il protocollo '"+protocol+"' non lo prevede"));
			}
		}
	}	
	
	private String showConsoleInfo(String msg) {
		Boolean debugOnConsole = this.getConfig().isCertificateDebugOnConsole();
		
		if (debugOnConsole)
			System.out.println(msg);
		return msg;
	}
	
	private KeyStore doKeyStore(File file, char[] passphrase) throws Throwable {
		KeyStore ks;
		InputStream in = null;
		if (null == file) {
			throw new ApsSystemException("Il file fornito per il keyStore non è valido");
		}
		in = new FileInputStream(file);
		ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(in, passphrase);
		in.close();
		return ks;
	}
	
	private File getCertificatesFile(String name) throws Throwable {
		String certificatePath = this.getConfig().getCertificatePath();
		String java_home = System.getProperty("java.home");
		File certificateDirectoryPath = new File(certificatePath);
		boolean pathDirsCreated;
		char SEP = File.separatorChar;
		File file = null;
		// controllo per l'esistenza dei parametri richiesti
		if (null == name) {
			ApsSystemUtils.getLogger().info(showConsoleInfo("il nome del certificato non è valido, uso il default 'jssecacerts'"));
			name = "jssecacerts";
		}
		if (null != java_home && java_home.length()==0 && this._mustCheckCertificates) {
			throw new ApsSystemException(showConsoleInfo("Variabile 'JAVA_HOME' non definita, richiesta se 'Enable' in 'certificate' è vera"));
		}
		if (null==certificatePath) {
			throw new ApsSystemException(showConsoleInfo("Variabile 'certificatePath' non definita, richiesta se 'Enable' in 'certificate' è vera"));
		}
		// ottiene e crea un nuovo certificato locale
		file = new File(certificatePath+SEP+name);
		// crea le directory intermedie
		pathDirsCreated = certificateDirectoryPath.mkdirs();
		if (!pathDirsCreated && !certificateDirectoryPath.exists()) {
			ApsSystemUtils.getLogger().info(showConsoleInfo("ATTENZIONE: non è stato possibile creare tutte le directory intermedie del percorso '"+file+"'"));
		}
		if (file.isFile() == false) {
			String workingDirectory = java_home + SEP + "lib" + SEP + "security";
			File dir = new File(workingDirectory);
			ApsSystemUtils.getLogger().info(showConsoleInfo("directory di lavoro '"+workingDirectory)+"'");
			file = new File(dir, name);
			if (file.isFile() == false) {
				file = new File(dir, "cacerts");
				ApsSystemUtils.getLogger().info(showConsoleInfo("creazione di un nuovo certificato '"+file+"'"));
			} 
		} else {
			ApsSystemUtils.getLogger().info(showConsoleInfo("certificato '"+name+"' acquisito in '"+certificatePath+"'"));
		}
		this._certificateInUse = file.toString();
		return file;
	}
	
	private void doSSLHandshake(String host, Integer port, String protocol) throws Throwable {
		String certificatePath = this.getConfig().getCertificatePath();
		char SEP = File.separatorChar;
		SSLContext context = null;
		TrustManagerFactory trustManagerFactory = null;
		X509TrustManager defaultTrustManager = null;
		KeyStore keyStore = null;
		SimpleTrustManager trustManager = null;
		SSLSocket socket = null;
		SSLSocketFactory factory = null;
		X509Certificate[] chain;
		MessageDigest sha1 = null;
		MessageDigest md5 = null;
		File file = null;
		String alias;
		final String certificateFile = "jssecacerts";
		
		file = getCertificatesFile(certificateFile);
		context = SSLContext.getInstance("TLS");
		trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		ApsSystemUtils.getLogger().info(showConsoleInfo("Caricamento file KeyStore '" + file + "'..."));
		keyStore=doKeyStore(file, this.getPasswordInUse().toCharArray());
		trustManagerFactory.init(keyStore);
		defaultTrustManager = (X509TrustManager) trustManagerFactory.getTrustManagers()[0];
		trustManager = new SimpleTrustManager(defaultTrustManager);
		context.init(null, new TrustManager[] {trustManager}, null);
		factory = context.getSocketFactory();
		ApsSystemUtils.getLogger().info(showConsoleInfo("SSL handshake con l'host '" + host + ":" + port + "'..."));
		socket = (SSLSocket)factory.createSocket(host, port);
		socket.setSoTimeout(10000);
		try {
			this._checkPerformed = true;
			socket.startHandshake();
			socket.close();
			ApsSystemUtils.getLogger().info(showConsoleInfo("\nCertificato dell'host '"+host+"' fidato"));
			this._hostTrusted = true;
			this._certificateInUse = file.toString();
			return;
		} catch (SSLException e) {
			ApsSystemUtils.getLogger().info(showConsoleInfo("\nIl certificato dell'host '"+host+"' non è fidato"));
			// a questo punto l'host non è nella lista dei server fidati quindi se 'lazyCheck' è posto a false allora segnaliamo l'errore
			if (!this.getConfig().isCertificateLazyCheck()) {
				this._hostTrusted=false;
				ApsSystemUtils.getLogger().info(showConsoleInfo("Il certificato per l'host '"+host+":"+port+"' non è stato accettato"));
			}
			chain = trustManager.getChain();
			if (chain == null) {
				showConsoleInfo("Errore nell'ottenere la catena dei server certificati");
				ApsSystemUtils.getLogger().info("Errore nell'ottenere la catena dei server certificati");
			} else {
				sha1 = MessageDigest.getInstance("SHA1");
				md5 = MessageDigest.getInstance("MD5");
				ApsSystemUtils.getLogger().info(showConsoleInfo("L'host '" + host + "' ha spedito " + chain.length + " certificato/i"));		
				for (int i = 0; i < chain.length; i++) {
					X509Certificate cert = chain[i];
					sha1.update(cert.getEncoded());
					md5.update(cert.getEncoded());
				}
				OutputStream out = new FileOutputStream(certificatePath+SEP+certificateFile);
				for (int s=0;s < chain.length; s++) {
					X509Certificate cert = chain[s];
					alias = host + "-" + (s + 1);
					keyStore.setCertificateEntry(alias, cert);
					ApsSystemUtils.getLogger().info(showConsoleInfo("Aggiunta del certificato in '"+certificatePath+SEP+certificateFile+"'..."));
					keyStore.store(out, this.getPasswordInUse().toCharArray());
					ApsSystemUtils.getLogger().info(showConsoleInfo("Certificato aggiunto con l'alias '"+alias+"'"));
				}
				out.close();
				this._hostTrusted = true;
				this._certificateInUse = certificatePath+SEP+certificateFile;
			}
		}
	}
	
	/**
	 *  Restituisce 'true' se e solo se l'host è ritenuto affidabile. Scatena un'eccezione se viene invocato prima che l'handshake 
	 *  venga effettuato, qualora 'enabled' sia posto a 'true'
	 */
	public boolean isHostTrusted() throws ApsSystemException {
		if (this._checkPerformed) {
			return this._hostTrusted && this._mustCheckCertificates;
		} else {
			if (this.getConfig().isCertificateEnable()) {
				throw new ApsSystemException("'isCertificateAquired' invocato prima che l'handshake fosse effettuato");
			}
		}
		return false;
	}
	
	/**
	 * Questo metodo indica se sia possibile procedere con la connessione in maniera 'affidabile'.
	 * NOTA BENE: SE l'handshaking è stato disabilitato allora restituisce 'true' (contestualmente 'isHostTrusted' risulta 'false')
	 * 
	 * @return 'true' se e solo se l'host è ritenuto affidabile oppure se il meccanismo di handshking risulta disabilitato. 
	 */
	public boolean proceedWithConnection() throws ApsSystemException {
		boolean isEnabled = this.getConfig().isCertificateEnable();
		return (isHostTrusted() && isEnabled) || !isEnabled;
	}
	
	public WebMailConfig getConfig() {
		return _config;
	}
	protected void setConfig(WebMailConfig config) {
		this._config = config;
	}
	
	/**
	 * Esporta il file usato per la registrazione dei certificati.
	 * 
	 * @return il file contenente il certificato in uso.
	 */
	public String getCertificateInUse() {
		return _certificateInUse;
	}
	
	/**
	 * Restituisce la password usata per registrare in locale i certificati autogenerati
	 * 
	 * @ return la password usata per registrare i certificati autogenerati
	 */	
	public String getPasswordInUse() {
		return JpwebmailSystemConstants.WEBMAIL_SSL_CERTIFICATE_STORE_PWD;
	}
	
	/**
	 * Se il protocollo è diverso da 'imaps' o 'pop3s' oppure se l'acquisizione dei certificati è disabilitata
	 * allora '_mustCheckCertificates' viene posto a 'false'. Di conseguenza, l'handshake non sarà mai effettuato E 
	 * '_hostTrusted' sarà sempre 'false'.
	 */
	private boolean _mustCheckCertificates;
	private boolean _hostTrusted;
	private boolean _checkPerformed = false;
	private String _certificateInUse;
	
	private WebMailConfig _config;
	
}