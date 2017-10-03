package dk.kea.class2017august.martin.gameengine_one;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class GameEngine extends Activity implements Runnable, SensorEventListener
{
    // Fields of glory

    private Screen screen;
    private Canvas canvas;
    private Bitmap virtualScreen;
    Rect src = new Rect();
    Rect dst = new Rect();

    private SoundPool soundPool;

    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;

    private Thread mainLoopThread;
    private State state = State.Paused;
    private List<State> stateChanges = new ArrayList<State>();

    private TouchHandler touchHandler;
    private TouchEventPool touchEventPool = new TouchEventPool();
    private List<TouchEvent> touchEventBuffer = new ArrayList<>();
    private List<TouchEvent> touchEventCopied = new ArrayList<>();

    private float[] accelerometer = new float[3];


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        surfaceView = new SurfaceView(this);
        setContentView(surfaceView);
        surfaceHolder = this.surfaceView.getHolder();
        //setContentView(R.layout.activity_main);


        touchHandler = new MultiTouchHandler(surfaceView, touchEventBuffer, touchEventPool);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if(sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0)
        {
            Sensor accelerometer = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        SoundPool.Builder sBuilder = new SoundPool.Builder();
        sBuilder.setMaxStreams(20);
        AudioAttributes.Builder audioAttrBuilder = new AudioAttributes.Builder();
        audioAttrBuilder.setUsage(AudioAttributes.USAGE_GAME);
        AudioAttributes audioAttr = audioAttrBuilder.build();
        sBuilder.setAudioAttributes(audioAttr);


        this.soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);

        screen = createStartScreen();

        if(surfaceView.getWidth() > surfaceView.getHeight())
        {
            setVirtualScreen(480, 320);
        }
        else
        {
            setVirtualScreen(320, 480);
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public void onSensorChanged(SensorEvent event)
    {
        System.arraycopy(event.values, 0, accelerometer, 0, 3);
        accelerometer[0] = -1.0f * accelerometer[0];
    }

    public void setVirtualScreen(int width, int height)
    {
        if(virtualScreen != null)
        {
            virtualScreen.recycle();
        }
        virtualScreen = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        canvas = new Canvas(virtualScreen);
    }

    // Service methods, skal udfyldes når man udarbejder sit spil.

    public abstract Screen createStartScreen();

    public void setScreen(Screen screen)
    {
        if(this.screen != null)
        {
            this.screen.dispose();
        }
        this.screen = screen;
    }
    public Bitmap loadBitmap(String filename)
    {
        InputStream in = null;
        Bitmap bitmap = null;
        try
        {
            in = getAssets().open(filename);
            bitmap = BitmapFactory.decodeStream(in);
            if (bitmap == null)
            {
                throw new RuntimeException("*** Could not find the graphics file:   " + filename);
            }
            return bitmap;
        }
        catch (IOException ex)
        {
            throw new RuntimeException("*** Could not open the graphics file:   " + filename);
        }
        finally
        {
            if (in != null)
            {
                try
                {
                    in.close();
                }
                catch(IOException ex)
                {
                    // If we get to this point, everything worked anyway.
                }
            }
        }
    }



    public Music loadMusic(String filename)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(filename);
            return new Music(assetFileDescriptor);
        }
        catch(IOException ex)
        {
            throw new RuntimeException("Could not load music file: " + filename + "!!!!!!!!!!");
        }

    }
    public Sound loadSound(String filename)
    {
        try
        {
            AssetFileDescriptor assetFileDescriptor = getAssets().openFd(filename);
            int soundId = soundPool.load(assetFileDescriptor, 0);
            Sound sound = new Sound(soundPool, soundId);
            return sound;
        }
        catch (IOException ex)
        {
            throw new RuntimeException("Could not load sound from file: " + filename);
        }

    }

    public void clearFramedBuffer(int color)
    {
        canvas.drawColor(color);
    }
    public int getFramedBufferWidth()
    {
        return virtualScreen.getWidth();
    }
    public int getFramedBufferHeight()
    {
        return virtualScreen.getHeight();
    }
    public void drawBitmap(Bitmap bitmap, int x, int y)
    {
        if(canvas != null){canvas.drawBitmap(bitmap, x, y, null);}
    }

    public void drawBitmap(Bitmap bitmap, int x, int y, int srcX, int srcY, int srcWidth, int srcHeight)
    {
        Rect src = new Rect();
        Rect dst = new Rect();
        if(canvas == null)
        {
            return;
        }

        src.left = srcX;
        src.top = srcY;
        src.right = srcX + srcWidth;
        src.bottom = srcY + srcHeight;
        dst.left = x;
        dst.top = y;
        dst.right = x + srcWidth;
        dst.bottom = y + srcHeight;

        canvas.drawBitmap(bitmap, src, dst, null);
    }

    public boolean isTouchDown(int pointer)
    {
        return touchHandler.isTouchDown(pointer);
    }
    public int getTouchX(int pointer)
    {
        int virtualX = 0;
        virtualX = (int) (((float) touchHandler.getTouchX(pointer)/ (float) surfaceView.getWidth()) * (float) virtualScreen.getWidth());
        return virtualX;
    }
    public int getTouchY(int pointer)
    {
        int virtualY = 0;
        virtualY = (int) (((float)touchHandler.getTouchY(pointer)/ (float) surfaceView.getHeight()) * (float) virtualScreen.getHeight());
        return virtualY;
    }

    public float[] getAccelerometer()
    {
        return accelerometer;
    }

    private void fillEvents()
    {
        synchronized(touchEventBuffer)
        {
            int stop = touchEventBuffer.size();
            for(int i = 0; i < stop; i++)
            {
                touchEventCopied.add(touchEventBuffer.get(i)); // Copy all objects from one list to the other
            }
            touchEventBuffer.clear();
        }
    }

    private void freeEvents()
    {
        synchronized (touchEventCopied)
        {
            int stop = touchEventCopied.size();
            for(int i = 0; i < stop; i++)
            {
                touchEventPool.free(touchEventCopied.get(i)); // return all used objects to the free pool
            }
            touchEventCopied.clear();
        }
    }

    public List<TouchEvent> getTouchEvents()
    {
        return touchEventCopied;
    }

    public void onPause()
    {
        super.onPause();
        synchronized (stateChanges)
        {
            if(isFinishing())
            {
                soundPool.release();
                stateChanges.add(State.Disposed);
            }
            else
            {
                stateChanges.add(State.Paused);
            }
        }
        try
        {
            mainLoopThread.join();
        }
        catch (Exception e)
        {
            Log.d("GameEngine", "Something went kaput");
        }
        if(isFinishing())
        {
            ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).unregisterListener(this);
        }

    }

    public void onResume()
    {
        super.onResume();
        synchronized (stateChanges)
        {
            stateChanges.add(state.Resumed);
        }
        mainLoopThread = new Thread(this);
        mainLoopThread.start();
    }

    @Override
    public void run()
    {
        while(true)
        {
            synchronized (stateChanges)
            {
                int stopval = stateChanges.size();

                for(int i = 0; i < stopval; i++)
                {
                    state = stateChanges.get(i);
                    if(state == State.Disposed)
                    {
                        if(screen != null)
                        {
                            screen.dispose();
                        }
                        Log.d("GameEngine", "Main loop thread is disposed");
                        stateChanges.clear();
                        return;
                    }
                    if(state == State.Paused)
                    {
                        if(screen != null)
                        {
                            screen.pause();
                        }
                        Log.d("GameEngine", "Main loop thread is paused");
                        stateChanges.clear();
                        return;
                    }
                    if(state == State.Resumed)
                    {
                        if(screen != null)
                        {
                            screen.resume();
                        }
                        Log.d("GameEngine", "Main loop thread is resumed");
                        state = State.Running;
                    }
                }
                stateChanges.clear();
            }

            // After the synchronized state we can do the actual work of the thread.
            if(state == State.Running)
            {
                if(!this.surfaceHolder.getSurface().isValid())
                {
                    continue;
                }

                Canvas canvas = surfaceHolder.lockCanvas();
                // Now we can do all the drawing stuff
                //canvas.drawColor(Color.CYAN);

                fillEvents();
                if(screen != null)
                {
                    screen.update(0);
                }
                freeEvents();
                // After the screen has mad all gameobjects to the virtualScreen we need
                // to copy and resize the virtual screen to the actual/physical surfaceview

                src.left = 0;
                src.top = 0;
                src.right = virtualScreen.getWidth() - 1;
                src.bottom = virtualScreen.getHeight() - 1;
                dst.left = 0;
                dst.top = 0;
                dst.right = surfaceView.getWidth();
                dst.bottom = surfaceView.getHeight();
                canvas.drawBitmap(virtualScreen, src, dst, null);

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
