package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.time.Instant;
import java.util.Date;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.repository.MessageRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.agiletec.aps.system.common.AbstractService;

@Service
public class MessageService extends AbstractService implements IMessageService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private MessageRepository messageRepository;

	protected MessageRepository getMessageRepository() {
		return messageRepository;
	}

	public void setMessageRepository(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}


	@Autowired
	public MessageService(MessageRepository messageRepository) {
		this.messageRepository = messageRepository;
	}

	@Override
	public void init() throws Exception {
		//messageRepository.
	}

	@Override
	public Page<IotMessage> findAll(Pageable pageable) {
		if (null == pageable) {
			pageable = createDefaultPageable();
		}
		return this.messageRepository.findAll(pageable);
	}

	private Pageable createDefaultPageable() {
		return  PageRequest.of(0, DEFAULT_PAGE_SIZE);
	}

	@Override
	public IotMessage add(IotMessage message) {
		if (null == message.getCreatedAt()) {			
			message.setCreatedAt(Instant.now());
		}
		return  this.messageRepository.insert(message);
	}

	@Override
	public Page<IotMessage> findByServerConfiguration(int serverId, String dashboardCode, Pageable pageable) {
		return this.messageRepository.findByServerIdAndDashboardCode(serverId, dashboardCode, pageable);
	}

	@Override
	public void deleteByServerConfiguration(int serverId, String dashboardCode) {
		this.messageRepository.deleteByServerIdAndDashboardCode(serverId, dashboardCode);
	}

	@Override
	public void deleteByServerId(int serverId) {
		this.messageRepository.deleteByServerId(serverId);
	}

	@Override
	public Page<IotMessage> findByServerConfigurationAndDate(int serverId, String dashboardCode, Date start, Date end, Pageable pageable) {
		if (null == pageable) {
			pageable = createDefaultPageable();
		}
		return this.messageRepository.findByServerIdAndDashboardCodeAndCreatedAtBetween(serverId, dashboardCode, start, end, pageable);
		
	}

}
