/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.agiletec.plugins.jpcrowdsourcing.aps.system.services.idea;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.agiletec.aps.system.exception.ApsSystemException;


public interface IIdeaManager {

	/**
	 * Restituisce il nodo root delle categorie associate al servizio crowd sourcing
	 * @return
	 */
	public String getCategoryRoot();

	/**
	 * Restituisce la configurazione del servizio
	 * @return
	 */
	public CrowdSourcingConfig getConfig();

	/**
	 * Aggiorna la configurazione del servizio
	 * @param config
	 */
	public void updateConfig(CrowdSourcingConfig config);


	/**
	 * Inserisce una nuova idea
	 * @param idea
	 */
	public void addIdea(IIdea idea) throws ApsSystemException;

	/**
	 * Elimina difinitivamente un'idea
	 * @param id
	 * @throws ApsSystemException
	 */
	public void deleteIdea(String id) throws ApsSystemException;

	/**
	 * Restituisce un'idea in base all'identificativo
	 * @param id
	 * @return
	 * @throws ApsSystemException
	 */
	public IIdea getIdea(String id) throws ApsSystemException;

	/**
	 * Aggiorna un'idea (title, descr, status, voti e tags)
	 * @param idea
	 * @throws ApsSystemException
	 */
	public void updateIdea(IIdea idea) throws ApsSystemException;

	/**
	 *
	 * @param instancecode Ca be null
	 * @param status Ca be null
	 * @param text Searches in title and text
	 * @param category
	 * @param order cab be null.
	 * @return
	 */
	public List<String> searchIdeas(String instancecode, Integer status, String text, String category, Integer order) throws ApsSystemException;

	/**
	 * Restituisce la mappa dei tag associati alle idee con le relative occorrenze,
	 * @param status lo stato delle idee
	 * @return
	 * @throws ApsSystemException
	 */
	public Map<String, Integer> getIdeaTags(Integer status) throws ApsSystemException;



	public StatisticInfoBean getActiveIdeaStatistics(String instanceCode) throws ApsSystemException;

	public StatisticInfoBean getActiveIdeaStatistics(Collection<String> groupCodes) throws ApsSystemException;

}
