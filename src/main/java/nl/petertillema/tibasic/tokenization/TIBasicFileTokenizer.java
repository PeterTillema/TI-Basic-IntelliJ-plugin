package nl.petertillema.tibasic.tokenization;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ReadAction;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.LineColumn;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import nl.petertillema.tibasic.run.TIBasicRunConfigurationOptions;
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
                                @NotNull TIBasicRunConfigurationOptions options) {
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
                TIBasicTokenizerService tokenizerService = ApplicationManager.getApplication().getService(TIBasicTokenizerService.class);
                TokenizeResult tokenizeResult = tokenizerService.tokenize(source, indicator);

                if (tokenizeResult.status() == TokenizeStatus.FAIL) {
                    // todo: count for comments and indentation
                    LineColumn lineColumn = StringUtil.offsetToLineColumn(source, tokenizeResult.errorOffset());
                    reporter.accept("Error encountered at line " + (lineColumn.line + 1) + ", column " + lineColumn.column);
                    onFinished.run();
                    return;
                }

                byte[] bytes = tokenizeResult.out();
                int programDataSize = bytes.length + 2;

                // Get some output properties
                String programName = options.getProgramNameField();
                programName = programName == null ? "PROGRAM" : programName;
                programName = programName
                        .trim()
                        .replace("theta", "[")
                        .toUpperCase()
                        .substring(0, Math.min(programName.length(), 8));
                int programType = switch (options.getProgramTypeField()) {
                    case "Protected Program" -> 0x06;
                    case "AppVar" -> 0x17;
                    default -> 0x05;
                };
                boolean archived = options.getArchivedField();

                ByteBuffer innerBuffer = ByteBuffer.allocate(bytes.length + 19);
                innerBuffer.order(ByteOrder.LITTLE_ENDIAN);
                innerBuffer.putShort((short) 0x0D);                                                         // Header size
                innerBuffer.putShort((short) programDataSize);                                              // Data size 2
                innerBuffer.put((byte) programType);                                                        // Program type
                innerBuffer.put(programName.getBytes());                                                    // Program name
                innerBuffer.put(new byte[]{0, 0, 0, 0, 0, 0, 0}, 0, 8 - programName.length()); // Program name padding
                innerBuffer.put((byte) 0);                                                                  // Version
                innerBuffer.put((byte) (archived ? 0x80 : 0x00));                                           // Archived
                innerBuffer.putShort((short) programDataSize);                                              // Data size 3
                innerBuffer.putShort((short) bytes.length);                                                 // Data size 4
                innerBuffer.put(bytes);                                                                     // Main data
                byte[] innerBytes = innerBuffer.array();

                int checksum = 0;
                for (byte b : innerBytes) {
                    checksum = (checksum + (b & 0xFF)) % 0x10000;
                }

                ByteBuffer buffer = ByteBuffer.allocate(bytes.length + 76);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                buffer.put("**TI83F*".getBytes());                                                          // Main header
                buffer.put(new byte[]{0x1A, 0x0A, 0x00});                                                   // Magic tokens
                buffer.put("Created by IntelliJ TI-Basic plugin".getBytes());                               // Comment
                buffer.put(new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00});                           // Comment padding
                buffer.putShort((short) innerBytes.length);                                                 // Data size 1
                buffer.put(innerBytes);                                                                     // Program data
                buffer.putShort((short) checksum);                                                          // Checksum
                byte[] bufferBytes = buffer.array();

                // Write the output file
                try {
                    File outputFile = new File(options.getOutputPathField());
                    ensureParent(outputFile);
                    try (FileOutputStream out = new FileOutputStream(outputFile)) {
                        out.write(bufferBytes);
                    }
                    reporter.accept("Wrote " + bufferBytes.length + " bytes to: " + outputFile.getAbsolutePath());
                } catch (IOException e) {
                    reporter.accept("Failed to write output: " + e.getMessage());
                }

                // Print a simple hex dump preview (first 512 bytes)
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
}
