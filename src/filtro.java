import java.io.File;
import java.io.FilenameFilter;

public class filtro {
	public static void main (String[] args) {
		try {
			File busca = new File("F:\\");
			FilenameFilter filtro = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					return name.contains("pergunta.txt");
				}
			};
			String[] resultado = busca.list(filtro);
			for (int i = 0; i < resultado.length; i++) {
				System.out.println(resultado[i]);
			}
			File[] res = busca.listFiles();
			for (int i = 0; i < res.length; i++) {
				System.out.println(res[i].getAbsolutePath());
			}
		}catch(Exception e) {
			
		}
		
	}
}
