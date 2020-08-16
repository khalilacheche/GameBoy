package gameboy;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import gameboy.component.Component;

/**
 * @author Khalil Haroun Achache
 */
public final class Bus {
	
	private List<Component> components;
	
	
	/**
	 * Create an empty Bus 
	 */
	public Bus() {
		this.components = new ArrayList<Component>();
	}
	
	
	/** Attach a component to the bus
	 * @param component : the component to be attached
	 * @throws NullPointerException if the component is Null
	 */
	public  void attach(Component component) {
		this.components.add(Objects.requireNonNull(component));
	}
	
	
	/** Reads the value contained at 
	 * @param address
	 * @return the data that one of the components has at that address (that is not NO_DATA), or 0xFF otherwise
	 * @throws IllegalArgumentException if the address is not a 16bit value
	 */
	public int read(int address) {
		Preconditions.checkBits16(address);
		for(Component c : components) {
			if(c.read(address)!=Component.NO_DATA)
				return c.read(address);
		}
		return 0xFF;
	}
	
	/** Writes the given data to all attached components at the given address
	 * @param address : the address to write to
	 * @param data : the data to write to
	 * @throws IllegalArgumentException if the address is not a 16bit value or the data is not 8bit value 
	 */
	public void write(int address, int data) {
		Preconditions.checkBits8(data);
		Preconditions.checkBits16(address);
		for(Component c : components)
			c.write(address,data);
	}
}
