package io.github.llfesteal.PlaytimeTracker.application.task;

import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;

public class BackupSessionsTask implements Runnable {

    private final SessionService sessionService;

    public BackupSessionsTask(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public void run() {
        this.sessionService.forceSaveSessions();
    }
}
