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
    String g_Login = "";

    public boolean createAccount(String loin, String password) {
        Logger.logFuncStart(); //Start function
        mail.createEmail(); //Start create email
        g_FingerPrint = getFingerPrint(); //Getting fingerprint for request
        String responseData = getAuthJson(mail.getMail(), loin, password, //Getting data before auth reauest
                birthDay,
                g_FingerPrint, null);
        String captchaKey = getCaptchaKey(responseData); //getting captcha key
        if (!checkCaptchaKey(captchaKey, responseData)) //check captcha key for valid
            return false;
        String readyCaptchaSolution = readyCaptchaSolution(getCaptchaId(captchaKey));
        responseData = getAuthJson(mail.getMail(), loin, password, birthDay, g_FingerPrint, readyCaptchaSolution);
        g_AccountToken = jsonUtils.getTextValue(responseData, "token", false);
        g_Password = password;
        g_Login = loin;
        Logger.logFuncEnd("Mail: " + mail.getMail() + "\n" + "login: " + loin + "\n" + "password: " + password + "\n" + "token: " + g_AccountToken);

        return true;
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
        Logger.logFuncEnd();
    }

    public void verifyNumber() {
        Logger.logFuncStart();
        String phoneNumber = phone.createDiscordNumber();
        String code = "";
        for (int i = 0; i < 3; i ++) {
            sendCode(phoneNumber, g_AccountToken);
            code = phone.getCode();
            if (!code.contains("WAIT") && !code.isEmpty())
                break;
        }
        if (code.contains("WAIT") || code.isEmpty()) {
            Logger.logError("Смс не пришло");
            Logger.logError("SMS: " + code);
            return;
        }
        String responseData = sendCodeRequest(phoneNumber, code, g_AccountToken);
        String verifyToken = jsonUtils.getTextValue(responseData,
                 "token",
                 true);
        responseData = sendVerifyToken(g_Password, verifyToken, g_AccountToken);
        Logger.logInfo(responseData);
        Logger.logFuncEnd();

        fileUtils.fileWriter(mail.getMail() + ":" + g_Login + ":" + g_Password + ":" + g_AccountToken);
    }
}
