
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;


public class StringSplitter {
	
	public static List<Provider> parseCSV(String csvContent) throws IOException 
    {
        List<Provider> providerData = new ArrayList<Provider>();
        String SPLIT_Character = "\n";
        StringTokenizer stToken = new StringTokenizer(csvContent, SPLIT_Character);
        
        String[] splittedRows = new String[stToken.countTokens()];

        int index = 0;
        while(stToken.hasMoreElements()) 
        {
            splittedRows[index++] = stToken.nextToken();
        }
        
        for (String row : splittedRows)
        {
            String[] data = row.split(",");
            Provider provider = new Provider();
            provider.setIdentifier(data[0]);
            provider.setName(data[1]);
            providerData.add(provider);
        }
        
        return providerData;
    }
    
    public static List<Provider> parseTSV(String tsvContent) throws IOException 
    {
        List<Provider> providerData = new ArrayList<Provider>();
        String SPLIT_Character = "\n";
        StringTokenizer stToken = new StringTokenizer(tsvContent, SPLIT_Character);
        
        String[] splittedRows = new String[stToken.countTokens()];

        int index = 0;
        while(stToken.hasMoreElements()) 
        {
            splittedRows[index++] = stToken.nextToken();
        }
        
        for (String row : splittedRows)
        {
            String[] data = row.split("\t");
            Provider provider = new Provider();
            provider.setIdentifier(data[0]);
            provider.setName(data[1]);
            providerData.add(provider);
        }
        
        return providerData;
    }

	
    /* This method is used to read the data's from an excel file.
    * @param fileName - Name of the excel file.
    */
    public static List<Provider> parseExcelFile(String filePath)
    {
        /**
        * Create a new instance for cellDataList
        */
        List<Provider> providerData = new ArrayList<Provider>();
        try
        {
            /**
            * Create a new instance for FileInputStream class
            */
            FileInputStream fileInputStream = new FileInputStream(filePath);
        
            /**
            * Create a new instance for POIFSFileSystem class
            */
            POIFSFileSystem fsFileSystem = new POIFSFileSystem(fileInputStream);
        
            /*
            * Create a new instance for HSSFWorkBook Class
            */
            HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
            HSSFSheet hssfSheet = workBook.getSheetAt(0);
        
            /**
            * Iterate the rows and cells of the spreadsheet
            * to get all the datas.
            */
            Iterator<Row> rowIterator = hssfSheet.rowIterator();
            boolean firstFlag = true;
            while (rowIterator.hasNext())
            {
                HSSFRow hssfRow = (HSSFRow) rowIterator.next();
                if(firstFlag)
                {
//                    hssfRow = (HSSFRow) rowIterator.next();
                    firstFlag = false;
                }
                Iterator<Cell> cellIterator = hssfRow.cellIterator();
        
                Provider provider = new Provider();
                
                HSSFCell hssfCell = (HSSFCell) cellIterator.next();
                int x=(int) hssfCell.getNumericCellValue();
                provider.setIdentifier(x+"");
                
                hssfCell = (HSSFCell) cellIterator.next();
                provider.setName(hssfCell.getStringCellValue());
                
                providerData.add(provider);
                
            }
            
            
            
      
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return providerData;
    }
    
    private static String getTagValue(String sTag, Element eElement) {
		NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
	 
	        Node nValue = (Node) nlList.item(0);
	 
		return nValue.getNodeValue();
	  }

	public static List<Provider> parseXMLFile(String filePath) {
		// TODO Auto-generated method stub
		 List<Provider> providerData = new ArrayList<Provider>();
		try{
		 String input ="";
		 
//		File fXmlFile = new File("C:\\Minakshi\\IP\\Project\\Client\\sample.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		File fXmlFile = new File(filePath);
		Document doc = dBuilder.parse(filePath);
		doc.getDocumentElement().normalize();
 
//		System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		NodeList nList = doc.getElementsByTagName("provider");
		System.out.println("-----------------------");
 
		for (int temp = 0; temp < nList.getLength(); temp++) {
 
		   Node nNode = nList.item(temp);
		   if (nNode.getNodeType() == Node.ELEMENT_NODE) {
 
		      Element eElement = (Element) nNode;
 
//		      System.out.println("Number  : " + getTagValue("number", eElement));
//		      System.out.println("Version : " + getTagValue("version", eElement));
//	              System.out.println("Title : " + getTagValue("title", eElement));
//		      System.out.println("Salary : " + getTagValue("salary", eElement));
		      Provider provider = new Provider();
		      provider.setIdentifier(getTagValue("identifier", eElement));
		      provider.setName(getTagValue("name", eElement));
		      providerData.add(provider);
//	              input = input.concat("ADD RFC " + getTagValue("number", eElement) + " " + getTagValue("version", eElement)+ "\nHost: " + InetAddress.getLocalHost().getHostName() + "\nPort: " + portNumber + "\nTitle: " + getTagValue("title", eElement) + "\n") ;
	         
   		   }
		}
	   System.out.println(input);
	  } catch (Exception e) {
		e.printStackTrace();
	  }
		return providerData;
	}
    
    
}
