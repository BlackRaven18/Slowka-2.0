package com.arek.database_utils;

import com.arek.language_learning_app.TranslationOrder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class DatabaseQueryManager {


    private static String databaseURL = DatabaseInfoManager.SPANISH_POLISH_DATABASE_URL;

    private static final String GET_WORDS_WITH_TRANSLATIONS ="SELECT sl.slowo, tl.tlumaczenie " +
            "FROM SLOWO sl, TLUMACZENIE tl " +
            "WHERE sl.id_slowa = tl.id_slowa " +
            "ORDER BY sl.slowo;";

    private static final String GET_WORDS_WITH_TRANSLATIONS_REVERSE = "SELECT tl.tlumaczenie, sl.slowo\n" +
            "FROM slowo sl, tlumaczenie tl\n" +
            "WHERE sl.id_slowa = tl.id_slowa;\n";


    public static QueryResult getQueryResult(String query){
        QueryResult queryResult = new QueryResult();
        ArrayList<String> line;

        try(Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query)){

            ResultSetMetaData rsmd = resultSet.getMetaData();

            while (resultSet.next()) {
                line = new ArrayList<>();

                for(int i = 1; i <= rsmd.getColumnCount(); i++){
                    line.add(resultSet.getString(i));
                }

                queryResult.addNewQueryLine(line);
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.err.println("query RESULT ERROR");
        }

        return queryResult;
    }

    public static void executeQuery(String query){
        try (Connection connection = DriverManager.getConnection(databaseURL);
             Statement statement = connection.createStatement()){
            statement.executeUpdate(query);

        }catch (SQLException e){
            e.printStackTrace();
            System.err.println("EXECUTE query ERROR");
        }
    }

    public static ArrayList<Word> getWords(){
        String query = "SELECT * FROM SLOWO";
        QueryResult queryResult = getQueryResult(query);

        ArrayList<Word> wordList = new ArrayList<>();
        for(int i = 0; i < queryResult.getQueryLines(); i++){
            ArrayList<String> line = queryResult.getQueryLine(i);
            Word word = new Word(Integer.parseInt(line.get(0)), line.get(1));
            wordList.add(word);
        }

        return wordList;
    }

    public static ArrayList<Translation> getTranslations(){
        String query = "SELECT * FROM TLUMACZENIE";
        QueryResult queryResult = getQueryResult(query);

        ArrayList<Translation> translationList = new ArrayList<>();
        for(int i = 0; i < queryResult.getQueryLines(); i++){
            ArrayList<String> line = queryResult.getQueryLine(i);
            Translation translation = new Translation(Integer.parseInt(line.get(0)), line.get(1), Integer.parseInt(line.get(2)));
            translationList.add(translation);
        }
        return translationList;
    }

    public static int getNewWordId(){
        String query = "SELECT MAX(ROWID)FROM SLOWO;";
        QueryResult queryResult = getQueryResult(query);

        if(queryResult.getQueryLine(0).get(0) == null){
            return 0;
        }

        return Integer.parseInt(queryResult.getQueryLine(0).get(0));
    }

    public static int getNewTranslationId(){
        String query = "SELECT MAX(ROWID) FROM TLUMACZENIE;";
        QueryResult queryResult = getQueryResult(query);

        if(queryResult.getQueryLine(0).get(0) == null){
            return 0;
        }

        return Integer.parseInt(queryResult.getQueryLine(0).get(0));
    }

    public static HashMap<String, ArrayList<String>> getWordsAndTranslations(TranslationOrder order){
        HashMap<String, ArrayList<String>> wordsAndTranslations = new HashMap<>();
        String query;
        QueryResult queryResult;

        if(order == TranslationOrder.NORMAL){
            query = GET_WORDS_WITH_TRANSLATIONS;
        } else {
            query = GET_WORDS_WITH_TRANSLATIONS_REVERSE;
        }

        queryResult = getQueryResult(query);
        for(int i = 0; i < queryResult.getQueryLines(); i++){
            ArrayList<String> translationList;

            if(wordsAndTranslations.containsKey(queryResult.getQueryLine(i).get(0))){
                translationList = wordsAndTranslations.get(queryResult.getQueryLine(i).get(0));
            }else {
                translationList = new ArrayList<>();
            }

            translationList.add(queryResult.getQueryLine(i).get(1));
            wordsAndTranslations.put(queryResult.getQueryLine(i).get(0), translationList);


        }
        return wordsAndTranslations;
    }

    public static ArrayList<WordDetails> getWordDetailsAsList(){
        ArrayList<WordDetails> wordDetailsList = new ArrayList<>();
        QueryResult queryResult;

        String query = "SELECT s.slowo, tl.tlumaczenie, typ.typ, kat.kategoria " +
                "FROM SLOWO s, TLUMACZENIE tl, TYP typ, KATEGORIA kat " +
                "WHERE s.id_slowa = tl.id_slowa " +
                "    AND s.id_typu = typ.id_typu" +
                "    AND s.id_kategorii = kat.id_kategorii;";

        queryResult = getQueryResult(query);
        for(int i = 0; i < queryResult.getQueryLines(); i++){
            String word = queryResult.getQueryLine(i).get(0);
            String translation = queryResult.getQueryLine(i).get(1);
            String type = queryResult.getQueryLine(i).get(2);
            String category = queryResult.getQueryLine(i).get(3);

            wordDetailsList.add(new WordDetails(word, translation, type, category));
        }
        return wordDetailsList;
    }

    public static DatabaseResponse addWordDetails(WordDetails wordDetails){

        int newWordId;

        //if word is in database
        if(isWordInDatabase(wordDetails.getWord())){
            newWordId = getWordId(wordDetails.getWord());

            //if translation is in database
            if(isTranslationInDatabase(newWordId, wordDetails.getTranslation())){
                return DatabaseResponse.DB_ALREADY_IN;
            }
            // adding new translations to the existing word
            addNewTranslation(newWordId, wordDetails.getTranslation());

        } else {
            //adding new word with translation
            addNewWord(wordDetails);
            newWordId = getWordId(wordDetails.getWord());
            addNewTranslation(newWordId, wordDetails.getTranslation());
        }

        return DatabaseResponse.DB_OK;
    }

    private static void addNewWord(WordDetails wordDetails){
        int newWordId = getNewWordId();
        int typeId = getTypeId(wordDetails.getType());
        int categoryId = getCategoryId(wordDetails.getCategory());

        String query = String.format("INSERT INTO SLOWO VALUES(%d, '%s', %d, %d);",
                newWordId, wordDetails.getWord(), typeId, categoryId);

        executeQuery(query);
    }

    private static void addNewTranslation(int wordId, String translation){
        int newTranslationId =getNewTranslationId();
        String query = String.format("INSERT INTO TLUMACZENIE VALUES(%d, '%s', %d);", newTranslationId, translation, wordId);

        executeQuery(query);
    }

    public static DatabaseResponse deleteWordDetails(WordDetails wordDetails){
        WordDetailsRowNumbers wordDetailsRowNumbers = getWordDetailsRowsNumbers(wordDetails);

        if(wordDetailsRowNumbers == null){
            return DatabaseResponse.DB_NOT_FOUND;
        }

        String deleteTranslationQuery = String.format("DELETE FROM TLUMACZENIE WHERE ROWID = %d;", wordDetailsRowNumbers.gettranslationRowNumber());
        String deleteWordQuery = String.format("DELETE FROM SLOWO WHERE ROWID = %d;", wordDetailsRowNumbers.getWordRowNumber());

        int numberOfWordTranslations = getWordNumberOfTranslations(wordDetails.getWord());

        executeQuery(deleteTranslationQuery);
        if(numberOfWordTranslations == 1){
            executeQuery(deleteWordQuery);
        }

        return DatabaseResponse.DB_OK;
    }

    public static DatabaseResponse changeWordDetails(WordDetails oldWordDetails, WordDetails newWordDetails) {
        WordDetailsRowNumbers wordDetailsRowNumbers = getWordDetailsRowsNumbers(oldWordDetails);

        if(wordDetailsRowNumbers == null){
            return DatabaseResponse.DB_NOT_FOUND;
        }

        int newTypeId = getTypeId(newWordDetails.getType());
        int newCategoryId = getCategoryId(newWordDetails.getCategory());

        //zapytania zwracające id typu i id kategorii o danej nazwie

        String changeWordQuery = String.format("UPDATE SLOWO SET slowo = '%s' WHERE ROWID = %d;",
                newWordDetails.getWord(), wordDetailsRowNumbers.getWordRowNumber());

        String changeTypeQuery = String.format("UPDATE SLOWO SET id_typu = %d WHERE ROWID = %d;",
                newTypeId, wordDetailsRowNumbers.getWordRowNumber());
        String changeCategoryQuery = String.format("UPDATE SLOWO SET id_kategorii = %d WHERE ROWID = %d;",
                newCategoryId, wordDetailsRowNumbers.getWordRowNumber());

        String changeTranslationQuery = String.format("UPDATE TLUMACZENIE SET tlumaczenie = '%s' WHERE ROWID = %d;",
                newWordDetails.getTranslation(), wordDetailsRowNumbers.gettranslationRowNumber());

        executeQuery(changeWordQuery);
        executeQuery(changeTypeQuery);
        executeQuery(changeCategoryQuery);
        executeQuery(changeTranslationQuery);


        return DatabaseResponse.DB_OK;
    }

    public static int getWordNumberOfTranslations(String word){

        String query = String.format("SELECT COUNT(*)" +
                " FROM SLOWO sl, TLUMACZENIE tl" +
                " WHERE sl.id_slowa = tl.id_slowa" +
                " AND sl.slowo = '%s';", word);

        QueryResult queryResult = getQueryResult(query);
        return Integer.parseInt(queryResult.getQueryLine(0).get(0));
    }

    public static WordDetailsRowNumbers getWordDetailsRowsNumbers(WordDetails wordDetails){
        String query = String.format("SELECT sl.ROWID, tl.ROWID " +
                "FROM SLOWO sl, TLUMACZENIE tl " +
                "WHERE sl.id_slowa = tl.id_slowa " +
                "AND sl.slowo = '%s' " +
                "AND tl.tlumaczenie = '%s';",
                wordDetails.getWord(), wordDetails.getTranslation());

        QueryResult queryResult = getQueryResult(query);

        if(queryResult.isEmpty()) {
            return null;
        }

        return new WordDetailsRowNumbers(Integer.parseInt(queryResult.getQueryLine(0).get(0)),
                Integer.parseInt(queryResult.getQueryLine(0).get(1)));
    }

    public static int getTypeId(String type){
        String query = String.format("SELECT id_typu FROM TYP WHERE typ = \"%s\";", type);
        QueryResult queryResult = getQueryResult(query);

        if(!queryResult.isEmpty()){
            return Integer.parseInt(queryResult.getQueryLine(0).get(0));
        } else {
            return -1;
        }
    }

    public static int getCategoryId(String category){
        String query = String.format("SELECT id_kategorii FROM KATEGORIA WHERE kategoria = \"%s\";", category);
        QueryResult queryResult = getQueryResult(query);

        if(!queryResult.isEmpty()){
            return Integer.parseInt(queryResult.getQueryLine(0).get(0));
        } else {
            return -1;
        }
    }


    private static int getWordId(String word){
        String query = String.format("SELECT id_slowa FROM SLOWO WHERE slowo = '%s';", word);
        QueryResult queryResult = getQueryResult(query);

        if(!queryResult.isEmpty()){
            return Integer.parseInt(queryResult.getQueryLine(0).get(0));
        } else {
            return -1;
        }
    }

    private static int getTranslationId(int wordId, String translation){
        String query = String.format("SELECT id_tlumaczenia FROM TLUMACZENIE WHERE id_slowa = %d AND tlumaczenie = '%s';", wordId, translation);
        QueryResult queryResult = getQueryResult(query);

        if(!queryResult.isEmpty()){
            return Integer.parseInt(queryResult.getQueryLine(0).get(0));
        } else {
            return -1;
        }
    }

    private static boolean isWordInDatabase(String word){
        return getWordId(word) != -1;
    }

    private static boolean isTranslationInDatabase(int wordId, String translation){
        return getTranslationId(wordId, translation) != -1;
    }

    public static void clearDatabase(){
        clearTranslationTable();
        clearWordTable();
    }

    private static void clearTranslationTable(){
        String query = "DELETE FROM TLUMACZENIE";

        executeQuery(query);
    }

    private static void clearWordTable(){
        String query = "DELETE FROM SLOWO";

        executeQuery(query);
    }

    public static void addWordsToWordTable(ArrayList<Word> wordList){
        String query;

        try(Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement()){

            for(Word word : wordList){
                query = String.format("INSERT INTO SLOWO VALUES (%d, '%s');", word.getWordID(), word.getWord());
                statement.executeUpdate(query);
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.err.println("ADD WORDS TO WORD TABLE ERROR");
        }
    }


    public static void addTranslationsToTranslationTable(ArrayList<Translation> translationList){
        String query;

        try(Connection connection = DriverManager.getConnection(databaseURL);
            Statement statement = connection.createStatement()){

            for(Translation translation : translationList){
                query = String.format("INSERT INTO TLUMACZENIE VALUES (%d, '%s', %d);", translation.getTranslationID(), translation.getTranslation(), translation.getWordID());
                statement.executeUpdate(query);
            }

        }catch (SQLException e){
            e.printStackTrace();
            System.err.println("ADD TRANSLATION TO TRANSLATION TABLE ERROR");
        }
    }



    public static void changeToSpanish(){
        databaseURL = DatabaseInfoManager.SPANISH_POLISH_DATABASE_URL;
    }
    public static void changeToEnglish(){
        databaseURL = DatabaseInfoManager.ENGLISH_POLISH_DATABASE_URL;
    }
    public static void changeToOtherDatabase(String URL){
        databaseURL = URL;
    }
}
