package com.ooyala.sample.players;

;
import android.os.Bundle;
import android.util.Log;

import com.ooyala.android.OoyalaPlayer;
import com.ooyala.android.PlayerDomain;
import com.ooyala.android.configuration.Options;
import com.ooyala.sample.R;
import com.ooyala.android.skin.OoyalaSkinLayoutController;
import com.ooyala.android.skin.configuration.SkinOptions;

import org.json.JSONObject;

/**
 * This activity illustrates how you can play basic playback video using the Skin SDK
 * you can also play Ooyala and VAST advertisements programmatically
 * through the SDK
 *
 */
public class OoyalaSkinPlayerActivity extends AbstractHookActivity  {

	@Override
	void completePlayerSetup(boolean asked) {
		if(asked) {
			// Get the SkinLayout from our layout xml
			skinLayout = findViewById(R.id.ooyalaSkin);
			PlayerDomain domain = new PlayerDomain(this.domain);
			// Create the OoyalaPlayer, with some built-in UI disabled
			Options options = createOptions();
			player = new OoyalaPlayer(pcode, domain, options);

			//Create the SkinOptions, and setup React
			JSONObject overrides = createSkinOverrides();
			SkinOptions skinOptions = new SkinOptions.Builder().setSkinOverrides(overrides).build();
			playerLayoutController = new OoyalaSkinLayoutController(getApplication(), skinLayout, player, skinOptions);
			//Add observer to listen to fullscreen open and close events
			playerLayoutController.addObserver(this);
			player.addObserver(this);

			if (player.setEmbedCode(embedCode)) {

			} else {
				Log.e(TAG, "Asset Failure");
			}
		}
	}

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_skin_simple_layout);
		completePlayerSetup(asked);
	}

	/**
	 * Create skin overrides to show up in the skin.
	 * Default commented. Uncomment to show changes to the start screen.
	 * @return the overrides to apply to the skin.json in the assets folder
	 */
	private JSONObject createSkinOverrides() {
		JSONObject overrides = new JSONObject();
//    JSONObject startScreenOverrides = new JSONObject();
//    JSONObject playIconStyleOverrides = new JSONObject();
//    try {
//      playIconStyleOverrides.put("color", "red");
//      startScreenOverrides.put("playButtonPosition", "bottomLeft");
//      startScreenOverrides.put("playIconStyle", playIconStyleOverrides);
//      overrides.put("startScreen", startScreenOverrides);
//    } catch (Exception e) {
//      Log.e(TAG, "Exception Thrown", e);
//    }
		return overrides;
	}
}
