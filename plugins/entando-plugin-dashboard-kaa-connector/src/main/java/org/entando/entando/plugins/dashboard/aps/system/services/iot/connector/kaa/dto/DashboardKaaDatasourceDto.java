package org.entando.entando.plugins.dashboard.aps.system.services.iot.connector.kaa.dto;

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
	public boolean supports(String serverType) {
		// TODO Auto-generated method stub
		return false;
	}
}
