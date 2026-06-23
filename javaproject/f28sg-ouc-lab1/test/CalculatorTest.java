import static org.junit.Assert.*;

import org.junit.Test;


public class CalculatorTest {

	/*
	 *  Tests for part 3
	 */
	
	@Test
	public void testCalculate() {
		String [] cmds1 = {"+","-","3","5","2"};
		String [] cmds2 = {"-","3","+","5","2"};
		
		assertEquals(0,Calculator.calculate(cmds1));
		assertEquals(-4,Calculator.calculate(cmds2));	
	}

	@Test
	public void testConvert() {
		assertEquals(1,Calculator.convert("1"));
		assertEquals(0,Calculator.convert("0"));
		assertEquals(-5,Calculator.convert("-5"));
	}

	@Test
	public void testIsNumber() {
		assertTrue(Calculator.isNumber("-1"));
		assertFalse(Calculator.isNumber("A"));
	}

	@Test
	public void testApplyOpPlus() {
		assertEquals(3,Calculator.applyOp(1,"+",2));
		assertEquals(0,Calculator.applyOp(3,"+",-3));
		assertEquals(-3,Calculator.applyOp(0,"+",-3));
		assertEquals(3,Calculator.applyOp(3,"+", 0));
	}
	
	@Test
	public void testApplyOpMinus() {
		assertEquals(-5,Calculator.applyOp(2,"-",7));
		assertEquals(3,Calculator.applyOp(5,"-",2));
		assertEquals(7,Calculator.applyOp(5,"-",-2));
		assertEquals(5,Calculator.applyOp(5,"-",0));
		assertEquals(-5,Calculator.applyOp(0,"-",5));
		assertEquals(5,Calculator.applyOp(0,"-",-5));
	}
	
	@Test
	public void testApplyOpTimes() {
		assertEquals(6,Calculator.applyOp(2,"*",3));
		assertEquals(-12,Calculator.applyOp(3,"*",-4));
		assertEquals(0,Calculator.applyOp(3,"*",0));
		assertEquals(0,Calculator.applyOp(0,"*",3));
	}
	
	@Test
	public void testApplyOpDivide() {
		assertEquals(2,Calculator.applyOp(4,"/",2));
		assertEquals(3,Calculator.applyOp(12,"/",4));
		assertEquals(-3,Calculator.applyOp(12,"/",-4));
		assertEquals(-3,Calculator.applyOp(-12,"/",4));
	}
	
	@Test(expected=CalculateException.class)
	public void testApplyOpException() {
		assertEquals(2,Calculator.applyOp(4,"a",2));
	}
}
