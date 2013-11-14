package entity;

import java.awt.Point;
import java.util.Stack;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.state.StateBasedGame;

import collisionGame.Constant;

public class Water extends Entity {

	Stack<Point> waterpoints; 
	PerlinLandscape landscape;
	public Water()
	{
		this.id = 4;
		waterpoints = new Stack<Point>();
	}

	/**
	 * find the marker points on the perlin terrain to draw the water
	 */
	public void findWaterPoints()
	{
		waterpoints.clear();
		if (owner.getID() == 3)
		{
			waterpoints.push(new Point(0,200));
			waterpoints.push(new Point(Constant.DISPLAY_WIDTH,200));
			return;
		}

		if (this.owner.getEntity(1) == null)
		{
			return;
		}
		landscape = (PerlinLandscape) this.owner.getEntity(1);
		Point[] points = landscape.points;
		int num = landscape.points.length;
		if (num < 3)
			return;
		
		int max = 0;
		int maxIndex = 0;
		for (int i=0; i<num; i++)
		{
			if (points[i].y > max)
			{
				maxIndex = i;
				max = points[i].y;
			}
		}
		
		for (int i = maxIndex; i>=0; i--)
		{
			if (points[i].y < points[maxIndex].y-Constant.DEPTH)
			{
				waterpoints.push(points[i]);
				break;
			}
		}
		
		for (int i=maxIndex; i<num; i++)
		{
			if (points[i].y < points[maxIndex].y-Constant.DEPTH)
			{
				waterpoints.push(points[i]);
				break;
			}
		} 
	}	

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		// TODO Auto-generated method stub
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
			findWaterPoints();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		// TODO Auto-generated method stub
			findWaterPoints();

		if (waterpoints == null || waterpoints.size() == 0)
			return;
		
		if (owner.getID() == 3)
		{
			gr.drawLine(Constant.PITLEFT.x, Constant.PITLEFT.y, 
					Constant.PITLEFT.x, Constant.DISPLAY_HEIGHT);
			gr.drawLine(Constant.PITRIGHT.x, Constant.PITRIGHT.y, 
					Constant.PITRIGHT.x, Constant.DISPLAY_HEIGHT);
			return;
		}
		boolean connect = true;		
		Point prev = waterpoints.pop();
		while (waterpoints.size() != 0)
		{
			if (connect)
			{
				gr.drawLine(prev.x, prev.y, waterpoints.peek().x, waterpoints.peek().y);
				connect = false;
			}
			else 
				connect = true;
			prev = waterpoints.pop();
		}

	}

}
