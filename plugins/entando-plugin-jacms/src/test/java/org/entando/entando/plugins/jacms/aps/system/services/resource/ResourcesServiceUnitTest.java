package org.entando.entando.plugins.jacms.aps.system.services.resource;

import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import org.entando.entando.plugins.jacms.aps.system.services.util.TestHelper;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.validation.FieldError;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
public class ResourcesServiceUnitTest {

    @Autowired
    private MessageSource messageSource;

    private IAuthorizationManager authorizationManager;
    private ResourcesService resourcesService;

    @Before
    public void setup() {
        authorizationManager = mock(IAuthorizationManager.class);
        resourcesService = new ResourcesService();

        when(authorizationManager.getGroupsByPermission(any(), matches(ResourcesService.PERMISSION_MANAGE_RESOURCES)))
                .thenReturn(TestHelper.createGroups());

        resourcesService.setAuthorizationManager(authorizationManager);
        resourcesService.setFileAllowedExtensions(Arrays.stream(new String[] { "pdf","txt" }).collect(Collectors.toList()));
        resourcesService.setImageAllowedExtensions(Arrays.stream(new String[] { "jpeg","png" }).collect(Collectors.toList()));
    }

    @Test(expected = ValidationGenericException.class)
    public void testInvalidGroup() {
        resourcesService.validateGroup(null, "invalidGroup");
    }

    @Test
    public void testGroupValidation() {
        resourcesService.validateGroup(null, "free");
        resourcesService.validateGroup(null, "admin");
    }

    @Test
    public void testMimeTypeValidation() {
        resourcesService.validateMimeType("Image", "application/jpeg");
        resourcesService.validateMimeType("Image", "application/png");
        resourcesService.validateMimeType("Attach", "application/pdf");
        resourcesService.validateMimeType("Attach", "application/txt");
    }

    @Test(expected = ValidationGenericException.class)
    public void testInvalidImageMimeType() {
        resourcesService.validateMimeType("Image", "application/pdf");
    }

    @Test(expected = ValidationGenericException.class)
    public void testInvalidAttachMimeType() {
        resourcesService.validateMimeType("Attach", "application/jpeg");
    }

    @Test(expected = ValidationGenericException.class)
    public void testInvalidResourceType() {
        resourcesService.validateMimeType("image", "application/jpeg");
    }

    private String resolveLocalizedMessage(String code, Object... args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, args, currentLocale);
    }

}
