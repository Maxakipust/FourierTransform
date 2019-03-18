public class ComplexNumber {
    private double real;
    private double imaginary;
    public ComplexNumber(double real, double imaginary){
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImaginary() {
        return imaginary;
    }

    public void setImaginary(double imaginary) {
        this.imaginary = imaginary;
    }

    public double getMagnitude() {
        return Math.sqrt(real * real + imaginary * imaginary);
    }

    public double getMagnitudeSquared() { return real*real + imaginary*imaginary; }

    public ComplexNumber plus(ComplexNumber number){
        return new ComplexNumber(this.real+number.real, this.imaginary+number.imaginary);
    }

    public ComplexNumber minus(ComplexNumber number){
        return new ComplexNumber(this.real-number.imaginary, this.imaginary-number.imaginary);
    }

    public ComplexNumber times(ComplexNumber number){
        if(number==null){
            return new ComplexNumber(0,0);
        }
        Double real_part = this.real*number.real - this.imaginary*number.imaginary;
        Double imag_part = this.real*number.imaginary+this.imaginary*number.real;
        return new ComplexNumber(real_part,imag_part);
    }

    @Override
    public String toString() {
        return real+" + "+imaginary+"i";
    }
}
