package gdrc.sports.gui.janela;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import gdrc.sports.gui.component.PainelInfoCliente;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.util.propriedades.Propriedade;
import gdrc.sports.util.relatorio.RelatorioCliente;
import gdrc.sports.util.relatorio.RelatorioCliente.TipoDadoRelatorio;

/**
 * Cria uma janela para exibir o relatório para o cliente 
 * fornecido ao criar a janela. 
 * Exibe os seguintes dados para o cliente fornecido:
 * 
 * <ul>
 * 		<li>Exercício com maior duração;</li>
 * 		<li>Exercício com maior distância percorrida;</li>
 * 		<li>Exercício com maior perda de calorias;</li>
 * 		<li>Exercício com maior número de passos;</li>
 * 		<li>Exercício com maior velocidade atingida</li>
 * </ul>
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class IgRelatorioCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private Cliente cliente;
	private RelatorioCliente relatorioCliente;

	/**
	 * Cria e exibe a janela para exibir o relatório 
	 * do cliente fornecido por parâmetro.
	 *  
	 * @param location 	O componente utilizado para centralizar a janela.
	 * @param cliente	O cliente que terá o relatório exibido na janela.
	 */
	public IgRelatorioCliente(Component location, Cliente cliente) {
		this.cliente = cliente;
		this.relatorioCliente = new RelatorioCliente(cliente.getExercicios());
		
		//Configurações da janela.
		setBounds(100, 100, 442, 495);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Relatório do Cliente");
		setIconImage(Propriedade.ICONE_APLICACAO);
		setLocationRelativeTo(location);
		
		// Configurações do painel principal
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		// Configurações do painel que exibeas as informações do cliente.
		PainelInfoCliente painelCliente = new PainelInfoCliente(cliente);
		painelCliente.setBounds(10, 11, painelCliente.getWidth(), painelCliente.getHeight());
		contentPanel.add(painelCliente);
		
		// Configurações do painel que exibe o relatorio do cliente.
		PainelRelatorioCliente painelRelatorio = new PainelRelatorioCliente(cliente);
		painelRelatorio.setBounds(10, 132, painelRelatorio.getWidth(), painelRelatorio.getHeight());
		contentPanel.add(painelRelatorio);
		
		// Configurações do painel de botoes.
		JPanel painelDeBotoes = new JPanel();
		painelDeBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(painelDeBotoes, BorderLayout.SOUTH);
		
		JButton buttonExportarPDF = new JButton("Exportar para PDF");
		painelDeBotoes.add(buttonExportarPDF);
		buttonExportarPDF.addActionListener((ActionEvent e) -> { exportarRelatorioParaPDF();});
		
		JButton buttonInfoCliente = new JButton("Informações do Cliente");
		buttonInfoCliente.addActionListener((ActionEvent e) -> { new IgInfoCliente(this, cliente); });
		painelDeBotoes.add(buttonInfoCliente);
		
		JButton buttonFechar = new JButton("Fechar");
		buttonFechar.addActionListener((ActionEvent e) -> { dispose(); });
		painelDeBotoes.add(buttonFechar);
		
		setModal(true);
		setResizable(false);
		setVisible(true);
	}
	
	
	private void exportarRelatorioParaPDF() {
		IgExportarPDF janela = new IgExportarPDF(cliente);
		//Configura as opções da janela IgExportarPDF com as mesmas opções que estão nesta janela.
		janela.getCheckBoxInfoCliente().setSelected(true);
		janela.getCheckBoxRelatorioCliente().setSelected(true);
		janela.habilitarDesabilitarPainelRelatorio();
		janela.setRelatorioCliente(relatorioCliente);
		janela.setLocationRelativeTo(this);
		janela.setVisible(true);
	}


	/**
	 * Painel que agrupa todos os componentes utilizados para 
	 * exibição dos dados obtidos para a criação do relatório 
	 * do cliente.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	private class PainelRelatorioCliente extends JPanel{
		private JLabel labelDuracao;
		private JLabel labelDistancia;
		private JLabel labelCalorias;
		private JLabel labelPassos;
		private JLabel labelVelocidade;
		
		/**
		 * Construtor da classe que permite a criação do painel 
		 * com os dados do objeto {@link Cliente} que é passado
		 * por parâmetro.
		 * 
		 * @param cliente O objeto {@link Cliente} com seus exercícios 
		 * praticados. Exercicios que são utilizados para geração do 
		 * relatório.
		 */
		public PainelRelatorioCliente(Cliente cliente) {
			this.setBorder(new TitledBorder("Relatório do Cliente"));
			this.setSize(414, 290);
			this.setLayout(null);
			
			// Configurações dos componentes que rotulam os dados do relatório.
			JLabel labelMaiorDuracao = new JLabel("Exercício com maior duração:");
			labelMaiorDuracao.setBounds(10, 34, 207, 14);
			this.add(labelMaiorDuracao);
			
			JLabel labelMaiorDistancia = new JLabel("Exercício com maior distancia percorrida:");
			labelMaiorDistancia.setBounds(10, 83, 207, 14);
			this.add(labelMaiorDistancia);
			
			JLabel labelMaiorPerdaCal = new JLabel("Exercício com maior perda de calorias:");
			labelMaiorPerdaCal.setBounds(10, 133, 207, 14);
			this.add(labelMaiorPerdaCal);
			
			JLabel labelMaiorNumPassos = new JLabel("Exercício com maior número de passos:");
			labelMaiorNumPassos.setBounds(10, 186, 207, 14);
			this.add(labelMaiorNumPassos);
			
			JLabel labelMaiorVelocidade = new JLabel("Exercício com maior velocidade atingida:");
			labelMaiorVelocidade.setBounds(10, 239, 207, 14);
			this.add(labelMaiorVelocidade);
			
			// Configurações dos componentes que exibem os dados do relatorio.
			labelDuracao = new JLabel();
			Exercicio ex = relatorioCliente.getExercicioMaiorDuracao();
			labelDuracao.setText(RelatorioCliente.formatarDadosRelatorio(ex, TipoDadoRelatorio.DURACAO));
			labelDuracao.setBounds(20, 59, 404, 14);
			this.add(labelDuracao);
			
			labelDistancia = new JLabel();
			ex = relatorioCliente.getExercicioMaiorDistancia();
			labelDistancia.setText(RelatorioCliente.formatarDadosRelatorio(ex, TipoDadoRelatorio.DISTANCIA));
			labelDistancia.setBounds(20, 108, 404, 14);
			this.add(labelDistancia);
			
			labelCalorias = new JLabel();
			ex = relatorioCliente.getExercicioMaiorPerdaCalorias();
			labelCalorias.setText(RelatorioCliente.formatarDadosRelatorio(ex, TipoDadoRelatorio.CALORIAS));
			labelCalorias.setBounds(20, 161, 404, 14);
			this.add(labelCalorias);
			
			labelPassos = new JLabel();
			ex = relatorioCliente.getExercicioMaiorNumPassos();
			labelPassos.setText(RelatorioCliente.formatarDadosRelatorio(ex, TipoDadoRelatorio.PASSOS));
			labelPassos.setBounds(20, 211, 404, 14);
			this.add(labelPassos);
			
			labelVelocidade = new JLabel();
			ex = relatorioCliente.getExercicioMaiorVelocidade();
			labelVelocidade.setText(RelatorioCliente.formatarDadosRelatorio(ex, TipoDadoRelatorio.VELOCIDADE));
			labelVelocidade.setBounds(20, 261, 404, 14);
			this.add(labelVelocidade);
		}
	}// class PainelRelatorioCliente
	
}
