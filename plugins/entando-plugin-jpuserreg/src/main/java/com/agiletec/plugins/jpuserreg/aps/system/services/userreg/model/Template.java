package com.agiletec.plugins.jpuserreg.aps.system.services.userreg.model;

public class Template implements Cloneable {
	
	@Override
	public Template clone() {
		Template template = new Template();
		template.setSubject(this.getSubject());
		template.setBody(this.getBody());
		return template;
	}
	
	public String getSubject() {
		return _subject;
	}
	public void setSubject(String subject) {
		this._subject = subject;
	}
	
	public String getBody() {
		return _body;
	}
	public void setBody(String body) {
		this._body = body;
	}
	
	private String _subject;
	private String _body;
	
}