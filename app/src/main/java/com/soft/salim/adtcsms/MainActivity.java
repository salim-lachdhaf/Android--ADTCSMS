package com.soft.salim.adtcsms;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private EditText ReceiverNumber;
    private EditText Param1;
    private EditText Param2;
    private EditText Msg;
    private Spinner Commande;
    private EditText SecurityCode;
    private AdView mAdView;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAdView != null) {
            mAdView.destroy();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Param1 = this.findViewById(R.id.param1);
        Param2 = this.findViewById(R.id.param2);
        Msg = this.findViewById(R.id.msg);
        Button send = this.findViewById(R.id.send);
        Commande = this.findViewById(R.id.commande);

        SecurityCode = this.findViewById(R.id.securitycode);

        MobileAds.initialize(this, getString(R.string.adMob_Id));

        mInterstitialAd = new InterstitialAd(this);
        // set the ad unit ID
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        mAdView = findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        Commande.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 18:
                    case 24:
                        Param1.setVisibility(View.VISIBLE);
                        Param1.setHint("Phone number");
                        Msg.setVisibility(View.GONE);
                        Param2.setVisibility(View.GONE);
                        break;
                    case 26:
                        Param1.setVisibility(View.VISIBLE);
                        Param1.setHint("Old PIN code");
                        Param2.setVisibility(View.VISIBLE);
                        Param2.setHint("New PIN code");
                        Msg.setVisibility(View.GONE);
                        break;
                    case 23:
                        Param1.setVisibility(View.VISIBLE);
                        Param1.setHint("New Lock code");
                        Param2.setVisibility(View.GONE);
                        Msg.setVisibility(View.GONE);
                        break;
                    case 25:
                        Param1.setVisibility(View.VISIBLE);
                        Param1.setHint("Phone number");
                        Msg.setVisibility(View.VISIBLE);
                        Param2.setVisibility(View.GONE);
                        break;
                    case 22:
                    case 21:
                    case 20:
                    case 19:
                        Param1.setVisibility(View.VISIBLE);
                        Param1.setHint("Duration (Second)");
                        Param2.setVisibility(View.GONE);
                        Msg.setVisibility(View.GONE);
                        break;
                    default:
                        Param1.setVisibility(View.GONE);
                        Param2.setVisibility(View.GONE);
                        Msg.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterstitial();
                sendDialog();
            }
        });

    }

    public String EnvoyerMsg(String message) {
        return "Send this code to your controlled device:\n" +
                message;
    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    private void sendCommande() {
        String cmd = Commande.getSelectedItem().toString();
        String P1 = Param1.getText().toString();
        String P2 = Param2.getText().toString();
        String MSG = Msg.getText().toString();
        String SC = SecurityCode.getText().toString();

        if (SC.equals("") || SC.length() < 2) {
            sendDialogResult("Error", getResources().getString(R.string.InvalidCode));
        } else if (cmd.equals(getResources().getString(R.string.Commande_prompt))) {
            sendDialogResult("Error", getResources().getString(R.string.InvalidCommand));
        } else {
            switch (cmd) {
                case "SMS Logs":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@1@@@"));
                    break;
                case "Call Logs":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@2@@@"));
                    break;
                case "Enable Wifi":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@9@@@"));
                    break;
                case "Lock Screen":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@11@" + P1 + "@@"));
                    break;
                case "Disable Wifi":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@10@@@"));
                    break;
                case "Enable Tracking":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@20@1@@"));
                    break;
                case "Disable Tracking":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@20@0@@"));
                    break;
                case "Hide Application":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@16@@@"));
                    break;
                case "Show Application":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@17@@@"));
                    break;
                case "Tracking History":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@22@@@"));
                    break;
                case "Back Camera Capture":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@5@2@@"));
                    break;
                case "Front Camera Capture":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@5@1@@"));
                    break;
                case "Enable Call Recording":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@4@1@@"));
                    break;
                case "Reset to Factory Mode":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@21@@@"));
                    break;
                case "Delete Internal memory":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@14@@@"));
                    break;
                case "Delete External memory":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@15@@@"));
                    break;
                case "Disable Call Recording":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@4@0@@"));
                    break;
                case "Enable Geographic limitation":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@18@1@@"));
                    break;
                case "Disable Geographic limitation":
                    sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@18@0@@"));
                    break;
                case "Call Restrict":
                    try {
                        Integer.parseInt(P1.toString());
                    } catch (Exception e) {
                        sendDialogResult("Send Command", getResources().getString(R.string.CallErreur));
                    }
                    if (P1.length() >= 8 && P1.length() <= 20) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@3@" + P1 + "@@"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.CallErreur));
                    }
                    break;
                case "Front Video Record":
                    int param;
                    try {
                        param = Integer.parseInt(P2);
                    } catch (Exception e) {
                        param = 1;
                    }
                    if (param >= 2 && param <= 100) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@6@1@" + P2 + "@"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.AlarmErreur));
                    }
                    break;
                case "Back Video Record":
                    try {
                        param = Integer.parseInt(P2);
                    } catch (Exception e) {
                        param = 1;
                    }
                    if (param >= 2 && param <= 100) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@6@2@" + P2 + "@"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.AlarmErreur));
                    }
                    break;
                case "Capture Audio":
                    try {
                        param = Integer.parseInt(P1);
                    } catch (Exception e) {
                        param = 1;
                    }
                    if (param >= 2 && param <= 100) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@7@" + P1 + "@@"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.AlarmErreur));
                    }
                    break;
                case "Make Alarm":
                    try {
                        param = Integer.parseInt(P1);
                    } catch (Exception e) {
                        param = 1;
                    }
                    if (param >= 2 && param <= 100) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@8@" + P1 + "@@"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.AlarmErreur));
                    }
                    break;
                case "Make Phone Call":
                    try {
                        Integer.parseInt(P1.replace("+", "00"));
                    } catch (Exception e) {
                    }
                    if (P1.length() >= 8 && P1.length() <= 20) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@12@" + P1.replace("+", "00") + "@@"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.InvalidNumber));
                    }
                    break;
                case "Send SMS":
                    try {
                        Integer.parseInt(P1.replace("+", "00"));
                    } catch (Exception e) {
                    }
                    if (P1.length() >= 8 && P1.length() <= 20) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@13@" + P1.replace("+", "00") + "@" + MSG + " @"));
                    } else {
                        sendDialogResult("Send Command", getResources().getString(R.string.InvalidNumber));
                    }
                    break;
                case "Change PIN Code":
                    try {
                        Integer.parseInt(P1);
                        Integer.parseInt(P2);
                    } catch (Exception e) {
                    }
                    if (P1.length() <= 8 && P1.length() >= 4 && P2.length() <= 8 && P2.length() >= 4) {
                        sendDialogResult("Send Command", EnvoyerMsg("@" + SC + "@19@" + P1 + "@" + P2 + " @"));
                    } else {
                        sendDialogResult("Error", getResources().getString(R.string.PinErreur));
                    }
                    break;
            }

        }
    }

    private void sendDialog() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setTitle("Confirm command generation");
        lBuilder.setCancelable(false);
        lBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                sendCommande();
            }
        });
        lBuilder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        lBuilder.create().show();
    }

    private void sendDialogResult(String pTitle, String pMessage) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setTitle(pTitle);
        lBuilder.setMessage(pMessage);
        lBuilder.setCancelable(false);
        lBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        lBuilder.create().show();
    }
}
