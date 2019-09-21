package gdrc.sports.io.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gdrc.sports.tipo.Velocidade;

/**
 * Contém os métodos para a persistência e recuperação 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link Velocidade}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class VelocidadeDAO extends DataBaseDAO<Velocidade>{

	@Override
	public boolean salvar(Velocidade velocidade) throws SQLException {
		PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		statement.setLong(1, velocidade.getCodigo());
		statement.setFloat(2, velocidade.getVelocidadeMaxima());
		statement.setFloat(3, velocidade.getVelocidadeMedia());
		
		statement.execute();
		statement.close();
		return true;
	}
	
	@Override
	public boolean excluir(Velocidade velocidade) throws SQLException {
		return excluirPorCodigo(velocidade.getCodigo());
	}
	
	@Override
	protected Velocidade capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		Velocidade velocidade = new Velocidade();
		velocidade.setCodigo(rs.getLong("codigo"));
		velocidade.setVelocidadeMaxima(rs.getFloat("velocidade_maxima"));
		velocidade.setVelocidadeMedia(rs.getFloat("velocidade_media"));
		return velocidade;
	}
	
	@Override
	public String nomeTabela() {
		return "velocidade";
	}

	@Override
	public String colunasTabela() {
		return "codigo, velocidade_maxima, velocidade_media";
	}
	
	@Override
	public String nomeSequence() {
		return "velocidade-seq";
	}

}
