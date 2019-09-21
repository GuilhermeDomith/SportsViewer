package gdrc.sports.io.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gdrc.sports.tipo.Elevacao;

/**
 * Contém os métodos para a persistência e recuperação 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link Elevacao}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ElevacaoDAO extends DataBaseDAO<Elevacao>{


	@Override
	public boolean salvar(Elevacao elevacao) throws SQLException {
		PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		statement.setLong(1, elevacao.getCodigo());
		statement.setInt(2, elevacao.getMaiorElevacao());
		statement.setInt(3, elevacao.getMenorElevacao());
		
		statement.execute();
		statement.close();
		return true;
	}
	
	@Override
	public boolean excluir(Elevacao elevacao) throws SQLException {
		return excluirPorCodigo(elevacao.getCodigo());
	}
	
	@Override
	protected Elevacao capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		Elevacao elevacao = new Elevacao();
		elevacao.setCodigo(rs.getLong("codigo"));
		elevacao.setMaiorElevacao(rs.getInt("maior_elevacao"));
		elevacao.setMenorElevacao(rs.getInt("menor_elevacao"));
		return elevacao;
	}
	
	@Override
	public String nomeTabela() {
		return "elevacao";
	}

	@Override
	public String colunasTabela() {
		return "codigo, maior_elevacao, menor_elevacao";
	}
	
	@Override
	public String nomeSequence() {
		return "elevacao-seq";
	}

}
