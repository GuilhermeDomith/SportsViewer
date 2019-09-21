package gdrc.sports.io.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gdrc.sports.tipo.Elevacao;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.Ritmo;
import gdrc.sports.tipo.Velocidade;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.tipo.time.Hora;

/**
 * Cont�m os m�todos para a persist�ncia e recupera��o 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link ExercicioDetalhado}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ExercicioDetalhadoDAO extends DataBaseDAO<ExercicioDetalhado>{
	private ExercicioDAO exercicioDAO;
	
	/**
	 * Construtor default que permite instanciar o objeto 
	 * com as depend�ncias necess�rias para conex�o com 
	 * o banco de dados.  
	 */
	public ExercicioDetalhadoDAO() {
		exercicioDAO = new ExercicioDAO();
	}
	
	@Override
	public boolean salvar(ExercicioDetalhado exercicioDetalhado) throws SQLException {
		PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		
		statement.setLong(1, exercicioDetalhado.getCodigo());
		statement.setLong(2, exercicioDetalhado.getVelocidade().getCodigo());
		statement.setLong(3, exercicioDetalhado.getRitmo().getCodigo());
		statement.setLong(4, exercicioDetalhado.getElevacao().getCodigo());
		
		statement.execute();
		statement.close();
		return true;
	}
	
	@Override
	public boolean excluir(ExercicioDetalhado exercicioDetalhado) throws SQLException {
		return excluirPorCodigo(exercicioDetalhado.getCodigo());
	}
	
	/**
	 * Pesquisa no banco de dados por exerc�cios detalhados que contenham as chaves que s�o
	 * fornecidas por par�metro.<br><br>
	 * 
	 * As chaves a serem fornecidas podem ser <code>null</code>, de forma que seja 
	 * fornecido apenas aquela ou aquelas que se pretende pesquisar. Portanto, 
	 * se todas as chaves forem fornecidas � certo que a lista retornada ter� apenas 1 
	 * exerc�cio detalhado, se encontrado. Pois as chaves fornecidas formam a chave 
	 * prim�ria da tabela que guarda os exerc�cios. 
	 * 
	 * @param email			O email do cliente para pesquisar exerc�cios detalhado com o mesmo.
	 * @param data			A data dos exerc�cio para pesquisar exerc�cios detalhado com a mesma.
	 * @param tempoInicio	A hora de inicio do exercicio para pesquisar exerc�cios detalhadocom o mesmo.
	 * @param tempoFim		A hora do fim do exercicio para pesquisar exerc�cios detalhado com o mesmo.
	 * 
	 * @return Retorna a lista com todos os exerc�cios detalhado que foram encontrados 
	 * com as chaves fornecidas por par�metro. Caso n�o encontre nenhum, retorna uma lista
	 * vazia.
	 * 
	 * @throws SQLException Dispara a exece��o caso ocorra um erro ao fazer a busca no 
	 * banco de dados.
	 */
	public List<ExercicioDetalhado> buscarExercicioDetalhado(
			String email, Data data, Hora tempoInicio, Hora tempoFim) throws SQLException {
		
		PreparedStatement stm = instrucaoSQLBuscarExercicio(email, data, tempoInicio, tempoFim);
		ResultSet rs = stm.executeQuery();
		
		List<ExercicioDetalhado> exercicios = new ArrayList<>();
		while(rs.next()){
			//Obt�m os dados do ResultSet que se referem a tabela exercicio.
			Exercicio exercicio = exercicioDAO.capturarObjetoDoResultSet(rs);
			//Obt�m os dados do resultset que se referem a tabela exercicio_detalhado.
			ExercicioDetalhado exercicioDet = capturarObjetoDoResultSet(rs);
			exercicios.add(unirAtributosDosExercicios(exercicioDet, exercicio));
		}
		
		rs.close();
		stm.close();
		return exercicios;
	}
	
	
	/**
	 * Obt�m a instru��o SQL que busca todos os exerc�cios detalhados com as chaves 
	 * passadas por par�metro. A instru��o busca todos os exerc�cios que est�o 
	 * na tabela exercicio_detalhado e com seus dados na tabela exercicio.
	 * Portanto, a instru��o permite a busca apenas de objeto de inst�ncia 
	 * {@link ExercicioDetalhado}.  
	 */
	private PreparedStatement instrucaoSQLBuscarExercicio(
			String email, Data data, Hora tempoInicio, Hora tempoFim) throws SQLException {

		String sql = "select * from exercicio_detalhado as ED " 
				   + "left join exercicio as E "
				   + "on E.codigo = ED.codigo ";

		String condicoesPesquisa = "";		
		// Adiciona as condi��es de pesquisa que foram passasdas por parametro.
		if(email != null)
			condicoesPesquisa = String.format(" email='%s';", email);
		
		if(data != null)
			condicoesPesquisa = String.format("%s data=%d;", condicoesPesquisa, 
												 data.getData().toEpochDay());
		if(tempoInicio != null)
			condicoesPesquisa = String.format("%s tempo_inicio=%d;", condicoesPesquisa, 
												 tempoInicio.getHora().toNanoOfDay());
		if(tempoFim != null)
			condicoesPesquisa = String.format("%s tempo_fim=%d;", condicoesPesquisa, 
												 tempoFim.getHora().toNanoOfDay());
		
		if(!condicoesPesquisa.isEmpty()) {
			// Obt�m a string sem o �ltimo ";" para n�o ser substitu�do por "and" abaixo. 
			condicoesPesquisa = condicoesPesquisa.substring(0, condicoesPesquisa.lastIndexOf(";"));
			condicoesPesquisa = condicoesPesquisa.replaceAll(";"," and ");
			sql += "where" + condicoesPesquisa;
		}
		
		return connection.prepareStatement(sql);
	}
	
	/**
	 * Fornece uma nova implementa��o do m�todo da subclasse para que seja inserida
	 * uma nova instru��o SQL. Com esta implementa��o os dados buscados n�o ser�o apenas
	 * dados da tabela em que um objeto {@link ExercicioDetalhado} � salvo, mas tamb�m 
	 * s�o buscados dados d� superclasse {@link Exercicio}.<br><br>
	 * 
	 * Portanto, os objetos listados por este m�todo cont�m inclusive os dados 
	 * da tabela de {@link Exercicio} que s�o referenciados pela tabela 
	 * de {@link ExercicioDetalhado}. 
	 */
	@Override
	public List<ExercicioDetalhado> listarTodos() throws SQLException {
		List<ExercicioDetalhado> exercicios = new ArrayList<>();
		/*Instru��o SQL para selecionar os dados da tabela exercicio_detalhado e exercicio
		 * que possuem o mesmo c�digo. Ou seja, obt�m todos os atributos de um objeto
		 * Exercicio e ExercicioDetalhado, iguinorando aqueles que exerc�cios que n�o
		 * possuem dados na tabela exercicio_detalhado.
		 */
		String sql = "select * from exercicio_detalhado as ED " 
				   + "left join exercicio as E "
				   + "on E.codigo = ED.codigo";

		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rs = stm.executeQuery();
		
		while(rs.next()){
			//Obt�m os dados do ResultSet que se referem a tabela exercicio.
			Exercicio exercicio = exercicioDAO.capturarObjetoDoResultSet(rs);
			//Obt�m os dados do resultset que se referem a tabela exercicio_detalhado.
			ExercicioDetalhado exercicioDet = capturarObjetoDoResultSet(rs);
			exercicios.add(unirAtributosDosExercicios(exercicioDet, exercicio));
		}
		
		rs.close();
		stm.close();
		return exercicios;
	}
	
	/**
	 * Insere todos os dados do objeto {@link Exercicio} dentro do objeto {@link ExercicioDetalhado}. 
	 */
	private ExercicioDetalhado unirAtributosDosExercicios(ExercicioDetalhado exercicioDet, Exercicio exercicio) {
		exercicioDet.setCliente(exercicio.getCliente());
		exercicioDet.setCaloriasPerdidas(exercicio.getCaloriasPerdidas());
		exercicioDet.setData(exercicio.getData());
		exercicioDet.setDistancia(exercicio.getDistancia());
		exercicioDet.setExercicio(exercicio.getExercicio());
		exercicioDet.setPassos(exercicio.getPassos());
		exercicioDet.setTempo(exercicio.getTempo());
		return exercicioDet;
	}
	
	@Override
	protected ExercicioDetalhado capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		ExercicioDetalhado exercicioDetalhado = new ExercicioDetalhado();
		exercicioDetalhado.setCodigo(rs.getLong("codigo"));
		
		Velocidade velocidade = new Velocidade();
		velocidade.setCodigo(rs.getLong("codigo_velocidade"));
		exercicioDetalhado.setVelocidade(velocidade);
		
		Ritmo ritmo = new Ritmo();
		ritmo.setCodigo(rs.getLong("codigo_ritmo"));
		exercicioDetalhado.setRitmo(ritmo);
		
		Elevacao elevacao = new Elevacao();
		elevacao.setCodigo(rs.getLong("codigo_elevacao"));
		exercicioDetalhado.setElevacao(elevacao);
		
		return exercicioDetalhado;
	}
	
	@Override
	public String nomeTabela() {
		return "exercicio_detalhado";
	}

	@Override
	public String colunasTabela() {
		return "codigo, codigo_velocidade, codigo_ritmo, codigo_elevacao";
	}
	
}
