/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.google.json;

import com.google.dao.Customer;
import com.google.dao.CustomerDao;
import com.google.ratel.deps.gson.*;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class GsonPerfTest {

    int count = 5000;

    int warmup = 5;

    Gson gson;

    public void testFromJson() {
        //String json = "[{\"id\":0,\"firstName\":\"Matthew\",\"lastName\":\"Wakefield\",\"state\":\"West Virginia\",\"birthDate\":\"Sep 11, 1993 8:33:51 AM\"},{\"id\":1,\"firstName\":\"Hannah\",\"lastName\":\"Driver\",\"state\":\"North Carolina\",\"birthDate\":\"Sep 20, 1973 8:33:51 AM\"},{\"id\":2,\"firstName\":\"Olivia\",\"lastName\":\"Tripp\",\"state\":\"Wisconsin\",\"birthDate\":\"Dec 31, 1978 8:33:51 AM\"},{\"id\":3,\"firstName\":\"Cristman\",\"lastName\":\"Wakefield\",\"state\":\"Massachusetts\",\"birthDate\":\"Jul 22, 1966 8:33:51 AM\"},{\"id\":4,\"firstName\":\"Hannah\",\"lastName\":\"Trull\",\"state\":\"Oregon\",\"birthDate\":\"Oct 9, 1988 8:33:51 AM\"},{\"id\":5,\"firstName\":\"Warner\",\"lastName\":\"O\\u0027Leary\",\"state\":\"Missouri\",\"birthDate\":\"Apr 17, 1967 8:33:51 AM\"},{\"id\":6,\"firstName\":\"Brittany\",\"lastName\":\"Drake\",\"state\":\"Montana\",\"birthDate\":\"Jan 16, 1998 8:33:51 AM\"},{\"id\":7,\"firstName\":\"Emma\",\"lastName\":\"Jetter\",\"state\":\"Nevada\",\"birthDate\":\"Aug 24, 1986 8:33:51 AM\"},{\"id\":8,\"firstName\":\"Michael\",\"lastName\":\"Tripp\",\"state\":\"Hawaii\",\"birthDate\":\"Jun 13, 1965 8:33:51 AM\"},{\"id\":9,\"firstName\":\"Hannah\",\"lastName\":\"O\\u0027Malley\",\"state\":\"Kentucky\",\"birthDate\":\"Aug 20, 1963 8:33:51 AM\"},{\"id\":10,\"firstName\":\"Emily\",\"lastName\":\"Baker\",\"state\":\"Hawaii\",\"birthDate\":\"Mar 7, 1994 8:33:51 AM\"},{\"id\":11,\"firstName\":\"Sarah\",\"lastName\":\"Aaron\",\"state\":\"New York\",\"birthDate\":\"Apr 19, 1990 8:33:51 AM\"},{\"id\":12,\"firstName\":\"Andrew\",\"lastName\":\"Badger\",\"state\":\"Kansas\",\"birthDate\":\"Mar 22, 1959 8:33:51 AM\"},{\"id\":13,\"firstName\":\"David\",\"lastName\":\"Crounse\",\"state\":\"Maryland\",\"birthDate\":\"Aug 4, 1997 8:33:51 AM\"},{\"id\":14,\"firstName\":\"Warner\",\"lastName\":\"Bagley\",\"state\":\"Oklahoma\",\"birthDate\":\"Jun 18, 1973 8:33:51 AM\"},{\"id\":15,\"firstName\":\"Billy\",\"lastName\":\"Bolingbroke\",\"state\":\"Washington\",\"birthDate\":\"Jun 15, 2000 8:33:51 AM\"},{\"id\":16,\"firstName\":\"Billy\",\"lastName\":\"Wakefield\",\"state\":\"Alaska\",\"birthDate\":\"Dec 14, 1996 8:33:51 AM\"},{\"id\":17,\"firstName\":\"Emma\",\"lastName\":\"Neville\",\"state\":\"South Dakota\",\"birthDate\":\"Mar 28, 1977 8:33:51 AM\"},{\"id\":18,\"firstName\":\"Andrew\",\"lastName\":\"Downs\",\"state\":\"Texas\",\"birthDate\":\"Aug 1, 1976 8:33:51 AM\"},{\"id\":19,\"firstName\":\"Madison\",\"lastName\":\"Downs\",\"state\":\"South Dakota\",\"birthDate\":\"Jun 24, 1976 8:33:51 AM\"},{\"id\":20,\"firstName\":\"Warner\",\"lastName\":\"Trull\",\"state\":\"Iowa\",\"birthDate\":\"Feb 22, 1990 8:33:51 AM\"},{\"id\":21,\"firstName\":\"Emily\",\"lastName\":\"Trull\",\"state\":\"Arizona\",\"birthDate\":\"Sep 19, 1982 8:33:51 AM\"},{\"id\":22,\"firstName\":\"Emma\",\"lastName\":\"Bagley\",\"state\":\"Kansas\",\"birthDate\":\"Apr 22, 1996 8:33:51 AM\"},{\"id\":23,\"firstName\":\"Matthew\",\"lastName\":\"Bagley\",\"state\":\"Michigan\",\"birthDate\":\"Dec 18, 1979 8:33:51 AM\"},{\"id\":24,\"firstName\":\"Billy\",\"lastName\":\"Drake\",\"state\":\"Montana\",\"birthDate\":\"Dec 5, 1954 8:33:51 AM\"},{\"id\":25,\"firstName\":\"Hannah\",\"lastName\":\"Baker\",\"state\":\"Alabama\",\"birthDate\":\"Apr 20, 1986 8:33:51 AM\"},{\"id\":26,\"firstName\":\"Cristman\",\"lastName\":\"Waller\",\"state\":\"Arkansas\",\"birthDate\":\"Mar 29, 1985 8:33:51 AM\"},{\"id\":27,\"firstName\":\"Hannah\",\"lastName\":\"Jetter\",\"state\":\"Arkansas\",\"birthDate\":\"Apr 3, 1967 8:33:51 AM\"},{\"id\":28,\"firstName\":\"Andrew\",\"lastName\":\"Drake\",\"state\":\"Mississippi\",\"birthDate\":\"Sep 22, 1951 8:33:51 AM\"},{\"id\":29,\"firstName\":\"Michael\",\"lastName\":\"Driver\",\"state\":\"Connecticut\",\"birthDate\":\"Nov 23, 1956 8:33:51 AM\"},{\"id\":30,\"firstName\":\"Colin\",\"lastName\":\"Downs\",\"state\":\"Pennsylvania\",\"birthDate\":\"Jan 22, 1964 8:33:51 AM\"},{\"id\":31,\"firstName\":\"Michael\",\"lastName\":\"Badger\",\"state\":\"Oklahoma\",\"birthDate\":\"Nov 5, 1997 8:33:51 AM\"},{\"id\":32,\"firstName\":\"Emily\",\"lastName\":\"Jetter\",\"state\":\"Oklahoma\",\"birthDate\":\"Feb 27, 1958 8:33:51 AM\"},{\"id\":33,\"firstName\":\"Cristman\",\"lastName\":\"O\\u0027Leary\",\"state\":\"Kansas\",\"birthDate\":\"Jul 20, 1993 8:33:51 AM\"},{\"id\":34,\"firstName\":\"Hannah\",\"lastName\":\"Jetter\",\"state\":\"Mississippi\",\"birthDate\":\"Aug 23, 1964 8:33:51 AM\"},{\"id\":35,\"firstName\":\"Hannah\",\"lastName\":\"Bagley\",\"state\":\"Minnesota\",\"birthDate\":\"Oct 2, 1982 8:33:51 AM\"},{\"id\":36,\"firstName\":\"Emma\",\"lastName\":\"Neville\",\"state\":\"Maine\",\"birthDate\":\"Jul 16, 1978 8:33:51 AM\"},{\"id\":37,\"firstName\":\"Emma\",\"lastName\":\"Driver\",\"state\":\"Maryland\",\"birthDate\":\"Jul 3, 1997 8:33:51 AM\"},{\"id\":38,\"firstName\":\"Emily\",\"lastName\":\"Bagley\",\"state\":\"Montana\",\"birthDate\":\"Sep 28, 1959 8:33:51 AM\"},{\"id\":39,\"firstName\":\"Emma\",\"lastName\":\"Jetter\",\"state\":\"Alabama\",\"birthDate\":\"Apr 12, 1970 8:33:51 AM\"},{\"id\":40,\"firstName\":\"Emily\",\"lastName\":\"Jetter\",\"state\":\"California\",\"birthDate\":\"May 25, 1992 8:33:51 AM\"},{\"id\":41,\"firstName\":\"Michael\",\"lastName\":\"Badger\",\"state\":\"Iowa\",\"birthDate\":\"Jan 4, 1981 8:33:51 AM\"},{\"id\":42,\"firstName\":\"Brittany\",\"lastName\":\"Wakefield\",\"state\":\"Alabama\",\"birthDate\":\"Jun 3, 1956 8:33:51 AM\"},{\"id\":43,\"firstName\":\"Billy\",\"lastName\":\"Drake\",\"state\":\"Texas\",\"birthDate\":\"Aug 27, 1987 8:33:51 AM\"},{\"id\":44,\"firstName\":\"Sarah\",\"lastName\":\"Waller\",\"state\":\"Nevada\",\"birthDate\":\"Nov 11, 1973 8:33:51 AM\"},{\"id\":45,\"firstName\":\"Hannah\",\"lastName\":\"O\\u0027Malley\",\"state\":\"Delaware\",\"birthDate\":\"Aug 25, 1998 8:33:51 AM\"},{\"id\":46,\"firstName\":\"Colin\",\"lastName\":\"Bagley\",\"state\":\"Texas\",\"birthDate\":\"Apr 14, 1995 8:33:51 AM\"},{\"id\":47,\"firstName\":\"Matthew\",\"lastName\":\"Drake\",\"state\":\"Rhode Island\",\"birthDate\":\"Mar 9, 1956 8:33:51 AM\"},{\"id\":48,\"firstName\":\"Daniel\",\"lastName\":\"Wakefield\",\"state\":\"West Virginia\",\"birthDate\":\"Jun 11, 1972 8:33:51 AM\"},{\"id\":49,\"firstName\":\"Sarah\",\"lastName\":\"O\\u0027Malley\",\"state\":\"Maryland\",\"birthDate\":\"Jul 20, 1992 8:33:51 AM\"}]";
        String json = "[{\"id\":0,\"firstName\":\"Daniel\",\"lastName\":\"Crounse\",\"state\":\"Louisiana\",\"birthDate\":-163006499920},{\"id\":1,\"firstName\":\"Jacob\",\"lastName\":\"Jasper\",\"state\":\"Louisiana\",\"birthDate\":260007900080},{\"id\":2,\"firstName\":\"Sarah\",\"lastName\":\"Duff\",\"state\":\"Pennsylvania\",\"birthDate\":-72632099920},{\"id\":3,\"firstName\":\"Cristman\",\"lastName\":\"Drake\",\"state\":\"Alabama\",\"birthDate\":436782300080},{\"id\":4,\"firstName\":\"Matthew\",\"lastName\":\"Tripp\",\"state\":\"Maryland\",\"birthDate\":-129396899920},{\"id\":5,\"firstName\":\"Colin\",\"lastName\":\"Driver\",\"state\":\"Arkansas\",\"birthDate\":456222300080},{\"id\":6,\"firstName\":\"Michael\",\"lastName\":\"Driver\",\"state\":\"Louisiana\",\"birthDate\":235211100080},{\"id\":7,\"firstName\":\"Madison\",\"lastName\":\"Crounse\",\"state\":\"West Virginia\",\"birthDate\":955268700080},{\"id\":8,\"firstName\":\"David\",\"lastName\":\"Duff\",\"state\":\"Hawaii\",\"birthDate\":944987100080},{\"id\":9,\"firstName\":\"David\",\"lastName\":\"Baker\",\"state\":\"Oregon\",\"birthDate\":113905500080},{\"id\":10,\"firstName\":\"Warner\",\"lastName\":\"Drake\",\"state\":\"California\",\"birthDate\":768903900080},{\"id\":11,\"firstName\":\"Olivia\",\"lastName\":\"Bagley\",\"state\":\"Iowa\",\"birthDate\":173003100081},{\"id\":12,\"firstName\":\"Hannah\",\"lastName\":\"Aaron\",\"state\":\"Tennessee\",\"birthDate\":701166300081},{\"id\":13,\"firstName\":\"Billy\",\"lastName\":\"Baker\",\"state\":\"Hawaii\",\"birthDate\":610273500081},{\"id\":14,\"firstName\":\"Matthew\",\"lastName\":\"Duff\",\"state\":\"Missouri\",\"birthDate\":150279900081},{\"id\":15,\"firstName\":\"Brittany\",\"lastName\":\"Duff\",\"state\":\"Illinois\",\"birthDate\":-201022499919},{\"id\":16,\"firstName\":\"Daniel\",\"lastName\":\"Waller\",\"state\":\"Connecticut\",\"birthDate\":89713500081},{\"id\":17,\"firstName\":\"Madison\",\"lastName\":\"Tripp\",\"state\":\"Maryland\",\"birthDate\":51697500081},{\"id\":18,\"firstName\":\"Emma\",\"lastName\":\"Bagley\",\"state\":\"Oregon\",\"birthDate\":485252700081},{\"id\":19,\"firstName\":\"Sarah\",\"lastName\":\"Duff\",\"state\":\"Wisconsin\",\"birthDate\":635761500081},{\"id\":20,\"firstName\":\"Emily\",\"lastName\":\"Badger\",\"state\":\"Tennessee\",\"birthDate\":195726300081},{\"id\":21,\"firstName\":\"Olivia\",\"lastName\":\"Drake\",\"state\":\"Idaho\",\"birthDate\":220609500081},{\"id\":22,\"firstName\":\"Olivia\",\"lastName\":\"Waller\",\"state\":\"Pennsylvania\",\"birthDate\":426155100081},{\"id\":23,\"firstName\":\"David\",\"lastName\":\"O\\u0027Malley\",\"state\":\"Nebraska\",\"birthDate\":366107100081},{\"id\":24,\"firstName\":\"Royalle\",\"lastName\":\"Bolingbroke\",\"state\":\"Alaska\",\"birthDate\":-272302499919},{\"id\":25,\"firstName\":\"Sarah\",\"lastName\":\"Baker\",\"state\":\"Virginia\",\"birthDate\":-240075299919},{\"id\":26,\"firstName\":\"Cletus\",\"lastName\":\"Trull\",\"state\":\"Rhode Island\",\"birthDate\":810980700081},{\"id\":27,\"firstName\":\"Billy\",\"lastName\":\"O\\u0027Leary\",\"state\":\"New Mexico\",\"birthDate\":436091100081},{\"id\":28,\"firstName\":\"Emily\",\"lastName\":\"Towers\",\"state\":\"Minnesota\",\"birthDate\":-366219299919},{\"id\":29,\"firstName\":\"Hannah\",\"lastName\":\"Jasper\",\"state\":\"Kansas\",\"birthDate\":407492700081},{\"id\":30,\"firstName\":\"Joshua\",\"lastName\":\"Aaron\",\"state\":\"Rhode Island\",\"birthDate\":957601500081},{\"id\":31,\"firstName\":\"Brittany\",\"lastName\":\"Duff\",\"state\":\"Nevada\",\"birthDate\":444212700081},{\"id\":32,\"firstName\":\"Emily\",\"lastName\":\"Aaron\",\"state\":\"Hawaii\",\"birthDate\":-176916899919},{\"id\":33,\"firstName\":\"Joshua\",\"lastName\":\"Neville\",\"state\":\"Oregon\",\"birthDate\":-31505699919},{\"id\":34,\"firstName\":\"Royalle\",\"lastName\":\"Duff\",\"state\":\"Delaware\",\"birthDate\":-384708899919},{\"id\":35,\"firstName\":\"Cristman\",\"lastName\":\"Driver\",\"state\":\"Missouri\",\"birthDate\":641031900081},{\"id\":36,\"firstName\":\"Emma\",\"lastName\":\"Bagley\",\"state\":\"Oklahoma\",\"birthDate\":515147100081},{\"id\":37,\"firstName\":\"Olivia\",\"lastName\":\"Bagley\",\"state\":\"Missouri\",\"birthDate\":-499016099919},{\"id\":38,\"firstName\":\"Royalle\",\"lastName\":\"Duff\",\"state\":\"Indiana\",\"birthDate\":444903900081},{\"id\":39,\"firstName\":\"Jacob\",\"lastName\":\"Crounse\",\"state\":\"Alaska\",\"birthDate\":-437240099918},{\"id\":40,\"firstName\":\"Olivia\",\"lastName\":\"Driver\",\"state\":\"Pennsylvania\",\"birthDate\":196935900082},{\"id\":41,\"firstName\":\"Madison\",\"lastName\":\"Duff\",\"state\":\"Ohio\",\"birthDate\":859883100082},{\"id\":42,\"firstName\":\"Billy\",\"lastName\":\"Trull\",\"state\":\"Michigan\",\"birthDate\":-452446499918},{\"id\":43,\"firstName\":\"David\",\"lastName\":\"Wakefield\",\"state\":\"Alabama\",\"birthDate\":366711900082},{\"id\":44,\"firstName\":\"Sarah\",\"lastName\":\"Driver\",\"state\":\"Ohio\",\"birthDate\":-75656099918},{\"id\":45,\"firstName\":\"Warner\",\"lastName\":\"Wakefield\",\"state\":\"Massachusetts\",\"birthDate\":214647900082},{\"id\":46,\"firstName\":\"Brittany\",\"lastName\":\"Bagley\",\"state\":\"Iowa\",\"birthDate\":889518300082},{\"id\":47,\"firstName\":\"Sarah\",\"lastName\":\"O\\u0027Malley\",\"state\":\"Louisiana\",\"birthDate\":509963100082},{\"id\":48,\"firstName\":\"Emily\",\"lastName\":\"O\\u0027Leary\",\"state\":\"Montana\",\"birthDate\":-94491299918},{\"id\":49,\"firstName\":\"David\",\"lastName\":\"Bolingbroke\",\"state\":\"Massachusetts\",\"birthDate\":210414300082}]";

        Customer[] customers = null;
        for (int i = 0; i < warmup; i++) {
            customers = gson.fromJson(json, Customer[].class);
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            customers = gson.fromJson(json, Customer[].class);
        }

        long time = (System.currentTimeMillis() - start);
        System.out.println("Gson FromJson customer count: " + customers.length + ", time: " + time);
    }

    public void testToJson() {
        List<Customer> customers = CustomerDao.getInstance().findAll();

        int size = 0;
        for (int i = 0; i < warmup; i++) {
            String json = gson.toJson(customers);
            size = json.length();
        }

        long start = System.currentTimeMillis();

        for (int i = 0; i < count; i++) {
            String json = gson.toJson(customers);
            //System.out.println(json);
            //System.out.println(" ");
            size = json.length();
        }

        long time = (System.currentTimeMillis() - start);
        System.out.println("Gson ToJson size: " + size + ", time: " + time);
    }

    public static void main(String args[]) {
        GsonPerfTest test = new GsonPerfTest();
        GsonBuilder builder = new GsonBuilder();
// Register an adapter to manage the date types as long values 
        builder.registerTypeAdapter(Date.class, new JsonSerializer<Date>() {

            @Override
            public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
                //return new JsonElement() Date(json.getAsJsonPrimitive().getAsLong());
                JsonElement el = new JsonPrimitive(src.getTime());
                return el;
            }
        });
        
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });

        test.gson = builder.create();
        //test.gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssz").create();
        //test.gson =  new GsonBuilder().create();

        test.testToJson();
        test.testFromJson();
    }
}
