import java.awt.*;

public class DataPoint {
    private int frequency;
    private double magnitude;
    private double time;
    private int timeIndex;
    private boolean keyPoint = false;
    private Color color = null;

    public DataPoint(int frequency, double magnitude, int timeIndex, long sampleRate){
        this.frequency = frequency;
        this.magnitude = magnitude;
        this.timeIndex = timeIndex;
        this.time = timeIndex/sampleRate;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public double getTimeIndex() {
        return timeIndex;
    }

    public void setTimeIndex(int timeIndex) {
        this.timeIndex = timeIndex;
    }

    public boolean isKeyPoint() {
        return keyPoint;
    }

    public void setKeyPoint(boolean keyPoint) {
        this.keyPoint = keyPoint;
    }

    public Color getColor(double max){
        double mag = mapNumber(magnitude, 0, max, 0, 1);
        if(color==null){
            if(keyPoint){
                return new Color((int)(255),0,0);
            }
            return new Color((int)(mag*255),(int)(mag*255),(int)(mag*255));
        }
        return color;
    }

    public void setColor(Color c){
        color = c;
    }

    private double mapNumber(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
