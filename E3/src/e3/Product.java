package e3;

public class Product {
	protected int id;
    protected String name;
	// Your code for the product class goes here

    public Product(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // need to check toString() implementation
    public String toString() {
        return this.name + " " + this.id;
    }
}