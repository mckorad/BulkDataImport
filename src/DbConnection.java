import java.sql.*;
import javax.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class DbConnection {
	

	private List<Provider> dbProviderData;
	private List<Provider> existingProviderList;
	private boolean overwriteData;
	
	
	void setDbProvider(List<Provider> dbProviderData)
	{
		this.dbProviderData = dbProviderData;
	}
	void setOverWriteData(boolean overwriteData)
	{
		this.overwriteData = overwriteData;
	}
	
	List<Provider> getExistingProviderList()
	{
		return existingProviderList;
		
	}

	
	
	public DbConnection()
	{
		dbProviderData = new ArrayList<Provider>();
	}
	
	public void updateDB()
	{
		//System.out.println("Inserting values in Mysql database table!");
		Connection con = null;
		String url = "jdbc:mysql://localhost:3306/";
		String db = "projectoss"; 						// String connectionUrl = "jdbc:mysql://localhost/projectDB?user=root&password=root"; 
		String driver = "com.mysql.jdbc.Driver";
		String user = "root";
		String password = "coep309min";
		try
		{	
			Class.forName(driver);  
		    con = DriverManager.getConnection(url+db,user,password);
		    
	    	Set<String> idList = new HashSet<String>();
			Statement statement = con.createStatement();
			ResultSet resultSet = null;
			
			
			resultSet = statement.executeQuery("Select * from Provider");
			
			if(resultSet != null )
			{	
				System.out.println("Not null");
				while(resultSet.next())
				{
					System.out.println("Identifier: " +resultSet.getString(1));
					System.out.println("Name: "+resultSet.getString(2));
				}
			}
			
			try
			{
				resultSet = statement.executeQuery("Select identifier from Provider");
				if(resultSet != null )
				{	
					System.out.println("Not null");
					while(resultSet.next())
					{
						idList.add(resultSet.getString(1));
						System.out.println("\n::"+resultSet.getString(1));
					}
				}
			}
			finally
			{
				try
				{
					resultSet.close();
					statement.close();
				}
				catch(Exception exe)
				{
					
					exe.printStackTrace();
				}
			}
			
			PreparedStatement st = null;
			try
			{
				if(overwriteData)
			    {
					System.out.println("here");
					for(Provider data : dbProviderData)
					{
						
						if(idList.contains(data.getIdentifier()))
						{
							st = con.prepareStatement("update Provider set name = '"+ data.getName()+ "' where identifier = '" + data.getIdentifier()+"'");  
						    st.executeUpdate();
						}
						else
						{
							st = con.prepareStatement("insert into Provider values ('"+ data.getIdentifier() +"', '"+ data.getName()+"')");  
							st.executeUpdate();
						}
					}
			    }
				else
				{
					for(Provider data : dbProviderData)
					{
						if(idList.contains(data.getIdentifier()))
						{
							existingProviderList = new ArrayList<Provider>();
							existingProviderList.add(data);
						    
						}
						else
						{
							st = con.prepareStatement("insert into Provider values ('"+ data.getIdentifier() +"','"+ data.getName()+"')");  
							st.executeUpdate();
						}
					}
					
				}
			}
			finally
			{
				//st.close();
			}
		}
		catch(Exception exe)
		{
			System.out.print("exception"+exe);
			exe.printStackTrace();
		}
	}

}
