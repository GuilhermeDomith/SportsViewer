package gdrc.sports.tipo;

import java.util.Comparator;

import gdrc.sports.tipo.time.Data;

/**
 * Esta classe mantém os informações obtidas de um cliente na realização de uma 
 * determinada atividade física. Os atributos desta classe foram escolhidos de 
 * forma que abrange qualquer modalidade de exercicio, ou que pelo menos, maior parte 
 * deles.<br><br>
 * 
 * Portanto, para que haja uma maior especificação, permitindo a adição de dados
 * relacionados a outros exercícios físicos, que o usuário da classe acredita não atender, 
 * é recomendado estende-la para que seja feita uma maior especificação.<br><br>
 * 
 * Para ver os relacionamentos atribuídos à esta classe e outras classes do pacote tipo, é 
 * recomendado visualizar o diagrama de classes anexado à documentação do pacote 
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
	 * desta classe com todas as informações do exercício realizado 
	 * pelo cliente.
	 *  
	 * @param cliente  	O nome do cliente que realizou o exercício.
	 * @param data  	A data em que o exercício foi realizado. 
	 * @param tempo  	O tempo em que o exercício foi realizado.
	 * @param distancia 	A distancia percorrida na realização do exercício.
	 * @param caloriasPerdidas		As calorias perdidas na realização do exercício.
	 * @param passos	Os passoas dados na realização do execício.
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
	 * Obtém o código único do objeto exercicio.
	 * 
	 * @return Retorna o código único que identifica o exercicio.
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o código único que identifica o objeto exercicio.
	 * 
	 * @param codigo O código único que identifica o exercicio.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obtém o nome do exercício realizado pelo cliente.
	 * 
	 * @return Retorna o nome do exercício.
	 */
	public String getExercicio() {
		return exercicio;
	}

	/**
	 * Insere o nome do exercício realizado pelo cliente.
	 * 
	 * @param exercicio O nome do exercício realizado.
	 */
	public void setExercicio(String exercicio) {
		this.exercicio = exercicio;
	}

	/**
	 * Obtém o cliente que realizou o exercício.
	 * 
	 * @return Retorna o cliente.
	 */
	public Cliente getCliente() {
		return cliente;
	}

	/**
	 * Insere o cliente que realizou o exercício. 
	 * 
	 * @param cliente	O cliente que realizou o exercício.
	 */
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	/**
	 * Obtém a data que o cliente realizou o exercício.
	 * 
	 * @return Retorna o a data do exercíco.
	 */
	public Data getData() {
		return data;
	}

	/**
	 * Insere a data em que o cliente realizou o exercício. 
	 * 
	 * @param data	A data do exercício.
	 */
	public void setData(Data data) {
		this.data = data;
	}

	/**
	 * Obtém o tempo registrado em que o cliente realizou o exercício.
	 * 
	 * @return Retorna o tempo do exercíco.
	 */
	public Tempo getTempo() {
		return tempo;
	}
	
	/**
	 * Insere o tempo que o cliente realizou o exercício. 
	 * 
	 * @param tempo	O tempo que a atividade foi realizada.
	 */
	public void setTempo(Tempo tempo) {
		this.tempo = tempo;
	}

	/**
	 * Obtém a distancia que o cliente percorreu no exercício.
	 * 
	 * @return Retorna o a distancia percorrida durante o exercíco.
	 */
	public float getDistancia() {
		return distancia;
	}

	/**
	 * Insere a distância que o cliente percorreu durante
	 * o exercício. 
	 * 
	 * @param distancia	A distância percorrida.
	 */
	public void setDistancia(float distancia) {
		this.distancia = distancia;
	}

	/**
	 * Obtém as calorias que o cliente queimou durante o exercício.
	 * 
	 * @return Retorna o total de calorias perdidas no exercício.
	 */
	public float getCaloriasPerdidas() {
		return caloriasPerdidas;
	}

	/**
	 * Insere o total de calorias que o cliente queimou durante
	 * o exercício. 
	 * 
	 * @param caloriasPerdidas	O número de calorias perdidas.
	 */
	public void setCaloriasPerdidas(float caloriasPerdidas) {
		this.caloriasPerdidas = caloriasPerdidas;
	}

	/**
	 * Obtém número de passos que o cliente realizou no exercício.
	 * 
	 * @return Retorna o número de passos.
	 */
	public int getPassos() {
		return passos;
	}

	/**
	 * Insere o número de passos que o cliente realizou no exercício. 
	 * 
	 * @param passos	O número de passos realizados.
	 */
	public void setPassos(int passos) {
		this.passos = passos;
	}
	
	/**
	 * Retorna uma {@link String} que representa o valor das informações do cliente
	 * no exercício realizado.
	 */
	@Override
	public String toString() {
		return String.format("\n\nExercicio: %s\n--Cliente--\n%s\n----------\n"
				+ "Data: %s\nTempo:\n%s\nDistancia:\n%s\nCalorias Perdidas: %s\npassos: %s",
				exercicio, cliente, data, tempo, distancia, caloriasPerdidas, passos);
	}

	/**
	 * Critério de comparação por data e hora de início 
	 * de um exercício. <br><br>
	 * 
	 * Caso seja utilizado para ordenação ela será feita de forma 
	 * ascendente, de forma que, se data do objeto atual for igual à data 
	 * do exercício passado por parâmetro, a comparação será feita partir da 
	 * hora de início do exercício.
	 */
	@Override
	public int compareTo(Exercicio exercicio) {
		if(!data.getData().equals(exercicio.getData().getData()))
			return data.getData().compareTo(exercicio.getData().getData());
		else
			return tempo.getHoraInicio().compareTo(exercicio.getTempo().getHoraInicio());
		
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como critério de 
	 * ordenação em ordem ascendente de um conjunto de exercícios por 
	 * numero de passos dados.
	 */
	public static class ComparatorPassosExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return Integer.compare(ex1.getPassos(), ex2.getPassos());
		}
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como critério de 
	 * ordenação em ordem ascendente de um conjunto de exercícios por 
	 * caloria perdida.
	 */
	public static class ComparatorCaloriasExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return Float.compare(ex1.getCaloriasPerdidas(), ex2.getCaloriasPerdidas());
		}
	}
	
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como critério de 
	 * ordenação em ordem ascendente de um conjunto de exercícios por 
	 * distância percorrida.
	 */
	public static class ComparatorDistanciaExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return Float.compare(ex1.getDistancia(), ex2.getDistancia());
		}
	}
	
	/**
	 * Um {@link Comparator} que pode ser utilizado como critério de 
	 * ordenação em ordem ascendente de um conjunto de exercícios 
	 * por duração.
	 */
	public static class ComparatorDuracaoExercicio implements Comparator<Exercicio>{
		
		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return ex1.getTempo().getDuracao().compareTo(ex2.getTempo().getDuracao());
		}
	}
	
}
