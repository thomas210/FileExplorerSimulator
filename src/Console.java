import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

public class Console {
	private File[] roots = null;
	private File diretorioAtual = null;
	
	public Console() {
		this.roots = File.listRoots();	//ESSE ATRIBUTO VAI FICAR COM OS ROOTS
		this.diretorioAtual = new File(roots[0].getPath());	//ARMAZENA O DIRETORIO ATUAL, INICIALMENTE O DIR ATUAL É DISCO F:
	}
	
	/*COMANDO 1: LISTAR TODOS OS ROOTS, BASICAMENTE RETORNA O CAMINHO DOS ROOTS, QUE JA FORAM INICIALIZADOS NO CONSTRUTOR*/
	public ArrayList<String> getRoots() {
		ArrayList<String> listaRoots = new ArrayList();	
		for (int i = 0; i < roots.length; i++) {
			listaRoots.add(roots[i].getPath());	//PEGANDO O CAMINHO DOS ROOTS E COLOCANDO NUM ARRAYLIST
		}
		return listaRoots;
	}
	
	
	/*COMANDO 2; LISTAR ARQ DOS ROOTS, PEGA OS ROOTS E DEPOIS CHAMA O METODO PRIVADO QUE FOI A LISTAGEM COM RECURSIVIDADE, RETORNA UM ARRAYLIST COM TODOS OS 
	 * NOMES DOS ARQUIVOS*/
	public ArrayList<String> listarArqRoots() {
		ArrayList<String> todosArquivos = new ArrayList<String>();
		for (int i = 0; i < this.roots.length; i++) {	//PEGO O ROOT E MANDO LISTAR
			todosArquivos.add(this.roots[i].getPath());	//ADICIONA O ROOT NO ARRAYLIST
			todosArquivos.addAll(this.lsArqRoots(this.roots[i], 1));	//COMEÇA A LISTAR O ROOT
		}
		return todosArquivos;
	}
	
	/*LISTAR OS ARQUIVOS DO DIRETORIO, CASO TENHA UM DIRETOIO DENTRO DELE, ELE LISTA TAMBÉM, ORGANIZADO POR SETAS
	 * EX.: 
	 * C:\\
	 * ->PROGRAM FILES
	 * ->->COREL
	 * COREL ESTA DENTRO DE PROGRAM FILES E PROGRAM FILES ESTA DENTRO DE C*/
	private ArrayList<String> lsArqRoots(File root, int label){	//LABEL É A QUANTIDADE DE SETAS
		ArrayList<String> listaArquivos = new ArrayList<String>();
		String[] lista = root.list();	//RECEBE OS ARQUIVOS DO DIRETORIO
		String res = new String();
		try {
			for (int j = 0; j < label; j++) {	//PARA ORGANIZAR ELE PRINTA UMA SETA PRA CADA 'CAMADA' DO ARQUIVO
				res = res + "->";
			}
			for (int i = 0; i < lista.length; i++) {	//PERCORRE O VETOR DE STRING COM OS ARQUIVOS DO DIRETORIO
				listaArquivos.add(res + lista[i]);	//COLOCA NO ARRAYLIST
				File subpasta = new File(root, lista[i]);
				if (subpasta.isDirectory()) {	//SE O ARQUIVO FOR UM DIRETORIO ENTAO CHAMA A FUNÇÃO RECURSIVAMENTE PARA LISTAR A SUBPASTA
					int novaLabel = label + 1;
					listaArquivos.addAll(this.lsArqRoots(subpasta, novaLabel));	//COLOCO NO ARRAYLIST GERAL OS ARQUIVOS DA SUBPASTA
				}
			}
		}catch(Exception e) {

		}
		
		return listaArquivos;
	}
	
	/*COMANDO 3*/
	public ArrayList<String> busca (String nome){
		return this.search(nome, this.diretorioAtual);	//COMANÇANDO A PARTIR DO DIR ATUAL
	}
	
	private ArrayList<String> search(String nome, File dir) {
		ArrayList<String> resposta = new ArrayList<String>();
		FilenameFilter filtro = new FilenameFilter() {	//FILTRO DA BUSCA
			
			@Override
			public boolean accept(File dir, String name) {
				return name.contains(nome);
			}
		};
		String[] resultado = null;
		try {
			resultado = dir.list(filtro);	//RETORNA OS ARQUIVOS DO DIR ATUAL QUE BATEM A PESQUISA
			for(int i = 0; i < resultado.length; i++) {
				resposta.add(resultado[i] +" "+ dir.getAbsolutePath());	//COLOCANDO NO ARRAYLIST JUNTO COM O CAMINHO
			}
			File[] diretorios = dir.listFiles();	//PEGO OS DIR QUE TEM DENTRO DA PASTA ATUAL
			for (int i = 0; i < diretorios.length; i++) {
				if (diretorios[i].isDirectory()) {
					resposta.addAll(search(nome, diretorios[i]));	//CASO SEJA UM DIRETORIO ENTÃO ELE FAZ A BUSCA NELA TAMBÉM RECURSIVAMENTE
				}
			}
		}catch(Exception e) {
			
		}
		return resposta;	//RETORNA O ARRAYLIST COM A RESPOSTA
	}
	
