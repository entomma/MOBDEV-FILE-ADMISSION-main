package com.example.formadmission;
import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class submission extends AppCompatActivity {

    // Declare as class-level variables
    private EditText firstNameEditText, middleNameEditText, lastNameEditText, phoneNumberEditText, emailEditText;
    private EditText streetAddressEditText, barangayEditText, cityEditText, provinceEditText, zipCodeEditText;
    private Spinner birthMonthSpinner, birthDaySpinner, birthYearSpinner;
    private RadioGroup genderGroup;
    private TextView errorFirstName, errorPhoneNumber, errorMiddleName, errorLastName, errorMonth, errorDay, errorYear, errorEmailAddress, errorCourse;
    private CheckBox course_cs, course_business_admin, course_mechanical_engg, course_electrical_engg, course_civil_engg, course_medicine, course_psychology, course_arts_humanities, course_biotechnology, course_law;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submission);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Log.d("PopulateForm", "Student ID: " + intent.getIntExtra("student_id", -1));
        Log.d("PopulateForm", "First Name: " + intent.getStringExtra("first_name"));
        Log.d("PopulateForm", "Gender: " + intent.getStringExtra("gender"));
        Log.d("PopulateForm", "Birth Date: " + intent.getStringExtra("birth_date"));
        Log.d("PopulateForm", "City: " + intent.getStringExtra("city"));
        Log.d("PopulateForm", "Courses: " + intent.getStringArrayListExtra("course_id"));
        Log.d("PopulateForm", "province: " + intent.getStringArrayListExtra("province"));


        // Initialize UI components
        firstNameEditText = findViewById(R.id.firstName);
        middleNameEditText = findViewById(R.id.middleName);
        lastNameEditText = findViewById(R.id.lastName);
        phoneNumberEditText = findViewById(R.id.phoneNumber);
        emailEditText = findViewById(R.id.emailAddress);
        streetAddressEditText = findViewById(R.id.streetAddress);
        barangayEditText = findViewById(R.id.barangay);
        cityEditText = findViewById(R.id.city);
        provinceEditText = findViewById(R.id.province);
        zipCodeEditText = findViewById(R.id.zipCode);

        // Gender RadioGroup
        genderGroup = findViewById(R.id.genderGroup);

        // Error TextViews
        errorFirstName = findViewById(R.id.errorFirstName);
        errorPhoneNumber = findViewById(R.id.errorPhoneNumber);
        errorLastName = findViewById(R.id.errorLastName);
        errorMiddleName = findViewById(R.id.errorMiddleName);
        errorMonth = findViewById(R.id.errorMonth);
        errorDay = findViewById(R.id.errorDay);
        errorYear = findViewById(R.id.errorYear);
        errorEmailAddress = findViewById(R.id.errorEmailAddress);
        errorCourse = findViewById(R.id.errorCourse);

        // Course Checkboxes
        course_psychology = findViewById(R.id.course_psychology);
        course_biotechnology = findViewById(R.id.course_biotechnology);
        course_cs = findViewById(R.id.course_cs);
        course_business_admin = findViewById(R.id.course_business_admin);
        course_mechanical_engg = findViewById(R.id.course_mechanical_engg);
        course_electrical_engg = findViewById(R.id.course_electrical_engg);
        course_civil_engg = findViewById(R.id.course_civil_engg);
        course_medicine = findViewById(R.id.course_medicine);
        course_arts_humanities = findViewById(R.id.course_arts_humanities);
        course_law = findViewById(R.id.course_law);

        // Spinners for birthdate
        birthMonthSpinner = findViewById(R.id.birthMonth);
        birthDaySpinner = findViewById(R.id.birthDay);
        birthYearSpinner = findViewById(R.id.birthYear);

        // Initialize Spinners with values
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_dropdown_item);
        birthMonthSpinner.setAdapter(monthAdapter);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_array, android.R.layout.simple_spinner_dropdown_item);
        birthDaySpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> yearAdapter = ArrayAdapter.createFromResource(this,
                R.array.years_array, android.R.layout.simple_spinner_dropdown_item);
        birthYearSpinner.setAdapter(yearAdapter);

        Button submitButton = findViewById(R.id.submitBtn);
        submitButton.setOnClickListener(v -> saveStudentData());

        // Retrieve data passed from previous screen (if any)
        populateFormFields(intent);
    }

    private void populateFormFields(Intent intent) {
        int studentId = intent.getIntExtra("student_id", -1); // Default value is -1
        String firstName = intent.getStringExtra("first_name");
        String middleName = intent.getStringExtra("middle_name");
        String lastName = intent.getStringExtra("last_name");
        String gender = intent.getStringExtra("gender");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("phone_number");
        String barangay = intent.getStringExtra("barangay");
        String zip = intent.getStringExtra("zipcode");
        String street = intent.getStringExtra("street");
        String city = intent.getStringExtra("city");
        String province = intent.getStringExtra("province");
        String birthDate = intent.getStringExtra("birth_date");
        ArrayList<String> selectedCourses = intent.getStringArrayListExtra("course_id"); // List of selected courses

        // Log values for debugging
        Log.d("PopulateForm", "First Name: " + firstName);
        Log.d("PopulateForm", "Gender: " + gender);
        Log.d("PopulateForm", "Birth Date: " + birthDate);
        Log.d("PopulateForm", "Courses: " + selectedCourses);
        Log.d("PopulateForm", "Courses: " + zip);
        Log.d("PopulateForm", "province: " + province);

        // Fill form fields
        firstNameEditText.setText(firstName);
        middleNameEditText.setText(middleName);
        lastNameEditText.setText(lastName);
        barangayEditText.setText(barangay);
        emailEditText.setText(email);
        phoneNumberEditText.setText(phone);
        streetAddressEditText.setText(street);
        zipCodeEditText.setText(zip);
        cityEditText.setText(city);
        provinceEditText.setText(province);

        // Handle gender radio buttons (Male/Female)
        RadioButton radioMale = findViewById(R.id.radioMale);
        RadioButton radioFemale = findViewById(R.id.radioFemale);
        if ("Male".equals(gender)) {
            radioMale.setChecked(true);
        } else if ("Female".equals(gender)) {
            radioFemale.setChecked(true);
        }

        // Handle courses (checkboxes)
        if (selectedCourses != null) {
            for (String course : selectedCourses) {
                switch (course) {
                    case "1": course_psychology.setChecked(true); break;
                    case "2": course_biotechnology.setChecked(true); break;
                    case "3": course_cs.setChecked(true); break;
                    case "4": course_business_admin.setChecked(true); break;
                    case "5": course_mechanical_engg.setChecked(true); break;
                    case "6": course_electrical_engg.setChecked(true); break;
                    case "7": course_civil_engg.setChecked(true); break;
                    case "8": course_medicine.setChecked(true); break;
                    case "9": course_arts_humanities.setChecked(true); break;
                    case "10": course_law.setChecked(true); break;
                }
            }
        }

        // Handle birthdate (split it into year, month, and day)
        if (birthDate != null && !birthDate.isEmpty()) {
            String[] birthDateParts = birthDate.split("-");
            String birthYear = birthDateParts[0];
            String birthMonth = birthDateParts[1];
            String birthDay = birthDateParts[2];

            // Set the selection of spinners
            birthYearSpinner.setSelection(getIndex(birthYearSpinner, birthYear));
            birthMonthSpinner.setSelection(getIndex(birthMonthSpinner, birthMonth));
            birthDaySpinner.setSelection(getIndex(birthDaySpinner, birthDay));
        }
    }

    private int getIndex(Spinner spinner, String item) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equals(item)) {
                return i;
            }
        }
        return 0; // Default to first item if not found
    }

    public void saveStudentData() {
        if (!validateForm()) {
            return; // Stop if validation fails
        }

        // Collect form data
        String firstName = firstNameEditText.getText().toString().trim();
        String middleName = middleNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String birthMonth = birthMonthSpinner.getSelectedItem().toString();
        String birthDay = birthDaySpinner.getSelectedItem().toString();
        String birthYear = birthYearSpinner.getSelectedItem().toString();

        // Gender
        int selectedGenderId = genderGroup.getCheckedRadioButtonId();
        RadioButton genderRadioButton = findViewById(selectedGenderId);
        String gender = genderRadioButton.getText().toString();

        // Contacts
        String phoneNumber = phoneNumberEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        // Address
        String streetAddress = streetAddressEditText.getText().toString().trim();
        String barangay = barangayEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String province = provinceEditText.getText().toString().trim();
        String zipCode = zipCodeEditText.getText().toString().trim();

        // Create date of birth string (format: yyyy-MM-dd)
        String dobString = birthYear + "-" + String.format("%02d", birthMonthSpinner.getSelectedItemPosition()) + "-" + String.format("%02d", birthDaySpinner.getSelectedItemPosition());
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to save the record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    new Thread(() -> {
                        ConnectionHelper connectionHelper = new ConnectionHelper();
                        Connection connection = connectionHelper.connect_to_db("FormAdmission", "postgres", "leerajenn");
                        if (connection != null) {
                            try {
                                connection.setAutoCommit(false);
                                String studentSQL = "INSERT INTO students DEFAULT VALUES RETURNING student_id";
                                int studentID = 0;
                                try (PreparedStatement stmt = connection.prepareStatement(studentSQL)) {
                                    ResultSet rs = stmt.executeQuery();
                                    if (rs.next()) {
                                        studentID = rs.getInt("student_id");
                                    }
                                }
                                String nameSQL = "INSERT INTO studentname (student_id, first_name, middle_name, last_name, gender) VALUES (?, ?, ?, ?, ?)";
                                try (PreparedStatement stmt = connection.prepareStatement(nameSQL)) {
                                    stmt.setInt(1, studentID);
                                    stmt.setString(2, firstName);
                                    stmt.setString(3, middleName);
                                    stmt.setString(4, lastName);
                                    stmt.setString(5, gender);
                                    stmt.executeUpdate();
                                }
                                String contactSQL = "INSERT INTO studentcontact (student_id, email, phone_number) VALUES (?, ?, ?)";
                                try (PreparedStatement stmt = connection.prepareStatement(contactSQL)) {
                                    stmt.setInt(1, studentID);
                                    stmt.setString(2, email);
                                    stmt.setString(3, phoneNumber);
                                    stmt.executeUpdate();
                                }
                                String bdaySQL = "INSERT INTO studentbirth (student_id, date_of_births) VALUES (?, ?)";
                                try (PreparedStatement stmt = connection.prepareStatement(bdaySQL)) {
                                    stmt.setInt(1, studentID);
                                    stmt.setString(2, dobString);
                                    stmt.executeUpdate();
                                }
                                String addressSQL = "INSERT INTO studentaddress (student_id, street, barangay, city, province, zipcode) VALUES (?, ?, ?, ?, ?, ?)";
                                try (PreparedStatement stmt = connection.prepareStatement(addressSQL)) {
                                    stmt.setInt(1, studentID);
                                    stmt.setString(2, streetAddress);
                                    stmt.setString(3, barangay);
                                    stmt.setString(4, city);
                                    stmt.setString(5, province);
                                    stmt.setString(6, zipCode);
                                    stmt.executeUpdate();
                                }
                                String courseSQL = "INSERT INTO studentpreference (student_id, course_id) VALUES (?, ?)";
                                try (PreparedStatement stmt = connection.prepareStatement(courseSQL)) {
                                    stmt.setInt(1, studentID);
                                    if (course_cs.isChecked()) {
                                        stmt.setInt(2, 1);
                                        stmt.executeUpdate();
                                    }
                                    if (course_business_admin.isChecked()) {
                                        stmt.setInt(2, 2);
                                        stmt.executeUpdate();
                                    }
                                    if (course_mechanical_engg.isChecked()) {
                                        stmt.setInt(2, 3);
                                        stmt.executeUpdate();
                                    }
                                }
                                connection.commit();
                                runOnUiThread(() -> {
                                    Toast.makeText(submission.this, "Student record saved successfully!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(submission.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                });
                            } catch (SQLException e) {
                                e.printStackTrace();
                                runOnUiThread(() -> {
                                    Toast.makeText(submission.this, "Error inserting data into database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });
                            }
                        } else {
                            runOnUiThread(() -> {
                                Toast.makeText(submission.this, "Database connection failed!", Toast.LENGTH_SHORT).show();
                            });
                        }
                    }).start();
                })
                .setNegativeButton("No", null)
                .show();
    }
    private boolean validateForm() {
        boolean isValid = true;

        // Reset all error messages
        errorFirstName.setVisibility(View.GONE);
        errorMiddleName.setVisibility(View.GONE);
        errorLastName.setVisibility(View.GONE);
        errorPhoneNumber.setVisibility(View.GONE);
        errorEmailAddress.setVisibility(View.GONE);
        errorMonth.setVisibility(View.GONE);
        errorDay.setVisibility(View.GONE);
        errorYear.setVisibility(View.GONE);
        errorCourse.setVisibility(View.GONE);

        // Validate Name Fields
        if (firstNameEditText.getText().toString().trim().isEmpty()) {
            errorFirstName.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (lastNameEditText.getText().toString().trim().isEmpty()) {
            errorLastName.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Validate Phone and Email
        String phone = phoneNumberEditText.getText().toString().trim();
        if (phone.isEmpty() || !phone.startsWith("09") || phone.length() != 11) {
            errorPhoneNumber.setVisibility(View.VISIBLE);
            isValid = false;
        }

        String email = emailEditText.getText().toString().trim();
        if (email.isEmpty() || (!email.endsWith("@gmail.com") && !email.endsWith("@yahoo.com"))) {
            errorEmailAddress.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Validate Gender
        if (genderGroup.getCheckedRadioButtonId() == -1) {
            isValid = false;
        }

        // Validate Date of Birth (Month, Day, Year)
        if (birthMonthSpinner.getSelectedItemPosition() == 0) {
            errorMonth.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (birthDaySpinner.getSelectedItemPosition() == 0) {
            errorDay.setVisibility(View.VISIBLE);
            isValid = false;
        }
        if (birthYearSpinner.getSelectedItemPosition() == 0) {
            errorYear.setVisibility(View.VISIBLE);
            isValid = false;
        }

        // Validate Course
        boolean courseSelected = course_psychology.isChecked() || course_biotechnology.isChecked()
                || course_cs.isChecked() || course_business_admin.isChecked()
                || course_mechanical_engg.isChecked() || course_electrical_engg.isChecked()
                || course_civil_engg.isChecked() || course_medicine.isChecked()
                || course_arts_humanities.isChecked() || course_law.isChecked();

        if (!courseSelected) {
            errorCourse.setVisibility(View.VISIBLE);
            isValid = false;
        }

        return isValid;
    }
}