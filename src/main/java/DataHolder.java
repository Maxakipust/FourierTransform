import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.awt.color.*;

public class DataHolder {
    String name;
    long sampleRate;
    List<HashMap<Integer,DataPoint>> data = new LinkedList<HashMap<Integer, DataPoint>>();
    double minMag;
    double maxMag;
    double minFreq;
    double maxFreq;

    public DataHolder(String name, long sampleRate) {
        this.name = name;
        this.sampleRate = sampleRate;

    }

    public void addTime(){
        data.add(new HashMap<Integer, DataPoint>());
    }

    public void addData(int time, DataPoint dataPoint){
        maxFreq = dataPoint.getFrequency()>maxFreq?dataPoint.getFrequency():maxFreq;
        minFreq = dataPoint.getFrequency()<minFreq?dataPoint.getFrequency():minFreq;
        maxMag = dataPoint.getMagnitude()>maxMag?dataPoint.getMagnitude():maxMag;
        minMag = dataPoint.getMagnitude()<minMag?dataPoint.getMagnitude():minMag;

        data.get(time).put(dataPoint.getFrequency(), dataPoint);
    }

    public void addData(DataPoint dataPoint){
        addData(data.size()-1,dataPoint);
    }

    public DataPoint getData(int time, int frequency){
        return data.get(time).get(frequency);
    }

    public List<DataPoint> getDataPoints(int time){
        return new ArrayList(data.get(time).values());
    }

    public List<DataPoint> getDataPoints(){
        List<DataPoint> results = new LinkedList<DataPoint>();
        for(int i = 0; i<data.size(); i++){
            results.addAll(getDataPoints(i));
        }
        return results;
    }

    public BufferedImage drawImage(int width, int height, int freqHeight, double magHeight){
        BufferedImage img = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        for(int i = 0; i < data.size(); i++){
            HashMap<Integer,DataPoint> dataMap = data.get(i);
            for(Map.Entry<Integer,DataPoint> entry: dataMap.entrySet()){
                double x = mapNumber(i,0,data.size()-1,0,width);
                double y = mapNumber(entry.getKey(),0, freqHeight, 0, height);
                //double mag = mapNumber(entry.getValue().getMagnitude(), 0, magHeight, 0,1);
                if(y<height && x<width) {
                    try {
                        img.setRGB((int) x, (int) ((height-1)-y), entry.getValue().getColor(magHeight).getRGB());
                    } catch (Exception ex) {
                        System.out.println("out of bounds");
                    }
                }
            }
        }
        return img;
    }

    public BufferedImage drawImage(int height, int freqHeight){
        return this.drawImage(data.size()-1,height,freqHeight, maxMag);
    }

    private Color getHSVColor(double mag, DataPoint dataPoint){

        return Color.getHSBColor((float)(0.4-mag),1,1);
    }


    private double mapNumber(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public DataPoint[] markKeyPoints(){
        return null;
    }

}
