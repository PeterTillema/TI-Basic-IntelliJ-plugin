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
NUMBER = ((\~?([0-9]+(\.[0-9]*)?|[0-9]*\.[0-9]+))(\|E(\~?[0-9]+))?) | "[i]" | "𝑖"

// All kind of variables
ANS_VARIABLE = "Ans"
LIST_VARIABLE_NAME = ([A-Zθ]|theta){1,5}
LIST_VARIABLE = "L1" | "L₁" | "L2" | "L₂" | "L3" | "L₃" | "L4" | "L₄" | "L5" | "L₅" | "L6" | "L₆" | ("|L" {LIST_VARIABLE_NAME})
EQUATION_VARIABLE_1 = "{Y1}" | "Y₁" | "{Y2}" | "Y₂" | "{Y3}" | "Y₃" | "{Y4}" | "Y₄" | "{Y5}" | "Y₅" | "{Y6}" | "Y₆" | "{Y7}" | "Y₇" | "{Y8}" | "Y₈" | "{Y9}" | "Y₉" | "{Y0}" | "Y₀"
EQUATION_VARIABLE_2 = "{X1T}" | "X₁ᴛ" | "{Y1T}" | "Y₁ᴛ" | "{X2T}" | "X₂ᴛ" | "{Y2T}" | "Y₂ᴛ" | "{X3T}" | "X₃ᴛ" | "{Y3T}" | "Y₃ᴛ" | "{X4T}" | "X₄ᴛ" | "{Y4T}" | "Y₄ᴛ" | "{X5T}" | "X₅ᴛ" | "{Y5T}" | "Y₅ᴛ" | "{X6T}" | "X₆ᴛ" | "{Y6T}" | "Y₆ᴛ"
EQUATION_VARIABLE_3 = "{r1}" | "r₁" | "{r2}" | "r₂" | "{r3}" | "r₃" | "{r4}" | "r₄" | "{r5}" | "r₅" | "{r6}" | "r₆"
EQUATION_VARIABLE_4 = "|u" | "|v" | "|w"
PICTURE_VARIABLE = "Pic1" | "Pic2" | "Pic3" | "Pic4" | "Pic5" | "Pic6" | "Pic7" | "Pic8" | "Pic9" | "Pic0"
GDB_VARIABLE = "GDB1" | "GDB2" | "GDB3" | "GDB4" | "GDB5" | "GDB6" | "GDB7" | "GDB8" | "GDB9" | "GDB0"
STRING_VARIABLE = "Str1" | "Str2" | "Str3" | "Str4" | "Str5" | "Str6" | "Str7" | "Str8" | "Str9" | "Str0"
SIMPLE_VARIABLE = "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z" | "theta" | "θ"
MATRIX_VARIABLE = "[A]" | "[B]" | "[C]" | "[D]" | "[E]" | "[F]" | "[G]" | "[H]" | "[I]" | "[J]"
STATISTIC_VARIABLE = "[RegEQ]" | "RegEQ" | "[n]" | "[xhat]" | "x̄" | "ẋ" | "[Sigmax]" | "Σx" | "[Sigmax^2]" | "Σx²" | "Σx^2" | "sigmax²" | "[Sx]" | "Sx" | "[sigmax]" | "σx" | "[minX]" | "minX" | "[maxX]" | "maxX" | "[minY]" | "minY" | "[maxY]" | "maxY" |
    "[yhat]" | "ȳ" | "[Sigmay]" | "Σy" | "[Sigmay^2]" | "Σy²" | "Σy^2" | "sigmay²" | "[Sy]" | "Sy" | "[sigmay]" | "σy" | "[Sigmaxy]" | "Σxy" | "[r]" | "[Med]" | "Med" | "[Q1]" | "Q₁" | "[Q₁]" | "[Q3]" | "Q₃" | "[Q₃]" | "[|a]" | "[|b]" | "[|c]" | "[|d]" |
    "[|e]" | "[x1]" | "x₁" | "[x2]" | "x₂" | "[x3]" | "x₃" | "[y1]" | "y₁" | "[y2]" | "y₂" | "[y3]" | "y₃" | "[recursiven]" | "𝑛" | "[𝒏]" | "[p]" | "[z]" | "[t]" | "[chi^2]" | "χ²" | "χ^2" | "chi²" | "[|F]" | "[df]" | "[phat]" | "[p̂]" | "[ṗ]" | "[phat1]" |
    "p̂₁" | "p̂1" | "ṗ₁" | "ṗ1" | "phat₁" | "[phat2]" | "p̂₂" | "p̂2" | "ṗ₂" | "ṗ2" | "phat₂" | "[xhat1]" | "x̄₁" | "ẋ₁" | "ẋ1" | "xhat₁" | "[Sx1]" | "Sx₁" | "[n1]" | "n₁" | "[xhat2]" | "x̄₂" | "x̄2" | "ẋ₂" | "ẋ2" | "xhat₂" | "[Sx2]" | "Sx₂" | "[n2]" | "n₂" |
    "[Sxp]" | "Sxp" | "[lower]" | "lower" | "[upper]" | "upper" | "[s]" | "[r^2]" | "r²" | "[R^2]" | "R²" | "[factordf]" | "[factorSS]" | "[factorMS]" | "[errordf]" | "[errorSS]" | "[errorMS]"
