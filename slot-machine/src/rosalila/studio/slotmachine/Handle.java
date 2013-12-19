package rosalila.studio.slotmachine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Handle extends Image {
	String state;
	Sound bet_sound;
	
	public Handle(int x, int y, final SlotLogic slot_logic, final SlotMachine activity)
	{
		super(new AnimationDrawable(4,4,128,256,0,"data/handle.png",0.10f));
		
		bet_sound = Gdx.audio.newSound(Gdx.files.internal("data/bet_sound.ogg"));

        setPosition(x, y);
        addCaptureListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		if(slot_logic.canRoll())
        		{
        			((AnimationDrawable) getDrawable()).animateRow(1,false);
        		}
        		
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            	if(slot_logic.canRoll())
            	{
            		bet_sound.play(1.0f);
            		
	        		slot_logic.beginRoll();
	        		
	        		Runnable myRunnable = new Runnable()
	        		{
	        			public void run(){
	        				try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	        				((AnimationDrawable) getDrawable()).animateRow(2,false);
	        				slot_logic.endRoll();
	        			}
	        		};
	        		
	        		Thread thread = new Thread(myRunnable);
	        		thread.start();
            	}
            }
        });
        state = "animating";
        ((AnimationDrawable) getDrawable()).animateRow(0,true);
	}

	@Override
	public void act(float delta)
	{
		((AnimationDrawable) this.getDrawable()).act(delta);
		super.act(delta);
	}
}