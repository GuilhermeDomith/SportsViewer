package gdrc.sports.tipo.grafico;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;

/**
 * Classe que permite criar um gr�fico de linha a partir 
 * dos dados que devem ser adicionados ao dataset.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class GraficoLinha extends GraficoCategory{

	/**
	 * Instancia o objeto definindo as 
	 * configura��es padr�es do gr�fico.
	 */
	public GraficoLinha() {
		super();
	}

	/**
	 * Instancia o objeto com configura��es padr�es do gr�fico
	 * e os t�tulos utilizados no gr�fico.
	 * 
	 * @param tituloGrafico O t�tulo principal a ser exibido pelo gr�fico, t�tulo que 
	 * resume os tipos de dados utilizados pelo gr�fico.
	 * @param tituloEixoY O t�tulo referente ao dados do eixo y no gr�fico.
	 * @param tituloEixoX O t�tulo referente ao dados do eixo x no gr�fico.
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
		// Modifica o renderer se n�o for do tipo LineRenderer3D.
		if(renderer instanceof LineAndShapeRenderer) {
			LineAndShapeRenderer rendererLineAndShape = (LineAndShapeRenderer) renderer;
			// Gerar formas aleat�rias para o pontos que ligam as linhas.
			rendererLineAndShape.setAutoPopulateSeriesShape(true);
			// Exibir os pontos que ligam as linhas;
			rendererLineAndShape.setBaseShapesVisible(true);
			// Utilizar apenas o losango de tamanho 4.5 para exibir os pontos.
			//renderer.setBaseShape(ShapeUtilities.createDiamond(4.5f));
			grafico.getCategoryPlot().setRenderer(renderer);
		}
	}
}
