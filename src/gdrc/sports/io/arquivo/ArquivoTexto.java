package gdrc.sports.io.arquivo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Contém os métodos necessários para fazer o acesso e manipulação dos
 * dados armazenados em um arquivo texto.<br><br>
 * 
 * A leitura e escrita é feita de forma sequencial, portanto sempre que 
 * for necessário ler ou escrever a partir do incício do arquivo é 
 * necessário abrir o arquivo novamente, ou utilizar o método 
 * {@link #reabrirArquivo()} caso já tenha sido aberto anteriormente.
 * <br><br>
 * Caso não seja feito, as operações de leitura e escrita acontecerão 
 * a partir do último ponto lido no arquivo.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class ArquivoTexto{
	
	private String pathArquivo;
	private Scanner scanner;
	private Formatter formatter;
	
	/**
	 * Cria um arquivo com o nome que é passado por parâmetro contendo 
	 * o nome do arquivo e o caminho.
	 *  
	 * @param path O caminho do diretório onde o arquivo será criado junto do
	 * nome do mesmo.
	 * 
	 * @return Retorna <code>true</code> se o arquivo foi criado corretamente.
	 * @throws FileNotFoundException Dispara a execeção se ocorreu um erro ao
	 * criar o arquivo no caminho indicado.
	 */
	public boolean criarArquivo(String path) throws FileNotFoundException {
		formatter = new Formatter(path);
		pathArquivo = path;
		return true;
	}
	
	/**
	 * Abre o arquivo localizado no caminho passado por parâmetro.
	 *  
	 * @param path O caminho do arquivo indicando onde está armazenado 
	 * no sistema de arquivos.
	 * 
	 * @return Retorna <code>true</code> se o arquivo foi aberto corretamente.
	 * @throws FileNotFoundException Dispara a execeção se ocorreu um erro ao
	 * abrir o arquivo no caminho indicado.
	 */
	public boolean abrirArquivo(String path) throws FileNotFoundException {
		scanner = new Scanner(new FileInputStream(path));
		pathArquivo = path;
		return true;
	}
	
	/**
	 * Lê o conteúdo do arquivo a partir do último ponto lido. Caso nenhuma 
	 * operação de leitura tenha sido feita anteriormente, o conteúdo lido 
	 * será todo o conteúdo armazenado no arquivo, do início ao fim.
	 * 
	 * @return Retorna o conteúdo lido do arquivo.
	 */
	public String lerConteudo() {
		StringBuilder buffer = new StringBuilder();
		while(scanner.hasNextLine())
			buffer.append(scanner.nextLine()).append('\n');
		
		return buffer.toString();
	}
	
	/**
	 * Escreve o conteúdo passado por parâmetro no arquivo. O conteúdo 
	 * será escrito no local em que a última leitura foi feita. 
	 * 
	 * @param conteudo Conteúdo a ser escrito no arquivo.
	 * @return Retorna <code>true</code> se a escrita foi feita corretamente.
	 */
	public boolean escreverConteudo(String conteudo) {
		formatter.format("%s\n", conteudo);
		return true;
	}
	
	/**
	 * Procura no arquivo o identificador passado por parâmetro e obtém o 
	 * conteúdo da mesma linha após o mesmo, caso seja encontrado.
	 * 
	 * @param identificador O identificador a ser procurado no arquivo.
	 * 
	 * @return Retorna o contéudo após o identificador encontrado. 
	 * Caso não encontre o identificador retorna <code>null</code>.
	 */
	public String buscarConteudoIdentificador(String identificador) {
		Pattern pattern = Pattern.compile(identificador + ": ");
		
		while(scanner.hasNextLine())
			if(scanner.findInLine(pattern) == null) {
				// Pula para a próxima linha como não encontrou.
				scanner.nextLine();
			}else{/* Como o ponteiro do arquivo parou no identificador encontrado,
				 * retorna o conteúdo restante da linha, ou seja o conteudo do
				 * identificador. Caso não tenha nada em frente a ele retorna null. 
				 */
				return (scanner.hasNextLine())? scanner.nextLine() : null;
			}
		
		return null;
	}
	
	/**
	 * Reabre o arquivo para que uma possível leitura feita 
	 * posteriormente comece a partir do incício do arquivo.
	 *  
	 * @return Retorna <code>true</code> se o arquivo foi 
	 * reaberto corretamente.
	 * 
	 * @throws IOException Dispara a execeção caso ocorra um erro 
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
	 * @throws IOException Dispara a execeção caso ocorra um erro 
	 * ao fechar o arquivo. 
	 */
	public boolean fechar() throws IOException {
		if(scanner != null) scanner.close();
		if(formatter != null) formatter.close();
		return true;
	}
	
}
