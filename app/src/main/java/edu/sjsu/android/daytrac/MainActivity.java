package edu.sjsu.android.daytrac;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class MainActivity extends BaseClass implements MyAdapter.OnCardListener, AddTaskFragment.FragmentListener, GraphFragment.FragmentListener{
    private static ArrayList<Task> tasks;
    private static ArrayList<TaskHistory> taskHistories;

    public static ArrayList<String> taskHistoriesStrings;
    private RecyclerView recyclerView;
    private Button addTaskButton;
    private FloatingActionButton graphButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTaskButton = findViewById(R.id.addTaskButton);
        // Set the click listener for the button.
        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayFragment(1);
                addTaskButton.setVisibility(View.GONE);
                graphButton.setVisibility(View.GONE);
            }
        });

        graphButton = findViewById(R.id.graphButton);
        graphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayFragment(2);
                addTaskButton.setVisibility(View.GONE);
                graphButton.setVisibility(View.GONE);
            }
        });

        loadData();
        endOfDayOperations();

        taskHistoriesStrings = new ArrayList<>();
        for(int i = 0; i < taskHistories.size(); i++){
            taskHistoriesStrings.add(taskHistories.get(i).getName());
        }

        //set adapter to custom adapter with the data
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new MyAdapter(tasks, this));

        // Set onSwiped
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                tasks.remove(viewHolder.getAdapterPosition());
                recyclerView.getAdapter().notifyItemRemoved(viewHolder.getAdapterPosition());
                saveData();
            }

            //Disables swiping by returning 0 for swipe flag
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }
        };
        // Attach to the RecyclerView
        ItemTouchHelper itemTouch = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouch.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.clearHistoryButton:
                taskHistories = new ArrayList<>();
                taskHistoriesStrings = new ArrayList<>();
                return true;
            case R.id.uninstallButton:
                Intent delete = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + getPackageName()));
                startActivity(delete);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCardClick(int position) {
    }

    @Override
    public void onCardChanged() {
        saveData();
    }

    public void endOfDayOperations(){

        if(!tasks.isEmpty()){
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String strTodaysDate = df.format(Calendar.getInstance().getTime());
            Date todaysDate = null;
            try {
                todaysDate = df.parse(strTodaysDate);
            }catch(ParseException e){
                e.printStackTrace();
            }
            //Do something if the tasks were not assigned for today
            if(tasks.get(0).getDateAdded().before(todaysDate)){
                Log.d("EOD", "OLD TASKS PRESENT");
                Iterator<Task> it = tasks.iterator();
                while(it.hasNext()){
                    Task currentTask = it.next();
                    boolean match = false;
                    Iterator <TaskHistory> itHistory = taskHistories.iterator();
                    //iterate through taskHistories to see if current task has been stored before
                    while(!match && itHistory.hasNext()){
                        TaskHistory currentTaskHistory = itHistory.next();
                        //If match is found
                        if(currentTask.getName().compareTo(currentTaskHistory.getName()) == 0){
                            match = true;
                            //if task was completed increment the historical record of successes
                            if(currentTask.isbCompleted() == true){
                                currentTaskHistory.setSuccesses(currentTaskHistory.getSuccesses() + 1);
                            }else{
                                //if task was not completed increment the historical record of failures
                                currentTaskHistory.setFailures(currentTaskHistory.getFailures() + 1);
                            }
                        }
                    }
                    //If no match was found after searching through entire history
                    if(!match){
                        taskHistories.add(new TaskHistory(currentTask.getName(),currentTask.getDateAdded(), currentTask.isbCompleted()));
                    }
                }
                tasks.clear();
                saveData();
            }
            Log.d("EOD", "All tasks current");

            Iterator<TaskHistory> it = taskHistories.iterator();
            while(it.hasNext()){
                TaskHistory currentTask = it.next();
            }
        }
    }

    public void displayFragment(int choice) {
        if(choice == 1) {
            AddTaskFragment addTaskFragment = AddTaskFragment.newInstance(this);
            // Get the FragmentManager and start a transaction.
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Add the addTaskFragment.
            fragmentTransaction.add(R.id.fragment_container, addTaskFragment).addToBackStack(null).commit();
            // Set boolean flag to indicate fragment is open.
        }else if(choice == 2){
            GraphFragment graphFragment = GraphFragment.newInstance(this);
            // Get the FragmentManager and start a transaction.
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            // Add the addTaskFragment.
            fragmentTransaction.add(R.id.fragment_container, graphFragment).addToBackStack(null).commit();
        }
    }


    public void closeFragment(int choice) {
        if(choice == 1) {
            // Get the FragmentManager.
            FragmentManager fragmentManager = getSupportFragmentManager();
            // Check to see if the fragment is already showing.
            AddTaskFragment addTaskFragment = (AddTaskFragment) fragmentManager
                    .findFragmentById(R.id.fragment_container);
            if (addTaskFragment != null) {
                // Create and commit the transaction to remove the fragment.
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.remove(addTaskFragment).commit();
            }
            recyclerView.getAdapter().notifyDataSetChanged();
            saveData();
            addTaskButton.setVisibility(View.VISIBLE);
            graphButton.setVisibility(View.VISIBLE);
        }else if(choice == 2){
            // Get the FragmentManager.
            FragmentManager fragmentManager = getSupportFragmentManager();
            // Check to see if the fragment is already showing.
            GraphFragment graphFragment = (GraphFragment) fragmentManager
                    .findFragmentById(R.id.fragment_container);
            if (graphFragment != null) {
                // Create and commit the transaction to remove the fragment.
                FragmentTransaction fragmentTransaction =
                        fragmentManager.beginTransaction();
                fragmentTransaction.remove(graphFragment).commit();
            }
            addTaskButton.setVisibility(View.VISIBLE);
            graphButton.setVisibility(View.VISIBLE);
        }
    }

    private void loadData() {
        // method to load arraylist from shared prefs
        // initializing our shared prefs with name as
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for gson.
        Gson gson = new Gson();
        Gson gson2 = new Gson();

        // below line is to get to string present from our
        // shared prefs if not present setting it as null.
        String json = sharedPreferences.getString("tasks", null);
        String json2 = sharedPreferences.getString("taskHistories", null);

        // below line is to get the type of our array list.
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        Type type2 = new TypeToken<ArrayList<TaskHistory>>() {}.getType();

        // in below line we are getting data from gson
        // and saving it to our array list
        tasks = gson.fromJson(json, type);
        taskHistories = gson2.fromJson(json2, type2);

        // checking below if the array list is empty or not
        if (tasks == null) {
            // if the array list is empty
            // creating a new array list.
            tasks = new ArrayList<>();
        }

        // checking below if the array list is empty or not
        if (taskHistories == null) {
            // if the array list is empty
            // creating a new array list.
            taskHistories = new ArrayList<>();
        }
    }

    private void saveData() {
        // method for saving the data in array list.
        // creating a variable for storing data in
        // shared preferences.
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);

        // creating a variable for editor to
        // store data in shared preferences.
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // creating a new variable for gson.
        Gson gson = new Gson();
        Gson gson2 = new Gson();

        // getting data from gson and storing it in a string.
        String json = gson.toJson(tasks);
        String json2 = gson2.toJson(taskHistories);

        // below line is to save data in shared
        // prefs in the form of string.
        editor.putString("tasks", json);
        editor.putString("taskHistories", json2);

        // below line is to apply changes
        // and save data in shared prefs.
        editor.apply();

        // after saving data we are displaying a toast message.
        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }


    public static ArrayList<Task> getTasks(){
        return tasks;
    }

    public static ArrayList<TaskHistory> getTaskHistories(){
        return taskHistories;
    }


    @Override
    public void onCloseFragment(int choice) {
        closeFragment(choice);
    }
}