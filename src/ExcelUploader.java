import java.io.IOException;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

public class ExcelUploader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String TMP_DIR_PATH = "c:\\tmp";
	private File tmpDir;
	private static final String DESTINATION_DIR_PATH = "C:\\tmp";
	private File destinationDir;



	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ExcelUploader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		tmpDir = new File(TMP_DIR_PATH);
		if (!tmpDir.isDirectory()) {
			throw new ServletException(TMP_DIR_PATH + " is not a directory");
		}
		// String realPath =
		// getServletContext().getRealPath(DESTINATION_DIR_PATH);
		String realPath = "C:\\tmp";
		destinationDir = new File(realPath);
		if (!destinationDir.isDirectory()) {
			throw new ServletException(DESTINATION_DIR_PATH
					+ " is not a directory");
		}

	}


	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		ServletFileUpload upload = new ServletFileUpload();
//		String overwrite = null;
//	try{
//		 FileItemIterator iterator = upload.getItemIterator(request);
//
//         while (iterator.hasNext()) {
//             FileItemStream item = iterator.next();
//
//             InputStream stream = item.openStream();
//
//             if (item.isFormField()) {
////                 log.warning("Got a form field: " + item.getFieldName()  + " " +item);
//                 overwrite = item.getFieldName();
////                 String something = item;
//
//
//             } 
//
//         }
//		
//	}catch(FileUploadException e){
//		e.printStackTrace();
//		
//	}	
		PrintWriter out = response.getWriter();
		
//		String overwrite = request.getParameter("setoverwritedata1");

//		String overwrite2 = request.getParameter("setoverwritedata");

		String filePath = "";

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();


		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB
String overwrite=null;

		fileItemFactory.setRepository(tmpDir);

		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		try {
			/*
			 * Parse the request
			 */
			List items = uploadHandler.parseRequest(request);
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				/*
				 * Handle Form Fields.
				 */
				if (item.isFormField()) {
					
					if(item.getFieldName().equalsIgnoreCase("setoverwritedata1")&& item.getString().equalsIgnoreCase("selected"))
					{
						overwrite="true";
					}
//					overwrite = item.getFieldName();
//					  System.out.println("Field name: " + item.getFieldName());
//				        System.out.println("Field value: " + item.getString());
//					String asach = item.getString("setoverwritedata1");

				} else {

					// Handle Uploaded files.
					/*
					 * Write file to the ultimate location.
					 */

					File file = new File(destinationDir, item.getName());
					filePath = file.getAbsolutePath();

					item.write(file);
				}
			}
		} catch (FileUploadException ex) {
			log("Error encountered while parsing the request", ex);
		} catch (Exception ex) {
			log("Error encountered while uploading file", ex);
		}

		/* make the calls from here */

		List<Provider> dbProviderData = new ArrayList<Provider>();
		if(filePath.endsWith("xls"))
			dbProviderData = StringSplitter.parseExcelFile(filePath);
		else
			dbProviderData = StringSplitter.parseXMLFile(filePath);

		
		boolean setOverWriteData = false;	
		if (overwrite != null) {
			System.out.println("\nhere\n");
			setOverWriteData = true;
		}

		
		
		DbConnection db = new DbConnection();
		db.setDbProvider(dbProviderData);
		db.setOverWriteData(setOverWriteData);
		List<Provider> existingProviderList = new ArrayList<Provider>();
		db.updateDB();
		existingProviderList = db.getExistingProviderList();

		out.print("<!DOCTYPE html><html><body>");

		if (existingProviderList != null) {

			out.println("<h1>These updates were not made because overwrite "
					+ "option was not selected, and there was a clash "
					+ "with identifier values:</h1>");
			for (Provider p : existingProviderList) {
				out.print("<br> Identifier: " + p.getIdentifier() + "\t Name: "
						+ p.getName());
			}
		} else {
			out.println("<h1>All the updates/inserts were successful. Database has been updated.</h1>");

		}

		out.println("</body></html>");
		

	}
}
