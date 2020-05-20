package org.entando.entando.plugins.jacms.aps.system.services.mockhelper;

import com.agiletec.plugins.jacms.aps.system.services.content.model.Content;
import com.agiletec.plugins.jacms.aps.system.services.content.model.ContentDto;
import org.entando.entando.aps.system.services.page.IPageService;
import org.entando.entando.web.common.assembler.PagedMetadataMapper;
import org.entando.entando.web.common.model.PagedMetadata;
import org.entando.entando.web.common.model.RestListRequest;
import org.entando.entando.web.component.ComponentUsageEntity;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ContentMockHelper {

    public static final String CONTENT_ID = "contId";
    public static final String CONTENT_STATUS = Content.STATUS_READY;
    public static final String CONTENT_TYPE_CODE = "type_code";

    /**
     *
     * @param restListRequest
     * @param count
     * @return
     */
    public static PagedMetadata<ContentDto> mockPagedContentDto(RestListRequest restListRequest, int count) {

        List<ContentDto> contentDtoList = IntStream.range(0, count)
                .mapToObj(ContentMockHelper::mockContentDto)
                .collect(Collectors.toList());

        return new PagedMetadata<>(restListRequest, contentDtoList, count);
    }


    /**
     *
     * @param i
     * @return
     */
    public static ContentDto mockContentDto(int i) {

        ContentDto contentDto = new ContentDto();
        contentDto.setId(CONTENT_ID + i);
        contentDto.setStatus(CONTENT_STATUS);
        contentDto.setTypeCode(CONTENT_TYPE_CODE);

        return contentDto;
    }


    /**
     *
     * @param restListRequest
     * @param count
     * @return
     */
    public static PagedMetadata mockPagedContentComponentUsageEntity(RestListRequest restListRequest, int count) {

        List<ComponentUsageEntity> componentUsageEntityList = IntStream.range(0, count)
                .mapToObj(ContentMockHelper::mockComponentUsageEntity)
                .collect(Collectors.toList());

        return new PagedMetadata(restListRequest, componentUsageEntityList, count);
    }


    /**
     *
     * @param i
     * @return
     */
    public static ComponentUsageEntity mockComponentUsageEntity(int i) {

        ComponentUsageEntity componentUsageEntity = new ComponentUsageEntity();
        componentUsageEntity.setCode(CONTENT_ID + i);
        componentUsageEntity.setStatus(IPageService.STATUS_ONLINE);
        componentUsageEntity.setType(ComponentUsageEntity.TYPE_CONTENT);

        return componentUsageEntity;
    }


}