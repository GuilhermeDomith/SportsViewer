package gdrc.sports.io.pdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Jpeg;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.codec.PngImage;

/**
 * Esta classe permite criar e escrever um documento do tipo PDF. <br>
 * O conteúdo do documento pode ser escrito utilizando os métodos 
 * fornecidos pela classe que permitem adicionar varios tipo de 
 * elementos em sequência. <br><br> 
 * 
 * Para que a escrita no documento possa ser feita deve-se 
 * primeiramente abrir o arquivo atraves do método fornecido.
 * E fecha-lo ao término da escrita para liberar os recursos
 * do sistema. 
 *  
 * @author Guilherme Domith Ribeiro Coelho
 */
public class PDF {
	private File arquivo;
	private OutputStream outputStream;
	private Document documentoPDF;
	
	/**
	 * Construtor que permite instanciar o objeto  
	 * indicando o path name, caminho que indica onde documento PDF 
	 * será escrito.
	 * 
	 * @param pathName 		Caminho que indica onde o documento 
	 * PDF será escrito.
	 */
	public PDF(String pathName) {
		this(new File(pathName));
	}
	
	/**
	 * Construtor que permite instanciar o objeto com 
	 * a referencia do arquivo no qual o documento PDF será escrito.
	 * 
	 * @param arquivo Arquivo que mantém o pathname do arquivo no qual 
	 * o documento PDF será escrito.  
	 */
	public PDF(File arquivo) {
		this.arquivo = arquivo;
		documentoPDF = new Document(PageSize.A4);
	}
	
	/**
	 * Abre o documento PDF para que possa fazer a escrita.
	 *  
	 * @return Retorna <code>true</code> se o documento foi aberto com sucesso.
	 * 
	 * @throws FileNotFoundException Dispara a execeção caso não seja
	 * possível localizar o arquivo passado no construtor da classe.
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao
	 * abrir o documento PDF.
	 */
	public boolean abrir() throws FileNotFoundException, DocumentException {
		outputStream = new FileOutputStream(arquivo);
		PdfWriter.getInstance(documentoPDF, outputStream);
		documentoPDF.open();
		return true;
	}
	
	/**
	 * Permite adicionar um parágrafo ao PDF.
	 * 
	 * @param paragrafo 	O texto a ser adicionado como parágrafo.
	 * 
	 * @return Retorna <code>true</code> se o parágrafo foi escrito com sucesso.
	 * 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao
	 * escrever o elemento no documento PDF.
	 */
	public boolean addParagrafo(String paragrafo) throws DocumentException{
        Paragraph p = new Paragraph(paragrafo);
		return documentoPDF.add(p);
	}
	
	/**
	 * Permite adicionar uma imagem do formato JPEG, através dos seus bytes, com o tamanho 
	 * especificado por parâmetro.
	 *
	 * @param bytesImagem 	Os bytes da imagem a ser inserida no documento.
	 * @param largura		Largura que a imagem irá ocupar no PDF.
	 * @param altura		Altura que a imagem irá ocupar no PDF.
	 * 
	 * @return Retorna <code>true</code> se a imagem foi adicionada 
	 * com sucesso.
	 * 
	 * @throws IOException Dispara a execeção caso ocorra um erro ao escrever 
	 * a imagem em arquivo. 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao 
	 * adicionar a imagem. Verifique se o formato da imagem realmente é
	 * JPEG.
	 */
	public boolean addImagemJpeg(byte[] bytesImagem, float largura, float altura) 
									throws IOException, DocumentException{
		Jpeg imagem = new Jpeg(bytesImagem, largura, altura);
		imagem.scaleAbsoluteWidth(largura);
		imagem.scaleAbsoluteHeight(altura);
		imagem.setAlignment(Jpeg.ALIGN_CENTER);
		return documentoPDF.add(imagem);
	}
	
