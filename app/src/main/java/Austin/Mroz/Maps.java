package Austin.Mroz;

import java.util.logging.Level;
import java.util.Scanner;
import java.util.logging.Logger;
import java.net.URL;
import java.net.URLEncoder;
import java.io.IOException;
import com.google.android.gms.maps.model.LatLng;

public class Maps {
	//should scrub before uploading
	private String token = "AIzaSyDgjI3RXwyEQ2miVgUbtMd3lJwoe91c5gc";
	private static String base = "https://maps.googleapis.com/maps/api/geocode/json";
	public Maps() {
		//try {
		//	Scanner s = new Scanner(new File("key.txt"));
		//	token = s.nextLine();//swap to buffered readers eventually
		//} catch(IOException e) {
		//	Logger.getLogger("").log(Level.SEVERE,"Could not open key.txt",e);
		//}
	}

	public LatLng query(String loc) throws IOException {
		URL u = new URL(base+"?address=" + URLEncoder.encode(loc)
				+ "&key=" + token);
		Json res = Json.read(u.openConnection().getInputStream());
		if(!"OK".equals(res.get("status")))
			throw new IOException("Request failed with "+res);
		Json ll = res.get("geometry").get("location");
		return new LatLng(Double.parseDouble(ll.get("lat").s()),
				Double.parseDouble(ll.get("lng").s()));
	}
}
