package scm;

import java.io.UnsupportedEncodingException;
import org.json.simple.parser.ParseException;

public class Test {
    public static void main(String[] args) throws ParseException, UnsupportedEncodingException{
        System.out.println("go");

        CoordConvert f = new CoordConvert("김포시_고촌읍_상미로"); //sk api에서 지번반영 한 좌표 안나옴1.
        String[] coord = f.coordReturn();
        System.out.println(coord[0]+" "+coord[1]);

        FindStation z = new FindStation(coord);  //카카오 지하철 최근접 찾는게 아마 직선거리 같다.
        z.printNearStation();

        System.out.println();

        StationInfo a1 = new StationInfo("대흥(서강대앞)"); //
        a1.stationInfoSearch();
        

    }
    
}
