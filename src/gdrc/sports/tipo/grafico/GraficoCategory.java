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
 * Mant�m os dados necess�rios para cria��o de um gr�fico que 
 * utiliza um {@link Dataset} do tipo {@link DefaultCategoryDataset}.
 * <br><br>
 * 
 * Um {@link DefaultCategoryDataset} permite inserir tr�s dados, s�o
 * eles: valor, linha e coluna referente a esse valor. 
 * Ao inserir esses dados nesta classe eles s�o representados
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
	
	/** T�tulo a ser exibido no topo do gr�fico*/
	protected String tituloGrafico;
	/** T�tulo a ser exibido a esquerda do gr�fico, se referindo ao eixo Y.*/
	protected String tituloEixoY;
	/** T�tulo a ser exibido na base do gr�fico, se referindo ao eixo X.*/
	protected String tituloEixoX;
	/** O gr�fico que foi gerado a partir do dataset criado.*/
	protected JFreeChart grafico;
	/** Guarda os dados que ser�o utilizados para gera��o do gr�fico.*/
	protected DefaultCategoryDataset dataSet;
	
	/**
	 * Instancia o gr�fico com as configura��es padr�es 
	 * defidas por esta classe para serem utilizadas ao 
	 * gerar um gr�fico.
	 */
	public GraficoCategory() {
		tituloGrafico = tituloEixoY = tituloEixoX = "";
		this.dataSet = new DefaultCategoryDataset();
		
		// Configura��es padr�es para o gr�fico.
		INCLUIR_LEGENDA = true; 
		INCLUIR_TOOLTIPS = true; 
		INCLUIR_URLS = true;
		ORIENTACAO_GRAFICO = PlotOrientation.VERTICAL;
		ROTACAO_EIXO_X = CategoryLabelPositions.UP_45;
	}

	/**
	 * Instancia o gr�fico com os titulos que s�o passados por par�metro 
	 * e as configura��es padr�es defidas pela classe para a personaliza��o
	 * do gr�fico.
	 * 
	 * @param tituloGrafico O t�tulo principal a ser exibido pelo gr�fico, t�tulo que 
	 * 		  resume os tipos de dados utilizados pelo gr�fico.
	 * @param tituloEixoY O t�tulo referente ao dados do eixo y no gr�fico.
	 * @param tituloEixoX O t�tulo referente ao dados do eixo x no gr�fico.
	 */
	public GraficoCategory(String tituloGrafico, String tituloEixoY, String tituloEixoX) {
		this();
		this.tituloGrafico = tituloGrafico;
		this.tituloEixoY = tituloEixoY;
		this.tituloEixoX = tituloEixoX;
	}
	
	/**
	 * Gera um gr�fico do tipo {@link GraficoCategory} com os 
	 * dados inseridos na classe. Ao implementar este m�todo deve-se utilizar 
	 * os titulos, dataset s�o mantidos por esta classe para a cria��o do mesmo.<br>
	 *  
	 * Como cada tipo de gr�fico possui um modo diferente para de instancia-lo.
	 * A classe que estender esta classe, deve implementar este m�todo e retornar 
	 * a refer�ncia da pr�pria classe, caso tenha sido criado corretamente.
	 * 
	 * @return Retorna a refer�ncia da pr�pria classe ao terminar de gerar o 
	 * gr�fico espec�fico. 
	 */
	public abstract GraficoCategory gerarGrafico();
	
	/**
	 * Aplica personaliza��os padr�o, definida por esta classe, ou definidas 
	 * pelo usu�rio da classe. <br>
	 * 
	 * Deve ser chamada antes ou depois de gerar o gr�fico, mas � importante 
	 * que seja chamada para que assegure que todas configura��es ser�o 
	 * aplicadas ao gr�fico. <br><br>
	 * 
	 * Pode e deve ser sobreescrito para adicionar configura��es espec�ficas
	 * do gr�fico que herdar esta classe. � importante frizar que este 
	 * m�todo aplica algumas configura��es padr�es. 
	 * Portanto, caso sobreescreva, chame este m�todos para que tais 
	 * configura��es sejam aplicadas se for necess�rio.
	 */
	protected void aplicarPersonalizacoesAoGrafico() {
		grafico.getCategoryPlot().getDomainAxis().setCategoryLabelPositions(ROTACAO_EIXO_X);
		grafico.setBackgroundPaint(new Color(245, 255, 250));
		grafico.setTitle(new TextTitle(tituloGrafico, new Font("Tahoma", Font.BOLD, 18)));
	}
	
	/**
	 * Adiciona ao dataset a m�dia dos valores que ser�o extra�dos dos objetos 
	 * do tipo <b>T</b>, passado por par�metro.<br>
	 * Os dados a serem extra�dos de cada objeto deve ser implementado 
	 * pelo objeto {@link GeradorConteudoDataSet} que tamb�m � passado por par�metro.
	 * <br> 
	 * A m�dia a ser inserida � calculada para todos os valores que ser�o inseridos na 
	 * mesma linha e coluna. <br><br>
	 * 
	 * Por exemplo:<br>
	 * Do objeto A foi extra�do, valor: 3, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto B foi extra�do, valor: 2, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto C foi extra�do, valor: 4, linha: chavelinhaY e coluna: chaveColunaX<br>
	 * Do objeto D foi extra�do, valor: 1, linha: chavelinhaZ e coluna: chaveColunaX<br>
	 * 
	 * <br>
	 * Ent�o o resultado do dataset ser�:
	 * <br><br>
	 * 
	 * <table border=1 summary="Exemplo de calculo da m�dia no dataset">
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
			
			
			/* Conta quantas vezes o conte�do que ser� inserido tem a 
			 * mesma chave, para obter a m�dia posteriormente.
			 */
			if(!contadorLinhaColuna.containsKey(conteudo))
				contadorLinhaColuna.put(conteudo, 1);
			else contadorLinhaColuna.put(conteudo, contadorLinhaColuna.get(conteudo)+1);
			
			somarValorAoDataSet(conteudo);
		}
		
		/* Percorre os conteudos adicionados ao dataset obtendo a soma 
		 * que est� no dataset e dividindo pela quantidade de vezes contada.
		 */
		for(ConteudoDataSet conteudo : contadorLinhaColuna.keySet()) {
			Number valor = dataSet.getValue(conteudo.getChaveLinha(), conteudo.getChaveColuna());
			int quantidade = contadorLinhaColuna.get(conteudo); 
			
			dataSet.addValue(valor.doubleValue()/quantidade, 
							conteudo.getChaveLinha(), 
							conteudo.getChaveColuna());}
	}
	
	
	/**
	 * Soma os dados do conte�do passado por par�metro ao dataset.
	 * Soma se j� houver um valor existente na mesma linha e 
	 * coluna, se n�o existir a linha e coluna no dataset apenas 
	 * adiciona ao dataset.
	 * 
	 * @param conteudo Objeto que cont�m o valor , linha e coluna 
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
	 * Adiciona ao dataset a soma dos valores que ser�o extra�dos dos objetos 
	 * do tipo <b>T</b>, passado por par�metro.<br>
	 * Os dados a serem extra�dos de cada objeto deve ser implementado 
	 * pelo objeto {@link GeradorConteudoDataSet} que tamb�m � passado por par�metro.
	 * <br> 
	 * A soma a ser inserida � calculada para todos os valores que ser�o inseridos na 
	 * mesma linha e coluna. <br><br>
	 * 
	 * Por exemplo:<br>
	 * Do objeto A foi extra�do, valor: 3, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto B foi extra�do, valor: 2, linha: chavelinhaY e coluna: chaveColunaW<br>
	 * Do objeto C foi extra�do, valor: 4, linha: chavelinhaY e coluna: chaveColunaX<br>
	 * Do objeto D foi extra�do, valor: 1, linha: chavelinhaZ e coluna: chaveColunaX<br>
	 * 
	 * <br>
	 * Ent�o o resultado do dataset ser�:
	 * <br><br>
	 * 
	 * <table border=1 summary="Exemplo de calculo da m�dia no dataset">
	 * <tr> <th></th> 				<th>ChaveColunaW</th>		<th>ChaveColunaX</th> </tr>
	 * <tr> <td>ChaveLinhaY	</td> 	<td>  soma(3,2) = 5  </td> 	<td> 4 </td> </tr>
	 * <tr> <td>ChaveLinhaZ</td>	<td><code>null</code></td>  <td> 1 </td></tr>
	 * </table>
	 * 
	 * @param objetosT				A lista de objetos a ser adicionada no dataset.
	 * @param objetoGerador 	O objeto a ser utilizado para obter os dados de cada objeto da lista no 
	 * primeiro par�metro.
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
	 * Preenche no dataset todos os valores que n�o foram inseridos 
	 * para uma linha e coluna com o valor 0. Se n�o foi inserido nenhum 
	 * valor ele ser� <code>null</code>. <br>
	 * 
	 * Portanto, caso o usu�rio necessite que o gr�fico mostre o dados 
	 * mesmo para os que n�o h� valor, � preciso que adicione 0 para
	 * que isso aconte�a. 
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
	 * Obt�m o gr�fico gerado como um objeto {@link JPanel}, que 
	 * pode ser adicinado diretamente em um {@link Container}.
	 * 
	 * @return Retorna o painel com o gr�fico gerado.
	 */
	public ChartPanel obterPainelGrafico() {
		return new ChartPanel(grafico);
	}
	
	/**
	 * Obt�m os bytes da imagem do gr�fico gerado no formato JPEG.
	 * A imagem ser� criada de acordo com a largura e altura passada 
	 * por par�metro.
	 *  
	 * @param largura 	A largura do gr�fico em imagem a ser gerada.
	 * @param altura	A altura do gr�fico em imagem a ser gerada.
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
	 * Obt�m uma janela contendo o gr�fico que foi gerado.
	 * 
	 * @param largura 	A largura do janela que ir� exibir o gr�fico.
	 * @param altura	A altura do da janela que ir� exibir o gr�fico.
	 * 
	 * @return Retorna a janela com o gr�fico a ser exibido.
	 */
	public ChartFrame obterFrameGrafico(int largura, int altura) {
		boolean INCLUIR_SCROLLPANE = true;
		ChartFrame chartFrame = new ChartFrame(tituloGrafico, grafico, INCLUIR_SCROLLPANE);
		chartFrame.setSize(700, 500);
		return chartFrame;
	}
	
	/**
	 * Obt�m o t�tulo associado ao gr�fico.
	 * T�tulo que resume os dados utilizados.
	 * 
	 * @return Retorna o t�tulo do gr�fico.
	 */
	public String getTituloGrafico() {
		return tituloGrafico;
	}

	/**
	 * Insere o t�tulo que est� associado ao conte�do do gr�fico.
	 * T�tulo que resume os dados utilizados.
	 * 
	 * @param tituloGrafico O t�tulo a ser exibido no topo do gr�fico.
	 */
	public void setTituloGrafico(String tituloGrafico) {
		this.tituloGrafico = tituloGrafico;
	}

	/**
	 * Obt�m o t�tulo associado ao eixo X do gr�fico.
	 * 
	 * @return Retorna o t�tulo do eixo X.
	 */
	public String getTituloEixoY() {
		return tituloEixoY;
	}

	/**
	 * Insere o t�tulo que est� associado ao eixo Y do gr�fico.
	 * 
	 * @param tituloEixoY O t�tulo a ser exibido.
	 */
	public void setTituloEixoY(String tituloEixoY) {
		this.tituloEixoY = tituloEixoY;
	}

	/**
	 * Obt�m o t�tulo associado ao eixo Y do gr�fico.
	 * 
	 * @return Retorna o t�tulo do eixo Y.
	 */
	public String getTituloEixoX() {
		return tituloEixoX;
	}

	/**
	 * Insere o t�tulo que est� associado ao eixo X do gr�fico.
	 * 
	 * @param tituloEixoX O t�tulo a ser exibido.
	 */
	public void setTituloEixoX(String tituloEixoX) {
		this.tituloEixoX = tituloEixoX;
	}

	/**
	 * Interface que oferece uma fun��o para extrair o conte�do a ser inserido 
	 * em um dataset a partir de um �nico objeto do tipo <b>T</b>. A partir 
	 * desse objeto ser� extra�do um valor associado a uma linha e coluna 
	 * que ser� defido por quem implementa esta interface. 
	 * <br> 
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 * @param <T> Tipo de objeto que ser� utilizado para extrair os valores 
	 * para um DataSet a ser exibirdo em um gr�fico.
	 */
	@FunctionalInterface
	public static interface GeradorConteudoDataSet<T>{
		/**
		 * Obt�m os dados do objeto <b>T</b> passado por par�metro que 
		 * podem ser inseridos em um dataset. <br><br>
		 * 
		 * Os dados a serem adicionados no objeto {@link ConteudoDataSet} 
		 * a ser retornado devem ser os dados que o usu�rio pretende que 
		 * seja adicionado em um dataset, que ser� exibido por um gr�fico.
		 * <br> 
		 * Caso o objeto passado por par�metro n�o tenha os dados necess�rios a 
		 * serem exibidos deve-se retornar <code>null</code>.
		 * 
		 * @param objetoT O objeto do qual ser� extra�do os dados para um gr�fico.
		 *  
		 * @return Retorna o objeto {@link ConteudoDataSet} que agrupa um valor 
		 * associado a uma linha e coluna de um dataset. 
		 * Retorna <code>null</code> se o objeto passado por par�metro n�o possui 
		 * o dado necess�rio. 
		 */
		public ConteudoDataSet gerarValorDataSet(T objetoT);
	}
	
	/**
	 * Agrupa os dados de um gr�fico que representa apenas
	 * um valor associado a uma linha e a uma coluna do dataset. 
	 * 
	 * Deve ser utilizada para manter o valor que ser� 
	 * adicionado em um gr�fico que utiliza um {@link Dataset} do 
	 * tipo {@link DefaultCategoryDataset}. <br><br>
	 * 
	 * O tipo de Dataset {@link DefaultCategoryDataset} permite adicionar 
	 * os dados valor, chave da linha e chave da coluna, que s�o 
	 * utilizados para construir uma tabela que resultar� em um gr�fico.
	 * 
	 * @see GraficoCategory
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public static class ConteudoDataSet implements Comparable<ConteudoDataSet>{
		
		private Comparable<?> chaveLinha, chaveColuna;
		private Number valor;

		/**
		 * Construtor que permite instanciar o objeto com 
		 * os atributos necess�rios.
		 */
		public ConteudoDataSet() {
			chaveLinha = chaveColuna = "";
			valor = 0;
		}

		/**
		 * Construtor que permite instanciar o objeto adicionando os 
		 * valores que fazem referencia a um valor da tabela constru�da 
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
		 * Obt�m a chave da linha que representa 
		 * uma linha da tabela de dados para um gr�fico.
		 * 
		 * @return Retorna a chave da linha.
		 */
		public Comparable<?> getChaveLinha() {
			return chaveLinha;
		}
		
		/**
		 * Insere a chave da linha que representa 
		 * uma linha da tabela de dados para um gr�fico.
		 *
		 * @param chaveLinha A chave que identifica a linha 
		 * de um gr�fico. 
		 */
		public void setChaveLinha(Comparable<?> chaveLinha) {
			this.chaveLinha = chaveLinha;
		}
		
		/**
		 * Obt�m a chave da coluna que representa 
		 * uma coluna da tabela de dados para um gr�fico.
		 * 
		 * @return Retorna a chave da coluna.
		 */
		public Comparable<?> getChaveColuna() {
			return chaveColuna;
		}
		
		/**
		 * Insere a chave da coluna que representa 
		 * uma coluna da tabela de dados para um gr�fico.
		 *
		 * @param chaveColuna A chave que identifica a coluna 
		 * de um gr�fico. 
		 */
		public void setChaveColuna(Comparable<?> chaveColuna) {
			this.chaveColuna = chaveColuna;
		}
		
		/**
		 * Obt�m o valor associado a linha e coluna 
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
