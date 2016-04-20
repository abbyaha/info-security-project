package haseley.abby.info_security_password_creator_and_manager;

import java.util.Random;

public class GeneratePassword {

    static Random rn = new Random();
    static int words_index = 0;
    static int word_count = 0;
    static final String lower = "abcdefghijklmnopqrstuvwxyz";
    static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static String one_char_index = "";

    /**
     * This method takes the sentence and, according to the specifications given in the parameters,
     * creates an appropriate password for the user.
     *
     * @param sentence The sentence to use to create the new password
     * @param length   The length of the password without number
     * @param caps     How many capitals in the password
     * @param nums     How many numbers in the password
     * @param spec     How many special characters in the password
     * @return         A string that is the new password
     */
    public static String createPassword(String sentence, int length, int caps, int nums, int spec){

        //Get number of spaces to subtract them from the length of the sentence to get an accurate password length
        int counter = 0;
        for(int i = 0; i < sentence.length(); i++) {
            if(sentence.charAt(i) == ' ') {
                counter++;
            }
        }

        sentence = sentence.toLowerCase();

        //Make array of words
        String[] words = sentence.split(" ");
        int total = sentence.length() - counter;

        //Move through the sentence array start to finish and shorten each word, starting on the end
        //and moving to the start of each word to maintain some word integrity. It will then move to
        //deleting single letter words if it still needs to get the length requirement.
        String current_word = words[words_index];
        int i = 0;
        while (i < (total - length + nums)) {
            if (current_word.length() > 1) {
                words[words_index] = shorten(current_word);
                i++;
            } else if ((current_word.length() == 1) && (word_count != words.length)) {
                if (!one_char_index.contains(Integer.toString(words_index) + ",")) {
                    word_count++;
                    one_char_index = one_char_index + Integer.toString(words_index) + ",";
                }
            } else if (current_word.length() == 1) {
                words[words_index] = "";
                i++;
            }
            current_word = nextWord(words);
            if (current_word.equals("")) {
                current_word = nextWord(words);
            }
        }

        //Capitalizes random letters in random words in the sentence.
        i = 0;
        while (i < caps) {
            int rand = rn.nextInt(words.length);
            String word = words[rand];
            if (!word.equals("")) {
                if (!upper.contains(word)) {
                    words[rand] = cap(word);
                    i++;
                }
            }
        }

        //Make a string from the current array of words that have been shortened and capitalized.
        StringBuilder builder = new StringBuilder();
        for (String string : words) {
            builder.append(string);
        }
        String new_sent = builder.toString();

        //Change random characters that are not capitals to special characters.
        i = 0;
        while (i < spec) {
            int rand = rn.nextInt(new_sent.length());
            char change = new_sent.charAt(rand);
            if (lower.contains(Character.toString(change))){
                i++;
                change = addSpecial(change);
                new_sent = new_sent.substring(0, rand) + change +new_sent.substring(rand + 1);
            }
        }

        //Inserts the specified number of random numbers randomly into the password.
        for(int j = 0; j < nums; j++){
            int rand = rn.nextInt(9) + 1;
            int insert = rn.nextInt(new_sent.length() - 1);
            new_sent = new_sent.substring(0, insert) + Integer.toString(rand) + new_sent.substring(insert);
        }

        return new_sent;
    }

    /**
     * Capitalizes a random character in the given string.
     *
     * @param word The string to capitalize
     * @return     The given string with one character capitalized
     */
    public static String cap(String word){
        char change = ' ';
        int rand = -1;
        while (!lower.contains(Character.toString(change))) {
            rand = rn.nextInt(word.length());
            change = word.charAt(rand);
        }

        change = Character.toUpperCase(change);
        return word.substring(0, rand) + change + word.substring(rand + 1);
    }

    /**
     * Takes off the last character in the string.
     *
     * @param word  The string to shorten
     * @return      The shortened string
     */
    public static String shorten(String word){
        StringBuilder sb = new StringBuilder(word);

        sb.deleteCharAt(word.length() - 1);

        return sb.toString();
    }

    /**
     * Iterates through the given array.
     *
     * @param words Array of strings to move through
     * @return      A string in the array
     */
    public static String nextWord(String[] words){

        if (words_index < words.length - 1) {
            words_index++;
        } else {
            words_index = 0;
        }

        return words[words_index];
    }

    /**
     * Changes a letter character into a special character.
     *
     * @param change The character to change
     * @return       The modified character
     */
    public static char addSpecial(char change){
        switch (change) {
            case 'a' :
                change = '@';
                break;
            case 'b' :
                change = '&';
                break;
            case 'c' :
                change = '(';
                break;
            case 'd' :
                change = ')';
                break;
            case 'e' :
                change = '>';
                break;
            case 'f' :
                change = '?';
                break;
            case 'g' :
                change = '/';
                break;
            case 'h' :
                change = '|';
                break;
            case 'i' :
                change = '!';
                break;
            case 'j' :
                change = '%';
                break;
            case 'k' :
                change = '<';
                break;
            case 'l' :
                change = '\\';
                break;
            case 'm' :
                change = '=';
                break;
            case 'n' :
                change = '`';
                break;
            case 'o' :
                change = '.';
                break;
            case 'p' :
                change = ';';
                break;
            case 'q' :
                change = '_';
                break;
            case 'r' :
                change = '-';
                break;
            case 's' :
                change = '~';
                break;
            case 't' :
                change = '+';
                break;
            case 'u' :
                change = ',';
                break;
            case 'v' :
                change = '[';
                break;
            case 'w' :
                change = ']';
                break;
            case 'x' :
                change = '{';
                break;
            case 'y' :
                change = '}';
                break;
        }

        return change;
    }

}
