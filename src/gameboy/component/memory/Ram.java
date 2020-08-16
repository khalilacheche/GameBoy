package gameboy.component.memory;

import java.util.Objects;

import gameboy.Preconditions;

public final class Ram {
	private int[] data;
	private final int size;
	/**
	 * @param size
	 */
	public Ram(int size) {
		Preconditions.checkArgument(size>=0);
		this.size = size;
		this.data= new int[size];
	}
	
	/**
	 * @return
	 */
	public int size() {
		return this.size;
	}
	/**
	 * @param index
	 * @return
	 */
	public int read(int index) {
		return data[Objects.checkIndex(index,size)];
	}
	/**
	 * @param index
	 * @param value
	 */
	public void write(int index, int value) {		
		this.data[Objects.checkIndex(index,size)]= Preconditions.checkBits8(value); 		
	}

	
	
}
