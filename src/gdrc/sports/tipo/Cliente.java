package gdrc.sports.tipo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gdrc.sports.tipo.time.Data;

/**
 * Esta classe mantém os principais dados relacionado a um cliente, que são os 
 * seus dados pessoais.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class Cliente implements  Comparable<Cliente>{
	private String nome, email, sexo;
	private float peso, altura;
	private Data dataNascimento;
	private List<Exercicio> exercicios;
	
	/**
	 * Construtor default para instanciar o objeto desta classe. 
	 */
	public Cliente() {
		this.nome = this.email = this.sexo;
		this.dataNascimento = new Data();
		this.exercicios = new ArrayList<>();
	}

	/**
	 * Construtor sobrecarregado que permite instanciar o objeto 
	 * desta classe com todos os dados do cliente.
	 *
	 *  @param nome Nome do cliente
	 *  @param email Email do cliente
	 *  @param sexo Sexo do cliente
	 *  @param peso Peso do cliente
	 *  @param altura Altura do cliente
	 *  @param dataNascimento Data de nascimento do cliente
	 */
	public Cliente(String nome, String email, String sexo, 
				   float peso, float altura, Data dataNascimento) {
		this.nome = nome;
		this.email = email;
		this.sexo = sexo;
		this.peso = peso;
		this.altura = altura;
		this.dataNascimento = dataNascimento;
		this.exercicios = new ArrayList<>();
	}
	

	/**
	 * Obtém o nome do cliente.
	 * 
	 * @return Retorna o nome.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Insere o nome do cliente.
	 * 
	 * @param nome O nome a ser inserido.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Obtém o email do cliente.
	 * 
	 * @return Retorna o email.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Insere o email do cliente.
	 * 
	 * @param email O email a ser inserido.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Obtém o sexo do cliente.
	 * 
	 * @return Retorna o sexo.
	 */
	public String getSexo() {
		return sexo;
	}

	/**
	 * Insere o sexo do cliente.
	 * 
	 * @param sexo O sexo a ser inserido.
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	/**
	 * Obtém o peso do cliente.
	 * 
	 * @return Retorna o peso.
	 */
	public float getPeso() {
		return peso;
	}

	/**
	 * Insere o peso do cliente.
	 * 
	 * @param peso O peso a ser inserido.
	 */
	public void setPeso(float peso) {
		this.peso = peso;
	}

	/**
	 * Obtém a altura do cliente.
	 * 
	 * @return Retorna a altura.
	 */
	public float getAltura() {
		return altura;
	}


	/**
	 * Insere a altura do cliente.
	 * 
	 * @param altura A altura a ser inserida.
	 */
	public void setAltura(float altura) {
		this.altura = altura;
	}

	/**
	 * Obtém a data de nascimento do cliente.
	 * 
	 * @return Retorna a data de nascimento.
	 */
	public Data getDataNascimento() {
		return dataNascimento;
	}

	/**
	 * Insere a data de nascimento do cliente.
	 * 
	 * @param dataNascimento A data de nascimento a ser inserida.
	 */
	public void setDataNascimento(Data dataNascimento) {
		this.dataNascimento = dataNascimento;
	}
	
	/**
	 * Obtém o conjunto de exercícios reference ao Cliente.
	 * 
	 * @return Retorna o conjunto de exercícios.
	 */
	public List<Exercicio> getExercicios() {
		return exercicios;
	}

	/**
	 * Insere o conjunto de exercícios reference ao Cliente.
	 * 
	 * @param exercicios Os exercícios que referente ao cliente. 
	 */
	public void setExercicios(List<Exercicio> exercicios) {
		this.exercicios = exercicios;
	}
	
	/**
	 * Obtém o nome de todos os exercícios contido no objeto
	 * {@link Cliente}. O nomes não serão repetidos para o caso 
	 * de ter mais de um exercício com o mesmo nome.
	 * 
	 * @return Retorna o conjunto com o nome dos exercícios.
	 */
	public Set<String> obterNomesExercicios() {
		Set<String> nomesExercicios = new HashSet<>();
		for(Exercicio exercicio : exercicios)
			nomesExercicios.add(exercicio.getExercicio());
				
		return nomesExercicios;
	}

	/**
	 * Retorna uma {@link String} que representa o valor de todos os atributos do 
	 * cliente mantido no objeto.
	 */
	@Override
	public String toString() {
		return String.format("Nome: %s\nEmail: %s\nSexo: %s"
						 + "\nPeso: %.2f\nAltura: %.2f\nData de Nascimento: %s", 
						 	  nome, email, sexo, peso, altura, dataNascimento);
	}
	
	@Override
	public int compareTo(Cliente cliente) {
		return nome.compareToIgnoreCase(cliente.getNome());
	}

}
