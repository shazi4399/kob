package com.kob.backend.service.pk;

public interface ReceiveBotMoveService {
    String receiveBotMove(Integer userId, Integer direction);

    interface StartGameService {
        String startGame(Integer aId, Integer aBotId, Integer bId, Integer bBotId);
    }
}
