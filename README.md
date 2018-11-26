For HW2, see SoapYandexTest/ folder. It includes the soapUI project file (Open Source, groovy-based version)

# Test cases

The list of cases is not the same as the SOAP UI one, because some test cases can be implemented as a single test case with multiple data sets.

All those test cases run the checkTexts method for REST. For SOAP all cases run checkText, except for multilingualTest. Some cases send multiple queries.

## validateResponseBody (REST only)
We check that the response body has the valid structure as described in specifications.

## simpleRightWordTest
We're sending a correct English word and expecting an "empty" response structure.

## simpleWrongWordTest
We're sending an English word with an orphographic error end expect the response to return:
* this word 
* a correct suggestion on fixing that error (one of suggestions should be as expected)
* the error code that should be 1 (ERROR_UNKNOWN_WORD)

## errorLengthTest
We're sending an English word with an orphographic error end expect the response to return the length of the error present.

## errorPositionTest
The same as errorLengthTest, but now we expect the position of the word that has the error.

## unknownWordTest
We're sending a combination of letters in various languages and expecting the response to have this "word". No suggestion is needed, just an error code 1.

**BUG (REST)**: the response is empty, like there were no errors. This bug doesn't appear for a SOAP request.

## optionCheckTest (REST) / repeatedWordTest, digitModeTest, urlModeTest, capitalizationTest (SOAP)
We test each test data twice, with two options:
* a "lenient" option that would skip the error. We expect the "empty" response here.
* a "strict" option that would spot a word with error and return the word and its error code.

For SOAP tests we check each data in a dedicated test (repeatedWordTest, digitModeTest, urlModeTest, capitalizationTest), because it would be hard to spot an error in SOAP UI if we mix all the tests together in one.

For REST tests it's possible to use a single test for each data set:

|№| text (expectedWrongWord is in bold)            |expectedRightWord| options (lenient)   | options (strict)        |expectedErrorCode    |
|---|-----------------------------------------------|-----------------|---------------------|-------------------------|---------------------|
|1 |There **should** should be a mistake somewhere.|-                  |(none)               |FIND_REPEAT_WORDS        |ERROR_REPEATED_WORD  |
|2 |Don't do that, **LeeroyJenkins1992**!          |Leeroy Jenkins 1992|IGNORE_DIGITS        |(none)                   |ERROR_UNKNOWN_WORD   |
|3 |Click Here -> https://**totallynotfishing**.io/|-                  |IGNORE_URLS          |(none)                   |ERROR_UNKNOWN_WORD   |
|4 |**sHOUTING** is not permitted                  |shouting           |IGNORE_CAPITALIZATION|(none)                   |ERROR_CAPITALIZATION |

***BUG (SOAP)***
* urlModeTest (data №3) returns the empty response even a "strict" option, i.e. no error found.

***BUGS (REST)***
* Data №1 and №3 returns the empty response even with a "strict" option, i.e. no error found.
* Data №4 returns incorrect suggestions (no "shouting" in the list). The SOAP response had "shouting".

## multilingualTest
The idea of this test is that Speller should recognize errors in all supported languages correctly.

To make the test easy to read and write, we assume that there is only one error per text and we check only the first one. We also assume that we check orpho errors and don't expect a specific error code (it's always 1 by the nature of the test data), but rather expect the right suggestions. 

We send all texts in a single CheckResponses request with `lang="en,ru,uk"`.

| text (expectedWrongWord is in bold)|expectedRightWord  |
|------------------------------------|-------------------|
|Как **проити** в библиотеку?        |пройти             |
|Have a **nies** day!                |nice               |
|Я цього не **разумію**              |розумію            |

## optionCombinationTest

The test checks how `options` work when combined. We use the same text for all test data, but we change options.
The text is made that there should be an error for each combination of options tested.

`text=emily (emi38s) has has found a bug at https://yandex.ru`

|№ | options       | expectedWrongWord | expectedErrorCode    |
|---|---------------|-------------------|----------------------|
|1  |IGNORE_DIGITS, IGNORE_URLS                                          |emily              |ERROR_CAPITALIZATION  |
|2  |IGNORE_DIGITS, IGNORE_CAPITALIZATION                                |ru (from yandex.ru)|ERROR_UNKNOWN_WORD    |
|3  |IGNORE_URLS, IGNORE_CAPITALIZATION                                  |emi38s             |ERROR_UNKNOWN_WORD    |
|4  |IGNORE_URLS, IGNORE_DIGITS, FIND_REPEAT_WORDS, IGNORE_CAPITALIZATION|has                |ERROR_REPEATED_WORD   |

**A lot of bugs! (REST)** Basically all test data failed for REST because all the responses were empty.

To check if the tests weren't broken, I entered the same data in the [web form](https://speller.yandex.net/services/spellservice?op=checkTexts) that Yandex offers. All of the data returned empty response bodies.

## htmlFormatTest

We set the attribute `format="html"` and try it on a text that has HTML tags.

`text=<p><a href="http://en.wikipedia.org">Wikipedia</a> is an <b>online enciclopediae</b></p>`

This example has only one error in the word enciclopediae, so assume `expectedErrorCount=1`. The result shouln't have any tag parts and should have exactly one error.

***BUGS!***
* SOAP: The response only has an empty `<SpellResult/>` tag in SOAP UI. Apparently it can't parse tags.
* REST: The response shows an error in the world Wikipedia that shouldn't be here:


    [
        [
            {
                "code": 1,
                "pos": 37,
                "row": 0,
                "col": 37,
                "len": 9,
                "word": "Wikipedia",
                "s": [
                    ",Wikipedia"
                ]
            },
            {
                "code": 1,
                "pos": 67,
                "row": 0,
                "col": 67,
                "len": 13,
                "word": "enciclopediae",
                "s": [
                    "encyclopedia",
                    "encyclopedia e",
                    "encyclopedias",
                    "encyclopaedia"
                ]
            }
        ]
    ]
