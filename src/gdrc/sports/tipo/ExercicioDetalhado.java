package gdrc.sports.tipo;

import java.util.Comparator;

/**
 * Esta classe mant�m os dados de uma atividade f�sica realizada por 
 * um cliente que precisa ser detalhada como o ritmo, velocidade e eleva��o
 * durante a atividade.<br><br>
 * 
 * Como as informa��es desta classe � uma especifica��o de um exerc�cio, ou seja, 
 * precisa de mais detalhes que as atividades f�sicas comuns, esta classe herda as 
 * informa��es da classe mais generalizada {@link Exercicio}.
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
	 * desta classe com as informa��es do exerc�cio detalhado realizado 
	 * pelo cliente. 
	 *  
	 * @param ritmo  	O ritmo em que o cliente realizou o exerc�cio.
	 * @param velocidade  	A velocidade que o cliente atingiu no exerc�cio. 
	 * @param elevacao  	A eleva��o atingida pelo cliente durante o exerc�cio.
	 */
	public ExercicioDetalhado(Ritmo ritmo, Velocidade velocidade, Elevacao elevacao) {
		super();
		this.ritmo = ritmo;
		this.velocidade = velocidade;
		this.elevacao = elevacao;
	}

	/**
	 * Obt�m o ritmo em que o cliente realizou o exerc�cio.
	 * 
	 * @return Retorna o ritmo do cliente no exerc�co.
	 */
	public Ritmo getRitmo() {
		return ritmo;
	}

	/**
	 * Insere o ritmo em que o cliente realizou o exerc�cio.
	 * 
	 * @param ritmo O ritmo do cliente no exerc�co.
	 */
	public void setRitmo(Ritmo ritmo) {
		this.ritmo = ritmo;
	}

	/**
	 * Obt�m a velocidade atingida no exerc�cio pelo cliente.
	 * 
	 * @return Retorna a velocidade atingida.
	 */
	public Velocidade getVelocidade() {
		return velocidade;
	}

	/**
	 * Insere a velocidade atingida no exerc�cio pelo cliente.
	 * 
	 * @param velocidade A velocidade atingida.
	 */
	public void setVelocidade(Velocidade velocidade) {
		this.velocidade = velocidade;
	}

	/**
	 * Obt�m a eleva��o atingida pelo cliente durante o exerc�cio.
	 * 
	 * @return Retorna a eleva��o atingida pelo cliente.
	 */
	public Elevacao getElevacao() {
		return elevacao;
	}

	/**
	 * Insere a eleva��o atingida pelo cliente durante o exerc�cio. 
	 * 
	 * @param elevacao	A eleva��o atingida pelo cliente.
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
	 * Um {@link Comparator} que pode ser utilizado como crit�rio de 
	 * ordena��o em ordem ascendente de um conjunto de exerc�cios
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
