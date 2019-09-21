package gdrc.sports.io.bd.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gdrc.sports.io.padrao.DAO;
import gdrc.sports.io.padrao.InformacaoDaTabela;

/**
 * Classe utilizada para acessar, manipular, e manter uma conex�o 
 * �nica com o banco de dados.<br><br>
 * 
 * Os m�todos implementados por esta classe necessitam da 
 * implementa��o espec�fica de outros m�todos. Portanto, � necess�rio estender 
 * esta classe para fazer a persist�ncia um objeto do tipo <b>T</b>. <br>
 *  
 * Cada classe que estender esta classe estar� associada a um tavela no 
 * banco de dados, por isso dever� implementar os m�todos da 
 * interface {@link InformacaoDaTabela}. <br> 
 * Caso seja necess�rio outros modos de acesso ao banco de dados que n�o 
 * s�o fornecidos por esta classe, estes m�todos podem ser implementados 
 * pela classe que estender {@link DataBaseDAO} e utilizar a refer�ncia 
 * {@link #connection} mantida por ela.
 * 
 * A conex�o estabelecida com o banco de dados � obtida senguindo o padr�o 
 * de projeto de software <i>Singleton</i>, ou seja, garante que apenas uma 
 * �nica inst�ncia da conex�o com o banco ser� feita.
 *
 * @author Guilherme Domith Ribeiro Coelho
 *
 * @param <T> Par�metro de tipo que deve ser fornecido para identificar o tipo 
 * de dados que ser� manipulado. 
 * 
 */
public abstract class DataBaseDAO<T> implements DAO<T>, InformacaoDaTabela{
	private static final String USER_DB =  "postgres",
									 		SENHA_DB =  "aluno",
									 		DRIVER_DB = "jdbc:postgresql:",
									 		PATH_DB = "//localhost/AvaliacaoFisica";
	
	/**
	 * Enumera��o que indica qual o tipo de instru��o SQL que ser� gerado 
	 * por m�todos dessa classe.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public enum TipoInstrucaoSQL {SALVAR, EXCLUIR_POR_CODIGO, LISTAR_TODOS_REGISTROS, 
								  PROXIMA_SEQUENCIA, BUSCAR_POR_CODIGO}
	
	/** Guarda a connex�o com o banco de dados.*/
	protected static Connection connection;

	@Override
	public abstract boolean salvar(T obtejoT) throws SQLException;
	
