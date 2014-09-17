package com.agiletec.plugins.jpmail.mock.messages;

public class MockMailMessage {
	
	public MockMailMessage(String from, String to, String data) {
		this._from = from;
		this._to = to;
		this._data = data;
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
	
	private String _from;
	private String _to;
	private String _data;
	
}
