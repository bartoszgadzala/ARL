package pl.bgadzala.arl.samples.demo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import pl.bgadzala.arl.Format;

/**
 * Launcher activity.
 */
public class DemoActivity extends Activity {

    private Spinner mSpinnerFormat;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ArrayAdapter<Format> formatsAdapter = new ArrayAdapter<Format>(this, android.R.layout.simple_spinner_item, Format.values());
        formatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFormat = (Spinner) findViewById(R.id.spinnerFormat);
        mSpinnerFormat.setAdapter(formatsAdapter);
    }

}