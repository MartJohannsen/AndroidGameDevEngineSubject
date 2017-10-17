package dk.kea.class2017august.martin.gameengine_one;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.List;

/**
 * Created by Martin on 05-09-2017.
 */

public class TestScreen extends Screen
{
    Bitmap bob = null;
    int bobX = 0;
    int bobY = 50;
    TouchEvent event = null;
    Sound sound = null;
    Music music = null;
    boolean isPlaying = false;


    public TestScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        bob = gameEngine.loadBitmap("bob.png");
        sound = gameEngine.loadSound("blocksplosion.wav");
        music = gameEngine.loadMusic("music.ogg");
        music.setLooping(true);
        isPlaying = true;
    }

    @Override
    public void update(float deltaTime)
    {
        gameEngine.clearFramedBuffer(Color.MAGENTA);

        bobX = bobX + (int) (10 * deltaTime);
        if(bobX > gameEngine.getFramedBufferWidth())
        {
            bobX = 0 - bob.getWidth();
        }

        gameEngine.drawBitmap(bob, bobX, bobY);



        //gameEngine.drawBitmap(bob, 200, 100);

        //gameEngine.drawBitmap(bob, 100, 200, 0, 0, 64, 64);
        /*
        for(int pointer = 0; pointer < 5; pointer++)
        {
            if(gameEngine.isTouchDown(pointer))
            {
                gameEngine.drawBitmap(bob, gameEngine.getTouchX(pointer), gameEngine.getTouchY(pointer));
            }
        }
        */

        List<TouchEvent> touchEvents = gameEngine.getTouchEvents();

        int stop = touchEvents.size();


        if(stop == 0 && event != null)
        {
            gameEngine.drawBitmap(bob, gameEngine.getTouchX(event.pointer), gameEngine.getTouchY(event.pointer));
        }
        for (int i = 0; i < stop; i++)
        {
            event = touchEvents.get(i);
            Log.d("TestScreen", "Touch event type: " + event.type + ", x: " + gameEngine.getTouchX(event.pointer) + ", y: " + gameEngine.getTouchY(event.pointer));
            gameEngine.drawBitmap(bob, gameEngine.getTouchX(event.pointer), gameEngine.getTouchY(event.pointer));
            if(event.type == TouchEvent.TouchEventType.Down)
            {
                sound.play(1);
            }
        }

        if(gameEngine.isTouchDown(0))
        {
            if(music.isPlaying())
            {
                music.pause();
                isPlaying = false;
            }
            else
            {
                music.play();
                isPlaying = true;
            }
        }

        // Anvendes til accelerometer handling på telefonen
        /*
        float accX = gameEngine.getAccelerometer()[0];
        float accY = gameEngine.getAccelerometer()[1];
        float x = gameEngine.getFramedBufferWidth()/2 + (accX/10) * gameEngine.getFramedBufferWidth();
        float y = gameEngine.getFramedBufferHeight()/2 + (accY/10) * gameEngine.getFramedBufferHeight();
        gameEngine.drawBitmap(bob, (int)(x-(bob.getWidth()/2)), (int)(y-(bob.getHeight()/2)));
        */
    }

    @Override
    public void pause()
    {
        Log.d("TestScreen", "The screen is PAUSED");
        music.pause();
    }

    @Override
    public void resume()
    {
        Log.d("TestScreen", "The screen is Resumed");
        music.play();
        isPlaying = true;
    }

    @Override
    public void dispose()
    {
        Log.d("TestScreen", "The screen is Disposed");
        music.stop();
    }
}
