package rosalila.studio.slotmachine;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "slot-machine";
		cfg.useGL20 = false;
		//cfg.width = 1280;
		//cfg.height = 736;
		cfg.width = 800;
		cfg.height = 444;
		
		new LwjglApplication(new SlotMachine(new DesktopFunctions()), cfg);
	}
}