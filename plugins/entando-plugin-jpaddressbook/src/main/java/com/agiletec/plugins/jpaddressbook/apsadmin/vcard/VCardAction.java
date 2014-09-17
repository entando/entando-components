/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
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
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpaddressbook.apsadmin.vcard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.struts2.interceptor.ServletRequestAware;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.apsadmin.system.entity.type.AbstractEntityConfigAction;
import com.agiletec.plugins.jpaddressbook.aps.system.JpaddressbookSystemConstants;
import com.agiletec.plugins.jpaddressbook.aps.system.services.addressbook.IAddressBookManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.IVCardManager;
import com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.model.VCardContactField;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;

/**
 * @author A.Cocco
 */
public class VCardAction extends AbstractEntityConfigAction implements IVCardAction, ServletRequestAware {
	
	@Override
	public void validate() {
		super.validate();
		Map<String, String> profileMapping = new HashMap<String, String>();
		try {
			List<VCardContactField> vCardFields = this.getVcardManager().getVCardFields();
			Set<String> selectedFields = this.getSelectedFields();
			IApsEntity apsEntity = this.getUserProfileManager().getDefaultProfileType();
			for (VCardContactField field : vCardFields) {
				String fieldCode = field.getCode();
				if (selectedFields.contains(fieldCode)) {
					String paramName = "sel_" + fieldCode;
					String profileAttribute = this.getRequest().getParameter(paramName);
					if (profileAttribute == null || apsEntity.getAttribute(profileAttribute) == null) {
						String[] args = { this.getText("jpaddressbook.vcard.field." + fieldCode) };
						this.addFieldError(paramName, this.getText("Errors.vcardConfig.attribute.notValid", args));
					} else {
						profileMapping.put(fieldCode, profileAttribute);
					}
				}
			}
		} catch (ApsSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.setProfileMapping(profileMapping);
	}
	
	@Override
	public String edit() {
		try {
			List<VCardContactField> vCardFields = this.getVcardManager().getVCardFields();
			Set<String> selectedFields = new TreeSet<String>();
			for (VCardContactField field : vCardFields) {
				if (field.isEnabled()) {
					selectedFields.add(field.getCode());
				}
			}
			this.setSelectedFields(selectedFields);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	@Override
	public String filterFields() {
		try {
			List<VCardContactField> vCardFields = this.getVcardManager().getVCardFields();
			Set<String> selectedFields = this.getSelectedFields();
			Map<String, String> profileMapping = new HashMap<String, String>();
			for (VCardContactField field : vCardFields) {
				String fieldCode = field.getCode();
				if (selectedFields.contains(fieldCode)) {
					profileMapping.put(fieldCode, field.getProfileAttribute());
				}
			}
			this.setProfileMapping(profileMapping);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	/**
	 * Save the vcard configuration
	 * @return true if the result of configuration's save it's ok.
	 */
	public String save() {
		List<VCardContactField> fields = new ArrayList<VCardContactField>();
		try {
			List<VCardContactField> vCardFields = this.getVcardManager().getVCardFields();
			Set<String> selectedFields = this.getSelectedFields();
			for (VCardContactField field : vCardFields) {
				String fieldCode = field.getCode();
				String profileAttribute = this.getRequest().getParameter("sel_" + field.getCode());
				field.setEnabled(selectedFields.contains(fieldCode));
				field.setProfileAttribute(profileAttribute);
				fields.add(field);
			}
			this.getVcardManager().updateVCardMapping(vCardFields);
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "save");
			return FAILURE;
		}
		return SUCCESS;
	}
	
	public List<VCardContactField> getVCardFields() {
		List<VCardContactField> vCardFields = null;
		try {
			vCardFields = this.getVcardManager().getVCardFields();
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getVCardFields");
			throw new RuntimeException(t);
		}
		return vCardFields;
	}
	
	/**
	 * Returns vcard inputstream
	 * @return vcard inputstream
	 */
	public InputStream getInputStream() {
		InputStream vcardInputStream = null;
		this.setNameFile("vcard_" + DateConverter.getFormattedDate(new java.util.Date(),"dd/MM/yyyy") + ".vcf");
		try {
			String vcard = this.getVcardManager().getVCard(this.getEntityId());
			vcardInputStream = new ByteArrayInputStream(vcard.getBytes("UTF-8"));
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "getInputStream");
		}
		return vcardInputStream;
	}
	
	/**
	 * Download VCard
	 * @return VCard
	 * @throws Exception
	 */
	public String downloadVCard() {
		return SUCCESS;
	}
	
	/**
	 * Returns the valid VCard fields
	 * @return the valid VCard fields
	 */
	public List<AttributeInterface> getEntityFields() {
		List<AttributeInterface> entityFields = new ArrayList<AttributeInterface>();
		IApsEntity apsEntity = this.getUserProfileManager().getDefaultProfileType();
		List<AttributeInterface>  attributeInterfaces = apsEntity.getAttributeList();
		if(attributeInterfaces != null){
			for (int i = 0; i < attributeInterfaces.size(); i++) {
				AttributeInterface attribute = attributeInterfaces.get(i);
				if(JpaddressbookSystemConstants.ATTRIBUTE_TYPE_DATE.equals(attribute.getType().trim())
						|| JpaddressbookSystemConstants.ATTRIBUTE_TYPE_ENUMERATOR.equals(attribute.getType().trim())
						|| JpaddressbookSystemConstants.ATTRIBUTE_TYPE_HYPERTEXT.equals(attribute.getType().trim())
						|| JpaddressbookSystemConstants.ATTRIBUTE_TYPE_LONGTEXT.equals(attribute.getType().trim())
						|| JpaddressbookSystemConstants.ATTRIBUTE_TYPE_MONOTEXT.equals(attribute.getType().trim())
						|| JpaddressbookSystemConstants.ATTRIBUTE_TYPE_NUMBER.equals(attribute.getType().trim())
						|| JpaddressbookSystemConstants.ATTRIBUTE_TYPE_TEXT.equals(attribute.getType().trim())){
					entityFields.add(attribute);
				}
			}
		}
		return entityFields;
	}
	
	public Set<String> getSelectedFields() {
		return _selectedFields;
	}
	public void setSelectedFields(Set<String> selectedFields) {
		this._selectedFields = selectedFields;
	}
	
	public Map<String, String> getProfileMapping() {
		return _profileMapping;
	}
	protected void setProfileMapping(Map<String, String> profileMapping) {
		this._profileMapping = profileMapping;
	}
	
	/**
	 * Sets entity ID
	 * @param entityId the entityId to set
	 */
	public void setEntityId(String entityId) {
		this._entityId = entityId;
	}
	
	/**
	 * Returns entity ID
	 * @return the entityId
	 */
	public String getEntityId() {
		return _entityId;
	}
	
	protected IVCardManager getVcardManager() {
		return _vcardManager;
	}
	public void setVcardManager(IVCardManager vcardManager) {
		this._vcardManager = vcardManager;
	}
	
	protected IAddressBookManager getAddressBookManager() {
		return _addressBookManager;
	}
	public void setAddressBookManager(IAddressBookManager addressBookManager) {
		this._addressBookManager = addressBookManager;
	}
	
	/**
	 * Sets file name
	 * @param nameFile the nameFile to set
	 */
	public void setNameFile(String nameFile) {
		this._nameFile = nameFile;
	}
	/**
	 * Returns file name
	 * @return the nameFile
	 */
	public String getNameFile() {
		return _nameFile;
	}
	
	protected IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}
	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}
	
	private Set<String> _selectedFields = new TreeSet<String>();
	
	private Map<String, String> _profileMapping;
	private String _entityId;
	private String _nameFile;
	
	private IAddressBookManager _addressBookManager;
	private IUserProfileManager _userProfileManager;
	private IVCardManager _vcardManager;
	
}