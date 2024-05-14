package interprete;
import java.util.List;


public class Parser {
    private final List<Token> tokens;
    private int i = 0;
    private Token preanalisis;

    public Parser(List<Token> tokens){
        this.tokens = tokens;
        this.preanalisis = tokens.get(i);
    }

    public void parse() throws ParserException{
        program();

        if (preanalisis.getTipo() != TipoToken.EOF) {
            String mensaje = "Se encontro un error en el programa";
            throw new ParserException(mensaje);
        }
    }

    private void program() throws Exception{
        //declaration();
    }

    private void declaration() throws Exception{
        switch (preanalisis.getTipo()) {
            case FUN:
                funDecl();
                declaration();
                break;
            case VAR:
                varDecl();
                declaration();
                break;
            case BANG:
            case MINUS:
            case TRUE:
            case FALSE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
            case FOR:
            case IF:
            case PRINT:
            case RETURN:
            case WHILE:
            case LEFT_BRACE:
                statement();
                declaration();
                break;
            default:
                String mensaje = "Error se esperaba una declaracion pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    private void funDecl() throws Exception{
        match(TipoToken.FUN);
        function();
    }

    private void varDecl() throws Exception{
        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);
        varInit();
        match(TipoToken.SEMICOLON);
    }
    private void statement() throws Exception{
        switch (preanalisis.getTipo()) {
            case BANG:
            case MINUS:
            case TRUE:
            case FALSE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
                EXPR_STMT();
                break;
            case FOR:
                FOR_STMT();
                break;
            case IF:
                IF_STMT();
                break;
            case PRINT:
                PRINT_STMT();
                break;
            case RETURN:
                RETURN_STMT();
                break;
            case WHILE:
                WHILE_STMT();
                break;
            case LEFT_BRACE:
                BLOCK();
                break;
            default:
                String mensaje = "Error se esperaba una sentencia pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    private void varInit() throws Exception{
        if (preanalisis.getTipo() == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            expression();
        }
    }
    private void match(TipoToken tt) throws ParserException{
        if (preanalisis.getTipo() == tt) {
            i++;
            preanalisis = tokens.get(i);
        }else{
            String mensaje = "Error se esperaba " + preanalisis.getTipo() +
                " pero se encontro " + tt;
            throw new ParserException(mensaje);
        }
    }

    private Token previus() {return this.tokens.get(i-1);}
    
}
