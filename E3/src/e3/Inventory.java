package e3;

import java.util.ArrayList;
import java.util.List;

public class Inventory implements Observable {

    protected List<Observer> observers;
    protected Product product;
    protected double availableQuantity;
    protected double backorderedQuantity;

	// Your code for Inventory class goes here


    public Inventory(Product product) {
        this.product = product;
        this.availableQuantity = 0;
        this.backorderedQuantity = 0;
        this.observers = new ArrayList<Observer>();
    }

    protected void updateQuantities (double stock, double backord) {
        this.availableQuantity += stock;
        this.backorderedQuantity += backord;

    }

    public void registerObserver(Observer o) {
        observers.add(o);

    }

    public void removeObserver(Observer o) {
        int index = observers.indexOf(o);
        if (index >= 0) {
            observers.remove(index);
        }

    }

    public void notifyObserver() {
        //for (Observer o: observers) {
        //    o.update(availableQuantity, backorderedQuantity);
        //}
        for(int i = 0; i < observers.size(); i++) {
            Observer observer = (Observer) observers.get(i);
            observer.update(availableQuantity, backorderedQuantity);
        }

        for(int i = 0; i < observers.size(); i++) {
            Observer observer = (Observer) observers.get(i);
            if (observer instanceof SalesOrder) {
                if (((SalesOrder)observer).shipped) {
                    removeObserver(observer);
                }
            }
        }

    }

    @Override
    public String toString() {
        return this.product.id + " " + this.product.name
                + ", Available: " + availableQuantity
                + ", Backorders: " + backorderedQuantity ;
    }
}
