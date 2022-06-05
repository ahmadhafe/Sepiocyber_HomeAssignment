import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.awt.*;
import java.io.*;

public class Utils {

    public WebDriver startChromeDriver()
    {
        return new ChromeDriver();
    }

    public long currentTimeMillis()
    {
        return System.currentTimeMillis();
    }

    /**
     * Method to wait in seconds
     *
     * @param timeToWaitInSeconds
     */
    public void waitAction(long timeToWaitInSeconds) {
        long start_time = System.currentTimeMillis();
        long wait_time = timeToWaitInSeconds * 1000;
        long end_time = start_time + wait_time;

        while (System.currentTimeMillis() < end_time)
            ;

    }

    public void waitActionInMillis(long timeToWaitInMillis)
    {
        long start_time = currentTimeMillis();
        long end_time = start_time + timeToWaitInMillis;

        while (currentTimeMillis() < end_time)
            ;
    }

    public boolean isTimeout(long originalTime, long timeoutInSeconds)
    {
        long wait_time = timeoutInSeconds * 1000;

        long end_time = originalTime + wait_time;

        return (currentTimeMillis() > end_time);
    }

    public String formatStringRtl(String text)
    {
        StringBuilder newText = new StringBuilder();
        for(char c : text.toCharArray())
        {
            if(!(c == '\u200E'))
                newText.append(c);
        }

        return newText.toString().trim();
    }

    public int countOccurrences(String text, char searchedChar)
    {
        return countOccurrences(text,searchedChar,0);
    }

    private int countOccurrences(String text, char searchedChar, int index) {
        if (index >= text.length()) {
            return 0;
        }

        int count = text.charAt(index) == searchedChar ? 1 : 0;
        return count + countOccurrences(text, searchedChar, index + 1);
    }

    public int parseInt(String number) {
        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e){
            return -1;
        }
    }

    public double parseDouble(String number) {
        if (number.contains(".")) {
            try {
                return Double.parseDouble(number);
            }
            catch (NumberFormatException errorParseDouble){
                return -1;
            }
        }

        try {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException errorParseInt){
            return -1;
        }
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public boolean compareRGBWithTolerance(Color expectedColor, Color actualColor, int tolerance)
    {
        if (expectedColor == null || actualColor == null) {
            // TODO: log error - can't compare null values
            return false;
        }

        int minR = Math.max(expectedColor.getRed() - tolerance, 0);
        int minG = Math.max(expectedColor.getGreen() - tolerance, 0);
        int minB = Math.max(expectedColor.getBlue() - tolerance, 0);
        int maxR = Math.min(expectedColor.getRed() + tolerance, 255);
        int maxG = Math.min(expectedColor.getGreen() + tolerance, 255);
        int maxB = Math.min(expectedColor.getBlue() + tolerance, 255);

        if ((actualColor.getRed() >= minR && actualColor.getRed() <= maxR)
            && (actualColor.getGreen() >= minG && actualColor.getGreen() <= maxG)
            && (actualColor.getBlue() >= minB && actualColor.getBlue() <= maxB)) {
            return true;
        }

        return false;
    }

    public boolean compareRGB(Color color1, Color color2) {
        if (color1 != null && color2 != null) {
            return color1.equals(color2);
        }

        // TODO: log error - can't compare null values
        return false;
    }

    public String getOnlyEnglishLettersFromString(String str) {
        return str.replaceAll("[^a-zA-Z]", "");
    }

    public String getOnlyNumbersFromString(String str)
    {
        return str.replaceAll("[^0-9\\.]","");
    }

    public double getOnlyDoubleFromString(String str) {
        String onlyDouble = getOnlyNumbersFromString(str);
        return parseDouble(onlyDouble);
    }

    public int getOnlyIntFromString(String str) {
        String onlyInt = getOnlyNumbersFromString(str);
        return parseInt(onlyInt);
    }

    public String getRandomStringFromArray(String[] array)
    {
        int max = array.length;
        return array[getRandomNumber(0,max)];
    }

    public String getRandomStringFromArray(String[] array, String textToExclude)
    {
        int max = array.length;
        String currentString = textToExclude;
        long startTime = System.currentTimeMillis();
        while (currentString.equals(textToExclude) && !isTimeout(startTime,3))
        {
            currentString = array[getRandomNumber(0,max)];
        }

        return currentString;

    }

    public boolean comparePositiveInt(int expectedNumber, int actualNumber) {
        return comparePositiveIntWithTolerance(expectedNumber, actualNumber, 0);
    }

    public boolean comparePositiveDouble(double expectedNumber, double actualNumber) {
        return comparePositiveDoubleWithTolerance(expectedNumber, actualNumber, 0);
    }

    public boolean comparePositiveIntWithTolerance(int expectedNumber,int actualNumber, int tolerance) {
        if (expectedNumber < 0 || actualNumber < 0) {
            return false;
        }

        int min = Math.max(expectedNumber - tolerance, 0);
        int max = expectedNumber + tolerance;

        return (actualNumber >= min && actualNumber <= max);
    }

    public boolean comparePositiveDoubleWithTolerance(double expectedNumber,double actualNumber, double tolerance) {
        if (expectedNumber < 0 || actualNumber < 0) {
            return false;
        }

        double min = Math.max(expectedNumber - tolerance, 0);
        double max = expectedNumber + tolerance;

        return (actualNumber >= min && actualNumber <= max);
    }

    //the weight is displayed cross app with 2 digits after the decimal point. the function align the converted number with app's rounding definition.
    public double convertKilogramToPound(double kilograms) {
        double weightInPound = kilograms * 2.2046;
        return Math.round(weightInPound * 100) / 100.0;
    }

    //the weight is displayed cross app with 2 digits after the decimal point. the function align the converted number with app's rounding definition.
    public double convertGramToOunce(double grams) {
        double weightInOunce = grams * 0.035274;
        return Math.round(weightInOunce * 100) / 100.0;
    }

    public String getCurrentPath()
    {
        return new File("").getAbsolutePath();
    }

    public boolean isEqualAndNotEmptyStrings(String str1, String str2) {
        return str1 != null && !str1.isEmpty() && str1.equals(str2);
    }

    public BufferedReader runShellCmd(String cmd) {
        try {

            System.out.println("Executing command: " + cmd);
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

            return reader;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String splitCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ),
                " "
        );
    }

    public String reverseWordsOrder(String phrase) {
        String words[]=phrase.split("\\s");
        StringBuilder reverseWord= new StringBuilder();
        for(int i = words.length-1;i>=0;i--){
            reverseWord.append(words[i]).append(" ");
        }
        return reverseWord.toString().trim();

    }

}
