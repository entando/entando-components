package com.agiletec.plugins.jpmail.mock.authn;

import java.util.ArrayList;
import java.util.List;

import org.subethamail.smtp.AuthenticationHandler;
import org.subethamail.smtp.AuthenticationHandlerFactory;

public class MockAuthenticationHandlerFactory implements AuthenticationHandlerFactory {

	@Override
	public List<String> getAuthenticationMechanisms() {
		List<String> authMec = new ArrayList<String>();
		authMec.add("PLAIN");
		return authMec;
	}

	@Override
	public AuthenticationHandler create() {
		return new MockAuthenticationHandler();
	}

}
