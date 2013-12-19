package rosalila.studio.slotmachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class WinAnimation extends Image
{
	public WinAnimation()
	{
		super(new AnimationDrawable(2,1,512,256,0,getAnim()));
		((AnimationDrawable)this.getDrawable()).animateRow(0,true);
		
	}
	
	static Animation[] getAnim()
	{
		
        Texture texture = new Texture(Gdx.files.internal("data/win.png"));
        int FRAME_COLS=2;
        int FRAME_ROWS=1;
        
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth() / FRAME_COLS, texture.getHeight() / FRAME_ROWS);
		
		Animation animations[] = new Animation[2];
		
		//Marruja
			TextureRegion texture_region[] = new TextureRegion[FRAME_COLS];
	        for (int j = 0; j < FRAME_COLS; j++) {
	        	texture_region[j] = tmp[0][j];
	        }
		//Slow
	        animations[0] = new Animation(1f, texture_region);
	    //Fast
	        animations[1] = new Animation(0.1f, texture_region);
		
		return animations;
	}
	
	void animatee()
	{
		((AnimationDrawable)this.getDrawable()).animateRow(0,true);
	}
	
	@Override
	public void act(float delta)
	{
		((AnimationDrawable) this.getDrawable()).act(delta);
		super.act(delta);
	}
	
	void animateBet(int bet_pos)
	{
		((AnimationDrawable)this.getDrawable()).animateRow(bet_pos,true);
	}
	
	void win()
	{
		((AnimationDrawable)this.getDrawable()).animateRow(1,true);
	}
	
	void unwin()
	{
		((AnimationDrawable)this.getDrawable()).animateRow(0,true);
	}
}
