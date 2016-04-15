package haseley.abby.info_security_password_creator_and_manager;

import java.util.Random;

/**
 * Created by Abby on 4/12/16.
 * Generates a sentence to use to create a password.
 */
public class GenerateSentence {

    static Random rn = new Random();

    public static String createSentence(){
        String sentence = "";
        String proper_noun = "";
        String common_noun = "";
        String verb = "";
        String adverb = "";
        String adj = "";
        String art = "";
        String conj = "";
        String prep = "";
        String plur = "is";

        int choose = rn.nextInt(4) + 1;
        //choose = 1;

        switch (choose) {
            case 1 :
                //abby loves to see buildings
                proper_noun = Words.pNoun();
                verb = Words.verb();
                if (verb.charAt(verb.length() - 1) != 's') {
                    verb = verb + "s";
                }
                String verb2 = Words.verb();
                if (verb2.charAt(verb2.length() - 1) == 's') {
                    verb2 = verb2.substring(0, verb2.length()-1);
                }
                common_noun = Words.cNoun();
                if (common_noun.charAt(common_noun.length() - 1) != 's') {
                    common_noun = common_noun + "s";
                }

                sentence = proper_noun + " " + verb + " to " + verb2 + " " + common_noun;
                break;
            case 2 :
                //the dog is very nice
                art = Words.article();
                common_noun = Words.cNoun();
                if (common_noun.charAt(common_noun.length() - 1) == 's') {
                    plur = "are";
                }
                adverb = Words.adverb();
                adj = Words.adjective();

                sentence = art + " " + common_noun + " " + plur + " " + adverb + " " + adj;
                break;
            case 3 :
                //however the phone is quite good
                conj = Words.conjunction();
                art = Words.article();
                common_noun = Words.cNoun();
                if (common_noun.charAt(common_noun.length() - 1) == 's') {
                    plur = "are";
                }
                adverb = Words.adverb();
                adj = Words.adjective();

                sentence = conj + " the " + common_noun + " " + plur + " " + adverb + " " + adj;
                break;
            case 4 :
                //
                proper_noun = Words.pNoun();
                String prop_noun2 = Words.pNoun();
                verb = Words.verb();
                if (verb.charAt(verb.length() - 1) == 's') {
                    verb = verb.substring(0, verb.length()-1);
                }
                String verb_2 = Words.verb();
                if (verb_2.charAt(verb_2.length() - 1) == 's') {
                    verb_2 = verb_2.substring(0, verb_2.length()-1);
                }

                sentence = proper_noun + " and " + prop_noun2 + " " + verb + " to " + verb_2;
                break;
            default:
                sentence = "this app is the coolest";
                break;
        }

        return sentence;
    }

}
