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
package com.agiletec.plugins.jpblog.aps.system.services.renderer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.agiletec.aps.system.common.entity.model.IApsEntity;
import com.agiletec.aps.system.common.renderer.EntityWrapper;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.renderer.BaseContentRenderer;
import com.agiletec.plugins.jpblog.aps.system.services.blog.IBlogManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.ICommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.Comment;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.model.CommentSearchBean;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.IRatingManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.model.Rating;

public class BlogContentRenderer extends BaseContentRenderer {

	private static final Logger _logger =  LoggerFactory.getLogger(BlogContentRenderer.class);
	
	@Override
	protected EntityWrapper getEntityWrapper(IApsEntity entity) {
		BlogContentWrapper wrapper = new BlogContentWrapper((Content)entity, this.getBeanFactory());
		List<String> comments = this.getComments(entity.getId());
		if (null != comments) {
			wrapper.setCommentsCount(comments.size());
		}
		Rating rating = this.getRating(entity.getId());
		if (null != rating) {
			wrapper.setRatingAverage(rating.getAverage());
			wrapper.setRatingCount(rating.getVoters());
		}
		List<String> specialCategoriesRootCodes = this.getBlogManager().getSpecialCategories();
		wrapper.setSpecialCategoriesRootCodes(specialCategoriesRootCodes);
		return wrapper;
	}
	
	private List<String> getComments(String contentId) {
		List<String> comments = new ArrayList<String>();
		try {
			CommentSearchBean commentSearchBean = new CommentSearchBean();
			commentSearchBean.setContentId(contentId);
			commentSearchBean.setStatus(Comment.STATUS_APPROVED);
			comments = this.getCommentManager().searchCommentIds(commentSearchBean);
		} catch (Throwable t) {
			_logger.error("Errore in caricamento lista commenti per contenuto {}", contentId, t);
			throw new RuntimeException("Errore in caricamento lista commenti per contenuto " + contentId); 
		}
		return comments;
	}

	private Rating getRating(String contentId) {
		Rating rating = null;
		try {
			rating = (Rating) this.getRatingManager().getContentRating(contentId);
		} catch (Throwable t) {
			_logger.error("Errore in caricamento rating per contenuto {}", contentId, t);
			throw new RuntimeException("Errore in caricamento rating per contenuto " + contentId); 
		}
		return rating;
	}

//	/**
//	 * Sono i codici di categoria specificati in sysconfig:params.
//	 * Se una categoria associata al contenuto è figlia di una di queste, sarà presente in getSpecialCategories del wrapper
//	 * @return
//	 */
//	private List<String> getSpecialCategories() {
//		List<String> catCodes = new ArrayList<String>();
//		try {
//			this.getBlogManager().getSpecialCategories()
//			String catConfig = this.getConfigManager().getParam(JpblogSystemConstants.CONFIG_PARAM_BLOG_CATEGORIES);
//			if (null != catConfig && catConfig.trim().length() > 0) {
//				String[] code = catConfig.split(",");
//				for (int i = 0; i < code.length; i++) {
//					catCodes.add(code[i].trim());
//				}
//				//catCodes = Arrays.asList(catConfig.split(","));
//			}
//		} catch (Throwable t) {
//			ApsSystemUtils.logThrowable(t, this, "getSpecialCategories");
//			throw new RuntimeException("Errore in caricamento SpecialCategories "); 
//		}
//		return catCodes;
//	}

	public void setRatingManager(IRatingManager ratingManager) {
		this._ratingManager = ratingManager;
	}
	protected IRatingManager getRatingManager() {
		return _ratingManager;
	}

	public void setCommentManager(ICommentManager commentManager) {
		this._commentManager = commentManager;
	}
	protected ICommentManager getCommentManager() {
		return _commentManager;
	}

	public void setBlogManager(IBlogManager blogManager) {
		this._blogManager = blogManager;
	}
	protected IBlogManager getBlogManager() {
		return _blogManager;
	}

	private IRatingManager _ratingManager;
	private ICommentManager _commentManager;
	private IBlogManager _blogManager;
}
