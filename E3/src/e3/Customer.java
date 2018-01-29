package e3;

public class Customer {
    protected int id;
    protected String name;
	// Your full code for this class goes here

    public Customer(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // need to check if this toString() works also
    public String toString() {
        return this.name;
    }
}