	/**
	 * Permite adicionar uma imagem do formato JPEG com o tamanho especificado 
	 * por parâmetro.
	 * 
	 * @param urlImagem 	url que referencia a imagem no sistema de arquivos.
	 * @param largura		Largura que a imagem irá ocupar no PDF.
	 * @param altura		Altura que a imagem irá ocupar no PDF.
	 * 
	 * @return Retorna <code>true</code> se a imagem foi adicionada 
	 * com sucesso.
	 * 
	 * @throws IOException Dispara a exceção caso ocorra um erro ao escrever 
	 * a imagem em arquivo. 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao 
	 * adicionar a imagem. Verifique se o formato da imagem realmente é
	 * JPEG.
	 */
	public boolean addImagemJpeg(URL urlImagem, float largura, float altura) 
									throws IOException, DocumentException{
		Jpeg imagem = new Jpeg(urlImagem);
		imagem.scaleAbsoluteWidth(largura);
		imagem.scaleAbsoluteHeight(altura);
		imagem.setAlignment(Jpeg.ALIGN_CENTER);
		imagem.setBorder(1);
		return documentoPDF.add(imagem);
	}
	
	/**
	 * Permite adicionar uma imagem do formato PNG com o tamanho especificado 
	 * por parâmetro.
	 * 
	 * @param urlImagem 	url que referencia a imagem no sistema de arquivos.
	 * @param largura		Largura que a imagem irá ocupar no PDF.
	 * @param altura		Altura que a imagem irá ocupar no PDF.
	 * 
	 * @return  Retorna <code>true</code> se a imagem foi adicionada 
	 * com sucesso.
	 * 
	 * @throws IOException Dispara a execeção caso ocorra um erro ao escrever 
	 * a imagem em arquivo. 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao 
	 * adicionar a imagem. Verifique se o formato da imagem realmente é
	 * PNG.
	 */
	public boolean addImagemPng(URL urlImagem, float largura, float altura)
									throws IOException, DocumentException{
		Image imagem = criarImagemPNG(urlImagem, largura, altura);
		imagem.setAlignment(Element.ALIGN_CENTER);
		
		return documentoPDF.add(imagem);
	}
	
	/**
	 * Permite adicionar um título já formatado ao PDF. O título será formatado 
	 * em negrito para se destacar dos demais elementos. O tamanho, o alinhamento 
	 * horizontal e um ícone associado ao título pode ser adicionado por meio dos 
	 * parâmetros.
	 * 
	 * @param titulo 		O texto a ser adicionado como título.
	 * @param fontSize		O tamanho da fonte do título.
	 * @param alinhamento 	A constante que indica qual será o alinhamento 
	 * horizontal do título. Pode ser {@link Element#ALIGN_CENTER} por exemplo.
	 * @param icone 		O ícone associado ao título caso necessário. 
	 * Pode ser <code>null</code>.
	 * 
	 * @return Retorna <code>true</code> se o título foi escrito com sucesso.
	 * 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao
	 * adicionar o título no documento PDF.
	 */
	public boolean addTitulo(String titulo, float fontSize, int alinhamento, Image icone) throws DocumentException{
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80f);
        table.setWidths(new int[] {1,4});
        table.setHorizontalAlignment(alinhamento);
        
        // Se o alinhamento for para esquerda o ícone terá um espaço menor.
    	if(alinhamento == Element.ALIGN_LEFT)
    		table.setWidths(new int[] {1,9});
        
        PdfPCell iconeCell = new PdfPCell();
        if(icone != null) { iconeCell.addElement(icone); }
    	iconeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        iconeCell.setBorder(0);
        table.addCell(iconeCell);
        
        Paragraph paragrafo = new Paragraph(titulo);
        paragrafo.getFont().setSize(fontSize);
        paragrafo.getFont().setStyle(Font.BOLD);
        paragrafo.setAlignment(alinhamento);
        
