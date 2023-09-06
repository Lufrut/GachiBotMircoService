package org.gachi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {
    private final UpdateController updateController;

    public WebHookController(UpdateController updateController) {
        this.updateController = updateController;
    }

    @PostMapping(value = "/callback/update")
    public ResponseEntity<?> onUpdateReceived(@RequestBody Update update) {
        updateController.processUpdate(update);

        return ResponseEntity.ok().build();
    }
}