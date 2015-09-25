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

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IIdea {

	public String getId();

	public String getTitle();

	public String getDescr();

	public Date getPubDate();

	public String getUsername();
	
	public int getStatus();

	public List<String> getTags();

	public int getVotePositive();

	public int getVoteNegative();

	public Map<Integer, List<Integer>> getComments();
	
	public List<Integer> getCommentsToApprove();
	public List<Integer> getCommentsNotApproved();
	public List<Integer> getCommentsApproved();
	
	public String getInstanceCode();
	/**
	 * Status of not approved
	 */
	public static int STATUS_NOT_APPROVED = 1;

	/**
	 * Status of to approve
	 */
	public static int STATUS_TO_APPROVE = 2;

	/**
	 * Status of approved
	 */
	public static int STATUS_APPROVED = 3;
	
	public static final int[] STATES = {STATUS_NOT_APPROVED, STATUS_TO_APPROVE, STATUS_APPROVED};
}
