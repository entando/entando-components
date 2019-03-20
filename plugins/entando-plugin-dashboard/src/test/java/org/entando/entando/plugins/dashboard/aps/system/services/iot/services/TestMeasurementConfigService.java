package org.entando.entando.plugins.dashboard.aps.system.services.iot.services;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class TestMeasurementConfigService {

  @InjectMocks
  MeasurementConfigService measurementConfigService;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testSave(){
    MeasurementType measurementType =  new MeasurementType();
    measurementType.setName("temperature");
    measurementType.setType("int");
//    MeasurementConfig measurementConfig = measurementConfigService
//        .save(Arrays.asList(measurementType), "123456789", "1");
//    assertEquals(measurementConfig.getConfig().get("123456789_1_version1"),Arrays.asList(measurementType));
  }

}
