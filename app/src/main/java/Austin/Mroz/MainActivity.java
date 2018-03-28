package Austin.Mroz;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {
    public static final String EXTRA_MESSAGE="Austin.Mroz.MESSAGE";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void sendMessage(View view) {
	Intent i = new Intent(this,DisplayMessageActivity.class);
	EditText e = (EditText)findViewById(R.id.edit_message);
	String s = e.getText().toString();
	i.putExtra(EXTRA_MESSAGE, s);
	startActivity(i);
    }
}
