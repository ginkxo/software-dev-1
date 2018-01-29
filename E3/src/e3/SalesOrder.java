package e3;

public class SalesOrder implements Observer, DisplayElement {
    private static int orderSequence=0;
    protected int id;
    protected Customer customer;
    protected double quantity;
    protected Observable inventory;
    protected boolean shipped; //helper variable

    public SalesOrder(Customer customer, double quantity, Observable inventory) {
        this.customer = customer;
        this.quantity = quantity;
        this.inventory = inventory;
        this.shipped = false;
        if(((Inventory)inventory).availableQuantity >= quantity) {
            ((Inventory)inventory).availableQuantity-=quantity;
            ((Inventory)inventory).backorderedQuantity-=quantity;
            display(quantity);
        } else {
            inventory.registerObserver(this);
            this.orderSequence+=1;
            this.id = orderSequence;
            ((Inventory)inventory).updateQuantities(0,quantity);
            inventory.notifyObserver();
        }
    }

    public void update(double availQty, double ordQty) {
        //ordQty += quantity;
        //((Inventory)inventory).updateQuantities(0,ordQty+quantity);
        if (availQty >= quantity) {
            if (ship(availQty)) {
                ((Inventory)inventory).availableQuantity-=quantity;
                ((Inventory)inventory).backorderedQuantity-=quantity;
                display(quantity);
                //inventory.removeObserver(this);
                this.shipped=true;
            }
        }
        else {
            //display(quantity);
        }

    }

    public void display(double displayQuantity) {
        System.out.println(this.toString() + " " + displayQuantity);
    }

    private boolean ship(double availableQuantity) {
        //if(availableQuantity )
        if (availableQuantity >= quantity) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Shipping Order #"+id+" to "+customer.toString()+", Product: "+((Inventory)inventory).product.name+", Quantity:";
    }

    // Your code for SalesOrder class goes here
}