package org.entando.entando.plugins.jacms.aps.system.services.resource;

import com.agiletec.aps.system.services.authorization.AuthorizationManager;
import com.agiletec.aps.system.services.authorization.IAuthorizationManager;
import com.agiletec.aps.system.services.group.Group;
import org.entando.entando.plugins.jacms.aps.system.init.portdb.Resource;
import org.entando.entando.plugins.jacms.aps.system.services.util.TestHelper;
import org.entando.entando.web.common.exceptions.ValidationGenericException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ResourcesServiceUniTest {

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

}
