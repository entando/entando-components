/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IIotConfigManager {

	public IotConfig getIotConfig(int id) throws ApsSystemException;

	public List<Integer> getIotConfigs() throws ApsSystemException;

	public List<Integer> searchIotConfigs(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addIotConfig(IotConfig iotConfig) throws ApsSystemException;

	public void updateIotConfig(IotConfig iotConfig) throws ApsSystemException;

	public void deleteIotConfig(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<IotConfig> getIotConfigs(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}