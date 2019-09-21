package gdrc.sports.tipo.grafico;

import org.jfree.chart.ChartFactory;

/**
 * Classe que permite criar um gráfico de coluna a partir 
 * dos dados que devem ser adicionados ao dataset.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class GraficoColuna extends GraficoCategory{
	
	/**
	 * Instancia o objeto definindo as 
	 * configurações padrões do gráfico.
	 */
	public GraficoColuna() {
		super();
	}

	/**
	 * Instancia o objeto com configurações padrões do gráfico
	 * e os títulos utilizados no gráfico.
	 * 
	 * @param tituloGrafico O título principal a ser exibido pelo gráfico, título que 
	 * resume os tipos de dados utilizados pelo gráfico.
	 * @param tituloEixoY O título referente ao dados do eixo y no gráfico.
	 * @param tituloEixoX O título referente ao dados do eixo x no gráfico.
	 */
	public GraficoColuna(String tituloGrafico, String tituloEixoY, String tituloEixoX) {
		super(tituloGrafico, tituloEixoY, tituloEixoX);
	}

	@Override
	public GraficoColuna gerarGrafico() {
		grafico = ChartFactory.createBarChart3D(
				tituloGrafico, 
				tituloEixoX,
				tituloEixoY,
				dataSet,
				ORIENTACAO_GRAFICO,
				INCLUIR_LEGENDA, INCLUIR_TOOLTIPS, INCLUIR_URLS);
		
		preencherDadosVaziosDoDataset();
		aplicarPersonalizacoesAoGrafico();
		return this;
	}
	
}
