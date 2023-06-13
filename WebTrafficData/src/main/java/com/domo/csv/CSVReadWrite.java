package com.domo.csv;

import com.domo.csv.POJO.User;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

public class CSVReadWrite {

    private static final String SAMPLE_CSV_FILE_PATH = "src\\main\\resources\\TrafficData.csv";
    //
    static Set<String> setOfData = new HashSet<>();
    static HashMap<String, Integer> finalOut = new HashMap<>();

    public static void main(String[] args) throws IOException {
        //List of user object
        List<User> userData = new ArrayList<>();

        try (
                //Read CSV file
                Reader reader = Files.newBufferedReader(Paths.get(SAMPLE_CSV_FILE_PATH));
        ) {
            //Convert CSV to User Object
            CsvToBean<User> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(User.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            for (User csvUser : (Iterable<User>) csvToBean) {
                //add data of each line of csv in the user object
                userData.add(csvUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Sort the user data on the basis of timestamp
        userData.sort(Comparator.comparing(User::getTimestamp));
        //Store user history of visited pages
        HashMap<String, List<String>> historyMap = new HashMap<>();
        Iterator it = userData.iterator();
        List<String> addNewitem = new ArrayList<>();
        List<String> mapExistingItem = new ArrayList<>();

        while (it.hasNext()) {
            User getUser = (User) it.next();
            if (historyMap.containsKey(getUser.getSession_id())) {
                mapExistingItem = historyMap.get(getUser.getSession_id());
                mapExistingItem.add(getUser.getPage_id());
                historyMap.put(getUser.getSession_id(), mapExistingItem);
            } else {
                addNewitem.add(getUser.getPage_id());
                historyMap.put(getUser.getSession_id(), addNewitem);
            }
            addNewitem = new ArrayList<>();

        }
        //Find the possible combinations of pages visited by each user
        findCombinations(historyMap);

    }

    private static void findCombinations(HashMap<String, List<String>> historyMap) {
        for (Map.Entry<String, List<String>> entry : historyMap.entrySet()) {
            //get each record from map and find the possible combinations
            String[] array = entry.getValue().toArray(new String[0]);
            int k = 3; // value of required tep sequence
            combination(array, k);

        }
        TreeMap<String, Integer> sorted = new TreeMap<>();
        sorted.putAll(finalOut);
        for (Map.Entry<String, Integer> entry : sorted.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public static void combination(Object[] elements, int K) {
        //get length of the array
        int lengthOfList = elements.length;
        //Invalid input
        if (K > lengthOfList) {
            return;
        }
        // init combination index array
        int combination[] = new int[K];
        int r = 0; // index for combination array
        int i = 0; // index for elements array
        //Count of occurrence of each combination
        int count = 0;

        while (r >= 0) {
            // forward step if i < (N + (r-K))
            if (i <= (lengthOfList + (r - K))) {
                combination[r] = i;
                // if combination array is full print and increment i;
                if (r == K - 1) {
                    String output = "";
                    //get the combine pages visited by each user
                    for (int z = 0; z < combination.length; z++) {
                        output += elements[combination[z]] + " ";
                    }
                    //add the output string in set for unique values
                    if (i == 0 || i == combination.length - 1) {
                        setOfData.add(output);

                    }
                    //Add only that data to map which present in set and the count of occurrence
                    if (setOfData.contains(output)) {
                        if (finalOut.containsKey(output)) {
                            finalOut.put(output, finalOut.get(output) + 1);
                        } else {
                            finalOut.put(output, count + 1);
                        }
                    }
                    i++;
                } else {
                    // if combination is not full yet, select next element
                    i = combination[r] + 1;
                    r++;
                }
            }
            // backward step
            else {
                r--;
                if (r >= 0)
                    i = combination[r] + 1;
            }
        }

    }

}
