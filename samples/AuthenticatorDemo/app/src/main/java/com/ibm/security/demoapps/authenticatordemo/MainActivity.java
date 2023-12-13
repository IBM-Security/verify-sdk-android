package com.ibm.security.demoapps.authenticatordemo;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.ibm.security.verifysdk.*;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.logging.HttpLoggingInterceptor;

public class MainActivity extends AppCompatActivity {

    final int SCAN_QR_REQUEST = 24;
    final String TAG = "Authenticator Demo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextHelper.sharedInstance().setContext(getApplicationContext());
        NetworkHandler.sharedInstance().setLoggingInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        Intent intent = new Intent(getApplicationContext(), UIQRScanView.class);
        startActivityForResult(intent, SCAN_QR_REQUEST);    // [01]
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_QR_REQUEST && data != null && data.hasExtra(IQRScanResult.class.getName())) {
            IQRScanResult scanResult = (IQRScanResult) data.getExtras().get(IQRScanResult.class.getName());    // [03]

            AuthenticatorContext.sharedInstance().create(scanResult,    // [04]
                    new IResultCallback<IAuthenticator>() {
                        @Override
                        public void handleResult(IAuthenticator iAuthenticator, VerifySdkException e) {    // [07]

                            if (e == null) {
                                if (iAuthenticator instanceof IMfaAuthenticator) {
                                    IMfaAuthenticator mfaAuthenticator = (IMfaAuthenticator) iAuthenticator;

                                    Observable<AuthenticationMethod> signatureAuthenticationMethodObservable = Observable.fromIterable(mfaAuthenticator.getAvailableMethods());

                                    signatureAuthenticationMethodObservable
                                            .subscribeOn(Schedulers.computation())
                                            .flatMap(new Function<AuthenticationMethod, ObservableSource<?>>() {
                                                @Override
                                                public ObservableSource<?> apply(AuthenticationMethod authenticationMethod) {

                                                    if (authenticationMethod instanceof TotpAuthenticationMethod)
                                                        return Observable.empty();

                                                    String keyName = mfaAuthenticator.getKeyName(authenticationMethod.getSubType());    // [08][09]
                                                    boolean authenticationRequired = false;

                                                    KeyStoreHelper.createKeyPair(keyName, Algorithm.valueOf(authenticationMethod.getAlgorithm()).getAlgorithm(),    // [10]
                                                            authenticationRequired, new IResultCallback<PublicKey>() {
                                                                @Override
                                                                public void handleResult(PublicKey publicKey, VerifySdkException e) {    // [12]

                                                                    if (e == null) {
                                                                        SignatureAuthenticationMethod method = (SignatureAuthenticationMethod) authenticationMethod;
                                                                        method.setPublicKey(KeyStoreHelper.exportPublicKey(keyName, Base64.NO_WRAP));    // [13]

                                                                        if (iAuthenticator instanceof CloudAuthenticator) {
                                                                            method.setSignedData(KeyStoreHelper.signData(keyName, authenticationMethod.getAlgorithm(),  // [14][15][16]
                                                                                    iAuthenticator.getIdentifier(), Base64.NO_WRAP));
                                                                        }

                                                                    }
                                                                    else {
                                                                        Log.e(TAG, "Error: " + e.toString());
                                                                    }
                                                                }
                                                            });
                                                    return Observable.empty();
                                                }
                                            })
                                            .toList()
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableSingleObserver<List<Object>>() {
                                                @Override
                                                public void onSuccess(List<Object> objects) {
                                                    Log.i(TAG, "Keys successfully generated");

                                                    ArrayList<SignatureAuthenticationMethod> methodsToEnroll = new ArrayList<>();
                                                    for (AuthenticationMethod authenticationMethod : mfaAuthenticator.getAvailableMethods())  {
                                                        if (authenticationMethod instanceof SignatureAuthenticationMethod)    {
                                                            methodsToEnroll.add((SignatureAuthenticationMethod)authenticationMethod);    // [17]
                                                        }
                                                    }

                                                    AuthenticatorContext.sharedInstance().enroll(mfaAuthenticator,    // [18]
                                                            methodsToEnroll, new IResultCallback<List<VerifySdkException>>() {
                                                                @Override
                                                                public void handleResult(List<VerifySdkException> verifySdkExceptionList, VerifySdkException verifySdkException) {    // [21]

                                                                    if (verifySdkException != null) { // 'serious' issue, most likely network IO related
                                                                        Log.e(TAG, "Network error: " + verifySdkException.toString());
                                                                        // go to error activity
                                                                    }
                                                                    else if (verifySdkExceptionList.size() > 0)    {

                                                                        for (VerifySdkException v : verifySdkExceptionList) {
                                                                            Log.e(TAG, "Enrollment failed: " + v.toString());
                                                                        }
                                                                        // go to error activity
                                                                    }
                                                                    else {
                                                                        try {
                                                                            Log.i(TAG, "Authenticator:" + mfaAuthenticator.serializeToJson(false));
                                                                            // go to TOTP enrollment
                                                                        } catch (VerifySdkException e1) {
                                                                            Log.e(TAG, "Error: " + e1.toString());
                                                                        }
                                                                    }
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }
                                            });
                                } else {
                                    // OTP authenticator --> done
                                }
                            } else {
                                Log.e(TAG, "Error: " + e.toString());
                            }
                        }
                    });
        }
    }
}
