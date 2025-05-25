package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import vn.fu_ohayo.dto.request.ContentSpeakingRequest;
import vn.fu_ohayo.dto.response.ContentSpeakingResponse;
import vn.fu_ohayo.entity.ContentSpeaking;

@Mapper(componentModel = "spring")
public interface ContentMapper {
    ContentSpeaking contentSpeakingRequestToContentSpeaking(@MappingTarget ContentSpeaking contentSpeaking, ContentSpeakingRequest contentSpeakingRequest);
    ContentSpeakingResponse toContentSpeakingResponse(ContentSpeaking contentSpeaking);
}
