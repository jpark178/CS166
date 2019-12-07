/*
 * Template JAVA User Interface
 * =============================
 *
 * Database Management Systems
 * Department of Computer Science &amp; Engineering
 * University of California - Riverside
 *
 * Target DBMS: 'Postgres'
 *
 */


import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 * This class defines a simple embedded SQL utility class that is designed to
 * work with PostgreSQL JDBC drivers.
 *
 */
public class DBProject {

   // reference to physical database connection.
   private Connection _connection = null;

   // handling the keyboard inputs through a BufferedReader
   // This variable can be global for convenience.
   static BufferedReader in = new BufferedReader(
                                new InputStreamReader(System.in));

   /**
    * Creates a new instance of DBProject
    *
    * @param hostname the MySQL or PostgreSQL server hostname
    * @param database the name of the database
    * @param username the user name used to login to the database
    * @param password the user login password
    * @throws java.sql.SQLException when failed to make a connection.
    */
   public DBProject (String dbname, String dbport, String user, String passwd) throws SQLException {

      System.out.print("Connecting to database...");
      try{
         // constructs the connection URL
         String url = "jdbc:postgresql://localhost:" + dbport + "/" + dbname;
         System.out.println ("Connection URL: " + url + "\n");

         // obtain a physical connection
         this._connection = DriverManager.getConnection(url, user, passwd);
         System.out.println("Done");
      }catch (Exception e){
         System.err.println("Error - Unable to Connect to Database: " + e.getMessage() );
         System.out.println("Make sure you started postgres on this machine");
         System.exit(-1);
      }//end catch
   }//end DBProject

   /**
    * Method to execute an update SQL statement.  Update SQL instructions
    * includes CREATE, INSERT, UPDATE, DELETE, and DROP.
    *
    * @param sql the input SQL string
    * @throws java.sql.SQLException when update failed
    */
   public void executeUpdate (String sql) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the update instruction
      stmt.executeUpdate (sql);

