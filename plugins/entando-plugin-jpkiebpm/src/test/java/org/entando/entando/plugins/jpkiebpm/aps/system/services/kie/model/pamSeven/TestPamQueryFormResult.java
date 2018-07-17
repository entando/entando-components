package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.pamSeven;


import junit.framework.TestCase;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.KieVersionTransformer;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper.FormToBpmHelper;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieProcessFormQueryResult;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.KieTaskDetail;
import org.entando.entando.plugins.jprestapi.aps.core.helper.JAXBHelper;
import org.json.JSONObject;
import org.junit.Test;

import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.HashMap;

public class TestPamQueryFormResult extends TestCase {
	private final static transient Logger logger = Logger.getLogger(TestPamQueryFormResult.class);
	
	@Test
	public void testTaskDetailMarshal() throws Throwable{
		String response = "{\n" + 
				"  \"task-id\" : 1,\n" + 
				"  \"task-priority\" : 0,\n" + 
				"  \"task-name\" : \"Manually Appraise Borrower\",\n" + 
				"  \"task-subject\" : \"\",\n" + 
				"  \"task-description\" : \"\",\n" + 
				"  \"task-type\" : null,\n" + 
				"  \"task-form\" : \"Appraisal\",\n" + 
				"  \"task-status\" : \"Ready\",\n" + 
				"  \"task-actual-owner\" : \"\",\n" + 
				"  \"task-created-by\" : \"\",\n" + 
				"  \"task-created-on\" : {\n" + 
				"  \"java.util.Date\" : 1531691851738\n" + 
				"},\n" + 
				"  \"task-activation-time\" : {\n" + 
				"  \"java.util.Date\" : 1531691851738\n" + 
				"},\n" + 
				"  \"task-expiration-time\" : null,\n" + 
				"  \"task-skippable\" : true,\n" + 
				"  \"task-workitem-id\" : 1,\n" + 
				"  \"task-process-instance-id\" : 1,\n" + 
				"  \"task-parent-id\" : -1,\n" + 
				"  \"task-process-id\" : \"com.redhat.bpms.examples.mortgage.MortgageApplication\",\n" + 
				"  \"task-container-id\" : \"mortgage_1.0\",\n" + 
				"  \"task-pot-owners\" : [ \"appraiser\" ],\n" + 
				"  \"task-excl-owners\" : [ ],\n" + 
				"  \"task-business-admins\" : [ \"Administrator\", \"Administrators\" ],\n" + 
				"  \"task-input-data\" : null,\n" + 
				"  \"task-output-data\" : null\n" + 
				"}";
		
		StreamSource ss = new StreamSource(new java.io.StringReader(response));
		
		Object obj = JAXBHelper.unmarshall(ss, KieTaskDetail.class, false, true);
		
		logger.info("Obj:" + obj);
		
		assertNotNull("Expecting non-null marshalled object", obj);
		
	}

    @Test
    public void testPamSevenFormParse() throws Throwable{

        try {
//            String kieProcessFormXML = FileUtils.readFileToString(new File("src/test/resources/examples/bpmSampleFiles/jbpm7-mortgage-process-form.xml"));
           String kieProcessFormXML2 = FileUtils.readFileToString(new File("src/test/resources/examples/bpmSampleFiles/jbpm7-task-complete-form.xml"));

//            PamProcessQueryFormResult pamSeven = (PamProcessQueryFormResult) JAXBHelper
//                    .unmarshall(kieProcessFormXML, PamProcessQueryFormResult.class, true, false);
//
//            assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate());
//
//            assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate().getRows());
//            assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate().getRows().get(0).getLayoutColums().get(0).getLayoutComponents().get(0));
//            KieProcessFormQueryResult result = KieVersionTransformer.pamSevenFormToPamSix(pamSeven);

//            List<KieProcessFormQueryResult> forms =  result.getNestedForms();

            PamProcessQueryFormResult pamSeven2 = (PamProcessQueryFormResult) JAXBHelper
                    .unmarshall(kieProcessFormXML2, PamProcessQueryFormResult.class, true, false);

            KieProcessFormQueryResult result2 = KieVersionTransformer.pamSevenFormToPamSix(pamSeven2);

			String formData = FormToBpmHelper.generateHumanTaskFormJson(result2, new JSONObject(), new HashMap<>());
			System.out.println(formData);
//            List<KieProcessFormQueryResult> forms2 =  result2.getNestedForms();
//
//
//            assertEquals(3, forms.size());
//
//
//            String xml = JAXBHelper.marshall(result, true, false);
//
//            String xml2 = JAXBHelper.marshall(result2, true, false);
//            System.out.println(xml);
//
//            System.out.println(xml2);
        }catch(Throwable e){
            throw e;
        }


    }
    
    @Test
    public void testPamSevenSimpleFormParse() throws Throwable{
    	
    	try {
    		String kieProcessFormXML = FileUtils.readFileToString(new File("src/test/resources/examples/xml/jbpm7-simple-process-task-form.xml"));
    		
    		PamProcessQueryFormResult pamSeven = (PamProcessQueryFormResult) JAXBHelper
    				.unmarshall(kieProcessFormXML, PamProcessQueryFormResult.class, true, false);
    		
    		assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate());
    		
    		assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate().getRows());
    		//assertNotNull(pamSeven.getArrays().get(0).getLayoutTemplate().getRows().get(0).getLayoutColums().get(0).getLayoutComponents().get(0));
    		KieProcessFormQueryResult result = KieVersionTransformer.pamSevenFormToPamSix(pamSeven);
    		
    		String xml = JAXBHelper.marshall(result, true, false);
    		
    		logger.info(xml);
    		
    	}catch(Throwable e){
    		throw e;
    	}
    	
    	
    }
}
