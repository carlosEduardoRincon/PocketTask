package br.edu.ifsp.dpdm.taskhub;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.edu.ifsp.dpdm.taskhub.dao.entities.TaskDAO;
import br.edu.ifsp.dpdm.taskhub.model.Task;

public class TaskManager extends Activity {

    DatePickerDialog picker;
    private Task t;
    private List<Task> tasks;
    private TaskDAO taskDAO;
    private EditText edID;
    private EditText edTitle;
    private EditText edDescription;
    private EditText edDeadline;
    private CheckBox ckCompleted;

    private String operation;
    private Spinner spPriority;
    private String[] priorities = {"High", "Medium", "Low"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        edID = (EditText) findViewById(R.id.edID);
        edTitle = (EditText) findViewById(R.id.edTitle);
        edDescription = (EditText) findViewById(R.id.edDescription);
        edDeadline = (EditText) findViewById(R.id.edDate);

        edDeadline.setInputType(InputType.TYPE_NULL);
        edDeadline.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(TaskManager.this,
                    (view, year1, monthOfYear, dayOfMonth) -> edDeadline.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1), year, month, day);
            picker.show();
        });

        spPriority = (Spinner) findViewById(R.id.spPriority);
        ckCompleted = (CheckBox) findViewById(R.id.ckCompleted);
        taskDAO = new TaskDAO(getApplicationContext());

        Intent intent = getIntent();
        if (intent.hasExtra("selectedItem")) {
            loadData(Objects.requireNonNull(intent.getParcelableExtra("selectedItem")));
            operation = intent.getParcelableExtra("operation");
        } else {
            operation = "New";
        }

        loadPriorities();
    }

    private void loadPriorities() {
        try {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, priorities);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spPriority.setAdapter(adapter);
        } catch (Exception ex) {
            showMessage("Erro: " + ex.getMessage());
        }
    }

    public void saveTask(View v) {

        if (operation != null && operation.equalsIgnoreCase("New")) {
            t = new Task();
        }

        t.setTitle(edTitle.getText().toString());
        t.setDescription(edDescription.getText().toString());
        t.setPriority(priorities[spPriority.getSelectedItemPosition()]);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            t.setDeadline((dateFormat.parse(edDeadline.getText().toString())));
        } catch (ParseException e) {/*IGNORE*/}
        t.setCompleted(ckCompleted.isChecked());

        if (operation != null && operation.equalsIgnoreCase("new")) {
            taskDAO.salvar(t);
            showMessage("Task added with success!");
        } else {
            taskDAO.atualizar(t);
            showMessage("Task updated with success!");
        }

        cleanData();
    }

    public void novo(View v) {
        operation = new String("Novo");
        cleanData();
    }

    private void cleanData() {
        edID.setText("");
        edTitle.setText("");
        edDescription.setText("");
        ckCompleted.setChecked(false);
        edDeadline.setText("");
        spPriority.setSelection(0);
    }


    public void loadData(Task task) {
        t = task;
        edID.setText(String.valueOf(task.getId()));
        edTitle.setText(task.getTitle());
        edDescription.setText(task.getDescription());
        ckCompleted.setChecked(task.isCompleted());
        edDeadline.setText(String.valueOf(task.getDeadline()));

        String priority = task.getPriority();
        Log.v("TaskManager", "msg: " + priority);

        int selectionPosition = 0;
        if (priority.equalsIgnoreCase("Medium")) {
            selectionPosition = 1;
        } else if (priority.equalsIgnoreCase("Low")) {
            selectionPosition = 2;
        }
        spPriority.setSelection(selectionPosition);

    }

    private void showMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        Intent it = new Intent();
        setResult(4, it);
        finish();
    }
}
