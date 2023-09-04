package org.gachi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.gachi.controller.UpdateController;
import org.gachi.service.AnswerConsumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static org.gachi.model.RabbitQueue.ANSWER_MESSAGE;

@Log4j2
@Service
public class AnswerConsumerImpl implements AnswerConsumer {
    private final UpdateController updateController;

    public AnswerConsumerImpl(UpdateController updateController) {
        this.updateController = updateController;
    }

    @Override
    @RabbitListener(queues = ANSWER_MESSAGE)
    public void consume(SendMessage sendMessage) {
        //sonarlint write that it's always true but actually not, sometimes it can be null
        if(sendMessage.getChatId() != null) {
            updateController.setView(sendMessage);
        }
    }
}
