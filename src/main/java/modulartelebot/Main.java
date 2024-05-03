package modulartelebot;

import java.io.File;
import java.util.Arrays;

import modulartelebot.botmodules.Pirate;
import modulartelebot.botmodules.YTDL;

public class Main {
    public static void main(String[] args) {
        if (!Arrays.asList(new File("./").listFiles()).contains(new File("temp")))
            new File("./temp").mkdir();
        Bot bot = new Bot("gourpbot", getToken("TELEGRAM_TOKEN"));
        new Pirate(bot);
        new YTDL(bot);
        // add more bots here

    }

    public static String getToken(String tokenKey) {
        String token = System.getenv(tokenKey);
        if (token == null) {
            Log.log("cannot get token", Log.FLAVOR.ERR);
        }
        return token;
    }
}
