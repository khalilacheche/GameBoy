package gameboy;

public interface Preconditions {
	
	
	/** Checks if argument is fulfilled
	 * @param b : the condition
	 * @throws IllegalArgumentException if the condition is not fulfilled
	 */
	public static void checkArgument(boolean b) {
		if (!b)
			throw new IllegalArgumentException();
	}
	
	
	
	/** Verifies if the given value is an 8bit value
	 * @param the value to check
	 * @return the given value
	 */
	public static int checkBits8(int v) {
		if (v<0 || v>0xFF)
			throw new IllegalArgumentException();
		return v;
	}
	/** Verifies if the given value is a 16bit value
	 * @param the value to check
	 * @return the given value
	 */
	public static int checkBits16(int v) {
		if (v<0 || v>0xFFFF)
			throw new IllegalArgumentException();
		return v;
	}
}
