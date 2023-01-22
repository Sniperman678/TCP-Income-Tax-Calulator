// Pravin Cherian - Fernandez
//c3306899
//SENG4500
package pravin;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RangeGroup {

    public List<Range> ranges;

    RangeGroup() {
        ranges = new ArrayList<>();
    }

    public List<Range> addRange(Range newRange) {
        List<Range> newList = new ArrayList<>();
        if(ranges.size() == 0){
            newList.add(newRange);
            return newList;
        }

        boolean merged = false;
        for (int index = 0; index < ranges.size(); index++) {
            Range old = ranges.get(index);
            if(!isOverLapping(old, newRange)) {
                // No overlap with old one.
                newList.add(old);
            } else {
                if(old.start < newRange.start && old.end < newRange.end) {
                    System.out.println("second half of old range is intersecting");
                    newList.add(new Range(old.start, newRange.start - 1, old.baseTax, old.tax));
                    newList.add(newRange);
                    merged = true;
                } else if(old.start > newRange.start &&  old.end > newRange.end) {
                    System.out.println("first half of old is intersecting");
                    newList.add(new Range(newRange.end + 1, old.end, old.baseTax, old.tax));
                    merged = true;
                } else if(old.start > newRange.start && old.end < newRange.end) {
                    System.out.println("old is eaten completely by new");
                    newList.add(newRange);
                    merged = true;
                } else if(old.start < newRange.start && old.end > newRange.end) {
                    System.out.println("old range eats new one completely");
                    newList.add(new Range(old.start, newRange.start - 1, old.baseTax, old.tax));
                    newList.add(new Range(newRange.start, newRange.end, newRange.baseTax, newRange.tax));
                    newList.add(new Range(newRange.end+1, old.end, old.baseTax, old.tax));
                    merged = true;
                }
            }
        }
        if(!merged) {
            System.out.println("Nothing is matching");
            newList.add(newRange);
        }
        newList.sort(Comparator.comparingInt(a -> a.start));
        return newList;
    }


    private boolean isOverLapping(Range a, Range b) {
        return !((a.start > b.end) || (b.start > a.end));
    }

    //Tax calculation

}
