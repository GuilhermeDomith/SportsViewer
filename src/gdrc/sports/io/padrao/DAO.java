package gdrc.sports.io.padrao;

import java.util.List;

/**
 * Interface que fornece uma assinatura dos métodos necessários para 
 * acesso e manipulação aos dados da aplicação. A interface pode ser utilizada 
 * para acesso à diferentes meios de armazenamento, desde que implemente os métodos
 * definidos por esta interface.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 * @param <T> O parametro de tipo que deve ser fornecido para o conhecimento do 
 * tipo de dado que está sendo manipulado na implementação.
 */
public interface DAO<T> {
	
	/**
	 * Salva o objeto passado por parâmetro no meio de armazenamento 
	 * utilizado pela aplicação.  
	 * 
	 * @param objetoT 	O objeto a ser salvo.
	 * 
	 * @return	Retorna <code>true</code> se o objeto foi salvo corretamente.
	 * @throws Exception Dispara uma execeção caso ocorra um erro ao salvar 
	 * o objeto.
	 */
	public boolean salvar(T objetoT) throws Exception;
	
	/**
	 * Exclui o objeto passado por parâmetro do meio de armazenamento 
	 * utilizado pela aplicação.  
	 * 
	 * @param objetoT 	O objeto a ser excluído.
	 * 
	 * @return Retorna <code>true</code> se o objeto foi excluído corretamente.
	 * @throws Exception Dispara uma execeção caso ocorra um erro ao excluir 
	 * o objeto.
	 */
	public boolean excluir(T objetoT) throws Exception;
	
	/**
	 * Busca do meio de armazenamento utilizado pela aplicação o objeto que possui 
	 * o código passado por parâmetro 
	 * 
	 * @param codigoRegistro 	O código a ser utilizado para buscar um objeto.
	 * 
	 * @return	Retorna o objeto caso tenha sido encontrado. Se não, retorna
	 * <code>null</code>.
	 * @throws Exception Dispara uma execeção caso ocorra um erro ao buscar 
	 * um objeto.
	 */
	public T buscar(long codigoRegistro) throws Exception;
	
	/**
	 * Lista todos os objeto do tipo <b>T</b> salvos no meio de armazenamento 
	 * da aplicação.
	 * 
	 * @return Retorna a lista de objetos do tipo <b>T</b>. 
	 * 
	 * @throws Exception Dispara a exceção caso ocorra um erro ao listar
	 * os objetos.
	 */
	public List<T> listarTodos() throws Exception;
	
}
