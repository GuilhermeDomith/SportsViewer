package gdrc.sports.tipo.grafico;

import org.jfree.chart.ChartFactory;

/**
 * Classe que permite criar um gr�fico de coluna a partir 
 * dos dados que devem ser adicionados ao dataset.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class GraficoColuna extends GraficoCategory{
	
	/**
	 * Instancia o objeto definindo as 
	 * configura��es padr�es do gr�fico.
	 */
	public GraficoColuna() {
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
