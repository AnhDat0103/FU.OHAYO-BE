package vn.fu_ohayo.controller.admin;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import vn.fu_ohayo.dto.request.DialogueRequest;
import vn.fu_ohayo.dto.response.ApiResponse;
import vn.fu_ohayo.dto.response.DialogueResponse;
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

    @GetMapping("/all")
    public ApiResponse<List<Dialogue>> getAllDialogue() {
        return ApiResponse.<List<Dialogue>>builder()
                .code("200")
                .status("success")
                .message("Get all dialogues successfully")
                .data(dialogueService.getAllDialogues())
                .build();
    }

    @PostMapping()
    public ApiResponse<Dialogue> createDialogue(@Valid @RequestBody DialogueRequest dialogueRequest) {
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
    public ApiResponse<Page<Dialogue>> getDialoguesByContentSpeakingIdPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @PathVariable long contentSpeakingId) {
        Page<Dialogue> dialogues = dialogueService.getDialoguePage(page, size, contentSpeakingId);
        return ApiResponse.<Page<Dialogue>>builder()
                .code("200")
                .status("success")
                .message("Get dialogues by content speaking id successfully")
                .data(dialogues)
                .build();
    }

    @GetMapping("/page/all")
    public ApiResponse<Page<Dialogue>> getDialoguesAllPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size){
            Page<Dialogue> dialogues = dialogueService.getAllDialoguePage(page, size);
        return ApiResponse.<Page<Dialogue>>builder()
                .code("200")
                .status("success")
                .message("Get dialogues all page successfully")
                .data(dialogues)
                .build();
    }

    @GetMapping("/content_speakingAll/{contentSpeakingId}")
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

    @PatchMapping("/accept/{id}")
    public ApiResponse<DialogueResponse> acceptDialogue(
            @PathVariable Integer id
    ) {
        DialogueResponse response = dialogueService.acceptDialogue(id);
        return ApiResponse.<DialogueResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/reject/{id}")
    public ApiResponse<DialogueResponse> rejectDialogue(
            @PathVariable Integer id
    ) {
        DialogueResponse response = dialogueService.rejectDialogue(id);
        return ApiResponse.<DialogueResponse>builder()
                .code("200")
                .status("success")
                .message("Accept successfully")
                .data(response)
                .build();
    }

    @PatchMapping("/inactive/{id}")
    public ApiResponse<DialogueResponse> inActiveDialogue(
            @PathVariable Integer id
    ) {
        DialogueResponse response = dialogueService.inActiveDialogue(id);
        return ApiResponse.<DialogueResponse>builder()
                .code("200")
                .status("success")
                .message("Inactive successfully")
                .data(response)
                .build();
    }
}
