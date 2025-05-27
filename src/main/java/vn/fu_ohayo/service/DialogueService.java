package vn.fu_ohayo.service;

import vn.fu_ohayo.entity.Dialogue;

import java.util.List;

public interface DialogueService {
    List<Dialogue> getAllDialogues();
    Dialogue getDialogueById(long id);
    Dialogue handleSaveDialogue(Dialogue dialogue);
    void deleteDialogueById(long id);
//    Dialogue updatePutDialogue(Dialogue dialogue, long id);
    Dialogue updatePatchDialogue(long id, Dialogue dialogue);
    List<Dialogue> getDialoguesByContentSpeakingId(long contentSpeakingId);
}
