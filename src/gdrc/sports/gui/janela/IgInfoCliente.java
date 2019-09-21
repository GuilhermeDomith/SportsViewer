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
 * Cria uma janela que exibe todas as informações
 * do cliente que é passado pelo contrutor da classe.
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
	 * passado por parâmetro. <br><br>
	 * O objeto passado deve conter todos os exercícios 
	 * que precisam ser exibidos na janela.
	 * 
	 * @param location O Componente utilizado para posicionar a janela.
	 * @param cliente O cliente a ser exibido pela janela.
	 */
	public IgInfoCliente(Component location, Cliente cliente) {

		// Configurações da janela
		setBounds(100, 100, 442, 406);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(location);
		setTitle("Informações do Cliente");
		setIconImage(Propriedade.ICONE_APLICACAO);
		
		// Configurações do painel principal
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// Configurações do painel que exibeas as informações do cliente.
		PainelInfoCliente painelCliente = new PainelInfoCliente(cliente);
		painelCliente.setBounds(10, 11, painelCliente.getWidth(), painelCliente.getHeight());;
		contentPanel.add(painelCliente);
		
		// Configurações do painel e componentes que exibem os exercícios.
		JPanel painelExercicios = new JPanel();
		painelExercicios.setLayout(new BorderLayout());
		painelExercicios.setBorder(new TitledBorder("Exercícios "));
		painelExercicios.setBounds(10, 138, 414, 185);
		contentPanel.add(painelExercicios);
		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		painelExercicios.add(scrollPane);
		
		verticalBoxExercicios = Box.createVerticalBox();
		scrollPane.setViewportView(verticalBoxExercicios);
		
		// Configurações do painel de botões.
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
	 * Exibe as informações principais de todos os exercicios passados 
	 * por parametro no painel Box exibido pela janela. <br><br>
	 * 
	 * @param exercicios Exercícios a serem exibidos no painel Box da janela.
	 */
	private void exibirExercicios(List<Exercicio> exercicios) {
		if(exercicios == null || exercicios.size() == 0) return;
		
		Collections.sort(exercicios);
		
		LocalDate dataAnterior = null;
		int alturaBox=0;
		// Cria um painel Box vertical com vários painéis que exibem o Exercicio.
		for(Exercicio ex : exercicios) {
			// Adiciona a data ao painel, caso seja diferente a data do exercício anterior.
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
	 * Cria um novo painel para exibir as informações resumidas do 
	 * exercício passado por parâmetro.
	 * 
	 * @param exercicio O exercício com as informações a serem 
	 * adicionadas no painel.
	 * 
	 * @return Retorna o novo painel com as principais informações
	 * do exercício.
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
	 * Cria um painel já estilizado com a data passada por parâmetro.
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
	 * que exibe uma informação resumida do exercício. <br><br>
	 *  
	 * O primeiro evento tratado é, ao passar o mouse sobre o painel de 
	 * exercício é feita uma alteração na cor de forma que simulando um JList, 
	 * indicando que é clicável. 
	 * O outro evento é, ao clicar no painel é exibido uma janela que dá a 
	 * informação completa do exercício associado ao painel clicado.
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
										  "Informações Exercício",
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
