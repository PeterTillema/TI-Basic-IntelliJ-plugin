package nl.petertillema.tibasic.controlFlow.type;

import com.intellij.codeInspection.dataFlow.memory.DfaMemoryStateImpl;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.value.DfaValue;
import com.intellij.codeInspection.dataFlow.value.DfaVariableValue;
import nl.petertillema.tibasic.controlFlow.descriptor.ListElementDescriptor;
import nl.petertillema.tibasic.controlFlow.descriptor.SpecialFieldDescriptor;
import nl.petertillema.tibasic.controlFlow.operator.BinaryOperator;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DfElementMap {

    private final int dimensions;

    private final List<DfType> dimensionLengths;
    private final Map<Integer, Map<Integer, DfBigDecimalType>> elements;

    public DfElementMap(int dimensions, List<DfType> dimensionLengths, Map<Integer, Map<Integer, DfBigDecimalType>> elements) {
        this.dimensions = dimensions;
        this.dimensionLengths = dimensionLengths;
        this.elements = elements;
    }

    public int getDimensions() {
        return dimensions;
    }

    public List<DfType> getDimensionLengths() {
        return dimensionLengths;
    }

    public DfElementMap unknownElements() {
        return new DfElementMap(dimensions, new ArrayList<>(dimensionLengths), new HashMap<>());
    }

    public DfElementMap execOperator(@NotNull Function<DfBigDecimalType, DfType> operator) {
        Map<Integer, Map<Integer, DfBigDecimalType>> newElements = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> rowEntry : elements.entrySet()) {
            Map<Integer, DfBigDecimalType> newRow = new HashMap<>();
            for (Map.Entry<Integer, DfBigDecimalType> colEntry : rowEntry.getValue().entrySet()) {
                DfBigDecimalType oldValue = colEntry.getValue();
                DfType result = operator.apply(oldValue);
                if (result instanceof DfBigDecimalType bigDecimalType) {
                    newRow.put(colEntry.getKey(), bigDecimalType);
                }
            }
            if (!newRow.isEmpty()) newElements.put(rowEntry.getKey(), newRow);
        }
        return new DfElementMap(dimensions, new ArrayList<>(dimensionLengths), newElements);
    }

    public DfElementMap execBiOperator(@NotNull DfElementMap other, @NotNull BiFunction<DfBigDecimalType, DfBigDecimalType, DfType> operator) {
        if (this.dimensions != other.dimensions) {
            return new DfElementMap(this.dimensions, new ArrayList<>(this.dimensionLengths), new HashMap<>());
        }

        Map<Integer, Map<Integer, DfBigDecimalType>> newElements = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> leftRowEntry : this.elements.entrySet()) {
            int rowIndex = leftRowEntry.getKey();
            Map<Integer, DfBigDecimalType> leftRow = leftRowEntry.getValue();
            Map<Integer, DfBigDecimalType> rightRow = other.elements.get(rowIndex);
            if (rightRow == null) continue;

            Map<Integer, DfBigDecimalType> outRow = new HashMap<>();
            for (Map.Entry<Integer, DfBigDecimalType> leftColEntry : leftRow.entrySet()) {
                int colIndex = leftColEntry.getKey();
                DfBigDecimalType left = leftColEntry.getValue();
                DfBigDecimalType right = rightRow.get(colIndex);
                if (right == null) continue;
                DfType result = operator.apply(left, right);
                if (result instanceof DfBigDecimalType bigDecimalType) {
                    outRow.put(colIndex, bigDecimalType);
                }
            }
            if (!outRow.isEmpty()) {
                newElements.put(rowIndex, outRow);
            }
        }

        return new DfElementMap(this.dimensions, new ArrayList<>(this.dimensionLengths), newElements);
    }

    public DfElementMap matrixMultiply(@NotNull DfElementMap other) {
        Map<Integer, Map<Integer, DfBigDecimalType>> result = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> leftRowEntry : this.elements.entrySet()) {
            int rowIndex = leftRowEntry.getKey();
            Map<Integer, DfBigDecimalType> leftRow = leftRowEntry.getValue();
            Map<Integer, DfBigDecimalType> resultRow = new HashMap<>();

            // Build contributions by iterating over k where left has non-empty
            for (Map.Entry<Integer, DfBigDecimalType> leftColEntry : leftRow.entrySet()) {
                int colIndex = leftColEntry.getKey();
                DfBigDecimalType a_ik = leftColEntry.getValue();
                Map<Integer, DfBigDecimalType> rightRowEntry = other.elements.get(colIndex);
                if (rightRowEntry == null) continue;

                for (Map.Entry<Integer, DfBigDecimalType> rightColEntry : rightRowEntry.entrySet()) {
                    int j = rightColEntry.getKey();
                    DfBigDecimalType b_kj = rightColEntry.getValue();

                    // product
                    DfType prodType = a_ik.eval(b_kj, BinaryOperator.TIMES);
                    if (!(prodType instanceof DfBigDecimalType prod)) continue;

                    DfBigDecimalType current = resultRow.get(j);
                    if (current == null) {
                        resultRow.put(j, prod);
                    } else {
                        DfType sumType = current.eval(prod, BinaryOperator.PLUS);
                        if (sumType instanceof DfBigDecimalType sumBd) {
                            resultRow.put(j, sumBd);
                        }
                    }
                }
            }

            if (!resultRow.isEmpty()) {
                result.put(rowIndex, resultRow);
            }
        }

        // Resulting dimension lengths: rows from left, cols from right
        List<DfType> newLengths = new ArrayList<>(2);
        DfType length1 = this.dimensionLengths.isEmpty() ? SpecialFieldDescriptor.LIST_LENGTH.getDfType(null) : this.dimensionLengths.getFirst();
        DfType length2 = other.dimensionLengths.size() > 1 ? other.dimensionLengths.get(1) : SpecialFieldDescriptor.LIST_LENGTH.getDfType(null);
        newLengths.add(length1);
        newLengths.add(length2);

        return new DfElementMap(2, newLengths, result);
    }

    public DfElementMap transpose() {
        if (this.dimensions != 2) {
            return new DfElementMap(this.dimensions, new ArrayList<>(this.dimensionLengths), new HashMap<>());
        }

        Map<Integer, Map<Integer, DfBigDecimalType>> newElements = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> rowEntry : elements.entrySet()) {
            int rowIndex = rowEntry.getKey();
            for (Map.Entry<Integer, DfBigDecimalType> colEntry : rowEntry.getValue().entrySet()) {
                int colIndex = colEntry.getKey();
                DfBigDecimalType value = colEntry.getValue();
                Map<Integer, DfBigDecimalType> tRow = newElements.computeIfAbsent(colIndex, k -> new HashMap<>());
                tRow.put(rowIndex, value);
            }
        }

        List<DfType> newLengths = new ArrayList<>(2);
        if (this.dimensionLengths.size() == 2) {
            newLengths.add(this.dimensionLengths.get(1));
            newLengths.add(this.dimensionLengths.get(0));
        } else {
            newLengths.addAll(this.dimensionLengths);
        }

        return new DfElementMap(2, newLengths, newElements);
    }

    public DfElementMap inverse() {
        if (this.dimensions != 2) {
            return new DfElementMap(this.dimensions, new ArrayList<>(this.dimensionLengths), new HashMap<>());
        }

        // Determine size n as max index found in rows and columns
        int n = 0;
        for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> e : elements.entrySet()) {
            n = Math.max(n, e.getKey());
            for (Integer j : e.getValue().keySet()) {
                n = Math.max(n, j);
            }
        }
        if (n == 0) {
            return new DfElementMap(2, new ArrayList<>(this.dimensionLengths), new HashMap<>());
        }

        // Create mutable copies for A and I (identity)
        Map<Integer, Map<Integer, DfBigDecimalType>> A = new HashMap<>();
        for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> row : elements.entrySet()) {
            A.put(row.getKey(), new HashMap<>(row.getValue()));
        }
        Map<Integer, Map<Integer, DfBigDecimalType>> I = new HashMap<>();
        // Prepare constants 0 and 1
        DfBigDecimalType ZERO = (DfBigDecimalType) DfBigDecimalConstantType.fromValue(0);
        DfBigDecimalType ONE = (DfBigDecimalType) DfBigDecimalConstantType.fromValue(1);
        for (int i = 1; i <= n; i++) {
            Map<Integer, DfBigDecimalType> rowI = new HashMap<>();
            rowI.put(i, ONE);
            I.put(i, rowI);
        }

        // Helpers for arithmetic, returning null if result is not numeric
        Function<DfBigDecimalType, DfBigDecimalType> keep = x -> x; // placeholder to keep style
        BiFunction<DfBigDecimalType, DfBigDecimalType, DfBigDecimalType> plus = (x, y) -> {
            DfType r = x.eval(y, BinaryOperator.PLUS);
            return (r instanceof DfBigDecimalType bd) ? bd : null;
        };
        BiFunction<DfBigDecimalType, DfBigDecimalType, DfBigDecimalType> minus = (x, y) -> {
            DfType r = x.eval(y, BinaryOperator.MINUS);
            return (r instanceof DfBigDecimalType bd) ? bd : null;
        };
        BiFunction<DfBigDecimalType, DfBigDecimalType, DfBigDecimalType> mul = (x, y) -> {
            DfType r = x.eval(y, BinaryOperator.TIMES);
            return (r instanceof DfBigDecimalType bd) ? bd : null;
        };
        BiFunction<DfBigDecimalType, DfBigDecimalType, DfBigDecimalType> div = (x, y) -> {
            DfType r = x.eval(y, BinaryOperator.DIVIDE);
            return (r instanceof DfBigDecimalType bd) ? bd : null;
        };

        // Gaussian elimination
        for (int k = 1; k <= n; k++) {
            // Pivot
            Map<Integer, DfBigDecimalType> pivotRow = A.get(k);
            DfBigDecimalType pivot = pivotRow == null ? null : pivotRow.get(k);
            if (pivot == null) {
                // cannot invert due to missing pivot information
                return new DfElementMap(2, new ArrayList<>(this.dimensionLengths), new HashMap<>());
            }

            // Normalize pivot row: row_k = row_k / pivot (for both A and I)
            Map<Integer, DfBigDecimalType> rowAk = A.computeIfAbsent(k, kk -> new HashMap<>());
            Map<Integer, DfBigDecimalType> rowIk = I.computeIfAbsent(k, kk -> new HashMap<>());
            // Normalize A row
            Map<Integer, DfBigDecimalType> newRowAk = new HashMap<>();
            for (Map.Entry<Integer, DfBigDecimalType> entry : rowAk.entrySet()) {
                DfBigDecimalType val = entry.getValue();
                DfBigDecimalType norm = div.apply(val, pivot);
                if (norm != null) newRowAk.put(entry.getKey(), norm);
            }
            // Ensure the diagonal becomes 1
            newRowAk.put(k, ONE);
            A.put(k, newRowAk);

            // Normalize I row
            Map<Integer, DfBigDecimalType> newRowIk = new HashMap<>();
            for (Map.Entry<Integer, DfBigDecimalType> entry : rowIk.entrySet()) {
                DfBigDecimalType val = entry.getValue();
                DfBigDecimalType norm = div.apply(val, pivot);
                if (norm != null) newRowIk.put(entry.getKey(), norm);
            }
            I.put(k, newRowIk);

            // Eliminate other rows
            for (int i = 1; i <= n; i++) {
                if (i == k) continue;
                Map<Integer, DfBigDecimalType> rowAi = A.computeIfAbsent(i, ii -> new HashMap<>());
                DfBigDecimalType factor = rowAi.get(k);
                if (factor == null) continue; // nothing to eliminate

                // A[i,*] = A[i,*] - factor * A[k,*]
                Map<Integer, DfBigDecimalType> newRowAi = new HashMap<>(rowAi);
                for (Map.Entry<Integer, DfBigDecimalType> entry : A.get(k).entrySet()) {
                    int j = entry.getKey();
                    DfBigDecimalType a_kj = entry.getValue();
                    DfBigDecimalType prod = mul.apply(factor, a_kj);
                    if (prod == null) continue;
                    DfBigDecimalType current = newRowAi.get(j);
                    if (current == null) current = ZERO;
                    DfBigDecimalType updated = minus.apply(current, prod);
                    if (updated != null) newRowAi.put(j, updated);
                }
                // Force eliminated column to zero by removing it (sparse zero)
                newRowAi.remove(k);
                A.put(i, newRowAi);

                // I[i,*] = I[i,*] - factor * I[k,*]
                Map<Integer, DfBigDecimalType> rowIi = I.computeIfAbsent(i, ii -> new HashMap<>());
                Map<Integer, DfBigDecimalType> newRowIi = new HashMap<>(rowIi);
                for (Map.Entry<Integer, DfBigDecimalType> entry : I.get(k).entrySet()) {
                    int j = entry.getKey();
                    DfBigDecimalType i_kj = entry.getValue();
                    DfBigDecimalType prod = mul.apply(factor, i_kj);
                    if (prod == null) continue;
                    DfBigDecimalType current = newRowIi.get(j);
                    if (current == null) current = ZERO;
                    DfBigDecimalType updated = minus.apply(current, prod);
                    if (updated != null) newRowIi.put(j, updated);
                }
                I.put(i, newRowIi);
            }
        }

        // Inverse is now in I
        List<DfType> newLengths = new ArrayList<>(2);
        // Preserve square dimensions metadata if present; fallback to unknown lengths
        DfType len = this.dimensionLengths.isEmpty() ? SpecialFieldDescriptor.LIST_LENGTH.getDfType(null) : this.dimensionLengths.getFirst();
        newLengths.add(len);
        newLengths.add(len);
        return new DfElementMap(2, newLengths, I);
    }

    public static DfElementMap loadFromSource(@NotNull DfaMemoryStateImpl state, @NotNull DfaVariableValue source) {
        int dimensions = 1;
        List<DfType> lengths = new ArrayList<>();
        DfType sourceType = state.getDfType(source);
        if (sourceType instanceof DfListType listType) {
            lengths.add(listType.getSpecialFieldType());
        } else if (sourceType instanceof DfMatrixType matrixType) {
            lengths.add(matrixType.getSpecialFieldType());
        } else {
            lengths.add(SpecialFieldDescriptor.LIST_LENGTH.getDfType(null));
        }

        Map<Integer, Map<Integer, DfBigDecimalType>> elems = new HashMap<>();

        for (DfaVariableValue child : source.getDependentVariables()) {
            if (!source.equals(child.getQualifier())) continue;
            if (!(child.getDescriptor() instanceof ListElementDescriptor childElementDescriptor)) continue;
            int index = childElementDescriptor.index();
            DfType childType = state.getDfType(child);
            if (childType instanceof DfListType rowListType) {
                if (dimensions < 2) {
                    dimensions = 2;
                    lengths.add(rowListType.getSpecialFieldType());
                }
                Map<Integer, DfBigDecimalType> row = elems.computeIfAbsent(index, k -> new HashMap<>());
                for (DfaVariableValue grandChild : child.getDependentVariables()) {
                    if (!child.equals(grandChild.getQualifier())) continue;
                    if (!(grandChild.getDescriptor() instanceof ListElementDescriptor led2)) continue;
                    DfType elType = state.getDfType(grandChild);
                    if (elType instanceof DfBigDecimalType bigDecimalType) {
                        row.put(led2.index(), bigDecimalType);
                    }
                }
            } else if (childType instanceof DfBigDecimalType bigDecimalType) {
                Map<Integer, DfBigDecimalType> row = elems.computeIfAbsent(index, k -> new HashMap<>());
                row.put(1, bigDecimalType);
            }
        }

        while (lengths.size() < dimensions) {
            lengths.add(SpecialFieldDescriptor.LIST_LENGTH.getDfType(null));
        }

        return new DfElementMap(dimensions, lengths, elems);
    }

    public void exportTo(@NotNull DfaMemoryStateImpl state, @NotNull DfaVariableValue destination) {
        if (dimensions == 1) {
            DfType mainLength = dimensionLengths.isEmpty() ? SpecialFieldDescriptor.LIST_LENGTH.getDfType(null) : dimensionLengths.getFirst();
            DfType destType = SpecialFieldDescriptor.LIST_LENGTH.asDfType(mainLength);
            state.recordVariableType(destination, destType);

            for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> rowEntry : elements.entrySet()) {
                int index = rowEntry.getKey();
                DfaValue elementValue = new ListElementDescriptor(index).createValue(state.getFactory(), destination);
                if (elementValue instanceof DfaVariableValue elementVar) {
                    DfBigDecimalType val = rowEntry.getValue().get(1);
                    if (val != null) {
                        state.recordVariableType(elementVar, val);
                    }
                }
            }
        } else if (dimensions == 2) {
            DfType mainLength = dimensionLengths.isEmpty() ? SpecialFieldDescriptor.MATRIX_LENGTH.getDfType(null) : dimensionLengths.getFirst();
            DfType destType = SpecialFieldDescriptor.MATRIX_LENGTH.asDfType(mainLength);
            state.recordVariableType(destination, destType);

            DfType length2 = dimensionLengths.size() > 1 ? dimensionLengths.get(1) : SpecialFieldDescriptor.LIST_LENGTH.getDfType(null);
            for (Map.Entry<Integer, Map<Integer, DfBigDecimalType>> rowEntry : elements.entrySet()) {
                int rowIndex = rowEntry.getKey();
                DfaValue rowValue = new ListElementDescriptor(rowIndex).createValue(state.getFactory(), destination);
                if (rowValue instanceof DfaVariableValue rowVar) {
                    DfType rowType = SpecialFieldDescriptor.LIST_LENGTH.asDfType(length2);
                    state.recordVariableType(rowVar, rowType);
                    for (Map.Entry<Integer, DfBigDecimalType> colEntry : rowEntry.getValue().entrySet()) {
                        int colIndex = colEntry.getKey();
                        DfaValue colValue = new ListElementDescriptor(colIndex).createValue(state.getFactory(), rowVar);
                        if (colValue instanceof DfaVariableValue colVar) {
                            state.recordVariableType(colVar, colEntry.getValue());
                        }
                    }
                }
            }
        }
    }

}
