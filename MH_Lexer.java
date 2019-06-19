
// File:   MH_Lexer.java

// Java template file for lexer component of Informatics 2A Assignment 1.
// Concerns lexical classes and lexer for the language MH (`Micro-Haskell').


import java.io.* ;

class MH_Lexer extends GenLexer implements LEX_TOKEN_STREAM {

static class VarAcceptor extends Acceptor implements DFA {
    public String lexClass(){return "VAR";};
    public int numberOfStates(){return 3;};
    int next(int state, char c)
    {
        switch(state)  // 
        {
            case 0: if(CharTypes.isSmall(c)) return 1; else return 2; // The first alphabet cannot be capital
            case 1: if(CharTypes.isLetter(c) || CharTypes.isDigit(c) || c == '\'') return 1; else return 2;
            default: return 2;
        }
    }
    boolean accepting(int state){return(state == 1);}
    int dead(){return 2;}
}

static class NumAcceptor extends Acceptor implements DFA {
    public String lexClass(){return "NUM";};
    public int numberOfStates(){return 4;};
    int next(int state, char c)
    {
        switch(state)
        {
            case 0: if(c == '0') return 1; else if(CharTypes.isDigit(c)) return 2; else return 3; 
            case 1: return 3; // Only accpet 0 when 0 is the first digit
            case 2: if(CharTypes.isDigit(c)) return 2; else return 3;
            default: return 3;
        }
    }
    boolean accepting(int state){return(state == 1 || state == 2);}
    int dead(){return 3;}
}

static class BooleanAcceptor extends Acceptor implements DFA {
    public String lexClass(){return "BOOLEAN";};
    public int numberOfStates(){return 10;};
    int next(int state, char c)
    {
        switch(state)
        {
            case 0: if(c == 'T') return 1; else if(c == 'F') return 5; else return 9;
            case 1: if(c == 'r') return 2; else return 9;
            case 2: if(c == 'u') return 3; else return 9;
            case 3: if(c == 'e') return 4; else return 9;
            case 4: return 9;
            case 5: if(c == 'a') return 6; else return 9;
            case 6: if(c == 'l') return 7; else return 9;
            case 7: if(c == 's') return 8; else return 9;
            case 8: if(c == 'e') return 4; else return 9;
            default: return 9;
        }
    }
    boolean accepting(int state){return(state == 4);}
    int dead(){return 9;}
}

static class SymAcceptor extends Acceptor implements DFA {
    public String lexClass(){return "SYM";};
    public int numberOfStates(){return 3;};
    int next(int state, char c)
    {
        switch(state)
        {
            case 0: if(CharTypes.isSymbolic(c)) return 1; else return 2;
            case 1: if(CharTypes.isSymbolic(c)) return 1; else return 2;
            default: return 2;
        }
    }
    boolean accepting(int state){return(state == 1);}
    int dead(){return 2;}
}

static class WhitespaceAcceptor extends Acceptor implements DFA {
    public String lexClass(){return "";}; // Ignore the whitespace
    public int numberOfStates(){return 3;};
    int next(int state, char c)
    {
        switch(state)
        {
            case 0: if(CharTypes.isWhitespace(c)) return 1; else return 2;
            case 1: if(CharTypes.isWhitespace(c)) return 1; else return 2;
            default: return 2;
        }
    }
    boolean accepting(int state){return(state == 1);}
    int dead(){return 2;}
}

static class CommentAcceptor extends Acceptor implements DFA {
    public String lexClass(){return "";}; // Ignore the comment
    public int numberOfStates(){return 5;};
    int next(int state, char c)
    {
        switch(state)
        {
            case 0: if(c == '-') return 1; else return 4;
            case 1: if(c == '-') return 2; else return 4;
            case 2: if(c == '-') return 2; else if(CharTypes.isSymbolic(c) || CharTypes.isNewline(c)) return 4; else return 3;
            case 3: if(CharTypes.isNewline(c)) return 4; else return 3;
            default: return 4;
        }
    }
    boolean accepting(int state){return(state == 2 || state == 3);}
    int dead(){return 4;}
}

static class TokAcceptor extends Acceptor implements DFA {

    String tok ;
    int tokLen ;
    TokAcceptor (String tok) {this.tok = tok ; tokLen = tok.length() ;}

    public String lexClass(){return tok;};
    public int numberOfStates(){return (tokLen + 2);};
    int next(int state, char c)
    {
        if((state < tokLen) && (c == tok.charAt(state)))
            {
                state++;
                return state;
            }
        else return (tokLen+1);
    }
    boolean accepting(int state){return(state == tokLen);}
    int dead(){return (tokLen+1);}
}

    static DFA Var_Acc = new VarAcceptor() ;
    static DFA Num_Acc = new NumAcceptor() ;
    static DFA Bool_Acc = new BooleanAcceptor() ;
    static DFA Sym_Acc = new SymAcceptor() ;
    static DFA WS_Acc = new WhitespaceAcceptor() ;
    static DFA Comment_Acc = new CommentAcceptor() ;
    static DFA Text_Int_Acc = new TokAcceptor("Integer");
    static DFA Text_Bool_Acc = new TokAcceptor("Bool");
    static DFA Text_If_Acc = new TokAcceptor("if");
    static DFA Text_Then_Acc = new TokAcceptor("then");
    static DFA Text_Else_Acc = new TokAcceptor("else");
    static DFA Text_LB_Acc = new TokAcceptor("(");
    static DFA Text_RB_Acc = new TokAcceptor(")");
    static DFA Text_Semi_Acc = new TokAcceptor(";");

    static DFA[] MH_acceptors = new DFA[] {Text_If_Acc, Text_Then_Acc, Text_Else_Acc, Text_LB_Acc, Text_RB_Acc, Text_Semi_Acc, Var_Acc, Num_Acc, Bool_Acc, Sym_Acc, WS_Acc, Comment_Acc, Text_Int_Acc, Text_Bool_Acc};
    // Firstly detect whether it is a special token, so put if, then and else at the front
    MH_Lexer (Reader reader) {
    super(reader,MH_acceptors) ;
    }

}

