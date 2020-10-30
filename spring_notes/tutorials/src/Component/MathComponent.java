package Component;

import org.springframework.stereotype.Component;

@Component("Math-comp")
public class MathComponent {
    public int add(int x, int y) {
        return x + y;
    }
}