      // close the instruction
      stmt.close ();
   }//end executeUpdate

   /**
    * Method to execute an input query SQL instruction (i.e. SELECT).  This
    * method issues the query to the DBMS and outputs the results to
    * standard out.
    *
    * @param query the input query string
    * @return the number of rows returned
    * @throws java.sql.SQLException when failed to execute the query
    */
   public int executeQuery (String query) throws SQLException {
      // creates a statement object
      Statement stmt = this._connection.createStatement ();

      // issues the query instruction
      ResultSet rs = stmt.executeQuery (query);

      /*
       ** obtains the metadata object for the returned result set.  The metadata
       ** contains row and column info.
       */
      ResultSetMetaData rsmd = rs.getMetaData ();
      int numCol = rsmd.getColumnCount ();
      int rowCount = 0;

      // iterates through the result set and output them to standard out.
      boolean outputHeader = true;
      while (rs.next()){
	 if(outputHeader){
	    for(int i = 1; i <= numCol; i++){
		System.out.print(rsmd.getColumnName(i) + "\t");
	    }
	    System.out.println();
	    outputHeader = false;
	 }
         for (int i=1; i<=numCol; ++i)
            System.out.print (rs.getString (i) + "\t");
         System.out.println ();
         ++rowCount;
      }//end while
      stmt.close ();
      return rowCount;
   }//end executeQuery

   /**
    * Method to close the physical connection if it is open.
    */
   public void cleanup(){
      try{
         if (this._connection != null){
            this._connection.close ();
         }//end if
      }catch (SQLException e){
         // ignored.
      }//end try
   }//end cleanup

   /**
    * The main execution method
    *
    * @param args the command line arguments this inclues the <mysql|pgsql> <login file>
    */
   public static void main (String[] args) {
      if (args.length != 3) {
         System.err.println (
            "Usage: " +
            "java [-classpath <classpath>] " +
            DBProject.class.getName () +
            " <dbname> <port> <user>");
         return;
      }//end if
      
      Greeting();
      DBProject esql = null;
      try{
         // use postgres JDBC driver.
         Class.forName ("org.postgresql.Driver").newInstance ();
         // instantiate the DBProject object and creates a physical
         // connection.
         String dbname = args[0];
         String dbport = args[1];
         String user = args[2];
         esql = new DBProject (dbname, dbport, user, "");

         boolean keepon = true;
         while(keepon) {
            // These are sample SQL statements
				System.out.println("MAIN MENU");
				System.out.println("---------");
				System.out.println("1. Add new customer");
				System.out.println("2. Add new room");
				System.out.println("3. Add new maintenance company");
				System.out.println("4. Add new repair");
				System.out.println("5. Add new Booking"); 
				System.out.println("6. Assign house cleaning staff to a room");
				System.out.println("7. Raise a repair request");
				System.out.println("8. Get number of available rooms");
				System.out.println("9. Get number of booked rooms");
				System.out.println("10. Get hotel bookings for a week");
				System.out.println("11. Get top k rooms with highest price for a date range");
				System.out.println("12. Get top k highest booking price for a customer");
				System.out.println("13. Get customer total cost occurred for a give date range"); 
				System.out.println("14. List the repairs made by maintenance company");
				System.out.println("15. Get top k maintenance companies based on repair count");
				System.out.println("16. Get number of repairs occurred per year for a given hotel room");
				System.out.println("17. < EXIT");

            switch (readChoice()){
				   case 1: addCustomer(esql); break;
				   case 2: addRoom(esql); break;
				   case 3: addMaintenanceCompany(esql); break;
				   case 4: addRepair(esql); break;
				   case 5: bookRoom(esql); break;
				   case 6: assignHouseCleaningToRoom(esql); break;
				   case 7: repairRequest(esql); break;
				   case 8: numberOfAvailableRooms(esql); break;
				   case 9: numberOfBookedRooms(esql); break;
				   case 10: listHotelRoomBookingsForAWeek(esql); break;
				   case 11: topKHighestRoomPriceForADateRange(esql); break;
				   case 12: topKHighestPriceBookingsForACustomer(esql); break;
				   case 13: totalCostForCustomer(esql); break;
				   case 14: listRepairsMade(esql); break;
				   case 15: topKMaintenanceCompany(esql); break;
				   case 16: numberOfRepairsForEachRoomPerYear(esql); break;
				   case 17: keepon = false; break;
				   default : System.out.println("Unrecognized choice!"); break;
            }//end switch
         }//end while
      }catch(Exception e) {
         System.err.println (e.getMessage ());
      }finally{
         // make sure to cleanup the created table and close the connection.
         try{
            if(esql != null) {
               System.out.print("Disconnecting from database...");
               esql.cleanup ();
               System.out.println("Done\n\nBye !");
            }//end if
         }catch (Exception e) {
            // ignored.
         }//end try
      }//end try
   }//end main
   
   public static void Greeting(){
      System.out.println(
         "\n\n*******************************************************\n" +
         "              User Interface      	               \n" +
         "*******************************************************\n");
   }//end Greeting

   /*
    * Reads the users choice given from the keyboard
    * @int
    **/
   public static int readChoice() {
      int input;
      // returns only if a correct value is given.
      do {
         System.out.print("Please make your choice: ");
         try { // read the integer, parse it and break.
            input = Integer.parseInt(in.readLine());
            break;
         }catch (Exception e) {
            System.out.println("Your input is invalid!");
            continue;
         }//end try
      }while (true);
      return input;
   }//end readChoice

   
   public static void addCustomer(DBProject esql){//DONE
	  // Given customer details add the customer in the DB 
      // Your code goes here.
   	int customerID;
   	while(true){
   		System.out.print("Input Customer ID: ");
   		try {
   			customerID = Integer.parseInt(in.readLine());
   			break;
   		}catch(Exception e) {
   			System.out.println("This customerID does not exist!");
   			continue;
   		}
   	};

   	String fName;
   	while(true){
   		System.out.print("Input Customer first name: ");
   		try {
   			fName = in.readLine();
   			if(fName.length() > 30) {
   				throw new RuntimeException("First name cannot be longer than 30 characters");
   			}
   			break;
   		} catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};

   	String lName;
   	while(true){
   		System.out.print("Input Customer last name: ");
   		try {
   			lName = in.readLine();
   			if(fName.length() > 30) {
   				throw new RuntimeException("Last name cannot be longer than 30 characters");
   			}
   			break;
   		} catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};

   	
    String address;
   	while(true){
   		System.out.print("Input Customer address: ");
   		try {
   			address = in.readLine();
   			if(address.length() == 0) {
   				throw new RuntimeException("Customer address cannot be left blank");
   			}
   			break;
   		} catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};

   	int phNo;
   	while(true){
   		System.out.print("Input Customer phone number: ");
   		try {
   			phNo = Integer.parseInt(in.readLine());
   			if(phNo <= 0) {
   				throw new RuntimeException("Customer phone number cannot be left blank");
   			}
   			break;
   		} catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};

   	int yearInput;
   	int monthInput;
   	int dayInput;
   	boolean isLeap;
   	while(true){
		System.out.println("Date is in format MM/dd/yyyy.");
   		System.out.print("Input Customer birth year: ");
   		try {
   			yearInput = Integer.parseInt(in.readLine());
   			if(yearInput == 0) {
   				throw new RuntimeException("Customer birth year cannot be left blank");
   			}
   			if(yearInput <= 0 || yearInput > 9999) {
				throw new RuntimeException("Please input valid year (1 - 9999).");
			}
   			break;
   		}catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};

	// Checking for leap year.
	if(yearInput % 4 == 0) {
		if(yearInput % 100 == 0) {
			if(yearInput % 400 == 0) {
				isLeap = true;
			} else {
				isLeap = false;
			}
		} else {
			isLeap = true;
		}
	} else {
		isLeap = false;
	}

   	while(true){
   		System.out.print("Input Customer birth month: ");
   		try {
   			monthInput = Integer.parseInt(in.readLine());
   			if(monthInput <= 0 || monthInput > 12) {
   				throw new RuntimeException("Customer birth month cannot be left blank and cannot be greater than 12 (December)");
   			}
   			break;
   		}catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};
   	while(true){
   		System.out.print("Input Customer birth day: ");
   		try {
   			dayInput = Integer.parseInt(in.readLine());
   			if(monthInput == 1 || monthInput == 3 || monthInput == 5 || monthInput == 7 || monthInput == 8 || monthInput == 10 || monthInput == 12) {
				if(dayInput <= 0 || dayInput > 31) {
					throw new RuntimeException("Please input valid date.");
				}
			}
			if(monthInput == 4 || monthInput == 6 || monthInput == 9 || monthInput == 11) {
				if(dayInput <= 0 || dayInput > 30) {
					throw new RuntimeException("Please input valid date.");
				}
			}
			if(monthInput == 2) {
				if(isLeap) {
					if(dayInput <= 0 || dayInput > 29) {
						throw new RuntimeException("Please input valid date.");
					}
				} else {
					if(dayInput <= 0 || dayInput > 28) {
						throw new RuntimeException("Please input valid date.");
					}
				}
			}
   			break;
   		}catch (Exception e) {
   			System.out.println("Your input is invalid!");
   			continue;
   		}
   	};
   	//SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
   	//Date finaldate = new Date(monthInput,dayInput,yearInput);
    String finaldate = monthInput + "/" + dayInput + "/" + yearInput;
    System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
    System.out.println(finaldate);
    //Date dob = new SimpleDateFormat.parse(finaldate);
   	//String dob = simpleDateFormat.format(finaldate);
    String dob = finaldate;
	
   	String gender;
   	while(true){
   		System.out.print("Input Customer Gender ( 'Male', 'Female', or 'Other'): ");
   		try{
   			gender = in.readLine();
   			if(!(gender.equals("Male") || gender.equals("Female") || gender.equals("Other"))) {
   				throw new RuntimeException("Customer gender must be identified through 'Male', 'Female', or 'Other' ");
   			}
   			break;
   		}catch (Exception e) {
   			System.out.println("Your Input is invalid!");
   			continue;
   		}
   	}

    String query;
    try{
   		query = "INSERT INTO Customer ( customerID, fName, lName, Address, phNo, DOB, gender) VALUES (" + customerID + ", \'" + fName + "\', \'" + lName + "\', \'" + address + "\', \'" + phNo + "\', \'" + dob + "\',\'" + gender + "\');";
   		//query = "INSERT INTO Customer ( customerID, fName, lName, Address, phNo, DOB, gender) VALUES (" + customerID + "\', \'" + fName + "\', \'" + lName + "\', \'" + address + "\', \'" + phNo + "\', \'" + dob + "\', \'" + gender + "\'');\'";
   		esql.executeUpdate(query);
    }catch(Exception e) {
    	System.err.println("Query failed: " + e.getMessage());
    }
 	// ...
 	// ...
   }//end addCustomer

   public static void addRoom(DBProject esql){//DONE
	  // Given room details add the room in the DB
   	int hotelID;
   	while(true){
   		System.out.print("Input the hotelID for the room: ");
   		try{
   			hotelID = Integer.parseInt(in.readLine());
   			break;
   		}catch (Exception e) {
   			System.out.println("Your Input is invalid!");
   			continue;
   		}
   	}

   	int roomNo;
   	while(true){
   		System.out.print("Input the Room Number: ");
   		try{
   		roomNo = Integer.parseInt(in.readLine());
   		break;
   		}catch (Exception e) {
   			System.out.println("Your Input is invalid!");
   			continue;
   		}
   	}

    String roomType;
    while(true){
    	System.out.print("Input the Room Type: ");
    	try{
    		roomType = in.readLine();
    		if(roomType.length() <= 0 || roomType.length() > 10) {
    			throw new RuntimeException("RoomType cannot be longer than 10 characters!");
    		}
    		break;
    	}catch (Exception e) {
    		System.out.println("Your input is invalid!");
    		continue;
    	}
    }

    String query;
    try{
   		query = "INSERT INTO Room ( hotelID, roomNo, roomType) VALUES (" + hotelID + ", \'" + roomNo + "\', \'" + roomType + "\');";
   		esql.executeUpdate(query);
    }catch(Exception e) {
    	System.err.println("Query failed: " + e.getMessage());
    }
      // Your code goes here.
      // ...
      // ...
   }//end addRoom

	public static void addMaintenanceCompany(DBProject esql){//DONE
		// Given maintenance Company details add the maintenance company in the DB
		
		int compID;
		while(true) {
			System.out.print("Input Company ID: ");
			try {
				compID = Integer.parseInt(in.readLine());
				break;
			}catch(Exception e) {
				System.out.println("This compID does not exist!");
				continue;
			}
		};
		
		String compName;
		while(true) {
			System.out.print("Input Company Name: ");
			try {
				compName = in.readLine();
				if(compName.length() > 30) {
					throw new RuntimeException("Company name cannot be longer than 30 characters");
				}
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};	  
		
		String compAddress;
		while(true) {
			System.out.print("Input Company Address: ");
			try {
				compAddress = in.readLine();
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
		
		String compCertified;
		boolean isCertified;
		while(true) {
			System.out.print("Is this company certified? (y/n): ");
			try {
				compCertified = in.readLine();
				if(compCertified.equals("y") || compCertified.equals("Y")) {
					isCertified = true;
				} else if(compCertified.equals("n") || compCertified.equals("N")) {
					isCertified = false;
				} else {
					throw new RuntimeException("Please enter \'y\' or \'n\'");
				}
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
		
		String query;
		try{
			query = "INSERT INTO MaintenanceCompany (cmpID, name, address, isCertified) VALUES (" + compID + ", \'" + compName + "\', \'" + compAddress + "\', \'" + isCertified + "\');";
			esql.executeUpdate(query);
		}catch(Exception e) {
			System.err.println("Query failed: " + e.getMessage());
		}
      // ...
      // ...
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){//DONE
	  // Given repair details add repair in the DB
      // Your code goes here.
      
		int repairID;
		while(true) {
			System.out.print("Input repair ID: ");
			try {
				repairID = Integer.parseInt(in.readLine());
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
	  
		int hotelID;
		while(true) {
			System.out.print("Input hotel ID: ");
			try {
				hotelID = Integer.parseInt(in.readLine());
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
	  
		int roomNum;
		while(true) {
			System.out.print("Input room number: ");
			try {
				roomNum = Integer.parseInt(in.readLine());
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
	  
		int maintCompany;
		while(true) {
			System.out.print("Input maintenance company ID: ");
			try {
				maintCompany = Integer.parseInt(in.readLine());
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
	  
		int yearInput;
		int monthInput;
		int dayInput;
		boolean isLeap;
	  
		while(true) {
			System.out.print("Input Repair date year: ");
			try{
				yearInput = Integer.parseInt(in.readLine());
				if(yearInput == 0) {
					throw new RuntimeException("Repair date year cannot be left blank.");
				}
				if(yearInput <= 0 || yearInput > 9999) {
					throw new RuntimeException("Please input valid year (1 - 9999).");
				}
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
	  
		// Checking for leap year.
		if(yearInput % 4 == 0) {
			if(yearInput % 100 == 0) {
				if(yearInput % 400 == 0) {
					isLeap = true;
				} else {
					isLeap = false;
				}
			} else {
				isLeap = true;
			}
		} else {
			isLeap = false;
		}

	  while(true) {
		  System.out.print("Input Repair date month: ");
		  try {
			  monthInput = Integer.parseInt(in.readLine());
			  if(monthInput == 0) {
				  throw new RuntimeException("Repair date month cannot be left blank.");
			  }
			  if(monthInput < 0 || monthInput > 12) {
				  throw new RuntimeException("Please input valid month (1 - 12).");
			  }
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  };
	  while(true) {
		  System.out.print("Input Repair date day: ");
		  try {
			  dayInput = Integer.parseInt(in.readLine());
			  if(dayInput == 0) {
				  throw new RuntimeException("Repair date day cannot be left blank.");
			  }
			  if(monthInput == 1 || monthInput == 3 || monthInput == 5 || monthInput == 7 || monthInput == 8 || monthInput == 10 || monthInput == 12) {
				  if(dayInput <= 0 || dayInput > 31) {
					  throw new RuntimeException("Please input valid date.");
				  }
			  }
			  if(monthInput == 4 || monthInput == 6 || monthInput == 9 || monthInput == 11) {
				  if(dayInput <= 0 || dayInput > 30) {
					  throw new RuntimeException("Please input valid date.");
				  }
			  }
			  if(monthInput == 2) {
				  if(dayInput <= 0 || dayInput > 28) {
					  throw new RuntimeException("Please input valid date.");
				  }
			  }
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  };
    //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
    //Date finaldate = new Date(monthInput,dayInput,yearInput);
    //String repairDate = simpleDateFormat.format(finaldate);
    //System.out.println("YOUR GIVEN DATE IS " + repairDate);
  
    String finaldate = monthInput + "/" + dayInput + "/" + yearInput;
    System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
    System.out.println(finaldate);
    //Date dob = new SimpleDateFormat.parse(finaldate);
    //String dob = simpleDateFormat.format(finaldate);
    String repairDate = finaldate;


		String description;
		while(true) {
			System.out.print("Input repair description: ");
			try {
				description = in.readLine();
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
		
		String repairType;
		while(true) {
			System.out.print("Input repair type: ");
			try {
				repairType = in.readLine();
				if(repairType.length() > 30) {
					throw new RuntimeException("Repair type cannot be longer than 30 characters.");
				}
				break;
			} catch (Exception e) {
				System.out.println("Your input is invalid!");
				continue;
			}
		};
	  
		String query;
		try {
			query = "INSERT INTO Repair (rID, hotelID, roomNo, mCompany, repairDate, description, repairType) VALUES (" + repairID + ", \'" + hotelID + "\', \'" + roomNum + "\', \'" + maintCompany + "\', \'" + repairDate + "\', \'" + description + "\', \'" + repairType + "\');";
			esql.executeUpdate(query);
		} catch (Exception e) {
			System.err.println("Query failed: " + e.getMessage());
	    }
	  // ...
      // ...
   }//end addRepair

   public static void bookRoom(DBProject esql){//DONE
	  // Given hotelID, roomNo and customer Name create a booking in the DB 
      // Your code goes here.
      int hotelID;
      int roomNo;
      int customerID;
    	String query;
    	String input;
    	int bID;
    	int bookingyear;
    	int bookingmonth;
    	int bookingday;
    	int noOfPeople;
    	double price;
	    String temp;
      String tempdate;
    	while(true){
        	System.out.print("Please input Customer ID: ");
        	try{
          		customerID = Integer.parseInt(in.readLine());
          		break;
        	}catch (Exception e) {
          	System.out.println("Your Input is invalid!");
          	continue;
        	}
     	}

      while(true){
        System.out.print("Please input Hotel ID: ");
        try{
          hotelID = Integer.parseInt(in.readLine());
          break;
        }catch (Exception e) {
          System.out.println("Your Input is invalid!");
          continue;
        }
      }

      while(true){
        System.out.print("Please input Room Number: ");
        try{
          roomNo = Integer.parseInt(in.readLine());
          break;
        }catch (Exception e) {
          System.out.println("Your Input is invalid!");
          continue;
        }
      }

    	try{//try1
      	query = "SELECT bID\nFROM Booking\nWHERE hotelID = " + hotelID + " AND roomNo = " + roomNo + " AND customer = " + customerID + ";";
      	if(esql.executeQuery(query) == 0) {
      		while(true){
      			System.out.println("Your Booking does not yet exist. Would you like to create a new Booking?(y/n): ");
      			try{//try2
      				input = in.readLine();
      				if(input.equals("y") || input.equals("Y")) {
							  while(true){
								  System.out.print("Please input Booking Number: ");
								  try{//try3
									 bID = Integer.parseInt(in.readLine());
									 break;
								  }catch (Exception e) {// catch1
							      System.out.println("Your input is invalid!");
						      }
      				  }
                while(true){
                  System.out.println("Booking Date is required!");
                  System.out.print("Please input Booking Date Year: ");
                  try{
                    bookingyear = Integer.parseInt(in.readLine());
                    if(bookingyear == 0) {
                      throw new RuntimeException("Booking date year cannot be left blank.");
                    }
                    if(bookingyear < 0 || bookingyear > 9999) {
                      throw new RuntimeException("Please input valid year (1 - 9999).");
                    }
                    break;
                  }catch(Exception e) {
                    System.out.println("Your input is invalid");
                  }
                }
                while(true){
                  System.out.print("Please input Booking Date Month: ");
                  try{
                    bookingmonth = Integer.parseInt(in.readLine());
                    if(bookingmonth == 0) {
                      throw new RuntimeException("Booking date month cannot be left blank.");
                    }
                    if(bookingmonth < 0 || bookingmonth > 12) {
                      throw new RuntimeException("Please input valid month (1 - 12).");
                    }
                    break;
                  }catch(Exception e) {
                    System.out.println("Your input is invalid");
               	  }
               	}
               	while(true){
               	  System.out.print("Please input Booking Date Day: ");
                  try{
                    bookingday = Integer.parseInt(in.readLine());
                    if(bookingday == 0) {
                      throw new RuntimeException("Booking date day cannot be left blank.");
                    }
                    if(bookingmonth == 1 || bookingmonth == 3 || bookingmonth == 5 || bookingmonth == 7 || bookingmonth == 8 || bookingmonth == 10 || bookingmonth == 12) {
                      if(bookingday <= 0 || bookingday > 31) {
                        throw new RuntimeException("Please input valid date.");
                      }
                    }
                    if(bookingmonth == 4 || bookingmonth == 6 || bookingmonth == 9 || bookingmonth == 11) {
                      if(bookingday <= 0 || bookingday > 30) {
                        throw new RuntimeException("Please input valid date.");
                      }
                    }
                    if(bookingmonth == 2) {
                      if(bookingday <= 0 || bookingday > 28) {
                        throw new RuntimeException("Please input valid date.");
                      }
                    }
                    break;
                  }catch(Exception e) {
                    System.out.println("Your input is invalid");
                  }
                }

                String finaldate = bookingmonth + "/" + bookingday + "/" + bookingyear;
                System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
                System.out.println(finaldate);
                temp = finaldate;
			          
                while(true){
                 	System.out.print("Please input the number of People for the Booking: ");
                 	try{
                    noOfPeople = Integer.parseInt(in.readLine());
                    break;
                 	}catch (Exception e) {
                   	System.out.println("Your input is invalid!");
                  }
                }
                			//PRICE IS DATATYPE NUMERIC(6,2) XXXXXX.XX <==== FIX
                while(true) {
                  System.out.print("Please input the Price: ");
                  try{
                    price = Double.parseDouble(in.readLine());
                    break;
                  }catch(Exception e) {
                    System.out.println("Your input is invalid!");
                  }
                }
              }
      			}catch(Exception e){
            	System.out.println("Your input is invalid!");
              continue;
            }
        	}
          try{
            query = "INSERT INTO Booking( bID, customer, hotelID, roomNo, bookingDate, noOfPeople, price) VALUES (" + bID + ", \'" + customerID + "\', \'" + hotelID + "\', \'" + roomNo + "\', \'" + temp + "\', \'" + noOfPeople + "\', \'" + price + "\');";
            esql.executeUpdate(query);
            break;
          }catch(Exception e){
            System.out.println("Query failed: " + e.getMessage());
          }
      	}

    	}catch(Exception e){
        System.out.println("Your input is invalid!");
    }    			
      // ...
  }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){//DONE
	  // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      // Your code goes here.

	int assignedID = 2001;

	int staffSSN;
	while(true) {
		System.out.println("Please input Staff SSN: ");
		try {
			staffSSN = Integer.parseInt(in.readLine());
			break;
		} catch (Exception e) {
			System.out.println("Your input is invalid!");
			continue;
		}
	}

	int hotelID;
	while(true) {
		System.out.println("Please input Hotel ID: ");
		try {
			hotelID = Integer.parseInt(in.readLine());
			break;
		} catch (Exception e) {
			System.out.println("Your input is invalid!");
			continue;
		}
	}

	int roomNum;
	while(true) {
		System.out.println("Please input Room number: ");
		try {
			roomNum = Integer.parseInt(in.readLine());
			break;
		} catch (Exception e) {
			System.out.println("Your input is invalid!");
			continue;
		}
	}

	String query;
		try {
			query = "INSERT INTO Assigned (asgID, staffID, hotelID, roomNo) VALUES (" + assignedID + ", " + staffSSN + ", " + hotelID + ", " + roomNum + ");";
			esql.executeUpdate(query);
			assignedID = assignedID + 1;
		} catch (Exception e) {
			System.out.println("Query failed: " + e.getMessage());
		}		
      // ...
      // ...
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){//DONE
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
      // Your code goes here.
      int request = 2000;
      
      int hotelID;
      while(true) {
		  System.out.println("Please input Hotel ID: ");
		  try {
			  hotelID = Integer.parseInt(in.readLine());
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	  
	  int staffSSN;
	  while(true) {
		  System.out.println("Please input Staff SSN: ");
		  try {
			  staffSSN = Integer.parseInt(in.readLine());
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	  
	  int roomNum;
	  while(true) {
		  System.out.println("Please input Room number: ");
		  try {
			  roomNum = Integer.parseInt(in.readLine());
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	  
	  int repairID;
	  while(true) {
		  System.out.println("Please input Repair ID: ");
		  try {
			  repairID = Integer.parseInt(in.readLine());
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	  
	  String description;
	  while(true) {
		  System.out.println("Please input a description: ");
		  try {
			  description = in.readLine();
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
    int yearInput;
    int monthInput;
    int dayInput;
    boolean isLeap;
    
    while(true) {
      System.out.print("Input Repair date year: ");
      try{
        yearInput = Integer.parseInt(in.readLine());
        if(yearInput == 0) {
          throw new RuntimeException("Repair date year cannot be left blank.");
        }
        if(yearInput <= 0 || yearInput > 9999) {
          throw new RuntimeException("Please input valid year (1 - 9999).");
        }
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        continue;
      }
    };
    
    // Checking for leap year.
    if(yearInput % 4 == 0) {
      if(yearInput % 100 == 0) {
        if(yearInput % 400 == 0) {
          isLeap = true;
        } else {
          isLeap = false;
        }
      } else {
        isLeap = true;
      }
    } else {
      isLeap = false;
    }

    while(true) {
      System.out.print("Input Repair date month: ");
      try {
        monthInput = Integer.parseInt(in.readLine());
        if(monthInput == 0) {
          throw new RuntimeException("Repair date month cannot be left blank.");
        }
        if(monthInput < 0 || monthInput > 12) {
          throw new RuntimeException("Please input valid month (1 - 12).");
        }
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        continue;
      }
    };
    while(true) {
      System.out.print("Input Repair date day: ");
      try {
        dayInput = Integer.parseInt(in.readLine());
        if(dayInput == 0) {
          throw new RuntimeException("Repair date day cannot be left blank.");
        }
        if(monthInput == 1 || monthInput == 3 || monthInput == 5 || monthInput == 7 || monthInput == 8 || monthInput == 10 || monthInput == 12) {
          if(dayInput <= 0 || dayInput > 31) {
            throw new RuntimeException("Please input valid date.");
          }
        }
        if(monthInput == 4 || monthInput == 6 || monthInput == 9 || monthInput == 11) {
          if(dayInput <= 0 || dayInput > 30) {
            throw new RuntimeException("Please input valid date.");
          }
        }
        if(monthInput == 2) {
          if(dayInput <= 0 || dayInput > 28) {
            throw new RuntimeException("Please input valid date.");
          }
        }
        break;
      } catch (Exception e) {
        System.out.println("Your input is invalid!");
        continue;
      }
    };
    String finaldate = monthInput + "/" + dayInput + "/" + yearInput;
    System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
    System.out.println(finaldate);

    String requestDate = finaldate;

	  String query;
	  while(true) {
		  try {
			  query = "INSERT INTO Request (reqID, managerID, repairID, requestDate, description) VALUES (" + request + ", \'" + staffSSN + ", \'" + repairID + ", \'" + requestDate + ", \'" + description + "\');";
			  esql.executeQuery(query);
			  request = request + 1;
		  } catch (Exception e) {
			  System.out.println("Query failed: " + e.getMessage());
		  }
	  }
      // ...
      // ...
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){//DONE
	  // Given a hotelID, get the count of rooms available  (NUM OF TOTAL ROOMS OF THE HOTEL - THE NUMBER ROOMS BOOKED)
      // Your code goes here.
   		int hotelID;
   		while(true){
   			System.out.println("Please input hotel ID: ");
   			try{
   				hotelID = Integer.parseInt(in.readLine());
   				break;
   			}catch(Exception e){
   				System.out.println("Your Input is invalid!");
   				continue;
   			}
   		}
;
   		String query;
   			try{
   				//query = "SELECT R.hotelID, A1-B1 FROM (SELECT R.roomNo\nFROM Room R\nWHERE R.hotelID = " + hotelID + ") as A1, (SELECT B.roomNo\nFROM Booking B\nWHERE B.hotelID = " + hotelID + ") as B1)";
				query = "SELECT COUNT(R.roomNo) FROM Room R WHERE R.hotelID = " + hotelID + "\nEXCEPT\nSELECT COUNT(B.roomNo) FROM Booking B WHERE B.hotelID = " + hotelID + ";";
   				esql.executeQuery(query);
   			}catch(Exception e) {
   				System.out.println("Query failed: " + e.getMessage());
   			}
      // ...
      // ...
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){//DONE
	  // Given a hotelID, get the count of rooms booked
      // Your code goes here.
   		int hotelID;

   		while(true){
   			System.out.println("Please input hotel ID: ");
   			try{
   				hotelID = Integer.parseInt(in.readLine());
   				break;
   			}catch(Exception e){
   				System.out.println("Your Input is invalid!");
   				continue;
   			}
   		}
   		String query;
   		while(true){
   			try{
   				query = "SELECT count(B.roomNo)\nFROM Booking B WHERE hotelID = " + hotelID + ";";
   				esql.executeQuery(query);
   				break;
   			}catch(Exception e){
   				System.out.println("Query failed: " + e.getMessage());
   			}
   		}
      // ...
      // ...
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      // Your code goes here.
   		int hotelID;
   		int checkyear;
   		int checkmonth;
   		int checkday;
   		while(true){
   			System.out.println("Please input hotel ID: ");
   			try{
   				hotelID = Integer.parseInt(in.readLine());
   				break;
   			}catch(Exception e){
   				System.out.println("Your Input is invalid!");
   				continue;
   			}
   		}
   		while(true){
            System.out.print("Please input Booking Date Year: ");
            try{
                checkyear = Integer.parseInt(in.readLine());
                if(checkyear == 0) {
                    throw new RuntimeException("Booking date year cannot be left blank.");
                }
                if(checkyear < 0 || checkyear > 9999) {
                    throw new RuntimeException("Please input valid year (1 - 9999).");
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        while(true){
            System.out.print("Please input Booking Date Month: ");
            try{
                checkmonth = Integer.parseInt(in.readLine());
                if(checkmonth == 0) {
                    throw new RuntimeException("Booking date month cannot be left blank.");
                }
                if(checkmonth < 0 || checkmonth > 12) {
                    throw new RuntimeException("Please input valid month (1 - 12).");
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        while(true){
            System.out.print("Please input Booking Date Day: ");
            try{
                checkday = Integer.parseInt(in.readLine());
                if(checkday == 0) {
                    throw new RuntimeException("Booking date day cannot be left blank.");
                }
                if(checkmonth == 1 || checkmonth == 3 || checkmonth == 5 || checkmonth == 7 || checkmonth == 8 || checkmonth == 10 || checkmonth == 12) {
                    if(checkday <= 0 || checkday > 31) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                if(checkmonth == 4 || checkmonth == 6 || checkmonth == 9 || checkmonth == 11) {
                    if(checkday <= 0 || checkday > 30) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                if(checkmonth == 2) {
                    if(checkday <= 0 || checkday > 28) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        //Date finaldate = new Date(checkmonth,checkday,checkyear);
        //String dob = simpleDateFormat.format(finaldate);
        //System.out.println("YOUR GIVEN DATE IS " + dob);
        String finaldate = checkmonth + "/" + checkday + "/" + checkyear;
        String bookingDate = finaldate;
        String finalenddate = checkmonth + "/" + (checkday+7) + "/" + checkyear;
        String bookingendDate = finalenddate;
        System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
        System.out.println(finaldate);
        System.out.println("The Date after a week is: ");
        System.out.println(finalenddate);
        //Date dob = new SimpleDateFormat.parse(finaldate);
        //String dob = simpleDateFormat.format(finaldate);
        

   		String query;
   			try{
   				query = "SELECT R.roomNo FROM Booking B, Room R WHERE B.hotelID = " + hotelID + " AND R.hotelID = " + hotelID + "AND B.bookingDate >= '" + bookingDate + "';"; //"' AND B.bookingDate <= '" + bookingendDate + "';";
          //esql.executeQuery(query);
          if(esql.executeQuery(query) != 0) {
            System.out.println("These are the available rooms in hotel (HotelID: " + hotelID + " ) this week. ");
          }
          else{
            System.out.println("There are no available rooms in this hotel this week.");
          }
   			}catch(Exception e){
				System.out.println("Query failed. " + e.getMessage());
   			}
      // ...
      // ...
   }//end listHotelRoomBookingsForAWeek
   
   public static void topKHighestRoomPriceForADateRange(DBProject esql){
	  // List Top K Rooms with the highest price for a given date range
      // Your code goes here.
	
      // ...
      // ...
   }//end topKHighestRoomPriceForADateRange
   
   public static void topKHighestPriceBookingsForACustomer(DBProject esql){
	  // Given a customer ID, List Top K highest booking price for a customer 
      // Your code goes here.
      
      int customerID;
      while(true) {
		  System.out.print("Please enter customer ID: ");
		  try {
			  customerID = Integer.parseInt(in.readLine());
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }

	  
	  String k;
	  while(true) {
		  System.out.print("Please enter a range: ");
		  try {
			  k = in.readLine();
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	  
	  String query;
      try {
		  query = "SELECT price FROM Booking B, Customer C WHERE C.customerID = " + customerID + " ORDER BY price DESC LIMIT " + k + ";"; 
		  esql.executeQuery(query);
	  } catch (Exception e) {
		  System.err.println(e.getMessage());
	  }
      // ...
      // ...
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){//DONE
	  // Given a hotelID, customerID and date range get the total cost incurred by the customer
    //PRICE PER DAY = ROOM COST PER DAY
      // Your code goes here.
	int hotelID;
   	while(true){
   		System.out.println("Please input hotel ID: ");
   		try{
   			hotelID = Integer.parseInt(in.readLine());
   			break;
   		}catch(Exception e){
   			System.out.println("Your Input is invalid!");
   			continue;
   		}
   	}
	int customerID;
	while(true){
		System.out.println("Please input customer ID: ");
		try{
			customerID = Integer.parseInt(in.readLine());
			break;
		}catch(Exception e){
			System.out.println("Your Input is invalid!");
			continue;
		}
	}

	int checkmonth;
	int checkday;
	int checkyear;
	while(true){
	    System.out.print("A starting date is required for the date range.");
            System.out.print("Please input Starting Date Year: ");
            try{
                checkyear = Integer.parseInt(in.readLine());
                if(checkyear == 0) {
                    throw new RuntimeException("Starting date year cannot be left blank.");
                }
                if(checkyear < 0 || checkyear > 9999) {
                    throw new RuntimeException("Please input valid year (1 - 9999).");
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        while(true){
            System.out.print("Please input starting Date Month: ");
            try{
                checkmonth = Integer.parseInt(in.readLine());
                if(checkmonth == 0) {
                    throw new RuntimeException("starting date month cannot be left blank.");
                }
                if(checkmonth < 0 || checkmonth > 12) {
                    throw new RuntimeException("Please input valid month (1 - 12).");
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        while(true){
            System.out.print("Please input starting Date Day: ");
            try{
                checkday = Integer.parseInt(in.readLine());
                if(checkday == 0) {
                    throw new RuntimeException("starting date day cannot be left blank.");
                }
                if(checkmonth == 1 || checkmonth == 3 || checkmonth == 5 || checkmonth == 7 || checkmonth == 8 || checkmonth == 10 || checkmonth == 12) {
                    if(checkday <= 0 || checkday > 31) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                if(checkmonth == 4 || checkmonth == 6 || checkmonth == 9 || checkmonth == 11) {
                    if(checkday <= 0 || checkday > 30) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                if(checkmonth == 2) {
                    if(checkday <= 0 || checkday > 28) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }

        String finaldate = checkmonth + "/" + checkday + "/" + checkyear;
        System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
        System.out.println(finaldate);
        String startDate = finaldate;

	while(true){
	    System.out.print("An End date is required for the date range.");
            System.out.print("Please input End Date Year: ");
            try{
                checkyear = Integer.parseInt(in.readLine());
                if(checkyear == 0) {
                    throw new RuntimeException("End date year cannot be left blank.");
                }
                if(checkyear < 0 || checkyear > 9999) {
                    throw new RuntimeException("Please input valid year (1 - 9999).");
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        while(true){
            System.out.print("Please input End Date Month: ");
            try{
                checkmonth = Integer.parseInt(in.readLine());
                if(checkmonth == 0) {
                    throw new RuntimeException("End date month cannot be left blank.");
                }
                if(checkmonth < 0 || checkmonth > 12) {
                    throw new RuntimeException("Please input valid month (1 - 12).");
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }
        while(true){
            System.out.print("Please input End Date Day: ");
            try{
                checkday = Integer.parseInt(in.readLine());
                if(checkday == 0) {
                    throw new RuntimeException("End date day cannot be left blank.");
                }
                if(checkmonth == 1 || checkmonth == 3 || checkmonth == 5 || checkmonth == 7 || checkmonth == 8 || checkmonth == 10 || checkmonth == 12) {
                    if(checkday <= 0 || checkday > 31) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                if(checkmonth == 4 || checkmonth == 6 || checkmonth == 9 || checkmonth == 11) {
                    if(checkday <= 0 || checkday > 30) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                if(checkmonth == 2) {
                    if(checkday <= 0 || checkday > 28) {
                        throw new RuntimeException("Please input valid date.");
                    }
                }
                break;
            }catch(Exception e) {
                System.out.println("Your input is invalid");
            }
        }

        String finishdate = checkmonth + "/" + checkday + "/" + checkyear;
        System.out.println("Your inputted date is (In the format of MM/dd/yyyy ) : ");
        System.out.println(finishdate);
        String endDate = finishdate;



	String query;
		try{
			query = "SELECT C.customerID, SUM(B.price) FROM Customer C, Booking B WHERE B.hotelID = " + hotelID + " AND C.customerID = " + customerID +  /*" AND B.bookingDate >= " + startDate + " AND B.bookingDate <= " + endDate + */" GROUP BY C.customerID;";
			esql.executeQuery(query);
		}catch(Exception e){
			System.out.println("Query failed. " + e.getMessage());
		}

      // ...
      // ...
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){//DONE
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
      
	int maintID;      
	//String maintName;
      while(true) {
		  //System.out.println("Please enter maintenance company name: ");
			System.out.println("Please enter maintenance company ID: ");		  
		try {
			  //maintName = in.readLine();
			maintID = Integer.parseInt(in.readLine());
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
      
	String maintName;
      while(true) {
		  System.out.println("Please enter maintenance company name: ");  
		try {
			  maintName = in.readLine();
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	
	  
	  String query;
	  try{
		  //query = "SELECT M.name, R.rID, R.repairType, R.hotelID, R.roomNo FROM Repair R, MaintenanceCompany M WHERE M.name = " + maintName + " AND M.cmpID = R.mCompany;";
query = "SELECT M.name, R.rID, R.repairType, R.hotelID, R.roomNo FROM Repair R, MaintenanceCompany M WHERE M.cmpID = " + maintID + " AND M.cmpID = R.mCompany;";
		  esql.executeQuery(query);
	  } catch (Exception e) {
		  System.err.println(e.getMessage());
	  }
      
      // ...
      // ...
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){//DONE
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      // Your code goes here.
      
	  
	  String k;
	  while(true) {
		  System.out.println("Please enter a range: ");
		  try {
			  k = in.readLine();
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  }
	  
	  String query;
	  try {
		  query = "SELECT M.name, COUNT(DISTINCT R.rID)\nFROM MaintenanceCompany M, Repair R WHERE M.cmpID = R.mCompany GROUP BY M.name ORDER BY COUNT(DISTINCT R.rID) DESC LIMIT " + k + ";";
		  esql.executeQuery(query);
	  } catch (Exception e) {
		  System.err.println(e.getMessage());
	  }
      // ...
      // ...
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
	String query;
      // ...
      // ...
   }//end listRepairsMade

}//end DBProject
