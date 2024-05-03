package modulartelebot.botmodules;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.convertapi.client.Config;
import com.convertapi.client.ConvertApi;
import com.convertapi.client.Param;

import modulartelebot.Bot;
import modulartelebot.BotModule;

public class GitRatio extends BotModule {
  private String[] messageArgs;
  private final String githubChartApiURL = "https://ghchart.rshah.org/";
  private final String convertApiURL = "https://v2.convertapi.com/convert/svg/to/png";
  private File tempDir;
  
  public GitRatio(Bot bot, String convertApiToken){
    super(bot);
    Config.setDefaultSecret(convertApiToken);
    addCommand("/ratio");
  }

  private File getGitActivity(URL url, String fileName) throws InterruptedException, ExecutionException, IOException{
    File convertedImage = ConvertApi.convert("svg", "png", new Param("File", url.toString())).get().saveFile(Path.of(tempDir.getCanonicalPath())).get().toFile();
    convertedImage.renameTo(new File(tempDir.getCanonicalPath()+"/"+fileName));
    return convertedImage;
  }

  private void setupTempDirectory(){
    if(tempDir == null){
      tempDir = new File("ratio-files");
      tempDir.mkdir();
    }
  }

  private void deleteTempDirectory(){
    if(tempDir != null && tempDir.exists()){
      tempDir.delete();
    }
  }

  @Override
  public void update(String message, String chatId) throws TelegramApiException{
    messageArgs = message.split(" ");
    if(messageArgs.length < 3){
      send(new SendMessage(chatId, "ERR: Too few arguments provided to command!"));
    }else if(messageArgs.length > 3){
      send(new SendMessage(chatId, "ERR: Too many arguments provided to command!"));
    }else{
      try {
        setupTempDirectory();
        SendPhoto userStatsOnePhoto = new SendPhoto();
        userStatsOnePhoto.setChatId(chatId);
        SendPhoto userStatsTwoPhoto = new SendPhoto();
        userStatsTwoPhoto.setChatId(chatId);
        URL userStatsOne = new URL(String.format("%s/%s", githubChartApiURL, messageArgs[1]));
        File fileOne = getGitActivity(userStatsOne, "firstdev.png");
        URL userStatsTwo = new URL(String.format("%s/%s", githubChartApiURL, messageArgs[2]));
        File fileTwo = getGitActivity(userStatsTwo, "seconddev.png");
        userStatsOnePhoto.setPhoto(new InputFile(tempDir.listFiles()[0]));
        userStatsTwoPhoto.setPhoto(new InputFile(tempDir.listFiles()[1]));
        send(new SendMessage(chatId, messageArgs[1]+"'s Git Activity"));
        send(userStatsOnePhoto);
        send(new SendMessage(chatId, messageArgs[2]+"'s Git Activity"));
        send(userStatsTwoPhoto);
        send(new SendMessage(chatId, "HAHA Soy Dev"));
        deleteTempDirectory();
      } catch (Exception e) {
        System.err.println("An exception has occurred: "+e);
      }
    }
  }

  @Override
  public void init() {}
}
