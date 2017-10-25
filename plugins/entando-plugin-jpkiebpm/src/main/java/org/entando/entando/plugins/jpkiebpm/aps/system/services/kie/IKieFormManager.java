/*
 * The MIT License
 *
 * Copyright 2017 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.*;
import org.json.JSONObject;

import com.agiletec.aps.system.exception.ApsSystemException;

/**
 *
 * @author Entando
 */
public interface IKieFormManager {

	public final static String BEAN_NAME_ID = "jpkiebpmsManager";

	/**
	 * Return service configuration
	 *
	 * @return
	 */
	public KieBpmConfig getConfig();

	/**
	 * List KIE containers
	 *
	 * @return
	 * @throws com.agiletec.aps.system.exception.ApsSystemException
	 */
	public List<KieContainer> getContainersList() throws ApsSystemException;

	/**
	 * Update service configuration
	 *
	 * @param config
	 * @return
	 * @throws ApsSystemException
	 */
	public KieBpmConfig updateConfig(KieBpmConfig config) throws ApsSystemException;

	/**
	 * Return the process definition list
	 *
	 * @return
	 * @throws ApsSystemException
	 */
	public List<kieProcess> getProcessDefinitionsList() throws ApsSystemException;

	/**
	 * Get the process instances give the process ID
	 *
	 * @param processId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ApsSystemException
	 */
	public List<KieProcessInstance> getProcessInstancesList(String processId, int page, int pageSize) throws ApsSystemException;

	/**
	 * Get the list of human task
	 *
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws ApsSystemException
	 */
	public List<KieTask> getHumanTaskList(String groups, int page, int pageSize) throws ApsSystemException;

	/**
	 * Get the deiser human task form given container ID and form ID
	 *
	 * @param containerId
	 * @param taskId
	 * @return
	 * @throws ApsSystemException
	 */
	public KieProcessFormQueryResult getTaskForm(String containerId, long taskId) throws ApsSystemException;

	/**
	 * Return the process forms.
	 *
	 * @note This method marshals the XML returned by the server
	 * @param containerId
	 * @param processId
	 * @return
	 * @throws ApsSystemException
	 * @note This method marshals the XML returned by the server
	 */
	public KieProcessFormQueryResult getProcessForm(String containerId, String processId) throws ApsSystemException;

	/**
	 * Submit a form and start the related process
	 *
	 * @param containerId
	 * @param processId
	 * @param input
	 * @return
	 * @throws ApsSystemException
	 */
	public String startProcessSubmittingForm(String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

	public String startProcessSubmittingForm(KieProcessFormQueryResult form, String containerId, String processId, Map<String, Object> input) throws ApsSystemException;

	/**
	 * Get the process diagram
	 *
	 * @param containerId
	 * @param processId
	 * @return
	 * @throws ApsSystemException
	 */
	public String getProcInstDiagramImage(String containerId, String processId) throws ApsSystemException;

	/**
	 * Get the data of the current task
	 *
	 * @param containerId
	 * @param taskId
	 * @return
	 * @throws ApsSystemException
	 */
	public JSONObject getTaskFormData(String containerId, long taskId) throws ApsSystemException;

	/**
	 *
	 * @param containerId
	 * @param taskId
	 * @param input
	 * @return
	 * @throws ApsSystemException
	 */
	public String completeHumanFormTask(final String containerId, final long taskId, final Map<String, String> input) throws ApsSystemException;

	/**
	 *
	 *
	 * @param containerId
	 * @param taskId
	 * @param form
	 * @param task
	 * @param input
	 * @return
	 * @throws ApsSystemException
	 */
	String completeHumanFormTask(final String containerId, final long taskId, final KieProcessFormQueryResult form, final JSONObject task, final Map<String, Object> input)
			throws ApsSystemException;

	KieTask getHumanTask(String processId) throws ApsSystemException;

	KieTaskDetail getTaskDetail(final String containerId, final Long taskId) throws ApsSystemException;
}
