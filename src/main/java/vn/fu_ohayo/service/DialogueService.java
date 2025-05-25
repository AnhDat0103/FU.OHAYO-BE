package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.Dialogue;

import java.util.List;

public interface DialogueService {
    List<Dialogue> getAllDialogues();
    Dialogue getDialogueById(long id);
    Dialogue handleSaveDialogue(Dialogue dialogue);
    void deleteDialogueById(long id);
}
