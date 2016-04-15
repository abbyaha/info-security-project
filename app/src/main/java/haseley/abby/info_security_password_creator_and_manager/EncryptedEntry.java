package haseley.abby.info_security_password_creator_and_manager;

import java.io.Serializable;

// Author: Daniele Bellutta
// April 2016

// This is a simple class meant to contain the encrypted data from PasswordEntry objects.
public final class EncryptedEntry implements Serializable {
    private static final int serialVersionUID = 9270384;

    private byte account[];
    private byte sentence[];
    private byte password[];
    private byte date[];

    // Constructor
    public EncryptedEntry(byte[] key, byte[] accountArray, byte[] sentenceArray, byte[] passwordArray, byte[] dateArray) {
        // Automatically encrypt all data that is put into this object.
        account = Crypto.encrypt(key, accountArray);
        sentence = Crypto.encrypt(key, sentenceArray);
        password = Crypto.encrypt(key, passwordArray);
        date = Crypto.encrypt(key, dateArray);
    }

    // Constructor
    public EncryptedEntry(byte[] key, String accountString, String sentenceString, String passwordString, String dateString) {
        this(key, accountString.getBytes(), sentenceString.getBytes(), passwordString.getBytes(), dateString.getBytes());
    }

    // Constructor
    public EncryptedEntry(byte[] key, PasswordEntry data) {
        this(key, data.getAccount(), data.getSentence(), data.getPassword(), data.getCreationDate());
    }

    public byte[] getAccount(byte[] key) {
        return Crypto.decrypt(key, account);
    }

    public byte[] getSentence(byte[] key) {
        return Crypto.decrypt(key, sentence);
    }

    public byte[] getPassword(byte[] key) {
        return Crypto.decrypt(key, password);
    }

    public byte[] getDate(byte[] key) {
        return Crypto.decrypt(key, date);
    }

    // This method decrypts this object into an unencrypted PasswordEntry object.
    public PasswordEntry decrypt(byte[] key) {
        return new PasswordEntry(new String(this.getAccount(key)), new String(this.getSentence(key)), new String(this.getPassword(key)), new String(this.getDate(key)));
    }
}
