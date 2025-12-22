package nl.petertillema.tibasic.controlFlow.descriptor;

import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.type.DfListType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

import static nl.petertillema.tibasic.controlFlow.type.DfBigDecimalRangeType.fromRange;
import static nl.petertillema.tibasic.controlFlow.type.rangeSet.RangeSet.range;

public class ListDescriptor extends TIBasicVariableDescriptor {

    public ListDescriptor(@NotNull String name) {
        super(fixCustomListName(name));
    }

    @Override
    public @NotNull DfType getDfType(@Nullable DfaVariableValue qualifier) {
        return new DfListType(SpecialFieldDescriptor.LIST_LENGTH, fromRange(range(BigDecimal.ZERO, BigDecimal.valueOf(999))));
    }

    private static String fixCustomListName(String listName) {
        listName = listName.replace("⌊", "|L")
                .replace("ʟ", "|L")
                .replace("smallL", "|L");
        if (listName.matches("^L[1-6]$") || listName.startsWith("|L") || listName.equals("Ans")) return listName;
        return "|L" + listName;
    }

}
