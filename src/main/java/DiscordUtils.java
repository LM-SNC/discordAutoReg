import Data.Warnings;
import org.jsoup.Connection;

public class DiscordUtils {
    WebUtils webUtils;
    DataUtils dataUtils;
    Number number;
    String g_password;
    String g_accountToken;

    private String fingerPrint;

    public DiscordUtils() {
        webUtils = new WebUtils(CONSTANTS.discordModel);
        dataUtils = new DataUtils();
        number = new Number();
    }

    public String createAccount(String mail, String loin, String password, String birthDay) {

        String response = webUtils.getTextValue(webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/auth/fingerprint",
                "https://discord.com/register"),
                "fingerprint", false);

        System.out.println("Получили отпечаток: " + response);
        fingerPrint = response;


        webUtils.addBody("{\"fingerprint\": \"" + response + "\"," +
                " \"email\": \"" + mail + "\", \"consent\": \"true\"," +
                " \"date_of_birth\": " + "\"" + birthDay + "\"," +
                " \"gift_code_sku_id\": \"null\"," +
                " \"invite\": \"null\"," +
                " \"password\": \""+ password +"\"," +
                " \"username\": \"" + loin + "\"," +
                " \"captcha_key\" : \"" + null + "\"}");

        response = webUtils.getTextCon(Connection.Method.POST, "https://discord.com/api/v9/auth/register", "https://discord.com/register");
        webUtils.clear();
        String captcaKey = webUtils.getTextValue(response, "captcha_sitekey", false);
        this.g_password = password;



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


        String captchaId = webUtils.getTextCon(Connection.Method.POST, "http://rucaptcha.com/in.php?key=" + CONSTANTS.captchaToken + "&method=hcaptcha&sitekey=" + captcaKey + "&pageurl=https://discord.com/register",  "https://yandex.ru").substring(3);

        System.out.println("Ожидаем решение капчи...");
        String readyCaptcha = "";
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readyCaptcha = webUtils.getTextCon(Connection.Method.POST, "http://rucaptcha.com/res.php?key=" + CONSTANTS.captchaToken + "&action=get&id=" + captchaId,"https://yandex.ru").substring(3);

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
        response = webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/auth/register",
                "https://discord.com/register");
        webUtils.clear();
        System.out.println(response);

        g_accountToken = webUtils.getTextValue(response, "token", false);
        return g_accountToken;
    }





    //END CREATE ACCOUNT
    //START VERIFY EMAIL

    public String verifyEmail(String verifyLink) {
        String[] pathKey =  verifyLink.split("/");
        webUtils.addHeader("path", "/ls/" + pathKey[pathKey.length-1]);
        webUtils.addData("upn", pathKey[pathKey.length-1].substring(10, pathKey[pathKey.length-1].length()-1));
       // System.out.println(pathKey[pathKey.length-1].substring(10, pathKey[pathKey.length-1].length()-1));
        pathKey = webUtils.getParsedCon(Connection.Method.GET,
                verifyLink,
                "https://discord.com/register").location().split("/");
        webUtils.clear();

        String location = pathKey[pathKey.length-1].substring(13, pathKey[pathKey.length-1].length());
        System.out.println(pathKey[pathKey.length-1]);
        System.out.println(location);

       // webUtils.addData("captcha_key", "null");
        //webUtils.addData("token", location);
        //webUtils.addHeader("fingerprint", fingerPrint);
        webUtils.addBody("{\"fingerprint\": \"" +  fingerPrint + "\"," +
                " \"token\": \"" + location + "\"," +
                " \"captcha_key\" : \"null\"}");
        String response = webUtils.getTextCon(Connection.Method.POST, "https://discord.com/api/v9/auth/verify", "https://discord.com/verify");
        String captchaKey = webUtils.getTextValue(response, "captcha_sitekey", false);
        System.out.println(response + "::" + captchaKey);
        webUtils.clear();

        String captchaId = webUtils.getTextCon(Connection.Method.POST,
                "http://rucaptcha.com/in.php?key=" + CONSTANTS.captchaToken + "&method=hcaptcha&sitekey=" + captchaKey + "&pageurl=https://discord.com/register",
                "https://yandex.ru").substring(3);

        System.out.println("Ожидаем решение капчи...");
        String readyCaptcha = "";
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            readyCaptcha = webUtils.getTextCon(Connection.Method.POST, "http://rucaptcha.com/res.php?key=" + CONSTANTS.captchaToken + "&action=get&id=" + captchaId,"https://yandex.ru").substring(3);

            if (!readyCaptcha.contains("READY")) {
                if (readyCaptcha.contains("UNSOLVABLE")) {
                    System.out.println("Каптчу посчиатли неразрешимой, отправляем новый запрос....");

                    captchaId = webUtils.getTextCon(Connection.Method.POST,
                            "http://rucaptcha.com/in.php?key=" + CONSTANTS.captchaToken + "&method=hcaptcha&sitekey=" + captchaKey + "&pageurl=https://discord.com/register",
                            "https://yandex.ru").substring(3);
                    continue;
                } else {
                    break;
                }
            }
        }

        System.out.println("Готовая капча: " + readyCaptcha);
        webUtils.addBody("{\"fingerprint\": \"" +  fingerPrint + "\"," +
                " \"token\": \"" + location + "\"," +
                " \"captcha_key\" : \"" + readyCaptcha + "\"}");

        return webUtils.getTextCon(Connection.Method.POST, "https://discord.com/api/v9/auth/verify", "https://discord.com/verify");
    }

    public void verifyNumber() {
        webUtils.clear();
        String response;
        String rusNumber = number.createDiscordNumber();
        webUtils.addBody("{\"phone\": \"" + rusNumber + "\"}");
        webUtils.addHeader("authorization", g_accountToken);
        response = webUtils.getTextCon(Connection.Method.POST, "https://discord.com/api/v9/users/@me/phone", "https://discord.com/channels/@me");
        System.out.println("Отправили код: " + response);
        System.out.println("Номер: " + rusNumber);
        System.out.println("Токен: " + g_accountToken);
        webUtils.clear();

        String code = number.getCode();
        System.out.println(code);
        webUtils.addBody("{\"phone\": \"" + rusNumber + "\"," +
                " \"code\" : \"" + code + "\"}");
        webUtils.addHeader("authorization", g_accountToken);
        response = webUtils.getTextCon(Connection.Method.POST, "https://discord.com/api/v9/phone-verifications/verify", "https://discord.com/channels/@me");

        System.out.println("Возврат после отправки кода: " + response);
        webUtils.clear();

        String token = webUtils.getTextValue(response, "token", true);
        System.out.println("Token: " + token);

        webUtils.addBody("{\"password\": \"" + g_password + "\"," +
                " \"phone_token\" : \"" + token + "\"}");
        webUtils.addHeader("authorization", g_accountToken);
        response = webUtils.getTextCon(Connection.Method.POST, "https://discord.com/api/v9/users/@me/phone", "https://discord.com/channels/@me");
        System.out.println(response);
        webUtils.clear();


    }

}
