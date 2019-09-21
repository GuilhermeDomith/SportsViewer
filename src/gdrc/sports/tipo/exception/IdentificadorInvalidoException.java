package gdrc.sports.tipo.exception;

/**
 * Sinaliza que um identificador procurado em arquivo 
 * não foi encontrado ou possui um valor associado ele 
 * diferente do que é esperado.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class IdentificadorInvalidoException extends Exception{
	
	/**
	 * Construtor sobrecarregado que instancia o objeto com 
	 * a mensagem passada por parâmetro.
	 * 
	 * @param message Mensagem a ser exibida quando a Exceção 
	 * for disparada. 
	 */
	public IdentificadorInvalidoException(String message) {
		super(message);
	}
	
}
