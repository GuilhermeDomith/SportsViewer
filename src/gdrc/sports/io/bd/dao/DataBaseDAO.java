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
 * Classe utilizada para acessar, manipular, e manter uma conexão 
 * única com o banco de dados.<br><br>
 * 
 * Os métodos implementados por esta classe necessitam da 
 * implementação específica de outros métodos. Portanto, é necessário estender 
 * esta classe para fazer a persistência um objeto do tipo <b>T</b>. <br>
 *  
 * Cada classe que estender esta classe estará associada a um tavela no 
 * banco de dados, por isso deverá implementar os métodos da 
 * interface {@link InformacaoDaTabela}. <br> 
 * Caso seja necessário outros modos de acesso ao banco de dados que não 
 * são fornecidos por esta classe, estes métodos podem ser implementados 
 * pela classe que estender {@link DataBaseDAO} e utilizar a referência 
 * {@link #connection} mantida por ela.
 * 
 * A conexão estabelecida com o banco de dados é obtida senguindo o padrão 
 * de projeto de software <i>Singleton</i>, ou seja, garante que apenas uma 
 * única instância da conexão com o banco será feita.
 *
 * @author Guilherme Domith Ribeiro Coelho
 *
 * @param <T> Parâmetro de tipo que deve ser fornecido para identificar o tipo 
 * de dados que será manipulado. 
 * 
 */
public abstract class DataBaseDAO<T> implements DAO<T>, InformacaoDaTabela{
	private static final String USER_DB =  "postgres",
									 		SENHA_DB =  "aluno",
									 		DRIVER_DB = "jdbc:postgresql:",
									 		PATH_DB = "//localhost/AvaliacaoFisica";
	
	/**
	 * Enumeração que indica qual o tipo de instrução SQL que será gerado 
	 * por métodos dessa classe.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public enum TipoInstrucaoSQL {SALVAR, EXCLUIR_POR_CODIGO, LISTAR_TODOS_REGISTROS, 
								  PROXIMA_SEQUENCIA, BUSCAR_POR_CODIGO}
	
	/** Guarda a connexão com o banco de dados.*/
	protected static Connection connection;

	@Override
	public abstract boolean salvar(T obtejoT) throws SQLException;
	
	@Override
	public abstract boolean excluir(T objetoT) throws SQLException;
	
	
	/**
	 * Obtém a instrução SQL de acordo com o objeto passado por parâmetro. Cada 
	 * instrução necessitará da insersão dos dados do objeto do tipo <b>T</b> 
	 * no objeto {@link PreparedStatement} retornado. Caso seja:<br><br>
	 * 
	 * {@link TipoInstrucaoSQL#SALVAR} - Deve-se inserir os atributos do objeto <b>T</b>
	 * na mesma ordem em que é fornecido as colunas da tabela em {@link #colunasTabela()};
	 * <br>
	 * {@link TipoInstrucaoSQL#EXCLUIR_POR_CODIGO} - Deve-se inserir o código do objeto <b>T</b>;
	 * <br>
	 * {@link TipoInstrucaoSQL#LISTAR_TODOS_REGISTROS} - Não é necessário inserir dados;
	 * <br>
	 * {@link TipoInstrucaoSQL#PROXIMA_SEQUENCIA} - Não é necessário inserir dados;
	 * <br>
	 * {@link TipoInstrucaoSQL#EXCLUIR_POR_CODIGO} - Deve-se inserir o código do objeto <b>T</b>;
	 * 
	 * @param tipoAcesso Enumeração que indentifica o tipo de instrução SQL.
	 * 
	 * @return Retorna a instrução SQL caso seja obtida com sucesso.
	 * @throws SQLException Dispara uma execeção caso ocorra um erro ao obter a instrução SQL.
	 */
	protected PreparedStatement obterInstrucaoSQL(TipoInstrucaoSQL tipoAcesso) throws SQLException{
		String sql;
		
		switch (tipoAcesso) {
		case SALVAR:
			sql = String.format("INSERT INTO %s(%s) VALUES (%s)", nomeTabela(),
										 colunasTabela(),
										 /* Utiliza a mesma string com os nomes da coluna para troca-los 
										  * pelo símbolo  '?', que será tratado pelo PreparedStatement. */
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
	 * Oferece uma implementação para a busca de dados que possuam o código como
	 * chave primária (PK) ou chave única (UK).<br> 
	 * Caso este método seja chamado para obter dados de uma tabela que não tenha 
	 * um código como chave primária ou chave única certamente não será encontrado 
	 * nenhum dado.<br><br> 
	 * 
	 * Portanto, certifique-se de que a classe objeto <b>T</b> salvo possui o 
	 * campo código. Para tabelas que não possua um campo código a classe que 
	 * estender {@link DataBaseDAO} deverá implementar o próprio método de busca, 
	 * especificando os parâmetros necessários para a busca.
	 * 
	 * @param codigoRegistro 	O código que faz referência ao objeto no banco de dados.
	 * 
	 * @return Retorna o objeto <b>T</b> que foi encontrado. Retorna <code>null</code> 
	 * caso não encontre o objeto com o codigo especificado.
	 * 
	 * @throws SQLException Dispara uma exceção caso ocorra um erro ao encontrar o objeto ou 
	 * para o caso do objeto <b>T</b> não possuir o campo código como dito acima.
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
	 * Oferece uma implementação para deletar um registro que possua um código como 
	 * chave primária (PK) ou chave única (UK). <br><br>
	 * 
	 * Caso o registro não possua código não será possível realizar o delete a partir deste 
	 * método. Portanto, neste caso, será necessário uma implementação específica 
	 * que delete o registro a partir de outros campos chave.
	 * 
	 * @param codigoRegistro 	O código único do registro a ser deletado.
	 * 
	 * @return Retorna <code>true</code> se o registro for deletado corretamente.
	 * @throws SQLException Dispara uma exceção caso ocorra um erro ao deletar o dado 
	 * com o código especificado. 
	 */
	public boolean excluirPorCodigo(long codigoRegistro) throws SQLException{
		PreparedStatement stm = obterInstrucaoSQL(TipoInstrucaoSQL.EXCLUIR_POR_CODIGO);
		
		stm.setLong(1, codigoRegistro);
		
		stm.execute();
		stm.close();
		return true;
	}
	

	/**
	 * Obtém todos os dados salvos na tabela associada a classe que estender  
	 * {@link DataBaseDAO}.
	 * 
	 * @return Retorna a lista de objetos do tipo <b>T</b> salvos na tabela. 
	 * Caso não tenha nenhum dado a lista retornada será vazia. 
	 * 
	 * @throws SQLException Exceção disparada se ocorrer um erro ao listar
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
	 * Obtém o próximo código referente ao SEQUENCE associado a tabela 
	 * da classe que estender {@link DataBaseDAO}. <br><br>
	 * 
	 * Para o caso da classe não utilizar um SEQUENCE, ou seja, não oferecer 
	 * uma nova implementação para o método {@link #nomeSequence()}, não será 
	 * possível encontrar um novo código. Portanto irá disparar uma execeção.
	 * 
	 * @return Retorna o próximo código gerado pelo sequence utilizado pela tabela.
	 * 
	 * @throws SQLException Dispara uma execeção caso ocorra um erro ao buscar o novo
	 * valor do SEQUENCE, ou para o caso do SEQUENCE não ter sido localizado no banco de dados.
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
	 * Instancia  e constrói o objeto do tipo <b>T</b> com os dados obtidos 
	 * a partir do objeto {@link ResultSet} passado por parâmetro.
	 *  
	 * @param resultSet 	Objeto {@link ResultSet} que mantém os dados 
	 * recuperados do banco de dados.
	 * 
	 * @return 	Retorna o objeto que foi obtido no {@link ResultSet} e que 
	 * mantém os dados que foram lidos no banco de dados.
	 * @throws	 SQLException Dispara uma exceção aconteça um erro ao ler a instrução SQL
	 * ou caso a ordem de insersão dos dados no objeto não seja a mesma que a ordem 
	 * estabelecida pela instrução SQL. 
	 * 
	 */
	protected abstract T capturarObjetoDoResultSet(ResultSet resultSet) throws SQLException;
	
	/**
	 * Abre a conexão com o banco de dados.
	 * 
	 * @return Retorna <code>true</code> se a conexão for realizada com sucesso.
	 * 
	 * @throws SQLException Dispara a execeção caso ocorra um erro ao
	 * abrir a conexão com o banco de dados. 
	 */
	public static boolean abrirConexao() throws SQLException {
		if(connection == null)
			connection = DriverManager.getConnection(DRIVER_DB + PATH_DB, USER_DB, SENHA_DB);
		
		return true;
	}
	
	/**
	 * Encerra a conexão com o banco de dados estabelecida por esta classe.
	 * 
	 * @return Retorna <code>true</code> se a conexão tiver sido encerrada 
	 * corretamente, se não, retorna <code>false</code>. 
	 * 
	 * @throws SQLException Dispara a execeção caso ocorra um erro ao
	 * fechar a conexão com o banco de dados. 
	 */
	public static boolean fecharConexao() throws SQLException {
		if(connection != null)
			connection.close();
		
		connection = null;
		return true;
	}
}
