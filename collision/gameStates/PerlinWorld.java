package gameStates;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import collisionGame.Constant;

import entity.*;


public class PerlinWorld extends State {

	
	public ArrayList<Entity> entities;
	
	public PerlinWorld()
	{
		this.id = 2;
		entities = new ArrayList<Entity>();
	}
	

	/**
	 * The last place that slick2d has to run before leaving this state
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		this.entities = new ArrayList<Entity>();	
	}
	
	/** 
	 * Randomly generate a point on the left end of the screen
	 * @param min minimum height
	 * @param max maximum height
	 * @return
	 */
	public Point randomGeneratorLL(float min, float max)
	{
		Random random = new Random();
		float ranFloat2 = random.nextFloat();
		float y = ranFloat2*(max - min) + min;
		return new Point(0, (int) y);
	}
	
	/**
	 * Randomly generate a point on the right end of the screen
	 * @param min minimum height
	 * @param max maximum height
	 * @return
	 */
	public Point randomGeneratorRL(float min, float max)
	{
		Random random = new Random();
		float ranFloat2 = random.nextFloat();
		float y = ranFloat2*(max - min) + min;
		return new Point(Constant.DISPLAY_WIDTH, (int) y);
	}
	/**
	 * Slick2d enter into this state from then enter() window
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		if (Constant.LEVEL == 5)
			game.enterState(5);
		
		super.enter(container, game);
		
		Constant.LEFT_LIMIT = randomGeneratorLL(350, 400);
		Constant.RIGHT_LIMIT = randomGeneratorRL(250, 300);
		
		this.addEntity(new PerlinLandscape());
		this.addEntity(new CannonBallSystem());
		this.addEntity(new Water());
		this.addEntity(new Avatar());
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		g.drawString("LEVEL: " + Constant.LEVEL, 50, 30);
		g.drawString("WINDSPEED  (+) to increase (-) to decrease: " + Constant.WIND.x, 400, 10);
		g.drawString("Firing Time Interval(ms): (0) to increase (9) to decrease: " + Constant.FIRING_TIME_INTERVAL, 400, 30);
		g.drawString("GRAVITY: "+Constant.GRAVITY.y, 400, 50);
		Input input = container.getInput();
		
		if (input.isKeyDown(Input.KEY_EQUALS))
		{
			Constant.WIND.x++;
		}
		
		if (input.isKeyDown(Input.KEY_MINUS))
		{
			Constant.WIND.x--;
		}
		
		if (input.isKeyDown(Input.KEY_0))
		{
			Constant.FIRING_TIME_INTERVAL++;
		}
		
		if (input.isKeyDown(Input.KEY_9))
		{
			Constant.FIRING_TIME_INTERVAL--;
		}
		
		
		for (Entity entity : entities)
		{
			entity.render(container, game, g);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		for (Entity entity : entities)
		{
			entity.update(container, game, delta);
		}
	}

	@Override
	public void addEntity(Entity entity) {
		// TODO Auto-generated method stub
			entity.setOwnerState(this);			
			entities.add(entity);
	}

	@Override
	public Entity getEntity(int id) {
		// TODO Auto-generated method stub
		for (Entity entity : entities)
		{
			if (entity.getId() == id)
				return entity;
		}
		return null;
	}

}
