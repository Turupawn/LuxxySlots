package rosalila.studio.slotmachine.boxes;



public class SlotBox extends Box
{
	public SlotBox(int x,int y)
	{
		super(x,y,4,8,64,64,(int)((Math.random()*1000)%7+1),"data/frutas.png",0.1f);
	}
}