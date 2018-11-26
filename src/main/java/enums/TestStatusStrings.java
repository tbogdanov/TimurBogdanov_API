package enums;

public enum TestStatusStrings {
    FAIL_RESPONSE_SIZE("Unexpected number of answers in the response body."),
    FAIL_INCORRECT_ERROR_NUMBER("Unexpected number of errors for a text.");

    private String text;

    TestStatusStrings(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
