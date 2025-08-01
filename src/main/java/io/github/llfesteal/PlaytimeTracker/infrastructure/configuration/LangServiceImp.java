package io.github.llfesteal.PlaytimeTracker.infrastructure.configuration;

import fr.lifesteal.pluginframework.api.config.ConfigRepository;
import fr.lifesteal.pluginframework.core.config.Colorized;
import fr.lifesteal.pluginframework.core.config.ConfigParam;
import fr.lifesteal.pluginframework.core.config.ConfigServiceBase;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LangServiceImp extends ConfigServiceBase implements LangService {

    @Colorized
    @ConfigParam(paramKey = "error.only-players-allowed")
    private String errorOnlyPlayersAllowedMessage = "&cOnly players can do that!";

    @Colorized
    @ConfigParam(paramKey = "error.session.no-session-alive")
    private String errorNoSessionAlive = "&cThere is no session alive ! If you think this is a bug, please contact your administrator.";

    @Colorized
    @ConfigParam(paramKey = "message.playtime")
    private List<String> playtimeMessages = new ArrayList<>() {{
        add("&aYour current playtime for this session is : &e%current_playtime%");
        add("&aYour total playtime is : &e%total_playtime%");
    }};

    @Colorized
    @ConfigParam(paramKey = "error.player-not-found")
    private String errorPlayerNotFound = "&cPlayer %player% not found !";

    @Colorized
    @ConfigParam(paramKey = "message.lookup")
    private String lookupMessage = "%player% &ahas played &e%playtime% &abetween &b%start_date% &aand &b%end_date% &a!";

    public LangServiceImp(Logger logger, ConfigRepository configRepository) {
        super(logger, configRepository);
    }

    @Override
    public String getErrorOnlyPlayersAllowedMessage() {
        return errorOnlyPlayersAllowedMessage;
    }

    @Override
    public String getErrorNoSessionAlive() {
        return errorNoSessionAlive;
    }

    @Override
    public List<String> getPlaytimeMessages(String formattedCurrentPlaytime, String formattedTotalPlaytime) {
        return playtimeMessages.stream()
                .map(line -> line
                        .replaceAll("%current_playtime%", formattedCurrentPlaytime)
                        .replaceAll("%total_playtime%", formattedTotalPlaytime))
                .collect(Collectors.toList());
    }

    @Override
    public String getPlaytimeLookupMessage(String playerName, String duration, String startDate, String endDate) {
        return lookupMessage
                .replaceAll("%player%", playerName)
                .replaceAll("%playtime%", duration)
                .replaceAll("%start_date%", startDate)
                .replaceAll("%end_date%", endDate);
    }

    @Override
    public String getErrorPlayerNotFound(String playerIdentifier) {
        return errorPlayerNotFound.replaceAll("%player%", playerIdentifier);
    }
}
