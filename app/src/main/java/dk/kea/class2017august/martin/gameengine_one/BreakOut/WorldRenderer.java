package dk.kea.class2017august.martin.gameengine_one.BreakOut;

import android.graphics.Bitmap;

import dk.kea.class2017august.martin.gameengine_one.GameEngine;

/**
 * Created by Martin on 10-10-2017.
 */

public class WorldRenderer
{
    GameEngine gameEngine;
    World world;
    Bitmap ballImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;
        ballImage = gameEngine.loadBitmap("BreakoutAssets/ball.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(ballImage,world.ball.x, world.ball.y );
    }
}
