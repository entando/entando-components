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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ognl.InappropriateExpressionException;
import ognl.MethodFailedException;
import ognl.NoSuchPropertyException;
import ognl.Ognl;
import ognl.OgnlContext;

import org.apache.commons.lang.StringUtils;
import org.entando.entando.aps.system.services.userprofile.IUserProfileManager;
import org.entando.entando.aps.system.services.userprofile.model.IUserProfile;
import org.entando.entando.plugins.jpwebform.aps.system.services.JpwebformSystemConstants;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.events.FormSubmittedEvent;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.parse.FormTypeDOM;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.parse.MessageNotifierConfigDOM;
import org.entando.entando.plugins.jpwebform.aps.system.services.form.parse.StepConfigsDOM;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Answer;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageModel;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageRecordVO;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.MessageTypeNotifierConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.SmallMessageType;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepGuiConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.TypeVersionGuiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.IEntityDAO;
import com.agiletec.aps.system.common.entity.IEntitySearcherDAO;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingEvent;
import com.agiletec.aps.system.common.entity.event.EntityTypesChangingObserver;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.common.entity.model.attribute.CompositeAttribute;
import com.agiletec.aps.system.common.entity.model.attribute.ITextAttribute;
import com.agiletec.aps.system.common.entity.parse.EntityHandler;
import com.agiletec.aps.system.common.renderer.IEntityRenderer;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.i18n.II18nManager;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import com.agiletec.aps.system.services.lang.ILangManager;
import com.agiletec.aps.system.services.lang.Lang;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jpmail.aps.services.mail.IMailManager;
import com.agiletec.plugins.jpmail.aps.services.mail.MailSendersUtilizer;

/**
 * Implementation of the Manager of Form Message Object.
 *
 * @author E.Santoboni
 */
public class FormManager extends ApsEntityManager implements IFormManager, EntityTypesChangingObserver, MailSendersUtilizer {

	private static final Logger _logger =  LoggerFactory.getLogger(FormManager.class);

	@Override
	public void init() throws Exception {
		super.init();
		this.initSmallMessageTypes();
		this.loadNotifierConfig();
		this.loadStepsConfig();
		this.checkConfig();
		_logger.debug("{} ready. Initialized {} entity types", this.getClass().getName(), super.getEntityTypes().size());
	}

	@Override
	protected void release() {
		super.release();
		this.getLastVersionConfigs().clear();
		this.getTypeVersionConfigs().clear();
	}

