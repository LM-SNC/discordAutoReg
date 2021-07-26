package Core;

import Modules.Mail;
import Modules.Phone;
import Utils.*;

public class DiscordAccountsManager extends DiscordUtils {
    DataUtils dataUtils = new DataUtils();
    JsonUtils jsonUtils = new JsonUtils();
    FileUtils fileUtils = new FileUtils();

    Mail mail = new Mail();
    Phone phone = new Phone();

    String birthDay = dataUtils.getDiscordBirthDay();
    String g_FingerPrint = "";
    String g_AccountToken = "";
    String g_Password = "";

    public void createAccount(String loin, String password) {
        Logger.logFuncStart(); //Start function
        mail.createEmail(); //Start create email
        g_FingerPrint = getFingerPrint(); //Getting fingerprint for request
        String responseData = getAuthJson(mail.getMail(), loin, password, //Getting data before auth reauest
                birthDay,
                g_FingerPrint, null);
        String captchaKey = getCaptchaKey(responseData); //getting captcha key
        if (!checkCaptchaKey(captchaKey, responseData)) //check captcha key for valid
            return;
        String readyCaptchaSolution = readyCaptchaSolution(getCaptchaId(captchaKey));
        responseData = getAuthJson(mail.getMail(), loin, password, birthDay, g_FingerPrint, readyCaptchaSolution);
        g_AccountToken = jsonUtils.getTextValue(responseData, "token", false);
        g_Password = password;
        Logger.logFuncEnd("Mail: " + mail.getMail() + "\n" + "login: " + loin + "\n" + "password: " + password + "\n" + "token: " + g_AccountToken);
        fileUtils.fileWriter(mail.getMail() + ":" + loin + ":" + password + ":" + g_AccountToken);
    }

    public void verifyMail() {
        Logger.logFuncStart();
        String link = mail.getActuallyMessage();
        Logger.logInfo(link);
        Logger.logInfo(mail.getMail());
        String discordLink = mail.getVerifyLink(link);
        Logger.logInfo(discordLink);

        String location = getLocation(discordLink);
        String response = getVerifyJson(g_FingerPrint, location, null);
        String captchaKey = getCaptchaKey(response);
        String captchaId = getCaptchaId(captchaKey);

        String readyCaptcha = readyCaptchaSolution(captchaId);
        getVerifyJson(g_FingerPrint, location, readyCaptcha);
    }

    public void verifyNumber() {
        String phoneNumber = phone.createDiscordNumber();
        sendCode(phoneNumber, g_AccountToken);
        String code = phone.getCode();
        String responseData = sendCodeRequest(phoneNumber, code, g_AccountToken);
        String verifyToken = jsonUtils.getTextValue(responseData,
                 "token",
                 true);
        sendVerifyToken(g_Password, verifyToken, g_AccountToken);
    }
}
