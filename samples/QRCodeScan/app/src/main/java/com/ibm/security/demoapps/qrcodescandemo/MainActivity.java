package com.ibm.security.demoapps.qrcodescandemo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ibm.security.verifysdk.CloudQRScan;
import com.ibm.security.verifysdk.ContextHelper;
import com.ibm.security.verifysdk.HmacAlgorithm;
import com.ibm.security.verifysdk.HotpGeneratorContext;
import com.ibm.security.verifysdk.IQRScanResult;
import com.ibm.security.verifysdk.OnPremiseQRScan;
import com.ibm.security.verifysdk.OtpQRScan;
import com.ibm.security.verifysdk.UIQRScanView;

public class MainActivity extends AppCompatActivity {

    private final int SCAN_QR_REQUEST = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContextHelper.sharedInstance().setContext(getApplicationContext());

        HotpGeneratorContext hotpGeneratorContext = new HotpGeneratorContext("secret", 6, HmacAlgorithm.SHA1, 0);
        Log.i("SDK Demo", "HOTP: " + hotpGeneratorContext.create());
    }

    public void onClickScanQRCode(View view) {

        Intent intent = new Intent(getApplicationContext(), UIQRScanView.class);
        startActivityForResult(intent, SCAN_QR_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SCAN_QR_REQUEST && data != null && data.hasExtra(IQRScanResult.class.getName())) {

            IQRScanResult scanResult = (IQRScanResult)data.getExtras().get(IQRScanResult.class.getName());

            TextView tvTitle = new TextView(this);
            tvTitle.setPadding(10, 30, 10, 30);
            tvTitle.setGravity(Gravity.CENTER);
            tvTitle.setTextColor(Color.BLACK);
            tvTitle.setTextSize(17);

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (scanResult instanceof OtpQRScan) {

                OtpQRScan otpQRScan = (OtpQRScan) scanResult;

                tvTitle.setText("OTP QR Code Scan");
                View view = inflater.inflate(R.layout.dialog_layout_otp, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setView(view);
                builder.setCustomTitle(tvTitle);
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tvUsername = alertDialog.findViewById(R.id.user_name);
                TextView tvIssuer = alertDialog.findViewById(R.id.issuer);
                TextView tvSecret = alertDialog.findViewById(R.id.secret);
                TextView tvType = alertDialog.findViewById(R.id.type);
                TextView tvAlgorithm = alertDialog.findViewById(R.id.algorithm);
                TextView tvDigits = alertDialog.findViewById(R.id.digits);
                TextView tvCounter = alertDialog.findViewById(R.id.counter);
                TextView tvPeriod = alertDialog.findViewById(R.id.period);

                tvUsername.setText(otpQRScan.getUsername());
                tvIssuer.setText(otpQRScan.getIssuer());
                tvSecret.setText(otpQRScan.getSecret());
                tvType.setText(otpQRScan.getType());
                tvAlgorithm.setText(otpQRScan.getAlgorithm().name());
                tvDigits.setText(String.valueOf(otpQRScan.getDigits()));
                tvCounter.setText(String.valueOf(otpQRScan.getCounter()));
                tvPeriod.setText(String.valueOf(otpQRScan.getPeriod()));

                Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setTextColor(Color.BLACK);

            } else if (scanResult instanceof OnPremiseQRScan) {

                OnPremiseQRScan onPremiseQRScan = (OnPremiseQRScan) scanResult;
                tvTitle.setText("OnPremise QR Code Scan");
                View view = inflater.inflate(R.layout.dialog_layout_onprem, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setView(view);
                builder.setCustomTitle(tvTitle);
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tvClientId = alertDialog.findViewById(R.id.client_id);
                TextView tvCode = alertDialog.findViewById(R.id.code);
                TextView tvDetailsUrl = alertDialog.findViewById(R.id.details_url);
                TextView tvVersion = alertDialog.findViewById(R.id.version);
                TextView tvIgnoreSsl = alertDialog.findViewById(R.id.ignore_ssl);

                tvClientId.setText(onPremiseQRScan.getClientId());
                tvCode.setText(onPremiseQRScan.getCode());
                tvDetailsUrl.setText(onPremiseQRScan.getDetailsUrl());
                tvVersion.setText(String.valueOf(onPremiseQRScan.getVersion()));
                tvIgnoreSsl.setText(String.valueOf(onPremiseQRScan.isIgnoreSslCerts()));

            } else if (scanResult instanceof CloudQRScan)   {

                CloudQRScan cloudQRScan = (CloudQRScan)scanResult;

                tvTitle.setText("Cloud QR Code Scan");
                View view = inflater.inflate(R.layout.dialog_layout_cloud, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setView(view);
                builder.setCustomTitle(tvTitle);
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                TextView tvVersion = alertDialog.findViewById(R.id.cloud_version);
                TextView tvRegister = alertDialog.findViewById(R.id.register);
                TextView tvPlatform = alertDialog.findViewById(R.id.platform);
                TextView tvCode = alertDialog.findViewById(R.id.cloud_code);
                TextView tvAccountName = alertDialog.findViewById(R.id.account_name);

                tvVersion.setText(cloudQRScan.getVersion());
                tvRegister.setText(cloudQRScan.getRegisterUri());
                tvPlatform.setText(cloudQRScan.getPlatform());
                tvCode.setText(String.valueOf(cloudQRScan.getCode()));
                tvAccountName.setText(cloudQRScan.getAccountName());

            } else {
                showToast("Unknown QR code");
            }
        }
    }

    private void showToast(final String message) {

        Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
