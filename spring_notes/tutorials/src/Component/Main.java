package Component;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    
    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.scan("Component");
        ctx.refresh();

        int one = 1, two = 2, three;
        three = ctx.getBean(MathComponent.class).add(one, two);
        System.out.println(one + " + " + two + " = " + three);

        three = ((MathComponent) ctx.getBean("Math-comp")).add(one, two);
        System.out.println(one + " + " + two + " = " + three);

        ctx.close();
    }
}
