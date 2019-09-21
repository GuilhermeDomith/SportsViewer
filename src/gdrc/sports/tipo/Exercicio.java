package gdrc.sports.tipo;

import java.util.Comparator;

import gdrc.sports.tipo.time.Data;

/**
 * Esta classe mant�m os informa��es obtidas de um cliente na realiza��o de uma 
 * determinada atividade f�sica. Os atributos desta classe foram escolhidos de 
 * forma que abrange qualquer modalidade de exercicio, ou que pelo menos, maior parte 
 * deles.<br><br>
 * 
 * Portanto, para que haja uma maior especifica��o, permitindo a adi��o de dados
 * relacionados a outros exerc�cios f�sicos, que o usu�rio da classe acredita n�o atender, 
 * � recomendado estende-la para que seja feita uma maior especifica��o.<br><br>
 * 
 * Para ver os relacionamentos atribu�dos � esta classe e outras classes do pacote tipo, � 
 * recomendado visualizar o diagrama de classes anexado � documenta��o do pacote 
 * {@link gdrc.sports.tipo}
 * 
 * @see gdrc.sports.tipo
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Exercicio implements Comparable<Exercicio>{
	private long codigo;
	private String exercicio;
	private Cliente cliente;
	private Data data;
	private Tempo tempo;
	private float distancia,
				  caloriasPerdidas;
	private int passos;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Exercicio() {
		exercicio = "";
		cliente = new Cliente();
		data = new Data();
		tempo = new Tempo();
	}

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com todas as informa��es do exerc�cio realizado 
	 * pelo cliente.
	 *  
	 * @param cliente  	O nome do cliente que realizou o exerc�cio.
	 * @param data  	A data em que o exerc�cio foi realizado. 
	 * @param tempo  	O tempo em que o exerc�cio foi realizado.
	 * @param distancia 	A distancia percorrida na realiza��o do exerc�cio.
	 * @param caloriasPerdidas		As calorias perdidas na realiza��o do exerc�cio.
	 * @param passos	Os passoas dados na realiza��o do exec�cio.
	 */
	public Exercicio(Cliente cliente, Data data, Tempo tempo, 
					float distancia, float caloriasPerdidas, int passos) {
		this.cliente = cliente;
		this.data = data;
		this.tempo = tempo;
		this.distancia = distancia;
		this.caloriasPerdidas = caloriasPerdidas;
		this.passos = passos;
	}
	
	/**
	 * Obt�m o c�digo �nico do objeto exercicio.
	 * 
	 * @return Retorna o c�digo �nico que identifica o exercicio.
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o c�digo �nico que identifica o objeto exercicio.
	 * 
	 * @param codigo O c�digo �nico que identifica o exercicio.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obt�m o nome do exerc�cio realizado pelo cliente.
	 * 
	 * @return Retorna o nome do exerc�cio.
	 */
	public String getExercicio() {
		return exercicio;
	}

	/**
	 * Insere o nome do exerc�cio realizado pelo cliente.
	 * 
	 * @param exercicio O nome do exerc�cio realizado.
	 */
	public void setExercicio(String exercicio) {
		this.exercicio = exercicio;
	}

	/**
	 * Obt�m o cliente que realizou o exerc�cio.
	 * 
	 * @return Retorna o cliente.
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Insere o cliente que realizou o exerc�cio. 
	 * 
	 * @param cliente	O cliente que realizou o exerc�cio.
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Obt�m a data que o cliente realizou o exerc�cio.
	 * 
	 * @return Retorna o a data do exerc�co.
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Insere a data em que o cliente realizou o exerc�cio. 
	 * 
	 * @param data	A data do exerc�cio.
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Obt�m o tempo registrado em que o cliente realizou o exerc�cio.
	 * 
	 * @return Retorna o tempo do exerc�co.
	 */
	public Tempo getTempo() {
		return tempo;
	}
	
	/**
	 * Insere o tempo que o cliente realizou o exerc�cio. 
	 * 
	 * @param tempo	O tempo que a atividade foi realizada.
	 */
	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}

	/**
	 * Obt�m a distancia que o cliente percorreu no exerc�cio.
	 * 
	 * @return Retorna o a distancia percorrida durante o exerc�co.
	 */
	public float getDistancia() {
		return distancia;
	}

	/**
	 * Insere a dist�ncia que o cliente percorreu durante
	 * o exerc�cio. 
	 * 
	 * @param distancia	A dist�ncia percorrida.
	 */
	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}

	/**
	 * Obt�m as calorias que o cliente queimou durante o exerc�cio.
	 * 
	 * @return Retorna o total de calorias perdidas no exerc�cio.
	 */
	public float getCaloriasPerdidas() {
		return caloriasPerdidas;
	}

	/**
	 * Insere o total de calorias que o cliente queimou durante
	 * o exerc�cio. 
	 * 
	 * @param caloriasPerdidas	O n�mero de calorias perdidas.
	 */
	public void setCaloriasPerdidas(float caloriasPerdidas) {
		this.caloriasPerdidas = caloriasPerdidas;
	}

	/**
	 * Obt�m n�mero de passos que o cliente realizou no exerc�cio.
	 * 
	 * @return Retorna o n�mero de passos.
	 */
	public int getPassos() {
		return passos;
	}

	/**
	 * Insere o n�mero de passos que o cliente realizou no exerc�cio. 
	 * 
	 * @param passos	O n�mero de passos realizados.
	 */
	public void setPassos(int passos) {
		this.passos = passos;
	}
	
	/**
	 * Retorna uma {@link String} que representa o valor das informa��es do cliente
	 * no exerc�cio realizado.
	 */
	@Override
	public String toString() {
		return String.format("\n\nExercicio: %s\n--Cliente--\n%s\n----------\n"
				+ "Data: %s\nTempo:\n%s\nDistancia:\n%s\nCalorias Perdidas: %s\npassos: %s",
				exercicio, cliente, data, tempo, distancia, caloriasPerdidas, passos);
	}

	/**
	 * Crit�rio de compara��o por data e hora de in�cio 
	 * de um exerc�cio. <br><br>
	 * 
	 * Caso seja utilizado para ordena��o ela ser� feita de forma 
	 * ascendente, de forma que, se data do objeto atual for igual � data 
	 * do exerc�cio passado por par�metro, a compara��o ser� feita partir da 
	 * hora de in�cio do exerc�cio.
	 */
	@Override
	public int compareTo(Exercicio exercicio) {
		if(!data.getData().equals(exercicio.getData().getData()))
			return data.getData().compareTo(exercicio.getData().getData());
		else
			return tempo.getHoraInicio().compareTo(exercicio.getTempo().getHoraInicio());
		
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como crit�rio de 
	 * ordena��o em ordem ascendente de um conjunto de exerc�cios por 
	 * numero de passos dados.
	 */
	public static class ComparatorPassosExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return Integer.compare(ex1.getPassos(), ex2.getPassos());
		}
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como crit�rio de 
	 * ordena��o em ordem ascendente de um conjunto de exerc�cios por 
	 * caloria perdida.
	 */
	public static class ComparatorCaloriasExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return Float.compare(ex1.getCaloriasPerdidas(), ex2.getCaloriasPerdidas());
		}
	}
	
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como crit�rio de 
	 * ordena��o em ordem ascendente de um conjunto de exerc�cios por 
	 * dist�ncia percorrida.
	 */
	public static class ComparatorDistanciaExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return Float.compare(ex1.getDistancia(), ex2.getDistancia());
		}
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como crit�rio de 
	 * ordena��o em ordem ascendente de um conjunto de exerc�cios 
	 * por dura��o.
	 */
	public static class ComparatorDuracaoExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return ex1.getTempo().getDuracao().compareTo(ex2.getTempo().getDuracao());
		}
	}
	
}
