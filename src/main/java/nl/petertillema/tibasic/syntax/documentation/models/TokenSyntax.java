package nl.petertillema.tibasic.syntax.documentation.models;

import com.fasterxml.jackson.databind.JsonNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record TokenSyntax(
        @Nullable String specificName,
        @NotNull String syntax,
        @Nullable String comment,
        @NotNull List<List<Object>> arguments,
        @NotNull String description,
        boolean inEditorOnly,
        @NotNull JsonNode location,
        @Nullable String specialCategory
) {
}
