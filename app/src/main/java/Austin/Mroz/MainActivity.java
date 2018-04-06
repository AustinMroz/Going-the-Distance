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
	if(gen<.25)
	    gen = -8*(gen-.25)*(gen-.25)+.5;
	else
	    gen = 7.0/9*(gen-.25)*(gen-.25)+.5;
	dist = (1-gen)*12450;

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
}
