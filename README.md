# PlaytimeTracker
A simple tool to track your playtime in games.

## Features
- Track playtime for multiple games
- View total player playtime
- Filter playtime by date period

## Configuration 
### config.yml
The ``config.yml`` file allows you to configure the plugin settings. Below is an example configuration:

```yaml
database:
  type: sqlite
  prefix: PlaytimeTracker_
  mysql:
    ==: MySQLConfig
    databaseUser: root
    databaseHost: localhost
    databaseName: test
    databasePassword: ''
    databasePort: 3306
  sqlite:
    fileName: playtime.db
autosave-delay: 5000
duration-format: '%d days, %02d hours, %02d minutes, %02d seconds'
```

The ``lang.yml`` file allows you to configure messages used by the plugin. Below is an example configuration:
```yaml
error:
  only-players-allowed: '&cOnly players can do that!'
  session:
    no-session-alive: '&cThere is no session alive ! If you think this is a bug, please
      contact your administrator.'
  player-not-found: '&cPlayer %player% not found !'
message:
  playtime:
  - '&aYour current playtime for this session is : &e%current_playtime%'
  - '&aYour total playtime is : &e%total_playtime%'
  lookup: '%player% &ahas played &e%playtime% &abetween &b%start_date% &aand &b%end_date%
    &a!'
```

## Commands
- ``/playtime`` - Displays the total playtime for all games.
- ``/playtime lookup [playerUUID/playerName] <start_date> <end_date>`` - Filters playtime by date range, if not date is provided, it will show the total playtime for the player.

## Placeholders
Placeholders can be used to display playtime information in other plugins or systems using [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI). 
The following placeholders are available:

- `%TotalPlaytime%` - Displays the total player playtime.
- `%TodayPlaytime%` - Displays the player platime for the current day.
- `%CurrentPlaytime%` - Displays the current session playtime.

## Permissions
- playtime.playtime - Allows players to use the `/playtime` command.
- playtime.lookup - Allows players to use the `/playtime lookup` command.

## API
Documentation coming soon.

```java
public interface PlaytimeTrackerAPI {
    void startNewSession(UUID playerId) throws SessionAlreadyActiveException;

    void endSession(UUID playerId) throws NoSessionFoundException;

    Duration getPlayerPlaytime(UUID playerId, LocalDateTime startDate, LocalDateTime endDate);
}
```
