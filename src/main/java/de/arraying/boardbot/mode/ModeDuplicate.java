package de.arraying.boardbot.mode;

import de.arraying.boardbot.Bot;
import de.arraying.boardbot.Config;
import net.dv8tion.jda.api.entities.TextChannel;

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
public final class ModeDuplicate implements Mode{

    /**
     * Duplicates the current announcement channel.
     * Then, it sets the topic to the one of the old channel.
     * @return A brand new TextChannel.
     */
    @Override
    public TextChannel getPreparedChannel() {
        Config config = Bot.INSTANCE.getConfig();
        TextChannel current = Bot.INSTANCE.getJda().getTextChannels().stream()
                .filter(channel -> channel.getName().equalsIgnoreCase(config.getChannel())
                        && (config.getGuild().isEmpty() || channel.getGuild().getId().equals(config.getGuild())))
                .findFirst()
                .orElse(null);
        if(current == null) {
            return null;
        }
        TextChannel replacement = current.createCopy()
                .setPosition(current.getPosition())
                .complete();
        current.delete().queue();
        return replacement;
    }

}
