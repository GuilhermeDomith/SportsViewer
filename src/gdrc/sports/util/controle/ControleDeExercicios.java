package gdrc.sports.util.controle;

import java.sql.SQLException;
import java.util.List;

import gdrc.sports.io.bd.dao.ElevacaoDAO;
import gdrc.sports.io.bd.dao.ExercicioDAO;
import gdrc.sports.io.bd.dao.ExercicioDetalhadoDAO;
import gdrc.sports.io.bd.dao.RitmoDAO;
import gdrc.sports.io.bd.dao.RitmoDetalhadoDAO;
import gdrc.sports.io.bd.dao.VelocidadeDAO;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Elevacao;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.Ritmo;
import gdrc.sports.tipo.Velocidade;
import gdrc.sports.tipo.Ritmo.RitmoDetalhado;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.tipo.time.Hora;

/**
 * Mantém o controle único do acesso aos dados de um {@link Exercicio} 
 * e dos dados de classes que estendem {@link Exercicio}. <br><br>
 * 
 * As operações existentes sobre qualquer objeto que seja 
 * um {@link Exercicio} é implementada nesta classe. 
 * Para o caso de futuras edições no programa que adicionam classes que 
 * estendem {@link Exercicio}, os mesmos deverão ser adiciodos nesta 
 * classe para serem manipulados através das classes DAO.
 * 
 * @see gdrc.sports.io.bd.dao 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class ControleDeExercicios {
	// Acesso aos dados referente à um Exercício.
	private ExercicioDAO exercicioDAO;
	
	// Acesso aos dados referente à um Exercício Detalhado.
	private ExercicioDetalhadoDAO exercicioDetalhadoDAO;
	private ElevacaoDAO elevacaoDAO;
	private RitmoDAO ritmoDAO;
	private RitmoDetalhadoDAO ritmoDetalhadoDAO;
	private VelocidadeDAO velocidadeDAO;
	
	/**
	 * Construtor que instancia o objeto com as conexões 
	 * necessárias para manipular os dados da aplicação. 
	 */
	public ControleDeExercicios() {
		exercicioDAO = new ExercicioDAO();
		exercicioDetalhadoDAO = new ExercicioDetalhadoDAO();
		elevacaoDAO = new ElevacaoDAO();
		ritmoDAO = new RitmoDAO();
		ritmoDetalhadoDAO = new RitmoDetalhadoDAO();
		velocidadeDAO = new VelocidadeDAO();
	}
	
	/**
	 * Salva o objeto exercício no banco de dados. O exercício será salvo depois de salvar
	 * os objetos que o exercício faz referência em outras tabela, pois ele depende destes
	 * objetos já estarem salvos em suas tabelas para que possam ser referenciados na tabela
	 * de exercício.<br><br>
	 * 
	 * Não é necessário atribuir código ao objeto exercício e nem à seus objetos referencias,
	 * pois eles serão gerados automaticamente antes de salvar todos os objetos que utilizam 
	 * código para referenciá-lo. <br><br>
	 * 
	 * Caso o {@link Cliente} referenciado pelo exercício não esteja salvo no banco de dados
	 * o exercício não poderá ser salvo, pois o cliente é referenciado por um exercício. 
	 * Portanto, certifique-se de que o cliente foi salvo anteriormente.
	 *  
	 * @param exercicio O objeto {@link Exercicio} com todos os seus dados a serem salvos 
	 * no banco de dados.
	 * 
	 * @return Retorna <code>true</code> se o objeto for salvo corretamente. Retorna 
	 *  <code>false</code> caso ocorra um erro ao salvar o exercicio, certifique-se de
	 *  que o cliente está salvo no banco de dados. 
	 */
	public boolean salvarExercicio(Exercicio exercicio) {
		try {
			
			// Adiciona o código da exercicio que será utilizado para referênciá-lo.
			exercicio.setCodigo(exercicioDAO.proximoValorSequence());
			
			exercicioDAO.salvar(exercicio);
					
			if(exercicio instanceof ExercicioDetalhado) {
				ExercicioDetalhado exercicioDetalhado = (ExercicioDetalhado)exercicio; 
				
				// Adiciona o código da elevação que o exercicio detalhado irá fazer referência.
				exercicioDetalhado.getElevacao().setCodigo(elevacaoDAO.proximoValorSequence());
				elevacaoDAO.salvar(exercicioDetalhado.getElevacao());
				
				// Adiciona o código do ritmo que o exercicio detalhado irá fazer referência.
				exercicioDetalhado.getRitmo().setCodigo(ritmoDAO.proximoValorSequence());
				ritmoDAO.salvar(exercicioDetalhado.getRitmo());
				
				// Adiciona o código da velocidade que o exercicio detalhado irá fazer referência.
				exercicioDetalhado.getVelocidade().setCodigo(velocidadeDAO.proximoValorSequence());
				velocidadeDAO.salvar(exercicioDetalhado.getVelocidade());
				
				exercicioDetalhadoDAO.salvar(exercicioDetalhado);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return false; 
		}
		
		return true;
	}
	
	/**
	 * Exclui o exercício passado por parâmetro da base de dados da aplicação e 
	 * também todos os dados que são referenciados por ele. <br><br>
	 * 
	 * O exercício a ser excluído deve conter os dados email, data, horaInicio e 
	 * horaFim para que ele seja encontrado e excluído. Os dados referenciados 
	 * por ele deve conter o código para que tabém sejam excluídos.
	 * 
	 * @param exercicio 	O exercício a ser excluído.
	 * @return Retorna <code>true</code> se foi excluído com sucesso. Se não,
	 * retorna <code>false</code>.
	 */
	public boolean deletarExercicio(Exercicio exercicio) {
		try {
			exercicioDAO.excluir(exercicio);
			
			if(exercicio instanceof ExercicioDetalhado) {
				ExercicioDetalhado ex = (ExercicioDetalhado) exercicio;
				elevacaoDAO.excluir(ex.getElevacao());
				velocidadeDAO.excluir(ex.getVelocidade());
				ritmoDAO.excluir(ex.getRitmo());
				for(RitmoDetalhado ritmoDetalhado : ex.getRitmo().getDivisoes())
					ritmoDetalhadoDAO.excluir(ritmoDetalhado);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/**
	 * Lista todos os objetos que é um {@link Exercicio}. Ou seja, 
	 * obtém no banco de dados inclusive os objetos que estendem a
	 * classe {@link Exercicio}.
	 * 
	 * @return Retorna a lista com todos os tipos de exercicios lidos 
	 * do banco de dados. Caso não entrontre nenhum exercício, a lista será vazia.
	 * Se ocorrer um erro ao ler os dados no banco, retorna <code>null</code>.
	 * 
	 */
	public List<Exercicio> listarTodosExercicios(){
		/* Cria a lista para armazenar os dados referente a classe 
		 * Exercício no banco de dados
		 */
		List<Exercicio> exercicios;
		
		try {
			List<ExercicioDetalhado> exerciciosDetalhados = exercicioDetalhadoDAO.listarTodos();
			exercicios = exercicioDAO.listarTodos();
			
			//Obtém os dados de cada exercício detalhado que estão em outras tabelas.
			for(ExercicioDetalhado exercicioDet : exerciciosDetalhados) 
				obterDadosDeOutrasTabelas(exercicioDet);
			
			exercicios.addAll(exerciciosDetalhados);
		} catch (SQLException e) {
			e.printStackTrace();
			return null; 
		}
		
		return exercicios;
	}

	/**
	 * Obtém no banco de dados os dados que a classe {@link ExercicioDetalhado} faz 
	 * referência e armazena no objeto que será passado por parâmeto.<br></br>
	 * 
	 * Portanto, o exercício passado como parâmetro deve
	 * conter a chave primária (PK) ou a chave única (UC) 
	 * da tabela que será buscada os dados.<br></br>
	 * 
	 * Por exemplo: Atualmente um {@link ExercicioDetalhado} faz referência a 
	 * um objeto {@link Elevacao}, então o código da elevação deve estar contida 
	 * no objeto para que a elevação possa ser encontrado e então os outros dados 
	 * da elevação serão armazenados.
	 *   
	 * 
	 * @param exercicio O objeto contendo a chaves que identificam um
	 * dado que precisa ser buscado em outra tabela no banco de dados.
	 * 
	 * @return Retorna o objeto exercício com os dados de outras tabelas
	 * que um {@link ExercicioDetalhado} faz referência. 
	 * Retorna <code>null</code> caso ocorra um erro ao fazer a busca dos dados 
	 * referenciado pelo {@link ExercicioDetalhado} ou porque não foi fornecida 
	 * a chave necessária para buscar os dados.  
	 */
	private Exercicio obterDadosDeOutrasTabelas(ExercicioDetalhado exercicio) {
		if(exercicio == null) return null;
		
		try {
			long codigoVelocidade = exercicio.getVelocidade().getCodigo();
			Velocidade velocidade = velocidadeDAO.buscar(codigoVelocidade);
			exercicio.setVelocidade(velocidade);
			
			long codigoRitmo = exercicio.getRitmo().getCodigo();
			Ritmo ritmo = ritmoDAO.buscar(codigoRitmo);
			exercicio.setRitmo(ritmo);

			long codigoElevacao = exercicio.getElevacao().getCodigo();
			Elevacao elevacao = elevacaoDAO.buscar(codigoElevacao);
			exercicio.setElevacao(elevacao);
		} catch (SQLException | NullPointerException e) {
			e.printStackTrace();
			return null;
		}
		
		return exercicio;
	}
	
	
	/**
	 * Lista todos os exercícios da aplicação que tenham os mesmos dados que são 
	 * passados por parâmetro.<br><br>
	 *   
	 * Os dados que não precise ser pesquisado pode ser passado como <code>null</code>, 
	 * podendo passar até um único parâmetro. Se os quatro valores forem fornecidos 
	 * será encontrado apenas 1 ou nenhum exercício, pois os quatro valores 
	 * identificam um único exercício. 
	 * 
	 * @param email 		O email do exercício a ser pesquisado.
	 * @param data			A data do exercício a ser pesquisada
	 * @param tempoInicio	A hora de inicio do exercício a ser pesquisada.
	 * @param tempoFim		A hora do fim do exercício a ser pesquisado.
	 * 
	 * @return Retorna a lista com os exercícios encontrados. Se não encontrar nenhum 
	 * a lista será vazia. Caso ocorra um erro ao buscar os exercícios retorna <code>null</code>.
	 */
	public List<Exercicio> listarExerciciosComChaves(String email, Data data, Hora tempoInicio, Hora tempoFim) {
		
		/* Cria a lista para armazenar os dados referente a classe 
		 * Exercício no banco de dados
		 */
		List<Exercicio> exercicios;
		List<ExercicioDetalhado> exerciciosDet; 
		try {
			exercicios = exercicioDAO.buscarExercicios(email, data, tempoInicio, tempoFim);
			exerciciosDet = exercicioDetalhadoDAO.buscarExercicioDetalhado(email, data, tempoInicio, tempoFim);
			
			for(ExercicioDetalhado exercicioDet : exerciciosDet)
				obterDadosDeOutrasTabelas(exercicioDet);
			
			exercicios.addAll(exerciciosDet);
		} catch (SQLException e) {
			e.printStackTrace();
			return null; 
		}
		
		return exercicios;
	}

}
