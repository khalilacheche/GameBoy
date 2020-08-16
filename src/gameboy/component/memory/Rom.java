package gameboy.component.memory;

import java.util.Arrays;
import java.util.Objects;


public final class Rom {
	private final byte[] data;
	
	public Rom(byte[] data) {
		Objects.requireNonNull(data);
		this.data = Arrays.copyOf(data, data.length);
	}
	
	public int size() {
		return data.length;
	}
	public int read(int index) {
		return Byte.toUnsignedInt(data[Objects.checkIndex(index,0xFF+1)]);
	}
}
