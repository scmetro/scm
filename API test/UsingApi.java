package scm;

import java.io.IOException;

import okhttp3.*;

import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;


class CoordConvert {
    private String address;
    private String city_do;
    private String gu_gun;
    private String dong;
    private String bunji;
    private String detailAddress;
    private static String addressFlag = "FO2";
    private static String SKAppkey = "PQWsjnch4uaJdimlvEfXc8A3s3EZmZXj1uSaRM31";

    CoordConvert(String address){
        this.address = address;
        String teamAddress[] = this.address.split("_");
        city_do = teamAddress[0];
        gu_gun = teamAddress[1];
        dong = teamAddress[2];
        if (teamAddress.length >= 4) bunji= teamAddress[3];
        if (teamAddress.length >= 5) detailAddress= teamAddress[4];
    }

    String[] coordReturn() throws ParseException, UnsupportedEncodingException{
        String url = "https://apis.openapi.sk.com/tmap/geo/geocoding?version=1&"+
        "&city_do="+URLEncoder.encode(city_do,"utf-8")
        +"&gu_gun="+URLEncoder.encode(gu_gun,"utf-8")
        +"&dong="+URLEncoder.encode(dong,"utf-8")
        +"&addressFlag="+"F02"+"&coordType=WGS84GEO";
      
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"appKey\":\""+SKAppkey+"\"}");
        Request request = new Request.Builder()
        .url(url)
        .get()
        .addHeader("accept", "application/json")
        .addHeader("content-type", "application/json")
        .addHeader("appKey", "PQWsjnch4uaJdimlvEfXc8A3s3EZmZXj1uSaRM31")
        .build();
        
        String[] coord = {"NaN", "NaN"};

        try{
            Response response = client.newCall(request).execute();


            System.out.println(response);

        
            
            String departString = response.body().string();
            
            JSONParser parser = new JSONParser();
            JSONObject departJson = (JSONObject) parser.parse(departString);
            System.out.println(departJson);

            JSONObject coordinateInfo = (JSONObject)departJson.get("coordinateInfo");
            String newLat = (String)coordinateInfo.get("newLat");
            String newLon = (String)coordinateInfo.get("newLon");


            System.out.println(newLat);
            System.out.println(newLon);

            coord[0] = newLat;
            coord[1] = newLon;

            //System.out.println(response.body().string());
            
        }catch(IOException e){
            System.out.println("null");
            
        }
        return coord;
    }
}

class FindStation {
    
    private String address;
    private static String kakaoApiKey = "ec84f2e571c4e74f56d082d2ec3532fe";
    private String x;
    private String y;
    private String nearStation;
    private String[] stations = new String[5];

    FindStation(String[] coords){
        y= coords[0];
        x= coords[1];
    }
    
    String nearStationRetrun() throws UnsupportedEncodingException{
        
        String url =  "https://dapi.kakao.com/v2/local/search/category.json?category_group_code=SW8&x="+x+"&y="+y+"&radius=20000";
    
        OkHttpClient client = new OkHttpClient();
    
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"query\":서강대}");
        Request request = new Request.Builder()
          .url(url)
          .post(body)
          .addHeader("Authorization", "KakaoAK "+kakaoApiKey)
          .build();
    
        try{
            Response response = client.newCall(request).execute();
            nearStation = response.body().string();
            
        }catch(IOException e){
            nearStation = "null";
        }
    
        return nearStation;

    }

    void printNearStation() throws ParseException, UnsupportedEncodingException{
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject)jsonParser.parse(nearStationRetrun());
        JSONArray documents = (JSONArray)jsonObject.get("documents");

        for (int i=0; i<5; i++){
            JSONObject nearStationInfo = (JSONObject)documents.get(i);
            String staName = (String)nearStationInfo.get("place_name");
            String distance = (String)nearStationInfo.get("distance");
            String x = (String)nearStationInfo.get("x");
            String y = (String)nearStationInfo.get("y");
            System.out.println(staName + " " + distance+ " " + x+" "+ y);
            stations[i] = staName;
        }

    }
}

//long beforeTime = System.currentTimeMillis();
// long afterTime = System.currentTimeMillis();
//long secDiffTime = (afterTime - beforeTime);

class StationInfo {
    private static String StationKey = "737242654939383033314b58586c4a";
    private static String type = "json";
    String stationName;

    int	subwayId;	// 지하철호선ID
    String updnLine;	//상하행선구분 (2호선 : (내선:0,외선:1),상행,하행)
    String trainLineNm;	//도착지방면 (성수행 - 구로디지털단지방면)
    String subwayHeading; //내리는문방향(오른쪽,왼쪽)
    int statnFid;	//이전지하철역ID
    int statnTid;	//다음지하철역ID
    int statnId;	//지하철역ID
    String statnNm;	//지하철역명
    int trnsitCo;	//환승노선수
    int ordkey;	//도착예정열차순번 (상하행코드(1자리), 순번(첫번째, 두번째 열차 , 1자리), 첫번째 도착예정 정류장 - 현재 정류장(3자리), 목적지 정류장, 급행여부(1자리))
    int subwayList;	//연계호선ID (1002, 1007 등 연계대상 호상ID)
    int statnList;	//연계지하철역ID(1002000233,1007000744)
    String btrainSttus;	//열차종류(급행,ITX)
    String barvlDt;	//열차도착예정시간(단위:초)
    String btrainNo;	//열차번호 (현재운행하고 있는 호선별 열차번호)
    int bstatnId;	//종착지하철역ID
    String bstatnNm;	//종착지하철역명
    String recptnDt;	//열차도착정보를 생성한 시각
    String arvlMsg2;	//첫번째도착메세지(전역 진입, 전역 도착 등)
    String arvlMsg3;	//두번째도착메세지(종합운동장 도착, 12분 후 (광명사거리) 등)
    int arvlCd; //도착코드 (0:진입, 1:도착, 2:출발, 3:전역출발, 4:전역진입, 5:전역도착, 99:운행중)
    

    StationInfo(String stationName){
        this.stationName = stationName;
    }
    
    void stationInfoSearch() throws UnsupportedEncodingException {
        String encodeVal = URLEncoder.encode(stationName, "utf-8");

        String tempUrl = "http://swopenapi.seoul.go.kr/api/subway/"+StationKey+"/"+type+"/realtimeStationArrival/0/20/"+encodeVal;

        System.out.println(tempUrl);

        try{
            URL url = new URL(tempUrl);

            BufferedReader bf;
            bf = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            String result = bf.readLine();

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonParser.parse(result);

            System.out.println(jsonObject);

            JSONArray realtimeArrivalList = (JSONArray)jsonObject.get("realtimeArrivalList");

            JSONObject metroInfo = (JSONObject)realtimeArrivalList.get(1);

            try{
                this.subwayId = Integer.valueOf((String)metroInfo.get("subwayId"));
            } catch (NumberFormatException e){
                subwayId = 0;
            }
            
            this.trainLineNm =(String)metroInfo.get("trainLineNm");

            System.out.println(subwayId);
            System.out.println(trainLineNm);
            System.out.println("");
            
        }catch(Exception e){
            System.out.print("실패");
            e.printStackTrace();
        }

    }
}
 
public class UsingApi {


    public static void main(String[] args) throws ParseException, UnsupportedEncodingException{
        System.out.println("");
        
    }
    
}
