import java.util.Random;
import java.util.SplittableRandom;

public class DataUtils {
    private final int[] min = {1970, 1, 1};
    private final int[] max = {2002, 12, 28};

    private SplittableRandom splittableRandom;
    DataUtils() {
        splittableRandom = new SplittableRandom();
    }

    private int generateNumber(int min, int max) {
        return splittableRandom.nextInt(min, max);
    }

    private char generateSymbol() {
        Random r = new Random();
        return (char)(r.nextInt(26) + 'a');
    }

    public String getDiscordBirthDay() {
        StringBuilder birthDay = new StringBuilder();
        for (int i = 0; i < 3; i ++) {
            int numb = generateNumber(min[i], max[i]);
            if (numb < 10)
                birthDay.append("0").append(numb).append("-");
            else
                birthDay.append(numb).append("-");
        }
        return birthDay.substring(0, birthDay.length()-1);
    }

    public String getDiscordLogin() {
        String login = "";
        for(int i = 0; i < generateNumber(7, 12); i++) {
            login += generateSymbol();
        }
        return login + generateNumber(0, 99);
    }

    public String getDiscordPassword () {
        String password = "";
        for(int i = 0; i < generateNumber(8, 14); i++) {
            if (generateNumber(0, 3) > 0)
                password += generateSymbol();
            else
                password += generateNumber(0, 9);
        }
        return password;
    }
}
