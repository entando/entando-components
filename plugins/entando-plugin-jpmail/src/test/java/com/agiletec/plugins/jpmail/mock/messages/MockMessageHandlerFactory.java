package com.agiletec.plugins.jpmail.mock.messages;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.MessageHandlerFactory;

public class MockMessageHandlerFactory implements MessageHandlerFactory {

    public MessageHandler create(MessageContext ctx) {
    	if (this._messageHandler == null) {
    		this._messageHandler = new MockMessageHandler(ctx);
    	}
        return _messageHandler;
    }

    public MockMessageHandler getMessageHandler() {
		return _messageHandler;
	}
	public void setMessageHandler(MockMessageHandler messageHandler) {
		this._messageHandler = messageHandler;
	}

	private MockMessageHandler _messageHandler;

}