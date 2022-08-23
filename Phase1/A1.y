%{
    #include <stdio.h>
    #include <string.h>
    #include <stdlib.h>
    #include <assert.h>
    extern void yyerror();
    extern int yylex();

struct node{
    char* word;
    struct node* next;
};
struct wordList{
    struct node* head;
    struct node* tail;
};
struct macroNode{
    char* macroName;
    struct wordList* args;
    struct wordList* expansion;
    int numArgs;
    struct macroNode *next;
};
struct macroList{
    struct macroNode* head;
    struct macroNode* tail;
};
struct macroList* DS  = NULL;
struct macroList* DS0 = NULL;
struct macroList* DS1 = NULL;
struct macroList* DS2 = NULL;
struct macroList* DE = NULL;
struct macroList* DE0 = NULL;
struct macroList* DE1 = NULL;
struct macroList* DE2 = NULL;
void printList(struct wordList* l){
    struct node* temp = l->head;
    while(temp != NULL){
        printf("%s ",temp->word);
        temp = temp->next;
    }
    printf("\n");
}
void insertWord(struct wordList* h,char* s){
    struct node* temp = (struct node*)malloc(sizeof(struct node));
    temp->word = (char*)malloc((strlen(s)+1)*sizeof(char));
    strcpy(temp->word,s);
    temp->next = NULL;
    if(h->head == NULL){
        h->head = temp;
        h->tail = temp;
    }
    else{
        h->tail->next = temp;
        h->tail = temp;    
    }
    return;
    
}
void insertNode(struct wordList* h,struct node* n){
    struct node* temp = (struct node*)malloc(sizeof(struct node));
    temp->word = (char*)malloc((strlen(n->word)+1)*sizeof(char));
    strcpy(temp->word,n->word);
    temp->next = NULL;
    assert(h!=NULL);
    if(h->head == NULL){
        h->head = temp;
        h->tail = temp;
    }
    else{
        h->tail->next = temp;
        h->tail = temp;
    }
}
struct wordList* makeNullList(){
    struct wordList* temp = (struct wordList*)malloc(sizeof(struct wordList));
    assert(temp!=NULL);
    temp->head = NULL;
    temp->tail = NULL;
    return temp;
}

void joinLists(struct wordList* l1, struct wordList* l2){
    assert(l1 != NULL && l2 != NULL);
    if(l1->head == NULL){
        if(l2->head != NULL){
            l1->head = l2->head;
            l1->tail = l2->tail;
        }
    }
    else {
        //printf("DOINGRIGHT\n");
        if(l2->head != NULL){
            assert(l1->tail != NULL);
            l1->tail->next = l2->head;
            l1->tail = l2->tail;
        }
    }
     //printf("MERGESUCCESS\n");
}
struct macroList* addMacro(struct macroList* h, int n,char* name, struct wordList* args ,struct wordList* exp){
    struct macroNode* temp = (struct macroNode*)malloc(sizeof(struct macroNode));
    temp->macroName = (char*)malloc((strlen(name)+1)*sizeof(char));
    strcpy(temp->macroName, name);
    temp->args =  args;
    temp->expansion = exp;
    temp->numArgs = n;
    temp->next = NULL;
    if(h == NULL){
        struct macroList* t = (struct macroList*)malloc(sizeof(struct macroList));
        t->head = temp;
        t->tail = temp;
        return t;
    }
    else{
        h->tail->next = temp;
        h->tail = temp;
        return h;
    }
}
struct wordList* searchAndReplace(int n, struct macroNode* reqMacro,struct wordList* newargs){
    struct wordList* finalList = makeNullList();
    struct wordList** tempList =(struct wordList**)calloc(n,sizeof(struct wordList*));
    struct node* tempPtr = newargs->head;
    if(n == 0){
        return reqMacro->expansion;
    }
    int i = 0;
    tempList[i] = makeNullList();
    while(tempPtr!=NULL){
        if(strcmp(tempPtr->word,",") == 0){
            i++;
            tempList[i]=makeNullList();
        }
        else{
            insertNode(tempList[i],tempPtr);
        }
        tempPtr = tempPtr->next;
    }
    struct node* reqArgsPtr;
    tempPtr = reqMacro->expansion->head;
    while(tempPtr!=NULL){
        reqArgsPtr = reqMacro->args->head;
        i = 0;
        while(reqArgsPtr!=NULL){
            if(strcmp(tempPtr->word,reqArgsPtr->word)==0){
                joinLists(finalList,tempList[i]);
                break;
            }
            i++;
            reqArgsPtr=reqArgsPtr->next;
        }
        if(reqArgsPtr==NULL){
            insertNode(finalList,tempPtr);
        }
        tempPtr = tempPtr->next;
    }
    tempPtr = finalList->head;
    while(tempPtr!=NULL){
        if(tempPtr->next != NULL){
            struct node * lastp = tempPtr->next;
            if(lastp->next == NULL){
                if(strcmp(lastp->word,";")==0){
                    finalList->tail = tempPtr;
                    tempPtr->next = NULL;
                    break;
                }
            }    
        }
        tempPtr = tempPtr->next;
    }
    //printList(finalList);
    return finalList;
}
struct wordList* expandMacro(int n,char* name,struct wordList* arguments){
    int numOfArgs = 0;
    struct macroNode* macroPtr = NULL;
    struct macroList* checkMacrosList;
    if(n == 0){
        if(arguments->head == NULL){                
            checkMacrosList = DS0;
        }
        else{
            struct node* ptr = arguments->head;
            numOfArgs = 1;
            while(ptr!= arguments->tail){
                if(strcmp(ptr->word,",") == 0){
                    numOfArgs++;
                }
                ptr = ptr->next;
            }
            if(numOfArgs == 1){
                checkMacrosList = DS1;
            }
            if(numOfArgs == 2){
                checkMacrosList = DS2;
            }
            if(numOfArgs >  2){
                checkMacrosList = DS;
            }
        }
        if(checkMacrosList == NULL){
            //printf("CHECKMACRONULL IN STMT\n");
            yyerror();
            exit(0);
        }
        else{
            macroPtr = checkMacrosList->head;
            while(macroPtr != NULL){
                if(strcmp(macroPtr->macroName,name)==0){
                    break;
                }
                macroPtr = macroPtr->next;
            }
        }
    }
    if(n == 1){
        if(arguments->head == NULL){              
            checkMacrosList = DE0;
            if(DE0 == NULL){
            }
        }
        else{
            struct node* ptr = arguments->head;
            numOfArgs = 1;
            while(ptr!= arguments->tail){
                if(strcmp(ptr->word,",") == 0){
                    numOfArgs++;
                }
                ptr = ptr->next;
            }
            if(numOfArgs == 1){
                checkMacrosList = DE1;
            }
            if(numOfArgs == 2){
                checkMacrosList = DE2;
            }
            if(numOfArgs >  2){
                checkMacrosList = DE;
            }
        }
        if(checkMacrosList == NULL){
            //printf("CHECKMACRONULL IN EXPR\n");
            yyerror();
            exit(0);
        }
        else{
            macroPtr = checkMacrosList->head;
            while(macroPtr != NULL){
                if(strcmp(macroPtr->macroName,name)==0){
                    break;
                }
                macroPtr = macroPtr->next;
            }
        }
    }
    if(macroPtr == NULL){
        //printf("MACRONOTFOUND IN STMT\n");
        yyerror();
        exit(0);
    }
    return searchAndReplace(numOfArgs,macroPtr,arguments);
}



%}

