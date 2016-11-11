package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import de.hdodenhof.circleimageview.CircleImageView;
import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.FitchArtists;
import utility.imaginet.com.judgeme.util.IabHelper;
import utility.imaginet.com.judgeme.util.IabResult;
import utility.imaginet.com.judgeme.util.Inventory;
import utility.imaginet.com.judgeme.util.Purchase;

/**
 * Created by IM0033 on 11/27/2015.
 */
public class UpGradeActivity extends Activity {
    private Button upGrade_backButton, upGrade_Button;
    private ImageView upGradeSetZone;
    private CircleImageView upGrade_UserImageView;
    private ImageLoader imageLoader;
    private PrefManager prefManager;
    IabHelper mHelper;
    private TextView upGradeArtistNameText, upGrade_accounterName, upGrade_followsTextView, upGrade_accountType, upGrade_activeClipsTextView, upGrade_to, upGrade_premiumTextView;
    static final String TEST_PREMIUM = "premium_upgrade";
    static final String TEST_PRO = "pro_upgrade";
    // static final String TEST_PREMIUM = "premium_test_";
    // static final String TEST_PRO = "pro_test_";
    static final String TAG = "In-App_bill";
    boolean mIsPremium = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upgrade);
        LoadData();
    }

    private void LoadData() {
        FindViewById();
        prefManager = new PrefManager(getApplicationContext());
        imageLoader = AppController.getInstance().getImageLoader();
        fetchArtist(judgeMeUrls.FETCH_ARTIST + "&token=" + prefManager.getObjectId(), 1);


        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtpZ56il3ppNBFiI3zC8UHH+9SMLtCEhH7YdpBNp6i4OsKzHBtAok461UfykcDgc4M7UV5nCSeMAnRyxuR2Ia9CbkojGw8ztNYypbL0cQxn85eWToAQia74DzVoQ+8KdoQEtS1Yc/+bAYgGbP60AVYnZ6eAmP5HrHSfzwInr3TPhrDI/WvBZWg7p3mMSr1H7Hczi5iZO6AKL4UOhZfATfl9TEMYwANSjNDRDDOTWmHJnlUkc5cxb0D5KxEw+5T5V7Crr8zY/MiiXKAorei9fjILNCN2Er7SRnCDCXMGUfpY68WuG6tR2DnKZKOCsFg3oFPHVF86Iz+X/mbG7p9aP7BQIDAQAB";
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.enableDebugLogging(true);
        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               Log.d(TAG, "In-app Billing setup failed: " +
                                                       result);
                                           } else {
                                               Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                           if (mHelper == null) return;
                                           Log.d(TAG, "Setup successful. Querying inventory.");
                                           //mHelper.queryInventoryAsync(mReceivedInventoryListener);
                                       }

                                   });
        onClick();
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {
            if (mHelper == null) {
                return;
            }
            if (result.isFailure()) {
                // Handle failure
                Log.d("in_appError", "" + result);
            }
            Purchase premiumPurchase = inventory.getPurchase(TEST_PREMIUM);
            if (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase)) {
               /* mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);*/
                Log.d(TAG, "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));
            }

            Purchase proPurchase = inventory.getPurchase(TEST_PRO);
            if (proPurchase != null && verifyDeveloperPayload(proPurchase)) {
              /*  mHelper.consumeAsync(inventory.getPurchase(ITEM_PRO),
                        mConsumeFinishedListener);*/
                Log.d(TAG, "User is " + (mIsPremium ? "PRO" : "NOT PRO"));
            }

            Log.d(TAG, "" + result);
        }

    };

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            String checksumValue = null;
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);
            //if (mHelper == null) return;
            if (result.isFailure()) {
                // Handle error
                complain("Error purchasing: " + result);
                Log.d(TAG, "" + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain("Error purchasing. Authenticity verification failed.");
                //setWaitScreen(false);
                return;
            }
            Log.d(TAG, "Purchase successful.");

            if (purchase.getSku().equals(TEST_PREMIUM)) {
                //consumeItem();
                String checksum = prefManager.getUID() + "PremiumUpgrade";
                checksumValue = md5(checksum);
                fetchArtist(judgeMeUrls.UPGRADE + "&token=" + prefManager.getObjectId() + "&checksum=" + checksumValue, 5);
                //mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                mIsPremium = true;
                alert("Thank you for upgrading to premium!");
                Log.d(TAG + "OnIabPurchaseFinishedListener", "" + purchase);
                upGrade_Button.setEnabled(true);

            } else if (purchase.getSku().equals(TEST_PRO)) {
                String checksum = prefManager.getUID() + "ProUpgrade";
                checksumValue = md5(checksum);
                //mHelper.consumeAsync(purchase, mConsumeFinishedListener);
                alert("Thank you for upgrading to pro!");
                //consumeItem();
                Log.d(TAG + "OnIabPurchaseFinishedListener", "" + purchase);
                upGrade_Button.setEnabled(true);
                fetchArtist(judgeMeUrls.UPGRADE + "&token=" + prefManager.getObjectId() + "&checksum=" + checksumValue, 5);
            }

        }
    };

    private void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    private boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();
        return true;
    }

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {
                    Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);
                    if (mHelper == null) return;
                    // String checksumValue = null;
                    if (result.isSuccess()) {
                        if (purchase.getSku().equals(TEST_PREMIUM)) {
                            // String checksum = prefManager.getUID() + "PremiumUpgrade";
                            //checksumValue = md5(checksum);
                        } else if (purchase.getSku().equals(TEST_PRO)) {
                            // String checksum = prefManager.getUID() + "ProUpgrade";
                            //checksumValue = md5(checksum);
                        }
                        upGrade_Button.setEnabled(true);
                        //fetchArtist(judgeMeUrls.UPGRADE + "&token=" + prefManager.getObjectId() + "&checksum=" + checksumValue, 5);
                        Log.d("in_appisSuccess_OnConsumeFinishedListener", "" + result);
                    } else {
                        // handle error
                        complain("Error while consuming: " + result);
                        Log.d("in_appError", "" + result);
                    }
                }
            };

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + "," + data);
        if (mHelper == null) return;
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    private void fetchArtist(String url, final int type) {
        Log.d("userResponse", "" + url);
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 1) {
                    Log.d("userResponse", "" + response);
                    FitchArtists setData = new FitchArtists();
                    setData.setArtistsResponseValue(response);
                    upGrade_accounterName.setText(setData.getFirstname() + " " + setData.getLastname());
                    upGrade_followsTextView.setText("#" + setData.getRank() + " " + "in" + " " + setData.getZone());
                    //upGrade_followsTextView.setText("#" + " " + setData.getNumberoflikes() + " " + "in" + setData.getZone());
                    upGrade_accountType.setText(setData.getAccounttype());
                    upGrade_activeClipsTextView.setText(setData.getActiveclips() + " " + "active clips" + "," + " " + "with Advertising");
                    upGrade_to.setText(setData.getUpgradeto());
                    upGrade_premiumTextView.setText(setData.getUpgradeaccountactiveclips() + " " + "active clips" + "," + " " + "with Advertising");
                    String ZoneSetting = setData.getZone();
                    imageLoader(setData.getPhotoURL(), "UserPhoto");
                    if (ZoneSetting != null) {
                        if (ZoneSetting.startsWith("World")) {
                            imageLoader(setData.getWorldiconURLlowres(), "Zone");
                            // upGrade_followsTextView.setText("#" + setData.getRank() + " " + "in" + " " + "World.");
                        } else if (ZoneSetting.startsWith("Country")) {
                            imageLoader(setData.getCountryiconURLlowres(), "Zone");
                            // upGrade_followsTextView.setText("#" + setData.getRank() + " " + "in" + " " + setData.getCountry());
                        } else if (ZoneSetting.startsWith("State")) {
                            imageLoader(setData.getStateiconURLlowres(), "Zone");
                            // upGrade_followsTextView.setText("#" + setData.getRank() + " " + "in" + " " + setData.getState());
                        } else if (ZoneSetting.startsWith("City")) {
                            imageLoader(setData.getCityiconURLlowres(), "Zone");
                            // upGrade_followsTextView.setText("#" + setData.getRank() + " " + "in" + " " + setData.getCity());
                        }
                    }
                } else if (type == 5) {
                    Log.d("Inn app-response", "" + response);
                }
            }
        });
    }

    public void imageLoader(String photoURL, final String imageview) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageview.startsWith("UserPhoto")) {
                    upGrade_UserImageView.setImageBitmap(imageContainer.getBitmap());
                } else {
                    upGradeSetZone.setImageBitmap(imageContainer.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("ErrorProPic", volleyError.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    private void FindViewById() {
        upGradeArtistNameText = (TextView) findViewById(R.id.upGradeArtistNameText);
        upGrade_followsTextView = (TextView) findViewById(R.id.upGrade_followsTextView);
        upGrade_accountType = (TextView) findViewById(R.id.upGrade_accountType);
        upGrade_activeClipsTextView = (TextView) findViewById(R.id.upGrade_activeClipsTextView);
        upGrade_to = (TextView) findViewById(R.id.upGrade_to);
        upGrade_premiumTextView = (TextView) findViewById(R.id.upGrade_premiumTextView);
        upGrade_accounterName = (TextView) findViewById(R.id.upGrade_accounterName);
        upGrade_backButton = (Button) findViewById(R.id.upGrade_backButton);
        upGrade_Button = (Button) findViewById(R.id.upGrade_Button);
        upGradeSetZone = (ImageView) findViewById(R.id.upGradeSetZone);
        upGrade_UserImageView = (CircleImageView) findViewById(R.id.upGrade_UserImageView);

    }


    void complain(String message) {
        Log.e(TAG, "**** Judgeme Error: " + message);
        alert("Error: " + message);
    }

    void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    private void onClick() {
        upGrade_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                //String checksum = prefManager.getUID() + "ProUpgrade";
                //String checksumValue = md5(checksum);
                //Log.d("checksum",checksumValue);
            }
        });
        upGradeSetZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchArtist(judgeMeUrls.NEXTZONE + "&token=" + prefManager.getObjectId(), 2);
            }
        });
        upGrade_Button.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View v) {
                                                 /* String checksum = prefManager.getUID() + "PremiumUpgrade";
                                                  Log.d("checksumvalue", md5(checksum));
                                                  String checksumValue = md5(checksum);
                                                  fetchArtist(judgeMeUrls.UPGRADE + "&token=" + prefManager.getObjectId() + "&checksum=" + checksumValue, 5);*/
                                                  upGrade_Button.setEnabled(false);
                                                  // mHelper.launchPurchaseFlow(UpGradeActivity.this, "android.test.purchased", 1000, mPurchaseFinishedListener, "payload");

                                                  if (upGrade_accountType.getText().toString().startsWith("Novice")) {
                                                      if (!mIsPremium) {
                                                          mHelper.launchPurchaseFlow(UpGradeActivity.this, TEST_PREMIUM, IabHelper.ITEM_TYPE_SUBS, 10001,
                                                                  mPurchaseFinishedListener, "");
                                                      }

                                                  } else if (upGrade_accountType.getText().toString().startsWith("Premium")) {
                                                      mHelper.launchPurchaseFlow(UpGradeActivity.this, TEST_PRO, IabHelper.ITEM_TYPE_SUBS, 10002,
                                                              mPurchaseFinishedListener, "mypurchasetoken");
                                                  }

                                              }
                                          }

        );
    }
}
