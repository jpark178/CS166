CREATE INDEX index_customer_customerid ON Customer USING BTREE(customerID);
CREATE INDEX index_customer_fname ON Customer USING BTREE(fname);
CREATE INDEX index_customer_lname ON Customer USING BTREE(lname);
CREATE INDEX index_customer_address ON Customer USING BTREE(address);
CREATE INDEX index_customer_phNo ON Customer USING BTREE(phNo);
CREATE INDEX index_customer_DOB ON Customer USING BTREE(DOB);
CREATE INDEX index_customer_gender ON Customer USING BTREE(gender);

CREATE INDEX index_hotel_hotelid ON Hotel USING BTREE(hotelID);
CREATE INDEX index_hotel_address ON Hotel USING BTREE(address);
CREATE INDEX index_hotel_manager ON Hotel USING BTREE(manager);

CREATE INDEX index_staff_SSN ON Staff USING BTREE(SSN);
CREATE INDEX index_staff_fname ON Staff USING BTREE(fname);
CREATE INDEX index_staff_lname ON Staff USING BTREE(lname);
CREATE INDEX index_staff_address ON Staff USING BTREE(address);
CREATE INDEX index_staff_role ON Staff USING BTREE(role);
CREATE INDEX index_staff_employerID ON Staff USING BTREE(employerID);

CREATE INDEX index_room_hotelid ON Room USING BTREE(hotelID);
CREATE INDEX index_room_roomNo ON Room USING BTREE(roomNo);
CREATE INDEX index_room_roomType ON Room USING BTREE(roomType);

CREATE INDEX index_maintenancecompany_cmpid ON MaintenanceCompany USING BTREE(cmpID);
CREATE INDEX index_maintenancecompany_name ON MaintenanceCompany USING BTREE(name);
CREATE INDEX index_maintenancecompany_address ON MaintenanceCompany USING BTREE(address);
CREATE INDEX index_maintenancecompany_iscertified ON MaintenanceCompany USING BTREE(iscertified);

CREATE INDEX index_booking_bid ON Booking USING BTREE(bID);
CREATE INDEX index_booking_customer ON Booking USING BTREE(customer);
CREATE INDEX index_booking_hotelid ON Booking USING BTREE(hotelID);
CREATE INDEX index_booking_roomno ON Booking USING BTREE(roomNo);
CREATE INDEX index_booking_bookingdate ON Booking USING BTREE(bookingDate);
CREATE INDEX index_booking_noofpeople ON Booking USING BTREE(noOfPeople);
CREATE INDEX index_booking_price ON Booking USING BTREE(price);