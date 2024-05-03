package modulartelebot.botmodules;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.Arrays;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import modulartelebot.Bot;
import modulartelebot.BotModule;
import modulartelebot.Log;

public class YTDL extends BotModule {
	private final File tempDir = new File("./temp/yt-dlp");
	private final String[] validDomainSpecifier = { "youtube", "youtu.be" };

	public YTDL(Bot bot) {
		super(bot);
		addCommand("/ytdl");
		if (!tempDir.exists())
			tempDir.mkdir();
	}

	private void cleanTempDirectory() {
		File[] temporaryFiles = tempDir.listFiles();
		if (temporaryFiles.length > 0) {
			for (File tempFile : temporaryFiles) {
				tempFile.delete();
			}
		}
	}

	private File downloadVideo(String query) throws ExecutionException, IOException, InterruptedException {
		String ytDlp = String.format("./temp/py_venv/bin/yt-dlp \"%s\" -f mp4 -o %s/video.mp4", query, tempDir);
		new ProcessBuilder("/bin/bash", "-c", ytDlp).start().onExit().get();
		return new File(String.format("%s/video.mp4", tempDir));
	}

	@Override
	public void update(String message, String chatId) throws TelegramApiException {
		String[] commandArguments = message.strip().split(" ");
		if (commandArguments.length > 2) {
			send(new SendMessage(chatId, "Err: Invalid number of arguments provided to command"));
		}
		if (Arrays.stream(validDomainSpecifier).anyMatch(commandArguments[1]::contains)) {
			send(new SendMessage(chatId, String.format("Downloading video: [%s]", commandArguments[1])));
			try {
				SendVideo downloadedVideo = new SendVideo();
				downloadedVideo.setChatId(chatId);
				downloadedVideo.setVideo(new InputFile(downloadVideo(commandArguments[1])));
				send(downloadedVideo);
			} catch (ExecutionException | IOException | InterruptedException e) {
				Log.log(String.format("An exception occurred while spawning download process!%s", e), Log.FLAVOR.ERR);
			} finally {
				cleanTempDirectory();
			}
		}
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

		command.append("source ./temp/py_venv/bin/activate ; pip install yt-dlp");

		try {
			Pirate.run(command.toString());
			Log.log("installed yt-dlp to python venv", Log.FLAVOR.SUCCESS);
		} catch (Exception e) {
			Log.log("unable to create python virtual env or install ytdl", Log.FLAVOR.ERR);
		}
	}
}
