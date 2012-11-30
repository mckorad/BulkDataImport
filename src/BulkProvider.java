import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.output.*;

/**
 * Servlet implementation class BulkProvider
 */
public class BulkProvider extends HttpServlet {
	private static final long serialVersionUID = 1L;
	boolean setOverWriteData = false;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
//		if(request.getParameter("submitxl")!=null)
//		{
//			System.out.println("Inside");
////			getServletContext().getServlet("exceluploader");
//			RequestDispatcher dispatcher=getServletContext().getRequestDispatcher( "/servlet/exceluploader" );
//			   dispatcher.forward( request, response );
//
//		}
//		else
//			System.out.println("request.getParameter submitxl is null");
		PrintWriter out = response.getWriter();
		String radio = request.getParameter("action");
		String text = request.getParameter("data");
		String overwrite = request.getParameter("setoverwritedata");
	
		

		if (overwrite != null) {
			setOverWriteData = true;
		}

		List<Provider> dbProviderData = new ArrayList<Provider>();

		if (radio.equals("csvContent")) {
			dbProviderData = StringSplitter.parseCSV(text);

		}

		if (radio.equals("tsvContent")) {
			dbProviderData = StringSplitter.parseTSV(text);

		}

		/* call db classes here */

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
