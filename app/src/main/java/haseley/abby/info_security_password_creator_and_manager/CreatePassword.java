package haseley.abby.info_security_password_creator_and_manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;

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

                //Grab the current date info
                Calendar now = Calendar.getInstance();
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
            PasswordFile.encryptStore(getApplicationContext(), "WhiteWizard2", "MyDifficultPassw", passwords);
        } catch(Exception e){
            //TODO: Uh?
        }
    }

    private void getPasswordsFromFile(){
        try {
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard2", "MyDifficultPassw");
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
