package org.gachi.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface PlayerCommandService {
    SendMessage register(Update update);

    SendMessage help(Update update);

    SendMessage delete(Update update);
}
