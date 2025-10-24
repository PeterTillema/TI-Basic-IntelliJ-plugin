package nl.petertillema.tibasic.tokenization;

import com.intellij.openapi.components.Service;
import com.intellij.openapi.progress.ProgressIndicator;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public final class TIBasicTokenizerService {

    private static final Map<String, TokenBytes> TOKEN_TABLE = new HashMap<>();
    private static final Map<TokenBytes, String> TOKEN_TABLE_REVERSED = new HashMap<>();
    private static final List<String> TOKEN_KEYS = new ArrayList<>();

    public TIBasicTokenizerService() {
        this.loadTokensXml();
    }

    public @NotNull TokenizeResult tokenize(String text, ProgressIndicator indicator) {
        List<byte[]> output = new ArrayList<>();
        int i = 0;

        while (i < text.length()) {
            if (indicator.isCanceled()) break;

            if (text.charAt(i) == '\n') {
                output.add(new byte[]{0x3F});
                i++;
                continue;
            }

            // If there is an escape, check the upcoming chars
            if (text.charAt(i) == '\\') {
                // Character escape \xFF or \uFFFF
                try {
                    if (i + 1 >= text.length()) {
                        return new TokenizeResult(TokenizeStatus.FAIL, text.length() - 1, new byte[0]);
                    }
                    if (text.charAt(i + 1) == 'x') {
                        if (i + 3 >= text.length()) {
                            return new TokenizeResult(TokenizeStatus.FAIL, text.length() - 1, new byte[0]);
                        }
                        var b = Integer.parseInt(text.substring(i + 2, i + 4), 16);
                        output.add(new byte[]{(byte) b});
                        i += 4;
                        continue;
                    } else if (text.charAt(i + 1) == 'u') {
                        if (i + 5 >= text.length()) {
                            return new TokenizeResult(TokenizeStatus.FAIL, text.length() - 1, new byte[0]);
                        }
                        var b = Integer.parseInt(text.substring(i + 2, i + 6), 16);
                        output.add(new byte[]{(byte) (b % 256), (byte) (b / 256)});
                        i += 6;
                        continue;
                    } else {
                        i++;
                    }
                } catch (NumberFormatException e) {
                    return new TokenizeResult(TokenizeStatus.FAIL, i + 2, new byte[0]);
                }
            }

            boolean matched = false;
            // The keys are sorted by length descending, so looping through the list works fine,
            // as the longest match is first encountered.
            for (String k : TOKEN_KEYS) {
                if (regionMatches(text, i, k)) {
                    output.add(TOKEN_TABLE.get(k).bytes());
                    i += k.length();
                    matched = true;
                    break;
                }
            }
            if (!matched) {
                return new TokenizeResult(TokenizeStatus.FAIL, i, new byte[0]);
            }
        }

        // Flatten all the bytes to a single array
        int total = output.stream().mapToInt(a -> a.length).sum();
        byte[] bytes = new byte[total];
        int p = 0;
        for (byte[] arr : output) {
            System.arraycopy(arr, 0, bytes, p, arr.length);
            p += arr.length;
        }

        return new TokenizeResult(TokenizeStatus.OK, -1, bytes);
    }

    public String detokenize(byte[] bytes) {
        if (bytes.length < 76) {
            throw new RuntimeException("Not enough input data");
        }

        var i = 17 + 55 + 2;
        var out = new StringBuilder();
        var currBytes = new byte[0];

        while (i < bytes.length - 2) {
            currBytes = appendByte(currBytes, bytes[i]);
            i++;
            var foundToken = TOKEN_TABLE_REVERSED.get(new TokenBytes(currBytes));
            if (foundToken == null) {
                if (currBytes.length >= 3) {
                    throw new RuntimeException("Invalid bytes encountered");
                }
            } else {
                out.append(foundToken);
                currBytes = new byte[0];
            }
        }

        return out.toString();
    }

    private boolean regionMatches(String text, int offset, String token) {
        if (offset + token.length() > text.length()) {
            return false;
        }
        for (int j = 0; j < token.length(); j++) {
            if (text.charAt(offset + j) != token.charAt(j)) {
                return false;
            }
        }
        return true;
    }

    private void loadTokensXml() {
        try (InputStream tokensXmlStream = this.getClass().getResourceAsStream("/tokens.xml")) {
            if (tokensXmlStream == null) return;

            var factory = DocumentBuilderFactory.newInstance();
            var builder = factory.newDocumentBuilder();
            var document = builder.parse(tokensXmlStream);
            var mainTokens = document.getElementsByTagName("tokens").item(0);
            if (mainTokens != null) {
                this.loadAllTokens(mainTokens, new byte[0]);
            }
            // Build TOKEN_KEYS sorted by length, longest first
            TOKEN_KEYS.clear();
            TOKEN_KEYS.addAll(TOKEN_TABLE.keySet());
            TOKEN_KEYS.sort(Comparator.comparingInt(String::length).reversed());
        } catch (Exception ignored) {
        }
    }

    private void loadAllTokens(Node root, byte[] prefix) {
        if (root == null) return;
        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node n = children.item(i);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String name = n.getNodeName();
            if ("token".equals(name)) {
                String valueAttr = getValueAttribute(n);
                byte[] bytes = appendByte(prefix, parseHexByte(valueAttr));
                addTokenStrings(n, bytes);
            } else if ("two-byte".equals(name)) {
                String valueAttr = getValueAttribute(n);
                byte twoPrefix = parseHexByte(valueAttr);
                byte[] newPrefix = appendByte(prefix, twoPrefix);
                // Recurse into its child tokens
                loadAllTokens(n, newPrefix);
            }
        }
    }

    private static void addTokenStrings(Node tokenNode, byte[] bytes) {
        // Collect potential string representations from the <lang> elements
        Set<String> keys = new HashSet<>();
        NodeList children = tokenNode.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node verOrLang = children.item(i);
            if (verOrLang.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            if ("version".equals(verOrLang.getNodeName())) {
                // dive into version -> lang
                NodeList vkids = verOrLang.getChildNodes();
                for (int j = 0; j < vkids.getLength(); j++) {
                    Node maybeLang = vkids.item(j);
                    if (maybeLang.getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    addFromLangNode(keys, maybeLang);
                }
            } else {
                addFromLangNode(keys, verOrLang);
            }
        }

        var tokenBytes = new TokenBytes(bytes);
        for (String key : keys) {
            TOKEN_TABLE.putIfAbsent(key, tokenBytes);
            TOKEN_TABLE_REVERSED.putIfAbsent(tokenBytes, key);
        }
    }

    private static void addFromLangNode(Set<String> keys, Node maybeLang) {
        if ("lang".equals(maybeLang.getNodeName())) {
            // inner tags accessible/variant might contain alternate text
            NodeList langKids = maybeLang.getChildNodes();
            for (int k = 0; k < langKids.getLength(); k++) {
                Node lk = langKids.item(k);
                if (lk.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                String nname = lk.getNodeName();
                if ("accessible".equals(nname) || "variant".equals(nname)) {
                    String txt = lk.getTextContent();
                    if (txt != null && !txt.isEmpty()) keys.add(txt);
                }
            }
        }
    }

    private static String getValueAttribute(Node node) {
        if (node == null || node.getAttributes() == null) return null;
        Node a = node.getAttributes().getNamedItem("value");
        return a != null ? a.getNodeValue() : null;
    }

    private static byte parseHexByte(String value) {
        if (value == null) return 0;
        String s = value.trim();
        if (s.startsWith("$")) {
            s = s.substring(1);
        }
        if (s.startsWith("0x") || s.startsWith("0X")) {
            s = s.substring(2);
        }
        int v = Integer.parseInt(s, 16) & 0xFF;
        return (byte) v;
    }

    private static byte[] appendByte(byte[] prefix, byte b) {
        byte[] out = new byte[prefix.length + 1];
        System.arraycopy(prefix, 0, out, 0, prefix.length);
        out[prefix.length] = b;
        return out;
    }
}
