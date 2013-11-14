package collisionGame;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.Log;

import gameStates.*;



public class CollisionGame extends StateBasedGame {

	
	public CollisionGame()
	{
		super("Collision Game");

	}

	public static void main(String[] args) {
		try {
			Log.setVerbose(true);

			AppGameContainer app = new AppGameContainer(new CollisionGame());
			app.setDisplayMode(Constant.DISPLAY_WIDTH, Constant.DISPLAY_HEIGHT, false);
			app.setShowFPS(true);
			app.setTargetFrameRate(Constant.FPS);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		// TODO Auto-generated method stub
		
		this.addState(new StartMenu());
		this.addState(new PerlinWorld());
		this.addState(new WaterWorld());
		this.addState(new EndMenu());
		this.addState(new Win());
	}

}
