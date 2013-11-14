package gameStates;

import java.awt.Point;
import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import collisionGame.Constant;

import entity.Avatar;
import entity.Entity;
import entity.PerlinLandscape;
import entity.Water;

public class WaterWorld extends State {


	public ArrayList<Entity> entities;
	
	public WaterWorld()
	{
		this.id = 3;
		entities = new ArrayList<Entity>();
	}


	@Override
	public void leave(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.leave(container, game);
		this.entities = new ArrayList<Entity>();	
	}
	
	
	@Override
	public void enter(GameContainer container, StateBasedGame game) throws SlickException
	{
		super.enter(container, game);
		
		Constant.LEFT_LIMIT = new Point(0, 300);
		Constant.RIGHT_LIMIT = new Point(Constant.DISPLAY_WIDTH, 300);
		
		this.addEntity(new PerlinLandscape());
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
		g.drawString("LEVEL: " + Constant.LEVEL, 50, 50);
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
