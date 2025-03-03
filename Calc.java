import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Scanner;

public class Calc {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        InputStream is = System.in;

        System.out.println("Seleccione el modo de entrada:");
        System.out.println("1. Ingresar operaciones por consola");
        System.out.println("2. Leer operaciones desde un archivo");
        System.out.print("Opción (1 o 2): ");
        String opcion = scanner.nextLine();

        if (opcion.trim().equals("2")) {
            System.out.print("Ingrese el nombre del archivo: ");
            String filename = scanner.nextLine();
            try {
                is = new FileInputStream(filename);
            } catch (Exception e) {
                System.err.println("Error: No se pudo abrir el archivo " + filename);
                return;
            }
        } else {
            System.out.println("Ingrese las operaciones (finalice con EOF, Ctrl+D o Ctrl+Z según su sistema):");
        }

        // Crear el flujo de entrada para ANTLR
        ANTLRInputStream input = new ANTLRInputStream(is);
        LabeledExprLexer lexer = new LabeledExprLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        LabeledExprParser parser = new LabeledExprParser(tokens);
        
        parser.removeErrorListeners();
        parser.addErrorListener(new BaseErrorListener(){
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                                    int line, int charPositionInLine, String msg, RecognitionException e) {
                System.err.println("Error de sintaxis en línea " + line + ":" + charPositionInLine + " " + msg);
                System.exit(1);
            }
        });

        ParseTree tree = parser.prog();
        EvalVisitor eval = new EvalVisitor();
        eval.visit(tree);
    }
}

