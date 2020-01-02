package de.arraying.boardbot;

import net.dv8tion.jda.api.OnlineStatus;

/**
 * Copyright 2020 Arraying
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public final class Config {

    private final String token;
    private final String channel;
    private final String url;
    private String guild = "";
    private String mode = "duplicate";
    private String statusType = OnlineStatus.ONLINE.toString();
    private String statusMessage = "Announcement fetching.";
    private int interval = 1;

    /**
     * Loads all the values from environment variables.
     */
    Config() {
        this.token = System.getenv("TOKEN");
        this.channel = System.getenv("CHANNEL");
        this.url = System.getenv("URL");
        String guild = System.getenv("GUILD");
        if(guild != null) {
            this.guild = guild;
        }
        String mode = System.getenv("MODE");
        if(mode != null) {
            this.mode = mode;
        }
        String statusType = System.getenv("STATUS_TYPE");
        if(statusType != null) {
            this.statusType = statusType;
        }
        String statusMessage = System.getenv("STATUS_MESSAGE");
        if(statusMessage != null) {
            this.statusMessage = statusMessage;
        }
        String intervalRaw = System.getenv("INTERVAL");
        if(intervalRaw != null) {
            this.interval = Integer.valueOf(intervalRaw);
        }
    }

    String getToken() {
        return token;
    }

    public String getChannel() {
        return channel;
    }

    public String getGuild() {
        return guild;
    }

    String getUrl() {
        return url;
    }

    String getMode() {
        return mode;
    }

    String getStatusType() {
        return statusType;
    }

    String getStatusMessage() {
        return statusMessage;
    }

    int getInterval() {
        return interval;
    }

}
