package scm;

import java.io.*;
import java.util.*;
//import org.apache.poi.*;
//import org.apache.poi.ss.usermodel.Cell;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

//import scm.SearchPath;

enum sID{
    /*--- 0:신분당 1:1호선 2:2호선 3:3호선 4:4호선 5:5호선 6:6호선 7:7호선 8:8호선 9:9호선 10:인천1 11:인천2 12:경의중앙 ---*/
    /*--- 13:경춘선 14:수인분당 15:공항 16:신림선 17:의정부 18:에버라인 19:경강선 20:우이신설 21:서해선 22:김포골드 ---*/
    L1, L2, L3, L4, L5, L6, L7, L8, L9,
    신분당, 인천1, 인천2, 경의중앙,경춘선,
    수인분당, 공항, 신림선, 의정부, 에버라인,
    경강선, 우이신설, 서해선, 김포골드;
}

class TranslationInterface{
    public String name;
    private int subwayId;
    private HashMap<String, Integer> connectedStation = new HashMap<>();
    private String first, second;
    private int fCost, sCost;
    public TranslationInterface(int subwayId, String first, String second, int fCost, int sCost){
        this.subwayId = subwayId;
        this.first = first;
        this.second = second;
        this.fCost = fCost;
        this.sCost = sCost;
        if (first != null) connectedStation.put(first, fCost);
        if (second != null) connectedStation.put(second, sCost);
    }

    public void printInfo(){
        System.out.print("호선:" + subwayId);
        System.out.print(" 상행:" + first + "," + fCost);
        System.out.print(" 하행:" + second + "," + sCost);
    }
}

class TmpStationInterface{
    private ArrayList<TranslationInterface> connectedStation = new ArrayList<>();
    private boolean[] subwayId = new boolean[23];
    private String stationName;

    public TmpStationInterface(int SubwayId, String stationName,
                             String first, String second, int fCost, int sCost){
        addLine(SubwayId, first, second, fCost, sCost);
        this.stationName = stationName;
    }

    public void addLine(int newLine, String first, String second, int fCost, int sCost){
        subwayId[newLine] = true;
        connectedStation.add(new TranslationInterface(newLine, first, second, fCost, sCost));
    }

    public String getName(){
        return stationName;
    }

    public void printInfo(){
        System.out.println("이름:" + stationName + " ");
        TranslationInterface t;
        int i;
        for (i = 0; i < connectedStation.size() - 1; i++){
            t = connectedStation.get(i);
            t.printInfo();
            System.out.print(" / ");
        }
        t = connectedStation.get(i);
        t.printInfo();
        System.out.println();
        System.out.println();
    }
}
/*
class StationInterface{
    private ArrayList<TranslationInterface> connectedStation = new ArrayList<>();
    private boolean[] subwayId = new boolean[23];
    private String stationName;

    public StationInterface(int SubwayId, String stationName,
                             String first, String second, int fCost, int sCost){
        addLine(SubwayId, first, second, fCost, sCost);
        this.stationName = stationName;
    }

    public void addLine(int newLine, String first, String second, int fCost, int sCost){
        subwayId[newLine] = true;
        connectedStation.add(new TranslationInterface(newLine, first, second, fCost, sCost));
    }

    public String getName(){
        return stationName;
    }

    public void printInfo(){
        System.out.println("이름:" + stationName + " ");
        TranslationInterface t;
        int i;
        for (i = 0; i < connectedStation.size() - 1; i++){
            t = connectedStation.get(i);
            t.printInfo();
            System.out.print(" / ");
        }
        t = connectedStation.get(i);
        t.printInfo();
        System.out.println();
        System.out.println();
    }
}
*/
public class Station{
    //private static HashMap<String, StationInterface> stations = new HashMap<>();
    private static HashMap<String, TmpStationInterface> tmpStations = new HashMap<>();

    public static void main(String[] args) throws IOException, CsvValidationException{
        /*----- csv filereader test -----*/
        /*--- absolute path of csv file ---*/
        String stationCsv = "C:\\Users\\Kimbeomjoon.DESKTOP-5T6FIPU\\Desktop\\SCM\\서울교통공사_1-8호선 역간거리 및 소요시간_20220111.csv";
        String[] line; // 연번/호선/역명/소요시간
        ArrayList<String[]> stations = new ArrayList<>();
        CSVReader reader = new CSVReader(new FileReader(stationCsv));
        line = reader.readNext();
        /*--- read csv file ---*/
        while((line = reader.readNext()) != null){
            String[] tmpList = new String[4];
            for (int i = 0; i <= 3; i++) tmpList[i] = line[i];
            stations.add(tmpList);
        }
        /*--- construct tmp db for stations ---*/
        int prevCost = 0;
        for (int i = 0; i < stations.size(); i++){
            int Id = Integer.parseInt(stations.get(i)[1]);

            String upsideStation = null;
            if (i != stations.size() - 1) upsideStation = stations.get(i + 1)[2];
            String downsideStation = null;
            if (i != 0) downsideStation = stations.get(i - 1)[2];

            String[] tmpCost = stations.get(i)[3].split(":");
            int upsideCost = Integer.parseInt(tmpCost[0]) * 60 + Integer.parseInt(tmpCost[1]);
            int downsideCost = prevCost;
            prevCost = upsideCost;

            if (tmpStations.get(stations.get(i)[2]) == null){
                String name = stations.get(i)[2];
                tmpStations.put(name, new TmpStationInterface(Id, name, upsideStation, 
                                downsideStation, upsideCost, downsideCost));
            }
            else{
                TmpStationInterface t = tmpStations.get(stations.get(i)[2]);
                t.addLine(Id, upsideStation, downsideStation, upsideCost, downsideCost);
            }
        }
        /*--- print information of stations ---*/
        for (int i = 0; i < stations.size(); i++){
            TmpStationInterface t = tmpStations.get(stations.get(i)[2]);
            t.printInfo();
        }
    }
}
