package haseley.abby.info_security_password_creator_and_manager;

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

public class LogIn extends AppCompatActivity {
    private Button LoginButton;
    private Button FingerPrintButton;

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";

    /** Alias for our key in the Android Key Store */
    private static final String FINGER_KEY_NAME = "my_finger_key";
    private static final String CRYPT_KEY_NAME = "my_crypt_key";

    @Inject KeyguardManager mKeyguardManager;
    @Inject FingerprintManager mFingerprintManager;
    @Inject FingerprintAuthenticationDialogFragment mFragment;
    @Inject KeyStore mKeyStore;
    @Inject KeyGenerator mKeyGenerator;
    @Inject Cipher mCipher;
    @Inject SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Login", "Entered onCreate");
        ((InjectedApplication) getApplication()).inject(this);
        setContentView(R.layout.activity_log_in);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("Login", "About to make buttons");
        LoginButton = (Button) findViewById(R.id.btnLogIn);
        LoginButton.setVisibility(View.GONE);
        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: This button may be removed, the other button handles all cases. This button is just to bypass authentication
                Log.d("Login", "Entered Login's onClick");
                goToPasswordView();
            }
        });
        Log.d("Login", "Finished LogIn, making FingerPrint");
        FingerPrintButton = (Button) findViewById(R.id.btnFingerPrint);

        if(isFirstRun()){
            //present view to set the initial password
            makeCryptKey();
            Log.d("firstrun", "Creating crypt key");
            Intent intent = new Intent(this, ChangePassword.class);
            startActivity(intent);

            mSharedPreferences.edit().putBoolean("firstrun", false).apply();
        }

        Log.d("Finger", "Checking fingerprint support");
        //check for lock support
        if (!mKeyguardManager.isKeyguardSecure()) {
            Log.d("Finger", "is not secure");
            Toast.makeText(this,
                    "Secure lock screen hasn't set up.\n"
                            + "Go to 'Settings -> Security -> Fingerprint' to set up a fingerprint",
                    Toast.LENGTH_LONG).show();
            FingerPrintButton.setEnabled(false);
            return;
        }

        //noinspection ResourceType
        if (!mFingerprintManager.hasEnrolledFingerprints()) {
            Log.d("Finger", "has no fingerprints");
            // This happens when no fingerprints are registered.
            Toast.makeText(this,
                    "Go to 'Settings -> Security -> Fingerprint' and register at least one fingerprint",
                    Toast.LENGTH_LONG).show();
            FingerPrintButton.setEnabled(false);
            return;
        }
        Log.d("Finger", "No fingerprint errors");


        makeFingerKey();
        FingerPrintButton.setEnabled(true);
        FingerPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (initCipher()) {
                    // Show the fingerprint dialog.
                    mFragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                    boolean useFingerprintPreference = mSharedPreferences
                            .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key), true);
                    if (useFingerprintPreference) {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                    } else {
                        mFragment.setStage(
                                FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                    }
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                } else {
                    // This happens if the lock screen has been disabled or or a fingerprint got enrolled.
                    mFragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                    mFragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                    mFragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
                }
            }
        });
    }

    public boolean isFirstRun(){
        return mSharedPreferences.getBoolean("firstrun", true);
    }

    public byte[] getKey(){
        Object secretKey = null;
        try {
            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) mKeyStore.getEntry(CRYPT_KEY_NAME, null);
            secretKey = secretKeyEntry.getSecretKey();
            Log.d("Key from login: ", Base64.encodeToString(convertToBytes(secretKey),Base64.DEFAULT));
            return convertToBytes(secretKey);
        }catch (Exception e){
            Log.e("getKey: ", Log.getStackTraceString(e));
            return null;
        }
    }

    public void makeCryptKey(){
        try {
            mKeyStore.load(null);
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(CRYPT_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();
        }catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }

        return;
    }

    public void makeFingerKey() {
        try {

            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            mKeyGenerator.init(new KeyGenParameterSpec.Builder(FINGER_KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            mKeyGenerator.generateKey();

        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException  e) {
            throw new RuntimeException(e);
        }
    }
    private boolean initCipher() {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(FINGER_KEY_NAME, null);
            mCipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private byte[] convertToBytes(Object object) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutput out = new ObjectOutputStream(bos)) {
            out.writeObject(object);
            return bos.toByteArray();
        }
    }
    public void onAuth(boolean withFingerprint) {
        if (withFingerprint) {
            Log.d("confirm"," Authentication happened with fingerprint");
        } else {
            // Authentication happened with backup password.
            Log.d("confirm"," Authentication happened with backup password");
        }
        goToPasswordView();
    }


    private void goToPasswordView(){
        Log.d("Login", "About to make an intent");
        Intent intent = new Intent(this, PasswordViewer.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }else if (id == R.id.action_change_password){
            Toast.makeText(this,
                    "Please log in to change password",
                    Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
