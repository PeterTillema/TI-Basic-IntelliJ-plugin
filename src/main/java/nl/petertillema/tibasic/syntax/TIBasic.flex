package nl.petertillema.tibasic.syntax;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.TokenType;
import nl.petertillema.tibasic.psi.TIBasicTypes;

%%

%class TIBasicLexer
%implements FlexLexer
%unicode
%line
%column
%function advance
%type IElementType

// Special sequel of tokens
EOL = "\r"|"\n"|"\r\n"
WHITE_SPACE = [\ \t\f]
COMMENT = \/\/[^\r\n]*
EXPONENT = \|E \~?[0-9]+
NUMBER = ((\~?([0-9]+(\.[0-9]*)?|[0-9]*\.[0-9]+)){EXPONENT}?) | "[i]" | {EXPONENT}

// All kind of variables
ANS_VARIABLE = "Ans"
LIST_VARIABLE_NAME = ([A-Zθ]|theta)([0-9A-Zθ]|theta){0,4}
LIST_VARIABLE_NAME2 = ([A-Zθ]|theta)([0-9A-Zθ]|theta){1,4}
LIST_VARIABLE = ("L" [1-6]) | ("|L" {LIST_VARIABLE_NAME})
EQUATION_VARIABLE_1 = "{Y" \d "}"
EQUATION_VARIABLE_2 = "{" [XY] [1-6] "T}"
EQUATION_VARIABLE_3 = "{r" [1-6] "}"
EQUATION_VARIABLE_4 = "|u" | "|v" | "|w"
PICTURE_VARIABLE = "Pic" \d
GDB_VARIABLE = "GDB" \d
STRING_VARIABLE = "Str" \d
SIMPLE_VARIABLE = [A-Z] | "theta" | "θ"
MATH_VARIABLE = "pi" | "[e]"
MATRIX_VARIABLE = "[" [A-J] "]"
STATISTIC_VARIABLE = "[RegEQ]" | "RegEQ" | "[n]" | "[xhat]" | "[Sigmax]" | "[Sigmax^2]" | "[Sx]" | "Sx" | "[sigmax]" | "[minX]" | "minX" | "[maxX]" | "maxX" | "[minY]" | "minY" | "[maxY]" | "maxY" |
    "[yhat]" | "[Sigmay]" | "[Sigmay^2]" | "[Sy]" | "Sy" | "[sigmay]" | "[Sigmaxy]" | "[r]" | "[Med]" | "Med" | "[Q1]" | "[Q3]" | "[|a]" | "[|b]" | "[|c]" | "[|d]" |
    "[|e]" | "[x1]" | "[x2]" | "[x3]" | "[y1]" | "[y2]" | "[y3]" | "[recursiven]" | "[p]" | "[z]" | "[t]" | "[chi^2]" | "χ^2" | "[|F]" | "[df]" | "[phat]" | "[phat1]" |
    "[phat2]" | "[xhat1]" | "[Sx1]" | "[n1]" | "[xhat2]" | "[Sx2]" |
    "[Sxp]" | "Sxp" | "[lower]" | "lower" | "[upper]" | "upper" | "[s]" | "[r^2]" | "[R^2]" | "[factordf]" | "[factorSS]" | "[factorMS]" | "[errordf]" | "[errorSS]" | "[errorMS]"
COLOR_VARIABLE = "BLUE" | "Blue" | "RED" | "Red" | "BLACK" | "Black" | "MAGENTA" | "Magenta" | "GREEN" | "Green" | "ORANGE" | "Orange" | "BROWN" | "Brown" | "NAVY" | "Navy" | "LTBLUE" | "LtBlue" | "YELLOW" | "Yellow" | "WHITE" | "White" | "LTGRAY" |
    "LtGray" | "LTGREY" | "LtGrey" | "MEDGRAY" | "MedGray" | "MEDGREY" | "MedGrey" | "GRAY" | "Gray" | "GREY" | "Grey" | "DARKGRAY" | "DarkGray" | "DARKGREY" | "DarkGrey"
IMAGE_VARIABLE = "Image1" | "Image2" | "Image3" | "Image4" | "Image5" | "Image6" | "Image7" | "Image8" | "Image9" | "Image0"

