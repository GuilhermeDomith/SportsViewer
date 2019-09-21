package gdrc.sports.gui.janela;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import gdrc.sports.gui.es.MensagemUsuario;
import gdrc.sports.gui.janela.IgRelatorioGrafico.PainelPeriodo;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.util.propriedades.Propriedade;
import gdrc.sports.util.relatorio.RelatorioCliente;
import gdrc.sports.util.relatorio.RelatorioGrafico;
import gdrc.sports.util.relatorio.RelatorioPDF;

/**
 * Classe que cria uma janela para exibi��o de todos os 
 * componentes que permitem o usu�rio da aplica��o selecionar as 
 * configura��es do rel�t�rio a ser exportado para PDF.
 *  
 * @author Guilherme Domith Ribeiro Coelho
 */
public class IgExportarPDF extends JDialog {
	private final JPanel contentPanel = new JPanel();
	
	private PainelPeriodo painelPeriodo;
	private JComboBox<String> comboBoxExercicioFiltrar;
	private JCheckBox checkBoxInfoCliente;
	private JCheckBox checkBoxRelatorioCliente;
	private JCheckBox checkBoxGraficoColunas;
	private JCheckBox checkBoxGraficoLinhas;
	private JCheckBox checkBoxGraficoColunasGeral;
	private JCheckBox checkBoxAbrirExternamente;
	
