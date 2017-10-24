package dk.kea.class2017august.martin.gameengine_one.BreakOut;

import android.graphics.Bitmap;

import dk.kea.class2017august.martin.gameengine_one.GameEngine;
import dk.kea.class2017august.martin.gameengine_one.Screen;
import dk.kea.class2017august.martin.gameengine_one.State;

/**
 * Created by Martin on 03-10-2017.
 */

public class GameScreen extends Screen
{
    enum State
    {
        Paused,
        Running,
        GameOver
    }

    Bitmap background = null;
    Bitmap resume = null;
    Bitmap gameOver = null;
    State state = State.Running;

    World world = null;
    WorldRenderer worldRenderer = null;

    public GameScreen(GameEngine gameEngine) {
        super(gameEngine);
        world = new World(gameEngine);
        worldRenderer = new WorldRenderer(gameEngine, world);
        background = gameEngine.loadBitmap("BreakOutAssets/background.png");
        resume = gameEngine.loadBitmap("BreakOutAssets/resume.png");
        gameOver = gameEngine.loadBitmap("BreakOutAssets/gameover.png");

    }

    @Override
    public void update(float deltaTime)
    {
        if(state == State.Paused && gameEngine.getTouchEvents().size() > 0)
        {
            state = State.Running;
        }

        if(state == State.GameOver && gameEngine.getTouchEvents().size() > 0)
        {
            gameEngine.setScreen(new MainMenuScreen(gameEngine));
            return;
        }
        if(state == State.Running && gameEngine.getTouchY(0) < 38 && gameEngine.getTouchX(0) > 320-38)
        {
            state = State.Paused;
            return;
        }
        gameEngine.drawBitmap(background, 0, 0);

        if(state == State.Running)
        {
            world.update(deltaTime, gameEngine.getAccelerometer()[0]);
        }
        worldRenderer.render();

        if(state == State.Paused)
        {
            gameEngine.drawBitmap(resume, 60 - resume.getWidth()/2, 240 - resume.getHeight()/2);
        }
        if(state == State.GameOver)
        {
            gameEngine.drawBitmap(gameOver, 160 - gameOver.getWidth()/2, 240- gameOver.getHeight()/2);
        }




    }

    @Override
    public void pause() {
        if(state == State.Running)
        {
            state = State.Paused;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
