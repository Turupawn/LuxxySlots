package rosalila.studio.slotmachine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Background extends Image{
	Texture texture;
	Background(Texture texture)
	{
		super(texture);
		this.texture=texture;
	}
}
