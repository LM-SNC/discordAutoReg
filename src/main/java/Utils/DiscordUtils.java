package Utils;

import Data.CONSTANTS;
import Data.Warnings;
import Modules.Phone;
import org.jsoup.Connection;

public class DiscordUtils {
    WebUtils webUtils;
    DataUtils dataUtils;
    JsonUtils jsonUtils;

    public DiscordUtils() {
        webUtils = new WebUtils(CONSTANTS.discordModel);
        dataUtils = new DataUtils();
        jsonUtils = new JsonUtils();
    }

    public String getFingerPrint () {
        Logger.logFuncStart();
        String fingerPrint = jsonUtils.getTextValue(webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/auth/fingerprint",
                "https://discord.com/register"),
                "fingerprint", false);
        Logger.logFuncEnd(fingerPrint);
        return fingerPrint;
    }

    public String getAuthJson(String mail, String loin, String password, String birthDay, String fingerPrint, String captchaSolution) {
        Logger.logFuncStart();
        webUtils.addBody("{\"fingerprint\": \"" + fingerPrint + "\"," +
                " \"email\": \"" + mail + "\", \"consent\": \"true\"," +
                " \"date_of_birth\": " + "\"" + birthDay + "\"," +
                " \"gift_code_sku_id\": \"null\"," +
                " \"invite\": \"null\"," +
                " \"password\": \""+ password +"\"," +
                " \"username\": \"" + loin + "\"," +
                " \"captcha_key\" : \"" + captchaSolution + "\"}");

        String responseData = webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/auth/register",
                "https://discord.com/register");
        Logger.logFuncEnd(responseData);
        return responseData;
    }

    public String getCaptchaKey(String json) {
        Logger.logFuncStart();
        String captchaKey = jsonUtils.getTextValue(json,
                "captcha_sitekey",
                false);
        Logger.logFuncEnd(captchaKey);
        return captchaKey;
    }

    public boolean checkCaptchaKey(String captchaKey, String response) {
        Logger.logFuncStart();
        if (captchaKey != null) {
            Logger.logInfo("captchaKey: " + captchaKey);
            Logger.logFuncEnd("response: " + response);
            return true;
        }
        else {
            for (Warnings error : Warnings.values()) {
                if (response.contains(error.name())) {
                    Logger.logInfo("Невозможно создать аккаунт: " + error.getTitle());
                    //System.out.println("Невозможно создать аккаунт: " + error.getTitle());
                    return false;
                }
            }
        }
        Logger.logInfo("captchaKey: " + captchaKey);
        Logger.logFuncEnd("response: " + response);
        return false;
    }

    public String getCaptchaId(String captchaKey) {
        Logger.logFuncStart();
        String captchaId = webUtils.getTextCon(Connection.Method.POST,
                "http://rucaptcha.com/in.php?key=" + CONSTANTS.captchaToken + "&method=hcaptcha&sitekey=" + captchaKey + "&pageurl=https://discord.com/register",
                "https://yandex.ru").substring(3);
        Logger.logFuncEnd(captchaId);
        return captchaId;
    }

    public String readyCaptchaSolution(String captchaId) {
        Logger.logFuncStart();
        String solution = null;
        while (true) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            solution = webUtils.getTextCon(Connection.Method.POST,
                    "http://rucaptcha.com/res.php?key=" + CONSTANTS.captchaToken + "&action=get&id=" + captchaId,
                    "https://yandex.ru").substring(3);

            if (!solution.contains("READY"))
                break;
        }
        Logger.logFuncEnd(solution);
        return solution;
    }

    public String getLocation(String verifyLink) {
        Logger.logFuncStart();
        String[] pathKey =  verifyLink.split("/");
        String lastPathKeyElement =  pathKey[pathKey.length-1];
        webUtils.addHeader("path", "/ls/" + lastPathKeyElement);
        webUtils.addData("upn",lastPathKeyElement.substring(10,lastPathKeyElement.length()-1));
        pathKey = webUtils.getParsedCon(Connection.Method.GET,
                verifyLink,
                "https://discord.com/register").location().split("/");
        lastPathKeyElement = pathKey[pathKey.length-1];
        String location = lastPathKeyElement.substring(13, lastPathKeyElement.length());
        Logger.logFuncEnd(location);
        return location;
    }

    public String getVerifyJson(String fingerPrint, String location, String readyCaptcha) {
        Logger.logFuncStart();
        webUtils.addBody("{\"fingerprint\": \"" +  fingerPrint + "\"," +
                " \"token\": \"" + location + "\"," +
                " \"captcha_key\" : \"" + readyCaptcha + "\"}");
        String responseData = webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/auth/verify",
                "https://discord.com/verify");
        Logger.logFuncEnd(responseData);
        return responseData;
    }

    public void sendCode(String phoneNumber, String accountToken) {
        Logger.logFuncStart();
        webUtils.addBody("{\"phone\": \"" + phoneNumber + "\"}");
        webUtils.addHeader("authorization", accountToken);
        Logger.logFuncEnd(webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/users/@me/phone",
                "https://discord.com/channels/@me"));
    }

    public String sendCodeRequest(String phoneNumber, String code, String accountToken) {
        Logger.logFuncStart();
        webUtils.addBody("{\"phone\": \"" + phoneNumber + "\"," +
                " \"code\" : \"" + code + "\"}");
        webUtils.addHeader("authorization", accountToken);
        String responseData = webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/phone-verifications/verify",
                "https://discord.com/channels/@me");
        Logger.logFuncEnd(responseData);
        return responseData;
    }

    public String sendVerifyToken(String password, String verifyToken, String accountToken) {
        Logger.logFuncStart();
        webUtils.addBody("{\"password\": \"" + password + "\"," + //g_password is null
                " \"phone_token\" : \"" + verifyToken + "\"}");
        webUtils.addHeader("authorization", accountToken);
        String response = webUtils.getTextCon(Connection.Method.POST,
                "https://discord.com/api/v9/users/@me/phone",
                "https://discord.com/channels/@me");
        Logger.logFuncEnd(response);
        return response;
    }
}