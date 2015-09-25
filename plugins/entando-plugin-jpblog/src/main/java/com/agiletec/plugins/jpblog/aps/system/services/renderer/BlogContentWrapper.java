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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

import com.agiletec.aps.system.services.category.Category;
import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.renderer.ContentWrapper;

public class BlogContentWrapper extends ContentWrapper {

	/**
	 * Inizializzazione del Wrapper. 
	 * @param content Il contenuto da utilizzare 
	 * dal servizio di renderizzazione. 
	 */
	public BlogContentWrapper(Content content) {
		super(content);
	}

	public BlogContentWrapper(Content content, BeanFactory beanFactory) {
		super(content, beanFactory);
	}

	/**
	 * Numero dei commenti 
	 */
	public void setCommentsCount(int commentsCount) {
		this._commentsCount = commentsCount;
	}
	public int getCommentsCount() {
		return _commentsCount;
	}

	/**
	 * Numero dei votanti
	 */
	public int getRatingCount() {
		return _ratingCount;
	}
	public void setRatingCount(int ratingCount) {
		this._ratingCount = ratingCount;
	}

	/**
	 * Media dei voti
	 */
	public double getRatingAverage() {
		return _ratingAverage;
	}
	public void setRatingAverage(double ratingAverage) {
		this._ratingAverage = ratingAverage;
	}

	public String getRatingAverage(String format) {
		DecimalFormat f = new DecimalFormat(format);  // this will helps you to always keeps in two decimal places
		String val = f.format(this.getRatingAverage());
		return val;
	}

	public List<Category> getSpecialCategories() {
		List<Category> categories = new ArrayList<Category>();
		List<Category> allCategories = super.getCategories();
		if (null != allCategories) {
			Iterator<Category> it = allCategories.iterator();
			while (it.hasNext()) {
				Category cat = it.next();
				for (int i = 0; i < this._specialCategoriesRootCodes.size(); i++) {
					String rootCode = this._specialCategoriesRootCodes.get(i);
					if (cat.isChildOf(rootCode)) {
						categories.add(cat);
						break;
					}
				}
			}
		}
		return categories;
	}

	// LE CATEGORIE NON HANNO MAI FIGLI
	//	public List<Category> getSpecialCategoriesLeaf() {
	//		List<Category> categories = new ArrayList<Category>();
	//		List<Category> allCategories = super.getCategories();
	//		if (null != allCategories) {
	//			Iterator<Category> it = allCategories.iterator();
	//			while (it.hasNext()) {
	//				Category cat = it.next();
	//				for (int i = 0; i < this._specialCategoriesRootCodes.size(); i++) {
	//					String rootCode = this._specialCategoriesRootCodes.get(i);
	//					if (cat.isChildOf(rootCode) && cat.getChildren().length == 0) {
	//						categories.add(cat);
	//						break;
	//					}
	//				}
	//			}
	//		}
	//		return categories;
	//	}

	//	public List<Category> getTags() {
	//		List<Category> categories = new ArrayList<Category>();
	//		List<Category> allCategories = super.getCategories();
	//		if (null != allCategories) {
	//			Iterator<Category> it = allCategories.iterator();
	//			while (it.hasNext()) {
	//				Category cat = it.next();
	//				//!
	//				if (cat.isChildOf(ITagCloudManager.DEFAULT_CATEGORY_ROOT)) {
	//					categories.add(cat);
	//				}
	//			}
	//		}
	//		return categories;
	//	}


	public void setSpecialCategoriesRootCodes(List<String> specialCategoriesRootCodes) {
		this._specialCategoriesRootCodes = specialCategoriesRootCodes;
	}



	private int _commentsCount;
	private int _ratingCount;
	private double _ratingAverage;
	private List<String> _specialCategoriesRootCodes = new ArrayList<String>();

}

