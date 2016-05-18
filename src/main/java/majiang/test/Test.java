package majiang.test;

public class Test {

    public static void main(String[] args) {
        Student s1 = new Student();
        s1.setAge(1);
        s1.setName("1");
        Student s2 = (Student) s1.clone();
        s1.setAge(2);
        System.out.println(s1.getAge());
    }

}
