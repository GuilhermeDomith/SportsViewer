package gdrc.sports.io.padrao;

import java.util.List;

/**
 * Interface que fornece uma assinatura dos m�todos necess�rios para 
 * acesso e manipula��o aos dados da aplica��o. A interface pode ser utilizada 
 * para acesso � diferentes meios de armazenamento, desde que implemente os m�todos
 * definidos por esta interface.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 * @param <T> O parametro de tipo que deve ser fornecido para o conhecimento do 
 * tipo de dado que est� sendo manipulado na implementa��o.
 */
public interface DAO<T> {
	
	/**
	 * Salva o objeto passado por par�metro no meio de armazenamento 
	 * utilizado pela aplica��o.  
	 * 
	 * @param objetoT 	O objeto a ser salvo.
	 * 
	 * @return	Retorna <code>true</code> se o objeto foi salvo corretamente.
	 * @throws Exception Dispara uma exece��o caso ocorra um erro ao salvar 
	 * o objeto.
	 */
	public boolean salvar(T objetoT) throws Exception;
	
	/**
	 * Exclui o objeto passado por par�metro do meio de armazenamento 
	 * utilizado pela aplica��o.  
	 * 
	 * @param objetoT 	O objeto a ser exclu�do.
	 * 
	 * @return Retorna <code>true</code> se o objeto foi exclu�do corretamente.
	 * @throws Exception Dispara uma exece��o caso ocorra um erro ao excluir 
	 * o objeto.
	 */
	public boolean excluir(T objetoT) throws Exception;
	
	/**
	 * Busca do meio de armazenamento utilizado pela aplica��o o objeto que possui 
	 * o c�digo passado por par�metro 
	 * 
	 * @param codigoRegistro 	O c�digo a ser utilizado para buscar um objeto.
	 * 
	 * @return	Retorna o objeto caso tenha sido encontrado. Se n�o, retorna
	 * <code>null</code>.
	 * @throws Exception Dispara uma exece��o caso ocorra um erro ao buscar 
	 * um objeto.
	 */
	public T buscar(long codigoRegistro) throws Exception;
	
	/**
	 * Lista todos os objeto do tipo <b>T</b> salvos no meio de armazenamento 
	 * da aplica��o.
	 * 
	 * @return Retorna a lista de objetos do tipo <b>T</b>. 
	 * 
	 * @throws Exception Dispara a exce��o caso ocorra um erro ao listar
	 * os objetos.
	 */
	public List<T> listarTodos() throws Exception;
	
}
