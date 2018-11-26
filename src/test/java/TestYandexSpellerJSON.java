import beans.YandexSpellerAnswer;
import core.YandexSpellerApi;
import enums.testData.OptionTestData;
import org.testng.annotations.Test;

import java.util.List;

import static core.YandexSpellerConstants.ErrorCodes.ERROR_UNKNOWN_WORD;
import static core.YandexSpellerConstants.Formats.HTML;
import static core.YandexSpellerConstants.Language.*;
import static core.YandexSpellerConstants.OP_NONE;
import static enums.TestStatusStrings.FAIL_INCORRECT_ERROR_NUMBER;
import static enums.TestStatusStrings.FAIL_RESPONSE_SIZE;
import static enums.testData.SimpleTestStrings.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class TestYandexSpellerJSON {

    @Test
    public void validateResponseBody(){
        YandexSpellerApi.with()
                .defaultAttrbutes()
                .text(WRONG_ADDED_LETTER.text)
                .callCheckTexts()
                .then().specification(YandexSpellerApi.successResponse());
    }

    @Test
    public void simpleRightWordTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .text(RIGHT_1.text, RIGHT_2.text)
                        .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(2));
        for (List<YandexSpellerAnswer> answer : answers) {
            assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answer, hasSize(0));
        }
    }

    @Test
    public void simpleWrongWordTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .text(WRONG_ADDED_LETTER.text, WRONG_MISSED_LETTER.text)
                        .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(2));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));
        
        YandexSpellerAnswer answer0 = answers.get(0).get(0);
        
        assertThat(answer0.word, equalTo(WRONG_ADDED_LETTER.wrongWord));
        assertThat(answer0.s, hasItem(WRONG_ADDED_LETTER.rightWord));
        assertThat(answer0.code, equalTo(ERROR_UNKNOWN_WORD.getCode()));

        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(1), hasSize(1));
        
        YandexSpellerAnswer answer1 = answers.get(1).get(0);
        
        assertThat(answer1.word, equalTo(WRONG_MISSED_LETTER.wrongWord));
        assertThat(answer1.s, hasItem(WRONG_MISSED_LETTER.rightWord));
        assertThat(answer1.code, equalTo(ERROR_UNKNOWN_WORD.getCode()));
    }

    @Test
    public void errorLengthTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .text(WRONG_ADDED_LETTER.text)
                        .callCheckTexts()
        );

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));
        assertThat(answers.get(0).get(0).len, equalTo(WRONG_ADDED_LETTER.wrongWord.length()));
    }

    @Test
    public void errorPositionTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .text(WRONG_MISSED_LETTER.text)
                        .callCheckTexts()
        );

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));
        assertThat(answers.get(0).get(0).pos,
                equalTo(WRONG_MISSED_LETTER.text.indexOf(WRONG_MISSED_LETTER.wrongWord)));
    }

    // 4 tests combined in one. see OptionTestData, method optionCheckProvider
    @Test(dataProvider = "optionCheckTest", dataProviderClass = OptionTestData.class)
    public void optionCheckTest(String text, String wrongWord, String rightWord,
                                Integer lenientOption, Integer strictOption, Integer expectedCode) {
        // A lenient option check, usually OP_IGNORE_... It should return an empty request.
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .options(lenientOption)
                        .text(text)
                        .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(0));

        // A strict option check, without IGNORE or with FIND_REPEATED_WORDS. It should find something!
        answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .options(strictOption)
                        .text(text)
                        .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));

        YandexSpellerAnswer answer = answers.get(0).get(0);

        assertThat(answer.word, equalTo(wrongWord));
        assertThat(answer.code,equalTo(expectedCode));
        if (!rightWord.isEmpty()) {
            assertThat(answer.s, hasItem(rightWord));
        }
    }

    @Test
    public void unknownWordTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .text(UNKNOWN_WORD.text)
                        .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));
        YandexSpellerAnswer answer = answers.get(0).get(0);
        assertThat  (answer.word, equalTo(UNKNOWN_WORD.text));
        assertThat(answer.code,equalTo(ERROR_UNKNOWN_WORD.getCode()));
    }

    @Test
    public void multilingualTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                .options(OP_NONE)
                .language(EN, RU, UK)
                .text(MULTILINGUAL_EN.text, MULTILINGUAL_RU.text, MULTILINGUAL_UK.text)
                .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(3));
        for (List<YandexSpellerAnswer> answer : answers) {
            assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answer, hasSize(1));
        }

        assertThat(answers.get(0).get(0).word, equalTo(MULTILINGUAL_EN.wrongWord));
        assertThat(answers.get(1).get(0).word, equalTo(MULTILINGUAL_RU.wrongWord));
        assertThat(answers.get(2).get(0).word, equalTo(MULTILINGUAL_UK.wrongWord));

        assertThat(answers.get(0).get(0).s, hasItem(MULTILINGUAL_EN.rightWord));
        assertThat(answers.get(1).get(0).s, hasItem(MULTILINGUAL_RU.rightWord));
        assertThat(answers.get(2).get(0).s, hasItem(MULTILINGUAL_UK.rightWord));
    }

    @Test(dataProvider = "optionCombinationTest", dataProviderClass = OptionTestData.class)
    public void optionCombinationTest(String text, String wrongWord, Integer options, Integer expectedCode) {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                .defaultAttrbutes()
                .text(text)
                .options(options)
                .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));
        YandexSpellerAnswer answer = answers.get(0).get(0);
        assertThat(answer.word, equalTo(wrongWord));
        assertThat(answer.code,equalTo(expectedCode));
    }

    @Test
    public void htmlFormatTest() {
        List<List<YandexSpellerAnswer>> answers = YandexSpellerApi.getYandexSpellerAnswers(
                YandexSpellerApi.with()
                        .defaultAttrbutes()
                        .text(HTML_FORMAT.text)
                        .format(HTML)
                        .callCheckTexts());

        assertThat(FAIL_RESPONSE_SIZE.getText(), answers, hasSize(1));
        assertThat(FAIL_INCORRECT_ERROR_NUMBER.getText(), answers.get(0), hasSize(1));

        YandexSpellerAnswer answer = answers.get(0).get(0);

        assertThat(answer.word, equalTo(HTML_FORMAT.wrongWord));
        assertThat(answer.s, hasItem(HTML_FORMAT.rightWord));
        assertThat(answer.code, equalTo(ERROR_UNKNOWN_WORD));
    }
}
