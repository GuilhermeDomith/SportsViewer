package gdrc.sports.io.padrao;

/**
 * Esta interface define os m�todos necess�rios para obter as informa��es de 
 * uma tabela no banco de dados que est� associada a uma classe. Tabela na qual ser�o 
 * persistidos os objetos desta classe. <br>
 * 
 * A classe que implementar esta interface deve certificar-se de que
 * existe uma tabela, no banco de dados, com os mesmos dados 
 * fornecidos nos m�todos que ser�o implementados.
 *     
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public interface InformacaoDaTabela {
	
	/**
	 * Obt�m o nome da tabela do banco de dados que faz rer�ncia 
	 * � tabela usada para armazenar os dados da classe 
	 * que implementa esta interface.
	 * 
	 * @return Retorna o nome da tabela no banco de dados 
	 * referente a classe atual, classe que implementa a interface.
	 */
	public String nomeTabela();
	
	/**
	 * Obt�m os nomes das colunas da tabela. Colunas que est�o associadas
	 * aos atributos do objeto da classe que implementa a interface. Ao implementar 
	 * este m�todo tenha certeza de colocar os nomes das colunas separadas por ','. <br>
	 * Por exemplo: Se a classe for Aluno e  guarda na tabela 'aluno' os atributos 
	 * 'matricula', 'nome' e 'nota'. Ent�o, neste exemplo deve retornar uma {@link String} 
	 * no formato:<br><br>
	 * 
	 * <center><b>"matricula, nome, nota"</b></center>
	 * 
	 *  
	 * @return Retorna a <code>String</code> com os nomes das colunas.
	 */
	public String colunasTabela();
	
	/**
	 * Obt�m o nome da sequencia respons�vel por gerar um novo codigo para 
	 * o dado que est� sendo gravado. <br><br>
	 * 
	 * Visto que, n�o s�o todas as tabelas que utilizam o c�digo como gera��o autom�tica, 
	 * ent�o, este m�todo deve ser sobrescrito com uma nova implementa��o apenas por classes 
	 * que s�o persistidas em uma tabela que possua um SEQUENCE associado a ela.
	 *    
	 * @return Retorna o nome do sequence referente � tabela associado a classe.
	 * Retorn uma {@link String} vazia se n�o hover uma nova implementa��o deste m�todo,
	 * indicando que a tabela associada a classe n�o possui um sequence associados a ela.
	 */
	public default String nomeSequence() {
		return "";
	}
}
