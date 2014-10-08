package modles;

import interfaces.MemberInfo;

/**
 * Created by csun on 10/6/14.
 */
public class Person {
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

    private String name;
    private int age;

    public MemberInfo getMember() {
        return member;
    }

    public void setMember(MemberInfo memberInfo) {
        this.member = memberInfo;
    }

    private MemberInfo member;

    public  Person(){}
    public Person(String name){
        this.name = name;
    }

    public Person(int age, String name){
        this.name = name;
        this.age = age;
    }

    public Person(int age, String name, MemberInfo memberInfo){
        this.name = name;
        this.age = age;
        member = memberInfo;
    }
   public  static void main(String[] args){
       System.out.println(Person.class.getName());
   }
}
