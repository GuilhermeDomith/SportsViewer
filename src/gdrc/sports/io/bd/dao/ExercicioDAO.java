package gdrc.sports.io.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.Tempo;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.tipo.time.Hora;

/**
 * Cont�m os m�todos para a persist�ncia e recupera��o 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link Exercicio}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ExercicioDAO extends DataBaseDAO<Exercicio>{
	
	@Override
	public boolean salvar(Exercicio exercicio) throws SQLException {
		PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		
		statement.setString(1, exercicio.getCliente().getEmail());
		statement.setLong(2, exercicio.getTempo().getHoraInicio().getHora().toNanoOfDay());
		statement.setLong(3, exercicio.getTempo().getHoraFim().getHora().toNanoOfDay());
		statement.setLong(4, exercicio.getData().getData().toEpochDay());
		statement.setFloat(5, exercicio.getDistancia());
		statement.setFloat(6, exercicio.getCaloriasPerdidas());
		statement.setInt(7, exercicio.getPassos());
		statement.setLong(8, exercicio.getCodigo());
		statement.setLong(9, exercicio.getTempo().getDuracao().getHora().toNanoOfDay());
		statement.setString(10, exercicio.getExercicio());
		
		statement.execute();
		statement.close();
		return true;
	}
	
	@Override
	public boolean excluir(Exercicio exercicio) throws SQLException {
		String sql = "DELETE FROM exercicio WHERE email=? and data=? and tempo_inicio=? and tempo_fim=?;";
		
		PreparedStatement stm = connection.prepareStatement(sql);
		stm.setString(1, exercicio.getCliente().getEmail());
		stm.setLong(2, exercicio.getData().getData().toEpochDay());
		stm.setLong(3, exercicio.getTempo().getHoraInicio().getHora().toNanoOfDay());
		stm.setLong(4, exercicio.getTempo().getHoraFim().getHora().toNanoOfDay());
		
		stm.execute();
		stm.close();
		
		return true;
	}
	

	/**
	 * Pesquisa no banco de dados por exerc�cios que contenham as chaves que s�o
	 * fornecidas por par�metro.<br><br>
	 * 
	 * As chaves a serem fornecidas podem ser <code>null</code>, de forma que seja 
	 * fornecido apenas aquela ou aquelas que se pretende pesquisar. Portanto, se todas 
	 * as chaves forem fornecidas � certo que a lista retornada ter� apenas 1 
	 * exerc�cio, se encontrado. Pois as chaves fornecidas s�o formam a chave 
	 * prim�ria da tabela que guarda os exerc�cios. 
	 * 
	 * @param email			O email do cliente para pesquisar exerc�cios com o mesmo.
	 * @param data			A data dos exerc�cio para pesquisar exerc�cios com a mesma.
	 * @param tempoInicio	A hora de inicio do exercicio para pesquisar exerc�cios com o mesmo.
	 * @param tempoFim		A hora do fim do exercicio para pesquisar exerc�cios com o mesmo.
	 * 
	 * @return Retorna a lista com todos os exerc�cios que foram encontrados com as 
	 * chaves fornecidas por par�metro. Caso n�o encontre nenhum retorna uma lista
	 * vazia.
	 * 
	 * @throws SQLException Dispara a exece��o caso ocorra um erro ao fazer a busca no 
	 * banco de dados.
	 */
	public List<Exercicio> buscarExercicios(
			String email, Data data, Hora tempoInicio, Hora tempoFim) throws SQLException {
		
		PreparedStatement stm = instrucaoSQLBuscarExercicio(email, data, tempoInicio, tempoFim);
		ResultSet rs = stm.executeQuery();
		
		List<Exercicio> exercicios = new ArrayList<>();
		while(rs.next()) 
			exercicios.add(capturarObjetoDoResultSet(rs));
	
		rs.close();
		stm.close();
		return exercicios;
	}
	
	/**
	 * Obt�m a instru��o SQL que busca todos os exerc�cios com as chaves 
	 * passadas por par�metro. A instru��o busca todos os exerc�cios que est�o 
	 * na tabela exercicio, mas n�o possui atributos na tabela exercicio_detalhado.
	 * Portanto, a instru��o permite a busca apenas de objeto de inst�ncia {@link Exercicio}.  
	 */
	private PreparedStatement instrucaoSQLBuscarExercicio(
			String email, Data data, Hora tempoInicio, Hora tempoFim) throws SQLException {
		
		String sql = String.format("select * from exercicio as E where not exists "
									+"(select codigo from exercicio_detalhado as ED "
									+ "where E.codigo = ED.codigo)");

		// Adiciona as condi��es de pesquisa que foram passadas por par�metro.
		if(email != null) {
			sql = String.format("%s and email='%s'", sql, email);
		}if(data != null) {
			sql = String.format("%s and data=%d", sql, data.getData().toEpochDay());
		}if(tempoInicio != null) { 
			sql = String.format("%s and tempo_inicio=%d", sql, tempoInicio.getHora().toNanoOfDay());
		}if(tempoFim != null) {
			sql = String.format("%s and tempo_fim=%d", sql, tempoFim.getHora().toNanoOfDay());
		}
		
		return connection.prepareStatement(sql);
	}

	@Override
	protected Exercicio capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		Exercicio exercicio = new Exercicio();
		
		Tempo tempo = new Tempo();
		tempo.setHoraInicio(new Hora(rs.getLong("tempo_inicio")));
		tempo.setHoraFim(new Hora(rs.getLong("tempo_fim")));
		tempo.setDuracao(new Hora(rs.getLong("duracao")));
		
		exercicio.setTempo(tempo);
		exercicio.getCliente().setEmail(rs.getString("email"));
		exercicio.setData(new Data(rs.getLong("data")));
		
		exercicio.setCaloriasPerdidas(rs.getFloat("calorias"));
		exercicio.setDistancia(rs.getFloat("distancia"));
		exercicio.setExercicio(rs.getString("exercicio"));
		exercicio.setPassos(rs.getInt("passos"));
		exercicio.setCodigo(rs.getLong("codigo"));
				
		return exercicio;
	}

	
	/**
	 * Fornece uma nova implementa��o do m�todo da subclasse para que seja inserida
	 * uma nova instru��o SQL para listar os exerc�cios. <br><br> 
	 * 
	 * Com esta implementa��o os dados buscados ser�o apenas dados da tabela 
	 * exercicio, mas que n�o possui dados salvos na tabela 
	 * exercicio_detalhado.
	 * 
	 * Portanto, os objetos listados por este m�todo cont�m os dados 
	 * da tabela de {@link Exercicio} excluindo aqueles que possui dados 
	 * adicionais na tabela de {@link ExercicioDetalhado}. 
	 */
	@Override
	public List<Exercicio> listarTodos() throws SQLException {
		List<Exercicio> exercicios = new ArrayList<>();
		/*Instru��o SQL para selecionar os dados da tabela exercicio, cujo c�digo
		 * n�o est� contido na tabela exercicio_detalhado. Ou seja, ir� obter os dados
		 * que s�o apenas instancia de Exerc�cio, ignorando os objeto Exerc�cioDetalhado.
		 */
		String sql = "select * from exercicio as E where not exists " 
				  + "(select codigo from exercicio_detalhado as ED "
				  + "where E.codigo = ED.codigo)";

		PreparedStatement stm = connection.prepareStatement(sql);
		ResultSet rs = stm.executeQuery();

		while(rs.next()){
			Exercicio exercicio = capturarObjetoDoResultSet(rs);
			exercicios.add(exercicio);
		}

		rs.close();
		stm.close();

		return exercicios;
	}
	
	@Override
	public String nomeTabela() {
		return "exercicio";
	}

	@Override
	public String colunasTabela() {
		return "email, tempo_inicio, tempo_fim, data, distancia, calorias, passos, codigo, duracao, exercicio";
	}
	
	@Override
	public String nomeSequence() {
		return "exercicio-seq";
	}

}
