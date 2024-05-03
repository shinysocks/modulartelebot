# modulartelebot
### an extensible telegram bot

---
## Current Modules
* GitRatio (compare your git graph with your friends)
* Pirate (download songs from spotify)
* YTDL (download videos from youtube)
* Test (demonstrate module functionality)

## Usage
create a telegram bot
* [obtain your bot token](https://core.telegram.org/bots/tutorial#obtain-your-bot-token)

create a docker image
```
docker build -t modulartelebot-image .
```
create and run a container
```
docker run -d --name modulartelebot -e TELEGRAM_TOKEN='your_token_here' modulartelebot-image
```
monitor the container
```
docker logs -f modulartelebot
```

## Contributing
*to contribute to this project just open a pull request!!*

currently any new modules should extend the BotModule class which contains 4 important methods:
* addCommand() -> adds a command which the bot will listen for.
* update() -> called whenever the bot recieves a message containing the specified command
* init() -> called when the bot is started
* send(SendMessage | SendPhoto | SendVideo | SendAudio | SendDocument) -> sends specified content

### Example usage from Test.java:
```
public class Test extends BotModule {
	public Test(Bot bot) {
		super(bot);
		addCommand("!test");
	}

	@Override
	public void update(String message, String chatId) throws TelegramApiException {
		send(new SendMessage(chatId, "testing"));
	}

	@Override
	public void init() {}
}
```

## Credits
* [TelegramBots API](https://github.com/rubenlagus/TelegramBots)
* [spotify-downloader](https://github.com/spotDL/spotify-downloader)
* [youtube-dl](https://github.com/ytdl-org/youtube-dl)




