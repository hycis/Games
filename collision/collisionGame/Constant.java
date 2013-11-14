package collisionGame;

import java.awt.Point;

import org.newdawn.slick.geom.Vector2f;

public class Constant {
	
	/** GENERAL */
	public final static float TIMESTEP = (float) 0.03;
	public final static int FPS = 50;
	public final static int DISPLAY_WIDTH = 1024;
	public final static int DISPLAY_HEIGHT = 600;
	public final static Vector2f GRAVITY = new Vector2f(0, 50);
	public static Vector2f WIND = new Vector2f(0, 0);
	public static int LEVEL = 1;
	
	/** Avatar Constants */
	public final static float HALF_HEIGHT = 30;
	public final static float MAX_VELOCITY = 100;
	public final static float DELTA_FACTOR = (float) 0.004;
	public final static float INTERNAL_ACC_MAG = 30;
	public final static Vector2f WEIGHT = new Vector2f(0, 300);
	public final static Vector2f JUMP_VEL = new Vector2f(0, -300);
	
	/** PerlinLandscape Constants */
	public static int SEED = 11;
	public static int OCTAVE = 8;
	public final static int AMPLITUDE = 250;
	public static Point LEFT_LIMIT = new Point(0, 300);
	public static Point RIGHT_LIMIT = new Point(DISPLAY_WIDTH, 300);
	
	/** CannonBallSystem Constants 
	* CannonBalls Initial firing metrics */
	public final static Vector2f INITIAL_POSITION = new Vector2f(0, 0);
	public final static Vector2f INITIAL_VELOCITY = new Vector2f(1, 20);
	public final static Vector2f INITIAL_ACCELERATION = new Vector2f(100, 1);
	public final static float RADIUS = 10;
	public static float FIRING_TIME_INTERVAL = 2000; 
	
	/** Water Constants */
	//public final static float WATER_HEIGHT = 420;
	public final static int DEPTH = 20;
	public final static Vector2f PITLEFT = new Vector2f(500, 300);
	public final static Vector2f PITRIGHT = new Vector2f(600, 300);
}
