package nl.petertillema.tibasic.syntax.documentation.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public record Token(
        @NotNull String name,
        @Nullable String accessibleName,
        @Nullable List<String> nameVariants,
        @NotNull String type,
        @NotNull List<String> categories,
        @NotNull List<TokenSyntax> syntaxes,
        @NotNull Map<String, String> localizations,
        @Nullable Map<String, String> since,
        @Nullable Map<String, String> until,
        @Nullable Boolean isAlias
) {
}
