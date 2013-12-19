package rosalila.studio.slotmachine;

import java.util.ArrayList;

import rosalila.studio.slotmachine.boxes.SlotBox;
import rosalila.studio.slotmachine.button.Bet;
import rosalila.studio.slotmachine.button.Lines;
import rosalila.studio.slotmachine.button.SimpleButton;
import rosalila.studio.slotmachine.character.Player;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class SlotMachine implements ApplicationListener{
	
	private Stage stage;
	
	SlotLogic slot_logic;
	
	SpriteBatch spriteBatch;
	BitmapFont font;
	
	Image read_barcode;
	Image collector_button;
	
	//String res="1280x736";
	String res="800x444";
	
	ArrayList<Image> tuto_images;
	int current_tuto_image=-1;
	int tuto_size=6;
	
	boolean lock_reading_code=false;
	
	private final AndroidFunctionsInterface androidFunctions;
	
	int collect_insurer=0;
	
	public SlotMachine(AndroidFunctionsInterface desktopFunctions)
	{
		this.androidFunctions = desktopFunctions;
	}
        
	@Override
	public void create() {	        
		//Stage init
		//androidFunctions.SwarmInitiate();
		
//		int i_temp=0;
//		while(androidFunctions.getScore()==-1)
//		{
////			try {
////				Thread.sleep(1);
////			} catch (InterruptedException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////			i_temp++;
////			if(i_temp<10000)
////				break;
//		}
		
		androidFunctions.ShowLeaderboardSwarm();
		
		stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//font
		font = new BitmapFont(Gdx.files.internal("data/default.fnt"),false);
		Skin uiSkin;
		uiSkin = new Skin();
		uiSkin.add("default", new BitmapFont());
		//Label style
		LabelStyle label_syle = new LabelStyle();
		label_syle.font = font;
		label_syle.fontColor = Color.WHITE;
		uiSkin.add("default", label_syle);
		
		Texture texture_background = new Texture("data/background.png");
		Background background = new Background(texture_background);
		
		WinAnimation win = new WinAnimation();
		if(res=="1280x736")
			win.setPosition(0, 150);
		else
			win.setPosition(145, 40);
		
		//Lines ui
		
		ArrayList<Image>lines_ui=new ArrayList<Image>();
		
		Texture texture_line_ui1 = new Texture("data/line_ui.png");
		Image line_ui1 = new Image(texture_line_ui1);
		if(res=="1280x736")
			line_ui1.setPosition(0, 384);
		else
			line_ui1.setPosition(80, 200);
		
//		box_offset_x=160;
//		box_offset_y=230;
		
		line_ui1.setVisible(false);
		lines_ui.add(line_ui1);
		
		Texture texture_line_ui2 = new Texture("data/line_ui.png");
		Image line_ui2 = new Image(texture_line_ui2);
		if(res=="1280x736")
			line_ui2.setPosition(0, 256);
		else
			line_ui2.setPosition(80, 136);
		line_ui2.setVisible(false);
		lines_ui.add(line_ui2);
		
		Texture texture_line_ui3 = new Texture("data/line_ui.png");
		Image line_ui3 = new Image(texture_line_ui3);
		if(res=="1280x736")
			line_ui3.setPosition(0, 128);
		else
			line_ui3.setPosition(80, 72);
		line_ui3.setVisible(false);
		lines_ui.add(line_ui3);
		
		//end Lines ui
		
		ArrayList< ArrayList<SlotBox> > boxes = new ArrayList< ArrayList<SlotBox> >();
		int box_offset_x;
		int box_offset_y;
		int box_width;
		int box_height;
		if(res=="1280x736")
		{
			box_offset_x=256;
			box_offset_y=384;
			box_width=256;
			box_height=128;
		}else
		{
			box_offset_x=211;
			box_offset_y=200;
			box_width=64;
			box_height=64;
		}
		for(int i=0;i<6;i++)
		{
			ArrayList<SlotBox> boxes_columns = new ArrayList<SlotBox>();
			for(int j=0;j<3;j++)
			{
				SlotBox box = new SlotBox(box_offset_x+i*box_width,box_offset_y-j*box_height);
				boxes_columns.add(box);
			}
			boxes.add(boxes_columns);
		}
		
		Label label_credits = new Label("Credits:\n"+androidFunctions.getScore(),uiSkin);
		if(res=="1280x736")
			label_credits.setPosition(25,160);
		else
			label_credits.setPosition(25,75);
		Label label_total_bet = new Label("Total bet:\n1",uiSkin);
		if(res=="1280x736")
			label_total_bet.setPosition(25,260);
		else
			label_total_bet.setPosition(25,150);
		Label label_last_prize = new Label("Last prize:\n0",uiSkin);
		if(res=="1280x736")
			label_last_prize.setPosition(25,360);
		else
			label_last_prize.setPosition(25,225);
		
		Lines lines;
		if(res=="1280x736")
			lines = new Lines(1050,500,3,label_total_bet,lines_ui);
		else
			lines = new Lines(672,360,3,label_total_bet,lines_ui);
		
		ArrayList<Double>bets=new ArrayList<Double>();
		bets.add(1.0);
		bets.add(5.0);
		bets.add(10.0);
		bets.add(20.0);
		Bet bet;
		if(res=="1280x736")
			bet = new Bet(1050,350,bets,label_total_bet);
		else
			bet = new Bet(672,290,bets,label_total_bet);
		
		BackgroundButton bg_btn1 = new BackgroundButton(380, 600, 1, stage);
		BackgroundButton bg_btn2 = new BackgroundButton(480, 600, 2, stage);
		BackgroundButton bg_btn3 = new BackgroundButton(580, 600, 3, stage);
		BackgroundButton bg_btn4 = new BackgroundButton(680, 600, 4, stage);
		BackgroundButton bg_btn5 = new BackgroundButton(780, 600, 5, stage);
		BackgroundButton bg_btn6 = new BackgroundButton(880, 600, 6, stage);
		BackgroundButton bg_btn7 = new BackgroundButton(980, 600, 7, stage);
		BackgroundButton bg_btn8 = new BackgroundButton(1080, 600, 8, stage);
		
		ArrayList<Powerup> powerups=new ArrayList<Powerup>();
		/*powerups
		Powerup pb_btn1 = new Powerup(380, 50, 1,uiSkin,label_credits);
		Powerup pb_btn2 = new Powerup(480, 50, 2,uiSkin,label_credits);
		Powerup pb_btn3 = new Powerup(580, 50, 3,uiSkin,label_credits);
		Powerup pb_btn4 = new Powerup(680, 50, 4,uiSkin,label_credits);
		Powerup pb_btn5 = new Powerup(780, 50, 5,uiSkin,label_credits);
		Powerup pb_btn6 = new Powerup(880, 50, 6,uiSkin,label_credits);
		Powerup pb_btn7 = new Powerup(980, 50, 7,uiSkin,label_credits);
		powerups.add(pb_btn1);
		powerups.add(pb_btn2);
		powerups.add(pb_btn3);
		powerups.add(pb_btn4);
		powerups.add(pb_btn5);
		powerups.add(pb_btn6);
		powerups.add(pb_btn7);
		*/
		
		tuto_images=new ArrayList<Image>();
		for(int i=0;i<tuto_size;i++)
		{
			Texture texture_tuto = new Texture("data/tuto_"+i+".png");
			Image image_tuto = new Image(texture_tuto);
			image_tuto.setVisible(false);
			tuto_images.add(image_tuto);
		}
		
		//slot_logic = new SlotLogic(boxes, androidFunctions.getScore(), win,lines,bet,label_credits,label_total_bet,label_last_prize,powerups,androidFunctions);
		slot_logic = new SlotLogic(boxes, 100, win,lines,bet,label_credits,label_total_bet,label_last_prize,powerups,androidFunctions);
		
		//Barcode reader
		Texture texture_read_barcode = new Texture("data/read_barcode.png");
		read_barcode = new SimpleButton(-1,-1,texture_read_barcode)
		{
			@Override
			public void onTouchUp()
			{
        		if(lock_reading_code)
        		{
        			return;
        		}
        		
        		//Tread to read barcode
        		Runnable myRunnable = new Runnable(){
        			public void run(){
        				androidFunctions.readBarCode();
        			}
        		};
        		
        		Thread thread = new Thread(myRunnable);
        		thread.start();
        		
        		//Thread to update the credits
        		Runnable myRunnable2 = new Runnable(){
        			public void run(){
        				lock_reading_code=true;
        				float readed_barcode=-1;
        				int tries=0;
 		        		do
 		        		{
 		        			readed_barcode=androidFunctions.getReadedBarcode();
 		        			try
 		        			{
 		        				Thread.sleep(500);
 		        			} catch (InterruptedException e) {
 								// TODO Auto-generated catch block
 								e.printStackTrace();
 							}
 		        			if(tries>10)
 		        			{
 		        				lock_reading_code=false;
 		        				return;
 		        			}
 		        		}while(readed_barcode==-1);
	        			slot_logic.credits+=readed_barcode;
	        			slot_logic.label_credits.setText(slot_logic.getCreditsString());
	        			lock_reading_code=false;
	        		}
        		};

        		Thread thread2 = new Thread(myRunnable2);
        		thread2.start();
			}
		};
		if(res=="1280x736")
			read_barcode.setPosition(1050, 0);
		else
			read_barcode.setPosition(650, 0);
		// end Barcode reader
		
		
		//Collector
		final Label label_collect = new Label("Colectando XX \nfavor no tocar la pantalla y llamar al encargado.",uiSkin);
		if(res=="1280x736")
			label_collect.setPosition(10,50);
		else
			label_collect.setPosition(5,0);
		label_collect.setVisible(false);
		
		label_collect.addCaptureListener(new InputListener() {
        	public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
        	{
        		
        		return true;
            }
            
            public void touchUp (InputEvent event, float x, float y, int pointer, int button)
            {
            	collect_insurer++;
            	if(collect_insurer>5)
            	{
            		collect_insurer=0;
	            	slot_logic.credits=0;
	            	slot_logic.label_credits.setText(slot_logic.getCreditsString());
	            	label_collect.setVisible(false);
	            	
            		read_barcode.setVisible(true);
                	collector_button.setVisible(true);
            	}
            }
        });
		// end Collector
		
		//Collector button
		Texture texture_collector_button = new Texture("data/collector_button.png");
		collector_button = new SimpleButton(-1,-1,texture_collector_button)
		{
			@Override
			public void onTouchUp()
			{
            	read_barcode.setVisible(false);
            	collector_button.setVisible(false);
            	label_collect.setText("Colectando "+slot_logic.credits+"\nFavor no tocar la pantalla y llamar al encargado.");
            	label_collect.setVisible(true);
			}
		};
		if(res=="1280x736")
			collector_button.setPosition(800, 0);
		else
			collector_button.setPosition(-500, 0);
		// end Collector button
		
		//Tuto button
		Texture texture_tuto_button = new Texture("data/tuto_button.png");
		Image tuto_button = new SimpleButton(-1,-1,texture_tuto_button)
		{
			public void onTouchUp()
			{
            	if(current_tuto_image==-1)
            		current_tuto_image=0;
            	else
            	{
            		current_tuto_image++;
            		if(current_tuto_image>=tuto_size)
            			current_tuto_image=-1;
            	}
            	for(int i=0;i<tuto_size;i++)
            	{
            		tuto_images.get(i).setVisible(false);
            	}
            	if(current_tuto_image>=0)
            		tuto_images.get(current_tuto_image).setVisible(true);
			}
		};
		if(res=="1280x736")
			tuto_button.setPosition(600, 0);
		else
			tuto_button.setPosition(300, 0);
		// end Tuto button
		
		lines.slot_logic=slot_logic;
		bet.slot_logic=slot_logic;
		/* powerups
		pb_btn1.slot_logic=slot_logic;
		pb_btn2.slot_logic=slot_logic;
		pb_btn3.slot_logic=slot_logic;
		pb_btn4.slot_logic=slot_logic;
		pb_btn5.slot_logic=slot_logic;
		pb_btn6.slot_logic=slot_logic;
		pb_btn7.slot_logic=slot_logic;
		*/
		Handle handle;
		if(res=="1280x736")
			handle = new Handle(1050,200,slot_logic,this);
		else
			handle = new Handle(650,50,slot_logic,this);
		slot_logic.handle = handle;
		
		//Add objects to the stage
		stage.addActor(background);
		stage.addActor(win);
		for(int i=0;i<boxes.size();i++)
		{
			for(int j=0;j<boxes.get(i).size();j++)
				stage.addActor(boxes.get(i).get(j));
		}
		stage.addActor(handle);
		stage.addActor(lines);
		stage.addActor(bet);
		stage.addActor(line_ui1);
		stage.addActor(line_ui2);
		stage.addActor(line_ui3);
		stage.addActor(label_credits);
		stage.addActor(label_total_bet);
		stage.addActor(label_last_prize);
		
		//Tuto
		for(int i=0;i<tuto_size;i++)
		{
			stage.addActor(tuto_images.get(i));
		}
		
		stage.addActor(bg_btn1);
		stage.addActor(bg_btn2);
		stage.addActor(bg_btn3);
		stage.addActor(bg_btn4);
		stage.addActor(bg_btn5);
		stage.addActor(bg_btn6);
		stage.addActor(bg_btn7);
		stage.addActor(bg_btn8);
		stage.addActor(read_barcode);
		stage.addActor(collector_button);
		stage.addActor(tuto_button);
		stage.addActor(label_collect);
		
		Label label_special_event_pot = new Label("Premio: 0",uiSkin);
		SpecialEvent se= new SpecialEvent(slot_logic,stage,label_special_event_pot);
		stage.addActor(se);
		
//		stage.addActor(new Player(stage));
		
		/* powerups
		stage.addActor(pb_btn1);
		stage.addActor(pb_btn2);
		stage.addActor(pb_btn3);
		stage.addActor(pb_btn4);
		stage.addActor(pb_btn5);
		stage.addActor(pb_btn6);
		stage.addActor(pb_btn7);
		*/
		
		//stage.addActor(button);
		//stage.addActor(introText);
	}
	
	@Override
	public void render() {
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);                                            // #14
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		slot_logic.act();
		//slot_logic.credits=androidFunctions.getScore();
	}
	
	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		stage.setViewport(800, 444, true);	
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
}
