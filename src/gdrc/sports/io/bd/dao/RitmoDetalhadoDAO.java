package gdrc.sports.io.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import gdrc.sports.tipo.Ritmo.RitmoDetalhado;
import gdrc.sports.tipo.time.Hora;

/**
 * Contém os métodos para a persistência e recuperação 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link RitmoDetalhado}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RitmoDetalhadoDAO extends DataBaseDAO<RitmoDetalhado>{

	@Override
	public boolean salvar(RitmoDetalhado ritmo) throws SQLException {
		PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		
		statement.setLong(1, ritmo.getCodigo());
		statement.setFloat(2, ritmo.getQuilometro());
		statement.setLong(3, ritmo.getRitmo().getHora().toNanoOfDay());
		
		statement.execute();
		statement.close();
		return false;
	}
	
	@Override
	public boolean excluir(RitmoDetalhado ritmoDetalhado) throws SQLException {
		return excluirPorCodigo(ritmoDetalhado.getCodigo());
	}
	
	@Override
	protected RitmoDetalhado capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		RitmoDetalhado ritmo = new RitmoDetalhado();
		ritmo.setCodigo(rs.getLong("codigo"));
		ritmo.setQuilometro(rs.getFloat("quilometro"));
		ritmo.setRitmo(new Hora(rs.getLong("ritmo")));
		return ritmo;
	}
	
	@Override
	public String nomeTabela() {
		return "ritmo_detalhado";
	}

	@Override
	public String colunasTabela() {
		return "codigo, quilometro, ritmo";
	}
	
	@Override
	public String nomeSequence() {
		return "ritmo-detalhado-seq";
	}

}
