package dk.kea.class2017august.martin.gameengine_one;

import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by Martin on 12-09-2017.
 */

public class MultiTouchHandler implements TouchHandler, View.OnTouchListener
{
    private boolean[] isTouched = new boolean[20];
    private int[] touchX = new int[20];
    private int[] touchY = new int[20];
    private List<TouchEvent> touchEventBuffer;
    private TouchEventPool touchEventPool;


    public MultiTouchHandler(View v, List<TouchEvent> touchEventBuffer, TouchEventPool touchEventpool)
    {
        v.setOnTouchListener(this);
        this.touchEventBuffer = touchEventBuffer;
        this.touchEventPool = touchEventpool;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        TouchEvent touchEvent = null;
        int action = event.getAction() & MotionEvent.ACTION_MASK;
        int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT; // More fiddling will commence here
        int pointerId = event.getPointerId(pointerIndex);

        switch(action)
        {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.obtains();
                touchEvent.type = TouchEvent.TouchEventType.Down;
                touchEvent.pointer = pointerId;
                touchEvent.x = (int) event.getX(pointerIndex);
                touchX[pointerId] = touchEvent.x;
                touchEvent.y = (int) event.getY(pointerIndex);
                touchY[pointerId] = touchEvent.y;
                isTouched[pointerId] = true;
                synchronized (touchEventBuffer)
                {
                    touchEventBuffer.add(touchEvent);
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.obtains();
                touchEvent.type = TouchEvent.TouchEventType.Up;
                touchEvent.pointer = pointerId;
                touchEvent.x = (int) event.getX(pointerIndex);
                touchX[pointerId] = touchEvent.x;
                touchEvent.y = (int) event.getY(pointerIndex);
                touchY[pointerId] = touchEvent.y;
                isTouched[pointerId] = false;
                synchronized (touchEventBuffer)
                {
                    touchEventBuffer.add(touchEvent);
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                synchronized (touchEventBuffer)
                {
                    for(int i = 0; i < pointerCount; i++)
                    {
                        touchEvent = touchEventPool.obtains();
                        touchEvent.type = TouchEvent.TouchEventType.Dragged;
                        pointerIndex = i;
                        pointerId = event.getPointerId(pointerIndex);
                        touchEvent.pointer = pointerId;
                        touchEvent.x = (int) event.getX(pointerIndex);
                        touchX[pointerId] = touchEvent.x;
                        touchEvent.y = (int) event.getY(pointerIndex);
                        touchY[pointerId] = touchEvent.y;
                        isTouched[pointerId] = true;
                        touchEventBuffer.add(touchEvent);
                    }
                }
                return true;
        }
        return true;
    }

    @Override
    public boolean isTouchDown(int pointer)
    {
        return isTouched[pointer];
    }

    @Override
    public int getTouchX(int pointer)
    {
        return touchX[pointer];
    }

    @Override
    public int getTouchY(int pointer)
    {
        return touchY[pointer];
    }
}
