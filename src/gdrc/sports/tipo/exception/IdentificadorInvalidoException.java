package gdrc.sports.tipo.exception;

/**
 * Sinaliza que um identificador procurado em arquivo 
 * n�o foi encontrado ou possui um valor associado ele 
 * diferente do que � esperado.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class IdentificadorInvalidoException extends Exception{
	
	/**
	 * Construtor sobrecarregado que instancia o objeto com 
	 * a mensagem passada por par�metro.
	 * 
	 * @param message Mensagem a ser exibida quando a Exce��o 
	 * for disparada. 
	 */
	public IdentificadorInvalidoException(String message) {
		super(message);
	}
	
}