	@Override
	public abstract boolean excluir(T objetoT) throws SQLException;
	
	
	/**
	 * Obt�m a instru��o SQL de acordo com o objeto passado por par�metro. Cada 
	 * instru��o necessitar� da insers�o dos dados do objeto do tipo <b>T</b> 
	 * no objeto {@link PreparedStatement} retornado. Caso seja:<br><br>
	 * 
	 * {@link TipoInstrucaoSQL#SALVAR} - Deve-se inserir os atributos do objeto <b>T</b>
	 * na mesma ordem em que � fornecido as colunas da tabela em {@link #colunasTabela()};
	 * <br>
	 * {@link TipoInstrucaoSQL#EXCLUIR_POR_CODIGO} - Deve-se inserir o c�digo do objeto <b>T</b>;
	 * <br>
	 * {@link TipoInstrucaoSQL#LISTAR_TODOS_REGISTROS} - N�o � necess�rio inserir dados;
	 * <br>
	 * {@link TipoInstrucaoSQL#PROXIMA_SEQUENCIA} - N�o � necess�rio inserir dados;
	 * <br>
	 * {@link TipoInstrucaoSQL#EXCLUIR_POR_CODIGO} - Deve-se inserir o c�digo do objeto <b>T</b>;
	 * 
	 * @param tipoAcesso Enumera��o que indentifica o tipo de instru��o SQL.
	 * 
	 * @return Retorna a instru��o SQL caso seja obtida com sucesso.
	 * @throws SQLException Dispara uma exece��o caso ocorra um erro ao obter a instru��o SQL.
	 */
	protected PreparedStatement obterInstrucaoSQL(TipoInstrucaoSQL tipoAcesso) throws SQLException{
		String sql;
		
		switch (tipoAcesso) {
		case SALVAR:
			sql = String.format("INSERT INTO %s(%s) VALUES (%s)", nomeTabela(),
										 colunasTabela(),
										 /* Utiliza a mesma string com os nomes da coluna para troca-los 
										  * pelo s�mbolo  '?', que ser� tratado pelo PreparedStatement. */
										 colunasTabela().replaceAll("[\\w]+", "?"));  
			break;
		case EXCLUIR_POR_CODIGO: 
			sql = String.format("DELETE FROM %s WHERE codigo=?", nomeTabela());
			break;
		case LISTAR_TODOS_REGISTROS:
			sql = String.format("SELECT * FROM %s;", nomeTabela());
			break;
		case PROXIMA_SEQUENCIA:
			sql = String.format("SELECT nextval('%s');",nomeSequence());
			break;
		case BUSCAR_POR_CODIGO:
			sql = String.format("SELECT * FROM %s WHERE codigo=?", nomeTabela());
			break;
		default: return null;
		}
		
		return connection.prepareStatement(sql);
	}
	
	
	/**
	 * Oferece uma implementa��o para a busca de dados que possuam o c�digo como
	 * chave prim�ria (PK) ou chave �nica (UK).<br> 
	 * Caso este m�todo seja chamado para obter dados de uma tabela que n�o tenha 
	 * um c�digo como chave prim�ria ou chave �nica certamente n�o ser� encontrado 
	 * nenhum dado.<br><br> 
	 * 
	 * Portanto, certifique-se de que a classe objeto <b>T</b> salvo possui o 
	 * campo c�digo. Para tabelas que n�o possua um campo c�digo a classe que 
	 * estender {@link DataBaseDAO} dever� implementar o pr�prio m�todo de busca, 
	 * especificando os par�metros necess�rios para a busca.
	 * 
	 * @param codigoRegistro 	O c�digo que faz refer�ncia ao objeto no banco de dados.
	 * 
	 * @return Retorna o objeto <b>T</b> que foi encontrado. Retorna <code>null</code> 
	 * caso n�o encontre o objeto com o codigo especificado.
	 * 
	 * @throws SQLException Dispara uma exce��o caso ocorra um erro ao encontrar o objeto ou 
	 * para o caso do objeto <b>T</b> n�o possuir o campo c�digo como dito acima.
	 */
	@Override
	public T  buscar(long codigoRegistro) throws SQLException {
		PreparedStatement stm = obterInstrucaoSQL(TipoInstrucaoSQL.BUSCAR_POR_CODIGO);
		stm.setLong(1, codigoRegistro);
		
		ResultSet rs = stm.executeQuery();
		
		T objetoT = null;
		if(rs.next()) 
			objetoT = capturarObjetoDoResultSet(rs);
	
		rs.close();
		stm.close();
		return objetoT;
	}
	
	/**
	 * Oferece uma implementa��o para deletar um registro que possua um c�digo como 
	 * chave prim�ria (PK) ou chave �nica (UK). <br><br>
	 * 
	 * Caso o registro n�o possua c�digo n�o ser� poss�vel realizar o delete a partir deste 
	 * m�todo. Portanto, neste caso, ser� necess�rio uma implementa��o espec�fica 
	 * que delete o registro a partir de outros campos chave.
	 * 
	 * @param codigoRegistro 	O c�digo �nico do registro a ser deletado.
	 * 
	 * @return Retorna <code>true</code> se o registro for deletado corretamente.
	 * @throws SQLException Dispara uma exce��o caso ocorra um erro ao deletar o dado 
	 * com o c�digo especificado. 
	 */
	public boolean excluirPorCodigo(long codigoRegistro) throws SQLException{
		PreparedStatement stm = obterInstrucaoSQL(TipoInstrucaoSQL.EXCLUIR_POR_CODIGO);
		
		stm.setLong(1, codigoRegistro);
		
		stm.execute();
		stm.close();
		return true;
	}
	

