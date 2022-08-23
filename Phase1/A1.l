%{
    #include <stdio.h>
    #include <string.h>
    #include <stdlib.h>
    #include "A1.tab.h"
    void yyerror();
    int yyparse(void);
%}

%%
"#defineStmt"[0-2]? {
    //printf("%s",yytext);
    int len = strlen(yytext);
    if(yytext[len-1] == 't'){
        return DEFSTMT;
    }
    else if(yytext[len-1] == '0'){
        return DEFSTMT0;
    }
    else if(yytext[len-1] == '1'){
        return DEFSTMT1;
    }
    else if(yytext[len-1] == '2'){
        return DEFSTMT2;
    }
}
"#defineExpr"[0-2]? {
    //printf("%s",yytext);
    
    int len = strlen(yytext);
    if(yytext[len-1] == 't'){
        return DEFEXPR;
    }
    else if(yytext[len-1] == '0'){
        return DEFEXPR0;
    }
    else if(yytext[len-1] == '1'){
        return DEFEXPR1;
    }
    else if(yytext[len-1] == '2'){
        return DEFEXPR2;
    }
}
"class" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return CLASS;
}
"public" { 
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);   
    return PUBLIC;
}
"static" {  
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return STATIC;  
}
"void" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return VOID;
}
"main" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return MAIN;
}
"extends" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return EXTENDS;
}
"return" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return RETURN;
}
"String" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return STRING;
}
"int" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return INT;
}
"boolean" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return BOOL;
}
"System.out.println" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return SYSPRINT;
}
"if" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return IF;
}
"else" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return ELSE;
}
"while" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return WHILE;
}
"length" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return LENGTH;
}
"true" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return TRUE;
}
"false" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return FALSE;
}
"this" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return THIS;
}
"new" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return NEW;
}


0|([1-9][0-9]*) {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);
    return NUM;
}
[a-zA-Z][_a-zA-Z0-9]* {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);
    return ID;
}

"(" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return LPAR;
}
")" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);  
    return RPAR;
}
"{" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);   
    return LCURLY;
}
"}" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);   
    return RCURLY;
}
"[" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);  
    return LSQUARE;
}
"]" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);   
    return RSQUARE;
}
";" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);  
    return SEMICOL;
}
"," {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return COMMA;
}
"." {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);  
    return DOT;
}
"!" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return NOT;
}

("&&"|"||") {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return BOOLOP;
}
"="  {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return EQUAL;
}
"<="|"!=" {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext); 
    return RELOP;
}
("+"|"-"|"*"|"/") {
    //printf("%s",yytext);
    yylval.charStr = (char *)malloc((strlen(yytext)+1)*sizeof(char));
    strcpy(yylval.charStr,yytext);  
    return ARITHOP;
}

("//"[^\n]*|"/*"([^*]|"*"[^/])*"*/") ;
[ \t\n\r\f]+		;

<<EOF>> {return EOF_token;}
. {
    //printf("INVALID CHAR\n");
    yyerror();
    return 1;
}


%%
void yyerror(){
    printf("/");
    printf("/Failed to parse input code\n");
    exit(0);
}
int main(int argc, char ** argv){
    if(argc == 1){
        //input from stdin
    }
    else if(argc > 1){
        yyin = fopen(argv[1],"r");
        if(yyin == NULL){
            perror("Error :");
            return -1;
        }
    }
    yyparse();
}