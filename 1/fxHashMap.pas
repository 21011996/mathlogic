unit fxHashMap;

interface

uses SysUtils, Classes, StrUtils, Variants{, QStrings};

type
  THashNode = class;

  //Тип элемента
  THashNodeType = (hntEmpty, hntElement, hntArray, hntObject);

  //Масив элементов
  TNodeArray = array of THashNode;

  //Тип хначения хэш-функции
  THashKey = Cardinal;

  //Описание элемента
  THashNode = class(TObject)
  private
    FParent: THashNode;
    FHashKey: THashKey;         //Хэш ключа
    FKey: Variant;              //Ключ
    FValue: Variant;            //Простое значение
    FEmptyNode: THashNode;      //Указатель на пустую ноду для вставки в массив
    FNodeType: THashNodeType;   //Тип элемента
    FNodeArray: TNodeArray;     //Указатель на Array
    function GetArrayValue(const _Key: Variant): THashNode;
    procedure SetKey(const _Value: Variant);
    procedure SetValue(const _Value: Variant);
    procedure SetElementFromEmpty(NodeParent: THashNode);
    procedure SetArrayFromElement(NodeParent: THashNode);
    procedure ArrayToObject;
  protected
    //Поиск ноды в NodeArray
    function FindArrayNode(const _Key: Variant): THashNode; virtual;
    //Получение хэша ключа
    function Hash(const _Key: Variant): THashKey; virtual;
    //Удаление всех значений из NodeArray
    procedure DelNodeArray; virtual;
    class function CompareNode(const A, B: THashNode): Integer; virtual;
    procedure SetNodeType(_NodeType: THashNodeType);
    //Разбор строки
    procedure Parse(const NodeString: String); virtual;
    //Создание пустой ноды
    procedure CreateEmptyNode; virtual;

    property EmptyNode: THashNode read FEmptyNode write FEmptyNode;
  public
    constructor Create(_Parent: THashNode);
    destructor Destroy; override;
    //Получение содержимого в виде строки
    function RPrint(const Level: Integer = 0): String;
    function Append: THashNode;
    procedure Clear;
    function Count: Integer;

    property NodeType: THashNodeType read FNodeType;
    property HashMapValue[const _Key: Variant]: THashNode read GetArrayValue; default;
    property Key: Variant read FKey write SetKey;
    property Value: Variant read FValue write SetValue;
    property NodeArray: TNodeArray read FNodeArray;
    property NodeParent: THashNode read FParent;
  end;

  //Базовый класс
  THashMap = class(THashNode)
  public
    constructor Create;
    destructor Destroy; override;
  end;

  //function V2C(const V: Variant): TClass;
  //function C2V(C: TClass): Variant;


implementation

{function V2C(const V: Variant): TClass;
begin
  Result := TVarData(V).vPointer;
end;

function C2V(C: TClass): Variant;
begin
  TVarData(Result).VType := varHashMap;
  TVarData(Result).vPointer := C;
end;}


{ THashMap }

constructor THashMap.Create;
begin
  inherited Create(nil);
  FNodeType:=hntObject;
  Key:=Null;
end;

destructor THashMap.Destroy;
begin
  inherited;
end;

{ THashNode }

function THashNode.Append: THashNode;
begin
  if not Assigned(FEmptyNode) then
    FEmptyNode:=THashNode.Create(Self);

  FEmptyNode.FNodeType:=hntEmpty;
  FEmptyNode.SetKey(null);
  Result:=FEmptyNode;
end;

procedure THashNode.ArrayToObject;
var
  i: Integer;
  N: THashNode;
begin
  for i:=0 to High(FNodeArray) do begin
    N:=FNodeArray[i];
    if VarIsNull(N.FKey) then N.SetKey(i);
  end;
end;

procedure THashNode.Clear;
begin
  FNodeType:=hntEmpty;
  FEmptyNode:=nil;
  FKey:=Null;
  FValue:=Null;
  DelNodeArray;
end;

class function THashNode.CompareNode(const A, B: THashNode): Integer;
var _A, _B: THashKey;
begin
  _A:=A.FHashKey;
  _B:=B.FHashKey;
  if _A=_B then Result:=0
  else
    if _A<_B then Result:=-1
    else Result:=1;
end;

constructor THashNode.Create(_Parent: THashNode);
begin
  FNodeType:=hntEmpty;
  FParent:=_Parent;
  FKey:=Null;
  FValue:=Null;
  SetLength(FNodeArray, 0);
  FEmptyNode:=nil;
end;

procedure THashNode.CreateEmptyNode;
begin
  if not Assigned(FEmptyNode) then
    FEmptyNode:=THashNode.Create(Self);
end;

procedure THashNode.DelNodeArray;
var i: Integer;
begin
  for i:=0 to High(FNodeArray) do begin
    FreeAndNil(FNodeArray[i]);
  end;

  SetLength(FNodeArray, 0);
end;

destructor THashNode.Destroy;
begin
  DelNodeArray;
  FreeAndNil(FEmptyNode);

  inherited;
end;

function THashNode.FindArrayNode(const _Key: Variant): THashNode;
var
  SHash: THashKey;
  i: Integer;
  R: THashNode;
begin
  Result:=nil;

  case FNodeType of
    hntObject: Begin
      SHash:=Hash(_Key);

      //Пока ищем линейно
      i:=0;
      while i<=High(FNodeArray) do begin
        R:=FNodeArray[i];
        if (R.FHashKey=SHash) and (R.Key=_Key) then begin
          Result:=R;
          Break;
        end;
        inc(i);
      end;
    end;
    hntArray: Begin
      if VarType(_Key) in [varSmallint, varInteger, varShortInt..varInt64] then
        if (_Key>=0) and (_Key<=High(FNodeArray)) then
          Result:=FNodeArray[Cardinal(_Key)];
    end;
  end;

