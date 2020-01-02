# BoardBot
Auto-updating board bot.

## Running
```
docker container run --name boardbot -d \
  -e TOKEN=yourtoken \
  -e CHANNEL=yourchannel \
  -e URL=yoururl \
  arraying/boardbot
```
## Variables
 
Variables allow you to configure the bot to your liking.
All variables with a default value are optional.
They can be specified to change the default behaviour.
All variables with no default value are required.
The bot will not run without them.
Please note that variables are case sensitive.

### TOKEN
**Default:** *none*

Represents the bot token, used to log into Discord.

### CHANNEL
**Default:** *none*

The channel in which the board is posted.
When `MODE` is set to `clear`, this should be the channel ID.
When `MODE` is set to `duplicate` this should be the channel name.

### URL
**Default:** *none*

The URL to get the content from.
This can be virtually any source that can be updated, from GitHub Gists to Pastebin.
Please note that the content returned needs to be raw, not HTML.

### GUILD
**Default:** *empty string*

The server ID in which the board should be handled.
This exists because when `MODE` is set to `duplicate`, there is a possibility that there are channels with the same name accross different servers.
Because the bot automatically retrieves the first one, it could lead to the wrong channel being grabbed when serving multiple guilds under the same bot.
Therefore, to mitigate this, the `GUILD` variable can be specified to restrict the search to a single server.

### MODE
**Default:** duplicate

When there is an update, there are two possible ways of updating the board.
The method can be set using this variable.

`clear` deletes all the messages in the channel.
This is done individually, and can take a very long time.
It is advised to only use `clear` with a few short messages.

`duplicate` clones the channel, and deletes the original.
The name, topic, permissions, position, etc. will be retained.

### STATUS_TYPE
**Defalt:** ONLINE

The online status for the bot.
Allowed values are `DO_NOT_DISTURB`, `IDLE`, `INVISIBLE` and `ONLINE`.

### STATUS_MESSAGE
**Default:** Announcement fetching.

The game the bot is playing.

### INTERVAL
**Default:** 1

The time, in minutes, to wait between update checks.
Please note that the time is counted from the point where the pervious update is complete.

## Content

The content polled from the URL can be very dynamic.
There are virtually no restrictions placed on it.
The content will be split up into multiple Discord messages, should it be too long.
Because the content is what is being sent to Discord directly, all markdown that works in Discord is supported.

Futhermore there are some custom action that can be specified in the content.
Each action needs to be on a new line, otherwise it will not get parsed.

#### \file <url>

Gets the file located at <url> and appends it to the message. 
If there is no previous message, just the file will be sent.
As many of these as wanted can be placed after each other.

#### \next

Tells the parser to start a new message.
This is useful when you have large chunks of text accross different messages and you want to retain paragraphs.

#### Example
```
Hey there!
\file https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png
**Formatting is possible**
\file https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png
\file https://github.githubassets.com/images/modules/logos_page/GitHub-Mark.png
A message.
\next
Another message!
```
![Example](https://i.imgur.com/70CJ2K8.png)
