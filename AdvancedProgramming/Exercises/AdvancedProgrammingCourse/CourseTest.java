package AdvancedProgramming.Exercises.AdvancedProgrammingCourse;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class InvalidActivityException extends Exception
{
    public InvalidActivityException() {
    }
}
class Student
{

    String id;
    String name;
    int firstTermPoints;
    int secondTermPoints;
    int labPoints;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public double sumPoints()
    {
        //midterm1 * 0.45 + midterm2 * 0.45 + labs.
        return firstTermPoints*0.45+secondTermPoints*0.45+labPoints;

    }
    public int grade()
    {
        double sum=sumPoints();
      if(sum>90 && sum<=100)
          return 10;
      else if(sum>80 && sum<=90)
          return 9;
     else if(sum>70 && sum<=80)
          return 8;
     else if(sum>60 && sum<=70)
          return 7;
     else if(sum>50 && sum<=60)
          return 6;
      else
          return 5;
    }

    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d",id,name,
                firstTermPoints,secondTermPoints,labPoints,sumPoints(),grade());
    }
}
class AdvancedProgrammingCourse
{

    Map<String,Student> students;
    Map<Integer,Integer> numStudentsByGrade;

    public AdvancedProgrammingCourse() {
        students=new HashMap<>();
        numStudentsByGrade=new HashMap<>();

        IntStream.range(5,11).forEach(i->
                {
                     numStudentsByGrade.put(i,0);
                }
        );
    }
    public void addStudent (Student s)
    {
        students.put(s.id,s);

    }
    public void updateStudent (String idNumber, String activity, int points) throws InvalidActivityException {
        Student student=students.get(idNumber);
        if(activity.equals("midterm1"))
        {
            student.firstTermPoints=points;
        }
        else
            if(activity.equals("midterm2"))
            {
                student.secondTermPoints=points;
            }
            else if(activity.equals("labs"))
            {
                student.labPoints=points;
            }

    }
    public List<Student> getFirstNStudents (int n)
    {
        Comparator<Student> comparator=Comparator.comparing(Student::sumPoints).reversed();
       return students.values().stream().sorted(comparator).limit(n).collect(Collectors.toList());
    }

    public void printStatistics() {
       DoubleSummaryStatistics summary=students.values().stream().filter(student -> student.grade()!=5).mapToDouble(Student::sumPoints).summaryStatistics();
        System.out.println(String.format("Count: %d Min: %.2f Average: %.2f Max: %.2f",summary.getCount(),summary.getMin(),summary.getAverage(),summary.getMax()));
    }
   public Map<Integer, Integer> getGradeDistribution() {
        students.values().stream().forEach(student ->
        {
            int value=numStudentsByGrade.get(student.grade());
            value++;
            numStudentsByGrade.put(student.grade(),value);
        });
        return numStudentsByGrade;

    }

}
public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                try {
                    advancedProgrammingCourse.updateStudent(idNumber, activity, points);
                } catch (InvalidActivityException e) {
                    throw new RuntimeException(e);
                }
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}
