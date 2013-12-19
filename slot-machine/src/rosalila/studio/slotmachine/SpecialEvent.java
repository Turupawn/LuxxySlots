package rosalila.studio.slotmachine;

import java.util.ArrayList;

import rosalila.studio.slotmachine.boxes.SpecialEventBox;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SpecialEvent extends Actor
{
	Image background;
	Image continue_btn;
	ArrayList<SpecialEventBox>boxes;
	SlotLogic slot_logic;
	Stage stage;
	Label label_wining;
	public SpecialEvent(SlotLogic slot_logic_param, Stage stage, Label label_wining)
	{
		this.background = new Image(new AnimationDrawable(2,1,1024,512,0,"data/special_event_bg.png",0.10f));
		((AnimationDrawable)this.background.getDrawable()).animateRow(0, true);
		this.label_wining=label_wining;
		
		this.slot_logic=slot_logic_param;
		this.stage=stage;
		
		this.continue_btn = new Image(new Texture("data/continue.png"));
		continue_btn.setPosition(400, 150);
		continue_btn.setVisible(false);
		
		this.continue_btn.addCaptureListener(new InputListener()
        {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	slot_logic.addCredits(getPrize());
            	unactivate();
            }
        });
		
		boxes=new ArrayList<SpecialEventBox>();
		boxes.add(new SpecialEventBox(270, 220,this));
		boxes.add(new SpecialEventBox(370, 220,this));
		boxes.add(new SpecialEventBox(470, 220,this));
		boxes.add(new SpecialEventBox(270, 120,this));
		boxes.add(new SpecialEventBox(370, 120,this));
		boxes.add(new SpecialEventBox(470, 120,this));
		
		stage.addActor(background);
		
		for(int i=0;i<boxes.size();i++)
		{
			stage.addActor(boxes.get(i));
		}
		
		stage.addActor(continue_btn);
		stage.addActor(label_wining);
		
		slot_logic_param.special_event=this;
		
		unactivate();
	}
	
//	@Override
//	public void draw(SpriteBatch batch, float parentAlpha)
//	{
//		super.draw(batch, parentAlpha);
//	}
	
	@Override
	public void act(float delta)
	{
		super.act(delta);
		((AnimationDrawable)background.getDrawable()).act(delta);
		if(gameOver())
			continue_btn.setVisible(true);
		label_wining.setText("Premio: "+getPrize());
	}
	
	public boolean gameOver()
	{
		int cont=0;
		for(int i=0;i<boxes.size();i++)
		{
			if(boxes.get(i).current_row!=0)
				cont++;
		}
		return cont>=3;
	}
	
	public void activate()
	{
		background.setVisible(true);
		continue_btn.setVisible(false);
		label_wining.setVisible(true);
		for(int i=0;i<boxes.size();i++)
		{
			boxes.get(i).reset();
		}
	}
	
	public void unactivate()
	{
		background.setVisible(false);
		continue_btn.setVisible(false);
		label_wining.setVisible(false);
		for(int i=0;i<boxes.size();i++)
		{
			boxes.get(i).setVisible(false);
			boxes.get(i).current_row=0;
		}
	}
	
	public int getPrize()
	{
		int prize=0;
		for(int i=0;i<boxes.size();i++)
		{
			prize+=boxes.get(i).current_row*100;
		}
		return prize;
	}
}
