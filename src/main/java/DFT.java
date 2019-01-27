public class DFT {
    static ComplexNumber[] dft(double[] data){

        int n = data.length;
        ComplexNumber[] ans = new ComplexNumber[(int)Math.ceil(n/2)];
        for(int i = 0; i<ans.length; i++){
            ans[i] = dft(data, i);
        }
        return ans;
    }

    static ComplexNumber dft(double[] data, int freq){
        double count = data.length;
        double sumreal = 0;
        double sumimag = 0;
        for(int index = 0; index<count;index++){
            double angle = ( 2 * Math.PI * index * freq)/count;
            sumreal += data[index] * Math.cos(angle);
            sumimag += data[index] * -Math.sin(angle);
        }
        return new ComplexNumber(sumreal, sumimag);
    }
}
