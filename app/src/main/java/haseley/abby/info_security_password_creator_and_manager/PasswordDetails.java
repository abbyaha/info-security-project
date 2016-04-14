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