	/**
	 * Obt�m todos os dados salvos na tabela associada a classe que estender  
	 * {@link DataBaseDAO}.
	 * 
	 * @return Retorna a lista de objetos do tipo <b>T</b> salvos na tabela. 
	 * Caso n�o tenha nenhum dado a lista retornada ser� vazia. 
	 * 
	 * @throws SQLException Exce��o disparada se ocorrer um erro ao listar
	 * os objetos no banco de dados.
	 */
	@Override
	public List<T> listarTodos() throws SQLException {
		PreparedStatement stm = obterInstrucaoSQL(TipoInstrucaoSQL.LISTAR_TODOS_REGISTROS);
		ResultSet rs = stm.executeQuery();
		
		List<T> objetosT = new ArrayList<>();
		while(rs.next())
			objetosT.add(capturarObjetoDoResultSet(rs));
		
		rs.close();
		stm.close();
		return objetosT;
	}
	
	/**
	 * Obt�m o pr�ximo c�digo referente ao SEQUENCE associado a tabela 
	 * da classe que estender {@link DataBaseDAO}. <br><br>
	 * 
	 * Para o caso da classe n�o utilizar um SEQUENCE, ou seja, n�o oferecer 
	 * uma nova implementa��o para o m�todo {@link #nomeSequence()}, n�o ser� 
	 * poss�vel encontrar um novo c�digo. Portanto ir� disparar uma exece��o.
	 * 
	 * @return Retorna o pr�ximo c�digo gerado pelo sequence utilizado pela tabela.
	 * 
	 * @throws SQLException Dispara uma exece��o caso ocorra um erro ao buscar o novo
	 * valor do SEQUENCE, ou para o caso do SEQUENCE n�o ter sido localizado no banco de dados.
	 */
	public long proximoValorSequence() throws SQLException {
		PreparedStatement stm = obterInstrucaoSQL(TipoInstrucaoSQL.PROXIMA_SEQUENCIA);
		ResultSet rs = stm.executeQuery();
		
		long codigo = 0;
		if(rs.next()) 
			 codigo = rs.getLong("nextval");
		
		rs.close();
		stm.close();
		return codigo;
	}

	/**
	 * Instancia  e constr�i o objeto do tipo <b>T</b> com os dados obtidos 
	 * a partir do objeto {@link ResultSet} passado por par�metro.
	 *  
	 * @param resultSet 	Objeto {@link ResultSet} que mant�m os dados 
	 * recuperados do banco de dados.
	 * 
	 * @return 	Retorna o objeto que foi obtido no {@link ResultSet} e que 
	 * mant�m os dados que foram lidos no banco de dados.
	 * @throws	 SQLException Dispara uma exce��o aconte�a um erro ao ler a instru��o SQL
	 * ou caso a ordem de insers�o dos dados no objeto n�o seja a mesma que a ordem 
	 * estabelecida pela instru��o SQL. 
	 * 
	 */
	protected abstract T capturarObjetoDoResultSet(ResultSet resultSet) throws SQLException;
	
	/**
	 * Abre a conex�o com o banco de dados.
	 * 
	 * @return Retorna <code>true</code> se a conex�o for realizada com sucesso.
	 * 
	 * @throws SQLException Dispara a exece��o caso ocorra um erro ao
	 * abrir a conex�o com o banco de dados. 
	 */
	public static boolean abrirConexao() throws SQLException {
		if(connection == null)
			connection = DriverManager.getConnection(DRIVER_DB + PATH_DB, USER_DB, SENHA_DB);
		
		return true;
	}
	
	/**
	 * Encerra a conex�o com o banco de dados estabelecida por esta classe.
	 * 
	 * @return Retorna <code>true</code> se a conex�o tiver sido encerrada 
	 * corretamente, se n�o, retorna <code>false</code>. 
	 * 
	 * @throws SQLException Dispara a exece��o caso ocorra um erro ao
	 * fechar a conex�o com o banco de dados. 
	 */
	public static boolean fecharConexao() throws SQLException {
		if(connection != null)
			connection.close();
		
		connection = null;
		return true;
	}
}
