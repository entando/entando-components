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