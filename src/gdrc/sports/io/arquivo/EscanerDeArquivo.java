package gdrc.sports.io.arquivo;

import java.io.File;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.Ritmo.RitmoDetalhado;
import gdrc.sports.tipo.Tempo;
import gdrc.sports.tipo.exception.IdentificadorInvalidoException;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.tipo.time.Hora;

/**
 * Esta classe possui os métodos necessários para fazer o escaneamento dos
 * dados de um exercício físico.<br><br>
 *  
 * A leitura correta dos dados dependerá da ordem dos dados mantidos no 
 * arquivo, podendo ocorrer uma execeção caso um dado não seja encontrado ou 
 * não possua o valor esperado.
 *   
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class EscanerDeArquivo {
	/** Expressão regular para validar email.*/
	public static final String EXPRESSAO_REGULAR_EMAIL = "[^\\.][âêôáéíóúãõà\\w\\.\\*\\+\\-!#$%&'/=?{|}]+[^\\.]@[a-z]+(\\.[a-z]+)+";
	
	private ArquivoTexto arquivoTexto;
	private String identificadorCorrente;
	private String conteudoIdentificador;
	
	/**
	 * Construtor default responsável por inicializar o objeto com
	 * os objetos necessários para escanear um arquivo.
	 */
	public EscanerDeArquivo() {
		arquivoTexto = new ArquivoTexto();
		identificadorCorrente = conteudoIdentificador = "";
	}
	
	/**
	 * Escaneia o arquivo parcialmente obtendo o {@link Exercicio} cadastrado apenas
	 * com os dados fundamentais para que possa ser feita uma verificação de que 
	 * o Exercício atualmente lido já não foi cadastrado em execuções anteriores.   
	 * 
	 * @param file O arquivo ser escaneado para extrair as informações do exercício.
	 * 
	 * @return Retorna o objeto {@link Exercicio} parcialmente preenchido;
	 *  
	 * @throws IdentificadorInvalidoException Dispara a execeção se algum dos dados 
	 * do exercício lido for inválido ou não pôde ser lido.
	 * @throws IOException Dispara a execeção caso ocorra um erro ao abrir 
	 * o arquivo.  
	 */
	public Exercicio escanearArquivoParcialmente(File file) throws IdentificadorInvalidoException,
																   IOException{
		Exercicio exercicio = verificarTipoDeExercicio(file);
		if(exercicio == null) return null;
		
		try { 
			exercicio.getCliente().setEmail(lerIdentificadorEmail());
			
			String data = proximoIdentificador("Data");
			exercicio.setData(Data.converterParaData(data));

			Tempo tempo = lerIdentificadorTempo();
			exercicio.setTempo(tempo);
			
		}catch (DateTimeException e) {
			throw new IdentificadorInvalidoException(String.format(
					"O conteúdo '%s' do identificador '%s' é inválido.", 
					conteudoIdentificador, identificadorCorrente)); 
		}
		
		return exercicio;
	}
	
	/**
	 * Escaneia o arquivo obtendo todos os dados {@link Exercicio}.
	 * Os dados do {@link Cliente} associado ao exercício também são lidos 
	 * e adicionados ao objeto {@link Exercicio} que será retornado, caso possa
	 * ser lido do arquivo.   
	 * 
	 * @param file O arquivo ser escaneado para extrair as informações do exercício.
	 * 
	 * @return Retorna o objeto {@link Exercicio} preenchido. Caso não seja possível
	 * identificar o tipo de exercício retorna <code>null</code>.
	 *  
	 * @throws IdentificadorInvalidoException Dispara a execeção se algum dos dados 
	 * do exercício lido for inválido ou não pôde ser lido.
	 * @throws IOException Dispara a execeção caso ocorra um erro ao abrir 
	 * o arquivo.
	 */
	public Exercicio escanearArquivo(File file) throws IdentificadorInvalidoException,
													   IOException{
		Exercicio exercicio = verificarTipoDeExercicio(file);
		if(exercicio == null) return null;
		
		try { 
			exercicio.setCliente(escanearCliente());

			escanearExercicio(exercicio);
			if(exercicio instanceof ExercicioDetalhado)
				escanearExercicio((ExercicioDetalhado)exercicio);

		}catch (NumberFormatException | DateTimeParseException e) {
			throw new IdentificadorInvalidoException(String.format(
					"O conteúdo '%s' do identificador '%s' é inválido.", 
					conteudoIdentificador, identificadorCorrente)); 
		}
		
		return exercicio;
	}
	
	private String lerIdentificadorEmail() throws IdentificadorInvalidoException {
		String email = proximoIdentificador("E-mail");
		email = email.toLowerCase();
		if(!email.matches(EXPRESSAO_REGULAR_EMAIL))
				throw new IdentificadorInvalidoException( String.format(
						"O conteúdo '%s' do identificador 'E-mail' é inválido.", conteudoIdentificador));
				
		return email.trim();
	}

	private Tempo lerIdentificadorTempo() throws DateTimeException, IdentificadorInvalidoException{
		Tempo tempo = new Tempo();
		
		String stringArray[] = proximoIdentificador("Tempo").split(" - ");
		if(stringArray.length < 2) throw new DateTimeException("Formato incorreto.");
		tempo.setHoraInicio(Hora.converterParaHora(stringArray[0]+":00"));
		tempo.setHoraFim(Hora.converterParaHora(stringArray[1]+":00"));
		return tempo;
	}
	
	/**
	 * Abre o arquivo passado por parâmetro e verifica qual é o 
	 * tipo de exercício que será lido, podendo ser atualmente um
	 * {@link Exercicio} ou um {@link ExercicioDetalhado}. <br><br> 
	 * 
	 * O exercício será reaberto após o seu conteúdo ser lido por este 
	 * método para que uma possível chamada ao métodos para escanear o arquivo 
	 * comece a leitura a partir do início, como esperado. 
	 * 
	 * @param file O arquivo a ser extraído o tipo de Exercício armazenado.
	 *  
	 * @return Retorna o objeto vazio instanciado com o tipo {@link Exercicio}
	 * ou {@link ExercicioDetalhado}, que será instanciado dependendo do tipo 
	 * identificado no arquivo.
	 * 
	 * @throws IOException Dispara a execeção caso ocorra um erro ao abrir 
	 * o arquivo.
	 * @throws IdentificadorInvalidoException Dispara a exceção caso não seja
	 * possível identificar o tipo do exercício.
	 */
	private Exercicio verificarTipoDeExercicio(File file) throws IOException,
																 IdentificadorInvalidoException{
		String conteudo;

		arquivoTexto.abrirArquivo(file.getAbsolutePath());
		conteudo = arquivoTexto.lerConteudo();
		// Reabre o arquivo para voltar ao início do mesmo.
		arquivoTexto.reabrirArquivo();

		if(conteudo == null)
			throw new IdentificadorInvalidoException("O exercício físico "
					+ "não pôde ser lido corretamente.");

		Exercicio exercicio;
		if(conteudo.contains("------ Ritmo ------"))
			exercicio = new ExercicioDetalhado();
		else
			exercicio = new Exercicio();
		
		String nomeExercicio = proximoIdentificador("Exercício");
		exercicio.setExercicio(nomeExercicio.toUpperCase());
		return exercicio;
	}

	/**
	 * Começa a leitura do arquivo para obter os dados do {@link Cliente}. 
	 * 
	 * @return Retorna o objeto {@link Cliente} com os dados lidos do arquivo.
	 * 
	 * @throws IdentificadorInvalidoException Disparada se identificador a ser 
	 * lido não pôde ser encontrado.
	 * @throws NumberFormatException Disparada se ocorrer um erro ao formatar um 
	 * valor em {@link String} para um número.
	 * @throws DateTimeParseException Disparada se ocorrer um erro ao formatar um 
	 * Data ou Hora em {@link String} para um objeto {@link Data} ou {@link Hora}.
	 */
	private Cliente escanearCliente() throws IdentificadorInvalidoException,
											 NumberFormatException,
											 DateTimeParseException{
		
		Cliente cliente = new Cliente();
		String conteudo;

		cliente.setNome(proximoIdentificador("Nome"));
		cliente.setSexo(proximoIdentificador("Sexo"));

		conteudo = proximoIdentificador("Altura").replace(" m", "").replace(',', '.');
		cliente.setAltura(Float.parseFloat(conteudo));

		conteudo =  proximoIdentificador("Peso").replace(" Kg", "").replace(',', '.');;
		cliente.setPeso(Float.parseFloat(conteudo));

		conteudo = proximoIdentificador("Data de nascimento");
		cliente.setDataNascimento(Data.converterParaData(conteudo));

		cliente.setEmail(proximoIdentificador("E-mail"));

		return cliente;
	}
	
	/**
	 * Começa a leitura do arquivo para obter os dados de um ojeto do tipo
	 * {@link Exercicio}.
	 *  
	 * @return Retorna o objeto {@link Exercicio} com os dados lidos do arquivo.
	 * 
	 * @throws IdentificadorInvalidoException Disparada se identificador a ser 
	 * lido não pôde ser encontrado.
	 * @throws NumberFormatException Disparada se ocorrer um erro ao formatar um 
	 * valor em {@link String} para um número.
	 * @throws DateTimeParseException Disparada se ocorrer um erro ao formatar um 
	 * Data ou Hora em {@link String} para um objeto {@link Data} ou {@link Hora}.
	 */
	private Exercicio escanearExercicio(Exercicio exercicio) 
									throws IdentificadorInvalidoException,
										   NumberFormatException,
										   DateTimeParseException{
		
		if(exercicio == null) return null;

		String conteudo;

		// Remove caracteres em branco caso tenha.
		conteudo = proximoIdentificador("Data").replaceAll("[\\s]", "");;
		exercicio.setData(Data.converterParaData(conteudo));

		Tempo tempo = lerIdentificadorTempo();
		exercicio.setTempo(tempo);

		// Remove caracteres em branco caso tenha.
		conteudo = proximoIdentificador("Duração").replaceAll("[\\s]", "");
		exercicio.getTempo().setDuracao(Hora.converterParaHora(conteudo));

		conteudo =  tratarValorFloat(proximoIdentificador("Distância"));
		exercicio.setDistancia(Float.parseFloat(conteudo));

		conteudo = tratarValorFloat(proximoIdentificador("Calorias perdidas"));
		exercicio.setCaloriasPerdidas(Float.parseFloat(conteudo));

		conteudo = tratarValorInteiro(proximoIdentificador("Passos"));
		exercicio.setPassos(Integer.parseInt(conteudo));

		return exercicio;
	}
	
	private String tratarValorFloat(String conteudo) {
		// Remove qualquer letra, espaço em branco e '.', depois substitui a virgula.
		return conteudo.replaceAll("[a-zA-Z\\s\\.]", "").replace(',','.');
	}
	
	private String tratarValorInteiro(String conteudo) {
		// Como é um número inteiro, remove tudo que não é número.
		return conteudo.replaceAll("[^(0-9)]", "");
	}

	/**
	 * Começa a leitura do arquivo para obter os dados de um ojeto do tipo
	 * {@link ExercicioDetalhado}.
	 *  
	 * @return Retorna o objeto {@link ExercicioDetalhado} com os dados lidos do arquivo.
	 * 
	 * @throws IdentificadorInvalidoException Disparada se identificador a ser lido não
	 * pôde ser encontrado.
	 * @throws NumberFormatException Disparada se ocorrer um erro ao formatar um 
	 * valor em {@link String} para um número.
	 * @throws DateTimeParseException Disparada se ocorrer um erro ao formatar uma 
	 * Data ou Hora em {@link String} para um objeto {@link Data} ou {@link Hora}.
	 */
	private Exercicio escanearExercicio(ExercicioDetalhado exercicio) 
								throws IdentificadorInvalidoException,
									   NumberFormatException,
									   DateTimeParseException{
		if(exercicio == null) return null;

		String conteudo;
		conteudo = tratarValorFloat(proximoIdentificador("Velocidade média").replaceAll("[/]",""));
		exercicio.getVelocidade().setVelocidadeMedia(Float.parseFloat(conteudo));

		conteudo = tratarValorFloat(proximoIdentificador("Velocidade máxima").replaceAll("[/]",""));
		exercicio.getVelocidade().setVelocidadeMaxima(Float.parseFloat(conteudo));

		// Remove qualquer letra, barra invertida e espaços em branco do ritmo.
		conteudo = proximoIdentificador("Ritmo médio").replaceAll("[/a-zA-Z\\s]", "");
		exercicio.getRitmo().setRitmoMedio(Hora.converterParaHora("00°"+conteudo));

		conteudo = proximoIdentificador("Ritmo máximo").replaceAll("[/a-zA-Z\\s]", "");
		exercicio.getRitmo().setRitmoMaximo(Hora.converterParaHora("00°"+conteudo));

		conteudo = tratarValorInteiro(proximoIdentificador("Menor elevação"));
		exercicio.getElevacao().setMenorElevacao(Integer.parseInt(conteudo));
		
		conteudo = tratarValorInteiro(proximoIdentificador("Maior elevação"));
		exercicio.getElevacao().setMaiorElevacao(Integer.parseInt(conteudo));

		// Lê o conteúdo restante do arquivo.
		conteudo = arquivoTexto.lerConteudo();
		identificadorCorrente = "Ritmos";
		
		// Compila o regex para identificar a expressão como: 6,75 Km: 09’45”
		Pattern pattern = Pattern.compile("\\d+(,\\d+)*\\s[kmKM]{2}:\\s(\\d+.)+");
		Matcher m = pattern.matcher(conteudo);
		
		List<RitmoDetalhado> divisoes = new ArrayList<>();
		// Encontra cada linha do ritmo com o regex acima.
		while(m.find()) {
			String linha = m.group();
			conteudoIdentificador = linha;
			
			RitmoDetalhado ritmoDetalhado = new RitmoDetalhado();
			
			// Remove tudo que está após o KM e depois trata o número como valor float.
			float quilometro = Float.parseFloat(tratarValorFloat(linha.replaceAll("[kmKM]{2}.*", "")));
			ritmoDetalhado.setQuilometro(quilometro);
			
			String ritmoS = linha.replaceAll(".*[kmKM]{2}:\\s", "");
			Hora ritmo = Hora.converterParaHora("00°"+ritmoS);
			ritmoDetalhado.setRitmo(ritmo);
			
			divisoes.add(ritmoDetalhado);
		}
		
		exercicio.getRitmo().setDivisoes(divisoes);
		return exercicio;
	}
	
	/**
	 * Lê no arquivo o conteúdo relacionado ao identificador passado por parâmetro.
	 * 
	 * @param identificador O identificador que será procurado no arquivo.
	 * 
	 * @return Retorna a {@link String} contendo o conteúdo após o identificador 
	 * encontrado.
	 * @throws IdentificadorInvalidoException  Dispara a exceção se identificador 
	 * passado por parâmetro não pôde ser encontrado.
	 */
	private String proximoIdentificador(String identificador) throws IdentificadorInvalidoException{
		this.identificadorCorrente = identificador;
		this.conteudoIdentificador = arquivoTexto.buscarConteudoIdentificador(identificador);
		
		if(conteudoIdentificador == null)
			throw new IdentificadorInvalidoException(
					String.format("O Identificador '%s' não foi encontrado.", identificador));
		
		return conteudoIdentificador;
	}
	
}