COLOR_VARIABLE = "BLUE" | "Blue" | "RED" | "Red" | "BLACK" | "Black" | "MAGENTA" | "Magenta" | "GREEN" | "Green" | "ORANGE" | "Orange" | "BROWN" | "Brown" | "NAVY" | "Navy" | "LTBLUE" | "LtBlue" | "YELLOW" | "Yellow" | "WHITE" | "White" | "LTGRAY" |
    "LtGray" | "LTGREY" | "LtGrey" | "MEDGRAY" | "MedGray" | "MEDGREY" | "MedGrey" | "GRAY" | "Gray" | "GREY" | "Grey" | "DARKGRAY" | "DarkGray" | "DARKGREY" | "DarkGrey"
IMAGE_VARIABLE = "Image1" | "Image2" | "Image3" | "Image4" | "Image5" | "Image6" | "Image7" | "Image8" | "Image9" | "Image0"

// Other tokens
WINDOW_TOKENS = "ZXscl" | "ZYscl" | "Xscl" | "Yscl" | "UnStart" | "u(nMin)" | "u(𝑛Min)" | "u(𝒏Min)" | "VnStart" | "V𝒏Start" | "v(nMin)" | "v(𝑛Min)" | "v(𝒏Min)" | "Un-1" | "U𝑛-₁" | "U𝒏-₁" | "Un-₁" | "Un-1" | "U𝑛-₁" | "U𝒏-₁" | "Un-₁" | "Vn-1" | "V𝑛-₁" |
    "V𝒏-₁" | "Vn-₁" | "Vn-1" | "V𝑛-₁" | "V𝒏-₁" | "Vn-₁" | "ZUnStart" | "Zu(nMin)" | "Zu(𝑛Min)" | "Zu(𝒏Min)" | "Zu(nmin)" | "ZVnStart" | "Zv(nMin)" | "Zv(𝑛Min)" | "Zv(𝒏Min)" | "Zv(nmin)" | "Xmin" | "Xmax" | "Ymin" | "Ymax" | "Tmin" | "Tmax" | "thetaMin" |
    "θmin" | "θMin" | "thetaMax" | "θmax" | "θMax" | "ZXmin" | "ZXmax" | "ZYmin" | "ZYmax" | "Zthetamin" | "Zθmin" | "Zthetamax" | "Zθmax" | "ZTmin" | "ZTmax" | "TblStart" | "nMin" | "𝑛Min" | "𝒏Min" | "PlotStart" | "ZPlotStart" | "nMax" | "𝑛Max" | "𝒏Max" |
    "ZnMax" | "Z𝑛Max" | "Z𝒏Max" | "nStart" | "𝑛Start" | "nMin" | "𝑛Min" | "𝒏Min" | "ZnMin" | "Z𝑛Min" | "Z𝒏Min" | "DeltaTbl" | "ΔTbl" | "∆Tbl" | "Tstep" | "thetastep" | "θstep" | "ZTstep" | "Zthetastep" | "Zθstep" | "DeltaX" | "ΔX" | "∆X" | "DeltaY" | "ΔY" |
    "∆Y" | "XFact" | "YFact" | "TblInput" | "|N" | "𝗡" | "|𝗡" | "I%" | "PV" | "PMT" | "FV" | "|P/Y" | "P/Y" | "|C/Y" | "C/Y" | "w(nMin)" | "w(𝑛Min)" | "w(𝒏Min)" | "Zw(nMin)" | "Zw(𝑛Min)" | "Zw(𝒏Min)" | "PlotStep" | "ZPlotStep" | "Xres" | "ZXres" | "TraceStep"

