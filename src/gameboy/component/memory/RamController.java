package gameboy.component.memory;

import java.util.Objects;

import gameboy.Preconditions;
import gameboy.component.Component;

/**
 * @author Khalil Haroun Achache
 */
public final class RamController implements Component {
	private final Ram ram;
	private final int startAddress;
	private final int endAddress;
	
	/** RamController for the given ram, with span from startAddress to the endAddress of the ram
	 * @param ram : the ram
	 * @param startAddress : inclusive
	 * @param endAddress : exclusive
	 * @throws NullPointerException if the given Ram is Null
	 * @throws IllegalArgumetException if the startAdress or endAddress is not a 16bit value, or if the size is bigger than the ram size
	 */
	public RamController(Ram ram, int startAddress, int endAddress) {
		Objects.requireNonNull(ram);
		this.ram = ram;
		Preconditions.checkArgument(startAddress<=endAddress &&  endAddress-startAddress <=ram.size());
		this.startAddress = Preconditions.checkBits16(startAddress);
		this.endAddress = Preconditions.checkBits16(endAddress);
		
	}
	
	
	/** RamController for the given ram, with span from startAddress to the size of the ram
	 * @param ram
	 * @param startAddress
	 * @throws NullPointerException if the given Ram is Null
	 * @throws IllegalArgumetException if the startAdress or endAddress is not a 16bit value, or if the size is bigger than the ram size
	 */
	public RamController(Ram ram, int startAddress) {
		this(ram,startAddress,startAddress+ram.size());
	}
	
	
	@Override
	public int read(int address) {
		Preconditions.checkBits16(address);
		if(!(address>=startAddress && address < endAddress))
			return Component.NO_DATA;
		return ram.read(Preconditions.checkBits16(address-startAddress));
	}

	@Override
	public void write(int address, int data) {
		Preconditions.checkBits16(address);
		if((address>=startAddress && address < endAddress))
			ram.write(Preconditions.checkBits16(address-startAddress), Preconditions.checkBits8(data));
			
	}

}
