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
 * Classe que cria uma janela para exibição de todos os 
 * componentes que permitem o usuário da aplicação selecionar as 
 * configurações do relátório a ser exportado para PDF.
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
	 * Cria a janela que permite o usuário exportar um relatório
	 * para um documento PDF a partir dos dados do cliente passado por
	 * parâmetro. Com este construtor a janela não será exibida, sendo 
	 * necessário utilizar o método {@link #setVisible(boolean)} quando 
	 * for preciso.
	 * 
	 * @param cliente	O objeto {@link Cliente} que terão seus dados e 
	 * relatórios exportados para PDF. 
	 */
	public IgExportarPDF(Cliente cliente) {
		this.cliente = cliente;
		
		// Configuração da janela.
		setBounds(100, 100, 438, 419);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(Propriedade.ICONE_APLICACAO);
		setTitle("Exportar para PDF");
		
		// Configuração do painel principal.
		contentPanel.setLayout(null);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		//Configuração do painel para selecionar os dados para exportar.
		JPanel painelDadosExportar = new JPanel();
		painelDadosExportar.setBorder(new TitledBorder("Dados a Exportar"));
		painelDadosExportar.setBounds(10, 11, 399, 335);
		contentPanel.add(painelDadosExportar);
		painelDadosExportar.setLayout(null);
		
		checkBoxInfoCliente = new JCheckBox("Informações do Cliente");
		checkBoxInfoCliente.setBounds(32, 28, 192, 23);
		painelDadosExportar.add(checkBoxInfoCliente);
		
		checkBoxRelatorioCliente = new JCheckBox("Relatório do Cliente");
		checkBoxRelatorioCliente.setBounds(32, 54, 192, 23);
		painelDadosExportar.add(checkBoxRelatorioCliente);
		
		JPanel painelRelatorioGrafico = new JPanel();
		painelRelatorioGrafico.setBounds(10, 93, 379, 231);
		painelRelatorioGrafico.setBorder(new TitledBorder("Relatório Gráfico"));
		painelDadosExportar.add(painelRelatorioGrafico);
		painelRelatorioGrafico.setLayout(null);
		
		checkBoxGraficoColunas = new JCheckBox("Relatório Gráfico de Colunas");
		checkBoxGraficoColunas.setBounds(11, 24, 192, 23);
		checkBoxGraficoColunas.addActionListener((ActionEvent e) -> { 
			habilitarDesabilitarPainelRelatorio();
		});
		painelRelatorioGrafico.add(checkBoxGraficoColunas);
		
		checkBoxGraficoLinhas = new JCheckBox("Relatório Gráfico de Linhas");
		checkBoxGraficoLinhas.setBounds(11, 50, 192, 23);
		checkBoxGraficoLinhas.addActionListener((ActionEvent e) -> { 
			habilitarDesabilitarPainelRelatorio();
		});
		painelRelatorioGrafico.add(checkBoxGraficoLinhas);
		
		checkBoxGraficoColunasGeral = new JCheckBox("Relatório Gráfico de Colunas Geral");
		checkBoxGraficoColunasGeral.setBounds(11, 76, 192, 23);
		checkBoxGraficoColunasGeral.addActionListener((ActionEvent e) -> { 
			habilitarDesabilitarPainelRelatorio();
		});
		painelRelatorioGrafico.add(checkBoxGraficoColunasGeral);
		
		JLabel labelFiltrarExercicio = new JLabel("Exercício para os gráficos:");
		labelFiltrarExercicio.setBounds(21, 109, 148, 14);
		painelRelatorioGrafico.add(labelFiltrarExercicio);
		
		comboBoxExercicioFiltrar = new JComboBox<>(new Vector<>(cliente.obterNomesExercicios()));
		comboBoxExercicioFiltrar.setBounds(161, 106, 177, 20);
		painelRelatorioGrafico.add(comboBoxExercicioFiltrar);
		comboBoxExercicioFiltrar.addItem("Todos");
		comboBoxExercicioFiltrar.setSelectedItem("Todos");
		
		JLabel labelInformacao = new JLabel("(Considera todos os exercícios)");
		labelInformacao.setBounds(209, 80, 167, 14);
		painelRelatorioGrafico.add(labelInformacao);
		
		//Configuração do painel para selecionar o período.
		painelPeriodo = new PainelPeriodo();
		painelPeriodo.setBounds(10, 139, 358, 85);
		painelRelatorioGrafico.add(painelPeriodo);
		
		//Configuração do painel de botões.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		checkBoxAbrirExternamente = new JCheckBox("Abrir documento após exportar");
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
	 * Habilita o painel e componentes que permite configurar o relatório 
	 * gráfico se alguma dos checkbox do relatório gráfico estiver marcado.
	 * Se não, desabilita os mesmos.  
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
	 * Cria e exibe a janela que permite o usuário exportar um relatório
	 * para um documento PDF a partir dos dados do cliente passado por
	 * parâmetro.
	 * 
	 * @param location	O componente utilizado para centralizar a janela. 
	 * @param cliente	O objeto {@link Cliente} que terão seus dados e 
	 * relatórios exportados para PDF. 
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
		// Obtém o relatório gráfico se foi selecionado para exportar.
		if(checkBoxGraficoColunas.isSelected() 
			|| checkBoxGraficoLinhas.isSelected()
			|| checkBoxGraficoColunasGeral.isSelected()) {
			
			relatorioGrafico = obterRelatorioGrafico();
			if(relatorioGrafico == null) return;
		}
		
		String dataEHora = formatarDataParaNomedeArquivo(Calendar.getInstance());
		String nomeArquivo = String.format("Relatório_%s (%s).pdf", cliente.getNome(), dataEHora);
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
			MensagemUsuario.msgErro(String.format("%s\nO arquivo não pôde ser encontrado "
					+ "no caminho especificado ou está sendo \nutilizado por outro programa.", 
					file.getAbsolutePath()));
			return;
		} catch (IOException e) {
			MensagemUsuario.msgErro("Ocorreu um erro ao exportar o relatório.");
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
	 * Obtém o relatório do cliente que será exportado para PDF.
	 * Cria o novo relatório com os dados que o usuários selecionou
	 * na janela criada por esta classe.
	 * 
	 * @return Retorna o relatório do cliente configurado com os 
	 * dados fornecidos na janela.
	 */
	private RelatorioGrafico obterRelatorioGrafico() {

		Data dataInicial = painelPeriodo.obterDataInicial();
		if(dataInicial == null) {
			MensagemUsuario.msgInfo("A data inicial fornecida é inválida.");
			return null;
		}

		Data dataFinal = painelPeriodo.obterDataFinal();
		if(dataFinal == null) {
			MensagemUsuario.msgInfo("A data final fornecida é inválida.");
			return null;
		}

		// Verifica se o relatório já foi criado.
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
	 * Formata a data passada por parâmetro em um <code>String</code> no
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
	 * Exibe a janela que permite ao usuário criar um arquivo
	 * no sistema de arquivos do sistema operacional para salvar 
	 * o pdf que será exportado pela aplicação.
	 * 
	 * @param location 		O componente utilizado para centralizar a janela.
	 * @param nomePadrao 	O nome do arquivo a ser exibido como sugestão para 
	 * o usuário
	 * 
	 * @return Retorna o arquivo criado pelo usuário. Caso o usuário
	 * cancele a operação retorna <code>null</code>.
	 */
	public static File fileChooserCriarArquivoPDF(Component location, String nomePadrao) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fileChooser.setDialogTitle("Salvar Relatório PDF");
		fileChooser.setSelectedFile(new File(nomePadrao));
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.pdf", "pdf"));
		
		if(fileChooser.showSaveDialog(location) == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile(); 
			//Verifica se o usuário forneceu um nome sem a extenção.
			if(!file.getName().contains(".pdf")) 
				file = new File(file.getAbsolutePath()+".pdf");
				
			return file;
		}
		
		return null;
	}
	
	/**
	 * Abre o documento passado por parâmetro com a aplicação 
	 * definido padrão definida no sistema operacional do usuário para
	 * o certo tipo de arquivo.
	 * 
	 * @param arquivo 	O arquivo que será aberto por outro 
	 * programa do sistema operacional.
	 * 
	 * @return Retorna <code>true</code> se o documento pôde 
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
	 * Obtém o checkbox da janela que indica se a Informação do 
	 * cliente inserido na classe deve ou não ser exportada.
	 * 
	 * @return Retorna a referência do checkbox da janela.
	 */
	public JCheckBox getCheckBoxInfoCliente() {
		return checkBoxInfoCliente;
	}

	/**
	 * Obtém o checkbox da janela que indica se o relatório do 
	 * cliente inserido na classe deve ou não ser exportado.
	 * 
	 * @return Retorna a referência do checkbox da janela.
	 */
	public JCheckBox getCheckBoxRelatorioCliente() {
		return checkBoxRelatorioCliente;
	}

	/**
	 * Obtém o checkbox da janela que indica se o relatório gráfico 
	 * de colunas do cliente inserido na classe deve ou não 
	 * ser exportado.
	 * 
	 * @return Retorna a referência do checkbox da janela.
	 */
	public JCheckBox getCheckBoxGraficoColunas() {
		return checkBoxGraficoColunas;
	}

	/**
	 * Obtém o checkbox da janela que indica se o relatório gráfico 
	 * de linhas do cliente inserido na classe deve ou não 
	 * ser exportado.
	 * 
	 * @return Retorna a referência do checkbox da janela.
	 */
	public JCheckBox getCheckBoxGraficoLinhas() {
		return checkBoxGraficoLinhas;
	}

	/**
	 * Obtém o checkbox da janela que indica se o relatório gráfico 
	 * de colunas geral do cliente inserido na classe deve ou não 
	 * ser exportado.
	 * 
	 * @return Retorna a referência do checkbox da janela.
	 */
	public JCheckBox getCheckBoxGraficoColunasGeral() {
		return checkBoxGraficoColunasGeral;
	}

	/**
	 * Obtém o painel que mantém as datas do período, que deve ser 
	 * fornecido pelo usuário da aplicação, para gerar um relatório
	 * gráfico.
	 * 
	 * @return Retorna a referência do painel de período.
	 */
	public PainelPeriodo getPainelPeriodo() {
		return painelPeriodo;
	}

	/**
	 * Obtém o combobox da janela que indica se qual o exercício a ser 
	 * considerado ao gerar um relatório gráfico do cliente inserido na 
	 * classe.
	 * 
	 * @return Retorna a referência do combobox da janela.
	 */
	public JComboBox<String> getComboBoxExercicioFiltrar() {
		return comboBoxExercicioFiltrar;
	}

	/**
	 * Obtém o relatório do cliente que será exportado.  
	 * 
	 * @return Retorna o relatório do cliente. Caso não ainda 
	 * tenha sido gerado ou não foi inserido nenhum relatório, 
	 * retorna <code>null</code>. 
	 */
	public RelatorioCliente getRelatorioCliente() {
		return relatorioCliente;
	}

	/**
	 * Insere o relatório do cliente que deve ser exportado.
	 * Pode ser utilizado para evitar de que um relatória seja 
	 * gerado novamente caso já tenha sido criado antes.
	 * 
	 * @param relatorioCliente 	O relatório do cliente inserido 
	 * na classe que deve ser exportado.
	 */
	public void setRelatorioCliente(RelatorioCliente relatorioCliente) {
		this.relatorioCliente = relatorioCliente;
	}

	/**
	 * Obtém o relatório gráfico do cliente que será exportado.  
	 * 
	 * @return Retorna o relatório gráfico. Caso não ainda 
	 * tenha sido gerado ou não foi inserido nenhum relatório, 
	 * retorna <code>null</code>. 
	 */
	public RelatorioGrafico getRelatorioGrafico() {
		return relatorioGrafico;
	}

	/**
	 * Insere o relatório gráfico do cliente que deve ser exportado.
	 * Pode ser utilizado para evitar de que um relatória gráfico 
	 * seja gerado novamente caso já tenha sido criado antes.
	 * 
	 * @param relatorioGrafico 	O relatório gráfico do cliente 
	 * inserido na classe que deve ser exportado.
	 */
	public void setRelatorioGrafico(RelatorioGrafico relatorioGrafico) {
		this.relatorioGrafico = relatorioGrafico;
	}
}
