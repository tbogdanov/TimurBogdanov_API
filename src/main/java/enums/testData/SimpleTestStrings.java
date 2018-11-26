package enums.testData;

public enum SimpleTestStrings {
    RIGHT_1("Right", null, null),
    RIGHT_2("Left", null, null),
    WRONG_ADDED_LETTER("He doesn't speak Englisch", "Englisch", "English"),
    WRONG_MISSED_LETTER("But he understand it vry well", "vry", "very"),
    UNKNOWN_WORD("überзетц that", "überзетц", null),
    HTML_FORMAT("<p><a href=\"http://en.wikipedia.org\">Wikipedia</a> is an <b>online enciclopediae</b></p>",
            "enciclopediae", "encyclopedia"),
    MULTILINGUAL_RU("Как проити в библиотеку?", "проити", "пройти"),
    MULTILINGUAL_EN("Have a nies day!", "nies", "nice"),
    MULTILINGUAL_UK("Я цього не разумію.", "разумію", "розумію");

    public String text;
    public String wrongWord;
    public String rightWord;

    SimpleTestStrings(String text, String wrongWord, String rightWord) {
        this.text = text;
        this.wrongWord = wrongWord;
        this.rightWord = rightWord;
    }
}
