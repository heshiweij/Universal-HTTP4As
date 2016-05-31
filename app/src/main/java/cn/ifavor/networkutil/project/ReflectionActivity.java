package cn.ifavor.networkutil.project;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-05-30
 * @Time: 11:49
 * @des ${TODO}
 */
public class ReflectionActivity {

    public static void main(String[] args) {
        // 使用常规方法调用
        /*Person p = new Person();
		p.setAge(20);
		p.setName("hsw");
		System.out.println(p.toString());
		System.out.println(p.getName());
		System.out.println(p.getAge());*/


        // 使用反射方法调用
        Class<Person> clazz = Person.class;

        try {
            // 创建对象
            Person p = clazz.newInstance();

            // 访问成员变量
            //			Field f1 = clazz.getField("name"); // 只能访问 public
            //			f1.set(p, "hsw");

            //			Field f1 = clazz.getDeclaredField("name"); // 可以访问 private
            //			f1.setAccessible(true);
            //			f1.set(p, "hsw");

            // 访问成员方法
            //			Method m1 = clazz.getMethod("show"); // 没有参数，可以不指定形参  // 只能访问 public
            //			String cnt = (String) m1.invoke(p); // 没有参数，可以不传递参数
            //			System.out.println(cnt);

            //			Method m1 = clazz.getDeclaredMethod("show"); // 没有参数，可以不指定形参的类型  // 只能访问 public
            //			m1.setAccessible(true);
            //			String cnt = (String) m1.invoke(p);  // 没有参数，可以不传递参数 // 可以访问 private
            //			System.out.println(cnt);

            //			Method m1 = clazz.getMethod("display", String.class); // 有参数，需要指定形参的类型  // 只能访问 public
            //			String cnt = (String) m1.invoke(p, "CHN"); // 没有参数，可以不传递参数
            //			System.out.println(cnt);

            Method m1 = clazz.getDeclaredMethod("display", String.class); // 没有参数，可以不指定形参  // 只能访问 public
            m1.setAccessible(true);
            String cnt = (String) m1.invoke(p, "AMA");  // 没有参数，可以不传递参数 // 可以访问 private
            System.out.println(cnt); // 没有返回值，obj == null


            System.out.println(p.toString());
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Throwable e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
}

class Person {
    public String name;
    private int age;

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

    private String show() {
        String cnt = "我是中国人";
        return cnt;
    }

    private void display(String nation) {
        System.out.println("I am " + nation);
    }

    public String toString() {
        return "Person [name=" + name + ", age=" + age + "]";
    }









}
