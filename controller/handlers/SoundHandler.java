package controller.handlers;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SoundHandler {

    private List<Clip> activeClips = new ArrayList<>(); // Track all active clips
    private Clip criticalClip; // Track the critical sound clip (e.g., game over)

    public void playSound(String fileName, boolean isCritical) {

        String filePath = "/resources/sound_effect/" + fileName + ".wav";
        try {
            if (isCritical) {
                // Stop any previous critical sound
                if (criticalClip != null && criticalClip.isRunning()) {
                    criticalClip.stop();
                    criticalClip.close();
                }

                // Load and play the critical sound
                criticalClip = createClip(filePath);
                criticalClip.start();

                // Add a listener to clean up the critical clip when done
                criticalClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        criticalClip.close();
                    }
                });

            } else {
                // For non-critical sounds, ensure no overlapping
                stopNonCriticalSounds();

                // Load and play the non-critical sound
                Clip nonCriticalClip = createClip(filePath);
                nonCriticalClip.start();

                // Add the non-critical clip to the active clips list
                activeClips.add(nonCriticalClip);

                // Add a listener to clean up the non-critical clip when done
                nonCriticalClip.addLineListener(event -> {
                    if (event.getType() == LineEvent.Type.STOP) {
                        nonCriticalClip.close();
                        activeClips.remove(nonCriticalClip); // Remove from the active list
                    }
                });
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private Clip createClip(String filePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // Load the sound file
        InputStream audioInputStream = getClass().getResourceAsStream(filePath);
        if (audioInputStream == null) {
            throw new IOException("Sound file not found: " + filePath);
        }

        // Create a new Clip
        Clip clip = AudioSystem.getClip();
        clip.open(AudioSystem.getAudioInputStream(audioInputStream));

        // Adjust the volume (gain)
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float volumeIncrease = 9.0f; // Increase volume by 9 dB
        float newVolume = Math.min(gainControl.getMaximum(), gainControl.getValue() + volumeIncrease);
        gainControl.setValue(newVolume);

        return clip;
    }

    private void stopNonCriticalSounds() {
        // Stop and close all active non-critical clips
        for (Clip clip : activeClips) {
            if (clip.isRunning()) {
                clip.stop();
                clip.close();
            }
        }
        activeClips.clear(); // Clear the list of active clips
    }
}