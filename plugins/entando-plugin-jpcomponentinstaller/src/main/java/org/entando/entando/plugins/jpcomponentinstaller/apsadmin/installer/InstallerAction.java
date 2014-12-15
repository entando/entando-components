/*
*
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software.
* Entando is a free software;
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package org.entando.entando.plugins.jpcomponentinstaller.apsadmin.installer;

import com.agiletec.aps.util.SelectItem;
import com.agiletec.apsadmin.system.BaseAction;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

import org.entando.entando.aps.system.init.IInitializerManager;
import org.entando.entando.aps.system.init.model.Component;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer.AvailableArtifact;
import org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer.IArtifactInstallerManager;
import org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer.IComponentCatalogueManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author E.Santoboni
 */
public class InstallerAction extends BaseAction {
	
	private static final Logger _logger =  LoggerFactory.getLogger(InstallerAction.class);
	
	public String chooseVersion() {
		try {
			String result = this.checkArtifactId();
			if (null != result) return result;
		} catch (Throwable t) {
			_logger.error("error on chooseVersion", t);
		}
		return SUCCESS;
	}
	
	public String downloadIntro() {
		try {
			String result = this.checkArtifactId();
			if (null != result) return result;
		} catch (Throwable t) {
			_logger.error("error on downloadIntro", t);
		}
		return SUCCESS;
	}
	
	public String installIntro() {
		try {
			String result = this.checkVersion();
			if (null != result) return result;
			boolean downloadResult = this.getArtifactInstallerManager()
					.downloadArtifact(this.getAvailableArtifactId(), this.getVersion());
            if (!downloadResult) {
                AvailableArtifact aa = this.getArtifactToInstall();
                String[] args = {aa.getDescription(), aa.getGroupId(), aa.getArtifactId(), this.getVersion()};
                this.addActionError(this.getText("jpcomponentinstaller.error.artifact.download", args));
                return "intro";
            }
		} catch (Throwable t) {
			_logger.error("error on installIntro", t);
		}
		return SUCCESS;
	}
	
	public String install() {
		try {
			String result = this.checkVersion();
			if (null != result) return result;
			boolean installResult = this.getArtifactInstallerManager()
					.installArtifact(this.getAvailableArtifactId(), this.getVersion());
            AvailableArtifact aa = this.getArtifactToInstall();
			String[] args = {aa.getDescription(), aa.getGroupId(), aa.getArtifactId(), this.getVersion()};
			if (!installResult) {
                this.addActionError(this.getText("jpcomponentinstaller.error.artifact.install", args));
                return "intro";
            }
			this.addActionMessage(this.getText("jpcomponentinstaller.message.artifact.installDone", args));
		} catch (Throwable t) {
			_logger.error("error on install", t);
		}
		return SUCCESS;
	}
	
	protected String checkArtifactId() {
		Integer id = this.getAvailableArtifactId();
		if (null == id) {
			return "intro";
		}
		AvailableArtifact availableArtifact = this.getComponentCatalogueManager().getArtifact(id);
		if (null == availableArtifact) {
			this.addActionError(this.getText("jpcomponentinstaller.error.artifact.notExists"));
			return "intro";
		}
		if (this.isInstalledArtifact(availableArtifact)) {
			String[] args = {availableArtifact.getDescription()};
			this.addActionError(this.getText("jpcomponentinstaller.error.artifact.alreadyInstalled", args));
			return "intro";
		}
		this.setArtifactToInstall(availableArtifact);
		return null;
	}
	
	protected String checkVersion() {
		String result = this.checkArtifactId();
		if (null != result) return result;
		String version = this.getVersion();
		if (StringUtils.isEmpty(version)) {
			this.addFieldError("version", this.getText("jpcomponentinstaller.error.artifact.nullVersion"));
			return "chooseVersion";
		}
		if (!this.getArtifactVersions(this.getAvailableArtifactId()).contains(version)) {
			String[] args = {version};
			this.addFieldError("version", this.getText("jpcomponentinstaller.error.artifact.invalidVersion", args));
			return "chooseVersion";
		}
		return null;
	}
	
	public List<SelectItem> getAvailableComponents() {
		List<SelectItem> items = new ArrayList<SelectItem>();
		List<AvailableArtifact> artifacts = this.getComponentCatalogueManager().getArtifacts();
		for (int i = 0; i < artifacts.size(); i++) {
			AvailableArtifact artifact = artifacts.get(i);
			if (!this.isInstalledArtifact(artifact)) {
				SelectItem item = new SelectItem(artifact.getId().toString(), artifact.getDescription(), artifact.getLabel());
				items.add(item);
			}
		}
		return items;
	}
	
