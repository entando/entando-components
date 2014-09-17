package com.agiletec.plugins.jpmail.mock.authn;

import org.subethamail.smtp.AuthenticationHandler;
import org.subethamail.smtp.RejectException;

public class MockAuthenticationHandler implements AuthenticationHandler {

	@Override
	public String auth(String paramString) throws RejectException {
		return "";
	}

	@Override
	public Object getIdentity() {
		return "testuser";
	}

}
