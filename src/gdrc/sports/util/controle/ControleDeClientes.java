package gdrc.sports.util.controle;

import gdrc.sports.io.bd.dao.ClienteDAO;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe que permite manipular os dados dos objetos 
 * {@link Cliente} da aplica��o.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class ControleDeClientes {
	private ControleDeExercicios controleDeExercicios;
	private ClienteDAO clienteDAO;
	
	/**
	 * Construtor que instancia o objeto com as conex�es 
	 * necess�rias para manipular os dados da aplica��o. 
	 */
	public ControleDeClientes() {
		clienteDAO = new ClienteDAO();
		controleDeExercicios = new ControleDeExercicios();
	}
	
	/**
	 * Salva um cliente na base de dados da aplica��o.
	 * 
	 * @param cliente 	O objeto {@link Cliente} a ser salvo.
	 * @return Retorna <code>true</code> se foi salvo com 
	 * sucesso, se n�o, retorna <code>false</code>.
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
	 * Busca um cliente com o email passado por par�metro. 
	 * N�o busca os exerc�cios feitos pelo cliente, apenas 
	 * as suas informa��es pessoais.
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
	 * um trecho no nome igual ao que � passado por par�metro.
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
	 * Busca um cliente da aplica��o com o mesmo email fornecido 
	 * por par�metro. Caso encontre obt�m todos os exerc�cios do
	 * mesmo. 
	 * 
	 * @param email 	O email a ser pesquisado.
	 * @return Retorna o cliente com todos os exerc�cios 
	 * f�sicos. 
	 */
	public Cliente buscarClienteComExercicios(String email) {
		Cliente cliente = buscarClientePorEmail(email);
		if(cliente == null) return null;
		
		List<Exercicio> exercicios = controleDeExercicios.listarExerciciosComChaves(email, null, null, null);
		cliente.setExercicios(exercicios);
		return cliente;
	}
	
	/**
	 * obt�m uma lista com todos os clientes da aplica��o.
	 * 
	 * @return Retorna a lista com todos os clientes salvos, caso 
	 * n�o tenha nenhum retorna uma lista vazia. 
	 */
	public List<Cliente> listarTodosClientes(){
		try { return clienteDAO.listarTodos();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

}
