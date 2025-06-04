package vn.fu_ohayo.mapper;

import org.mapstruct.Mapper;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.entity.Grammar;

@Mapper(componentModel = "spring", uses = LessonMapper.class)
public interface GrammarMapper {

    Grammar toGrammar(GrammarRequest grammarRequest);
    GrammarResponse toGrammarResponse(Grammar grammar);
}
