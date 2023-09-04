package org.gachi.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface ConsumerService {
    void consumerTextMessageUpdates(Update update);
}
