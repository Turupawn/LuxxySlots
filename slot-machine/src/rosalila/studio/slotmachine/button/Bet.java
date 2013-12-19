package rosalila.studio.slotmachine.button;

import java.util.ArrayList;

import rosalila.studio.slotmachine.AnimationDrawable;
import rosalila.studio.slotmachine.SlotLogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Bet extends SimpleButton{
	ArrayList<Double>bets;
	int current_bet;
	Label label_total_bet;
	public SlotLogic slot_logic;
	
	public Bet(int x, int y, ArrayList<Double>bets_param, Label label_total_bet_param)
	{
		super(x,y,1,4,128,64,0,"data/bet.png",0.1f);
		
		this.bets = bets_param;
		this.label_total_bet=label_total_bet_param;
        
        current_bet = 0;
        animateBet(current_bet);
	}

	@Override
	public void act(float delta)
	{
		((AnimationDrawable) this.getDrawable()).act(delta);
		super.act(delta);
	}
	
	void animateBet(int bet_pos)
	{
		((AnimationDrawable) getDrawable()).animateRow(bet_pos,true);
	}
	
	public double getValue()
	{
		return bets.get(current_bet);
	}
	
	@Override
	public void onTouchDown()
	{
		if(slot_logic.is_rolling)
			return;
		
		current_bet++;
		if(current_bet>=bets.size())
			current_bet=0;
		animateBet(current_bet);
		label_total_bet.setText(slot_logic.getTotalBetString());
	}
	
	public int getMultiplier()
	{
		if(current_bet==1)
			return 1;
		if(current_bet==2)
			return 5;
		if(current_bet==3)
			return 10;
		if(current_bet==4)
			return 20;
		return 1;
	}
}
