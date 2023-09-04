package org.gachi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.gachi.service.ConsumerService;
import org.gachi.service.MainService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.gachi.model.RabbitQueue.TEXT_MESSAGE_UPDATE;

@Service
@Log4j2
public class ConsumerServiceImpl implements ConsumerService {
    private final MainService mainService;

    public ConsumerServiceImpl(MainService mainService) {
        this.mainService = mainService;
    }


    @Override
    @RabbitListener(queues = TEXT_MESSAGE_UPDATE)
    public void consumerTextMessageUpdates(Update update) {
        mainService.commandSorter(update);
    }
}