	/*COMANDO 4: lista os arquivos do diretorio especificado*/
	public ArrayList<String> listarDir (){
		ArrayList<String> arquivos = new ArrayList<String>();
		try{
			String[] lista = this.diretorioAtual.list();
			for (int i = 0; i < lista.length; i++) {
				arquivos.add(lista[i]);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return arquivos;
	}
	
	//5 DELETE
	public void deletarArquivo(String nome) throws Exception{
		File arquivo = new File(diretorioAtual, nome);	//DELETA CASO O ARQUIVO ESTEJA NO DIRETORIO ATUAL
		if (!(arquivo.delete())) {
			throw new Exception("Arquivo não existe");
		}
	}
	
	//6 ATRIBUTOS DE UM ARQUIVO
	public String mostrarAtributos (String nome) throws Exception{
		String res = null;
		File arquivo = new File(diretorioAtual, nome);
		if (!arquivo.exists()) {
			throw new Exception("Arquivo não existe");
		}
		res = "Nome: " + arquivo.getName() + "\n";
		res = res + "Caminho: " + arquivo.getAbsolutePath() + "\n";		
		if (arquivo.isHidden()) {
			res = res + "Arquivo Oculto\n";
		}else {
			res = res + "Arquivo visível\n";
		}
		res = res + "Tipo: ";
		if (arquivo.isDirectory())
			res = res + "Diretorio\n";
		else if (arquivo.isFile()) {
			res = res + "Arquivo\n";
			res = res + "Tamanho total: " + arquivo.length() + " Bytes\n";
		}
		return res;
	}
	
	//COMANDO 7
	public void copiarArquivo(String nome, String caminhoDestino) throws Exception{
		FileInputStream streamEntrada = null;
		FileOutputStream streamSaida = null;
		File entrada = new File(this.getDir(),nome);	//CRIA O FILE ONDE O ARQUIVO ESTA
		if (!entrada.exists()) {
			throw new Exception("Arquivo não existe");
		}
		File saida = new File(caminhoDestino + "Copia" + nome);	//CRIA O FILE ONDE O ARQUIVO VAI FICAR
		streamEntrada = new FileInputStream(entrada);
		streamSaida = new FileOutputStream(saida);
		byte[] buffer = new byte[1024];	
		while ((streamEntrada.read(buffer)) > 0) {	//LE OS BYTES DO ARQUIVOS
			streamSaida.write(buffer);	//COLA NO DESTINO
		}	
		try {
			streamEntrada.close();	//FECHA AS STREAMS
			streamSaida.close();	//FECHA AS STREAMS
		} catch (IOException e) {
			
		}	
	}
	
	//COMANDO CD
	public void mudarDir(String caminho) throws Exception{
		File novoDir = null;
		if (caminho.startsWith("..")) {
			novoDir = new File(this.diretorioAtual.getParent());
		}
		else{
			novoDir = new File(diretorioAtual, caminho);
			if(novoDir.exists()) {
				this.diretorioAtual = novoDir;
				return;
			}
		}
		for (int i = 0; i < this.roots.length; i++) {
			if (caminho.startsWith(this.roots[i].getPath())){
				novoDir = new File(caminho);
				break;
			}
		}
		if(novoDir.exists()) {
			this.diretorioAtual = novoDir;
		}else {
			throw new Exception("Caminho não existe");
		}
	}
	
	//DIR ATUAL
	public String getDir() {
		return this.diretorioAtual.getAbsolutePath();
	}
	
	public String help() {
		String res;
		res = "1 OU lsr: Listar roots\n";
		res = res + "2 OU lsa: listar tudo a partir dos roots\n";
		res = res + "3 OU search \"ARQUIVO\": pesquisar pro arquivo ou pasta a partir do diretorio atual\n";
		res = res + "4 OU ls: listar diretorio atual, listagem simples\n";
		res = res + "5 OU del \"ARQUIVO\": remover arquivo do diretório atual\n";
		res = res + "6 OU info \"ARQUIVO\": informações do arquivo citado\n";
		res = res + "7 OU copy \"ARQUIVO\": copia o arquivo e cola no caminho de destino que é informado em seguida\n";
		res = res + "cd \"DIRETORIO\": muda o diretorio para o especificado\n";
		res = res + "q: sair do console\n";
		return res;
	}
	

	
}
