package haseley.abby.info_security_password_creator_and_manager;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

// Author: Daniele Bellutta
// March 2016

public final class PasswordFile {

    // ECB mode was chosen so that an IV is not necessary when encrypting or decrypting.
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";
    private static final String ALGORITHMTYPE = "AES";

    // This is a private constructor to prevent the class from being instantiated.
    private PasswordFile() {
        // Do nothing.
    }

    // This method takes an ArrayList of data and writes it to the file with the specified name. It
    // requires a Cipher object initialized with the appropriate key in order to perform the
    // encryption.
    private static void writeToFile(Context context, String fileName, Cipher cipher, ArrayList<PasswordEntry> data) throws IOException {
        // Open the file buffer. Note that this is a CipherOutputStream wrapped in an
        // ObjectOutputStream, so everything will indeed be encrypted.
        ObjectOutputStream output = new ObjectOutputStream(new CipherOutputStream(new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)), cipher));

        // Loop through all of the elements in the ArrayList and write them to the file.
        for (int i = 0; i < data.size(); i++) {
            // Write the current object to the file.
            output.writeObject(data.get(i));
        }

        // Make sure to close the output stream!
        output.close();
    }

    // This method takes an ArrayList of data, encrypts it using the specified key, and writes it to
    // the file with the specified name.
    public static void encryptStore(Context context, String file, byte[] key, ArrayList<PasswordEntry> data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        // Create the necessary Cipher object based on the key.
        SecretKeySpec sks = new SecretKeySpec(key, ALGORITHMTYPE);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        // Set the Cipher to be in encryption mode.
        cipher.init(Cipher.ENCRYPT_MODE, sks);

        // Write the data to the file.
        writeToFile(context, file, cipher, data);
    }

    // This is an alias for the other encryptStore() method that takes a String for the key instead
    // of an array of bytes.
    public static void encryptStore(Context context, String file, String key, ArrayList<PasswordEntry> data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        encryptStore(context, file, key.getBytes(), data);
    }

    // This method reads all the PasswordEntry objects from the file with the specified name and
    // returns them in an ArrayList. It requires a Cipher object initialized with the appropriate
    // key to do the decryption.
    private static ArrayList<PasswordEntry> readFromFile(Context context, String fileName, Cipher cipher) throws IOException, ClassNotFoundException {
        // Initialize an empty ArrayList to hold the data later on.
        ArrayList<PasswordEntry> data = new ArrayList<PasswordEntry>();
        // Open the file buffer. Note that this is a CipherInputStream wrapped in an
        // ObjectInputStream, so the decryption will happen automatically.
        ObjectInputStream input = new ObjectInputStream(new CipherInputStream(new BufferedInputStream(context.openFileInput(fileName)), cipher));

        try {
            // Continuously try to read an object from the file until the end of the file is reached
            // (and an EOFException is thrown).
            while(true) {
                // Read the next object from the file.
                PasswordEntry nextEntry = (PasswordEntry) (input.readObject());
                // Add the object to the ArrayList to be returned.
                data.add(nextEntry);
            }
        } catch (EOFException e) {
            // Catch the EOFException when the end of the file is reached. Nothing needs to be done
            // here.
        }

        // Make sure to close the input stream!
        input.close();
        // Return the ArrayList full of data.
        return data;
    }

    // This method reads all the PasswordEntry objects from the file with the specified name,
    // decrypts them using the specified key, and returns them in an ArrayList.
    public static ArrayList<PasswordEntry> decryptStore(Context context, String file, byte[] key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException {
        // Create the necessary Cipher object based on the key.
        SecretKeySpec sks = new SecretKeySpec(key, ALGORITHMTYPE);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        // Set the Cipher to be in decryption mode.
        cipher.init(Cipher.DECRYPT_MODE, sks);

        // Read from the file and return the data.
        return readFromFile(context, file, cipher);
    }

    // This is an alias for the other decryptStore() method that takes a String for the key instead
    // of an array of bytes.
    public static ArrayList<PasswordEntry> decryptStore(Context context, String file, String key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException {
        return decryptStore(context, file, key.getBytes());
    }
}
