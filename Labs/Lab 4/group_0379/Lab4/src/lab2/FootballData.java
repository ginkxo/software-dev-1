package lab2;

import java.util.Arrays;

public class FootballData {
	
	public static void main(String[] args) {
		Football[] myTeams = new Football[5];
		
		Football barcelona = new Football("Spain", "Barcelona", 2025);
		Football bayern = new Football("Germany", "Bayern", 2010);
		Football madrid = new Football("Spain", "Real Madrid", 2003);
		Football madrid2 = new Football("Spain", "Atletico Madrid", 1948);
		Football turin = new Football("Italy", "Juventus", 1936);
		
		myTeams[0] = barcelona;
		myTeams[1] = bayern;
		myTeams[2] = madrid2;
		myTeams[3] = turin;
		myTeams[4] = madrid;
		
		for (int i = 0; i<myTeams.length; i++) {
			System.out.println(myTeams[i]);
		}
		
		Arrays.sort(myTeams);
		
		for (int i = 0; i<myTeams.length; i++) {
			System.out.println(myTeams[i]);
		}
	}

}
