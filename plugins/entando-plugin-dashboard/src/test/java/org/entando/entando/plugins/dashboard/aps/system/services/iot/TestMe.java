package org.entando.entando.plugins.dashboard.aps.system.services.iot;

import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.Measurement;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementConfig;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementTemplate;
import org.entando.entando.plugins.dashboard.aps.system.services.iot.model.MeasurementType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestMe {

  
  
  /*
  miisure > temperatura
  
  
   */
  
  public void testXX() {

    //
    MeasurementTemplate temperaturaEntando = new MeasurementTemplate();
    temperaturaEntando.setId("TEMP_XXX");
    temperaturaEntando.addField("e_data", "date");
    temperaturaEntando.addField("e_temperatura", "int");
    
    
    /**
     * // chiedere lo schema ad un server x e devo poter creare il mapping
     *  
     *  1) chedi schema:
     *    - kaa mi restituisce una schema json
     *
                 {
                 "id": "32768",
                 "metaInfo": {
                 "id": "32768",
                 "fqn": "org.kaaproject.kaa.schema.sample.DataCollection",
                 "tenantId": "1",
                 "applicationId": "32768"
                 },
                 "version": 1,
                 "body": "{\n  \"type\" : \"record\",\n  \"name\" : \"DataCollection\",\n  \"namespace\" : \"org.kaaproject.kaa.schema.sample\",\n  \"fields\" : [ {\n    \"name\" : \"temperature\",\n    \"type\" : \"int\"\n  } ],\n  \"version\" : 1,\n  \"dependencies\" : [ ],\n  \"displayName\" : \"DataCollection\"\n}",
                 "defaultRecord": "{\"temperature\":0}",
                 "createdUsername": "devuser",
                 "createdTime": 1551973247055,
                 "dependencySet": []
                 }
     "fields\" : [ {\n    \"name\" : \"temperature\",\n    \"type\" : \"int\"\n  } ]
     
     * 
     * questo serve a costruire il forma di mapping tra un MeasurementType e l'istanza delo stesso per un dato server (identificato da un token id)
     * 
     * 
     * 
     * GET_SCHEMA:
     * ogni server, per permettere al controller di costruire l'interfaccia di mapping deve avere una classe specifica, che estrae le property dello schema
     * 
     * 1) o prende dallo schema
     * 2) o prende un dato a caso per poi estrarne tutte le properties
     * 
     * 
     * ipotesi di salvataggio della conf
     * {
     *   "MeasurementType_id" : "TEMP_XXX",
     *   "tokenId" : "quello che identifica uno specifico DS  server X",
     *   "mapping" : 
     *   ...array
     *   {
     *    "sourceName" : "temperature",
     *    "sourceType" : "int",
     *    "dest" : "e_temperatura",
     *    "transoformer" : "optional bean name per la straformazione del valore"
     *   }
     *   {
     *    "source" : "oggetto.prop.x",
     *    "dest" : "temperatura",
     *    "transoformer" : "defaultl | noop | optional bean name per la straformazione del valore"
     *   }
     *   
     * }
     * 
     * 
     * //come mi prendo i dati?
     * 
     * 
     * 
     * 
     * {"temperature":26} //get data tokenId da server x 
     * {"temperature":33}
     * {"temperature":34}
     * {"temperature":32}
     * {"temperature":28}
     * {"temperature":25}
     * {"temperature":30}
     * {"temperature":25}
     * {"temperature":32}
     * {"temperature":32}
     * 
     * 
     * //??? prendi dati da server X
     * 2) parsa i dati in arrivo secondo lo schema sopraindicato
     * 3) output finale
     * 
     * 
     * 
     */

    
    

    /** outut finale: aka quello che deve essere eraogato al WIDGET entanto, secondo lo schema cui sopra
     *  {
     *    [
     *    {
     *    "e_data": "2012-02-02-10:10:59.999},
     *    "temperatura:" 12
     *    {
     *    "data": "2012-02-03-23:10:59.999},
     *    "temperatura:" 8
     *    }
     *    ]
     *    
     *  }
     * 
     */
    

    /*
    Map<String, List<MeasurementType>> config = new HashMap<>();
    config.put("app-token-KAA", schemaDiUnsStrutturaGenericaDiTemperatura);
    config.put("app-token-SW", schemaDiUnsStrutturaGenericaDiTemperatura);
 */

    
  }
  
  
}