	private Cliente cliente;
	private RelatorioCliente relatorioCliente;
	private RelatorioGrafico relatorioGrafico;
	
	
	/**
	 * Cria a janela que permite o usu�rio exportar um relat�rio
	 * para um documento PDF a partir dos dados do cliente passado por
	 * par�metro. Com este construtor a janela n�o ser� exibida, sendo 
	 * necess�rio utilizar o m�todo {@link #setVisible(boolean)} quando 
	 * for preciso.
	 * 
	 * @param cliente	O objeto {@link Cliente} que ter�o seus dados e 
	 * relat�rios exportados para PDF. 
	 */
	public IgExportarPDF(Cliente cliente) {
		this.cliente = cliente;
		
		// Configura��o da janela.
		setBounds(100, 100, 438, 419);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(Propriedade.ICONE_APLICACAO);
		setTitle("Exportar para PDF");
		
		// Configura��o do painel principal.
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		//Configura��o do painel para selecionar os dados para exportar.
		JPanel painelDadosExportar = new JPanel();
		painelDadosExportar.setBorder(new TitledBorder("Dados a Exportar"));
		painelDadosExportar.setBounds(10, 11, 399, 335);
		contentPanel.add(painelDadosExportar);
		painelDadosExportar.setLayout(null);
		
		checkBoxInfoCliente = new JCheckBox("Informa��es do Cliente");
		checkBoxInfoCliente.setBounds(32, 28, 192, 23);
		painelDadosExportar.add(checkBoxInfoCliente);
		
		checkBoxRelatorioCliente = new JCheckBox("Relat�rio do Cliente");
		checkBoxRelatorioCliente.setBounds(32, 54, 192, 23);
		painelDadosExportar.add(checkBoxRelatorioCliente);
		
		JPanel painelRelatorioGrafico = new JPanel();
		painelRelatorioGrafico.setBounds(10, 93, 379, 231);
		painelRelatorioGrafico.setBorder(new TitledBorder("Relat�rio Gr�fico"));
		painelDadosExportar.add(painelRelatorioGrafico);
		painelRelatorioGrafico.setLayout(null);
		
		checkBoxGraficoColunas = new JCheckBox("Relat�rio Gr�fico de Colunas");
		checkBoxGraficoColunas.setBounds(11, 24, 192, 23);
		checkBoxGraficoColunas.addActionListener((ActionEvent e) -> { 
			habilitarDesabilitarPainelRelatorio();
		});
		painelRelatorioGrafico.add(checkBoxGraficoColunas);
		
		checkBoxGraficoLinhas = new JCheckBox("Relat�rio Gr�fico de Linhas");
		checkBoxGraficoLinhas.setBounds(11, 50, 192, 23);
		checkBoxGraficoLinhas.addActionListener((ActionEvent e) -> { 
			habilitarDesabilitarPainelRelatorio();
		});
		painelRelatorioGrafico.add(checkBoxGraficoLinhas);
		
		checkBoxGraficoColunasGeral = new JCheckBox("Relat�rio Gr�fico de Colunas Geral");
		checkBoxGraficoColunasGeral.setBounds(11, 76, 192, 23);
		checkBoxGraficoColunasGeral.addActionListener((ActionEvent e) -> { 
			habilitarDesabilitarPainelRelatorio();
		});
		painelRelatorioGrafico.add(checkBoxGraficoColunasGeral);
		
		JLabel labelFiltrarExercicio = new JLabel("Exerc�cio para os gr�ficos:");
		labelFiltrarExercicio.setBounds(21, 109, 148, 14);
		painelRelatorioGrafico.add(labelFiltrarExercicio);
		
		comboBoxExercicioFiltrar = new JComboBox<>(new Vector<>(cliente.obterNomesExercicios()));
		comboBoxExercicioFiltrar.setBounds(161, 106, 177, 20);
		painelRelatorioGrafico.add(comboBoxExercicioFiltrar);
		comboBoxExercicioFiltrar.addItem("Todos");
		comboBoxExercicioFiltrar.setSelectedItem("Todos");
		
		JLabel labelInformacao = new JLabel("(Considera todos os exerc�cios)");
		labelInformacao.setBounds(209, 80, 167, 14);
		painelRelatorioGrafico.add(labelInformacao);
		
		//Configura��o do painel para selecionar o per�odo.
		painelPeriodo = new PainelPeriodo();
		painelPeriodo.setBounds(10, 139, 358, 85);
		painelRelatorioGrafico.add(painelPeriodo);
		
		//Configura��o do painel de bot�es.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		checkBoxAbrirExternamente = new JCheckBox("Abrir documento ap�s exportar");
		checkBoxAbrirExternamente.setMargin(new Insets(2, 2, 2, 85));
		checkBoxAbrirExternamente.setSelected(true);
		buttonPane.add(checkBoxAbrirExternamente);
		
		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener((ActionEvent e) -> { dispose(); });
		
		JButton buttonExportar = new JButton("Exportar");
		buttonExportar.addActionListener((ActionEvent e) -> { exportarPDF(); });
		buttonPane.add(buttonExportar);
		buttonPane.add(cancelButton);
		
		setModal(true);
		setResizable(false);
	}
	
	/**
	 * Habilita o painel e componentes que permite configurar o relat�rio 
	 * gr�fico se alguma dos checkbox do relat�rio gr�fico estiver marcado.
	 * Se n�o, desabilita os mesmos.  
	 */
	public void habilitarDesabilitarPainelRelatorio() {
		boolean relatorioGraficoHabilitado = false;
		if(checkBoxGraficoColunas.isSelected() 
				|| checkBoxGraficoLinhas.isSelected()
				|| checkBoxGraficoColunasGeral.isSelected()) {
			relatorioGraficoHabilitado = true;
		}
		
		painelPeriodo.setEnabled(relatorioGraficoHabilitado);
		painelPeriodo.getTextFieldDataInicial().setEnabled(relatorioGraficoHabilitado);
		painelPeriodo.getTextFieldDataFinal().setEnabled(relatorioGraficoHabilitado);
		comboBoxExercicioFiltrar.setEnabled(relatorioGraficoHabilitado);
	}

