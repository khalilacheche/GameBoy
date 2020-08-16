package gameboy.bits;

public interface Bit {
	/**
	 * @return the order of the bit in the interval
	 */
	int ordinal();
	/**
	 * @return
	 */
	default int index () {
		return ordinal();
	}
	/**
	 * @return the same
	 */
	default int mask() {
		return 1<< ordinal();
	}
}
