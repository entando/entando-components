package org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;

public class DashboardSitewhereDatasourceDto extends AbstractDashboardDatasourceDto {

    public DashboardSitewhereDatasourceDto() {
        super.setDatasourcesConfigDto(new SitewhereApplicationConfigDto()
        );
    }

    public SitewhereApplicationConfigDto getSitewhereDatasourceConfigDto() {
        return (SitewhereApplicationConfigDto) super.getDatasourcesConfigDto();
    }

    public void setSitewhereDatasourceConfigDto(
        SitewhereApplicationConfigDto sitewhereDatasourceConfigDto) {
        super.setDatasourcesConfigDto(sitewhereDatasourceConfigDto);
    }
    
}
