package haseley.abby.info_security_password_creator_and_manager;

import java.io.Serializable;

/**
 * Created by Daniele on 4/9/16.
 */
public final class EncryptedEntry implements Serializable {
    private static final int serialVersionUID = 9270384;

    private byte account[];
    private byte sentence[];
    private byte password[];

    public EncryptedEntry(byte[] key, byte[] accountArray, byte[] sentenceArray, byte[] passwordArray) {
        account = Crypto.encrypt(key, accountArray);
        sentence = Crypto.encrypt(key, sentenceArray);
        password = Crypto.encrypt(key, passwordArray);
    }

    public EncryptedEntry(byte[] key, String accountString, String sentenceString, String passwordString) {
        this(key, accountString.getBytes(), sentenceString.getBytes(), passwordString.getBytes());
    }

    public EncryptedEntry(byte[] key, PasswordEntry data) {
        this(key, data.getAccount(), data.getSentence(), data.getPassword());
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

    public PasswordEntry decrypt(byte[] key) {
        return new PasswordEntry(new String(this.getAccount(key)), new String(this.getSentence(key)), new String(this.getPassword(key)));
    }
}
