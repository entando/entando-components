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

import com.agiletec.aps.system.common.IManager;
import com.agiletec.aps.system.common.notify.ApsEvent;

/**
 * @author E.Santoboni
 */
public class ContentFeedbackChangedEvent extends ApsEvent {
    
    public void notify(IManager srv) {
        ((ContentFeedbackChangedObserver) srv).updateFromContentFeedbackChanged(this);
    }
    
    public Class getObserverInterface() {
        return ContentFeedbackChangedObserver.class;
    }
    
    public int getCommentId() {
        return _commentId;
    }
    public void setCommentId(int commentId) {
        this._commentId = commentId;
    }
    
    public String getContentId() {
        return _contentId;
    }
    public void setContentId(String contentId) {
        this._contentId = contentId;
    }
    
    public int getOperationCode() {
        return _operationCode;
    }
    public void setOperationCode(int operationCode) {
        this._operationCode = operationCode;
    }
    
    public int getObjectCode() {
        return _objectCode;
    }
    public void setObjectCode(int objectCode) {
        this._objectCode = objectCode;
    }
    
    private String _contentId;
    private int _commentId;
    private int _operationCode;
    private int _objectCode;
    
    public static final int CONTENT_COMMENT = 1;
    public static final int CONTENT_RATING = 4;
    public static final int COMMENT_RATING = 7;
    
    public static final int INSERT_OPERATION_CODE = 10;
    public static final int REMOVE_OPERATION_CODE = 14;
    public static final int UPDATE_OPERATION_CODE = 17;
    
}