package org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.AbstractDashboardDatasourceDto;

public class DashboardKaaDatasourceDto extends AbstractDashboardDatasourceDto {

    public DashboardKaaDatasourceDto() {
        super.setDatasourcesConfigDto(new KaaApplicationConfigDto()
        );
    }

    public KaaApplicationConfigDto getKaaDatasourceConfigDto() {
        return (KaaApplicationConfigDto) super.getDatasourcesConfigDto();
    }

    public void setKaaDatasourceConfigDto(
            KaaApplicationConfigDto kaaDatasourceConfigDto) {
        super.setDatasourcesConfigDto(kaaDatasourceConfigDto);
    }

    @Override
    public String getServerType() {
        return super.getDashboardConfigDto().getServerDescription();
    }

    @Override
    public int getDashboardId() {
        return super.getDashboardConfigDto().getId();
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
