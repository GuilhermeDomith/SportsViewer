package gdrc.sports.gui.janela;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import gdrc.sports.gui.component.PainelInfoCliente;
import gdrc.sports.gui.es.MensagemUsuario;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.grafico.GraficoCategory;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.util.propriedades.Propriedade;
import gdrc.sports.util.relatorio.RelatorioGrafico;
import gdrc.sports.util.relatorio.RelatorioGrafico.TipoGrafico;

/**
 * Cria a janela que permite exibir o relat�rio gr�fico do cliente 
 * fornecido ao criar a janela. <br><br>
 * Permite ao usu�rio da aplica��o selecionar os dados e per�odo a ser 
 * utilizado para gerar o relat�rio gr�fico do cliente.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class IgRelatorioGrafico extends JDialog {
	private enum TipoGraficoSelecionado {COLUNA, LINHA, COLUNA_GERAL};
	private final JPanel contentPanel = new JPanel();
	
	private JComboBox<TipoDadoGrafico> comboBoxDadosGrafico;
	private JComboBox<String> comboBoxFiltrarExercicio;
	private JPanel painelExibeGrafico;
	private PainelPeriodo painelPeriodo;
	
	private Cliente cliente;
	private RelatorioGrafico relatorioGrafico;
	private TipoGraficoSelecionado tipoGraficoSelecionado;

	/**
	 * Cria e exibe a janela para exibir os componentes que permitem 
	 * configurar e exibir o relat�rio gr�fico do cliente fornecido 
	 * por par�metro.
	 *  
	 * @param location 	O componente utilizado para centralizar a janela.
	 * @param cliente	O cliente que ter� o relat�rio gr�fico exibido na janela.
	 */
	public IgRelatorioGrafico(Component location, Cliente cliente) {
		this.cliente = cliente;
		
		// Configura��es da janela
		setBounds(100, 100, 423, 448);
		getContentPane().setLayout(new BorderLayout());
		setLocationRelativeTo(location);
		setTitle("Relat�rio Gr�fico do Cliente");
		setIconImage(Propriedade.ICONE_APLICACAO);
		
		// Configura��es do painel principal.
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// Configura��es do painel que exibe as informa��es do cliente.
		PainelInfoCliente painelInfoCliente = new PainelInfoCliente(cliente);
		painelInfoCliente.setBounds(10, 11, 399, painelInfoCliente.getHeight());
		contentPanel.add(painelInfoCliente);
		
		// Configura��es do painel que permite a entrada do periodo.
		painelPeriodo = new PainelPeriodo();
		painelPeriodo.setBounds(10, 132, 399, 85);
		contentPanel.add(painelPeriodo);
		
		ButtonGroup btnGroupTipoGrafico = new ButtonGroup();
		
		// Configura��es do painel que exibe o gr�fico selecionado.
		painelExibeGrafico = new JPanel(new BorderLayout());
		painelExibeGrafico.setBorder(new TitledBorder("Gr�fico"));
		painelExibeGrafico.setBounds(419, 11, 576, 357);
		painelExibeGrafico.add(new JLabel("Nenhum gr�fico a ser exibido.", JLabel.CENTER));
		contentPanel.add(painelExibeGrafico);
		
		// Configura��es do painel para selecinar o tipo de dado do gr�fico
		JPanel painelDadoGrafico = new JPanel();
		painelDadoGrafico.setBounds(10, 280, 399, 95);
		painelDadoGrafico.setBorder(new TitledBorder("Dados para o Gr�fico"));
		contentPanel.add(painelDadoGrafico);
		painelDadoGrafico.setLayout(null);
		
		JLabel labelDados = new JLabel("Dados:");
		labelDados.setBounds(52, 26, 46, 14);
		painelDadoGrafico.add(labelDados);
		
		comboBoxDadosGrafico = new JComboBox<>();
		comboBoxDadosGrafico.setBounds(119, 23, 206, 20);
		painelDadoGrafico.add(comboBoxDadosGrafico);
		
		JLabel labelFiltrar = new JLabel("Filtrar Exerc�cio:");
		labelFiltrar.setBounds(52, 54, 85, 14);
		painelDadoGrafico.add(labelFiltrar);
		
		comboBoxFiltrarExercicio = new JComboBox<>(new Vector<>(cliente.obterNomesExercicios()));
		comboBoxFiltrarExercicio.setBounds(165, 54, 160, 20);
		painelDadoGrafico.add(comboBoxFiltrarExercicio);
		comboBoxFiltrarExercicio.addItem("Todos");
		comboBoxFiltrarExercicio.setSelectedItem("Todos");
		
		// Configura��es do painel para configurar o gr�fico.
		JPanel painelTipoGrafico = new JPanel();
		painelTipoGrafico.setBounds(10, 221, 399, 57);
		contentPanel.add(painelTipoGrafico);
		painelTipoGrafico.setBorder(new TitledBorder("Tipo de gr\u00E1fico"));
		painelTipoGrafico.setLayout(null);
		
		JRadioButton radioBtnGraficoLinha = new JRadioButton("Gr\u00E1fico de Linha");
		radioBtnGraficoLinha.addActionListener((ActionEvent e) -> {
			atualizarDadosComboBox(TipoGraficoSelecionado.LINHA);
		});
		radioBtnGraficoLinha.setBounds(135, 21, 103, 23);
		painelTipoGrafico.add(radioBtnGraficoLinha);
		btnGroupTipoGrafico.add(radioBtnGraficoLinha);
		
		JRadioButton radioBtnGraficoColunas = new JRadioButton("Gr\u00E1fico de Colunas");
		radioBtnGraficoColunas.addActionListener((ActionEvent e) -> {
			atualizarDadosComboBox(TipoGraficoSelecionado.COLUNA);
		});
		radioBtnGraficoColunas.setBounds(18, 21, 115, 23);
		painelTipoGrafico.add(radioBtnGraficoColunas);
		btnGroupTipoGrafico.add(radioBtnGraficoColunas);
		
		JRadioButton radioBtnGraficoGeral = new JRadioButton("Gr\u00E1fico de Colunas Geral\r\n");
		radioBtnGraficoGeral.addActionListener((ActionEvent e) -> {
			atualizarDadosComboBox(TipoGraficoSelecionado.COLUNA_GERAL);
		});
		radioBtnGraficoGeral.setBounds(240, 21, 145, 23);
		painelTipoGrafico.add(radioBtnGraficoGeral);
		btnGroupTipoGrafico.add(radioBtnGraficoGeral);
		
		radioBtnGraficoColunas.doClick();
		
		// Configura��es do painel de bot�es.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.LEFT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		JButton buttonExportarPDF = new JButton("Exportar para PDF");
		buttonPane.add(buttonExportarPDF);
		buttonExportarPDF.addActionListener((ActionEvent e) -> { exportarRelatorioPDF(); });
		
		JButton btnExibirInformaesDo = new JButton("Informa��es do Cliente");
		btnExibirInformaesDo.setHorizontalAlignment(SwingConstants.LEFT);
		btnExibirInformaesDo.addActionListener((ActionEvent e) -> { 
			new IgInfoCliente(this, cliente);
		});
		buttonPane.add(btnExibirInformaesDo);
		
		JButton buttonGerarGrafico = new JButton("Visualizar Gr�fico");
		buttonGerarGrafico.setHorizontalTextPosition(SwingConstants.LEFT);
		buttonGerarGrafico.setHorizontalAlignment(SwingConstants.RIGHT);
		buttonPane.add(buttonGerarGrafico);
		buttonGerarGrafico.addActionListener((ActionEvent e) -> {
			gerarGrafico();
			if(getWidth() < 1000) {
				this.setSize(1013, 448);
				this.setLocationRelativeTo(location);
			}
		});
		
		setModal(true);
		setResizable(false);
		setVisible(true);
	}

	private void exportarRelatorioPDF() {
		IgExportarPDF janela = new IgExportarPDF(cliente);
		switch (tipoGraficoSelecionado) {
		case COLUNA: janela.getCheckBoxGraficoColunas().setSelected(true);
			break;
		case COLUNA_GERAL: janela.getCheckBoxGraficoColunasGeral().setSelected(true);
			break;
		case LINHA: janela.getCheckBoxGraficoLinhas().setSelected(true);
			break;
		}
		
		Object dataInicial = painelPeriodo.getTextFieldDataInicial().getValue();
		Object dataFinal = painelPeriodo.getTextFieldDataFinal().getValue();
		
		// Altera as datas do painel para o caso de eles terem sido fornecidas invertidas.
		janela.getPainelPeriodo().getTextFieldDataInicial().setValue(dataInicial);
		janela.getPainelPeriodo().getTextFieldDataFinal().setValue(dataFinal);
		//Configura as op��es da janela IgExportarPDF com as mesmas op��es que est�o nesta janela. 
		janela.getComboBoxExercicioFiltrar().setSelectedItem(comboBoxFiltrarExercicio.getSelectedItem());
		janela.getCheckBoxInfoCliente().setSelected(true);
		janela.setRelatorioGrafico(relatorioGrafico);
		janela.setLocationRelativeTo(this);
		janela.setVisible(true);
	}

	private void atualizarDadosComboBox(TipoGraficoSelecionado tipoGraficoSelecionado) {
		comboBoxDadosGrafico.removeAllItems();
		this.tipoGraficoSelecionado = tipoGraficoSelecionado;
		
		switch (tipoGraficoSelecionado) {
		case COLUNA:
			comboBoxDadosGrafico.setModel(new DefaultComboBoxModel<>(TipoDadoGrafico.values()));
			comboBoxDadosGrafico.removeItem(TipoDadoGrafico.MEDIA_CALORIAS_PERDIDAS);
			comboBoxDadosGrafico.removeItem(TipoDadoGrafico.MEDIA_DISTANCIA);
			comboBoxFiltrarExercicio.setEnabled(true);
			break;
		case LINHA:
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.DISTANCIA_PERCORRIDA);
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.CALORIAS_PERDIDAS);
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.PASSOS_DADOS);
			comboBoxFiltrarExercicio.setEnabled(true);
			break;
		case COLUNA_GERAL:
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.PASSOS_DADOS);
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.MEDIA_DISTANCIA);
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.DISTANCIA_PERCORRIDA);
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.MEDIA_CALORIAS_PERDIDAS);
			comboBoxDadosGrafico.addItem(TipoDadoGrafico.CALORIAS_PERDIDAS);
			comboBoxFiltrarExercicio.setSelectedItem("Todos");
			comboBoxFiltrarExercicio.setEnabled(false);
			break;
		}
			
	}

	private void gerarGrafico() {
		Data dataInicial = painelPeriodo.obterDataInicial();
		if(dataInicial == null) {
			MensagemUsuario.msgErro("A data inicial fornecida � inv�lida.");
			return;
		}
		
		Data dataFinal = painelPeriodo.obterDataFinal();
		if(dataFinal == null) {
			MensagemUsuario.msgErro("A data final fornecida � inv�lida.");
			return;
		}
		
		if(relatorioGrafico == null || relatorioGrafico.getDataInicial().compareTo(dataInicial) != 0 
				|| relatorioGrafico.getDataFinal().compareTo(dataFinal) != 0) {
			relatorioGrafico = new RelatorioGrafico(cliente.getExercicios(), dataInicial, dataFinal);
		}
		
		TipoDadoGrafico tipoDadoSelecionado = (TipoDadoGrafico) comboBoxDadosGrafico.getSelectedItem();
		String exercicioFiltrar = (String) comboBoxFiltrarExercicio.getSelectedItem();
		
		if(exercicioFiltrar.equalsIgnoreCase("Todos")) 
			 relatorioGrafico.setFiltrarExercicio(null);
		else relatorioGrafico.setFiltrarExercicio(exercicioFiltrar);
		
		/* Obt�m o gr�fico de acordo com o tipo do gr�fico 
		 * e o tipo dos dados para o gr�fico.*/
		GraficoCategory grafico = null;
		switch (tipoGraficoSelecionado) {
		case COLUNA:
			relatorioGrafico.setTipoGrafico(TipoGrafico.COLUNA);
			relatorioGrafico.setSepararExercicios(true);
			grafico = obterGrafico(TipoGrafico.COLUNA, tipoDadoSelecionado);
			break;
		case LINHA:
			relatorioGrafico.setTipoGrafico(TipoGrafico.LINHA);
			relatorioGrafico.setSepararExercicios(true);
			grafico = obterGrafico(TipoGrafico.LINHA, tipoDadoSelecionado);
			break;
		case COLUNA_GERAL:
			relatorioGrafico.setTipoGrafico(TipoGrafico.COLUNA);
			relatorioGrafico.setSepararExercicios(false);
			grafico = obterGrafico(TipoGrafico.COLUNA, tipoDadoSelecionado);
			break;
		}
		
		adicionarGraficoAoPainel(grafico);
		
		// Atualiza as datas do painel para o caso de terem sido fornecidas invertidas.
		painelPeriodo.textFieldDataInicial.setValue(relatorioGrafico.getDataInicial());
		painelPeriodo.textFieldDataFinal.setValue(relatorioGrafico.getDataFinal());
	}

	private GraficoCategory obterGrafico(TipoGrafico tipoGrafico, TipoDadoGrafico tipoDados) {
		switch (tipoDados) {
		case DISTANCIA_PERCORRIDA:
			return relatorioGrafico.graficoDistanciaPorDia();
		case DURACAO:
			return relatorioGrafico.graficoDuracaoPorDia();
		case CALORIAS_PERDIDAS:
			return relatorioGrafico.graficoCaloriaPorDia();
		case PASSOS_DADOS:
			return relatorioGrafico.graficoPassosPorDia();
		case RITMO_MEDIO:
			return relatorioGrafico.graficoRitmoMedioPorDia();
		case VELOCIDADE_MEDIA:
			return relatorioGrafico.graficoVelocidadeMediaPorDia();
		case MEDIA_DISTANCIA:
			return relatorioGrafico.graficoMediaDistanciaPorDia();
		case MEDIA_CALORIAS_PERDIDAS:
			return relatorioGrafico.graficoMediaCaloriaPorDia();
		default: return null;
		}
	}

	/**
	 * Adiciona o gr�fico passado por par�metro ao painel da 
	 * janela que ir� exibi-lo.
	 * 
	 * @param grafico 	O gr�fico a ser exibido.
	 */
	private void adicionarGraficoAoPainel(GraficoCategory grafico) {
		if(grafico == null) return;
		
		painelExibeGrafico.removeAll();
		JPanel painelGrafico = grafico.obterPainelGrafico();
		painelExibeGrafico.add(painelGrafico, BorderLayout.CENTER);
		painelExibeGrafico.validate();
	}
	
	/**
	 * Op��es dispon�veis para serem exibidas no componente 
	 * da janela que permite escolher o tipo de dado para gerar 
	 * um gr�fico.  
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	private enum TipoDadoGrafico {
		DURACAO("Dura��o"),
		DISTANCIA_PERCORRIDA("Dist�ncia Percorrida"),
		CALORIAS_PERDIDAS("Calorias Perdidas"),
		PASSOS_DADOS("Passos Dados"),
		VELOCIDADE_MEDIA("Velocidade M�dia"),
		RITMO_MEDIO("R�tmo M�dio"), 
		MEDIA_DISTANCIA("M�dia Dist�ncia Percorrida"), 
		MEDIA_CALORIAS_PERDIDAS("M�dias Calorias Perdidas");
		
		private String rotulo;

		private TipoDadoGrafico(String rotulo) {
			this.rotulo = rotulo;
		}

		@Override
		public String toString() { return rotulo; }
	}
	
	/**
	 * Painel que permite a entrada de duas datas pelo usu�rio da 
	 * aplica��o. Datas que se referem a um per�odo, com data inicial 
	 * e data final.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	public static class PainelPeriodo extends JPanel{
		private JFormattedTextField textFieldDataInicial, textFieldDataFinal;

		/**
		 * Cria o painel que exibe os componentes para entrada 
		 * de uma data inicial e data final. 
		 */
		public PainelPeriodo() {
			setBorder(new TitledBorder("Per�odo"));
			setSize(399, 85);
			FlowLayout flowLayout = new FlowLayout(FlowLayout.CENTER, 15, 18);
			setLayout(flowLayout);
			
			
			MaskFormatter mascara;
			try { mascara = new MaskFormatter("##/##/####");
				  mascara.setPlaceholderCharacter('-');
			} catch (ParseException e1) { mascara = new MaskFormatter();}
			
			JLabel labelDataInicial = new JLabel("Data Inicial:");
			labelDataInicial.setPreferredSize(new Dimension(60, 14));
			add(labelDataInicial);
			
			textFieldDataInicial = new JFormattedTextField(mascara);
			textFieldDataInicial.setValue(new Data(LocalDate.now().minusMonths(1)));
			textFieldDataInicial.setPreferredSize(new Dimension(86, 20));
			textFieldDataInicial.setHorizontalAlignment(JTextField.CENTER);
			add(textFieldDataInicial);
			
			JLabel labelDataFinal = new JLabel("Data Final:");
			labelDataFinal.setPreferredSize(new Dimension(53, 14));
			add(labelDataFinal);
			
			textFieldDataFinal = new JFormattedTextField(mascara);
			textFieldDataFinal.setValue(new Data());
			textFieldDataFinal.setPreferredSize(new Dimension(86, 20));
			textFieldDataFinal.setHorizontalAlignment(JTextField.CENTER);
			add(textFieldDataFinal);
		}
		
		/**
		 * Obt�m a data que foi fornecida no campo data inicial do painel.
		 * 
		 * @return Retorna a data se ela foi fornecida, se n�o foi 
		 * fornecida ou � uma data inv�lida retorna <code>null</code>. 
		 */
		public Data obterDataInicial() {
			return converterData(textFieldDataInicial.getText());
		}
		
		/**
		 * Obt�m a data que foi fornecida no campo data final do painel.
		 * 
		 * @return Retorna a data se ela foi fornecida, se n�o foi 
		 * fornecida retorna a data atual. Se for fornecido uma data 
		 * inv�lida retorna <code>null</code>. 
		 */
		public Data obterDataFinal() {
			// Se n�o foi fornecido nada na data final retorna a data atual.
			if(textFieldDataFinal.getValue() == null)
				return new Data();
				
			return converterData(textFieldDataFinal.getText());
		}
		
		private Data converterData(String dataString) {
			try {  return Data.converterParaData(dataString);
			}catch (DateTimeParseException e) { return null; }
		}

		/**
		 * Obtem o componente utilizado para entrada da data inicial.
		 * 
		 * @return Retorna o componente {@link JFormattedTextField} 
		 * da data inical.
		 */
		public JFormattedTextField getTextFieldDataInicial() {
			return textFieldDataInicial;
		}

		/**
		 * Obtem o componente utilizado para entrada da data final.
		 * 
		 * @return Retorna o componente {@link JFormattedTextField} 
		 * da data final.
		 */
		public JFormattedTextField getTextFieldDataFinal() {
			return textFieldDataFinal;
		}

	}
}