// Functions to be used in expressions
EXPR_FUNCTIONS_WITH_ARGS = "round(" | "pxl-Test(" | "augment(" | "rowSwap(" | "row+(" | "*row(" | "*row+(" | "max(" | "min(" | "R>Pr(" | "R►Pr(" | "R>Ptheta(" | "R►Pθ(" | "R►Ptheta(" | "R>Pθ(" | "P>Rx(" | "P►Rx(" | "P>Ry(" | "P►Ry(" | "median(" | "randM(" |
    "mean(" | "solve(" | "seq(" | "fnInt(" | "nDeriv(" | "fMin(" | "fMax(" | "sqrt(" | "√(" | "cuberoot(" | "³√(" | "ln(" | "e^^(" | "𝑒^(" | "log(" | "10^^(" | "₁₀^(" | "sin(" | "sin^-1(" | "sin⁻¹(" | "arcsin(" | "asin(" | "cos(" | "cos^-1(" | "cos⁻¹(" |
    "arccos(" | "acos(" | "tan(" | "tan^-1(" | "tan⁻¹(" | "arctan(" | "atan(" | "sinh(" | "sinh^-1(" | "sinh⁻¹(" | "arcsinh(" | "asinh(" | "cosh(" | "cosh^-1(" | "cosh⁻¹(" | "arccosh(" | "acosh(" | "tanh(" | "tanh^-1(" | "tanh⁻¹(" | "arctanh(" | "atanh(" |
    "int(" | "abs(" | "det(" | "identity(" | "dim(" | "sum(" | "prod(" | "not(" | "iPart(" | "fPart(" | "npv(" | "irr(" | "bal(" | "SigmaPrn(" | "ΣPrn(" | "SigmaInt(" | "ΣInt(" | ">Nom(" | "►Nom(" | ">Eff(" | "►Eff(" | "dbd(" | "lcm(" | "gcd(" | "randInt(" |
    "randBin(" | "sub(" | "stdDev(" | "variance(" | "inString(" | "normalcdf(" | "invNorm(" | "tcdf(" | "chi^2cdf(" | "χ²cdf(" | "χ^2cdf(" | "chi²cdf(" | "Fcdf(" | "𝙵cdf(" | "𝐅cdf(" | "binompdf(" | "binomcdf(" | "poissonpdf(" | "poissoncdf(" | "geometpdf(" |
    "geometcdf(" | "normalpdf(" | "tpdf(" | "chi^2pdf(" | "χ²pdf(" | "χ^2pdf(" | "chi²pdf(" | "Fpdf(" | "𝙵pdf(" | "𝐅pdf(" | "randNorm(" | "conj(" | "real(" | "imag(" | "angle(" | "cumSum(" | "expr(" | "length(" | "DeltaList(" | "ΔList(" | "ref(" | "rref(" |
    "remainder("
EXPR_FUNCTIONS_NO_ARGS = "rand" | "getKey"

// Commands, which should be present at the start of the line
COMMAND_WITH_PARENS = "Text(" | "Line(" | "Pt-On(" | "Pt-Off(" | "Pt-Change(" | "Pxl-On(" | "Pxl-Off(" | "Pxl-Change(" | "Shade(" | "Circle(" | "Tangent(" | "IS>(" | "DS<(" | "Output(" | "Fill(" | "SortA(" | "SortD(" | "Menu(" | "Send(" | "Get(" |
    "Plot1(" | "Plot2(" | "Plot3(" | "GraphColor(" | "TextColor(" | "Matr>list(" | "Matr►list(" | "List>matr(" | "List►matr("
