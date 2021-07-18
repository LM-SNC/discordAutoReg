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
        DiscordUtils discordUtils = new DiscordUtils();

        String mail = "m1ain1s_42021@bk.ru";
        String loin = "123asdd";
        String password = "koasdasb";
        String token = discordUtils.createAccount(mail, loin, password);

        if (token != null)
            System.out.println("Успешная регистрация: " + token);
        else
            System.out.println("В прорцессе регистрации произошла ошибка!");

    }
}