	@Override
	public Message resumeMessageByUserAndType(String user, String typeCode, Integer status) throws ApsSystemException {
		EntitySearchFilter userFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_USERNAME, false, user, false);
		EntitySearchFilter typeFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_MESSAGE_TYPE, false, typeCode, false);
		EntitySearchFilter dateFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_SEND_DATE, false);
		EntitySearchFilter statusFilter = new EntitySearchFilter(IFormManager.FILTER_KEY_DONE, false, status, false);
		dateFilter.setOrder(EntitySearchFilter.DESC_ORDER);
		EntitySearchFilter[] filters = new EntitySearchFilter[]{userFilter, typeFilter, dateFilter, statusFilter};
		List<String> searchId = this.searchId(filters);
		if (null != searchId && !searchId.isEmpty()) {
			return this.getMessage(searchId.get(0));
		} else {
			return null;
		}
	}

	/**
	 * Initialize the SmallMessageType list and map.
	 */
	protected void initSmallMessageTypes() {
		Map<String, SmallMessageType> smallMessageTypes = new HashMap<String, SmallMessageType>(this.getEntityTypes().size());
		List<IApsEntity> types = new ArrayList<IApsEntity>(this.getEntityTypes().values());
		for (int i = 0; i < types.size(); i++) {
			IApsEntity type = types.get(i);
			if (type instanceof Message) {
				if (0 != ((Message) type).getVersionType().intValue()) {
					SmallMessageType smallMessageType = new SmallMessageType();
					smallMessageType.setCode(type.getTypeCode());
					smallMessageType.setDescr(type.getTypeDescr());
					smallMessageTypes.put(smallMessageType.getCode(), smallMessageType);
				}
			}
		}
		this.setSmallMessageTypesMap(smallMessageTypes);
	}

	protected void loadNotifierConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpwebformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpwebformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM);
			}
			MessageNotifierConfigDOM configDOM = new MessageNotifierConfigDOM();
			this.setNotifierConfigMap(configDOM.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error initializing the configuration", t);
			throw new ApsSystemException("Error initializing the configuration", t);
		}
	}

	protected void loadStepsConfig() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpwebformSystemConstants.MESSAGE_STEPS_CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpwebformSystemConstants.MESSAGE_STEPS_CONFIG_ITEM);
			}
			StepConfigsDOM dom = new StepConfigsDOM();
			this.setStepsConfig(dom.extractConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading Steps Config", t);
			throw new ApsSystemException("Error loading Steps Config", t);
		}
	}

	@Override
	public void deleteTypeVersion(String typeCode) throws ApsSystemException {
		try {
			this.getFormTypeGuiDAO().deleteTypeVersion(typeCode);
		} catch (Throwable t) {
			_logger.error("Error removing typeVersion configuration", t);
			this.loadNotifierConfig();
			throw new ApsSystemException("Error removing typeVersion configuration");
		}
	}

	@Override
	public void updateFromEntityTypesChanging(EntityTypesChangingEvent event) {
		try {
			if (this.getName().equals(event.getEntityManagerName())) {
				switch (event.getOperationCode()) {
					case EntityTypesChangingEvent.REMOVE_OPERATION_CODE:
						String typeCode = event.getOldEntityType().getTypeCode();
						this.removeNotifierConfig(typeCode);
						_logger.trace("Removed notifier configuration for entity type {}", typeCode);
						break;
					case EntityTypesChangingEvent.INSERT_OPERATION_CODE:
						MessageTypeNotifierConfig config = new MessageTypeNotifierConfig();
						config.setTypeCode(event.getNewEntityType().getTypeCode());
						config.setStore(true);
						this.saveNotifierConfig(config);
						break;
					default:
						break;
				}
			}
		} catch (Throwable t) {
			_logger.error("error in updateFromEntityTypesChanging", t);
		}
	}

	/**
	 * Returns the notifier's configuration for a given type of message.
	 *
	 * @param type The type of the message.
	 * @return The notifier's configuration for a given type of message.
	 */
	@Override
	public MessageTypeNotifierConfig getNotifierConfig(String typeCode) {
		return _messageNotifierConfigMap.get(typeCode);
	}

	@Override
	public void saveNotifierConfig(MessageTypeNotifierConfig config) throws ApsSystemException {
		try {
			MessageNotifierConfigDOM configDOM = new MessageNotifierConfigDOM();
			Map<String, MessageTypeNotifierConfig> configMap = this.getNotifierConfigMap();
			configMap.put(config.getTypeCode(), config);
			String xml = configDOM.createConfigXml(configMap);
			ConfigInterface configManager = this.getConfigManager();
			configManager.updateConfigItem(JpwebformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM, xml);
		} catch (Throwable t) {
			_logger.error("Error updating notifier configuration", t);
			this.loadNotifierConfig();
			throw new ApsSystemException("Error updating notifier configuration");
		}
	}

	@Override
	public Boolean validateOgnl(String expression, Message entity, String currentUser) throws ApsSystemException {
		Boolean value = true;
		if (StringUtils.isBlank(expression)) {
			return value;
		}
		try {
			Object expr = Ognl.parseExpression(expression);
			OgnlContext context = this.createContextForExpressionValidation(entity, currentUser);
			return (Boolean) Ognl.getValue(expr, context, entity, Boolean.class);
		} catch (Throwable t) {
			_logger.error("error in validateOgnl", t);
			throw new ApsSystemException("Error on validateOgnl");
		}
	}

	public OGNL_MESSAGES verifyOgnlExpression(String expression, Message entity, String currentUser) throws ApsSystemException {
		if (!StringUtils.isBlank(expression)) {
			try {
				Object expr = Ognl.parseExpression(expression);
				OgnlContext context = this.createContextForExpressionValidation(entity, currentUser);
				Ognl.getValue(expr, context, entity, Boolean.class);
			} catch (InappropriateExpressionException ex) {
				_logger.error("InappropriateExpressionException in expression {}", expression, ex);
				//Logger.getLogger(FormManager.class.getName()).log(Level.SEVERE, null, ex);
				return OGNL_MESSAGES.METHOD_ERROR;
			} catch (MethodFailedException ex) {
				_logger.error("MethodFailedException in expression {}", expression, ex);
				//Logger.getLogger(FormManager.class.getName()).log(Level.SEVERE, null, ex);
				return OGNL_MESSAGES.METHOD_ERROR;
			} catch (NoSuchPropertyException ex) {
				_logger.error("NoSuchPropertyException in expression {}", expression, ex);
				//Logger.getLogger(FormManager.class.getName()).log(Level.SEVERE, null, ex);
				return OGNL_MESSAGES.PROPERTY_ERROR;
			} catch (Throwable t) {
				return OGNL_MESSAGES.GENERIC_ERROR;
			}
		}
		return OGNL_MESSAGES.SUCCESS;
	}

	protected OgnlContext createContextForExpressionValidation(Message entity, String currentUser) throws ApsSystemException {
		OgnlContext context = new OgnlContext();
		Map<String, Lang> langs = new HashMap<String, Lang>();
		List<Lang> langList = this.getLangManager().getLangs();
		for (int i = 0; i < langList.size(); i++) {
			Lang lang = langList.get(i);
			langs.put(lang.getCode(), lang);
		}
		context.put("langs", langs);
		context.put("entity", entity);
		IUserProfile userProfile = this.getUserProfileManager().getProfile(currentUser);
		context.put("currentUser", userProfile);
		return context;
	}

	protected Map<String, MessageTypeNotifierConfig> getNotifierConfigMap() {
		return _messageNotifierConfigMap;
	}

	protected void setNotifierConfigMap(Map<String, MessageTypeNotifierConfig> messageNotifierConfigMap) {
		this._messageNotifierConfigMap = messageNotifierConfigMap;
	}

	/**
	 * Remove the notifier's configuration for the given type of message.
	 *
	 * @param type The type of the message.
	 * @throws ApsSystemException
	 */
	protected void removeNotifierConfig(String typeCode) throws ApsSystemException {
		try {
			MessageNotifierConfigDOM configDOM = new MessageNotifierConfigDOM();
			Map<String, MessageTypeNotifierConfig> configMap = this.getNotifierConfigMap();
			configMap.remove(typeCode);

			String xml = configDOM.createConfigXml(configMap);
			ConfigInterface configManager = this.getConfigManager();
			configManager.updateConfigItem(JpwebformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM, xml);
		} catch (Throwable t) {
			_logger.error("Error updating notifier configuration", t);
			this.loadNotifierConfig();
			throw new ApsSystemException("Error updating notifier configuration");
		}
	}

	protected void checkConfig() {
		Map<String, MessageTypeNotifierConfig> notifierConfig = this.getNotifierConfigMap();
		Set<String> messageTypes = this.getSmallMessageTypesMap().keySet();
		Iterator<String> iter = messageTypes.iterator();
		while (iter.hasNext()) {
			String messageType = iter.next();
			if (!notifierConfig.containsKey(messageType)) {
				_logger.warn("Message Type {} hasn't notifier configuration!", messageType );
			}
		}
	}

	@Override
	public String[] getSenderCodes() {
		Collection<MessageTypeNotifierConfig> notifierConfigs = this.getNotifierConfigMap().values();
		String[] senderCodes = new String[notifierConfigs.size()];
		int i = 0;
		for (MessageTypeNotifierConfig config : notifierConfigs) {
			senderCodes[i++] = config.getSenderCode();
		}
		return senderCodes;
	}

	@Override
	public Message createMessageType(String typeCode) {
		return (Message) super.getEntityPrototype(typeCode);
	}

	@Override
	public List<SmallMessageType> getSmallMessageTypes() {
		List<SmallMessageType> smallMessageTypes = new ArrayList<SmallMessageType>();
		smallMessageTypes.addAll(this._smallMessageTypes.values());
		Collections.sort(smallMessageTypes);
		return smallMessageTypes;
	}

	@Override
	public Map<String, SmallMessageType> getSmallMessageTypesMap() {
		return this._smallMessageTypes;
	}

	protected void setSmallMessageTypesMap(Map<String, SmallMessageType> smallMessageTypes) {
		this._smallMessageTypes = smallMessageTypes;
	}

	@Override
	public String getMailAttributeName(String typeCode) {
		MessageTypeNotifierConfig config = this.getNotifierConfig(typeCode);
		String mailAttrName = null;
		if (config != null) {
			mailAttrName = config.getMailAttrName();
		}
		return mailAttrName;
	}

	@Override
	public List<String> loadMessagesId(EntitySearchFilter[] filters) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = this.getEntitySearcherDao().searchId(filters);
		} catch (Throwable t) {
			_logger.error("Errore caricamento id message", t);
			throw new ApsSystemException("Errore caricamento id message", t);
		}
		return contentsId;
	}

	@Override
	public List<String> loadMessagesId(EntitySearchFilter[] filters, boolean answered) throws ApsSystemException {
		List<String> contentsId = null;
		try {
			contentsId = ((IFormSearcherDAO) this.getEntitySearcherDao()).searchId(filters, answered);
		} catch (Throwable t) {
			_logger.error("Errore caricamento id message", t);
			throw new ApsSystemException("Errore caricamento id message", t);
		}
		return contentsId;
	}

	@Override
	public Message getMessage(String id) throws ApsSystemException {
		Message message = null;
		try {
			MessageRecordVO messageRecord = (MessageRecordVO) this.getFormDAO().loadEntityRecord(id);
			if (messageRecord != null) {
				message = (Message) this.getTypeVersionGui(messageRecord.getTypeCode(), messageRecord.getVersionType()).getPrototype().getEntityPrototype();
				message.setLangCode(messageRecord.getLangCode());
				this.valueEntityFromXml(message, messageRecord.getXml());
				message.setUsername(messageRecord.getUsername());
				message.setCreationDate(messageRecord.getCreationDate());
				message.setCompleted(messageRecord.isCompleted());
				message.setSendDate(messageRecord.getSendDate());
			}
		} catch (Throwable t) {
			_logger.error("Error loading messageRecord", t);
			throw new ApsSystemException("Error loading messageRecord", t);
		}
		return message;
	}

