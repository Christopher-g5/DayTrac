package edu.sjsu.android.daytrac;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static edu.sjsu.android.daytrac.MainActivity.getTasks;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddTaskFragment extends Fragment{

    private String newTask;
    private AutoCompleteTextView userInput;
    private Button addButton;
    private Button closeButton;
    private static FragmentListener mFragmentListener;

    public AddTaskFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View rootview = inflater.inflate(R.layout.fragment_add_task, container, false);

        userInput = rootview.findViewById(R.id.newTask);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, MainActivity.taskHistoriesStrings);
        userInput.setAdapter(adapter);

        addButton = rootview.findViewById(R.id.confirmAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                String strDate = df.format(Calendar.getInstance().getTime());
                //Date date = new Date();
                Date date = null;
                try {
                    date = df.parse(strDate);
                }catch(ParseException e){
                    e.printStackTrace();
                }

                ArrayList<Task> tasks = getTasks();
                newTask = userInput.getText().toString();
                if(newTask.equals("")){

                }else{
                    tasks.add(new Task(newTask, date));
                }
                mFragmentListener.onCloseFragment(1);
            }
        });

        closeButton = rootview.findViewById(R.id.closeButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragmentListener.onCloseFragment(1);
            }
        });

        return rootview;
    }

    public static AddTaskFragment newInstance(FragmentListener fragmentListener) {
        mFragmentListener = fragmentListener;
        return new AddTaskFragment();
    }

    public interface FragmentListener{
        void onCloseFragment(int choice);
    }
}