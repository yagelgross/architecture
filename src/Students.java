import java.io.*;
import java.util.*;

public class Students {
    private int[] grades;

    public int[] insertGrades(String fileName) {
        List gradesList = new ArrayList<>();
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
            gradesList.add(Integer.valueOf(encodedGrade));
        }
        } catch (IOException e) {
            e.printStackTrace();
        }

        int[] gradesArray = new int[gradesList.size()];
        for (int i = 0; i < gradesList.size(); i++) {
            gradesArray[i] = (int) gradesList.get(i);
        }
        grades = Arrays.copyOf(gradesArray, gradesArray.length);
        return gradesArray;
    }

    public void displayHex() {
        StringBuilder hexOutput = new StringBuilder();
        for (int grade : grades) {
            hexOutput.append(String.format("%08X", grade));
        }
        System.out.println(hexOutput.toString());
    }

    public int getStudentExam(int i, int j) {
        String s = String.format("%08x", grades[i]);
        int o = j * 2;
        if (o - 2 < 0 || o - 1 >= s.length()) {
            throw new IllegalArgumentException("Invalid indices for substring extraction.");
        }
        String extracted = s.substring(o - 2, o - 1);
        return Integer.parseInt(extracted, 16);
    }

    public void setStudentExam(int i, int j, int k) {
        String s = String.format("%08x", grades[i]);
        String q = String.format("%02x", k); // Ensures k is represented as two hex digits
        for (int m = 0; m < 2; m++) {
            s = s.substring(0, j * 2 - 2 + m) + q.charAt(m) + s.substring(j * 2 - 2 + m + 1);
        }
        grades[i] = Integer.parseInt(s, 16);
    }

    public float averageStudent(int i) {
        float avg = 0;
        String s = String.format("%08x", grades[i]);
        for (int m = 0; m < 8; m += 2) {
            avg += Integer.parseInt(s.substring(m, m+2), 16);
        }
        return avg / 4;
    }

    public float averageExam(int j) {
        if (grades.length == 0) return 0;
        float avg = 0;
        for (int i = 0; i < grades.length; i++) {
            avg += getStudentExam(i, j);
        }
        return avg / grades.length;
    }
}