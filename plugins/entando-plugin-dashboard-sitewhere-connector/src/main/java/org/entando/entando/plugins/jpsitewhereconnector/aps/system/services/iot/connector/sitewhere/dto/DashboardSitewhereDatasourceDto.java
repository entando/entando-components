package org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.dto;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;

import static org.entando.entando.plugins.jpsitewhereconnector.aps.system.services.iot.connector.sitewhere.SitewhereConstants.SITEWHERE_SERVER_TYPE;

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

    @Override
    public String getServerType() {
        return SITEWHERE_SERVER_TYPE;
    }

    @Override
    public <T extends DatasourcesConfigDto> T getDatasource() {
        return (T) super.getDatasourcesConfigDto();
    }

    @Override
    public String getDatasourceCode() {
        return super.getDatasourcesConfigDto().getDatasourceCode();
    }

    @Override
    public boolean supports(String serverType) {
        return serverType.equals(this.getServerType());
    }
}
