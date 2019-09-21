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
 * Contém os métodos para a persistência e recuperação 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link ExercicioDetalhado}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ExercicioDetalhadoDAO extends DataBaseDAO<ExercicioDetalhado>{
	private ExercicioDAO exercicioDAO;
	
	/**
	 * Construtor default que permite instanciar o objeto 
	 * com as dependências necessárias para conexão com 
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
	 * Pesquisa no banco de dados por exercícios detalhados que contenham as chaves que são
	 * fornecidas por parâmetro.<br><br>
	 * 
	 * As chaves a serem fornecidas podem ser <code>null</code>, de forma que seja 
	 * fornecido apenas aquela ou aquelas que se pretende pesquisar. Portanto, 
	 * se todas as chaves forem fornecidas é certo que a lista retornada terá apenas 1 
	 * exercício detalhado, se encontrado. Pois as chaves fornecidas formam a chave 
	 * primária da tabela que guarda os exercícios. 
	 * 
	 * @param email			O email do cliente para pesquisar exercícios detalhado com o mesmo.
	 * @param data			A data dos exercício para pesquisar exercícios detalhado com a mesma.
	 * @param tempoInicio	A hora de inicio do exercicio para pesquisar exercícios detalhadocom o mesmo.
	 * @param tempoFim		A hora do fim do exercicio para pesquisar exercícios detalhado com o mesmo.
	 * 
	 * @return Retorna a lista com todos os exercícios detalhado que foram encontrados 
	 * com as chaves fornecidas por parâmetro. Caso não encontre nenhum, retorna uma lista
	 * vazia.
	 * 
	 * @throws SQLException Dispara a execeção caso ocorra um erro ao fazer a busca no 
	 * banco de dados.
	 */
	public List<ExercicioDetalhado> buscarExercicioDetalhado(
			String email, Data data, Hora tempoInicio, Hora tempoFim) throws SQLException {
		
		PreparedStatement stm = instrucaoSQLBuscarExercicio(email, data, tempoInicio, tempoFim);
		ResultSet rs = stm.executeQuery();
		
		List<ExercicioDetalhado> exercicios = new ArrayList<>();
		while(rs.next()){
			//Obtém os dados do ResultSet que se referem a tabela exercicio.
			Exercicio exercicio = exercicioDAO.capturarObjetoDoResultSet(rs);
			//Obtém os dados do resultset que se referem a tabela exercicio_detalhado.
			ExercicioDetalhado exercicioDet = capturarObjetoDoResultSet(rs);
			exercicios.add(unirAtributosDosExercicios(exercicioDet, exercicio));
		}
		
		rs.close();
		stm.close();
		return exercicios;
	}
	
	
	/**
	 * Obtém a instrução SQL que busca todos os exercícios detalhados com as chaves 
	 * passadas por parâmetro. A instrução busca todos os exercícios que estão 
	 * na tabela exercicio_detalhado e com seus dados na tabela exercicio.
	 * Portanto, a instrução permite a busca apenas de objeto de instância 
	 * {@link ExercicioDetalhado}.  
	 */
	private PreparedStatement instrucaoSQLBuscarExercicio(
			String email, Data data, Hora tempoInicio, Hora tempoFim) throws SQLException {

		String sql = "select * from exercicio_detalhado as ED " 
				   + "left join exercicio as E "
				   + "on E.codigo = ED.codigo ";

		String condicoesPesquisa = "";		
		// Adiciona as condições de pesquisa que foram passasdas por parametro.
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
			// Obtém a string sem o último ";" para não ser substituído por "and" abaixo. 
			condicoesPesquisa = condicoesPesquisa.substring(0, condicoesPesquisa.lastIndexOf(";"));
			condicoesPesquisa = condicoesPesquisa.replaceAll(";"," and ");
			sql += "where" + condicoesPesquisa;
		}
		
		return connection.prepareStatement(sql);
	}
	
	/**
	 * Fornece uma nova implementação do método da subclasse para que seja inserida
	 * uma nova instrução SQL. Com esta implementação os dados buscados não serão apenas
	 * dados da tabela em que um objeto {@link ExercicioDetalhado} é salvo, mas também 
	 * são buscados dados dá superclasse {@link Exercicio}.<br><br>
	 * 
	 * Portanto, os objetos listados por este método contém inclusive os dados 
	 * da tabela de {@link Exercicio} que são referenciados pela tabela 
	 * de {@link ExercicioDetalhado}. 
	 */
	@Override
	public List<ExercicioDetalhado> listarTodos() throws SQLException {
		List<ExercicioDetalhado> exercicios = new ArrayList<>();
		/*Instrução SQL para selecionar os dados da tabela exercicio_detalhado e exercicio
		 * que possuem o mesmo código. Ou seja, obtém todos os atributos de um objeto
		 * Exercicio e ExercicioDetalhado, iguinorando aqueles que exercícios que não
		 * possuem dados na tabela exercicio_detalhado.
		 */
		String sql = "select * from exercicio_detalhado as ED " 
				   + "left join exercicio as E "
				   + "on E.codigo = ED.codigo";

		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rs = stm.executeQuery();
		
		while(rs.next()){
			//Obtém os dados do ResultSet que se referem a tabela exercicio.
			Exercicio exercicio = exercicioDAO.capturarObjetoDoResultSet(rs);
			//Obtém os dados do resultset que se referem a tabela exercicio_detalhado.
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
