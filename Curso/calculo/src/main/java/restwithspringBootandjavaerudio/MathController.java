package restwithspringBootandjavaerudio;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import restwithspringBootandjavaerudio.exceptions.UnsupportedMathOperationException;
import restwithspringBootandjavaerudio.logica.Operacao;
import restwithspringBootandjavaerudio.logica.Validacao;

@RestController
public class MathController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@RequestMapping(value = "/sum/{numberOne}/{numberTwo}", 
			method=RequestMethod.GET)
	public Double sum(@PathVariable(value = "numberOne") String numberOne,
					  @PathVariable(value = "numberTwo") String numberTwo) throws Exception{
		
		if(!Validacao.isNumeric(numberOne) || !Validacao.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}else {
			return Operacao.soma(Validacao.convertToDouble(numberOne), Validacao.convertToDouble(numberTwo));
		}
		
	}
	
	@RequestMapping(value = "/sub/{numberOne}/{numberTwo}", 
			method=RequestMethod.GET)
	public Double sub(@PathVariable(value = "numberOne") String numberOne,
					  @PathVariable(value = "numberTwo") String numberTwo) throws Exception{
		
		if(!Validacao.isNumeric(numberOne) || !Validacao.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}else {
			return Operacao.subtracao(Validacao.convertToDouble(numberOne), Validacao.convertToDouble(numberTwo));
		}
		
	}
	
	@RequestMapping(value = "/mult/{numberOne}/{numberTwo}", 
			method=RequestMethod.GET)
	public Double mult(@PathVariable(value = "numberOne") String numberOne,
					  @PathVariable(value = "numberTwo") String numberTwo) throws Exception{
		
		if(!Validacao.isNumeric(numberOne) || !Validacao.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}else {
			return Operacao.multiplicacao(Validacao.convertToDouble(numberOne), Validacao.convertToDouble(numberTwo));
		}
		
	}
	
	@RequestMapping(value = "/div/{numberOne}/{numberTwo}", 
			method=RequestMethod.GET)
	public Double div(@PathVariable(value = "numberOne") String numberOne,
					  @PathVariable(value = "numberTwo") String numberTwo) throws Exception{
		
		if(!Validacao.isNumeric(numberOne) || !Validacao.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}else {
			return Operacao.divisao(Validacao.convertToDouble(numberOne), Validacao.convertToDouble(numberTwo));
		}
		
	}

	@RequestMapping(value = "/med/{numberOne}/{numberTwo}", 
			method=RequestMethod.GET)
	public Double med(@PathVariable(value = "numberOne") String numberOne,
					  @PathVariable(value = "numberTwo") String numberTwo) throws Exception{
		
		if(!Validacao.isNumeric(numberOne) || !Validacao.isNumeric(numberTwo)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}else {
			return Operacao.media(Validacao.convertToDouble(numberOne), Validacao.convertToDouble(numberTwo));
		}
		
	}
	
	@RequestMapping(value = "/raiz/{numberOne}", 
			method=RequestMethod.GET)
	public Double raiz(@PathVariable(value = "numberOne") String numberOne) throws Exception{
		
		if(!Validacao.isNumeric(numberOne)) {
			throw new UnsupportedMathOperationException("Please set a numeric value");
		}else {
			return Operacao.raizQuadrada(Validacao.convertToDouble(numberOne));
		}
		
	}
	
}
