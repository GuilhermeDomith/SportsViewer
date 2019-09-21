package gdrc.sports.gui.janela;

import gdrc.sports.tipo.Cliente;
import gdrc.sports.util.controle.ControleDeClientes;
import gdrc.sports.util.propriedades.Propriedade;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Label;

/**
 * Classe que cria uma janela que permite o usu�rio da aplica��o 
 * pesquisar por um cliente e executar uma a��o sobre o cliente 
 * que for selecionado por ele.<br><br> 
 * 
 * As op��es dispon�veis para executar sobre um cliente selecionado 
 * est�o agrupadas no enum {@link Opcao}. As op��es dispon�veis d�o
 * acesso as sequintes fun��es: <br><br>
 * 
 * {@link Opcao#EXIBIR_INFO} - Permite que o usu�rio da aplica��o pesquise por 
 * um cliente e visualize as informa��es do cliente selecionado. 
 * As informa��es ser�o exibidas por uma janela {@link IgInfoCliente}.<br>
 * 
 * {@link Opcao#GERAR_GRAFICO} - Permite que o usu�rio da aplica��o pesquise por 
 * um cliente e gere os gr�ficos referente ao cliente que for selecionado. 
 * Relat�rio gr�ficos que ser� exibido pela janela {@link IgRelatorioGrafico}.<br>
 * 
 * {@link Opcao#GERAR_RELATORIO} - Permite que o usu�rio da aplica��o pesquise por 
 * um cliente e gere os relat�rios referente ao cliente que for selecionado. 
 * O relat�rio ser� exibido em uma janela {@link IgRelatorioCliente}. <br>
 * 
 * {@link Opcao#EXPORTAR_PDF} - Permite que o usu�rio da aplica��o pesquise por 
 * um cliente e exporte as informa��es e relat�rios referente ao cliente 
 * que for selecionado. Na configura��es para exporta��o do PDF ser�o exibidas 
 * em uma janela {@link IgExportarPDF}. <br>
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class IgPesquisa extends JDialog {
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldPesquisa;
	private JList<Cliente> listClientes;
	
	private ControleDeClientes controleDeClientes;
	private JLabel labelStatusPesquisa;
	private Opcao opcaoPesquisa;

	/**
	 * Cria a janela que permite pesquisar um {@link Cliente} 
	 * no Banco de dados e permite executar uma a��o sobre um 
	 * cliente dependendo segundo par�metro passado.
	 * 
	 * @param location 	O Componente utilizado para posicionar a janela.
	 * @param opcao 	A op�ao que indica qual ser� a func�o que 
	 * o usu�rio ter� acesso ao clicar sobre um cliente.
	 */
	public IgPesquisa(Component location, Opcao opcao) {
		this.controleDeClientes = new ControleDeClientes();
		this.opcaoPesquisa = opcao;
		
		// Configura��es da janela
		setBounds(100, 100, 442, 480);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(opcao.getTituloParaJanela());
		setIconImage(Propriedade.ICONE_APLICACAO);
		setLocationRelativeTo(location);

		// Configura��es do painel principal
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		Label label = new Label(opcao.tituloPainel);
		label.setAlignment(Label.CENTER);
		label.setFont(new Font("Dialog", Font.PLAIN, 18));
		label.setBounds(10, 10, 414, 45);
		contentPanel.add(label);
		
		// Configura��es do painel de pesquisa e seus componentes.
		JPanel painelPesquisa = new JPanel();
		painelPesquisa.setBorder(new TitledBorder((Border)null));
		painelPesquisa.setBounds(10, 61, 414, 76);
		painelPesquisa.setLayout(null);
		contentPanel.add(painelPesquisa);

		JButton btnPesquisar = new JButton("Pesquisar");
		btnPesquisar.setIcon(new ImageIcon(Propriedade.URL_ICONE_PESQUISA_MINI));
		btnPesquisar.addActionListener((ActionEvent e) -> { pesquisarCliente();});
		btnPesquisar.setBounds(294, 31, 110, 34);
		painelPesquisa.add(btnPesquisar);
		
		textFieldPesquisa = new JTextField();
		textFieldPesquisa.setFont(new Font("Tahoma", Font.PLAIN, 16));
		textFieldPesquisa.setBounds(10, 31, 274, 34);
		textFieldPesquisa.addActionListener((ActionEvent e) -> { btnPesquisar.doClick();});
		// Limpa o textField de busca ao come�ar a escrever.
		textFieldPesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) { labelStatusPesquisa.setText(""); }
		});
		painelPesquisa.add(textFieldPesquisa);
		
		JLabel lblNomeDoCliente = new JLabel("Nome do Cliente:");
		lblNomeDoCliente.setBounds(13, 11, 92, 14);
		painelPesquisa.add(lblNomeDoCliente);

		// Configura��es dos componentes para exibir o resultado da pesquisa.
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 177, 414, 231);
		contentPanel.add(scrollPane);

		listClientes = new JList<>();
		listClientes.setFont(new Font("Tahoma", Font.PLAIN, 10));
		listClientes.setCellRenderer(new ListRendererCliente());
		scrollPane.setViewportView(listClientes);
		
		labelStatusPesquisa = new JLabel("");
		labelStatusPesquisa.setHorizontalAlignment(JLabel.RIGHT);
		labelStatusPesquisa.setBounds(208, 148, 216, 14);
		contentPanel.add(labelStatusPesquisa);
		
		JButton btnListarTodos = new JButton("Listar Todos");
		btnListarTodos.setBounds(8, 143, 104, 23);
		btnListarTodos.addActionListener((ActionEvent e) -> {listarTodosClientes();});
		contentPanel.add(btnListarTodos);
		
		// Configura��es do painel de bot�es.
		JPanel painelDeBotoes = new JPanel();
		getContentPane().add(painelDeBotoes, BorderLayout.SOUTH);

		JButton btnOk = new JButton(opcao.getDescricaoDaOpcao());
		btnOk.addActionListener((ActionEvent e) -> {executarAcaoSobreCliente();});
		painelDeBotoes.add(btnOk);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener((ActionEvent e) -> {dispose();});
		painelDeBotoes.add(btnCancelar);
		
		setModal(true);
		setResizable(false);
		setVisible(true);
	}

	/**
	 * Lista todos os clientes da aplica��o no componente {@link JList}
	 * exibido pela janela.
	 */
	private void listarTodosClientes() {
		List<Cliente> clientes = controleDeClientes.listarTodosClientes();
		
		if(clientes.size() == 0) {
			labelStatusPesquisa.setText("Nenhum cliente cadastrado.");
			listClientes.removeAll();
		}
		
		Collections.sort(clientes);
		JList<Cliente> list = new  JList<>(clientes.toArray(new Cliente[0]));
		listClientes.setModel(list.getModel());
	}

	/**
	 * Executa a a��o sobre o cliente selecionado no componente {@link JList}.
	 * A a��o a ser executada depender� da constante {@link Opcao} 
	 * escolhida ao criar a janela. 
	 */
	private void executarAcaoSobreCliente() {
		Cliente cliente = listClientes.getSelectedValue();
		if(cliente == null) return;
		
		cliente = controleDeClientes.buscarClienteComExercicios(cliente.getEmail());
		
		this.setVisible(false);
		switch (opcaoPesquisa) {
		case EXIBIR_INFO: new IgInfoCliente(this, cliente); break;
		case GERAR_RELATORIO: new IgRelatorioCliente(this, cliente); break;
		case GERAR_GRAFICO: new IgRelatorioGrafico(this, cliente); break;
		case EXPORTAR_PDF: new IgExportarPDF(this, cliente); break;
		}
		this.setVisible(true);
	}

	/**
	 * Pesquisa um cliente no banco de dados com o nome que foi 
	 * fornecido pelo usu�rio no componente text field exibido 
	 * na janela.
	 */
	private void pesquisarCliente() {
		// Limpa a pesquisa com um ListModel vazio.
		listClientes.setModel(new DefaultListModel<>());
		
		String nomeCliente = textFieldPesquisa.getText().trim();
		if(nomeCliente.isEmpty()) return;
		
		List<Cliente> clientes = controleDeClientes.buscarClientePorNome(nomeCliente);
		Collections.sort(clientes);
		if(clientes.size() == 0) 
			labelStatusPesquisa.setText("Nenhum cliente encontrado.");
		else {
			JList<Cliente> list = new JList<>(clientes.toArray(new Cliente[0]));
			listClientes.setModel(list.getModel());
		}
	}
	
	/**
	 * Cria um {@link ListCellRenderer} personalizado que permite 
	 * inserir as informa��es resumidas de um {@link Cliente} a fim 
	 * de ser exibido como uma c�lula do componente {@link JList}.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public static class ListRendererCliente implements ListCellRenderer<Cliente>{

		@Override
		public Component getListCellRendererComponent(JList<? extends Cliente> list, Cliente value, int index,
				boolean isSelected, boolean cellHasFocus) {

			DefaultListCellRenderer renderer = new DefaultListCellRenderer();
			JLabel label = (JLabel) renderer.getListCellRendererComponent(list, value, 
													index, isSelected, cellHasFocus);
			label.setText(String.format("<html>%s<br><b>%s</b></html>", value.getNome(), value.getEmail()));
			label.setFont(new Font(label.getFont().getFontName(), Font.PLAIN, 18));
			
			return label;
		}
	} // class ListRendererCliente
	
	/**
	 * Agrupa o conjunto de fun��es que podem ser abertas a partir da janela 
	 * {@link IgPesquisa}. As op�oes abaixo guardam as configura��es que 
	 * ser�o utilizadas pela janela para cada fun��o dispon�vel.
	 *  
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public enum Opcao {
		/** Guarda as configura��es da janela se for necess�rio exibir as informa��es de um cliente.*/
		EXIBIR_INFO("Exibir Informa��es", "Visualizar Informa��es do Cliente", "Selecione um cliente para exibir suas informa��es"),
		/** Guarda as configura��es da janela se for necess�rio gerar um relat�rio para cliente.*/
		GERAR_RELATORIO("Gerar Relat�rio", "Gerar Relat�rio do Cliente", "Selecione um cliente para gerar o seu relat�rio"),
		/** Guarda as configura��es da janela se for necess�rio gerar um relat�rio gr�fico para cliente.*/
		GERAR_GRAFICO("Gerar Gr�fico", "Gerar Gr�fico do Cliente",  "Selecione um cliente para gerar o relat�rio gr�fico"), 
		/** Guarda as configura��es da janela se for necess�rio exportar os dados do cliente para PDF.*/
		EXPORTAR_PDF("Exportar Relat�rio", "Exportar Relat�rio do Cliente",  "Selecione um cliente para exportar seus dados");
		
		private String descricaoDaFuncao, tituloParaJanela, tituloPainel;

		private Opcao(String opcao, String titulo, String tituloPainel) {
			this.descricaoDaFuncao = opcao;
			this.tituloParaJanela = titulo;
			this.tituloPainel = tituloPainel;
		}

		/**
		 * Obt�m a descri��o da op��o referente a constante escolhida.
		 *  
		 * @return Retorna a descri��o da op��o.
		 */
		public String getDescricaoDaOpcao() {
			return descricaoDaFuncao;
		}

		/**
		 * Obt�m o titulo para janela referente a constante escolhida.
		 *  
		 * @return Retorna o t�tulo para janela.
		 */
		public String getTituloParaJanela() {
			return tituloParaJanela;
		}

		/**
		 * Obt�m o titulo para o painel referente a constante escolhida.
		 *  
		 * @return Retorna o t�tulo para o painel.
		 */
		public String getTituloPainel() {
			return tituloPainel;
		}
	} // enum Opcao
}
