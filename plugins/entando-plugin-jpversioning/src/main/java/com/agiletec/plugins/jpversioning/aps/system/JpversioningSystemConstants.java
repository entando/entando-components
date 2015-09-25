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
package com.agiletec.plugins.jpversioning.aps.system;

/**
 * @author G.Cocco
 */
public interface JpversioningSystemConstants {
	
	/**
	 * Nome del servizio che gestisce le risorse cestinate.
	 */
	public static final String TRASHED_RESOURCE_MANAGER = "jpversioningTrashedResourceManager";
	
	/**
	 * Nome del servizio che gestisce le risorse cestinate.
	 */
	public static final String VERSIONING_MANAGER = "jpversioningVersioningManager";
	
	public final static String TRASHED_IMAGE_RESOURCE_DIR_NAME = "images";
	
	public final static String TRASHED_ATTACH_RESOURCE_DIR_NAME = "documents";
	
	public final static String CONFIG_PARAM_DELETE_MID_VERSIONS = "jpversioning_deleteMidVersions";
	
	public final static String CONFIG_PARAM_RESOURCE_TRASH_FOLDER = "jpversioning_resourceTrashRootDiskFolder";
	
	public final static String DEFAULT_RESOURCE_TRASH_FOLDER_NAME = "jpversioning/trashedresources";
	
}