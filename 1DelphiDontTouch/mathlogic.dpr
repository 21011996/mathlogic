uses
   Classes, Generics.Collections;
type
  Expression=class
  public
    left,right:Expression;
    literal:string;
    op:Char;
    constructor Create; overload;
    constructor Create(literal:string); overload;
    constructor Create(left:Expression;right:Expression;operation:Char); overload;
  end;

  ArrayOfExpressions=array of Expression;

var
  f,g:Text;
  parstr,help:string;
  axioms,allExpressions:ArrayOfExpressions;
  map: TDictionary<string, Expression>;



constructor Expression.Create;
  begin
  end;

constructor Expression.Create(literal:string);
  begin
    Self.literal:=literal;
    Self.op:='s';
    Self.left:=Expression.Create;
    Self.right:=Expression.Create;
  end;

constructor Expression.Create(left:Expression;right:Expression;operation:Char);
  begin
    Self.left:=left;
    Self.right:=right;
    Self.op:=operation;
    Self.literal:='null';
  end;
  

begin
  Assign(f,'axiom.in');
  Assign(g,'axiom.out');
  Reset(f);
  Rewrite(g);





  Close(f);
  Close(g);


end.

