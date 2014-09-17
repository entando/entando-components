/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
*/
package com.agiletec.plugins.jpcontentfeedback;

import junit.framework.Test;
import junit.framework.TestSuite;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.TestCommentFrontEndAction;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.comment.TestCommentManager;
import com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.rating.TestRatingManager;
import com.agiletec.plugins.jpcontentfeedback.apsadmin.comment.TestCommentAction;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for jAPS jpcontentfeedback Plugin");
		
		// FrontEnd
		suite.addTestSuite(TestCommentFrontEndAction.class);
		suite.addTestSuite(TestCommentManager.class);
		suite.addTestSuite(TestRatingManager.class);

		//BackEnd
		suite.addTestSuite(TestCommentAction.class);
		
		return suite;
	}

}