package pl.bgadzala.arl;

/**
 * Format unaware audio recorder.
 */
public interface Recorder {
    /**
     * Starts recorder. It is possible to start recorder multiple times, to switch between PAUSED and STARTED states.
     * Once recorder is stopped, it cannot be started again.
     */
    void start();

    /**
     * Pauses recorder. It is possible to start recording while paused.
     */
    void pause();

    /**
     * Definitely stops recorder - it cannot be started again. All audio resources will be released.
     */
    void stop();

    /**
     * Checks if recording is started.
     *
     * @return <code>true</code> if recording is started
     */
    boolean isStarted();

    /**
     * Checks if recording is active (may be paused).
     *
     * @return <code>true</code> if recording is active (recorder is started or paused)
     */
    boolean isRecording();

    /**
     * Gets value of the max amplitude for read samples. Stored amplitude will be
     * immediately zeroed - till next sample is read.
     *
     * @return value of max amplitude of read samples
     */
    int getMaxAmplitude();
}
