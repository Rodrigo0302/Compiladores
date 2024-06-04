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

    public void parse() throws Exception{
        program();

        if (preanalisis.getTipo() != TipoToken.EOF) {
            String mensaje = "Se encontro un error en el programa";
            throw new ParserException(mensaje);
        }
    }
    //-----------------GRAMATICA DEL PROYECTO FINAL-----------------

    private void program() throws Exception{
        declaration();
    }
    //-----------------DECLARACIONES-----------------
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
                break;
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

    private void varInit() throws Exception{
        if (preanalisis.getTipo() == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            expression();
        }
    }
    //-----------------SENTECIAS-----------------
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

    private void exprStmt() throws Exception{
        expression();
        match(TipoToken.SEMICOLON);
    }

    private void forStmt() throws Exception{
        match(TipoToken.FOR);
        match(TipoToken.LEFT_PAREN);
        forStmt_1();
        forStmt_2();
        forStmt_3();
        match(TipoToken.RIGHT_PAREN);
        statement();
    }

    private void forStmt_1() throws Exception{
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

    private void forStmt_2() throws Exception{
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
                break;
        }
    }

    private void forStmt_3() throws Exception{
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

    private void ifStmt() throws Exception{
        match(TipoToken.IF);
        match(TipoToken.LEFT_PAREN);
        expression();
        match(TipoToken.RIGHT_PAREN);
        statement();
        elseStmt();
    }

    private void elseStmt() throws Exception{
        if (preanalisis.getTipo() == TipoToken.ELSE) {
            match(TipoToken.ELSE);
            statement();
            
        }
    }

    private void printStmt() throws Exception{
        match(TipoToken.PRINT);
        expression();
        match(TipoToken.SEMICOLON);
    }

    private void returnStmt() throws Exception{
        match(TipoToken.RETURN);
        returnExpOpc();
        match(TipoToken.SEMICOLON);
    }

    private void returnExpOpc() throws Exception{
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

    private void whileStmt() throws Exception{
        match(TipoToken.WHILE);
        match(TipoToken.LEFT_PAREN);
        expression();
        match(TipoToken.RIGHT_PAREN);
        statement();
    }

    private void block() throws Exception{
        match(TipoToken.LEFT_BRACE);
        declaration();
        match(TipoToken.RIGHT_BRACE);
    }

    //-----------------EXPRESIONES-----------------
    private void expression() throws Exception{
        assignment();
    }

    private void assignment() throws Exception{
        logicOr();
        assignmentOpc();
    }

    private void assignmentOpc() throws Exception{
        if (preanalisis.getTipo() == TipoToken.EQUAL) {
            match(TipoToken.EQUAL);
            expression();
        }
    }

    private void logicOr() throws Exception{
        logicAnd();
        logicOr_2();
    }

    private void logicOr_2() throws Exception{
        if (preanalisis.getTipo() == TipoToken.OR) {
            match(TipoToken.OR);
            logicAnd();
            logicOr_2();
        }
    }

    private void logicAnd() throws Exception{
        equality();
        logicAnd_2();
    }   

    private void logicAnd_2() throws Exception{
        if (preanalisis.getTipo() == TipoToken.AND) {
            match(TipoToken.AND);
            equality();
            logicAnd_2();
        }
    }

    private void equality() throws Exception{
        comparison();
        equality_2();
    }

    private void equality_2() throws Exception{
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

    private void comparison() throws Exception{
        term();
        comparison_2();
    }

    private void comparison_2() throws Exception{
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

    private void term() throws Exception{
        factor();
        term_2();
    }

    private void term_2() throws Exception{
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

    private void factor() throws Exception{
        unary();
        factor_2();
    }

    private void factor_2() throws Exception{
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

    private void unary() throws Exception{
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

    private void call() throws Exception{
        primary();
        call_2();
    }

    private void call_2() throws Exception{
        if (preanalisis.getTipo() == TipoToken.LEFT_PAREN) {
            match(TipoToken.LEFT_PAREN);
            arguments_Opc();
            match(TipoToken.RIGHT_PAREN);
        }
    }

    private void primary() throws Exception{
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
    private void function() throws Exception{
        match(TipoToken.IDENTIFIER);
        match(TipoToken.LEFT_PAREN);
        parameters_Opc();
        match(TipoToken.RIGHT_PAREN);
        block();
    }

    private void parameters_Opc() throws Exception{
        if (preanalisis.getTipo() == TipoToken.IDENTIFIER) {
            parameters();
        }
    }

    private void parameters() throws Exception{
        match(TipoToken.IDENTIFIER);
        parameters_2();
    }

    private void parameters_2() throws Exception{
        if (preanalisis.getTipo() == TipoToken.COMMA) {
            match(TipoToken.COMMA);
            match(TipoToken.IDENTIFIER);
            parameters_2();
        }
    }

    private void arguments_Opc() throws Exception{
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

    private void arguments() throws Exception{
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