// Other tokens
WINDOW_TOKENS = "ZXscl" | "ZYscl" | "Xscl" | "Yscl" | "UnStart" | "ZUnStart" | "ZVnStart" | "Xmin" | "Xmax" | "Ymin" | "Ymax" | "Tmin" | "Tmax" | "thetaMin" |
    "θmin" | "θMin" | "thetaMax" | "θmax" | "θMax" | "ZXmin" | "ZXmax" | "ZYmin" | "ZYmax" | "Zthetamin" | "Zθmin" | "Zthetamax" | "Zθmax" | "ZTmin" | "ZTmax" | "TblStart" | "nMin" | "PlotStart" | "ZPlotStart" | "nMax" |
    "ZnMax" | "nStart" | "nMin" | "ZnMin" | "Z𝑛Min" | "Z𝒏Min" | "DeltaTbl" | "Tstep" | "thetastep" | "θstep" | "ZTstep" | "Zthetastep" | "Zθstep" | "DeltaX" | "DeltaY" |
    "XFact" | "YFact" | "TblInput" | "|N" | "I%" | "PV" | "PMT" | "FV" | "|P/Y" | "P/Y" | "|C/Y" | "C/Y" | "PlotStep" | "ZPlotStep" | "Xres" | "ZXres" | "TraceStep"

// prgm call
PRGM_CALL = "prgm" ([A-Zθ]|theta)([0-9A-Zθ]|theta){0,7}

// Functions to be used in expressions
EXPR_FUNCTIONS_WITH_ARGS = "round" | "pxl-Test" | "augment" | "rowSwap" | "row+" | "*row" | "*row+" | "max" | "min" | "R>Pr" | "R>Ptheta" | "R>Pθ" | "P>Rx" | "P>Ry" | "median" | "randM" | "mean" | "solve" |
    "seq" | "fnInt" | "nDeriv" | "fMin" | "fMax" | "sqrt" | "cuberoot" | "ln" | "e^^" | "log" | "10^^" | "sin" | "sin^-1" | "arcsin" | "asin" | "cos" | "cos^-1" | "arccos" | "acos" | "tan" | "tan^-1" |
    "arctan" | "atan" | "sinh" | "sinh^-1" | "arcsinh" | "asinh" | "cosh" | "cosh^-1" | "arccosh" | "acosh" | "tanh" | "tanh^-1" |  "arctanh" | "atanh" | "int" | "abs" | "det" | "identity" | "dim" | "sum" | "prod" | "not" |
    "iPart" | "fPart" | "npv" | "irr" | "bal" | "SigmaPrn" | "SigmaInt" | ">Nom" | ">Eff" | "dbd" | "lcm" | "gcd" | "randInt" | "randBin" | "sub" | "stdDev" | "variance" | "inString" | "normalcdf" | "invNorm" | "tcdf" |
    "chi^2cdf" | "χ^2cdf" | "Fcdf" | "binompdf" | "binomcdf" | "poissonpdf" | "poissoncdf" | "geometpdf" | "geometcdf" | "normalpdf" | "tpdf" | "chi^2pdf" | "χ^2pdf" | "Fpdf" |
    "randNorm" | "conj" | "real" | "imag" | "angle" | "cumSum" | "expr" | "length" | "DeltaList" | "ΔList" | "ref" | "rref" | "remainder" | "checkTmr" | "timeCnv" | "dayOfWk" | "getDtStr" | "getTmStr" | "invT" | "eval" | "randIntNoRep" | "logBASE" | piecewise |
    "toString" | "invBinom"
EXPR_FUNCTIONS_NO_ARGS = "rand" | "getKey" | "getDate" | "getTime" | "startTmr" | "getDtFmt" | "getTmFmt" | "isClockOn" | "LEFT" | "CENTER" | "RIGHT"

// Commands, which should be present at the start of the line
COMMAND_WITH_PARENS = "Text" | "Line" | "Pt-On" | "Pt-Off" | "Pt-Change" | "Pxl-On" | "Pxl-Off" | "Pxl-Change" | "Shade" | "Circle" | "Tangent" | "IS>" | "DS<" | "Output" | "Fill" | "SortA" | "SortD" | "Menu" | "Send" | "Get" | "Plot1" | "Plot2" | "Plot3" |
    "GraphColor" | "TextColor" | "Matr>list" | "List>matr" | "ShadeNorm" | "Shade_t" | "Shadechi^2" | "ShadeF" | "Z-Test" | "2-SampZTest" | "1-PropZTest" | "2-PropZTest" |
    "chi^2-Test" | "χ^2-Test" | "2-SampZInt" | "1-PropZInt" | "2-PropZInt" | "GraphStyle" | "GetCalc" | "Equ>String" | "String>Equ" | "Select" | "ANOVA" | "setDate" | "setTime" | "setDtFmt" | "setTmFmt" |
    "OpenLib" | "chi^2GOF-Test" | "χ^2GOF-Test" | "Asm" | "AsmComp"
