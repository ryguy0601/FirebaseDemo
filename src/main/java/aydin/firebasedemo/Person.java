package aydin.firebasedemo;

public class Person {
    private String name;
    private int age;
    private String num;

    public Person(String name, int age, String num) {
        this.name = name;
        this.age = age;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String num() {
        return num;
    }

    public void num(String num) {
        this.num = num;
    }
}