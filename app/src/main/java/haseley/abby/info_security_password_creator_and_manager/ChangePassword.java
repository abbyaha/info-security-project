package haseley.abby.info_security_password_creator_and_manager;

import android.app.Fragment;
import android.view.View;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.SecretKey;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import javax.inject.Inject;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by Michael on 4/14/2016.
 */
public class ChangePassword extends Fragment {
    SharedPreferences mSharedPreferences;

    public ChangePassword() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_change_login, container, false);
        Button ChangeButton = (Button) rootView.findViewById(R.id.change_button);
        ChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
                Log.d("Change", "Change password");

            }
        });
        return rootView;
    }

    public void change()
    {
        EditText pass = (EditText)getActivity().findViewById(R.id.password);
        String password = pass.getText().toString();
        //mSharedPreferences.edit().putString("stored_key", password).apply();
        Log.d("Password", "Password is: " + password);
        getActivity().finish();

    }


}



