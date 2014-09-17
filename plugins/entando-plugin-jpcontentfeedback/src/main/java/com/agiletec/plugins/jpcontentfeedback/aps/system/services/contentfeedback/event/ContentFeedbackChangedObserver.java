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
package com.agiletec.plugins.jpcontentfeedback.aps.system.services.contentfeedback.event;

import com.agiletec.aps.system.common.notify.ObserverService;

/**
 * @author E.Santoboni
 */
public interface ContentFeedbackChangedObserver extends ObserverService {
    
    public void updateFromContentFeedbackChanged(ContentFeedbackChangedEvent event);
    
}
