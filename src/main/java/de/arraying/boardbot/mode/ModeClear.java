package de.arraying.boardbot.mode;

import com.sun.scenario.effect.Offset;
import de.arraying.boardbot.Bot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;

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
public final class ModeClear implements Mode {

    /**
     * Clears all the messages in the channel, individually.
     * @return The same TextChannel.
     */
    @Override
    public TextChannel getPreparedChannel() {
        TextChannel current = Bot.INSTANCE.getJda().getTextChannelById(Bot.INSTANCE.getConfig().getChannel());
        if(current == null) {
            return null;
        }
        List<Message> toDelete = new ArrayList<>();
        long index = current.getLatestMessageIdLong();
        toDelete.add(current.retrieveMessageById(index).complete());
        for(;;) { // There's probably a more elegant way to do this.
            MessageHistory history = current.getHistoryBefore(index, 100).complete();
            List<Message> messages = history.getRetrievedHistory();
            toDelete.addAll(messages);
            if(messages.size() < 100) { // We are getting in chunks of 100. If < 100, this is the last chunk.
                break;
            }
            index = messages.get(99).getIdLong();
        }
        for(Message message : toDelete) {
            message.delete().complete(); // Complete such that everything is done by the time the method returns.
        }
        return current;
    }

}