	/**
	 * Cria e exibe a janela que permite o usu�rio exportar um relat�rio
	 * para um documento PDF a partir dos dados do cliente passado por
	 * par�metro.
	 * 
	 * @param location	O componente utilizado para centralizar a janela. 
	 * @param cliente	O objeto {@link Cliente} que ter�o seus dados e 
	 * relat�rios exportados para PDF. 
	 */
	public IgExportarPDF(Component location, Cliente cliente) {
		this(cliente);
		
		checkBoxInfoCliente.setSelected(true);
		checkBoxGraficoColunas.setSelected(true);
		checkBoxGraficoLinhas.setSelected(true);
		checkBoxGraficoColunasGeral.setSelected(true);
		checkBoxRelatorioCliente.setSelected(true);
		setLocationRelativeTo(location);
		setVisible(true);
	}

	private void exportarPDF() {
		// Obt�m o relat�rio gr�fico se foi selecionado para exportar.
		if(checkBoxGraficoColunas.isSelected() 
			|| checkBoxGraficoLinhas.isSelected()
			|| checkBoxGraficoColunasGeral.isSelected()) {
			
			relatorioGrafico = obterRelatorioGrafico();
			if(relatorioGrafico == null) return;
		}
		
		String dataEHora = formatarDataParaNomedeArquivo(Calendar.getInstance());
		String nomeArquivo = String.format("Relat�rio_%s (%s).pdf", cliente.getNome(), dataEHora);
		File file = fileChooserCriarArquivoPDF(this, nomeArquivo);
		if(file == null) return;
		
		try {
			
			RelatorioPDF relatorioPDF = new RelatorioPDF(file);
			relatorioPDF.escreverCabecalho(cliente, dataEHora);
			if(checkBoxInfoCliente.isSelected())
				relatorioPDF.escreverInformacaoCliente(cliente);
			
			escreverRelatorioCliente(relatorioPDF);
			escreverRelatorioGrafico(relatorioPDF);
						
			relatorioPDF.fechar();
		} catch (FileNotFoundException e) {
			MensagemUsuario.msgErro(String.format("%s\nO arquivo n�o p�de ser encontrado "
					+ "no caminho especificado ou est� sendo \nutilizado por outro programa.", 
					file.getAbsolutePath()));
			return;
		} catch (IOException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao exportar o relat�rio.");
			return;
		}
		
		if(checkBoxAbrirExternamente.isSelected()) abrirDocumentoExternamente(file);
	}
	
	
	private void escreverRelatorioGrafico(RelatorioPDF relatorioPDF) throws IOException {
		if(relatorioGrafico != null) {
			if(checkBoxGraficoColunas.isSelected())
				relatorioPDF.escreverRelatorioGraficoColuna(relatorioGrafico);
			if(checkBoxGraficoLinhas.isSelected())
				relatorioPDF.escreverRelatorioGraficoLinha(relatorioGrafico);
			if(checkBoxGraficoColunasGeral.isSelected())
				relatorioPDF.escreverRelatorioGraficoColunaGeral(relatorioGrafico);
		}
	}

	private void escreverRelatorioCliente(RelatorioPDF relatorioPDF) throws IOException {
		if(checkBoxRelatorioCliente.isSelected()) {
			if(relatorioCliente == null)
				relatorioCliente = new RelatorioCliente(cliente.getExercicios());
			
			relatorioPDF.escreverRelatorioCliente(relatorioCliente);
		}
	}

