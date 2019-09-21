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
 * Classe que permite gerar gráficos a partir dos exercícios e no período 
 * que são fornecidos no contrutor da classe. <br>
 * 
 * Os gráficos gerados utilizam diferentes dados dos exercícios e é possível 
 * gerar diferentes gráficos modificando as consfigurações através dos 
 * métodos de acesso da classe. 
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RelatorioGrafico {
	/**Indica qual o tipo de gráfico pode ser utilizado. 
	 * @author Guilherme Domith Ribeiro Coelho */
	public enum TipoGrafico {COLUNA, LINHA};
	
	private List<Exercicio> exercicios, exerciciosDoPeriodo;
	private TipoGrafico tipoGrafico;
	private boolean separarExercicios; /* Indica se os exercicios com a mesma data 
											 serão adicionados separadamente no gráfico.*/
	private String filtrarExercicio; /* Indica se os gráficos a serem gerados serão apenas 
										para o exercício filtrado (exercício contido nesta variável). */
	private Data dataInicial, dataFinal;

	/**
	 * Construtor que cria um novo relatório com os exercícios 
	 * necessários para a geração dos gráfico no período fornecido.<br>
	 * 
	 * O período deve ser fornecido por meio da data inicial e data final,
	 * que indica que os exercícios a considerar, ao gerar os gráficos,
	 * serão aqueles que têm a data entre este intervalo. 
	 *  
	 * @param exercicios 	Os exercício que serão utilizados para popular os gráficos.
	 * @param dataInicial	A data inicial que determina o limite inferior dos exercícios 
	 * que serão considerados na geração dos gráficos.
	 * @param dataFinal		A data final que determina o limite superior dos exercícios 
	 * que serão considerados na geração dos gráficos.
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
	 * Obtém os dados data e exercício, coluna e linha respectivamente, que são dados 
	 * em comum de todos os exercícios que serão adicionados no gráfico.
	 * Caso o exercício não for o mesmo exercício que está na variavel 
	 * exercicioFiltrar retorna <code>null</code>. 
	 * 
	 * @param exercicio 	O exercício a ser adicionado no gráfico.
	 * @param string		A string que representa a linha que está sendo adicionada. 
	 * Se o relatório estiver configurado para separar os exercício ela não será 
	 * utilizada, a linha será o próprio nome do exercício.
	 * 
	 * @return Retorna o conteudo a ser adicionado no gráfic com a linha e coluna, mas ainda
	 * faltando o valor. Caso o exercício seja um exercício configurado para não adicionar 
	 * retorna <code>null</code>.
	 */
	private ConteudoDataSet obterColunaELinhaDoExercicio(Exercicio exercicio, String descricaoLinha) {
		/* Verifica se o exercicio passado por parâmetro é para adicionar no gráfico,
		 * Se for null significa que pode adicionar qualquer exercício. */
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
				(filtrarExercicio == null)? "Todos os Exercícios": filtrarExercicio , 
			    dataInicial, dataFinal);
	}
	
	
	/**
	 * Obtém o gráfico com os dados referentes a duração dos exercícios 
	 * que foram feitos no mesmo dia. O cálculo da duração será feito com 
	 * os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * soma da duração de todos os exercícios. Se não, apenas do exercício especificado. 
	 * Por exemplo, a duração apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a soma da duração dos exercicios de mesmo tipo e feitos no mesmo dia. 
	 * Permite ter mais de um exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo a a duração dos exercícios 
	 * por data, dentro do período fornecido para o relatório.
	 */
	public GraficoCategory graficoDuracaoPorDia(){
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);
							
		grafico.setTituloGrafico(obterTituloGrafico("Duração"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Duração (Min)");
		
		grafico.adicionarAoDataSetSoma(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Duração");
			if(conteudo == null) return null;
			
			Hora duracao = exercicio.getTempo().getDuracao();
			conteudo.setValor(duracao.getHora().toSecondOfDay()/60); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}
	
	/**
	 * Obtém o gráfico com os dados referentes a distancia percorrida nos exercícios 
	 * que foram feitos no mesmo dia. O cálculo da distancia percorrida será feito com 
	 * os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * soma da distancia percorrida em todos os exercícios. Se não, apenas do exercício 
	 * especificado. Por exemplo, a distancia percorrida apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a soma da distancia percorrida nos exercicios de mesmo tipo e feitos no mesmo 
	 * dia. Permite ter mais de um exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo a distância percorrida nos 
	 * exercícios por data, dentro do período fornecido para o relatório.
	 */
	public GraficoCategory graficoDistanciaPorDia(){
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);
		
		grafico.setTituloGrafico(obterTituloGrafico("Distância Percorrida"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Distância (Km)");
		
		grafico.adicionarAoDataSetSoma(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Distância");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getDistancia()); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}
	
	/**
	 * Obtém o gráfico com os dados referentes às calorias perdidas nos exercícios 
	 * que foram feitos no mesmo dia. O cálculo das calorias perdidas será feito com 
	 * os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * soma das calorias perdidas em todos os exercícios. Se não, apenas do exercício 
	 * especificado. Por exemplo, as calorias perdidas apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a soma das calorias perdidas dos exercicios de mesmo tipo e feitos no mesmo 
	 * dia. Permite ter mais de um exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo a caloria perdida nos exercícios 
	 * por data, dentro do período fornecido para o relatório.
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
	 * Obtém o gráfico com os dados referentes média da distancia percorrida nos exercícios 
	 * que foram feitos no mesmo dia. O cálculo da média da distancia percorrida 
	 * será feito com os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * a média da distancia percorrida de todos os exercícios. Se não, apenas do 
	 * exercício especificado. Por exemplo, a média da distancia percorrida apenas 
	 * dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a média da distancia percorrida dos exercicios de mesmo tipo e feitos no 
	 * mesmo dia. Permite ter mais de um exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo a média da distância percorrida 
	 * nos exercícios por data, dentro do período fornecido para o relatório.
	 */
	public GraficoCategory graficoMediaDistanciaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);
		
		grafico.setTituloGrafico(obterTituloGrafico("Média da Distância Percorrida"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Distância Média (Km)");
		
		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Distância");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getDistancia()); 
			return conteudo;
		});
		
		return grafico.gerarGrafico();
	}

	/**
	 * Obtém o gráfico com os dados referentes média de calorias perdidas nos exercícios 
	 * que foram feitos no mesmo dia. O cálculo da média de calorias perdidas 
	 * será feito com os exercícios dentro do período fornecido e irá 
	 * depender também das configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * média de calorias perdidas em todos os exercícios. 
	 * Se não, apenas do exercício especificado. Por exemplo, as calorias perdidas 
	 * apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a média de calorias perdidas de todos dos exercicios de mesmo tipo e feitos no 
	 * mesmo dia. Permite ter mais de um exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo a média de calorias perdidas nos exercícios 
	 * por data, dentro do período fornecido para o relatório.
	 */
	public GraficoCategory graficoMediaCaloriaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("Média de Calorias Perdidas"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Média de Calorias Perdida (Kcal)");

		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			
			ConteudoDataSet conteudo = obterColunaELinhaDoExercicio(exercicio, "Calorias");
			if(conteudo == null) return null;
			
			conteudo.setValor(exercicio.getCaloriasPerdidas()); 
			return conteudo;
		});

		return grafico.gerarGrafico();
	}

	/**
	 * Obtém o gráfico com os dados referentes ao número de passos dados nos exercícios 
	 * que foram feitos no mesmo dia. O cálculo passos dados será feito com 
	 * os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * soma dos passos em todos os exercícios. Se não, apenas do exercício especificado. 
	 * Por exemplo, os passos apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a soma apenas dos exercicios de mesmo tipo e feitos no mesmo dia. 
	 * Permite ter mais de um exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo os passos dados nos exercícios 
	 * por data, dentro do período fornecido para o relatório.
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
	 * Obtém o gráfico com os dados referentes ao Ritmo Médio dos exercícios 
	 * que foram feitos no mesmo dia. O cálculo do ritmo médio será feito com 
	 * os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * média de todos os exercícios. Se não, apenas do exercício especificado. 
	 * Por exemplo, o ritmo médio apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a média dos exercicios de mesmo tipo e feitos no mesmo dia. Permite ter mais de um 
	 * exercício associado a uma data. 
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo o ritmo médio nos exercícios 
	 * por data, dentro do período fornecido para o relatório.
	 */
	public GraficoCategory graficoRitmoMedioPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("Rítmo Médio dos "));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Rítmo Médio (Min/Km)");

		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			// Se não for ExercicioDetalhado não terá Ritmo.
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
	 * Obtém o gráfico com os dados referentes a Velocidade Média dos exercícios 
	 * que foram feitos no mesmo dia. O cálculo da velocidade média será feito com 
	 * os exercícios dentro do período fornecido e irá depender também das 
	 * configurações fornecidas nos métodos de acesso da classe.<br>
	 * 
	 * Portanto, certifique-se de inserir os dados pelos métodos de acesso para 
	 * obter o tipo gráfico de acordo com o esperado.
	 * 
	 * Se {@link #setFiltrarExercicio(String)} for <code>null</code> irá obter a 
	 * média de todos os exercícios. Se não, apenas do exercício especificado. 
	 * Por exemplo, a velocidade média apenas dos exercícios "caminhada".
	 * 
	 * Se {@link #setSepararExercicios(boolean)} for <code>true</code> irá obter 
	 * a média dos exercicios de mesmo tipo e feitos no mesmo dia. Permite ter mais de um 
	 * exercício associado a uma data.
	 *  
	 * O tipo de gráfico irá depender do valor fornecido em {@link #setTipoGrafico(TipoGrafico)}
	 * 
	 * @return Retorna o gráfico exibindo a velocidade média dos exercícios 
	 * por data, dentro do período fornecido para o relatório.
	 */
	public GraficoCategory graficoVelocidadeMediaPorDia() {
		GraficoCategory grafico = obterInstanciaGrafico(tipoGrafico);

		grafico.setTituloGrafico(obterTituloGrafico("Velocidade Média"));
		grafico.setTituloEixoX("Dia");
		grafico.setTituloEixoY("Velocidade Média (Km/h)");
			

		grafico.adicionarAoDataSetMedia(exerciciosDoPeriodo,  (Exercicio exercicio) -> {
			// Se não for ExercicioDetalhado não terá velocidade.
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
	 * Obtém uma nova instância de uma subclasse de {@link GraficoCategory}
	 * de acordo com o parâmetro que for passado.
	 * 
	 * @param tipoGrafico Tipo de gráfico do qual se deseja ter uma nova instância.
	 * @return Retorna a nova instância do gráfico.
	 */
	public GraficoCategory obterInstanciaGrafico(TipoGrafico tipoGrafico) {
		switch (tipoGrafico) {
		case COLUNA: return new GraficoColuna();
		case LINHA : return new GraficoLinha();
		default: return null;
		}
	}
	
	/**
	 * Obtém uma sublista dos exercícios que estão no intervalo das 
	 * datas fornecidas ao instanciar a classe.
	 * 
	 * @return Retorna a sublista com os exercicios no periodo.
	 */
	private List<Exercicio> obterExerciciosDoPeriodo() {
		inverterDatasSeTrocadas();
		Collections.sort(exercicios);
		
		int indiceInicio = procurarIndiceDataInicial();
		int indiceFinal = procurarIndiceDataFinal();

		//Obtém o valor negativo oposto ao tamanho da lista, e soma uma unidade 
		int indiceMaxNegativo = (exercicios.size()+1)*-1;
		
		/* Se os dois indices forem igual a -1 ou igual ao tamanho da lista -1 
		 * indica que o período fornecido está fora do intervalo dos exercícios 
		 * contidos na lista. Ou seja, não há exercícios neste período.*/
		if((indiceInicio == indiceMaxNegativo && indiceFinal == indiceMaxNegativo) 
			 || (indiceInicio == -1 && indiceFinal == -1)) return new ArrayList<>();
		
		/* Se o índice for menor que 0 também indica que é a posição que a data ficaria 
		 * se existisse na lista, de acordo com a documentação do binarySearch que foi 
		 * utilizado para encontrar os índices. Então o índice possivel se a data 
		 * existisse na lista seria o positivo desse índice encontrado menos um: 
		 * indice válido = ((indiceNegativo * -1) -1). */
		if(indiceInicio < 0) indiceInicio = obterIndiceValidoDaLista(indiceInicio);
		if(indiceFinal < 0) indiceFinal = obterIndiceValidoDaLista(indiceFinal);
		
		return exercicios.subList(indiceInicio, indiceFinal+1);
	}
	
	/**
	 * Obtém um índice válido para a lista de exercicios. O indice obtido é 
	 * o oposto do indice que foi fornecido menos 1.
	 * 
	 * Portanto se for fornecido -5 o resultado será 4. Se o número obtido 
	 * for do tamanho da lista de exercicios então subtrain mais uma unidade
	 * para indexar o último elemento da lista. 
	 * 
	 * @param indiceFinal O indice a ser convertido para o seu oposto.
	 * 
	 * @return Retorna o indice fornecido com o sinal trocado e 
	 * subtraído em uma unidade. 
	 */
	private int obterIndiceValidoDaLista(int indiceFinal) {
		int indice = (indiceFinal * -1) -1;
		return (indice == exercicios.size())? indice -1: indice;
	}
	
	/**
	 * Procura o índice que indexa o primeiro exercício da lista 
	 * que tem data igual ou superior a dataInicial da classe.
	 *   
	 * Caso não possua o exercício com a data retorna o indice que 
	 * ele ficaria se existisse na lista, mas neste caso retorna o 
	 * número como negativo e soma em uma unidade. 
	 * Por exemplo: O exercicio com a data X ficaria na posição 10 se 
	 * exestisse então seria retornado: (10*-1)-1 = -11.  
	 * 
	 * @return Retorna o indice do primeiro exercicio da lista com data igual ou 
	 * superior a dataInicial ou o indice negativo que indica qual posição ele ficaria. 
	 */
	private int  procurarIndiceDataInicial() {
		Exercicio ex = new Exercicio();
		ex.setData(dataInicial);
		int indiceInicio = Collections.binarySearch(exercicios, ex, new ComparatorDataExercicio());
		
		if(indiceInicio < 0) return indiceInicio;
			
		/* Como o indice encontrado é positivo, indicando que a data existe na lista, 
		 * e uma busca binária encontra apenas a primeira data a medida que vai 
		 * repartindo ao meio, é preciso encontrar a PRIMEIRA ocorrencia da data
		 * na lista. Então percorre do indice encontrado para BAIXO até encontra-lo.
		 */
		for(int indice = indiceInicio-1 ; indice >= 0; indice--) {
			if(exercicios.get(indice).getData().compareTo(dataInicial) == 0)
				indiceInicio = indice;
			else break;
		}
		
		return indiceInicio;
	}
	
	/**
	 * Procura o índice que indexa o último exercício da lista 
	 * que tem data igual ou inferior a dataFinal da classe.
	 *   
	 * Caso não possua o exercício com a data retorna o indice que 
	 * ele ficaria se existisse na lista, mas neste caso retorna o 
	 * número como negativo e soma em uma unidade. 
	 * Por exemplo: O exercicio com a data X ficaria na posição 10 se 
	 * exestisse então seria retornado: (10*-1)-1 = -11.  
	 * 
	 * @return  Retorna o indice do último exercicio da lista com data igual ou 
	 * inferior a dataFinal ou o indice negativo que indica qual posição ele ficaria.
	 */
	private int  procurarIndiceDataFinal() {
		Exercicio ex = new Exercicio();
		ex.setData(dataFinal);
		int indiceFinal = Collections.binarySearch(exercicios, ex, new ComparatorDataExercicio());
		
		if(indiceFinal < 0) return indiceFinal;
		
		/* Como o indice encontrado é positivo, indicando que a data existe na lista, 
		 * e uma busca binária encontra apenas a primeira data a medida que vai 
		 * repartindo ao meio, é preciso encontrar a ÚLTIMA ocorrencia da data final
		 * na lista. Então percorre do indice encontrado para CIMA até encontra-lo.
		 */
		for(int indice = indiceFinal + 1; indice < exercicios.size(); indice++) {
			if(exercicios.get(indice).getData().compareTo(dataFinal) == 0)
				indiceFinal = indice;
			else break;
		}
		
		return indiceFinal;
	}
	
	/**
	 * Inverte as datas do perído do relatorio fornecido para o 
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
	 * Obtém a sublista de exercícios que foi obtida a partir da lista 
	 * de exercício inserida na classe e no período fornecido.
	 * 
	 * @return Retorna a lista de exercícios que possui 
	 * a data dentro do período fornecido na criação da classe.
	 */
	public List<Exercicio> getExerciciosPeriodo() {
		return exerciciosDoPeriodo;
	}

	/**
	 * Obtém a data inicial que foi fornecida para gerar o relatório.
	 * 
	 * @return Retorna a data inicial do relatório.
	 */
	public Data getDataInicial() {
		return dataInicial;
	}

	/**
	 * Obtém a data final que foi fornecida para gerar o relatório.
	 * 
	 * @return Retorna a data final do relatório.
	 */
	public Data getDataFinal() {
		return dataFinal;
	}

	/**
	 * Obtém o tipo de gráfico atual configurado no relatório.
	 * @return Retorna o tipo de gráfico.
	 */
	public TipoGrafico getTipoGrafico() {
		return tipoGrafico;
	}

	/**
	 * Insere o tipo de gráfico a ser gerado pelo relatório ao chamar 
	 * os métodos para gera-lo.
	 *  
	 * @param tipoGrafico	O tipo de gráfico a ser gerado pelo relatório.
	 */
	public void setTipoGrafico(TipoGrafico tipoGrafico) {
		this.tipoGrafico = tipoGrafico;
	}

	/**
	 * Verifica se o relatótio está ou não configurado 
	 * para separar os exercícios por nome ao gerar um gráfico.
	 * 
	 * @return Retorna <code>true</code> se está configurado para 
	 * separar os exercícios por nome. Se não, retorna <code>false</code>.
	 */
	public boolean isSepararExercicios() {
		return separarExercicios;
	}

	/**
	 * Indica se o gráfico irá separar os exercícios por nome 
	 * ao gerar um gráfico.
	 * 
	 * @param separarExercicios <code>true</code> se for preciso 
	 * separar os exercícios por nome. Se não, deve ser <code>false</code>.
	 */
	public void setSepararExercicios(boolean separarExercicios) {
		this.separarExercicios = separarExercicios;
	}

	/**
	 * Obtém o nome do exercício único que o relatório está considerando 
	 * ao gerar um gráfico.
	 * 
	 * @return Retorna o nome do exercício configurado para gerar o relatório, 
	 * se não houver restrição de exercício retorna <code>true</code>; 
	 */
	public String getExercicioFiltrado() {
		return filtrarExercicio;
	}
	
	
	/** 
	 * Insere o nome do exercício que o relatório deve considerar 
	 * ao gerar os gráficos. Portando, se fornecido, o gráfico será 
	 * exibido com os dados apenas dos exercícios que possuem o nome 
	 * igual ao que foi fornecido.
	 * 
	 * @param exercicioFiltrar	O nome do exercício a ser considerado
	 * ao gerar os gráficos. Caso não seja preciso esta restrição de 
	 * exercício deve ser <code>null</code>.
	 */
	public void setFiltrarExercicio(String exercicioFiltrar) {
		this.filtrarExercicio = exercicioFiltrar;
	}
	
	/**
	 * Critério de comparação utilizado para comparar apenas as datas
	 * dos exercícios que são passados por parâmetro. Caso seja 
	 * utilizado como critério de ordenação irá ordenar os exercícios 
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
