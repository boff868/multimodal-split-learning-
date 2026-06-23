import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;

/**
 * This class implements an iterative version of recursive square pattern drawing using Stacks.
 * It mimics the recursive behavior through stack operations to generate nested squares in all four corners,
 * without using any recursive method calls. 
 */
public class RecursiveSquaresIteratively extends Frame {
	public RecursiveSquaresIteratively() {
		setSize(300, 300);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent windowEvent) {
				System.exit(0);
			}
		});
	}
	  /**
     * the class is to encapsulate the properties of a square.
     * Stores the top-left coordinates and side length of a square.
     */
class Square {
        int xPosition;
        int yPosition;
        int length;
        Square(int x, int y, int length) {
            this.xPosition = x;
            this.yPosition = y;
            this.length = length;
        }
    }
/**
 * Core method to iteratively draw nested squares using stacks.
 * Generates squares in all four corners of each parent square, up to the specified recursion depth.
 * 
 * @param g          Graphics2D context for drawing operations
 * @param xPosition  X-coordinate of the initial square's top-left corner
 * @param yPosition  Y-coordinate of the initial square's top-left corner
 * @param length     Side length of the initial square
 * @param n          Recursion depth (number of nested layers to generate)
 */
 private void drawSquare(Graphics2D g, int xPosition, int yPosition, int length, int n) {
	// Main stack to manage squares for processing (mimics recursion call stack)
     Stack stack = new Stack(100);
     // Push the initial square into the main stack to start processing
     stack.push(new Square(xPosition, yPosition, length));
     
     // Temporary stack 1: used to store parent squares during child generation
     Stack stack2 = new Stack(100);
     // Temporary stack 2: used to restore parent squares back to the main stack
     Stack stack3 = new Stack(100);
	     for(int i=0;i<n;i++) {
	    	 int newLength=length/2;
	    	 for(int j=0;j<Math.pow(4,i);j++)
		    	{     
		    	      Square new0=(Square)stack.pop();
		    	      stack3.push(new0);
		    	      stack2.push(new Square(new0.xPosition-length/4,new0.yPosition-length/4,newLength));
		    	      stack2.push(new Square(new0.xPosition-length/4,new0.yPosition+length*3/4,newLength));
		    	      stack2.push(new Square(new0.xPosition+length*3/4,new0.yPosition-length/4,newLength));
		    	      stack2.push(new Square(new0.xPosition+length*3/4,new0.yPosition+length*3/4,newLength));
		 
		    	}
	    	 length=newLength;
	         // Restore parent squares from newStack3 back to the main stack (maintain order)
	            while (!stack3.isEmpty()) {
	                stack.push(stack3.pop());
	            }
	            
	            // Move all generated child squares from newStack2 to the main stack
	            // These will be processed as parent squares in the next iteration
	            while (!stack2.isEmpty()) {
	                stack.push(stack2.pop());
	            }
	        }
	        
	        // After generating all layers, draw every square stored in the main stack
	        while (!stack.isEmpty()) {
	            Square squareToDraw = (Square) stack.pop();
	            // Draw the square using Graphics2D (Rectangle2D.Double for precise floating-point coordinates)
	            g.draw(new Rectangle2D.Double(
	                squareToDraw.xPosition, 
	                squareToDraw.yPosition, 
	                squareToDraw.length, 
	                squareToDraw.length
	            ));
	        }
}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// recursive pattern of order n
		int n = 3;
		drawSquare(g2, 100, 100, 100, n);
	}

	public static void main(String[] args) {
		RecursiveSquaresIteratively squaresGUI = new RecursiveSquaresIteratively();
		squaresGUI.setVisible(true);
	}
}