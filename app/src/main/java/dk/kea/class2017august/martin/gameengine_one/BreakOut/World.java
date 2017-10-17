package dk.kea.class2017august.martin.gameengine_one.BreakOut;

/**
 * Created by Martin on 10-10-2017.
 */

public class World
{
    public static float MIN_X = 0;
    public static float MAX_X = 319;
    public static float MIN_Y = 36;
    public static float MAX_Y = 479;

    Ball ball = new Ball();
    public void update(float deltatime)
    {
        ball.x = (int)(ball.x + ball.vx * deltatime);
        ball.y = (int)(ball.y + ball.vy * deltatime);

        if(ball.x < MIN_X)
        {
            ball.vx = -ball.vx;
            ball.x = (int)MIN_X;
        }
        if (ball.x > MAX_X)
        {
            ball.vx = -ball.vx;
            ball.x = (int)(MAX_X - ball.WIDTH);
        }
        if(ball.y < MIN_Y)
        {
            ball.vy = -ball.vy;
            ball.y = (int)MIN_Y;
        }
        if (ball.y > MAX_Y)
        {
            ball.vy = -ball.vy;
            ball.y = (int)(MAX_Y-ball.HEIGHT);
        }



    }
}
