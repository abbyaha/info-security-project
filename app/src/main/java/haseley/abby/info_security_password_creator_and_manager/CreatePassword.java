package haseley.abby.info_security_password_creator_and_manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class CreatePassword extends AppCompatActivity {
    //Variables selected by user
    String numUpperCase;
    String numSpecial;
    String numNumbers;
    String passwordLength;
    //Generated on user request, saved with entry
    String sentence;
    String password;
    //Pointers to the regions on the screen
    EditText upperField;
    EditText specialField;
    EditText numsField;
    EditText lengthField;
    EditText sentenceField;
    //valid fields flag
    boolean valid = false;
    //Needed in order to add new password
    ArrayList<PasswordEntry> passwords = new ArrayList<>();
    private static final String CRYPT_KEY_NAME = "my_crypt_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

        //Button for saving the current password and sentence with the account name
        Button AcceptButton = (Button) findViewById(R.id.btnAccept);
        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Get the account to save
                EditText accountField = (EditText) findViewById(R.id.txtAccount);
                String accountName = accountField.getText().toString();
                //Get the sentence to save
                EditText sentenceField = (EditText) findViewById(R.id.txtSentence);
                String sentence = sentenceField.getText().toString();
                //Get the password to save
                TextView resultField = (TextView) findViewById(R.id.txtPassword);
                password = resultField.getText().toString();

                //Grab the current date info
                Calendar now = Calendar.getInstance();
                //Save date as string in the format Year.Month.Day.Hour.Minute.Second
                String date = "" + now.get(Calendar.YEAR) + "."
                        + now.get(Calendar.MONTH) + "."
                        + now.get(Calendar.DAY_OF_MONTH) + "."
                        + now.get(Calendar.HOUR_OF_DAY) + "."
                        + now.get(Calendar.MINUTE) + "."
                        + now.get(Calendar.SECOND);

                //Package the info in an entry
                PasswordEntry entry = new PasswordEntry(accountName, sentence, password, date);
                //Add the entry to the database
                addEntry(entry);

                Toast.makeText(getApplicationContext(), "Password Created", Toast.LENGTH_SHORT).show();
                goToPasswordViewer();
            }
        });

        //Button for generating a sentence and putting it in the sentence field
        final Button genSentence = (Button) findViewById(R.id.btnGenerateSentence);
        genSentence.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Log.d("Password creation --->", "Generating sentence");
                generateSentence();

            }
        });

        //Button for generating a password from the sentence and with the set parameters
        Button genPassword = (Button) findViewById(R.id.btnCreatePass);
        genPassword.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                upperField = (EditText) findViewById(R.id.txtNumUpperCase);
                numUpperCase = upperField.getText().toString();

                specialField = (EditText) findViewById(R.id.txtNumSpecialChars);
                numSpecial = specialField.getText().toString();

                numsField = (EditText) findViewById(R.id.txtNumNumbers);
                numNumbers = numsField.getText().toString();

                lengthField = (EditText) findViewById(R.id.txtPassLength);
                passwordLength = lengthField.getText().toString();

                sentenceField = (EditText) findViewById(R.id.txtSentence);
                sentence = sentenceField.getText().toString();

                valid = validate();

                if (valid) {
                    Log.d("Password creation --->", "Generating password");
                    generatePassword();
                }
            }
        });
    }

    //Adds a given password object to the saved list of passwords
    private void addEntry(PasswordEntry entry){
        getPasswordsFromFile();
        //Add to list
        passwords.add(entry);

        try {
            //Save list as encrypted file
            PasswordFile.encryptStore(getApplicationContext(), "WhiteWizard2", Arrays.copyOfRange(getKey(), 0, 16), passwords);
        } catch(Exception e){
            //Should not get here
            Log.e("Sore Passwords", "Was not able to write the entry to file");
        }
    }

    public byte[] getKey(){
        KeyStore mKeyStore;
        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
            mKeyStore.load(null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }

        Object secretKey = null;
        try {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) mKeyStore.getEntry(CRYPT_KEY_NAME, null);
            secretKey = secretKeyEntry.getSecretKey();
            Log.d("Key", Base64.encodeToString(convertToBytes(secretKey), Base64.DEFAULT));
            return convertToBytes(secretKey);
        }catch (Exception e){
            Log.e("getKey: ", Log.getStackTraceString(e));
            return null;
        }
    }
    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }

    //Gets the list of current passwords from file
    private void getPasswordsFromFile(){
        try {
            //Decrypt the password list
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard2",Arrays.copyOfRange(getKey(),0,16));
        } catch(Exception e){
            //Should not get here
        }
    }

    @Override
    public void onBackPressed() {
        //Allow user to navigate back to the view screen when Back is used within the app
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause(){
        //End this activity if the user leaves the app or this view
        super.onPause();
        finish();
    }

    public void generateSentence(){
        TextView sentenceField = (TextView) findViewById(R.id.txtSentence);
        sentence = GenerateSentence.createSentence();
        sentenceField.setText(sentence);
    }

    public boolean validate(){
        boolean test = true;
        boolean sentence_empty = false;
        boolean length_empty = false;
        String message = "Please ";

        if (sentence.matches("")) {
            test &= false;
            sentence_empty = true;
            message = message + "enter or generate a sentence ";
        }

        if (!sentence_empty) {
            if (passwordLength.matches("")) {
                test &= false;
                length_empty = true;
                message = message + "enter a password length, ";
            } else if (Integer.parseInt(passwordLength) > sentence.length()) {
                test &= false;
                message = message + "make the length shorter than the sentence, ";
            }
            if (!length_empty) {
                if (numUpperCase.matches("")) {
                    test &= false;
                    message = message + "enter number of capitals, ";
                } else if (Integer.parseInt(numUpperCase) > Integer.parseInt(passwordLength)) {
                    test &= false;
                    message = message + "make the number of capitals less than the length, ";
                }

                if (numSpecial.matches("")) {
                    test &= false;
                    message = message + "enter number of special characters, ";
                } else if (Integer.parseInt(numSpecial) > Integer.parseInt(passwordLength)) {
                    test &= false;
                    message = message + "make the number of special characters less than the length, ";
                }

                if (numNumbers.matches("")) {
                    test &= false;
                    message = message + "enter number of numbers ";
                }
            }
        }

        message = message + "and try again.";

        if (!test) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        return test;
    }

    public void generatePassword(){
        Log.d("Password creation --->","calling create password");
        password = GeneratePassword.createPassword(sentence, Integer.parseInt(passwordLength),
                Integer.parseInt(numUpperCase), Integer.parseInt(numNumbers), Integer.parseInt(numSpecial));
        Log.d("Password creation --->", password);
        TextView resultField = (TextView) findViewById(R.id.txtPassword);
        resultField.setText(password);
    }


    public void goToPasswordViewer(){
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
    }
}
