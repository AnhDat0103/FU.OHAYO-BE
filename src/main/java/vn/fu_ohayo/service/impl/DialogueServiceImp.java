package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.ContentSpeaking;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.repository.ContentSpeakingRepository;
import vn.fu_ohayo.repository.DialogueRepository;
import vn.fu_ohayo.service.DialogueService;

import java.util.List;

@Service
public class DialogueServiceImp implements DialogueService {

    private final DialogueRepository dialogueRepository;
    private final ContentSpeakingRepository contentSpeakingRepository;

    public DialogueServiceImp(DialogueRepository dialogueRepository, ContentSpeakingRepository contentSpeakingRepository) {
        this.dialogueRepository = dialogueRepository;
        this.contentSpeakingRepository = contentSpeakingRepository;
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
    public Dialogue handleSaveDialogue(Dialogue dialogue) {
        return dialogueRepository.save(dialogue);
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
        if (dialogue != null) {
            if (dialogueRequest.getAnswerJp() != null) {
                dialogue.setAnswerJp(dialogueRequest.getAnswerJp());
            }
            if (dialogueRequest.getAnswerVn() != null) {
                dialogue.setAnswerVn(dialogueRequest.getAnswerVn());
            }
            if (dialogueRequest.getQuestiontJp() != null) {
                dialogue.setQuestiontJp(dialogueRequest.getQuestiontJp());
            }
            if (dialogueRequest.getQuestiontVn() != null) {
                dialogue.setQuestiontVn(dialogueRequest.getQuestiontVn());
            }
        }
        return dialogueRepository.save(dialogue);
    }

    @Override
    public List<Dialogue> getDialoguesByContentSpeakingId(long contentSpeakingId) {
        ContentSpeaking contentSpeaking = contentSpeakingRepository.findById(contentSpeakingId).orElse(null);
        return dialogueRepository.findByContentSpeaking(contentSpeaking);
    }
}