COMMAND_NO_PARENS = "CubicReg" | "QuartReg" | "Radian" | "Degree" | "Normal" | "Sci" | "Eng" | "Float" | "Fix" | "Horiz" | "FullScreen" | "Full" | "Func" | "Param" | "Polar" | "Seq" | "IndpntAuto" | "IndpntAsk" | "DependAuto" | "DependAsk" | "Trace" |
    "ClrDraw" | "ZStandard" | "ZTrig" | "ZBox" | "ZoomIn" | "ZoomOut" | "ZSquare" | "ZInteger" | "ZPrevious" | "ZDecimal" | "ZoomStat" | "ZoomRcl" | "PrintScreen" | "ZoomSto" | "FnOn" | "FnOff" | "StorePic" | "RecallPic" | "StoreGDB" | "RecallGDB" |
    "Vertical" | "Horizontal" | "DrawInv" | "DrawF" | "Return" | "Pause" | "Stop" | "Input" | "Prompt" | "Disp" | "DispGraph" | "ClrHome" | "DispTable" | "PlotsOn" | "PlotsOff" | "DelVar" | "Sequential" | "Simul" | "PolarGC" | "RectGC" | "CoordOn" |
    "CoordOff" | "Connected" | "Thick" | "Dot" | "Dot-Thick" | "AxesOn" | "AxesOff" | "GridOn" | "GridDot" | "GridOff" | "LabelOn" | "LabelOff" | "Web" | "Time" | "uvAxes" | "vwAxes" | "uwAxes" | "ClockOff" | "ClockOn" | "ExecLib" | "ExprOn" | "ExprOff" |
    "BackgroundOn" | "BackgroundOff" | "Wait" | "Archive" | "UnArchive" | "SetUpEditor"

// Other tokens with higher priority (must be matched before COMMAND_NO_PARENS to avoid conflicts)
// These tokens have prefixes that overlap with COMMAND_NO_PARENS tokens
OTHER_TOKEN_PRIORITY = "Dot-Thin"

OTHER_TOKEN = ">DMS" | ">Dec" | ">Frac" | "Boxplot" | "!" | "tvm_Pmt" | "tvm_I%" | "tvm_PV" | "tvm_N" | "tvm_FV" | ">Rect" | ">Polar" | "SinReg" | "Logistic" | "LinRegTTest" | "T-Test" |
    "ZInterval" | "2-SampTTest" | "2-SampFTest" | "TInterval" | "2-SampTInt" | "Pmt_End" | "Pmt_Bgn" | "Real" | "re^thetai" | "re^θi" | "a+bi" | "ClrAllLists" |
    "ModBoxplot" | "NormProbPlot" | "G-T" | "ZoomFit" | "DiagnosticOn" | "DiagnosticOff" | "AsmPrgm" | "LinRegTInt" | "Manual-Fit" | "ZQuadrant1" | "ZFrac1/2" | "ZFrac1/3" |
    "ZFrac1/4" | "ZFrac1/5" | "ZFrac1/8" | "ZFrac1/10" | "n/d" | "Un/d" | ">n/d<>Un/d" | ">F<>D" | "Sigma(" |
    "[MATHPRINT]" | "MATHPRINT" | "[CLASSIC]" | "CLASSIC" | "[n/d]" | "[Un/d]" | "[AUTO]" | "AUTO" | "[DEC]" | "DEC" | "[FRAC]" | "FRAC" | "[FRAC-APPROX]" | "FRAC-APPROX" | "[STATWIZARD ON]" | "STATWIZARD ON" | "[STATWIZARD OFF]" |
    "STATWIZARD OFF" | "GridLine" | "QuickPlot&Fit-EQ" | "Asm84CPrgm" | "DetectAsymOn" | "DetectAsymOff" | "BorderColor" | "Thin" | "PlySmlt2" | "Asm84CEPrgm" | "pieceWise(" | "xroot" |
    "ˣ√" | "1-VarStats" | "2-VarStats" | "LinReg(a+bx)" | "ExpReg" | "LnReg" | "PwrReg" | "Med-Med" | "QuadReg" | "ClrList" | "ClrTable" | "Histogram" | "xyLine" | "Scatter" | "LinReg(ax+b)"

