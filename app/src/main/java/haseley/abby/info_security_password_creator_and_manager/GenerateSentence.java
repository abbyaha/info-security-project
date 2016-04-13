package haseley.abby.info_security_password_creator_and_manager;

import java.util.Random;

/**
 * Created by Abby on 4/12/16.
 * Generates a sentence to use to create a password.
 */
public class GenerateSentence {

    static String[] conjunction = {"and", "or", "but", "because", "for", "nor", "so", "yet", "after", "even if"};
    static String[] proper_noun = {"Joanna", "Abby", "Jamie", "Michael", "Daniele", "Stephen", "Jeffery"};
    static String[] common_noun = {"man", "woman", "fish", "elephant", "dog", "person"};
    static String[] determiner = {"a", "the", "every", "some"};
    static String[] adjective = {"big", "tiny", "pretty", "bald"};
    static String[] intransitive_verb = {"runs", "jumps", "talks", "sleeps"};
    static String[] transitive_verb = {"loves", "hates", "sees", "knows", "looks for", "finds"};
    static String[] adverb = {"quickly", "uneasily", "firmly", "abruptly", "wearily", "kindly"};
    static String[] possessive = {"his", "hers", "theirs"};
    static String[] infinitive = {"to walk", "to come over", "to go", "to cry", "to realize"};
    static String[] verb_array = {"runs", "walks", "sits", "cries", "dances"};
    static String[] movement = {"to", "from", "near", "back to", "away from", "over"};

    static Random rn = new Random();

    public static String createSentence(){
        String sentence = "";

        int sentence_type = rn.nextInt(1) + 1;

        switch (sentence_type) {
            case 1 :
                sentence = getProperNoun() + " " + getAdverb() + " " + getVerb() + " " +
                        getPlace() + " " + getDeterminer() + " " + getCommonNoun();
                break;
        }

        return sentence;
    }

    public static String getCommonNoun(){
        String noun;

        int new_rand = rn.nextInt(common_noun.length);
        noun = common_noun[new_rand];

        return noun;
    }

    public static String getProperNoun(){
        String noun;

        int new_rand = rn.nextInt(proper_noun.length);
        noun = proper_noun[new_rand];

        return noun;
    }

    public static String getConjunction(){
        String conj;

        int rand = rn.nextInt(conjunction.length);
        conj = conjunction[rand];

        return conj;
    }

    public static String getDeterminer(){
        String det;

        int rand = rn.nextInt(determiner.length);
        det = determiner[rand];

        return det;
    }

    public static String getAdjective(){
        String adj;

        int rand = rn.nextInt(adjective.length);
        adj = adjective[rand];

        return adj;
    }

    public static String getInVerb(){
        String verb;

        int rand = rn.nextInt(intransitive_verb.length);
        verb = intransitive_verb[rand];

        return verb;
    }

    public static String getTVerb(){
        String verb;

        int rand = rn.nextInt(transitive_verb.length);
        verb = transitive_verb[rand];

        return verb;
    }

    public static String getAdverb(){
        String a_verb;

        int rand = rn.nextInt(adverb.length);
        a_verb = adverb[rand];

        return a_verb;
    }

    public static String getInfinitive(){
        String inf;

        int rand = rn.nextInt(infinitive.length);
        inf = infinitive[rand];

        return inf;
    }

    public static String getVerb(){
        String verb;

        int rand = rn.nextInt(verb_array.length);
        verb = verb_array[rand];

        return verb;
    }

    public static String getPlace(){
        String place;

        int rand = rn.nextInt(movement.length);
        place = movement[rand];

        return place;
    }

}
