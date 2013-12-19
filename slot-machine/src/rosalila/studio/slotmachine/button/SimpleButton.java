package rosalila.studio.slotmachine.button;

import java.util.ArrayList;

import rosalila.studio.slotmachine.AnimationDrawable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SimpleButton extends Image
{
	ArrayList<Sound>button_sound;
	public SimpleButton(int x, int y, int rows, int columns, int width, int height, int current_position, String texture_path, float animation_velocity)
	{
		super(new AnimationDrawable(rows,columns,width,height,current_position,texture_path,animation_velocity));
		
		this.setPosition(x, y);
		
		globalInitializations();
	}
	
	public SimpleButton(int x, int y, Texture texture)
	{
		super(new AnimationDrawable(texture));
		
		this.setPosition(x, y);
		
		globalInitializations();
	}
	
	void globalInitializations()
	{
		button_sound=new ArrayList<Sound>();
		button_sound.add(Gdx.audio.newSound(Gdx.files.internal("data/button_sound/1.ogg")));
		button_sound.add(Gdx.audio.newSound(Gdx.files.internal("data/button_sound/2.ogg")));
		button_sound.add(Gdx.audio.newSound(Gdx.files.internal("data/button_sound/3.ogg")));
		button_sound.add(Gdx.audio.newSound(Gdx.files.internal("data/button_sound/4.ogg")));
		
		setColor(1.0f,1.0f,1.0f,0.65f);
		
        addCaptureListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
        	{
        		setColor(1.0f,1.0f,1.0f,1.0f);
        		onTouchDown();
        		button_sound.get(((AnimationDrawable)getDrawable()).current_row_animation).play(1.0f);
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	onTouchUp();
            	setColor(1.0f,1.0f,1.0f,0.65f);
            }
        });
	}
	
	public void onTouchDown()
	{
		
	}
	
	public void onTouchUp()
	{
		
	}
}
