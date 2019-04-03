package org.entando.entando.plugins.dashboard.web;

import javax.servlet.http.HttpSession;

import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.IDashboardConfigService;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DashboardConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.dashboardconfig.model.DatasourcesConfigDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.DashboardDatasourceDto;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementPayload;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.services.IConnectorService;
import org.entando.entando.plugins.dashboard.aps.system.services.storage.IotMessage;
import org.entando.entando.plugins.dashboard.web.iot.ConnectorController;
import org.entando.entando.web.AbstractControllerTest;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.xml.bind.v2.schemagen.xmlschema.Any;

import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConnectorControllerTest extends AbstractControllerTest {

	@Mock
	private IDashboardConfigService dashboardConfigService;

	@Mock
	private IConnectorService connectorService;

	@Mock
	private HttpSession httpSession;

	@InjectMocks
	private ConnectorController controller;
	
	
	@Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .addInterceptors(entandoOauth2Interceptor)
                .setHandlerExceptionResolvers(createHandlerExceptionResolver())
                .build();
    }

    @Test
    public void shouldGetExistingMeasurement() throws Exception {
       
    	IotMessage iotMessage = new IotMessage();
		MeasurementPayload m = new MeasurementPayload();
		JsonObject obj = new JsonObject();

		String jsonString = "{\"timestamp\":\"2019-03-22T15:03:26\",\"temperature\":\"20\"}";

		JsonElement c = new com.google.gson.JsonParser().parse(jsonString);
		obj.add("measure", c);

		List<JsonObject> misur = new ArrayList<JsonObject>();
		misur.add(obj);
//		m.setMeasurements(misur);
//		iotMessage.setContent(m);

		List<IotMessage> lista = new ArrayList<IotMessage>();
		lista.add(iotMessage);
		
		RestListRequest requestList = new RestListRequest();

		PagedMetadata<IotMessage> pagedMetadata = new PagedMetadata<>(requestList, 1);
		pagedMetadata.setBody(lista);
    	
        when(this.dashboardConfigService.existsById(Mockito.anyInt())).thenReturn(true);
        DashboardDatasourceDto dashboardDatasourceDto = new DashboardDatasourceDto();
        dashboardDatasourceDto.setDatasourcesConfigDto(new DatasourcesConfigDto());
        when(dashboardConfigService.getDashboardDatasourceDto(Mockito.anyInt(), Mockito.anyString())).thenReturn(dashboardDatasourceDto);
        
//        when(connectorService.getDeviceMeasurements(dashboardDatasourceDto, null,null, requestList)).thenReturn(pagedMetadata);
        
        String accessToken = "ddd";
        ResultActions result =mockMvc.perform(get("/plugins/dashboard/server/2/datasource/3/data")
                .header("Authorization", "Bearer " + accessToken));
//        result.andExpect(status().isOk());
    }
    
    
//    private String accessToken ="222";
//    
//    private ResultActions performGetContent(String code, String modelId,
//            boolean online, String langCode, UserDetails user) throws Exception {
//        String accessToken = mockOAuthInterceptor(user);
//        String path = "/plugins/cms/contents/{code}";
//        if (null != modelId) {
//            path += "/model/" + modelId;
//        }
//        path += "?status=" + ((online) ? IContentService.STATUS_ONLINE : IContentService.STATUS_DRAFT);
//        if (null != langCode) {
//            path += "&lang=" + langCode;
//        }
//        return mockMvc.perform(
//                get(path, code)
//                .sessionAttr("user", user)
//                .header("Authorization", "Bearer " + accessToken));
//    }
    
    

}
