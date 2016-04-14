package haseley.abby.info_security_password_creator_and_manager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class CreatePassword extends AppCompatActivity {

    int numUpperCase;
    int numSpecial;
    int numNumbers;
    int passwordLength;
    String sentence;
    String password;
    EditText upperField;
    EditText specialField;
    EditText numsField;
    EditText lengthField;
    EditText sentenceField;
    private static final String CRYPT_KEY_NAME = "my_crypt_key";

    ArrayList<PasswordEntry> passwords = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);

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
                //Package the info in an entry
                PasswordEntry entry = new PasswordEntry(accountName, sentence, password);
                //Add the entry to the database
                addEntry(entry);

                Toast.makeText(getApplicationContext(), "Password Created", Toast.LENGTH_SHORT).show();
                //TODO: Go back to list
            }
        });

        final Button genSentence = (Button) findViewById(R.id.btnGenerateSentence);
        genSentence.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Log.d("Password creation --->","Generating sentence");
                generateSentence();

            }
        });

        Button genPassword = (Button) findViewById(R.id.btnCreatePass);
        genPassword.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Log.d("Password creation --->","Generating password");
                generatePassword();

            }
        });
    }

    private void addEntry(PasswordEntry entry){
        getPasswordsFromFile();

        passwords.add(entry);

        try {
            PasswordFile.encryptStore(getApplicationContext(), "WhiteWizard", Arrays.copyOfRange(getKey(),0,16), passwords);
        } catch(Exception e){
            //TODO: Uh?
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
            Log.d("Key", Base64.encodeToString(convertToBytes(secretKey),Base64.DEFAULT));
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
    private void getPasswordsFromFile(){
        try {
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard", Arrays.copyOfRange(getKey(),0,16));
        } catch(Exception e){
            //TODO: Uh?
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }

    public void generateSentence(){
        TextView sentenceField = (TextView) findViewById(R.id.txtSentence);
        sentence = GenerateSentence.createSentence();
        sentenceField.setText(sentence);
    }

    public void generatePassword(){
        upperField = (EditText) findViewById(R.id.txtNumUpperCase);
        numUpperCase = Integer.parseInt(upperField.getText().toString());

        specialField = (EditText) findViewById(R.id.txtNumSpecialChars);
        numSpecial = Integer.parseInt(specialField.getText().toString());

        numsField = (EditText) findViewById(R.id.txtNumNumbers);
        numNumbers = Integer.parseInt(numsField.getText().toString());

        lengthField = (EditText) findViewById(R.id.txtPassLength);
        passwordLength = Integer.parseInt(lengthField.getText().toString());

        sentenceField = (EditText) findViewById(R.id.txtSentence);
        sentence = sentenceField.getText().toString();

        Log.d("Password creation --->","calling create password");
        password = GeneratePassword.createPassword(sentence, passwordLength, numUpperCase, numSpecial, numNumbers);
        Log.d("Password creation --->", password);
        TextView resultField = (TextView) findViewById(R.id.txtPassword);
        resultField.setText(password);
    }
}
