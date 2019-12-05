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
import java.sql.Date;
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

   
   public static void addCustomer(DBProject esql){
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
   			if(phNo == 0) {
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
				if(dayInput < 0 || dayInput > 31) {
					throw new RuntimeException("Please input valid date.");
				}
			}
			if(monthInput == 4 || monthInput == 6 || monthInput == 9 || monthInput == 11) {
				if(dayInput < 0 || dayInput > 30) {
					throw new RuntimeException("Please input valid date.");
				}
			}
			if(monthInput == 2) {
				if(isLeap) {
					if(dayInput < 0 || dayInput > 29) {
						throw new RuntimeException("Please input valid date.");
					}
				} else {
					if(dayInput < 0 || dayInput > 28) {
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
   	Date DOB = new Date(yearInput,monthInput,dayInput);

   	String gender;
   	while(true){
   		System.out.print("Input Customer Gender ( 'Male', 'Female', or 'Other'): ");
   		try{
   			gender = in.readLine();
   			if(gender != "Male" || gender != "Female" || gender != "Other") {
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
   		query = "INSERT INTO Customer ( customerID, fName, lName, Address, phNo, DOB, gender) VALUES (" + customerID + ", \'" + fName + "\', \'" + lName + "\', \'" + address + "\', \'" + phNo + "\', \'" + gender + "\');";
   		esql.executeQuery(query);
    }catch(Exception e) {
    	System.err.println("Query failed: " + e.getMessage());
    }
 	// ...
 	// ...
   }//end addCustomer

   public static void addRoom(DBProject esql){
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
   		esql.executeQuery(query);
    }catch(Exception e) {
    	System.err.println("Query failed: " + e.getMessage());
    }
      // Your code goes here.
      // ...
      // ...
   }//end addRoom

	public static void addMaintenanceCompany(DBProject esql){
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
			System.out.print("Is this comapny certified? (y/n): ");
			try {
				compCertified = in.readLine();
				if(compCertified == "y" || compCertified == "Y") {
					isCertified = true;
				} else if(compCertified == "n" || compCertified == "N") {
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
			query = "INSERT INTO MainetenanceCompany (cmpID, name, address, isCertified) VALUES (" + compID + ", \'" + compName + "\', \'" + compAddress + "\', \'" + isCertified + "\');";
			esql.executeQuery(query);
		}catch(Exception e) {
			System.err.println("Query failed: " + e.getMessage());
		}
      // ...
      // ...
   }//end addMaintenanceCompany

   public static void addRepair(DBProject esql){
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
				  if(dayInput < 0 || dayInput > 31) {
					  throw new RuntimeException("Please input valid date.");
				  }
			  }
			  if(monthInput == 4 || monthInput == 6 || monthInput == 9 || monthInput == 11) {
				  if(dayInput < 0 || dayInput > 30) {
					  throw new RuntimeException("Please input valid date.");
				  }
			  }
			  if(monthInput == 2) {
				  if(dayInput < 0 || dayInput > 28) {
					  throw new RuntimeException("Please input valid date.");
				  }
			  }
			  break;
		  } catch (Exception e) {
			  System.out.println("Your input is invalid!");
			  continue;
		  }
	  };
	  
	  Date repairDate = new Date(yearInput, monthInput, dayInput);
		
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
		} catch (Exception e) {
			System.err.println("Query failed: " + e.getMessage());
	    }
	  // ...
      // ...
   }//end addRepair

   public static void bookRoom(DBProject esql){
	  // Given hotelID, roomNo and customer Name create a booking in the DB 
      // Your code goes here.
      int hotelID;
      int roomNo;
      int customerID;

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
      String query;
      String input;
      int bID;
      int bookingyear;
      int bookingmonth;
      int bookingday;
      Date bookingDate;
      int noOfPeople;
      int price;
      try{
      	query = "SELECT bID\nFROM Booking\nWHERE hotelID = " + hotelID + " AND roomNo = " + roomNo + " AND customer = " + customerID + ";";
      	if(esql.executeQuery(query) == 0) {
      		while(true){
      			System.out.println("Your Booking does not yet exist. Would you like to create a new Booking?(y/n): ");
      			try{
      				input = in.readLine();
      				if(input == "y" || input == "Y") {
						    while(true){
							   System.out.print("Please input Booking Number: ");
							   try{
								    bID = Integer.parseInt(in.readLine());
								    break;
							   }catch (Exception e) {
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
                  try{
                    bookingday = Integer.parseInt(in.readLine());
                    if(bookingday == 0) {
                      throw new RuntimeException("Booking date day cannot be left blank.");
                    }
                    if(bookingmonth == 1 || bookingmonth == 3 || bookingmonth == 5 || bookingmonth == 7 || bookingmonth == 8 || bookingmonth == 10 || bookingmonth == 12) {
                      if(bookingday < 0 || bookingday > 31) {
                        throw new RuntimeException("Please input valid date.");
                      }
                    }
                    if(bookingmonth == 4 || bookingmonth == 6 || bookingmonth == 9 || bookingmonth == 11) {
                      if(bookingday < 0 || bookingday > 30) {
                        throw new RuntimeException("Please input valid date.");
                      }
                    }
                    if(bookingmonth == 2) {
                      if(bookingday < 0 || bookingday > 28) {
                        throw new RuntimeException("Please input valid date.");
                      }
                    }
                    break;
                  }catch(Exception e) {
                    System.out.println("Your input is invalid");
                  }
                }
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
                    price = Integer.parseInt(in.readLine());
                    break;
                  }catch(Exception e) {
                    System.out.println("Your input is invalid!");
                  }
                }
                try{
                  query = "INSERT INTO Booking( bID, customer, hotelID, roomNo, bookingdate, noOfPeople, price) VALUES (" + bID + ", \'" + customerID + "\', \'" + hotelID + "\', \'" + roomNo + "\', \'" + bookingdate + "\', \'" + noOfPeople + "\', \'" + price + "\');";
                  esql.executeUpdate(query);
                }catch(Exception e){
                  System.out.println("Your input is invalid!");
                }
              }else if(input != "n" || input != "N") {
                throw new RuntimeException("Your input is invalid!");
              }
              break;
      			}catch(Exception e){
              System.out.println("Your input is invalid!");
              continue;
            }
      		}
      	}else { // if NOT n or N <=== if anything else , runtimeexception
          while(true){

          }
        }
      }catch(Exception e){
        System.out.println("Your input is invalid!");
      }
      // ...
      // ...
   }//end bookRoom

   public static void assignHouseCleaningToRoom(DBProject esql){
	  // Given Staff SSN, HotelID, roomNo Assign the staff to the room 
      // Your code goes here.
      // ...
      // ...
   }//end assignHouseCleaningToRoom
   
   public static void repairRequest(DBProject esql){
	  // Given a hotelID, Staff SSN, roomNo, repairID , date create a repair request in the DB
      // Your code goes here.
      // ...
      // ...
   }//end repairRequest
   
   public static void numberOfAvailableRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms available 
      // Your code goes here.
      // ...
      // ...
   }//end numberOfAvailableRooms
   
   public static void numberOfBookedRooms(DBProject esql){
	  // Given a hotelID, get the count of rooms booked
      // Your code goes here.
      // ...
      // ...
   }//end numberOfBookedRooms
   
   public static void listHotelRoomBookingsForAWeek(DBProject esql){
	  // Given a hotelID, date - list all the rooms available for a week(including the input date) 
      // Your code goes here.
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
	  // Given a customer Name, List Top K highest booking price for a customer 
      // Your code goes here.
      // ...
      // ...
   }//end topKHighestPriceBookingsForACustomer
   
   public static void totalCostForCustomer(DBProject esql){
	  // Given a hotelID, customer Name and date range get the total cost incurred by the customer
    //PRICE PER DAY = ROOM COST PER DAY
      // Your code goes here.
      // ...
      // ...
   }//end totalCostForCustomer
   
   public static void listRepairsMade(DBProject esql){
	  // Given a Maintenance company name list all the repairs along with repairType, hotelID and roomNo
      // Your code goes here.
      // ...
      // ...
   }//end listRepairsMade
   
   public static void topKMaintenanceCompany(DBProject esql){
	  // List Top K Maintenance Company Names based on total repair count (descending order)
      // Your code goes here.
      // ...
      // ...
   }//end topKMaintenanceCompany
   
   public static void numberOfRepairsForEachRoomPerYear(DBProject esql){
	  // Given a hotelID, roomNo, get the count of repairs per year
      // Your code goes here.
      // ...
      // ...
   }//end listRepairsMade

}//end DBProject
