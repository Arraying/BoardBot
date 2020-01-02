package de.arraying.boardbot;

import net.dv8tion.jda.api.entities.TextChannel;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.concurrent.atomic.AtomicInteger;

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
final class Updater {

    private final AtomicInteger integer = new AtomicInteger();
    private File previous;

    /**
     * Update is the main updater method.
     * First, the file is polled, and it checks if there was a change.
     * If there was a change, the file will be parsed and organised into entries.
     * Then, the channel gets prepared for the update.
     * Finally, all entries get sent to the channel.
     * @throws Exception If there is an exception.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    void update() throws Exception {
        Logger logger = Bot.INSTANCE.getLogger();
        File now = getFileFromUrl(Bot.INSTANCE.getConfig().getUrl());
        if(previous != null
                && FileUtils.contentEquals(previous, now)) {
            delete(now); // Clean up.
            logger.info("No updates found.");
            return;
        }
        logger.info("Updates found.");
        if(previous != null) {
            delete(now); // Clean up.
        }
        previous = now; // The future is now, old man.
        Entry initial = new Entry();
        Entry current = initial;
        for(String line : Files.readAllLines(now.toPath())) {
            if(line.startsWith("\\next")) {
                current = current.next();
            } else if(line.startsWith("\\file ")) { // It is a file.
                line = line.substring(6);
                File attachment = getFileFromUrl(line);
                current = current.file(attachment);
            } else { // It is text.
                current = current.append(line + "\n"); // The newline is lost otherwise.
            }
        }
        logger.info("Getting channel...");
        TextChannel channel = Bot.INSTANCE.getMode().getPreparedChannel();
        if(channel == null) {
            logger.info("Channel not available.");
            return;
        }
        logger.info("Sending message(s)...");
        initial.forEach(it -> it.send(channel)); // Custom Iterable implementations are hot.
    }

    /**
     * Gets a file from a URL.
     * @param urlString The URL as a string.
     * @return A file.
     * @throws IOException An exception if the file could not be created, or the URL is invalid.
     */
    private File getFileFromUrl(String urlString) throws IOException {
        File dump = new File("dump." + System.currentTimeMillis() + "." + integer.incrementAndGet() + getExtension(urlString));
        URL url = new URL(urlString);
        FileUtils.copyURLToFile(url, dump);
        return dump;
    }

    /**
     * Gets the file extension for the URL.
     * @param urlString The URL.
     * @return The extension, with a ".".
     */
    private String getExtension(String urlString) {
        if(urlString.contains(".")) {
            String end = urlString.substring(urlString.lastIndexOf('.'));
            if(end.contains("/")) {
                end = end.substring(0, end.indexOf('/')); // Avoid files being created as directories.
            }
            return end;
        }
        return ".txt"; // Force a text file.
    }

    /**
     * Deletes a file or directory.
     * @param file The file.
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void delete(File file) throws IOException {
        if(file.isDirectory()) { // Should never be the case, but you never know.
            FileUtils.deleteDirectory(file); // Love having helper methods.
        } else {
            file.delete();
        }
    }

}
