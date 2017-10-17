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
    Bitmap paddleImage;
    Bitmap blocksImage;

    public WorldRenderer(GameEngine gameEngine, World world)
    {
        this.gameEngine = gameEngine;
        this.world = world;
        ballImage = gameEngine.loadBitmap("BreakOutAssets/ball.png");
        paddleImage = gameEngine.loadBitmap("BreakOutAssets/paddle.png");
        blocksImage = gameEngine.loadBitmap("BreakOutAssets/blocks.png");
    }

    public void render()
    {
        gameEngine.drawBitmap(ballImage,world.ball.x, world.ball.y );
        gameEngine.drawBitmap(paddleImage,(int)world.paddle.x, (int) world.paddle.y);

        int blockListSize = world.blocks.size();
        Block block = null;

        for(int i = 0; i < blockListSize; i++)
        {
            block = world.blocks.get(i);
            gameEngine.drawBitmap(blocksImage, (int) block.x, (int) block.y,
                    0, block.type * (int)Block.HEIGHT,
                    (int)Block.WIDTH, (int) Block.HEIGHT);
        }
    }
}
