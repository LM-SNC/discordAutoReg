package Core;

import Data.CONSTANTS;
import Data.Proxy;
import Utils.DataUtils;
import Utils.Logger;
import Utils.WebUtils;
import org.jsoup.Connection;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CONSTANTS.setDiscordModel();
        CONSTANTS.setGmailnatorModel();
        CONSTANTS.setDefaultModel();

        WebUtils webUtils = new WebUtils(CONSTANTS.defaultModel);
        Proxy proxy = new Proxy("s");

        System.out.println(System.getProperty("https.proxyHost"));
        System.out.println(System.getProperty("https.proxyPort"));
        proxy.setProxy(); //System.setProperty - ресет нужен блять РЕСЕТ

        System.out.println(System.getProperty("https.proxyHost"));
        System.out.println(System.getProperty("https.proxyPort"));
        Logger.logInfo("Connecting to gather link");
        webUtils.getTextCon(Connection.Method.POST,
                "https://iplogger.org/24cXc6",
                "https://google.com", true);

        System.out.println(System.getProperty("https.proxyHost"));
        System.out.println(System.getProperty("https.proxyPort"));
        proxy.resetProxy();

        webUtils.getTextCon(Connection.Method.POST,
                "https://iplogger.org/24cXc6",
                "https://google.com", true);

        System.out.println(System.getProperty("https.proxyHost"));
        System.out.println(System.getProperty("https.proxyPort"));

//        DataUtils dataUtils = new DataUtils();
//        DiscordAccountsManager discordAccountsManager = new DiscordAccountsManager();
//        String login = dataUtils.getDiscordLogin();
//        String password = dataUtils.getDiscordPassword();
//        if (!discordAccountsManager.createAccount(login, password))
//            return;
//        Thread.sleep(5000);
//        discordAccountsManager.verifyMail();
//        Thread.sleep(5000);
//        discordAccountsManager.verifyNumber();
       //fileUtils.fileWriter(mail + ":" + login + ":" + password);
    }
}