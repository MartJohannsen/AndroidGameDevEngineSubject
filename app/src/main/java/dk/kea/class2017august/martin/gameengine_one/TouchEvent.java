package dk.kea.class2017august.martin.gameengine_one;

/**
 * Created by Martin on 12-09-2017.
 */

public class TouchEvent
{
    public enum TouchEventType
    {
        Down,
        Up,
        Dragged
    }

    public TouchEventType type;
    public int x;
    public int y;
    public int pointer;


}
