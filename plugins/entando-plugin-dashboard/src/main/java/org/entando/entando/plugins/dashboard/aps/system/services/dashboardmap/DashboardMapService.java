/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardmap.model.DashboardMapDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardmap.model.DashboardMapRequest;
import org.entando.entando.plugins.dashboard.web.dashboardmap.validator.DashboardMapValidator;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import com.agiletec.aps.system.common.FieldSearchFilter;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.common.model.dao.SearcherDaoPaginatedResult;
import org.entando.entando.aps.system.exception.RestServerError;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.entando.entando.aps.system.services.DtoBuilder;
import org.entando.entando.aps.system.services.IDtoBuilder;
import org.entando.entando.aps.system.exception.ResourceNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DashboardMapService implements IDashboardMapService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardMapManager dashboardMapManager;
    private IDtoBuilder<DashboardMap, DashboardMapDto> dtoBuilder;


    protected IDashboardMapManager getDashboardMapManager() {
        return dashboardMapManager;
    }

    public void setDashboardMapManager(IDashboardMapManager dashboardMapManager) {
        this.dashboardMapManager = dashboardMapManager;
    }

    protected IDtoBuilder<DashboardMap, DashboardMapDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardMap, DashboardMapDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardMap, DashboardMapDto>() {

            @Override
            protected DashboardMapDto toDto(DashboardMap src) {
                DashboardMapDto dto = new DashboardMapDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardMapDto> getDashboardMaps(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardMapDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardMap> dashboardMaps = this.getDashboardMapManager().getDashboardMaps(filters);
            List<DashboardMapDto> dtoList = dtoBuilder.convert(dashboardMaps.getList());

            PagedMetadata<DashboardMapDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardMaps);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardMaps", t);
            throw new RestServerError("error in search dashboardMaps", t);
        }
    }

    @Override
    public DashboardMapDto updateDashboardMap(DashboardMapRequest dashboardMapRequest) {
        try {
	        DashboardMap dashboardMap = this.getDashboardMapManager().getDashboardMap(dashboardMapRequest.getId());
	        if (null == dashboardMap) {
	            throw new ResourceNotFoundException(DashboardMapValidator.ERRCODE_DASHBOARDMAP_NOT_FOUND, "dashboardMap", String.valueOf(dashboardMapRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardMapRequest, dashboardMap);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardMap);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardMapManager().updateDashboardMap(dashboardMap);
            return this.getDtoBuilder().convert(dashboardMap);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardMap {}", dashboardMapRequest.getId(), e);
            throw new RestServerError("error in update dashboardMap", e);
        }
    }

    @Override
    public DashboardMapDto addDashboardMap(DashboardMapRequest dashboardMapRequest) {
        try {
            DashboardMap dashboardMap = this.createDashboardMap(dashboardMapRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardMap);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardMapManager().addDashboardMap(dashboardMap);
            DashboardMapDto dto = this.getDtoBuilder().convert(dashboardMap);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardMap", e);
            throw new RestServerError("error in add dashboardMap", e);
        }
    }

    @Override
    public void removeDashboardMap(int  id) {
        try {
            DashboardMap dashboardMap = this.getDashboardMapManager().getDashboardMap(id);
            if (null == dashboardMap) {
                logger.info("dashboardMap {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardMap);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardMapManager().deleteDashboardMap(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardMap {}", id, e);
            throw new RestServerError("error in delete dashboardMap", e);
        }
    }

    @Override
    public DashboardMapDto getDashboardMap(int  id) {
        try {
	        DashboardMap dashboardMap = this.getDashboardMapManager().getDashboardMap(id);
	        if (null == dashboardMap) {
	            logger.warn("no dashboardMap found with code {}", id);
	            throw new ResourceNotFoundException(DashboardMapValidator.ERRCODE_DASHBOARDMAP_NOT_FOUND, "dashboardMap", String.valueOf(id));
	        }
	        DashboardMapDto dto = this.getDtoBuilder().convert(dashboardMap);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardMap {}", id, e);
            throw new RestServerError("error in loading dashboardMap", e);
        }
    }

    private DashboardMap createDashboardMap(DashboardMapRequest dashboardMapRequest) {
        DashboardMap dashboardMap = new DashboardMap();
        BeanUtils.copyProperties(dashboardMapRequest, dashboardMap);
        return dashboardMap;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardMap dashboardMap) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardMap, "dashboardMap");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardMap dashboardMap) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardMap, "dashboardMap");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardMap dashboardMap) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardMap, "dashboardMap");
        return errors;
    }

}

