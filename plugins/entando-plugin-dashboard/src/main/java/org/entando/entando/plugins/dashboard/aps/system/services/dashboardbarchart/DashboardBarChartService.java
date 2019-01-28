/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardbarchart.model.DashboardBarChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardbarchart.model.DashboardBarChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardbarchart.validator.DashboardBarChartValidator;

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
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.aps.system.exception.RestRourceNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class DashboardBarChartService implements IDashboardBarChartService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardBarChartManager dashboardBarChartManager;
    private IDtoBuilder<DashboardBarChart, DashboardBarChartDto> dtoBuilder;


    protected IDashboardBarChartManager getDashboardBarChartManager() {
        return dashboardBarChartManager;
    }

    public void setDashboardBarChartManager(IDashboardBarChartManager dashboardBarChartManager) {
        this.dashboardBarChartManager = dashboardBarChartManager;
    }

    protected IDtoBuilder<DashboardBarChart, DashboardBarChartDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardBarChart, DashboardBarChartDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardBarChart, DashboardBarChartDto>() {

            @Override
            protected DashboardBarChartDto toDto(DashboardBarChart src) {
                DashboardBarChartDto dto = new DashboardBarChartDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardBarChartDto> getDashboardBarCharts(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardBarChartDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardBarChart> dashboardBarCharts = this.getDashboardBarChartManager().getDashboardBarCharts(filters);
            List<DashboardBarChartDto> dtoList = dtoBuilder.convert(dashboardBarCharts.getList());

            PagedMetadata<DashboardBarChartDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardBarCharts);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardBarCharts", t);
            throw new RestServerError("error in search dashboardBarCharts", t);
        }
    }

    @Override
    public DashboardBarChartDto updateDashboardBarChart(DashboardBarChartRequest dashboardBarChartRequest) {
        try {
	        DashboardBarChart dashboardBarChart = this.getDashboardBarChartManager().getDashboardBarChart(dashboardBarChartRequest.getId());
	        if (null == dashboardBarChart) {
	            throw new RestRourceNotFoundException(DashboardBarChartValidator.ERRCODE_DASHBOARDBARCHART_NOT_FOUND, "dashboardBarChart", String.valueOf(dashboardBarChartRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardBarChartRequest, dashboardBarChart);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardBarChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardBarChartManager().updateDashboardBarChart(dashboardBarChart);
            return this.getDtoBuilder().convert(dashboardBarChart);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardBarChart {}", dashboardBarChartRequest.getId(), e);
            throw new RestServerError("error in update dashboardBarChart", e);
        }
    }

    @Override
    public DashboardBarChartDto addDashboardBarChart(DashboardBarChartRequest dashboardBarChartRequest) {
        try {
            DashboardBarChart dashboardBarChart = this.createDashboardBarChart(dashboardBarChartRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardBarChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardBarChartManager().addDashboardBarChart(dashboardBarChart);
            DashboardBarChartDto dto = this.getDtoBuilder().convert(dashboardBarChart);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardBarChart", e);
            throw new RestServerError("error in add dashboardBarChart", e);
        }
    }

    @Override
    public void removeDashboardBarChart(int  id) {
        try {
            DashboardBarChart dashboardBarChart = this.getDashboardBarChartManager().getDashboardBarChart(id);
            if (null == dashboardBarChart) {
                logger.info("dashboardBarChart {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardBarChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardBarChartManager().deleteDashboardBarChart(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardBarChart {}", id, e);
            throw new RestServerError("error in delete dashboardBarChart", e);
        }
    }

    @Override
    public DashboardBarChartDto getDashboardBarChart(int  id) {
        try {
	        DashboardBarChart dashboardBarChart = this.getDashboardBarChartManager().getDashboardBarChart(id);
	        if (null == dashboardBarChart) {
	            logger.warn("no dashboardBarChart found with code {}", id);
	            throw new RestRourceNotFoundException(DashboardBarChartValidator.ERRCODE_DASHBOARDBARCHART_NOT_FOUND, "dashboardBarChart", String.valueOf(id));
	        }
	        DashboardBarChartDto dto = this.getDtoBuilder().convert(dashboardBarChart);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardBarChart {}", id, e);
            throw new RestServerError("error in loading dashboardBarChart", e);
        }
    }

    private DashboardBarChart createDashboardBarChart(DashboardBarChartRequest dashboardBarChartRequest) {
        DashboardBarChart dashboardBarChart = new DashboardBarChart();
        BeanUtils.copyProperties(dashboardBarChartRequest, dashboardBarChart);
        return dashboardBarChart;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardBarChart dashboardBarChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardBarChart, "dashboardBarChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardBarChart dashboardBarChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardBarChart, "dashboardBarChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardBarChart dashboardBarChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardBarChart, "dashboardBarChart");
        return errors;
    }

}

