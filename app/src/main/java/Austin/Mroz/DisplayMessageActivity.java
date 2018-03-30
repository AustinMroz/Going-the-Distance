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
import com.google.android.gms.maps.CameraUpdateFactory;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.logging.Logger;



public class DisplayMessageActivity extends Activity implements OnMapReadyCallback {
	private Address adr;
	private FusedLocationProviderClient fusedLocationClient;
	private static Location loc;

	@Override
		public void onCreate(Bundle sis) {
			super.onCreate(sis);
			Intent i = getIntent();
			String m = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
			setContentView(R.layout.activity_map_fragment);
			MapFragment mf = (MapFragment)getFragmentManager().findFragmentById(R.id.map);

			fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
			fusedLocationClient.getLastLocation().addOnSuccessListener(this, (l) -> loc = l);
			try {
				adr = new Geocoder(this).getFromLocationName(m,1).get(0);
				mf.getMapAsync(this);
			} catch(IOException e) {
				TextView t = new TextView(this);
				t.setTextSize(40);
				t.setText("An IO Exception occured, please try again.");
				setContentView(t);
				return;
			}
		}
	public void onMapReady(GoogleMap gm) {
		LatLng targ = new LatLng(adr.getLatitude(),adr.getLongitude());
		String label = adr.getPostalCode();
		gm.setMyLocationEnabled(true);
		if(loc!=null) {
			float[] f = new float[1];
			Location.distanceBetween(loc.getLatitude(),loc.getLongitude(),
					adr.getLatitude(), adr.getLongitude(), f);
			label = f[0] +" meters";
		}
		if (label == null)
			label = "Nothing to show";
		gm.addMarker(new MarkerOptions().position(targ).title(label));
		Logger.getLogger("").info(""+adr.getExtras());
		gm.animateCamera(CameraUpdateFactory.newLatLngZoom(targ,14));
	}
}
