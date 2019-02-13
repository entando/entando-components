/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardtable.model.DashboardTableDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardtable.model.DashboardTableRequest;
import org.entando.entando.plugins.dashboard.web.dashboardtable.validator.DashboardTableValidator;

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

public class DashboardTableService implements IDashboardTableService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardTableManager dashboardTableManager;
    private IDtoBuilder<DashboardTable, DashboardTableDto> dtoBuilder;


    protected IDashboardTableManager getDashboardTableManager() {
        return dashboardTableManager;
    }

    public void setDashboardTableManager(IDashboardTableManager dashboardTableManager) {
        this.dashboardTableManager = dashboardTableManager;
    }

    protected IDtoBuilder<DashboardTable, DashboardTableDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardTable, DashboardTableDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardTable, DashboardTableDto>() {

            @Override
            protected DashboardTableDto toDto(DashboardTable src) {
                DashboardTableDto dto = new DashboardTableDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardTableDto> getDashboardTables(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardTableDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardTable> dashboardTables = this.getDashboardTableManager().getDashboardTables(filters);
            List<DashboardTableDto> dtoList = dtoBuilder.convert(dashboardTables.getList());

            PagedMetadata<DashboardTableDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardTables);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardTables", t);
            throw new RestServerError("error in search dashboardTables", t);
        }
    }

    @Override
    public DashboardTableDto updateDashboardTable(DashboardTableRequest dashboardTableRequest) {
        try {
	        DashboardTable dashboardTable = this.getDashboardTableManager().getDashboardTable(dashboardTableRequest.getId());
	        if (null == dashboardTable) {
	            throw new ResourceNotFoundException(DashboardTableValidator.ERRCODE_DASHBOARDTABLE_NOT_FOUND, "dashboardTable", String.valueOf(dashboardTableRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardTableRequest, dashboardTable);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardTable);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardTableManager().updateDashboardTable(dashboardTable);
            return this.getDtoBuilder().convert(dashboardTable);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardTable {}", dashboardTableRequest.getId(), e);
            throw new RestServerError("error in update dashboardTable", e);
        }
    }

    @Override
    public DashboardTableDto addDashboardTable(DashboardTableRequest dashboardTableRequest) {
        try {
            DashboardTable dashboardTable = this.createDashboardTable(dashboardTableRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardTable);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardTableManager().addDashboardTable(dashboardTable);
            DashboardTableDto dto = this.getDtoBuilder().convert(dashboardTable);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardTable", e);
            throw new RestServerError("error in add dashboardTable", e);
        }
    }

    @Override
    public void removeDashboardTable(int  id) {
        try {
            DashboardTable dashboardTable = this.getDashboardTableManager().getDashboardTable(id);
            if (null == dashboardTable) {
                logger.info("dashboardTable {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardTable);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardTableManager().deleteDashboardTable(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardTable {}", id, e);
            throw new RestServerError("error in delete dashboardTable", e);
        }
    }

    @Override
    public DashboardTableDto getDashboardTable(int  id) {
        try {
	        DashboardTable dashboardTable = this.getDashboardTableManager().getDashboardTable(id);
	        if (null == dashboardTable) {
	            logger.warn("no dashboardTable found with code {}", id);
	            throw new ResourceNotFoundException(DashboardTableValidator.ERRCODE_DASHBOARDTABLE_NOT_FOUND, "dashboardTable", String.valueOf(id));
	        }
	        DashboardTableDto dto = this.getDtoBuilder().convert(dashboardTable);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardTable {}", id, e);
            throw new RestServerError("error in loading dashboardTable", e);
        }
    }

    private DashboardTable createDashboardTable(DashboardTableRequest dashboardTableRequest) {
        DashboardTable dashboardTable = new DashboardTable();
        BeanUtils.copyProperties(dashboardTableRequest, dashboardTable);
        return dashboardTable;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardTable dashboardTable) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardTable, "dashboardTable");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardTable dashboardTable) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardTable, "dashboardTable");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardTable dashboardTable) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardTable, "dashboardTable");
        return errors;
    }

}

