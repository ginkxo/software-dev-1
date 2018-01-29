package e1;

public class GaussInteger {
	private int real, imag;
	
	GaussInteger() {
		this.real = 0;
		this.imag = 0;
	}
	
	GaussInteger(int real, int imag) {
		this.real = real;
		this.imag = imag;
	}
	
	public int getReal() {
		return real;
	}

	public void setReal(int real) {
		this.real = real;
	}

	public int getImag() {
		return imag;
	}

	public void setImag(int imag) {
		this.imag = imag;
	}

	// define and code the add method below
	public GaussInteger add(GaussInteger operand) {
		int real_sum = this.getReal() + operand.getReal();
		int imag_sum = this.getImag() + operand.getImag();
		GaussInteger new_integer = new GaussInteger(real_sum, imag_sum);
		return new_integer;
	}
	
	// define and code the moduleSquared method below
	public int moduleSquared() {
		return (this.getReal() * this.getReal()) + (this.getImag() * this.getImag());
	}
	
	// define and code the isInvertible method below
	public boolean isInvertible() {
		if (this.moduleSquared() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	// Add the necessary formal parameter
	public boolean equals(GaussInteger other) {
	   //boolean result = true;
	   // write code that assigns the correct value to result
		
	   boolean result = true;
	   if (this.getReal() == other.getReal() && this.getImag() == other.getImag()) {
		   return result;
	   } else {
		   result = false;
		   return result;
	   }
	}
	
	public String toString() {
		//String result = "";
		// your code goes here
		String real_component = Integer.toString(this.getReal());
		String imag_component = Integer.toString(this.getImag());
		if (this.getImag() > 0) {
			return real_component + "+" + imag_component + "i";
		} else {
			return real_component + imag_component + "i";
		}
	}

}