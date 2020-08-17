package gameboy;

import gameboy.component.memory.Ram;
import gameboy.component.memory.RamController;

public final class GameBoy {
	private final Bus bus;
	private final RamController workRam;
	private final RamController echoRam;
	
	
	public GameBoy(Object cartridge) {
		this.bus = new Bus();
		Ram ram = new Ram (AddressMap.WORK_RAM_SIZE);
		this.workRam = new RamController(ram,AddressMap.WORK_RAM_START,AddressMap.WORK_RAM_END);
		this.echoRam = new RamController(ram,AddressMap.ECHO_RAM_START,AddressMap.ECHO_RAM_END);
		
		bus.attach(workRam);
		bus.attach(echoRam);
	}
	
	public Bus bus() {
		return this.bus;
	}
	
}
