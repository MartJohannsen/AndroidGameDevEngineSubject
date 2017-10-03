package dk.kea.class2017august.martin.gameengine_one;

/**
 * Created by Martin on 12-09-2017.
 */

public class TouchEventPool extends Pool<TouchEvent>
{
    @Override
    protected TouchEvent newItem()
    {
        return new TouchEvent();
    }

}
