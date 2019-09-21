package gdrc.sports.tipo.time;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Fornece os métodos para manipular e armazenar um objeto 
 * do tipo {@link LocalDate}. Esta classe permite converter a data do 
 * tipo {@link LocalDate} para uma {@link String} e vice-versa, 
 * através dos métodos estáticos fornecidos.<br><br>
 * 
 * Portanto, para a conversão, não é necessário instanciar um 
 * objeto desta classe. Caso seja necessário manter o valor, deve-se
 * instanciar um objeto da classe e utilizar os métodos de acesso para obter
 * a data do tipo desejado, {@link LocalDate} ou {@link String}.
 * 
 * @see LocalDate
 *   
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Data implements Comparable<Data>{
	private LocalDate data;
	private String dataString;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Data() {
		this(LocalDate.now());
	}

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com a data do tipo {@link LocalDate}.
	 * 
	 * @param data A data que será mantida e manipulada por 
	 * esta classe.
	 */
	public Data(LocalDate data) {
		setData(data);
	}
	
	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com a data do tipo <code>long</code>.
	 * 
	 * @param data A data que será mantida e manipulada por 
	 * esta classe.
	 */
	public Data(long data) {
		setData(LocalDate.ofEpochDay(data));
	}

	/**
	 * Converte a data em {@link String} para o um objeto do tipo 
	 * {@link Data}. O formato da data passada por parâmetro deve
	 * estar no formato:
	 * 
	 * <br><br><center><b>DD/MM/AA</b>  ou  <b>DD/MM/AAAA</b></center><br><br>
	 *  
	 * @param dataString A data que será convertida para um objeto 
	 * do tipo {@link Data}
	 * 
	 * @return Retorna o objeto do tipo {@link Data}.
	 * 
	 * @throws DateTimeParseException Dispara a exceção se o formato da 
	 * data passado por parâmeto  for incompatível com o formato exigido.
	 */
	public static Data converterParaData(String dataString) throws DateTimeParseException{
		String pattern = (dataString.length() == 10)? "dd/MM/yyyy" : "dd/MM/yy";
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
		return new Data(LocalDate.parse(dataString, formatter));
	}
	
	/**
	 * Converte a data do tipo {@link LocalDate} para o um objeto {@link String}.
	 * O formato da data em {@link String} será:
	 * 
	 * <br><br><center><b>DD/MM/AA</b></center><br><br>
	 *  
	 * @param data A data que será convertida para um objeto do 
	 * tipo {@link String}.
	 * 
	 * @return Retorna o objeto do tipo {@link String} ou retorna 
	 * <code>null</code> caso ocorra um erro na conversão.
	 */
	public static String converterParaString(LocalDate data) {
		if(data == null) return null;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		try{
			return data.format(formatter);
		}catch (DateTimeException e) { return null; }
	}
	
	/**
	 * Obtém a data do tipo {@link LocalDate}.
	 * 
	 * @return Retorna o objeto data {@link LocalDate}.
	 */
	public LocalDate getData() {
		return data;
	}

	/**
	 * Insere a data do tipo {@link LocalDate}.
	 * 
	 * @param data - A data a ser inserida.
	 */
	public void setData(LocalDate data) {
		this.data = data;
		this.dataString = converterParaString(data);
	}

	/**
	 * Obtém a data do tipo {@link String}.
	 * 
	 * @return Retorna o objeto data em {@link String}.
	 */
	
	public String getDataString() {
		return dataString;
	}

	/**
	 * Retorna uma {@link String} que representa o valor da data mantida no objeto.
	 */
	@Override
	public String toString() {
		return  dataString;
	}

	/**
	 * Compara a data fornecida por parâmetro com com o objeto Data atual.
	 * 
	 * @param data A data a ser comparada com este objeto data.
	 * 
	 * @return Retorna um valor inteiro negativo se este objeto for menor, 0 se for igual ou
	 * um inteiro positivo se for maior que o objeto fornecido por parâmetro. 
	 */
	@Override
	public int compareTo(Data data) {
		return this.data.compareTo(data.getData());
	}
	
}
