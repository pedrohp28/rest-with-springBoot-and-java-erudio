package restwithspringBootandjavaerudio.logica;

import restwithspringBootandjavaerudio.exceptions.UnsupportedMathOperationException;

public class Operacao {

	public static Double soma(double n1, double n2) {
	
		return n1 + n2;
		
	}
	
	public static Double subtracao(double n1, double n2) {
		
		return n1 - n2;
		
	}
	
	public static Double multiplicacao(double n1, double n2) {
		
		return n1 * n2;
		
	}
	
	public static Double divisao(double n1, double n2) {
		
		return n1 / n2;
		
	}
	
	public static Double media(double n1, double n2) {
		
		return (n1 + n2)/2;
		
	}
	
	public static Double raizQuadrada(double n1) {
		
		return Math.sqrt(n1);
		
	}
}
