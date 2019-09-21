package gdrc.sports.io.arquivo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Cont�m os m�todos necess�rios para fazer o acesso e manipula��o dos
 * dados armazenados em um arquivo texto.<br><br>
 * 
 * A leitura e escrita � feita de forma sequencial, portanto sempre que 
 * for necess�rio ler ou escrever a partir do inc�cio do arquivo � 
 * necess�rio abrir o arquivo novamente, ou utilizar o m�todo 
 * {@link #reabrirArquivo()} caso j� tenha sido aberto anteriormente.
 * <br><br>
 * Caso n�o seja feito, as opera��es de leitura e escrita acontecer�o 
 * a partir do �ltimo ponto lido no arquivo.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ArquivoTexto{
	
	private String pathArquivo;
	private Scanner scanner;
	private Formatter formatter;
	
	/**
	 * Cria um arquivo com o nome que � passado por par�metro contendo 
	 * o nome do arquivo e o caminho.
	 *  
	 * @param path O caminho do diret�rio onde o arquivo ser� criado junto do
	 * nome do mesmo.
	 * 
	 * @return Retorna <code>true</code> se o arquivo foi criado corretamente.
	 * @throws FileNotFoundException Dispara a exece��o se ocorreu um erro ao
	 * criar o arquivo no caminho indicado.
	 */
	public boolean criarArquivo(String path) throws FileNotFoundException {
		formatter = new Formatter(path);
		pathArquivo = path;
		return true;
	}
	
	/**
	 * Abre o arquivo localizado no caminho passado por par�metro.
	 *  
	 * @param path O caminho do arquivo indicando onde est� armazenado 
	 * no sistema de arquivos.
	 * 
	 * @return Retorna <code>true</code> se o arquivo foi aberto corretamente.
	 * @throws FileNotFoundException Dispara a exece��o se ocorreu um erro ao
	 * abrir o arquivo no caminho indicado.
	 */
	public boolean abrirArquivo(String path) throws FileNotFoundException {
		scanner = new Scanner(new FileInputStream(path));
		pathArquivo = path;
		return true;
	}
	
	/**
	 * L� o conte�do do arquivo a partir do �ltimo ponto lido. Caso nenhuma 
	 * opera��o de leitura tenha sido feita anteriormente, o conte�do lido 
	 * ser� todo o conte�do armazenado no arquivo, do in�cio ao fim.
	 * 
	 * @return Retorna o conte�do lido do arquivo.
	 */
	public String lerConteudo() {
		StringBuilder buffer = new StringBuilder();
		while(scanner.hasNextLine())
			buffer.append(scanner.nextLine()).append('\n');
		
		return buffer.toString();
	}
	
	/**
	 * Escreve o conte�do passado por par�metro no arquivo. O conte�do 
	 * ser� escrito no local em que a �ltima leitura foi feita. 
	 * 
	 * @param conteudo Conte�do a ser escrito no arquivo.
	 * @return Retorna <code>true</code> se a escrita foi feita corretamente.
	 */
	public boolean escreverConteudo(String conteudo) {
		formatter.format("%s\n", conteudo);
		return true;
	}
	
	/**
	 * Procura no arquivo o identificador passado por par�metro e obt�m o 
	 * conte�do da mesma linha ap�s o mesmo, caso seja encontrado.
	 * 
	 * @param identificador O identificador a ser procurado no arquivo.
	 * 
	 * @return Retorna o cont�udo ap�s o identificador encontrado. 
	 * Caso n�o encontre o identificador retorna <code>null</code>.
	 */
	public String buscarConteudoIdentificador(String identificador) {
		Pattern pattern = Pattern.compile(identificador + ": ");
		
		while(scanner.hasNextLine())
			if(scanner.findInLine(pattern) == null) {
				// Pula para a pr�xima linha como n�o encontrou.
				scanner.nextLine();
			}else{/* Como o ponteiro do arquivo parou no identificador encontrado,
				 * retorna o conte�do restante da linha, ou seja o conteudo do
				 * identificador. Caso n�o tenha nada em frente a ele retorna null. 
				 */
				return (scanner.hasNextLine())? scanner.nextLine() : null;
			}
		
		return null;
	}
	
	/**
	 * Reabre o arquivo para que uma poss�vel leitura feita 
	 * posteriormente comece a partir do inc�cio do arquivo.
	 *  
	 * @return Retorna <code>true</code> se o arquivo foi 
	 * reaberto corretamente.
	 * 
	 * @throws IOException Dispara a exece��o caso ocorra um erro 
	 * ao reabrir arquivo. 
	 */
	public boolean reabrirArquivo() throws IOException {
		fechar();
		abrirArquivo(pathArquivo);
		return true;
	}

	/**
	 * Fecha o arquivos.
	 *  
	 * @return Retorna <code>true</code> se o arquivo foi 
	 * fechado corretamente.
	 * 
	 * @throws IOException Dispara a exece��o caso ocorra um erro 
	 * ao fechar o arquivo. 
	 */
	public boolean fechar() throws IOException {
		if(scanner != null) scanner.close();
		if(formatter != null) formatter.close();
		return true;
	}
	
}
