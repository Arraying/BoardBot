package de.arraying.boardbot;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;

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
public final class Entry implements Iterable<Entry> {

    private String string = "";
    private File file;
    Entry next;

    /**
     * Appends text to the string.
     * If the text is past the threshold, a new entry will automatically be created and appended to.
     * @param text The text, not null.
     * @return The last Entry in the chain.
     */
    Entry append(String text) {
        int length = string.length();
        if(text.length() + length > 2000) { // Check if the length is too long.
            text += text.substring(0, 2000 - length); // Get the characters that can still fit.
            next = new Entry();
            return next.append(text.substring(2000 - length)); // Recursively fill the next Entry.
        } else {
            string += text;
            return this;
        }
    }

    /**
     * Sets the file for the Entry.
     * If this entry already has a file, then a new Entry will be created.
     * @param file The file.
     * @return Entry the last Entry in the chain.
     */
    Entry file(File file) {
        next = new Entry();
        if(this.file != null) {
            return next.file(file); // Technically does not need to be recursive but I think it looks elegant.
        } else {
            this.file = file;
            return next;
        }
    }

    /**
     * Forcefully moves onto the next Entry by creating it.
     * @return a new Entry.
     */
    Entry next() {
        next = new Entry();
        return next;
    }

    /**
     * Sends the Entry to a TextChannel.
     * @param channel The TextChannel to send to.
     */
    void send(TextChannel channel) {
        if(string.isEmpty()
                && file == null) {
            return; // Nothing to send.
        }
        if(string.isEmpty()) {
            channel.sendFile(file).queue(success -> clean()); // Thanks JDA for making this so extra.
        } else {
            MessageAction action = channel.sendMessage(string);
            if(file != null) {
                action = action.addFile(file);
            }
            action.queue(success -> clean());
        }
    }

    /**
     * Gets the iterator for the chain.
     * @return An iterator implementation.
     */
    @NotNull
    @Override
    public Iterator<Entry> iterator() {
        return new EntryIterator(this);
    }

    /**
     * Cleans up, if applicable.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void clean() {
        if(file != null) {
            file.delete();
        }
    }
}
