package gdrc.sports.tipo;

import java.time.Duration;
import java.time.LocalTime;

import gdrc.sports.tipo.time.Hora;

/**
 * Mantém os atributos relacionados ao tempo realizado em uma 
 * determinada atividade.<br><br>
 * 
 * O dados que podem ser guardados são os dados que especificam a hora do 
 * início, hora do fim e duração total da atividade. A duração seria o 
 * mesmo que a diferença entre hora fim e hora inicio, mas pode ser mais
 * detalhada, contendo inclusive os segundos corridos na atividade.<br><br>
 *  
 * Portanto, a duração será calculada automaticamente somente se não for 
 * fornecida pelo usuário da classe.
 * 
 * @see Hora
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Tempo {
	private Hora horaInicio,
				 horaFim,
				 duracao;

	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Tempo() {
		this(new Hora(), new Hora(), new Hora());
	}

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com hora de inicio e hora do fim e duração 
	 * da atividade.
	 *  
	 * @param horaInicio   A hora que define o início da atividade.
	 * @param horaFim   A hora que define o fim da atividade.
	 * @param duracao   duração total da atividade.
	 */
	public Tempo(Hora horaInicio, Hora horaFim, Hora duracao) {
		super();
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.duracao = duracao;
	}
	
	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com a hora de inicio e hora do fim da atividade.<br><br>
	 * 
	 * Como não será atribuido a duração, ela será calculada automaticamente 
	 * utilizando a diferença entre hora fim e hora inicio da atividade. 
	 * Mas a duração poderá ser incluida posteriormente a partir dos métodos 
	 * de acesso se necessário.  
	 *  
	 * @param horaInicio  A hora que define o início da atividade.
	 * @param horaFim  A hora que define o fim da atividade.
	 */
	public Tempo(Hora horaInicio, Hora horaFim) {
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.duracao = calcularDuracaoTotal();
	}

	/**
	 * Permite calcular a duração total de uma atividade a partir dos
	 * dados que especificam a hora do início e hora do fim da atividade.
	 * <br><br>
	 * Atenção! Caso já tenha atribuido uma duração mais específica que as horas
	 * de inicio e fim da atividade, não é recomendado calcular a duração 
	 * novamente para não perder a que foi atribuída.
	 * 
	 * @return Retorna a duração total da atividade calculada a partir da 
	 * diferença entre a hora do fim e hora do início da atividade. 
	 */
	public Hora calcularDuracaoTotal() {
		this.duracao = new Hora();
		Duration duration = Duration.between(horaInicio.getHora(), horaFim.getHora());
		this.duracao.setHora(LocalTime.ofNanoOfDay(duration.toNanos())); 
		return this.duracao;
	}

	/**
	 * Obtém a hora de início da atividade realizada.
	 * 
	 * @return Retorna a hora do início. 
	 */
	public Hora getHoraInicio() {
		return horaInicio;
	}

	/**
	 * Insere a hora do início da atividade realizada.
	 * 
	 * @param horaInicio A hora que especifica o 
	 * início da atividade.
	 */
	public void setHoraInicio(Hora horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * Obtém a hora do fim da atividade realizada.
	 * 
	 * @return Retorna a hora do fim. 
	 */
	public Hora getHoraFim() {
		return horaFim;
	}

	/**
	 * Insere a hora do fim da atividade realizada.
	 * 
	 * @param horaFim A hora que especifica o 
	 * fim da atividade.
	 */
	public void setHoraFim(Hora horaFim) {
		this.horaFim = horaFim;
	}

	/**
	 * Obtém a duração total da atividade realizada.
	 * 
	 * @return Retorna a duração total. 
	 */
	public Hora getDuracao() {
		return duracao;
	}

	/**
	 * Insere a duração total da atividade realizada.
	 * 
	 * @param duracao A total de horas que especifica a 
	 * duração total da atividade.
	 */
	public void setDuracao(Hora duracao) {
		this.duracao = duracao;
	}

	/**
	 * Retorna uma {@link String} que representa o valor dos atributos do tempo
	 * mantida no objeto.
	 */
	@Override
	public String toString() {
		return String.format("Início: %s\nFim: %s\nDuração: %s", horaInicio, horaFim, duracao);
	}

}
