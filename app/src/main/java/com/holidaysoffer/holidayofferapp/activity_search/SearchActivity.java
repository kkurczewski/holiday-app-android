package com.holidaysoffer.holidayofferapp.activity_search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.activity_main.fragments.SearchListFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public final class SearchActivity extends AppCompatActivity implements CalendarDatePickerDialogFragment.OnDateSetListener {

    private static final String DATE_PICKER_FRAGMENT_TAG = "date_picker_fragment";
    private static final String DATE_SEPARATOR = "/";

    private GridLayout checkBoxRoot;
    private EditText priceEditText;
    private EditText dateStart;

    private List<CheckBox> checkBoxes = new ArrayList<>();

    private int day;
    private int month;
    private int year;
    private int mealSelection;
    private int personsSelection;
    private Spinner mealsSpinner;
    private Spinner personCountSpinner;
    private TextInputLayout priceErrorLabel;
    private TextInputLayout dateErrorLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        checkBoxRoot = (GridLayout) findViewById(R.id.check_box_root);
        priceEditText = (EditText) findViewById(R.id.price_input);
        priceErrorLabel = (TextInputLayout) findViewById(R.id.price_error_label);
        dateErrorLabel = (TextInputLayout) findViewById(R.id.date_start_error_label);

        personCountSpinner = (Spinner) findViewById(R.id.person_count_spinner);
        mealsSpinner = (Spinner) findViewById(R.id.meal_spinner);

        dateStart = (EditText) findViewById(R.id.date_start_input);
        dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalendarDialog();
            }
        });

        getSupportActionBar().setTitle(R.string.title_search_activity);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSearchForm();
            }
        });

        setSpinner(R.array.persons_count_spinner, personCountSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                personsSelection = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                personsSelection = 0;
            }
        });
        setSpinner(R.array.meals_spinner, mealsSpinner, new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                mealSelection = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mealSelection = 0;
            }
        });
        setCheckBoxes();

        SearchParameters searchParameters = (SearchParameters) getIntent().getSerializableExtra(SearchListFragment.RETURNED_SEARCH_PARAMS);
        fillFields(searchParameters);
    }

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int month, int day) {
        setDateLabel(year, month, day);
    }

    protected int boolArrayToInt(boolean[] climateSelection) {
        int flag = 0;
        int weight = 1;
        for (boolean selection : climateSelection) {
            flag += selection ? weight : 0;
            weight *= 2;
        }

        return flag;
    }

    protected boolean[] getClimateSelection(int climateFlag, int bits) {
        boolean[] selection = new boolean[bits];

        for (int i = 0; i < bits; i++) {
            if (((1 << i) & climateFlag) > 0) {
                selection[i] = true;
            }
        }
        return selection;
    }

    private void fillFields(SearchParameters searchParameters) {
        if (searchParameters != null) {

            setDateLabel(searchParameters.getYear(), searchParameters.getMonth(), searchParameters.getDay());
            priceEditText.setText(String.valueOf(searchParameters.getPrice()));
            setSpinnerSelection(personCountSpinner, searchParameters.getPersons());
            setSpinnerSelection(mealsSpinner, searchParameters.getMealPreference());

            int size = checkBoxes.size();
            boolean[] selection = getClimateSelection(searchParameters.getClimateFlag(), size);

            for (int i = 0; i < size; i++) {
                checkBoxes.get(i).setChecked(selection[i]);
            }
        }
    }

    private void setDateLabel(int year, int month, int day) {
        dateStart.setText(day + DATE_SEPARATOR + (month + 1) + DATE_SEPARATOR + year);
        this.day = day;
        this.month = month;
        this.year = year;
    }

    private void setSpinnerSelection(Spinner spinner, int restoredOption) {
        // indexing from 0, 'not chosen' has value: -1 so adding 1 to get zero index
        spinner.setSelection(restoredOption + 1);
    }

    private void sendSearchForm() {

        boolean hasErrors = false;

        priceErrorLabel.setError(null);
        dateErrorLabel.setError(null);

        BigDecimal price = null;
        try {
            String priceString = String.valueOf(priceEditText.getText());
            if (!priceString.isEmpty()) {
                price = BigDecimal.valueOf(Double.parseDouble(priceString));
            } else {
                priceErrorLabel.setError(getString(R.string.empty_input));
                hasErrors = true;
            }
        } catch (NumberFormatException e) {
            priceErrorLabel.setError(getString(R.string.not_a_number_input));
            hasErrors = true;
        }

        if (dateStart.getText().toString().isEmpty()) {
            dateErrorLabel.setError(getString(R.string.empty_input));
            hasErrors = true;
        }

        int climateFlags = boolArrayToInt(getCheckboxesValues());

        if (hasErrors) {
            return;
        }

        SearchParameters searchParameters = new SearchParameters(day, month, year, price, personsSelection, mealSelection, climateFlags);

        Intent intent = new Intent();
        intent.putExtra(SearchListFragment.RETURNED_SEARCH_PARAMS, searchParameters);
        setResult(RESULT_OK, intent);
        finish();
    }

    private boolean[] getCheckboxesValues() {

        int size = checkBoxes.size();
        boolean[] selection = new boolean[size];

        for (int i = 0; i < size; i++) {
            selection[i] = checkBoxes.get(i).isChecked();
        }
        return selection;
    }

    private void showCalendarDialog() {
        CalendarDatePickerDialogFragment calendarPicker = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener(this)
                .setFirstDayOfWeek(Calendar.MONDAY);
        calendarPicker.show(getSupportFragmentManager(), DATE_PICKER_FRAGMENT_TAG);
    }

    private void setCheckBoxes() {

        String[] climateLabels = getResources().getStringArray(R.array.climate_tags);

        int childCount = checkBoxRoot.getChildCount();

        for (int i = 0; i < childCount; i++) {
            CheckBox checkBox = (CheckBox) checkBoxRoot.getChildAt(i);
            checkBox.setText(climateLabels[i]);
            checkBoxes.add(checkBox);
        }
    }

    private void setSpinner(int itemArray, Spinner spinner, AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, itemArray, R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

}
