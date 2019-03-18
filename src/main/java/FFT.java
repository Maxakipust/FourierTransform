import java.lang.reflect.Array;
import java.util.Arrays;

public class FFT {
    static private ComplexNumber[] fftHelper(double[] data){

        if(data.length == 1){
            return new ComplexNumber[]{new ComplexNumber(data[0],0)};
        }
        if(data.length%2!=0){
            throw new IllegalArgumentException("data.length is not a power of 2");
        }

        double[] evens = new double[data.length/2];
        for(int i = 0; i<data.length/2; i++){
            evens[i]=data[2*i];
        }
        ComplexNumber[] q = fftHelper(evens);
        double[] odds = new double[data.length/2];
        for(int i = 0; i<data.length/2; i++){
            odds[i]=data[2*i+1];
        }
        ComplexNumber[] r = fftHelper(odds);

        ComplexNumber[] y = new ComplexNumber[data.length];
        for(int i = 0; i<data.length/2; i++){
            double angle = -2*i*Math.PI / data.length;
            ComplexNumber wk = new ComplexNumber(Math.cos(angle), Math.sin(angle));
            y[i] = q[i].plus(wk.times(r[i]));
            y[i+data.length/2] = q[i].minus(wk.times(r[i]));
        }
        return y;
    }
    static public ComplexNumber[] fft(double[] data){
        ComplexNumber[] arr = fftHelper(data);
        return Arrays.copyOfRange(arr,0,data.length/2);
    }
}
