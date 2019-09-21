package gdrc.sports.util.controle;

import gdrc.sports.io.bd.dao.ClienteDAO;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe que permite manipular os dados dos objetos 
 * {@link Cliente} da aplicação.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class ControleDeClientes {
	private ControleDeExercicios controleDeExercicios;
	private ClienteDAO clienteDAO;
	
	/**
	 * Construtor que instancia o objeto com as conexões 
	 * necessárias para manipular os dados da aplicação. 
	 */
	public ControleDeClientes() {
		clienteDAO = new ClienteDAO();
		controleDeExercicios = new ControleDeExercicios();
	}
	
	/**
	 * Salva um cliente na base de dados da aplicação.
	 * 
	 * @param cliente 	O objeto {@link Cliente} a ser salvo.
	 * @return Retorna <code>true</code> se foi salvo com 
	 * sucesso, se não, retorna <code>false</code>.
	 */
	public boolean salvarCliente(Cliente cliente) {
		try { clienteDAO.salvar(cliente);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Busca um cliente com o email passado por parâmetro. 
	 * Não busca os exercícios feitos pelo cliente, apenas 
	 * as suas informações pessoais.
	 * 
	 * @param email O email a ser pesquisado.
	 * @return Retorna o cliente encontrado.
	 */
	public Cliente buscarClientePorEmail(String email) {
		Cliente cliente;
		try { cliente = clienteDAO.buscaClientePorEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return cliente;
	}
	
	/**
	 * Busca todos os clientes que tenham o nome igual ou tenha
	 * um trecho no nome igual ao que é passado por parâmetro.
	 * 
	 * @param nome 	O nome a ser pesquisado. 
	 * @return Retorna a lista com todos os clientes encontrados.
	 */
	public List<Cliente> buscarClientePorNome(String nome) {
		List<Cliente> clientes;
		try { clientes = clienteDAO.buscarClientePorNome(nome);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		return clientes;
	}
	
	/**
	 * Busca um cliente da aplicação com o mesmo email fornecido 
	 * por parâmetro. Caso encontre obtém todos os exercícios do
	 * mesmo. 
	 * 
	 * @param email 	O email a ser pesquisado.
	 * @return Retorna o cliente com todos os exercícios 
	 * físicos. 
	 */
	public Cliente buscarClienteComExercicios(String email) {
		Cliente cliente = buscarClientePorEmail(email);
		if(cliente == null) return null;
		
		List<Exercicio> exercicios = controleDeExercicios.listarExerciciosComChaves(email, null, null, null);
		cliente.setExercicios(exercicios);
		return cliente;
	}
	
	/**
	 * obtém uma lista com todos os clientes da aplicação.
	 * 
	 * @return Retorna a lista com todos os clientes salvos, caso 
	 * não tenha nenhum retorna uma lista vazia. 
	 */
	public List<Cliente> listarTodosClientes(){
		try { return clienteDAO.listarTodos();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
