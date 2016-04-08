package haseley.abby.info_security_password_creator_and_manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreatePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_password);


        Button CreateButton = (Button) findViewById(R.id.btnCreatePass);
        CreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePassword();
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
        //TODO: Add password to the database
    }
}
