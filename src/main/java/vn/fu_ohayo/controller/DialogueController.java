package vn.fu_ohayo.controller;

import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.DialogueRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.entity.Dialogue;
import vn.fu_ohayo.service.DialogueService;

import java.util.List;

@RestController
@RequestMapping("/dialogue")
public class DialogueController {
    DialogueService dialogueService;
    public DialogueController(DialogueService dialogueService) {
        this.dialogueService = dialogueService;
    }
    @GetMapping()
    public ApiResponse<List<Dialogue>> getDialogues() {
        return ApiResponse.<List<Dialogue>>builder()
                .code("200")
                .status("success")
                .message("Get all dialogues successfully")
                .data(dialogueService.getAllDialogues())
                .build();
    }

    @PostMapping()
    public ApiResponse<Dialogue> createDialogue( @RequestBody DialogueRequest dialogueRequest) {
        Dialogue newDialogue = dialogueService.handleSaveDialogue(dialogueRequest);
        return ApiResponse.<Dialogue>builder()
                .code("201")
                .status("success")
                .message("Created new dialogue successfully")
                .data(newDialogue)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Dialogue> getDialogueById(@PathVariable int id) {
        return ApiResponse.<Dialogue>builder()
                .code("200")
                .status("success")
                .message("Get dialogue by id successfully")
                .data(dialogueService.getDialogueById(id))
                .build();
    }

    @GetMapping("/content_speaking/{contentSpeakingId}")
    public ApiResponse<List<Dialogue>> getDialoguesByContentSpeakingId(@PathVariable long contentSpeakingId) {
        List<Dialogue> dialogues = dialogueService.getDialoguesByContentSpeakingId(contentSpeakingId);
        return ApiResponse.<List<Dialogue>>builder()
                .code("200")
                .status("success")
                .message("Get dialogues by content speaking id successfully")
                .data(dialogues)
                .build();
    }

    @PatchMapping({"/{id}"})
    public ApiResponse<Dialogue> updateDialogue(@PathVariable int id, @RequestBody Dialogue dialogue) {
        Dialogue updatedDialogue = dialogueService.updatePatchDialogue(id, dialogue);
        return ApiResponse.<Dialogue>builder()
                .code("200")
                .status("success")
                .message("Updated dialogue successfully")
                .data(updatedDialogue)
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Dialogue> deleteDialogue(@PathVariable int id) {
        dialogueService.deleteDialogueById(id);
        return ApiResponse.<Dialogue>builder()
                .code("200")
                .status("success")
                .message("Deleted dialogue successfully")
                .build();
    }
}
