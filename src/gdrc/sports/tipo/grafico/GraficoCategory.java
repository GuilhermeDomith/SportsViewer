package gdrc.sports.tipo.grafico;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.UnknownKeyException;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.Dataset;

/**
 * Mantém os dados necessários para criação de um gráfico que 
 * utiliza um {@link Dataset} do tipo {@link DefaultCategoryDataset}.
 * <br><br>
 * 
 * Um {@link DefaultCategoryDataset} permite inserir três dados, são
 * eles: valor, linha e coluna referente a esse valor. 
 * Ao inserir esses dados nesta classe eles são representados
 * de acordo com o exemplo abaixo:
 * <br><br>
 * 
 * <table border=1 summary="Exemplo dos dados representados por um dataset">
 * <tr> <th></th>				<th>ChaveColunaW</th> 		<th>ChaveColunaX</th> </tr>
 * <tr> <td>ChaveLinhaY	</td>	<td> 0.5 </td> 				<td> 2.3 </td> </tr>
 * <tr> <td>ChaveLinhaZ</td>	<td><code>3.7</code></td>  	<td> 4.1 </td></tr>
 * </table>
 * 
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public abstract class GraficoCategory{
	private final CategoryLabelPositions ROTACAO_EIXO_X;
	protected final PlotOrientation ORIENTACAO_GRAFICO;
	protected final boolean INCLUIR_LEGENDA, INCLUIR_TOOLTIPS, INCLUIR_URLS;
	
	/** Título a ser exibido no topo do gráfico*/
	protected String tituloGrafico;
	/** Título a ser exibido a esquerda do gráfico, se referindo ao eixo Y.*/
	protected String tituloEixoY;
	/** Título a ser exibido na base do gráfico, se referindo ao eixo X.*/
	protected String tituloEixoX;
	/** O gráfico que foi gerado a partir do dataset criado.*/
	protected JFreeChart grafico;
	/** Guarda os dados que serão utilizados para geração do gráfico.*/
	protected DefaultCategoryDataset dataSet;
	
	/**
	 * Instancia o gráfico com as configurações padrões 
	 * defidas por esta classe para serem utilizadas ao 
	 * gerar um gráfico.
	 */
	public GraficoCategory() {
		tituloGrafico = tituloEixoY = tituloEixoX = "";
		this.dataSet = new DefaultCategoryDataset();
		
		// Configurações padrões para o gráfico.
		INCLUIR_LEGENDA = true; 
		INCLUIR_TOOLTIPS = true; 
		INCLUIR_URLS = true;
		ORIENTACAO_GRAFICO = PlotOrientation.VERTICAL;
		ROTACAO_EIXO_X = CategoryLabelPositions.UP_45;
	}

	/**
	 * Instancia o gráfico com os titulos que são passados por parâmetro 
	 * e as configurações padrões defidas pela classe para a personalização
	 * do gráfico.
	 * 
	 * @param tituloGrafico O título principal a ser exibido pelo gráfico, título que 
	 * 		  resume os tipos de dados utilizados pelo gráfico.
	 * @param tituloEixoY O título referente ao dados do eixo y no gráfico.
	 * @param tituloEixoX O título referente ao dados do eixo x no gráfico.
	 */
	public GraficoCategory(String tituloGrafico, String tituloEixoY, String tituloEixoX) {
		this();
		this.tituloGrafico = tituloGrafico;
		this.tituloEixoY = tituloEixoY;
		this.tituloEixoX = tituloEixoX;
	}
	
	/**
	 * Gera um gráfico do tipo {@link GraficoCategory} com os 
	 * dados inseridos na classe. Ao implementar este método deve-se utilizar 
	 * os titulos, dataset são mantidos por esta classe para a criação do mesmo.<br>
	 *  
	 * Como cada tipo de gráfico possui um modo diferente para de instancia-lo.
	 * A classe que estender esta classe, deve implementar este método e retornar 
	 * a referência da própria classe, caso tenha sido criado corretamente.
	 * 
	 * @return Retorna a referência da própria classe ao terminar de gerar o 
	 * gráfico específico. 
	 */
	public abstract GraficoCategory gerarGrafico();
	
	/**
	 * Aplica personalizaçãos padrão, definida por esta classe, ou definidas 
	 * pelo usuário da classe. <br>
	 * 
	 * Deve ser chamada antes ou depois de gerar o gráfico, mas é importante 
	 * que seja chamada para que assegure que todas configurações serão 
	 * aplicadas ao gráfico. <br><br>
	 * 
	 * Pode e deve ser sobreescrito para adicionar configurações específicas
	 * do gráfico que herdar esta classe. É importante frizar que este 
	 * método aplica algumas configurações padrões. 
	 * Portanto, caso sobreescreva, chame este métodos para que tais 
	 * configurações sejam aplicadas se for necessário.
	 */
	protected void aplicarPersonalizacoesAoGrafico() {
		grafico.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(ROTACAO_EIXO_X);
		grafico.setBackgroundPaint(new Color(245, 255, 250));
		grafico.setTitle(new TextTitle(tituloGrafico, new Font("Tahoma", Font.BOLD, 18)));
	}
	
	/**
	 * Adiciona ao dataset a média dos valores que serão extraídos dos objetos 
	 * do tipo <b>T</b>, passado por parâmetro.<br>
	 * Os dados a serem extraídos de cada objeto deve ser implementado 
	 * pelo objeto {@link GeradorConteudoDataSet} que também é passado por parâmetro.
	 * <br> 
	 * A média a ser inserida é calculada para todos os valores que serão inseridos na 
	 * mesma linha e coluna. <br><br>
	 * 
	 * Por exemplo:<br>
	 * Do objeto A foi extraído, valor: 3, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto B foi extraído, valor: 2, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto C foi extraído, valor: 4, linha: chavelinhaY e coluna: chaveColunaX<br>
	 * Do objeto D foi extraído, valor: 1, linha: chavelinhaZ e coluna: chaveColunaX<br>
	 * 
	 * <br>
	 * Então o resultado do dataset será:
	 * <br><br>
	 * 
	 * <table border=1 summary="Exemplo de calculo da média no dataset">
	 * <tr> <th></th>				<th>ChaveColunaW</th>		<th>ChaveColunaX</th> </tr>
	 * <tr> <td>ChaveLinhaY	</td> 	<td>media(3,2) = 2.5</td> 	<td> 4 </td> </tr>
	 * <tr> <td>ChaveLinhaZ</td>	<td><code>null</code></td>  <td> 1 </td></tr>
	 * </table>
	 * 
	 * @param objetosT				A lista de objetos a ser adicionada no dataset.
	 * @param objetoGerador 	O objeto a ser utilizado para obter os dados de cada objeto da lista no 
	 * 
	 *  @param <T> O tipo de objeto que ser inserido no dataset.
	 */
	public <T> void adicionarAoDataSetMedia(List<T> objetosT, GeradorConteudoDataSet<T> objetoGerador) {
		Map<ConteudoDataSet, Integer> contadorLinhaColuna = new TreeMap<>();
		
		for(T objetoT : objetosT) {
			ConteudoDataSet conteudo = objetoGerador.gerarValorDataSet(objetoT);
			if(conteudo == null) continue;
			
			
			/* Conta quantas vezes o conteúdo que será inserido tem a 
			 * mesma chave, para obter a média posteriormente.
			 */
			if(!contadorLinhaColuna.containsKey(conteudo))
				contadorLinhaColuna.put(conteudo, 1);
			else contadorLinhaColuna.put(conteudo, contadorLinhaColuna.get(conteudo)+1);
			
			somarValorAoDataSet(conteudo);
		}
		
		/* Percorre os conteudos adicionados ao dataset obtendo a soma 
		 * que está no dataset e dividindo pela quantidade de vezes contada.
		 */
		for(ConteudoDataSet conteudo : contadorLinhaColuna.keySet()) {
			Number valor = dataSet.getValue(conteudo.getChaveLinha(), conteudo.getChaveColuna());
			int quantidade = contadorLinhaColuna.get(conteudo); 
			
			dataSet.addValue(valor.doubleValue()/quantidade, 
							conteudo.getChaveLinha(), 
							conteudo.getChaveColuna());}
	}
	
	
	/**
	 * Soma os dados do conteúdo passado por parâmetro ao dataset.
	 * Soma se já houver um valor existente na mesma linha e 
	 * coluna, se não existir a linha e coluna no dataset apenas 
	 * adiciona ao dataset.
	 * 
	 * @param conteudo Objeto que contém o valor , linha e coluna 
	 * a ser adicionado ou somado ao dataset. 
	 */
	public void somarValorAoDataSet(ConteudoDataSet conteudo){
		if(conteudo == null) return;
		
		try{ dataSet.incrementValue(conteudo.getValor().doubleValue(), 
									conteudo.getChaveLinha(), 
									conteudo.getChaveColuna());
		}catch (UnknownKeyException e) { 
			dataSet.addValue(conteudo.getValor().doubleValue(),
							 conteudo.getChaveLinha(),
							 conteudo.getChaveColuna());
		}

	}

	
	/**
	 * Adiciona ao dataset a soma dos valores que serão extraídos dos objetos 
	 * do tipo <b>T</b>, passado por parâmetro.<br>
	 * Os dados a serem extraídos de cada objeto deve ser implementado 
	 * pelo objeto {@link GeradorConteudoDataSet} que também é passado por parâmetro.
	 * <br> 
	 * A soma a ser inserida é calculada para todos os valores que serão inseridos na 
	 * mesma linha e coluna. <br><br>
	 * 
	 * Por exemplo:<br>
	 * Do objeto A foi extraído, valor: 3, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto B foi extraído, valor: 2, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto C foi extraído, valor: 4, linha: chavelinhaY e coluna: chaveColunaX<br>
	 * Do objeto D foi extraído, valor: 1, linha: chavelinhaZ e coluna: chaveColunaX<br>
	 * 
	 * <br>
	 * Então o resultado do dataset será:
	 * <br><br>
	 * 
	 * <table border=1 summary="Exemplo de calculo da média no dataset">
	 * <tr> <th></th> 				<th>ChaveColunaW</th>		<th>ChaveColunaX</th> </tr>
	 * <tr> <td>ChaveLinhaY	</td> 	<td>  soma(3,2) = 5  </td> 	<td> 4 </td> </tr>
	 * <tr> <td>ChaveLinhaZ</td>	<td><code>null</code></td>  <td> 1 </td></tr>
	 * </table>
	 * 
	 * @param objetosT				A lista de objetos a ser adicionada no dataset.
	 * @param objetoGerador 	O objeto a ser utilizado para obter os dados de cada objeto da lista no 
	 * primeiro parâmetro.
	 * 
	 * @param <T> O tipo de objeto que ser inserido no dataset.
	 */
	public <T> void adicionarAoDataSetSoma(List<T> objetosT, GeradorConteudoDataSet<T> objetoGerador) {
		for(T objetoT : objetosT) {
			
			ConteudoDataSet conteudo = objetoGerador.gerarValorDataSet(objetoT);
			if(conteudo == null) continue;
			
			somarValorAoDataSet(conteudo);
		}
		
	}
	
	/**
	 * Preenche no dataset todos os valores que não foram inseridos 
	 * para uma linha e coluna com o valor 0. Se não foi inserido nenhum 
	 * valor ele será <code>null</code>. <br>
	 * 
	 * Portanto, caso o usuário necessite que o gráfico mostre o dados 
	 * mesmo para os que não há valor, é preciso que adicione 0 para
	 * que isso aconteça. 
	 */
	protected void preencherDadosVaziosDoDataset() {
		@SuppressWarnings("unchecked")
		List<Comparable<?>> colunas = dataSet.getColumnKeys();
		@SuppressWarnings("unchecked")
		List<Comparable<?>> linhas = dataSet.getRowKeys();
		
		Number valor;
		for(Comparable<?> keyColuna : colunas) 
			for(Comparable<?> keyLinha : linhas) {
				valor = dataSet.getValue(keyLinha, keyColuna);
				if(valor == null)
					dataSet.setValue(0, keyLinha, keyColuna);
			}
	}

	/**
	 * Obtém o gráfico gerado como um objeto {@link JPanel}, que 
	 * pode ser adicinado diretamente em um {@link Container}.
	 * 
	 * @return Retorna o painel com o gráfico gerado.
	 */
	public ChartPanel obterPainelGrafico() {
		return new ChartPanel(grafico);
	}
	
	/**
	 * Obtém os bytes da imagem do gráfico gerado no formato JPEG.
	 * A imagem será criada de acordo com a largura e altura passada 
	 * por parâmetro.
	 *  
	 * @param largura 	A largura do gráfico em imagem a ser gerada.
	 * @param altura	A altura do gráfico em imagem a ser gerada.
	 * 
	 * @return Retorna os bytes da imagem gerada. Caso ocorra um erro 
	 * ao gerar a imagem retorna <code>null</code>.
	 */
	public byte[] obterImagemGraficoJPEG(int largura, int altura) {
		ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
		
		try { ChartUtilities.writeChartAsJPEG(byteArray, grafico, largura, altura);
		} catch (IOException e) { return null; }
		
		return byteArray.toByteArray();
	}

	/**
	 * Obtém uma janela contendo o gráfico que foi gerado.
	 * 
	 * @param largura 	A largura do janela que irá exibir o gráfico.
	 * @param altura	A altura do da janela que irá exibir o gráfico.
	 * 
	 * @return Retorna a janela com o gráfico a ser exibido.
	 */
	public ChartFrame obterFrameGrafico(int largura, int altura) {
		boolean INCLUIR_SCROLLPANE = true;
		ChartFrame chartFrame = new ChartFrame(tituloGrafico, grafico, INCLUIR_SCROLLPANE);
		chartFrame.setSize(700, 500);
		return chartFrame;
	}
	
	/**
	 * Obtém o título associado ao gráfico.
	 * Título que resume os dados utilizados.
	 * 
	 * @return Retorna o título do gráfico.
	 */
	public String getTituloGrafico() {
		return tituloGrafico;
	}

	/**
	 * Insere o título que está associado ao conteúdo do gráfico.
	 * Título que resume os dados utilizados.
	 * 
	 * @param tituloGrafico O título a ser exibido no topo do gráfico.
	 */
	public void setTituloGrafico(String tituloGrafico) {
		this.tituloGrafico = tituloGrafico;
	}

	/**
	 * Obtém o título associado ao eixo X do gráfico.
	 * 
	 * @return Retorna o título do eixo X.
	 */
	public String getTituloEixoY() {
		return tituloEixoY;
	}

	/**
	 * Insere o título que está associado ao eixo Y do gráfico.
	 * 
	 * @param tituloEixoY O título a ser exibido.
	 */
	public void setTituloEixoY(String tituloEixoY) {
		this.tituloEixoY = tituloEixoY;
	}

	/**
	 * Obtém o título associado ao eixo Y do gráfico.
	 * 
	 * @return Retorna o título do eixo Y.
	 */
	public String getTituloEixoX() {
		return tituloEixoX;
	}

	/**
	 * Insere o título que está associado ao eixo X do gráfico.
	 * 
	 * @param tituloEixoX O título a ser exibido.
	 */
	public void setTituloEixoX(String tituloEixoX) {
		this.tituloEixoX = tituloEixoX;
	}

	/**
	 * Interface que oferece uma função para extrair o conteúdo a ser inserido 
	 * em um dataset a partir de um único objeto do tipo <b>T</b>. A partir 
	 * desse objeto será extraído um valor associado a uma linha e coluna 
	 * que será defido por quem implementa esta interface. 
	 * <br> 
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 * @param <T> Tipo de objeto que será utilizado para extrair os valores 
	 * para um DataSet a ser exibirdo em um gráfico.
	 */
	@FunctionalInterface
	public static interface GeradorConteudoDataSet<T>{
		/**
		 * Obtém os dados do objeto <b>T</b> passado por parâmetro que 
		 * podem ser inseridos em um dataset. <br><br>
		 * 
		 * Os dados a serem adicionados no objeto {@link ConteudoDataSet} 
		 * a ser retornado devem ser os dados que o usuário pretende que 
		 * seja adicionado em um dataset, que será exibido por um gráfico.
		 * <br> 
		 * Caso o objeto passado por parâmetro não tenha os dados necessários a 
		 * serem exibidos deve-se retornar <code>null</code>.
		 * 
		 * @param objetoT O objeto do qual será extraído os dados para um gráfico.
		 *  
		 * @return Retorna o objeto {@link ConteudoDataSet} que agrupa um valor 
		 * associado a uma linha e coluna de um dataset. 
		 * Retorna <code>null</code> se o objeto passado por parâmetro não possui 
		 * o dado necessário. 
		 */
		public ConteudoDataSet gerarValorDataSet(T objetoT);
	}
	
	/**
	 * Agrupa os dados de um gráfico que representa apenas
	 * um valor associado a uma linha e a uma coluna do dataset. 
	 * 
	 * Deve ser utilizada para manter o valor que será 
	 * adicionado em um gráfico que utiliza um {@link Dataset} do 
	 * tipo {@link DefaultCategoryDataset}. <br><br>
	 * 
	 * O tipo de Dataset {@link DefaultCategoryDataset} permite adicionar 
	 * os dados valor, chave da linha e chave da coluna, que são 
	 * utilizados para construir uma tabela que resultará em um gráfico.
	 * 
	 * @see GraficoCategory
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public static class ConteudoDataSet implements Comparable<ConteudoDataSet>{
		
		private Comparable<?> chaveLinha, chaveColuna;
		private Number valor;

		/**
		 * Construtor que permite instanciar o objeto com 
		 * os atributos necessários.
		 */
		public ConteudoDataSet() {
			chaveLinha = chaveColuna = "";
			valor = 0;
		}

		/**
		 * Construtor que permite instanciar o objeto adicionando os 
		 * valores que fazem referencia a um valor da tabela construída 
		 * por um {@link DefaultCategoryDataset}.
		 *  
		 * @param valor O valor referente a uma linha e coluna da tabela.
		 * @param chaveLinha A linha em que se encontra o valor.
		 * @param chaveColuna A coluna em que se encontra o valor.
		 */
		public ConteudoDataSet(Number valor, Comparable<?> chaveLinha, Comparable<?> chaveColuna) {
			this.valor = valor;
			this.chaveLinha = chaveLinha;
			this.chaveColuna = chaveColuna;
		}
		
		/**
		 * Obtém a chave da linha que representa 
		 * uma linha da tabela de dados para um gráfico.
		 * 
		 * @return Retorna a chave da linha.
		 */
		public Comparable<?> getChaveLinha() {
			return chaveLinha;
		}
		
		/**
		 * Insere a chave da linha que representa 
		 * uma linha da tabela de dados para um gráfico.
		 *
		 * @param chaveLinha A chave que identifica a linha 
		 * de um gráfico. 
		 */
		public void setChaveLinha(Comparable<?> chaveLinha) {
			this.chaveLinha = chaveLinha;
		}
		
		/**
		 * Obtém a chave da coluna que representa 
		 * uma coluna da tabela de dados para um gráfico.
		 * 
		 * @return Retorna a chave da coluna.
		 */
		public Comparable<?> getChaveColuna() {
			return chaveColuna;
		}
		
		/**
		 * Insere a chave da coluna que representa 
		 * uma coluna da tabela de dados para um gráfico.
		 *
		 * @param chaveColuna A chave que identifica a coluna 
		 * de um gráfico. 
		 */
		public void setChaveColuna(Comparable<?> chaveColuna) {
			this.chaveColuna = chaveColuna;
		}
		
		/**
		 * Obtém o valor associado a linha e coluna 
		 * deste objeto.
		 * 
		 * @return Retorna o valor.
		 */
		public Number getValor() {
			return valor;
		}
		
		/**
		 * Insere o valor associado a linha e coluna 
		 * deste objeto.
		 *
		 * @param valor O valor associado a linha e coluna.
		 */
		public void setValor(Number valor) {
			this.valor = valor;
		}

		@Override
		public String toString() {
			return String.format("Chave linha: %s, Chave coluna: %s, Valor: %s",
								  chaveLinha, chaveColuna, valor);
		}

		@Override
		public int compareTo(ConteudoDataSet conteudo) {
			int comparacaoLinha = chaveLinha.toString().compareTo(conteudo.chaveLinha.toString());
			int comparacaoColuna = chaveColuna.toString().compareTo(conteudo.chaveColuna.toString());
			
			if(comparacaoLinha == 0 && comparacaoColuna == 0)
				return 0;
			
			int soma = comparacaoLinha+comparacaoColuna;
			
			return (soma >= 0)? 	1	 :	 -1;
		}
		
		@Override
		public boolean equals(Object objeto) {
			if(objeto == null) return false;
			if(this == objeto) return true;
			if(!(objeto instanceof ConteudoDataSet)) return false;
			
			ConteudoDataSet conteudo = (ConteudoDataSet) objeto;
			
			if(chaveLinha.equals(conteudo.chaveLinha) && chaveColuna.equals(conteudo.chaveColuna))
				return true;
			else return false;
		}

	}// class GeradorConteudo
}
