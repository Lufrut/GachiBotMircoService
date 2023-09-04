package org.gachi.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface GameService {
    SendMessage play(Update update);

    SendMessage getSlaves(Update update);

    SendMessage stealSlaves(Update update);
}
