package vn.fu_ohayo.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.GrammarRequest;
import vn.fu_ohayo.dto.response.GrammarResponse;
import vn.fu_ohayo.service.GrammarService;

import java.util.List;

@Service
public class GrammarServiceImp implements GrammarService {

    private final ContentServiceImp contentServiceImp;
    public GrammarServiceImp(ContentServiceImp contentServiceImp) {
        this.contentServiceImp = contentServiceImp;
    }
    @Override
    public List<GrammarResponse> getAllGrammars() {
        return null;
    }

    @Override
    public GrammarResponse getGrammarById(String id) {
        return null;
    }

    @Override
    public GrammarResponse saveGrammar(int lessonId, GrammarRequest grammarRequest) {
        return null;
    }

    @Override
    public GrammarResponse updateGrammar(String id, GrammarRequest grammarRequest) {
        return null;
    }

    @Override
    public GrammarResponse patchGrammar(String id, GrammarRequest grammarRequest) {
        return null;
    }

    @Override
    public void deleteGrammarById(String id) {

    }

    @Override
    public Page<GrammarResponse> getGrammarPage(int page, int size) {
        return null;
    }
}
