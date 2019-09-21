package gdrc.sports.tipo.time;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Fornece os métodos para manipular e armazenar um objeto 
 * do tipo {@link LocalTime}. Esta classe permite converter a hora do 
 * tipo {@link LocalTime} para uma {@link String} e vice-versa, 
 * através dos métodos estáticos fornecidos. <br><br>
 * 
 * Portanto, para a conversão, não é necessário instanciar um 
 * objeto desta classe. Caso seja necessário manter o valor, deve-se
 * instanciar um objeto da classe e utilizar os métodos de acesso para obter
 * a hora do tipo desejado, {@link LocalTime} ou {@link String}.
 * 
 * @see LocalTime
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Hora implements Comparable<Hora>{
	private LocalTime hora;
	private String horaString;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Hora() {
		this(LocalTime.now());
	}

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com o objeto hora do tipo {@link LocalTime}.
	 * 
	 * @param hora A hora que será mantida e manipulada por 
	 * esta classe.
	 */
	public Hora(LocalTime hora) {
		setHora(hora);
	}
	

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com o objeto hora do tipo <code>long</code>.
	 * 
	 * @param hora A hora que será mantida e manipulada por 
	 * esta classe.
	 */
	public Hora(long hora) {
		setHora(LocalTime.ofNanoOfDay(hora));
	}

	/**
	 * Converte a hora em {@link String} para o um objeto do tipo 
	 * {@link Hora}. O formato da hora passada por parâmetro deve
	 * estar no formato:
	 * 
	 * <br><br><center>
	 * <b>hh:mm:ss</b> ou
	 * <b>hh°mm'ss''</b>
	 * </center><br><br>
	 *  
	 * @param horaString A hora que será convertida para um objeto 
	 * desta classe.
	 * 
	 * @return Retorna o objeto do tipo {@link Hora} ou retorna 
	 * <code>null</code> caso ocorra um erro na conversão. Caso isso
	 * ocorra, verifique o formato descrito acima.
	 * 
	 * @throws DateTimeParseException Dispara a exceção se o formato da hora passado 
	 * por parâmeto for incompatível com o formato exigido.
	 */
	public static Hora converterParaHora(String horaString) throws DateTimeParseException{
		/* Remove o último simbolo se houver (exceto numeros).
		 * Ex.: hh°mm'ss'' ->  hh°mm'ss
		 */
		horaString = horaString.replaceAll("[^0-9]$", "");
		/* Altera os caracteres que não seja número para o simbolo ':'. 
		 * Ex.: hh°mm'ss ->  hh:mm:ss
		 */
		horaString = horaString.replaceAll("[^0-9]", ":");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		
		return new Hora(LocalTime.parse(horaString, formatter));
	}
	
	/**
	 * Converte a hora do tipo {@link LocalTime} para o um objeto {@link String}.
	 * O formato da hora em {@link String} será:
	 * 
	 * <br><br><center><b>hh:mm:ss</b></center><br><br>
	 *  
	 * @param hora A hora que será convertida para um objeto do 
	 * tipo {@link String}.
	 * 
	 * @return Retorna o objeto do tipo {@link String} ou retorna 
	 * <code>null</code> caso ocorra um erro na conversão.
	 */
	public static String converterParaString(LocalTime hora) {
		if(hora == null) return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		try{
			return hora.format(formatter);
		}catch (DateTimeException e) { return null; }
	}
	
	/**
	 * Obtém a hora do tipo {@link LocalTime}.
	 * 
	 * @return Retorna o objeto hora {@link LocalTime}.
	 */
	public LocalTime getHora() {
		return hora;
	}

	/**
	 * Insere a hora do tipo {@link LocalTime}.
	 * 
	 * @param hora - A hora a ser inserida.
	 */
	public void setHora(LocalTime hora) {
		this.hora = hora;
		this.horaString = converterParaString(hora);
	}

	/**
	 * Obtém a hora do tipo {@link String}.
	 * 
	 * @return Retorna o objeto hora em {@link String}.
	 */
	
	public String getHoraString() {
		return horaString;
	}

	/**
	 * Retorna uma {@link String} que representa o valor da hora mantida no objeto.
	 */
	@Override
	public String toString() {
		return String.format("%s", horaString);
	}
	
	
	/**
	 * Compara a hora fornecida por parâmetro com com o objeto Hora atual.
	 * 
	 * @param hora - A hora a ser comparada com este objeto Hora.
	 * 
	 * @return Retorna um valor inteiro negativo se este objeto for menor, 0 se for igual ou
	 * um inteiro positivo se for maior que o objeto fornecido por parâmetro. 
	 */
	@Override
	public int compareTo(Hora hora) {
		return this.hora.compareTo(hora.getHora());
	}
	
}
