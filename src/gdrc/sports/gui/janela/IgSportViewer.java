package gdrc.sports.gui.janela;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import gdrc.sports.gui.janela.IgPesquisa.Opcao;
import gdrc.sports.util.propriedades.Propriedade;


/**
 * Classe que cria a janela principal para que o usuário 
 * da aplicação <i>Sports Viewer</i> possa ter acesso às 
 * principais funções. <br><br> 
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class IgSportViewer extends JFrame {
	private JPanel contentPane;

	/**
	 * Cria e exibe a janela principal da aplicação 
	 * <i>Avalizaçao Fisica</i>.
	 */
	public IgSportViewer() {
		
		// Configurações da janela.
		setBounds(100, 100, 623, 345);
		setLocationRelativeTo(null);;
		setTitle(Propriedade.NOME_PROGRAMA);
		setIconImage(Propriedade.ICONE_APLICACAO);
		
		// Configurações do painel da janela.
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		// Configurações do painel principal que agrupa os componentes
		JPanel painelPrincipal = new JPanel();
		contentPane.add(painelPrincipal, BorderLayout.CENTER);
		painelPrincipal.setLayout(null);
		
		// Configurações do painel menu com acesso as funções da aplicação
		JPanel painelMenuBotoes = new JPanel();
		painelMenuBotoes.setBorder(new TitledBorder((Border)null));
		painelMenuBotoes.setBounds(10, 26, 342, 232);
		painelPrincipal.add(painelMenuBotoes);
		painelMenuBotoes.setLayout(null);
		
		JButton btnImportarDoc = new JButton("Importar Exercícios");
		btnImportarDoc.setHorizontalAlignment(SwingConstants.LEFT);
		btnImportarDoc.setIcon(new ImageIcon(Propriedade.URL_ICONE_IMPORTAR));
		btnImportarDoc.addActionListener((ActionEvent e) ->{ new IgImportarDocumento(this); });
		btnImportarDoc.setBounds(10, 24, 155, 54);
		painelMenuBotoes.add(btnImportarDoc);
		
		JButton btnPesquisarCliente = new JButton("Pesquisar Cliente");
		btnPesquisarCliente.setHorizontalAlignment(SwingConstants.LEFT);
		btnPesquisarCliente.setIcon(new ImageIcon(Propriedade.URL_ICONE_PESQUISA));
		btnPesquisarCliente.addActionListener((ActionEvent e) ->{ new IgPesquisa(this, Opcao.EXIBIR_INFO); });
		btnPesquisarCliente.setBounds(94, 154, 155, 54);
		painelMenuBotoes.add(btnPesquisarCliente);
		
		JButton btnRelatorioCliente = new JButton("Gerar Gráficos");
		btnRelatorioCliente.setHorizontalAlignment(SwingConstants.LEFT);
		btnRelatorioCliente.setIcon(new ImageIcon(Propriedade.URL_ICONE_GRAFICO));
		btnRelatorioCliente.addActionListener((ActionEvent e) ->{ new IgPesquisa(this, Opcao.GERAR_GRAFICO); });
		btnRelatorioCliente.setBounds(175, 24, 155, 54);
		painelMenuBotoes.add(btnRelatorioCliente);
		
		JButton btnRelatorioGraficos = new JButton("Gerar Relatório");
		btnRelatorioGraficos.setHorizontalAlignment(SwingConstants.LEFT);
		btnRelatorioGraficos.setIcon(new ImageIcon(Propriedade.URL_ICONE_RELATORIO));
		btnRelatorioGraficos.addActionListener((ActionEvent e) ->{ new IgPesquisa(this, Opcao.GERAR_RELATORIO); });
		btnRelatorioGraficos.setBounds(175, 89, 155, 54);
		painelMenuBotoes.add(btnRelatorioGraficos);
		
		JButton buttonExportarPdf = new JButton("Exportar PDF");
		buttonExportarPdf.setHorizontalAlignment(SwingConstants.LEFT);
		buttonExportarPdf.setIcon(new ImageIcon(Propriedade.URL_ICONE_EXPORTAR));
		buttonExportarPdf.setBounds(10, 89, 155, 54);
		buttonExportarPdf.addActionListener((ActionEvent e) -> { new IgPesquisa(this, Opcao.EXPORTAR_PDF);});
		painelMenuBotoes.add(buttonExportarPdf);
		
		JLabel lblSportsViewer = new JLabel(Propriedade.NOME_PROGRAMA);
		lblSportsViewer.setForeground(SystemColor.activeCaption);
		lblSportsViewer.setFont(new Font("Cinzel Bold", Font.PLAIN, 30));
		lblSportsViewer.setBounds(362, 11, 235, 53);
		painelPrincipal.add(lblSportsViewer);
		
		JLabel labelLogoAplicacao = new JLabel();
		labelLogoAplicacao.setIcon(new ImageIcon(Propriedade.URL_LOGO_APLICACAO));
		labelLogoAplicacao.setBounds(362, 63, 227, 232);
		painelPrincipal.add(labelLogoAplicacao);
		
		setResizable(false);
		setVisible(true);
	}
	
	}
