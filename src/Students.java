/*
Yagel Gross - 322450842 - יגל גרוס
Tal Aronson - 212676977 - טל ארונסון
 */

import java.io.*;
import java.util.*;

public class Students {
    private int[] gradesArray;

    public int[] insertGrades(String fileName) {
        List<Integer> gradesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] grades = line.split(",");
                if (grades.length != 4) continue;

                int encodedGrade = 0;
                for (int i = 0; i < 4; i++) {
                    int grade = Integer.parseInt(grades[i]);
                    encodedGrade |= (grade & 0xFF) << (i * 8);
                }
                gradesList.add(encodedGrade);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        gradesArray = new int[gradesList.size()];
        for (int i = 0; i < gradesList.size(); i++) {
            gradesArray[i] = gradesList.get(i);
        }

        return gradesArray;
    }

    public void displayHex() {
        for (int grade : gradesArray) {
            System.out.printf("%08X", grade);
        }
        System.out.println();
    }

    public int getStudentExam(int i, int j) {
        if (i < 1 || i > gradesArray.length || j < 1 || j > 4) {
            throw new IllegalArgumentException("Invalid student or exam index");
        }
        return (gradesArray[i - 1] >> ((j - 1) * 8)) & 0xFF;
    }

    public void setStudentExam(int i, int j, int k) {
        if (i < 1 || i > gradesArray.length || j < 1 || j > 4 || k < 0 || k > 100) {
            throw new IllegalArgumentException("Invalid input values");
        }
        gradesArray[i - 1] &= ~(0xFF << ((j - 1) * 8));
        gradesArray[i - 1] |= (k & 0xFF) << ((j - 1) * 8);
    }

    public float averageStudent(int i) {
        if (i < 1 || i > gradesArray.length) {
            throw new IllegalArgumentException("Invalid student index");
        }
        int sum = 0;
        for (int j = 0; j < 4; j++) {
            sum += (gradesArray[i - 1] >> (j * 8)) & 0xFF;
        }
        return sum / 4.0f;
    }

    public float averageExam(int j) {
        if (j < 1 || j > 4) {
            throw new IllegalArgumentException("Invalid exam index");
        }
        int sum = 0;
        for (int i = 0; i < gradesArray.length; i++) {
            sum += (gradesArray[i] >> ((j - 1) * 8)) & 0xFF;
        }
        return sum / (float) gradesArray.length;
    }

}