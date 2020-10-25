package BeanHelloWorld;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainApp {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("BeanHelloWorld/Beans.xml");
        HelloWorld obj1 = (HelloWorld) context.getBean("an-id", HelloWorld.class);
        HelloWorld obj2 = (HelloWorld) context.getBean("an-id", HelloWorld.class);
        if (obj1 == obj2) {
            System.out.println("Same Bean ID get the SAME object");
        } else {
            System.out.println("Same Bean ID get DIFFERENT objects");
        }
        obj1.getMessage();
    }
}
