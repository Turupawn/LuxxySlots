package rosalila.studio.slotmachine;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.swarmconnect.Swarm;
import com.swarmconnect.SwarmAchievement;
import com.swarmconnect.SwarmAchievement.GotAchievementsMapCB;
import com.swarmconnect.SwarmActiveUser;
import com.swarmconnect.SwarmActiveUser.GotCloudDataCB;
import com.swarmconnect.SwarmLeaderboard;
import com.swarmconnect.SwarmLeaderboard.GotLeaderboardCB;
import com.swarmconnect.SwarmLeaderboard.GotScoreCB;
import com.swarmconnect.SwarmLeaderboardScore;
import com.swarmconnect.delegates.SwarmLoginListener;

public class MainActivity extends AndroidApplication implements
		AndroidFunctionsInterface {
	int loaded_score = 0;
	float readed_barcode;
	boolean loaded_score_ready = false;

	int LEADERBOARD_ID = 10730;
	int GAME_ID = 6908;
	String GAME_KEY = "dbdbdfb7482f4b39e3d85845e90c2e88";

	public static SwarmLeaderboard leaderboards;
	public static String yourGameCloudData = "0,1";

	Map<Integer, SwarmAchievement> achievements;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		cfg.useGL20 = false;

		Swarm.setActive(this);
		// // if ( Swarm.isEnabled() ) {
		// Swarm.init(MainActivity.this, GAME_ID, "yourSwarmGameKey",
		// mySwarmLoginListener);
		// // }
		// // SwarmLeaderboard.showLeaderboard(LEADERBOARD_ID);
		//
		// Swarm.showDashboard();

		final SlotMachine slot_machine = new SlotMachine(this);
		
		readed_barcode=-1;

		initialize(slot_machine, cfg);
		
		//readBarCode();
	}
	
	@Override
	protected
	void onDestroy()
	{
		super.onDestroy();
		System.runFinalizersOnExit(true);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	// Swarm
	@Override
	public void onResume() {
		super.onResume();
		try {
			Swarm.setActive(this);
		} catch (Exception e) {
		}
	}

	// Swarm
	@Override
	public void onPause() {
		super.onPause();
		try {
			Swarm.setInactive(this);
		} catch (Exception e) {
		}
	}

	// Swarm
	private SwarmLoginListener mySwarmLoginListener = new SwarmLoginListener() {
		// This method is called when the login process has started
		// (when a login dialog is displayed to the user).
		public void loginStarted() {
		}

		// This method is called if the user cancels the login process.
		public void loginCanceled() {

		}

		// This method is called when the user has successfully logged in.
		public void userLoggedIn(SwarmActiveUser user) {
			// Load our Leaderboard
			try {
				SwarmLeaderboard.getLeaderboardById(LEADERBOARD_ID,
						new GotLeaderboardCB() {
							public void gotLeaderboard(SwarmLeaderboard lb) {
								leaderboards = lb;

								// Load previous score
								if (loaded_score == -1) {
									// position=22;
									GotScoreCB got_score = new GotScoreCB() {
										@Override
										public void gotScore(
												SwarmLeaderboardScore arg0) {
											// TODO Auto-generated method stub
											if (arg0 != null)
												loaded_score = (int) arg0.score;
											else
												loaded_score = 0;
										}
									};
									if (Swarm.user != null)
										leaderboards.getScoreForUser(
												Swarm.user, got_score);
								}
							}
						});
			} catch (Exception e) {
			}

			try {
				SwarmAchievement.getAchievementsMap(callback);
			} catch (Exception e) {
			}
		}

		// This method is called when the user logs out.
		public void userLoggedOut() {

		}

	};

	// Swarm
	public boolean IsSwarmInitiated() {
		return Swarm.isInitialized();
	}

	// Swarm
	public void SwarmInitiate() {
		// Ensure it runs on UI thread
		final MainActivity a_temp=this;
		MainActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				//Swarm.preload(a_temp, GAME_ID, GAME_KEY);
				Swarm.setLeaderboardNotificationsEnabled(false);
				try {
					if (!Swarm.isInitialized()) {
						Swarm.init(MainActivity.this, GAME_ID, GAME_KEY,
								mySwarmLoginListener);
					}
				} catch (Exception e) {
				}
			}
		});
	}

	GotAchievementsMapCB callback = new GotAchievementsMapCB() {
		public void gotMap(Map<Integer, SwarmAchievement> achievements) {
			// Store the map of achievements somewhere to be used later.
			MainActivity.this.achievements = achievements;
		}
	};

	public void SwarmUnlockAchievement(int AchievementID) {
		// Make sure that we have our achievements map.
		if (MainActivity.this.achievements != null) {
			// Grab the achievement from our map.
			SwarmAchievement achievement = MainActivity.this.achievements
					.get(AchievementID);
			// No need to unlock more than once…
			if (achievement != null)// !! achievement.unlocked == false)
			{
				achievement.unlock();
			}
		}
	}

	// Swarm
	public void SubmitScore(float score) {
		// Swarm submit score – uses an interface AndroidFunctionsInterface
		// that allows calls from the main project.
		try {
			if (MainActivity.leaderboards != null) {
				// Leaderboards come in 3 types, INTEGER, FLOAT, and TIME
				// TIME Leaderboards are submitted in seconds.
				// We can optionally pass in String data which can be retrieved
				// from
				// a score at a later time to implement things like replays.
				MainActivity.leaderboards.submitScore(score, null, null);
			}
		} catch (Exception e) {
		}
	}

	// Swarm
	public void ShowLeaderboardSwarm() {
		// Swarm Leader board show – uses an interface AndroidFunctionsInterface
		// that allows calls from the main project.

		// Ensure it runs on UI thread
		MainActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				try {
					Swarm.showLeaderboards();
				} catch (Exception e) {
				}
			}
		});
	}

	// // Swarm
	public void SwarmYourGameCloudDataSave(String theYourGameCloudData) {
		// Save to the cloud
		if (Swarm.isLoggedIn()) {
			Swarm.user.saveCloudData("YourGameCloudData", theYourGameCloudData);
		}
	}

	// Swarm
	GotCloudDataCB callback1 = new GotCloudDataCB() {
		public void gotData(String data) {
			// Did our request fail (network offline, and uncached)?
			if (data == null) {
				// Handle failure case.
				MainActivity.yourGameCloudData = "";
				return;
			}

			// Has this key never been set? Default it to a value…
			if (data.length() == 0) {
				data = "0,1";
			}

			// Parse the level data for later use
			MainActivity.yourGameCloudData = data;
		}
	};

	public void SwarmYourGameCloudDataLoad() {
		if (Swarm.isLoggedIn()) {
			Swarm.user.getCloudData("YourGameCloudData", callback1);
		}
	}

	public String SwarmYourGameCloudDataGet() {
		// must call SwarmYourGameCloudDataLoad first
		return MainActivity.yourGameCloudData;
	}

	public float getScore() {
		return loaded_score;
	}
	
	public float getReadedBarcode() {
		return readed_barcode;
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		//retrieve result of scanning - instantiate ZXing object
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		//check we have a valid result
		if (scanningResult != null) {
			//get content from Intent Result
			String scanContent = scanningResult.getContents();
			//get format name of data scanned
			String scanFormat = scanningResult.getFormatName();
			//output to UI
//			formatTxt.setText("FORMAT: "+scanFormat);
//			contentTxt.setText("CONTENT: "+scanContent);
			
			readed_barcode=Integer.parseInt(scanContent);
			
		}
		else{
			//invalid scan data or scan canceled
			Toast toast = Toast.makeText(getApplicationContext(), 
					"No scan data received!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
	
	public void readBarCode()
	{
		readed_barcode=-1;
		IntentIntegrator scanIntegrator = new IntentIntegrator(this);
		//start scanning
		scanIntegrator.initiateScan();
	}
}