//	@Override
	public Message getMessage(String id, TypeVersionGuiConfig config) throws ApsSystemException {
		Message message = null;
		try {
			MessageRecordVO messageRecord = (MessageRecordVO) this.getFormDAO().loadEntityRecord(id);
			if (messageRecord != null) {
				message = (Message) config.getPrototype().getEntityPrototype();
				message.setLangCode(messageRecord.getLangCode());
				this.valueEntityFromXml(message, messageRecord.getXml());
				message.setUsername(messageRecord.getUsername());
				message.setCreationDate(messageRecord.getCreationDate());
				message.setCompleted(messageRecord.isCompleted());
				message.setSendDate(messageRecord.getSendDate());
			}
		} catch (Throwable t) {
			_logger.error("Error loading messageRecord", t);
			throw new ApsSystemException("Error loading messageRecord", t);
		}
		return message;
	}

	protected void valueEntityFromXml(IApsEntity entityPrototype, String xml) throws ApsSystemException {
		try {
			SAXParserFactory parseFactory = SAXParserFactory.newInstance();
			SAXParser parser = parseFactory.newSAXParser();
			InputSource is = new InputSource(new StringReader(xml));
			EntityHandler handler = this.getEntityHandler();
			handler.initHandler(entityPrototype, this.getXmlAttributeRootElementName(), this.getCategoryManager());
			parser.parse(is, handler);
		} catch (Throwable t) {
			_logger.error("Error detected while creating the entity. xml:{}", xml, t);
			throw new ApsSystemException("Error detected while creating the entity", t);
		}
	}

	@Override
	public void addUpdateMessage(Message message) throws ApsSystemException {
		try {
			String id = message.getId();
			boolean newMessage = (null == id || null == this.getFormDAO().loadEntityRecord(id));
			if (newMessage) {
				int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
				id = message.getTypeCode() + key;
				message.setId(id);
				this.getFormDAO().addEntity(message);
			} else {
				this.getFormDAO().updateEntity(message);
			}
		} catch (Throwable t) {
			_logger.error("Error saving message", t);
			throw new ApsSystemException("Error saving message", t);
		}
	}

	@Override
	public void sendMessage(Message message) throws ApsSystemException {
		this.sendMessage(message, null);
	}

	@Override
	public void sendMessage(Message message, String customEmail) throws ApsSystemException {
		try {
			this.sendMessageNotification(message, customEmail);
			this.addUpdateMessage(message);
		} catch (Throwable t) {
			_logger.error("Error saving message", t);
			throw new ApsSystemException("Error saving message", t);
		}
	}

	@Override
	public void deleteMessage(String messageId) throws ApsSystemException {
		try {
			this.getFormDAO().deleteEntity(messageId);
		} catch (Throwable t) {
			_logger.error("Error deleting message {}", messageId, t);
			throw new ApsSystemException("Error deleting message " + messageId, t);
		}
	}

	@Override
	public boolean sendAnswer(Answer answer) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			answer.setAnswerId(String.valueOf(key));
			this.getFormDAO().addAnswer(answer);
			boolean sent = this.sendAnswerNotification(answer);
			return sent;
		} catch (Throwable t) {
			_logger.error("Error saving answer");
			throw new ApsSystemException("Error saving answer", t);
		}
	}

	@Override
	public List<Answer> getAnswers(String messageId) throws ApsSystemException {
		try {
			return this.getFormDAO().loadAnswers(messageId);
		} catch (Throwable t) {
			_logger.error("Error saving message", t);
			throw new ApsSystemException("Error saving message", t);
		}
	}

	protected boolean sendMessageNotification(Message message, String customEmail) throws ApsSystemException {
		boolean sent = false;
		try {
			this.notifyFormSubmittedEvent(message);
			MessageTypeNotifierConfig config = this.getNotifierConfig(message.getTypeCode());
			if (config != null && config.isNotifiable()) {
				MessageModel messageModel = null;
				String renderingLangCode = this.getLangManager().getDefaultLang().getCode();
				String subject = null;
				String text = null;
				String[] recipientsTo = null;
				if (customEmail != null && !customEmail.isEmpty()) {
					messageModel = config.getMessageModelResp();
					subject = this.getEntityRenderer().render(message, messageModel.getSubjectModel(), renderingLangCode, false);
					text = this.getEntityRenderer().render(message, messageModel.getBodyModel(), renderingLangCode, true);
					recipientsTo = new String[]{customEmail};
				} else {
					messageModel = config.getMessageModel();
					subject = this.getEntityRenderer().render(message, messageModel.getSubjectModel(), renderingLangCode, false);
					text = this.getEntityRenderer().render(message, messageModel.getBodyModel(), renderingLangCode, true);
					recipientsTo = config.getRecipientsTo();
				}
				String[] recipientsCc = config.getRecipientsCc();
				String[] recipientsBcc = config.getRecipientsBcc();
				String senderCode = config.getSenderCode();
				this.getMailManager().sendMail(text, subject,
						recipientsTo, recipientsCc, recipientsBcc, senderCode, IMailManager.CONTENTTYPE_TEXT_HTML);
				sent = true;
			} else {
				_logger.warn("Message notification not sent! Message lacking in notifier configuration.");
			}
		} catch (Throwable t) {
			_logger.error("Error sending notification to message {}", message.getId(), t);
			throw new ApsSystemException("Error sending notification to message " + message.getId(), t);
		}
		return sent;
	}

	protected boolean sendAnswerNotification(Answer answer) throws ApsSystemException {
		boolean sent = false;
		try {
			Message message = this.getMessage(answer.getMessageId());
			MessageTypeNotifierConfig config = this.getNotifierConfig(message.getTypeCode());
			if (config != null) {
				String email = this.extractUserMail(message, config);
				if (null != email) {
					String renderingLangCode = message.getLangCode();
					if (renderingLangCode == null || this.getLangManager().getLang(renderingLangCode) == null) {
						renderingLangCode = this.getLangManager().getDefaultLang().getCode();
					}
					MessageModel messageModel = config.getMessageModel();
					String subject = this.getEntityRenderer().render(message, messageModel.getSubjectModel(), renderingLangCode, false);
					subject = "RE: " + subject;
					String text = answer.getText();
					String senderCode = config.getSenderCode();
					String[] recipientsTo = new String[]{email};
					Properties attachmentFiles = answer.getAttachments();
					this.getMailManager().sendMail(text, subject, IMailManager.CONTENTTYPE_TEXT_PLAIN,
							attachmentFiles, recipientsTo, null, null, senderCode);
					sent = true;
				} else {
					_logger.warn("ATTENTION: email Attribute {} for Message {} isn't valued!!\nCheck \"jpwebform_messageTypes\" Configuration or \"{}\" Configuration", config.getMailAttrName(), message.getId(), JpwebformSystemConstants.MESSAGE_NOTIFIER_CONFIG_ITEM);
				}
			} else {
				_logger.warn("Answer not sent! Message lacking in notifier configuration.");
			}
		} catch (Throwable t) {
			_logger.error("error in sendAnswerNotification", t);
			// Do not launch any exception
			//			throw new ApsSystemException("Error sending notification for answer " + answer.getAnswerId(), t);
		}
		return sent;
	}

	protected String extractUserMail(Message message, MessageTypeNotifierConfig config) {
		String email = null;
		if (null != config) {
			String mailAttrName = config.getMailAttrName();
			if (null != mailAttrName && mailAttrName.length() > 0) {
				ITextAttribute mailAttribute = (ITextAttribute) message.getAttribute(mailAttrName);
				if (mailAttribute != null) {
					email = mailAttribute.getText();
				}
			}
		}
		return email;
	}

	protected Map<String, TypeVersionGuiConfig> getLastVersionConfigs() {
		return _lastVersionConfigs;
	}

	protected Map<String, TypeVersionGuiConfig> getTypeVersionConfigs() {
		return _typeVersionConfigs;
	}

	@Override
	public TypeVersionGuiConfig getTypeVersionGui(String formTypeCode) throws ApsSystemException {
		try {
			TypeVersionGuiConfig config = this.getLastVersionConfigs().get(formTypeCode);
			if (null == config) {
				config = this.getFormTypeGuiDAO().getTypeVersionGui(formTypeCode);
				this.fillVersionType(config);
				this.getLastVersionConfigs().put(formTypeCode, config);
			}
			return config;
		} catch (Throwable t) {
			_logger.error("Error extracting last Type Version Gui for type {}", formTypeCode, t);
			throw new ApsSystemException("Error extracting last Type Version Gui for type " + formTypeCode, t);
		}
	}

	@Override
	public TypeVersionGuiConfig getTypeVersionGui(String formTypeCode, Integer version) throws ApsSystemException {
		try {
			String key = formTypeCode + "%" + version;
			TypeVersionGuiConfig config = this.getTypeVersionConfigs().get(key);
			if (null == config) {
				config = this.getFormTypeGuiDAO().getTypeVersionGui(formTypeCode, version);
				this.fillVersionType(config);
				this.getTypeVersionConfigs().put(key, config);
			}
			return config;
		} catch (Throwable t) {
			_logger.error("Error extracting Type Version Gui for type {}, version {}" ,formTypeCode, version, t);
			throw new ApsSystemException("Error extracting Type Version Gui for type " + formTypeCode + ", version " + version, t);
		}
	}

	private void fillVersionType(TypeVersionGuiConfig config) throws ApsSystemException {
		if (null == config) {
			return;
		}
		try {
			StepConfigsDOM dom = new StepConfigsDOM();
			StepsConfig stepsConfig = dom.extractStepConfig(config.getStepsConfigXml());
			config.setStepsConfig(stepsConfig);
			String typeCode = config.getFormTypeCode();
			String modelConfig = config.getPrototypeXml();
			Message prototype = (Message) super.getEntityTypeDom().extractEntityType(modelConfig, super.getEntityClass(), this.getEntityDom(), typeCode);
			List<AttributeInterface> attributes = prototype.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface attribute = attributes.get(i);
				String[] disablingCodes = stepsConfig.getDisabilingCodes(attribute.getName());
				attribute.setDisablingCodes(disablingCodes);
			}
			config.setPrototype(prototype);
		} catch (Throwable t) {
			_logger.error("Error filling Type Version", t);
			throw new ApsSystemException("Error filling Type Version", t);
		}
	}

	@Override
	public void deleteStepsConfig(String formTypeCode) throws ApsSystemException {
		try {
			StepConfigsDOM dom = new StepConfigsDOM();
			this.getStepsConfig().remove(formTypeCode);
			String xml = dom.createConfigXml(this.getStepsConfig());
			ConfigInterface configManager = this.getConfigManager();
			configManager.updateConfigItem(JpwebformSystemConstants.MESSAGE_STEPS_CONFIG_ITEM, xml);
		} catch (Throwable t) {
			_logger.error("Error saving Steps Config", t);
			throw new ApsSystemException("Error saving Steps Config", t);
		}
	}

	@Override
	public void saveStepsConfig(StepsConfig config) throws ApsSystemException {
		try {
			String formTypeCode = config.getFormTypeCode();
			StepConfigsDOM dom = new StepConfigsDOM();
			this.getStepsConfig().put(formTypeCode, config);
			String xml = dom.createConfigXml(this.getStepsConfig());
			ConfigInterface configManager = this.getConfigManager();
			configManager.updateConfigItem(JpwebformSystemConstants.MESSAGE_STEPS_CONFIG_ITEM, xml);
		} catch (Throwable t) {
			_logger.error("Error saving Steps Config", t);
			throw new ApsSystemException("Error saving Steps Config", t);
		}
	}

	@Override
	public StepsConfig getPublicSteps(String formTypeCode) throws ApsSystemException {
		return this.getTypeVersionGui(formTypeCode).getStepsConfig();
	}

	@Override
	public StepsConfig getPublicSteps(String formTypeCode, Integer version) throws ApsSystemException {
		return this.getTypeVersionGui(formTypeCode, version).getStepsConfig();
	}

	@Override
	public StepGuiConfig generateWorkStepUserGui(String formTypeCode, String stepCode, boolean save) throws ApsSystemException {
		try {
			StepsConfig stepsConfig = this.getStepsConfig().get(formTypeCode);
			IApsEntity prototype = this.getEntityPrototype(formTypeCode);
			UserGuiGenerator generator = this.getUserGuiGenerator();
			String userGui = generator.generateUserGui(stepCode, stepsConfig, prototype);
			StepGuiConfig stepGuiConfig = new StepGuiConfig();
			stepGuiConfig.setUserGui(userGui);
			stepGuiConfig.setStepCode(stepCode);
			stepGuiConfig.setFormTypeCode(formTypeCode);
			if (save) {
				this.getFormTypeGuiDAO().saveWorkGuiConfig(stepGuiConfig);
				this.saveStepsConfig(stepsConfig);
			}
			return stepGuiConfig;
		} catch (Throwable t) {
			_logger.error("Error generating Work Step User Gui - {} - stepCode {}",formTypeCode,stepCode, t);
			throw new ApsSystemException("Error generating Work Step User Gui - formTypeCode " + formTypeCode + " - stepCode " + stepCode, t);
		}
	}

	@Override
	public StepGuiConfig getWorkGuiConfig(String typeCode, String stepCode) throws ApsSystemException {
		try {
			return this.getFormTypeGuiDAO().getWorkGuiConfig(typeCode, stepCode);
		} catch (Throwable t) {
			_logger.error("error in getWorkGuiConfig", t);
			throw new ApsSystemException("Error deleting Work Step User Gui - "
					+ "formTypeCode " + typeCode + " - stepCode " + stepCode, t);
		}
	}

	@Override
	public void saveWorkGuiConfig(StepGuiConfig config) throws ApsSystemException {
		try {
			this.getFormTypeGuiDAO().saveWorkGuiConfig(config);
		} catch (Throwable t) {
			_logger.error("Error saving Work Step User Gui - formTypeCode:{} - stepCode:{}", config.getFormTypeCode(), config.getStepCode(), t);
			throw new ApsSystemException("Error saving Work Step User Gui - "
					+ "formTypeCode " + config.getFormTypeCode() + " - stepCode " + config.getStepCode(), t);
		}
	}

	@Override
	public void deleteWorkGuiConfig(String typeCode, String stepCode) throws ApsSystemException {
		try {
			this.getFormTypeGuiDAO().deleteWorkGuiConfig(typeCode, stepCode);
		} catch (Throwable t) {
			_logger.error("Error deleting Work Step User Gui formTypeCode:{} - stepCode ", typeCode, stepCode, t);
			throw new ApsSystemException("Error deleting Work Step User Gui - "
					+ "formTypeCode " + typeCode + " - stepCode " + stepCode, t);
		}
	}

	@Override
	public void deleteAllWorkGuiConfig(String typeCode) throws ApsSystemException {
		try {
			this.getFormTypeGuiDAO().deleteAllWorkGuiConfig(typeCode);
		} catch (Throwable t) {
			_logger.error("Error deleting Work Step User Gui - formTypeCode {}", typeCode, t);
			throw new ApsSystemException("Error deleting Work Step User Gui - formTypeCode " + typeCode, t);
		}
	}

	@Override
	public synchronized void generateNewVersionType(String formTypeCode) throws ApsSystemException {
		try {
			Message prototype = (Message) super.getEntityPrototype(formTypeCode);
			Integer version = 1;
			if (null != prototype) {
				version = prototype.getVersionType() + 1;
			}
			prototype.setVersionType(version);
			String modelXml = new FormTypeDOM().getXml(prototype);
			StepsConfig stepsConfig = this.getStepsConfig(formTypeCode);
			String stepsXml = new StepConfigsDOM().createConfigXml(stepsConfig);
			TypeVersionGuiConfig versionConfig = new TypeVersionGuiConfig();
			versionConfig.setFormTypeCode(formTypeCode);
			versionConfig.setPrototypeXml(modelXml);
			versionConfig.setPrototype(prototype);
			versionConfig.setStepsConfigXml(stepsXml);
			versionConfig.setStepsConfig(stepsConfig);
			Map<String, StepGuiConfig> workGuiConfigs = this.getFormTypeGuiDAO().getWorkGuiConfigs(formTypeCode);
			List<StepGuiConfig> guiConfigs = new ArrayList<StepGuiConfig>();
			guiConfigs.addAll(workGuiConfigs.values());
			versionConfig.setGuiConfigs(guiConfigs);
			versionConfig.setVersion(prototype.getVersionType());
			this.generateLabels(versionConfig);
			this.getFormTypeGuiDAO().addTypeVersionGui(versionConfig);
			this.getLastVersionConfigs().put(formTypeCode, versionConfig);
			this.updateEntityPrototype(prototype);
		} catch (Throwable t) {
			_logger.error("Error generating new version type - {}", formTypeCode, t);
			throw new ApsSystemException("Error generating new version type - " + formTypeCode, t);
		}
	}

	private void generateLabels(TypeVersionGuiConfig versionConfig) throws ApsSystemException {
		Integer version = versionConfig.getVersion();
		StepsConfig stepsConfig = versionConfig.getStepsConfig();
		List<Step> steps = stepsConfig.getSteps();
		for (int i = 0; i < steps.size(); i++) {
			Step step = steps.get(i);
			//String labelKey = "jpwebform_TITLE_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_"+step.getCode();
			String prevLabelKey = this.createLabelKey("jpwebform_TITLE",
					versionConfig.getFormTypeCode(), String.valueOf(version - 1), step.getCode());
			String labelKey = this.createLabelKey("jpwebform_TITLE",
					versionConfig.getFormTypeCode(), String.valueOf(version), step.getCode());
			String stepCode = step.getCode();
			this.createCheckLabels(prevLabelKey, labelKey, stepCode);
			if (!step.equals(stepsConfig.getFirstStep())) {
				//String backLabelKey = "jpwebform_BACK_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_"+step.getCode();
				String prevBackLabelKey = this.createLabelKey("jpwebform_BACK",
						versionConfig.getFormTypeCode(), String.valueOf(version - 1), step.getCode());
				String backLabelKey = this.createLabelKey("jpwebform_BACK",
						versionConfig.getFormTypeCode(), String.valueOf(version), step.getCode());
				this.createCheckLabels(prevBackLabelKey, backLabelKey, "back");
			}
			//String submitLabelKey = "jpwebform_SUBMIT_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_"+step.getCode();
			String prevSubmitLabelKey = this.createLabelKey("jpwebform_SUBMIT",
					versionConfig.getFormTypeCode(), String.valueOf(version - 1), step.getCode());
			String submitLabelKey = this.createLabelKey("jpwebform_SUBMIT",
					versionConfig.getFormTypeCode(), String.valueOf(version), step.getCode());
			this.createCheckLabels(prevSubmitLabelKey, submitLabelKey, "submit");
			if (stepsConfig.isConfirmGui()) {
				//String backConfirmLabelKey = "jpwebform_BACK_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_confirm";
				String prevBackConfirmLabelKey = this.createLabelKey("jpwebform_BACK",
						versionConfig.getFormTypeCode(), String.valueOf(version - 1), "confirm");
				String backConfirmLabelKey = this.createLabelKey("jpwebform_BACK",
						versionConfig.getFormTypeCode(), String.valueOf(version), "confirm");
				this.createCheckLabels(prevBackConfirmLabelKey, backConfirmLabelKey, "Back");
				//String submitConfirmLabelKey =  "jpwebform_SUBMIT_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_confirm";
				String prevSubmitConfirmLabelKey = this.createLabelKey("jpwebform_SUBMIT",
						versionConfig.getFormTypeCode(), String.valueOf(version - 1), "confirm");
				String submitConfirmLabelKey = this.createLabelKey("jpwebform_SUBMIT",
						versionConfig.getFormTypeCode(), String.valueOf(version), "confirm");
				this.createCheckLabels(prevSubmitConfirmLabelKey, submitConfirmLabelKey, "Submit");
			}
			List<String> attributeOrder = step.getAttributeOrder();
			for (int j = 0; j < attributeOrder.size(); j++) {
				String attributeName = attributeOrder.get(j);
				Object attribute = versionConfig.getPrototype().getAttribute(attributeName);
				if (attribute instanceof CompositeAttribute) {
					List<AttributeInterface> attributes = ((CompositeAttribute) attribute).getAttributes();
					for (int k = 0; k < attributes.size(); k++) {
						AttributeInterface attributeInterface = attributes.get(k);
						//labelKey = "jpwebform_LABEL_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_"+step.getCode()+"_"+attributeInterface.getName();
						prevLabelKey = this.createLabelKey("jpwebform_LABEL", versionConfig.getFormTypeCode(),
								String.valueOf(version - 1), step.getCode(), attributeInterface.getName());
						labelKey = this.createLabelKey("jpwebform_LABEL", versionConfig.getFormTypeCode(),
								String.valueOf(version), step.getCode(), attributeInterface.getName());
						String description = attributeInterface.getDescription();
						String defaultValue = (StringUtils.isBlank(description)) ? attributeInterface.getName() : description;
						this.createCheckLabels(prevLabelKey, labelKey, defaultValue);
					}
				}
				//labelKey =  "jpwebform_LABEL_"+versionConfig.getFormTypeCode()+"_"+versionConfig.getVersion()+"_"+step.getCode()+"_"+attributeName;
				prevLabelKey = this.createLabelKey("jpwebform_LABEL", versionConfig.getFormTypeCode(),
						String.valueOf(version - 1), step.getCode(), attributeName);
				labelKey = this.createLabelKey("jpwebform_LABEL", versionConfig.getFormTypeCode(),
						String.valueOf(version), step.getCode(), attributeName);
				this.createCheckLabels(prevLabelKey, labelKey, attributeName);
			}
		}
	}

	private void createCheckLabels(String prevLabelkey, String labelKey, String defaultValue) throws ApsSystemException {
		ApsProperties properties = this.getI18nManager().getLabelGroup(prevLabelkey);
		if (null == properties) {
			properties = new ApsProperties();
			this.fillPropertyForEveryLang(properties, defaultValue);
		}
		this.addUpdateLabel(labelKey, properties);
	}

	private String createLabelKey(String prefix, String... parameters) {
		StringBuilder label = new StringBuilder(prefix);
		for (int i = 0; i < parameters.length; i++) {
			String param = parameters[i];
			label.append("_").append(param);
		}
		return label.toString();
	}

	private void addUpdateLabel(String labelKey, ApsProperties properties) throws ApsSystemException {
		Map<String, ApsProperties> labelGroups = this.getI18nManager().getLabelGroups();
		if (!labelGroups.containsKey(labelKey)) {
			this.getI18nManager().addLabelGroup(labelKey, properties);
		} else {
			this.getI18nManager().updateLabelGroup(labelKey, properties);
		}
	}

	private void fillPropertyForEveryLang(ApsProperties properties, String defaultValue) {
		List<Lang> langs = this.getLangManager().getLangs();
		for (int k = 0; k < langs.size(); k++) {
			Lang lang = langs.get(k);
			properties.put(lang.getCode(), defaultValue);
		}
	}

	@Override
	public boolean checkStepGui(String entityTypeCode) {
		StepsConfig stepsConfig = this.getStepsConfig(entityTypeCode);
		if (stepsConfig != null) {
			return stepsConfig.checkGuiSteps();
		}
		return false;
	}

	@Override
	public StepsConfig getStepsConfig(String formTypeCode) {
		return this.getStepsConfig().get(formTypeCode);
	}

	private void notifyFormSubmittedEvent(Message message) {
		FormSubmittedEvent event = new FormSubmittedEvent();
		event.setMessage(message);
		this.notifyEvent(event);
	}

	protected Map<String, StepsConfig> getStepsConfig() {
		return _stepsConfig;
	}

	protected void setStepsConfig(Map<String, StepsConfig> stepsConfig) {
		this._stepsConfig = stepsConfig;
	}

	@Override
	public IApsEntity getEntity(String entityId) throws ApsSystemException {
		return this.getMessage(entityId);
	}

	@Override
	protected IEntityDAO getEntityDao() {
		return (IEntityDAO) this._formDAO;
	}

	protected IFormDAO getFormDAO() {
		return _formDAO;
	}

	public void setFormDAO(IFormDAO formDAO) {
		this._formDAO = formDAO;
	}

	@Override
	protected IEntitySearcherDAO getEntitySearcherDao() {
		return _entitySearcherDAO;
	}

	public void setEntitySearcherDAO(IEntitySearcherDAO entitySearcherDAO) {
		this._entitySearcherDAO = entitySearcherDAO;
	}

	protected IFormTypeGuiDAO getFormTypeGuiDAO() {
		return _formTypeGuiDAO;
	}

	public void setFormTypeGuiDAO(IFormTypeGuiDAO formTypeGuiDAO) {
		this._formTypeGuiDAO = formTypeGuiDAO;
	}

	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}

	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	protected ConfigInterface getConfigManager() {
		return _configManager;
	}

	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}

	protected ILangManager getLangManager() {
		return _langManager;
	}

	public void setLangManager(ILangManager langManager) {
		this._langManager = langManager;
	}

	protected IEntityRenderer getEntityRenderer() {
		return _entityRenderer;
	}

	public void setEntityRenderer(IEntityRenderer entityRenderer) {
		this._entityRenderer = entityRenderer;
	}

	protected IMailManager getMailManager() {
		return _mailManager;
	}

	public void setMailManager(IMailManager mailManager) {
		this._mailManager = mailManager;
	}

	protected II18nManager getI18nManager() {
		return _i18nManager;
	}

	public void setI18nManager(II18nManager i18nManager) {
		this._i18nManager = i18nManager;
	}

	public UserGuiGenerator getUserGuiGenerator() {
		return _userGuiGenerator;
	}

	public void setUserGuiGenerator(UserGuiGenerator userGuiGenerator) {
		this._userGuiGenerator = userGuiGenerator;
	}

	public IUserProfileManager getUserProfileManager() {
		return _userProfileManager;
	}

	public void setUserProfileManager(IUserProfileManager userProfileManager) {
		this._userProfileManager = userProfileManager;
	}

	/**
	 * Mappa dei prototipi dei tipi di contenuti il forma Small, indicizzati in
	 * base al codice del tipo.
	 */
	private Map<String, SmallMessageType> _smallMessageTypes;

	/**
	 * A map containing the configuration of the notifier for each message type,
	 * identified by the type code.
	 */
	private Map<String, MessageTypeNotifierConfig> _messageNotifierConfigMap;

	private Map<String, StepsConfig> _stepsConfig;

	private Map<String, TypeVersionGuiConfig> _typeVersionConfigs = new HashMap<String, TypeVersionGuiConfig>();
	private Map<String, TypeVersionGuiConfig> _lastVersionConfigs = new HashMap<String, TypeVersionGuiConfig>();

	private IFormDAO _formDAO;
	private IEntitySearcherDAO _entitySearcherDAO;
	private IFormTypeGuiDAO _formTypeGuiDAO;
	private IKeyGeneratorManager _keyGeneratorManager;
	private ConfigInterface _configManager;

	private ILangManager _langManager;
	private IEntityRenderer _entityRenderer;
	private IMailManager _mailManager;
	private II18nManager _i18nManager;
	private UserGuiGenerator _userGuiGenerator;
	private IUserProfileManager _userProfileManager;

}
