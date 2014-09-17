package com.agiletec.plugins.jpmail.mock.messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.subethamail.smtp.MessageContext;
import org.subethamail.smtp.MessageHandler;
import org.subethamail.smtp.RejectException;

public class MockMessageHandler implements MessageHandler {
	


	public MockMessageHandler(MessageContext ctx) {
		this._ctx = ctx;
		if (null == _messages) {
			_messages = new ArrayList<MockMailMessage>();
		}
	}

	public void from(String from) throws RejectException {
		this.setFrom(from);
		System.out.println("MockMessageHandler - FROM:"+from);
	}

	public void recipient(String recipient) throws RejectException {
		this.setTo(recipient);
		System.out.println("MockMessageHandler - RECIPIENT:"+recipient);
	}

	public void data(InputStream data) throws IOException {
		String dataStr = this.convertStreamToString(data);
		this.setData(dataStr);
		System.out.println("MockMessageHandler - MAIL DATA");
		System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
		System.out.println(dataStr);
		System.out.println("= = = = = = = = = = = = = = = = = = = = = = = = = = = = = = =");
		MockMailMessage message = new MockMailMessage(this.getFrom(), this.getTo(), this._data);
		this.getMessages().add(message);
		System.out.println(" MockMessageHandler - size " + this.getMessages().size());
	}

	public void done() {
		System.out.println("MockMessageHandler - Finished");
	}

	public String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	public String getFrom() {
		return _from;
	}
	public void setFrom(String from) {
		this._from = from;
	}

	public String getTo() {
		return _to;
	}
	public void setTo(String to) {
		this._to = to;
	}

	public String getData() {
		return _data;
	}
	public void setData(String data) {
		this._data = data;
	}

	public List<MockMailMessage> getMessages() {
		return _messages;
	}
	private void setMessages(List<MockMailMessage> messages) {
		this._messages = messages;
	}

	private String _from;
	private String _to;
	private String _data;
	private MessageContext _ctx;
	private List<MockMailMessage> _messages;

}
