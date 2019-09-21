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
 * Classe que cria uma janela que permite o usuário da aplicação 
 * pesquisar por um cliente e executar uma ação sobre o cliente 
 * que for selecionado por ele.<br><br> 
 * 
 * As opções disponíveis para executar sobre um cliente selecionado 
 * estão agrupadas no enum {@link Opcao}. As opções disponíveis dão
 * acesso as sequintes funções: <br><br>
 * 
 * {@link Opcao#EXIBIR_INFO} - Permite que o usuário da aplicação pesquise por 
 * um cliente e visualize as informações do cliente selecionado. 
 * As informações serão exibidas por uma janela {@link IgInfoCliente}.<br>
 * 
 * {@link Opcao#GERAR_GRAFICO} - Permite que o usuário da aplicação pesquise por 
 * um cliente e gere os gráficos referente ao cliente que for selecionado. 
 * Relatório gráficos que será exibido pela janela {@link IgRelatorioGrafico}.<br>
 * 
 * {@link Opcao#GERAR_RELATORIO} - Permite que o usuário da aplicação pesquise por 
 * um cliente e gere os relatórios referente ao cliente que for selecionado. 
 * O relatório será exibido em uma janela {@link IgRelatorioCliente}. <br>
 * 
 * {@link Opcao#EXPORTAR_PDF} - Permite que o usuário da aplicação pesquise por 
 * um cliente e exporte as informações e relatórios referente ao cliente 
 * que for selecionado. Na configurações para exportação do PDF serão exibidas 
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
	 * no Banco de dados e permite executar uma ação sobre um 
	 * cliente dependendo segundo parâmetro passado.
	 * 
	 * @param location 	O Componente utilizado para posicionar a janela.
	 * @param opcao 	A opçao que indica qual será a funcão que 
	 * o usuário terá acesso ao clicar sobre um cliente.
	 */
	public IgPesquisa(Component location, Opcao opcao) {
		this.controleDeClientes = new ControleDeClientes();
		this.opcaoPesquisa = opcao;
		
		// Configurações da janela
		setBounds(100, 100, 442, 480);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(opcao.getTituloParaJanela());
		setIconImage(Propriedade.ICONE_APLICACAO);
		setLocationRelativeTo(location);

		// Configurações do painel principal
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		Label label = new Label(opcao.tituloPainel);
		label.setAlignment(Label.CENTER);
		label.setFont(new Font("Dialog", Font.PLAIN, 18));
		label.setBounds(10, 10, 414, 45);
		contentPanel.add(label);
		
		// Configurações do painel de pesquisa e seus componentes.
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
		// Limpa o textField de busca ao começar a escrever.
		textFieldPesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) { labelStatusPesquisa.setText(""); }
		});
		painelPesquisa.add(textFieldPesquisa);
		
		JLabel lblNomeDoCliente = new JLabel("Nome do Cliente:");
		lblNomeDoCliente.setBounds(13, 11, 92, 14);
		painelPesquisa.add(lblNomeDoCliente);

		// Configurações dos componentes para exibir o resultado da pesquisa.
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
		
		// Configurações do painel de botões.
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
	 * Lista todos os clientes da aplicação no componente {@link JList}
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
	 * Executa a ação sobre o cliente selecionado no componente {@link JList}.
	 * A ação a ser executada dependerá da constante {@link Opcao} 
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
	 * fornecido pelo usuário no componente text field exibido 
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
	 * inserir as informações resumidas de um {@link Cliente} a fim 
	 * de ser exibido como uma célula do componente {@link JList}.
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
	 * Agrupa o conjunto de funções que podem ser abertas a partir da janela 
	 * {@link IgPesquisa}. As opçoes abaixo guardam as configurações que 
	 * serão utilizadas pela janela para cada função disponível.
	 *  
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	public enum Opcao {
		/** Guarda as configurações da janela se for necessário exibir as informações de um cliente.*/
		EXIBIR_INFO("Exibir Informações", "Visualizar Informações do Cliente", "Selecione um cliente para exibir suas informações"),
		/** Guarda as configurações da janela se for necessário gerar um relatório para cliente.*/
		GERAR_RELATORIO("Gerar Relatório", "Gerar Relatório do Cliente", "Selecione um cliente para gerar o seu relatório"),
		/** Guarda as configurações da janela se for necessário gerar um relatório gráfico para cliente.*/
		GERAR_GRAFICO("Gerar Gráfico", "Gerar Gráfico do Cliente",  "Selecione um cliente para gerar o relatório gráfico"), 
		/** Guarda as configurações da janela se for necessário exportar os dados do cliente para PDF.*/
		EXPORTAR_PDF("Exportar Relatório", "Exportar Relatório do Cliente",  "Selecione um cliente para exportar seus dados");
		
		private String descricaoDaFuncao, tituloParaJanela, tituloPainel;

		private Opcao(String opcao, String titulo, String tituloPainel) {
			this.descricaoDaFuncao = opcao;
			this.tituloParaJanela = titulo;
			this.tituloPainel = tituloPainel;
		}

		/**
		 * Obtém a descrição da opção referente a constante escolhida.
		 *  
		 * @return Retorna a descrição da opção.
		 */
		public String getDescricaoDaOpcao() {
			return descricaoDaFuncao;
		}

		/**
		 * Obtém o titulo para janela referente a constante escolhida.
		 *  
		 * @return Retorna o título para janela.
		 */
		public String getTituloParaJanela() {
			return tituloParaJanela;
		}

		/**
		 * Obtém o titulo para o painel referente a constante escolhida.
		 *  
		 * @return Retorna o título para o painel.
		 */
		public String getTituloPainel() {
			return tituloPainel;
		}
	} // enum Opcao
}
