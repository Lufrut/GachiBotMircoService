package org.gachi.repository;

import org.gachi.model.PlayerChat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerChatRepository extends JpaRepository<PlayerChat,Long> {
    PlayerChat findPlayerChatByChat_IdAndPlayer_Id(Long chatId, Long playerId);

    Boolean existsByChat_IdAndPlayer_Id(Long chatId, Long playerId);
}
