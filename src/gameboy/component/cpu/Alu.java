package gameboy.component.cpu;

import java.util.Objects;

import gameboy.Preconditions;
import gameboy.bits.Bit;
import gameboy.bits.Bits;

/**
 * @author Khalil Haroun Achache
 */
public final class Alu {	
	
	private final static int BYTE_MASK = 0xFF;
	private final static int H_BYTE_MASK = 0xF;
	
	private Alu() {
	}
	
	private enum Flag implements Bit{
		UNUSED_0,
		UNUSED_1,
		UNUSED_2,
		UNUSED_3, 
		/**
		 * True iff the result is zero 
		 */
		C,
		/**
		 * True iff the operation is a substraction 
		 */
		H,
		/**
		 * True iff the first N/2 bits generated a carry 
		 */
		N,
		/**
		 * True iff the last N/2 bits generated a carry 
		 */
		Z
	}
	private enum RotDir{
		LEFT, RIGHT
	}
	
	
	
	
	public static int maskZNHC(boolean z, boolean n, boolean h, boolean c) {
		return packValueZNHC(0,z,n,h,c);
	}
	
	
	public static int unpackValue(int valueFlags) {
		return Bits.extract(valueFlags, Flag.Z.index()+1, 16);
	}
	
	public static int unpackFlags(int valueFlags) {
		return Bits.extract(valueFlags, 0, 8);
	}
	
	public static int add(int l, int r, boolean c0) {
		Preconditions.checkBits8(l);
		Preconditions.checkBits8(r);
		int sum = l + r + b2i(c0);
		boolean c = sum > BYTE_MASK;
		boolean h = (l&0xF)+(r&0xF)+b2i(c0) > H_BYTE_MASK;
		int value = sum & BYTE_MASK;
		return packValueZNHC(value,value==0,false,h,c);
	}
	
	public static int add(int l, int r) {
		return add(l,r,false);
	}
	
	public static int add16L(int l, int r) {
		Preconditions.checkBits16(l);
		Preconditions.checkBits16(r);
		
		int sum = (l+r)&0xFFFF;
		boolean c = (l& BYTE_MASK ) + (r & BYTE_MASK) > BYTE_MASK;
		boolean h = (l&H_BYTE_MASK) + (r&H_BYTE_MASK) > H_BYTE_MASK;
		return packValueZNHC(sum,false,false,h,c);
	}
	public static int add16H(int l, int r) {
		Preconditions.checkBits16(l);
		Preconditions.checkBits16(r);
		int sum = (l+r)&0xFFFF;
		boolean c = (l& 0xFFFF ) + (r&0xFFFF) > 0xFFFF;
		boolean h = (l& 0xFFF) + (r& 0xFFF) > 0xFFF;
		return packValueZNHC(sum,false,false,h,c);
	}
	
	
	public static int sub(int l, int r, boolean b0) {
		Preconditions.checkBits8(l);
		Preconditions.checkBits8(r);
		int sub = l - r - b2i(b0);
		boolean h =(l& H_BYTE_MASK)-(r& H_BYTE_MASK)- b2i(b0) < 0;
		boolean c = sub < 0;
		int value = sub & BYTE_MASK;
		return packValueZNHC(value,value==0,true,h,c);
	}
	public static int sub(int l, int r) {
		return sub(l,r,false);
	}
	public static int bcdAdjust(int v, boolean n, boolean h, boolean c) {
		Preconditions.checkBits16(v);
		int fixL = b2i(h || (!n && (v&H_BYTE_MASK)>9));
		int fixH = b2i(c || (!n && (v>0x99)));
		int fix = fixH * 0x60 + fixL * 0x06;
		int value = n ?  v-fix : v+fix;
		value = value&BYTE_MASK;
		return packValueZNHC(value,value==0,n,false,(c || (!n && (v>0x99))));
	}
	
	public static int and(int l, int r) {
		Preconditions.checkBits8(l);
		Preconditions.checkBits8(r);
		int value = l&r;
		
		return packValueZNHC(value, value==0, false,true,false);
	}
	
	public static int or(int l, int r) {
		Preconditions.checkBits8(l);
		Preconditions.checkBits8(r);
		int value = l|r;
		
		return packValueZNHC(value, value==0, false,false,false);
	}
	
	public static int xor(int l, int r) {
		Preconditions.checkBits8(l);
		Preconditions.checkBits8(r);
		int value = l^r;
		
		return packValueZNHC(value, value==0, false,false,false);
	}
	
	public static int shiftLeft(int v) {
		Preconditions.checkBits8(v);
		boolean c = Bits.test(v, 7);
		int value = (v<<1) & BYTE_MASK;
		
		return packValueZNHC(value,value==0,false,false,c);
	}
	
	public static int shiftRightA(int v) {
		Preconditions.checkBits8(v);
		v = Bits.signExtend8(v);
		boolean c = Bits.test(v, 0);
		int value = (v>>1) & BYTE_MASK;
		
		return packValueZNHC(value,value==0,false,false,c);
	}
	public static int shiftRightL(int v) {
		Preconditions.checkBits8(v);
		boolean c = Bits.test(v, 0);
		int value = (v>>1) & BYTE_MASK;
		
		return packValueZNHC(value,value==0,false,false,c);
	}
	
	
	public static int rotate(RotDir d, int v) {
		Preconditions.checkBits8(v);
		boolean c = Bits.test(v, d == RotDir.LEFT ? 7:0);
		int value = Bits.rotate(8, v, d == RotDir.LEFT ? 1:-1);
		
		return packValueZNHC(value,value==0,false,false,c);
	}
	public static int rotate(RotDir d, int v, boolean c) {
		Preconditions.checkBits8(v);
		int value = Bits.rotate(9, Bits.set(v, 8, c), d == RotDir.LEFT ? 1:-1);
		boolean newc = Bits.test(value, 8);
		value = value& BYTE_MASK;
		return packValueZNHC(value,value==0,false,false,newc);
	}
	
	
	public static int swap(int v) {
		Preconditions.checkBits8(v);
		int temp = (v & H_BYTE_MASK)<< 4 ;
		v = temp | ((v & 0xF0)>>4);
		return packValueZNHC(v,v==0,false,false,false);
	}
	public static int testBit(int v, int bitIndex) {
		Preconditions.checkBits8(v);
		Objects.checkIndex(bitIndex, 8);
		boolean z = !Bits.test(v, bitIndex);
		return packValueZNHC(0,z, false, true, false);
	}
	
	
	
	private static int b2i(boolean b) {
		return b ? 1 : 0; 
	}
	
	private static int packValueZNHC(int v,boolean z,boolean n,boolean h,boolean c) {
		int r =0;
		if(z)
			r=r|(1<<(Flag.Z.index()));
		
		if(n)
			r=r|(1<<(Flag.N.index()));
		
		if (h) 
			r=r|(1<<(Flag.H.index()));
		
		if (c)
			r=r|(1<<(Flag.C.index())); 
		r=r|(v<<(Flag.Z.index())+1);
		
		return r;
		}
}
