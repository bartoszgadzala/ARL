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
}
