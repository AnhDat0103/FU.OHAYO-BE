package vn.fu_ohayo.service.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.fu_ohayo.dto.request.DialogueRequest;
import vn.fu_ohayo.dto.response.DialogueResponse;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.enums.ContentStatus;
import vn.fu_ohayo.enums.ErrorEnum;
import vn.fu_ohayo.exception.AppException;
import vn.fu_ohayo.mapper.DialogueMapper;
import vn.fu_ohayo.repository.DialogueRepository;
import vn.fu_ohayo.service.ContentSpeakingService;
import vn.fu_ohayo.service.DialogueService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DialogueServiceImp implements DialogueService {

    private final DialogueRepository dialogueRepository;
    private final ContentSpeakingService contentSpeakingService;
    private final DialogueMapper dialogueMapper;

    //    @Lazy khiến Spring chỉ khởi tạo ContentSpeakingService khi thật sự cần, tránh lỗi vòng tròn.
    public DialogueServiceImp(DialogueRepository dialogueRepository, @Lazy ContentSpeakingService contentSpeakingService, DialogueMapper dialogueMapper) {
        this.dialogueRepository = dialogueRepository;
        this.contentSpeakingService = contentSpeakingService;
        this.dialogueMapper = dialogueMapper;
    }


    @Override
    public List<Dialogue> getAllDialogues() {
        return dialogueRepository.findAll();
    }

    @Override
    public Dialogue getDialogueById(long id) {
        return dialogueRepository.findById(id).orElse(null);
    }

    @Override
    public Dialogue handleSaveDialogue(DialogueRequest dialogueRequest) {
        Dialogue newDialogue = Dialogue.builder()
                .contentSpeaking(contentSpeakingService.getContentSpeakingById(dialogueRequest.getContentSpeakingId()))
                .answerJp(dialogueRequest.getAnswerJp())
                .answerVn(dialogueRequest.getAnswerVn())
                .questionJp(dialogueRequest.getQuestionJp())
                .questionVn(dialogueRequest.getQuestionVn())
                .build();
        return dialogueRepository.save(newDialogue);
    }

    @Override
    public void deleteDialogueById(long id) {
        dialogueRepository.deleteById(id);
    }

//    @Override
//    public Dialogue updatePutDialogue(Dialogue dialogue, long id) {
//        Dialogue dialogue1 = dialogueRepository.findById(id).orElse(null);
//        if (dialogue1 != null) {
//
//        }
//    }

    @Override
    public Dialogue updatePatchDialogue(long id, Dialogue dialogueRequest) {
        Dialogue dialogue = dialogueRepository.findById(id).orElse(null);
        boolean isUpdated = false;
        if (dialogue != null) {
            if (dialogueRequest.getAnswerJp() != null) {
                dialogue.setAnswerJp(dialogueRequest.getAnswerJp());
                isUpdated = true;
            }
            if (dialogueRequest.getAnswerVn() != null) {
                dialogue.setAnswerVn(dialogueRequest.getAnswerVn());
                isUpdated = true;
            }
            if (dialogueRequest.getQuestionJp() != null) {
                dialogue.setQuestionJp(dialogueRequest.getQuestionJp());
                isUpdated = true;
            }
            if (dialogueRequest.getQuestionVn() != null) {
                dialogue.setQuestionVn(dialogueRequest.getQuestionVn());
                isUpdated = true;
            }
        }
        return dialogueRepository.save(dialogue);
    }

    @Override
    public List<DialogueResponse> getDialoguesByContentSpeakingId(long contentSpeakingId) {
        ContentSpeaking contentSpeaking = contentSpeakingService.getContentSpeakingById(contentSpeakingId);
        return dialogueRepository.findByContentSpeaking(contentSpeaking).stream().map(dialogueMapper::toDialogueResponse).collect(Collectors.toList());
    }

    @Override
    public Page<DialogueResponse> getDialoguePage(int page, int size, long contentSpeakingId) {
        Pageable pageable = PageRequest.of(page - 1, size);
        ContentSpeaking contentSpeaking = contentSpeakingService.getContentSpeakingById(contentSpeakingId);
        Page<Dialogue> dialoguePage = dialogueRepository.findAllByContentSpeaking(contentSpeaking, pageable);
        return dialoguePage.map(dialogueMapper::toDialogueResponse);
    }

    @Override
    public void deleteDialogueByContenSpeaking(ContentSpeaking contentSpeaking) {
        List<Dialogue> dialogues = dialogueRepository.findByContentSpeaking(contentSpeaking);
        dialogueRepository.deleteAll(dialogues);
    }

    @Override
    public Page<DialogueResponse> getAllDialoguePage(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Dialogue> dialoguePage = dialogueRepository.findAll(pageable);
        return dialoguePage.map(dialogueMapper::toDialogueResponse);
    }

}
