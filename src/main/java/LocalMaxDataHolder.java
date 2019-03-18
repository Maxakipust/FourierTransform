import java.awt.*;
import java.util.*;
import java.util.List;

public class LocalMaxDataHolder extends DataHolder {
    public LocalMaxDataHolder(String name, long sampleRate) {
        super(name, sampleRate);
    }

    @Override
    public DataPoint[] markKeyPoints(){
        List<DataPoint> ret = new ArrayList<DataPoint>();
        for(int x = 1; x<data.size()-1; x++){
            //System.out.println(x+"/"+ret.size());
            List<DataPoint> entryList = getEntryList(x);
            for(int y = 1; y<entryList.size()-1; y++){
                DataPoint point = entryList.get(y);
                DataPoint up = entryList.get(y+1);
                DataPoint down = entryList.get(y-1);
                DataPoint right = getEntryList(x+1).get(y);
                DataPoint left = getEntryList(x-1).get(y);
                double updif = up.getMagnitude()-down.getMagnitude();
                double rightdif = right.getMagnitude()-left.getMagnitude();
                double mag = Math.sqrt(Math.pow(updif,2)+Math.pow(rightdif,2));
                double angle = Math.atan(updif/rightdif);
                point.setColor(Color.getHSBColor((float)angle, (float)mag, 1));
            }
        }
        DataPoint[] retArr = new DataPoint[ret.size()];
        retArr = ret.toArray(retArr);
        return retArr;
    }

    private boolean isMax(Double x, Double n1, Double n2, Double n3, Double n4){
        double fuzz = 0.4;

        if(x-fuzz>=n1){
            if(x-fuzz>=n2){
                if(x-fuzz>=n3){
                    if(x-fuzz>=n4) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private List<DataPoint> getEntryList(int x){
        Set<Map.Entry<Integer,DataPoint>> set =  data.get(x).entrySet();
        List<DataPoint> entryList = new ArrayList<DataPoint>();
        for(Map.Entry<Integer,DataPoint> entry: set){
            entryList.add(entry.getValue());
        }
        Collections.sort(entryList, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                if(o1 instanceof DataPoint && o2 instanceof DataPoint){
                    DataPoint obj1 = (DataPoint)o1;
                    DataPoint obj2 = (DataPoint)o2;

                    if(obj1.getFrequency()>obj2.getFrequency()){
                        return 1;
                    }
                    if(obj1.getFrequency()<obj2.getFrequency()){
                        return -1;
                    }
                }
                return 0;
            }
        });
        return entryList;
    }
}
