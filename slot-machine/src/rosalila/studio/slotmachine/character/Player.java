package rosalila.studio.slotmachine.character;

import java.util.ArrayList;

import rosalila.studio.slotmachine.button.SimpleButton;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Player extends Actor
{
	Stage stage;
	Image background;
	Image character;
	SimpleButton shop_button;
	ArrayList<SimpleButton>shop_items;
	ArrayList<Image>wear;
	public Player(Stage stage)
	{
		this.stage=stage;
		background = new Image(new Texture("data/player/shop_background.png"));
		background.setVisible(false);		
		character = new Image(new Texture("data/player/player.png"));
		shop_button = new SimpleButton(0, 350, new Texture("data/player/shop_button.png"))
		{
			@Override
			public void onTouchDown() {
				showShop();
			}
		};
		shop_items=new ArrayList<SimpleButton>();
		wear=new ArrayList<Image>();
		
		Texture texture_shirt1 = new Texture("data/player/shirt1.png");
		SimpleButton button_shirt1 = new SimpleButton(200,300,texture_shirt1)
		{
			@Override
			public void onTouchUp()
			{
				hideAllWear();
				wear.get(0).setVisible(true);
				hideShop();
			}
		};
		Image image_shirt1 = new Image(texture_shirt1);
		image_shirt1.setVisible(false);
		image_shirt1.setPosition(-15, 112);
		wear.add(image_shirt1);
		shop_items.add(button_shirt1);
		
		Texture texture_shirt2 = new Texture("data/player/shirt2.png");
		SimpleButton button_shirt2 = new SimpleButton(300,300,texture_shirt2)
		{
			@Override
			public void onTouchUp()
			{
				hideAllWear();
				wear.get(1).setVisible(true);
				hideShop();
			}
		};
		Image image_shirt2 = new Image(texture_shirt2);
		image_shirt2.setVisible(false);
		image_shirt2.setPosition(-15, 112);
		wear.add(image_shirt2);
		shop_items.add(button_shirt2);
		
		stage.addActor(background);
		stage.addActor(character);
		stage.addActor(shop_button);
		
		for(int i=0;i<shop_items.size();i++)
		{
			stage.addActor(shop_items.get(i));
		}
		
		for(int i=0;i<wear.size();i++)
		{
			stage.addActor(wear.get(i));
		}
		hideShop();
	}
	
	void hideAllWear()
	{
		for(int i=0;i<wear.size();i++)
			wear.get(i).setVisible(false);
	}
	
	void showShop()
	{
		background.setVisible(true);
		for(int i=0;i<shop_items.size();i++)
			shop_items.get(i).setVisible(true);
	}
	
	void hideShop()
	{
		background.setVisible(false);
		for(int i=0;i<shop_items.size();i++)
			shop_items.get(i).setVisible(false);
	}
}
