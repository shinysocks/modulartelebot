package modulartelebot.botmodules;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import modulartelebot.Bot;
import modulartelebot.BotModule;

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
