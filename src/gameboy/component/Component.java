package gameboy.component;

import gameboy.Bus;

/**
 * @author Khalil Haroun Achache
 */
public interface Component {
	
	/**
	 * Signals that there's no data
	 */
	public final static int NO_DATA = 0x100;
	
	/**Reads one byte of data at a given address
	 * @param address : the address to read from 
	 * @return the data stocked in the component at that address, or NO_DATA if the address contains no value
	 * @throws IllegalArgumentException if the address is not a 16bit value
	 */
	public int read(int address);
	
	
	
	/**Writes the given data at the given address
	 * 
	 * 
	 * @param address : the address to write to
	 * @param data : the data to write
	 * @throws IllegalArgumentException if the address is not a 16bit value, or the data is not 8bit value
	 */
	public void write(int address, int data);
	
	/** Attach this component to a data bus
	 * @param bus : The bus to attach to
	 */
	public default void attachTo(Bus bus) {
		bus.attach(this);
	}

}
