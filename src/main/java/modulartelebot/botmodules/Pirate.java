package modulartelebot.botmodules;

import java.io.File;

import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import modulartelebot.Bot;
import modulartelebot.BotModule;
import modulartelebot.Log;

public class Pirate extends BotModule {
    public Pirate(Bot bot) {
        super(bot);
        addCommand("/pirate");
        addCommand("open.spotify.com");
    }

    @Override
    public void update(String message, String chatId) throws TelegramApiException {
        String query = "";
        if (message.contains("/pirate")) {
            query = message.split("/pirate")[1].strip();
            send(new SendMessage(chatId, String.format("downloading '%s'...", query)));
        } else {
            String[] words = message.split(" ");
            for (String word : words) {
                if (word.contains("open.spotify.com")) {
                    send(new SendMessage(chatId, "found spotify link. downloading.."));
                    query = word;
                }
            }
        }

        File subTempDir = new File("./temp/pirate", query);

        // download
        try {
            download(query, subTempDir);
        } catch (Exception e) {
            send(new SendMessage(chatId, "failed to download, " + e.getMessage()));
            Log.log(String.format("failed to download '%s', %s", query, e.getMessage()), Log.FLAVOR.ERR);
            return;
        }

        // send song;
        for (File f : subTempDir.listFiles()) {
            send(new SendDocument(chatId, new InputFile(f)));
            f.delete();
        }
        subTempDir.delete();
    }

    private void download(String query, File dir) throws Exception {
        // TODO fix url directories and albums
        String spotdl = String.format("./temp/py_venv/bin/spotdl download '%s' --format mp3 --output '%s'", query, dir);
        run(spotdl);
    }

    public static void run(String command) throws Exception {
        new ProcessBuilder("/bin/sh", "-c", command).start().onExit().get();
    }

    @Override
    public void init() {
        StringBuilder command = new StringBuilder();

        if (new File("./temp/py_venv").exists()) {
            Log.log("python venv already active.", Log.FLAVOR.INFO);
        } else {
            Log.log("creating python virtual environment..", Log.FLAVOR.INFO);
            command.append("python3 -m venv ./temp/py_venv ; ");
        }

        command.append("source ./temp/py_venv/bin/activate ; pip install spotdl yt-dlp ; ");
        command.append("spotdl --download-ffmpeg");

        try {
            Pirate.run(command.toString());
            Log.log("installed spotdl to python venv", Log.FLAVOR.SUCCESS);
        } catch (Exception e) {
            Log.log("unable to create python virtual env or install spotdl", Log.FLAVOR.ERR);
        }
    }
}
