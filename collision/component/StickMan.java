package component;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;

import collisionGame.Constant;

public class StickMan {
	
	public Image image;
	public Vector2f pos;
	public Vector2f vel;
	public Vector2f acc;
	
	public StickMan() throws SlickException
	{
		image = new Image("component/stickman.png");
		pos = new Vector2f(50,0);
		vel = new Vector2f(0,0);
		acc = new Vector2f(0,0);
	}
		
	public void updatePosition(float timeStep)
	{
		this.pos.x += vel.x * timeStep + 0.5 * acc.x * Math.pow(timeStep, 2);
		this.pos.y += vel.y * timeStep + 0.5 * acc.y * Math.pow(timeStep, 2);
		vel.x += acc.x * timeStep;
		vel.y += acc.y * timeStep;
	}
	
	public void setAcc(Vector2f acc)
	{
		this.acc.x = acc.x;
		this.acc.y = acc.y;
	}
	
	public float cenX()
	{
		return pos.x + Constant.HALF_HEIGHT;
	}
	
	public float cenY()
	{
		return pos.y + Constant.HALF_HEIGHT;
	}
	
	public float minX()
	{
		return pos.x;
	}
	
	public float maxX()
	{
		return pos.x + 2*Constant.HALF_HEIGHT;
	}
	
	public float minY()
	{
		return pos.y;
	}
	
	public float maxY()
	{
		return pos.y + 2*Constant.HALF_HEIGHT;
	}
}
