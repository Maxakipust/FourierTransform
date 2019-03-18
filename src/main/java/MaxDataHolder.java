import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MaxDataHolder extends DataHolder {
    public MaxDataHolder(String name, long sampleRate) {
        super(name, sampleRate);
    }

    @Override
    public DataPoint[] markKeyPoints(){
        List<DataPoint> ret = new ArrayList<DataPoint>();
        for(HashMap<Integer, DataPoint> map: data){
            DataPoint max = null;
            for(Map.Entry<Integer,DataPoint> data:  map.entrySet()){
                if(max == null) max = data.getValue();
                if(max.getMagnitude()<data.getValue().getMagnitude()) max = data.getValue();
            }
            max.setKeyPoint(true);
            ret.add(max);
        }
        DataPoint[] retArr = new DataPoint[ret.size()];
        retArr = ret.toArray(retArr);
        return retArr;
    }
}
