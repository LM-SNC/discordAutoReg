package Core;

import Data.CONSTANTS;
import Utils.DataUtils;
import Utils.FileUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CONSTANTS.setDiscordModel();
        CONSTANTS.setGmailnatorModel();

        DataUtils dataUtils = new DataUtils();

        DiscordAccountsManager discordAccountsManager = new DiscordAccountsManager();


        String login = dataUtils.getDiscordLogin();
        String password = dataUtils.getDiscordPassword();

        if (!discordAccountsManager.createAccount(login, password))
            return;
        Thread.sleep(5000);
        discordAccountsManager.verifyMail();
        Thread.sleep(5000);
        discordAccountsManager.verifyNumber();

       //fileUtils.fileWriter(mail + ":" + login + ":" + password);
    }
}
