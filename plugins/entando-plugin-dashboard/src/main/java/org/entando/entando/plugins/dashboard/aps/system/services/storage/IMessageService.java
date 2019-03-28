package org.entando.entando.plugins.dashboard.aps.system.services.storage;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMessageService {

	public static final int DEFAULT_PAGE_SIZE = 50;

	/**
	 * Save a message
	 * @param message the message to save
	 * @return the saved message 
	 */
	IotMessage add(IotMessage message);

	Page<IotMessage> findAll(Pageable pageable);

	Page<IotMessage> findByServerConfiguration(int serverId, String dashboardCode, Pageable pageable);

	void deleteByServerConfiguration(int serverId, String dashboardCode);

	void deleteByServerId(int serverId);

	Page<IotMessage> findByServerConfigurationAndDate(int serverId, String dashboardCode, Date start, Date end, Pageable pageable);
	
}
