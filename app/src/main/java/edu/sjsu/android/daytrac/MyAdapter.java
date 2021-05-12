package edu.sjsu.android.daytrac;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<Task> tasks;
    private OnCardListener mOnCardListener;

    public MyAdapter(ArrayList<Task> dataSet, OnCardListener onCardListener){
        tasks = dataSet;
        this.mOnCardListener = onCardListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //Create a new view which defines the UI of the list item
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view, mOnCardListener);
    }


    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        //Get element from your dataset at this position and replace the contents of the view with that element
        final Task task = tasks.get(position);
        viewHolder.name.setText(task.getName());
        viewHolder.bComplete.setChecked(task.isbCompleted());
        //Set listener for checkbox to update object's bChecked bool
        viewHolder.bComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                task.setbCompleted(isChecked);
                mOnCardListener.onCardChanged();
                Log.d("CHECK","The task is" + task.getName()+ " in position " + position + " bCompleted = " + isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    //matches variables with views on UI
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public CheckBox bComplete;
        public OnCardListener onCardListener;

        public ViewHolder(View view, OnCardListener onCardListener){
            super(view);
            name = view.findViewById(R.id.task);
            bComplete = view.findViewById(R.id.bComplete);
            this.onCardListener = onCardListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(bComplete.isChecked()==true){
            }
            onCardListener.onCardClick(getAdapterPosition());
        }
    }

    public interface OnCardListener{
        void onCardClick(int position);
        void onCardChanged();
    }
}