%state STRING, CUSTOM_LIST_WITHOUT_L

%%

<YYINITIAL> {
    // Structural tokens (highest priority)
    {EOL}                                                     { return TIBasicTypes.CRLF; }
    ":"                                                       { return TIBasicTypes.COLON; }
    {WHITE_SPACE}+                                            { return TokenType.WHITE_SPACE; }
    {COMMENT}                                                 { return TIBasicTypes.COMMENT; }
    {NUMBER}                                                  { return TIBasicTypes.NUMBER; }
    "\""                                                      { yybegin(STRING); }

    // Control flow keywords (before variables that might conflict)
    // Note: Match without trailing space to prevent "Lbl" from matching as "L"+"b"+"l"
    "If"                                                      { return TIBasicTypes.IF; }
    "Then"                                                    { return TIBasicTypes.THEN; }
    "Else"                                                    { return TIBasicTypes.ELSE; }
    "End"                                                     { return TIBasicTypes.END; }
    "While"                                                   { return TIBasicTypes.WHILE; }
    "Repeat"                                                  { return TIBasicTypes.REPEAT; }
    "For"                                                     { return TIBasicTypes.FOR; }
    "Goto"                                                    { return TIBasicTypes.GOTO; }
    "Lbl"                                                     { return TIBasicTypes.LBL; }

    // Operators and punctuation
    "->"                                                      { yybegin(CUSTOM_LIST_WITHOUT_L); return TIBasicTypes.STO; }
    "^^2"                                                     { return TIBasicTypes.POW2; }
    "^^3"                                                     { return TIBasicTypes.POW3; }
    "^^T"                                                     { return TIBasicTypes.TRANSPOSE; }
    "^^o"                                                     { return TIBasicTypes.TO_DEGREE; }
    "^^r"                                                     { return TIBasicTypes.TO_RADIAN; }
    "^^-1"                                                    { return TIBasicTypes.INVERSE; }
    "+"                                                       { return TIBasicTypes.PLUS; }
    "-"                                                       { return TIBasicTypes.MINUS; }
    "*"                                                       { return TIBasicTypes.TIMES; }
    "/"                                                       { return TIBasicTypes.DIVIDE; }
    "^"                                                       { return TIBasicTypes.POW; }
    "="                                                       { return TIBasicTypes.EQ; }
    "!="                                                      { return TIBasicTypes.NE; }
    ">="                                                      { return TIBasicTypes.GE; }
    "<="                                                      { return TIBasicTypes.LE; }
    ">"                                                       { return TIBasicTypes.GT; }
    "<"                                                       { return TIBasicTypes.LT; }
    "~"                                                       { return TIBasicTypes.NEG; }
    "or"                                                      { return TIBasicTypes.OR; }
    "xor"                                                     { return TIBasicTypes.XOR; }
    "and"                                                     { return TIBasicTypes.AND; }
    "nPr"                                                     { return TIBasicTypes.NPR; }
    "nCr"                                                     { return TIBasicTypes.NCR; }
    ","                                                       { return TIBasicTypes.COMMA; }
    "("                                                       { return TIBasicTypes.LPAREN; }
    ")"                                                       { return TIBasicTypes.RPAREN; }
    "{"                                                       { return TIBasicTypes.LCURLY; }
    "}"                                                       { return TIBasicTypes.RCURLY; }
    "["                                                       { return TIBasicTypes.LBRACKET; }
    "]"                                                       { return TIBasicTypes.RBRACKET; }

    // High-priority other tokens (must be checked before COMMAND_NO_PARENS to avoid "Dot" matching "Dot-Thin")
    {OTHER_TOKEN_PRIORITY}                                    { return TIBasicTypes.TOKEN; }

    // prgm call. Is considered a single token, as there is no space allowed in between
    {PRGM_CALL}                                               { return TIBasicTypes.PRGM_CALL; }

    // Commands and functions (multi-character tokens before single-char variables)
    {COMMAND_WITH_PARENS}                                     { return TIBasicTypes.COMMAND_WITH_PARENS; }
    {COMMAND_NO_PARENS}                                       { return TIBasicTypes.COMMAND_NO_PARENS; }
    {EXPR_FUNCTIONS_WITH_ARGS}                                { return TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS; }
    {EXPR_FUNCTIONS_NO_ARGS}                                  { return TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS; }
    {WINDOW_TOKENS}                                           { return TIBasicTypes.WINDOW_TOKENS; }
    {OTHER_TOKEN}                                             { return TIBasicTypes.TOKEN; }

    // Multi-character variables (before simple variables)
    {MATH_VARIABLE}                                           { return TIBasicTypes.MATH_VARIABLE; }
    {ANS_VARIABLE}                                            { return TIBasicTypes.ANS_VARIABLE; }
    {LIST_VARIABLE}                                           { return TIBasicTypes.LIST_VARIABLE; }
    {EQUATION_VARIABLE_1}                                     { return TIBasicTypes.EQUATION_VARIABLE_1; }
    {EQUATION_VARIABLE_2}                                     { return TIBasicTypes.EQUATION_VARIABLE_2; }
    {EQUATION_VARIABLE_3}                                     { return TIBasicTypes.EQUATION_VARIABLE_3; }
    {EQUATION_VARIABLE_4}                                     { return TIBasicTypes.EQUATION_VARIABLE_4; }
    {PICTURE_VARIABLE}                                        { return TIBasicTypes.PICTURE_VARIABLE; }
    {GDB_VARIABLE}                                            { return TIBasicTypes.TOKEN; }
    {STRING_VARIABLE}                                         { return TIBasicTypes.STRING_VARIABLE; }
    {MATRIX_VARIABLE}                                         { return TIBasicTypes.MATRIX_VARIABLE; }
    {STATISTIC_VARIABLE}                                      { return TIBasicTypes.TOKEN; }
    {COLOR_VARIABLE}                                          { return TIBasicTypes.COLOR_VARIABLE; }
    {IMAGE_VARIABLE}                                          { return TIBasicTypes.TOKEN; }

    // Single-character variables (LOWEST priority - fallback for A-Z, theta)
    {SIMPLE_VARIABLE}                                         { return TIBasicTypes.SIMPLE_VARIABLE; }

    // Invalid identifier-like sequences (catch "abc" as one token instead of "a"+"b"+"c")
    // This must come AFTER all valid tokens to avoid matching valid multi-char tokens
    [a-z][A-Za-z0-9]+                                         { return TokenType.BAD_CHARACTER; }
}

