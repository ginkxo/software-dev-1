package finalproject;

public class testShopper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Shopper Bob = new Shopper();
		Shopper Jill = new Shopper();
		Product PokemonShirt = new Product("TSHIRT", "Pokemon Shirt", "Team Mystic v-neck", 2000, 50);
		Product PokemonBackpack = new Product("BACKPACK", "Pokemon Backpack", "The perfect place to store your pokeballs", 2850, 50);
		Bob.viewCart();
		Bob.addToCart(PokemonShirt, 4);
		Bob.addToCart(PokemonBackpack, 1);
		Bob.viewCart();
		Bob.checkOut();
		Bob.addToCart(PokemonShirt,  5);
		Bob.removeFromCart(PokemonShirt, 4);
		Jill.addToCart(PokemonShirt,  1);
		Jill.checkOut();
		Bob.checkOut();
		Bob.viewInvoices();
		System.out.println("Shirts remaining in store: " + PokemonShirt.getQuantity());
	}
}
