uses
   Classes, Generics.Collections,SysUtils;
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

var
  f,g:Text;
  parstr,help,now:string;
  flag:boolean;
  axioms,allExpressions:TList;
  map: TDictionary<string, Expression>;
  exp:Expression;



constructor Expression.Create;
  begin
  end;

constructor Expression.Create(literal:string);
  begin
    Self.literal:=literal;
    Self.op:='s';
    Self.left:=nil;
    Self.right:=nil;
  end;

constructor Expression.Create(left:Expression;right:Expression;operation:Char);
  begin
    Self.left:=left;
    Self.right:=right;
    Self.op:=operation;
    Self.literal:='null';
  end;

function parse(l:integer;r:integer):Expression;
  var
    balance,i:integer;
  begin
    balance:=0;
    if (l > r) then
      begin
        parse:=nil;
        exit;
      end;
    for I := l to r do
      begin
        if (parstr[i] = '(') then
          begin
            inc(balance);
            continue;
          end;
        if (parstr[i] = ')') then
          begin
            balance:=balance-1;;
            continue;
          end;
        if (balance = 0) and (parstr[i] = '>') then
          begin
            parse:=Expression.Create(parse(l,i-1),parse(i+1,r),'>');
            Exit;
          end;
      end;
    balance:=0;
    for I := l to r do
      begin
        if (parstr[i] = '(') then
          begin
            inc(balance);
            continue;
          end;
        if (parstr[i] = ')') then
          begin
            balance:=balance-1;;
            continue;
          end;
        if (balance = 0) and (parstr[i] = '|') then
          begin
            parse:=Expression.Create(parse(l,i-1),parse(i+1,r),'|');
            Exit;
          end;
      end;
    balance:=0;
    for I := l to r do
      begin
        if (parstr[i] = '(') then
          begin
            inc(balance);
            continue;
          end;
        if (parstr[i] = ')') then
          begin
            balance:=balance-1;;
            continue;
          end;
        if (balance = 0) and (parstr[i] = '&') then
          begin
            parse:=Expression.Create(parse(l,i-1),parse(i+1,r),'&');
            Exit;
          end;
      end;
    balance:=0;
    for I := l to r do
      begin
        if (parstr[i] = '(') then
          begin
            inc(balance);
            continue;
          end;
        if (parstr[i] = ')') then
          begin
            balance:=balance-1;;
            continue;
          end;
        if (balance = 0) and (parstr[i] = '!') then
          begin
            parse:=Expression.Create(parse(l,i-1),parse(i+1,r),'!');
            Exit;
          end;
      end;
    if (parstr[l] <> '(') then
      begin
        parse:=Expression.create(copy(parstr,l,r+1-l));
        exit;
      end;
    parse:=parse(l+1,r-1);
  end;

procedure addAxioms();
  begin
    parstr := 'a>(b>a)';
    axioms.add(parse(1, length(parstr)));
    parstr := '(c>b)>(c>b>d)>(c>d)';
    axioms.add(parse(1, length(parstr)));
    parstr := 'c>b>(c&b)';
    axioms.add(parse(1, length(parstr)));
    parstr := 'c&a>c';
    axioms.add(parse(1, length(parstr)));
    parstr := 'c&a>a';
    axioms.add(parse(1, length(parstr)));
    parstr := 'c>c|b';
    axioms.add(parse(1, length(parstr)));
    parstr := 'b>c|b';
    axioms.add(parse(1, length(parstr)));
    parstr := '(c>d)>(b>d)>(c|b>d)';
    axioms.add(parse(1, length(parstr)));
    parstr := '((c)>(b))>((c)>!(b))>!(c)';
    axioms.add(parse(1, length(parstr)));
    parstr := '!!a->a';
    axioms.add(parse(1, length(parstr)));
  end;

function equalsTree(one:Expression;two:Expression):Boolean;
  begin
    if (one=nil) and (two=nil) then
      begin
        equalsTree:=True;
        Exit;
      end;
    if (one=nil) and (two=nil) then
      begin
        equalsTree:=False;
        Exit;
      end;
    equalsTree:=(one.op=two.op) and (one.literal=two.literal) and equalsTree(one.right,two.right) and equalsTree(one.left,two.left);
  end;

function equals(my:Expression; axiom:Expression):boolean;
  var
    temp:expression;
  begin
    if (my=nil) and (axiom=nil) then
      begin
        equals:=true;
        exit;
      end;
    if (my=nil) or (axiom=nil) then
      begin
        equals:=false;
        exit;
      end;
    if (my.op=axiom.op) and (my.op<>'s') then
      begin
        equals:=equals(my.left,axiom.left) and equals(my.right,axiom.right);
        exit;
      end;
    if (axiom.op='s') then
      begin
        if (not map.containsKey(axiom.literal)) then
          begin
            map.Add(axiom.literal,my);
            equals:=true;
            exit;
          end;
        map.TryGetValue(axiom.literal,temp);
        if (equalsTree(my,temp)) then
          begin
            equals:=true;
            Exit;
          end
        else
          begin
            equals:=False;
            Exit;
          end;

      end;
    equals:=False;
  end;

function axiomSatisfy(exp:Expression):Boolean;
  var
    i:Integer;
  begin
    for I := 0 to axioms.Count - 1 do
      begin
        map.Clear;
        if (equals(exp,axioms[i])) then
          begin
            help:='Сх. акс. '+IntToStr(i+1);
            axiomSatisfy:=True;
            exit;
          end;
      end;
    axiomSatisfy:=False;
  end;

function checkMP(exp:Expression):Boolean;
  var
    i,j:Integer;
    temp,left:Expression;
  begin
    for I := 0 to allExpressions.Count - 1 do
      begin
        temp:=allExpressions[i];
        if (temp.op <> '>') then
          Continue;
        if (equalsTree(temp.right,exp)) then
          begin
            left:=temp.left;

            for j := 0 to allExpressions.Count - 1 do
              if (equalsTree(left,allExpressions[j])) then
                begin
                  help:='M.P. '+inttostr(j+1)+', '+inttostr(i+1);
                  checkMP:=True;
                  Exit;
                end;
          end;

      end;
    checkMP:=False;
  end;


begin
  flag:=True;
  Assign(f,'proof0.out');
  Assign(g,'proooff.out');
  Reset(f);
  Rewrite(g);
  map:= TDictionary<String, Expression>.Create;
  axioms:=TList.Create;
  allExpressions := Tlist.Create;
  addAxioms();
  while (not Eof(f)) do
    begin
      help:='';
      Readln(f,parstr);
      parstr:=StringReplace(parstr,'->','>',[rfReplaceAll]);
      exp:=parse(1, Length(parstr));
      allExpressions.Add(exp);
      if not (axiomSatisfy(Exp) or checkMP(exp)) then
          Writeln(g,'(',allExpressions.Count,') ',parstr,' (Не доказано)')
      else
        Writeln(g,'(',allExpressions.Count,') ',StringReplace(parstr,'>','->',[rfReplaceAll]),' (',help,')');
    end;
  Close(f);
  Close(g);
end.
