package Austin.Mroz;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.lang.Math;
import java.util.logging.Logger;

public class MainActivity extends Activity {
    public static final String EXTRA_MESSAGE="Austin.Mroz.MESSAGE";
    private double dist;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

	double gen = Math.random();
	dist = 16000*gen*gen*gen;

	setContentView(R.layout.activity_main);
	EditText e = (EditText)findViewById(R.id.edit_message);
	if(e!=null)
	   e.setText(String.format("Try to guess an address that is %d kilometers away",(int)dist));
	else
	    Logger.getLogger("").severe("Could not get text view");

	final TextView t = (TextView) findViewById(R.id.distance_view);
	if(t!=null)
	   t.setText(String.format("Try to guess an address that is %d kilometers away",(int)dist));
	else
	    Logger.getLogger("").severe("Could not get text view");


    }
    public void sendMessage(View view) {
	Intent i = new Intent(this,DisplayMessageActivity.class);
	EditText e = (EditText)findViewById(R.id.edit_message);
	String s = e.getText().toString();
	i.putExtra(EXTRA_MESSAGE, s);
	i.putExtra("Austin.Mroz.DISTANCE",dist);
	startActivity(i);
    }
    //24901 miles is circumference
    //12450 is max distance
    //aim for 250-300 average
    //want s curve 
    //consider piecewise?
    //-8(x-.25)^2+.5 for x<.5
    //8/9(x-.25)^2+.5

    //furthest distance I can get with gm is 20,000.
    //This is in the middle of the indian ocean.
    //furthest landmass is ~16.5 in 'straya
    //~8-14 is most of eurasia
    //250 for nearby stuff.
    //<2k for US
    //aim for 25% 0-250,
    //25% 250-2k
    //25% 2k-8k
    //25% 8-16k

    //With a 16000 multiplier, cutoffs are
    //1/64
    //1/8
    //1/2
    //this is almost exactly x^3...
}