end;

function THashNode.GetArrayValue(const _Key: Variant): THashNode;
begin
  //Ищем существующую ноду
  Result:=FindArrayNode(_Key);

  if not Assigned(Result) then begin
    //Создание пустой ноды
    CreateEmptyNode;

    FEmptyNode.FNodeType:=hntEmpty;

    FEmptyNode.SetKey(_Key);

    Result:=FEmptyNode;
  end;
end;

function THashNode.Hash(const _Key: Variant): THashKey;
var
  SKey: String;
  //SHA1: TSHA1Digest;
  i: Integer;
begin
  Result:=0;

  SKey:=VarToStr(_Key);

  {$R-}
  for i:=1 to Length(SKey) do
    Result:=Result*37+Ord(SKey[i]);
  {$R+}
  
  {Q_SHA1(SKey, SHA1);

  for i:=0 to 4 do
    Result:=Result+Q_UIntToHex(SHA1[i], 8);}
end;

function THashNode.Count: Integer;
begin
  Result:=High(FNodeArray)+1;
end;

procedure THashNode.Parse(const NodeString: String);
begin

end;

function THashNode.RPrint(const Level: Integer = 0): String;
var
  i: Integer;
  _pref: String;
  VarLen: Integer;

  function _ToStr(const _Value: Variant): String;
  var
    i: Integer;
  begin
    case VarType(_Value) of
      varArray: begin
        Result:='';
        VarLen:=VarArrayHighBound(_Value, 1);
        for i:=0 to VarLen do begin
          if i>0 then Result:=Result+', ';
          Result:=Result+_ToStr(_Value[i]);
        end;
        Result:='['+Result+']';
      end;
      else
        Result:=VarToStr(_Value);
    end;    
  end;
begin
  _pref:=StrUtils.DupeString('  ', Level);
  if not VarIsNull(FKey) then
    Result:=_pref+'['+VarToStr(FKey)+'] -> '
  else
    Result:=_pref;

  case FNodeType of
    hntEmpty:
      Result:=Result+'[EMPTY];'#13#10;
    hntElement:
      Result:=Result+_ToStr(FValue)+';'#13#10;
    hntArray: begin
      Result:=Result+'('#13#10;
      for i:=0 to High(FNodeArray) do
        Result:=Result+FNodeArray[i].RPrint(Level+1);
      Result:=Result+_pref+')'#13#10;  
    end;
  end;
end;

procedure THashNode.SetArrayFromElement(NodeParent: THashNode);
begin
  if Assigned(NodeParent) then begin
    if not(VarIsNull(FKey)) and (NodeParent.FNodeType=hntArray) then begin
      NodeParent.FNodeType:=hntObject;
      NodeParent.ArrayToObject;
    end
    else begin
      if NodeParent.FNodeType=hntElement then
        NodeParent.FNodeType:=hntArray;
    end;    
  end;
end;

procedure THashNode.SetElementFromEmpty(NodeParent: THashNode);
var EN: THashNode;
begin
  if Assigned(NodeParent) and Assigned(NodeParent.FEmptyNode) then begin
    //Добавление нового элемента
    SetLength(NodeParent.FNodeArray, High(NodeParent.FNodeArray)+2);
    EN:=NodeParent.FEmptyNode;
    NodeParent.FNodeArray[High(NodeParent.FNodeArray)]:=EN;

    NodeParent.FEmptyNode:=nil;

    if NodeParent.FNodeType=hntElement then
      SetArrayFromElement(NodeParent);

    case NodeParent.FNodeType of
      hntEmpty: begin
        if (VarType(EN.FKey) in [varSmallint, varInteger, varShortInt..varInt64]) and
          (EN.FKey=High(NodeParent.FNodeArray))
        then begin
          NodeParent.FNodeType:=hntArray;
          EN.SetKey(Null);
        end
        else
          NodeParent.FNodeType:=hntObject;

        SetElementFromEmpty(NodeParent.FParent);
      end;
      hntArray: begin
        if (VarType(EN.FKey) in [varSmallint, varInteger, varShortInt..varInt64]) and
          (EN.FKey=High(NodeParent.FNodeArray))
        then begin
          EN.SetKey(Null);
        end
        else begin
          NodeParent.FNodeType:=hntObject;
          NodeParent.ArrayToObject;
        end;
      end;
    end;
  end;
end;

procedure THashNode.SetKey(const _Value: Variant);
var HK: THashKey;
begin
  HK:=Hash(_Value);
  if HK<>FHashKey then begin
    FKey:=_Value;
    FHashKey:=HK;
  end;
end;

procedure THashNode.SetNodeType(_NodeType: THashNodeType);
begin
  FNodeType:=_NodeType;
end;

procedure THashNode.SetValue(const _Value: Variant);

  procedure SetArrayValue;
  var i: Integer;
  begin
    if VarIsArray(_Value) then begin
      for i:=0 to VarArrayHighBound(_Value, 1) do
         GetArrayValue(i).Value:=_Value[i];
      //SetArrayFromElement(FParent);   
    end
    else begin
      FNodeType:=hntElement;
      FValue:=_Value;
    end;
  end;

begin
  case FNodeType of
    hntEmpty: begin
      SetElementFromEmpty(FParent);
    end;
    hntElement: begin
      SetArrayFromElement(FParent);
    end;
    hntArray, hntObject: begin
      DelNodeArray;
    end;
  end;

  SetArrayValue;
end;

end.
