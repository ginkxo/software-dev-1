package lab2;

public class Football implements Comparable<Football> {
	
	String country;
	String club;
	int points;

	public Football(String country, String club, int points){
		this.country = country;
		this.club = club;
		this.points = points;
	}
	
	@Override
	public int compareTo(Football o) {
		// TODO Auto-generated method stub
		int compared = this.club.compareTo(o.club);
		if (compared < 0) {
			return -1;
		} else if (compared > 0) {
			return 1;
		} else {
			return compared;
		}
	}
	
	@Override
	public String toString(){
		return this.country + ", " + this.club + ", " + this.points + " points";
	}
	
	public boolean equals(Football f){
		return this.country == f.country &&
				this.club == f.club &&
				this.points == f.points;
	}
	
	@Override
	public int hashCode(){
		int result = 17; 
		result = result*31+country.length(); 
		result = result*31+club.length();
		result = result*31+points;
		return result;
		
	}
	
	public static void main(String[] args) {
		Football bluejays = new Football("Toronto","Blue Jays", 9000);
		Football rangers = new Football("Texas","Rangers",2);
		
		System.out.println(rangers.compareTo(bluejays));
		
		
	}
	
	

}
