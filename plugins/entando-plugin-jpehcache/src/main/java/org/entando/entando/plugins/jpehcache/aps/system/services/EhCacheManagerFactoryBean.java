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
package org.entando.entando.plugins.jpehcache.aps.system.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.agiletec.aps.util.FileTextReader;

/**
 * @author Dmitriy Kopylenko
 * @author Juergen Hoeller
 * @author Eugenio Santoboni
 */
public class EhCacheManagerFactoryBean implements FactoryBean<CacheManager>, InitializingBean, DisposableBean {

	private static final Logger _logger = LoggerFactory.getLogger(EhCacheManagerFactoryBean.class);

	// Check whether EhCache 2.1+ CacheManager.create(Configuration) method is available...
	private static final Method createWithConfiguration =
			ClassUtils.getMethodIfAvailable(CacheManager.class, "create", Configuration.class);
	
	protected final Log logger = LogFactory.getLog(getClass());

	private Resource configLocation;

	private boolean shared = false;

	private String cacheManagerName;

	private CacheManager cacheManager;
	
	/**
	 * Set the location of the EhCache config file. A typical value is "/WEB-INF/ehcache.xml".
	 * <p>Default is "ehcache.xml" in the root of the class path, or if not found,
	 * "ehcache-failsafe.xml" in the EhCache jar (default EhCache initialization).
	 * @see net.sf.ehcache.CacheManager#create(java.io.InputStream)
	 * @see net.sf.ehcache.CacheManager#CacheManager(java.io.InputStream)
	 */
	public void setConfigLocation(Resource configLocation) {
		this.configLocation = configLocation;
	}
	
	/**
	 * Set whether the EhCache CacheManager should be shared (as a singleton at the VM level)
	 * or independent (typically local within the application). Default is "false", creating
	 * an independent instance.
	 * @see net.sf.ehcache.CacheManager#create()
	 * @see net.sf.ehcache.CacheManager#CacheManager()
	 */
	public void setShared(boolean shared) {
		this.shared = shared;
	}
	
	/**
	 * Set the name of the EhCache CacheManager (if a specific name is desired).
	 * @see net.sf.ehcache.CacheManager#setName(String)
	 */
	public void setCacheManagerName(String cacheManagerName) {
		this.cacheManagerName = cacheManagerName;
	}
	
	@Override
	public void afterPropertiesSet() throws IOException, CacheException {
		logger.info("Initializing EhCache CacheManager");
		InputStream is = this.extractEntandoConfig();
		try {
			// A bit convoluted for EhCache 1.x/2.0 compatibility.
			// To be much simpler once we require EhCache 2.1+
			if (this.cacheManagerName != null) {
				if (this.shared && createWithConfiguration == null) {
					// No CacheManager.create(Configuration) method available before EhCache 2.1;
					// can only set CacheManager name after creation.
					this.cacheManager = (is != null ? CacheManager.create(is) : CacheManager.create());
					this.cacheManager.setName(this.cacheManagerName);
				} else {
					Configuration configuration = (is != null ? ConfigurationFactory.parseConfiguration(is) :
							ConfigurationFactory.parseConfiguration());
					configuration.setName(this.cacheManagerName);
					if (this.shared) {
						this.cacheManager = (CacheManager) ReflectionUtils.invokeMethod(createWithConfiguration, null, configuration);
					} else {
						this.cacheManager = new CacheManager(configuration);
					}
				}
			} else if (this.shared) {
				// For strict backwards compatibility: use simplest possible constructors...
				this.cacheManager = (is != null ? CacheManager.create(is) : CacheManager.create());
			} else {
				this.cacheManager = (is != null ? new CacheManager(is) : new CacheManager());
			}
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}
	
	public InputStream extractEntandoConfig() throws IOException, CacheException {
		InputStream is1 = (this.configLocation != null ? this.configLocation.getInputStream() : null);
		if (null == is1) {
			return null;
		}
		InputStream is2 = null;
		try {
			String text = FileTextReader.getText(is1);
			String cacheFolder = System.getProperty("java.io.tmpdir") + File.separator + this.getApplicationBaseUrl().hashCode();
			text = text.replaceFirst(CACHE_DISK_ROOT_FOLDER_MARKER, cacheFolder);
			byte[] bytes = text.getBytes("UTF-8");
			is2 = new ByteArrayInputStream(bytes);
		} catch (Throwable t) {
			_logger.error("Error detected while extracting entando cache config", t);
			throw new CacheException("Error detected while extracting entando cache config", t);
		} finally {
			is1.close();
		}
		return is2;
	}
	
	@Override
	public CacheManager getObject() {
		return this.cacheManager;
	}
	
	@Override
	public Class<? extends CacheManager> getObjectType() {
		return (this.cacheManager != null ? this.cacheManager.getClass() : CacheManager.class);
	}
	
	@Override
	public boolean isSingleton() {
		return true;
	}
	
	@Override
	public void destroy() {
		logger.info("Shutting down EhCache CacheManager");
		this.cacheManager.shutdown();
	}
	
	protected String getApplicationBaseUrl() {
		return _applicationBaseUrl;
	}
	public void setApplicationBaseUrl(String applicationBaseUrl) {
		this._applicationBaseUrl = applicationBaseUrl;
	}
	
	private String _applicationBaseUrl;
	
	private final String CACHE_DISK_ROOT_FOLDER_MARKER = "@cacheDiskRootFolder@";
	
}
