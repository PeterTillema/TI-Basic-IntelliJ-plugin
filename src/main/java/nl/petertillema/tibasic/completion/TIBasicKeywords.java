package nl.petertillema.tibasic.completion;

import java.util.List;

public class TIBasicKeywords {

    public static final List<String> SIMPLE_VARIABLES = List.of(
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z", "theta", "θ"
    );

    public static final List<String> LIST_MATRIX_VARIABLES = List.of(
            "L1", "L2", "L3", "L4", "L5", "L6", "L₁", "L₂", "L₃", "L₄", "L₅", "L₆", "[A]", "[B]", "[C]", "[D]",
            "[E]", "[F]", "[G]", "[H]", "[I]", "[J]"
    );

    public static final List<String> STORE_VARIABLES = List.of(
            "{Y1}", "{Y2}", "{Y3}", "{Y4}", "{Y5}", "{Y6}", "{Y7}", "{Y8}", "{Y9}", "{Y0}", "Y₁", "Y₂",
            "Y₃", "Y₄", "Y₅", "Y₆", "Y₇", "Y₈", "Y₉", "Y₀", "{X1}", "{X2}", "{X3}", "{X4}", "{X5}", "{X6}", "{Y1}",
            "{Y2}", "{Y3}", "{Y4}", "{Y5}", "{Y6}", "X₁", "X₂", "X₃", "X₄", "X₅", "X₆", "Y₁", "Y₂", "Y₃", "Y₄", "Y₅", "Y₆",
            "{r1}", "{r2}", "{r3}", "{r4}", "{r5}", "{r6}", "r₁", "r₂", "r₃", "r₄", "r₅", "r₆", "|u", "|v", "|w", "Str0",
            "Str1", "Str2", "Str3", "Str4", "Str5", "Str6", "Str7", "Str8", "Str9", "Str0"
    );

    public static final List<String> OTHER_VARIABLES = List.of(
            "Pic1", "Pic2", "Pic3", "Pic4", "Pic5", "Pic6", "Pic7", "Pic8", "Pic9", "Pic0", "GDB1",
            "GDB2", "GDB3", "GDB4", "GDB5", "GDB6", "GDB7", "GDB8", "GDB9", "GDB0"
    );

    public static final List<String> CONTROL_FLOW_KEYWORDS = List.of(
            "If", "While", "Repeat", "For", "Goto", "Lbl", "DelVar", "Disp", "IS>", "DS<", "Asm"
    );

    public static final List<String> COMMANDS_WITH_PARENS = List.of(
            "Text", "Line", "Pt-On", "Pt-Off", "Pt-Change", "Pxl-On", "Pxl-Off", "Pxl-Change",
            "Shade", "Circle", "Tangent", "Output", "Fill", "SortA", "SortD", "Menu", "Send", "Get",
            "Plot1", "Plot2", "Plot3", "GraphColor", "TextColor", "Matr>list", "Matr►list", "List>matr", "List►matr",
            "ShadeNorm", "Shade_t", "Shadechi^2", "Shadeχ²", "Shadeχ^2", "Shadechi²", "ShadeF", "Shade𝙵", "Shade𝐅",
            "Z-Test", "2-SampZTest", "1-PropZTest", "2-PropZTest", "chi^2-Test", "χ²-Test", "χ^2-Test", "chi²-Test",
            "2-SampZInt", "1-PropZInt", "2-PropZInt", "GraphStyle", "GetCalc", "Equ>String", "Equ►String", "String>Equ",
            "String►Equ", "Select", "ANOVA", "setDate", "setTime", "setDtFmt", "setTmFmt", "OpenLib", "chi^2GOF-Test",
            "χ²GOF-Test", "χ^2GOF-Test", "chi²GOF-Test", "AsmComp"
    );

