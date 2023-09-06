package org.gachi.service.impl;

import lombok.extern.log4j.Log4j2;
import org.gachi.configuration.Locales;
import org.gachi.enums.CooldownNames;
import org.gachi.exception.CooldownNotExistException;
import org.gachi.exception.InventoryNotExistException;
import org.gachi.model.Cooldown;
import org.gachi.model.Inventory;
import org.gachi.model.PlayerChat;
import org.gachi.repository.CooldownRepository;
import org.gachi.repository.InventoryRepository;
import org.gachi.repository.PlayerChatRepository;
import org.gachi.service.GameService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.SplittableRandom;

@Log4j2
@Service
public class GameServiceImpl implements GameService {


    private final PlayerChatRepository playerChatRepository;

    private final InventoryRepository inventoryRepository;

    private final CooldownRepository cooldownRepository;

    public GameServiceImpl(PlayerChatRepository playerChatRepository, InventoryRepository inventoryRepository, CooldownRepository cooldownRepository) {
        this.playerChatRepository = playerChatRepository;
        this.inventoryRepository = inventoryRepository;
        this.cooldownRepository = cooldownRepository;
    }

    // here we use -x+36 function to get "weight" for each case,
    private int getCumWithChance() {
        SplittableRandom random = new SplittableRandom();
        int randomNumber = random.nextInt(214);
        int counter = 0;
        for (int i = 0; i <= 10; i++) {
            counter += (-1 * (Math.pow(i - 5.0, 2)) + 36);
            if (randomNumber < counter) return i;
        }
        return 0;
    }

    private int getSlavesWithChance() {
        SplittableRandom random = new SplittableRandom();
        int randomNumber = random.nextInt(9);
        if (randomNumber < 2) {
            return 0;
        } else if (randomNumber < 5) {
            return 1;
        } else if (randomNumber < 8) {
            return 2;
        } else return 3;
    }

    private Boolean isCooldownPass(LocalDateTime time, int cooldownInMinutes) {
        return Objects.isNull(time) || cooldownInMinutes <= Duration.between(time, LocalDateTime.now()).toMinutes();
    }

    //I think better make additional proxy method to put there Boolean true and retrieve cooldown
    private Boolean isCooldownPassProxy(PlayerChat playerChat, int cooldownInMinutes) {
        return Boolean.TRUE.equals(
                isCooldownPass(playerChat.getCooldown().getDrinkCumCooldown(),
                        cooldownInMinutes
                ));
    }

    private int timeRest(LocalDateTime time, int cooldownInMinutes) {
        return cooldownInMinutes - (int) Duration.between(time, LocalDateTime.now()).toMinutes();
    }

    private int drinkCum(PlayerChat playerChat) {
        Inventory inventory;
        try {
            inventory = inventoryRepository.findById(playerChat.getInventory().getId())
                    .orElseThrow(InventoryNotExistException::new);
        } catch (InventoryNotExistException ex) {
            log.error(ex);
            return 0;
        }
        int cumDrunk = (inventory.getSlaves() + 1) * getCumWithChance();
        inventory.setCumAmount(
                inventory.getCumAmount()
                        + cumDrunk
        );
        inventoryRepository.save(inventory);

        return cumDrunk;
    }

    void updateCooldown(PlayerChat playerChat, CooldownNames cooldownNames) {
        try {
            Cooldown cooldown = cooldownRepository.findById(playerChat.getCooldown().getId())
                    .orElseThrow(CooldownNotExistException::new);
            switch (cooldownNames) {
                case DRINK_CUM_COOLDOWN -> cooldown.setDrinkCumCooldown(LocalDateTime.now());
                case GET_SLAVE_COOLDOWN -> cooldown.setGetSlaveCooldown(LocalDateTime.now());
                case STEAL_SLAVE__COOLDOWN -> cooldown.setStealSlaveCooldown(LocalDateTime.now());
            }
            cooldownRepository.save(cooldown);
        } catch (CooldownNotExistException ex) {
            log.error(ex);
        }

    }

    private PlayerChat getPlayerChatFromUpdate(Update update) {
        return playerChatRepository.findPlayerChatByChat_IdAndPlayer_Id(
                update.getMessage().getChatId(),
                update.getMessage().getFrom().getId()
        );
    }

    @Override
    public SendMessage play(Update update) {
        PlayerChat playerChat = getPlayerChatFromUpdate(update);
        if (
                isCooldownPassProxy(
                        playerChat,
                        CooldownNames.DRINK_CUM_COOLDOWN.getCooldownInMinutes()
                )
        ) {

            int cumDrunk = drinkCum(playerChat);
            updateCooldown(playerChat, CooldownNames.DRINK_CUM_COOLDOWN);
            return SendMessage.builder()
                    .chatId(playerChat.getChat().getId())
                    .text(Locales.drunkCumSuccessfully(playerChat.getChat().getLanguage()) + cumDrunk)
                    .build();
        } else {
            int time = timeRest(
                    playerChat.getCooldown().getDrinkCumCooldown(),
                    CooldownNames.DRINK_CUM_COOLDOWN.getCooldownInMinutes()
            );
            return SendMessage.builder()
                    .chatId(playerChat.getChat().getId())
                    .text(Locales.drunkCumCooldown(playerChat.getChat().getLanguage())
                            + time
                            + Locales.minutes(playerChat.getChat().getLanguage())
                    )
                    .build();
        }
    }

    private int findSlaves(PlayerChat playerChat) {
        Inventory inventory;
        try {
            inventory = inventoryRepository.findById(playerChat.getInventory().getId())
                    .orElseThrow(InventoryNotExistException::new);
        } catch (InventoryNotExistException ex) {
            log.error(ex);
            return 0;
        }
        int gotSlaves = getSlavesWithChance();
        inventory.setSlaves(
                inventory.getSlaves()
                        + gotSlaves
        );
        inventoryRepository.save(inventory);

        return gotSlaves;
    }

    @Override
    public SendMessage getSlaves(Update update) {
        PlayerChat playerChat = getPlayerChatFromUpdate(update);
        if (
                isCooldownPassProxy(
                        playerChat,
                        CooldownNames.GET_SLAVE_COOLDOWN.getCooldownInMinutes()
                )
        ) {
            int gotSlaves = findSlaves(playerChat);
            updateCooldown(playerChat, CooldownNames.GET_SLAVE_COOLDOWN);


            return SendMessage.builder()
                    .chatId(playerChat.getChat().getId())
                    .text(Locales.gotSlavesSuccessfully(playerChat.getChat().getLanguage()) + gotSlaves)
                    .build();
        } else {
            int time = timeRest(
                    playerChat.getCooldown().getGetSlaveCooldown(),
                    CooldownNames.GET_SLAVE_COOLDOWN.getCooldownInMinutes()
            );
            return SendMessage.builder()
                    .chatId(playerChat.getChat().getId())
                    .text(Locales.gotSlavesCooldown(playerChat.getChat().getLanguage())
                            + time
                            + Locales.minutes(playerChat.getChat().getLanguage())
                    )
                    .build();
        }
    }

    @Override
    public SendMessage stealSlaves(Update update) {
        return null;
        //TODO for next updates
    }
}
