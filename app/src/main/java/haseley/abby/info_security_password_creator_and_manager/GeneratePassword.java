package haseley.abby.info_security_password_creator_and_manager;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Abby on 4/12/16.
 * Generates a password from given criteria
 */
public class GeneratePassword {

    static Random rn = new Random();
    static final char[] lowercase = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    static final char[] uppercase = {'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public static String createPassword(String sentence, int passLength, int numCap, int numSpecial, int numNumbers){
        String password = "th!z1s_@tsTPwrd";


        return password;
    }

    public static String capitalize(String temp_pass){
        String pass = "";
        char change = ' ';
        int rand = -1;
        while (!Arrays.asList(lowercase).contains(change)) {
            rand = rn.nextInt(temp_pass.length());
            change = temp_pass.charAt(rand);
        }

        change = Character.toUpperCase(change);
        pass = temp_pass.substring(0, rand) + change + temp_pass.substring(rand + 1);

        return pass;
    }

}
