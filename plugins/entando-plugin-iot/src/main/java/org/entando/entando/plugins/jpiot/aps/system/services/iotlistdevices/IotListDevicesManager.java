/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices;

import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.event.IotListDevicesChangedEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;
import org.entando.entando.plugins.jpiot.aps.system.services.iotlistdevices.api.JAXBIotListDevices;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

public class IotListDevicesManager extends AbstractService implements IIotListDevicesManager {

	private static final Logger logger =  LoggerFactory.getLogger(IotListDevicesManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public IotListDevices getIotListDevices(int id) throws ApsSystemException {
		IotListDevices iotListDevices = null;
		try {
			iotListDevices = this.getIotListDevicesDAO().loadIotListDevices(id);
		} catch (Throwable t) {
			logger.error("Error loading iotListDevices with id '{}'", id,  t);
			throw new ApsSystemException("Error loading iotListDevices with id: " + id, t);
		}
		return iotListDevices;
	}

	@Override
	public List<Integer> getIotListDevicess() throws ApsSystemException {
		List<Integer> iotListDevicess = new ArrayList<Integer>();
		try {
			iotListDevicess = this.getIotListDevicesDAO().loadIotListDevicess();
		} catch (Throwable t) {
			logger.error("Error loading IotListDevices list",  t);
			throw new ApsSystemException("Error loading IotListDevices ", t);
		}
		return iotListDevicess;
	}

	@Override
	public List<Integer> searchIotListDevicess(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> iotListDevicess = new ArrayList<Integer>();
		try {
			iotListDevicess = this.getIotListDevicesDAO().searchIotListDevicess(filters);
		} catch (Throwable t) {
			logger.error("Error searching IotListDevicess", t);
			throw new ApsSystemException("Error searching IotListDevicess", t);
		}
		return iotListDevicess;
	}

	@Override
	public void addIotListDevices(IotListDevices iotListDevices) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			iotListDevices.setId(key);
			this.getIotListDevicesDAO().insertIotListDevices(iotListDevices);
			this.notifyIotListDevicesChangedEvent(iotListDevices, IotListDevicesChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding IotListDevices", t);
			throw new ApsSystemException("Error adding IotListDevices", t);
		}
	}
 
	@Override
	public void updateIotListDevices(IotListDevices iotListDevices) throws ApsSystemException {
		try {
			this.getIotListDevicesDAO().updateIotListDevices(iotListDevices);
			this.notifyIotListDevicesChangedEvent(iotListDevices, IotListDevicesChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating IotListDevices", t);
			throw new ApsSystemException("Error updating IotListDevices " + iotListDevices, t);
		}
	}

	@Override
	public void deleteIotListDevices(int id) throws ApsSystemException {
		try {
			IotListDevices iotListDevices = this.getIotListDevices(id);
			this.getIotListDevicesDAO().removeIotListDevices(id);
			this.notifyIotListDevicesChangedEvent(iotListDevices, IotListDevicesChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting IotListDevices with id {}", id, t);
			throw new ApsSystemException("Error deleting IotListDevices with id:" + id, t);
		}
	}


	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/iotListDevicess?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBIotListDevices> getIotListDevicessForApi(Properties properties) throws Throwable {
		List<JAXBIotListDevices> list = new ArrayList<JAXBIotListDevices>();
		List<Integer> idList = this.getIotListDevicess();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> iotListDevicesIterator = idList.iterator();
			while (iotListDevicesIterator.hasNext()) {
				int currentid = iotListDevicesIterator.next();
				IotListDevices iotListDevices = this.getIotListDevices(currentid);
				if (null != iotListDevices) {
					list.add(new JAXBIotListDevices(iotListDevices));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/iotListDevices?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
    public JAXBIotListDevices getIotListDevicesForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
		JAXBIotListDevices jaxbIotListDevices = null;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        IotListDevices iotListDevices = this.getIotListDevices(id);
        if (null == iotListDevices) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IotListDevices with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbIotListDevices = new JAXBIotListDevices(iotListDevices);
        return jaxbIotListDevices;
    }

    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/iotListDevices 
     * @param jaxbIotListDevices
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addIotListDevicesForApi(JAXBIotListDevices jaxbIotListDevices) throws ApiException, ApsSystemException {
        if (null != this.getIotListDevices(jaxbIotListDevices.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IotListDevices with id " + jaxbIotListDevices.getId() + " already exists", Response.Status.CONFLICT);
        }
        IotListDevices iotListDevices = jaxbIotListDevices.getIotListDevices();
        this.addIotListDevices(iotListDevices);
    }

    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/iotListDevices 
     * @param jaxbIotListDevices
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateIotListDevicesForApi(JAXBIotListDevices jaxbIotListDevices) throws ApiException, ApsSystemException {
        if (null == this.getIotListDevices(jaxbIotListDevices.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IotListDevices with id " + jaxbIotListDevices.getId() + " does not exist", Response.Status.CONFLICT);
        }
        IotListDevices iotListDevices = jaxbIotListDevices.getIotListDevices();
        this.updateIotListDevices(iotListDevices);
    }

    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/iotListDevices?id=1
	 * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteIotListDevicesForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        this.deleteIotListDevices(id);
    }

	private void notifyIotListDevicesChangedEvent(IotListDevices iotListDevices, int operationCode) {
		IotListDevicesChangedEvent event = new IotListDevicesChangedEvent();
		event.setIotListDevices(iotListDevices);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<IotListDevices> getIotListDevicess(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<IotListDevices> pagedResult = null;
        try {
            List<IotListDevices> iotListDevicess = new ArrayList<>();
            int count = this.getIotListDevicesDAO().countIotListDevicess(filters);

            List<Integer> iotListDevicesNames = this.getIotListDevicesDAO().searchIotListDevicess(filters);
            for (Integer iotListDevicesName : iotListDevicesNames) {
                iotListDevicess.add(this.getIotListDevices(iotListDevicesName));
            }
            pagedResult = new SearcherDaoPaginatedResult<IotListDevices>(count, iotListDevicess);
        } catch (Throwable t) {
            logger.error("Error searching iotListDevicess", t);
            throw new ApsSystemException("Error searching iotListDevicess", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<IotListDevices> getIotListDevicess(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getIotListDevicess(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setIotListDevicesDAO(IIotListDevicesDAO iotListDevicesDAO) {
		 this._iotListDevicesDAO = iotListDevicesDAO;
	}
	protected IIotListDevicesDAO getIotListDevicesDAO() {
		return _iotListDevicesDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IIotListDevicesDAO _iotListDevicesDAO;
}
