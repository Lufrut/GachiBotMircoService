package org.gachi.enums;


public enum ServiceCommand {
    HELP("/help"),
    REGISTRATION("/registration"),
    GET_STATS("/getstats"),
    PLAY("/play"),
    GET_SLAVES("/getslaves"),
    TOP_10_CUM("/top10_cum"),
    TOP_10_WORLD_CUM("/top10world_cum"),
    TOP_10_SLAVES("/top10_slaves"),
    TOP_10_WORLD_SLAVES("/top10world_slaves"),
    STEAL_SLAVES("/steal_slaves"),
    USERS_COUNT("/users_count");

    private final String value;

    ServiceCommand(String value) {
        this.value = value;
    }

    public static final String BOT_NICKNAME = "@gachi_starship_bot";

    @Override
    public String toString() {
        return value;
    }

    public static ServiceCommand fromValue(String v) {
        for (ServiceCommand c : ServiceCommand.values()) {
            if (v.matches(c.value) || v.matches(c.value + BOT_NICKNAME)) {
                return c;
            }
        }
        return null;
    }
}
