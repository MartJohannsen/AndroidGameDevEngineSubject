package dk.kea.class2017august.martin.gameengine_one;

/**
 * Created by Martin on 12-09-2017.
 */

public interface
TouchHandler
{
    public boolean isTouchDown(int pointer);
    public int getTouchX(int pointer);
    public int getTouchY(int pointer);
}
