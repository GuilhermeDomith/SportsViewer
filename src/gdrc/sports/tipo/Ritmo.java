package gdrc.sports.tipo;

import gdrc.sports.tipo.time.Hora;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Esta classe mantém todos os dados relacionados ao ritmo atingido em uma 
 * determinada atividade.<br><br>
 * 
 * Podem ser guardados os instantes ritmo máximo e ritmo médio da atividade e também
 * o ritmo detalhado que é o ritmo para cada quilômetro percorrido na atividade. O
 * ritmo máximo e ritmo médio serão obtidos a partir do ritmo detalhado caso eles não
 * sejam fornecidos.
 * 
 * 
 * @see Hora
 * @see RitmoDetalhado
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Ritmo{
	private long codigo;
	private List<RitmoDetalhado> divisoes;
	private Hora ritmoMaximo,
				 ritmoMedio;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Ritmo() {
		this(new Hora(), new Hora());
		divisoes = new ArrayList<>();
	}
	
	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com o ritmo máximo e ritmo médio da atividade.
	 *  
	 * @param ritmoMaximo   O ritmo que representa o ritmo máximo na atividade.
	 * @param ritmoMedio   O ritmo que representa o ritmo médio na atividade.
	 */
	public Ritmo(Hora ritmoMaximo, Hora ritmoMedio) {
		this.ritmoMaximo = ritmoMaximo;
		this.ritmoMedio = ritmoMedio;
	}
	
	/**
	 * Obtém o ritmo médio calculado a partir dos ritmos detalhados 
	 * inseridos no objeto. Pode ser utilizado para o caso de o ritmo 
	 * máximo não ser fornecido pelo usuário.
	 * 
	 * @return Retorna o ritmo médio a partir dos ritmos inseridos. Retorna 
	 * <code>null</code> se nenhum ritmo tiver sido inserido.
	 */
	public Hora calcularRitmoMedio() {
		if(divisoes.size() == 0) return null;
		
		long soma = 0;
		for(RitmoDetalhado r : divisoes)
			soma += r.getRitmo().getHora().toNanoOfDay();
		
		long media = soma / divisoes.size();
		return new Hora(LocalTime.ofNanoOfDay(media));
	}
	
	/**
	 * Obtém o ritmo máximo a partir dos ritmos detalhados inseridos no
	 * objeto. Pode ser utilizado para o caso de o ritmo médio não
	 * ser fornecido pelo usuário.
	 *   
	 * @return Retorna o ritmo máximo dos ritmos detalhados inseridos. Retorna 
	 * <code>null</code> se nenhum ritmo tiver sido inserido.
	 */
	public Hora calcularRitmoMaximo() {
		if(divisoes.size() == 0) return null;
		Collections.sort(divisoes);
		return divisoes.get(0).getRitmo();
	}
	
	/**
	 * Obtém o código único o objeto ritmo.
	 * 
	 * @return Retorna o código único que identifica o objeto ritmo.
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o código único que identifica o objeto ritmo.
	 * 
	 * @param codigo O código único que identifica o objeto ritmo.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Insere todos os ritmos detalhados para cada quilômetro 
	 * da atividade realizada.
	 * 
	 * @return Retorna um conjunto com todas as divisoes de cada ritmo
	 * para cada quilômetro da atividade.
	 */
	public List<RitmoDetalhado> getDivisoes() {
		return divisoes;
	}

	/**
	 * Insere todos os ritmos detalhados para cada quilômetro 
	 * da atividade realizada.
	 * 
	 * @param divisoes Conjunto com todas as divisoes de cada ritmo 
	 * para cada quilômetro da atividade.
	 */
	public void setDivisoes(List<RitmoDetalhado> divisoes) {
		this.divisoes = divisoes;
	}

	/**
	 * Obtém ritmo máximo da atividade realizada.
	 * 
	 * @return Retorna o ritmo máximo. 
	 */
	public Hora getRitmoMaximo() {
		return ritmoMaximo;
	}

	/**
	 * Insere a ritmo máximo da atividade realizada.
	 * 
	 * @param ritmoMaximo O instante que especifica o ritmo máximo 
	 * da atividade.
	 */
	public void setRitmoMaximo(Hora ritmoMaximo) {
		this.ritmoMaximo = ritmoMaximo;
	}

	/**
	 * Obtém ritmo médio da atividade realizada.
	 * 
	 * @return Retorna o ritmo médio. 
	 */
	public Hora getRitmoMedio() {
		return ritmoMedio;
	}

	/**
	 * Insere a ritmo médio da atividade realizada.
	 * 
	 * @param ritmoMedio O instante que especifica o ritmo médio 
	 * da atividade.
	 */
	public void setRitmoMedio(Hora ritmoMedio) {
		this.ritmoMedio = ritmoMedio;
	}
	
	/**
	 * Retorna uma {@link String} que representa o valor dos 
	 * atributos do ritmo em uma atividade mantida neste objeto, 
	 * com os ritmos detalhados de cada quilômetro e o ritmo máximo 
	 * e médio.
	 */
	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(String.format("Ritmo Máximo: %s\n", ritmoMaximo));
		stringBuilder.append(String.format("Ritmo Médio: %s\n", ritmoMedio));
		stringBuilder.append("Divisões:");
		for(RitmoDetalhado ritmo : divisoes)
			stringBuilder.append("\n\t").append(ritmo);
		
		return stringBuilder.toString();
	}

	/**
	 * Mantém os atributos relacionados ao ritmo para cada quilômetro realizado 
	 * em uma determinada atividade.<br><br>
	 * 
	 * Os dados mantidos associam o quilômetro em um instante na atividade e
	 * o ritmo no instante que atinge o quilômetro. 
	 *  
	 * @see Hora
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	public static class RitmoDetalhado implements Comparable<RitmoDetalhado>{
		private long codigo;
		private float quilometro;
		private Hora ritmo;
		
		/**
		 * Construtor default para instanciar o objeto desta classe. 
		 */
		public RitmoDetalhado() {
			ritmo = new Hora();
		}

		/**
		 * Construtor sobrecarregado que permite instanciar o objeto 
		 * desta classe com o quilometro e o ritmo atingido neste mesmo
		 * quilometro.
		 *  
		 * @param quilometro  O quilômetro em um determinado instante na atividade.
		 * @param ritmo   O ritmo atingido no quilômetro que está associado.
		 */
		public RitmoDetalhado(float quilometro, Hora ritmo) {
			this.quilometro = quilometro;
			this.ritmo = ritmo;
		}

		/**
		 * Obtém o código único o objeto ritmo detalhado.
		 * 
		 * @return Retorna o código único que identifica o ritmo detalhado.
		 */
		public long getCodigo() {
			return codigo;
		}

		/**
		 * Insere o código único que identifica o objeto ritmo detalhado.
		 * 
		 * @param codigo O código único que identifica o ritmo detalhado.
		 */
		public void setCodigo(long codigo) {
			this.codigo = codigo;
		}

		/**
		 * Obtém o quilometro atingido na atividade.
		 * 
		 * @return Retorna o quilometro.
		 */
		public float getQuilometro() {
			return quilometro;
		}

		/**
		 * Insere o quilometro atingido em um determinado 
		 * instante da atividade.
		 * 
		 * @param quilometro O quilometro atingido na atividade.
		 */
		public void setQuilometro(float quilometro) {
			this.quilometro = quilometro;
		}

		/**
		 * Obtém o ritmo atingido na atividade no quilometro
		 * associado neste objeto.
		 * 
		 * @return Retorna o ritmo.
		 */
		public Hora getRitmo() {
			return ritmo;
		}

		/**
		 * Insere o ritmo atingido no quilometro associado neste objeto.
		 * 
		 * @param ritmo O ritmo associado ao quilometro na atividade.
		 */
		public void setRitmo(Hora ritmo) {
			this.ritmo = ritmo;
		}

		/**
		 * Retorna uma {@link String} que representa o valor dos 
		 * atributos do ritmo detalhado mantida neste objeto, com quilômetro
		 * e ritmo.
		 */
		@Override
		public String toString() {
			return String.format("Km: %.2f - Ritmo: %s", quilometro, ritmo);
		}

		/**
		 * Compara a ritmo detalhado fornecido por parâmetro com com o objeto 
		 * ritmo detalhado atual. Na comparação é levada em consideração apenas o
		 * ritmo, desconsiderando o quilômetro.  
		 * 
		 * @param ritmoDetalhado O ritmo a ser comparadado com este objeto ritmo detalhado.
		 * 
		 * @return Retorna um valor inteiro negativo se o ritmo for menor, 0 se o ritmo 
		 * for igual ou um inteiro positivo se for maior que o objeto fornecido por parâmetro. 
		 */
		@Override
		public int compareTo(RitmoDetalhado ritmoDetalhado) {
			return this.ritmo.compareTo(ritmoDetalhado.ritmo);
		}
	}// class RitmoDetalhado.
	
	/**
	 * Define o comparator que deve ser utilizado para comparação 
	 * entre dois objetos {@link RitmoDetalhado}. Diferente do  {@link Comparable} 
	 * implementado pela classe {@link RitmoDetalhado}, este comparator permite 
	 * comparar os quilômetros do ritmo detalhado e não o ritmo.<br><br>
	 * 
	 * Portanto, pode ser utilizado para ordenar um conjunto de objetos {@link RitmoDetalhado}
	 * a partir do quilometro.
	 *  
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	public class RitmoDetalhadoComparator implements Comparator<RitmoDetalhado>{

		/**
		 * Compara os objetos {@link RitmoDetalhado} fornecidos por parâmetro.
		 * Na comparação é levada em consideração apenas o quilometro, 
		 * desconsiderando outros atributos do objeto.  
		 * 
		 * @param ritmoDetalhado O objeto {@link RitmoDetalhado} a ser comparado com o 
		 * segundo parametro.
		 * @param ritmoDetalhado2 O objeto {@link RitmoDetalhado} a ser comparado com o 
		 * primerio parametro.
		 * 
		 * @return Retorna um valor inteiro negativo se o primeiro for menor, 0 se  for igual 
		 * ou um inteiro positivo se for maior que o segundo objeto fornecido por parâmetro. 
		 */
		@Override
		public int compare(RitmoDetalhado ritmoDetalhado, RitmoDetalhado ritmoDetalhado2) {
			return 0;
		}
		
	}

	/**
	 * Converte o ritmo em uma <code>String</code> 
	 * já formatada no padrão mm'ss".
	 *  
	 * @param ritmo  O ritmo a ser convertido para <code>String</code>.
	 * @return Retorna a {@link String} com o ritmo formatado.
	 */
	public static String obterRitmoComoString(Hora ritmo) {
		return String.format("%tM'%1$tS\"", ritmo.getHora());
	}

}
