package e3;

public interface Observable {
	
	// Your code for the Observable interface goes here

    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObserver();


}
