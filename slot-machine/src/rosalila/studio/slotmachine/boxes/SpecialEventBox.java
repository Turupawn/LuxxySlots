package rosalila.studio.slotmachine.boxes;

import rosalila.studio.slotmachine.AnimationDrawable;
import rosalila.studio.slotmachine.SpecialEvent;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class SpecialEventBox extends Box
{
	SpecialEvent special_event;
	Sound win_sound;
	public SpecialEventBox(int x, int y, SpecialEvent special_event_param)
	{
		super(x,y,4,8,64,64,0,"data/frutas.png",0.1f);
		
		win_sound = Gdx.audio.newSound(Gdx.files.internal("data/win_sound.ogg"));
		
		((AnimationDrawable) getDrawable()).animateRow(0,true);
		
		this.special_event = special_event_param;
		
        addCaptureListener(new InputListener()
        {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	if(current_row==0 && !special_event.gameOver())
            	{
            		win_sound.play(1.0f);
            		endRoll();
            	}
            }
        });
	}
	
	public void reset()
	{
		current_row=0;
		((AnimationDrawable) getDrawable()).animateRow(current_row,true);
		setVisible(true);
	}
}
