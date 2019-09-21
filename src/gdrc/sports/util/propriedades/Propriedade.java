package gdrc.sports.util.propriedades;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import gdrc.sports.app.SportsViewerApp;

/**
 * Define as configurações e ícones utilizados pela aplicação.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public interface Propriedade {
	public String NOME_PROGRAMA = "Sports Viewer";
	public String CAMINHO_PACOTE_ICONES = "/gdrc/sports/gui/img/icone/";
	
	public URL URL_ICONE_APLICACAO = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icone_aplicacao.png");
	public URL URL_ICONE_PESQUISA = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icon_pesquisa.png");
	public URL URL_ICONE_PESQUISA_MINI = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icon_pesquisa_mini.png");
	public URL URL_ICONE_EXPORTAR = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icon_exportar.png");
	public URL URL_ICONE_IMPORTAR = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icon_importar.png");
	public URL URL_ICONE_INFO = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icone_info.png");
	public URL URL_ICONE_GRAFICO = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icon_relatorio_grafico.png");
	public URL URL_ICONE_RELATORIO = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "icon_relatorio.png");
	
	public URL URL_LOGO_APLICACAO = SportsViewerApp.class.getResource(CAMINHO_PACOTE_ICONES + "logo_aplicacao.png");
	public Image ICONE_APLICACAO = Toolkit.getDefaultToolkit().getImage(URL_ICONE_APLICACAO);
	
}