COMMAND_NO_PARENS = "CubicReg" | "QuartReg" | "Radian" | "Degree" | "Normal" | "Sci" | "Eng" | "Float" | "Fix" | "Horiz" | "FullScreen" | "Full" | "Func" | "Param" | "Polar" | "Seq" | "IndpntAuto" | "IndpntAsk" | "DependAuto" | "DependAsk" | "Trace" |
    "ClrDraw" | "ZStandard" | "ZTrig" | "ZBox" | "ZoomIn" | "ZoomOut" | "ZSquare" | "ZInteger" | "ZPrevious" | "ZDecimal" | "ZoomStat" | "ZoomRcl" | "PrintScreen" | "ZoomSto" | "FnOn" | "FnOff" | "StorePic" | "RecallPic" | "StoreGDB" | "RecallGDB" |
    "Vertical" | "Horizontal" | "DrawInv" | "DrawF" | "Return" | "Pause" | "Stop" | "Input" | "Prompt" | "Disp" | "DispGraph" | "ClrHome" | "DispTable" | "PlotsOn" | "PlotsOff" |
    "DelVar" | "Sequential" | "Simul" | "PolarGC" | "RectGC" | "CoordOn" | "CoordOff" | "Connected" | "Thick" | "Dot" | "Dot-Thick" | "AxesOn" | "AxesOff" | "GridOn" | "GridDot" | "GridOff" | "LabelOn" | "LabelOff" | "Web" | "Time" |
    "uvAxes" | "vwAxes" | "uwAxes"

// Other tokens with higher priority (must be matched before COMMAND_NO_PARENS to avoid conflicts)
// These tokens have prefixes that overlap with COMMAND_NO_PARENS tokens
// Note: I don't think "Dot-Thin" is actually used in TI-Basic, but it is included here because it was in the original OTHER_TOKEN list
OTHER_TOKEN_PRIORITY = "Dot-Thin"

