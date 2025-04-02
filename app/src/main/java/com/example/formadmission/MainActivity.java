package com.example.formadmission;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Statement;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private StudentAdapter studentAdapter;
    private List<Student> studentList = new ArrayList<>();
    private Button move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        move = findViewById(R.id.mover);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, submission.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch students list asynchronously
        new FetchStudentsTask().execute();
    }

    // ✅ Move this method OUTSIDE FetchStudentsTask
    public void GetTextFromSQL(View v) {
        new GetDataTask().execute();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the student list (reload data from the database)
        new FetchStudentsTask();  // Call the method that loads the data into your ListView or RecyclerView
    }
    private class FetchStudentsTask extends AsyncTask<Void, Void, List<Student>> {
        @Override
        protected List<Student> doInBackground(Void... voids) {
            List<Student> students = new ArrayList<>();
            String sql = "SELECT sn.student_id, sn.first_name, sn.middle_name, sn.last_name, sc.email, sa.street, sa.barangay, sa.city, sa.province, sa.zipcode,  sc.phone_number " +
                    "FROM studentcontact sc " +
                    "INNER JOIN studentname sn ON sc.student_id = sn.student_id " +
                    "INNER JOIN studentaddress sa ON sc.student_id = sa.student_id";

            try (Connection conn = new ConnectionHelper().connect_to_db("FormAdmission", "postgres", "leerajenn");
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int studentid = rs.getInt("student_id");

                    // Explicitly log each field to check if it's being retrieved correctly
                    String firstName = rs.getString("first_name");
                    String middleName = rs.getString("middle_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    String street = rs.getString("street");
                    String barangay = rs.getString("barangay");
                    String city = rs.getString("city");
                    String province = rs.getString("province");
                    String zipcode = rs.getString("zipcode");
                    String phone_number = rs.getString("phone_number");

                    // Log the retrieved values
                    Log.d("PopulateForm", "First Name: " + firstName);
                    Log.d("PopulateForm", "Middle Name: " + middleName);
                    Log.d("PopulateForm", "Last Name: " + lastName);
                    Log.d("PopulateForm", "City: " + city);

                    // Form the full name
                    String fullname = firstName + " " + (middleName != null ? middleName + " " : "") + lastName;
                    String address = street + " " + barangay + " " + city + " " + province + " " + zipcode;

                    // Add student to the list
                    students.add(new Student(studentid, fullname, firstName, address, city , zipcode, province, barangay, street, email, phone_number));  // Make sure to pass the correct data

                    // Check if the fields are null
                    if (firstName == null || middleName == null || lastName == null || city == null) {
                        Log.e("PopulateForm", "Missing Data: " + studentid);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return students;
        }

        @Override
        protected void onPostExecute(List<Student> result) {
            if (!result.isEmpty()) {
                // Pass the OnItemClickListener to the StudentAdapter
                studentAdapter = new StudentAdapter(result, new StudentAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        // Get the selected student object
                        Student student = result.get(position);

                        // Pass the student data to the Submission activity
                        Intent intent = new Intent(MainActivity.this, submission.class);
                        intent.putExtra("student_id", student.getStudentid()); // Pass the student ID
                        intent.putExtra("first_name", student.getfirstName()); // Pass the full name
                        intent.putExtra("city", student.getCity()); // Pass the city
                        intent.putExtra("zipcode", student.getZip());
                        intent.putExtra("province", student.getProvince());
                        intent.putExtra("barangay", student.getBarangay());
                        intent.putExtra("street", student.getStreet());
                        intent.putExtra("email", student.getEmail());
                        intent.putExtra("phone_number", student.getPhone_number());
                        startActivity(intent);
                    }
                });

                recyclerView.setAdapter(studentAdapter);
                Toast.makeText(MainActivity.this, "Student Data Retrieved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to Retrieve Student Data", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class GetDataTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> studentDataList = new ArrayList<>();
            ConnectionHelper db = new ConnectionHelper();
            Connection connect = null;
            try {
                connect = db.connect_to_db("FormAdmission", "postgres", "leerajenn");
                if (connect != null) {
                    String query = "SELECT first_name FROM studentname;";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);

                    while (rs.next()) { // Fetch all student names
                        studentDataList.add(rs.getString(1));
                    }
                    rs.close();
                    stmt.close();
                    connect.close();  // Closing the connection after use
                }
            } catch (Exception e) {
                Log.e("DB Error", e.getMessage());
            }
            return studentDataList;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (!result.isEmpty()) {
                TextView tx1 = findViewById(R.id.textView1);
                TextView tx2 = findViewById(R.id.textView2);

                tx1.setText(result.get(0));  // First student name
                if (result.size() > 1) {
                    tx2.setText(result.get(1));  // Second student name (if exists)
                } else {
                    tx2.setText("N/A");
                }
                Toast.makeText(MainActivity.this, "Data Retrieved!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Failed to Retrieve Data", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
