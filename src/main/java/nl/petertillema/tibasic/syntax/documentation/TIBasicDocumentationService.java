package nl.petertillema.tibasic.syntax.documentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.components.Service;
import nl.petertillema.tibasic.syntax.documentation.models.Token;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public final class TIBasicDocumentationService {

    private final Map<String, String> TOKEN_DOCUMENTATION = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TIBasicDocumentationService() {
        this.loadTokenDocumentation();
    }

    public String getToken(String token) {
        return TOKEN_DOCUMENTATION.get(token);
    }

    private void loadTokenDocumentation() {
        try (InputStream catalogJsonStream = this.getClass().getResourceAsStream("/catalog.json")) {
            if (catalogJsonStream == null) return;

            var catalog = objectMapper.readValue(catalogJsonStream, new TypeReference<Map<String, Token>>() {
            });
            for (var token : catalog.entrySet()) {
                addToken(token.getValue(), token.getKey());
            }
        } catch (Exception ignored) {
        }
    }

    private void addToken(Token token, String bytes) {
        var html = getTokenDoc(token, bytes);

        TOKEN_DOCUMENTATION.put(token.name(), html);
        if (token.accessibleName() != null) TOKEN_DOCUMENTATION.put(token.accessibleName(), html);
        if (token.nameVariants() != null) {
            for (var variant : token.nameVariants()) {
                TOKEN_DOCUMENTATION.put(variant, html);
            }
        }
    }

    private String getTokenDoc(Token token, String bytes) {
        var sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(token.name());
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        sb.append("Bytes: ");
        sb.append("<code>");
        sb.append(bytes);
        sb.append("</code>");
        sb.append(DocumentationMarkup.CONTENT_END);

        var syntaxes = new ArrayList<String>();

        for (var syntax : token.syntaxes()) {
            var sb1 = new StringBuilder();
            sb1.append("<code>");
            sb1.append(syntax.syntax());
            sb1.append("</code><br><br>");
            sb1.append("Description: ");
            sb1.append(syntax.description());

            var fields = new ArrayList<String>();

            // Eventually add the location
            if (syntax.location() instanceof ArrayNode arrayNode) {
                var elements = new ArrayList<String>();
                for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                    var element = it.next();
                    if (element instanceof TextNode textNode) {
                        elements.add("<code>" + textNode.textValue() + "</code>");
                    }
                }

                fields.add("Location: " + String.join(" ➔ ", elements));
            }

            // Eventually add the comment
            if (syntax.comment() != null && !syntax.comment().isBlank()) {
                fields.add("Comment: " + syntax.comment());
            }

            if (!fields.isEmpty()) {
                sb1.append("<br><br>");
                sb1.append(String.join("<br>", fields));
            }

            syntaxes.add(sb1.toString());
        }

        sb.append("<hr>");
        sb.append(String.join("<hr>", syntaxes));

        return sb.toString();
    }

}
