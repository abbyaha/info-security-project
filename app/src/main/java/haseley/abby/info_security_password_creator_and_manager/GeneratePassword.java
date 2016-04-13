package haseley.abby.info_security_password_creator_and_manager;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;
import java.lang.Character;
import java.lang.StringBuilder;

/**
 * Created by Abby on 4/12/16.
 * Generates a password from given criteria
 */
public class GeneratePassword {

    static Random rn = new Random();
    static final String lowercase = "abcdefghijklmnopqrstuvwxyz";

    public static String createPassword(String sentence, int passLength, int numCap, int numSpecial, int numNumbers){
        String password = "th!z1s_@tsTPwrd";
        String temp_sent = sentence;

        for (int i = 0; i < (sentence.length() - passLength); i++) {
            password = shorten(temp_sent);
            temp_sent = password;
        }

        for (int i = 0; i < numCap; i++) {
            password = capitalize(temp_sent);
            temp_sent = password;
        }

        return password;
    }

    public static String capitalize(String temp_pass){
        String pass = "";
        char change = ' ';
        int rand = -1;
        while (!lowercase.contains(Character.toString(change))) {
            rand = rn.nextInt(temp_pass.length());
            change = temp_pass.charAt(rand);
        }

        change = Character.toUpperCase(change);
        pass = temp_pass.substring(0, rand) + change + temp_pass.substring(rand + 1);

        return pass;
    }

    public static String shorten(String temp_pass){
        StringBuilder sb = new StringBuilder(temp_pass);
        int rand = rn.nextInt(temp_pass.length());

        sb.deleteCharAt(rand);

        return sb.toString();
    }


}
