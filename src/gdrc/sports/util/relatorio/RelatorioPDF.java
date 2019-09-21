package gdrc.sports.util.relatorio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;

import gdrc.sports.io.pdf.PDF;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.grafico.GraficoCategory;
import gdrc.sports.util.propriedades.Propriedade;
import gdrc.sports.util.relatorio.RelatorioCliente.TipoDadoRelatorio;
import gdrc.sports.util.relatorio.RelatorioGrafico.TipoGrafico;

/**
 * Classe utilizada para exportar os dados, relat�rio, 
 * e relat�rio gr�fco do cliente para um documento PDF.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RelatorioPDF {
	private File file;
	private PDF documento;
	
	/**
	 * Construtor default que permite instanciar o objeto  
	 * indicando o path name, caminho que indica onde o relat�rio 
	 * ser� exportado.
	 * 
	 * @param pathName 		Caminho que indica onde o relat�rio 
	 * ser� exportado.
	 * 
	 * @throws IOException Dispara a exece��o caso n�o seja
	 * poss�vel localizar o caminho passado por par�metro ou 
	 * o arquivo n�o pode ser aberto. 
	 */
	public RelatorioPDF(String pathName) throws IOException{
		this(new File(pathName));
	}
	
	/**
	 * Construtor default que permite instanciar o objeto com 
	 * a arquivo no qual ser� exportado o relat�rio.
	 * 
	 * @param file 		Arquivo que mant�m o pathname do arquivo no qual 
	 * o rela�rio ser� exportado.  
	 * 
	 *  @throws IOException Dispara a exece��o caso n�o seja
	 * poss�vel localizar o caminho passado por par�metro ou 
	 * o arquivo n�o pode ser aberto. 
	 */
	public RelatorioPDF(File file) throws IOException {
		try {
			 this.file = file;
			 this.documento = new PDF(file);
			 this.documento.abrir();
			 
		}catch (DocumentException e) {
			throw new IOException(String.format("O arquivo '%s'n�o p�de ser aberto.", 
					file.getAbsolutePath()));
		}catch (FileNotFoundException e) {
			throw new IOException(String.format("O arquivo '%s'n�o foi encontrado.", 
												file.getAbsolutePath()));
		}
	}
	
	/**
	 * Escreve o cabe�alho do documento PDF com os dados da aplica��o e os dados do cliente 
	 * passado por par�metro.
	 * 
	 * @param cliente		O cliente referente ao relat�rio que est� sendo exportado.
	 * @param dataEHora		<code>String</code> contendo a data e hora que ser� escrita no 
	 * 						cabe�alho.
	 * 
	 * @return Retorna <code>true</code> se o cabe�alho for escrito corretamente.
	 * 
	 * @throws IOException Dispara a exce��o caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverCabecalho(Cliente cliente, String dataEHora) throws IOException {
		try {
			Image icone = PDF.criarImagemPNG(Propriedade.URL_LOGO_APLICACAO, 70, 70);
			documento.addTitulo(Propriedade.NOME_PROGRAMA, 45, Element.ALIGN_CENTER, icone);
			documento.addParagrafo("");

			documento.addTitulo(cliente.getNome(), 15, Element.ALIGN_RIGHT, null);
			documento.addTitulo(cliente.getEmail(), 13, Element.ALIGN_RIGHT, null);
			documento.addTitulo("Criado em: " + dataEHora, 13, Element.ALIGN_RIGHT, null);
			documento.addNovasLinhas(1);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever cabe�alho do relat�rio no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve as informa��es do cliente passado por par�metro no documento PDF. 
	 * 
	 * @param cliente		O cliente referente ao relat�rio que est� sendo exportado.
	 * 
	 * @return Retorna <code>true</code> se as informa��es do cliente forem escritas
	 * corretamente.
	 * @throws IOException Dispara a exce��o caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverInformacaoCliente(Cliente cliente) throws IOException {
		try {
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_INFO, 25, 25);
			documento.addTitulo("Informa��es do Cliente", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);

			documento.addParagrafo(String.format("Nome: %s", cliente.getNome()));
			documento.addLink("Email: ",cliente.getEmail(), "mailto:"+cliente.getEmail());
			documento.addParagrafo(String.format("Data de Nascimento: %s", cliente.getDataNascimento()));
			documento.addParagrafo(String.format("Sexo: %s", cliente.getSexo()));
			documento.addParagrafo(String.format("Altura: %.2f m", cliente.getAltura()));
			documento.addParagrafo(String.format("Sexo: %.2f Kg", cliente.getPeso()));

			documento.addNovasLinhas(2);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever informa��es do cliente no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve o relat�rio do cliente, que exibe os exerc�cios com maior destaque, 
	 * no documento PDF.
	 * 
	 * @param relatorio		O relat�rio do cliente a ser exportado.
	 * 
	 * @return Retorna <code>true</code> se o relat�rio for escrito corretamente.
	 * @throws IOException Dispara a exce��o caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioCliente(RelatorioCliente relatorio) throws IOException{
		try {
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_RELATORIO, 25, 25);
			documento.addTitulo("Relat�rio do Cliente", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);
			
			Exercicio exc;
			documento.addParagrafo("Exerc�cio com maior dura��o:");
			exc = relatorio.getExercicioMaiorDuracao();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.DURACAO));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exerc�cio com maior dist�ncia percorrida:");
			exc = relatorio.getExercicioMaiorDistancia();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.DISTANCIA));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exerc�cio com maior perda de calorias:");
			exc = relatorio.getExercicioMaiorPerdaCalorias();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.CALORIAS));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exerc�cio com maior n�mero de passos:");
			exc = relatorio.getExercicioMaiorNumPassos();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.PASSOS));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exerc�cio com maior velocidade atingida:");
			exc = relatorio.getExercicioMaiorVelocidade();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.VELOCIDADE));

			documento.addNovasLinhas(2);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever relat�rio do cliente no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve o relat�rio gr�fico de colunas no documento PDF. Escreve os gr�ficos para 
	 * os dados: Dura��o, Dist�ncia percorrida, Calorias perdidas, Passos dados, Ritmo m�dio e 
	 * Velocidade m�dia. 
	 * 
	 * @param relatorio		O relat�rio gr�fico que ser� utilizado para obter os gr�ficos 
	 * necess�rios.
	 * 
	 * @return Retorna <code>true</code> se o relat�rio for escrito corretamente.
	 * @throws IOException Dispara a exce��o caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioGraficoColuna(RelatorioGrafico relatorio) throws IOException{
		try {
			relatorio.setTipoGrafico(TipoGrafico.COLUNA);
			relatorio.setSepararExercicios(true);

			documento.criarNovaPagina();
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_GRAFICO, 25, 25);
			documento.addTitulo("Relat�rio Gr�fico de Coluna", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoDuracaoPorDia());		
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoDistanciaPorDia());
			documento.addNovasLinhas(1);

			documento.criarNovaPagina();
			escreverGrafico(relatorio.graficoCaloriaPorDia());		
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoPassosPorDia());		
			documento.addNovasLinhas(1);

			documento.criarNovaPagina();
			escreverGrafico(relatorio.graficoRitmoMedioPorDia());		
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoVelocidadeMediaPorDia());		
			documento.addNovasLinhas(2);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever relat�rio gr�fico de colunas no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve o relat�rio gr�fico de linhas no documento PDF. Escreve os gr�ficos para 
	 * os dados: Dist�ncia percorrida, Calorias perdidas, Passos dados.
	 * 
	 * @param relatorio		O relat�rio gr�fico que ser� utilizado para obter os gr�ficos 
	 * necess�rios.
	 * 
	 * @return Retorna <code>true</code> se o relat�rio for escrito corretamente.
	 * @throws IOException Dispara a exce��o caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioGraficoLinha(RelatorioGrafico relatorio) throws IOException {
		try {
			relatorio.setTipoGrafico(TipoGrafico.LINHA);
			relatorio.setSepararExercicios(true);

			documento.criarNovaPagina();
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_GRAFICO, 25, 25);
			documento.addTitulo("Relat�rio Gr�fico de Linha", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoDistanciaPorDia());
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoCaloriaPorDia());		
			documento.addNovasLinhas(1);

			documento.criarNovaPagina();
			escreverGrafico(relatorio.graficoPassosPorDia());		
			documento.addNovasLinhas(1);

		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever relat�rio gr�fico de linhas no PDF: "+file.getName());
		}
		
		return true;
	}

	/**
	 * Escreve o relat�rio gr�fico de colunas geral no documento PDF. Escreve os gr�ficos para 
	 * os dados: Passos dados, M�dia da dist�ncia percorrida, Dist�ncia percorrida, 
	 * M�dia das calorias perdidas, Calorias perdidas, Ritmo m�dio e Velocidade m�dia. 
	 * 
	 * @param relatorio		O relat�rio gr�fico que ser� utilizado para obter os gr�ficos 
	 * necess�rios.
	 * 
	 * @return Retorna <code>true</code> se o relat�rio for escrito corretamente.
	 * @throws IOException Dispara a exce��o caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioGraficoColunaGeral(RelatorioGrafico relatorio) throws IOException {
		try {
			relatorio.setTipoGrafico(TipoGrafico.COLUNA);
			relatorio.setSepararExercicios(false);
			relatorio.setFiltrarExercicio(null);

			documento.criarNovaPagina();
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_GRAFICO, 25, 25);
			documento.addTitulo("Relat�rio Gr�fico de Coluna Geral", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoPassosPorDia());		
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoMediaDistanciaPorDia());
			documento.addNovasLinhas(1);

			documento.criarNovaPagina();
			escreverGrafico(relatorio.graficoDistanciaPorDia());		
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoMediaCaloriaPorDia());		
			documento.addNovasLinhas(1);

			documento.criarNovaPagina();
			escreverGrafico(relatorio.graficoCaloriaPorDia());		
			documento.addNovasLinhas(1);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever relat�rio gr�fico de colunas geral no PDF: "+file.getName());
		}
		return true;
	}
	
	private void escreverGrafico(GraficoCategory grafico) throws IOException, DocumentException {
		byte[] imagemGrafico = grafico.obterImagemGraficoJPEG(500, 320);
		if(imagemGrafico != null) documento.addImagemJpeg(imagemGrafico, 500, 320);
	}

	/**
	 * Fecha o documento PDF utilizado para gravar o relat�rio.
	 * 
	 * @return Retorna <code>true</code> se o documento foi fechado 
	 * com sucesso.
	 * @throws IOException Dispara a exce��o caso ocorra erro ao fechar 
	 * o documento PDF. 
	 */
	public boolean fechar() throws IOException {
		try { if(documento != null) documento.fechar();
		} catch (IOException e) {
			throw new IOException("Erro ao fechar documento PDF: "+file.getName());
		}
		return true;
	}
}