	/**
	 * Obt�m o relat�rio do cliente que ser� exportado para PDF.
	 * Cria o novo relat�rio com os dados que o usu�rios selecionou
	 * na janela criada por esta classe.
	 * 
	 * @return Retorna o relat�rio do cliente configurado com os 
	 * dados fornecidos na janela.
	 */
	private RelatorioGrafico obterRelatorioGrafico() {

		Data dataInicial = painelPeriodo.obterDataInicial();
		if(dataInicial == null) {
			MensagemUsuario.msgInfo("A data inicial fornecida � inv�lida.");
			return null;
		}

		Data dataFinal = painelPeriodo.obterDataFinal();
		if(dataFinal == null) {
			MensagemUsuario.msgInfo("A data final fornecida � inv�lida.");
			return null;
		}

		// Verifica se o relat�rio j� foi criado.
		if(relatorioGrafico == null || relatorioGrafico.getDataInicial().compareTo(dataInicial) != 0 
				|| relatorioGrafico.getDataFinal().compareTo(dataFinal) != 0) {
			relatorioGrafico = new RelatorioGrafico(cliente.getExercicios(), dataInicial, dataFinal);
		}

		// Atualiza as datas do painel para o caso de terem sido fornecidas invertidas.
		painelPeriodo.getTextFieldDataInicial().setValue(relatorioGrafico.getDataInicial());
		painelPeriodo.getTextFieldDataFinal().setValue(relatorioGrafico.getDataFinal());

		String exercicioFiltrar = (String)comboBoxExercicioFiltrar.getSelectedItem();
		relatorioGrafico.setFiltrarExercicio((exercicioFiltrar.equals("Todos")? null : exercicioFiltrar));
		return relatorioGrafico;
	}

	
	/**
	 * Formata a data passada por par�metro em um <code>String</code> no
	 * formato que possa ser escrita no nome de um arquivo. Ou seja, no formato
	 * de data e hora sem caracteres especiais.
	 * 
	 * @param data A data a ser formatada em string.
	 * @return Retorna a data em <code>String</code> formatada.
	 */
	public static String formatarDataParaNomedeArquivo(Calendar data) {
		return String.format("%1$td de %1$tB de %1$tY - %1$tHh %1$tMmin", data);
	}

	

