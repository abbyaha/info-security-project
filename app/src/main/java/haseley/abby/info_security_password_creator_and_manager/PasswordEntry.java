package haseley.abby.info_security_password_creator_and_manager;

import java.io.Serializable;

/**
 * Created by Daniele on 3/14/16.
 */
public final class PasswordEntry implements Serializable {
    private static final int serialVersionUID = 4654897;

    private String account;
    private String sentence;
    private String password;

    public PasswordEntry(String accountString, String sentenceString, String passwordString) {
        account = accountString;
        sentence = sentenceString;
        password = passwordString;
    }

    public void setAccount(String newAccountName) {
        account = newAccountName;
    }

    public void setSentence(String newSentence) {
        sentence = newSentence;
    }

    public void setPassword(String newPassword) {
        password = newPassword;
    }

    public String getAccount() {
        return account;
    }

    public String getSentence() {
        return sentence;
    }

    public String getPassword() {
        return password;
    }
    @Override
    public String toString() {
        return "Account : " + this.account;
    }
}