%union{
    //char  singleChar;
    char* charStr;
    struct wordList* list;
}

%token<charStr> DEFSTMT DEFSTMT0 DEFSTMT1 DEFSTMT2
%token<charStr> DEFEXPR DEFEXPR0 DEFEXPR1 DEFEXPR2
%token<charStr> CLASS PUBLIC STATIC VOID MAIN EXTENDS RETURN LENGTH
%token<charStr> STRING INT BOOL TRUE FALSE THIS NEW
%token<charStr> SYSPRINT IF ELSE WHILE

%token<charStr> LPAR RPAR LCURLY RCURLY LSQUARE RSQUARE SEMICOL COMMA DOT NOT
%token<charStr> BOOLOP EQUAL RELOP ARITHOP NUM ID EOF_token

%type<list> goal mainClass methodDec methodDecStar
%type<list> typeDecStar typeDec typeIDColStar type typeIDOnce commaTIDStar
%type<list> macroStar macroDef macroDefE macroDefS
%type<list> stmtStar statement
%type<list> expression exprOnce commaExprStar primExpr
%type<list> atleast3ID commaIDStar

%start goal
//
%%
goal  :   macroStar mainClass typeDecStar EOF_token{
                        $$ = $2;
                        joinLists($$,$3);
                        printList($$);
                        return 0;
                    }
