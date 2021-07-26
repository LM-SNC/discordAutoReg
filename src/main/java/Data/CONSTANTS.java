package Data;

import java.util.HashMap;

public class CONSTANTS {
    public static String token = "";
    public static String captchaToken = "";
    public static String numberToken = "";

    public static HashMap<String, String> discordModel = new HashMap<>();
    public static void setDiscordModel() {
        discordModel.put("content-type", "application/json");
        discordModel.put("scheme", "https");
        discordModel.put("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        discordModel.put("authority", "click.discord.com");
        discordModel.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
        discordModel.put("sec-ch-ua", "\" Not;A Brand\";v=\"99\", \"Google Chrome\";v=\"91\", \"Chromium\";v=\"91\"");
        discordModel.put("sec-ch-ua-mobile", "?0");
    }

    public static HashMap<String, String> gmailnatorModel = new HashMap<>();
    public static void setGmailnatorModel() {
        discordModel.put("accept-language", "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        discordModel.put("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
    }
}
