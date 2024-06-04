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
    //-----------------GRAMATICA DEL PROYECTO FINAL-----------------

    private void program() throws ParserException{
        declaration();
    }
    //-----------------DECLARACIONES-----------------
    private void declaration() throws ParserException{
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
                break;
        }
    }

    private void funDecl() throws ParserException{
        match(TipoToken.FUN);
        function();
    }

    private void varDecl() throws ParserException{
        match(TipoToken.VAR);
        match(TipoToken.IDENTIFIER);
        varInit();
        match(TipoToken.SEMICOLON);
    }

    private void varInit() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            expression();
        }
    }
    //-----------------SENTECIAS-----------------
    private void statement() throws ParserException{
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
                exprStmt();
                break;
            case FOR:
                forStmt();
                break;
            case IF:
                ifStmt();
                break;
            case PRINT:
                printStmt();
                break;
            case RETURN:
                returnStmt();
                break;
            case WHILE:
                whileStmt();
                break;
            case LEFT_BRACE:
                block();
                break;
            default:
                String mensaje = "Error se esperaba una sentencia pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    private void exprStmt() throws ParserException{
        expression();
        match(TipoToken.SEMICOLON);
    }

    private void forStmt() throws ParserException{
        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        forStmt_1();
        forStmt_2();
        forStmt_3();
        match(TipoToken.RIGHT_PAREN);
        statement();
    }

    private void forStmt_1() throws ParserException{
        switch (preanalisis.getTipo()) {
            case VAR:
                varDecl();
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
                exprStmt();
                break;
            case SEMICOLON:
                match(TipoToken.SEMICOLON);
                break;
            default:
                String mensaje = "Error, se esperaba una sentencia, una declaracion o ; en la incializacion del for pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    private void forStmt_2() throws ParserException{
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
                expression();
                match(TipoToken.SEMICOLON);
                break;
            case SEMICOLON:
                match(TipoToken.SEMICOLON);
                break;
        
            default:
                String mensaje = "Error, se esperaba una expresion o ; en el segundo argumento del for pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    private void forStmt_3() throws ParserException{
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
                expression();
                break;
            default:
                break;
        }
    }

    private void ifStmt() throws ParserException{
        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        expression();
        match(TipoToken.RIGHT_PAREN);
        statement();
        elseStmt();
    }

    private void elseStmt() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.ELSE) {
            match(TipoToken.ELSE);
            statement();
            
        }
    }

    private void printStmt() throws ParserException{
        match(TipoToken.PRINT);
        expression();
        match(TipoToken.SEMICOLON);
    }

    private void returnStmt() throws ParserException{
        match(TipoToken.RETURN);
        returnExpOpc();
        match(TipoToken.SEMICOLON);
    }

    private void returnExpOpc() throws ParserException{
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
                expression();
                break;

            default:
                break;
        }
    }

    private void whileStmt() throws ParserException{
        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        expression();
        match(TipoToken.RIGHT_PAREN);
        statement();
    }

    private void block() throws ParserException{
        match(TipoToken.LEFT_BRACE);
        declaration();
        match(TipoToken.RIGHT_BRACE);
    }

    //-----------------EXPRESIONES-----------------
    private void expression() throws ParserException{
        assignment();
    }

    private void assignment() throws ParserException{
        logicOr();
        assignmentOpc();
    }

    private void assignmentOpc() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            expression();
        }
    }

    private void logicOr() throws ParserException{
        logicAnd();
        logicOr_2();
    }

    private void logicOr_2() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.OR) {
            match(TipoToken.OR);
            logicAnd();
            logicOr_2();
        }
    }

    private void logicAnd() throws ParserException{
        equality();
        logicAnd_2();
    }   

    private void logicAnd_2() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.AND) {
            match(TipoToken.AND);
            equality();
            logicAnd_2();
        }
    }

    private void equality() throws ParserException{
        comparison();
        equality_2();
    }

    private void equality_2() throws ParserException{
        switch (preanalisis.getTipo()) {
            case  BANG_EQUAL:
                match(TipoToken.BANG_EQUAL);
                comparison();
                equality_2();
                break;
            case EQUAL_EQUAL:
                match(TipoToken.EQUAL_EQUAL);
                comparison();
                equality_2();
                break;
        
            default:
                break;
        }
    }

    private void comparison() throws ParserException{
        term();
        comparison_2();
    }

    private void comparison_2() throws ParserException{
        switch (preanalisis.getTipo()) {
            case GREATER:
                match(TipoToken.GREATER);
                term();
                comparison_2();
                break;
            case GREATER_EQUAL:
                match(TipoToken.GREATER_EQUAL);
                term();
                comparison_2();
                break;
            case LESS:
                match(TipoToken.LESS);
                term();
                comparison_2();
                break;
            case LESS_EQUAL:
                match(TipoToken.LESS_EQUAL);
                term();
                comparison_2();
                break;
        
            default:
                break;
        }
    }   

    private void term() throws ParserException{
        factor();
        term_2();
    }

    private void term_2() throws ParserException{
        switch (preanalisis.getTipo()) {
            case MINUS:
                match(TipoToken.MINUS);
                factor();
                term_2();
                break;
            case PLUS:
                match(TipoToken.PLUS);
                factor();
                term_2();
                break;
        
            default:
                break;
        }
    }

    private void factor() throws ParserException{
        unary();
        factor_2();
    }

    private void factor_2() throws ParserException{
        switch (preanalisis.getTipo()) {
            case SLASH:
                match(TipoToken.SLASH);
                unary();
                factor_2();
                break;
            case STAR:
                match(TipoToken.STAR);
                unary();
                factor_2();
                break;
        
            default:
                break;
        }
    }

    private void unary() throws ParserException{
        switch (preanalisis.getTipo()) {
            case BANG:
                match(TipoToken.BANG);
                unary();
                break;
            case MINUS:
                match(TipoToken.MINUS);
                unary();
                break;
            case TRUE:
            case FALSE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
            case LEFT_PAREN:
                call();
                break;

            default:
                String mensaje = "Error se esperaba un operador unario pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    private void call() throws ParserException{
        primary();
        call_2();
    }

    private void call_2() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.LEFT_PAREN) {
            match(TipoToken.LEFT_PAREN);
            arguments_Opc();
            match(TipoToken.RIGHT_PAREN);
        }
    }

    private void primary() throws ParserException{
        switch (preanalisis.getTipo()) {
            case TRUE:
            case FALSE:
            case NULL:
            case NUMBER:
            case STRING:
            case IDENTIFIER:
                match(preanalisis.getTipo());
                break;
            case LEFT_PAREN:
                match(TipoToken.LEFT_PAREN);
                expression();
                match(TipoToken.RIGHT_PAREN);
                break;
            default:
                String mensaje = "Error se esperaba un valor primario pero se encontro " + preanalisis.getTipo();
                throw new ParserException(mensaje);
        }
    }

    //-----------------OTRAS-----------------
    private void function() throws ParserException{
        match(TipoToken.IDENTIFIER);
        match(TipoToken.LEFT_PAREN);
        parameters_Opc();
        match(TipoToken.RIGHT_PAREN);
        block();
    }

    private void parameters_Opc() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.IDENTIFIER) {
            parameters();
        }
    }

    private void parameters() throws ParserException{
        match(TipoToken.IDENTIFIER);
        parameters_2();
    }

    private void parameters_2() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            parameters_2();
        }
    }

    private void arguments_Opc() throws ParserException{
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
                expression();
                arguments();
                break;
        
            default:
                break;
        }
    }

    private void arguments() throws ParserException{
        if (preanalisis.getTipo() == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            expression();
            arguments();
        }
    }

    private void match(TipoToken tt) throws ParserException{
        if (preanalisis.getTipo() == tt) {
            i++;
            preanalisis = tokens.get(i);
        }else{
            String mensaje = "Error se esperaba " + tt +
                " pero se encontro " + preanalisis.getTipo();
            throw new ParserException(mensaje);
        }
    }

    @SuppressWarnings("unused")
    private Token previus() {return this.tokens.get(i-1);}
    
}
