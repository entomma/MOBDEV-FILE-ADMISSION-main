package com.example.formadmission;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnItemClickListener onItemClickListener;

    public StudentAdapter(List<Student> studentList, OnItemClickListener onItemClickListener) {
        this.studentList = studentList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_item, parent, false);
        return new StudentViewHolder(view, onItemClickListener);  // Pass the listener here
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentNumber.setText(student.getStudentNumber()); // Student number
        holder.tvStudentName.setText(student.getFullName()); // Full name
        holder.tvStudentAddress.setText(student.getAddress()); // Address

    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentNumber, tvStudentName, tvStudentAddress;

        public StudentViewHolder(View itemView, final OnItemClickListener onItemClickListener) {
            super(itemView);
            tvStudentNumber = itemView.findViewById(R.id.tvStudentNumber);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentAddress = itemView.findViewById(R.id.tvStudentAddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(getAdapterPosition());  // Pass position to listener
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
