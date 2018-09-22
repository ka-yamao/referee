package c.local.com.referee;

public class Score {

	public int firstPoint;
	public int secondPoint;

	public Score() {

	}

	public Score(int firstPoint, int secondPoint) {
		this.firstPoint = firstPoint;
		this.secondPoint = secondPoint;

	}

	public void clear() {
		this.firstPoint = 0;
		this.secondPoint = 0;
	}

	public String first() {
		return String.valueOf(this.firstPoint);
	}

	public String second() {
		return String.valueOf(this.secondPoint);
	}
}
