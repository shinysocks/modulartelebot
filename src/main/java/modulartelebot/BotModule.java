package modulartelebot;

import java.util.ArrayList;

import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Extend this class to create modules used by the bot.
 * Make sure to add modules to the bot in Main.
 */
public abstract class BotModule {
	private ArrayList<String> commands = new ArrayList<String>();
	private Bot bot;

	public BotModule(Bot bot) {
	    this.bot = bot;
	    bot.addModule(this);
	    init();
	}

    public void addCommand(String command) {
        this.commands.add(command);
        Log.log("added command '" + command + "'", Log.FLAVOR.INFO);
    }

    public ArrayList<String> getCommands() {
        return commands;
    }
    
    public abstract void update(String message, String chatId) throws TelegramApiException;

    public abstract void init();

    public void send(SendMessage message) throws TelegramApiException { bot.execute(message); }
    public void send(SendPhoto message) throws TelegramApiException { bot.execute(message); }
    public void send(SendVideo message) throws TelegramApiException { bot.execute(message); }
    public void send(SendAudio message) throws TelegramApiException { bot.execute(message); }
    public void send(SendDocument message) throws TelegramApiException { bot.execute(message); }
}
