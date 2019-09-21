package gdrc.sports.util.relatorio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.grafico.GraficoCategory;
import gdrc.sports.tipo.grafico.GraficoCategory.ConteudoDataSet;
import gdrc.sports.tipo.grafico.GraficoColuna;
import gdrc.sports.tipo.grafico.GraficoLinha;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.tipo.time.Hora;

/**
 * Classe que permite gerar gr�ficos a partir dos exerc�cios e no per�odo 
 * que s�o fornecidos no contrutor da classe. <br>
 * 
 * Os gr�ficos gerados utilizam diferentes dados dos exerc�cios e � poss�vel 
 * gerar diferentes gr�ficos modificando as consfigura��es atrav�s dos 
 * m�todos de acesso da classe. 
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RelatorioGrafico {
	/**Indica qual o tipo de gr�fico pode ser utilizado. 
	 * @author Guilherme Domith Ribeiro Coelho */
	public enum TipoGrafico {COLUNA, LINHA};
	
	private List<Exercicio> exercicios, exerciciosDoPeriodo;
	private TipoGrafico tipoGrafico;
	private boolean separarExercicios; /* Indica se os exercicios com a mesma data 
											 ser�o adicionados separadamente no gr�fico.*/
	private String filtrarExercicio; /* Indica se os gr�ficos a serem gerados ser�o apenas 
										para o exerc�cio filtrado (exerc�cio contido nesta vari�vel). */
	private Data dataInicial, dataFinal;

	/**
	 * Construtor que cria um novo relat�rio com os exerc�cios 
	 * necess�rios para a gera��o dos gr�fico no per�odo fornecido.<br>
	 * 
	 * O per�odo deve ser fornecido por meio da data inicial e data final,
	 * que indica que os exerc�cios a considerar, ao gerar os gr�ficos,
	 * ser�o aqueles que t�m a data entre este intervalo. 
	 *  
	 * @param exercicios 	Os exerc�cio que ser�o utilizados para popular os gr�ficos.
	 * @param dataInicial	A data inicial que determina o limite inferior dos exerc�cios 
	 * que ser�o considerados na gera��o dos gr�ficos.
	 * @param dataFinal		A data final que determina o limite superior dos exerc�cios 
	 * que ser�o considerados na gera��o dos gr�ficos.
	 */
	public RelatorioGrafico(List<Exercicio> exercicios, Data dataInicial, Data dataFinal) {
		this.exercicios = new ArrayList<>(exercicios);
		this.dataInicial = dataInicial;
		this.dataFinal = dataFinal;
		
		this.exerciciosDoPeriodo = obterExerciciosDoPeriodo();
		
		this.tipoGrafico = TipoGrafico.COLUNA;
		this.separarExercicios = false;
		this.filtrarExercicio = null;
	}
	
	
	/**
	 * Obt�m os dados data e exerc�cio, coluna e linha respectivamente, que s�o dados 
	 * em comum de todos os exerc�cios que ser�o adicionados no gr�fico.
	 * Caso o exerc�cio n�o for o mesmo exerc�cio que est� na variavel 
	 * exercicioFiltrar retorna <code>null</code>. 
	 * 
	 * @param exercicio 	O exerc�cio a ser adicionado no gr�fico.
	 * @param string		A string que representa a linha que est� sendo adicionada. 
	 * Se o relat�rio estiver configurado para separar os exerc�cio ela n�o ser� 
	 * utilizada, a linha ser� o pr�prio nome do exerc�cio.
	 * 
	 * @return Retorna o conteudo a ser adicionado no gr�fic com a linha e coluna, mas ainda
	 * faltando o valor. Caso o exerc�cio seja um exerc�cio configurado para n�o adicionar 
	 * retorna <code>null</code>.
	 */
	private ConteudoDataSet obterColunaELinhaDoExercicio(Exercicio exercicio, String descricaoLinha) {
		/* Verifica se o exercicio passado por par�metro � para adicionar no gr�fico,
		 * Se for null significa que pode adicionar qualquer exerc�cio. */
		if(filtrarExercicio != null)
			if(!exercicio.getExercicio().equalsIgnoreCase(filtrarExercicio))
				return null;
		
		ConteudoDataSet conteudo = new ConteudoDataSet();
		
		conteudo.setChaveLinha((separarExercicios)? exercicio.getExercicio() : descricaoLinha);
		conteudo.setChaveColuna(exercicio.getData().toString());
		return conteudo;
	}
	

	private String obterTituloGrafico(String descricaoDoDadoUtilizado) {
		return String.format("%s (%s)\nDe %s a %s", descricaoDoDadoUtilizado, 
				(filtrarExercicio == null)? "Todos os Exerc�cios": filtrarExercicio , 
			    dataInicial, dataFinal);
	}
	
	
	/**
	 * Obt�m o gr�fico com os dados referentes a dura��o dos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo da dura��o ser� feito com 
	 * os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * soma da dura��o de todos os exerc�cios. Se n�o, apenas do exerc�cio especificado. 
	 * Por exemplo, a dura��o apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a soma da dura��o dos exercicios de mesmo tipo e feitos no mesmo dia. 
	 * Permite ter mais de um exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo a a dura��o dos exerc�cios 
	 * por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoDuracaoPorDia(){
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);
							
		grafico.setTituloGrafico(obterTituloGrafico("Dura��o"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Dura��o (Min)");
		
		grafico.adicionarAoDataSetSoma(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Dura��o");
			if(conteudo == null) return null;
			
			Hora duracao = exercicio.getTempo().getDuracao();
			conteudo.setValor(duracao.getHora().toSecondOfDay()/60); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}
	
	/**
	 * Obt�m o gr�fico com os dados referentes a distancia percorrida nos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo da distancia percorrida ser� feito com 
	 * os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * soma da distancia percorrida em todos os exerc�cios. Se n�o, apenas do exerc�cio 
	 * especificado. Por exemplo, a distancia percorrida apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a soma da distancia percorrida nos exercicios de mesmo tipo e feitos no mesmo 
	 * dia. Permite ter mais de um exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo a dist�ncia percorrida nos 
	 * exerc�cios por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoDistanciaPorDia(){
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);
		
		grafico.setTituloGrafico(obterTituloGrafico("Dist�ncia Percorrida"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Dist�ncia (Km)");
		
		grafico.adicionarAoDataSetSoma(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Dist�ncia");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getDistancia()); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}
	
	/**
	 * Obt�m o gr�fico com os dados referentes �s calorias perdidas nos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo das calorias perdidas ser� feito com 
	 * os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * soma das calorias perdidas em todos os exerc�cios. Se n�o, apenas do exerc�cio 
	 * especificado. Por exemplo, as calorias perdidas apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a soma das calorias perdidas dos exercicios de mesmo tipo e feitos no mesmo 
	 * dia. Permite ter mais de um exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo a caloria perdida nos exerc�cios 
	 * por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoCaloriaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("Calorias Perdidas"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Calorias Perdidas (Kcal)");

		grafico.adicionarAoDataSetSoma(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Calorias");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getCaloriasPerdidas()); 
			return conteudo;
		});

		return grafico.gerarGrafico();
	}
	
	/**
	 * Obt�m o gr�fico com os dados referentes m�dia da distancia percorrida nos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo da m�dia da distancia percorrida 
	 * ser� feito com os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * a m�dia da distancia percorrida de todos os exerc�cios. Se n�o, apenas do 
	 * exerc�cio especificado. Por exemplo, a m�dia da distancia percorrida apenas 
	 * dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a m�dia da distancia percorrida dos exercicios de mesmo tipo e feitos no 
	 * mesmo dia. Permite ter mais de um exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo a m�dia da dist�ncia percorrida 
	 * nos exerc�cios por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoMediaDistanciaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);
		
		grafico.setTituloGrafico(obterTituloGrafico("M�dia da Dist�ncia Percorrida"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Dist�ncia M�dia (Km)");
		
		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Dist�ncia");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getDistancia()); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}

	/**
	 * Obt�m o gr�fico com os dados referentes m�dia de calorias perdidas nos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo da m�dia de calorias perdidas 
	 * ser� feito com os exerc�cios dentro do per�odo fornecido e ir� 
	 * depender tamb�m das configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * m�dia de calorias perdidas em todos os exerc�cios. 
	 * Se n�o, apenas do exerc�cio especificado. Por exemplo, as calorias perdidas 
	 * apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a m�dia de calorias perdidas de todos dos exercicios de mesmo tipo e feitos no 
	 * mesmo dia. Permite ter mais de um exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo a m�dia de calorias perdidas nos exerc�cios 
	 * por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoMediaCaloriaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("M�dia de Calorias Perdidas"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("M�dia de Calorias Perdida (Kcal)");

		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Calorias");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getCaloriasPerdidas()); 
			return conteudo;
		});

		return grafico.gerarGrafico();
	}

	/**
	 * Obt�m o gr�fico com os dados referentes ao n�mero de passos dados nos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo passos dados ser� feito com 
	 * os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * soma dos passos em todos os exerc�cios. Se n�o, apenas do exerc�cio especificado. 
	 * Por exemplo, os passos apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a soma apenas dos exercicios de mesmo tipo e feitos no mesmo dia. 
	 * Permite ter mais de um exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo os passos dados nos exerc�cios 
	 * por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoPassosPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("Passos Dados"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Passos");

		grafico.adicionarAoDataSetSoma(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Passos");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getPassos()); 
			return conteudo;
		});

		return grafico.gerarGrafico();
	}

	/**
	 * Obt�m o gr�fico com os dados referentes ao Ritmo M�dio dos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo do ritmo m�dio ser� feito com 
	 * os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * m�dia de todos os exerc�cios. Se n�o, apenas do exerc�cio especificado. 
	 * Por exemplo, o ritmo m�dio apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a m�dia dos exercicios de mesmo tipo e feitos no mesmo dia. Permite ter mais de um 
	 * exerc�cio associado a uma data. 
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo o ritmo m�dio nos exerc�cios 
	 * por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoRitmoMedioPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("R�tmo M�dio dos "));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("R�tmo M�dio (Min/Km)");

		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			// Se n�o for ExercicioDetalhado n�o ter� Ritmo.
			if(!(exercicio instanceof ExercicioDetalhado)) return null;
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Ritmo");
			if(conteudo == null) return null;
			
			ExercicioDetalhado exercicioDet = (ExercicioDetalhado) exercicio;
			conteudo.setValor(exercicioDet.getRitmo().getRitmoMedio().getHora().toSecondOfDay()/60); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}

	/**
	 * Obt�m o gr�fico com os dados referentes a Velocidade M�dia dos exerc�cios 
	 * que foram feitos no mesmo dia. O c�lculo da velocidade m�dia ser� feito com 
	 * os exerc�cios dentro do per�odo fornecido e ir� depender tamb�m das 
	 * configura��es fornecidas nos m�todos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos m�todos de acesso para 
	 * obter o tipo gr�fico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> ir� obter a 
	 * m�dia de todos os exerc�cios. Se n�o, apenas do exerc�cio especificado. 
	 * Por exemplo, a velocidade m�dia apenas dos exerc�cios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> ir� obter 
	 * a m�dia dos exercicios de mesmo tipo e feitos no mesmo dia. Permite ter mais de um 
	 * exerc�cio associado a uma data.
	 *  
	 * O tipo de gr�fico ir� depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gr�fico exibindo a velocidade m�dia dos exerc�cios 
	 * por data, dentro do per�odo fornecido para o relat�rio.
	 */
	public GraficoCategory graficoVelocidadeMediaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("Velocidade M�dia"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Velocidade M�dia (Km/h)");
			

		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			// Se n�o for ExercicioDetalhado n�o ter� velocidade.
			if(!(exercicio instanceof ExercicioDetalhado)) return null;
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Velocidade");
			if(conteudo == null) return null;
			
			ExercicioDetalhado exercicioDet = (ExercicioDetalhado) exercicio;
			conteudo.setValor(exercicioDet.getVelocidade().getVelocidadeMedia()); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}
	
	/**
	 * Obt�m uma nova inst�ncia de uma subclasse de {@link GraficoCategory}
	 * de acordo com o par�metro que for passado.
	 * 
	 * @param tipoGrafico Tipo de gr�fico do qual se deseja ter uma nova inst�ncia.
	 * @return Retorna a nova inst�ncia do gr�fico.
	 */
	public GraficoCategory obterInstanciaGrafico(TipoGrafico tipoGrafico) {
		switch (tipoGrafico) {
		case COLUNA: return new GraficoColuna();
		case LINHA : return new GraficoLinha();
		default: return null;
		}
	}
	
	/**
	 * Obt�m uma sublista dos exerc�cios que est�o no intervalo das 
	 * datas fornecidas ao instanciar a classe.
	 * 
	 * @return Retorna a sublista com os exercicios no periodo.
	 */
	private List<Exercicio> obterExerciciosDoPeriodo() {
		inverterDatasSeTrocadas();
		Collections.sort(exercicios);
		
		int indiceInicio = procurarIndiceDataInicial();
		int indiceFinal = procurarIndiceDataFinal();

		//Obt�m o valor negativo oposto ao tamanho da lista, e soma uma unidade 
		int indiceMaxNegativo = (exercicios.size()+1)*-1;
		
		/* Se os dois indices forem igual a -1 ou igual ao tamanho da lista -1 
		 * indica que o per�odo fornecido est� fora do intervalo dos exerc�cios 
		 * contidos na lista. Ou seja, n�o h� exerc�cios neste per�odo.*/
		if((indiceInicio == indiceMaxNegativo && indiceFinal == indiceMaxNegativo) 
			 || (indiceInicio == -1 && indiceFinal == -1)) return new ArrayList<>();
		
		/* Se o �ndice for menor que 0 tamb�m indica que � a posi��o que a data ficaria 
		 * se existisse na lista, de acordo com a documenta��o do binarySearch que foi 
		 * utilizado para encontrar os �ndices. Ent�o o �ndice possivel se a data 
		 * existisse na lista seria o positivo desse �ndice encontrado menos um: 
		 * indice v�lido = ((indiceNegativo * -1) -1). */
		if(indiceInicio < 0) indiceInicio = obterIndiceValidoDaLista(indiceInicio);
		if(indiceFinal < 0) indiceFinal = obterIndiceValidoDaLista(indiceFinal);
		
		return exercicios.subList(indiceInicio, indiceFinal+1);
	}
	
	/**
	 * Obt�m um �ndice v�lido para a lista de exercicios. O indice obtido � 
	 * o oposto do indice que foi fornecido menos 1.
	 * 
	 * Portanto se for fornecido -5 o resultado ser� 4. Se o n�mero obtido 
	 * for do tamanho da lista de exercicios ent�o subtrain mais uma unidade
	 * para indexar o �ltimo elemento da lista. 
	 * 
	 * @param indiceFinal O indice a ser convertido para o seu oposto.
	 * 
	 * @return Retorna o indice fornecido com o sinal trocado e 
	 * subtra�do em uma unidade. 
	 */
	private int obterIndiceValidoDaLista(int indiceFinal) {
		int indice = (indiceFinal * -1) -1;
		return (indice == exercicios.size())? indice -1: indice;
	}
	
	/**
	 * Procura o �ndice que indexa o primeiro exerc�cio da lista 
	 * que tem data igual ou superior a dataInicial da classe.
	 *   
	 * Caso n�o possua o exerc�cio com a data retorna o indice que 
	 * ele ficaria se existisse na lista, mas neste caso retorna o 
	 * n�mero como negativo e soma em uma unidade. 
	 * Por exemplo: O exercicio com a data X ficaria na posi��o 10 se 
	 * exestisse ent�o seria retornado: (10*-1)-1 = -11.  
	 * 
	 * @return Retorna o indice do primeiro exercicio da lista com data igual ou 
	 * superior a dataInicial ou o indice negativo que indica qual posi��o ele ficaria. 
	 */
	private int  procurarIndiceDataInicial() {
		Exercicio ex = new Exercicio();
		ex.setData(dataInicial);
		int indiceInicio = Collections.binarySearch(exercicios, ex, new ComparatorDataExercicio());
		
		if(indiceInicio < 0) return indiceInicio;
			
		/* Como o indice encontrado � positivo, indicando que a data existe na lista, 
		 * e uma busca bin�ria encontra apenas a primeira data a medida que vai 
		 * repartindo ao meio, � preciso encontrar a PRIMEIRA ocorrencia da data
		 * na lista. Ent�o percorre do indice encontrado para BAIXO at� encontra-lo.
		 */
		for(int indice = indiceInicio-1 ; indice >= 0; indice--) {
			if(exercicios.get(indice).getData().compareTo(dataInicial) == 0)
				indiceInicio = indice;
			else break;
		}
		
		return indiceInicio;
	}
	
	/**
	 * Procura o �ndice que indexa o �ltimo exerc�cio da lista 
	 * que tem data igual ou inferior a dataFinal da classe.
	 *   
	 * Caso n�o possua o exerc�cio com a data retorna o indice que 
	 * ele ficaria se existisse na lista, mas neste caso retorna o 
	 * n�mero como negativo e soma em uma unidade. 
	 * Por exemplo: O exercicio com a data X ficaria na posi��o 10 se 
	 * exestisse ent�o seria retornado: (10*-1)-1 = -11.  
	 * 
	 * @return  Retorna o indice do �ltimo exercicio da lista com data igual ou 
	 * inferior a dataFinal ou o indice negativo que indica qual posi��o ele ficaria.
	 */
	private int  procurarIndiceDataFinal() {
		Exercicio ex = new Exercicio();
		ex.setData(dataFinal);
		int indiceFinal = Collections.binarySearch(exercicios, ex, new ComparatorDataExercicio());
		
		if(indiceFinal < 0) return indiceFinal;
		
		/* Como o indice encontrado � positivo, indicando que a data existe na lista, 
		 * e uma busca bin�ria encontra apenas a primeira data a medida que vai 
		 * repartindo ao meio, � preciso encontrar a �LTIMA ocorrencia da data final
		 * na lista. Ent�o percorre do indice encontrado para CIMA at� encontra-lo.
		 */
		for(int indice = indiceFinal + 1; indice < exercicios.size(); indice++) {
			if(exercicios.get(indice).getData().compareTo(dataFinal) == 0)
				indiceFinal = indice;
			else break;
		}
		
		return indiceFinal;
	}
	
	/**
	 * Inverte as datas do per�do do relatorio fornecido para o 
	 * caso de a data inicial for superior a data final.
	 */
	private void inverterDatasSeTrocadas() {
		if(dataInicial.compareTo(dataFinal) > 0) {
			Data dataInicialAux = new Data(dataInicial.getData());
			this.dataInicial.setData(dataFinal.getData());
			this.dataFinal.setData(dataInicialAux.getData());
		}
	}

	/**
	 * Obt�m a sublista de exerc�cios que foi obtida a partir da lista 
	 * de exerc�cio inserida na classe e no per�odo fornecido.
	 * 
	 * @return Retorna a lista de exerc�cios que possui 
	 * a data dentro do per�odo fornecido na cria��o da classe.
	 */
	public List<Exercicio> getExerciciosPeriodo() {
		return exerciciosDoPeriodo;
	}

	/**
	 * Obt�m a data inicial que foi fornecida para gerar o relat�rio.
	 * 
	 * @return Retorna a data inicial do relat�rio.
	 */
	public Data getDataInicial() {
		return dataInicial;
	}

	/**
	 * Obt�m a data final que foi fornecida para gerar o relat�rio.
	 * 
	 * @return Retorna a data final do relat�rio.
	 */
	public Data getDataFinal() {
		return dataFinal;
	}

	/**
	 * Obt�m o tipo de gr�fico atual configurado no relat�rio.
	 * @return Retorna o tipo de gr�fico.
	 */
	public TipoGrafico getTipoGrafico() {
		return tipoGrafico;
	}

	/**
	 * Insere o tipo de gr�fico a ser gerado pelo relat�rio ao chamar 
	 * os m�todos para gera-lo.
	 *  
	 * @param tipoGrafico	O tipo de gr�fico a ser gerado pelo relat�rio.
	 */
	public void setTipoGrafico(TipoGrafico tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}

	/**
	 * Verifica se o relat�tio est� ou n�o configurado 
	 * para separar os exerc�cios por nome ao gerar um gr�fico.
	 * 
	 * @return Retorna <code>true</code> se est� configurado para 
	 * separar os exerc�cios por nome. Se n�o, retorna <code>false</code>.
	 */
	public boolean isSepararExercicios() {
		return separarExercicios;
	}

	/**
	 * Indica se o gr�fico ir� separar os exerc�cios por nome 
	 * ao gerar um gr�fico.
	 * 
	 * @param separarExercicios <code>true</code> se for preciso 
	 * separar os exerc�cios por nome. Se n�o, deve ser <code>false</code>.
	 */
	public void setSepararExercicios(boolean separarExercicios) {
		this.separarExercicios = separarExercicios;
	}

	/**
	 * Obt�m o nome do exerc�cio �nico que o relat�rio est� considerando 
	 * ao gerar um gr�fico.
	 * 
	 * @return Retorna o nome do exerc�cio configurado para gerar o relat�rio, 
	 * se n�o houver restri��o de exerc�cio retorna <code>true</code>; 
	 */
	public String getExercicioFiltrado() {
		return filtrarExercicio;
	}
	
	
	/** 
	 * Insere o nome do exerc�cio que o relat�rio deve considerar 
	 * ao gerar os gr�ficos. Portando, se fornecido, o gr�fico ser� 
	 * exibido com os dados apenas dos exerc�cios que possuem o nome 
	 * igual ao que foi fornecido.
	 * 
	 * @param exercicioFiltrar	O nome do exerc�cio a ser considerado
	 * ao gerar os gr�ficos. Caso n�o seja preciso esta restri��o de 
	 * exerc�cio deve ser <code>null</code>.
	 */
	public void setFiltrarExercicio(String exercicioFiltrar) {
		this.filtrarExercicio = exercicioFiltrar;
	}
	
	/**
	 * Crit�rio de compara��o utilizado para comparar apenas as datas
	 * dos exerc�cios que s�o passados por par�metro. Caso seja 
	 * utilizado como crit�rio de ordena��o ir� ordenar os exerc�cios 
	 * por data de forma ascendente.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	public static class ComparatorDataExercicio implements Comparator<Exercicio>{

		@Override
		public int compare(Exercicio ex1, Exercicio ex2) {
			return ex1.getData().compareTo(ex2.getData());
		}
		
	}

}
