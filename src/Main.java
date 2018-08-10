import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		Console cmd = new Console();
		Scanner scan = new Scanner(System.in);
		ArrayList<String> diretorios = new ArrayList<String>();
		while (true) {
			String palavra = null;
			String comando = null;
			String destino = null;
			System.out.print("<"+cmd.getDir()+">");
			comando = scan.nextLine();
			if (comando.startsWith("1")||comando.startsWith("lsr")) {
				diretorios = cmd.getRoots();
				for (int i = 0; i < diretorios.size(); i++) {
					System.out.println(diretorios.get(i));
				}
			}else if (comando.startsWith("2")||comando.startsWith("lsa")) {
				diretorios = cmd.listarArqRoots();
				for (int i = 0; i < diretorios.size(); i++) {
					System.out.println(diretorios.get(i));
				}
			}else if (comando.startsWith("3")||comando.startsWith("search")) {
				palavra = Main.separar(comando);
				diretorios = cmd.busca(palavra);
				for (int i = 0; i < diretorios.size(); i++) {
					System.out.println(diretorios.get(i));
				}
			}else if (comando.startsWith("4")||comando.startsWith("ls")) {
				diretorios = cmd.listarDir();
				for (int i = 0; i < diretorios.size(); i++) {
					System.out.println(diretorios.get(i));
				}
			}else if (comando.startsWith("5")||comando.startsWith("del")) {
				palavra = Main.separar(comando);
				try {
					cmd.deletarArquivo(palavra);
				}catch(Exception e) {
					System.out.println("Não foi possível deletar o arquivo, motivo: \n"+e.getMessage());
				}
			}else if (comando.startsWith("6")||comando.startsWith("info")) {
				palavra = Main.separar(comando);
				try{
					System.out.println(cmd.mostrarAtributos(palavra));
				}catch(Exception e) {
					System.out.println("Não foi possível mostrar o arquivo, motivo: "+e.getMessage());
				}
			}else if (comando.startsWith("7")||comando.startsWith("copy")) {
				palavra = Main.separar(comando);
				System.out.print("Caminho de destino: ");
				destino = scan.nextLine();
				try{
					cmd.copiarArquivo(palavra, destino);
				}catch(Exception e) {
					System.out.println("Não foi possível copiar, motivo: "+e.getMessage());
				}
			}else if (comando.startsWith("cd")) {
				palavra = Main.separar(comando);
				try{
					cmd.mudarDir(palavra);
				}catch(Exception e) {
					System.out.println("Não foi possível mudar o diretório, motivo:");
					System.out.println(e.getMessage());
				}
			}else if (comando.startsWith("help")) {
				System.out.println(cmd.help());
			}else if (comando.startsWith("q")){
				System.out.println("Saindo...");
				scan.close();
				break;
			}else {
				System.out.println("Opção Inválida");
			}
		}
	}
	
	//METODO PRA SEPARAR O CAMINHO DO COMANDO
	public static String separar (String palavra) {
		for (int i = 0; i < palavra.length(); i++) {
			if (palavra.charAt(i) == ' ') {
				return palavra.substring(i+1);
			}
		}
		return null;
	}
}
