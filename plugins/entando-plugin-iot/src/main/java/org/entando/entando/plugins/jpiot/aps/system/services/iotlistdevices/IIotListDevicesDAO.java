/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;

import java.util.List;

import com.agiletec.aps.system.common.FieldSearchFilter;

public interface IIotListDevicesDAO {

	public List<Integer> searchIotListDevicess(FieldSearchFilter[] filters);
	
	public IotListDevices loadIotListDevices(int id);

	public List<Integer> loadIotListDevicess();

	public void removeIotListDevices(int id);
	
	public void updateIotListDevices(IotListDevices iotListDevices);

	public void insertIotListDevices(IotListDevices iotListDevices);

    public int countIotListDevicess(FieldSearchFilter[] filters);
}