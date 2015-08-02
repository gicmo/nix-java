import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class RunStimObject {
    double[] time;
    double[] membrane_voltage;
    double[] spike_voltage;

    public RunStimObject(double[] time, double[] membrane_voltage, double[] spike_voltage) {
        this.time = time;
        this.membrane_voltage = membrane_voltage;
        this.spike_voltage = spike_voltage;
    }
}

public class LIF {
    private double stepsize;
    private double offset;
    private double tau_m;
    private double tau_a;
    private double da;
    private double D;
    private double v_threshold;
    private double v_reset;
    private double i_a;
    private double v;
    private double t;
    private List<Double> membrane_voltage;
    private List<Double> spike_times;

    public LIF() {
        this(0.0001, 1.6, 0.025, 0.02, 0.0, 3.5);
    }

    public LIF(double stepsize, double offset, double tau_m, double tau_a, double da, double D) {
        this.stepsize = stepsize;
        this.offset = offset;
        this.tau_m = tau_m;
        this.tau_a = tau_a;
        this.da = da;
        this.D = D;
        this.v_threshold = 1.0;
        this.v_reset = 0.0;
        this.i_a = 0.0;
        this.v = this.v_reset;
        this.t = 0.0;
        this.membrane_voltage = new ArrayList<>();
        this.spike_times = new ArrayList<>();
    }

    private void reset() {
        this.i_a = 0.0;
        this.v = this.v_reset;
        this.t = 0.0;
        this.membrane_voltage = new ArrayList<>();
        this.spike_times = new ArrayList<>();
    }

    private void lif(double stimulus, double noise) {
        this.i_a -= this.i_a - this.stepsize / this.tau_a * (this.i_a);
        this.v += this.stepsize * (-this.v + stimulus + noise + this.offset - this.i_a) / this.tau_m;
        this.membrane_voltage.add(this.v);
    }

    private Random rand = new Random();

    private void next(double stimulus) {
        double r = rand.nextDouble();
        double noise = this.D * ((r % 10000) - 5000.0) / 10000;
        if (rand.nextBoolean()) noise = -noise;

        this.lif(stimulus, noise);
        this.t += this.stepsize;
        if (this.v > this.v_threshold && (this.membrane_voltage).size() > 1) {
            this.v = this.v_reset;
            this.membrane_voltage.set(this.membrane_voltage.size() - 1, 2.0);
            this.spike_times.add(this.t);
            this.i_a += this.da;
        }
    }


    public RunStimObject runConstStim(int steps, double stimulus) {
        this.reset();
        for (int i = 0; i < steps; i++) {
            next(stimulus);
        }
        double[] time = new double[this.membrane_voltage.size()];
        for (int i = 0; i < time.length; i++) {
            time[i] = i * this.stepsize;
        }

        double[] mv = this.membrane_voltage.stream().mapToDouble(d -> d).toArray();
        double[] st = this.spike_times.stream().mapToDouble(d -> d).toArray();
        return new RunStimObject(time, mv, st);
    }

    public RunStimObject runStim(double[] stimulus) {
        this.reset();
        for (double s : stimulus) {
            next(s);
        }
        double[] time = new double[this.membrane_voltage.size()];
        for (int i = 0; i < time.length; i++) {
            time[i] = i * this.stepsize;
        }

        double[] mv = this.membrane_voltage.stream().mapToDouble(d -> d).toArray();
        double[] st = this.spike_times.stream().mapToDouble(d -> d).toArray();
        return new RunStimObject(time, mv, st);
    }

    public String toString() {
        return "[" + "" +
                "stepsize: \t" + this.stepsize +
                "offset:\t\t" + this.offset +
                "tau_m:\t\t" + this.tau_m +
                "tau_a:\t\t" + this.tau_a +
                "da:\t\t" + this.da +
                "D:\t\t" + this.D +
                "v_threshold:\t" + this.v_threshold +
                "v_reset:\t" + this.v_reset +
                "]";
    }
}