OTHER_TOKEN = ">DMS" | "►DMS" | ">Dec" | "►Dec" | ">Frac" | "►Frac" | "Boxplot" | "^^r" | "ʳ" | "^^o" | "°" | "^^-1" | "⁻¹" | "ˉ¹" | "^^2" | "²" | "^^T" | "ᵀ" | "^^3" | "³" | "(" | ")" | "\"" | """ | """ | "," |
    "[i]" | "𝑖" | "!" | "prgm" | "squareplot" | "□" | "plotsquare" |
    "crossplot" | "﹢" | "plotcross" | "dotplot" | "plotdot" | "*" | "/" | "nPr" | "nCr" | "pi" | "'" | "\'" | "?" |
    "tvm_Pmt" | "tvm_I%" | "tvm_PV" | "tvm_N" | "tvm_𝗡" | "tvm_FV" |
    ">Rect" | "►Rect" | ">Polar" | "►Polar" | "[e]" | "𝑒" | "SinReg" | "Logistic" | "LinRegTTest" | "ShadeNorm(" | "Shade_t(" | "Shadechi^2(" | "Shadeχ²(" | "Shadeχ^2(" |
    "Shadechi²(" | "ShadeF(" | "Shade𝙵(" | "Shade𝐅(" | "Z-Test(" | "T-Test" | "2-SampZTest(" | "1-PropZTest(" | "2-PropZTest(" | "chi^2-Test(" | "χ²-Test(" | "χ^2-Test(" | "chi²-Test(" | "ZInterval" |
    "2-SampZInt(" | "1-PropZInt(" | "2-PropZInt(" | "GraphStyle(" | "2-SampTTest" | "2-SampFTest" | "2-Samp𝙵Test" | "2-Samp𝐅Test" | "TInterval" | "2-SampTInt" | "SetUpEditor" | "Pmt_End" | "Pmt_Bgn" | "Real" | "re^thetai" | "r𝑒^θ𝑖" | "re^θ𝑖" |
    "re^θi" | "re^theta𝑖" | "a+bi" | "a+b𝑖" | "ExprOn" | "ExprOff" | "ClrAllLists" | "GetCalc(" | "Equ>String(" | "Equ►String(" | "String>Equ(" | "String►Equ(" | "Clear Entries" | "Select(" | "ANOVA(" | "ModBoxplot" | "NormProbPlot" |
    "G-T" | "ZoomFit" | "DiagnosticOn" | "DiagnosticOff" | "Archive" | "UnArchive" | "Asm(" | "AsmComp(" | "AsmPrgm" | "Á" | "À" | "Â" | "Ä" | "á" | "à" | "â" | "ä" | "É" | "È" | "Ê" | "Ë" | "é" | "è" | "ê" | "ë" | "Ì" | "Î" | "Ï" | "í" | "ì" | "î" | "ï" |
    "Ó" | "Ò" | "Ô" | "Ö" | "ó" | "ò" | "ô" | "ö" | "Ú" | "Ù" | "Û" | "Ü" | "ú" | "ù" | "û" | "ü" | "Ç" | "ç" | "Ñ" | "ñ" | "|'" | "´" | "^^'" | "|`" | "^^`" | "|:" | "¨" | "^^:" | "|?" | "¿" | "|!" | "¡" | "alpha" | "α" | "beta" | "β" | "gamma" | "γ" | "Delta" |
    "Δ" | "delta" | "δ" | "epsilon" | "ε" | "lambda" | "λ" | "mu" | "μ" | "greek_pi" | "|π" | "rho" | "ρ" | "Sigma" | "Σ" | "Phi" | "Φ" | "Omega" | "Ω" | "phat" | "ṗ" | "chi" | "χ" | "|F" | "𝐅" | "|𝐅" | "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" |
    "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z" | "sigma" | "σ" | "tau" | "τ" | "Í" | "GarbageCollect" | "|~" | "@" | "#" | "$" | "&" | "`" | ";" | "\"" | "|" | "_" | "%" | "..." | "…" | "|<" | "∠" | "sharps" |
    "ß" | "^^x" | "ˣ" | "smallT" | "ᴛ" | "small0" | "₀" | "small1" | "₁" | "small2" | "₂" | "small3" | "₃" | "small4" | "₄" | "small5" | "₅" | "small6" | "₆" | "small7" | "₇" | "small8" | "₈" | "small9" | "₉" | "small10" | "₁₀" | "<|" | "◄" | "|>" | "►" |
    "uparrow" | "↑" | "downarrow" | "↓" | "xmark" | "×" | "integral" | "∫" | "bolduparrow" | "🡁" | "🡅" | "bolddownarrow" | "🠿" | "🡇" | "squareroot" | "invertedequal" | "⌸" | "|L" | "ʟ" | "⌊" | "smallL" | "setDate(" | "setTime(" | "checkTmr(" | "setDtFmt(" |
    "setTmFmt(" | "timeCnv(" | "dayOfWk(" | "getDtStr(" | "getTmStr(" | "getDate" | "getTime" | "startTmr" | "getDtFmt" | "getTmFmt" | "isClockOn" | "ClockOff" | "ClockOn" | "OpenLib(" | "ExecLib" | "invT(" | "chi^2GOF-Test(" | "χ²GOF-Test(" | "χ^2GOF-Test(" |
    "chi²GOF-Test(" | "LinRegTInt" | "Manual-Fit" | "ZQuadrant1" | "ZFrac1/2" | "ZFrac1⁄2" | "ZFrac1/3" | "ZFrac1⁄3" | "ZFrac1/4" | "ZFrac1⁄4" | "ZFrac1/5" | "ZFrac1⁄5" | "ZFrac1/8" | "ZFrac1⁄8" | "ZFrac1/10" | "ZFrac1⁄10" | "mathprintbox" | "⬚" | "n/d" | "⁄" |
    "Un/d" | "󸏵" | "ᵤ" | ">n/d<>Un/d" | "►n⁄d◄►Un⁄d" | "►n/d◄►Un/d" | ">n⁄d<>Un⁄d" | ">F<>D" | "►F◄►D" | "Sigma(" | "Σ(" | "logBASE(" | "randIntNoRep(" | "[MATHPRINT]" | "MATHPRINT" | "[CLASSIC]" | "CLASSIC" | "[n/d]" | "n⁄d" | "[Un/d]" | "Un⁄d" |
    "[AUTO]" | "AUTO" | "[DEC]" | "DEC" | "[FRAC]" | "FRAC" | "[FRAC-APPROX]" | "FRAC-APPROX" | "[STATWIZARD ON]" | "STATWIZARD ON" | "[STATWIZARD OFF]" | "STATWIZARD OFF" | "GridLine" | "BackgroundOn" | "BackgroundOff" | "QuickPlot&Fit-EQ" |
    "Asm84CPrgm" | "DetectAsymOn" | "DetectAsymOff" | "BorderColor" | "plottinydot" | "·" | "Thin" | "PlySmlt2" | "Asm84CEPrgm" | "Quartiles Setting..." | "Quartiles Setting…" | "u(n-2)" | "u(𝑛-2)" | "u(𝒏-2)" | "v(n-2)" | "v(𝑛-2)" |
    "v(𝒏-2)" | "w(n-2)" | "w(𝑛-2)" | "w(𝒏-2)" | "u(n-1)" | "u(𝑛-1)" | "u(𝒏-1)" | "v(n-1)" | "v(𝑛-1)" | "v(𝒏-1)" | "w(n-1)" | "w(𝑛-1)" | "w(𝒏-1)" | "u(n)" | "u(𝑛)" | "u(𝒏)" | "v(n)" | "v(𝑛)" | "v(𝒏)" | "w(n)" | "w(𝑛)" | "w(𝒏)" | "u(n+1)" | "u(𝑛+1)" | "u(𝒏+1)" |
    "v(n+1)" | "v(𝑛+1)" | "v(𝒏+1)" | "w(n+1)" | "w(𝑛+1)" | "w(𝒏+1)" | "pieceWise(" | "SEQ(n)" | "SEQ(𝑛)" | "SEQ(𝒏)" | "SEQ(n+1)" | "SEQ(𝑛+1)" | "SEQ(𝒏+1)" | "SEQ(n+2)" | "SEQ(𝑛+2)" | "SEQ(𝒏+2)" | "LEFT" | "CENTER" | "RIGHT" | "invBinom(" | "Wait" | "toString(" |
    "eval(" | "ExecuteProgram" | "UndoClear" | "InsertLineAbove" | "CutLine" | "CopyLine" | "PasteLineBelow" | "InsertCommentAbove" | "QuitEditor" | "piecewise(" | "^" | "xroot" | "ˣ√" | "1-VarStats" | "2-VarStats" | "LinReg(a+bx)" | "ExpReg" |
    "LnReg" | "PwrReg" | "Med-Med" | "QuadReg" | "ClrList" | "ClrTable" | "Histogram" | "xyLine" | "Scatter" | "LinReg(ax+b)"

