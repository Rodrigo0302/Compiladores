package interprete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Interprete {
    static boolean existenErrores = false;
    public static void main(String[] args) throws IOException {
        if(args.length > 1) {
            System.out.println("Uso correcto: k [archivo.k]");

            // Convención defininida en el archivo "system.h" de UNIX
            System.exit(64);
        } else if(args.length == 1){
            String texto = "src\\interprete\\pruebas\\"+args[0];
            ejecutarArchivo(texto);
        } else{
            //ejecutarPrompt();
            ejecutarArchivo("Interprete\\src\\interprete\\pruebas\\Identificadores.txt");
        }
    }

    private static void ejecutarArchivo(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        ejecutar(new String(bytes, Charset.defaultCharset()));

        // Se indica que existe un error
        if(existenErrores) System.exit(65);
    }

    private static void ejecutarPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        for(;;){
            System.out.print(">>> ");
            String linea = reader.readLine();
            if(linea == null) break; // Presionar Ctrl + D
            ejecutar(linea);
            existenErrores = false;
        }
    }

    private static void ejecutar(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokens = scanner.scan();
        
        for (Token token : tokens) {
            System.out.println(token);
        }
        
        System.out.println(source);
    }

}
