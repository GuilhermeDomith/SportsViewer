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
 * Classe utilizada para exportar os dados, relatório, 
 * e relatório gráfco do cliente para um documento PDF.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class RelatorioPDF {
	private File file;
	private PDF documento;
	
	/**
	 * Construtor default que permite instanciar o objeto  
	 * indicando o path name, caminho que indica onde o relatório 
	 * será exportado.
	 * 
	 * @param pathName 		Caminho que indica onde o relatório 
	 * será exportado.
	 * 
	 * @throws IOException Dispara a execeção caso não seja
	 * possível localizar o caminho passado por parâmetro ou 
	 * o arquivo não pode ser aberto. 
	 */
	public RelatorioPDF(String pathName) throws IOException{
		this(new File(pathName));
	}
	
	/**
	 * Construtor default que permite instanciar o objeto com 
	 * a arquivo no qual será exportado o relatório.
	 * 
	 * @param file 		Arquivo que mantém o pathname do arquivo no qual 
	 * o relaório será exportado.  
	 * 
	 *  @throws IOException Dispara a execeção caso não seja
	 * possível localizar o caminho passado por parâmetro ou 
	 * o arquivo não pode ser aberto. 
	 */
	public RelatorioPDF(File file) throws IOException {
		try {
			 this.file = file;
			 this.documento = new PDF(file);
			 this.documento.abrir();
			 
		}catch (DocumentException e) {
			throw new IOException(String.format("O arquivo '%s'não pôde ser aberto.", 
					file.getAbsolutePath()));
		}catch (FileNotFoundException e) {
			throw new IOException(String.format("O arquivo '%s'não foi encontrado.", 
												file.getAbsolutePath()));
		}
	}
	
	/**
	 * Escreve o cabeçalho do documento PDF com os dados da aplicação e os dados do cliente 
	 * passado por parâmetro.
	 * 
	 * @param cliente		O cliente referente ao relatório que está sendo exportado.
	 * @param dataEHora		<code>String</code> contendo a data e hora que será escrita no 
	 * 						cabeçalho.
	 * 
	 * @return Retorna <code>true</code> se o cabeçalho for escrito corretamente.
	 * 
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
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
			throw new IOException("Erro ao escrever cabeçalho do relatório no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve as informações do cliente passado por parâmetro no documento PDF. 
	 * 
	 * @param cliente		O cliente referente ao relatório que está sendo exportado.
	 * 
	 * @return Retorna <code>true</code> se as informações do cliente forem escritas
	 * corretamente.
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverInformacaoCliente(Cliente cliente) throws IOException {
		try {
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_INFO, 25, 25);
			documento.addTitulo("Informações do Cliente", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);

			documento.addParagrafo(String.format("Nome: %s", cliente.getNome()));
			documento.addLink("Email: ",cliente.getEmail(), "mailto:"+cliente.getEmail());
			documento.addParagrafo(String.format("Data de Nascimento: %s", cliente.getDataNascimento()));
			documento.addParagrafo(String.format("Sexo: %s", cliente.getSexo()));
			documento.addParagrafo(String.format("Altura: %.2f m", cliente.getAltura()));
			documento.addParagrafo(String.format("Sexo: %.2f Kg", cliente.getPeso()));

			documento.addNovasLinhas(2);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever informações do cliente no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve o relatório do cliente, que exibe os exercícios com maior destaque, 
	 * no documento PDF.
	 * 
	 * @param relatorio		O relatório do cliente a ser exportado.
	 * 
	 * @return Retorna <code>true</code> se o relatório for escrito corretamente.
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioCliente(RelatorioCliente relatorio) throws IOException{
		try {
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_RELATORIO, 25, 25);
			documento.addTitulo("Relatório do Cliente", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);
			
			Exercicio exc;
			documento.addParagrafo("Exercício com maior duração:");
			exc = relatorio.getExercicioMaiorDuracao();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.DURACAO));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exercício com maior distância percorrida:");
			exc = relatorio.getExercicioMaiorDistancia();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.DISTANCIA));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exercício com maior perda de calorias:");
			exc = relatorio.getExercicioMaiorPerdaCalorias();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.CALORIAS));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exercício com maior número de passos:");
			exc = relatorio.getExercicioMaiorNumPassos();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.PASSOS));

			documento.addNovasLinhas(1);
			documento.addParagrafo("Exercício com maior velocidade atingida:");
			exc = relatorio.getExercicioMaiorVelocidade();
			documento.addParagrafo(RelatorioCliente.formatarDadosRelatorio(exc, TipoDadoRelatorio.VELOCIDADE));

			documento.addNovasLinhas(2);
		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever relatório do cliente no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve o relatório gráfico de colunas no documento PDF. Escreve os gráficos para 
	 * os dados: Duração, Distância percorrida, Calorias perdidas, Passos dados, Ritmo médio e 
	 * Velocidade média. 
	 * 
	 * @param relatorio		O relatório gráfico que será utilizado para obter os gráficos 
	 * necessários.
	 * 
	 * @return Retorna <code>true</code> se o relatório for escrito corretamente.
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioGraficoColuna(RelatorioGrafico relatorio) throws IOException{
		try {
			relatorio.setTipoGrafico(TipoGrafico.COLUNA);
			relatorio.setSepararExercicios(true);

			documento.criarNovaPagina();
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_GRAFICO, 25, 25);
			documento.addTitulo("Relatório Gráfico de Coluna", 15, Element.ALIGN_LEFT, icone);
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
			throw new IOException("Erro ao escrever relatório gráfico de colunas no PDF: "+file.getName());
		}
		return true;
	}
	
	/**
	 * Escreve o relatório gráfico de linhas no documento PDF. Escreve os gráficos para 
	 * os dados: Distância percorrida, Calorias perdidas, Passos dados.
	 * 
	 * @param relatorio		O relatório gráfico que será utilizado para obter os gráficos 
	 * necessários.
	 * 
	 * @return Retorna <code>true</code> se o relatório for escrito corretamente.
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioGraficoLinha(RelatorioGrafico relatorio) throws IOException {
		try {
			relatorio.setTipoGrafico(TipoGrafico.LINHA);
			relatorio.setSepararExercicios(true);

			documento.criarNovaPagina();
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_GRAFICO, 25, 25);
			documento.addTitulo("Relatório Gráfico de Linha", 15, Element.ALIGN_LEFT, icone);
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoDistanciaPorDia());
			documento.addNovasLinhas(1);

			escreverGrafico(relatorio.graficoCaloriaPorDia());		
			documento.addNovasLinhas(1);

			documento.criarNovaPagina();
			escreverGrafico(relatorio.graficoPassosPorDia());		
			documento.addNovasLinhas(1);

		} catch (DocumentException | IOException e) {
			throw new IOException("Erro ao escrever relatório gráfico de linhas no PDF: "+file.getName());
		}
		
		return true;
	}

	/**
	 * Escreve o relatório gráfico de colunas geral no documento PDF. Escreve os gráficos para 
	 * os dados: Passos dados, Média da distância percorrida, Distância percorrida, 
	 * Média das calorias perdidas, Calorias perdidas, Ritmo médio e Velocidade média. 
	 * 
	 * @param relatorio		O relatório gráfico que será utilizado para obter os gráficos 
	 * necessários.
	 * 
	 * @return Retorna <code>true</code> se o relatório for escrito corretamente.
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
	 * no documento PDF. 
	 */
	public boolean escreverRelatorioGraficoColunaGeral(RelatorioGrafico relatorio) throws IOException {
		try {
			relatorio.setTipoGrafico(TipoGrafico.COLUNA);
			relatorio.setSepararExercicios(false);
			relatorio.setFiltrarExercicio(null);

			documento.criarNovaPagina();
			Image icone = PDF.criarImagemPNG(Propriedade.URL_ICONE_GRAFICO, 25, 25);
			documento.addTitulo("Relatório Gráfico de Coluna Geral", 15, Element.ALIGN_LEFT, icone);
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
			throw new IOException("Erro ao escrever relatório gráfico de colunas geral no PDF: "+file.getName());
		}
		return true;
	}
	
	private void escreverGrafico(GraficoCategory grafico) throws IOException, DocumentException {
		byte[] imagemGrafico = grafico.obterImagemGraficoJPEG(500, 320);
		if(imagemGrafico != null) documento.addImagemJpeg(imagemGrafico, 500, 320);
	}

	/**
	 * Fecha o documento PDF utilizado para gravar o relatório.
	 * 
	 * @return Retorna <code>true</code> se o documento foi fechado 
	 * com sucesso.
	 * @throws IOException Dispara a exceção caso ocorra erro ao fechar 
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
