package nl.petertillema.tibasic.syntax.documentation;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.intellij.lang.documentation.DocumentationMarkup;
import com.intellij.openapi.components.Service;
import nl.petertillema.tibasic.syntax.documentation.models.Token;
import nl.petertillema.tibasic.syntax.documentation.models.TokenSyntax;

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

            Map<String, Token> catalog = objectMapper.readValue(catalogJsonStream, new TypeReference<>() {
            });
            for (Map.Entry<String, Token> token : catalog.entrySet()) {
                addToken(token.getValue(), token.getKey());
            }
        } catch (Exception ignored) {
        }
    }

    private void addToken(Token token, String bytes) {
        String html = getTokenDoc(token, bytes);

        TOKEN_DOCUMENTATION.put(token.name(), html);
        if (token.accessibleName() != null) TOKEN_DOCUMENTATION.put(token.accessibleName(), html);
        if (token.nameVariants() != null) {
            for (String variant : token.nameVariants()) {
                TOKEN_DOCUMENTATION.put(variant, html);
            }
        }
    }

    private String getTokenDoc(Token token, String bytes) {
        StringBuilder sb = new StringBuilder();
        sb.append(DocumentationMarkup.DEFINITION_START);
        sb.append(token.name());
        sb.append(DocumentationMarkup.DEFINITION_END);
        sb.append(DocumentationMarkup.CONTENT_START);
        sb.append("Bytes: ");
        sb.append("<code>");
        sb.append(bytes);
        sb.append("</code>");
        sb.append(DocumentationMarkup.CONTENT_END);

        ArrayList<String> syntaxes = new ArrayList<>();

        for (TokenSyntax syntax : token.syntaxes()) {
            StringBuilder sb1 = new StringBuilder();
            sb1.append("<code>");
            sb1.append(syntax.syntax());
            sb1.append("</code><br><br>");
            sb1.append("Description: ");
            sb1.append(syntax.description());

            ArrayList<String> fields = new ArrayList<>();

            // Eventually add the location
            if (syntax.location() instanceof ArrayNode arrayNode) {
                ArrayList<String> elements = new ArrayList<>();
                for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                    JsonNode element = it.next();
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
