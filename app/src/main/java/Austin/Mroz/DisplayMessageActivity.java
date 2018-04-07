package Austin.Mroz;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.CameraUpdate;
import android.location.Geocoder;
import android.location.Address;
import android.location.Location;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.logging.Logger;
import java.lang.Math; 


public class DisplayMessageActivity extends Activity implements OnMapReadyCallback {
	private Address adr;
	private FusedLocationProviderClient fusedLocationClient;
	private Location loc;
	private double dist;
	private GoogleMap gm;

	@Override
		public void onCreate(Bundle sis) {
			super.onCreate(sis);
			gm=null;
			Intent i = getIntent();
			String m = i.getStringExtra(MainActivity.EXTRA_MESSAGE);
			dist = i.getDoubleExtra("Austin.Mroz.DISTANCE",250.0);
			Logger.getLogger("").info(i.getExtras().toString());
			Logger.getLogger("").info(dist+"");
			setContentView(R.layout.activity_map_fragment);
			MapFragment mf = (MapFragment)getFragmentManager().findFragmentById(R.id.map);

			fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
			fusedLocationClient.getLastLocation().addOnSuccessListener(this, (l) -> locationCallback(l));
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
		this.gm=gm;
		synchronized(this) {
			if(loc !=null)
				showResult();
		}
	}
	public void locationCallback(Location l) {
		synchronized(this) {
			if(gm!=null && loc == null) {
				loc=l;
				showResult();
			}
			else
				loc=l;
		}
	}
	public void showResult() {
		LatLng targ = new LatLng(adr.getLatitude(),adr.getLongitude());
		String label = null;
		gm.setMyLocationEnabled(true);

		CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(targ,14f);

		if(loc!=null) {
			float[] f = new float[1];
			Location.distanceBetween(loc.getLatitude(),loc.getLongitude(),
					adr.getLatitude(), adr.getLongitude(), f);
			label = String.format("%.3f km off",Math.abs(dist-f[0]/1000));
		  setTitle(String.format("%.2f%% error",Math.abs(dist-f[0]/1000.0)/dist*100));

			gm.addCircle(new CircleOptions().center(new LatLng(loc.getLatitude(),loc.getLongitude()))
						.radius(dist*1000));

			//latlng bounds code. There's a correct way of doing it, but the logic is ugly and error prone.
			LatLngBounds llb = new LatLngBounds(targ,targ);
			LatLng other = new LatLng(loc.getLatitude(), loc.getLongitude());
			cu = CameraUpdateFactory.newLatLngBounds(llb.including(other),50);
		}
		if (label == null)
			label = "Nothing to show";
		gm.addMarker(new MarkerOptions().position(targ).title(label));
		Logger.getLogger("").info(""+adr.getExtras());
		gm.animateCamera(cu);
	}
}
