/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboarddonutchart.model.DashboardDonutChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboarddonutchart.model.DashboardDonutChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboarddonutchart.validator.DashboardDonutChartValidator;

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

public class DashboardDonutChartService implements IDashboardDonutChartService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardDonutChartManager dashboardDonutChartManager;
    private IDtoBuilder<DashboardDonutChart, DashboardDonutChartDto> dtoBuilder;


    protected IDashboardDonutChartManager getDashboardDonutChartManager() {
        return dashboardDonutChartManager;
    }

    public void setDashboardDonutChartManager(IDashboardDonutChartManager dashboardDonutChartManager) {
        this.dashboardDonutChartManager = dashboardDonutChartManager;
    }

    protected IDtoBuilder<DashboardDonutChart, DashboardDonutChartDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardDonutChart, DashboardDonutChartDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardDonutChart, DashboardDonutChartDto>() {

            @Override
            protected DashboardDonutChartDto toDto(DashboardDonutChart src) {
                DashboardDonutChartDto dto = new DashboardDonutChartDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardDonutChartDto> getDashboardDonutCharts(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardDonutChartDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardDonutChart> dashboardDonutCharts = this.getDashboardDonutChartManager().getDashboardDonutCharts(filters);
            List<DashboardDonutChartDto> dtoList = dtoBuilder.convert(dashboardDonutCharts.getList());

            PagedMetadata<DashboardDonutChartDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardDonutCharts);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardDonutCharts", t);
            throw new RestServerError("error in search dashboardDonutCharts", t);
        }
    }

    @Override
    public DashboardDonutChartDto updateDashboardDonutChart(DashboardDonutChartRequest dashboardDonutChartRequest) {
        try {
	        DashboardDonutChart dashboardDonutChart = this.getDashboardDonutChartManager().getDashboardDonutChart(dashboardDonutChartRequest.getId());
	        if (null == dashboardDonutChart) {
	            throw new RestRourceNotFoundException(DashboardDonutChartValidator.ERRCODE_DASHBOARDDONUTCHART_NOT_FOUND, "dashboardDonutChart", String.valueOf(dashboardDonutChartRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardDonutChartRequest, dashboardDonutChart);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardDonutChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardDonutChartManager().updateDashboardDonutChart(dashboardDonutChart);
            return this.getDtoBuilder().convert(dashboardDonutChart);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardDonutChart {}", dashboardDonutChartRequest.getId(), e);
            throw new RestServerError("error in update dashboardDonutChart", e);
        }
    }

    @Override
    public DashboardDonutChartDto addDashboardDonutChart(DashboardDonutChartRequest dashboardDonutChartRequest) {
        try {
            DashboardDonutChart dashboardDonutChart = this.createDashboardDonutChart(dashboardDonutChartRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardDonutChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardDonutChartManager().addDashboardDonutChart(dashboardDonutChart);
            DashboardDonutChartDto dto = this.getDtoBuilder().convert(dashboardDonutChart);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardDonutChart", e);
            throw new RestServerError("error in add dashboardDonutChart", e);
        }
    }

    @Override
    public void removeDashboardDonutChart(int  id) {
        try {
            DashboardDonutChart dashboardDonutChart = this.getDashboardDonutChartManager().getDashboardDonutChart(id);
            if (null == dashboardDonutChart) {
                logger.info("dashboardDonutChart {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardDonutChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardDonutChartManager().deleteDashboardDonutChart(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardDonutChart {}", id, e);
            throw new RestServerError("error in delete dashboardDonutChart", e);
        }
    }

    @Override
    public DashboardDonutChartDto getDashboardDonutChart(int  id) {
        try {
	        DashboardDonutChart dashboardDonutChart = this.getDashboardDonutChartManager().getDashboardDonutChart(id);
	        if (null == dashboardDonutChart) {
	            logger.warn("no dashboardDonutChart found with code {}", id);
	            throw new RestRourceNotFoundException(DashboardDonutChartValidator.ERRCODE_DASHBOARDDONUTCHART_NOT_FOUND, "dashboardDonutChart", String.valueOf(id));
	        }
	        DashboardDonutChartDto dto = this.getDtoBuilder().convert(dashboardDonutChart);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardDonutChart {}", id, e);
            throw new RestServerError("error in loading dashboardDonutChart", e);
        }
    }

    private DashboardDonutChart createDashboardDonutChart(DashboardDonutChartRequest dashboardDonutChartRequest) {
        DashboardDonutChart dashboardDonutChart = new DashboardDonutChart();
        BeanUtils.copyProperties(dashboardDonutChartRequest, dashboardDonutChart);
        return dashboardDonutChart;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardDonutChart dashboardDonutChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardDonutChart, "dashboardDonutChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardDonutChart dashboardDonutChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardDonutChart, "dashboardDonutChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardDonutChart dashboardDonutChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardDonutChart, "dashboardDonutChart");
        return errors;
    }

}

