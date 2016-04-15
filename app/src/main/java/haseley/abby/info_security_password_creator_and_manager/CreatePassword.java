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
    //Variables selected by user
    int numUpperCase;
    int numSpecial;
    int numNumbers;
    int passwordLength;
    //Generated on user request, saved with entry
    String sentence;
    String password;
    //Pointers to the regions on the screen
    EditText upperField;
    EditText specialField;
    EditText numsField;
    EditText lengthField;
    EditText sentenceField;
    //Needed in order to add new password
    ArrayList<PasswordEntry> passwords = new ArrayList<>();
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

                Log.d("Password creation --->","Generating sentence");
                generateSentence();

            }
        });

        //Button for generating a password from the sentence and with the set parameters
        Button genPassword = (Button) findViewById(R.id.btnCreatePass);
        genPassword.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Log.d("Password creation --->","Generating password");
                generatePassword();

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
            PasswordFile.encryptStore(getApplicationContext(), "WhiteWizard2", "MyDifficultPassw", passwords);
        } catch(Exception e){
            //Should not get here
        }
    }

    //Gets the list of current passwords from file
    private void getPasswordsFromFile(){
        try {
            //Decrypt the password list
            passwords = PasswordFile.decryptStore(getApplicationContext(), "WhiteWizard2", "MyDifficultPassw");
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
        password = GeneratePassword.createPassword(sentence, passwordLength, numUpperCase, numNumbers, numSpecial);
        Log.d("Password creation --->", password);
        TextView resultField = (TextView) findViewById(R.id.txtPassword);
        resultField.setText(password);
    }

    public void goToPasswordViewer(){
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
    }
}
