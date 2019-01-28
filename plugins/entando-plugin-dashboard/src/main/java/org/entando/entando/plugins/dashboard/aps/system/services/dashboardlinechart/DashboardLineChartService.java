/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardlinechart.model.DashboardLineChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardlinechart.model.DashboardLineChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardlinechart.validator.DashboardLineChartValidator;

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

public class DashboardLineChartService implements IDashboardLineChartService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardLineChartManager dashboardLineChartManager;
    private IDtoBuilder<DashboardLineChart, DashboardLineChartDto> dtoBuilder;


    protected IDashboardLineChartManager getDashboardLineChartManager() {
        return dashboardLineChartManager;
    }

    public void setDashboardLineChartManager(IDashboardLineChartManager dashboardLineChartManager) {
        this.dashboardLineChartManager = dashboardLineChartManager;
    }

    protected IDtoBuilder<DashboardLineChart, DashboardLineChartDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardLineChart, DashboardLineChartDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardLineChart, DashboardLineChartDto>() {

            @Override
            protected DashboardLineChartDto toDto(DashboardLineChart src) {
                DashboardLineChartDto dto = new DashboardLineChartDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardLineChartDto> getDashboardLineCharts(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardLineChartDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardLineChart> dashboardLineCharts = this.getDashboardLineChartManager().getDashboardLineCharts(filters);
            List<DashboardLineChartDto> dtoList = dtoBuilder.convert(dashboardLineCharts.getList());

            PagedMetadata<DashboardLineChartDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardLineCharts);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardLineCharts", t);
            throw new RestServerError("error in search dashboardLineCharts", t);
        }
    }

    @Override
    public DashboardLineChartDto updateDashboardLineChart(DashboardLineChartRequest dashboardLineChartRequest) {
        try {
	        DashboardLineChart dashboardLineChart = this.getDashboardLineChartManager().getDashboardLineChart(dashboardLineChartRequest.getId());
	        if (null == dashboardLineChart) {
	            throw new RestRourceNotFoundException(DashboardLineChartValidator.ERRCODE_DASHBOARDLINECHART_NOT_FOUND, "dashboardLineChart", String.valueOf(dashboardLineChartRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardLineChartRequest, dashboardLineChart);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardLineChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardLineChartManager().updateDashboardLineChart(dashboardLineChart);
            return this.getDtoBuilder().convert(dashboardLineChart);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardLineChart {}", dashboardLineChartRequest.getId(), e);
            throw new RestServerError("error in update dashboardLineChart", e);
        }
    }

    @Override
    public DashboardLineChartDto addDashboardLineChart(DashboardLineChartRequest dashboardLineChartRequest) {
        try {
            DashboardLineChart dashboardLineChart = this.createDashboardLineChart(dashboardLineChartRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardLineChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardLineChartManager().addDashboardLineChart(dashboardLineChart);
            DashboardLineChartDto dto = this.getDtoBuilder().convert(dashboardLineChart);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardLineChart", e);
            throw new RestServerError("error in add dashboardLineChart", e);
        }
    }

    @Override
    public void removeDashboardLineChart(int  id) {
        try {
            DashboardLineChart dashboardLineChart = this.getDashboardLineChartManager().getDashboardLineChart(id);
            if (null == dashboardLineChart) {
                logger.info("dashboardLineChart {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardLineChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardLineChartManager().deleteDashboardLineChart(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardLineChart {}", id, e);
            throw new RestServerError("error in delete dashboardLineChart", e);
        }
    }

    @Override
    public DashboardLineChartDto getDashboardLineChart(int  id) {
        try {
	        DashboardLineChart dashboardLineChart = this.getDashboardLineChartManager().getDashboardLineChart(id);
	        if (null == dashboardLineChart) {
	            logger.warn("no dashboardLineChart found with code {}", id);
	            throw new RestRourceNotFoundException(DashboardLineChartValidator.ERRCODE_DASHBOARDLINECHART_NOT_FOUND, "dashboardLineChart", String.valueOf(id));
	        }
	        DashboardLineChartDto dto = this.getDtoBuilder().convert(dashboardLineChart);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardLineChart {}", id, e);
            throw new RestServerError("error in loading dashboardLineChart", e);
        }
    }

    private DashboardLineChart createDashboardLineChart(DashboardLineChartRequest dashboardLineChartRequest) {
        DashboardLineChart dashboardLineChart = new DashboardLineChart();
        BeanUtils.copyProperties(dashboardLineChartRequest, dashboardLineChart);
        return dashboardLineChart;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardLineChart dashboardLineChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardLineChart, "dashboardLineChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardLineChart dashboardLineChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardLineChart, "dashboardLineChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardLineChart dashboardLineChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardLineChart, "dashboardLineChart");
        return errors;
    }

}

