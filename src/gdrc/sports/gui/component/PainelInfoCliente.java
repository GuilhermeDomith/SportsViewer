package gdrc.sports.gui.component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import gdrc.sports.tipo.Cliente;

/**
 * Um container {@link JPanel} que organiza os 
 * componentes utilizados para exibir as informações de
 * um objeto {@link Cliente}.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 */
public class PainelInfoCliente extends JPanel {
	private JLabel labelNome;
	private JLabel labelSexo;
	private JLabel labelAltura;
	private JLabel labelEmail;
	private JLabel labelDataNasc;
	private JLabel labelPeso;

	/**
	 * Instancia o objeto {@link JPanel} com os dados do cliente 
	 * que são passados por parâmetro. Os dados a serem exibidos 
	 * em componentes inseridos no Painel.
	 * 
	 * @param cliente O objeto {@link Cliente} com os dados a serem 
	 * exibidos pelo painel.
	 */
	public PainelInfoCliente(Cliente cliente) {
		this.setBorder(new TitledBorder("Informa\u00E7\u00F5es Cliente"));
		this.setSize(414, 117);
		this.setLayout(null);
		
		
		labelNome = new JLabel("Nome:");
		labelNome.setBounds(10, 26, 270, 14);
		this.add(labelNome);
		
		labelEmail = new JLabel("Email:");
		labelEmail.setBounds(10, 51, 270, 14);
		this.add(labelEmail);
		
		labelSexo = new JLabel("Sexo:");
		labelSexo.setBounds(290, 26, 114, 14);
		this.add(labelSexo);
		
		labelAltura = new JLabel("Altura:");
		labelAltura.setBounds(290, 51, 114, 14);
		this.add(labelAltura);
		
		labelPeso = new JLabel("Peso: ");
		labelPeso.setBounds(290, 76, 114, 14);
		this.add(labelPeso);
		
		labelDataNasc = new JLabel("Data de nascimento:");
		labelDataNasc.setBounds(10, 76, 254, 14);
		this.add(labelDataNasc);
		
		exibirInformacoesCliente(cliente);
	}
	
	/**
	 * Exibe as informações do {@link Cliente} passado por parâmetro. 
	 * As informações são inseridas nos componentes utilizados pelo 
	 * painel para exibição das informações do Cliente.
	 * 
	 * @param cliente O objeto {@link Cliente} a ser exibido no painel.
	 */
	private void exibirInformacoesCliente(Cliente cliente) {
		String textoLabel = "<html>%s <b>%s</b> %s</html>";
		labelNome.setText(String.format(textoLabel, labelNome.getText(), cliente.getNome(), ""));
		labelEmail.setText(String.format(textoLabel, labelEmail.getText(), cliente.getEmail(),""));
		labelAltura.setText(String.format(textoLabel, labelAltura.getText(), cliente.getAltura(), "m"));
		labelDataNasc.setText(String.format(textoLabel, labelDataNasc.getText(), cliente.getDataNascimento(),""));
		labelPeso.setText(String.format(textoLabel, labelPeso.getText(), cliente.getPeso(), "Kg"));
		labelSexo.setText(String.format(textoLabel, labelSexo.getText(), cliente.getSexo(),""));
	}

}
