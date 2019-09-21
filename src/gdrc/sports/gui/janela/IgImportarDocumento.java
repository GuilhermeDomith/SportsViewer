package gdrc.sports.gui.janela;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import gdrc.sports.gui.es.MensagemUsuario;
import gdrc.sports.io.arquivo.EscanerDeArquivo;
import gdrc.sports.tipo.Cliente;
import gdrc.sports.tipo.Exercicio;
import gdrc.sports.tipo.exception.IdentificadorInvalidoException;
import gdrc.sports.util.controle.ControleDeClientes;
import gdrc.sports.util.controle.ControleDeExercicios;
import gdrc.sports.util.propriedades.Propriedade;

/**
 * Cria e exibe a janela que permite que o usuário selecione os 
 * documentos que contêm os exercícios a serem importados.<br><br>
 * 
 * Ao fim da importação de todos os documentos exibe uma janela
 * que informa o status de cada documento, se ele foi importado
 * com sucesso ou não, e por qual motivo caso ocorra alguma falha.
 * 
 * @author Guilherme Domith Ribeiro Coelho
 *
 */
public class IgImportarDocumento extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private ControleDeExercicios controleDeExercicios;
	private ControleDeClientes controleDeClientes;
	
	private PainelDocumentos painelDocsNaoImportados, painelDocsImportados;
	private Exercicio exercicioExcluirDoBD;
	
	/**
	 * Construtor default que instancia o objeto da classe e 
	 * exibe a janela que permite importar os documentos.
	 * 
	 * @param location O Componente utilizado para posicionar a janela.
	 */
	public IgImportarDocumento(Component location) {
		controleDeClientes = new ControleDeClientes();
		controleDeExercicios = new ControleDeExercicios();
		
		// Configurações da janela.
		setBounds(100, 100, 565, 580);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		setLocationRelativeTo(location);
		setTitle("Importar Exercício Físico");
		setIconImage(Propriedade.ICONE_APLICACAO);
		
		// Configurações do painel principal da janela.
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		// Painel que exibe os documentos que foram importados com sucesso.
		painelDocsImportados = new PainelDocumentos("DOCUMENTOS IMPORTADOS", new Color(0, 100, 0));
		painelDocsImportados.setBounds(10, 11, 530, 240);
		contentPanel.add(painelDocsImportados);
		
		// Painel que exibe os documentos que falharam ao serem importados.
		painelDocsNaoImportados = new PainelDocumentos("DOCUMENTOS NÃO IMPORTADOS", new Color(255, 0, 0));
		painelDocsNaoImportados.setBounds(10, 270, 530, 240);
		contentPanel.add(painelDocsNaoImportados);
		
		//Painel de botões.
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton fecharButton = new JButton(" Fechar ");
		buttonPane.add(fecharButton);
		fecharButton.addActionListener((ActionEvent e) ->{
			this.dispose();
		});
		getRootPane().setDefaultButton(fecharButton);
		
		if(!importarDocumentos()){ 
			dispose();
			return;
		}
			
		setModal(true);
		setResizable(false);
		setVisible(true);
	}
	
	private boolean importarDocumentos() {
		// Obtém os documentos a serem importados.
		File[] documentos = selecionarArquivosImportar();
		if(documentos == null) {
			this.dispose();
			return false;
		}
		
		// Faz a leitura dos documentos e obtém o status de cada um ao importar.
		Map<String, List<File>> documentosStatus = escanearDocumentos(documentos);
		if(documentosStatus.size() == 0) {
			this.dispose();
			return false;
		}
	
		exibirDocumentosStatus(documentosStatus);
		return true;
	}

	/**
	 * Exibe os documentos agrupados pelo status que são passados 
	 * por parâmetro. O documento junto do seu status de importação 
	 * será exibido nos paineis da janela, separando os que foram 
	 * importados com sucesso e os que não foram.
	 *  
	 * @param documentosStatus O objeto que agrupa pelo status os documentos  
	 * importados com sucesso ou não.
	 */
	private void exibirDocumentosStatus(Map<String, List<File>> documentosStatus) {
		
		JTable tabela = criarTabelaDocumentoStatus(documentosStatus, StatusImportacao.SALVO.getStatus());
		if(tabela != null) painelDocsImportados.setTableDocumentos(tabela);
		
		documentosStatus.remove(StatusImportacao.SALVO.getStatus());
		if(documentosStatus.size() == 0) return;
		
		tabela = criarTabelaDocumentoStatus(documentosStatus, null);
		if(tabela != null) painelDocsNaoImportados.setTableDocumentos(tabela);
		
	}
	
	/**
	 * Cria uma tabela com a uma coluna para o nome do arquivo e a outra para o 
	 * status do arquivo ao ser importado. Se o parametro statusExibir for fornecido 
	 * <code>null</code> irá adicionar na tabela todos os arquivos com todos os status, 
	 * se não, será adicionado apenas os arquivos com o status que foi fernecido no 
	 * parametro.
	 * 
	 * @param documentosStatus O objeto {@link Map} que tem o conjunto de arquivos 
	 * associado a cada status.
	 * @param status O status que indica qual arquivo será adicionado na tabela. 
	 * Pode ser <code>null</code>
	 * 
	 * @return Retorna a tabela com o nome de cada arquivo associado ao seu status.
	 */
	private JTable criarTabelaDocumentoStatus(Map<String, List<File>> documentosStatus, String status) {
		
		if(status != null && !documentosStatus.containsKey(status)) 
			return null;
		
		/* Obtém os status a serem adicionados na tabela, se não foi fornecido
		 * nenhum status no parâmetro obtém todos os status (chaves do map).
		 */
		Set<String> chavesStatus = (status == null)? documentosStatus.keySet() : 
													 Collections.singleton(status);
		
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("Arquivo");
		tableModel.addColumn("Status");
		for(String chave : chavesStatus)
			for(File file : documentosStatus.get(chave))
				tableModel.addRow(new Object[] {file.getName(), chave});
		
		
		return new JTable(tableModel);
	}

	/**
	 * Escaneia todos os documentos passados por parâmetro para que seja
	 * extraído o exercício e o usuário. <br><br>
	 * 
	 * Cada documento primeiramente será escaneado parcialmente para que 
	 * a verificação das chaves primarias sejam buscadas no banco de dados,
	 * a fim de verificar se o documento já foi importado anteriormente.
	 * Caso já tenha sido gravado irá questionar ao usuário se deseja 
	 * sobrescrevê-lo. <br><br>
	 * 
	 * Então após feita a verificação a leitura total do documento será 
	 * feita e irá salvar os dados no banco. O usuário será salvo apenas se 
	 * não existir no Banco de dados. Na importação de cada documento será 
	 * gerada uma mensagem de status indicando o sucesso ou falha caso ocorra.    
	 *  
	 * @param documentos Os documentos à serem importados.
	 * 
	 * @return Retorna o conjunto de arquivos importados agrupados pelo
	 * status, uma {@link String} que é a chave do {@link Map} retornado.
	 */
	private Map<String, List<File>> escanearDocumentos(File documentos[]) {
		EscanerDeArquivo escaner = new EscanerDeArquivo();
		Map<String, List<File>> documentosStatus = new HashMap<>();
		String status;
		
		for(File file : documentos) {
			try {
				// Escaneia parcialmente para verificar se o exercício já existe no BD.
				Exercicio exercicio = escaner.escanearArquivoParcialmente(file);
				
				if(!confirmarSobreescreverExercicioSeExistir(exercicio, file.getName()))
					status = StatusImportacao.JA_IMPORTADO.getStatus();
				else {
					// Faz a leitura completa do exercício para enfim gravar no BD.
					exercicio = escaner.escanearArquivo(file);
					boolean clienteSalvo = salvarClienteNoBD(exercicio.getCliente());

					if(clienteSalvo) {
						boolean salvarExercicio = true;
						// Verifica se tem exercicio a excluir antes de salvar.
						if(exercicioExcluirDoBD != null) 
							salvarExercicio = controleDeExercicios.deletarExercicio(exercicioExcluirDoBD);
						
						if(salvarExercicio && controleDeExercicios.salvarExercicio(exercicio)) 
							 status = StatusImportacao.SALVO.getStatus();
						else status = StatusImportacao.FALHA_BD_EXERCICIO.getStatus();
					}else status = StatusImportacao.FALHA_BD_CLIENTE.getStatus();
				}
				
			}catch (IdentificadorInvalidoException identificadorException) {
				status = identificadorException.getMessage();
			}catch (IOException exception) {
				status = StatusImportacao.FALHA_ARQUIVO.getStatus();
			}
			
			// Adiciona um List vazio se o status ainda nao existe no map. 
			if(!documentosStatus.containsKey(status))
				documentosStatus.put(status, new ArrayList<>());
			
			documentosStatus.get(status).add(file);
			exercicioExcluirDoBD = null;
		}
		
		return documentosStatus;
	}

	/**
	 * Salva o cliente se ele ainda não existe na base de dados. 
	 * 
	 * @param cliente O cliente a ser salvo
	 * @return Retorna <code>true</code> se o cliente já estava salvo ou foi salvo corretamente,
	 * se não retorna <code>false</code>
	 */
	private boolean salvarClienteNoBD(Cliente cliente) {
		// Verifica se o cliente já existe no BD.
		Cliente clienteBusca = controleDeClientes.buscarClientePorEmail(cliente.getEmail());

		if(clienteBusca != null) return true;
		return controleDeClientes.salvarCliente(cliente);
	}

	/**
	 * Confirma se o exercício passado por parâmetro pode ou não ser gravado 
	 * no banco de dados. Para isso, primeiro verifica se já existe cadastrado.
	 * Se sim, solicita ao usuário que confirme sobrescrevê-lo. Se não, indica
	 * que o exercício pode ser gravado, neste caso retorna <code>true</code>.
	 * 
	 * @param exercicio O exercício a ser confirmado sua possível gravação no 
	 * banco de dados.
	 * @param fileName O nome do arquivo em que o exercício foi extraído. 
	 * 
	 * @return Retorna <code>true</code> se o exercício pode ser gravado, 
	 * se não, retorna <code>false</code>.
	 */
	private boolean confirmarSobreescreverExercicioSeExistir(Exercicio exercicio, String fileName) {
		List<Exercicio> exercicios = controleDeExercicios.listarExerciciosComChaves(
												exercicio.getCliente().getEmail(),
												exercicio.getData(), 
												exercicio.getTempo().getHoraInicio(),
												exercicio.getTempo().getHoraFim());

		if(exercicios.size() == 0) return true;
		
		String msg = String.format("O exercício do arquivo '%s' já foi \n"
				+ "importado anteriormente, deseja sobrescreve-lo?", fileName);
		Boolean confirmaSobrescrever = MensagemUsuario.msgConfirma(msg);
		if(confirmaSobrescrever == null || !confirmaSobrescrever) return false;
		
		/* Como a busca foi feita com as 4 chaves primarias terá apenas 
		 * um exercício na lista de exercícios. */
		exercicioExcluirDoBD = exercicios.get(0);
		
		return true;
	}

	/**
	 * Exibe a janela que permite ao usuário escolher um ou vários
	 * arquivos a partir do sistema de arquivos do sistema operacional.
	 * 
	 * Os arquivos permitidos são apenas arquivos com a extenção 'txt'.
	 * 
	 * @return Retorna o conjunto de arquivos selecionados pelo usuário.
	 */
	public File[] selecionarArquivosImportar() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
		fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
		fileChooser.setMultiSelectionEnabled(true);
		if(fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
			return fileChooser.getSelectedFiles();
		
		return null;
	}
	
	/**
	 * Classe que cria o painel contendo uma tabela para que seja
	 * exibido cada documento com o seu status gerado na importação.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	private class PainelDocumentos extends JPanel{
		private JTable tableDocumentos;
		private JScrollPane scrollPane;
		
		/**
		 * Construtor sobrecarregado que permite criar o painel 
		 * com o título na cor que passado por parâmeto.
		 * 
		 * @param titulo O título a ser exibido na parte de cima do painel.
		 * @param corTitulo A cor que será aplicada ao título. 
		 */
		public PainelDocumentos(String titulo, Color corTitulo) {
			TitledBorder titledBorder = new TitledBorder(titulo);
			titledBorder.setTitleJustification(TitledBorder.CENTER);
			titledBorder.setTitleColor(corTitulo);
			setBorder(titledBorder);
			setLayout(new BorderLayout());
			
			tableDocumentos = new JTable();
			scrollPane = new JScrollPane(tableDocumentos);
			add(scrollPane, BorderLayout.CENTER);
			
			JLabel documentosLabel = new JLabel("Nenhum Documento");
			documentosLabel.setSize(200, 30);
			documentosLabel.setHorizontalAlignment(JLabel.CENTER);
			scrollPane.setViewportView(documentosLabel);
		}

		/**
		 * Insere e configura a tabela de status dos documentos no painel.
		 * 
		 * @param tableDocumentos O componente contendo o documento 
		 * associado a sua mensagem de status gerado na importação.
		 */
		public void setTableDocumentos(JTable tableDocumentos) {
			this.tableDocumentos = tableDocumentos;
			
			TableColumn coluna = this.tableDocumentos.getColumnModel().getColumn(0);
			if(coluna != null) coluna.setMinWidth(200);
			
			coluna = this.tableDocumentos.getColumnModel().getColumn(1);
			if(coluna != null) coluna.setMinWidth(400);
			
			this.scrollPane.setViewportView(this.tableDocumentos);
		}
		
	}
	
	/**
	 * Rotula os principais erros que podem ocorrer ao importar 
	 * um documento de exercício físico.
	 * 
	 * @author Guilherme Domith Ribeiro Coelho
	 */
	private enum StatusImportacao{
		SALVO("Salvo com sucesso."),
		JA_IMPORTADO("Documento já importado anteriormente."),
		FALHA_BD_CLIENTE("Falha no banco de dados ao salvar Usuário."),
		FALHA_ARQUIVO("Erro ao abrir o arquivo"),
		FALHA_BD_EXERCICIO("Falha no banco de dados ao salvar Exercício."), 
		IDENTIFICADOR_INVALIDO("Identificador Inválido");
		
		private String status;

		private StatusImportacao(String status) {
			this.status = status;
		}

		public String getStatus() {
			return status;
		}

	}
	
	
}
