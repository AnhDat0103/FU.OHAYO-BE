package vn.fu_ohayo.service;

import org.springframework.data.domain.Page;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;

import java.util.List;

public interface GrammarService {

    List<GrammarResponse> getAllGrammars();
    GrammarResponse getGrammarById(String id);
    GrammarResponse saveGrammar(GrammarRequest grammarRequest);
    GrammarResponse updateGrammar(String id, GrammarRequest grammarRequest);
    GrammarResponse patchGrammar(String id, GrammarRequest grammarRequest);
    void deleteGrammarById(String id);
    Page<GrammarResponse> getGrammarPage(int page, int size);
    Page<GrammarResponse> getAllGrammars(int lessonId, int page, int size);
}
