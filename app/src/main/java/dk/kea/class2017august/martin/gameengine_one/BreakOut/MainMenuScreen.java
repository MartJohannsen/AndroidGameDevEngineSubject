package dk.kea.class2017august.martin.gameengine_one.BreakOut;

import android.graphics.Bitmap;
import android.provider.Settings;

import dk.kea.class2017august.martin.gameengine_one.GameEngine;
import dk.kea.class2017august.martin.gameengine_one.Screen;

/**
 * Created by Martin on 03-10-2017.
 */

public class MainMenuScreen extends Screen
{
    Bitmap mainMenu = null;
    Bitmap insertCoin = null;
    float passedTime = 0;
    long startTime = System.nanoTime();

    public MainMenuScreen(GameEngine gameEngine)
    {
        super(gameEngine);
        mainMenu = gameEngine.loadBitmap("BreakOutAssets/mainmenu.png");
        insertCoin = gameEngine.loadBitmap("BreakOutAssets/insertcoin.png");
    }

    @Override
    public void update(float deltaTime)
    {
        if(gameEngine.isTouchDown(0))
        {
            gameEngine.setScreen(new GameScreen(gameEngine));
            return;
        }
        gameEngine.drawBitmap(mainMenu, 0,0);
        passedTime = passedTime + deltaTime;

        if((passedTime - (int)passedTime) > 0.5f)
        {
            gameEngine.drawBitmap(insertCoin, (169 - insertCoin.getWidth()/2), 350);
        }
    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void dispose()
    {

    }
}
