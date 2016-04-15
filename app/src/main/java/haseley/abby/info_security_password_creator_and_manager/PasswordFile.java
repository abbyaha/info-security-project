package haseley.abby.info_security_password_creator_and_manager;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

// Author: Daniele Bellutta
// April 2016

public final class PasswordFile {

    // This is a private constructor to prevent the class from being instantiated.
    private PasswordFile() {
        // Do nothing.
    }

    // This method takes an ArrayList of data and writes it to the file with the specified name
    // after encrypting it with the specified key.
    private static void writeToFile(Context context, String fileName, byte[] key, ArrayList<PasswordEntry> data) throws IOException {
        ObjectOutputStream output = new ObjectOutputStream(new BufferedOutputStream(context.openFileOutput(fileName, Context.MODE_PRIVATE)));

        // Loop through all of the elements in the ArrayList and write them to the file.
        for (int i = 0; i < data.size(); i++) {
            // Encrypt the current object.
            EncryptedEntry current = new EncryptedEntry(key, data.get(i));
            // Write the current object to the file.
            output.writeObject(current);
        }

        // Make sure to close the output stream!
        output.close();
    }

    // This is a simple wrapper method.
    public static void encryptStore(Context context, String file, byte[] key, ArrayList<PasswordEntry> data) throws IOException {
        // Write the data to the file.
        writeToFile(context, file, key, data);
    }

    // This is an alias for the other encryptStore() method that takes a String for the key instead
    // of an array of bytes.
    public static void encryptStore(Context context, String file, String key, ArrayList<PasswordEntry> data) throws IOException {
        encryptStore(context, file, key.getBytes(), data);
    }

    private static ArrayList<PasswordEntry> readFromFile(Context context, String fileName, byte[] key) throws IOException, ClassNotFoundException {
        // Initialize an empty ArrayList to hold the data later on.
        ArrayList<PasswordEntry> data = new ArrayList<PasswordEntry>();

        ObjectInputStream input = new ObjectInputStream(new BufferedInputStream(context.openFileInput(fileName)));

        try {
            // Continuously try to read an object from the file until the end of the file is reached
            // (and an EOFException is thrown).
            while(true) {
                // Read the next object from the file.
                EncryptedEntry nextEntry = (EncryptedEntry) (input.readObject());
                // Decrypt the object and add it to the ArrayList to be returned.
                data.add(nextEntry.decrypt(key));
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

    // This is a simple wrapper method.
    public static ArrayList<PasswordEntry> decryptStore(Context context, String file, byte[] key) throws IOException, ClassNotFoundException {
        // Read from the file and return the data.
        return readFromFile(context, file, key);
    }

    // This is an alias for the other decryptStore() method that takes a String for the key instead
    // of an array of bytes.
    public static ArrayList<PasswordEntry> decryptStore(Context context, String file, String key) throws IOException, ClassNotFoundException {
        return decryptStore(context, file, key.getBytes());
    }
}
