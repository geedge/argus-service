package com.nis.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SortList<T> {  
    /** 
     * @param targetList 目标排序List 
     * @param sortField 排序字段 
     * @param sortMode 排序方式 
     */  
    @SuppressWarnings({ "unchecked", "rawtypes" })  
    public void sort(List<T> targetList, final String sortField, final String sortMode) {  
        Collections.sort(targetList, new Comparator() {  
            public int compare(Object obj1, Object obj2) {   
                int retVal = 0;  
                try {  
                    Method method1 = ((T)obj1).getClass().getMethod(sortField, null);  
                    Method method2 = ((T)obj2).getClass().getMethod(sortField, null);  
                    if (sortMode != null && "desc".equals(sortMode)) {  
                        retVal = method2.invoke(((T) obj2), null).toString().compareTo(method1.invoke(((T) obj1), null).toString()); // 倒序  
                    } else {  
                        retVal = method1.invoke(((T) obj1), null).toString().compareTo(method2.invoke(((T) obj2), null).toString()); // 正序  
                    }  
                } catch (Exception e) {  
                    throw new RuntimeException();  
                }  
                return retVal;  
            }  
        });  
    }  
      
    /** 
     * 测试方法 
     * @param args 
     */  
    public static void main(String[] args) {  
        List<Person> targetList = new ArrayList<Person>();  
        targetList.add(new Person("zhangsan",11));  
        targetList.add(new Person("lisi",12));  
        targetList.add(new Person("wangwu",13));  
        System.out.println("排序前: " + targetList);  
          
        SortList<Person> sortList = new SortList<Person>();  
        sortList.sort(targetList, "getAge", "desc");  
          
        System.out.println("排序后：" +targetList);  
    }  
}  
  
class Person {  
    private String name;  
    private int age;  
  
    public Person() {  
  
    }  
  
    public Person(String name, int age) {  
        this.name = name;  
        this.age = age;  
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
      
    public String toString(){  
        return "name: " + this.name + ",age: " + this.age;  
    }  
}  