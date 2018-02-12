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
package com.agiletec.plugins.jpblog.aps.system.services.blog;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.AbstractService;
import com.agiletec.aps.system.common.entity.ApsEntityManager;
import com.agiletec.aps.system.common.entity.model.EntitySearchFilter;
import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.entity.model.attribute.DateAttribute;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.ConfigInterface;
import com.agiletec.aps.system.services.category.Category;
import com.agiletec.aps.system.services.category.ICategoryManager;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.group.IGroupManager;
import com.agiletec.aps.util.DateConverter;
import com.agiletec.plugins.jacms.aps.system.services.content.IContentManager;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jpblog.aps.system.JpblogSystemConstants;
import com.agiletec.plugins.jpblog.aps.system.services.blog.model.GroupStatistic;
import com.agiletec.plugins.jpblog.aps.system.services.blog.util.BlogStatisticsUtil;

@Aspect
public class BlogManager extends AbstractService implements IBlogManager {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogManager.class);

	@Override
	public void init() throws Exception {
		this.loadConfigs();
		this.loadArchive();
		_logger.debug("{} ready", this.getClass().getName());
	}
	
	protected void loadConfigs() throws ApsSystemException {
		try {
			ConfigInterface configManager = this.getConfigManager();
			String xml = configManager.getConfigItem(JpblogSystemConstants.CONFIG_ITEM);
			if (xml == null) {
				throw new ApsSystemException("Configuration item not present: " + JpblogSystemConstants.CONFIG_ITEM);
			}
			this.setConfig(new BlogConfig(xml));
		} catch (Throwable t) {
			_logger.error("Error loading config", t);
			throw new ApsSystemException("Error loading config", t);
		}
	}
	
	protected void loadArchive() throws ApsSystemException {
		try {
			IContentManager contentManager = this.getContentManager();
			List<String> contents = this.loadContentIds();
			_logger.debug("Found {} for Blog archive", contents.size());
			
			Map<String, Map<String, List<GroupStatistic>>> archive = new HashMap<String, Map<String,List<GroupStatistic>>>();
			Iterator<String> contentIds = contents.iterator();
			while (contentIds.hasNext()) {
				String contentId = contentIds.next();
				Content content = contentManager.loadContent(contentId, true);
				Map<String, List<GroupStatistic>> typeCodeArchive = this.extractArchive(content.getTypeCode(), archive);
				this.addOccurrenceToArchive(content, typeCodeArchive);
			}
			this.setArchive(archive);
//			this.printCompleteArchive(archive);
		} catch (Throwable t) {
			_logger.error("Error loading archive statistics", t);
			throw new ApsSystemException("Errore loading archive statistics", t);
		}
	}
	/*
	private void printCompleteArchive(Map<String, Map<String, List<GroupStatistic>>> archive) {
		Iterator<Entry<String, Map<String, List<GroupStatistic>>>> archiveIter = archive.entrySet().iterator();
		while (archiveIter.hasNext()) {
			Entry<String, Map<String, List<GroupStatistic>>> entry = archiveIter.next();
			System.out.println("TypeCode: " + entry.getKey());
			this.printArchive(entry.getValue());
		}
	}
	
	private void printArchive(Map<String, List<GroupStatistic>> archive) {
		Iterator<Entry<String, List<GroupStatistic>>> archiveIter = archive.entrySet().iterator();
		while (archiveIter.hasNext()) {
			Entry<String, List<GroupStatistic>> monthStat = archiveIter.next();
			System.out.println("MONTH: " + monthStat.getKey());
			Iterator<GroupStatistic> groupStatsIter = monthStat.getValue().iterator();
			while (groupStatsIter.hasNext()) {
				GroupStatistic groupStat = groupStatsIter.next();
				System.out.println("OCCURRENCES: " + groupStat.getOccurrences() + " - GROUPS: " + groupStat.getGroups());
			}
		}
	}
	//*/
	public void updateConfig(IBlogConfig config) throws ApsSystemException {
		try {
			String xml = config.toXML();
			this.getConfigManager().updateConfigItem(JpblogSystemConstants.CONFIG_ITEM, xml);
			this.setConfig(config);
		} catch (Throwable t) {
			_logger.error("Error updating blog config", t);
			throw new ApsSystemException("Error updating blog config", t);
		}
	}
	
	@Override
	public List<BlogArchiveInfoBean> getOccurrencesByDate(String contentType, List<String> groupCodes) throws ApsSystemException {
		try {
			List<BlogArchiveInfoBean> list = new ArrayList<BlogArchiveInfoBean>();
			Map<String, List<GroupStatistic>> archive = this.extractArchive(contentType, this.getArchive());
			Iterator<Entry<String, List<GroupStatistic>>> archiveIter = archive.entrySet().iterator();
			while (archiveIter.hasNext()) {
				Entry<String, List<GroupStatistic>> monthStat = archiveIter.next();
				int occurrences = BlogStatisticsUtil.getOccurrences(groupCodes, monthStat.getValue());
				if (occurrences>0) {
					BlogArchiveInfoBean info = new BlogArchiveInfoBean();
					info.setGroupNames(groupCodes);
					String monthKey = monthStat.getKey();
					info.setYear(monthKey.substring(0, 4));
					info.setMonth(monthKey.substring(5, 7));
					info.setOccurrences(occurrences);
					list.add(info);
				}
			}
//			return this.getBlogDAO().getOccurrenceByDate(contentType, groupCodes);
			return list;
		} catch (Throwable t) {
			_logger.error("Errore loading OccurrencesByDate", t);
			throw new ApsSystemException("Errore in caricamento getOccurrencesByDate ");
		}
	}
	
	public List<String> getSpecialCategories() {
		List<String> catCodes = new ArrayList<String>();
		try {
			catCodes = this.getConfig().getCategories();
			if (null == catCodes || catCodes.size() == 0) {
				catCodes.add(this.getCategoryManager().getRoot().getCode());
			} else {
				catCodes = catCodes.subList(0, 1);
			}
			/*
			String catConfig = this.getConfigManager().getParam(JpblogSystemConstants.CONFIG_PARAM_BLOG_CATEGORIES);
			if (null != catConfig && catConfig.trim().length() > 0) {
				String[] code = catConfig.split(",");
				for (int i = 0; i < code.length; i++) {
					catCodes.add(code[i].trim());
				}
			}
			*/
		} catch (Throwable t) {
			_logger.error("Error loadng SpecialCategories", t);
			throw new RuntimeException("Errore in caricamento SpecialCategories");
		}
		return catCodes;
	}
	
	@Override
	public Map<Category, Integer> getOccurrences(List<String> contentTypeCodes, List<String> facetNodeCodes, List<String> groupCodes) throws ApsSystemException {
		Map<Category, Integer> occurrence = null;
		try {
			List<String> facetNodeCodes2 = new ArrayList<String>(facetNodeCodes);
			String categoryRootCode = this.getCategoryManager().getRoot().getCode();

			//se non esiste nessuna categoria specifica per il blog, la categoria di referimento è la root delle categorie
			//in questo caso, per la costruzione dello stat, NON deve essere passato il codice..
			//serve per far si che quando esiste una o più facetNodecodes lo stat sia ....WHERE contentrelations.refcategory = 'code'
			//ma quando nel blog non è settata nessuna categoria lo sta deve essere --....WHERE contentrelations.refcategory is not null.
			//perchè la categoria root NON viene scritta in contentrelations
			if (facetNodeCodes2.contains(categoryRootCode)) {
				facetNodeCodes2.remove(categoryRootCode);
			}
			Map<String, Integer> allOccurrence = this.getBlogDAO().getOccurrences(contentTypeCodes, facetNodeCodes2, groupCodes);
			if (null != allOccurrence) {
				occurrence = this.purgeMap(allOccurrence, facetNodeCodes, this.getCategoryManager(), null);
			}
		} catch (Throwable t) {
			_logger.error("Error loading occurrences", t);
			throw new ApsSystemException("Error loading occurrences", t);
		}
		return occurrence;
	}

	@Override
	public Map<Category, Integer> getOccurrences(List<String> contentTypeCodes, List<String> groupCodes) throws ApsSystemException {
		Map<Category, Integer> occurrence = null;
		try {
			List<String> facetNodeCodes = new ArrayList<String>(this.getSpecialCategories());
			String categoryRootCode = this.getCategoryManager().getRoot().getCode();

			//se non esiste nessuna categoria specifica per il blog, la categoria di referimento è la root delle categorie
			//in questo caso, per la costruzione dello stat, NON deve essere passato il codice..
			//serve per far si che quando esiste una o più facetNodecodes lo stat sia ....WHERE contentrelations.refcategory = 'code'
			//ma quando nel blog non è settata nessuna categoria lo sta deve essere --....WHERE contentrelations.refcategory is not null.
			//perchè la categoria root NON viene scritta in contentrelations
			if (facetNodeCodes.contains(categoryRootCode)) {
				facetNodeCodes.remove(categoryRootCode);
			}
			Map<String, Integer> allOccurrence = this.getBlogDAO().getOccurrences(contentTypeCodes, facetNodeCodes, groupCodes);

			if (null != allOccurrence) {
				occurrence = this.purgeMap(allOccurrence, this.getSpecialCategories(), this.getCategoryManager(), null);
			}
		} catch (Throwable t) {
			_logger.error("Error loading occurrences", t);
			throw new ApsSystemException("Error loading occurrences", t);
		}
		return occurrence;
	}

	/**
	 * Elimina dei risultati le categorie che non sono sui rami SpecialCategories.
	 * Inoltre elimina tutte le categorie che NON sono foglia
	 * @param occurrences
	 * @param facetNodeCodes
	 */
	/*private Map<Category, Integer> purgeMap(Map<String, Integer> occurrences, List<String> facetNodeCodes, ICategoryManager categoryManager) {
		Map<Category, Integer> purgedMap = new HashMap<Category, Integer>();
		for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
			String currentCat = entry.getKey();
			Category cat = categoryManager.getCategory(currentCat);
			boolean ok = false;
			if (null != cat) {
				for (int i = 0; i < facetNodeCodes.size(); i++) {
					String specialCatRootCode = facetNodeCodes.get(i);
					if (cat.isChildOf(specialCatRootCode) && cat.getChildren().length == 0) {
						ok = true;
						break;
					}
				}
			}
			if (ok) {
				purgedMap.put(cat, entry.getValue());
			}
		}
		return purgedMap;
	}
*/

	private Map<Category, Integer> purgeMap(Map<String, Integer> occurrences, List<String> facetNodeCodes, ICategoryManager categoryManager, Boolean onnyLeafs) {
		Map<Category, Integer> purgedMap = new HashMap<Category, Integer>();
		for (Map.Entry<String, Integer> entry : occurrences.entrySet()) {
			String currentCat = entry.getKey();
			Category cat = categoryManager.getCategory(currentCat);
			boolean ok = false;
			if (null != cat) {
				for (int i = 0; i < facetNodeCodes.size(); i++) {
					String specialCatRootCode = facetNodeCodes.get(i);
					boolean checkLeafs = null == onnyLeafs || !onnyLeafs.booleanValue() || (onnyLeafs.booleanValue() && cat.getChildren().length == 0);
					if (cat.isChildOf(specialCatRootCode) && checkLeafs) {
						ok = true;
						break;
					}
				}
			}
			if (ok) {
				purgedMap.put(cat, entry.getValue());
			}
		}
		return purgedMap;
	}
	
	// INIZIO - Metodi per gestione Archivio
	@Before("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.insertOnLineContent(..)) && args(content)")
	public void listenContentPublishing(Content content) {
		try {
			Object attribute = content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
			if (attribute!=null && attribute instanceof DateAttribute) {
				Date newDate = ((DateAttribute) attribute).getDate();
				Map<String, List<GroupStatistic>> archive = this.extractArchive(content.getTypeCode(), this.getArchive());
				Content oldContent = this.getContentManager().loadContent(content.getId(), true);
				if (oldContent==null) {// INSERT
					Set<String> groups = this.extractContentGroups(content);
					this.addOccurrenceToArchive(newDate, groups, content, archive);
				} else {// UPDATE
					this.updateOccurrenceToArchive(newDate, content, oldContent, archive);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in listenContentPublishing", t);
			// Non rilancia niente
		}
	}
	
	@Before("execution(* com.agiletec.plugins.jacms.aps.system.services.content.IContentManager.removeOnLineContent(..)) && args(content)")
	public void listenContentRemoving(Content content) {
		try {
			Object attribute = content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
			if (attribute!=null && attribute instanceof DateAttribute) {
				Content oldContent = this.getContentManager().loadContent(content.getId(), true);
				if (oldContent!=null) {// REMOVE
					Map<String, List<GroupStatistic>> archive = this.extractArchive(content.getTypeCode(), this.getArchive());
					Date oldDate = this.extractContentDate(oldContent);
					Set<String> oldGroups = this.extractContentGroups(oldContent);
					this.removeOccurrenceToArchive(oldDate, oldGroups, oldContent, archive);
				}
			}
		} catch (Throwable t) {
			_logger.error("error in listenContentRemoving", t);
			// Non rilancia niente
		}
	}
	
//	@Override
//	public void updateFromPublicContentChanged(PublicContentChangedEvent event) {
//		try {
//			Content content = event.getContent();
//			Object attribute = content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
//			if (attribute!=null && attribute instanceof DateAttribute) {
//				Date date = ((DateAttribute) attribute).getDate();
//				Map<String, List<GroupStatistic>> archive = this.extractArchive(content.getTypeCode(), this.getArchive());
//				int operationCode = event.getOperationCode();
//				if (operationCode==PublicContentChangedEvent.INSERT_OPERATION_CODE) {
//					Set<String> groups = this.extractContentGroups(content);
//					this.addOccurrenceToArchive(date, groups, archive);
//				} else if (operationCode==PublicContentChangedEvent.REMOVE_OPERATION_CODE) {
//					Content oldContent = this.getContentManager().loadContent(content.getId(), true);
//					Set<String> groups = this.extractContentGroups(oldContent);
//					this.removeOccurrenceToArchive(date, groups, archive);
//				} else {// UPDATE
////					this.updateOccurrenceToArchive(date, content, archive);
//				}
//				this.printCompleteArchive(this.getArchive());
//			}
//		} catch (Throwable t) {
//			ApsSystemUtils.logThrowable(t, this, "updateFromPublicContentChanged");
//			// Non rilancia niente
//		}
//	}
	
	private Date extractContentDate(Content content) {
		Date date = null;
		Object attribute = content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
		if (attribute!=null && attribute instanceof DateAttribute) {
			date = ((DateAttribute) attribute).getDate();
		}
		return date;
	}
	
	private List<String> loadContentIds() throws ApsSystemException {
		List<Object> typeCodes = new ArrayList<Object>();
		Iterator<IApsEntity> prototypes = this.getContentManager().getEntityPrototypes().values().iterator();
		while (prototypes.hasNext()) {
			IApsEntity prototype = prototypes.next();
			if (prototype.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE)!=null) {
				typeCodes.add(prototype.getTypeCode());
			}
		}
		if (typeCodes.size()>0) {
			EntitySearchFilter typeCodeFilter = new EntitySearchFilter(ApsEntityManager.ENTITY_TYPE_CODE_FILTER_KEY, false, typeCodes, false);
			EntitySearchFilter[] filters = { typeCodeFilter };
			List<Group> groups = this.getGroupManager().getGroups();
			List<String> groupCodes = new ArrayList<String>(groups.size());
			Iterator<Group> groupsIter = groups.iterator();
			while (groupsIter.hasNext()) {
				groupCodes.add(groupsIter.next().getAuthority());
			}
			return this.getContentManager().loadPublicContentsId(null, filters, groupCodes);
		}
		return new ArrayList<String>();
	}
	
	private void removeOccurrenceToArchive(Date date, Set<String> groups, Content content, Map<String, List<GroupStatistic>> archive) {
		if (date!=null) {
			String dateKey = DateConverter.getFormattedDate(date, MONTH_FORMAT_KEY);
			List<GroupStatistic> groupStatistics = archive.get(dateKey);
			if (groupStatistics!=null) {
				BlogStatisticsUtil.removeOccurrence(groups, groupStatistics);
				if (groupStatistics.isEmpty()) {
					archive.remove(dateKey);
				}
				_logger.debug("Removec occurrences for Content: {}", content.getId());
			}
		}
	}
	
	private void updateOccurrenceToArchive(Date newDate, Content newContent, Content oldContent, Map<String, List<GroupStatistic>> archive) throws ApsSystemException {
		Date oldDate = this.extractContentDate(oldContent);
		Set<String> groups = this.extractContentGroups(newContent);
		Set<String> oldGroups = this.extractContentGroups(oldContent);
		if (((newDate!=null || oldDate!=null) && (newDate==null || oldDate==null || newDate.compareTo(oldDate)!=0)) || 
				(groups.size()!=oldGroups.size() || !groups.containsAll(oldGroups))) {
			this.removeOccurrenceToArchive(oldDate, oldGroups, oldContent, archive);
			this.addOccurrenceToArchive(newDate, groups, newContent, archive);
		}
	}
	
	private void addOccurrenceToArchive(Date date, Set<String> groups, Content content, Map<String, List<GroupStatistic>> archive) {
		if (date!=null) {
			String dateKey = DateConverter.getFormattedDate(date, MONTH_FORMAT_KEY);
			List<GroupStatistic> groupStatistics = archive.get(dateKey);
			if (groupStatistics==null) {
				groupStatistics = new ArrayList<GroupStatistic>();
				archive.put(dateKey, groupStatistics);
			}
			BlogStatisticsUtil.addOccurrence(groups, groupStatistics);
			_logger.debug("Added occurrences for Content: {}", content.getId());
		}
	}
	
	private void addOccurrenceToArchive(Content content, Map<String, List<GroupStatistic>> archive) {
		Object attribute = content.getAttributeByRole(JpblogSystemConstants.ATTRIBUTE_ROLE_DATE);
		if (attribute!=null && attribute instanceof DateAttribute) {
			Date date = ((DateAttribute) attribute).getDate();
//			ApsSystemUtils.getLogger().info("Date: " + date);
			if (date!=null) {
				Set<String> groups = this.extractContentGroups(content);
//				ApsSystemUtils.getLogger().info("Groups: " + groups);
				this.addOccurrenceToArchive(date, groups, content, archive);
			}
		}
	}
	
	private Set<String> extractContentGroups(Content content) {
		Set<String> groups = new TreeSet<String>(content.getGroups());
		groups.add(content.getMainGroup());
		return groups;
	}
	// FINE - Metodi per gestione Archivio
	
	protected Map<String, List<GroupStatistic>> extractArchive(String typeCode, Map<String, Map<String, List<GroupStatistic>>> archive) {
		Map<String, List<GroupStatistic>> typeCodeArchive = archive.get(typeCode);
		if (typeCodeArchive==null) {
			typeCodeArchive = new TreeMap<String, List<GroupStatistic>>();
			archive.put(typeCode, typeCodeArchive);
		}
		return typeCodeArchive;
	}
	protected Map<String, Map<String, List<GroupStatistic>>> getArchive() {
		return _archive;
	}
	protected void setArchive(Map<String, Map<String, List<GroupStatistic>>> archive) {
		this._archive = archive;
	}
	
	public IBlogConfig getConfig() {
		return _config;
	}
	public void setConfig(IBlogConfig config) {
		this._config = config;
	}
	
	public void setConfigManager(ConfigInterface configManager) {
		this._configManager = configManager;
	}
	protected ConfigInterface getConfigManager() {
		return _configManager;
	}
	
	public void setBlogDAO(IBlogDAO blogDAO) {
		this._blogDAO = blogDAO;
	}
	protected IBlogDAO getBlogDAO() {
		return _blogDAO;
	}
	
	public void setCategoryManager(ICategoryManager categoryManager) {
		this._categoryManager = categoryManager;
	}
	protected ICategoryManager getCategoryManager() {
		return _categoryManager;
	}
	
	protected IContentManager getContentManager() {
		return _contentManager;
	}
	public void setContentManager(IContentManager contentManager) {
		this._contentManager = contentManager;
	}
	
	protected IGroupManager getGroupManager() {
		return _groupManager;
	}
	public void setGroupManager(IGroupManager groupManager) {
		this._groupManager = groupManager;
	}
	
	private ICategoryManager _categoryManager;
	private IBlogDAO _blogDAO;
	private ConfigInterface _configManager;
	private IContentManager _contentManager;
	private IGroupManager _groupManager;
	private IBlogConfig _config;
	
	private Map<String, Map<String, List<GroupStatistic>>> _archive;
	
	private final String MONTH_FORMAT_KEY = "yyyy-MM";
	
}