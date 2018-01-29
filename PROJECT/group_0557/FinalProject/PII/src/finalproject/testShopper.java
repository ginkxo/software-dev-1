package finalproject;

public class testShopper {

	public static void main(String[] args) {
		Shopper Bob = new Shopper("userName","password",new City("cityland",true), "blahlblah", 32, null);
		Shopper Jill = new Shopper("userName","password",new City("cityworld",true), "blahlblah", 35, null);
		Product PokemonShirt = new Product(new Category("TSHIRT",1), "Pokemon Shirt", "Team Mystic v-neck", "2000 awesome", 50);
		Product PokemonBackpack = new Product(new Category("BACKPACK",4), "Pokemon Backpack", "The perfect place to store your pokeballs", "2850 awesome", 50);
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
