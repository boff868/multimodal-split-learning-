import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

/**
 * this method draws a series of squares using recursion. n is the amount of
 * iterations. IF n==0, this method simply stops. when we do the recursion, we
 * need to calculate the xPosition and yPosition, all of them are 1/4 or 4/3
 * length away from the original position. after we get the right position, we
 * can draw all four squares again and again, until n finally equals to 0
 */
public class RecursiveSquares extends Frame {

	public RecursiveSquares() {
		setSize(300, 300);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}

	private void drawSquare(Graphics2D g, int xPosition, int yPosition, int length, int n) {
		/* Check to see if the base case has been reached */

		/*
		 * Otherwise, the recursive case
		 * 
		 * Step 1: draw the square with:
		 * 
		 * g.draw(new Rectangle2D.Double(xPosition, yPosition, length, length));
		 * 
		 * Step 2: make recursive calls to draw 4 smaller squares next time.
		 */
		if(n==0) {
			return;
		}
		g.draw(new Rectangle2D.Double(xPosition,yPosition,length,length));
		drawSquare(g,xPosition-length/4,yPosition-length/4,length/2,n-1);
		drawSquare(g,xPosition+length*3/4,yPosition-length/4,length/2,n-1);
		drawSquare(g,xPosition-length/4,yPosition+length*3/4,length/2,n-1);
		drawSquare(g,xPosition+length*3/4,yPosition+length*3/4,length/2,n-1);
	}

	// to get the ideal painting, I change the n to 4.
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// recursive pattern of order n
		int n = 4;
		drawSquare(g2, 100, 100, 100, n);
	}

	public static void main(String[] args) {
		RecursiveSquares squaresGUI = new RecursiveSquares();
		squaresGUI.setVisible(true);
	}
}
