/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.jpiot.aps.system.services.iotconfig;

import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.event.IotConfigChangedEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.core.Response;
import org.entando.entando.plugins.jpiot.aps.system.services.iotconfig.api.JAXBIotConfig;
import org.entando.entando.aps.system.services.api.IApiErrorCodes;
import org.entando.entando.aps.system.services.api.model.ApiException;

import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.keygenerator.IKeyGeneratorManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;

public class IotConfigManager extends AbstractService implements IIotConfigManager {

	private static final Logger logger =  LoggerFactory.getLogger(IotConfigManager.class);

	@Override
	public void init() throws Exception {
		logger.debug("{} ready.", this.getClass().getName());
	}
 
	@Override
	public IotConfig getIotConfig(int id) throws ApsSystemException {
		IotConfig iotConfig = null;
		try {
			iotConfig = this.getIotConfigDAO().loadIotConfig(id);
		} catch (Throwable t) {
			logger.error("Error loading iotConfig with id '{}'", id,  t);
			throw new ApsSystemException("Error loading iotConfig with id: " + id, t);
		}
		return iotConfig;
	}

	@Override
	public List<Integer> getIotConfigs() throws ApsSystemException {
		List<Integer> iotConfigs = new ArrayList<Integer>();
		try {
			iotConfigs = this.getIotConfigDAO().loadIotConfigs();
		} catch (Throwable t) {
			logger.error("Error loading IotConfig list",  t);
			throw new ApsSystemException("Error loading IotConfig ", t);
		}
		return iotConfigs;
	}

	@Override
	public List<Integer> searchIotConfigs(FieldSearchFilter filters[]) throws ApsSystemException {
		List<Integer> iotConfigs = new ArrayList<Integer>();
		try {
			iotConfigs = this.getIotConfigDAO().searchIotConfigs(filters);
		} catch (Throwable t) {
			logger.error("Error searching IotConfigs", t);
			throw new ApsSystemException("Error searching IotConfigs", t);
		}
		return iotConfigs;
	}

	@Override
	public void addIotConfig(IotConfig iotConfig) throws ApsSystemException {
		try {
			int key = this.getKeyGeneratorManager().getUniqueKeyCurrentValue();
			iotConfig.setId(key);
			this.getIotConfigDAO().insertIotConfig(iotConfig);
			this.notifyIotConfigChangedEvent(iotConfig, IotConfigChangedEvent.INSERT_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error adding IotConfig", t);
			throw new ApsSystemException("Error adding IotConfig", t);
		}
	}
 
	@Override
	public void updateIotConfig(IotConfig iotConfig) throws ApsSystemException {
		try {
			this.getIotConfigDAO().updateIotConfig(iotConfig);
			this.notifyIotConfigChangedEvent(iotConfig, IotConfigChangedEvent.UPDATE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error updating IotConfig", t);
			throw new ApsSystemException("Error updating IotConfig " + iotConfig, t);
		}
	}

	@Override
	public void deleteIotConfig(int id) throws ApsSystemException {
		try {
			IotConfig iotConfig = this.getIotConfig(id);
			this.getIotConfigDAO().removeIotConfig(id);
			this.notifyIotConfigChangedEvent(iotConfig, IotConfigChangedEvent.REMOVE_OPERATION_CODE);
		} catch (Throwable t) {
			logger.error("Error deleting IotConfig with id {}", id, t);
			throw new ApsSystemException("Error deleting IotConfig with id:" + id, t);
		}
	}


	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/iotConfigs?
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
	public List<JAXBIotConfig> getIotConfigsForApi(Properties properties) throws Throwable {
		List<JAXBIotConfig> list = new ArrayList<JAXBIotConfig>();
		List<Integer> idList = this.getIotConfigs();
		if (null != idList && !idList.isEmpty()) {
			Iterator<Integer> iotConfigIterator = idList.iterator();
			while (iotConfigIterator.hasNext()) {
				int currentid = iotConfigIterator.next();
				IotConfig iotConfig = this.getIotConfig(currentid);
				if (null != iotConfig) {
					list.add(new JAXBIotConfig(iotConfig));
				}
			}
		}
		return list;
	}

	/**
	 * GET http://localhost:8080/<portal>/api/rs/en/iotConfig?id=1
	 * @param properties
	 * @return
	 * @throws Throwable
	 */
    public JAXBIotConfig getIotConfigForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
		JAXBIotConfig jaxbIotConfig = null;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        IotConfig iotConfig = this.getIotConfig(id);
        if (null == iotConfig) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IotConfig with id '" + idString + "' does not exist", Response.Status.CONFLICT);
        }
        jaxbIotConfig = new JAXBIotConfig(iotConfig);
        return jaxbIotConfig;
    }

