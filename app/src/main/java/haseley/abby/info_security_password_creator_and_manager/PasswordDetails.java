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

        //TODO: Get the password object and its details from the bundle
        Bundle bundle = getIntent().getExtras();

        TextView accountText = (TextView)findViewById(R.id.txtAccount);
        //accountText.setText("New Account");
        accountText.setText(bundle.getString("Account"));

        TextView sentenceText = (TextView)findViewById(R.id.txtSentence);
        //sentenceText.setText("Put a sentence here.");
        sentenceText.setText(bundle.getString("Sentence"));

        TextView passwordText = (TextView)findViewById(R.id.txtPass);
        //sentenceText.setText("P@$$W0RD");
        passwordText.setText(bundle.getString("Password"));
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
}
