/*
 * Copyright 2017-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.ContentThreadConstants;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentState;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.model.ContentSuspendMove;
import org.entando.entando.plugins.jpcontentscheduler.aps.system.services.content.util.Utils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.agiletec.aps.system.ApsSystemUtils;
import com.agiletec.aps.system.common.entity.model.AttributeFieldError;
import com.agiletec.aps.system.common.entity.model.AttributeTracer;
import com.agiletec.aps.system.common.entity.model.attribute.AttributeInterface;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.page.IPage;
import com.agiletec.aps.system.services.page.IPageManager;
import com.agiletec.aps.system.services.page.Widget;
import com.agiletec.aps.util.ApsProperties;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.ContentModel;
import com.agiletec.plugins.jacms.aps.system.services.contentmodel.IContentModelManager;

public class ContentJobs extends QuartzJobBean implements ApplicationContextAware {

	private static final Logger _logger = LoggerFactory.getLogger(ContentJobs.class);

	private static final String APPLICATION_CONTEXT_KEY = "applicationContext";

	private IContentSchedulerManager _contentSchedulerManager;
	private IContentManager _contentManager;
	private ICategoryManager _categoryManager;
	private IPageManager _pageManager;
	private IContentModelManager _contentModelManager;

	private ApplicationContext _ctx;

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		this._ctx = ac;
	}

	private ApplicationContext getApplicationContext(JobExecutionContext context) throws Exception {
		ApplicationContext appCtx = (ApplicationContext) context.getScheduler().getContext()
				.get(APPLICATION_CONTEXT_KEY);
		if (appCtx == null) {
			throw new JobExecutionException(
					ContentThreadConstants.APP_CTX_ERROR + "\"" + APPLICATION_CONTEXT_KEY + "\"");
		}
		return appCtx;
	}

	private void initBeans(ApplicationContext appCtx) {
		this.setContentSchedulerManager(
				(IContentSchedulerManager) appCtx.getBean("jpcontentschedulerContentSchedulerManager"));
		this.setContentModelManager((IContentModelManager) appCtx.getBean("jacmsContentModelManager"));
		this.setCategoryManager((ICategoryManager) appCtx.getBean("CategoryManager"));
		this.setPageManager((IPageManager) appCtx.getBean("PageManager"));
	}

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			ApplicationContext appCtx = getApplicationContext(context);
			this.initBeans(appCtx);
			this.executeJob(appCtx);
		} catch (Exception ex) {
			_logger.error("error in executeInternal", ex);
			throw new RuntimeException("Errore in inserimento/sospensione contenuti online", ex);
		}
	}

	public void executeJob(ApplicationContext appCtx) throws JobExecutionException {
		try {
			if (this.getContentSchedulerManager().getConfig()
					.isActive()/* && isCurrentSiteAllowed() */) {
				Date startJobDate = new Date();
				_logger.info(ContentThreadConstants.START_TIME_LOG + Utils.printTimeStamp(startJobDate));
				List<ContentState> removedContents = new ArrayList<ContentState>();
				List<ContentState> publishedContents = new ArrayList<ContentState>();
				List<ContentState> moveContents = new ArrayList<ContentState>();
				try {
					this.publishContentsJob(publishedContents);
					// System.out.println("CONTENUTI DA PUBLICARE -> " +
					// publishedContents);
					this.suspendOrMoveContentsJob(removedContents, moveContents, appCtx);
					try {
						Collections.sort(publishedContents);
						Collections.sort(removedContents);
						Collections.sort(moveContents);
						Date endJobDate = new Date();
						_logger.info(ContentThreadConstants.END_TIME_LOG + Utils.printTimeStamp(endJobDate));
						this.getContentSchedulerManager().sendMailWithResults(publishedContents, removedContents,
								moveContents, startJobDate, endJobDate);
					} catch (Throwable t) {
						throw new ApsSystemException(ContentThreadConstants.ERROR_ON_MAIL, t);
					}
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, t.getMessage());
					throw new RuntimeException("Errore in inserimento/sospensione contenuti online", t);
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, t.getMessage());
			throw new RuntimeException("Errore inizializzazione spring bean: ", t);
		}
	}

	private void publishContentsJob(List<ContentState> publishedContents) throws ApsSystemException {
		try {
			// Restituisce gli id dei contenuti che hanno un attributo con nome
			// key Data_inizio e valore la data corrente
			List<String> contentIds = this.getContentSchedulerManager().getContentIdToPublish();
			// System.out.println("CONTENUTI DA PUBLICARE ESTRATTI -> " +
			// contentIds);
			Iterator<String> iter = contentIds.iterator();
			// Cicla di contenuti da pubblicare in data odierna
			while (iter.hasNext()) {
				String contentId = null;
				try {
					contentId = (String) iter.next();
					Content contentToPublish = this.getContentManager().loadContent(contentId, false);
					if (null == contentToPublish) {
						publishedContents.add(new ContentState(contentId, "null", "null",
								ContentThreadConstants.PUBLISH_ACTION, ContentThreadConstants.NULL_CONTENT));
						_logger.info("Pubblicazione automatica non riuscita: " + contentId + " - "
								+ ContentThreadConstants.NULL_CONTENT);
						continue;
					}
					if (contentToPublish.isOnLine()) {
						publishedContents.add(new ContentState(contentToPublish.getId(), contentToPublish.getTypeCode(),
								contentToPublish.getDescription(), ContentThreadConstants.PUBLISH_ACTION,
								ContentThreadConstants.ISALREADYONLINE));
						_logger.info("Pubblicazione automatica non riuscita: " + contentToPublish.getId() + " - "
								+ ContentThreadConstants.ISALREADYONLINE);
						continue;
					}
					if (!Content.STATUS_READY.equals(contentToPublish.getStatus())) {
						publishedContents.add(new ContentState(contentToPublish.getId(), contentToPublish.getTypeCode(),
								contentToPublish.getDescription(), ContentThreadConstants.PUBLISH_ACTION,
								ContentThreadConstants.NOTREADYSTATUS));
						_logger.info("Pubblicazione automatica non riuscita: " + contentToPublish.getId() + " - "
								+ ContentThreadConstants.NOTREADYSTATUS);
						continue;
					}
					boolean validation = this.scanEntity(contentToPublish);
					if (!validation) {
						publishedContents.add(new ContentState(contentToPublish.getId(), contentToPublish.getTypeCode(),
								contentToPublish.getDescription(), ContentThreadConstants.PUBLISH_ACTION,
								ContentThreadConstants.CONTENTWITHERRORS));
						_logger.info("Pubblicazione automatica non riuscita: " + contentToPublish.getId() + " - "
								+ ContentThreadConstants.CONTENTWITHERRORS);
						continue;
					}
					// pubblicazione on line del contenuto e modifica data di
					// ultima modifica
					this.getContentManager().insertOnLineContent(contentToPublish);
					_logger.info("Pubblicato automaticamente contenuto " + contentToPublish.getId());
					publishedContents.add(new ContentState(contentToPublish.getId(), contentToPublish.getTypeCode(),
							contentToPublish.getDescription(), ContentThreadConstants.PUBLISH_ACTION,
							ContentThreadConstants.ACTION_SUCCESS));
				} catch (Throwable t) {
					ApsSystemUtils.logThrowable(t, this, ContentThreadConstants.UNEXPECTED_ERROR);
					publishedContents.add(new ContentState(contentId, "null", "null",
							ContentThreadConstants.PUBLISH_ACTION, t.getMessage()));
				}
			}
		} catch (Throwable t) {
			throw new ApsSystemException(ContentThreadConstants.ERROR_ON_PUBLISH, t);
		}
	}

	public boolean scanEntity(Content currentContent) {
		try {
			List<AttributeInterface> attributes = currentContent.getAttributeList();
			for (int i = 0; i < attributes.size(); i++) {
				AttributeInterface entityAttribute = attributes.get(i);
				if (entityAttribute.isActive()) {
					List<AttributeFieldError> errors = entityAttribute.validate(new AttributeTracer());
					if (null != errors && errors.size() > 0) {
						return false;
					}
				}
			}
		} catch (Throwable t) {
			_logger.error("Error scanning Entity", t);
			throw new RuntimeException("Error scanning Entity", t);
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	private void suspendOrMoveContentsJob(List<ContentState> removedContents, List<ContentState> moveContents,
			ApplicationContext appCtx) throws ApsSystemException {
		try {
			// Restituisce gli id dei contenuti che hanno un attributo con nome
			// key Data_fine e valore la data corrente
			List<ContentSuspendMove> content = this.getContentSchedulerManager().getContentAttrDataFine();
			// Cicla per tipo di contenuto
			for (ContentSuspendMove c : content) {
				List<String> contentIds = c.getIdsContents();
				// se suspend = true si effettua la depubblicazione
				if (c.getSuspend() != null && c.getSuspend().equalsIgnoreCase("true")) {
					Iterator<String> iter = contentIds.iterator();
					while (iter.hasNext()) {
						String contentId = null;
						try {
							contentId = (String) iter.next();
							Content contentToSuspend = this.getContentManager().loadContent(contentId, false);
							if (null == contentToSuspend) {
								removedContents.add(new ContentState(contentId, "null", "null",
										ContentThreadConstants.SUSPEND_ACTION, ContentThreadConstants.NULL_CONTENT));
								ApsSystemUtils.getLogger().info("Sospensione automatica non riuscita: " + contentId
										+ " - " + ContentThreadConstants.NULL_CONTENT);
								continue;
							}
							if (!contentToSuspend.isOnLine()) {
								removedContents.add(new ContentState(contentToSuspend.getId(),
										contentToSuspend.getTypeCode(), contentToSuspend.getDescription(),
										ContentThreadConstants.SUSPEND_ACTION,
										ContentThreadConstants.ISALREADYSUSPENDED));
								ApsSystemUtils.getLogger().info("Sospensione automatica non riuscita: "
										+ contentToSuspend.getId() + " - " + ContentThreadConstants.ISALREADYSUSPENDED);
								continue;
							}
							// controllo se il contenuto è correlato a delle
							// pagine
							Map<String, List> references = Utils.getReferencingObjects(contentToSuspend, appCtx);
							String operation = "suspend";
							if (references.size() > 0) {
								// se i vincoli sono la presenza del contenuto
								// nelle pagine.
								List<IPage> listaPagine = references.get("CmsPageManagerWrapperUtilizers");
								if (listaPagine != null && listaPagine.size() > 0) {
									String contentIdOfContentToPublish = c.getFieldNameContentReplace();
									String contenutoSostitutivo = null;
									if (contentIdOfContentToPublish != null && !contentIdOfContentToPublish.isEmpty()) {
                                                                                contenutoSostitutivo = contentIdOfContentToPublish;
									}
									String contentModelIdOfContentToPublish = c.getFieldNameModelContentReplace();
									String modelContenutoSostitutivo = null;
									if (contentModelIdOfContentToPublish != null && !contentModelIdOfContentToPublish.isEmpty()) {
                                                                            modelContenutoSostitutivo = contentModelIdOfContentToPublish;
									}
									// Contenuto sostitutivo impostato nel caso
									// in cui non sia presente il campo per il
									// contenuto sostitutivo
									String contSostIdRepl = c.getContentIdReplace();
									String contSostModelRepl = c.getContentModelReplace();
									if (contenutoSostitutivo == null || contenutoSostitutivo.isEmpty()) {
										if (contSostIdRepl != null) {
											contenutoSostitutivo = contSostIdRepl;
											modelContenutoSostitutivo = contSostModelRepl;
										} else {
											ApsSystemUtils.getLogger().info(
													"Errore nella Sospensione/Spostamento dei contenuti, manca l'attributo 'contentReplace' nella configurazione del plugin");
										}
									}
									if (contenutoSostitutivo != null && !contenutoSostitutivo.isEmpty()) {
										// Controlli sul contenuto sostitutivo
										Content contentToReplace = this.getContentManager()
												.loadContent(contenutoSostitutivo, false);
										if (null == contentToReplace) {
											removedContents.add(new ContentState(contentToSuspend.getId(),
													contentToSuspend.getTypeCode(), contentToSuspend.getDescription(),
													ContentThreadConstants.SUSPEND_ACTION,
													ContentThreadConstants.NULL_CONTENT_REPLACE));
											ApsSystemUtils.getLogger()
													.info("Sospensione automatica non riuscita: "
															+ contentToSuspend.getId() + " - "
															+ ContentThreadConstants.NULL_CONTENT_REPLACE);
											continue;
										}
										if (!contentToReplace.isOnLine()) {
											removedContents.add(new ContentState(contentToSuspend.getId(),
													contentToSuspend.getTypeCode(), contentToSuspend.getDescription(),
													ContentThreadConstants.SUSPEND_ACTION,
													ContentThreadConstants.ISNOTONLINE));
											ApsSystemUtils.getLogger()
													.info("Sospensione automatica non riuscita: "
															+ contentToSuspend.getId() + " - "
															+ ContentThreadConstants.ISNOTONLINE);
											continue;
										}
										boolean contentModelExist = false;
										if (modelContenutoSostitutivo != null) {
											List<ContentModel> listcontentModel = this.getContentModelManager()
													.getModelsForContentType(contentToReplace.getTypeCode());
											for (ContentModel contMod : listcontentModel) {
												if (String.valueOf(contMod.getId()).equals(modelContenutoSostitutivo)) {
													contentModelExist = true;
													break;
												}
											}
											if (!contentModelExist) {
												modelContenutoSostitutivo = contentToReplace.getDefaultModel();
											}
										}

										// Trova l'elenco delle pagine in cui il
										// contenuto è settato
										for (IPage page : listaPagine) {
											int framePos = -1;
											Widget widget = null;
											for (int i = 0; i < page.getWidgets().length; i++) {
												Widget w = page.getWidgets()[i];
												if (w != null) {
													if (w.getConfig() != null) {
														ApsProperties prop = w.getConfig();
														String codeContentPage = prop.getProperty("contentId");
														if (codeContentPage != null && codeContentPage
																.equalsIgnoreCase(contentToSuspend.getId())) {
															// trova il widget
															// ed il framepos,
															// poi setta il
															// contenuto
															// sotitutivo
															ApsProperties propconfig = new ApsProperties();
															propconfig.setProperty("contentId", contenutoSostitutivo);
															if (modelContenutoSostitutivo != null) {
																propconfig.setProperty("modelId",
																		modelContenutoSostitutivo);
															}
															w.setConfig(propconfig);
															framePos = i;
															widget = w;
															break;
														}
													}
												}
											} // fine ciclo dei widget
											if (framePos != -1 && widget != null) {
												this.getPageManager().joinWidget(page.getCode(), widget, framePos);
                                                                                                this.getPageManager().setPageOnline(page.getCode());
												operation = "replaceContent";
											}
										}
									} else {
										removedContents.add(new ContentState(contentToSuspend.getId(),
												contentToSuspend.getTypeCode(), contentToSuspend.getDescription(),
												ContentThreadConstants.SUSPEND_ACTION,
												ContentThreadConstants.CONTENT_WITH_REFERENCES));
										ApsSystemUtils.getLogger()
												.info("Sospensione automatica non riuscita: " + contentToSuspend.getId()
														+ " - " + ContentThreadConstants.CONTENT_WITH_REFERENCES);
										continue;
									}
								} else {
									// se i vincoli non sono pagine -->
									// verificare abilitazione controllo sui
									// link
									String controlLink = this.getContentSchedulerManager()
											.getSystemParam("referencesUnlink");
									boolean controlActive = false;
									if (controlLink == null || controlLink.isEmpty()) {
										// se il campo non è esistente metto a
										// true cosi non avviene la
										// depubblicazione
										controlActive = true;
									}
									if (controlLink != null && controlLink.equalsIgnoreCase("enabled")) {
										controlActive = true;
									}
									if (controlActive) {
										// controllo sui link abilitato, non
										// avviene la depubblicazione
										removedContents.add(new ContentState(contentToSuspend.getId(),
												contentToSuspend.getTypeCode(), contentToSuspend.getDescription(),
												ContentThreadConstants.SUSPEND_ACTION,
												ContentThreadConstants.CONTENT_WITH_REFERENCE_CONTENT));
										ApsSystemUtils.getLogger()
												.info("Sospensione automatica non riuscita: " + contentToSuspend.getId()
														+ " - " + ContentThreadConstants.CONTENT_WITH_REFERENCE_CONTENT);
										continue;
									} else {
										// controllo sui link non abilitato si
										// depubblica comunque, mettendo in
										// evidenza nella mail la
										// presenza di possibili link rotti
										// List<Content> listaContenutiRef =
										// references.get("jacmsContentManagerUtilizers");
										operation = "controlDisabled";
									}
								}
							} // fine if contenuti con referenze

							// Aggiungo la categoria globale, se non sono state
							// precisate categorie assegnate al tipo di
							// contenuto
							Category catGlobal = null;
							if (c.getGlobalCat() != null) {
								catGlobal = this.getCategoryManager().getCategory(c.getGlobalCat());
							} else {
								// senza categoria globale non posso effettuare
								// lo spostamento o la depubblicazione
								ApsSystemUtils.getLogger().info(
										"Errore nella Sospensione/Spostamento dei contenuti, manca l'attributo 'globalcat' nella configurazione del plugin");
								continue;
							}

							if (c.getCatType() != null && c.getCatType().size() > 0) {
								for (String code : c.getCatType()) {
									Category cat = this.getCategoryManager().getCategory(code);
									if (!contentToSuspend.getCategories().contains(cat)) {
										contentToSuspend.addCategory(cat);
									}
								}
							} else {
								if (catGlobal == null) {
									ApsSystemUtils.getLogger().info(
											"Errore nella Sospensione/Spostamento dei contenuti, l'attributo 'globalcat' non corrisponde ad alcuna categoria esistente");
									continue;
								}
								if (!contentToSuspend.getCategories().contains(catGlobal)) {
									contentToSuspend.addCategory(catGlobal);
								}
							}
							// Sospendi contenuto = non più visibile online e
							// non viene cambiata la data di ultima modifica
							this.getContentSchedulerManager().removeOnLineContent(contentToSuspend, false);
							if (operation.equals("suspend")) {
								ApsSystemUtils.getLogger()
										.info("Sospeso automaticamente contenuto " + contentToSuspend.getId());
								removedContents.add(new ContentState(contentToSuspend.getId(),
										contentToSuspend.getTypeCode(), contentToSuspend.getDescription(),
										ContentThreadConstants.SUSPEND_ACTION, ContentThreadConstants.ACTION_SUCCESS));
							} else if (operation.equals("replaceContent")) {
								ApsSystemUtils.getLogger()
										.info("Sospeso automaticamente contenuto " + contentToSuspend.getId());
								removedContents.add(new ContentState(contentToSuspend.getId(),
										contentToSuspend.getTypeCode(),
										contentToSuspend.getDescription() + " - "
												+ ContentThreadConstants.REPLACE_CONTENT,
										ContentThreadConstants.SUSPEND_ACTION, ContentThreadConstants.ACTION_SUCCESS));
							} else if (operation.equals("controlDisabled")) {
								removedContents.add(new ContentState(contentToSuspend.getId(),
										contentToSuspend.getTypeCode(),
										contentToSuspend.getDescription() + " - "
												+ ContentThreadConstants.CONTENT_WITH_REFERENCES_AND_BROKEN_LINK,
										ContentThreadConstants.SUSPEND_ACTION, ContentThreadConstants.ACTION_SUCCESS));
								ApsSystemUtils.getLogger()
										.info("Sospensione automatica riuscita: " + contentToSuspend.getId() + " - "
												+ ContentThreadConstants.CONTENT_WITH_REFERENCES_AND_BROKEN_LINK);
							}
						} catch (Throwable t) {
							ApsSystemUtils.logThrowable(t, this, ContentThreadConstants.UNEXPECTED_ERROR);
							removedContents.add(new ContentState(contentId, "null", "null",
									ContentThreadConstants.SUSPEND_ACTION, t.getMessage()));
						}
					} // chiusura while
				} else if (c.getSuspend() != null && c.getSuspend().equalsIgnoreCase("false")) { // SPOSTA
																									// IN
																									// ARCHIVIO
					// se suspend = false si effettua lo spostamento del
					// contenuto in archivio senza depubblicare
					Iterator<String> iter = contentIds.iterator();
					while (iter.hasNext()) {
						String contentId = null;
						try {
							contentId = (String) iter.next();
							Content contentToMove = this.getContentManager().loadContent(contentId, false);
							if (null == contentToMove) {
								moveContents.add(new ContentState(contentId, "null", "null",
										ContentThreadConstants.MOVE_ACTION, ContentThreadConstants.NULL_CONTENT));
								ApsSystemUtils.getLogger().info("Spostamento automatico in archivio non riuscito: "
										+ contentId + " - " + ContentThreadConstants.NULL_CONTENT);
								continue;
							}
							if (!contentToMove.isOnLine()) {
								moveContents.add(new ContentState(contentToMove.getId(), contentToMove.getTypeCode(),
										contentToMove.getDescription(), ContentThreadConstants.MOVE_ACTION,
										ContentThreadConstants.ISALREADYSUSPENDED));
								ApsSystemUtils.getLogger().info("Spostamento automatico in archivio non riuscito: "
										+ contentToMove.getId() + " - " + ContentThreadConstants.ISALREADYSUSPENDED);
								continue;
							}
							boolean alreadyMove = false;
							String operation = "move";
							Map<String, List> references = Utils.getReferencingObjects(contentToMove, appCtx);
							if (references.size() > 0) {
								// Trovati dei vincoli con il contenuto da
								// spostare
								// Trova l'elenco delle pagine in cui il
								// contenuto è settato
								List<IPage> listaPagine = references.get("PageManagerUtilizers");
								if (listaPagine != null && listaPagine.size() > 0) {
									String contentIdOfContentToPublish = c.getFieldNameContentReplace();
									String contenutoSostitutivo = null;
									if (contentIdOfContentToPublish != null && !contentIdOfContentToPublish.isEmpty()) {
                                                                                contenutoSostitutivo = contentIdOfContentToPublish;
									}
									String contentModelIdOfContentToPublish = c.getFieldNameModelContentReplace();
									String modelContenutoSostitutivo = null;
									if (contentModelIdOfContentToPublish != null && !contentModelIdOfContentToPublish.isEmpty()) {
                                                                            modelContenutoSostitutivo = contentModelIdOfContentToPublish;
									}
									// Contenuto sostitutivo impostato nel caso
									// in cui non sia presente il campo per il
									// contenuto sostitutivo
									String contSostIdRepl = c.getContentIdReplace();
									String contSostModelRepl = c.getContentModelReplace();
									if (contenutoSostitutivo == null || contenutoSostitutivo.isEmpty()) {
										if (contSostIdRepl != null) {
											contenutoSostitutivo = contSostIdRepl;
											modelContenutoSostitutivo = contSostModelRepl;
										} else {
											ApsSystemUtils.getLogger().info(
													"Errore nella Sospensione/Spostamento dei contenuti, manca l'attributo 'contentReplace' nella configurazione del plugin");
										}
									}
									if (contenutoSostitutivo != null && !contenutoSostitutivo.isEmpty()) {
										// Controlli sulla correttezza del
										// contenuto sostitutivo
										Content contentToReplace = this.getContentManager()
												.loadContent(contenutoSostitutivo, false);
										if (null == contentToReplace) {
											moveContents.add(new ContentState(contentToMove.getId(),
													contentToMove.getTypeCode(), contentToMove.getDescription(),
													ContentThreadConstants.SUSPEND_ACTION,
													ContentThreadConstants.NULL_CONTENT_REPLACE));
											ApsSystemUtils.getLogger()
													.info("Spostamento automatico in archivio: " + contentToMove.getId()
															+ " - " + ContentThreadConstants.NULL_CONTENT_REPLACE);
											continue;
										}
										if (!contentToReplace.isOnLine()) {
											moveContents.add(new ContentState(contentToMove.getId(),
													contentToMove.getTypeCode(), contentToMove.getDescription(),
													ContentThreadConstants.SUSPEND_ACTION,
													ContentThreadConstants.ISNOTONLINE));
											ApsSystemUtils.getLogger()
													.info("Spostamento automatico in archivio: " + contentToMove.getId()
															+ " - " + ContentThreadConstants.ISNOTONLINE);
											continue;
										}
										boolean contentModelExist = false;
										if (modelContenutoSostitutivo != null) {
											List<ContentModel> listcontentModel = this.getContentModelManager()
													.getModelsForContentType(contentToReplace.getTypeCode());
											for (ContentModel contMod : listcontentModel) {
												if (String.valueOf(contMod.getId()).equals(modelContenutoSostitutivo)) {
													contentModelExist = true;
													break;
												}
											}
											if (!contentModelExist) {
												modelContenutoSostitutivo = contentToReplace.getDefaultModel();
											}
										}
										// scorro la lista di pagine
										for (IPage page : listaPagine) {
											int framePos = -1;
											Widget widget = null;
											for (int i = 0; i < page.getWidgets().length; i++) {
												Widget w = page.getWidgets()[i];
												if (w != null) {
													if (w.getConfig() != null) {
														ApsProperties prop = w.getConfig();
														String codeContentPage = prop.getProperty("contentId");
														if (codeContentPage.equalsIgnoreCase(contentToMove.getId())) {
															// trova il widget
															// ed il framepos,
															// poi setta il
															// contenuto
															// sotitutivo
															ApsProperties propconfig = new ApsProperties();
															propconfig.setProperty("contentId", contenutoSostitutivo);
															if (modelContenutoSostitutivo != null) {
																propconfig.setProperty("modelId",
																		modelContenutoSostitutivo);
															}
															w.setConfig(propconfig);
															framePos = i;
															widget = w;
															break;
														}
													}
												}
											} // fine ciclo dei widget
											if (framePos != -1 && widget != null) {
												this.getPageManager().joinWidget(page.getCode(), widget, framePos);
                                                                                                this.getPageManager().setPageOnline(page.getCode());
												operation = "replaceContent";
											}
										}
									} else {
										moveContents.add(new ContentState(contentToMove.getId(),
												contentToMove.getTypeCode(), contentToMove.getDescription(),
												ContentThreadConstants.SUSPEND_ACTION,
												ContentThreadConstants.CONTENT_WITH_REFERENCES));
										ApsSystemUtils.getLogger()
												.info("Spostamento automatico in archivio: " + contentToMove.getId()
														+ " - " + ContentThreadConstants.CONTENT_WITH_REFERENCES);
										continue;
									}
								}
							}
							// Aggiungo la categoria globale, se non sono state
							// precisate categorie assegnate al tipo di
							// contenuto
							Category catGlobal = null;
							if (c.getGlobalCat() != null) {
								catGlobal = this.getCategoryManager().getCategory(c.getGlobalCat());
							} else {
								// senza categoria globale non posso effettuare
								// lo spostamento o la depubblicazione
								ApsSystemUtils.getLogger().info(
										"Errore nella Sospensione/Spostamento dei contenuti, manca l'attributo 'globalcat' nella configurazione del plugin");
								continue;
							}
							if (c.getCatType() != null && c.getCatType().size() > 0) {
								for (String code : c.getCatType()) {
									Category cat = this.getCategoryManager().getCategory(code);
									if (!contentToMove.getCategories().contains(cat)) {
										contentToMove.addCategory(cat);
										alreadyMove = true;
									}
								}
							} else {
								if (catGlobal == null) {
									ApsSystemUtils.getLogger().info(
											"Errore nella Sospensione/Spostamento dei contenuti, l'attributo 'globalcat' non corrisponde ad alcuna categoria esistente");
									continue;
								}
								if (!contentToMove.getCategories().contains(catGlobal)) {
									contentToMove.addCategory(catGlobal);
									alreadyMove = true;
								}
							}
							if (!alreadyMove && operation.equals("move")) {
								// se non son state inserite nuove categorie -
								// non c'è bisogno di effettuare il save
								// se non c'è stata alcuna sostituzione nelle
								// pagine
								ApsSystemUtils.getLogger().info(
										"Spostamento automatico in archivio non riuscito: " + contentToMove.getId());
								moveContents.add(new ContentState(contentToMove.getId(), contentToMove.getTypeCode(),
										contentToMove.getDescription(), ContentThreadConstants.MOVE_ACTION,
										ContentThreadConstants.ISALREADYMOVED));
								continue;
							} else if (!alreadyMove && operation.equals("replaceContent")) {// se
																							// c'è
																							// stata
																							// una
																							// sostituzione
								ApsSystemUtils.getLogger()
										.info("Spostamento automatico contenuto " + contentToMove.getId());
								moveContents.add(new ContentState(contentToMove.getId(), contentToMove.getTypeCode(),
										contentToMove.getDescription() + " - " + ContentThreadConstants.REPLACE_CONTENT,
										ContentThreadConstants.MOVE_ACTION, ContentThreadConstants.ACTION_SUCCESS));
								continue;
							}
							// Spostamento del contenuto in archivio - senza
							// modifica della data di ultima mod e senza
							// depubblicazione
							this.getContentSchedulerManager().moveOnLineContent(contentToMove, false, false);
							if (alreadyMove && operation.equals("move")) {
								ApsSystemUtils.getLogger()
										.info("Spostamento automatico contenuto " + contentToMove.getId());
								moveContents.add(new ContentState(contentToMove.getId(), contentToMove.getTypeCode(),
										contentToMove.getDescription(), ContentThreadConstants.MOVE_ACTION,
										ContentThreadConstants.ACTION_SUCCESS));
							} else if (alreadyMove && operation.equals("replaceContent")) {
								ApsSystemUtils.getLogger()
										.info("Spostamento automatico contenuto " + contentToMove.getId());
								moveContents.add(new ContentState(contentToMove.getId(), contentToMove.getTypeCode(),
										contentToMove.getDescription() + " - " + ContentThreadConstants.REPLACE_CONTENT,
										ContentThreadConstants.MOVE_ACTION, ContentThreadConstants.ACTION_SUCCESS));
							}
						} catch (Throwable t) {
							ApsSystemUtils.logThrowable(t, this, ContentThreadConstants.UNEXPECTED_ERROR);
							moveContents.add(new ContentState(contentId, "null", "null",
									ContentThreadConstants.MOVE_ACTION, t.getMessage()));
						}
					} // chiusura while
				} else {
					ApsSystemUtils.getLogger()
							.info("Errore nella Sospensione/Spostamento dei contenuti, manca l'attributo suspend nella configurazione del tipo di contenuto "
									+ c.getTypeContent());
					continue;
				}
			}
		} catch (Throwable t) {
			ApsSystemUtils.logThrowable(t, this, "removeOnLine");
			throw new RuntimeException("Errore in sospensione contenuti online", t);
		}
	}

	public IContentManager getContentManager() {
		return _contentManager;
	}

	public void setContentManager(IContentManager manager) {
		this._contentManager = manager;
	}

	public IContentSchedulerManager getContentSchedulerManager() {
		return _contentSchedulerManager;
	}

	public void setContentSchedulerManager(IContentSchedulerManager contentSchedulerManager) {
		this._contentSchedulerManager = contentSchedulerManager;
	}

	public ICategoryManager getCategoryManager() {
		return _categoryManager;
	}

	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}

	public IPageManager getPageManager() {
		return _pageManager;
	}

	public void setPageManager(IPageManager pageManager) {
		this._pageManager = pageManager;
	}

	public IContentModelManager getContentModelManager() {
		return _contentModelManager;
	}

	public void setContentModelManager(IContentModelManager contentModelManager) {
		this._contentModelManager = contentModelManager;
	}

}