<STRING> {
    "\""                                                      { yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "->"                                                      { yypushback(2); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "//"                                                      { yypushback(2); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "\r\n"                                                    { yypushback(2); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "\r"|"\n"                                                 { yypushback(1); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    <<EOF>>                                                   { yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    [^]                                                       { }
}

<CUSTOM_LIST_WITHOUT_L> {
    {WINDOW_TOKENS}                                           { return TIBasicTypes.WINDOW_TOKENS; }
    {EQUATION_VARIABLE_1}                                     { yybegin(YYINITIAL); return TIBasicTypes.EQUATION_VARIABLE_1; }
    {EQUATION_VARIABLE_2}                                     { yybegin(YYINITIAL); return TIBasicTypes.EQUATION_VARIABLE_2; }
    {EQUATION_VARIABLE_3}                                     { yybegin(YYINITIAL); return TIBasicTypes.EQUATION_VARIABLE_3; }
    {EQUATION_VARIABLE_4}                                     { yybegin(YYINITIAL); return TIBasicTypes.EQUATION_VARIABLE_4; }
    {PICTURE_VARIABLE}                                        { yybegin(YYINITIAL); return TIBasicTypes.PICTURE_VARIABLE; }
    {GDB_VARIABLE}                                            { yybegin(YYINITIAL); return TIBasicTypes.TOKEN; }
    {STRING_VARIABLE}                                         { yybegin(YYINITIAL); return TIBasicTypes.STRING_VARIABLE; }
    {STATISTIC_VARIABLE}                                      { yybegin(YYINITIAL); return TIBasicTypes.TOKEN; }
    {LIST_VARIABLE_NAME2}                                     { yybegin(YYINITIAL); return TIBasicTypes.LIST_VARIABLE; }
    {SIMPLE_VARIABLE}                                         { yybegin(YYINITIAL); return TIBasicTypes.SIMPLE_VARIABLE; }
    [^]                                                       { yypushback(1); yybegin(YYINITIAL); }
}

[^]                                                           { return TokenType.BAD_CHARACTER; }