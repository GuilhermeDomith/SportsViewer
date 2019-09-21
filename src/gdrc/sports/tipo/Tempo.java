package gdrc.sports.tipo;

import java.time.Duration;
import java.time.LocalTime;

import gdrc.sports.tipo.time.Hora;

/**
 * Mant�m os atributos relacionados ao tempo realizado em uma 
 * determinada atividade.<br><br>
 * 
 * O dados que podem ser guardados s�o os dados que especificam a hora do 
 * in�cio, hora do fim e dura��o total da atividade. A dura��o seria o 
 * mesmo que a diferen�a entre hora fim e hora inicio, mas pode ser mais
 * detalhada, contendo inclusive os segundos corridos na atividade.<br><br>
 *  
 * Portanto, a dura��o ser� calculada automaticamente somente se n�o for 
 * fornecida pelo usu�rio da classe.
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
	 * desta classe com hora de inicio e hora do fim e dura��o 
	 * da atividade.
	 *  
	 * @param horaInicio   A hora que define o in�cio da atividade.
	 * @param horaFim   A hora que define o fim da atividade.
	 * @param duracao   dura��o total da atividade.
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
	 * Como n�o ser� atribuido a dura��o, ela ser� calculada automaticamente 
	 * utilizando a diferen�a entre hora fim e hora inicio da atividade. 
	 * Mas a dura��o poder� ser incluida posteriormente a partir dos m�todos 
	 * de acesso se necess�rio.  
	 *  
	 * @param horaInicio  A hora que define o in�cio da atividade.
	 * @param horaFim  A hora que define o fim da atividade.
	 */
	public Tempo(Hora horaInicio, Hora horaFim) {
		this.horaInicio = horaInicio;
		this.horaFim = horaFim;
		this.duracao = calcularDuracaoTotal();
	}

	/**
	 * Permite calcular a dura��o total de uma atividade a partir dos
	 * dados que especificam a hora do in�cio e hora do fim da atividade.
	 * <br><br>
	 * Aten��o! Caso j� tenha atribuido uma dura��o mais espec�fica que as horas
	 * de inicio e fim da atividade, n�o � recomendado calcular a dura��o 
	 * novamente para n�o perder a que foi atribu�da.
	 * 
	 * @return Retorna a dura��o total da atividade calculada a partir da 
	 * diferen�a entre a hora do fim e hora do in�cio da atividade. 
	 */
	public Hora calcularDuracaoTotal() {
		this.duracao = new Hora();
		Duration duration = Duration.between(horaInicio.getHora(), horaFim.getHora());
		this.duracao.setHora(LocalTime.ofNanoOfDay(duration.toNanos())); 
		return this.duracao;
	}

	/**
	 * Obt�m a hora de in�cio da atividade realizada.
	 * 
	 * @return Retorna a hora do in�cio. 
	 */
	public Hora getHoraInicio() {
		return horaInicio;
	}

	/**
	 * Insere a hora do in�cio da atividade realizada.
	 * 
	 * @param horaInicio A hora que especifica o 
	 * in�cio da atividade.
	 */
	public void setHoraInicio(Hora horaInicio) {
		this.horaInicio = horaInicio;
	}

	/**
	 * Obt�m a hora do fim da atividade realizada.
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
	 * Obt�m a dura��o total da atividade realizada.
	 * 
	 * @return Retorna a dura��o total. 
	 */
	public Hora getDuracao() {
		return duracao;
	}

	/**
	 * Insere a dura��o total da atividade realizada.
	 * 
	 * @param duracao A total de horas que especifica a 
	 * dura��o total da atividade.
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
		return String.format("In�cio: %s\nFim: %s\nDura��o: %s", horaInicio, horaFim, duracao);
	}

}
