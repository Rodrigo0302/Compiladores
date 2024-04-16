package interprete;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Scanner {

    private static final HashMap<String,TipoToken> palabrasReservadas;
    static {
        palabrasReservadas = new HashMap<>();
        palabrasReservadas.put("and", TipoToken.AND);
        palabrasReservadas.put("else", TipoToken.ELSE);
        palabrasReservadas.put("false", TipoToken.FALSE);
        palabrasReservadas.put("fun", TipoToken.FUN);
        palabrasReservadas.put("for", TipoToken.FOR);
        palabrasReservadas.put("if", TipoToken.IF);
        palabrasReservadas.put("null", TipoToken.NULL);
        palabrasReservadas.put("or", TipoToken.OR);
        palabrasReservadas.put("print", TipoToken.PRINT);
        palabrasReservadas.put("return", TipoToken.RETURN);
        palabrasReservadas.put("true", TipoToken.TRUE);
        palabrasReservadas.put("var", TipoToken.VAR);
        palabrasReservadas.put("while", TipoToken.WHILE);
    }

    private String source;

    public Scanner(String source){
        this.source = source + " ";
    }

    public List<Token> scan(){
        List<Token> tokens = new ArrayList<>();
        int estado = 0;
        String lexema = "";
        //System.out.printf("tam = %d",source.length());
        
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            System.out.println("Estado: " + estado);
            System.out.printf("c = %c\n",c);
            
            
            switch (estado) {
                //Estado Inicial
                case 0:
                    if(c == '>'){
                        estado = 1;
                        lexema += c;
                    }else if(c == '<'){
                        estado = 4;
                        lexema += c;
                    } else if(c == '='){
                        estado = 7;
                        lexema += c;
                    }else if(c == '!'){
                        estado = 10;
                        lexema += c;
                    }else if(Character.isLetter(c)){
                        estado =13;
                        lexema += c;
                    }else if(Character.isDigit(c)){
                        estado = 15;
                        lexema += c;
                    }else if (c == '"') {
                        estado = 24;
                        lexema += c;
                        
                    }else if(c== '/'){
                        estado = 26;
                        lexema += c;
                    }else if (Character.isWhitespace(c)) {
                        estado = 34;
                    }else if (c == '+') {
                        Token t = new Token(TipoToken.PLUS, "+");
                        tokens.add(t);
                    } else if(c == '-'){
                        Token t = new Token(TipoToken.MINUS, "-");
                        tokens.add(t);
                    }else if(c == '*'){
                        Token t = new Token(TipoToken.STAR, "*");
                        tokens.add(t);
                    }else if(c == ';'){
                        Token t = new Token(TipoToken.SEMICOLON, ";");
                        tokens.add(t);
                    }else if (c == ',') {
                        Token t = new Token(TipoToken.COMMA, ",");
                        tokens.add(t);
                    }else if (c == '.'){
                        Token t = new Token(TipoToken.DOT, ".");
                        tokens.add(t);
                    }else if (c == '{') {
                        Token t = new Token(TipoToken.LEFT_BRACE, "{");
                        tokens.add(t);
                    }else if (c == '}'){
                        Token t = new Token(TipoToken.RIGHT_BRACE, "}");
                        tokens.add(t);
                    }else if (c == '(') {
                        Token t = new Token(TipoToken.LEFT_PAREN, "(");
                        tokens.add(t);
                    }else if (c == ')') {
                        Token t = new Token(TipoToken.RIGHT_PAREN, ")");
                        tokens.add(t);
                        
                    }else{
                        String mensaje = "Caracter no valido: " + c;
                        throw new RuntimeException(mensaje);
                    }
                    break;
                    //Operaciones relacionales y otros 1 - 10
                case 1:
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.GREATER_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        Token t = new Token(TipoToken.GREATER, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 4:
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.LESS_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        Token t = new Token(TipoToken.LESS, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 7:
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.EQUAL_EQUAL, lexema);
                        tokens.add(t);
                        
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        Token t = new Token(TipoToken.EQUAL, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 10: 
                    if (c == '=') {
                        lexema += c;
                        Token t = new Token(TipoToken.BANG_EQUAL, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                    }
                    else{
                        Token t = new Token(TipoToken.BANG, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    //Identificadores y palabras reservadas
                case 13:
                    if(Character.isLetterOrDigit(c)){
                        //estado = 13;
                        lexema += c;
                    }else{
                        TipoToken tt = palabrasReservadas.get(lexema);
                        if(tt == null){
                            Token t = new Token(TipoToken.IDENTIFIER, lexema);
                            tokens.add(t);
                        }else{
                            Token t = new Token(tt, lexema);
                            tokens.add(t);
                            
                        }
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    //Numeros sin signo 15 - 20
                case 15:
                    if (Character.isDigit(c)) {
                        lexema += c;
                    }else if(c == '.'){
                        estado = 16;
                        lexema += c;
                    }
                    else if(c=='E'){
                        estado = 18;
                        lexema += c;
                    
                    }
                    else{
                         Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                         tokens.add(t);
                         estado = 0;
                         lexema = "";
                         i--;
                    }
                    break;
                case 16:
                    if (Character.isDigit(c)){
                        estado = 17;
                        lexema += c;
                    }else{
                        String mensaje = "Numero mal formado";
                        throw new RuntimeException(mensaje);
                    }
                    break;
                case 17:
                    if(Character.isDigit(c)){
                        lexema += c;
                    }else if(c == 'E'){
                        estado = 18;
                        lexema += c;
                    }
                    else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                case 18:
                    if (c == '+' || c == '-') {
                        estado = 19;
                        lexema += c;
                    }else if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    break;
                case 19:
                    if(Character.isDigit(c)){
                        estado = 20;
                        lexema += c;
                    }
                    break;
                case 20:
                    if(Character.isDigit(c)){
                        lexema += c;
                    }else{
                        Token t = new Token(TipoToken.NUMBER, lexema, Double.valueOf(lexema));
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--; 
                    }
                    break;
                    //Reconocimiento de cadenas
                case 24:
                    if(c == '"'){
                        lexema += c;
                        String literal = lexema.substring(1, lexema.length()-1);
                        Token t = new Token(TipoToken.STRING, lexema,literal);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        
                    }else if(c=='\n'){
                        String mensaje = "Fin de la cadena no econtrado en la linea";
                        throw new RuntimeException(mensaje);
                        
                    }else
                        lexema +=c;
                    break;
                    //Slash
                case 26:
                    if(c == '*'){
                        estado = 27;
                    }else if(c == '/'){
                        estado = 30;
                    }else{
                        Token t = new Token(TipoToken.SLASH, lexema);
                        tokens.add(t);
                        estado = 0;
                        lexema = "";
                        i--;
                    }
                    break;
                    //Comentarios multilinea
                case 27:
                    if(c == '*'){
                        estado = 28;
                    }
                    break;
                    //Comentarios multilinea
                case 28:
                    if (c == '/') {
                        estado = 0;
                        lexema = "";
                    }else if (c !='*') {
                        estado = 27;
                    }
                    break;
                    //Comentarios de una sola linea
                case 30:
                    if (c == '\n') {
                        estado = 0;
                        lexema = "";
                    }
                    break;
                    //Delimitadores, espacios en blanco
                case 34: 
                    if (!(Character.isWhitespace(c))) {
                        estado = 0;
                        i--;
                    }
                    break;
                    
                default:
                    System.out.println("Error Lexico");
                    throw new AssertionError();
                    
            }
        }
        tokens.add(new Token(TipoToken.EOF, ""));
        return tokens;
    }
}
