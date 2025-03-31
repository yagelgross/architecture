/*
Yagel Gross - 322450842 - יגל גרוס
Tal Aronson - 212676977 - טל ארונסון
 */

import java.io.*;
import java.util.*;

public class Studentsa {
    private int[] grades;

    public int[] insertGrades(String fileName) {
        List<Integer> gradesList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] gradeStrings = line.split(",");
                if (gradeStrings.length != 4) continue;

                int encodedGrade = 0;
                for (int i = 0; i < 4; i++) {
                    int grade = Integer.parseInt(gradeStrings[i]);
                    if (grade < 0 || grade > 100) throw new IllegalArgumentException("Grade must be between 0 and 100.");
                    encodedGrade |= (grade & 0xFF) << (i * 8);
                }
                gradesList.add(encodedGrade);
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }

        grades = gradesList.stream().mapToInt(i -> i).toArray();
        return grades;
    }

    public void displayHex() {
        StringBuilder hexOutput = new StringBuilder();
        for (int grade : grades) {
            hexOutput.append(String.format("%08X ", grade));
        }
        System.out.println(hexOutput.toString());
    }

    public int getStudentExam(int i, int j) {
        if (i < 0 || i >= grades.length || j < 0 || j > 3) {
            throw new IllegalArgumentException("Invalid student or exam index.");
        }
        int grade = grades[i];
        return (grade >> (8 * (3 - j))) & 0xFF; // הוצאת הציון המתאים בעזרת הזזת ביטים
    }

    public void setStudentExam(int i, int j, int k) {
        if (k < 0 || k > 100) throw new IllegalArgumentException("Grade must be between 0 and 100.");
        if (i < 0 || i >= grades.length || j < 0 || j > 3) {
            throw new IllegalArgumentException("Invalid student or exam index.");
        }

        grades[i] = (grades[i] & ~(0xFF << (8 * (3 - j)))) | ((k & 0xFF) << (8 * (3 - j)));
    }

    public float averageStudent(int i) {
        if (i < 0 || i >= grades.length) throw new IllegalArgumentException("Invalid student index.");
        float avg = 0;
        int grade = grades[i];
        for (int m = 0; m < 4; m++) {
            avg += (grade >> (8 * (3 - m))) & 0xFF;
        }
        return avg / 4;
    }

    public float averageExam(int j) {
        if (grades.length == 0) return 0;
        if (j < 0 || j > 3) throw new IllegalArgumentException("Invalid exam index.");
        float avg = 0;
        for (int i = 0; i < grades.length; i++) {
            avg += getStudentExam(i, j);
        }
        return avg / grades.length;
    }
}