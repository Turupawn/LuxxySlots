package rosalila.studio.slotmachine;

import java.util.ArrayList;

import rosalila.studio.slotmachine.boxes.SlotBox;
import rosalila.studio.slotmachine.button.Bet;
import rosalila.studio.slotmachine.button.Lines;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class SlotLogic {
	ArrayList< ArrayList<SlotBox> > boxes;
	ArrayList<Powerup> powerups;
	Handle handle;
	Lines lines;
	Bet bet;
	float credits;
	float credits_displayed;
	float credits_saved;
	WinAnimation win;
	SpecialEvent special_event;
	
	Label label_credits;
	Label label_total_bet;
	Label label_last_prize;
	double last_prize;
	public boolean is_rolling=false;
	Sound win_sound;
	
	private final AndroidFunctionsInterface androidFunctions;
	
	public SlotLogic(ArrayList< ArrayList<SlotBox> > boxes, float credits, WinAnimation win, Lines lines, Bet bet, Label label_credits, Label label_total_bet, Label label_last_prize, ArrayList<Powerup> powerups,AndroidFunctionsInterface androidFunctions_param)
	{
		this.boxes = boxes;
		this.credits = credits;
		this.credits_saved = credits;
		this.win = win;
		this.lines=lines;
		this.bet=bet;
		this.label_credits=label_credits;
		this.label_total_bet=label_total_bet;
		this.label_last_prize=label_last_prize;
		this.powerups=powerups;
		this.androidFunctions=androidFunctions_param;
		last_prize=0;
		win_sound = Gdx.audio.newSound(Gdx.files.internal("data/win_sound.ogg"));
	}
	
	void beginRoll()
	{
		is_rolling=true;
		
		lines.hideLinesUI();
		for(int i=0;i<powerups.size();i++)
		{
			powerups.get(i).value--;
			if(powerups.get(i).value<0)
				powerups.get(i).value=0;
			powerups.get(i).act(0);
		}
		addCredits((int)-getTotalBet());
		for(int i=0;i<boxes.size();i++)
		{
			for(int j=0;j<boxes.get(i).size();j++)
			{
				boxes.get(i).get(j).beginRoll();
				boxes.get(i).get(j).current_effect="";
			}
		}
		win.unwin();
		
		if((int)((Math.random()*1000)%5)==1)
			special_event.activate();
	}
	
	void endRoll()
	{
		for(int i=0;i<boxes.size();i++)
		{
			for(int j=0;j<boxes.get(i).size();j++)
				boxes.get(i).get(j).endRoll();
		}
		
		double prize=0;
		if(lines.getValue()==1)
		{
			prize+=winCombo6(1);
			prize+=winCombo5(1);
			prize+=winCombo4(1);
			prize+=winCombo3(1);
			prize+=winCombo2(1);
		}
		
		if(lines.getValue()==2)
		{
			prize+=winCombo6(0);
			prize+=winCombo5(0);
			prize+=winCombo4(0);
			prize+=winCombo3(0);
			prize+=winCombo2(0);
			
			prize+=winCombo6(2);
			prize+=winCombo5(2);
			prize+=winCombo4(2);
			prize+=winCombo3(2);
			prize+=winCombo2(2);
		}
		
		if(lines.getValue()==3)
		{
			prize+=winCombo6(0);
			prize+=winCombo5(0);
			prize+=winCombo4(0);
			prize+=winCombo3(0);
			prize+=winCombo2(0);
			
			prize+=winCombo6(1);
			prize+=winCombo5(1);
			prize+=winCombo4(1);
			prize+=winCombo3(1);
			prize+=winCombo2(1);
			
			prize+=winCombo6(2);
			prize+=winCombo5(2);
			prize+=winCombo4(2);
			prize+=winCombo3(2);
			prize+=winCombo2(2);
		}

		if(prize!=0)
		{
			win.win();
			last_prize=(int)prize*bet.getMultiplier();
			addCredits((int)prize*bet.getMultiplier());
			win_sound.play(1.0f);
		}
		
		label_last_prize.setText(getLastPrizeString());
		androidFunctions.SubmitScore(credits);
		
		is_rolling=false;
	}
	
	double winCombo6(int row)
	{
		double prize = 0;
		for(int col=0;col<1;col++)
		{
			if(boxes.get(col).get(row).current_row == boxes.get(col+1).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+2).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+3).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+4).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+5).get(row).current_row
					
					&& boxes.get(col).get(row).current_effect == ""
					&& boxes.get(col+1).get(row).current_effect == ""
					&& boxes.get(col+2).get(row).current_effect == ""
					&& boxes.get(col+3).get(row).current_effect == ""
					&& boxes.get(col+4).get(row).current_effect == ""
					&& boxes.get(col+5).get(row).current_effect == "")
			{
				double new_prize = getPrize(6,boxes.get(col).get(row).current_row);
				if(new_prize!=0)
				{
					prize+=new_prize;
					boxes.get(col).get(row).current_effect = "bigger size";
					boxes.get(col+1).get(row).current_effect = "bigger size";
					boxes.get(col+2).get(row).current_effect = "bigger size";
					boxes.get(col+3).get(row).current_effect = "bigger size";
					boxes.get(col+4).get(row).current_effect = "bigger size";
					boxes.get(col+5).get(row).current_effect = "bigger size";
				}
			}
		}
		return prize;
	}
	
	double winCombo5(int row)
	{
		double prize = 0;
		for(int col=0;col<2;col++)
		{
			if(boxes.get(col).get(row).current_row == boxes.get(col+1).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+2).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+3).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+4).get(row).current_row
					
					&& boxes.get(col).get(row).current_effect == ""
					&& boxes.get(col+1).get(row).current_effect == ""
					&& boxes.get(col+2).get(row).current_effect == ""
					&& boxes.get(col+3).get(row).current_effect == ""
					&& boxes.get(col+4).get(row).current_effect == "")
			{
				double new_prize = getPrize(5,boxes.get(col).get(row).current_row);
				if(new_prize!=0)
				{
					prize+=new_prize;
					boxes.get(col).get(row).current_effect = "bigger size";
					boxes.get(col+1).get(row).current_effect = "bigger size";
					boxes.get(col+2).get(row).current_effect = "bigger size";
					boxes.get(col+3).get(row).current_effect = "bigger size";
					boxes.get(col+4).get(row).current_effect = "bigger size";
				}
			}
		}
		return prize;
	}

	double winCombo4(int row)
	{
		double prize = 0;
		for(int col=0;col<3;col++)
		{
			if(boxes.get(col).get(row).current_row == boxes.get(col+1).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+2).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+3).get(row).current_row
					
					&& boxes.get(col).get(row).current_effect == ""
					&& boxes.get(col+1).get(row).current_effect == ""
					&& boxes.get(col+2).get(row).current_effect == ""
					&& boxes.get(col+3).get(row).current_effect == "")
			{
				double new_prize = getPrize(4,boxes.get(col).get(row).current_row);
				if(new_prize!=0)
				{
					prize+=new_prize;
					boxes.get(col).get(row).current_effect = "bigger size";
					boxes.get(col+1).get(row).current_effect = "bigger size";
					boxes.get(col+2).get(row).current_effect = "bigger size";
					boxes.get(col+3).get(row).current_effect = "bigger size";
				}
			}
		}
		return prize;
	}
	
	double winCombo3(int row)
	{
		double prize = 0;
		for(int col=0;col<4;col++)
		{
			if(boxes.get(col).get(row).current_row == boxes.get(col+1).get(row).current_row
					&& boxes.get(col).get(row).current_row == boxes.get(col+2).get(row).current_row
					
					&& boxes.get(col).get(row).current_effect == ""
					&& boxes.get(col+1).get(row).current_effect == ""
					&& boxes.get(col+2).get(row).current_effect == "")
			{
				double new_prize = getPrize(3,boxes.get(col).get(row).current_row);
				if(new_prize!=0)
				{
					prize+=new_prize;
					boxes.get(col).get(row).current_effect = "bigger size";
					boxes.get(col+1).get(row).current_effect = "bigger size";
					boxes.get(col+2).get(row).current_effect = "bigger size";
				}
			}
		}
		return prize;
	}
	
	double winCombo2(int row)
	{
		double prize = 0;
		for(int col=0;col<5;col++)
		{
			if(boxes.get(col).get(row).current_row == boxes.get(col+1).get(row).current_row
					
					&& boxes.get(col).get(row).current_effect == ""
					&& boxes.get(col+1).get(row).current_effect == "")
			{
				double new_prize = getPrize(2,boxes.get(col).get(row).current_row);
				if(new_prize!=0)
				{
					prize+=new_prize;
					boxes.get(col).get(row).current_effect = "bigger size";
					boxes.get(col+1).get(row).current_effect = "bigger size";
				}
			}
		}
		return prize;
	}
	
	String getCreditsString()
	{
		return "Credits:\n"+credits_displayed;
	}
	
	public String getTotalBetString()
	{
		return "Total bet:\n"+getTotalBet();
	}
	
	String getLastPrizeString()
	{
		return "Last prize:\n"+last_prize;
	}
	
	double getTotalBet()
	{
		return lines.getValue()*bet.getValue();
	}
	
	boolean canRoll()
	{
		return getTotalBet()<=credits && !is_rolling;
	}
	
	double getPrize(int combo,int current_row)
	{
		if(current_row==1)
		{
			if(combo==6)
				return 1000;
			if(combo==5)
				return 700;
			if(combo==4)
				return 380;
			if(combo==3)
				return 200;
			if(combo==2)
				return 100;
		}
		
		if(current_row==2)
		{
			if(combo==6)
				return 800;
			if(combo==5)
				return 500;
			if(combo==4)
				return 180;
			if(combo==3)
				return 80;
			if(combo==2)
				return 20;
		}
		
		if(current_row==3)
		{
			if(combo==6)
				return 650;
			if(combo==5)
				return 450;
			if(combo==4)
				return 150;
			if(combo==3)
				return 60;
			if(combo==2)
				return 15;
		}
		
		if(current_row==4)
		{
			if(combo==6)
				return 450;
			if(combo==5)
				return 250;
			if(combo==4)
				return 90;
			if(combo==3)
				return 40;
			if(combo==2)
				return 10;
		}
		
		if(current_row==5)
		{
			if(combo==6)
				return 250;
			if(combo==5)
				return 50;
			if(combo==4)
				return 25;
			if(combo==3)
				return 5;
		}
		
		if(current_row==6)
		{
			if(combo==6)
				return 50;
			if(combo==5)
				return 25;
			if(combo==4)
				return 5;
			if(combo==3)
				return 3;
		}
		
		if(current_row==7)
		{
			if(combo==6)
				return 25;
			if(combo==5)
				return 5;
			if(combo==4)
				return 3;
		}
		return 0;
	}
	
	void addCredits(int credits)
	{
		this.credits+=credits;
		//label_credits.setText(getCreditsString());
	}
	
	void act()
	{
		if(credits<credits_displayed)
		{
			if(credits_displayed-credits>1000)
			{
				credits_displayed-=50;
			}else if(credits_displayed-credits>100)
			{
				credits_displayed-=5;
			}else if(credits_displayed-credits>50)
			{
				credits_displayed-=3;
			}else
			{
				credits_displayed--;
			}
		}
		else if(credits>credits_displayed)
		{
			if(credits_displayed-credits<1000)
			{
				credits_displayed+=50;
			}else if(credits_displayed-credits<100)
			{
				credits_displayed+=5;
			}else if(credits_displayed-credits<50)
			{
				credits_displayed+=3;
			}else
			{
				credits_displayed++;
			}
		}
		label_credits.setText(getCreditsString());
	}
}
