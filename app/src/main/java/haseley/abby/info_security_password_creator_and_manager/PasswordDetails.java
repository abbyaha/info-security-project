package haseley.abby.info_security_password_creator_and_manager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PasswordDetails extends AppCompatActivity {

    List<PasswordEntry> passwords = new ArrayList<>();
    private static final String CRYPT_KEY_NAME = "my_crypt_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_details);

        final Bundle bundle = getIntent().getExtras();

        //Unpack the 4 values from the bundle and display them

        TextView accountText = (TextView)findViewById(R.id.txtAccount);
        accountText.setText(bundle.getString("Account"));

        TextView sentenceText = (TextView)findViewById(R.id.txtSentence);
        sentenceText.setText(bundle.getString("Sentence"));

        final TextView passwordText = (TextView)findViewById(R.id.txtPass);
        passwordText.setText(bundle.getString("Password"));

        TextView ageText = (TextView)findViewById(R.id.txtAge);
        ageText.setText(bundle.getString("Age"));

        final Button copyPass = (Button) findViewById(R.id.btnCopy);
        copyPass.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Log.d("Password details --->", "Copying");
                copyPassword(bundle.getString("Password"));

            }
        });

        final Button delPass = (Button) findViewById(R.id.btnDelete);
        delPass.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Log.d("Password details --->", "Deleting");
                deletePassword(bundle.getString("Password"));

            }
        });
    }

    @Override
    public void onBackPressed() {
        //Allow the user to navigate back to the password viewer from within the app
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause(){
        //End this activity if the user leaves the app or view
        super.onPause();
        finish();
    }

    @SuppressWarnings("deprecation")
    public void copyPassword(String password){
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipboard.setText(password);
        Toast.makeText(getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();

    }

    public void deletePassword(String password){
        getPasswordsFromFile();
        PasswordEntry result = null;

        int p_index = getIndexByPassword(password);
        passwords.remove(p_index);
        /*
        try {
            PasswordFile.encryptStore(getApplicationContext(), "WhiteWizard2", getKey(), (ArrayList<PasswordEntry>) passwords);
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        Toast.makeText(getApplicationContext(), "Password deleted", Toast.LENGTH_LONG).show();
        goToPasswordViewer();
    }

    private void getPasswordsFromFile(){
        try {
            //Decrypt password list
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard2",  Arrays.copyOfRange(getKey(), 0, 16));
        } catch(Exception e){
            //Shouldn't get here
            Log.e("Reading Passwords", "Could not read passwords from file");
        }
    }

    public int getIndexByPassword(String pass)
    {
        for(PasswordEntry p : passwords)
        {
            if(p.getPassword().equals(pass))
                return passwords.indexOf(p);
        }
        return -1;
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
            Log.d("Key in get key: ", Base64.encodeToString(convertToBytes(secretKey), Base64.DEFAULT));

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

    public void goToPasswordViewer(){
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
    }
}
