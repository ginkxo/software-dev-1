package lab6;

public class Address {
	String street;
	String city;
	String province;
	String postalCode;
	String country;
	
	public Address(String street, String city, String province, String postalCode, String country){
		this.street = street;
		this.city = city;
		this.province = province;
		this.postalCode = postalCode;
		this.country = country;

	}
	
	public String toString(){
		return street + ", " + city + ", " + province + ", " + postalCode + ", " + country;
	}
	
}
