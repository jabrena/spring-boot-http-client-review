package info.perf;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class Problem {

    private final List<String> DATA_FOR_TESTING;

    public Problem(int sampleNumber) {
        DATA_FOR_TESTING = createData(sampleNumber);
    }

    private List<String> createData(int sampleNumber) {
        List<String> data = new ArrayList<>();
        for (int i = 0; i < sampleNumber; i++) {
            data.add("Number : " + i);
        }
        return data;
    }

    public void AtomicIntegerWay() {
        AtomicInteger counter = new AtomicInteger();
        String space = " ";
        DATA_FOR_TESTING.stream()
                .sorted()
                .map(beanName -> new StringBuilder()
                        .append(counter.incrementAndGet())
                        .append(space)
                        .append(beanName)
                        .toString())
                .count();
    }

    public void LongAdderWay() {
        LongAdder counter = new LongAdder();
        String space = " ";
        DATA_FOR_TESTING.stream()
                .sorted()
                .map(beanName -> {
                    counter.increment();
                    return new StringBuilder()
                            .append(counter.intValue())
                            .append(space)
                            .append(beanName)
                            .toString();
                })
                .count();
    }
}
