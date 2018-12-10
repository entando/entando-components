/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;

import java.util.List;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IIotListDevicesManager {

	public IotListDevices getIotListDevices(int id) throws ApsSystemException;

	public List<Integer> getIotListDevicess() throws ApsSystemException;

	public List<Integer> searchIotListDevicess(FieldSearchFilter filters[]) throws ApsSystemException;

	public void addIotListDevices(IotListDevices iotListDevices) throws ApsSystemException;

	public void updateIotListDevices(IotListDevices iotListDevices) throws ApsSystemException;

	public void deleteIotListDevices(int id) throws ApsSystemException;

	public SearcherDaoPaginatedResult<IotListDevices> getIotListDevicess(List<FieldSearchFilter> fieldSearchFilters) throws ApsSystemException;
}