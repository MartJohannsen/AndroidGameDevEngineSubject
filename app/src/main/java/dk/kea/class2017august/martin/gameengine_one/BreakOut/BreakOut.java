package dk.kea.class2017august.martin.gameengine_one.BreakOut;

import dk.kea.class2017august.martin.gameengine_one.GameEngine;
import dk.kea.class2017august.martin.gameengine_one.Screen;

/**
 * Created by Martin on 03-10-2017.
 */

public class BreakOut extends GameEngine
{

    @Override
    public Screen createStartScreen()
    {
        return new MainMenuScreen(this);
    }
}