    public static final List<String> COMMANDS_NO_PARENS = List.of(
            "CubicReg", "QuartReg", "Radian", "Degree", "Normal", "Sci", "Eng", "Float", "Fix",
            "Horiz", "FullScreen", "Full", "Func", "Param", "Polar", "Seq", "IndpntAuto", "IndpntAsk", "DependAuto",
            "DependAsk", "Trace", "ClrDraw", "ZStandard", "ZTrig", "ZBox", "ZoomIn", "ZoomOut", "ZSquare", "ZInteger",
            "ZPrevious", "ZDecimal", "ZoomStat", "ZoomRcl", "PrintScreen", "ZoomSto", "FnOn", "FnOff", "StorePic",
            "RecallPic", "StoreGDB", "RecallGDB", "Vertical", "Horizontal", "DrawInv", "DrawF", "Return", "Pause",
            "Stop", "Input", "Prompt", "DispGraph", "ClrHome", "DispTable", "PlotsOn", "PlotsOff",
            "Sequential", "Simul", "PolarGC", "RectGC", "CoordOn", "CoordOff", "Connected", "Thick", "Dot", "AxesOn",
            "AxesOff", "GridOn", "GridDot", "GridOff", "LabelOn", "LabelOff", "Web", "Time", "uvAxes", "vwAxes",
            "uwAxes", "ClockOff", "ClockOn", "ExecLib", "ExprOn", "ExprOff", "BackgroundOn", "BackgroundOff", "Wait",
            "Archive", "UnArchive", "SetUpEditor", "DetectAsymOn", "DetectAsymOff", "Real", "BorderColor", "ClrList",
            "ClearEntries", "re^thetai", "r𝑒^θ𝑖", "re^θ𝑖", "re^θi", "re^theta𝑖", "a+bi", "a+b𝑖", "Pmt_End", "Pmt_Bgn",
            "ClrAllLists", "Thin", "GridLine", "DiagnosticOn", "DiagnosticOff", "TInterval", "SinReg", "Logistic",
            "LinRegTTest", "T-Test", "ZInterval", "2-SampTTest", "2-SampFTest", "2-Samp𝙵Test", "2-Samp𝐅Test",
            "2-SampTInt", "G-T", "ZoomFit", "LinRegTInt", "Manual-Fit", "ZQuadrant1", "LinReg(a+bx)", "LinReg(ax+b)",
            "ClrTable", "ExpReg", "LnReg", "PwrReg", "Med-Med", "QuadReg", "1-VarStats", "2-VarStats", "ZFrac1/2",
            "ZFrac1⁄2", "ZFrac1/3", "ZFrac1⁄3", "ZFrac1/4", "ZFrac1⁄4", "ZFrac1/5", "ZFrac1⁄5", "ZFrac1/8", "ZFrac1⁄8",
            "ZFrac1/10", "ZFrac1⁄10"
    );

    public static final List<String> PLOT_COMMANDS = List.of("Plot1", "Plot2", "Plot3");

    public static final List<String> EXPR_FUNCTIONS_WITH_ARGS = List.of(
            "round", "pxl-Test", "augment", "rowSwap", "row+", "*row", "*row+", "max", "min", "dim",
            "R>Pr", "R►Pr(", "R>Ptheta", "R>Pθ", "R►Ptheta", "R►Pθ", "P>Rx", "P►Rx", "P>Ry", "P►Ry", "median",
            "randM", "mean", "solve", "seq", "fnInt", "nDeriv", "fMin", "fMax", "sqrt", "√", "cuberoot", "³√",
            "ln", "e^^", "𝑒^", "log", "10^^", "₁₀^", "sin", "sin^-1", "sin⁻¹", "arcsin", "asin", "cos", "cos^-1",
            "cos⁻¹", "arccos", "acos", "tan", "tan^-1", "tan⁻¹", "arctan", "atan", "sinh", "sinh^-1", "sinh⁻¹",
            "arcsinh", "asinh", "cosh", "cosh^-1", "cosh⁻¹", "arccosh", "acosh", "tanh", "tanh^-1", "tanh⁻¹",
            "arctanh", "atanh", "int", "abs", "det", "identity", "sum", "prod", "not", "iPart", "fPart", "npv",
            "irr", "bal", "SigmaPrn", "ΣPrn", "SigmaInt", "ΣInt", ">Nom", "►Nom", ">Eff", "►Eff", "dbd", "lcm",
            "gcd", "randInt", "randBin", "sub", "stdDev", "variance", "inString", "normalcdf", "invNorm", "tcdf",
            "chi^2cdf", "χ²cdf", "χ^2cdf", "chi²cdf", "Fcdf", "𝙵cdf", "𝐅cdf", "binompdf", "binomcdf", "poissonpdf",
            "poissoncdf", "geometpdf", "geometcdf", "normalpdf", "tpdf", "chi^2pdf", "χ²pdf", "χ^2pdf", "chi²pdf",
            "Fpdf", "𝙵pdf", "𝐅pdf", "randNorm", "conj", "real", "imag", "angle", "cumSum", "expr", "length",
            "DeltaList", "ΔList", "ref", "rref", "remainder", "checkTmr", "timeCnv", "dayOfWk", "getDtStr",
            "getTmStr", "invT", "eval", "randIntNoRep", "logBASE", "piecewise", "toString", "invBinom"
    );

    public static final List<String> EXPR_FUNCTIONS_OPTIONAL_ARGS = List.of(
            "tvm_Pmt", "tvm_I%", "tvm_PV", "tvm_N", "tvm_𝗡", "tvm_FV"
    );

    public static final List<String> EXPR_FUNCTIONS_NO_ARGS = List.of(
            "rand", "getKey", "getDate", "getTime", "startTmr", "getDtFmt", "getTmFmt",
            "isClockOn", "LEFT", "CENTER", "RIGHT"
    );
}
