package Austin.Mroz;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import android.location.Geocoder;
import android.location.Address;

import java.io.IOException;
import java.util.logging.Logger;


public class DisplayMessageActivity extends Activity implements OnMapReadyCallback {
	private LatLng targ;
		@Override
		public void onCreate(Bundle sis) {
			super.onCreate(sis);
			Intent i = getIntent();
			String m = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
			//MapFragment mf = new MapFragment();
			MapFragment mf = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
			try {
				//targ = new Maps().query(m);
				Address a = new Geocoder(this).getFromLocationName(m,1).get(0);
				targ = new LatLng(a.getLatitude(),a.getLongitude());
				Logger.getLogger("").info(targ.toString());
			} catch(IOException e) {
				TextView t = new TextView(this);
				t.setTextSize(40);
				t.setText("An IO Exception occured, please try again.");
				setContentView(t);
				return;
			}
			//mf.getMapAsync(this);
			setContentView(R.layout.activity_map_fragment);
		}
		public void onMapReady(GoogleMap gm) {
			gm.addMarker(new MarkerOptions().position(targ));
			Logger.getLogger("").info("got callback");
		}
}
