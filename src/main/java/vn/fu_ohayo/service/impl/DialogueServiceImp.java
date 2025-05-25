package vn.fu_ohayo.service.impl;

import org.springframework.stereotype.Service;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.repository.DialogueRepository;
import vn.fu_ohayo.service.DialogueService;

import java.util.List;

@Service
public class DialogueServiceImp implements DialogueService {

    private final DialogueRepository dialogueRepository;

    public DialogueServiceImp(DialogueRepository dialogueRepository) {
        this.dialogueRepository = dialogueRepository;
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
}