        PdfPCell tituloCell = new PdfPCell(paragrafo);
        tituloCell.setBorder(0);
        tituloCell.setHorizontalAlignment(alinhamento);
        table.addCell(tituloCell);
        table.setSpacingAfter(5);
        
		return documentoPDF.add(table);
	}
	
	/**
	 * Cria uma imagem do tipo PNG que pode ser diretamente adicionada a um documento PDF.<br>  
	 * A imagem criada é uma {@link Image} da API Itext, e também um {@link Element}, por 
	 * isso o objeto pode ser diretamente inserido no PDF.
	 * 
	 * @param urlImagem 	url que referencia a imagem no sistema de arquivos.
	 * @param largura		Largura que a imagem deve ocupar no PDF quando adicionada.
	 * @param altura		Altura que a imagem deve ocupar no PDF quando adicionada.
	 * 
	 * @return Retorna o objeto {@link Image} do tipo PNG criado.
	 * 
	 * @throws IOException Dispara a exceção caso um erro ao criar a imagem.
	 */
	public static Image criarImagemPNG(URL urlImagem, float largura, float altura) throws IOException {
		Image imagem = PngImage.getImage(urlImagem);
		imagem.scaleAbsoluteWidth(largura);
		imagem.scaleAbsoluteHeight(altura);
		return imagem;
	}
	
	/**
	 * Permite adicionar ao PDF um link referenciado pelo texto 
	 * que é passado por parâmetro.
	 * 
	 * @param descricao 	A descrião que será adicionado antes do link.
	 * @param textoLink 	O texto a será exibido, que irá referenciar o link.
	 * @param link 			O link que está associado ao texto informado.
	 * 
	 * @return Retorna <code>true</code> se o link foi escrito com sucesso.
	 * 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao
	 * escrever o elemento no documento PDF.
	 */
	public boolean addLink(String descricao, String textoLink, String link) throws DocumentException {
		documentoPDF.add(new Phrase(descricao));
		
		Anchor anchor = new Anchor(textoLink);
		anchor.setReference(link);
		anchor.getFont().setColor(BaseColor.BLUE);
		anchor.getFont().setStyle(Font.UNDERLINE);
		return documentoPDF.add(anchor);
	}
	
	/**
	 * Permite adicionar um espaçamento horizontal ao PDF, adicionando 
	 * novas linhas após o último elemento adicionado ao PDF.
	 * 
	 * @param numeroLinhas O número de linhas a ser inserido após o último
	 * elemento adicionado.
	 * 
	 * @return Retorna <code>true</code> se as linhas foram inseridas 
	 * com sucesso.
	 * 
	 * @throws DocumentException Dispara a exceção caso ocorra um erro ao
	 * escrever as linhas no documento PDF.
	 */
	public boolean addNovasLinhas(float numeroLinhas) throws DocumentException{
		for(int i=0; i < numeroLinhas; i++)
			documentoPDF.add(Chunk.NEWLINE);
		return true;
	}
	
	/**
	 * Fecha o documento PDF e todas as suas dependências 
	 * utilizadas para fazer a escrita.
	 * 
	 * @return  Retorna <code>true</code> se o documento foi
	 * fechado corretamente.
	 * 
	 * @throws IOException Dispara a exceção caso o documento 
	 * não seja fechado corretamente.
	 */
	public boolean fechar() throws IOException {
		if(documentoPDF != null) documentoPDF.close();
		if(outputStream != null) outputStream.close();
		return true;
	}

	/**
	 * Cria uma nova página após o último elemento adicionado 
	 * no documento PDF.
	 * 
	 * @return Retorna <code>true</code> se a nova página foi 
	 * criada.
	 * 
	 * @throws DocumentException Dispara a exceção caso ocorra 
	 * um erro ao criar a nova página. 
	 */
	public boolean criarNovaPagina() throws DocumentException {
		return documentoPDF.add(Chunk.NEXTPAGE);
	}

}
