package entity;

import collisionGame.Constant;

import java.awt.Point;
import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.state.StateBasedGame;

public class PerlinLandscape extends Entity {

	Point[] points;
	long seed; 
	int octave;
	Random random;
	int amplitude;
	Point leftLimit;
	Point rightLimit;
	
	public PerlinLandscape() 
	{
		this.id = 1;
		this.octave = 0;
		this.seed = Constant.SEED;
		this.random = new Random(seed);
		this.amplitude = Constant.AMPLITUDE;
		this.leftLimit = Constant.LEFT_LIMIT;
		this.rightLimit = Constant.RIGHT_LIMIT;
		
		int numOfSegments = (int)Math.pow(2, Constant.OCTAVE);
		this.points = new Point[numOfSegments+1];
	}
	
	public void generatePerlin() 
	{
		if (owner.getID() == 3)
		{
			this.octave = 0;
			this.points = new Point[2];
		}
		
		else
			this.octave = Constant.OCTAVE;
			
		if (this.octave == 0)
		{
			points[0] = leftLimit;
			points[1] = rightLimit;
			return;
		}
		
		int numOfSegments = (int)Math.pow(2, this.octave);
		this.points = new Point[numOfSegments+1];
		
		points[0] = leftLimit;
		points[(int)Math.pow(2, this.octave)] = rightLimit;
		for (int oct=1; oct<=this.octave; oct++)
		{
			spanOctavePoints(oct);
		}
	}
	boolean flag = true;

	/**
	 * span the octave points 
	 * @param oct the octave of the perlin
	 */
	private void spanOctavePoints(int oct)
	{
		int numOfSegments = (int)Math.pow(2, oct);
		int len = points.length;
		int lastPos = len - 1;
		int startJump = lastPos / numOfSegments;
		for (int pos=startJump; pos<=lastPos-startJump; pos+=2*startJump)
		{
			this.points[pos] = midpoint(points[pos-startJump], points[pos+startJump]);
			double amp = 2 * this.amplitude / numOfSegments;
			double rand = this.random.nextDouble() * 2 - 1;
			double newAmp = rand * amp;
			this.points[pos].y += (int)newAmp;
		}
	}
	
	private Point midpoint(Point p1, Point p2) 
	{
		int midX = (p1.x + p2.x) / 2;
		int midY = (p1.y + p2.y) / 2;
		return new Point(midX, midY);
	}

	/**
	 * Sick2d framework to take in the generated perlin points
	 */
	@Override
	public void update(GameContainer gc, StateBasedGame sb, int delta) {
		// TODO Auto-generated method stub
		this.random.setSeed(this.seed);
		this.generatePerlin();
	}
	/**
	 * slick2d framework to render the perlin points on the screen
	 */
	@Override
	public void render(GameContainer gc, StateBasedGame sb, Graphics gr) {
		// TODO Auto-generated method stub
		
		int len = this.points.length;
		for (int i=0; i<len-1; i++)
		{
			Point pt = this.points[i];
			Point nextPt = this.points[i+1];
			gr.draw(new Line(pt.x, pt.y, nextPt.x, nextPt.y));
		}
		
	}

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		// TODO Auto-generated method stub
		
	}
	
}