macroStar   : macroDef macroStar{
                        $$ = makeNullList();
                    }
            | {$$ = makeNullList();}

typeDecStar : typeDec typeDecStar{
                        $$= makeNullList();
                        joinLists($$,$1);
                        assert($$!=NULL);
                        joinLists($$,$2);
                    }
            | {$$ = makeNullList();}

mainClass   : CLASS ID LCURLY PUBLIC STATIC VOID MAIN LPAR STRING LSQUARE RSQUARE ID RPAR LCURLY SYSPRINT LPAR expression RPAR SEMICOL RCURLY RCURLY{
                        $$ = makeNullList();
                        insertWord($$,$1);
                        insertWord($$,$2);
                        insertWord($$,$3);
                        insertWord($$,$4);
                        insertWord($$,$5);
                        insertWord($$,$6);
                        insertWord($$,$7);
                        insertWord($$,$8);
                        insertWord($$,$9);
                        insertWord($$,$10);
                        insertWord($$,$11);
                        insertWord($$,$12);
                        insertWord($$,$13);
                        insertWord($$,$14);
                        insertWord($$,$15);
                        insertWord($$,$16);
                        joinLists($$,$17);
                        insertWord($$,$18);
                        insertWord($$,$19);
                        insertWord($$,$20);
                        insertWord($$,$21);
                        //printList($$);
                    }

typeDec     : CLASS ID LCURLY typeIDColStar methodDecStar RCURLY{
                        $$ = makeNullList();
                        insertWord($$,$1);
                        insertWord($$,$2);
                        insertWord($$,$3);
                        joinLists($$,$4);
                        joinLists($$,$5);
                        insertWord($$,$6);
                        //printList($5);
                    }
            | CLASS ID EXTENDS ID LCURLY typeIDColStar methodDecStar RCURLY{
                        $$ = makeNullList();
                        insertWord($$,$1);
                        insertWord($$,$2);
                        insertWord($$,$3);
                        insertWord($$,$4);
                        insertWord($$,$5);
                        joinLists($$,$6);
                        joinLists($$,$7);
                        insertWord($$,$8);
                        //printList($$);
                    }

typeIDColStar  :  typeIDColStar type ID SEMICOL {
                        $$ =$1;
                        assert($$!=NULL);
                        joinLists($$,$2);
                        assert($1!=NULL);
                        assert($2!=NULL);
                        insertWord($$,$3);
                        insertWord($$,$4);
                        //printf("THISDONE\n");
                    }
            |   {$$ = makeNullList();}

methodDecStar   : methodDec methodDecStar{
                        $$ = $1;
                        assert($$!=NULL);
                        joinLists($$,$2);
                    }
                |   {$$ = makeNullList();}