%state STRING

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
    "For("                                                    { return TIBasicTypes.FOR; }
    "Goto"                                                    { return TIBasicTypes.GOTO; }
    "Lbl"                                                     { return TIBasicTypes.LBL; }

    // Operators and punctuation
    "->"                                                      { return TIBasicTypes.STO; }
    "+"                                                       { return TIBasicTypes.PLUS; }
    "-"                                                       { return TIBasicTypes.MINUS; }
    "*"                                                       { return TIBasicTypes.TIMES; }
    "/"                                                       { return TIBasicTypes.DIVIDE; }
    "="                                                       { return TIBasicTypes.EQ; }
    "!="                                                      { return TIBasicTypes.NE; }
    ">="                                                      { return TIBasicTypes.GE; }
    "<="                                                      { return TIBasicTypes.LE; }
    ">"                                                       { return TIBasicTypes.GT; }
    "<"                                                       { return TIBasicTypes.LT; }
    "~"                                                       { return TIBasicTypes.NEG; }
    " or "                                                    { return TIBasicTypes.OR; }
    " xor "                                                   { return TIBasicTypes.XOR; }
    " and "                                                   { return TIBasicTypes.AND; }
    ","                                                       { return TIBasicTypes.COMMA; }
    "("                                                       { return TIBasicTypes.LPAREN; }
    ")"                                                       { return TIBasicTypes.RPAREN; }
    "{"                                                       { return TIBasicTypes.LCURLY; }
    "}"                                                       { return TIBasicTypes.RCURLY; }
    "["                                                       { return TIBasicTypes.LBRACKET; }
    "]"                                                       { return TIBasicTypes.RBRACKET; }

    // High-priority other tokens (must be checked before COMMAND_NO_PARENS to avoid "Dot" matching "Dot-Thin")
    {OTHER_TOKEN_PRIORITY}                                    { return TIBasicTypes.TOKEN; }

    // Commands and functions (multi-character tokens before single-char variables)
    {COMMAND_WITH_PARENS}                                     { return TIBasicTypes.COMMAND_WITH_PARENS; }
    {COMMAND_NO_PARENS}                                       { return TIBasicTypes.COMMAND_NO_PARENS; }
    {EXPR_FUNCTIONS_WITH_ARGS}                                { return TIBasicTypes.EXPR_FUNCTIONS_WITH_ARGS; }
    {EXPR_FUNCTIONS_NO_ARGS}                                  { return TIBasicTypes.EXPR_FUNCTIONS_NO_ARGS; }
    {WINDOW_TOKENS}                                           { return TIBasicTypes.WINDOW_TOKENS; }
    {OTHER_TOKEN}                                             { return TIBasicTypes.TOKEN; }

    // Multi-character variables (before simple variables)
    {ANS_VARIABLE}                                            { return TIBasicTypes.ANS_VARIABLE; }
    {LIST_VARIABLE}                                           { return TIBasicTypes.LIST_VARIABLE; }
    {EQUATION_VARIABLE_1}                                     { return TIBasicTypes.EQUATION_VARIABLE_1; }
    {EQUATION_VARIABLE_2}                                     { return TIBasicTypes.EQUATION_VARIABLE_2; }
    {EQUATION_VARIABLE_3}                                     { return TIBasicTypes.EQUATION_VARIABLE_3; }
    {EQUATION_VARIABLE_4}                                     { return TIBasicTypes.EQUATION_VARIABLE_4; }
    {PICTURE_VARIABLE}                                        { return TIBasicTypes.TOKEN; }
    {GDB_VARIABLE}                                            { return TIBasicTypes.TOKEN; }
    {STRING_VARIABLE}                                         { return TIBasicTypes.STRING_VARIABLE; }
    {MATRIX_VARIABLE}                                         { return TIBasicTypes.MATRIX_VARIABLE; }
    {STATISTIC_VARIABLE}                                      { return TIBasicTypes.TOKEN; }
    {COLOR_VARIABLE}                                          { return TIBasicTypes.COLOR_VARIABLE; }
    {IMAGE_VARIABLE}                                          { return TIBasicTypes.TOKEN; }

    // Single-character variables (LOWEST priority - fallback for A-Z, theta)
    {SIMPLE_VARIABLE}                                         { return TIBasicTypes.SIMPLE_VARIABLE; }

    // Invalid identifier-like sequences (catch "Abc" as one token instead of "A"+"b"+"c")
    // This must come AFTER all valid tokens to avoid matching valid multi-char tokens
    [A-Za-z][A-Za-z0-9]+                                      { return TokenType.BAD_CHARACTER; }
}

<STRING> {
    "\""                                                      { yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "->"                                                      { yypushback(2); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "\r\n"                                                    { yypushback(2); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    "\r"|"\n"                                                 { yypushback(1); yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    <<EOF>>                                                   { yybegin(YYINITIAL); return TIBasicTypes.STRING; }
    [^]                                                       { }
}

[^]                                                           { return TokenType.BAD_CHARACTER; }