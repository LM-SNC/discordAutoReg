import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        CONSTANTS.setDiscordModel();
        CONSTANTS.setGmailnatorModel();

        DataUtils dataUtils = new DataUtils();
        DiscordUtils discordUtils = new DiscordUtils();
        Mail mailUt = new Mail();
        FileUtils fileUtils = new FileUtils();


//        number.createDiscordNumber();
//        number.getSms();

        mailUt.createEmail();
        String mail = mailUt.getMail();
        String login = dataUtils.getDiscordLogin();
        String password = dataUtils.getDiscordPassword();
        String birthDay = dataUtils.getDiscordBirthDay();

        String token =  discordUtils.createAccount(mail, login, password, birthDay);

        String link = mailUt.getActuallyMessage();
        String discordLink = mailUt.getDiscordLinkFromMessage(link);
        System.out.println(discordLink);

        System.out.println(mail + ":" + password + ":" + login + ":" + token);

        System.out.println(discordUtils.verifyEmail(discordLink));

        discordUtils.verifyNumber();

        fileUtils.fileWriter(mail + ":" + login + ":" + password + ":" + token);




    }
}
