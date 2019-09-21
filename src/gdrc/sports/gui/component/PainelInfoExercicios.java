package gdrc.sports.gui.component;

import java.awt.Font;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.ExercicioDetalhado;
import gdrc.sports.tipo.Ritmo;
import gdrc.sports.tipo.Ritmo.RitmoDetalhado;

/**
 * Cria um componente {@link JPanel} que agrupa as todas as 
 * informações de um objeto {@link Exercicio}. <br><br>
 *  
 * Ao criar o painel Exercício deverá ser passado no contrutor 
 * da classe, e então será adcionado as informações do Exercício 
 * de acordo com o tipo do objeto, podendo ser {@link Exercicio} 
 * ou {@link ExercicioDetalhado}.
 *   
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class PainelInfoExercicios extends JPanel {
	private enum TipoDadoLabel {PASSOS, CALORIAS, DISTANCIA, DURACAO, 
		DATA, TEMPO_INICIO, TEMPO_TERMINO, VELOCIDADE_MAX, 
		VELOCIDADE_MEDIA, MAIOR_ELEVACAO, MENOR_ELEVACAO, 
		RITMO_MAX, RITMO_MEDIO};

	private JLabel exercicioLabel;
	private JTable tableRitmoDetalhado;

	/**
	 * Contrutor defaul que instancia o obejto da classe 
	 * e cria os componentes que irão exibir as informações
	 * do exercício que é passado por parâmetro.
	 * 
	 * @param exercicio O exercício a ser exibido pelo painel.
	 */
	public PainelInfoExercicios(Exercicio exercicio) {
		if(exercicio == null) exercicio = new Exercicio();
		
		//Configurações do painel.
		setSize(380, 180);
		setLayout(null);
		setBorder(new TitledBorder((Border)null));
		
		//Configurações dos componentes que exibem os dados de um Exercício.
		exercicioLabel = new JLabel(exercicio.getExercicio());
		exercicioLabel.setHorizontalAlignment(SwingConstants.CENTER);
		exercicioLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		exercicioLabel.setBounds(31, 11, 319, 14);
		add(exercicioLabel);
		
		JLabel labelData = obterLabelExercicio(exercicio, TipoDadoLabel.DATA);
		labelData.setBounds(31, 47, 133, 14);
		add(labelData);
		
		JLabel labelInicio = obterLabelExercicio(exercicio, TipoDadoLabel.TEMPO_INICIO);
		labelInicio.setBounds(31, 72, 149, 14);
		add(labelInicio);
		
		JLabel labelTermino = obterLabelExercicio(exercicio, TipoDadoLabel.TEMPO_TERMINO);
		labelTermino.setBounds(200, 72, 160, 14);
		add(labelTermino);
		
		JLabel labelDuracao = obterLabelExercicio(exercicio, TipoDadoLabel.DURACAO);
		labelDuracao.setBounds(31, 97, 149, 14);
		add(labelDuracao);
		
		JLabel labelDistancia = obterLabelExercicio(exercicio, TipoDadoLabel.DISTANCIA);
		labelDistancia.setBounds(200, 97, 149, 14);
		add(labelDistancia);
		
		JLabel labelCalorias = obterLabelExercicio(exercicio, TipoDadoLabel.CALORIAS);
		labelCalorias.setBounds(200, 122, 149, 14);
		add(labelCalorias);
		
		JLabel labelPassos = obterLabelExercicio(exercicio, TipoDadoLabel.PASSOS);
		labelPassos.setBounds(31, 122, 133, 14);
		add(labelPassos);
		
		
		//Configurações dos componentes que exibem informações de ExercícioDetalhado.
		if(!(exercicio instanceof ExercicioDetalhado)) return;
		ExercicioDetalhado exercDetalhado = (ExercicioDetalhado) exercicio;
		this.setSize(380, 420);
		
		//Configurações dos componentes que exibem a velocidade do Exercício.
		JPanel velocidadePainel = new JPanel();
		velocidadePainel.setBorder(new TitledBorder("Velocidade"));
		velocidadePainel.setBounds(21, 150, 339, 43);
		velocidadePainel.setLayout(null);
		add(velocidadePainel);
		
		JLabel labelVelocMax = obterLabelExercicioDetalhado(exercDetalhado, TipoDadoLabel.VELOCIDADE_MAX);
		labelVelocMax.setBounds(30, 18, 147, 14);
		velocidadePainel.add(labelVelocMax);
		
		JLabel labelVelocMedia = obterLabelExercicioDetalhado(exercDetalhado, TipoDadoLabel.VELOCIDADE_MEDIA);
		labelVelocMedia.setBounds(187, 18, 142, 14);
		velocidadePainel.add(labelVelocMedia);

		//Configurações dos componentes que exibem a elevacao do Exercício.
		JPanel elevacaoPainel = new JPanel();
		elevacaoPainel.setBorder(new TitledBorder("Elevação"));
		elevacaoPainel.setBounds(21, 204, 339, 43);
		add(elevacaoPainel);
		elevacaoPainel.setLayout(null);
		
		JLabel labelMaiorElevacao = obterLabelExercicioDetalhado(exercDetalhado, TipoDadoLabel.MAIOR_ELEVACAO);
		labelMaiorElevacao.setBounds(30, 18, 145, 14);
		elevacaoPainel.add(labelMaiorElevacao);
		
		JLabel labelMenorElevacao = obterLabelExercicioDetalhado(exercDetalhado, TipoDadoLabel.MENOR_ELEVACAO);
		labelMenorElevacao.setBounds(187, 18, 144, 14);
		elevacaoPainel.add(labelMenorElevacao);

		//Configurações dos componentes que exibem o ritmo do Exercício.
		JPanel ritmoPainel = new JPanel();
		ritmoPainel.setBorder(new TitledBorder("Ritmo"));
		ritmoPainel.setBounds(21, 258, 339, 146);
		add(ritmoPainel);
		ritmoPainel.setLayout(null);
		
		JLabel labelRitmoMax = obterLabelExercicioDetalhado(exercDetalhado, TipoDadoLabel.RITMO_MAX);
		labelRitmoMax.setBounds(30, 23, 147, 14);
		ritmoPainel.add(labelRitmoMax);
		
		JLabel labelRitmoMedio = obterLabelExercicioDetalhado(exercDetalhado, TipoDadoLabel.RITMO_MEDIO);
		labelRitmoMedio.setBounds(187, 23, 142, 14);
		ritmoPainel.add(labelRitmoMedio);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(21, 48, 299, 87);
		ritmoPainel.add(scrollPane);
		
		// Exibe cada RitmoDetalhado em um JTable.
		List<RitmoDetalhado> divisoes = exercDetalhado.getRitmo().getDivisoes();
		Object linhas[][] = new Object[divisoes.size()][2];
		int linhaTabela = 0;
		for(RitmoDetalhado ritmo : divisoes) {
			linhas[linhaTabela][0] = ritmo.getQuilometro();
			linhas[linhaTabela++][1] = Ritmo.obterRitmoComoString(ritmo.getRitmo());
		}
		tableRitmoDetalhado = new JTable(linhas,new String[]{"Quilômetro","Ritmo"});
		scrollPane.setViewportView(tableRitmoDetalhado);
	}
	
	private JLabel obterLabelExercicio(Exercicio exercicio, TipoDadoLabel dadoRelatorio) {
		if(exercicio == null) return new JLabel();
		
		String textoLabel = "<html>%s: <b>%s</b> %s</html>";
		String rotulo = "", unidade = "";
		Object dado = null;
		
		switch (dadoRelatorio) {
		case CALORIAS: 
			rotulo = "Calorias perdidas"; unidade = "Kcal";
			dado = exercicio.getCaloriasPerdidas(); break;
		case DISTANCIA: 
			rotulo = "Distância"; unidade = "Km";
			dado = exercicio.getDistancia(); break;
		case DURACAO: 
			rotulo = "Duração";
			dado = exercicio.getTempo().getDuracao(); break;
		case PASSOS: 
			rotulo = "Passos";
			dado = exercicio.getPassos(); break;
		case DATA: 
			rotulo = "Data";
			dado = exercicio.getData(); break;
		case TEMPO_INICIO: 
			rotulo = "Início";
			dado = exercicio.getTempo().getHoraInicio(); break;
		case TEMPO_TERMINO: 
			rotulo = "Término";
			dado = exercicio.getTempo().getHoraFim(); break;
		default: return new JLabel();
		}
		return new JLabel(String.format(textoLabel, rotulo, dado, unidade));
	}
	
	private JLabel obterLabelExercicioDetalhado(ExercicioDetalhado exercicio, TipoDadoLabel dadoRelatorio) {
		if(exercicio == null) return new JLabel();
		
		String textoLabel = "<html>%s: <b>%s</b> %s</html>";
		String rotulo = "", unidade = "";
		Object dado = null;
		
		switch (dadoRelatorio) {
		case VELOCIDADE_MAX:  
			rotulo = "Máxima"; unidade = "Km/h";
			dado = exercicio.getVelocidade().getVelocidadeMaxima(); break;
		case VELOCIDADE_MEDIA:  
			rotulo = "Média"; unidade = "Km/h";
			dado = exercicio.getVelocidade().getVelocidadeMedia(); break;
		case RITMO_MAX:  
			rotulo = "Máximo"; unidade = "Min/Km";
			dado = Ritmo.obterRitmoComoString(exercicio.getRitmo().getRitmoMaximo()); break;
		case RITMO_MEDIO:  
			rotulo = "Médio"; unidade = "Min/Km";
			dado = Ritmo.obterRitmoComoString(exercicio.getRitmo().getRitmoMedio()); break;
		case MAIOR_ELEVACAO:  
			rotulo = "Maior"; unidade = "m";
			dado = exercicio.getElevacao().getMaiorElevacao(); break;
		case MENOR_ELEVACAO:  
			rotulo = "Menor"; unidade = "m";
			dado = exercicio.getElevacao().getMenorElevacao(); break;
		default: return new JLabel();
		}
		
		return new JLabel(String.format(textoLabel, rotulo, dado, unidade));
	}
	

}