	protected boolean isInstalledArtifact(AvailableArtifact artifact) {
		if (null == artifact) {
			return false;
		}
		List<Component> components = this.getComponentManager().getCurrentComponents();
		for (int i = 0; i < components.size(); i++) {
			Component component = components.get(i);
			String artifactId = component.getArtifactId();
			String groupId = component.getArtifactGroupId();
			if (null != groupId && null != artifactId) {
				if (groupId.equals(artifact.getGroupId()) && artifactId.equals(artifact.getArtifactId())) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<String> getArtifactVersions(Integer availableArtifactId) {
		try {
			return this.getArtifactInstallerManager().findAvailableVersions(availableArtifactId);
		} catch (Throwable t) {
			_logger.error("error extracting artifact versions", t);
			return new ArrayList<String>();
		}
	}
	
	public Component getComponent(String code) {
		return this.getComponentManager().getInstalledComponent(code);
	}
	
	public SystemInstallationReport getCurrentReport() {
		return this.getInitializerManager().getCurrentReport();
	}
	
	public String uninstallIntro() {
		try {
			String result = this.checkComponentToUninstall();
			if (null != result) return result;
		} catch (Throwable t) {
			_logger.error("error on uninstallIntro", t);
		}
		return SUCCESS;
	}
	
	public String uninstall() {
		try {
			String result = this.checkComponentToUninstall();
			if (null != result) return result;
			Component component = this.getComponent(this.getComponentCode());
			AvailableArtifact artifact = 
				this.getComponentCatalogueManager().getArtifact(component.getArtifactGroupId(), component.getArtifactId());
			this.getArtifactInstallerManager().uninstallArtifact(artifact.getId());
			String[] args = {component.getDescription()};
			this.addActionMessage(this.getText("jpcomponentinstaller.message.component.uninstallDone", args));
		} catch (Throwable t) {
			_logger.error("error on uninstall", t);
		}
		return SUCCESS;
	}
	
	protected String checkComponentToUninstall() {
		String componentCode = this.getComponentCode();
		if (null == componentCode) {
			return "intro";
		}
		Component component = this.getComponent(componentCode);
		if (null == component) {
			this.addActionError(this.getText("jpcomponentinstaller.error.component.notInstalled"));
			return "intro";
		}
		if (null == component.getUninstallerInfo()) {
			String[] args = {component.getDescription()};
			this.addActionError(this.getText("jpcomponentinstaller.error.component.uninstallable", args));
			return "intro";
		}
		AvailableArtifact artifact = 
				this.getComponentCatalogueManager().getArtifact(component.getArtifactGroupId(), component.getArtifactId());
		if (null == artifact) {
			String[] args = {component.getDescription()};
			this.addActionError(this.getText("jpcomponentinstaller.error.component.uninstallable", args));
			return "intro";
		}
		boolean hasDependencies = false;
		StringBuilder builder = new StringBuilder();
		List<Component> components = this.getComponentManager().getCurrentComponents();
		for (int i = 0; i < components.size(); i++) {
			Component installedComponent = components.get(i);
			List<String> dependencies = installedComponent.getDependencies();
			if (null == dependencies || !dependencies.contains(component.getCode())) {
				continue;
			}
			if (hasDependencies) {
				builder.append(" - ");
			}
			builder.append("'").append(installedComponent.getDescription()).append("'");
			hasDependencies = true;
		}
		if (hasDependencies) {
			String[] args = {builder.toString()};
			this.addActionError(this.getText("jpcomponentinstaller.error.component.locked", args));
			return "intro";
		}
		return null;
	}
	
	public Integer getAvailableArtifactId() {
		return _availableArtifactId;
	}
	public void setAvailableArtifactId(Integer availableArtifactId) {
		this._availableArtifactId = availableArtifactId;
	}
	
	public String getVersion() {
		return _version;
	}
	public void setVersion(String version) {
		this._version = version;
	}
	
	public AvailableArtifact getArtifactToInstall() {
		return _artifactToInstall;
	}
	public void setArtifactToInstall(AvailableArtifact artifactToInstall) {
		this._artifactToInstall = artifactToInstall;
	}
	
	public String getComponentCode() {
		return _componentCode;
	}
	public void setComponentCode(String componentCode) {
		this._componentCode = componentCode;
	}
	
	protected IInitializerManager getInitializerManager() {
		return _initializerManager;
	}
	public void setInitializerManager(IInitializerManager initializerManager) {
		this._initializerManager = initializerManager;
	}
	
	protected IComponentCatalogueManager getComponentCatalogueManager() {
		return _componentCatalogueManager;
	}
	public void setComponentCatalogueManager(IComponentCatalogueManager componentCatalogueManager) {
		this._componentCatalogueManager = componentCatalogueManager;
	}
	
	protected IArtifactInstallerManager getArtifactInstallerManager() {
		return _artifactInstallerManager;
	}
	public void setArtifactInstallerManager(IArtifactInstallerManager artifactInstallerManager) {
		this._artifactInstallerManager = artifactInstallerManager;
	}
	
	private Integer _availableArtifactId;
	private String _version;
	
	private AvailableArtifact _artifactToInstall;
	
	private String _componentCode;
	
	private IInitializerManager _initializerManager;
	private IComponentCatalogueManager _componentCatalogueManager;
	private IArtifactInstallerManager _artifactInstallerManager;
	
}