package org.entando.entando.plugins.jacms.web.resource.util;

import com.agiletec.aps.system.services.user.UserDetails;

import javax.servlet.http.HttpSession;

public class HttpSessionHelper {

    private static final String USER_KEY = "user";

    public static UserDetails extractCurrentUser(HttpSession httpSession) {
        return (UserDetails) httpSession.getAttribute(USER_KEY);
    }
}
