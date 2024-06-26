package interprete;

public class Token {

    final TipoToken tipo;
    final String lexema;
    final Object literal;

    public Token(TipoToken tipo, String lexema) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = null;
    }

    public Token(TipoToken tipo, String lexema, Object literal) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.literal = literal;
    }

    public TipoToken getTipo(){
        return tipo;
    }

    public String getLexema(){
        return lexema;
    }

    public Object getLiteral(){
        return literal;
    }

    @Override
    public String toString() {
        return "<" + tipo + " " + lexema + " " + literal + ">";
    }
}
