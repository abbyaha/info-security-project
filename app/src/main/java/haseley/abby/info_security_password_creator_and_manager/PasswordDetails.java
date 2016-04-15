package haseley.abby.info_security_password_creator_and_manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class PasswordDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_details);

        Bundle bundle = getIntent().getExtras();

        //Unpack the 4 values from the bundle and display them

        TextView accountText = (TextView)findViewById(R.id.txtAccount);
        accountText.setText(bundle.getString("Account"));

        TextView sentenceText = (TextView)findViewById(R.id.txtSentence);
        sentenceText.setText(bundle.getString("Sentence"));

        TextView passwordText = (TextView)findViewById(R.id.txtPass);
        passwordText.setText(bundle.getString("Password"));

        TextView ageText = (TextView)findViewById(R.id.txtAge);
        ageText.setText(bundle.getString("Age"));
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

    public void copyPassword(){

    }

    public void deletePassword(){

    }
}
