package io.github.llfesteal.PlaytimeTracker.application.task;

import io.github.llfesteal.PlaytimeTracker.domain.driven.SessionService;

import java.util.logging.Logger;

public class BackupSessionsTask implements Runnable {

    private final Logger logger;
    private final SessionService sessionService;

    public BackupSessionsTask(Logger logger, SessionService sessionService) {
        this.logger = logger;
        this.sessionService = sessionService;
    }

    @Override
    public void run() {
        this.logger.info("Backup session started...");

        try {
            this.sessionService.forceSaveSessions();
            this.logger.info("Backup session success !");
        } catch (Exception ex) {
            this.logger.severe("Error while saving sessions." + ex.getMessage());
        }
    }
}