methodDec   : PUBLIC type ID LPAR typeIDOnce RPAR LCURLY typeIDColStar stmtStar RETURN expression SEMICOL RCURLY{
                        $$ = makeNullList();
                        assert($$!=NULL);
                        insertWord($$,$1);
                        joinLists($$,$2);
                        insertWord($$,$3);
                        insertWord($$,$4);
                        joinLists($$,$5);
                        insertWord($$,$6);
                        insertWord($$,$7);
                        joinLists($$,$8);
                        joinLists($$,$9);
                        insertWord($$,$10);
                        joinLists($$,$11);
                        insertWord($$,$12);
                        insertWord($$,$13);
                        //printList($$);
                    }

typeIDOnce  : type ID commaTIDStar{
                        $$= makeNullList();
                        joinLists($$,$1);
                        assert($$!=NULL);
                        insertWord($$,$2);
                        joinLists($$,$3);
                        //printList($3);
                    }
            | {$$ = makeNullList();}

commaTIDStar    : COMMA type ID commaTIDStar{
                        $$ = makeNullList();
                        insertWord($$,$1);
                        joinLists($$,$2);
                        insertWord($$,$3);
                        joinLists($$,$4);
                        //printList($4);
                    }
                |{$$ = makeNullList();}

type        : INT LSQUARE RSQUARE{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    insertWord($$,$3);
                    //printList($$);
                }
            | BOOL{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    //printList($$);
                }
            | INT{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    //printList($$);
                }
            | ID{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    //printList($$);
                }

stmtStar    : statement stmtStar {
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    joinLists($$,$2);
                }
            |{$$ = makeNullList();}
statement   : LCURLY stmtStar RCURLY{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    joinLists($$,$2);
                    insertWord($$,$3);
                }
            | SYSPRINT LPAR expression RPAR SEMICOL{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                    insertWord($$,$5);
                }
            | ID EQUAL expression SEMICOL{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                }
            | ID LSQUARE expression RSQUARE EQUAL expression SEMICOL{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                    insertWord($$,$5);
                    joinLists($$,$6);
                    insertWord($$,$7);
                }
            | ID LPAR exprOnce RPAR SEMICOL{    ////////////MACRO CALL
                    $$ = makeNullList();
                    //insertWord($$,$2);
                    struct wordList* temp = expandMacro(0,$1,$3);
                    joinLists($$,temp);
                    //insertWord($$,$4);
                    insertWord($$,$5);
                }
            | IF LPAR expression RPAR statement{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                    joinLists($$,$5);
                }
            | IF LPAR expression RPAR statement ELSE statement{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                    joinLists($$,$5);
                    insertWord($$,$6);
                    joinLists($$,$7);
                }
            | WHILE LPAR expression RPAR statement{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                    joinLists($$,$5);
                }
commaExprStar   :COMMA expression commaExprStar{
                        $$ = makeNullList();
                        insertWord($$,$1);
                        joinLists($$,$2);
                        joinLists($$,$3);
                    }
                |   { $$ = makeNullList();}
exprOnce    : expression commaExprStar{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    joinLists($$,$2);
                }
            |   {$$ = makeNullList();}


expression  : primExpr BOOLOP primExpr{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    insertWord($$,$2);
                    joinLists($$,$3);
                }
            | primExpr RELOP primExpr{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    insertWord($$,$2);
                    joinLists($$,$3);
                }
            | primExpr ARITHOP primExpr{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    insertWord($$,$2);
                    joinLists($$,$3);
                }
            | primExpr LSQUARE primExpr RSQUARE{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    insertWord($$,$2);
                    joinLists($$,$3);
                    insertWord($$,$4);
                }
            | primExpr DOT LENGTH{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    insertWord($$,$2);
                    insertWord($$,$3);
                }
            | primExpr{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                }
            | primExpr DOT ID LPAR exprOnce RPAR{
                    $$= makeNullList();
                    joinLists($$,$1);
                    assert($$!=NULL);
                    insertWord($$,$2);
                    insertWord($$,$3);
                    insertWord($$,$4);
                    joinLists($$,$5);
                    insertWord($$,$6);
                }
            | ID LPAR exprOnce RPAR{    ////////////MACRO CALL
                    $$ = makeNullList();
                    insertWord($$,$2);
                    struct wordList* temp = expandMacro(1,$1,$3);
                    joinLists($$,temp);
                    insertWord($$,$4);
                }

