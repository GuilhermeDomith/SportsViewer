package gdrc.sports.tipo.grafico;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

/**
 * Classe que permite criar um gráfico de linha a partir 
 * dos dados que devem ser adicionados ao dataset.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class GraficoLinha extends GraficoCategory{

	/**
	 * Instancia o objeto definindo as 
	 * configurações padrões do gráfico.
	 */
	public GraficoLinha() {
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
	public GraficoLinha(String tituloGrafico, String tituloEixoY, String tituloEixoX) {
		super(tituloGrafico, tituloEixoY, tituloEixoX);
	}

	@Override
	public GraficoLinha gerarGrafico() {
		grafico = ChartFactory.createLineChart(
					tituloGrafico, 
					tituloEixoX,
					tituloEixoY,
					dataSet,
					ORIENTACAO_GRAFICO,
					INCLUIR_LEGENDA, INCLUIR_TOOLTIPS, INCLUIR_URLS);
		
		aplicarPersonalizacoesAoGrafico();
		return this;
	}
	
	@Override
	protected void aplicarPersonalizacoesAoGrafico() {
		super.aplicarPersonalizacoesAoGrafico();
		
		CategoryItemRenderer renderer = grafico.getCategoryPlot().getRenderer();
		// Modifica o renderer se não for do tipo LineRenderer3D.
		if(renderer instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer rendererLineAndShape = (LineAndShapeRenderer) renderer;
			// Gerar formas aleatórias para o pontos que ligam as linhas.
			rendererLineAndShape.setAutoPopulateSeriesShape(true);
			// Exibir os pontos que ligam as linhas;
			rendererLineAndShape.setBaseShapesVisible(true);
			// Utilizar apenas o losango de tamanho 4.5 para exibir os pontos.
			//renderer.setBaseShape(ShapeUtilities.createDiamond(4.5f));
			grafico.getCategoryPlot().setRenderer(renderer);
		}
	}
}
