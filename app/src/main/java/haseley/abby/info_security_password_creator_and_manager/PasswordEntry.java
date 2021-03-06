package haseley.abby.info_security_password_creator_and_manager;

import java.io.Serializable;

// Author: Daniele Bellutta
// March 2016

// This is a simple class for storing all the various pieces of information associated with a
// password, such as the name of the account with which it is associated and the sentence that was
// used to create it.
public final class PasswordEntry implements Serializable {
    private static final int serialVersionUID = 4654897;

    private String account;
    private String sentence;
    private String password;
    private String creationDate;

    // Constructor
    public PasswordEntry(String accountString, String sentenceString, String passwordString, String creationDate) {
        account = accountString;
        sentence = sentenceString;
        password = passwordString;
        this.creationDate = creationDate;
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

    public  void setCreationDate(String newDate) {creationDate = newDate;}

    public String getAccount() {
        return account;
    }

    public String getSentence() {
        return sentence;
    }

    public String getPassword() {
        return password;
    }

    public String getCreationDate(){return creationDate;}

    @Override
    public String toString() {
        return "Account : " + this.account;
    }
}
