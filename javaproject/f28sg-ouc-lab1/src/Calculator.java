/**
 * this class is used for polish notation calculator, so I first reverse the
 * array because in polish notation, all the calculators are in the front of the
 * numbers, so if I reverse the array, I can get all the numbers and out them in
 * the stack. Then, I will pop the numbers out. If I meet a calculator, I will
 * do the calculations and again push the result into the stack. I will repeat
 * the process until there is only one element in the stack, and that should be
 * the answer. Moreover, there are a lot of special conditions judgments. For
 * example. the array should be empty, and when we meet the calculator, there
 * should be at least 2 numbers in the stack to do the calculations, and an
 * equation should have more numbers than calculations
 */
// exception used for Q5
//this is the exception class
class CalculateException extends RuntimeException {
	public CalculateException(String err) {
		super(err);
	}
}

public class Calculator {

	/*
	 * the calculate first reverse the array to get the numbers first and store them
	 * in the stack then, when we get the calculations from the array we can use the
	 * figures in the stack to do some calculations there are also a lot of special
	 * conditions, I have mentioned them on the top
	 * the complexity is O(n) because the loop if takes in the array so it need to look through the loop
	 */
	public static int calculate(String[] cmds) {
		Stack st=new Stack();
		Reverse.reverse(cmds);
		for(int i=0;i<cmds.length;i++) {
			if(isNumber(cmds[i])) {
				st.push(convert(cmds[i]));
			}else {
				int a=(int) st.pop();
				int b=(int) st.pop();
				st.push(applyOp(a,cmds[i],b));
			}
		}
		return (int)st.top();
	}

	// convert all the numbers to integers
	//the complexity is O(1) because it just satisfy a simple judgment
	public static int convert(String s) throws NumberFormatException {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new CalculateException("Invalid number format: " + s);
		}
	}

	// this is a method to check whether the elements are numbers
	//the complexity is O(1) because it just satisfy a simple judgment
	public static boolean isNumber(String s) {
		if (s == null || s.isEmpty()) {
			return false;
		}
		// Try converting to check if it's a number
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	// apply the operator after converting the numbers
	//the complexity is O(1) because it just satisfy a simple judgment
	public static int applyOp(int fst, String op, int snd) {
		// simply just use switch to cover all the situations. Notably, we are not
		// allowed to divide by 0
		switch (op) {
		case "+":
			return fst + snd;
		case "-":
			return fst - snd;
		case "*":
			return fst * snd;
		case "/":
			if (snd == 0) {
				throw new CalculateException("Division by zero");
			}
			return fst / snd; // Integer division (truncates towards zero)
		default:
			throw new CalculateException("Unknown operator: " + op);
		}
	}
}
