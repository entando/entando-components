package org.entando.entando.web.digitalexchange.job;

import com.agiletec.aps.system.services.role.Permission;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;
import org.entando.entando.web.common.annotation.RestAccessControl;
import org.entando.entando.web.common.model.PagedRestResponse;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.common.model.SimpleRestResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(tags = {"digital-exchange", "job"})
@RequestMapping(value = "/digitalExchange/jobs")
public interface DigitalExchangeJobResource {

    @ApiOperation(value = "Get all available jobs")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PagedRestResponse<DigitalExchangeJob>> list(RestListRequest restListRequest);

    @ApiOperation(value = "Get a Digital Exchange job by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "Not FOund")
    })
    @RestAccessControl(permission = Permission.SUPERUSER)
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SimpleRestResponse<DigitalExchangeJob>> getById(String jobId);
}
