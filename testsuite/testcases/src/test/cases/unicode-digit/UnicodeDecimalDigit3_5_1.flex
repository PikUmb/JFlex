%%

%unicode 5.1
%public
%class UnicodeDecimalDigit3_5_1

%type int
%standalone

%include src/test/resources/common-unicode-enumerated-property-java

%%

<<EOF>> { printOutput(); return 1; }
\d { setCurCharPropertyValue("Nd"); }
[^\d] { setCurCharPropertyValue("Not-Nd"); }
