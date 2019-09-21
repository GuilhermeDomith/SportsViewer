package gdrc.sports.gui.janela;

import gdrc.sports.gui.component.PainelInfoCliente;
import gdrc.sports.gui.component.PainelInfoExercicios;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.time.Data;
import gdrc.sports.util.propriedades.Propriedade;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Cria uma janela que exibe todas as informa��es
 * do cliente que � passado pelo contrutor da classe.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class IgInfoCliente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private Box verticalBoxExercicios; 
	private JScrollPane scrollPane;

	/**
	 * Construtor que permite instanciar o objeto e 
	 * configurar a janela para exibir o objeto  {@link Cliente} 
	 * passado por par�metro. <br><br>
	 * O objeto passado deve conter todos os exerc�cios 
	 * que precisam ser exibidos na janela.
	 * 
	 * @param location O Componente utilizado para posicionar a janela.
	 * @param cliente O cliente a ser exibido pela janela.
	 */
	public IgInfoCliente(Component location, Cliente cliente) {

		// Configura��es da janela
		setBounds(100, 100, 442, 406);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(location);
		setTitle("Informa��es do Cliente");
		setIconImage(Propriedade.ICONE_APLICACAO);
		
		// Configura��es do painel principal
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// Configura��es do painel que exibeas as informa��es do cliente.
		PainelInfoCliente painelCliente = new PainelInfoCliente(cliente);
		painelCliente.setBounds(10, 11, painelCliente.getWidth(), painelCliente.getHeight());;
		contentPanel.add(painelCliente);
		
		// Configura��es do painel e componentes que exibem os exerc�cios.
		JPanel painelExercicios = new JPanel();
		painelExercicios.setLayout(new BorderLayout());
		painelExercicios.setBorder(new TitledBorder("Exerc�cios "));
		painelExercicios.setBounds(10, 138, 414, 185);
		contentPanel.add(painelExercicios);
		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		painelExercicios.add(scrollPane);
		
		verticalBoxExercicios = Box.createVerticalBox();
		scrollPane.setViewportView(verticalBoxExercicios);
		
		// Configura��es do painel de bot�es.
		JPanel painelDeBotoes = new JPanel();
		painelDeBotoes.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(painelDeBotoes, BorderLayout.SOUTH);

		JButton buttonFechar = new JButton("Fechar");
		buttonFechar.addActionListener( (ActionEvent e) -> { dispose(); });
		painelDeBotoes.add(buttonFechar);
		
		exibirExercicios(cliente.getExercicios());
		
		setModal(true);
		setResizable(false);
		setVisible(true);
	}
	
	/**
	 * Exibe as informa��es principais de todos os exercicios passados 
	 * por parametro no painel Box exibido pela janela. <br><br>
	 * 
	 * @param exercicios Exerc�cios a serem exibidos no painel Box da janela.
	 */
	private void exibirExercicios(List<Exercicio> exercicios) {
		if(exercicios == null || exercicios.size() == 0) return;
		
		Collections.sort(exercicios);
		
		LocalDate dataAnterior = null;
		int alturaBox=0;
		// Cria um painel Box vertical com v�rios pain�is que exibem o Exercicio.
		for(Exercicio ex : exercicios) {
			// Adiciona a data ao painel, caso seja diferente a data do exerc�cio anterior.
			if(dataAnterior == null || !ex.getData().getData().equals(dataAnterior)) {
				JPanel painelData  = novoPainelData(ex.getData());
				alturaBox += painelData.getHeight()+15;
				verticalBoxExercicios.add(painelData); 
			}
			
			// Adiciona o label que exibe o exercicio.
			JPanel painelExercicio = novoPainelExercicio(ex);
			alturaBox += painelExercicio.getHeight()+10;
			verticalBoxExercicios.add(painelExercicio);
			
			dataAnterior = ex.getData().getData();
		}
		
		Dimension dimension = new Dimension(scrollPane.getWidth(), alturaBox);
		verticalBoxExercicios.setPreferredSize(dimension);
	}
	
	/**
	 * Cria um novo painel para exibir as informa��es resumidas do 
	 * exerc�cio passado por par�metro.
	 * 
	 * @param exercicio O exerc�cio com as informa��es a serem 
	 * adicionadas no painel.
	 * 
	 * @return Retorna o novo painel com as principais informa��es
	 * do exerc�cio.
	 */
	private JPanel novoPainelExercicio(Exercicio exercicio) {
		JPanel painel = new JPanel(new BorderLayout());
		painel.setSize(scrollPane.getWidth(),30);
		painel.setBackground(Color.LIGHT_GRAY);
		
		JLabel labelExercicio = new JLabel();
		labelExercicio.setText(String.format("<html><pre> %s\n %s - %s</pre><html>", 
											exercicio.getExercicio(),exercicio.getTempo().getHoraInicio(),
											exercicio.getTempo().getHoraFim()));
		labelExercicio.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		
		painel.add(labelExercicio, BorderLayout.CENTER);
		painel.addMouseListener(new EventoMousePainelExercicio(painel, exercicio));
		return painel;
	}

	/**
	 * Cria um painel j� estilizado com a data passada por par�metro.
	 * 
	 * @param data A data a ser exibida no painel
	 * @return Retorna o novo painel contendo a data.
	 */
	private JPanel novoPainelData(Data data) {
		JPanel painel = new JPanel(new BorderLayout());
		painel.setSize(scrollPane.getWidth(),30);
		
		JLabel dataLabel = new JLabel();
		dataLabel.setText(data.toString());
		dataLabel.setHorizontalAlignment(JLabel.CENTER);
		dataLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		dataLabel.setForeground(Color.BLUE);
		
		painel.add(dataLabel, BorderLayout.CENTER);
		return painel;
	}
	
	/**
	 * Classe utilizada para tratar um evento de mouse sobre o painel
	 * que exibe uma informa��o resumida do exerc�cio. <br><br>
	 *  
	 * O primeiro evento tratado �, ao passar o mouse sobre o painel de 
	 * exerc�cio � feita uma altera��o na cor de forma que simulando um JList, 
	 * indicando que � clic�vel. 
	 * O outro evento �, ao clicar no painel � exibido uma janela que d� a 
	 * informa��o completa do exerc�cio associado ao painel clicado.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 *
	 */
	private class EventoMousePainelExercicio extends MouseAdapter{
		private Exercicio exercicio;
		private JPanel painel;
		
		public EventoMousePainelExercicio(JPanel painel, Exercicio exercicio) {
			this.exercicio = exercicio;
			this.painel = painel;
			this.painel.setBackground(Color.WHITE);
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			PainelInfoExercicios info = new PainelInfoExercicios(exercicio);
			info.setPreferredSize(new Dimension(info.getWidth(), info.getHeight()));
			JOptionPane.showMessageDialog(IgInfoCliente.this, info, 
										  "Informa��es Exerc�cio",
										  JOptionPane.PLAIN_MESSAGE);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			painel.setBackground(Color.LIGHT_GRAY);
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			painel.setBackground(Color.WHITE);
		}
	}
	
}
