import java.util.ArrayList;

public class Competition {
	// Scores
	ArrayList<Integer> scores;
	// Competitors
	ArrayList<String> competitors;
	// Highest Score
	int highestScore;
	// Winner
	String winner;
	
	Competition () {
		this.scores = new ArrayList<Integer>();
		this.competitors = new ArrayList<String>();
		this.highestScore = -1;
		this.winner = null;
	}
	
	void addScore(String competitor, int score) {
		// Get competitor index
		int index = competitors.indexOf(competitor);
		// Update score or add competitor and score
		if (index == -1) {
			scores.add(score);
			competitors.add(competitor);
		} else {
			if (score > scores.get(index)) {
				scores.set(index, score);
			}
		}
		// Update current best score and winner
		if (score > highestScore) {
			winner = competitor;
			highestScore = score;
		}
	}
	
	String getWinner() {
		return winner;
	}
	
	public String toString() {
		String res = "";
		for (int i = 0; i<competitors.size(); i++){
			res += competitors.get(i) + "(" + scores.get(i) + ") ";
		}
		return res;
	}
}
