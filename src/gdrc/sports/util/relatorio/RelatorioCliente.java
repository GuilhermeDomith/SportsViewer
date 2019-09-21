package gdrc.sports.util.relatorio;

import java.util.Collections;
import java.util.List;

import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.Exercicio.ComparatorCaloriasExercicio;
import gdrc.sports.tipo.Exercicio.ComparatorDistanciaExercicio;
import gdrc.sports.tipo.Exercicio.ComparatorDuracaoExercicio;
import gdrc.sports.tipo.Exercicio.ComparatorPassosExercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.ExercicioDetalhado.ComparatorVelocidadeExercicio;

/**
 * Permite obter um relat�rio para o cliente selecionando 
 * um {@link Exercicio}, dentre v�rios do cliente, que possui 
 * o maior valor.<br>
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RelatorioCliente {
	private List<Exercicio> exercicios;
	private Exercicio exercicioMaiorNumPassos;
	private Exercicio exercicioMaiorDuracao;
	private Exercicio exercicioMaiorDistancia;
	private Exercicio exercicioMaiorPerdaCalorias;
	private ExercicioDetalhado exercicioMaiorVelocidade;

	/**
	 * Construtor que instancia o relat�rio com os exerc�cios
	 * a serem usados para selecionar os que possuem maior 
	 * valores.
	 * 
	 * @param exercicios 	Os exercicios a serem comparados.
	 */
	public RelatorioCliente(List<Exercicio> exercicios) {
		this.exercicios = exercicios;
	}

	/**
	 * Obt�m o exerc�cios dentre os fornecidos que 
	 * possui o maior n�mero de passos.
	 *  
	 * @return Retorna o objeto {@link Exercicio} encontrado. 
	 * Se a lista de exerc�cios � vazia, retorna <code>null</code>.
	 */
	public Exercicio getExercicioMaiorNumPassos() {
		if(exercicioMaiorNumPassos == null && exercicios.size() > 0)
			exercicioMaiorNumPassos = Collections.max(exercicios, 
									  new ComparatorPassosExercicio());
		return exercicioMaiorNumPassos;
	}

	/**
	 * Obt�m o exerc�cios dentre os fornecidos que 
	 * possui a maior dura��o.
	 *  
	 * @return Retorna o objeto {@link Exercicio} encontrado. 
	 * Se a lista de exerc�cios � vazia, retorna <code>null</code>.
	 */
	public Exercicio getExercicioMaiorDuracao() {
		if(exercicioMaiorDuracao == null && exercicios.size() > 0)
			exercicioMaiorDuracao = Collections.max(exercicios, 
									new ComparatorDuracaoExercicio());
		return exercicioMaiorDuracao;
	}

	/**
	 * Obt�m o exerc�cios dentre os fornecidos que 
	 * possui a maior dist�ncia percorrida.
	 *  
	 * @return Retorna o objeto {@link Exercicio} encontrado. 
	 *Se a lista de exerc�cios � vazia, retorna <code>null</code>.
	 */
	public Exercicio getExercicioMaiorDistancia() {
		if(exercicioMaiorDistancia == null && exercicios.size() > 0)
			exercicioMaiorDistancia = Collections.max(exercicios, 
									  new ComparatorDistanciaExercicio());
		return exercicioMaiorDistancia;
	}

	/**
	 * Obt�m o exerc�cios dentre os fornecidos que 
	 * possui a maior perda de calorias.
	 *  
	 * @return Retorna o objeto {@link Exercicio} encontrado. 
	 * Se a lista de exerc�cios � vazia, retorna <code>null</code>.
	 */
	public Exercicio getExercicioMaiorPerdaCalorias() {
		if(exercicioMaiorPerdaCalorias == null && exercicios.size() > 0)
			exercicioMaiorPerdaCalorias = Collections.max(exercicios, 
										  new ComparatorCaloriasExercicio());
		return exercicioMaiorPerdaCalorias;
	}

	/**
	 * Obt�m o exerc�cios dentre os fornecidos que 
	 * possui a maior velocidade atingida.
	 *  
	 * @return Retorna o objeto {@link ExercicioDetalhado} encontrado. 
	 * Se a lista de exerc�cios � vazia ou n�o h� exerc�cio com o a 
	 * velocidade, retorna <code>null</code>.
	 */
	public ExercicioDetalhado getExercicioMaiorVelocidade() {
		if(exercicioMaiorVelocidade == null && exercicios.size() > 0) {
			Exercicio exercicio = Collections.max(exercicios, 
								  new ComparatorVelocidadeExercicio());
			if(exercicio instanceof ExercicioDetalhado)
				exercicioMaiorVelocidade = (ExercicioDetalhado) exercicio;
		}
		return exercicioMaiorVelocidade;
	}
	
	/**
	 * Obt�m o texto a ser exibido com os dados do exerc�cio de acordo com a 
	 * constante {@link TipoDadoRelatorio} passada por par�metro. A <code>String</code>
	 * � formatada com os seguintes dados do exerc�cio: <br>
	 * 
	 * <br><center> [nome do exerc�cio] - [data] - [descri��o do dado]: [valor] [unidade, se houver]</center><br>
	 * 
	 * @param exercicio 		O exerc�cio com os dados a ser formatado na <code>String</code>. 
	 * @param dadoRelatorio		O tipo de dado que se pretente obter do exerc�cio.
	 * 
	 * @return Retorna string formatada com os dados as serem exibidos.
	 */
	public static String formatarDadosRelatorio(Exercicio exercicio, TipoDadoRelatorio dadoRelatorio) {
		if(exercicio == null) return "N�o h�";
		String texto = "%s - %s - %s: %s %s";
		String descricao = "", unidade = "";
		Object dado = null;
		
		switch (dadoRelatorio) {
		case CALORIAS: 
			descricao = "Calorias perdidas"; unidade = "Kcal";
			dado = exercicio.getCaloriasPerdidas(); break;
		case DISTANCIA: 
			descricao = "Dist�ncia"; unidade = "Km";
			dado = exercicio.getDistancia(); break;
		case DURACAO: 
			descricao = "Dura��o";
			dado = exercicio.getTempo().getDuracao(); break;
		case PASSOS: 
			descricao = "Passos";
			dado = exercicio.getPassos(); break;
		case VELOCIDADE:  
			if(exercicio instanceof ExercicioDetalhado) { 
				descricao = "Velocidade"; unidade = "Km/h";
				ExercicioDetalhado ex = (ExercicioDetalhado) exercicio;
				dado = ex.getVelocidade().getVelocidadeMaxima(); break;
			} else return "N�o h�";
		}
		
		return String.format(texto, exercicio.getExercicio(), 
							 exercicio.getData(), descricao, dado, unidade);
	}
	
	/**
	 * Indica qual o tipo de dado do relat�rio est� sendo gerado uma {@link String}. 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public enum TipoDadoRelatorio {VELOCIDADE, PASSOS, CALORIAS, DISTANCIA, DURACAO};
	
}
