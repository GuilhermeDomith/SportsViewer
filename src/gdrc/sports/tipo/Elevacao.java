package gdrc.sports.tipo;


/**
 * Cont�m o dados relacionados a eleva��o atingida em uma 
 * determinada atividade.<br><br>
 * 
 * O dados mantidos s�o os dados que especificam a maior e menor eleva��o
 * atingida na atividade. Devem ser especificadas a eleva��o com a medida 
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
	 * desta classe com a maior e menor eleva��o atingida na atividade.
	 *  
	 * @param maiorElevacao Maior eleva��o atingida na atividade realizada. 
	 * @param menorElevacao Menor eleva��o atingida na atividade realizada.
	 */
	public Elevacao(int maiorElevacao, int menorElevacao) {
		this.maiorElevacao = maiorElevacao;
		this.menorElevacao = menorElevacao;
	}

	/**
	 * Obt�m o c�digo �nico o objeto eleva��o.
	 * 
	 * @return Retorna o c�digo �nico que identifica a eleva��o.
	 */	
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o c�digo �nico que identifica o objeto eleva��o.
	 * 
	 * @param codigo O c�digo �nico que identifica a eleva��o.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obt�m a maior eleva��o atingida na atividade.
	 * 
	 * @return Retorna a maior eleva��o. 
	 */
	public int getMaiorElevacao() {
		return maiorElevacao;
	}

	/**
	 * Insere a maior eleva��o atingida na atividade.
	 * 
	 * @param maiorElevacao A maior eleva��o atingida.
	 */
	public void setMaiorElevacao(int maiorElevacao) {
		this.maiorElevacao = maiorElevacao;
	}

	/**
	 * Obt�m a menor eleva��o atingida na atividade.
	 * 
	 * @return Retorna a menor eleva��o. 
	 */
	public int getMenorElevacao() {
		return menorElevacao;
	}

	/**
	 * Insere a menor eleva��o atingida na atividade.
	 * 
	 * @param menorElevacao A menor eleva��o atingida.
	 */
	public void setMenorElevacao(int menorElevacao) {
		this.menorElevacao = menorElevacao;
	}
	
	/**
	 * Retorna uma {@link String} que representa o valor dos atributos da eleva��o
	 * mantida no objeto.
	 */
	@Override
	public String toString() {
		return String.format("Maior Elevacao: %d\nMenor Elevacao: %d", maiorElevacao, menorElevacao);
	}

}
