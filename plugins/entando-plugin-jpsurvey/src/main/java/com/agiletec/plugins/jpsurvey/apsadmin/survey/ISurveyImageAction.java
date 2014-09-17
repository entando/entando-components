/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpsurvey.apsadmin.survey;

public interface ISurveyImageAction {

	/**
	 * Entry point per l'azione relativa all'associazione di immagini ad un survey
	 * @return
	 */
	public String associateSurveyImage();

	/**
	 * Associa un'immagine al sondaggio corrente
	 * @return
	 */
	public String joinImage();

	/**
	 * Ottiene la lingua di default per poter visualizzare correttamente il titolo del sondaggio sul quale si sta lavorando
	 * @return 
	 */
	public String getDefaultLangCode();

}