primExpr    :NUM{
                    $$ = makeNullList();
                    insertWord($$,$1);
                }
            |TRUE{
                    $$ = makeNullList();
                    insertWord($$,$1);
                }
            |FALSE{
                    $$ = makeNullList();
                    insertWord($$,$1);
                }
            |ID{
                    $$ = makeNullList();
                    insertWord($$,$1);
                }
            |THIS{
                    $$ = makeNullList();
                    insertWord($$,$1);
                }
            |NEW INT LSQUARE expression RSQUARE{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    insertWord($$,$3);
                    joinLists($$,$4);
                    insertWord($$,$5);
                }
            |NEW ID LPAR RPAR{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    insertWord($$,$3);
                    insertWord($$,$4);
                }
            |NOT expression{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    joinLists($$,$2);
                }
            |LPAR expression RPAR{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    joinLists($$,$2);
                    insertWord($$,$3);
                }

macroDef    :   macroDefE{$$ = $$ = makeNullList();}
            |   macroDefS{$$ = $$ = makeNullList();}
macroDefS   :   DEFSTMT ID LPAR atleast3ID RPAR LCURLY stmtStar RCURLY{
                    $$ = makeNullList();
                    DS = addMacro(DS,-1,$2,$4,$7);
                }
            |   DEFSTMT0 ID LPAR RPAR LCURLY stmtStar RCURLY{
                    $$ = makeNullList();
                    struct wordList* t = makeNullList();
                    DS0 = addMacro(DS0,0,$2,t,$6);
                }
            |   DEFSTMT1 ID LPAR ID RPAR LCURLY stmtStar RCURLY{
                    $$ = makeNullList();
                    struct wordList* t = makeNullList();
                    insertWord(t,$4);
                    DS1 = addMacro(DS1,1,$2,t,$7);
                }
            |   DEFSTMT2 ID LPAR ID COMMA ID RPAR LCURLY stmtStar RCURLY {
                    $$ = makeNullList();
                    struct wordList* t = makeNullList();
                    insertWord(t,$4);
                    insertWord(t,$6);
                    DS2 = addMacro(DS2,2,$2,t,$9);
                }
macroDefE   :DEFEXPR ID LPAR atleast3ID RPAR expression {
                    $$ = makeNullList();
                    DE = addMacro(DE,-1,$2,$4,$6);
                }
            |DEFEXPR0 ID LPAR RPAR LPAR expression RPAR {
                    $$ = makeNullList();
                    struct wordList* t = makeNullList();
                    DE0 = addMacro(DE0,0,$2,t,$6);
                }
            |DEFEXPR1 ID LPAR ID RPAR  LPAR expression RPAR {
                    $$ = makeNullList();
                    struct wordList* t = makeNullList();
                    insertWord(t,$4);
                    DE1 = addMacro(DE1,1,$2,t,$7);
                }
            |DEFEXPR2 ID LPAR ID COMMA ID RPAR  LPAR expression RPAR {
                    $$ = makeNullList();
                    struct wordList* t = makeNullList();
                    insertWord(t,$4);
                    insertWord(t,$6);
                    DE2 = addMacro(DE2,2,$2,t,$9);
                }
atleast3ID  :ID COMMA ID COMMA ID commaIDStar{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    insertWord($$,$3);
                    insertWord($$,$4);
                    insertWord($$,$5);
                    joinLists($$,$6);
                    //printList($$);
                }

commaIDStar :COMMA ID commaIDStar{
                    $$ = makeNullList();
                    insertWord($$,$1);
                    insertWord($$,$2);
                    joinLists($$,$3);
                }
            |   {
                    $$ = makeNullList();
                }

%%
