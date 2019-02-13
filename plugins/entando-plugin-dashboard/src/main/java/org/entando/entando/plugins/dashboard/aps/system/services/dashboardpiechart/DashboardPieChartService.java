/*
 *
 * <Your licensing text here>
 *
 */
package org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart;

import  org.entando.entando.plugins.dashboard.aps.system.services.dashboardpiechart.model.DashboardPieChartDto;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.plugins.dashboard.web.dashboardpiechart.model.DashboardPieChartRequest;
import org.entando.entando.plugins.dashboard.web.dashboardpiechart.validator.DashboardPieChartValidator;

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

public class DashboardPieChartService implements IDashboardPieChartService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDashboardPieChartManager dashboardPieChartManager;
    private IDtoBuilder<DashboardPieChart, DashboardPieChartDto> dtoBuilder;


    protected IDashboardPieChartManager getDashboardPieChartManager() {
        return dashboardPieChartManager;
    }

    public void setDashboardPieChartManager(IDashboardPieChartManager dashboardPieChartManager) {
        this.dashboardPieChartManager = dashboardPieChartManager;
    }

    protected IDtoBuilder<DashboardPieChart, DashboardPieChartDto> getDtoBuilder() {
        return dtoBuilder;
    }

    public void setDtoBuilder(IDtoBuilder<DashboardPieChart, DashboardPieChartDto> dtoBuilder) {
        this.dtoBuilder = dtoBuilder;
    }

    @PostConstruct
    public void onInit() {
        this.setDtoBuilder(new DtoBuilder<DashboardPieChart, DashboardPieChartDto>() {

            @Override
            protected DashboardPieChartDto toDto(DashboardPieChart src) {
                DashboardPieChartDto dto = new DashboardPieChartDto();
                BeanUtils.copyProperties(src, dto);
                return dto;
            }
        });
    }

    @Override
    public PagedMetadata<DashboardPieChartDto> getDashboardPieCharts(RestListRequest requestList) {
        try {
            List<FieldSearchFilter> filters = new ArrayList<FieldSearchFilter>(requestList.buildFieldSearchFilters());
            filters
                   .stream()
                   .filter(i -> i.getKey() != null)
                   .forEach(i -> i.setKey(DashboardPieChartDto.getEntityFieldName(i.getKey())));

            SearcherDaoPaginatedResult<DashboardPieChart> dashboardPieCharts = this.getDashboardPieChartManager().getDashboardPieCharts(filters);
            List<DashboardPieChartDto> dtoList = dtoBuilder.convert(dashboardPieCharts.getList());

            PagedMetadata<DashboardPieChartDto> pagedMetadata = new PagedMetadata<>(requestList, dashboardPieCharts);
            pagedMetadata.setBody(dtoList);

            return pagedMetadata;
        } catch (Throwable t) {
            logger.error("error in search dashboardPieCharts", t);
            throw new RestServerError("error in search dashboardPieCharts", t);
        }
    }

    @Override
    public DashboardPieChartDto updateDashboardPieChart(DashboardPieChartRequest dashboardPieChartRequest) {
        try {
	        DashboardPieChart dashboardPieChart = this.getDashboardPieChartManager().getDashboardPieChart(dashboardPieChartRequest.getId());
	        if (null == dashboardPieChart) {
	            throw new ResourceNotFoundException(DashboardPieChartValidator.ERRCODE_DASHBOARDPIECHART_NOT_FOUND, "dashboardPieChart", String.valueOf(dashboardPieChartRequest.getId()));
	        }
        	BeanUtils.copyProperties(dashboardPieChartRequest, dashboardPieChart);
            BeanPropertyBindingResult validationResult = this.validateForUpdate(dashboardPieChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardPieChartManager().updateDashboardPieChart(dashboardPieChart);
            return this.getDtoBuilder().convert(dashboardPieChart);
        } catch (ApsSystemException e) {
            logger.error("Error updating dashboardPieChart {}", dashboardPieChartRequest.getId(), e);
            throw new RestServerError("error in update dashboardPieChart", e);
        }
    }

    @Override
    public DashboardPieChartDto addDashboardPieChart(DashboardPieChartRequest dashboardPieChartRequest) {
        try {
            DashboardPieChart dashboardPieChart = this.createDashboardPieChart(dashboardPieChartRequest);
            BeanPropertyBindingResult validationResult = this.validateForAdd(dashboardPieChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardPieChartManager().addDashboardPieChart(dashboardPieChart);
            DashboardPieChartDto dto = this.getDtoBuilder().convert(dashboardPieChart);
            return dto;
        } catch (ApsSystemException e) {
            logger.error("Error adding a dashboardPieChart", e);
            throw new RestServerError("error in add dashboardPieChart", e);
        }
    }

    @Override
    public void removeDashboardPieChart(int  id) {
        try {
            DashboardPieChart dashboardPieChart = this.getDashboardPieChartManager().getDashboardPieChart(id);
            if (null == dashboardPieChart) {
                logger.info("dashboardPieChart {} does not exists", id);
                return;
            }
            BeanPropertyBindingResult validationResult = this.validateForDelete(dashboardPieChart);
            if (validationResult.hasErrors()) {
                throw new ValidationGenericException(validationResult);
            }
            this.getDashboardPieChartManager().deleteDashboardPieChart(id);
        } catch (ApsSystemException e) {
            logger.error("Error in delete dashboardPieChart {}", id, e);
            throw new RestServerError("error in delete dashboardPieChart", e);
        }
    }

    @Override
    public DashboardPieChartDto getDashboardPieChart(int  id) {
        try {
	        DashboardPieChart dashboardPieChart = this.getDashboardPieChartManager().getDashboardPieChart(id);
	        if (null == dashboardPieChart) {
	            logger.warn("no dashboardPieChart found with code {}", id);
	            throw new ResourceNotFoundException(DashboardPieChartValidator.ERRCODE_DASHBOARDPIECHART_NOT_FOUND, "dashboardPieChart", String.valueOf(id));
	        }
	        DashboardPieChartDto dto = this.getDtoBuilder().convert(dashboardPieChart);
	        return dto;
        } catch (ApsSystemException e) {
            logger.error("Error loading dashboardPieChart {}", id, e);
            throw new RestServerError("error in loading dashboardPieChart", e);
        }
    }

    private DashboardPieChart createDashboardPieChart(DashboardPieChartRequest dashboardPieChartRequest) {
        DashboardPieChart dashboardPieChart = new DashboardPieChart();
        BeanUtils.copyProperties(dashboardPieChartRequest, dashboardPieChart);
        return dashboardPieChart;
    }


    protected BeanPropertyBindingResult validateForAdd(DashboardPieChart dashboardPieChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardPieChart, "dashboardPieChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForDelete(DashboardPieChart dashboardPieChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardPieChart, "dashboardPieChart");
        return errors;
    }

    protected BeanPropertyBindingResult validateForUpdate(DashboardPieChart dashboardPieChart) {
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(dashboardPieChart, "dashboardPieChart");
        return errors;
    }

}

