package org.entando.entando.plugins.dashboard.aps.system.services.storage.repository;

import java.util.Date;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.IotMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<IotMessage, String> {

	public Page<IotMessage> findByServerIdAndDashboardCode(int serverId, String dashboardCode, Pageable pageable);

	public Page<IotMessage> findByServerIdAndDashboardCodeAndCreatedAtBetween(int serverId, String dashboardCode, Date start, Date end, Pageable pageable);
	
	
	public void deleteByServerIdAndDashboardCode(int serverId, String dashboardCode);

	public void deleteByServerId(int serverId);


    
}
