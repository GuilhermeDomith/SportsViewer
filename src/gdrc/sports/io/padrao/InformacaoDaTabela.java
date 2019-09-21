package gdrc.sports.io.padrao;

/**
 * Esta interface define os métodos necessários para obter as informações de 
 * uma tabela no banco de dados que está associada a uma classe. Tabela na qual serão 
 * persistidos os objetos desta classe. <br>
 * 
 * A classe que implementar esta interface deve certificar-se de que
 * existe uma tabela, no banco de dados, com os mesmos dados 
 * fornecidos nos métodos que serão implementados.
 *     
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public interface InformacaoDaTabela {
	
	/**
	 * Obtém o nome da tabela do banco de dados que faz rerência 
	 * à tabela usada para armazenar os dados da classe 
	 * que implementa esta interface.
	 * 
	 * @return Retorna o nome da tabela no banco de dados 
	 * referente a classe atual, classe que implementa a interface.
	 */
	public String nomeTabela();
	
	/**
	 * Obtém os nomes das colunas da tabela. Colunas que estão associadas
	 * aos atributos do objeto da classe que implementa a interface. Ao implementar 
	 * este método tenha certeza de colocar os nomes das colunas separadas por ','. <br>
	 * Por exemplo: Se a classe for Aluno e  guarda na tabela 'aluno' os atributos 
	 * 'matricula', 'nome' e 'nota'. Então, neste exemplo deve retornar uma {@link String} 
	 * no formato:<br><br>
	 * 
	 * <center><b>"matricula, nome, nota"</b></center>
	 * 
	 *  
	 * @return Retorna a <code>String</code> com os nomes das colunas.
	 */
	public String colunasTabela();
	
	/**
	 * Obtém o nome da sequencia responsável por gerar um novo codigo para 
	 * o dado que está sendo gravado. <br><br>
	 * 
	 * Visto que, não são todas as tabelas que utilizam o código como geração automática, 
	 * então, este método deve ser sobrescrito com uma nova implementação apenas por classes 
	 * que são persistidas em uma tabela que possua um SEQUENCE associado a ela.
	 *    
	 * @return Retorna o nome do sequence referente à tabela associado a classe.
	 * Retorn uma {@link String} vazia se não hover uma nova implementação deste método,
	 * indicando que a tabela associada a classe não possui um sequence associados a ela.
	 */
	public default String nomeSequence() {
		return "";
	}
}
