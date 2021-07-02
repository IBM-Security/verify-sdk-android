package com.ibm.security.demoapps.ci.a2oidcazndemo;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.ibm.security.verifysdk.ContextHelper;
import com.ibm.security.verifysdk.OAuthToken;
import com.ibm.security.verifysdk.VerifySdkException;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    private final int RESULT_ID = 42;
    private WeakReference<MainActivity> mainActivityWeakReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainActivityWeakReference = new WeakReference<>(this);
        ContextHelper.sharedInstance().setContext(getApplicationContext());
    }

    public void onClickUseGetOAuthToken(View v) {

        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("clientId", ((EditText) findViewById(R.id.etClientId)).getText().toString());
        intent.putExtra("clientSecret", ((EditText) findViewById(R.id.etClientSecret)).getText().toString());
        intent.putExtra("hostname", ((EditText) findViewById(R.id.etHostname)).getText().toString());
        this.startActivityForResult(intent, RESULT_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_ID && data != null && resultCode == RESULT_OK) {

            try {
                if (data.hasExtra("error")) {
                    VerifySdkException verifySdkException = (VerifySdkException)data.getExtras().get("error");
                    showDialog(verifySdkException.toString());
                }
                else if (data.hasExtra("token"))    {
                    OAuthToken oAuthToken = (OAuthToken)data.getExtras().get("token");

                        showDialog(oAuthToken.serializeToJson());
                }
                else {
                    showDialog("Something went wrong");
                }
            } catch (VerifySdkException e) {
                e.printStackTrace();
                showDialog(e.getMessage());
            }
        }
    }

    private void showDialog(final String data) {

        if (data == null)   {
            showDialog("Data == null");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivityWeakReference.get());

        builder.setTitle(R.string.app_name)
                .setMessage(data)
                .setPositiveButton("OK", (dialogInterface, i) -> { });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
