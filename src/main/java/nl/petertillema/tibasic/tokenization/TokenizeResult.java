package nl.petertillema.tibasic.tokenization;

public record TokenizeResult(TokenizeStatus status, int errorOffset, byte[] out) {
}
