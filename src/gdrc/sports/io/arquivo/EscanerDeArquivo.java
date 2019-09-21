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
 * Esta classe possui os m�todos necess�rios para fazer o escaneamento dos
 * dados de um exerc�cio f�sico.<br><br>
 *  
 * A leitura correta dos dados depender� da ordem dos dados mantidos no 
 * arquivo, podendo ocorrer uma exece��o caso um dado n�o seja encontrado ou 
 * n�o possua o valor esperado.
 *   
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class EscanerDeArquivo {
	/** Express�o regular para validar email.*/
	public static final String EXPRESSAO_REGULAR_EMAIL = "[^\\.][�����������\\w\\.\\*\\+\\-!#$%&'/=?{|}]+[^\\.]@[a-z]+(\\.[a-z]+)+";
	
	private ArquivoTexto arquivoTexto;
	private String identificadorCorrente;
	private String conteudoIdentificador;
	
	/**
	 * Construtor default respons�vel por inicializar o objeto com
	 * os objetos necess�rios para escanear um arquivo.
	 */
	public EscanerDeArquivo() {
		arquivoTexto = new ArquivoTexto();
		identificadorCorrente = conteudoIdentificador = "";
	}
	
	/**
	 * Escaneia o arquivo parcialmente obtendo o {@link Exercicio} cadastrado apenas
	 * com os dados fundamentais para que possa ser feita uma verifica��o de que 
	 * o Exerc�cio atualmente lido j� n�o foi cadastrado em execu��es anteriores.   
	 * 
	 * @param file O arquivo ser escaneado para extrair as informa��es do exerc�cio.
	 * 
	 * @return Retorna o objeto {@link Exercicio} parcialmente preenchido;
	 *  
	 * @throws IdentificadorInvalidoException Dispara a exece��o se algum dos dados 
	 * do exerc�cio lido for inv�lido ou n�o p�de ser lido.
	 * @throws IOException Dispara a exece��o caso ocorra um erro ao abrir 
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
					"O conte�do '%s' do identificador '%s' � inv�lido.", 
					conteudoIdentificador, identificadorCorrente)); 
		}
		
		return exercicio;
	}
	
	/**
	 * Escaneia o arquivo obtendo todos os dados {@link Exercicio}.
	 * Os dados do {@link Cliente} associado ao exerc�cio tamb�m s�o lidos 
	 * e adicionados ao objeto {@link Exercicio} que ser� retornado, caso possa
	 * ser lido do arquivo.   
	 * 
	 * @param file O arquivo ser escaneado para extrair as informa��es do exerc�cio.
	 * 
	 * @return Retorna o objeto {@link Exercicio} preenchido. Caso n�o seja poss�vel
	 * identificar o tipo de exerc�cio retorna <code>null</code>.
	 *  
	 * @throws IdentificadorInvalidoException Dispara a exece��o se algum dos dados 
	 * do exerc�cio lido for inv�lido ou n�o p�de ser lido.
	 * @throws IOException Dispara a exece��o caso ocorra um erro ao abrir 
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
					"O conte�do '%s' do identificador '%s' � inv�lido.", 
					conteudoIdentificador, identificadorCorrente)); 
		}
		
		return exercicio;
	}
	
	private String lerIdentificadorEmail() throws IdentificadorInvalidoException {
		String email = proximoIdentificador("E-mail");
		email = email.toLowerCase();
		if(!email.matches(EXPRESSAO_REGULAR_EMAIL))
				throw new IdentificadorInvalidoException( String.format(
						"O conte�do '%s' do identificador 'E-mail' � inv�lido.", conteudoIdentificador));
				
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
	 * Abre o arquivo passado por par�metro e verifica qual � o 
	 * tipo de exerc�cio que ser� lido, podendo ser atualmente um
	 * {@link Exercicio} ou um {@link ExercicioDetalhado}. <br><br> 
	 * 
	 * O exerc�cio ser� reaberto ap�s o seu conte�do ser lido por este 
	 * m�todo para que uma poss�vel chamada ao m�todos para escanear o arquivo 
	 * comece a leitura a partir do in�cio, como esperado. 
	 * 
	 * @param file O arquivo a ser extra�do o tipo de Exerc�cio armazenado.
	 *  
	 * @return Retorna o objeto vazio instanciado com o tipo {@link Exercicio}
	 * ou {@link ExercicioDetalhado}, que ser� instanciado dependendo do tipo 
	 * identificado no arquivo.
	 * 
	 * @throws IOException Dispara a exece��o caso ocorra um erro ao abrir 
	 * o arquivo.
	 * @throws IdentificadorInvalidoException Dispara a exce��o caso n�o seja
	 * poss�vel identificar o tipo do exerc�cio.
	 */
	private Exercicio verificarTipoDeExercicio(File file) throws IOException,
																 IdentificadorInvalidoException{
		String conteudo;

		arquivoTexto.abrirArquivo(file.getAbsolutePath());
		conteudo = arquivoTexto.lerConteudo();
		// Reabre o arquivo para voltar ao in�cio do mesmo.
		arquivoTexto.reabrirArquivo();

		if(conteudo == null)
			throw new IdentificadorInvalidoException("O exerc�cio f�sico "
					+ "n�o p�de ser lido corretamente.");

		Exercicio exercicio;
		if(conteudo.contains("------ Ritmo ------"))
			exercicio = new ExercicioDetalhado();
		else
			exercicio = new Exercicio();
		
		String nomeExercicio = proximoIdentificador("Exerc�cio");
		exercicio.setExercicio(nomeExercicio.toUpperCase());
		return exercicio;
	}

	/**
	 * Come�a a leitura do arquivo para obter os dados do {@link Cliente}. 
	 * 
	 * @return Retorna o objeto {@link Cliente} com os dados lidos do arquivo.
	 * 
	 * @throws IdentificadorInvalidoException Disparada se identificador a ser 
	 * lido n�o p�de ser encontrado.
	 * @throws NumberFormatException Disparada se ocorrer um erro ao formatar um 
	 * valor em {@link String} para um n�mero.
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
	 * Come�a a leitura do arquivo para obter os dados de um ojeto do tipo
	 * {@link Exercicio}.
	 *  
	 * @return Retorna o objeto {@link Exercicio} com os dados lidos do arquivo.
	 * 
	 * @throws IdentificadorInvalidoException Disparada se identificador a ser 
	 * lido n�o p�de ser encontrado.
	 * @throws NumberFormatException Disparada se ocorrer um erro ao formatar um 
	 * valor em {@link String} para um n�mero.
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
		conteudo = proximoIdentificador("Dura��o").replaceAll("[\\s]", "");
		exercicio.getTempo().setDuracao(Hora.converterParaHora(conteudo));

		conteudo =  tratarValorFloat(proximoIdentificador("Dist�ncia"));
		exercicio.setDistancia(Float.parseFloat(conteudo));

		conteudo = tratarValorFloat(proximoIdentificador("Calorias perdidas"));
		exercicio.setCaloriasPerdidas(Float.parseFloat(conteudo));

		conteudo = tratarValorInteiro(proximoIdentificador("Passos"));
		exercicio.setPassos(Integer.parseInt(conteudo));

		return exercicio;
	}
	
	private String tratarValorFloat(String conteudo) {
		// Remove qualquer letra, espa�o em branco e '.', depois substitui a virgula.
		return conteudo.replaceAll("[a-zA-Z\\s\\.]", "").replace(',','.');
	}
	
	private String tratarValorInteiro(String conteudo) {
		// Como � um n�mero inteiro, remove tudo que n�o � n�mero.
		return conteudo.replaceAll("[^(0-9)]", "");
	}

	/**
	 * Come�a a leitura do arquivo para obter os dados de um ojeto do tipo
	 * {@link ExercicioDetalhado}.
	 *  
	 * @return Retorna o objeto {@link ExercicioDetalhado} com os dados lidos do arquivo.
	 * 
	 * @throws IdentificadorInvalidoException Disparada se identificador a ser lido n�o
	 * p�de ser encontrado.
	 * @throws NumberFormatException Disparada se ocorrer um erro ao formatar um 
	 * valor em {@link String} para um n�mero.
	 * @throws DateTimeParseException Disparada se ocorrer um erro ao formatar uma 
	 * Data ou Hora em {@link String} para um objeto {@link Data} ou {@link Hora}.
	 */
	private Exercicio escanearExercicio(ExercicioDetalhado exercicio) 
								throws IdentificadorInvalidoException,
									   NumberFormatException,
									   DateTimeParseException{
		if(exercicio == null) return null;

		String conteudo;
		conteudo = tratarValorFloat(proximoIdentificador("Velocidade m�dia").replaceAll("[/]",""));
		exercicio.getVelocidade().setVelocidadeMedia(Float.parseFloat(conteudo));

		conteudo = tratarValorFloat(proximoIdentificador("Velocidade m�xima").replaceAll("[/]",""));
		exercicio.getVelocidade().setVelocidadeMaxima(Float.parseFloat(conteudo));

		// Remove qualquer letra, barra invertida e espa�os em branco do ritmo.
		conteudo = proximoIdentificador("Ritmo m�dio").replaceAll("[/a-zA-Z\\s]", "");
		exercicio.getRitmo().setRitmoMedio(Hora.converterParaHora("00�"+conteudo));

		conteudo = proximoIdentificador("Ritmo m�ximo").replaceAll("[/a-zA-Z\\s]", "");
		exercicio.getRitmo().setRitmoMaximo(Hora.converterParaHora("00�"+conteudo));

		conteudo = tratarValorInteiro(proximoIdentificador("Menor eleva��o"));
		exercicio.getElevacao().setMenorElevacao(Integer.parseInt(conteudo));
		
		conteudo = tratarValorInteiro(proximoIdentificador("Maior eleva��o"));
		exercicio.getElevacao().setMaiorElevacao(Integer.parseInt(conteudo));

		// L� o conte�do restante do arquivo.
		conteudo = arquivoTexto.lerConteudo();
		identificadorCorrente = "Ritmos";
		
		// Compila o regex para identificar a express�o como: 6,75 Km: 09�45�
		Pattern pattern = Pattern.compile("\\d+(,\\d+)*\\s[kmKM]{2}:\\s(\\d+.)+");
		Matcher m = pattern.matcher(conteudo);
		
		List<RitmoDetalhado> divisoes = new ArrayList<>();
		// Encontra cada linha do ritmo com o regex acima.
		while(m.find()) {
			String linha = m.group();
			conteudoIdentificador = linha;
			
			RitmoDetalhado ritmoDetalhado = new RitmoDetalhado();
			
			// Remove tudo que est� ap�s o KM e depois trata o n�mero como valor float.
			float quilometro = Float.parseFloat(tratarValorFloat(linha.replaceAll("[kmKM]{2}.*", "")));
			ritmoDetalhado.setQuilometro(quilometro);
			
			String ritmoS = linha.replaceAll(".*[kmKM]{2}:\\s", "");
			Hora ritmo = Hora.converterParaHora("00�"+ritmoS);
			ritmoDetalhado.setRitmo(ritmo);
			
			divisoes.add(ritmoDetalhado);
		}
		
		exercicio.getRitmo().setDivisoes(divisoes);
		return exercicio;
	}
	
	/**
	 * L� no arquivo o conte�do relacionado ao identificador passado por par�metro.
	 * 
	 * @param identificador O identificador que ser� procurado no arquivo.
	 * 
	 * @return Retorna a {@link String} contendo o conte�do ap�s o identificador 
	 * encontrado.
	 * @throws IdentificadorInvalidoException  Dispara a exce��o se identificador 
	 * passado por par�metro n�o p�de ser encontrado.
	 */
	private String proximoIdentificador(String identificador) throws IdentificadorInvalidoException{
		this.identificadorCorrente = identificador;
		this.conteudoIdentificador = arquivoTexto.buscarConteudoIdentificador(identificador);
		
		if(conteudoIdentificador == null)
			throw new IdentificadorInvalidoException(
					String.format("O Identificador '%s' n�o foi encontrado.", identificador));
		
		return conteudoIdentificador;
	}
	
}
