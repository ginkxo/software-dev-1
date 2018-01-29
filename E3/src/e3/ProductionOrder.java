package e3;

public class ProductionOrder implements Observer, DisplayElement {

    private static int orderSequence=0;
    protected int id;
    protected double minQuantity;
    protected Observable inventory;
	// Your code for the ProductionOrder class goes here


    public ProductionOrder(double minQuantity, Observable inventory) {
        if (minQuantity >= 1000) {
            this.minQuantity = minQuantity;
        } else {
            this.minQuantity = 1000;
        }
        this.inventory = inventory;
        this.orderSequence+=1;
        this.id = orderSequence;
        inventory.registerObserver(this);
        //inventory.notifyObserver();
    }


    public void update(double availQty, double ordQty) {
        if (ordQty >= minQuantity) {
            // availQty += minQuantity - ordQty;
            ((Inventory)inventory).updateQuantities(
                    ordQty,
                    0);
            display(((Inventory)inventory).availableQuantity);
            //inventory.removeObserver(this);
        }
    }

    public void display(double dispQty) {
        System.out.println(this.toString() + " " + dispQty);

    }

    @Override
    public String toString() {
        return "Production Order #"+id+", "+"item "+((Inventory)inventory).product.name + ", produced:";
    }
}
