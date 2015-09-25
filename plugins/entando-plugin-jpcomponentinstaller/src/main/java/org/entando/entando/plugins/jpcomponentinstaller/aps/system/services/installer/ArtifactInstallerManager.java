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
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.repository.internal.MavenRepositorySystemUtils;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.repository.Authentication;
import org.eclipse.aether.repository.LocalRepository;
import org.eclipse.aether.repository.RemoteRepository;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.util.repository.AuthenticationBuilder;
import org.eclipse.aether.version.Version;
import org.entando.entando.aps.system.init.IComponentManager;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.services.storage.IStorageManager;
import org.entando.entando.plugins.jpcomponentinstaller.aps.aetherutil.manual.ManualRepositorySystemFactory;
import org.entando.entando.plugins.jpcomponentinstaller.aps.system.InstallerSystemConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class ArtifactInstallerManager extends AbstractService implements IArtifactInstallerManager {
	
	private static final Logger _logger = LoggerFactory.getLogger(ArtifactInstallerManager.class);
	
	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public List<String> findAvailableVersions(Integer availableComponentId) throws ApsSystemException {
		List<String> versionsArray = new ArrayList<String>();
		AvailableArtifact availableArtifact = null;
		try {
			if (null == availableComponentId) {
				return null;
			}
			availableArtifact = this.getComponentCatalogueManager().getArtifact(availableComponentId);
			if (null == availableArtifact) {
				return null;
			}
			String entandoVersion = this.getConfigManager().getParam("version");
			RepositorySystem system = this.newSystem();
			RepositorySystemSession localSession = this.newSession(system);
			
			StringBuilder builder = new StringBuilder();
			builder.append(availableArtifact.getGroupId()).append(":").append(availableArtifact.getArtifactId());
			builder.append(":[").append(entandoVersion).append(",)");
			
			Artifact artifact = new DefaultArtifact(builder.toString());
			VersionRangeRequest rangeRequest = new VersionRangeRequest();
			rangeRequest.setArtifact(artifact);
			rangeRequest.setRepositories(this.newRepositories());
			VersionRangeResult rangeResult = system.resolveVersionRange(localSession, rangeRequest);
			List<Version> versions = rangeResult.getVersions();
			for (int i = 0; i < versions.size(); i++) {
				Version version = versions.get(i);
				String componentVersion = version.toString();
				if (componentVersion.startsWith(entandoVersion)) {
					versionsArray.add(componentVersion);
				}
			}
		} catch (Throwable t) {
			availableArtifact = (null != availableArtifact) ? availableArtifact : new AvailableArtifact();
			_logger.error("Error extracting version for component '{}' - '{}'", 
					availableArtifact.getGroupId(), availableArtifact.getArtifactId(), t);
		}
		return versionsArray;
	}
	
	private RepositorySystemSession newSession(RepositorySystem system) {
        DefaultRepositorySystemSession session = MavenRepositorySystemUtils.newSession();
		String homeDir = System.getProperty("user.home");
        LocalRepository localRepo = new LocalRepository(homeDir + "/.m2/repository");
        session.setLocalRepositoryManager(system.newLocalRepositoryManager(session, localRepo));
        return session;
    }
	
	private RepositorySystem newSystem() {
		return ManualRepositorySystemFactory.newRepositorySystem();
	}
	
	@Override
	public boolean downloadArtifact(Integer availableComponentId, String version) throws ApsSystemException {
		AvailableArtifact aa = null;
		try {
			if (null == availableComponentId) {
				return false;
			}
			aa = this.getComponentCatalogueManager().getArtifact(availableComponentId);
			if (null == aa) {
				return false;
			}
			RepositorySystem system = this.newSystem();
			RepositorySystemSession localSession = this.newSession(system);
			List<RemoteRepository> remoteRepositories = this.newRepositories();
			ArtifactRequest artifactRequest = new ArtifactRequest();
            Artifact artifact = new DefaultArtifact(aa.getGroupId(), aa.getArtifactId(), "", "war", version);
			artifactRequest.setArtifact(artifact);
			artifactRequest.setRepositories(remoteRepositories);
			ArtifactResult artifactResult = system.resolveArtifact(localSession, artifactRequest);
			artifact = artifactResult.getArtifact();
			String basePath = "plugins/jpcomponentinstaller/";
			InputStream ais = new FileInputStream(artifact.getFile());
			String filename = basePath + aa.getGroupId() + "_" + aa.getArtifactId() + "_" + version + ".war";
			if (!this.getStorageManager().exists(filename, true)) {
				this.getStorageManager().saveFile(filename, true, ais);
			}
			return true;
		} catch (Throwable t) {
			aa = (null != aa) ? aa : new AvailableArtifact();
			_logger.error("Error downloading artifact '{}' - '{}'", 
					aa.getGroupId(), aa.getArtifactId(), t);
			return false;
		}
	}
    
    @Override
    public boolean installArtifact(Integer availableComponentId, String version) throws ApsSystemException {
        AvailableArtifact aa = null;
		try {
			if (null == availableComponentId) {
				return false;
			}
			aa = this.getComponentCatalogueManager().getArtifact(availableComponentId);
			if (null == aa) {
				return false;
			}
            String basePath = "plugins/jpcomponentinstaller/";
			String filename = basePath + aa.getGroupId() + "_" + aa.getArtifactId() + "_" + version + ".war";
            if (!this.getStorageManager().exists(filename, true)) {
				return false;
			}
            InputStream is = this.getStorageManager().getStream(filename, true);
            this.getComponentInstaller().install(aa, version, is);
			return true;
		} catch (Throwable t) {
			aa = (null != aa) ? aa : new AvailableArtifact();
			_logger.error("Error installing artifact '{}' - '{}'", 
					aa.getGroupId(), aa.getArtifactId(), t);
			return false;
		}
    }
	
	@Override
	public boolean uninstallArtifact(Integer availableComponentId) throws ApsSystemException {
		AvailableArtifact aa = null;
		try {
			if (null == availableComponentId) {
				return false;
			}
			aa = this.getComponentCatalogueManager().getArtifact(availableComponentId);
			if (null == aa) {
				return false;
			}
			Component componentToRemove = null;
			List<Component> components = this.getComponentManager().getCurrentComponents();
			for (int i = 0; i < components.size(); i++) {
				Component component = components.get(i);
				boolean checkGroupId = (null != component.getArtifactGroupId()) ? component.getArtifactGroupId().equals(aa.getGroupId()) : false;
				boolean checkArtifactId = (null != component.getArtifactId()) ? component.getArtifactId().equals(aa.getArtifactId()) : false;
				if (checkArtifactId && checkGroupId) {
					componentToRemove = component;
					break;
				}
			}
			if (null == componentToRemove) {
				return false;
			}
			this.getComponentUninstaller().uninstallComponent(componentToRemove);
		} catch (Throwable t) {
			aa = (null != aa) ? aa : new AvailableArtifact();
			_logger.error("Error uninstalling artifact '{}' - '{}'", 
					aa.getGroupId(), aa.getArtifactId(), t);
			return false;
		}
		return true;
	}
	/*
	@Override
	public boolean uninstallArtifact(Component component) throws ApsSystemException {
		try {
			if (null == component || null == component.getUninstallerInfo()) {
				return false;
			}
			//backup database
			//move resources (jar, files and folders) on temp folder
			//remove records from db
			//drop tables
			//upgrade report
		} catch (Exception e) {
			//restore files on temp folder
			return false;
		} finally {
			//clean temp folder
		}
		return true;
	}
	*/
	private List<RemoteRepository> newRepositories() {
		List<RemoteRepository> list = new ArrayList<RemoteRepository>();
		list.add(this.newCentralRepository());
		RemoteRepository remote = this.newRemoteRepository();
		if (null != remote) {
			list.add(remote);
		}
        return list;
    }
	
    private RemoteRepository newCentralRepository() {
        return new RemoteRepository.Builder("central", "default", "http://central.maven.org/maven2/" ).build();
    }
	
    private RemoteRepository newRemoteRepository() {
		String param = this.getConfigManager().getParam(InstallerSystemConstants.NEXUS_REPOSITORY_ACTIVE_PARAM_NAME);
		boolean active = (null != param && Boolean.parseBoolean(param));
		if (!active) {
			return null;
		}
		String nexusUrl = this.getConfigManager().getParam(InstallerSystemConstants.NEXUS_REPOSITORY_URL_PARAM_NAME);
		String nexusUsername = this.getConfigManager().getParam(InstallerSystemConstants.NEXUS_REPOSITORY_USERNAME_PARAM_NAME);
		String nexusPassword = this.getConfigManager().getParam(InstallerSystemConstants.NEXUS_REPOSITORY_PASSWORD_PARAM_NAME);
		Authentication auth = new AuthenticationBuilder().addUsername(nexusUsername).addPassword(nexusPassword).build();
        RemoteRepository nexus =
            new RemoteRepository.Builder("nexus", "default", nexusUrl).setAuthentication(auth).build();
		return nexus;
    }
	
	protected IStorageManager getStorageManager() {
		return _storageManager;
	}
	public void setStorageManager(IStorageManager storageManager) {
		this._storageManager = storageManager;
	}
	
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	
	protected IComponentCatalogueManager getComponentCatalogueManager() {
		return _componentCatalogueManager;
	}
	public void setComponentCatalogueManager(IComponentCatalogueManager componentCatalogueManager) {
		this._componentCatalogueManager = componentCatalogueManager;
	}
	
	protected IComponentUninstaller getComponentUninstaller() {
		return _componentUninstaller;
	}
	public void setComponentUninstaller(IComponentUninstaller componentUninstaller) {
		this._componentUninstaller = componentUninstaller;
	}
	
	protected IComponentManager getComponentManager() {
		return _componentManager;
	}
	public void setComponentManager(IComponentManager componentManager) {
		this._componentManager = componentManager;
	}
	
	protected IComponentInstaller getComponentInstaller() {
		return _componentInstaller;
	}
	public void setComponentInstaller(IComponentInstaller componentInstaller) {
		this._componentInstaller = componentInstaller;
	}
    
	private IStorageManager _storageManager;
	private ConfigInterface _configManager;
	private IComponentCatalogueManager _componentCatalogueManager;
	private IComponentUninstaller _componentUninstaller;
	private IComponentManager _componentManager;
    private IComponentInstaller _componentInstaller;
	
}
