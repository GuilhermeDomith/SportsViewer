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
 * Mant�m o controle �nico do acesso aos dados de um {@link Exercicio} 
 * e dos dados de classes que estendem {@link Exercicio}. <br><br>
 * 
 * As opera��es existentes sobre qualquer objeto que seja 
 * um {@link Exercicio} � implementada nesta classe. 
 * Para o caso de futuras edi��es no programa que adicionam classes que 
 * estendem {@link Exercicio}, os mesmos dever�o ser adiciodos nesta 
 * classe para serem manipulados atrav�s das classes DAO.
 * 
 * @see gdrc.sports.io.bd.dao 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class ControleDeExercicios {
	// Acesso aos dados referente � um Exerc�cio.
	private ExercicioDAO exercicioDAO;
	
	// Acesso aos dados referente � um Exerc�cio Detalhado.
	private ExercicioDetalhadoDAO exercicioDetalhadoDAO;
	private ElevacaoDAO elevacaoDAO;
	private RitmoDAO ritmoDAO;
	private RitmoDetalhadoDAO ritmoDetalhadoDAO;
	private VelocidadeDAO velocidadeDAO;
	
	/**
	 * Construtor que instancia o objeto com as conex�es 
	 * necess�rias para manipular os dados da aplica��o. 
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
	 * Salva o objeto exerc�cio no banco de dados. O exerc�cio ser� salvo depois de salvar
	 * os objetos que o exerc�cio faz refer�ncia em outras tabela, pois ele depende destes
	 * objetos j� estarem salvos em suas tabelas para que possam ser referenciados na tabela
	 * de exerc�cio.<br><br>
	 * 
	 * N�o � necess�rio atribuir c�digo ao objeto exerc�cio e nem � seus objetos referencias,
	 * pois eles ser�o gerados automaticamente antes de salvar todos os objetos que utilizam 
	 * c�digo para referenci�-lo. <br><br>
	 * 
	 * Caso o {@link Cliente} referenciado pelo exerc�cio n�o esteja salvo no banco de dados
	 * o exerc�cio n�o poder� ser salvo, pois o cliente � referenciado por um exerc�cio. 
	 * Portanto, certifique-se de que o cliente foi salvo anteriormente.
	 *  
	 * @param exercicio O objeto {@link Exercicio} com todos os seus dados a serem salvos 
	 * no banco de dados.
	 * 
	 * @return Retorna <code>true</code> se o objeto for salvo corretamente. Retorna 
	 *  <code>false</code> caso ocorra um erro ao salvar o exercicio, certifique-se de
	 *  que o cliente est� salvo no banco de dados. 
	 */
	public boolean salvarExercicio(Exercicio exercicio) {
		try {
			
			// Adiciona o c�digo da exercicio que ser� utilizado para refer�nci�-lo.
			exercicio.setCodigo(exercicioDAO.proximoValorSequence());
			
			exercicioDAO.salvar(exercicio);
					
			if(exercicio instanceof ExercicioDetalhado) {
				ExercicioDetalhado exercicioDetalhado = (ExercicioDetalhado)exercicio; 
				
				// Adiciona o c�digo da eleva��o que o exercicio detalhado ir� fazer refer�ncia.
				exercicioDetalhado.getElevacao().setCodigo(elevacaoDAO.proximoValorSequence());
				elevacaoDAO.salvar(exercicioDetalhado.getElevacao());
				
				// Adiciona o c�digo do ritmo que o exercicio detalhado ir� fazer refer�ncia.
				exercicioDetalhado.getRitmo().setCodigo(ritmoDAO.proximoValorSequence());
				ritmoDAO.salvar(exercicioDetalhado.getRitmo());
				
				// Adiciona o c�digo da velocidade que o exercicio detalhado ir� fazer refer�ncia.
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
	 * Exclui o exerc�cio passado por par�metro da base de dados da aplica��o e 
	 * tamb�m todos os dados que s�o referenciados por ele. <br><br>
	 * 
	 * O exerc�cio a ser exclu�do deve conter os dados email, data, horaInicio e 
	 * horaFim para que ele seja encontrado e exclu�do. Os dados referenciados 
	 * por ele deve conter o c�digo para que tab�m sejam exclu�dos.
	 * 
	 * @param exercicio 	O exerc�cio a ser exclu�do.
	 * @return Retorna <code>true</code> se foi exclu�do com sucesso. Se n�o,
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
	 * Lista todos os objetos que � um {@link Exercicio}. Ou seja, 
	 * obt�m no banco de dados inclusive os objetos que estendem a
	 * classe {@link Exercicio}.
	 * 
	 * @return Retorna a lista com todos os tipos de exercicios lidos 
	 * do banco de dados. Caso n�o entrontre nenhum exerc�cio, a lista ser� vazia.
	 * Se ocorrer um erro ao ler os dados no banco, retorna <code>null</code>.
	 * 
	 */
	public List<Exercicio> listarTodosExercicios(){
		/* Cria a lista para armazenar os dados referente a classe 
		 * Exerc�cio no banco de dados
		 */
		List<Exercicio> exercicios;
		
		try {
			List<ExercicioDetalhado> exerciciosDetalhados = exercicioDetalhadoDAO.listarTodos();
			exercicios = exercicioDAO.listarTodos();
			
			//Obt�m os dados de cada exerc�cio detalhado que est�o em outras tabelas.
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
	 * Obt�m no banco de dados os dados que a classe {@link ExercicioDetalhado} faz 
	 * refer�ncia e armazena no objeto que ser� passado por par�meto.<br></br>
	 * 
	 * Portanto, o exerc�cio passado como par�metro deve
	 * conter a chave prim�ria (PK) ou a chave �nica (UC) 
	 * da tabela que ser� buscada os dados.<br></br>
	 * 
	 * Por exemplo: Atualmente um {@link ExercicioDetalhado} faz refer�ncia a 
	 * um objeto {@link Elevacao}, ent�o o c�digo da eleva��o deve estar contida 
	 * no objeto para que a eleva��o possa ser encontrado e ent�o os outros dados 
	 * da eleva��o ser�o armazenados.
	 *   
	 * 
	 * @param exercicio O objeto contendo a chaves que identificam um
	 * dado que precisa ser buscado em outra tabela no banco de dados.
	 * 
	 * @return Retorna o objeto exerc�cio com os dados de outras tabelas
	 * que um {@link ExercicioDetalhado} faz refer�ncia. 
	 * Retorna <code>null</code> caso ocorra um erro ao fazer a busca dos dados 
	 * referenciado pelo {@link ExercicioDetalhado} ou porque n�o foi fornecida 
	 * a chave necess�ria para buscar os dados.  
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
	 * Lista todos os exerc�cios da aplica��o que tenham os mesmos dados que s�o 
	 * passados por par�metro.<br><br>
	 *   
	 * Os dados que n�o precise ser pesquisado pode ser passado como <code>null</code>, 
	 * podendo passar at� um �nico par�metro. Se os quatro valores forem fornecidos 
	 * ser� encontrado apenas 1 ou nenhum exerc�cio, pois os quatro valores 
	 * identificam um �nico exerc�cio. 
	 * 
	 * @param email 		O email do exerc�cio a ser pesquisado.
	 * @param data			A data do exerc�cio a ser pesquisada
	 * @param tempoInicio	A hora de inicio do exerc�cio a ser pesquisada.
	 * @param tempoFim		A hora do fim do exerc�cio a ser pesquisado.
	 * 
	 * @return Retorna a lista com os exerc�cios encontrados. Se n�o encontrar nenhum 
	 * a lista ser� vazia. Caso ocorra um erro ao buscar os exerc�cios retorna <code>null</code>.
	 */
	public List<Exercicio> listarExerciciosComChaves(String email, Data data, Hora tempoInicio, Hora tempoFim) {
		
		/* Cria a lista para armazenar os dados referente a classe 
		 * Exerc�cio no banco de dados
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
