package org.entando.entando.plugins.dashboard.aps.system.services.storage.repository;

import java.util.Date;
import java.util.List;

import org.entando.entando.plugins.dashboard.aps.system.services.storage.IotMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.mongodb.BasicDBObject;

public interface MessageRepository extends MongoRepository<IotMessage, String> {

	public Page<IotMessage> findByServerIdAndDashboardCode(int serverId, String dashboardCode, Pageable pageable);

	public Page<IotMessage> findByServerIdAndDashboardCodeAndCreatedAtBetween(int serverId, String dashboardCode, Date start, Date end, Pageable pageable);
	
	public void deleteByServerIdAndDashboardCode(int serverId, String dashboardCode);

	public void deleteByServerId(int serverId);

	public List<IotMessage> findAllByServerIdAndDashboardCodeAndCreatedAtBetween(int serverId, String dashboardCode, Date start, Date end);

	public List<IotMessage> findAllByServerIdAndDashboardCodeAndCreatedAtAfter(int dashboardId, String datasourceCode,
			Date startDate);

	public List<IotMessage> findAllByServerIdAndDashboardCodeAndCreatedAtBefore(int dashboardId, String datasourceCode,
			Date endDate);

	public List<IotMessage> findAllByServerIdAndDashboardCode(int dashboardId, String datasourceCode);

	@Query(value = "{'serverId': ?0, 'dashboardCode': ?1}")// TODO, createdAt : { $or : [ {$gte : ?2},  {?2 : null}], $or : [{$lt : ?3 }, {?3 : null}] }}")
	public List<IotMessage> findContentMeasurementByServerIdAndDashboardCode(int dashboardId, String datasourceCode);
	
}
