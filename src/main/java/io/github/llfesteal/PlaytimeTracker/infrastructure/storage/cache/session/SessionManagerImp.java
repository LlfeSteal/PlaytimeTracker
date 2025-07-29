package io.github.llfesteal.PlaytimeTracker.infrastructure.storage.cache.session;

import io.github.llfesteal.PlaytimeTracker.domain.model.Session;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManagerImp implements SessionManager {

    private final Map<UUID, Session> playerSession = new ConcurrentHashMap<>();

    @Override
    public void add(Session session) {
        playerSession.put(session.getPlayerId(), session);
    }

    @Override
    public Session getByPlayerId(UUID playerId) {
        return this.playerSession.get(playerId);
    }

    @Override
    public Session remove(UUID playerId) {
        return this.playerSession.remove(playerId);
    }
}
