package haseley.abby.info_security_password_creator_and_manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreatePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);


        Button GenerateButton = (Button) findViewById(R.id.btnCreatePass);
        GenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePassword();
            }
        });

        Button AcceptButton = (Button) findViewById(R.id.btnAccept);
        AcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText accountField = (EditText) findViewById(R.id.txtAccount);
                String accountName = accountField.getText().toString();
                //TODO: Add password/account to the database

                Toast.makeText(getApplicationContext(), "Password Created", Toast.LENGTH_SHORT).show();
                //TODO: Go back to list
            }
        });
    }

    private  void makePassword(){
        EditText upperField = (EditText) findViewById(R.id.txtNumUpperCase);
        int numUpperCase = Integer.parseInt(upperField.getText().toString());

        EditText lowerField = (EditText) findViewById(R.id.txtNumLowerCase);
        int numLowerCase = Integer.parseInt(lowerField.getText().toString());

        EditText numsField = (EditText) findViewById(R.id.txtNumNumbers);
        int numNumbers = Integer.parseInt(numsField.getText().toString());

        EditText lengthField = (EditText) findViewById(R.id.txtPassLength);
        int passwordLength = Integer.parseInt(lengthField.getText().toString());

        EditText sentenceField = (EditText) findViewById(R.id.txtSentence);
        String sentence = sentenceField.getText().toString();

        //TODO: Generate a Password from input

        TextView resultField = (TextView) findViewById(R.id.txtPassword);
        //TODO: Put the generated password here
        resultField.setText("Put a password here");
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}
