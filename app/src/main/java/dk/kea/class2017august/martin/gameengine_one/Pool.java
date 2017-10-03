package dk.kea.class2017august.martin.gameengine_one;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 12-09-2017.
 */

public abstract class Pool <T>
{
    private List<T> items = new ArrayList<>();
    protected abstract T newItem();

    public T obtains()
    {
        int size = items.size();
        if(size == 0) return newItem();
        return items.remove(size-1);
    }

    public void free(T item)
    {
        items.add(item);
    }
}
