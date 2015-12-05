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
type
  Strong=class
  public
    value:string;
    constructor create(a:string); overload;
  end;
type
  Intonger=class
  public
    value:Integer;
    constructor create(a:Integer); overload;
  end;

var
  f,g:Text;
  parstr,help,now,start,strbeta,stralpha:string;
  flag:boolean;
  axioms,allExpressions,allAdmissions:TList;
  strExpressions,strAdmissions:TList;
  modusPonens:Tlist;
  map: TDictionary<string, Expression>;
  exp,alpha:Expression;
  i:Integer;
  stronger,stronger2:Strong;
  intoger:Intonger;

constructor Strong.create(a: string);
begin
  value:=a;
end;

constructor Intonger.create(a: Integer);
begin
  value:=a;
end;

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
                  help:='M.P. '+inttostr(i+2)+', '+inttostr(j+2);
                  checkMP:=True;
                  Exit;
                end;
          end;

      end;
    checkMP:=False;
  end;

procedure makeAdmissions(first:string);
  var
    allAdm:string;
    last,i:Integer;
  begin
    allAdm:=Copy(first,1,Pos('|',first)-1);
    parstr:=Copy(first,Pos('|',first)+2,Length(first)-Pos('|',first)+1);
    strbeta:=parstr;
    last:=1;
    for I := 1 to Length(allAdm) do
      begin
        if (allAdm[i]=',') then
          begin
            parstr:=Copy(allAdm,last,i-last);
            strAdmissions.Add(Strong.create(parstr));
            parstr:=StringReplace(parstr,'->','>',[rfReplaceAll]);
            allAdmissions.Add(parse(1,Length(parstr)));
            last:=i+1;
          end;
        if (i=Length(allAdm)) then
          begin
            parstr:=Copy(allAdm,last,i+1-last);
            strAdmissions.Add(Strong.create(parstr));
            stralpha:=parstr;
            parstr:=StringReplace(parstr,'->','>',[rfReplaceAll]);
            allAdmissions.Add(parse(1,Length(parstr)));
            alpha:=allAdmissions[allAdmissions.Count-1];
            if (last=1) then
              start:='|-('+stralpha+')->('+strbeta+')'
            else
              start:=Copy(first,1,last-2)+'|-('+stralpha+')->('+strbeta+')';
          end;
      end;
  end;

function checkAdmissions(Exp:Expression):Boolean;
  var
    i:Integer;
  begin
    for I := 0 to allAdmissions.Count - 1 do
      if (equalsTree(exp,allAdmissions[i])) then
        begin
          help:='Допущение '+inttostr(i+1);
          checkAdmissions:=True;
          Exit;
        end;
    checkAdmissions:=False;
  end;

procedure printMP(beta1,beta2:string);
  var
    temp,temp1:string;
  begin
    temp := '((' + stralpha + ')->(' + beta1 + '))';
    temp1 := '((' + beta2 + ')->(' + beta1 + '))';
    Writeln(g,'((' + stralpha + ')->(' + beta2 + '))->(((' + stralpha + ')->' + temp1 + ')->' + temp + ')');
    Writeln(g,'(((' + stralpha + ')->' + temp1 + ')->' + temp + ')');
    Writeln(g,'(' + stralpha + ')->(' + beta1 + ')');
  end;

procedure printLemm(strExp:string);
  var
    temp:string;
  begin
    temp := '((' + strExp + ')->(' + strExp + '))';
    Writeln(g,'(' + strExp + ')->' + temp);
    Writeln(g,'((' + strExp + ')->' + temp + ')->((' + strExp + ')->' + temp + '->(' + strExp + '))->' + temp);
    Writeln(g,'((' + strExp + ')->(' + temp + '->(' + strExp + ')))->' + temp);
    Writeln(g,'(' + strExp + ')->' + '(' + temp + '->(' + strExp + '))');
    Writeln(g,'(' + strExp + ')->(' + strExp + ')');
  end;




begin
  flag:=True;
  Assign(f,'proof0.in');
  Assign(g,'proof0.out');
  Reset(f);
  Rewrite(g);
  map:= TDictionary<String, Expression>.Create;
  axioms:=TList.Create;
  allExpressions:=TList.Create;
  strExpressions:=TList.Create;
  allAdmissions:=TList.Create;
  strAdmissions:=TList.Create;
  modusPonens:=TList.Create;
  addAxioms();
  Readln(f,now);
  makeAdmissions(now);
  flag:=False;
  while (not Eof(f)) do
    begin
      modusPonens.Add(Intonger.create(-1));
      help:='';
       Readln(f,now);
      parstr:=now;
      strExpressions.Add(strong.create(parstr));
      parstr:=StringReplace(parstr,'->','>',[rfReplaceAll]);
      exp:=parse(1,Length(parstr));
      allExpressions.Add(exp);
      flag:=axiomSatisfy(exp);
      if (not flag) then
        flag:=checkMP(exp);
      if (not flag) then
        flag:=checkAdmissions(exp);
      if (not flag) then
        begin
          Writeln(g,'Ошибка в ',allExpressions.Count);
          break;
        end;
    end;
  Writeln(g,start);
  allAdmissions.Delete(allAdmissions.Count-1);
  strAdmissions.Delete(strAdmissions.Count-1);
  for I := 0 to allExpressions.Count - 1 do
    begin
      help:='';
      exp:=allExpressions[i];
      stronger:=strExpressions[i];
      now:=stronger.value;
      if (checkAdmissions(exp)) then
        begin
          Writeln(g,now);
          Writeln(g,'('+now+')->(('+stralpha+')->('+now+'))');
          Writeln(g,'('+stralpha+')->('+now+')');
          //Writeln(g,' '+help);
          Continue
        end;
      if (axiomSatisfy(exp)) then
        begin
          Writeln(g,now);
          Writeln(g,'('+now+')->(('+stralpha+')->('+now+'))');
          Writeln(g,'('+stralpha+')->('+now+')');
          //Writeln(g,' '+help);
          Continue
        end;
      if (equalsTree(exp,alpha)) then
        begin
          printLemm(now);
          help:='Лемма';
          //Writeln(g,' '+help);
          Continue
        end;
      if (checkAdmissions(exp)) then
        begin
          stronger:=strExpressions[i];
          intoger:=modusPonens[i];
          stronger2:=strExpressions[intoger.value];
          printMP(stronger.value,stronger2.value);
          help:='MP '+inttostr(i+1)+' '+inttostr(intoger.value+1);
          //Writeln(g,' '+help);
          Continue
        end;
    end;

  Close(f);
  Close(g);
end.
