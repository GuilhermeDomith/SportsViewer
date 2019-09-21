package gdrc.sports.tipo;


/**
 * Contém o dados relacionados a elevação atingida em uma 
 * determinada atividade.<br><br>
 * 
 * O dados mantidos são os dados que especificam a maior e menor elevação
 * atingida na atividade. Devem ser especificadas a elevação com a medida 
 * em <b>metros (m)</b>
 *  
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Elevacao{
	private long codigo;
	private int maiorElevacao,
				menorElevacao;

	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Elevacao() { }

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com a maior e menor elevação atingida na atividade.
	 *  
	 * @param maiorElevacao Maior elevação atingida na atividade realizada. 
	 * @param menorElevacao Menor elevação atingida na atividade realizada.
	 */
	public Elevacao(int maiorElevacao, int menorElevacao) {
		this.maiorElevacao = maiorElevacao;
		this.menorElevacao = menorElevacao;
	}

	/**
	 * Obtém o código único o objeto elevação.
	 * 
	 * @return Retorna o código único que identifica a elevação.
	 */	
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o código único que identifica o objeto elevação.
	 * 
	 * @param codigo O código único que identifica a elevação.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obtém a maior elevação atingida na atividade.
	 * 
	 * @return Retorna a maior elevação. 
	 */
	public int getMaiorElevacao() {
		return maiorElevacao;
	}

	/**
	 * Insere a maior elevação atingida na atividade.
	 * 
	 * @param maiorElevacao A maior elevação atingida.
	 */
	public void setMaiorElevacao(int maiorElevacao) {
		this.maiorElevacao = maiorElevacao;
	}

	/**
	 * Obtém a menor elevação atingida na atividade.
	 * 
	 * @return Retorna a menor elevação. 
	 */
	public int getMenorElevacao() {
		return menorElevacao;
	}

	/**
	 * Insere a menor elevação atingida na atividade.
	 * 
	 * @param menorElevacao A menor elevação atingida.
	 */
	public void setMenorElevacao(int menorElevacao) {
		this.menorElevacao = menorElevacao;
	}
	
	/**
	 * Retorna uma {@link String} que representa o valor dos atributos da elevação
	 * mantida no objeto.
	 */
	@Override
	public String toString() {
		return String.format("Maior Elevacao: %d\nMenor Elevacao: %d", maiorElevacao, menorElevacao);
	}

}
