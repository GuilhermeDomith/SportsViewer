package gdrc.sports.tipo;


/**
 * Cont�m o dados relacionados a velocidade marcada em uma 
 * determinada atividade.<br><br>
 * 
 * O dados mantidos s�o os dados que especificam a velocidade m�xima 
 * e a velocidade m�dia atingida na atividade. Devem ser especificadas as velocidade 
 * com a medida em <b>quil�metro(s) por hora (km/h)</b>
 *  
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class Velocidade{
	private long codigo;
	private float velocidadeMaxima,
				  		 velocidadeMedia;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Velocidade() { }

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com a velocidade m�xima e a velocidade m�dia 
	 * atingida na atividade.
	 *  
	 * @param velocidadeMaxima Velocidade m�xima atingida na atividade realizada. 
	 * @param velocidadeMedia Velocidade m�dia atingida na atividade realizada.
	 */
	public Velocidade(float velocidadeMaxima, float velocidadeMedia) {
		this.velocidadeMaxima = velocidadeMaxima;
		this.velocidadeMedia = velocidadeMedia;
	}

	/**
	 * Obt�m o c�digo �nico do objeto velocidade.
	 * 
	 * @return Retorna o c�digo �nico que identifica a velocidade.
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o c�digo �nico que identifica o objeto velocidade.
	 * 
	 * @param codigo O c�digo �nico que identifica a velocidade.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obt�m a velocidade m�xima atingida na atividade.
	 * 
	 * @return Retorna a velocidade m�xima. 
	 */
	public float getVelocidadeMaxima() {
		return velocidadeMaxima;
	}

	/**
	 * Insere a velocidade m�xima atingida na 
	 * atividade.
	 * 
	 * @param velocidadeMaxima A velocidade m�xima atingida.
	 */
	public void setVelocidadeMaxima(float velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}

	/**
	 * Obt�m a velocidade m�dia atingida na atividade.
	 * 
	 * @return Retorna a velocidade m�dia. 
	 */
	public float getVelocidadeMedia() {
		return velocidadeMedia;
	}

	/**
	 * Insere a velocidade m�dia atingida na 
	 * atividade.
	 * 
	 * @param velocidadeMedia A velocidade m�dia atingida.
	 */
	public void setVelocidadeMedia(float velocidadeMedia) {
		this.velocidadeMedia = velocidadeMedia;
	}

	/**
	 * Retorna uma {@link String} que representa o valor dos atributos da velocidade
	 * mantida no objeto.
	 */
	@Override
	public String toString() {
		return String.format("Velocidade M�xima: %.2f\nVelocidade M�dia: %.2f", velocidadeMaxima, velocidadeMedia);
	}

}
