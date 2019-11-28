/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.plugins.jacms.aps.system.services.searchengine;

import com.agiletec.aps.system.common.tree.ITreeNode;
import com.agiletec.aps.system.common.tree.ITreeNodeManager;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.group.Group;
import com.agiletec.aps.system.services.lang.ILangManager;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.apache.lucene.util.BytesRef;
import org.entando.entando.aps.system.services.searchengine.*;
import org.slf4j.*;

import java.io.*;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

/**
 * Data Access Object dedita alle operazioni di ricerca 
 * ad uso del motore di ricerca interno.
 * @author E.Santoboni
 */
public class SearcherDAO implements ISearcherDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(SearcherDAO.class);

	private ITreeNodeManager treeNodeManager;
    private ILangManager langManager;

    private File indexDir;

	/**
	 * Inizializzazione del searcher.
	 * @param dir La cartella locale contenitore dei dati persistenti.
	 * @throws ApsSystemException In caso di errore
	 */
	@Override
	public void init(File dir) throws ApsSystemException {
		this.indexDir = dir;
	}
	
	private IndexSearcher getSearcher() throws IOException {
		FSDirectory directory = new SimpleFSDirectory(indexDir.toPath());
		IndexReader reader = DirectoryReader.open(directory);
		IndexSearcher searcher = new IndexSearcher(reader);
		return searcher;
	}
	
	private void releaseResources(IndexSearcher searcher) throws ApsSystemException {
		try {
			if (searcher != null) {
				searcher.getIndexReader().close();
			}
		} catch (IOException e) {
			throw new ApsSystemException("Error closing searcher", e);
		}
	}
	
    @Override
    public List<String> searchContentsId(SearchEngineFilter[] filters,
            SearchEngineFilter[] categories, Collection<String> allowedGroups) throws ApsSystemException {
        return this.searchContents(filters, categories, allowedGroups, false).getContentsId();
    }

    @Override
    public FacetedContentsResult searchFacetedContents(SearchEngineFilter[] filters,
            SearchEngineFilter[] categories, Collection<String> allowedGroups) throws ApsSystemException {
        return this.searchContents(filters, categories, allowedGroups, true);
    }
	
	protected FacetedContentsResult searchContents(SearchEngineFilter[] filters,
            SearchEngineFilter[] categories, Collection<String> allowedGroups, boolean faceted) throws ApsSystemException {
        FacetedContentsResult result = new FacetedContentsResult();
        List<String> contentsId = new ArrayList<>();
        IndexSearcher searcher = null;
        try {
            searcher = this.getSearcher();
            Query query = null;
            if ((null == filters || filters.length == 0)
                    && (null == categories || categories.length == 0)
                    && (allowedGroups != null && allowedGroups.contains(Group.ADMINS_GROUP_NAME))) {
                query = new MatchAllDocsQuery();
            } else {
                query = this.createQuery(filters, categories, allowedGroups);
            }
            Sort sort = null;
            boolean revert = false;
            if (null != filters) {
                for (int i = 0; i < filters.length; i++) {
                    SearchEngineFilter filter = filters[i];
                    if (null != filter.getOrder()) {
                        String fieldKey = this.getFilterKey(filter);
                        revert = filter.getOrder().toString().equalsIgnoreCase("DESC");
                        sort = new Sort(new SortField(fieldKey, SortField.Type.STRING));
                        break;
                    }
                }
            }
            TopDocs topDocs = (null != sort) ? searcher.search(query, 1000, sort) : searcher.search(query, 1000);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            Map<String, Integer> occurrences = new HashMap<>();
            if (scoreDocs.length > 0) {
                for (int index = 0; index < scoreDocs.length; index++) {
                    Document doc = searcher.doc(scoreDocs[index].doc);
                    contentsId.add(doc.get(IIndexerDAO.CONTENT_ID_FIELD_NAME));
                    if (faceted) {
                        Set<String> codes = new HashSet<>();
                        String[] categoryPaths = doc.getValues(IIndexerDAO.CONTENT_CATEGORY_FIELD_NAME);
                        for (int i = 0; i < categoryPaths.length; i++) {
                            String categoryPath = categoryPaths[i];
                            String[] paths = categoryPath.split(IIndexerDAO.CONTENT_CATEGORY_SEPARATOR);
                            codes.addAll(Arrays.asList(paths));
                        }
                        Iterator<String> iter = codes.iterator();
                        while (iter.hasNext()) {
                            String code = iter.next();
                            Integer value = occurrences.get(code);
                            if (null == value) {
                                value = 0;
                            }
                            occurrences.put(code, (value + 1));
                        }
                    }
                }
            }
            if (revert) {
                Collections.reverse(contentsId);
            }
            result.setOccurrences(occurrences);
            result.setContentsId(contentsId);
        } catch (IndexNotFoundException inf) {
            logger.error("no index was found in the Directory", inf);
        } catch (Throwable t) {
            logger.error("Error extracting documents", t);
            throw new ApsSystemException("Error extracting documents", t);
        } finally {
            this.releaseResources(searcher);
        }
        return result;
    }

    protected Query createQuery(SearchEngineFilter[] filters,
            SearchEngineFilter[] categories, Collection<String> allowedGroups) {
        BooleanQuery.Builder mainQuery = new BooleanQuery.Builder();
        if (filters != null && filters.length > 0) {
            for (int i = 0; i < filters.length; i++) {
                SearchEngineFilter filter = filters[i];
                Query fieldQuery = this.createQuery(filter);
                if (null != fieldQuery) {
                    mainQuery.add(fieldQuery, BooleanClause.Occur.MUST);
                }
            }
        }
        if (allowedGroups == null) {
            allowedGroups = new HashSet<>();
        }
        if (!allowedGroups.contains(Group.ADMINS_GROUP_NAME)) {
            if (!allowedGroups.contains(Group.FREE_GROUP_NAME)) {
                allowedGroups.add(Group.FREE_GROUP_NAME);
            }
            BooleanQuery.Builder groupsQuery = new BooleanQuery.Builder();
            Iterator<String> iterGroups = allowedGroups.iterator();
            while (iterGroups.hasNext()) {
                String group = iterGroups.next();
                TermQuery groupQuery = new TermQuery(new Term(IIndexerDAO.CONTENT_GROUP_FIELD_NAME, group));
                groupsQuery.add(groupQuery, BooleanClause.Occur.SHOULD);
            }
            mainQuery.add(groupsQuery.build(), BooleanClause.Occur.MUST);
        }
        if (null != categories && categories.length > 0) {
            BooleanQuery.Builder categoriesQuery = new BooleanQuery.Builder();
            for (int i = 0; i < categories.length; i++) {
                SearchEngineFilter categoryFilter = categories[i];
                List<String> allowedValues = categoryFilter.getAllowedValues();
                if (null != allowedValues && allowedValues.size() > 0) {
                    BooleanQuery.Builder singleCategoriesQuery = new BooleanQuery.Builder();
                    for (int j = 0; j < allowedValues.size(); j++) {
                        String singleCategory = allowedValues.get(j);
                        ITreeNode treeNode = this.getTreeNodeManager().getNode(singleCategory);
                        if (null != treeNode) {
                            String path = treeNode.getPath(IIndexerDAO.CONTENT_CATEGORY_SEPARATOR, false, this.getTreeNodeManager());
                            TermQuery categoryQuery = new TermQuery(new Term(IIndexerDAO.CONTENT_CATEGORY_FIELD_NAME, path));
                            singleCategoriesQuery.add(categoryQuery, BooleanClause.Occur.SHOULD);
                        }
                    }
                    categoriesQuery.add(singleCategoriesQuery.build(), BooleanClause.Occur.MUST);
                } else if (null != categoryFilter.getValue()) {
                    ITreeNode treeNode = this.getTreeNodeManager().getNode(categoryFilter.getValue().toString());
                    if (null != treeNode) {
                        String path = treeNode.getPath(IIndexerDAO.CONTENT_CATEGORY_SEPARATOR, false, this.getTreeNodeManager());
                        TermQuery categoryQuery = new TermQuery(new Term(IIndexerDAO.CONTENT_CATEGORY_FIELD_NAME, path));
                        categoriesQuery.add(categoryQuery, BooleanClause.Occur.MUST);
                    }
                }
            }
            mainQuery.add(categoriesQuery.build(), BooleanClause.Occur.MUST);
        }
        return mainQuery.build();
    }
    
    private Query createQuery(SearchEngineFilter filter) {
        BooleanQuery.Builder fieldQuery = null;
        String key = this.getFilterKey(filter);
        Object value = filter.getValue();
        List allowedValues = filter.getAllowedValues();
        if (null != allowedValues && !allowedValues.isEmpty()) {
            fieldQuery = new BooleanQuery.Builder();
            SearchEngineFilter.TextSearchOption option = filter.getTextSearchOption();
            if (null == option) {
                option = SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD;
            }
            //To be improved to manage different type
            for (int j = 0; j < allowedValues.size(); j++) {
                //NOTE: search for lower case....
                String singleValue = allowedValues.get(j).toString();
                String[] values = singleValue.split("\\s+");
                if (!option.equals(SearchEngineFilter.TextSearchOption.EXACT)) {
                    BooleanQuery.Builder singleOptionFieldQuery = new BooleanQuery.Builder();
                    BooleanClause.Occur bc = BooleanClause.Occur.SHOULD;
                    if (option.equals(SearchEngineFilter.TextSearchOption.ALL_WORDS)) {
                        bc = BooleanClause.Occur.MUST;
                    } else if (option.equals(SearchEngineFilter.TextSearchOption.ANY_WORD)) {
                        bc = BooleanClause.Occur.MUST_NOT;
                    }
                    for (int i = 0; i < values.length; i++) {
                        Query queryTerm = this.getTermQueryForTextSearch(key, values[i], filter.isLikeOption());
                        singleOptionFieldQuery.add(queryTerm, bc);
                    }
                    fieldQuery.add(singleOptionFieldQuery.build(), BooleanClause.Occur.SHOULD);
                } else {
                    PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
                    for (int i = 0; i < values.length; i++) {
                        phraseQuery.add(new Term(key, values[i].toLowerCase()), i);
                    }
                    fieldQuery.add(phraseQuery.build(), BooleanClause.Occur.SHOULD);
                }
            }
        } else if (null != value) {
            fieldQuery = new BooleanQuery.Builder();
            if (value instanceof String) {
                //NOTE: search for lower case....
                SearchEngineFilter.TextSearchOption option = filter.getTextSearchOption();
                if (null == option) {
                    option = SearchEngineFilter.TextSearchOption.AT_LEAST_ONE_WORD;
                }
                String stringValue = value.toString();
                String[] values = stringValue.split("\\s+");
                if (!option.equals(SearchEngineFilter.TextSearchOption.EXACT)) {
                    BooleanClause.Occur bc = BooleanClause.Occur.SHOULD;
                    if (option.equals(SearchEngineFilter.TextSearchOption.ALL_WORDS)) {
                        bc = BooleanClause.Occur.MUST;
                    } else if (option.equals(SearchEngineFilter.TextSearchOption.ANY_WORD)) {
                        bc = BooleanClause.Occur.MUST_NOT;
                    }
                    for (int i = 0; i < values.length; i++) {
                        Query queryTerm = this.getTermQueryForTextSearch(key, values[i], filter.isLikeOption());
                        fieldQuery.add(queryTerm, bc);
                    }
                } else {
                    PhraseQuery.Builder phraseQuery = new PhraseQuery.Builder();
                    for (int i = 0; i < values.length; i++) {
                        phraseQuery.add(new Term(key, values[i].toLowerCase()), i);
                    }
                    return phraseQuery.build();
                }
            } else if (value instanceof Date) {
                String toString = DateTools.timeToString(((Date) value).getTime(), DateTools.Resolution.MINUTE);
                TermQuery term = new TermQuery(new Term(key, toString));
                fieldQuery.add(term, BooleanClause.Occur.MUST);
            } else if (value instanceof Number) {
                TermQuery term = new TermQuery(new Term(key, value.toString()));
                fieldQuery.add(term, BooleanClause.Occur.MUST);
            }
        } else if (null != filter.getStart() || null != filter.getEnd()) {
            fieldQuery = new BooleanQuery.Builder();
            Query query = null;
            if (filter.getStart() instanceof Date || filter.getEnd() instanceof Date) {
                Long lowerValue = (null != filter.getStart()) ? ((Date) filter.getStart()).getTime() : Long.MIN_VALUE;
                Long upperValue = (null != filter.getEnd()) ? ((Date) filter.getEnd()).getTime() : Long.MAX_VALUE;
                query = LongPoint.newRangeQuery(key, lowerValue, upperValue);
            } else if (filter.getStart() instanceof Number || filter.getEnd() instanceof Number) {
                Long lowerValue = (null != filter.getStart()) ? ((Number) filter.getStart()).longValue() : Long.MIN_VALUE;
                Long upperValue = (null != filter.getEnd()) ? ((Number) filter.getEnd()).longValue() : Long.MAX_VALUE;
                query = LongPoint.newRangeQuery(key, lowerValue, upperValue);
            } else {
                String start = (null != filter.getStart()) ? filter.getStart().toString().toLowerCase() : null;
                String end = (null != filter.getEnd()) ? filter.getEnd().toString().toLowerCase() : null;
                query = new TermRangeQuery(key, new BytesRef(start), new BytesRef(end), true, true);
            }
            fieldQuery.add(query, BooleanClause.Occur.MUST);
        }
        if (null != fieldQuery) {
            return fieldQuery.build();
        }
        return null;
    }
    
    protected Query getTermQueryForTextSearch(String key, String value, boolean isLikeSearch) {
        //NOTE: search for lower case....
        String stringValue = value.toLowerCase();
        boolean useWildCard = false;
        if (value.startsWith("*") || value.endsWith("*")) {
            useWildCard = true;
        } else if (isLikeSearch) {
            stringValue = "*"+stringValue+"*";
            useWildCard = true;
        }
        Term term = new Term(key, stringValue);
        return (useWildCard) ? new WildcardQuery(term) : new TermQuery(term);
    } 
    
    protected String getFilterKey(SearchEngineFilter filter) {
        String key = filter.getKey();
        if (filter.isFullTextSearch()) {
            return key;
        }
        if (!filter.isAttributeFilter()
                && !(key.startsWith(IIndexerDAO.FIELD_PREFIX))) {
            key = IIndexerDAO.FIELD_PREFIX + key;
        } else if (filter.isAttributeFilter()) {
            String insertedLangCode = filter.getLangCode();
            String langCode = (StringUtils.isBlank(insertedLangCode)) ? this.getLangManager().getDefaultLang().getCode() : insertedLangCode;
            key = langCode.toLowerCase() + "_" + key;
        }
        return key;
    }
	
	@Override
    public void close() {
    	// nothing to do
    }

    public ITreeNodeManager getTreeNodeManager() {
        return treeNodeManager;
    }

    @Override
    public void setTreeNodeManager(ITreeNodeManager treeNodeManager) {
        this.treeNodeManager = treeNodeManager;
    }

    protected ILangManager getLangManager() {
        return langManager;
    }

    @Override
    public void setLangManager(ILangManager langManager) {
        this.langManager = langManager;
    }
    
}