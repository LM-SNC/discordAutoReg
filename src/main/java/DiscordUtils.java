import Data.Warnings;
import org.jsoup.Connection;

import java.util.HashMap;

public class DiscordUtils {
    WebUtils webUtils;

    public DiscordUtils() {
        webUtils = new WebUtils();
    }

    public String createAccount(String mail, String loin, String password) {

        String response = webUtils.getTextValue(webUtils.sendRequest(Connection.Method.POST,
                "https://discord.com/api/v9/auth/fingerprint",
                "https://discord.com/register"),
                "fingerprint", false);

        System.out.println("Получили отпечаток: " + response);
        String fingerPrint = response;


        webUtils.addBody("{\"fingerprint\": \"" + response + "\"," +
                " \"email\": \"" + mail + "\", \"consent\": \"true\"," +
                " \"date_of_birth\": \"1992-05-04\"," +
                " \"gift_code_sku_id\": \"null\"," +
                " \"invite\": \"null\"," +
                " \"password\": \""+ password +"\"," +
                " \"username\": \"" + loin + "\"," +
                " \"captcha_key\" : \"" + null + "\"}");

        response = webUtils.sendRequest(Connection.Method.POST, "https://discord.com/api/v9/auth/register", "https://discord.com/register");
        String captcaKey = webUtils.getTextValue(response, "captcha_sitekey", false);




        if (captcaKey != null)
            System.out.println("Ключ капчи: " + captcaKey);
        else {
          for (Warnings error : Warnings.values()) {
              if (response.contains(error.name())) {
                  System.out.println("Невозможно создать аккаунт: " + error.getTitle());
                  return null;
              }
          }
        }


//        else if (response.contains("EMAIL_ALREADY_REGISTERED"))
//            System.out.println("Ошибка получения ключа капчи, почта уже зарегестрирована!");
//        else if (response.contains("USERNAME_TOO_MANY_USERS"))
//            System.out.println("Ошибка получения ключа капчи капчи, невалидное имя!");
//        else if (response.contains("BASE_TYPE_MIN_LENGTH"))
//            System.out.println("Ошибка получения ключа капчи --капчи, невалидный пароль!");
//        else
//            System.out.println(response);


        String captchaId = webUtils.sendRequest(Connection.Method.POST, "http://rucaptcha.com/in.php?key=" + CONSTANTS.captchaToken + "&method=hcaptcha&sitekey=" + captcaKey + "&pageurl=https://discord.com/register",  "https://yandex.ru").substring(3);

        System.out.println("Ожидаем решение капчи...");
        String readyCaptcha = "";
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readyCaptcha = webUtils.sendRequest(Connection.Method.POST, "http://rucaptcha.com/res.php?key=" + CONSTANTS.captchaToken + "&action=get&id=" + captchaId,"https://yandex.ru").substring(3);

            if (!readyCaptcha.contains("READY")) {
                break;
            }
        }

        System.out.println("Готовая капча: " + readyCaptcha);


        webUtils.addBody("{\"fingerprint\": \"" +  fingerPrint + "\"," +
                " \"email\": \"" + mail + "\"," +
                " \"consent\": \"true\"," +
                " \"date_of_birth\": \"1992-05-04\"," +
                " \"gift_code_sku_id\": \"null\"," +
                " \"invite\": \"null\"," +
                " \"password\": \""+ password +"\"," +
                " \"username\": \"" + loin + "\"," +
                " \"captcha_key\" : \"" + readyCaptcha + "\"}");
        response = webUtils.sendRequest(Connection.Method.POST, "https://discord.com/api/v9/auth/register", "https://discord.com/register");
        System.out.println(response);

        return webUtils.getTextValue(response, "token", false);
    }

    public void verifyEmail() {

    }

    public void verifyNumber() {

    }

}
