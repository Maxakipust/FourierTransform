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

    @Override
    public String toString() {
        return real+" + "+imaginary+"i";
    }
}
