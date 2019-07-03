package org.sparkleshare.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Splash {@link Activity} which will be shown to user when no previously
 * saved credentials could be found.
 *
 * @author kai
 */
public class SplashActivity extends Activity {

  private Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = this;
    setContentView(R.layout.splash);

    /* Found credentials, forwarding to BrowsingActivity */
    SharedPreferences prefs = SettingsActivity.getSettings(this);
    if (prefs.contains("ident")) {
      Intent browseData = new Intent(this, BrowsingActivity.class);
      String serverUrl = prefs.getString("serverUrl", "");
      browseData.putExtra("url", serverUrl + "/api/getFolderList");
      startActivity(browseData);
      this.finish();
    }
  }

  /**
   * Will be called when user clicks a button inside this {@link Activity}
   *
   * @param target Button which was clicked by user
   */
  public void buttonClick(View target) {
    switch (target.getId()) {
      case R.id.btn_insert_linkcode:
        Intent setup = new Intent(context, SetupActivity.class);
        startActivity(setup);
        break;
      case R.id.btn_scan_qrcode:
        IntentIntegrator.initiateScan(this);
        break;
    }
  }

  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult scanResult =
      IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (scanResult != null && scanResult.getContents() != null) {
      String content = scanResult.getContents().toLowerCase();

      String url = content.split("sshare:")[1].split("#")[0];
      String linkcode = content.split("#")[1];
      Intent setup = new Intent(context, SetupActivity.class);
      setup.putExtra("url", url);
      setup.putExtra("linkcode", linkcode);
      startActivity(setup);
    }
  }

}
