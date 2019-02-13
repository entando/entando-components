/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardgaugechart.model.DashboardGaugeChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardgaugechart.model.DashboardGaugeChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardgaugechart.validator.DashboardGaugeChartValidator;

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

public class DashboardGaugeChartService implements IDashboardGaugeChartService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardGaugeChartManager dashboardGaugeChartManager;
    private IDtoBuilder<DashboardGaugeChart, DashboardGaugeChartDto> dtoBuilder;


    protected IDashboardGaugeChartManager getDashboardGaugeChartManager() {
        return dashboardGaugeChartManager;
    }

    public void setDashboardGaugeChartManager(IDashboardGaugeChartManager dashboardGaugeChartManager) {
        this.dashboardGaugeChartManager = dashboardGaugeChartManager;
    }

    protected IDtoBuilder<DashboardGaugeChart, DashboardGaugeChartDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardGaugeChart, DashboardGaugeChartDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardGaugeChart, DashboardGaugeChartDto>() {

            @Override
            protected DashboardGaugeChartDto toDto(DashboardGaugeChart src) {
                DashboardGaugeChartDto dto = new DashboardGaugeChartDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardGaugeChartDto> getDashboardGaugeCharts(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardGaugeChartDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardGaugeChart> dashboardGaugeCharts = this.getDashboardGaugeChartManager().getDashboardGaugeCharts(filters);
            List<DashboardGaugeChartDto> dtoList = dtoBuilder.convert(dashboardGaugeCharts.getList());

            PagedMetadata<DashboardGaugeChartDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardGaugeCharts);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardGaugeCharts", t);
            throw new RestServerError("error in search dashboardGaugeCharts", t);
        }
    }

    @Override
    public DashboardGaugeChartDto updateDashboardGaugeChart(DashboardGaugeChartRequest dashboardGaugeChartRequest) {
        try {
	        DashboardGaugeChart dashboardGaugeChart = this.getDashboardGaugeChartManager().getDashboardGaugeChart(dashboardGaugeChartRequest.getId());
	        if (null == dashboardGaugeChart) {
	            throw new ResourceNotFoundException(DashboardGaugeChartValidator.ERRCODE_DASHBOARDGAUGECHART_NOT_FOUND, "dashboardGaugeChart", String.valueOf(dashboardGaugeChartRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardGaugeChartRequest, dashboardGaugeChart);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardGaugeChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardGaugeChartManager().updateDashboardGaugeChart(dashboardGaugeChart);
            return this.getDtoBuilder().convert(dashboardGaugeChart);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardGaugeChart {}", dashboardGaugeChartRequest.getId(), e);
            throw new RestServerError("error in update dashboardGaugeChart", e);
        }
    }

    @Override
    public DashboardGaugeChartDto addDashboardGaugeChart(DashboardGaugeChartRequest dashboardGaugeChartRequest) {
        try {
            DashboardGaugeChart dashboardGaugeChart = this.createDashboardGaugeChart(dashboardGaugeChartRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardGaugeChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardGaugeChartManager().addDashboardGaugeChart(dashboardGaugeChart);
            DashboardGaugeChartDto dto = this.getDtoBuilder().convert(dashboardGaugeChart);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardGaugeChart", e);
            throw new RestServerError("error in add dashboardGaugeChart", e);
        }
    }

    @Override
    public void removeDashboardGaugeChart(int  id) {
        try {
            DashboardGaugeChart dashboardGaugeChart = this.getDashboardGaugeChartManager().getDashboardGaugeChart(id);
            if (null == dashboardGaugeChart) {
                logger.info("dashboardGaugeChart {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardGaugeChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardGaugeChartManager().deleteDashboardGaugeChart(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardGaugeChart {}", id, e);
            throw new RestServerError("error in delete dashboardGaugeChart", e);
        }
    }

    @Override
    public DashboardGaugeChartDto getDashboardGaugeChart(int  id) {
        try {
	        DashboardGaugeChart dashboardGaugeChart = this.getDashboardGaugeChartManager().getDashboardGaugeChart(id);
	        if (null == dashboardGaugeChart) {
	            logger.warn("no dashboardGaugeChart found with code {}", id);
	            throw new ResourceNotFoundException(DashboardGaugeChartValidator.ERRCODE_DASHBOARDGAUGECHART_NOT_FOUND, "dashboardGaugeChart", String.valueOf(id));
	        }
	        DashboardGaugeChartDto dto = this.getDtoBuilder().convert(dashboardGaugeChart);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardGaugeChart {}", id, e);
            throw new RestServerError("error in loading dashboardGaugeChart", e);
        }
    }

    private DashboardGaugeChart createDashboardGaugeChart(DashboardGaugeChartRequest dashboardGaugeChartRequest) {
        DashboardGaugeChart dashboardGaugeChart = new DashboardGaugeChart();
        BeanUtils.copyProperties(dashboardGaugeChartRequest, dashboardGaugeChart);
        return dashboardGaugeChart;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardGaugeChart dashboardGaugeChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardGaugeChart, "dashboardGaugeChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardGaugeChart dashboardGaugeChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardGaugeChart, "dashboardGaugeChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardGaugeChart dashboardGaugeChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardGaugeChart, "dashboardGaugeChart");
        return errors;
    }

}

