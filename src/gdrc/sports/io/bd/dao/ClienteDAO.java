package gdrc.sports.io.bd.dao;

import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.time.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contém os métodos para a persistência e recuperação 
 * no banco de dados na tabela que guarda os dados 
 * referente a um objeto do tipo {@link Cliente}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ClienteDAO extends DataBaseDAO<Cliente>{
	
	@Override
	public boolean salvar(Cliente cliente) throws SQLException {
		PreparedStatement stm = obterInstrucaoSQL(TipoInstrucaoSQL.SALVAR);
		
		stm.setString(1, cliente.getEmail());
		stm.setString(2, cliente.getNome());
		stm.setString(3, cliente.getSexo());
		stm.setFloat(4, cliente.getPeso());
		stm.setFloat(5, cliente.getAltura());
		stm.setLong(6, cliente.getDataNascimento().getData().toEpochDay());
		
		stm.execute();
		stm.close();
		return true;
	}
	
	@Override
	public boolean excluir(Cliente cliente) throws SQLException {
		String sql = "DELETE FROM cliente WHERE email=?";
		
		PreparedStatement stm = connection.prepareStatement(sql);
		stm.setString(1, cliente.getEmail());
		
		stm.execute();
		stm.close();
		return true;
	}

	/**
	 * Busca um cliente no banco de dados através do atributo <code>email</code> 
	 * passado por parâmetro.<br><br>  
	 * O email fornecido será utilizado para buscar apenas um cliente, 
	 * visto que o email é utilizado com chave primária do cliente.
	 *   
	 * @param email O email utilizado para buscar o cliente no banco de dados.
	 * 
	 * @return Caso encontre um cliente com o email fornecido retorna um objeto 
	 * {@link Cliente}. Se não, retorna <code>null</code>.
	 * 
	 * @throws SQLException Dispara a exceção caso ocorra um erro ao fazer a busca
	 * no banco de dados.  
	 */
	public Cliente buscaClientePorEmail(String email) throws SQLException {
		Cliente cliente = null;
		String sql = "SELECT * FROM cliente WHERE email=?";
		
		PreparedStatement stm = connection.prepareStatement(sql);
		stm.setString(1, email);
		ResultSet rs = stm.executeQuery();
		
		if(rs.next())
			cliente = capturarObjetoDoResultSet(rs);
	
		rs.close();
		stm.close();
		
		return cliente;
	}
	
	/**
	 * Busca um cliente no banco de dados através do atributo <code>nome</code> 
	 * passado por parâmetro.<br><br>  
	 * O nome fornecido será utilizado para buscar vários clientes, pois o 
	 * critério de pesquisa é que o cliente tenha pelo menos um trecho no 
	 * seu nome igual ao que foi fornecido por parâmetro. 
	 *   
	 * @param nome O nome utilizado para buscar os clientes no banco de dados.
	 * 
	 * @return Caso encontre clientes com o nome fornecido retorna uma lista
	 * com todos os encontrados. Se não encontrar, retorna uma lista vazia.
	 * 
	 * @throws SQLException Dispara a exceção caso ocorra um erro ao fazer a busca
	 * no banco de dados.  
	 */
	public List<Cliente> buscarClientePorNome(String nome) throws SQLException {
		List<Cliente> clientes = new ArrayList<>();
		String sql = "SELECT * FROM cliente WHERE nome ilike ?";
		
		PreparedStatement stm = connection.prepareStatement(sql);
		stm.setString(1, "%"+nome+"%");
		ResultSet rs = stm.executeQuery();
		
		while(rs.next())
			clientes.add(capturarObjetoDoResultSet(rs));
	
		rs.close();
		stm.close();
		return clientes;
	}

	@Override
	protected Cliente capturarObjetoDoResultSet(ResultSet rs) throws SQLException {
		Cliente cliente = new Cliente();
		
		cliente.setEmail(rs.getString("email"));
		cliente.setNome(rs.getString("nome"));
		cliente.setSexo(rs.getString("sexo"));
		cliente.setPeso(rs.getFloat("peso"));
		cliente.setAltura(rs.getFloat("altura"));
		Data dataNascimento = new Data(LocalDate.ofEpochDay(rs.getLong("data_de_nascimento")));
		cliente.setDataNascimento(dataNascimento);
		
		return cliente;
	}
	
	@Override
	public String nomeTabela() {
		return "cliente";
	}

	@Override
	public String colunasTabela() {
		return "email, nome, sexo, peso, altura, data_de_nascimento";
	}

}
