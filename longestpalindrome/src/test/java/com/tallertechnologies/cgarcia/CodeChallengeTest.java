package com.tallertechnologies.cgarcia;

import com.tallertechnologies.cgarcia.CodeChallenge;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodeChallengeTest {

    CodeChallenge codeChallenge = new CodeChallenge();
    @ParameterizedTest()
    @CsvSource(value = {
                "abac,aba",
                "tacag,aca",
                "wegeeksskeegyuwe,geeksskeeg",
                "withoutPalindrome,''",
                "'',''",
                " ,''",
                "null,''"},
            nullValues ={"null"}
    )
    void longestPalindromeInWord(String input, String result) throws ExecutionException, InterruptedException {
        assertEquals(result, codeChallenge.getLongestPalindrome(input).get());
    }
}
