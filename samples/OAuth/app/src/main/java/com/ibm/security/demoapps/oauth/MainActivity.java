package com.ibm.security.demoapps.oauth;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ibm.security.verifysdk.ContextHelper;
import com.ibm.security.verifysdk.IResultCallback;
import com.ibm.security.verifysdk.NetworkHandler;
import com.ibm.security.verifysdk.OAuthContext;
import com.ibm.security.verifysdk.OAuthToken;
import com.ibm.security.verifysdk.VerifySdkException;

import java.util.HashMap;

import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    private Activity activity;
    private OAuthToken token;
    private String registrationUrl = "https://<your_isam_here>/mga/sps/oauth/oauth20/token";
    private String clientId = "<your_clientid_here";
    private String secret = "<your_secret_here>";

    private EditText etUserName;
    private EditText etPassword;

    /*
        Caution: set IGNORE_SSL to 'true' will accept all SSL certificates
     */
    private final boolean IGNORE_SSL = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextHelper.sharedInstance().setContext(getApplicationContext());

        activity = this;
        etUserName = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
    }

    public void onClickUseGetOAuthToken(View v) {

        final String username = etUserName.getText().toString();
        final String password = etPassword.getText().toString();

        HashMap map = new HashMap();
        map.put("client_secret", secret);

        NetworkHandler.sharedInstance().setLoggingInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));

        if (username.isEmpty()) {
            showToast("Username is required");
        } else {

            OAuthContext.sharedInstance().authorize(registrationUrl, clientId, username, password, map,
            //OAuthContext.sharedInstance().authorize(registrationUrl, clientId, secret,
                    IGNORE_SSL, new IResultCallback<OAuthToken>() {
                        @Override
                        public void handleResult(OAuthToken oAuthToken, VerifySdkException e) {

                            if (oAuthToken != null)  {
                                token = oAuthToken;
                                showDialog(token);
                            } else {
                                showToast(e.toString());
                            }
                        }
                    });
        }
    }

    public void onClickRefreshOAuthToken(View v) {

    }

    private void showToast(final String message) {

        activity.runOnUiThread(() -> {
            Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        });
    }

    private void showDialog(final OAuthToken token) {

        if (token == null) {
            showToast("Something went wrong");
        } else {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity);

                                builder.setTitle("OAuth Sample")
                                        .setMessage(token.serializeToJson())
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });


                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();
                        } catch (VerifySdkException e) {
                            e.printStackTrace();
                        }
                    }
                });
        }
    }
}

