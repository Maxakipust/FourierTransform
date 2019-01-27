import WavFile.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main{
    public static Color getColor(double power) {
        double H = power * 0.4; // Hue (note 0.4 = Green, see huge chart below)
        double S = 1.0; // Saturation
        double B = 1.0; // Brightness

        return Color.getHSBColor((float)H, (float)S, (float)B);
    }

    public static Color getMonoChrome(double power){
        int color = (int)(power*255);

        return new Color(color,color,color);
    }

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
    public static void main(String[] args) {
        WavFile file = readWAV(args[0]);
        file.display();
        int sampleWidth = 255;

        int framesRead;
        List<double[]> results = new ArrayList<double[]>();
        double[] sample = new double[sampleWidth];
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        try {
            do {
                framesRead = file.readFrames(sample, sampleWidth);
                ComplexNumber[] sampleTransform = DFT.dft(sample);
                double[] res = new double[sampleTransform.length];
                for(int i = 0; i< sampleTransform.length; i++){
                    double mag = sampleTransform[i].getMagnitudeSquared();
                    mag = 10*Math.log(Math.max(mag,1.0));
                    res[res.length-i-1] = mag;
                    if(mag>max) max = mag;
                    if(mag<min) min = mag;
                }
                results.add(res);
            } while (framesRead != 0);
            file.close();

            double magRange = max-min;
            int width = results.size();
            int height = results.get(0).length;

            BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
            for(int x = 0; x<width;x++){
                for(int y = 0; y<height;y++){
                    double result = results.get(x)[y];
                    double mag = (result - min)/magRange;
                    image.setRGB(x,y,getMonoChrome(1-mag).getRGB());
                }
            }
            File outputfile = new File(args[1]);
            ImageIO.write(image, "png", outputfile);
            System.out.println("File saved");

            System.out.println("Frequency min: 0" );
            System.out.println("Frequency max: " + file.getSampleRate()/2);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        /*
        double[] data = new double[]{1.000000, 0.616019, -0.074742, -0.867709, -1.513756, -1.814072, -1.695685, -1.238285, -0.641981, -0.148568, 0.052986, -0.099981, -0.519991, -1.004504, -1.316210, -1.277204, -0.840320, -0.109751, 0.697148, 1.332076, 1.610114, 1.479484, 1.039674, 0.500934, 0.100986, 0.011428, 0.270337, 0.767317, 1.286847, 1.593006, 1.522570, 1.050172, 0.300089, -0.500000, -1.105360, -1.347092, -1.195502, -0.769329, -0.287350, 0.018736, -0.003863, -0.368315, -0.942240, -1.498921, -1.805718, -1.715243, -1.223769, -0.474092, 0.298324, 0.855015, 1.045127, 0.861789, 0.442361, 0.012549, -0.203743, -0.073667, 0.391081, 1.037403, 1.629420, 1.939760, 1.838000, 1.341801, 0.610829, -0.114220, -0.603767, -0.726857, -0.500000, -0.078413, 0.306847, 0.441288, 0.212848, -0.342305, -1.051947, -1.673286, -1.986306, -1.878657, -1.389067, -0.692377, -0.032016, 0.373796, 0.415623, 0.133682, -0.299863, -0.650208, -0.713739, -0.399757, 0.231814, 0.991509, 1.632070, 1.942987, 1.831075, 1.355754, 0.705338, 0.123579, -0.184921, -0.133598, 0.213573, 0.668583, 0.994522, 1.000000};
        double samplingRate = 1;
        for(int i = 0; i<=data.length/2;i++){
            ComplexNumber result = DFT.dft(data,i);
            System.out.print("Freq: "+ (i * samplingRate)/data.length);
            System.out.print(", Real: "+ result.getReal());
            System.out.print(", Imaginary: "+ result.getImaginary());
            System.out.println(", Magnitude: "+ result.getMagnitude());
        }
        */
    }
}