package rosalila.studio.slotmachine.button;

import java.util.ArrayList;

import rosalila.studio.slotmachine.AnimationDrawable;
import rosalila.studio.slotmachine.SlotLogic;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Lines extends SimpleButton {
	int lines;
	int max_lines;
	Label label_total_bet;
	public SlotLogic slot_logic;
	ArrayList<Image>lines_ui;
	public Lines(int x, int y, int max_lines_param, Label label_total_bet_param,ArrayList<Image>lines_ui_param)
	{
		super(x,y,1,4,128,64,0,"data/lines.png",0.1f);
		
		this.max_lines = max_lines_param;
		this.label_total_bet=label_total_bet_param;
		this.lines_ui=lines_ui_param;
		
        lines = 1;
        animateLines(lines);
	}

	@Override
	public void act(float delta)
	{
		((AnimationDrawable) this.getDrawable()).act(delta);
		super.act(delta);
	}
	
	void animateLines(int lines)
	{
		((AnimationDrawable) getDrawable()).animateRow(lines-1,true);
		if(lines==1)
		{
			lines_ui.get(0).setVisible(false);
			lines_ui.get(1).setVisible(true);
			lines_ui.get(2).setVisible(false);
		}
		if(lines==2)
		{
			lines_ui.get(0).setVisible(true);
			lines_ui.get(1).setVisible(false);
			lines_ui.get(2).setVisible(true);
		}
		if(lines==3)
		{
			lines_ui.get(0).setVisible(true);
			lines_ui.get(1).setVisible(true);
			lines_ui.get(2).setVisible(true);
		}
	}
	
	public int getValue()
	{
		return lines;
	}
	
	public void hideLinesUI()
	{
		for(int i=0;i<lines_ui.size();i++)
		{
			lines_ui.get(i).setVisible(false);
		}
	}
	
	@Override
	public void onTouchDown()
	{
		if(slot_logic.is_rolling)
			return;
		lines++;
		if(lines>max_lines)
			lines=1;
		animateLines(lines);
		label_total_bet.setText(slot_logic.getTotalBetString());
	}
}