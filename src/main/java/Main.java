import WavFile.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.List;

public class Main{
    /**
     * gets the color to display for a given power
     * @param power the magnitude of the sound at a frequency
     * @return a color to display
     */
    public static Color getColor(double power) {
        double H = power * 0.4;
        double S = 1.0;
        double B = 1.0;

        return Color.getHSBColor((float)H, (float)S, (float)B);
    }

    /**
     * gets the monocromatic color for a given power
     * @param power the magnitude of the sound at a frequency
     * @return a color to display
     */
    public static Color getMonoChrome(double power){
        if(power>=1){
            System.out.println(power);
            power = 1;
            //return new Color(255,0,0);
        }
        int color = (int)(power*255);

        return new Color(color,color,color);
    }

    /**
     * read a WAV file and convert it into a WAV file object
     * @param path the path to the file
     * @return the WavFile object
     */
    private static WavFile readWAV(String path){
        try {
            WavFile wavFile = WavFile.openWavFile(new File(path));
            return wavFile;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WavFileException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * The main entry point of the program
     * @param args the input file, the output image, the output text
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        //read in the wav file
        WavFile file = readWAV(args[0]);
        file.display();

        //define constants
        int sampleWidth = 256;
        int framesRead;

        double minMag = Double.MAX_VALUE;
        double maxMag = Double.MIN_VALUE;

        DataHolder holder = new LocalMaxDataHolder(args[0],file.getSampleRate());
        //loop through all of the frames
        do {
            //read the frames
            double[] sample = new double[sampleWidth];
            framesRead = file.readFrames(sample, sampleWidth);
            //preform the dft transform
            ComplexNumber[] sampleTransform = FFT.fft(sample);

            double[] res = new double[sampleTransform.length];

            holder.addTime();
            //loop through the result of the transform
            for(int i = 0; i< sampleTransform.length; i++){
                int index = res.length-i-1;
                //calculate the magnitude
                double mag = sampleTransform[i].getMagnitude();

                //save it
                res[index] = mag;

                minMag = mag<minMag?mag:minMag;
                maxMag = mag>maxMag?mag:maxMag;
                if(mag!=0) {
                    //System.out.println(mag);
                }
                int freq = (int)((double)i * file.getSampleRate())/sampleWidth;
                //System.out.println("i: "+i+", freq: "+freq);
                holder.addData(new DataPoint(freq, mag, i, file.getSampleRate()));
            }
        } while (framesRead != 0);
        //close the files
        file.close();
        System.out.println("marking key points");
        holder.markKeyPoints();
        System.out.println("done marking key points");

        BufferedImage image = holder.drawImage(200,20000);

        //save the image
        File outputfile = new File(args[1]);
        ImageIO.write(image, "png", outputfile);
        System.out.println("File saved");


    }

    static void createScale(int start, int end, int height, BufferedImage image, int buffer) throws IOException {

        double delta = ((double)end-start)/height;
        for(int y = 0; y<height; y+=1){
            double mag = (delta*y)+start;
            System.out.println(mag);
            int x = image.getWidth()-buffer;
            Color c = getMonoChrome(mag);
            image.setRGB(x,height-1-y,c.getRGB());
        }
    }

    static double sum(Collection<Double> arr){
        double ans = 0;
        for(double d: arr){
            ans+= d;
        }
        return ans;
    }

    static double sumSqu(Collection<Double> arr){
        double ans = 0;
        for(double d: arr){
            ans+= d* d;
        }
        return ans;
    }

    static double average(Collection<Double> arr){
        return sum(arr)/arr.size();
    }

    static double stdDev(Collection<Double> arr){
        double numerator = sumSqu(arr) - (Math.pow(sum(arr),2)/arr.size());
        return Math.sqrt(numerator/(arr.size()-1));
    }

    static double mapNumber(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}