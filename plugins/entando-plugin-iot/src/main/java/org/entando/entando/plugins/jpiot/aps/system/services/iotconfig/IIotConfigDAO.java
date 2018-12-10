/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IIotConfigDAO {

	public List<Integer> searchIotConfigs(FieldSearchFilter[] filters);
	
	public IotConfig loadIotConfig(int id);

	public List<Integer> loadIotConfigs();

	public void removeIotConfig(int id);
	
	public void updateIotConfig(IotConfig iotConfig);

	public void insertIotConfig(IotConfig iotConfig);

    public int countIotConfigs(FieldSearchFilter[] filters);
}