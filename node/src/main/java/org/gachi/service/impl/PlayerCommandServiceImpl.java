package org.gachi.service.impl;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.log4j.Log4j2;
import org.gachi.configuration.Locales;
import org.gachi.enums.LanguageCodes;
import org.gachi.model.*;
import org.gachi.repository.*;
import org.gachi.service.PlayerCommandService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.time.LocalDateTime;
import java.util.Objects;

@Log4j2
@Service
public class PlayerCommandServiceImpl implements PlayerCommandService {
    private final PlayerChatRepository playerChatRepository;

    private final ChatRepository chatRepository;

    private final PlayerRepository playerRepository;

    private final InventoryRepository inventoryRepository;

    private final CooldownRepository cooldownRepository;

    public PlayerCommandServiceImpl(PlayerChatRepository playerChatRepository,
                                    ChatRepository chatRepository,
                                    PlayerRepository playerRepository,
                                    InventoryRepository inventoryRepository,
                                    CooldownRepository cooldownRepository) {
        this.playerChatRepository = playerChatRepository;
        this.chatRepository = chatRepository;
        this.playerRepository = playerRepository;
        this.inventoryRepository = inventoryRepository;
        this.cooldownRepository = cooldownRepository;
    }

    private Chat getChat(Long chatId) {
        Chat chat;
        if (!chatRepository.existsById(chatId))
            chat = chatRepository.save(Chat.builder()
                    .id(chatId)
                    .language(LanguageCodes.UA)
                    .build());
        else try {
            chat = chatRepository.findById(chatId).orElseThrow(NullPointerException::new);
        } catch (NumberFormatException e) {
            log.error("Return null chat idk why");
            return null;
        }
        return chat;
    }

    private Player getPlayer(User user) {
        Long userId = user.getId();
        Player player;
        if (!playerRepository.existsById(userId))
            player = playerRepository.save(
                    Player.builder()
                            .id(userId)
                            .username(user.getUserName())
                            .firstName(user.getFirstName())
                            .lastName(user.getLastName())
                            .build());
        else try {
            player = playerRepository.findById(userId).orElseThrow(NullPointerException::new);
        } catch (NumberFormatException e) {
            log.error("Return null player idk why");
            return null;
        }
        return player;
    }

    @Override
    public SendMessage register(Update update) {
        if (
                Boolean.FALSE.equals(playerChatRepository.existsByChat_IdAndPlayer_Id(
                        update.getMessage().getChatId(),
                        update.getMessage().getFrom().getId()
                ))){
            Chat chat = getChat(update.getMessage().getChatId());
            Player player = getPlayer(update.getMessage().getFrom());
            Inventory inventory = inventoryRepository.save(
                    Inventory.builder()
                            .cumAmount(0)
                            .slaves(0)
                            .build()
            );
            Cooldown cooldown = cooldownRepository.save(
                    Cooldown.builder()
                            .drinkCumCooldown(null)
                            .stealSlaveCooldown(null)
                            .getSlaveCooldown(null)
                            .build()
            );

            playerChatRepository.save(
                    PlayerChat.builder()
                            .chat(chat)
                            .player(player)
                            .cooldown(cooldown)
                            .inventory(inventory)
                            .build()
            );
            return SendMessage.builder()
                    .chatId(chat.getId())
                    .text(Locales.registerLocale(chat.getLanguage()))
                    .build();
        } else {
            return SendMessage.builder()
                    .chatId(update.getMessage().getChatId())
                    .text(Locales.alreadyRegistered(getChat(update.getMessage().getChatId()).getLanguage()))
                    .build();
        }
    }

    @Override
    public SendMessage help(Update update) {
        return SendMessage.builder()
                .text(Locales.getHelpLocale(LanguageCodes.UA))
                .chatId(update.getMessage().getChatId())
                .build();
    }

    @Override
    public SendMessage delete(Update update) {
        return null;
    }
}
