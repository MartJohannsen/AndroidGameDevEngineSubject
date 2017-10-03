package dk.kea.class2017august.martin.gameengine_one;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

/**
 * Created by Martin on 05-09-2017.
 */

public class TestGame extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        return new TestScreen(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
