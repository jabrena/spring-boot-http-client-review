package info.perf;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

public class BenchmarkLoop {

    @State(Scope.Benchmark)
    public static class St{
        int n = 10000000;
        Problem problem = new Problem(n);
    }

    @Benchmark
    public void longAdder(St st) {
        st.problem.LongAdderWay();
    }

    @Benchmark
    public void atomicInteger(St st) {
        st.problem.AtomicIntegerWay();
    }
}
