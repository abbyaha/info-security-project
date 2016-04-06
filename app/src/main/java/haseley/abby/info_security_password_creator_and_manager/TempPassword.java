package haseley.abby.info_security_password_creator_and_manager;

/**
 * Created by Jewel on 4/6/2016.
 */
public class TempPassword {

    private String account;
    private String sentence;
    private String password;

    public TempPassword(){
        super();
    }

    public TempPassword(String account, String sentence, String password) {
        super();
        this.account = account;
        this.sentence = sentence;
        this.password = password;
    }

    @Override
    public String toString() {
        return "Account : " + this.account;
    }
}