    /**
     * POST Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/iotConfig 
     * @param jaxbIotConfig
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void addIotConfigForApi(JAXBIotConfig jaxbIotConfig) throws ApiException, ApsSystemException {
        if (null != this.getIotConfig(jaxbIotConfig.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IotConfig with id " + jaxbIotConfig.getId() + " already exists", Response.Status.CONFLICT);
        }
        IotConfig iotConfig = jaxbIotConfig.getIotConfig();
        this.addIotConfig(iotConfig);
    }

    /**
     * PUT Content-Type: application/xml http://localhost:8080/<portal>/api/rs/en/iotConfig 
     * @param jaxbIotConfig
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void updateIotConfigForApi(JAXBIotConfig jaxbIotConfig) throws ApiException, ApsSystemException {
        if (null == this.getIotConfig(jaxbIotConfig.getId())) {
            throw new ApiException(IApiErrorCodes.API_VALIDATION_ERROR, "IotConfig with id " + jaxbIotConfig.getId() + " does not exist", Response.Status.CONFLICT);
        }
        IotConfig iotConfig = jaxbIotConfig.getIotConfig();
        this.updateIotConfig(iotConfig);
    }

    /**
     * DELETE http://localhost:8080/<portal>/api/rs/en/iotConfig?id=1
	 * @param properties
     * @throws ApiException
     * @throws ApsSystemException
     */
    public void deleteIotConfigForApi(Properties properties) throws Throwable {
        String idString = properties.getProperty("id");
        int id = 0;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            throw new ApiException(IApiErrorCodes.API_PARAMETER_VALIDATION_ERROR, "Invalid Integer format for 'id' parameter - '" + idString + "'", Response.Status.CONFLICT);
        }
        this.deleteIotConfig(id);
    }

	private void notifyIotConfigChangedEvent(IotConfig iotConfig, int operationCode) {
		IotConfigChangedEvent event = new IotConfigChangedEvent();
		event.setIotConfig(iotConfig);
		event.setOperationCode(operationCode);
		this.notifyEvent(event);
	}

    @SuppressWarnings("rawtypes")
    public SearcherDaoPaginatedResult<IotConfig> getIotConfigs(FieldSearchFilter[] filters) throws ApsSystemException {
        SearcherDaoPaginatedResult<IotConfig> pagedResult = null;
        try {
            List<IotConfig> iotConfigs = new ArrayList<>();
            int count = this.getIotConfigDAO().countIotConfigs(filters);

            List<Integer> iotConfigNames = this.getIotConfigDAO().searchIotConfigs(filters);
            for (Integer iotConfigName : iotConfigNames) {
                iotConfigs.add(this.getIotConfig(iotConfigName));
            }
            pagedResult = new SearcherDaoPaginatedResult<IotConfig>(count, iotConfigs);
        } catch (Throwable t) {
            logger.error("Error searching iotConfigs", t);
            throw new ApsSystemException("Error searching iotConfigs", t);
        }
        return pagedResult;
    }

    @Override
    public SearcherDaoPaginatedResult<IotConfig> getIotConfigs(List<FieldSearchFilter> filters) throws ApsSystemException {
        FieldSearchFilter[] array = null;
        if (null != filters) {
            array = filters.toArray(new FieldSearchFilter[filters.size()]);
        }
        return this.getIotConfigs(array);
    }


	protected IKeyGeneratorManager getKeyGeneratorManager() {
		return _keyGeneratorManager;
	}
	public void setKeyGeneratorManager(IKeyGeneratorManager keyGeneratorManager) {
		this._keyGeneratorManager = keyGeneratorManager;
	}

	public void setIotConfigDAO(IIotConfigDAO iotConfigDAO) {
		 this._iotConfigDAO = iotConfigDAO;
	}
	protected IIotConfigDAO getIotConfigDAO() {
		return _iotConfigDAO;
	}

	private IKeyGeneratorManager _keyGeneratorManager;
	private IIotConfigDAO _iotConfigDAO;
}
