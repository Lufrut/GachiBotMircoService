package org.gachi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.gachi.enums.ServiceCommand;
import org.gachi.repository.PlayerChatRepository;
import org.gachi.service.GameService;
import org.gachi.service.MainService;
import org.gachi.service.PlayerCommandService;
import org.gachi.service.ProducerService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Log4j2
@Service
public class MainServiceImpl implements MainService {

    private final ProducerService producerService;

    private final PlayerCommandService playerCommandService;

    private final PlayerChatRepository playerChatRepository;

    private final GameService gameService;

    public MainServiceImpl(ProducerService producerService, PlayerCommandService playerCommandService, PlayerChatRepository playerChatRepository, GameService gameService) {
        this.producerService = producerService;
        this.playerCommandService = playerCommandService;
        this.playerChatRepository = playerChatRepository;
        this.gameService = gameService;
    }

    public boolean isPlayerRegistered(Long chatId, Long userId) {
        return playerChatRepository.existsByChat_IdAndPlayer_Id(chatId, userId);
    }

    @Override
    public void commandSorter(Update update) {
        var text = update.getMessage().getText();
        var serviceCommand = ServiceCommand.fromValue(text);
        if (Objects.nonNull(serviceCommand)) {


            if (serviceCommand.equals(ServiceCommand.HELP)) sendAnswer(playerCommandService.help(update));
            else if (serviceCommand.equals(ServiceCommand.REGISTRATION))
                sendAnswer(playerCommandService.register(update));
            else if (isPlayerRegistered(
                    update.getMessage().getChatId(),
                    update.getMessage().getFrom().getId()
            )) {
                switch (serviceCommand) {
                    case PLAY -> sendAnswer(gameService.play(update));
                    case GET_SLAVES -> sendAnswer(gameService.getSlaves(update));
                }
            } else {
                sendAnswer(SendMessage.builder()
                        .chatId(update.getMessage().getChatId())
                        .text("You haven't registered")
                        .build());
            }

        }

    }


    public void sendAnswer(SendMessage sendMessage) {
        producerService.producerAnswer(sendMessage);
    }
}
