package dk.kea.class2017august.martin.gameengine_one;

import android.media.SoundPool;

/**
 * Created by Martin on 26-09-2017.
 */

public class Sound
{
    int soundId;
    SoundPool soundPool;

    public Sound(SoundPool soundPool, int soundId)
    {
        this.soundId = soundId;
        this.soundPool = soundPool;
    }

    public void play(float volume)
    {
        soundPool.play(soundId, volume, volume, 0, 0, 1);
    }
}
