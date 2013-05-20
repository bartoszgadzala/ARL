package pl.bgadzala.arl.samples.demo;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.*;
import pl.bgadzala.arl.AudioStream;
import pl.bgadzala.arl.Format;
import pl.bgadzala.arl.RAFAudioStream;
import pl.bgadzala.arl.Recorder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;

/**
 * Launcher activity.
 */
public class DemoActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = "ARLDemo";

    private Spinner mSpinnerFormat;
    private TextView mTvStatus;
    private Format mSelectedFormat;
    private Recorder mRecorder;
    private ProgressBar mSpinnerRecording;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        ArrayAdapter<Format> formatsAdapter = new ArrayAdapter<Format>(this, android.R.layout.simple_spinner_item, Format.values());
        formatsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerFormat = (Spinner) findViewById(R.id.spinnerFormat);
        mSpinnerFormat.setAdapter(formatsAdapter);
        mSpinnerFormat.setOnItemSelectedListener(this);

        mTvStatus = (TextView) findViewById(R.id.tvStatus);

        mSpinnerRecording = (ProgressBar) findViewById(R.id.pbRecording);

        Button startButton = (Button) findViewById(R.id.btnStart);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonStartClicked();
            }
        });

        Button pauseButton = (Button) findViewById(R.id.btnPause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonPauseClicked();
            }
        });

        Button stopButton = (Button) findViewById(R.id.btnStop);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonStopClicked();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        mSelectedFormat = (Format) adapterView.getItemAtPosition(pos);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // NOP
    }

    /**
     * Invoked when user clicked on 'Start' button.
     */
    public void onButtonStartClicked() {
        try {
            String outFilename = null;
            if (mRecorder == null && mSelectedFormat != null) {
                mRecorder = mSelectedFormat.createRecorder(MediaRecorder.AudioSource.MIC, getOutputFile(mSelectedFormat));
            }
            if (mRecorder != null) {
                mRecorder.start();
                mSpinnerRecording.setVisibility(View.VISIBLE);
                setStatus("Recording started");
            }
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    /**
     * Invoked when user clicked on 'Pause' button.
     */
    public void onButtonPauseClicked() {
        try {
            if (mRecorder != null) {
                mRecorder.pause();
                mSpinnerRecording.setVisibility(View.INVISIBLE);
                setStatus("Recording paused");
            }
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    /**
     * Invoked when user clicked on 'Stop' button.
     */
    public void onButtonStopClicked() {
        try {
            if (mRecorder != null) {
                mRecorder.stop();
                mRecorder = null;
                mSpinnerRecording.setVisibility(View.INVISIBLE);
                setStatus("Recording stopped");
            }
        } catch (Exception ex) {
            handleError(ex);
        }
    }

    private void handleError(Exception ex) {
        setStatus(String.format("Error: %s", ex.getMessage()));
        Log.e(TAG, "Error", ex);
    }

    private void setStatus(String status) {
        mTvStatus.setText(status);
        Log.i(TAG, status);
    }

    private AudioStream getOutputFile(Format outputFormat) {
        File out = new File(getExternalStorageDirectoryForWriting(), String.format("arldemo.%s", outputFormat.getFileExtension()));
        try {
            return new RAFAudioStream(new RandomAccessFile(out, "rw"));
        } catch (FileNotFoundException ex) {
            throw new IllegalStateException("Cannot create output file for [" + outputFormat + "] format", ex);
        }
    }

    private File getExternalStorageDirectoryForWriting() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            throw new IllegalStateException("External storage is mounted in read-only mode");
        } else {
            throw new IllegalStateException("External storage is neither mounted nor writable");
        }
    }
}