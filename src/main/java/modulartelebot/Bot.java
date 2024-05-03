package modulartelebot;

import java.util.ArrayList;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SuppressWarnings("deprecation")
public class Bot extends TelegramLongPollingBot {
    private ArrayList<BotModule> modules;
    private String name;
    private String token;

    public Bot(String name, String token) {
        this.name = name;
        this.token = token;
        this.modules = new ArrayList<BotModule>();

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
        } catch (TelegramApiException e) {
            Log.log("unable to register bot.. " + e.getMessage(), Log.FLAVOR.ERR);
        }
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String message = update.getMessage().getText();
            String id = Long.toString(update.getMessage().getChatId());

            for (BotModule module : this.modules) {
                for (String c : module.getCommands()) {
                    if (message.contains(c)) {
                        Log.log("recieved command " + c, Log.FLAVOR.SUCCESS);
                        new Thread(() -> {
                            try {
                                module.update(message, id);
                            } catch (TelegramApiException e) {
                                Log.log("can't send message " + e.getMessage(), Log.FLAVOR.ERR);
                            }
                        }).start();
                    }
                }
            }
        }
    }

    public void addModule(BotModule module) {
        this.modules.add(module);
    }
}
