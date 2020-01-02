package de.arraying.boardbot;

import ch.qos.logback.classic.Level;
import com.sun.org.apache.xpath.internal.operations.Mod;
import de.arraying.boardbot.mode.Mode;
import de.arraying.boardbot.mode.ModeClear;
import de.arraying.boardbot.mode.ModeDuplicate;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
public enum Bot {

    INSTANCE;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Logger logger = LoggerFactory.getLogger("BoardBot");
    private final Config config = new Config();
    private final Updater updater = new Updater();
    private Mode mode;
    private JDA jda;

    public static void main(String[] args) {
        Bot.INSTANCE.run();
    }

    /**
     * Runs the bot.
     */
    public void run() {
        ((ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)).setLevel(Level.INFO);
        logger.info("Starting bot.");
        logger.info("Validating config...");
        if(config.getToken() == null) {
            logger.error("Token is null.");
            return;
        }
        if(config.getChannel() == null) {
            logger.error("Channel is null.");
            return;
        }
        if(config.getUrl() == null) {
            logger.error("URL is null.");
            return;
        }
        try {
            new URL(config.getUrl());
        } catch(MalformedURLException exception) {
            logger.error("Invalid URL.");
            return;
        }
        if(config.getMode().equalsIgnoreCase("duplicate")) {
            mode = new ModeDuplicate();
            logger.info("Using mode: 'duplicate'.");
        } else {
            mode = new ModeClear();
            logger.info("Using mode: 'clear'.");
        }
        if(config.getInterval() <= 0) {
            logger.error("Interval needs to be a positive nonzero integer.");
            return;
        }
        OnlineStatus status;
        try {
            status = OnlineStatus.valueOf(config.getStatusType().toUpperCase());
        } catch(IllegalArgumentException exception) {
            status = OnlineStatus.ONLINE;
        }
        logger.info("Using online status {} with text '{}'.", status, config.getStatusMessage());
        try {
            jda = new JDABuilder(config.getToken())
                    .setStatus(status)
                    .setActivity(Activity.playing(config.getStatusMessage()))
                    .build()
                    .awaitReady();
            logger.info("Logged in.");
        } catch(LoginException exception) {
            logger.error("Invalid bot token.");
        } catch(InterruptedException exception) {
            logger.error("Interrupted, somehow.");
        }
        executor.scheduleWithFixedDelay(() -> { // Scheduling with delay in case updating blocks for eons.
            try {
                updater.update();
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }, 0, config.getInterval(), TimeUnit.MINUTES);
    }

    public Logger getLogger() {
        return logger;
    }

    public Config getConfig() {
        return config;
    }

    public Mode getMode() {
        return mode;
    }

    public JDA getJda() {
        return jda;
    }

}
