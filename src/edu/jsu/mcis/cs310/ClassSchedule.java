package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ClassSchedule {
    
    private final String CSV_FILENAME = "jsu_sp24_v1.csv";
    private final String JSON_FILENAME = "jsu_sp24_v1.json";
    
    private final String CRN_COL_HEADER = "crn";
    private final String SUBJECT_COL_HEADER = "subject";
    private final String NUM_COL_HEADER = "num";
    private final String DESCRIPTION_COL_HEADER = "description";
    private final String SECTION_COL_HEADER = "section";
    private final String TYPE_COL_HEADER = "type";
    private final String CREDITS_COL_HEADER = "credits";
    private final String START_COL_HEADER = "start";
    private final String END_COL_HEADER = "end";
    private final String DAYS_COL_HEADER = "days";
    private final String WHERE_COL_HEADER = "where";
    private final String SCHEDULE_COL_HEADER = "schedule";
    private final String INSTRUCTOR_COL_HEADER = "instructor";
    private final String SUBJECTID_COL_HEADER = "subjectid";
    
    public String convertCsvToJsonString(List<String[]> csv) {
        
        return ""; // remove this!
        
    }
    
	public String convertJsonToCsvString(JsonObject json) {

    	List<String[]> csvData = new ArrayList<>();

    	//reteived the headers from the constants and added to a string
    	String[] headers = {
        	CRN_COL_HEADER, SUBJECT_COL_HEADER, NUM_COL_HEADER,
        	DESCRIPTION_COL_HEADER, SECTION_COL_HEADER, TYPE_COL_HEADER,
        	CREDITS_COL_HEADER, START_COL_HEADER, END_COL_HEADER, DAYS_COL_HEADER,
        	WHERE_COL_HEADER, SCHEDULE_COL_HEADER, INSTRUCTOR_COL_HEADER
    	};
    	csvData.add(headers);

    	//extracted the datga from the json
    	JsonObject scheduleTypeJson = (JsonObject) json.get("scheduletype");
    	JsonObject subjectJson = (JsonObject) json.get("subject");
    	JsonObject courseJson = (JsonObject) json.get("course");
    	JsonArray sectionJson = (JsonArray) json.get("section");
   	 
    	//for loop to process each section
    	for (Object obj : sectionJson) {
        	JsonObject section = (JsonObject) obj;

        	String[] row = new String[13];
        	String courseKey = section.get("subjectid").toString() + " " + section.get("num").toString();
        	JsonObject courseDetails = (JsonObject) courseJson.get(courseKey);
        	JsonArray instructors = (JsonArray) section.get("instructor");

        	//populate the row
        	row[0] = section.get("crn").toString();
        	row[1] = subjectJson.get(section.get("subjectid").toString()).toString();
        	row[2] = courseKey;
        	row[3] = courseDetails.get("description").toString();
        	row[4] = section.get("section").toString();
        	row[5] = section.get("type").toString();
        	row[6] = courseDetails.get("credits").toString();
        	row[7] = section.get("start").toString();
        	row[8] = section.get("end").toString();
        	row[9] = section.get("days").toString();
        	row[10] = section.get("where").toString();
        	row[11] = scheduleTypeJson.get(row[5]).toString();

        	//formatted instructors so you can have multiple
        	StringBuilder instructorsBuilder = new StringBuilder();
        	for (int j = 0; j < instructors.size(); j++) {
        	if (j > 0) {
               	instructorsBuilder.append(", ");
        	}
        	instructorsBuilder.append(instructors.get(j).toString());
        	}
        	row[12] = instructorsBuilder.toString();

        	csvData.add(row);
    	}
    	return getCsvString(csvData);
    	}
    
    public JsonObject getJson() {
        
        JsonObject json = getJson(getInputFileData(JSON_FILENAME));
        return json;
        
    }
    
    public JsonObject getJson(String input) {
        
        JsonObject json = null;
        
        try {
            json = (JsonObject)Jsoner.deserialize(input);
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return json;
        
    }
    
    public List<String[]> getCsv() {
        
        List<String[]> csv = getCsv(getInputFileData(CSV_FILENAME));
        return csv;
        
    }
    
    public List<String[]> getCsv(String input) {
        
        List<String[]> csv = null;
        
        try {
            
            CSVReader reader = new CSVReaderBuilder(new StringReader(input)).withCSVParser(new CSVParserBuilder().withSeparator('\t').build()).build();
            csv = reader.readAll();
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return csv;
        
    }
    
    public String getCsvString(List<String[]> csv) {
        
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer, '\t', '"', '\\', "\n");
        
        csvWriter.writeAll(csv);
        
        return writer.toString();
        
    }
    
    private String getInputFileData(String filename) {
        
        StringBuilder buffer = new StringBuilder();
        String line;
        
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        
        try {
        
            BufferedReader reader = new BufferedReader(new InputStreamReader(loader.getResourceAsStream("resources" + File.separator + filename)));

            while((line = reader.readLine()) != null) {
                buffer.append(line).append('\n');
            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return buffer.toString();
        
    }
    
}