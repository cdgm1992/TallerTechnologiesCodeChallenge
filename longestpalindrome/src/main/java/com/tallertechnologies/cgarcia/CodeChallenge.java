package com.tallertechnologies.cgarcia;

import org.apache.commons.lang3.StringUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CodeChallenge  {
    private final Predicate<String> isPalindrome = (String input) ->
            Optional.ofNullable(input).filter(word -> word.length() > 1).map(word -> {
                String reversedWord = new StringBuilder(input).reverse().toString();
                return input.equals(reversedWord);
            }).orElse(false);

    private List<String> getPossiblePalindromesForCharacter(Character character, final String word, List<String> result) {
        int firstIndexCharacter = word.indexOf(character);
        int lastIndexCharacter = word.lastIndexOf(character);
        if (firstIndexCharacter == lastIndexCharacter){
            return result;
        }
        String possiblePalindrome = word.substring(firstIndexCharacter, lastIndexCharacter + 1);
        result.add(possiblePalindrome);
        return getPossiblePalindromesForCharacter(character, possiblePalindrome.substring(0, possiblePalindrome.length() - 1), result);
    }

    private final Function<String, List<Character>> getListOfChars = word ->
            Optional.ofNullable(word)
                    .map(existingWord ->
                            existingWord.chars()
                                    .mapToObj(c -> (char) c)
                                    .collect(Collectors.toList()))
                    .orElseGet(ArrayList::new);
    private final Function<String, List<String>> getAllPalindromes = word -> {
        List<Character> charactersList = getListOfChars.apply(word);
        List<String> palindromes = new ArrayList<>();
        charactersList.parallelStream().forEach(
                character -> getPossiblePalindromesForCharacter(character, word, new ArrayList<String>())
                        .parallelStream().filter(isPalindrome)
                        .forEach(palindromes::add));
        return palindromes;
    };

    private final Comparator<String> byLengthReversed = (str1, str2) -> str2.length() - str1.length();

    private final BiFunction<List<String>, Comparator<String>, String> getFirstOrderedElement = (palindromeWords, comparator) -> {
        if (!palindromeWords.isEmpty()) {
            palindromeWords.sort(comparator);
            return palindromeWords.get(0);
        }
        return "";
    };

    private final Function<List<String>, String> getTheLongestPalindrome = palindromeWords ->
            getFirstOrderedElement.apply(palindromeWords, byLengthReversed);

    public CompletableFuture<String> getLongestPalindrome(final String word) {
        if (StringUtils.isEmpty(word) || StringUtils.isEmpty(word.trim())){
            return CompletableFuture.supplyAsync(() -> "");
        }
        return CompletableFuture.supplyAsync(() -> word)
                .thenApply(getAllPalindromes)
                .thenApply(getTheLongestPalindrome)
                .exceptionally(e -> "");

    }
}