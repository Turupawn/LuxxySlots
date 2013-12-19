package rosalila.studio.slotmachine.boxes;

import rosalila.studio.slotmachine.AnimationDrawable;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Box extends Image
{
	Animation animation;
	public int current_row;
	public String current_effect;
	int  original_x;
	int original_y;
	float original_width;
	float original_height;
	int rows;
	int columns;
	
	public Box(int x, int y, int rows, int columns, int sprite_width, int sprite_height, int current_row, String texture_path, float animation_velocity)
	{
		super(new AnimationDrawable(rows,columns,sprite_width,sprite_height, current_row,texture_path,animation_velocity));
		
		this.rows=rows;
		this.columns=columns;
		
		original_x = x;
		original_y = y;
		original_width = this.getWidth();
		original_height = this.getHeight();
		
		this.current_row = 0;

        setPosition(x, y);
        
//        current_effect = "bigger size";
	}
	
	@Override
	public void act(float delta)
	{
		((AnimationDrawable) this.getDrawable()).act(delta);
		super.act(delta);
		if(current_effect=="bigger size")
		{
			setScale((float)(getScaleX()+0.01));
			setPosition(original_x-(this.getWidth()*getScaleX()-this.getWidth())/2, original_y-(this.getHeight()*getScaleY()-this.getHeight())/2);
			if((float)(getScaleX()+0.1)>1.5)
			{
				current_effect = "smaller size";
			}
		}else if(current_effect=="smaller size")
		{
			setScale((float)(getScaleX()-0.01));
			setPosition(original_x-(this.getWidth()*getScaleX()-this.getWidth())/2, original_y-(this.getHeight()*getScaleY()-this.getHeight())/2);
			if((float)(getScaleX())<1.0)
			{
				setScale((float)1.0);
				setPosition(original_x-(this.getWidth()*getScaleX()-this.getWidth())/2, original_y-(this.getHeight()*getScaleY()-this.getHeight())/2);
				current_effect = "bigger size";
			}
		}else
		{
			setScale((float)1.0);
			setPosition(original_x-(this.getWidth()*getScaleX()-this.getWidth())/2, original_y-(this.getHeight()*getScaleY()-this.getHeight())/2);
		}
	}
	
	public void beginRoll()
	{
		current_row = 0;
		((AnimationDrawable) getDrawable()).animateRow(current_row,true);
	}
	
	public void endRoll()
	{
		current_row = (int)((Math.random()*1000)%(rows-1)+1);
		((AnimationDrawable) getDrawable()).animateRow(current_row,false);
		current_effect = "";
	}
}
