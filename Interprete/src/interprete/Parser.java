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
        //program();

        if (preanalisis.getTipo() != TipoToken.EOF) {
            String mensaje = "Se encontro un error en el programa";
            throw new ParserException(mensaje);
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
