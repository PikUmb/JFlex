%%

%unicode 6.0
%public
%class UnicodeDerivedCoreProperties_ID_Start_6_0

%type int
%standalone

%include src/test/resources/common-unicode-binary-property-java

%%

\p{ID_Start} { setCurCharPropertyValue(); }
[^] { }

<<EOF>> { printOutput(); return 1; }
