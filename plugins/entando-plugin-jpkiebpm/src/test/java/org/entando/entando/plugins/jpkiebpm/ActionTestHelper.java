package org.entando.entando.plugins.jpkiebpm;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.user.User;
import com.agiletec.apsadmin.system.BaseAction;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * This helper works if the action has both the InjectMocks and the Spy
 * annotations.
 */
public class ActionTestHelper {

    public static void initActionMocks(BaseAction action) {
        new ActionTestHelper(action).initMocks();
    }

    private final BaseAction action;

    private ActionTestHelper(BaseAction action) {
        this.action = action;
    }

    private void initMocks() {
        mockActionErrors();
        mockGetText();
        mockAuthorizationManager();
        mockCurrentUser();
    }

    private void mockActionErrors() {
        List<String> actionErrors = new ArrayList<>();
        doReturn(actionErrors).when(action).getActionErrors();
        doAnswer(fakeAddMessage(actionErrors))
                .when(action).addActionError(anyString());
    }

    private static <T> Answer fakeAddMessage(List<T> messages) {
        return invocation -> {
            T msg = (T) invocation.getArguments()[0];
            messages.add(msg);
            return null;
        };
    }

    private void mockGetText() {
        doAnswer(invocation -> invocation.getArguments()[0])
                .when(action).getText(anyString());
    }

    private void mockAuthorizationManager() {
        IAuthorizationManager authManager = mock(IAuthorizationManager.class);
        when(authManager.isAuthOnGroup(any(), any())).thenReturn(true);
        action.setAuthorizationManager(authManager);
    }

    private void mockCurrentUser() {

        User currentUser = mock(User.class);

        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute(SystemConstants.SESSIONPARAM_CURRENT_USER))
                .thenReturn(currentUser);

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        when(httpRequest.getSession()).thenReturn(session);

        action.setServletRequest(httpRequest);
    }
}
