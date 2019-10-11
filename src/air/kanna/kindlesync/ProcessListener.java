package air.kanna.kindlesync;

public interface ProcessListener {

	public void setMax(int max);
	public void next(String message);
	public void setPosition(int current, String message);
	public void finish(String message);
	
}
