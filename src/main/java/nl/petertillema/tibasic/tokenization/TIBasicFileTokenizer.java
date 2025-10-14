package nl.petertillema.tibasic.tokenization;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public final class TIBasicFileTokenizer {

    private TIBasicFileTokenizer() {
    }

    public static void tokenize(@NotNull Project project,
                                @NotNull PsiFile psiFile,
                                @NotNull Consumer<String> reporter,
                                @NotNull Runnable onFinished,
                                @NotNull File outputFile) {
        ProgressManager.getInstance().run(new Task.Backgroundable(project, "Tokenizing TI-Basic file") {
            @Override
            public void run(@NotNull ProgressIndicator indicator) {
                indicator.setIndeterminate(false);
                reporter.accept("Started tokenizing: " + psiFile.getName());

                final String[] sourceHolder = {null};

                ReadAction.run(() -> sourceHolder[0] = psiFile.getText());

                String source = sourceHolder[0];
                if (source == null) {
                    reporter.accept("No source content.");
                    onFinished.run();
                    return;
                }

                // Normalize newlines
                source = source.replace("\r\n", "\n").replace('\r', '\n');
                // Strip indentation
                source = Pattern.compile("^[ \\t]+", Pattern.MULTILINE).matcher(source).replaceAll("");
                // Strip line comments
                source = Pattern.compile("^//.*[\\n\\r]+", Pattern.MULTILINE).matcher(source).replaceAll("");
                // Strip other comments
                source = Pattern.compile("\\s+//.*/", Pattern.MULTILINE).matcher(source).replaceAll("");

                // Tokenize the entire source
                var tokenizerService = ApplicationManager.getApplication().getService(TIBasicTokenizerService.class);
                var tokenizeResult = tokenizerService.tokenize(source, indicator);

                if (tokenizeResult.status() == TokenizeStatus.FAIL) {
                    reporter.accept("Error encountered at offset " + tokenizeResult.errorOffset());
                    onFinished.run();
                    return;
                }

                byte[] bytes = tokenizeResult.out();
                var programDataSize = bytes.length + 2;

                var innerBuffer = ByteBuffer.allocate(bytes.length + 19);
                innerBuffer.order(ByteOrder.LITTLE_ENDIAN);
                innerBuffer.putShort((short) 0x0D);   // Header size
                innerBuffer.putShort((short) programDataSize); // Data size 2
                innerBuffer.put((byte) 5);   // Program type
                innerBuffer.put("CLRNOTE".getBytes());   // Program name
                innerBuffer.put(new byte[]{0x00}); // Program name padding
                innerBuffer.put((byte) 0);    // Version
                innerBuffer.put((byte) 0);   // Archived
                innerBuffer.putShort((short) programDataSize);   // Data size 3
                innerBuffer.putShort((short) bytes.length);   // Data size 4
                innerBuffer.put(bytes);  // Main data
                var innerBytes = innerBuffer.array();

                var checksum = 0;
                for (var b : innerBytes) {
                    checksum = (checksum + (b & 0xFF)) % 0x10000;
                }

                var buffer = ByteBuffer.allocate(bytes.length + 76);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.put("**TI83F*".getBytes());  // Main header
                buffer.put(new byte[]{0x1A, 0x0A, 0x00});   // Magic tokens
                buffer.put("Created by IntelliJ TI-Basic plugin".getBytes());   // Comment
                buffer.put(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});   // Comment padding
                buffer.putShort((short) innerBytes.length); // Data size 1
                buffer.put(innerBytes);
                buffer.putShort((short) checksum);  // Checksum

                // Write the output file
                try {
                    ensureParent(outputFile);
                    try (FileOutputStream out = new FileOutputStream(outputFile)) {
                        out.write(buffer.array());
                    }
                    reporter.accept("Wrote " + bytes.length + " bytes to: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    reporter.accept("Failed to write output: " + e.getMessage());
                }

                // Print a simple hex dump preview (first 512 bytes)
                reporter.accept(hexPreview(bytes, 512));
                reporter.accept("Done.");
                onFinished.run();
            }
        });
    }

    private static void ensureParent(File file) throws IOException {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()) {
                throw new IOException("Cannot create directory: " + parent);
            }
        }
    }

    private static String hexPreview(byte[] data, int maxBytes) {
        StringBuilder sb = new StringBuilder();
        int n = Math.min(data.length, maxBytes);
        for (int i = 0; i < n; i++) {
            if (i % 16 == 0) sb.append(String.format("%n%04X: ", i));
            sb.append(String.format("%02X ", data[i] & 0xFF));
        }
        if (data.length > n) sb.append(String.format("%n... (%d bytes total)", data.length));
        return sb.toString();
    }
}
