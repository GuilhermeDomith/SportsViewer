package gdrc.sports.io.bd.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import gdrc.sports.tipo.Ritmo;
import gdrc.sports.tipo.Ritmo.RitmoDetalhado;
import gdrc.sports.tipo.time.Hora;

/**
 * Contém os métodos para a persistência e recuperação 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link Ritmo}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RitmoDAO extends DataBaseDAO<Ritmo> {
	private RitmoDetalhadoDAO ritmoDetalhadoDAO;
	private RitmoRitmoDetDAO ritmoRitmoDetDAO;
	
	
	/**
	 * Construtor default que permite instanciar o objeto 
	 * com as dependências necessárias para conexão com 
	 * o banco de dados.  
	 */
	public RitmoDAO() {
		ritmoDetalhadoDAO = new RitmoDetalhadoDAO();
		ritmoRitmoDetDAO = new RitmoRitmoDetDAO();
	}
	
	
	/**
	 * Salva um objeto {@link Ritmo} no banco de dados e todos os seus objetos 
	 * contidos. Fornece uma nova implementação do método para salvar os dados 
	 * do objeto {@link Ritmo} que serão salvos em outras tabelas.
	 */
	@Override
	public boolean salvar(Ritmo ritmo) throws SQLException {
		PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		
		statement.setLong(1, ritmo.getCodigo());
		statement.setLong(2, ritmo.getRitmoMaximo().getHora().toNanoOfDay());
		statement.setLong(3, ritmo.getRitmoMedio().getHora().toNanoOfDay());
		
		statement.execute();
		statement.close();
		
		RitmoRitmoDetalhado ritmoRitmoDetalhado = new RitmoRitmoDetalhado();
		for(RitmoDetalhado ritmoDetalhado : ritmo.getDivisoes()) {
			/* Salva primeiro o exercicio detalhado que será referenciado por 
			 * ritmo_ritmo_detalhado, que associa um ritmo aos vários ritmos 
			 * detalhados.
			 */
			ritmoDetalhado.setCodigo(ritmoDetalhadoDAO.proximoValorSequence());
			ritmoDetalhadoDAO.salvar(ritmoDetalhado);
			
			ritmoRitmoDetalhado.setCodigoRitmo(ritmo.getCodigo());
			ritmoRitmoDetalhado.setCodigoRitmoDetalhado(ritmoDetalhado.getCodigo());
			ritmoRitmoDetDAO.salvar(ritmoRitmoDetalhado);
		}
		
		return true;
	}
	
	@Override
	public boolean excluir(Ritmo ritmo) throws SQLException {
		return excluirPorCodigo(ritmo.getCodigo());
	}

	@Override
	public Ritmo buscar(long codigo) throws SQLException {
		Ritmo ritmo = super.buscar(codigo);
		if(ritmo == null) return null;
		
		List<RitmoDetalhado> divisoes = ritmoRitmoDetDAO.buscarRitmosDetalhados(ritmo.getCodigo());
		ritmo.setDivisoes(divisoes);
		return ritmo;
	}

	@Override
	protected Ritmo capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		Ritmo ritmo = new Ritmo();
		ritmo.setCodigo(rs.getLong("codigo"));
		ritmo.setRitmoMaximo(new Hora(rs.getLong("ritmo_maximo")));
		ritmo.setRitmoMedio(new Hora(rs.getLong("ritmo_medio")));
		return ritmo;
	}
	
	@Override
	public String nomeTabela() {
		return "ritmo";
	}

	@Override
	public String colunasTabela() {
		return "codigo, ritmo_maximo, ritmo_medio";
	}
	
	@Override
	public String nomeSequence() {
		return "ritmo-seq";
	}

	
	
	/**
	 * Classe utilizada para fazer acesso aos dados contidos na tabela 
	 * ritmo_ritmo_detalhado. Tabela que faz a ligação entre um objeto {@link Ritmo}
	 * e seus variados {@link RitmoDetalhado}.<br><br>
	 * 
	 * É definida como acesso privado por ser utilizada apenas dentro da classe
	 * em que está definida.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	private class RitmoRitmoDetDAO extends DataBaseDAO<RitmoRitmoDetalhado>{

		@Override
		public boolean salvar(RitmoRitmoDetalhado ritmoRitmoDetalhado) throws SQLException {
			PreparedStatement statement = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
			
			statement.setLong(1, ritmoRitmoDetalhado.getCodigoRitmo());
			statement.setLong(2, ritmoRitmoDetalhado.getCodigoRitmoDetalhado());
			
			statement.execute();
			statement.close();
			return true;
		}
		
		/**
		 * Este método não é utilizado. Um objeto da tabela
		 * Ritmo_RitmoDetalhado é excluído em cascata, ou seja, excluído 
		 * automaticamente ao excluír um Ritmo ou RitmoDetalhado.
		 */
		@Override
		public boolean excluir(RitmoRitmoDetalhado objetoT) throws SQLException {
			return false;
		}
		
		/**
		 * Busca no banco de dados todos os objetos {@link RitmoDetalhado}
		 * referenciados pelo objeto {@link Ritmo}. O código do ritmo é 
		 * utilizado para buscar os objetos.
		 *  
		 * @param codigoRitmo 		O código do objeto {@link Ritmo}.
		 *  
		 * @return Retorna uma lista com todos os objetos {@link RitmoDetalhado}
		 * referenciado pelo {@link Ritmo}. Retorna uma lista vazia caso não 
		 * encontre nenhum.
		 * @throws SQLException Dispara uma execeção caso occorra um erro ao
		 * fazer a busca no banco de dados.
		 */
		public List<RitmoDetalhado> buscarRitmosDetalhados(long codigoRitmo) throws SQLException {
			/* Instrução SQL que primeiro seleciona todos os campos da tabela 
			 * ritmo_ritmo_detalhado que possuem o codigo_ritmo igual ao que foi 
			 * passado por parâmetro. E então seleciona todos os campos da tabela
			 * ritmo_detalhado que fazem referencia na seleção acima. Obtendo os 
			 * ritmos detalhados que pertencem apenas ao ritmo passado por parâmetro.
			 */
			String sql =  "select codigo, quilometro, ritmo " +
					 	  "from (select codigo_ritmo_detalhado " +
						        "from ritmo_ritmo_detalhado " +
						        "where codigo_ritmo=?) as J " +
						  "left join ritmo_detalhado as RD " +
						  "on J.codigo_ritmo_detalhado = RD.codigo";
			
			PreparedStatement stm = connection.prepareStatement(sql);
			stm.setLong(1, codigoRitmo);
			ResultSet rs = stm.executeQuery();
			
			List<RitmoDetalhado> ritmos = new ArrayList<>();
			while(rs.next()) 
				ritmos.add(ritmoDetalhadoDAO.capturarObjetoDoResultSet(rs));
		
			rs.close();
			stm.close();
			return ritmos;
		}

		@Override
		protected RitmoRitmoDetalhado capturarObjetoDoResultSet(ResultSet rs) 
																throws SQLException {
			
			RitmoRitmoDetalhado ritmoRitmoDetalhado = new RitmoRitmoDetalhado();
			ritmoRitmoDetalhado.setCodigoRitmo(rs.getLong("codigo_ritmo"));
			ritmoRitmoDetalhado.setCodigoRitmoDetalhado(rs.getLong("codigo_ritmo_detalhado"));
			return ritmoRitmoDetalhado;
		}
		
		@Override
		public String nomeTabela() {
			return "ritmo_ritmo_detalhado";
		}

		@Override
		public String colunasTabela() {
			return "codigo_ritmo, codigo_ritmo_detalhado";
		}
	} // class RitmoRitmoDetDAO.
	
	
	/**
	 * Esta classe é utilizada para agrupar os dados que serão salvos na tabela
	 * ritimo_ritmo_detalhado. Tabela que liga um objeto ritmo ao seus diversos
	 * ritmos detalhados. <br><br>
	 * 
	 * Portanto esta classe só será utilizada por esta classe onde está contida,
	 * {@link RitmoDAO}. Por esse motivo ela está definida como privada.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	private class RitmoRitmoDetalhado{
		private long codigoRitmo;
		private long codigoRitmoDetalhado;
		
		/**
		 * Construtor default para instanciar o objeto 
		 * desta classe. 
		 */
		public RitmoRitmoDetalhado() {}

		/**
		 * Obtém o código que identifica objeto {@link Ritmo}. 
		 * 
		 * @return Retorna o código único que identifica o objeto.
		 */
		public long getCodigoRitmo() {
			return codigoRitmo;
		}
		

		/**
		 * Insere o código que identifica objeto {@link Ritmo}
		 * 
		 * @param codigoRitmo O código único que identifica o objeto.
		 */
		public void setCodigoRitmo(long codigoRitmo) {
			this.codigoRitmo = codigoRitmo;
		}
		
		/**
		 * Obtém o código que identifica objeto {@link RitmoDetalhado}. 
		 * 
		 * @return Retorna o código único que identifica o objeto.
		 */
		public long getCodigoRitmoDetalhado() {
			return codigoRitmoDetalhado;
		}
		
		/**
		 * Insere o código que identifica objeto {@link RitmoDetalhado}.
		 * 
		 * @param codigoRitmoDetalhado O código único que identifica o objeto.
		 */
		public void setCodigoRitmoDetalhado(long codigoRitmoDetalhado) {
			this.codigoRitmoDetalhado = codigoRitmoDetalhado;
		}
		
		@Override
		public String toString() {
			return String.format("Código Ritmo: %d, Código Ritmo Detalhado: %d", 
					codigoRitmo, codigoRitmoDetalhado);
		}
	} // class RitmoRitmoDetalhado.
}
