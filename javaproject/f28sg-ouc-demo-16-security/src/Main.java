
public class Main {

	public static void main(String[] args) {
		 Competition race = new Competition();
		 
		 race.addScore("Dougal", 3);
		 race.addScore("Zoe", 2);
		 race.addScore("Nadir", 5);
		 race.addScore("Fiona", 4);
		 
		 System.out.println("Round 1: " + race);
		 System.out.println("Round 1's winner is " + race.getWinner());

		 race.addScore("Dougal", 4);
		 race.addScore("Zoe", 6);
		 race.addScore("Nadir", 2);
		 race.addScore("Fiona", 3);

		 System.out.println("Round 2: " + race);
		 System.out.println("Round 2's winner is " + race.getWinner());

		 Client.actions(race);
		 
		 System.out.println("Round 3: " + race);
		 System.out.println("Round 3's winner is " + race.getWinner());

		 
	}
}
