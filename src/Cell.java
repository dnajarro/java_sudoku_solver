import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cell {
    static final int NO_VALUE = Integer.MIN_VALUE;

    public List<Integer> domain = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    public int value = NO_VALUE;

    public Cell() {

    }

    public void assignValue(int value) {
        domain = new ArrayList<>(Arrays.asList(value));
        this.value = value;
    }

    public void setDomain(List<Integer> domain) {
        this.domain.addAll(domain);
        value = NO_VALUE;
    }

    public boolean removeValue(int value) {
        if (domain.contains(value)) {
            domain.remove(Integer.valueOf(value));
        }
        return !domain.isEmpty();
    }

    public void copyCell(Cell other) {
        List<Integer> newDomain = new ArrayList<>(other.domain);
        this.domain = newDomain;
        this.value = other.value;
    }
}
