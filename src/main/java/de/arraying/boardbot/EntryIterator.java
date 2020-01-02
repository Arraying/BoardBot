package de.arraying.boardbot;

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
public final class EntryIterator implements Iterator<Entry> {

    private Entry entry;

    /**
     * Creates a new iterator for entries.
     * This is a workaround such that the first element is included.
     * @param initial The initial Entry.
     */
    EntryIterator(Entry initial) {
        entry = initial;
    }

    /**
     * Whether or not there will be an Entry after the current one.
     * @return True if there will be, false otherwise.
     */
    @Override
    public boolean hasNext() {
        return entry != null;
    }

    /**
     * Gets the next Entry.
     * @return The Entry.
     */
    @Override
    public Entry next() {
        Entry now = entry;
        this.entry = this.entry.next;
        return now;
    }

}