	/**
	 * Exibe a janela que permite ao usu�rio criar um arquivo
	 * no sistema de arquivos do sistema operacional para salvar 
	 * o pdf que ser� exportado pela aplica��o.
	 * 
	 * @param location 		O componente utilizado para centralizar a janela.
	 * @param nomePadrao 	O nome do arquivo a ser exibido como sugest�o para 
	 * o usu�rio
	 * 
	 * @return Retorna o arquivo criado pelo usu�rio. Caso o usu�rio
	 * cancele a opera��o retorna <code>null</code>.
	 */
	public static File fileChooserCriarArquivoPDF(Component location, String nomePadrao) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fileChooser.setDialogTitle("Salvar Relat�rio PDF");
		fileChooser.setSelectedFile(new File(nomePadrao));
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));
		
		if(fileChooser.showSaveDialog(location) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile(); 
			//Verifica se o usu�rio forneceu um nome sem a exten��o.
			if(!file.getName().contains(".pdf")) 
				file = new File(file.getAbsolutePath()+".pdf");
				
			return file;
		}
		
		return null;
	}
	
	/**
	 * Abre o documento passado por par�metro com a aplica��o 
	 * definido padr�o definida no sistema operacional do usu�rio para
	 * o certo tipo de arquivo.
	 * 
	 * @param arquivo 	O arquivo que ser� aberto por outro 
	 * programa do sistema operacional.
	 * 
	 * @return Retorna <code>true</code> se o documento p�de 
	 * ser aberto corretamente.
	 */
	public static boolean abrirDocumentoExternamente(File arquivo){
		Desktop desktop = Desktop.getDesktop();
		try { desktop.open(arquivo);
		} catch (IOException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao tentar abrir o documento "+arquivo.getName());
			return false;
		}
		return true;
	}

	/**
	 * Obt�m o checkbox da janela que indica se a Informa��o do 
	 * cliente inserido na classe deve ou n�o ser exportada.
	 * 
	 * @return Retorna a refer�ncia do checkbox da janela.
	 */
	public JCheckBox getCheckBoxInfoCliente() {
		return checkBoxInfoCliente;
	}

	/**
	 * Obt�m o checkbox da janela que indica se o relat�rio do 
	 * cliente inserido na classe deve ou n�o ser exportado.
	 * 
	 * @return Retorna a refer�ncia do checkbox da janela.
	 */
	public JCheckBox getCheckBoxRelatorioCliente() {
		return checkBoxRelatorioCliente;
	}

	/**
	 * Obt�m o checkbox da janela que indica se o relat�rio gr�fico 
	 * de colunas do cliente inserido na classe deve ou n�o 
	 * ser exportado.
	 * 
	 * @return Retorna a refer�ncia do checkbox da janela.
	 */
	public JCheckBox getCheckBoxGraficoColunas() {
		return checkBoxGraficoColunas;
	}

	/**
	 * Obt�m o checkbox da janela que indica se o relat�rio gr�fico 
	 * de linhas do cliente inserido na classe deve ou n�o 
	 * ser exportado.
	 * 
	 * @return Retorna a refer�ncia do checkbox da janela.
	 */
	public JCheckBox getCheckBoxGraficoLinhas() {
		return checkBoxGraficoLinhas;
	}

	/**
	 * Obt�m o checkbox da janela que indica se o relat�rio gr�fico 
	 * de colunas geral do cliente inserido na classe deve ou n�o 
	 * ser exportado.
	 * 
	 * @return Retorna a refer�ncia do checkbox da janela.
	 */
	public JCheckBox getCheckBoxGraficoColunasGeral() {
		return checkBoxGraficoColunasGeral;
	}

	/**
	 * Obt�m o painel que mant�m as datas do per�odo, que deve ser 
	 * fornecido pelo usu�rio da aplica��o, para gerar um relat�rio
	 * gr�fico.
	 * 
	 * @return Retorna a refer�ncia do painel de per�odo.
	 */
	public PainelPeriodo getPainelPeriodo() {
		return painelPeriodo;
	}

	/**
	 * Obt�m o combobox da janela que indica se qual o exerc�cio a ser 
	 * considerado ao gerar um relat�rio gr�fico do cliente inserido na 
	 * classe.
	 * 
	 * @return Retorna a refer�ncia do combobox da janela.
	 */
	public JComboBox<String> getComboBoxExercicioFiltrar() {
		return comboBoxExercicioFiltrar;
	}

	/**
	 * Obt�m o relat�rio do cliente que ser� exportado.  
	 * 
	 * @return Retorna o relat�rio do cliente. Caso n�o ainda 
	 * tenha sido gerado ou n�o foi inserido nenhum relat�rio, 
	 * retorna <code>null</code>. 
	 */
	public RelatorioCliente getRelatorioCliente() {
		return relatorioCliente;
	}

	/**
	 * Insere o relat�rio do cliente que deve ser exportado.
	 * Pode ser utilizado para evitar de que um relat�ria seja 
	 * gerado novamente caso j� tenha sido criado antes.
	 * 
	 * @param relatorioCliente 	O relat�rio do cliente inserido 
	 * na classe que deve ser exportado.
	 */
	public void setRelatorioCliente(RelatorioCliente relatorioCliente) {
		this.relatorioCliente = relatorioCliente;
	}

	/**
	 * Obt�m o relat�rio gr�fico do cliente que ser� exportado.  
	 * 
	 * @return Retorna o relat�rio gr�fico. Caso n�o ainda 
	 * tenha sido gerado ou n�o foi inserido nenhum relat�rio, 
	 * retorna <code>null</code>. 
	 */
	public RelatorioGrafico getRelatorioGrafico() {
		return relatorioGrafico;
	}

	/**
	 * Insere o relat�rio gr�fico do cliente que deve ser exportado.
	 * Pode ser utilizado para evitar de que um relat�ria gr�fico 
	 * seja gerado novamente caso j� tenha sido criado antes.
	 * 
	 * @param relatorioGrafico 	O relat�rio gr�fico do cliente 
	 * inserido na classe que deve ser exportado.
	 */
	public void setRelatorioGrafico(RelatorioGrafico relatorioGrafico) {
		this.relatorioGrafico = relatorioGrafico;
	}
}
