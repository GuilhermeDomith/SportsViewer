package gdrc.sports.tipo;

import java.util.Comparator;

/**
 * Esta classe mantém os dados de uma atividade física realizada por 
 * um cliente que precisa ser detalhada como o ritmo, velocidade e elevação
 * durante a atividade.<br><br>
 * 
 * Como as informações desta classe é uma especificação de um exercício, ou seja, 
 * precisa de mais detalhes que as atividades físicas comuns, esta classe herda as 
 * informações da classe mais generalizada {@link Exercicio}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class ExercicioDetalhado extends Exercicio {
	private Ritmo ritmo;
	private Velocidade velocidade;
	private Elevacao elevacao;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public ExercicioDetalhado() {
		super();
		ritmo = new Ritmo();
		velocidade = new Velocidade();
		elevacao = new Elevacao();
	}
	
	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com as informações do exercício detalhado realizado 
	 * pelo cliente. 
	 *  
	 * @param ritmo  	O ritmo em que o cliente realizou o exercício.
	 * @param velocidade  	A velocidade que o cliente atingiu no exercício. 
	 * @param elevacao  	A elevação atingida pelo cliente durante o exercício.
	 */
	public ExercicioDetalhado(Ritmo ritmo, Velocidade velocidade, Elevacao elevacao) {
		super();
		this.ritmo = ritmo;
		this.velocidade = velocidade;
		this.elevacao = elevacao;
	}

	/**
	 * Obtém o ritmo em que o cliente realizou o exercício.
	 * 
	 * @return Retorna o ritmo do cliente no exercíco.
	 */
	public Ritmo getRitmo() {
		return ritmo;
	}

	/**
	 * Insere o ritmo em que o cliente realizou o exercício.
	 * 
	 * @param ritmo O ritmo do cliente no exercíco.
	 */
	public void setRitmo(Ritmo ritmo) {
		this.ritmo = ritmo;
	}

	/**
	 * Obtém a velocidade atingida no exercício pelo cliente.
	 * 
	 * @return Retorna a velocidade atingida.
	 */
	public Velocidade getVelocidade() {
		return velocidade;
	}

	/**
	 * Insere a velocidade atingida no exercício pelo cliente.
	 * 
	 * @param velocidade A velocidade atingida.
	 */
	public void setVelocidade(Velocidade velocidade) {
		this.velocidade = velocidade;
	}

	/**
	 * Obtém a elevação atingida pelo cliente durante o exercício.
	 * 
	 * @return Retorna a elevação atingida pelo cliente.
	 */
	public Elevacao getElevacao() {
		return elevacao;
	}

	/**
	 * Insere a elevação atingida pelo cliente durante o exercício. 
	 * 
	 * @param elevacao	A elevação atingida pelo cliente.
	 */
	public void setElevacao(Elevacao elevacao) {
		this.elevacao = elevacao;
	}

	@Override
	public String toString() {
		return String.format("%s\nVelocidade: %s\nElevacao: %s\nRitmo: %s", super.toString(), 
				velocidade, elevacao, ritmo);
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como critério de 
	 * ordenação em ordem ascendente de um conjunto de exercícios
	 * por velocidade.
	 */
	public static class ComparatorVelocidadeExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			boolean ex1EhExcDetalhado = ex1 instanceof ExercicioDetalhado,
					ex2EhExcDetalhado = ex2 instanceof ExercicioDetalhado;

			if(ex1EhExcDetalhado && !ex2EhExcDetalhado) return 1;
			if(!ex1EhExcDetalhado && ex2EhExcDetalhado)  return -1;
			if(ex1EhExcDetalhado && ex2EhExcDetalhado) {
				ExercicioDetalhado exd1 = (ExercicioDetalhado) ex1,
								   exd2 = (ExercicioDetalhado) ex2;
				return Float.compare(exd1.getVelocidade().getVelocidadeMaxima(),
									 exd2.getVelocidade().getVelocidadeMaxima());
			}else return 0;
		}
	}

}
