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
package org.entando.entando.plugins.jpwebform.aps.system.services.form;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Message;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.Step;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepGuiConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.StepsConfig;
import org.entando.entando.plugins.jpwebform.aps.system.services.message.model.TypeVersionGuiConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ServletContextAware;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.util.FileTextReader;

/**
 * @author E.Santoboni
 */
public class GuiGeneratorManager extends AbstractService implements IGuiGeneratorManager, ServletContextAware {

	private static final Logger _logger =  LoggerFactory.getLogger(GuiGeneratorManager.class);

	@Override
	public void init() throws Exception {
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	@Override
	public void checkUserGuis(TypeVersionGuiConfig config) throws ApsSystemException {
		if (null == config) {
			throw new ApsSystemException("Null config");
		}
		Integer typeVersionCode = config.getVersion();
		String formTypeCode = config.getFormTypeCode();
		try {
			String folderPath = this.getServletContext().getRealPath("/");
			String baseGuiSubFolder = "/WEB-INF/plugins/jpwebform/aps/jsp/internalservlet/form/";
			String baseCssSubFolder = "/plugins/jpwebform/static/css/";
			String guiFolder = folderPath + baseGuiSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
			String cssFolder = this.getResourceDiskRootFolder() + baseCssSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
			List<StepGuiConfig> guiConfigs = config.getGuiConfigs();
			for (int i = 0; i < guiConfigs.size(); i++) {
				StepGuiConfig stepGuiConfig = guiConfigs.get(i);
				String guiFileName = "entryStep_" + stepGuiConfig.getStepCode() + ".jsp";
				String guiPath = guiFolder + guiFileName;
				File guiFile = new File(guiPath);
				if (!guiFile.exists()) {
					String contentGui = this.generateGui(stepGuiConfig, typeVersionCode);
					if (null == contentGui) {
						contentGui = "";
					}
					this.save(guiFileName, guiFolder, contentGui);
				}
				String cssFileName = "entryStep_" + stepGuiConfig.getStepCode() + ".css";
				String cssPath = cssFolder + cssFileName;
				File cssFile = new File(cssPath);
				if (!cssFile.exists()) {
					String contentCss = stepGuiConfig.getUserCss();
					if (null == contentCss) {
						contentCss = "";
					}
					this.save(cssFileName, cssFolder, contentCss);
				}
			}
		} catch (Throwable t) {
			_logger.error("Error initializing the configuration", t);
			throw new ApsSystemException("Error initializing the configuration", t);
		}
	}
	
	
	@Override
	public void generatePreview(StepGuiConfig config) throws ApsSystemException {
		if (null == config) {
			throw new ApsSystemException("Null config");
		}
		Integer typeVersionCode = 0;
		String formTypeCode = config.getFormTypeCode();
		try {
			String folderPath = this.getServletContext().getRealPath("/");
			String baseGuiSubFolder = "/WEB-INF/plugins/jpwebform/aps/jsp/internalservlet/form/";
			String baseCssSubFolder = "/plugins/jpwebform/static/css/";
			String guiFolder = folderPath + baseGuiSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
			String cssFolder = this.getResourceDiskRootFolder() + baseCssSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
			String guiFileName = "entryStep_" + config.getStepCode() + ".jsp";
			String cssFileName = "entryStep_" + config.getStepCode() + ".css";
			StepsConfig stepsConfig = this.getFormManager().getStepsConfig(formTypeCode);
			if(stepsConfig != null){
				List<Step> steps = stepsConfig.getSteps();
				for (int i = 0; i < steps.size(); i++) {
					Step step = steps.get(i);
					generatePreviewByStepCode(step.getCode(), config);
				} 
				generatePreviewByStepCode("confirm", config);
				generatePreviewByStepCode("ending", config);
			} else {
				this.save(guiFileName, guiFolder, "");
				this.save(cssFileName, cssFolder, "");
			}
		} catch (Throwable t) {
			_logger.error("Error in generatePreview", t);
			throw new ApsSystemException("Error initializing the configuration", t);
		}
	}
	
	private void generatePreviewByStepCode(String stepCode, StepGuiConfig config) throws ApsSystemException{
		Integer typeVersionCode = 0;
		String formTypeCode = config.getFormTypeCode();
		String folderPath = this.getServletContext().getRealPath("/");
		String baseGuiSubFolder = "/WEB-INF/plugins/jpwebform/aps/jsp/internalservlet/form/";
		String baseCssSubFolder = "/plugins/jpwebform/static/css/";
		String guiFolder = folderPath + baseGuiSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
		String cssFolder = this.getResourceDiskRootFolder() + baseCssSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
		String guiFileName = "entryStep_" + stepCode + ".jsp";
		String cssFileName = "entryStep_" + stepCode + ".css";
		StepGuiConfig stepGuiConfig = this.getFormManager().getWorkGuiConfig(formTypeCode, stepCode);
		String contentCss = "";
		String stepGui = "";
		if(config.getStepCode().equals(stepCode)){
			stepGui = this.generateGui(config, typeVersionCode);
			contentCss = config.getUserCss();
		} else if(stepGuiConfig != null){
			stepGui = this.generateGui(stepGuiConfig, typeVersionCode);
			contentCss = stepGuiConfig.getUserCss();
		}
		this.save(cssFileName, cssFolder, contentCss);
		this.save(guiFileName, guiFolder, stepGui);
	}
	
	@Override
	public void generatePreview(String formTypeCode) throws ApsSystemException {
		Integer typeVersionCode = 0;
		try {
			String folderPath = this.getServletContext().getRealPath("/");
			String baseGuiSubFolder = "/WEB-INF/plugins/jpwebform/aps/jsp/internalservlet/form/";
			String baseCssSubFolder = "/plugins/jpwebform/static/css/";
			String guiFolder = folderPath + baseGuiSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
			String cssFolder = this.getResourceDiskRootFolder() + baseCssSubFolder + formTypeCode + File.separator + typeVersionCode.toString() + File.separator;
			
			StepsConfig stepsConfig = this.getFormManager().getStepsConfig(formTypeCode);
			if(stepsConfig != null){
				List<Step> steps = stepsConfig.getSteps();
				for (int i = 0; i < steps.size(); i++) {
					String contentCss = "";
					String stepGui = "";
					Step step = steps.get(i);
					String guiFileName = "entryStep_" + step.getCode() + ".jsp";
					String cssFileName = "entryStep_" + step.getCode() + ".css";
					StepGuiConfig stepGuiConfig = this.getFormManager().getWorkGuiConfig(formTypeCode, step.getCode());
					stepGui = this.generateGui(stepGuiConfig, typeVersionCode);
					contentCss = stepGuiConfig.getUserCss();
					this.save(cssFileName, cssFolder, contentCss);
					this.save(guiFileName, guiFolder, stepGui);
					contentCss = "";
				}
			}
		} catch (Throwable t) {
			_logger.error("error in generatePreview for type {}", formTypeCode, t);
			throw new ApsSystemException("error in generatePreview", t);
		}
	}
	
	
	
	private String generateGui(StepGuiConfig stepGuiConfig, Integer typeVersionCode) throws ApsSystemException {
		StringBuilder gui = new StringBuilder();
		try {
			String startInclude = this.getText(COMMON_MODULES_FOLDER + "entryStep_start.wft", this.getServletContext());
			gui.append(this.parseInclude(startInclude, null, stepGuiConfig, typeVersionCode));
			String userContentGui = stepGuiConfig.getUserGui();
			int postfixLen = MARKER_END.length();
			int end = 0;
			int parsed = 0;
			int start = userContentGui.indexOf(MARKER_START);
			while (start >= 0) {
				end = userContentGui.indexOf(MARKER_END, start);
				if (end >= 0) {
					end = end + postfixLen;
					String marker = userContentGui.substring(start, end);
					String parsedMarker = this.parseMarker(marker, stepGuiConfig, typeVersionCode);
					if (parsedMarker != null) {
						String invariantText = userContentGui.substring(parsed, start);
						gui.append(invariantText);
						gui.append(parsedMarker);
						parsed = end;
					} else {
						end = start + 1;
					}
					start = userContentGui.indexOf(MARKER_START, end);
				} else {
					start = -1;
				}
			}
			String residualText = userContentGui.substring(parsed);
			gui.append(residualText);
		} catch (Throwable t) {
			_logger.error("Error generating gui", t);
			throw new ApsSystemException("Error generating gui", t);
		}
		return gui.toString();
	}
	
	protected String parseMarker(String marker, StepGuiConfig stepGuiConfig, Integer typeVersionCode) throws ApsSystemException {
		String contentMarker = marker.substring(MARKER_START.length(), (marker.length()-MARKER_END.length()));
		String parsedMarker = null;
		try {
			String include = null;
			if (contentMarker.indexOf(";") > 0) {
				Properties property = new Properties();
				String[] params = contentMarker.split(";");
				for (int i=0; i<params.length; i++) {
					String[] parameter = params[i].split("=");
					if (parameter.length == 2) {
						String value = parameter[1].trim();
						int length = value.length();
						if (value.startsWith("\"") && value.endsWith("\"") && length>1) {
							value = value.substring(1, length-2);
						}
						property.put(parameter[0].trim(), value);
					}
				}
				//[[#fieldName="Address";type="label"#]]
				if (property.containsKey("fieldName") && property.containsKey("type")) {
					parsedMarker = this.parseAttributeMarker(property, stepGuiConfig, typeVersionCode);
				} else {
					//TODO MANAGE CASE
					parsedMarker = marker;
				}
			} else {
				boolean checkStepGui = this.getFormManager().checkStepGui(stepGuiConfig.getFormTypeCode());
				if (contentMarker.equals("title")) {
					include = this.getText(COMMON_MODULES_FOLDER + "entryStep_title.wft", this.getServletContext());
				} else if (contentMarker.equals("form-start")) {
					if(typeVersionCode == 0) {
						include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-start_preview.wft", this.getServletContext());
					} else {
						include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-start.wft", this.getServletContext());
					}
				} else if (contentMarker.equals("form-end")) {
					if(typeVersionCode == 0) {
						include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-end_preview.wft", this.getServletContext());
					} else {
						include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-end.wft", this.getServletContext());
					}
				} else if (contentMarker.equals("form-back")) {
						if(typeVersionCode == 0 && checkStepGui) {
							include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-back_preview.wft", this.getServletContext());
						} else if(checkStepGui){
							include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-back.wft", this.getServletContext());
						} else {
							include ="";
						}
				} else if (contentMarker.equals("form-submit")) {
						if(typeVersionCode == 0 && checkStepGui) {
							include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-submit_preview.wft", this.getServletContext());
						} else if(checkStepGui){
							include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-submit.wft", this.getServletContext());
						} else {
							include = "";
						}
				} else if (contentMarker.equals("inputmail")) {
					include = this.getText(COMMON_MODULES_FOLDER + "entryStep_form-inputmail.wft", this.getServletContext());
				}
				if (null == include) {
					return marker;
				}
				parsedMarker = this.parseInclude(include, null, stepGuiConfig, typeVersionCode);
			}
		} catch (Throwable t) {
			_logger.error("Error parsing marker {}",marker, t);
			throw new ApsSystemException("Error parsing marker " + marker, t);
		}
		return parsedMarker;
	}
	
	private String parseAttributeMarker(Properties property, StepGuiConfig stepGuiConfig, Integer typeVersionCode) throws ApsSystemException {
		String parsed = null;
		Map<String, String> labels = new HashMap<String, String>();
		try {
			Properties extraMarker = new Properties();
			Message prototype = null;
			if(typeVersionCode == 0){
				prototype = (Message) this.getFormManager().getEntityPrototype(stepGuiConfig.getFormTypeCode());
			} else {
				prototype = (Message) this.getFormManager().getTypeVersionGui(stepGuiConfig.getFormTypeCode(), typeVersionCode).getPrototype();
			}
			String attributeName = property.getProperty("fieldName");
			extraMarker.put("attributeName", attributeName);
			String markerType = property.getProperty("type");
			AttributeInterface attribute = (AttributeInterface) prototype.getAttribute(attributeName);
			if (null != attribute) {
				if (markerType.equals("label")) {
					String labelValue = property.getProperty("label");
					String includeFileName = "";
					if (null != labelValue) {
						includeFileName = "attribute_label.wft";
						extraMarker.put("attributeLabel", labelValue);
					} else if (typeVersionCode == 0) {
						includeFileName = "attribute_label_preview.wft";
					} else {
						includeFileName = "attribute_label_i18n.wft";
					}
					String infoLabel = this.getText(ATTRIBUTES_MODULES_FOLDER + includeFileName, this.getServletContext());
					parsed = this.parseInclude(infoLabel, extraMarker, stepGuiConfig, typeVersionCode);
				} else if (markerType.equals("info")) {
					String infoInclude= "";
					if (typeVersionCode == 0) {
						infoInclude = this.getText(ATTRIBUTES_MODULES_FOLDER + "attribute_info_preview.wft", this.getServletContext());
					} else {
						infoInclude = this.getText(ATTRIBUTES_MODULES_FOLDER + "attribute_info.wft", this.getServletContext());
					}
					parsed = this.parseInclude(infoInclude, extraMarker, stepGuiConfig, typeVersionCode);
				} else if (markerType.equals("input")) {
					String inputInclude = this.extractInputInclude(property, attribute, typeVersionCode);
					parsed = this.parseInclude(inputInclude, extraMarker, stepGuiConfig, typeVersionCode);
				}else {
					parsed = "** INVALID MARKER ** ATTRIBUTE '" + attributeName + "' ** MARKER TYPE '" + markerType + "' **";
				}
			} else {
				parsed = "** NULL ATTRIBUTE '" + attributeName + "' ** MARKER TYPE '" + markerType + "' **";
			}
		} catch (Throwable t) {
			_logger.error("Error parsing attribute marker - property {}", property, t);
			throw new ApsSystemException("Error parsing attribute marker - property " + property, t);
		}
		return parsed;
	}
	
	private String extractInputInclude(Properties property, AttributeInterface attribute, Integer typeVersionCode) throws ApsSystemException {
		String include = "";
		try {
			String isEditString = property.getProperty("edit");
			boolean isEdit = (null == isEditString || Boolean.parseBoolean(isEditString));
			String subfolder = (isEdit) ? "edit" : "view";
			String folder = ATTRIBUTES_MODULES_FOLDER + subfolder + File.separator;
			if(typeVersionCode == 0){
				include = include.concat(this.getText(ATTRIBUTES_MODULES_FOLDER + "attribute_PreviewSource.wft", this.getServletContext()));
			} else {
				include = include.concat(this.getText(ATTRIBUTES_MODULES_FOLDER + "attribute_Source.wft", this.getServletContext()));
			}
			include = include.concat(this.getText(folder + "attribute_" + attribute.getType() + ".wft", this.getServletContext()));
		} catch (Throwable t) {
			_logger.error("Error extracting include {}", property, t);
			throw new ApsSystemException("Error extracting include " + property, t);
		}
		return include;
	}
	
	protected String parseInclude(String include, Properties extraMarker, StepGuiConfig stepGuiConfig, Integer typeVersionCode) throws ApsSystemException {
		String startParam = "$${";
		String endParam = "}$$";
		StringBuilder parsedText = new StringBuilder();
		try {
			int postfixLen = endParam.length();
			int end = 0;
			int parsed = 0;
			int start = include.indexOf(startParam);
			while (start >= 0) {
				end = include.indexOf(endParam, start);
				if (end >= 0) {
					end = end + postfixLen;
					String param = include.substring(start, end);
					String parsedParam = null;
					if (param.equals(startParam+"formTypeCode"+endParam)) {
						parsedParam = stepGuiConfig.getFormTypeCode();
					} else if (param.contains(startParam+"formTypeVersion"+endParam)) {
						parsedParam = String.valueOf(typeVersionCode);
					} else if (param.contains(startParam+"stepCode"+endParam)) {
						parsedParam = stepGuiConfig.getStepCode();
					} else if (null != extraMarker) {
						Iterator<Object> iter = extraMarker.keySet().iterator();
						while (iter.hasNext()) {
							String key = (String) iter.next();
							if (param.contains(startParam + key + endParam)) {
								parsedParam = extraMarker.getProperty(key);
							}
						}
					}
					if (parsedParam != null) {
						String invariantText = include.substring(parsed, start);
						parsedText.append(invariantText);
						parsedText.append(parsedParam);
						parsed = end;
					} else {
						end = start + 1;
					}
					start = include.indexOf(startParam, end);
				} else {
					start = -1;
				}
			}
			String residualText = include.substring(parsed);
			parsedText.append(residualText);
		} catch (Throwable t) {
			_logger.error("Error parsing include text", t);
			throw new ApsSystemException("Error parsing include text", t);
		}
		return parsedText.toString();
	}
	
	protected void save(String filename, String folder, String content) {
		FileOutputStream outStream = null;
		InputStream is = null;
		try {
			File dir = new File(folder);
			if (!dir.exists() || !dir.isDirectory()) {
				dir.mkdirs();
			}
			String filePath = folder + filename;
			is = new ByteArrayInputStream(content.getBytes());
			byte[] buffer = new byte[1024];
			int length = -1;
			outStream = new FileOutputStream(filePath);
			while ((length = is.read(buffer)) != -1) {
				outStream.write(buffer, 0, length);
				outStream.flush();
			}
		} catch (Throwable t) {
			_logger.error("error in save", t);
		} finally {
			try {
				if (null != outStream) outStream.close();
			} catch (Throwable t) {
				throw new RuntimeException("Error while closing OutputStream ", t);
			}
			try {
				if (null != is) is.close();
			} catch (Throwable t) {
				throw new RuntimeException("Error while closing InputStream ", t);
			}
		}
	}
	
	static protected String getText(String path, ServletContext servletContext) throws Throwable {
		String text = null;
		InputStream is = null;
		try {
			is = servletContext.getResourceAsStream(path);
			text = FileTextReader.getText(is);
		} catch (Throwable t) {
			throw new ApsSystemException("Error readind text", t);
		} finally {
			if (null != is) {
				is.close();
			}
		}
		return text;
	}
	
	protected String getResourceDiskRootFolder() {
		return _resourceDiskRootFolder;
	}
	public void setResourceDiskRootFolder(String resourceDiskRootFolder) {
		this._resourceDiskRootFolder = resourceDiskRootFolder;
	}
	
	protected IFormManager getFormManager() {
		return _formManager;
	}
	public void setFormManager(IFormManager formManager) {
		this._formManager = formManager;
	}
	
	protected ServletContext getServletContext() {
		return _servletContext;
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this._servletContext = servletContext;
	}

	private String _resourceDiskRootFolder;
	
	private IFormManager _formManager;
	private ServletContext _servletContext;
	
	public static final String TEMPLATE_FOLDER = "/WEB-INF/plugins/jpwebform/aps/template/";
	private static final String COMMON_MODULES_FOLDER = TEMPLATE_FOLDER + "step/";
	private static final String ATTRIBUTES_MODULES_FOLDER = TEMPLATE_FOLDER + "modules/";
	
	public static final String MARKER_START = "[[#";
	public static final String MARKER_END = "#]]";
}
