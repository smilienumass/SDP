//package SDP_RFID;

import com.thingmagic.Reader;
import com.thingmagic.*;
import java.util.logging.Level;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.*; //for communication with Smeel's module
/**
 *
 * @author AfroJosh
 * Joshua Teixeira
 * SDP21: 2020-09 -> 2021-05
 */
/* TO DO:
    -   Implement parameter pass through to control read time, power
    -   Implement parameter to control verboseness of JSON output
    -   Implement parameter to stop deletion of RFID Read Output file (for debug)
        -   Perhaps as cmdline args (for all above)
*/



public class SDP_RFID {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ReaderException, InterruptedException
    {
        read();
    }
    @SuppressWarnings("unchecked") //Suppress unchecked type warning
    public static void read() throws ReaderException, InterruptedException{
        //Reader Settings
        Reader myReader = null;
        int[] antennaList = null;
        /* Windows Com port
         * String comPort = "tmr:///com5"; //Com port for current reader, URI syntax
         */
        
        /* NOTE!!!! You may need to customize this com port, check /dev/ to see what it should be !!!! */
        String comPort = "tmr:///dev/ttyACM0"; //Com port for current reader, URI syntax
        int readPowerCentiDBm = 2000; //2000 is default in my experience, with small read distance
         
        //File Writing Variables
        JSONObject rfidJSON = new JSONObject();
        boolean json = true; //set false if no json output wanted
        PrintWriter out = null;
        
        /* NOTE!!!! Customize these file names as you like, the first is my default windows path, second is raspbian path */
        //String filePath = "F:\\Files\\Documents\\College\\5-Senior\\RFIDJSON\\reads" + System.currentTimeMillis() +".json";
        String filePath = "/home/pi/Desktop/RFID/reads" + System.currentTimeMillis() +".json"; //raspbian file path
        
        try
        {
        int[] ants = {1};
        antennaList = ants; //set antenna list to look on ant 1
        
        //Create and connect to reader on com port var
        myReader = Reader.create(comPort);
        myReader.connect();
        
        if (Reader.Region.UNSPEC == (Reader.Region) myReader.paramGet("/reader/region/id"))
        {
            Reader.Region[] supportedRegions = (Reader.Region[]) myReader.paramGet(TMConstants.TMR_PARAM_REGION_SUPPORTEDREGIONS);
            if (supportedRegions.length < 1)
            {
                throw new Exception("Reader doesn't support any regions");
            }
            else
            {
                myReader.paramSet("/reader/region/id", supportedRegions[0]);
            }
        }
        System.out.println("Current Read Power Set to: " + myReader.paramGet("/reader/radio/readPower"));
        myReader.paramSet("/reader/radio/readPower", readPowerCentiDBm);
        System.out.println("Current Read Power Set to: " + myReader.paramGet("/reader/radio/readPower"));
        
        //Not sure whatt the 1000 here means... duration?
        SimpleReadPlan plan = new SimpleReadPlan(antennaList, TagProtocol.GEN2, null, null, 1000);
        myReader.paramSet(TMConstants.TMR_PARAM_READ_PLAN, plan);
        
        System.out.println("About to read tags");
        TagReadData[] redTags = myReader.read(1000); //read tags for 1s
        System.out.println("Done reading tags");
        
        System.out.println("Num of reads = " + redTags.length);
        
        if (json = true && redTags.length != 0){
            int tagCount = 0;                                                   //to iterate over
            out = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)));
            out.write("{\n\"readEvent\": [\n");
            for (TagReadData tr : redTags)                                      //for each tag read, push this metadata into a temp json object
            {
                JSONObject tempTagObj = new JSONObject();
                
                tempTagObj.put("EPC", tr.epcString());
                //new JSONObject<String,String>().put("EPC", tr.epcString());
                tempTagObj.put("Timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date(tr.getTime())));
                tempTagObj.put("RSSI", tr.getRssi());
                tempTagObj.put("Phase", tr.getPhase());
                tempTagObj.put("Readcount", tr.getReadCount());
                
                tagCount += 1;
                
                if(tagCount == redTags.length){
                    out.println(tempTagObj.toJSONString());
                }else{
                    out.println(tempTagObj.toJSONString()+",");  //print new temp obj to file, repeat
                }
                
            }
            out.write("]}");
            //If the file isnt closed, close it now
            if (out != null) {
                out.close();
            }
            
        }else{ //if no json just print EPCs to console
            for (TagReadData tr : redTags)
            {
                System.out.println("EPC: " + tr.epcString());
            }
        }
        
        //Shutdown and destroy reader object
        myReader.destroy();
        System.out.println("Reader Shut Down");
        
        } 
    catch (ReaderException re)
    {
        // Shut down reader
        if(myReader!=null)
        {
            myReader.destroy();
        }
        System.out.println("Reader Exception: " + re.getMessage());
    }
    catch (Exception re)
    {
        // Shut down reader
        if(myReader!=null)
        {
            myReader.destroy();
        }
        System.out.println("Exception: " + re.getMessage());
    }
}
}

