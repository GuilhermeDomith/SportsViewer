package gdrc.sports.tipo;


/**
 * Contém o dados relacionados a velocidade marcada em uma 
 * determinada atividade.<br><br>
 * 
 * O dados mantidos são os dados que especificam a velocidade máxima 
 * e a velocidade média atingida na atividade. Devem ser especificadas as velocidade 
 * com a medida em <b>quilômetro(s) por hora (km/h)</b>
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
	 * desta classe com a velocidade máxima e a velocidade média 
	 * atingida na atividade.
	 *  
	 * @param velocidadeMaxima Velocidade máxima atingida na atividade realizada. 
	 * @param velocidadeMedia Velocidade média atingida na atividade realizada.
	 */
	public Velocidade(float velocidadeMaxima, float velocidadeMedia) {
		this.velocidadeMaxima = velocidadeMaxima;
		this.velocidadeMedia = velocidadeMedia;
	}

	/**
	 * Obtém o código único do objeto velocidade.
	 * 
	 * @return Retorna o código único que identifica a velocidade.
	 */
	public long getCodigo() {
		return codigo;
	}

	/**
	 * Insere o código único que identifica o objeto velocidade.
	 * 
	 * @param codigo O código único que identifica a velocidade.
	 */
	public void setCodigo(long codigo) {
		this.codigo = codigo;
	}

	/**
	 * Obtém a velocidade máxima atingida na atividade.
	 * 
	 * @return Retorna a velocidade máxima. 
	 */
	public float getVelocidadeMaxima() {
		return velocidadeMaxima;
	}

	/**
	 * Insere a velocidade máxima atingida na 
	 * atividade.
	 * 
	 * @param velocidadeMaxima A velocidade máxima atingida.
	 */
	public void setVelocidadeMaxima(float velocidadeMaxima) {
		this.velocidadeMaxima = velocidadeMaxima;
	}

	/**
	 * Obtém a velocidade média atingida na atividade.
	 * 
	 * @return Retorna a velocidade média. 
	 */
	public float getVelocidadeMedia() {
		return velocidadeMedia;
	}

	/**
	 * Insere a velocidade média atingida na 
	 * atividade.
	 * 
	 * @param velocidadeMedia A velocidade média atingida.
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
		return String.format("Velocidade Máxima: %.2f\nVelocidade Média: %.2f", velocidadeMaxima, velocidadeMedia);
	}

}
